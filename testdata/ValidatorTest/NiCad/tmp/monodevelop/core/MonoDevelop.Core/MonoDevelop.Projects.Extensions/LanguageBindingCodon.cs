// LanguageBindingCodon.cs
//
// Author:
//   Lluis Sanchez Gual <lluis@novell.com>
//
// Copyright (c) 2007 Novell, Inc (http://www.novell.com)
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
using System.Collections;
using System.Diagnostics;
using System.ComponentModel;

using Mono.Addins;
using MonoDevelop.Projects;

namespace MonoDevelop.Projects.Extensions
{
[ExtensionNode (Description="A language binding. The specified class must implement MonoDevelop.Projects.ILanguageBinding")]
internal class LanguageBindingCodon : TypeExtensionNode
{
    [NodeAttribute("supportedextensions", "File extensions supported by this binding (to be shown in the Open File dialog)")]
    string[] supportedExtensions;

    public string[] Supportedextensions
    {
        get
        {
            return supportedExtensions;
        }
        set
        {
            supportedExtensions = value;
        }
    }

    public ILanguageBinding LanguageBinding
    {
        get
        {
            return (ILanguageBinding) GetInstance ();
        }
    }
}
}
