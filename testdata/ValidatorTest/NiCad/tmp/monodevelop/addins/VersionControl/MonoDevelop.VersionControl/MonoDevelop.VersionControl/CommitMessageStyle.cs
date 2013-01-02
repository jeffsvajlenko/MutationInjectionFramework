// CommitMessageStyle.cs
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
using MonoDevelop.Core.Serialization;

namespace MonoDevelop.VersionControl
{
[DataItem ("CommitMessageStyle")]
public class CommitMessageStyle : IEquatable<CommitMessageStyle>
{
    public CommitMessageStyle ()
    {
        Header = "";
        Indent = "";
        FirstFilePrefix = "* ";
        FileSeparator = ":\n* ";
        LastFilePostfix = ": ";
        LineAlign = 0;
        InterMessageLines = 1;
        IncludeDirectoryPaths = false;
        Wrap = true;
    }

    [ItemProperty]
    public string Header
    {
        get;
        set;
    }

    [ItemProperty]
    public string Indent
    {
        get;
        set;
    }

    [ItemProperty]
    public string FirstFilePrefix
    {
        get;
        set;
    }

    [ItemProperty]
    public string FileSeparator
    {
        get;
        set;
    }

    [ItemProperty]
    public string LastFilePostfix
    {
        get;
        set;
    }

    [ItemProperty]
    public int LineAlign
    {
        get;
        set;
    }

    [ItemProperty]
    public int InterMessageLines
    {
        get;
        set;
    }

    [ItemProperty]
    public bool IncludeDirectoryPaths
    {
        get;
        set;
    }

    [ItemProperty]
    public bool Wrap
    {
        get;
        set;
    }

    public void CopyFrom (CommitMessageStyle other)
    {
        Indent = other.Indent;
        FirstFilePrefix = other.FirstFilePrefix;
        FileSeparator = other.FileSeparator;
        LastFilePostfix = other.LastFilePostfix;
        LineAlign = other.LineAlign;
        InterMessageLines = other.InterMessageLines;
        Header = other.Header;
        IncludeDirectoryPaths = other.IncludeDirectoryPaths;
        Wrap = other.Wrap;
    }

    public bool Equals (CommitMessageStyle other)
    {
        return Indent == other.Indent &&
               FirstFilePrefix == other.FirstFilePrefix &&
               FileSeparator == other.FileSeparator &&
               LastFilePostfix == other.LastFilePostfix &&
               LineAlign == other.LineAlign &&
               InterMessageLines == other.InterMessageLines &&
               Header == other.Header &&
               IncludeDirectoryPaths == other.IncludeDirectoryPaths &&
               Wrap == other.Wrap;
    }
}
}
