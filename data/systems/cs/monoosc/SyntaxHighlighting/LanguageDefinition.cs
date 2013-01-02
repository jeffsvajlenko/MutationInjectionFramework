using System;
using System.Text;
using System.Text.RegularExpressions;
using System.Collections;
using System.Xml;
using System.IO;
using SyntaxHighlighting.Properties;

namespace SyntaxHighlighting
{
/// <summary>
/// Cette classe repr�sente un langage et toutes
/// ses r�gles
/// </summary>
public class LanguageDefinition
{
    private RuleCollection rules;

    /// <summary>
    ///
    /// </summary>
    public LanguageDefinition()
    {

        File.WriteAllText(FileDefinitionPath.DestPath + "HtmlTransform.xslt", Resources.HtmlTransform);
        File.WriteAllText(FileDefinitionPath.DestPath + "langageDefinition.xml", Resources.langageDefinition);
        File.WriteAllText(FileDefinitionPath.DestPath + "RtfTransform.xslt", Resources.RtfTransform);
        File.WriteAllText(FileDefinitionPath.DestPath + "StructureDefinitionLangage.xml", Resources.StructureDefinitionLangage);
        File.WriteAllText(FileDefinitionPath.DestPath + "StructureFichierXML.xml", Resources.StructureFichierXML);
        File.WriteAllText(FileDefinitionPath.DestPath + "Style.css", Resources.Style);

        rules = new RuleCollection();
    }

    /// <summary>
    /// Obtient l'ensemble des r�gles r�gissant
    /// ce langage
    /// </summary>
    public RuleCollection Rules
    {
        get
        {
            return rules;
        }
    }

    private bool isCaseSensitive = false;
    /// <summary>
    /// Obtient ou d�fini une valeur sp�cifiant si
    /// le langage respecte la casse
    /// </summary>
    public bool IsCaseSensitive
    {
        get
        {
            return isCaseSensitive;
        }
        set
        {
            isCaseSensitive = value;
        }
    }

    private string name = string.Empty;
    /// <summary>
    /// Obtient ou d�fini le nom du langage
    /// </summary>
    public string Name
    {
        get
        {
            return name;
        }
        set
        {
            name = value;
        }
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="filename"></param>
    /// <param name="langage"></param>
    public void LoadFromXML(string filename, string langage)
    {
        rules = new RuleCollection();

        XmlDocument doc = new XmlDocument();
        try
        {
            doc.Load(filename);
            XmlNode root = doc.ChildNodes[1];
            XmlNode lngNode;
            for (int i = 0; i < root.ChildNodes.Count; i++)
            {
                if (root.ChildNodes[i].Attributes["name"].Value == langage)
                {
                    lngNode = root.ChildNodes[i];
                    this.isCaseSensitive = bool.Parse(lngNode.Attributes["casesensitive"].Value);
                    XmlNode rulesNode = lngNode.FirstChild;
                    for (int j = 0; j < rulesNode.ChildNodes.Count; j++)
                    {
                        //rulesNode = rulesNode.ChildNodes[j];
                        /*Console.WriteLine("{0} : {1}", rulesNode.ChildNodes[j].Attributes["expression"].Value,
                            rulesNode.ChildNodes[j].Attributes["type"].Value);*/
                        rules.Add(
                            rulesNode.ChildNodes[j].Attributes["expression"].Value,
                            rulesNode.ChildNodes[j].Attributes["type"].Value,
                            this.isCaseSensitive);
                    }
                }
            }
            doc = null;
        }
        catch (Exception ex)
        {
            Console.WriteLine(ex.Message);
            return;
        }


    }

    #region Classe Rule
    /// <summary>
    /// Cette classe repr�sente une r�gle d'un langage
    /// </summary>
    public class Rule
    {
        /// <summary>
        ///
        /// </summary>
        /// <param name="expression"></param>
        /// <param name="type"></param>
        public Rule(Regex expression, string type)
        {
            this.expression = expression;
            this.type = type;
        }

        private Regex expression = null;
        /// <summary>
        /// Obtient ou d�fini l'expression r�guli�re d�finissant la r�gle
        /// </summary>
        public Regex Expression
        {
            get
            {
                return expression;
            }
            set
            {
                expression = value;
            }
        }

        private string type = null;
        /// <summary>
        /// Obtient ou d�fini le type de la r�gle (Comment, KeyWords...)
        /// </summary>
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
    }
    #endregion
    #region Classe RuleCollection
    /// <summary>
    /// Cette classe r�pr�sente une collection de r�gles pour
    /// un langage
    /// </summary>
    public class RuleCollection : System.Collections.CollectionBase
    {
        /// <summary>
        ///
        /// </summary>
        /// <param name="value"></param>
        /// <returns></returns>
        public int Add(Rule value)
        {
            return base.InnerList.Add(value);
        }
        /// <summary>
        ///
        /// </summary>
        /// <param name="expression"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public int Add(Regex expression, string type)
        {
            return base.InnerList.Add(new Rule(expression, type));
        }
        /// <summary>
        ///
        /// </summary>
        /// <param name="expression"></param>
        /// <param name="type"></param>
        /// <param name="isCaseSensitive"></param>
        /// <returns></returns>
        public int Add(string expression, string type, bool isCaseSensitive)
        {
            RegexOptions ro = RegexOptions.Multiline;
            if (isCaseSensitive)
            {
                //FIXME : in Waiting, mono fix this regression Bug
                if (Environment.OSVersion.Platform != PlatformID.Unix) ro |= RegexOptions.IgnoreCase;
            }
            return base.InnerList.Add(new Rule(new Regex(expression, ro), type));
        }
        /// <summary>
        ///
        /// </summary>
        /// <param name="expression"></param>
        /// <param name="type"></param>
        /// <returns></returns>
        public int Add(string expression, string type)
        {
            return this.Add(expression, type, true);
        }
        /// <summary>
        ///
        /// </summary>
        /// <param name="values"></param>
        public void AddRange(Rule[] values)
        {
            base.InnerList.AddRange(values);
        }
        /// <summary>
        ///
        /// </summary>
        /// <param name="index"></param>
        /// <returns></returns>
        public Rule this[int index]
        {
            get
            {
                return (Rule)base.InnerList[index];
            }
            set
            {
                base.InnerList[index] = value;
            }
        }
    }
    #endregion
}
}
