// MimeTypeNode.cs
//
// Author:
//   Lluis Sanchez Gual <lluis@novell.com>
//
// Copyright (c) 2008 Novell, Inc (http://www.novell.com)
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
//
//

using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Text.RegularExpressions;
using Mono.Addins;
using MonoDevelop.Core;
using System.Linq;


namespace MonoDevelop.Ide.Extensions
{
[ExtensionNodeChild (typeof(MimeTypeFileNode), "File")]
class MimeTypeNode: ExtensionNode
{
    [NodeAttribute]
    string icon;

    [NodeAttribute ("_description", Localizable=true)]
    string description;

    [NodeAttribute (Required=false)]
    string baseType;

    [NodeAttribute (Required=false)]
    protected bool isText;

    IFileNameEvalutor regex;

    public IconId Icon
    {
        get
        {
            return icon;
        }
        set
        {
            icon = value;
        }
    }

    public string Description
    {
        get
        {
            return description;
        }
        set
        {
            description = value;
        }
    }

    public string BaseType
    {
        get
        {
            if (string.IsNullOrEmpty (baseType))
                return isText ? "text/plain" : null;
            else
                return baseType;
        }
    }

    interface IFileNameEvalutor
    {
        bool SupportsFile (string fileName);
    }

    class RegexFileNameEvalutor : IFileNameEvalutor
    {
        Regex regex;

        public RegexFileNameEvalutor (MimeTypeNode node)
        {
            regex = CreateRegex (node);
        }

        Regex CreateRegex (MimeTypeNode node)
        {
            var globalPattern = new StringBuilder ();

            foreach (MimeTypeFileNode file in node.ChildNodes)
            {
                string pattern = Regex.Escape (file.Pattern);
                pattern = pattern.Replace ("\\*",".*");
                pattern = pattern.Replace ("\\?",".");
                pattern = pattern.Replace ("\\|","$|^");
                pattern = "^" + pattern + "$";
                if (globalPattern.Length > 0)
                    globalPattern.Append ('|');
                globalPattern.Append (pattern);
            }
            return new Regex (globalPattern.ToString ());
        }
        public bool SupportsFile (string fileName)
        {
            return regex.IsMatch (fileName);
        }
    }

    class EndsWithFileNameEvalutor : IFileNameEvalutor
    {
        string[] endings;

        public EndsWithFileNameEvalutor (MimeTypeNode node)
        {
            endings = ExtractEndings (node);
        }

        string[] ExtractEndings (MimeTypeNode node)
        {
            var result = new List<string> ();
            foreach (MimeTypeFileNode file in node.ChildNodes)
            {
                foreach (string pattern in file.Pattern.Split ('|'))
                {
                    result.Add (pattern.StartsWith ("*.") ? pattern.Substring (1) : pattern);
                }
            }
            return result.ToArray ();
        }

        public bool SupportsFile (string fileName)
        {
            foreach (var ending in endings)
                if (fileName.EndsWith (ending, StringComparison.Ordinal))
                    return true;
            return false;
        }

        internal static bool IsCompatible (MimeTypeNode node)
        {
            foreach (MimeTypeFileNode file in node.ChildNodes)
            {
                foreach (string pattern in file.Pattern.Split ('|'))
                {
                    var pat = pattern.StartsWith ("*.") ? pattern.Substring (1) : pattern;
                    if (pat.Any (p => p == '*' || p == '?'))
                        return false;
                }
            }
            return true;
        }
    }

    IFileNameEvalutor CreateFileNameEvalutor ()
    {
        if (EndsWithFileNameEvalutor.IsCompatible (this))
            return new EndsWithFileNameEvalutor (this);
        return new RegexFileNameEvalutor (this);
    }

    public bool SupportsFile (string fileName)
    {
        if (regex == null)
            regex = CreateFileNameEvalutor ();
        return regex.SupportsFile (fileName);
    }

    protected override void OnChildNodeAdded (ExtensionNode node)
    {
        base.OnChildNodeAdded (node);
        regex = null;
    }

    protected override void OnChildNodeRemoved (ExtensionNode node)
    {
        base.OnChildNodeRemoved (node);
        regex = null;
    }
}

class MimeTypeFileNode: ExtensionNode
{
    [NodeAttribute]
    string pattern;

    public string Pattern
    {
        get
        {
            return pattern;
        }
        set
        {
            pattern = value;
        }
    }
}
}
