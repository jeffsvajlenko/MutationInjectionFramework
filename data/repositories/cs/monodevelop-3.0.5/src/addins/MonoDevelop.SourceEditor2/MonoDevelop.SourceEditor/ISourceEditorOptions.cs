//
// ISourceEditorOptions.cs
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

using Pango;

using Mono.TextEditor;
using Mono.TextEditor.Highlighting;

using MonoDevelop.Core;
using MonoDevelop.Ide.Gui;
using MonoDevelop.Ide.Gui.Content;
using MonoDevelop.Projects;
using MonoDevelop.Ide.CodeCompletion;

namespace MonoDevelop.SourceEditor
{
public enum EditorFontType
{
    // Default Monospace font as set in the user's GNOME font properties
    DefaultMonospace,

    // Default Sans font as set in the user's GNOME font properties
    DefaultSans,

    // Custom font, will need to get the FontName property for more specifics
    UserSpecified
}

public interface ISourceEditorOptions : Mono.TextEditor.ITextEditorOptions
{

    bool EnableAutoCodeCompletion
    {
        get;
    }
    bool DefaultRegionsFolding
    {
        get;
    }
    bool DefaultCommentFolding
    {
        get;
    }
    bool EnableSemanticHighlighting
    {
        get;
    }
    //public bool AutoInsertTemplates {get; }
    bool TabIsReindent
    {
        get;
    }
    bool AutoInsertMatchingBracket
    {
        get;
    }
    bool EnableCodeCompletion
    {
        get;
    }
    bool UnderlineErrors
    {
        get;
    }
    EditorFontType EditorFontType
    {
        get;
    }
    bool UseViModes
    {
        get;
    }
}
}
