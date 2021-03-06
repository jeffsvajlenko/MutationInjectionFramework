//
// NSMenuItem.cs: Support for the NSMenuItem class
//
// Author:
//   Michael Hutchinson (mhutchinson@novell.com)
//   Miguel de Icaza (miguel@gnome.org)
//
// Copyright 2010, Novell, Inc.
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
using MonoMac.ObjCRuntime;
using MonoMac.Foundation;

namespace MonoMac.AppKit
{

public partial class NSMenuItem
{
    public NSMenuItem (string title, EventHandler handler) : this (title, "", handler)
    {
    }

    public NSMenuItem (string title, string charCode, EventHandler handler) : this (title, null, charCode)
    {
        Activated += handler;
    }

    public NSMenuItem (string title, string charCode) : this (title, null, charCode)
    {
    }

    public NSMenuItem (string title) : this (title, null, "")
    {
    }

    public event EventHandler Activated
    {
        add
        {
            Target = ActionDispatcher.SetupAction (Target, value);
            Action = ActionDispatcher.Action;
        }

        remove
        {
            ActionDispatcher.RemoveAction (Target, value);
        }
    }

}
}
