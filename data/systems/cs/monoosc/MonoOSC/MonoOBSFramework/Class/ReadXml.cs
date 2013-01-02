// ReadXml.cs created with MonoDevelop
//
//User: eric at 18:16 04/08/2008
//
// Copyright (C) 2008 [Petit Eric, surfzoid@gmail.com]
//
//Permission is hereby granted, free of charge, to any person
//obtaining a copy of this software and associated documentation
//files (the "Software"), to deal in the Software without
//restriction, including without limitation the rights to use,
//copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the
//Software is furnished to do so, subject to the following
//conditions:
//
//The above copyright notice and this permission notice shall be
//included in all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//OTHER DEALINGS IN THE SOFTWARE.
//

using System;
using System.Collections.Generic;
using System.Collections;
using System.Text;
using System.Xml;
using System.Windows.Forms;
using System.IO;

namespace MonoOBSFramework
{
#pragma warning disable 1584
/// <summary>
/// Various function to read and interpret XML data.
/// </summary>
public static class ReadXml
{
    /// <summary>
    /// Return the value of all atribute by the request "KeyNames" from the "RootNode"
    /// </summary>
    /// <param name="leXml">
    /// The XML content, for example the content of an XML file.
    /// </param>
    /// <param name="Rootnode">
    /// The root node where the search will start.
    /// </param>
    /// <param name="KeyName">
    /// The name of the key/node we want attribute values.
    /// </param>
    /// <returns>
    /// A <see cref="List&lt;T&gt;"/>A list of all getted value.
    /// </returns>
    public static List<string> GetValue(string leXml, string Rootnode, string KeyName)
    {
        List<string> Result = new List<string>();
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            if (doc.SelectNodes(Rootnode).Item(0).Attributes.Count > 0)
            {
                for (int j = 0; j < doc.SelectNodes(Rootnode).Item(0).Attributes.Count; j++)
                {
                    string AttrName = doc.SelectNodes(Rootnode).Item(0).Attributes[j].Name;
                    if (AttrName == KeyName)
                    {
                        Result.Add(doc.SelectNodes(Rootnode).Item(0).Attributes[j].InnerText);
                        return Result;
                    }
                }
            }

            foreach (XmlNode CurNode in doc.SelectNodes(Rootnode).Item(0).ChildNodes)
            {
                try
                {
                    if (string.IsNullOrEmpty(CurNode.Name) == false)
                    {
                        /*string TheFold = CurNode.Attributes.Item(0).InnerText;
                        string TheType = CurNode.Name;*/
                        if (CurNode.Name == KeyName)
                        {
                            if (CurNode.Attributes.Count > 0)
                            {
                                for (int i = 0; i < CurNode.Attributes.Count; i++)
                                {
                                    //string CurText = CurNode.Attributes.Item(i).Name;
                                    //if(!VarGlobal.LessVerbose)Console.WriteLine(CurNode.Attributes.Item(i).InnerText);
                                    Result.Add(CurNode.Attributes.Item(i).InnerText);
                                }
                            }
                            else
                            {
                                Result.Add(CurNode.InnerText);
                            }
                            //if (Result.Count > 0) return Result;
                        }
                    }
                }

                catch (Exception ex)
                {
                    if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
                }
            }
        }
        catch (Exception)
        {
            List<string> Err = new List<string>();
            Err.Add(leXml);
            return Err;
        }
        return Result;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <param name="Rootnode"></param>
    /// <param name="KeyName"></param>
    /// <param name="KeyValue"></param>
    /// <param name="ParentInFirst"></param>
    /// <returns>
    /// A <see cref="Dictionary&lt;T, T&gt;"/>A list of all getted value.
    /// </returns>
    public static Dictionary<string, string> GetValue(string leXml, string Rootnode, string KeyName, string KeyValue, bool ParentInFirst)
    {
        Dictionary<string, string> Result = new Dictionary<string, string>();
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);
            if (ParentInFirst == true) RootNde = RootNde.ParentNode;
            foreach (XmlNode CurNode in RootNde)
            {
                try
                {
                    if (string.IsNullOrEmpty(CurNode.Name) == false)
                    {
                        if (CurNode.Attributes.Count > 0)
                        {
                            string AttrVal = CurNode.Attributes[0].InnerText;
                            string AttrName = CurNode.Attributes[0].Name;

                            if (AttrName == KeyName & AttrVal == KeyValue)
                            {
                                foreach (XmlNode CurChildNode in CurNode.ChildNodes)
                                {
                                    if (CurChildNode.Attributes.Count > 0)
                                    {
                                        for (int i = 0; i < CurChildNode.Attributes.Count; i++)
                                        {
                                            Result.Add(CheckKeyExist(Result, CurChildNode.Attributes.Item(i).Name), CurChildNode.Attributes.Item(i).InnerText);
                                        }
                                    }
                                    else
                                    {
                                        Result.Add(CheckKeyExist(Result, CurChildNode.Name), CurChildNode.InnerText);
                                    }
                                }
                                if (Result.Count > 0) return Result;
                            }
                        }
                    }
                }

                catch (Exception ex)
                {
                    if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
                }
            }
        }
        catch (Exception)
        {
            Dictionary<string, string> Err = new Dictionary<string, string>();
            Err.Add("Error", leXml);
            return Err;
        }
        return Result;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <param name="NodeOfAttr">In fact the Xpath : Ex : /Names/Name</param>
    /// <param name="AttrName"></param>
    /// <returns></returns>
    public static string ReadAttrValue(string leXml, string NodeOfAttr, string AttrName)
    {
        string Result = string.Empty;
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNodeList xnList = doc.SelectNodes(NodeOfAttr);
            foreach (XmlNode xn in xnList)
            {
                foreach (XmlAttribute item in xn.Attributes)
                {
                    if (item.Name == AttrName) Result = item.Value;
                }
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }

        return Result;
    }

    private static string CheckKeyExist(Dictionary<string, string> Result, string Key)
    {
        string Unique = Key;
        try
        {
            int Idx = 0;
            while (Result.ContainsKey(Unique) == true)
            {
                Idx += 1;
                Unique = Key + Idx;
            }
        }
        catch (Exception ex)
        {

            if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message);
            return ex.Message;
        }
        return Unique;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <param name="Rootnode"></param>
    /// <param name="FirstAttrName"></param>
    /// <param name="FirstAttrValue"></param>
    /// <returns>
    /// A <see cref="Dictionary&lt;T, T&gt;"/>A list of all getted value.
    /// </returns>
    public static Dictionary<string, string> GetAllAttrValue(string leXml, string Rootnode, string FirstAttrName, string FirstAttrValue)
    {
        Dictionary<string, string> Result = new Dictionary<string, string>();
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    if (CurNode.Attributes.Count > 0)
                    {
                        string AttrName = CurNode.Attributes[0].Name;
                        string AttrVal = CurNode.Attributes[0].InnerText;

                        if (AttrName == FirstAttrName & AttrVal == FirstAttrValue)
                        {
                            for (int i = 0; i < CurNode.Attributes.Count; i++)
                            {
                                Result.Add(CheckKeyExist(Result, CurNode.Attributes[i].Name), CurNode.Attributes[i].InnerText);
                            }
                            return Result;
                        }
                    }
                }

            }
        }
        catch (Exception)
        {
            Dictionary<string, string> Err = new Dictionary<string, string>();
            Err.Add("Error", leXml);
            return Err;
        }
        return Result;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <returns>
    /// A <see cref="Dictionary&lt;T, T&gt;"/>A list of all getted value.
    /// </returns>
    public static Dictionary<string, string> GetAllAttrValue(string leXml)
    {
        Dictionary<string, string> Result = new Dictionary<string, string>();
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNode RootNde = doc.ChildNodes.Item(0);
            //if (string.IsNullOrEmpty(RootNde.Name) == false && )
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    Result.Add(CheckKeyExist(Result, CurNode.Name), CurNode.InnerText);
                    /*if (CurNode.Attributes.Count > 0)
                    {
                        string AttrName = CurNode.Attributes[0].Name;
                        string AttrVal = CurNode.Attributes[0].InnerText;

                            for (int i = 0; i < CurNode.Attributes.Count; i++)
                            {
                                Result.Add(CheckKeyExist(Result, CurNode.Attributes[i].Name), CurNode.Attributes[i].InnerText);
                            }


                    }*/
                }

            }
        }
        catch (Exception)
        {
            Dictionary<string, string> Err = new Dictionary<string, string>();
            Err.Add("Error", leXml);
            return Err;
        }
        return Result;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <returns>
    /// A <see cref="Dictionary&lt;T, T&gt;"/>A list of all getted value.
    /// </returns>
    public static TreeNode XmlToTreeNode(string leXml)
    {
        TreeNode Result = null;
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml.Replace(
                            @"<?xml version=""1.0"" encoding=""UTF-8""?>",
                            string.Empty));

            XmlNode RootNde = doc.ChildNodes.Item(0);
            if (string.IsNullOrEmpty(RootNde.Name) == false) Result = new TreeNode(RootNde.Name);
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    TreeNode Nde = new TreeNode(CurNode.Name);
                    Nde.Tag = CurNode.InnerText;
                    if (CurNode.Attributes.Count > 0)
                    {
                        //string AttrName = CurNode.Attributes[0].Name;
                        //string AttrVal = CurNode.Attributes[0].InnerText;
                        TreeNode ChildNde = null;
                        for (int i = 0; i < CurNode.Attributes.Count; i++)
                        {
                            if (CurNode.Attributes[i].InnerText != "#text")
                            {
                                ChildNde = new TreeNode(CurNode.Attributes[i].Name);
                                ChildNde.Tag = CurNode.Attributes[i].InnerText;
                                Nde.Nodes.Add(ChildNde);
                            }
                        }
                    }

                    foreach (XmlNode item in CurNode.ChildNodes)
                    {
                        if (item.Name != "#text")
                        {
                            TreeNode Nde2 = new TreeNode(item.Name);
                            if (!string.IsNullOrEmpty(item.InnerText)) Nde2.Tag = item.InnerText;
                            if (item.Attributes != null && item.Attributes.Count > 0)
                            {
                                /*string AttrName2 = item.Attributes[0].Name;
                                string AttrVal2 = item.Attributes[0].InnerText;*/
                                TreeNode ChildNde2 = null;
                                for (int i = 0; i < item.Attributes.Count; i++)
                                {
                                    if (item.Attributes[i].InnerText != "#text")
                                    {
                                        ChildNde2 = new TreeNode(item.Attributes[i].Name);
                                        ChildNde2.Tag = item.Attributes[i].InnerText;
                                        Nde2.Nodes.Add(ChildNde2);
                                    }
                                }
                            }
                            Nde.Nodes.Add(Nde2);
                        }
                        Result.Nodes.Add(Nde);
                    }
                }

            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0} {1} {2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
            TreeNode Err = new TreeNode("Error");
            return Err;
        }
        return Result;
    }
    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <param name="Rootnode"></param>
    /// <param name="AttrName"></param>
    /// <returns>
    /// A <see cref="List&lt;T&gt;"/>A list of all getted value.
    /// </returns>
    public static List<string> GetAllFirstAttrValue(string leXml, string Rootnode, string AttrName)
    {
        List<string> Result = new List<string>();
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    if (CurNode.Attributes.Count > 0)
                    {
                        string XAttrName = CurNode.Attributes[0].Name;

                        if (XAttrName == AttrName)
                        {
                            Result.Add(CurNode.Attributes[0].InnerText);

                        }
                    }
                }

            }
            return Result;
        }
        catch (Exception)
        {
            List<string> Err = new List<string>();
            Err.Add(leXml);
            return Err;
        }
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <param name="Rootnode"></param>
    /// <param name="BuildOrPublish"></param>
    /// <param name="Repository"></param>
    /// <param name="Arch"></param>
    /// <returns>
    /// A <see cref="CheckState"/>A list of all getted value.
    /// </returns>
    public static CheckState GetChkState(string leXml, string Rootnode, string BuildOrPublish, string Repository, string Arch)
    {
        CheckState Result = CheckState.Checked;
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(RootNde.Name) == false)
                {
                    if (CurNode.Name == BuildOrPublish)
                    {
                        foreach (XmlNode FindedNode in CurNode.ChildNodes)
                        {
                            bool RepoHere = false;
                            for (int i = 0; i < FindedNode.Attributes.Count; i++)
                            {
                                if (FindedNode.Attributes[i].InnerText == Repository)
                                {
                                    RepoHere = true;
                                    /*if (FindedNode.Name == "enable") Result = CheckState.Indeterminate;
                                    if (FindedNode.Name == "disable") Result = CheckState.Unchecked;*/
                                }
                                if (RepoHere == true)
                                {
                                    if (FindedNode.Attributes[i].InnerText == Arch)
                                    {
                                        Result = CheckState.Checked;
                                        if (FindedNode.Name == "enable") Result = CheckState.Indeterminate;
                                        if (FindedNode.Name == "disable") Result = CheckState.Unchecked;
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }

            }
            return Result;
        }
        catch (Exception)
        {
            return CheckState.Checked;
        }
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="AttrName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <returns>
    /// A <see cref="System.String"/>
    /// </returns>
    public static List<string> GetAllAttrValueByName(string leXml, string AttrName)
    {
        List<string> Result = new List<string>();
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNode RootNde = doc.ChildNodes.Item(0);
            bool HasChildYet = true;
            while (HasChildYet)
            {
                foreach (XmlNode CurNode in RootNde.ChildNodes)
                {
                    if (string.IsNullOrEmpty(CurNode.Name) == false)
                    {
                        if (AttrName == CurNode.Name) Result.Add(CurNode.InnerText);
                        if (CurNode.Attributes.Count > 0)
                        {
                            for (int i = 0; i < CurNode.Attributes.Count; i++)
                            {
                                if (AttrName == CurNode.Attributes[i].Name) Result.Add(CurNode.Attributes[i].InnerText);
                            }


                        }
                    }
                    if (CurNode.HasChildNodes)
                    {
                        RootNde = CurNode;
                    }
                    else
                    {
                        HasChildYet = false;
                    }
                }
            }
        }
        catch (Exception)
        {
            List<string> Err = new List<string>();
            Err.Add(leXml);
            return Err;
        }
        return Result;
    }

    /*private XmlNode[] GetAllChild(XmlNode Nde)
    {

    }*/

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <param name="RootName"></param>
    /// <param name="AttrVal"></param>
    /// <returns></returns>
    public static StringBuilder GetAllNodeOfAnAttrVal(string leXml, string RootName, string AttrVal)
    {
        StringBuilder Result = new StringBuilder("<collection>");
        try
        {
            XmlDocument doc = new XmlDocument();
            //doc.PreserveWhitespace = true;
            doc.LoadXml(leXml);
            foreach (XmlNode RootNdes in doc.FirstChild.ChildNodes)
            {
                foreach (XmlNode SecNde in RootNdes.ChildNodes)
                {
                    foreach (XmlAttribute Attr in SecNde.Attributes)
                    {
                        if (Attr.Value == AttrVal)
                        {
                            if (RootName == SecNde.Name) Result.Append(RootNdes.OuterXml);
                        }
                    }
                }
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
            return new StringBuilder(string.Format("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace));
        }
        return new StringBuilder(GetIndentedXML(Result.Append("</collection>").ToString()));
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="strXml"></param>
    /// <returns></returns>
    public static String GetIndentedXML(string strXml)
    {
        string strRet = null;
        if (strXml != null && strXml.Length > 0)
        {
            XmlDocument xdoc = new XmlDocument();
            xdoc.LoadXml(strXml);
            MemoryStream ms = new MemoryStream();
            XmlTextWriter xtw = new XmlTextWriter(ms, Encoding.UTF8);
            xtw.Formatting = Formatting.Indented;
            xdoc.Save(xtw);
            ms.Position = 0;
            StreamReader rd1 = new StreamReader(ms, Encoding.UTF8);
            strRet = rd1.ReadToEnd();
            ms.Close();
            xtw.Close();
            rd1.Close();
        }
        return strRet;
    }

    /// <summary>
    ///
    /// </summary>
    /// <param name="leXml"></param>
    /// <returns></returns>
    public static List<ListViewItem> GetAllValue(string leXml)
    {
        List<ListViewItem> Result = new List<ListViewItem>();
        try
        {
            XmlDocument doc = new XmlDocument();
            doc.LoadXml(leXml);
            XmlNodeList Lst = doc.SelectNodes("/" + doc.LastChild.Name);
            foreach (XmlNode RootNdes in Lst[0].ChildNodes)
            {
                Result.Add(AddAnItem("-----------", "-----------"));
                Result.Add(AddAnItem(RootNdes.Name, RootNdes.Value));
                foreach (XmlAttribute Attr1 in RootNdes.Attributes)
                {
                    Result.Add(AddAnItem(Attr1.Name, Attr1.Value));
                }
                foreach (XmlNode SecNde in RootNdes.ChildNodes)
                {
                    Result.Add(AddAnItem(SecNde.Name, SecNde.Value));
                    foreach (XmlAttribute Attr in SecNde.Attributes)
                    {
                        Result.Add(AddAnItem(Attr.Name, Attr.Value));
                    }
                }
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
            return Result;
        }
        return Result;
    }

    private static ListViewItem AddAnItem(string It, string SubIt)
    {
        /*if (!string.IsNullOrEmpty(SubIt))
        {*/
        ListViewItem Result = new ListViewItem(It);
        Result.SubItems.Add(SubIt);
        return Result;
        /*}
        else return new ListViewItem();*/
    }
}//class
}//Namespace
