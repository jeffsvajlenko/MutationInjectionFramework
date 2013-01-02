//
// IfSwitchStatement.cs
//
// Author:
//       Mike Krüger <mkrueger@novell.com>
//
// Copyright (c) 2010 Novell, Inc (http://www.novell.com)
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
using System.Xml;
using MonoDevelop.Core;
namespace MonoDevelop.DocFood
{
public class SwitchStatement : Node
{
    public const string XmlTag = "Switch";

    public List<Node> CaseSections
    {
        get;
        private set;
    }

    public SwitchStatement ()
    {
        CaseSections = new List<Node> ();
    }

    public override void Run (DocGenerator generator, object member)
    {
        if (!generator.EvaluateCondition (Attributes, member))
            return;
        foreach (var caseSection in CaseSections)
        {
            if (generator.EvaluateCondition (caseSection.Attributes, member))
            {
                caseSection.Children.ForEach (child => child.Run (generator, member));
                return;
            }
        }
        Children.ForEach (child => child.Run (generator, member));
    }

    public override  void Write (XmlWriter writer)
    {
        writer.WriteStartElement (XmlTag);
        foreach (var pair in Attributes)
        {
            writer.WriteAttributeString (pair.Key, pair.Value);
        }
        WriteNodeList (writer, CaseSections);
        if (Children.Count > 0)
        {
            writer.WriteStartElement ("Default");
            WriteNodeList (writer, Children);
            writer.WriteEndElement ();
        }
        writer.WriteEndElement ();
    }

    public static SwitchStatement Read (XmlReader reader)
    {
        SwitchStatement result = new SwitchStatement ();
        if (reader.MoveToFirstAttribute ())
        {
            do
            {
                result.SetAttribute (reader.LocalName, reader.Value);
            }
            while (reader.MoveToNextAttribute ());
        }
        XmlReadHelper.ReadList (reader, XmlTag, delegate ()
        {
            switch (reader.LocalName)
            {
            case "Case":
                result.CaseSections.Add (CaseStatement.Read (reader));
                return true;
            case "Default":
                result.Children = Node.ReadNodeList (reader, "Default");
                return true;
            }
            return false;
        });

        return result;
    }

    public class CaseStatement : Node
    {
        public const string XmlTag = "Case";

        public override void Run (DocGenerator generator, object member)
        {
            if (generator.EvaluateCondition (Attributes, member))
                Children.ForEach (child => child.Run (generator, member));
        }

        public override void Write (XmlWriter writer)
        {
            writer.WriteStartElement (XmlTag);
            foreach (var pair in Attributes)
            {
                writer.WriteAttributeString (pair.Key, pair.Value);
            }
            WriteNodeList (writer, Children);
            writer.WriteEndElement ();
        }

        public static CaseStatement Read (XmlReader reader)
        {
            CaseStatement result = new CaseStatement ();
            if (reader.MoveToFirstAttribute ())
            {
                do
                {
                    result.SetAttribute (reader.LocalName, reader.Value);
                }
                while (reader.MoveToNextAttribute ());
            }
            result.Children = Node.ReadNodeList (reader, XmlTag);
            return result;
        }
    }
}
}

