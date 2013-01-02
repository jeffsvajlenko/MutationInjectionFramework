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
using System.Text;
using System.Xml;
using System.Windows.Forms;
using System.IO;

namespace MonoOBSFramework
{
/// <summary>
/// Various function to write XML data.
/// </summary>
public static class WriteXml
{
    /// <summary>
    /// Check if a node exist before write data and give the possibility to create it
    /// </summary>
    /// <param name="XmlFs"></param>
    /// <param name="Rootnode"></param>
    /// <param name="NodeNameToFind"></param>
    /// <param name="CreateIfFalse"></param>
    /// <returns>
    /// A <see cref="System.Boolean"/>A list of all getted value.
    /// </returns>
    public static bool NodeExist(string XmlFs, string Rootnode, string NodeNameToFind, bool CreateIfFalse)
    {
        bool Result = false;
        XmlDocument doc = new XmlDocument();
        try
        {
            doc.Load(XmlFs);

        }
        catch (Exception)
        {
            return false;
        }

        try
        {
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    if (CurNode.Name == NodeNameToFind)
                    {
                        Result = true;
                        break;
                    }
                }

            }
            if (Result == false & CreateIfFalse == true)
            {
                try
                {
                    doc.DocumentElement.AppendChild(doc.CreateElement(NodeNameToFind));
                    //doc.Save(Console.Out);
                    doc.Save(XmlFs);
                }
                catch (Exception)
                {
                    //XWritter.Close();
                }
            }
            return Result;
        }
        catch (Exception)
        {
            return false;
        }
    }
    /// <summary>
    /// Append a child node to the given parent node.
    /// </summary>
    /// <param name="XmlFs"></param>
    /// <param name="OverWriteNode"></param>
    /// <param name="Rootnode"></param>
    /// <param name="ParentNode"></param>
    /// <param name="ChildNodeName"></param>
    /// <param name="AttributeKeyVal"></param>
    /// <returns>
    /// A <see cref="System.Boolean"/>A list of all getted value.
    /// </returns>
    public static bool AppendChild(string XmlFs, bool OverWriteNode, string Rootnode, string ParentNode, string ChildNodeName, Dictionary<string, string> AttributeKeyVal)
    {
        bool Result = false;
        XmlDocument doc = new XmlDocument();
        try
        {
            doc.Load(XmlFs);

        }
        catch (Exception)
        {
            return false;
        }
        try
        {
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);

            //Create a new node.
            XmlElement NodeToAdd = doc.CreateElement(ChildNodeName);
            foreach (KeyValuePair<string, string> item in AttributeKeyVal)
            {
                NodeToAdd.SetAttribute(item.Key, item.Value);
            }
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    if (CurNode.Name == ParentNode)
                    {
                        if (OverWriteNode == true)
                        {
                            foreach (XmlNode ChildNode in CurNode.ChildNodes)
                            {
                                /*if (ChildNode.Name == ChildNodeName)
                                {*/
                                if (ChildNode.Attributes.Count == 2 & NodeToAdd.Attributes.Count == 2)
                                {
                                    if (ChildNode.Attributes[0].Value == NodeToAdd.Attributes[0].Value & ChildNode.Attributes[1].Value == NodeToAdd.Attributes[1].Value)
                                    {
                                        CurNode.RemoveChild(ChildNode);
                                        break;
                                    }
                                }
                                //}
                            }

                            CurNode.AppendChild(NodeToAdd);
                            doc.Save(XmlFs);
                            Result = true;
                            break;
                        }
                    }
                }

            }
            return Result;
        }
        catch (Exception)
        {
            return false;
        }
    }

    /// <summary>
    /// Remove an existing node.
    /// </summary>
    /// <param name="XmlFs"></param>
    /// <param name="Rootnode"></param>
    /// <param name="ParentNode"></param>
    /// <param name="AttributeKeyVal"></param>
    /// <returns>
    /// A <see cref="System.Boolean"/>A list of all getted value.
    /// </returns>
    public static bool RemoveNode(string XmlFs, string Rootnode, string ParentNode, Dictionary<string, string> AttributeKeyVal)
    {
        bool Result = false;
        XmlDocument doc = new XmlDocument();
        try
        {
            doc.Load(XmlFs);

        }
        catch (Exception)
        {
            return false;
        }
        try
        {
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);

            //Create a new node.
            XmlElement NodeToAdd = doc.CreateElement("CheckedNode");
            int AttrCount = 0;
            foreach (KeyValuePair<string, string> item in AttributeKeyVal)
            {
                NodeToAdd.SetAttribute(item.Key, item.Value);
                AttrCount += 1;
            }
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    if (CurNode.Name == ParentNode)
                    {
                        foreach (XmlNode ChildNode in CurNode.ChildNodes)
                        {
                            if (ChildNode.Attributes.Count == 2 & NodeToAdd.Attributes.Count == 2)
                            {
                                if (ChildNode.Attributes[0].Value == NodeToAdd.Attributes[0].Value & ChildNode.Attributes[1].Value == NodeToAdd.Attributes[1].Value)
                                {
                                    CurNode.RemoveChild(ChildNode);

                                    doc.Save(XmlFs);
                                    Result = true;
                                    break;
                                }
                            }
                        }
                    }
                }

            }
            return Result;
        }
        catch (Exception)
        {
            return false;
        }
    }


    /// <summary>
    /// Remove an existing node.
    /// </summary>
    /// <param name="XmlFs"></param>
    /// <param name="Rootnode"></param>
    /// <param name="ParentNode"></param>
    /// <param name="FirstAttVal"></param>
    /// <returns>
    /// A <see cref="System.Boolean"/>A list of all getted value.
    /// </returns>
    public static bool RemoveNode(string XmlFs, string Rootnode, string ParentNode,
                                  string FirstAttName, string FirstAttVal)
    {
        XmlDocument doc = new XmlDocument();
        try
        {
            doc.Load(XmlFs);

        }
        catch (Exception)
        {
            return false;
        }
        try
        {
            XmlNode RootNde = doc.SelectNodes(Rootnode).Item(0);
            foreach (XmlNode CurNode in RootNde.ChildNodes)
            {
                if (string.IsNullOrEmpty(CurNode.Name) == false)
                {
                    if (CurNode.Name == ParentNode)
                    {
                        foreach (XmlAttribute Attr in CurNode.Attributes)
                        {
                            if (Attr.Name == FirstAttName & Attr.Value == FirstAttVal)
                            {
                                CurNode.ParentNode.RemoveChild(CurNode);
                                doc.Save(XmlFs);
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
        catch (Exception)
        {
            return false;
        }
    }

}//Class WriteXml
}//amespace
