// ExportProjectDialog.cs
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
using System.IO;
using MonoDevelop.Components;
using MonoDevelop.Projects;

namespace MonoDevelop.Ide.Projects
{
public partial class ExportSolutionDialog : Gtk.Dialog
{
    FileFormat[] formats;

    public ExportSolutionDialog (WorkspaceItem item, FileFormat selectedFormat)
    {
        this.Build();

        labelNewFormat.Text = item.FileFormat.Name;

        formats = Services.ProjectService.FileFormats.GetFileFormatsForObject (item);
        foreach (FileFormat format in formats)
            comboFormat.AppendText (format.Name);

        int sel = Array.IndexOf (formats, selectedFormat);
        if (sel == -1) sel = 0;
        comboFormat.Active = sel;

        if (formats.Length < 2)
        {
            table.Remove (newFormatLabel);
            newFormatLabel.Destroy ();
            newFormatLabel = null;
            table.Remove (comboFormat);
            comboFormat.Destroy ();
            comboFormat = null;
        }

        //auto height
        folderEntry.WidthRequest = 380;
        Resize (1, 1);

        folderEntry.Path = item.ItemDirectory;
        UpdateControls ();
    }

    public FileFormat Format
    {
        get
        {
            if (comboFormat == null)
                return formats[0];
            return formats [comboFormat.Active];
        }
    }

    public string TargetFolder
    {
        get
        {
            return folderEntry.Path;
        }
    }

    void UpdateControls ()
    {
        buttonOk.Sensitive = folderEntry.Path.Length > 0;
    }

    protected virtual void OnFolderEntryPathChanged(object sender, System.EventArgs e)
    {
        UpdateControls ();
    }
}
}
