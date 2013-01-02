// PkgFiles.cs created with MonoDevelop
//
//User: eric at 23:59 08/08/2008
//
// Copyright (C) 2008 [Petit Eric, surfzoid@gmail.com]
//
//Permission is hereby granted, free of charge, to any person
//obtaining a copy of this software and associated documentation
//files (the "Software"), to deal in the Software without
//restriction, including without limitation the rights to use,
//copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the
//Software is furnished to do so, subject to the following
//conditions:
//
//The above copyright notice and this permission notice shall be
//included in all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//OTHER DEALINGS IN THE SOFTWARE.
//

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using System.IO;
using MonoOBSFramework.Functions.Sources;
using MonoOBSFramework;

namespace MonoOSC.Ctrl
{
public partial class PkgFiles : UserControl
{
    private string Package = string.Empty;
    private int FsByteSize = 0;
    public PkgFiles(string PkgName,string FsName,int FsSize,long FsTime)
    {
        InitializeComponent();
        Package = PkgName;
        LblFsName.Text = FsName;
        toolTip1.SetToolTip(LblFsName, FsName);
        FsByteSize = FsSize;
        string FinalSize = FsSize.ToString() + " Byte";
        if (FsSize >= 1024) FinalSize = (FsSize / 1024).ToString() + " Kb";
        if (FsSize >= (1024 * 1024)) FinalSize = (FsSize / 1024 / 1024).ToString() + " Mo";
        LblFsSize.Text = FinalSize;
        toolTip1.SetToolTip(LblFsSize, FinalSize);
        //LblFsTime.Text = Convert.ToDateTime(FsTime).ToString();
        try
        {
            DateTime Dt2 = Convert.ToDateTime("1970-01-01T01:00:00+01:00");
            LblFsTime.Text = Dt2.AddSeconds(FsTime).ToString();
        }
        catch (ArgumentOutOfRangeException)
        {
            // fileCreationTime is not valid, so re-throw the exception.
            throw;
        }

        toolTip1.SetToolTip(LblFsTime, LblFsTime.Text);
        toolTip1.SetToolTip(LblShowEdit, "Display or edit content of " + FsName);
        toolTip1.SetToolTip(LblDownload, "Download the file " + FsName);
        toolTip1.SetToolTip(LblRemove, "Remove the file " + FsName);
    }

    private void LblShowEdit_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        string[] StrSize = LblFsSize.Text.Split(" ".ToCharArray());
        if (StrSize.Length == 2)
        {
            switch (StrSize[1])
            {
            case "Byte":
            case "Kb":
                EditShowAFs();
                break;
            case "Mo":
                if (Convert.ToInt32(StrSize[0]) > 1)
                {
                    if (MessageBox.Show("Are you sure, download and open a file more than " + StrSize[0] + " Mo ?", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Warning) == DialogResult.Yes)
                        EditShowAFs();
                }
                break;
            default:
                break;
            }
        }

        Cursor = Cursors.Default;
    }

    private void EditShowAFs()
    {
        string TmpFs = VarGlobal.MonoOBSFrameworkTmpDir + LblFsName.Text;
        string RpmSpecMan = VarGlobale.RpmSpecMPath;
        if(RpmSpecMan.IndexOf("rpmspecmanager",StringComparison.InvariantCultureIgnoreCase) > -1 &
                LblFsName.Text.EndsWith(".spec") == true &
                File.Exists(VarGlobale.RpmSpecMPath) == true )
        {
            StringBuilder SpecCont = new StringBuilder(SourceProjectPackageFile.GetSourceProjectPackageFile(Package, LblFsName.Text).ToString());
            //if(!VarGlobal.LessVerbose)Console.WriteLine(SpecCont.ToString());
            File.WriteAllText(TmpFs,SpecCont.ToString());
            System.Diagnostics.Process PSpec = new System.Diagnostics.Process();
            PSpec.StartInfo.FileName = RpmSpecMan;
            PSpec.StartInfo.Arguments = (char)(34) + TmpFs + (char)(34);
            PSpec.Start();
            //PSpec.WaitForExit();
            while (PSpec.HasExited == false)
            {
                Application.DoEvents();
            }
        }
        else
        {
            Forms.EditShowFile Editor = new MonoOSC.Forms.EditShowFile();
            Editor.SetFsName = LblFsName.Text;
            Editor.SetText = "Downloading " + LblFsName.Text;
            Editor.SetText = SourceProjectPackageFile.GetSourceProjectPackageFile(Package, LblFsName.Text).ToString();
            Cursor = Cursors.Default;
            Editor.ShowDialog();
            string XmlTemplate = Editor.SetText;
            File.WriteAllText(TmpFs, XmlTemplate);
        }

        if(MessageBox.Show("Update file on the server ?","Confirmation",MessageBoxButtons.YesNo,MessageBoxIcon.Question) == DialogResult.Yes)
        {
            if(!string.IsNullOrEmpty(File.ReadAllText(TmpFs)))
            {
                try
                {
                    MessageBox.Show(PutSourceProjectPackageFile.PutFile(Package,TmpFs).ToString(),"Result Info",MessageBoxButtons.OK,MessageBoxIcon.Information);
                    File.Delete(TmpFs);
                }
                catch (Exception Ex)
                {
                    if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
                }
                finally
                {
                    File.Delete(TmpFs);
                }
            }
        }

    }

    /*private string FindRpmSpecManager ()
    {
    	string RpmSpecMan = "Not Found";
    	if (Environment.OSVersion.Platform == PlatformID.Unix) {
    		RpmSpecMan = UnixShell.StartProcess("which","rpmspecmanager","/",true);
    	}
    	else {
    		RpmSpecMan = "rpmspecmanager.exe";
    	}
    	return RpmSpecMan;
    }*/

    private void LblDownload_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
    {
        saveFileDialog1.FileName = LblFsName.Text;
        if(saveFileDialog1.ShowDialog() == DialogResult.OK)
            SourceProjectPackageFile.GetSourceProjectPackageFile(Package, LblFsName.Text, saveFileDialog1.FileName, 4096, FsByteSize);
    }

    private void LblRemove_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
    {
        if (MessageBox.Show("The file " + LblFsName.Text + " will be permanently delete from the server.", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Warning) == DialogResult.Yes)
        {
            Cursor = Cursors.WaitCursor;
            DeleteSourceProjectPackageFile.DelFile(Package, LblFsName.Text);
            this.Dispose(true);
            Cursor = Cursors.Default;
        }

    }

    private void showEditToolStripMenuItem_Click(object sender, EventArgs e)
    {
        LblShowEdit_LinkClicked(sender,null);
    }

    private void downloadToolStripMenuItem_Click(object sender, EventArgs e)
    {
        LblDownload_LinkClicked(sender, null);
    }

    private void removeToolStripMenuItem_Click(object sender, EventArgs e)
    {
        LblRemove_LinkClicked(sender, null);
    }
}
}
