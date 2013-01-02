using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Collections;
using System.Web;
using System.IO;

using System.Xml;
using System.Xml.XPath;
using System.Xml.Xsl;


namespace SyntaxHighlighting
{
/// <summary>
/// Enumération des types de sortie
/// </summary>
#pragma warning disable 1591
public enum OutputType
{
    Rtf,
    Xml,
    Html
}

/// <summary>
/// Description résumée de Highlightor.
/// </summary>
public class Highlightor
{

    private LanguageDefinition langageDefinition;
    private StringBuilder codeColoriser;
    private string code;
    private int tabSize = 8;
    private CCMatchCollection matches;


    /// <summary>
    /// Equivalent en nombres d'espaces d'une tabulation
    /// </summary>
    /// <remarks>
    /// Par défaut, la valeur est fixée é 8
    /// </remarks>
    public int TabSize
    {
        get
        {
            return tabSize;
        }
        set
        {
            tabSize = value;
        }
    }

    /// <summary>
    /// Chaéne XML correspondant au parsing du code
    /// </summary>
    public string CodeColoriser
    {
        get
        {
            return codeColoriser.ToString();
        }
    }

    /// <summary>
    /// Construit une nouvelle instance de cette classe
    /// é partir du nom du langage de l'emplacement du fichier XML
    /// </summary>
    /// <param name="langage">Nom du langage</param>
    /// <param name="definitionFile">Emplacement du fichier XML contenant les définitions des langages</param>
    public Highlightor(string langage, string definitionFile)
    {
        langageDefinition = new LanguageDefinition();
        langageDefinition.LoadFromXML(definitionFile, langage);
        codeColoriser = new StringBuilder();
    }

    public string Source
    {
        get
        {
            return code;
        }
        set
        {
            code = value;
        }
    }

    public string HighLight()
    {
        return this.HighLight(this.Source);
    }

    /// <summary>
    /// Colorise la source et la formate suivant le
    /// type spécifié
    /// </summary>
    /// <param name="source">Source é coloriser</param>
    /// <returns>Code coloriser</returns>
    public string HighLight(string source)
    {
        StringBuilder sb = new StringBuilder();
        int curPos = 0;
        this.code = source;
        this.code = this.code.Replace("\r\n", "\n");
        // Construit la liste des résultats
        this.GetMatches();
        // Nettoie la collection
        this.CleanMatches();

        // parcours la liste des résultat pour construire
        // la chaine XML
        for (int i = 0; i < this.matches.Count; i++)
        {
            // Encode les caractéres spéciaux
            sb.Append(HttpUtility.HtmlEncode(this.code.Substring(curPos, this.matches[i].Index - curPos)));
            // si le résultat est sur plusieurs lignes,
            // il faut le rédecouper
            if (this.matches[i].Match.Value.IndexOf('\n') != -1)
            {
                string localString = this.matches[i].Match.Value;
                int localPos = 0, cur = 0;
                cur = localString.IndexOf('\n');
                do
                {
                    sb.Append("<" + this.matches[i].Type + ">");
                    sb.Append(HttpUtility.HtmlEncode(localString.Substring(localPos, cur - localPos)));
                    sb.Append("</" + this.matches[i].Type + ">\n");
                    localPos = cur + 1;
                    cur = localString.IndexOf('\n', localPos);
                }
                while (cur != -1);
                sb.Append("<" + this.matches[i].Type + ">");
                sb.Append(HttpUtility.HtmlEncode(localString.Substring(localPos)));
                sb.Append("</" + this.matches[i].Type + ">\n");

            }
            else
            {
                // sinon on l'insert directement
                sb.Append("<" + this.matches[i].Type + ">");
                sb.Append(HttpUtility.HtmlEncode(this.matches[i].Match.Value));
                sb.Append("</" + this.matches[i].Type + ">");
            }
            curPos = this.matches[i].Length + this.matches[i].Index;
        }
        // Ajoute la fin du code
        sb.Append(HttpUtility.HtmlEncode(this.code.Substring(curPos)));

        string line = string.Empty;
        string[] lines;
        int indent = 0;
        // construit le document xml
        codeColoriser = new StringBuilder();
        codeColoriser.Append("<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n");
        codeColoriser.Append("<Source xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n");
        lines = sb.ToString().Split('\n');
        // pour chaque ligne du xml
        // on calcul l'indentation
        // et on l'ajoute
        for (int i = 0; i < lines.Length; i++)
        {
            line = IndentLine(lines[i], out indent);
            codeColoriser.Append(String.Format("\t<Line indent=\"{0}\">{1}</Line>\n",
                                               indent, line));
        }
        codeColoriser.Append("\n</Source>");

        string ret = codeColoriser.ToString();
        // on nettoie les abérations du type <Keyword>public</Keyword> <Keyword>string</Keyword>
        Regex regex = new Regex(@"\<\/(?<tag>\w+)\>[ ]*\<\<tag>\>");
        ret = regex.Replace(ret, " ");
        // on retire les tags vide comme : <Commentaire></Commentaire>
        regex = new Regex(@"\<(?<tag>\w+)\>\<\/\<tag>\>");
        ret = regex.Replace(ret, string.Empty);

        // on reconstruit le stringbuilder a partir de ret
        codeColoriser = new StringBuilder(ret);
        return ret;
    }


    /// <summary>
    /// Transforme la chaine XML fournie en une chaéne HTML
    /// en utilisant une transformation XLST
    /// </summary>
    /// <param name="xmlString">Chaéne XML d'entrée</param>
    /// <returns>Chaéne HTML de sortie</returns>
    private string HtmlTransform(string xmlString)
    {
        // transformation du xml en html
        UTF8Encoding enc = new UTF8Encoding();
        StringBuilder sb = new StringBuilder();

        // transfert dans un fichier temporaire le contenu de xmlString
        StreamWriter sw = new StreamWriter(
            Path.GetTempPath() + FileDefinitionPath.PathDirSeparator + "~temp.xml", false);
        sw.Write(xmlString);
        sw.Close();
        sw = null;

        // Crée un flux en mémoire
        // pour enregistrer le html
        MemoryStream mswriter = new MemoryStream();
        XmlTextWriter xmlwriter = new XmlTextWriter(mswriter, System.Text.Encoding.UTF8);

        // charge le XML dans un document XPath
        XPathDocument xpathdocument = new XPathDocument(
            Path.GetTempPath() + FileDefinitionPath.PathDirSeparator + "~temp.xml");
#pragma warning disable 0618
        XslTransform xslt = new XslTransform();
        // Charge le fichier XSLT
        xslt.Load(FileDefinitionPath.DestPath + "HtmlTransform.xslt");
        // Transforme le XML dans le flux mémoire
        xslt.Transform(xpathdocument, null, xmlwriter, null);

        // écrit le flux mémoire dans un stringbuilder
        // pour la sortie de la méthode
        sb.Append(enc.GetString(mswriter.GetBuffer()));

        // ferme le flux
        mswriter.Close();
        mswriter = null;
        enc = null;

        try
        {
            // efface le fichier temporaire
            File.Delete(Path.GetTempPath() + FileDefinitionPath.PathDirSeparator + "~temp.xml");
        }
        catch (Exception ex)
        {
            Console.WriteLine("Pas de fichier à supprimer\n" + ex.Message);
        }

        // Ajout de la feuille de styles
        TextReader tr = new StreamReader(File.OpenRead(FileDefinitionPath.DestPath + "Style.css"));
        sb.Replace("${style}", tr.ReadToEnd());
        sb.Replace("<li></li>", "<li>&nbsp;</li>");

        tr.Close();
        tr = null;

        sb.Replace("$generator", System.Reflection.Assembly.GetExecutingAssembly().FullName);
        string dtd = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                     "<!DOCTYPE html \n\tPUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                     "\t\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n";

        // retourne la chaine html
        // sans les 2 1er caractéres qui correspondent é l'encodage UTF8
        return dtd + sb.ToString().Substring(2);
    }

    /// <summary>
    /// Transforme le flux XML en chaéne RTF
    /// </summary>
    /// <param name="xmlString">Chaéne XML</param>
    /// <returns>Chaéne RTF</returns>
    /// <remarks>
    /// Non implémenté pour le moment
    /// </remarks>
    private string RtfTransform(string xmlString)
    {
        // transformation du xml en html
        UnicodeEncoding enc = new UnicodeEncoding();
        StringBuilder sb = new StringBuilder();

        // Modification de la chaine XML
        // pour la rendre conforme au RTF
        xmlString = xmlString.Replace("\\", "\\\\").Replace("{", "\\{").Replace("}", "\\}").Replace(((char)0).ToString(),string.Empty);

        // transfert dans un fichier temporaire le contenu de xmlString
        StreamWriter sw = new StreamWriter(
            Path.GetTempPath() + FileDefinitionPath.PathDirSeparator + "~temp.xml", false);
        sw.Write(xmlString);
        sw.Close();
        sw = null;

        // Crée un flux en mémoire
        // pour enregistrer le html
        MemoryStream mswriter = new MemoryStream();
        XmlTextWriter xmlwriter = new XmlTextWriter(mswriter, System.Text.Encoding.Unicode);

        // charge le XML dans un document XPath
        XPathDocument xpathdocument = new XPathDocument(
            Path.GetTempPath() + FileDefinitionPath.PathDirSeparator + "~temp.xml");
        XslTransform xslt = new XslTransform();
        // Charge le fichier XSLT
        xslt.Load(FileDefinitionPath.DestPath + "RtfTransform.xslt");
        // Transforme le XML dans le flux mémoire
        xslt.Transform(xpathdocument, null, xmlwriter, null);

        // écrit le flux mémoire dans un stringbuilder
        // pour la sortie de la méthode
        sb.Append(enc.GetString(mswriter.GetBuffer()));

        // ferme le flux
        mswriter.Close();
        mswriter = null;
        enc = null;

        try
        {
            // efface le fichier temporaire
            File.Delete(Path.GetTempPath() + FileDefinitionPath.PathDirSeparator + "~temp.xml");
        }
        catch (Exception ex)
        {
            Console.WriteLine("Pas de fichier à supprimer\n" + ex.Message);
        }

        int cpt;
        string szSb = sb.ToString();
        //			string espace;
        Regex regex = new Regex(":(\\d+):");
        MatchCollection m = regex.Matches(szSb);
        for (int i = m.Count - 1; i >= 0; i--)
        {
            cpt = int.Parse(m[i].Groups[1].Value);
            if (cpt > 0)
            {
                szSb = szSb.Substring(0, m[i].Index) +
                       (new string(' ', cpt)) +
                       szSb.Substring(m[i].Index + m[i].Length);

            }
            else
            {
                szSb = szSb.Substring(0, m[i].Index) +
                       szSb.Substring(m[i].Index + m[i].Length);
            }

        }

        string Result = HttpUtility.HtmlDecode(szSb.ToString().Substring(3)
                                               .Trim('\t')
                                               .Replace("\r\n", string.Empty)
                                               .Replace("\t", "\\tab "));
        return Result;
    }


    public string Export(string source, OutputType type)
    {
        switch (type)
        {
        default:
        case OutputType.Xml:
            return source;
        case OutputType.Html:
            return HtmlTransform(source);
        case OutputType.Rtf:
            return RtfTransform(source);
        }
    }

    public string Export(OutputType type)
    {
        return Export(this.HighLight(this.code), type);
    }

    /// <summary>
    /// Construit la collection de résultat des expressions réguliéres
    /// </summary>
    private void GetMatches()
    {
        matches = new CCMatchCollection();
        for (int i = 0; i < langageDefinition.Rules.Count; i++)
        {
            matches.AddRange(
                langageDefinition.Rules[i].Expression.Matches(this.code),
                langageDefinition.Rules[i].Type);
        }
        // Trie les resultats dans l'ordre croissant
        matches.Sort();
    }

    /// <summary>
    /// Nettoie la collection de résultat en supprimant
    /// les résultats se trouvant é l'intérieur des autres
    /// </summary>
    private void CleanMatches()
    {
        ArrayList deleteItem = new ArrayList();
        CCMatch cc1, cc2;

        for (int i = 0; i < this.matches.Count; i++)
        {
            if (deleteItem.Contains(i))
            {
                continue;
            }
            cc1 = this.matches[i];
            for (int j = i + 1; j < this.matches.Count; j++)
            {
                cc2 = this.matches[j];
                if (cc1.Contains(cc2))
                {
                    deleteItem.Add(j);
                }
            }
        }

        for (int i = deleteItem.Count - 1; i >= 0; i--)
        {
            this.matches.RemoveAt((int)deleteItem[i]);
        }
    }

    /// <summary>
    /// Compte le numbre d'espace nécessaire é
    /// l'indentation de la ligne
    /// </summary>
    /// <param name="line">Ligne é indenter</param>
    /// <param name="indent">Nombre d'espaces nécessaire</param>
    /// <returns>Ligne commenéant sans espace</returns>
    private string IndentLine(string line, out int indent)
    {
        indent = 0;
        int i = 0;
        while (i < line.Length && (line[i] == '\t' || line[i] == ' '))
        {
            switch (line[i])
            {
            case '\t':
                indent += this.tabSize;
                break;
            case ' ':
                indent++;
                break;
            }
            i++;
        }
        return line.Trim('\t', ' ');
    }


    #region Classe CCMatch
    /// <summary>
    /// Réprésente un résultat d'expression réguliére,
    /// plus quelques éléments servant é leur gestion
    /// dans le parseur
    /// </summary>
    private class CCMatch : IComparable
    {
        private Match match;
        private string type;

        public CCMatch(Match match, string type)
        {
            this.match = match;
            this.type = type;
        }

        public Match Match
        {
            get
            {
                return match;
            }
            set
            {
                match = value;
            }
        }

        public int Index
        {
            get
            {
                return match.Index;
            }
        }

        public string Type
        {
            get
            {
                return type;
            }
            set
            {
                type = value;
            }
        }

        public int Length
        {
            get
            {
                return match.Length;
            }
        }
        #region Membres de IComparable

        public int CompareTo(object obj)
        {
            if (!(obj is CCMatch))
            {
                throw new ArgumentException("L'argument n'est pas de type CCMatch", "obj");
            }

            CCMatch y = obj as CCMatch;
            if (this.Index < y.Index)
            {
                return -1;
            }

            if (this.Index > y.Index)
            {
                return 1;
            }

            if (this.Index == y.Index)
            {
                if (this.Length < y.Length)
                {
                    return -1;
                }

                if (this.Length > y.Length)
                {
                    return 1;
                }
            }

            return 0;
        }

        #endregion

        public bool Contains(CCMatch match)
        {
            return (this.Index <= match.Index &&
                    this.Index + this.Length > match.Index);
        }
    }
    #endregion
    #region Classe CCMatchCollection
    /// <summary>
    /// Collection de résultat
    /// </summary>
    private class CCMatchCollection : System.Collections.CollectionBase
    {
        public CCMatch this[int index]
        {
            get
            {
                return (CCMatch)base.InnerList[index];
            }
        }

        public int Add(CCMatch value)
        {
            return base.InnerList.Add(value);
        }

        public void AddRange(CCMatch[] values)
        {
            base.InnerList.AddRange(values);
        }
        public void AddRange(MatchCollection matches, string type)
        {
            for (int i = 0; i < matches.Count; i++)
            {
                this.Add(new CCMatch(matches[i], type));
            }
        }

        public void Sort()
        {
            base.InnerList.Sort(null);
        }
    }
    #endregion
}
}
