//
// MenuToolButton.cs
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

namespace MonoDevelop.Components.Commands
{
public class MenuToolButton: Gtk.ToolButton
{
    Gtk.Menu menu;

    public MenuToolButton (Gtk.Menu menu, string icon): base (icon)
    {
        this.menu = menu;
        Child.ButtonPressEvent += new Gtk.ButtonPressEventHandler (OnButtonPress);

        if (string.IsNullOrEmpty (icon))
        {
            this.Expand = false;
            this.Homogeneous = false;
            this.IconWidget = new Gtk.Arrow (Gtk.ArrowType.Down, Gtk.ShadowType.None);
        }
    }

    [GLib.ConnectBeforeAttribute]
    void OnButtonPress (object sender, Gtk.ButtonPressEventArgs e)
    {
        menu.Popup (null, null, new Gtk.MenuPositionFunc (OnPosition), 3, Gtk.Global.CurrentEventTime);
        e.RetVal = true;
    }

    void OnPosition (Gtk.Menu menu, out int x, out int y, out bool pushIn)
    {
        this.ParentWindow.GetOrigin (out x, out y);
        x += this.Allocation.X;
        y += this.Allocation.Y + this.Allocation.Height;
        pushIn = true;
    }
}
}
