//
// InferredXmlCompletionProvider.cs
//
// Author:
//       Michael Hutchinson <mhutchinson@novell.com>
//
// Copyright (c) 2009 Novell, Inc. (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

using System;
using System.Collections.Generic;
using MonoDevelop.Ide.CodeCompletion;
using MonoDevelop.Xml.StateEngine;

namespace MonoDevelop.XmlEditor.Completion
{


public class InferredXmlCompletionProvider : IXmlCompletionProvider
{
    Dictionary<string,HashSet<string>> elementCompletions = new Dictionary<string,HashSet<string>> ();
    Dictionary<string,HashSet<string>> attributeCompletions = new Dictionary<string,HashSet<string>> ();

    public DateTime TimeStampUtc
    {
        get;
        set;
    }
    public int ErrorCount
    {
        get;
        set;
    }

    public InferredXmlCompletionProvider ()
    {
    }

    //TODO: respect namespaces
    public void Populate (XDocument doc)
    {
        foreach (XNode node in doc.AllDescendentNodes)
        {
            XElement el = node as XElement;
            if (el == null)
                continue;
            string parentName = "";
            XElement parentEl = el.Parent as XElement;
            if (parentEl != null)
                parentName = parentEl.Name.Name;

            HashSet<string> map;
            if (!elementCompletions.TryGetValue (parentName, out map))
            {
                map = new HashSet<string> ();
                elementCompletions.Add (parentName, map);
            }
            map.Add (el.Name.Name);

            if (!attributeCompletions.TryGetValue (el.Name.Name, out map))
            {
                map = new HashSet<string> ();
                attributeCompletions.Add (el.Name.Name, map);
            }
            foreach (XAttribute att in el.Attributes)
                map.Add (att.Name.Name);
        }
    }

    public CompletionDataList GetElementCompletionData ()
    {
        return GetChildElementCompletionData ("");
    }

    public CompletionDataList GetElementCompletionData (string namespacePrefix)
    {
        return new CompletionDataList ();
    }

    public CompletionDataList GetChildElementCompletionData (XmlElementPath path)
    {
        return GetCompletions (elementCompletions, path, XmlCompletionData.DataType.XmlElement);
    }

    public CompletionDataList GetAttributeCompletionData (XmlElementPath path)
    {
        return GetCompletions (attributeCompletions, path, XmlCompletionData.DataType.XmlAttribute);
    }

    public CompletionDataList GetAttributeValueCompletionData (XmlElementPath path, string name)
    {
        return new CompletionDataList ();
    }

    public CompletionDataList GetChildElementCompletionData (string tagName)
    {
        return GetCompletions (elementCompletions, tagName, XmlCompletionData.DataType.XmlElement);
    }

    public CompletionDataList GetAttributeCompletionData (string tagName)
    {
        return GetCompletions (attributeCompletions, tagName, XmlCompletionData.DataType.XmlAttribute);
    }

    public CompletionDataList GetAttributeValueCompletionData (string tagName, string name)
    {
        return new CompletionDataList ();
    }

    static CompletionDataList GetCompletions (Dictionary<string,HashSet<string>> map, string tagName, XmlCompletionData.DataType type)
    {
        var data = new CompletionDataList ();
        HashSet<string> values;
        if (map.TryGetValue (tagName, out values))
            foreach (string s in values)
                data.Add (new XmlCompletionData (s, type));
        return data;
    }

    static CompletionDataList GetCompletions (Dictionary<string,HashSet<string>> map, XmlElementPath path, XmlCompletionData.DataType type)
    {
        if (path == null || path.Elements.Count == 0)
            return new CompletionDataList ();
        return GetCompletions (map, path.Elements[path.Elements.Count - 1].Name, type);
    }
}
}
