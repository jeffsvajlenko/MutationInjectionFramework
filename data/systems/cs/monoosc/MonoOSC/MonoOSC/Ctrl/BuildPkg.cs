// BuildPkg.cs created with MonoDevelop
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
using MonoOBSFramework;
using MonoOBSFramework.Functions.Sources;
using System.Collections;
using MonoOBSFramework.Functions.BuildResults;
using System.IO;
using System.Diagnostics;

namespace MonoOSC.Ctrl
{
public partial class BuildPkg : UserControl
{
    private string Package = string.Empty;
    private string MetaPkgXmlFs = string.Empty;
    ArrayList AllRepo;
    public BuildPkg(string PkgName, ArrayList AllRepository)
    {
        InitializeComponent();
        Package = PkgName;
        AllRepo = AllRepository;
        MetaPkgXmlFs = VarGlobal.MonoOBSFrameworkTmpDir + "MonoOSC" + Package + "Meta.xml";
        backgroundWorkerFsList.RunWorkerAsync();
    }

    private void backgroundWorkerFsList_DoWork(object sender, DoWorkEventArgs e)
    {
        backgroundWorkerFsListIsBusy = true;
        if (CancelRequest == true) return;
        e.Result = GetSourceProjectPackage.GetFileList(Package);
    }

    private void backgroundWorkerFsList_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {
        if (CancelRequest == true) return;

    }

    private void backgroundWorkerFsList_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        if (CancelRequest == true &&
                backgroundWorkerAddFsIsBusy == false &&
                backgroundWorkerBuildStatusIsBusy == false &&
                backgroundWorkerPkgFlagsIsBusy == false)
        {
            CancelRequest = false;
            return;
        }

        if (CancelRequest == true) return;

        SetListFsPkg((StringBuilder)e.Result);
        backgroundWorkerFsListIsBusy = false;
    }

    delegate void SetListFsPkgCallback(StringBuilder XmlListFsPkg);
    private void SetListFsPkg(StringBuilder XmlListFsPkg)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (TbCtrlFsList.InvokeRequired)
        {
            SetListFsPkgCallback d = new SetListFsPkgCallback(SetListFsPkg);
            TbCtrlFsList.Invoke(d, XmlListFsPkg);
        }
        else
        {
            //if(!VarGlobal.LessVerbose)Console.WriteLine(XmlListFsPkg.ToString());
            List<string> Result = ReadXml.GetAllFirstAttrValue(XmlListFsPkg.ToString(), "directory", "name");
            if (Result.Count > 0)
            {
                PanelFsList.Controls.Clear();

                foreach (string item in Result)
                {
                    if (backgroundWorkerAddFs.CancellationPending == true) break;
                    Dictionary<string, string> SubResult = ReadXml.GetAllAttrValue(XmlListFsPkg.ToString(), "directory", "name", item);
                    string name = string.Empty;
                    int Size = 0;
                    long Time = 0;
                    foreach (string KeyName in SubResult.Keys)
                    {
                        switch (KeyName)
                        {
                        case "name":
                            name = SubResult[KeyName];
                            break;
                        case "size":
                            Size = Convert.ToInt32(SubResult[KeyName]);
                            break;
                        case "mtime":
                            Time = Convert.ToInt64(SubResult[KeyName]);
                            break;
                        default:
                            break;
                        }
                    }
                    PkgFiles TheCtrl = new PkgFiles(Package, name, Size, Time);
                    TheCtrl.Dock = DockStyle.Top;
                    PanelFsList.Controls.Add(TheCtrl);
                }

                //Mono have a damed bug who don't show ToolStrip2 since we rezise the control
                this.Height = this.Height + 1;
                this.Height = this.Height - 1;
            }

            VarGlobal.NetEvManager.DoSomething("List of files Done");
            if (FromBtn == false && backgroundWorkerBuildStatus.IsBusy == false) backgroundWorkerBuildStatus.RunWorkerAsync();
        }
    }

    private void backgroundWorkerPkgFlags_DoWork(object sender, DoWorkEventArgs e)
    {
        backgroundWorkerPkgFlagsIsBusy = true;
        if (CancelRequest == true) return;
        string XmlTemplate = SourceProjectPackageMeta.GetSourceProjectPackageMeta(Package).ToString();
        if (File.Exists(MetaPkgXmlFs) == true) File.Delete(MetaPkgXmlFs);
        File.WriteAllText(MetaPkgXmlFs, XmlTemplate);

    }

    private void backgroundWorkerPkgFlags_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {
        if (CancelRequest == true) return;

    }

    private void backgroundWorkerPkgFlags_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        try
        {
            if (CancelRequest == true &&
                    backgroundWorkerAddFsIsBusy == false &&
                    backgroundWorkerBuildStatusIsBusy == false &&
                    backgroundWorkerFsListIsBusy == false)
            {
                CancelRequest = false;
                return;
            }

            if (CancelRequest == true) return;

            if (backgroundWorkerAddFs.CancellationPending == true || CancelRequest == true) return;
            AddAllPkgFlagsRow();
            foreach (PkgBuildStatus Ctl in CtrColl.Values)
            {
                if (backgroundWorkerAddFs.CancellationPending == true || CancelRequest == true) break;
                Ctl.FinishedToAdd = true;
            }
            VarGlobal.NetEvManager.DoSomething("List of Flags project Done");
            if (CancelRequest == true) VarGlobal.NetEvManager.DoSomething(this.Package + " is disposed!");
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
        backgroundWorkerPkgFlagsIsBusy = false;
    }

    private void backgroundWorkerBuildStatus_DoWork(object sender, DoWorkEventArgs e)
    {
        backgroundWorkerBuildStatusIsBusy = true;
        if (CancelRequest == true) return;
    }

    private void backgroundWorkerBuildStatus_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {
        if (CancelRequest == true) return;

    }

    Dictionary<string, PkgBuildStatus> CtrColl = new Dictionary<string, PkgBuildStatus>();
    private void backgroundWorkerBuildStatus_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        if (CancelRequest == true &&
                backgroundWorkerAddFsIsBusy == false &&
                backgroundWorkerFsListIsBusy == false &&
                backgroundWorkerPkgFlagsIsBusy == false)
        {
            CancelRequest = false;
            return;
        }

        if (CancelRequest == true) return;
        if (backgroundWorkerAddFs.CancellationPending == true || CancelRequest == true) return;

        foreach (PkgBuildStatus item in CtrColl.Values)
        {
            item.CleanAll();
            item.Dispose();
        }
        CtrColl.Clear();
        tableLayoutPaneBuildStatus.Height = 10;
        tableLayoutPaneBuildStatus.Controls.Clear();
        foreach (Dictionary<string, string> Repos in AllRepo)
        {
            if (backgroundWorkerAddFs.CancellationPending == true || CancelRequest == true) break;
            List<string> Archs = new List<string>();
#pragma warning disable 0219
            string Repository = string.Empty;
            string Project = string.Empty;
            string ItemInfo = string.Empty;
            foreach (string KeyName in Repos.Keys)
            {
                if (backgroundWorkerAddFs.CancellationPending == true || CancelRequest == true) break;
                if (KeyName.StartsWith("arch", StringComparison.CurrentCultureIgnoreCase) == true) Archs.Add(Repos[KeyName]);
                switch (KeyName)
                {
                case "repository":
                    Repository = Repos[KeyName];
                    break;
                case "project":
                    Project = Repos[KeyName];
                    break;
                case "ItemInfo":
                    ItemInfo = Repos[KeyName];
                    break;
                default:
                    break;
                }
            }
            if (CancelRequest == true) return;
            PkgBuildStatus TheCtrl = new PkgBuildStatus(ItemInfo, Archs, Package);
            tableLayoutPaneBuildStatus.Controls.Add(TheCtrl);
            tableLayoutPaneBuildStatus.Height += TheCtrl.Height + 6;
            TheCtrl.Dock = DockStyle.Top;
            CtrColl.Add(TheCtrl.Handle.ToString(), TheCtrl);

        }

        VarGlobal.NetEvManager.DoSomething("List of packages Done");
        if (backgroundWorkerPkgFlags.IsBusy == false && CancelRequest == false) backgroundWorkerPkgFlags.RunWorkerAsync();
        backgroundWorkerBuildStatusIsBusy = false;
    }

    private void BuildPkg_Disposed(object sender, EventArgs e)
    {

    }

    private void BtnRefresh_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        FromBtn = true;
        if (backgroundWorkerFsList.IsBusy == false)
            backgroundWorkerFsList.RunWorkerAsync();
        Cursor = Cursors.Default;
    }

    private void BtnRebuild_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        MessageBox.Show((RebuildPkg.PostRebuildPkg(Package)).ToString(), "Result infos", MessageBoxButtons.OK, MessageBoxIcon.Information);
        Cursor = Cursors.Default;
    }

    bool FromBtn = false;
    private void BtnAddFs_Click(object sender, EventArgs e)
    {
        if (backgroundWorkerAddFs.IsBusy == false)
        {
            if (openFileDialog1.ShowDialog() == DialogResult.OK)
            {
                FromBtn = true;
                Debug.WriteLine("START TO UPLOAD A FILE :");
                toolStripProgressBarAddFs.Visible = true;
                toolStripProgressBarAddFs.Maximum = openFileDialog1.FileNames.Length;
                backgroundWorkerAddFs.RunWorkerAsync(openFileDialog1.FileNames);
            }
        }
        else
        {
            MessageBox.Show("An upload operation is already in progress, wait it terminate before restart a new one.", "Infos", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
    }

    private void backgroundWorkerAddFs_DoWork(object sender, DoWorkEventArgs e)
    {
        backgroundWorkerAddFsIsBusy = true;
        if (CancelRequest == true) return;
        string[] FsList = (string[])e.Argument;
        int Cnt = 0;
        foreach (string FsName in FsList)
        {
            if (backgroundWorkerAddFs.CancellationPending == true || CancelRequest == true) break;
            backgroundWorkerAddFs.ReportProgress(Cnt, FsName);
            PutSourceProjectPackageFile.PutFile(Package, FsName);
            Cnt += 1;
            backgroundWorkerAddFs.ReportProgress(Cnt, FsName);
        }
    }

    private void backgroundWorkerAddFs_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        if (CancelRequest == true &&
                backgroundWorkerBuildStatusIsBusy == false &&
                backgroundWorkerFsListIsBusy == false &&
                backgroundWorkerPkgFlagsIsBusy == false)
        {
            CancelRequest = false;
            return;
        }
        if (CancelRequest == true) return;

        LblStatusAddFs.Text = string.Empty;
        toolStripProgressBarAddFs.Visible = false;
        if (backgroundWorkerFsList.IsBusy == false && CancelRequest == false)
            backgroundWorkerFsList.RunWorkerAsync();
        backgroundWorkerAddFsIsBusy = false;
    }

    private void backgroundWorkerAddFs_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {
        if (CancelRequest == true) return;
        LblStatusAddFs.Text = "Uploading file " + new FileInfo(e.UserState.ToString()).Name;
        toolStripProgressBarAddFs.Value = e.ProgressPercentage;
    }

    private void BtnRefresh_MouseEnter(object sender, EventArgs e)
    {
        Cursor = Cursors.Hand;
    }

    private void BtnRefresh_MouseLeave(object sender, EventArgs e)
    {
        Cursor = Cursors.Default;
    }

    private void DtGridPkgFlag_CellContentClick(object sender, DataGridViewCellEventArgs e)
    {
        if (e.ColumnIndex > 0)
        {
            DataGridViewCheckBoxCell Obj = (DataGridViewCheckBoxCell)this.DtGridPkgFlag.CurrentCell;
            Obj.Value = Obj.EditedFormattedValue;
            string BuildPub = "publish";
            int ClmIdx = this.DtGridPkgFlag.CurrentCell.ColumnIndex;
            string ClmHeaderTxt = this.DtGridPkgFlag.Columns[ClmIdx].HeaderText;
            if (ClmHeaderTxt.StartsWith("Build", StringComparison.InvariantCultureIgnoreCase) == true) BuildPub = "build";
            if (e.RowIndex >= 0) ChangeChkColor(ref Obj, this.DtGridPkgFlag.Rows[e.RowIndex].Cells[0].Value.ToString(), true, BuildPub);
        }
    }

    private void ChangeAttrNode(string state, string BuildPublish, string Arch, string RepoName)
    {
        if (true)
        {
            WriteXml.NodeExist(MetaPkgXmlFs, "package", BuildPublish, true);
            Dictionary<string, string> AttribList = new Dictionary<string, string>();
            AttribList.Add("repository", RepoName);
            AttribList.Add("arch", Arch);
            WriteXml.AppendChild(MetaPkgXmlFs, true, "package", BuildPublish, state, AttribList);
        }
    }

    private void RemovNode(string BuildPublish, string Arch, string RepoName)
    {
        if (true)
        {
            Dictionary<string, string> AttribList = new Dictionary<string, string>();
            AttribList.Add("repository", RepoName);
            AttribList.Add("arch", Arch);
            WriteXml.RemoveNode(MetaPkgXmlFs, "package", BuildPublish, AttribList);
        }
    }

    private void AddAllPkgFlagsRow()
    {
        if (CancelRequest == true) return;
        string XmlDt = File.ReadAllText(MetaPkgXmlFs);
        DtGridPkgFlag.Rows.Clear();
        foreach (Dictionary<string, string> Repos in AllRepo)
        {
            if (CancelRequest == true) break;
            if (backgroundWorkerAddFs.CancellationPending == true) break;
            if (Repos.ContainsKey("ItemInfo") == true) this.DtGridPkgFlag.Rows.Add(AddAPkgFlagsRow(Repos["ItemInfo"], XmlDt));
        }
    }

    private DataGridViewRow AddAPkgFlagsRow(string RepoName, string XmlData)
    {
        DataGridViewCheckBoxCell Buildi586 = new DataGridViewCheckBoxCell(true), Publishi586 = new DataGridViewCheckBoxCell(true),
        Build64 = new DataGridViewCheckBoxCell(true), Publish64 = new DataGridViewCheckBoxCell(true);
        DataGridViewTextBoxCell RepoCell = new DataGridViewTextBoxCell();
        RepoCell.Value = RepoName;
        RepoCell.ToolTipText = RepoName;

        Buildi586.Value = ReadXml.GetChkState(XmlData, "package", "build", RepoName, "i586");
        Build64.Value = ReadXml.GetChkState(XmlData, "package", "build", RepoName, "x86_64");
        Publishi586.Value = ReadXml.GetChkState(XmlData, "package", "publish", RepoName, "i586");
        Publish64.Value = ReadXml.GetChkState(XmlData, "package", "publish", RepoName, "x86_64");
        Buildi586.Tag = "i586";
        Build64.Tag = "x86_64";
        Publishi586.Tag = "i586";
        Publish64.Tag = "x86_64";
        ChangeChkColor(ref Buildi586, RepoName, false, string.Empty);
        ChangeChkColor(ref Build64, RepoName, false, string.Empty);
        ChangeChkColor(ref Publishi586, RepoName, false, string.Empty);
        ChangeChkColor(ref Publish64, RepoName, false, string.Empty);
        DataGridViewRow TheRow = new DataGridViewRow();
        TheRow.Cells.AddRange(RepoCell, Buildi586, Publishi586, Build64, Publish64);
        return TheRow;
    }

    private void ChangeChkColor(ref DataGridViewCheckBoxCell ChkBoxCell, string RepoName, bool ChangeXml, string BuildPublish)
    {
        switch ((CheckState)ChkBoxCell.Value)
        {
        case CheckState.Checked:
            if (ChangeXml == true) RemovNode(BuildPublish, ChkBoxCell.Tag.ToString(), RepoName);
            ChkBoxCell.Style.BackColor = Color.Green;
            ChkBoxCell.ToolTipText = "Use the default value of the project";
            break;
        case CheckState.Indeterminate:
            if (ChangeXml == true) ChangeAttrNode("enable", BuildPublish, ChkBoxCell.Tag.ToString(), RepoName);
            ChkBoxCell.Style.BackColor = Color.Yellow;
            ChkBoxCell.ToolTipText = "Force enable";
            break;
        case CheckState.Unchecked:
            if (ChangeXml == true) ChangeAttrNode("disable", BuildPublish, ChkBoxCell.Tag.ToString(), RepoName);
            ChkBoxCell.Style.BackColor = Control.DefaultBackColor;
            ChkBoxCell.ToolTipText = "Force disable";
            break;
        default:
            break;
        }
    }

    private void BtnWritePkgMeta_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        MessageBox.Show(PutSourceProjectPkgMeta.PutProjectPkgMeta(Package, MetaPkgXmlFs).ToString(), "Result Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
        if (backgroundWorkerPkgFlags.IsBusy == false) backgroundWorkerPkgFlags.RunWorkerAsync();
        Cursor = Cursors.Default;
    }

    private void BtnEditMetaPkg_Click(object sender, EventArgs e)
    {

        XmlEditor EdFrm = new MonoOSC.XmlEditor();

        EdFrm.XmlFs = MetaPkgXmlFs;
        EdFrm.ShowDialog();
        if (MessageBox.Show("Update informations on the server ?", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
        {
            try
            {
                MessageBox.Show(PutSourceProjectPkgMeta.PutProjectPkgMeta(Package, MetaPkgXmlFs).ToString(), "Result Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
                if (backgroundWorkerPkgFlags.IsBusy == false) backgroundWorkerPkgFlags.RunWorkerAsync();
            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
            }
        }
    }

    private void BtnRefreshFlags_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        DtGridPkgFlag.Rows.Clear();
        if (backgroundWorkerPkgFlags.IsBusy == false) backgroundWorkerPkgFlags.RunWorkerAsync();
        Cursor = Cursors.Default;
    }

    private void BuildPkg_ControlRemoved(object sender, ControlEventArgs e)
    {
        if (CancelRequest == false) CancelTrafic();
    }

    public bool CancelRequest = false;
    public bool backgroundWorkerAddFsIsBusy = false;
    public bool backgroundWorkerBuildStatusIsBusy = false;
    public bool backgroundWorkerFsListIsBusy = false;
    public bool backgroundWorkerPkgFlagsIsBusy = false;
    public void CancelTrafic()
    {
        try
        {
            CancelRequest = true;
            backgroundWorkerAddFs.CancelAsync();
            backgroundWorkerBuildStatus.CancelAsync();
            backgroundWorkerFsList.CancelAsync();
            backgroundWorkerPkgFlags.CancelAsync();
            if (backgroundWorkerAddFsIsBusy == false &&
                    backgroundWorkerBuildStatusIsBusy == false &&
                    backgroundWorkerFsListIsBusy == false &&
                    backgroundWorkerPkgFlagsIsBusy == false)
            {
                CancelRequest = false;
                VarGlobal.NetEvManager.DoSomething(this.Package + " is disposed!");
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    private void BtnWipePkg_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("All the dead (build failled) file(s) of " + Package + " will be permanently delete (wipe) from the server.", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Warning) == DialogResult.Yes)
        {
            Cursor = Cursors.WaitCursor;
            MessageBox.Show((PostWipeBuildProjectPackageFiles.PostWipeBuildPrjPkgFiles(Package)).ToString());
            Cursor = Cursors.Default;
        }
    }

    private void BtnRefresAll_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        //FromBtn == true &&
        if (backgroundWorkerBuildStatus.IsBusy == false) backgroundWorkerBuildStatus.RunWorkerAsync();
        Cursor = Cursors.Default;
    }

    private void defaultToolStripMenuItem_Click(object sender, EventArgs e)
    {
        SetDefEnDis(CheckState.Checked);
    }

    private void enableToolStripMenuItem_Click(object sender, EventArgs e)
    {
        SetDefEnDis(CheckState.Indeterminate);
    }

    private void disableToolStripMenuItem_Click(object sender, EventArgs e)
    {
        SetDefEnDis(CheckState.Unchecked);
    }

    private void SetDefEnDis(CheckState TheSate)
    {
        int ClmIdxInternal = ClmIdx;
        int RowIdxInternal = RowIdx;
        if (ClmIdx > 0)
        {
            foreach (DataGridViewRow item in DtGridPkgFlag.Rows)
            {
                item.Cells[ClmIdxInternal].Value = TheSate;

                //item.Cells[ClmIdxInternal].Value = item.Cells[ClmIdxInternal].EditedFormattedValue;
                string BuildPub = "publish";
                string ClmHeaderTxt = this.DtGridPkgFlag.Columns[ClmIdxInternal].HeaderText;
                if (ClmHeaderTxt.StartsWith("Build", StringComparison.InvariantCultureIgnoreCase) == true) BuildPub = "build";
                DataGridViewCheckBoxCell TheCell = (DataGridViewCheckBoxCell)item.Cells[ClmIdxInternal];
                ChangeChkColor(ref TheCell, DtGridPkgFlag.Rows[TheCell.RowIndex].Cells[0].Value.ToString(), true, BuildPub);
            }
        }
        else
        {
            if (RowIdxInternal >= 0)
            {
                for (int i = 1; i < DtGridPkgFlag.Rows[RowIdxInternal].Cells.Count; i++)
                {
                    DtGridPkgFlag.Rows[RowIdxInternal].Cells[i].Value = TheSate;

                    //DtGridPkgFlag.Rows[RowIdxInternal].Cells[i].Value = DtGridPkgFlag.Rows[RowIdxInternal].Cells[i].EditedFormattedValue;
                    string BuildPub = "publish";
                    string ClmHeaderTxt = this.DtGridPkgFlag.Columns[DtGridPkgFlag.Rows[RowIdxInternal].Cells[i].ColumnIndex].HeaderText;
                    if (ClmHeaderTxt.StartsWith("Build", StringComparison.InvariantCultureIgnoreCase) == true) BuildPub = "build";
                    DataGridViewCheckBoxCell TheCell = (DataGridViewCheckBoxCell)DtGridPkgFlag.Rows[RowIdxInternal].Cells[i];
                    ChangeChkColor(ref TheCell, DtGridPkgFlag.Rows[RowIdxInternal].Cells[0].Value.ToString(), true, BuildPub);
                }
            }
        }
    }

    private int ClmIdx = 0;
    private int RowIdx = 0;
    private void DtGridPkgFlag_CellMouseMove(object sender, DataGridViewCellMouseEventArgs e)
    {
        ClmIdx = e.ColumnIndex;
        RowIdx = e.RowIndex;
        if (ClmIdx == 0) toolStripTextBox1.Text = "Set all item of this line to :";
        else toolStripTextBox1.Text = "Set all item of this column to :";
    }

    private void BuildPkg_VisibleChanged(object sender, EventArgs e)
    {
        /*toolStrip2.Visible = false;
        toolStrip2.Refresh();
        toolStrip2.Visible = true;*/
    }

    int MemHeight = 168;
    bool Collapse1 = false;
    bool Collapse2 = false;
    private void toolStripBtnHide_Click(object sender, EventArgs e)
    {
        Collapse1 = !Collapse1;
        if (Collapse1)
        {
            toolStripBtnHide.Image = imageList1.Images[1];
            MemHeight = splitContainerFsFlags.SplitterDistance;
            splitContainerFsFlags.SplitterDistance = 60;
        }
        else
        {
            toolStripBtnHide.Image = imageList1.Images[0];
            splitContainerFsFlags.SplitterDistance = MemHeight;
        }
    }

    private void toolStripBtnHideFlags_Click(object sender, EventArgs e)
    {
        Collapse2 = !Collapse2;
        if (Collapse2)
        {
            toolStripBtnHideFlags.Image = imageList1.Images[1];
            TbCtrlPkgFlags.Dock = DockStyle.Top;
            TbCtrlPkgFlags.Height = 60;
        }
        else
        {
            toolStripBtnHideFlags.Image = imageList1.Images[0];
            TbCtrlPkgFlags.Dock = DockStyle.Fill;
        }
    }

    private void splitContainerFsFlags_MouseDoubleClick(object sender, MouseEventArgs e)
    {
        SplitContainer Sp = (SplitContainer)sender;
        if (Sp.Orientation == Orientation.Horizontal)
        {
            if (Sp.SplitterRectangle.Y >= e.Y - 4 && Sp.SplitterRectangle.Y <= e.Y + 4) Sp.SplitterDistance =
                    Sp.Height / 2;
        }
        else
        {
            if (Sp.SplitterRectangle.X >= e.X - 4 && Sp.SplitterRectangle.X <= e.X + 4) Sp.SplitterDistance =
                    Sp.Width / 2;
        }
    }

    private void BtnAddTarSvn_Click(object sender, EventArgs e)
    {
        AddRemoteTarball Frm = new AddRemoteTarball();
        Frm.ShowDialog(this);
        if (File.Exists(Frm._TheTarBallFs))
        {
            if (backgroundWorkerAddFs.IsBusy == false)
            {
                FromBtn = true;
                Debug.WriteLine("START TO UPLOAD A FILE : " + Frm._TheTarBallFs);
                toolStripProgressBarAddFs.Visible = true;
                toolStripProgressBarAddFs.Maximum = Frm._TheTarBallFs.Length;
                backgroundWorkerAddFs.RunWorkerAsync(new string[] { Frm._TheTarBallFs });
            }
            else
            {
                MessageBox.Show("An upload operation is already in progress, wait it terminate before restart a new one.", "Infos", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }

        }
    }

}//class
}//namespace
