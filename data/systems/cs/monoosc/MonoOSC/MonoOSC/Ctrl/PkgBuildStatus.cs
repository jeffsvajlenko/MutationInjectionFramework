// PkgBuildStatus.cs created with MonoDevelop
//
//User: eric at 00:01 09/08/2008
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
using System.Collections;
using MonoOBSFramework.Functions.BuildResults;
using MonoOBSFramework;

namespace MonoOSC.Ctrl
{
public partial class PkgBuildStatus : UserControl
{
    string Repo = string.Empty;
    List<string> Archis;
    string Pkg = string.Empty;
    int MemHight = 35;
    public PkgBuildStatus(string Repository, List<string> Archs, string Package)
    {
        InitializeComponent();
        Repo = Repository;
        Archis = Archs;
        Pkg = Package;
        RefreshBuild();
    }

    Dictionary<string, PkgBuildStatusArch> CtrColl = new Dictionary<string, PkgBuildStatusArch>();
    private void RefreshBuild()
    {
        Cursor = Cursors.WaitCursor;
        try
        {
            CtrColl.Clear();
            panel1.Controls.Clear();
            LblRepository.Text = Repo;
            this.Height = 36;
            foreach (string Arch in Archis)
            {
                if (Application.OpenForms.Count <= 0) break;
                PkgBuildStatusArch TheCtrl = new PkgBuildStatusArch(Repo, Arch, Pkg);
                TheCtrl.Dock = DockStyle.Fill;
                this.Height += TheCtrl.Height;
                TheCtrl.Dock = DockStyle.Top;
                panel1.Controls.Add(TheCtrl);
                CtrColl.Add(TheCtrl.Handle.ToString(), TheCtrl);
                while (TheCtrl.OperationStatus == false)
                {
                    if (Application.OpenForms.Count <= 0) break;
                    Application.DoEvents();
                }
            }
            splitContainer1.SplitterDistance = 31;

        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
        Cursor = Cursors.Default;
    }

    private void BtnRefresh_Click(object sender, EventArgs e)
    {
        RefreshBuild();
    }

    private void BtnRebuild_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        string Result = (RebuildAllArch.PostRebuildAllArch(Repo, Pkg)).ToString();
        MessageBox.Show(Result, "Result infos", MessageBoxButtons.OK, MessageBoxIcon.Information);
        if (Result == "<status code=\"ok\" />\n") RefreshBuild();
        Cursor = Cursors.Default;
    }

    private bool _FinishedToAdd = false;
    public bool FinishedToAdd
    {
        get
        {
            return _FinishedToAdd;
        }
        set
        {
            _FinishedToAdd = value;
            foreach (PkgBuildStatusArch Ctrl in CtrColl.Values)
            {
                Ctrl.FinishedToAdd = true;
            }
        }
    }

    public void CleanAll()
    {
        foreach (PkgBuildStatusArch Ctrl in CtrColl.Values)
        {
            Ctrl.Dispose();
        }
    }

    private void BtnRebuild_MouseEnter(object sender, EventArgs e)
    {
        Cursor = Cursors.Hand;
    }

    private void BtnRebuild_MouseLeave(object sender, EventArgs e)
    {
        Cursor = Cursors.Default;
    }

    private void BtnHide_Click(object sender, EventArgs e)
    {
        splitContainer1.Panel2Collapsed = !splitContainer1.Panel2Collapsed;
        if (splitContainer1.Panel2Collapsed)
        {
            BtnHide.BackgroundImage = imageList1.Images[1];
            MemHight = this.Height;
            this.Height = 35;
        }
        else
        {
            BtnHide.BackgroundImage = imageList1.Images[0];
            this.Height = MemHight;
            splitContainer1.SplitterDistance = 35;
        }
    }

}//Class
}//NameSpace
