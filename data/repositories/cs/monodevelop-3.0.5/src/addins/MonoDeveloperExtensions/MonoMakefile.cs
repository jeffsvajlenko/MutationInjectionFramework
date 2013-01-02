//
// MonoMakefile.cs
//
// Author:
//   Lluis Sanchez Gual
//
// Copyright (C) 2005 Novell, Inc (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

using System;
using System.Collections;
using System.IO;
using MonoDevelop.Core;
using System.Text.RegularExpressions;
using MonoDevelop.Projects;

namespace MonoDeveloper
{
class MonoMakefile
{
    string content;
    string multilineMatch = @"(((?<content>.*)(?<!\\)\n)|((?<content>.*?)\\\n(\t(?<content>.*?)\\\n)*\t(?<content>.*?)(?<!\\)\n))";
    string fileName;

    public MonoMakefile (string file)
    {
        this.fileName = file;
        StreamReader sr = new StreamReader (file);
        content = sr.ReadToEnd ();
        sr.Close ();
    }

    public string FileName
    {
        get
        {
            return fileName;
        }
    }

    public string Content
    {
        get
        {
            return content;
        }
    }

    public string GetVariable (string var)
    {
        Regex varExp = new Regex(@"[.|\n]*^" + var + @"(?<sep>\s*:?=\s*)" + multilineMatch, RegexOptions.Multiline);
        return GetValue (var, varExp);
    }

    public string GetTarget (string var)
    {
        Regex targetExp = new Regex(@"[.|\n]*^" + var + @"(?<sep>\s*:\s*)" + multilineMatch + @"\t" + multilineMatch, RegexOptions.Multiline);
        return GetValue (var, targetExp);
    }

    string GetValue (string var, Regex exp)
    {
        Match match = exp.Match (content);
        if (!match.Success) return null;
        string value = "";
        foreach (Capture c in match.Groups["content"].Captures)
            value += c.Value;
        return value;
    }
}
}
