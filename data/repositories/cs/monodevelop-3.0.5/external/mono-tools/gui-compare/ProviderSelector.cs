// ProviderSelector.cs
//
// Copyright (c) 2007 Novell, Inc.
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

namespace guicompare
{


public partial class ProviderSelector : Gtk.Bin
{

    public ProviderSelector()
    {
        this.Build();
    }

    protected virtual void OnRadiobutton1Toggled (object sender, System.EventArgs e)
    {
        filechooserbutton2.Sensitive = false;
        filechooserbutton1.Sensitive = true;
    }

    protected virtual void OnRadiobutton2Toggled (object sender, System.EventArgs e)
    {
        filechooserbutton1.Sensitive = false;
        filechooserbutton2.Sensitive = true;
    }

    protected virtual void OnFilechooserbutton2Focused (object o, Gtk.FocusedArgs args)
    {
        Console.WriteLine ("Got focus");
    }

    protected virtual void OnFilechooserbutton2FocusChildSet (object o, Gtk.FocusChildSetArgs args)
    {
        radiobutton2.Active = true;
    }

    // true if this is an XML description, false if its an assembly
    public bool IsInfo
    {
        get
        {
            return radiobutton1.Active;
        }
    }

    public string File
    {
        get
        {
            if (IsInfo)
                return filechooserbutton1.Filename;
            else
                return filechooserbutton2.Filename;
        }
    }
}
}
