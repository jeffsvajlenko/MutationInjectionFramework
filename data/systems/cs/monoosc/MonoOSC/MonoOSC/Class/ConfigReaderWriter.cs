// ConfigReaderWriter.cs
//
//  Copyright (C) 2007 [Petit Eric  surfzoid@gmail.com]
//
// This program is free software; you can redistribute it and/or modify
//obtaining a copy of this software and associated documentation
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//
//

/*
 * Classe permettant la lecture et ecriture des fichiers de configuration (App.config)
 * Auteur: LEBRUN Thomas < lebrun_thomas@hotmail.com >
 *
 * Vous êtes libre d'utiliser, modifier, distribuer cette classe
 * tant que le nom de l'auteur y apparait clairement.
 *
 * */
using System;
using System.IO;
using System.Xml;
using System.Windows.Forms;
using System.Reflection;
using System.Diagnostics;

namespace MonoOSC
{



/// <summary>
/// Classe permettant la lecture et ecriture des fichiers de configuration (App.config)
/// </summary>
public static class ConfigReaderWriter
{
    /// <summary>
    ///
    /// </summary>
    public static string CONFIG_FILE = Application.UserAppDataPath.Replace(Application.ProductVersion, null) + Assembly.GetEntryAssembly().GetName().Name + ".exe.config";
    //public string CONFIG_FILE = Application.StartupPath + @"\" + Assembly.GetEntryAssembly().GetName().Name + ".exe.config";

    private static XmlTextReader LecteurXml = null;
    private static FileStream StreamFichier = null;

    /// <summary>
    /// Méthode utilisée pour charger le fichier de configuration dans un XmlTextReader
    /// </summary>
    /// <returns>Un XmlTextReader si tout va bien. Null dans le cas contraire.</returns>
    private static XmlTextReader ReadConfig()
    {
        try
        {
            if (File.Exists(CONFIG_FILE) == false) NoFsexist();
            StreamFichier = new FileStream(CONFIG_FILE, FileMode.Open, FileAccess.ReadWrite, FileShare.ReadWrite);

            LecteurXml = new XmlTextReader(StreamFichier);
        }
        catch (IOException ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
            MessageBox.Show("Erreur inattendue." + Environment.NewLine + ex.Message, "Erreur", MessageBoxButtons.OK, MessageBoxIcon.Error, MessageBoxDefaultButton.Button1);
        }

        return LecteurXml;
    }

    /// <summary>
    /// Méthode utilisée pour permettre la libération (fermeture) des ressources.
    /// </summary>
    public static void Close()
    {
        if (LecteurXml != null)
        {
            LecteurXml.Close();
        }

        if (StreamFichier != null)
        {
            StreamFichier.Close();
        }
    }

    /// <summary>
    /// Récupère, dans le fichier XML, la valeur de noeud demandé.
    /// </summary>
    /// <param name="ElementName">Noeud dont on veut la valeur</param>
    /// <returns>Une chaîne de caractère représentant la valeur de noeud demndé.</returns>
    public static string GetXmlValue(string ElementName,string DefaultValue)
    {
        string Value = null;
        XmlTextReader XmlReader = null;

        try
        {
            // Lecture du fichier de configuation
            XmlReader = ReadConfig();

            // Tant que l'on peut lire
            while (XmlReader.Read())
            {
                // On récupère le type de noeud actuel
                XmlNodeType NodeType = XmlReader.NodeType;

                // Si c'est un élément
                if (NodeType == XmlNodeType.Element)
                {
                    // et qu'il vaut "add" et que la valeur de "key" est égale à celle passée en paramètre
                    if (XmlReader.Name.Equals("add") && XmlReader.GetAttribute("key").Equals(ElementName))
                    {
                        // On récupère la valeur de cet élément
                        Value = XmlReader.GetAttribute("value");
                        // On ferme alors le XmlTextReader
                        XmlReader.Close();

                        // On retourne la valeur que l'on vient de trouver
                        return Value;
                    }
                }
            }
        }
        catch (XmlException ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
            MessageBox.Show("Erreur inattendue." + Environment.NewLine + ex.Message, "Erreur", MessageBoxButtons.OK, MessageBoxIcon.Error, MessageBoxDefaultButton.Button1);
        }
        finally
        {
            // Fermeture de XmlReader qui a chargé le fichier de configuration
            if (XmlReader != null)
            {
                XmlReader.Close();
            }
        }
        if(string.IsNullOrEmpty(Value) == true)Value = DefaultValue;
        return Value;
    }

    /// <summary>
    /// Insère, dans le fichier XML, la valeur de noeud demandé.
    /// </summary>
    /// <param name="ElementName">Noeud dont on veut modifier la valeur</param>
    /// <param name="sValue">Nouvelle valeur que l'on veut affecter au noeud</param>
    public static void SetXmlValue(string ElementName, string sValue)
    {
        XmlTextReader XmlReader = null;
        XmlDocument XmlWritter = null;

        try
        {
            // Lecture du fichier de configuation
            XmlReader = ReadConfig();

            // Tant que l'on peut lire
            Boolean Exist = false;
            while (XmlReader.Read())
            {
                // Si le nom vaut "add" et que la valeur de "key" est égale à celle passée en paramètre
                if (XmlReader.Name.Equals("add") && XmlReader.GetAttribute("key").Equals(ElementName))
                {
                    Exist = true;
                    // On libère les ressources (fichier de config)
                    Close();

                    // On charge le fichier de config dans un XmlDocument
                    XmlWritter = new XmlDocument();
                    XmlWritter.Load(CONFIG_FILE);

                    XmlElement ElementRacine = XmlWritter.DocumentElement;

                    XmlAttribute AttributXml = null;
                    XmlNode sectionNode = null;

                    XmlNode groupNode = ElementRacine;

                    // On recherche les Nodes du nom de "add"
                    sectionNode = groupNode.SelectSingleNode("appSettings");

                    // Si on ne trouve pas ce nom, on la rajoute
                    if (sectionNode == null)
                    {
                        sectionNode = groupNode.AppendChild(XmlWritter.CreateElement("appSettings"));
                    }

                    // On recherche ensuite la clé ayant dont le nom est passé en parmètre
                    XmlNode NodeCherche = sectionNode.SelectSingleNode("add[@key=\"" + ElementName + "\"]");

                    // Si cette clé n'existe pas, on la rajoute
                    if (NodeCherche == null)
                    {
                        XmlElement element = XmlWritter.CreateElement("add");
                        AttributXml = XmlWritter.CreateAttribute("key");
                        AttributXml.Value = ElementName;
                        element.Attributes.Append(AttributXml);

                        NodeCherche = sectionNode.AppendChild(element);
                    }

                    // On créé l'attribut value et on lui assigne la valeur passée en paramètre
                    AttributXml = XmlWritter.CreateAttribute("value");
                    AttributXml.Value = sValue.ToString();
                    NodeCherche.Attributes.Append(AttributXml);

                    // Enregistrement du fichier
                    XmlWritter.Save(CONFIG_FILE);
                }
            }
            if (Exist == false) AddInexist(ElementName, sValue);
        }
        catch (XmlException ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
            MessageBox.Show("Erreur XML inattendue." + Environment.NewLine + ex.Message, "Erreur", MessageBoxButtons.OK, MessageBoxIcon.Error, MessageBoxDefaultButton.Button1);
        }
        catch (Exception ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
            MessageBox.Show("Erreur inattendue." + Environment.NewLine + ex.Message, "Erreur", MessageBoxButtons.OK, MessageBoxIcon.Error, MessageBoxDefaultButton.Button1);
        }
        finally
        {
            // Fermeture de XmlReader qui a chargé le fichier de configuration (dans le cas où il ne serait pas déjà fermé)
            if (XmlReader != null)
            {
                XmlReader.Close();
            }
        }
    }

    private static void AddInexist(string ElementName, string sValue)
    {
        try
        {
            NoFsexist();
            XmlDocument XmlWritter = null;
            Close();

            // On charge le fichier de config dans un XmlDocument
            XmlWritter = new XmlDocument();
            XmlWritter.Load(CONFIG_FILE);

            XmlElement ElementRacine = XmlWritter.DocumentElement;

            XmlAttribute AttributXml = null;
            XmlNode sectionNode = null;

            XmlNode groupNode = ElementRacine;

            // On recherche les Nodes du nom de "add"
            sectionNode = groupNode.SelectSingleNode("appSettings");

            // Si on ne trouve pas ce nom, on la rajoute
            if (sectionNode == null)
            {
                sectionNode = groupNode.AppendChild(XmlWritter.CreateElement("appSettings"));
            }

            // On recherche ensuite la clé ayant dont le nom est passé en parmètre
            //XmlNode NodeCherche = sectionNode; //.SelectSingleNode("add[@key=\"" + ElementName + "\"]");
            //if(!VarGlobal.LessVerbose)Console.WriteLine(NodeCherche.ToString());

            XmlElement element = XmlWritter.CreateElement("add");
            AttributXml = XmlWritter.CreateAttribute("key");
            AttributXml.Value = ElementName;
            element.Attributes.Append(AttributXml);
            AttributXml = XmlWritter.CreateAttribute("value");
            AttributXml.Value = sValue;
            element.Attributes.Append(AttributXml);

            //NodeCherche = sectionNode.AppendChild(element);
            sectionNode.AppendChild(element);

            // Enregistrement du fichier
            XmlWritter.Save(CONFIG_FILE);
        }
        catch (Exception ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
            NoFsexist();
            //throw;
        }
    }


    private static void NoFsexist()
    {
        try
        {
            System.IO.FileInfo FsInf = new System.IO.FileInfo(CONFIG_FILE);
            if (FsInf.Exists == false)
            {
                FsInf.Directory.Create();
                System.IO.File.WriteAllText(CONFIG_FILE, Properties.Resources.DefConf, System.Text.Encoding.UTF8);
            }
        }
        catch (Exception ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
            //throw;
        }
    }

}

}
