// PkgBuildStatusArch.cs created with MonoDevelop
//
//User: eric at 00:00 09/08/2008
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
using MonoOBSFramework.Functions.BuildResults;
using MonoOBSFramework;
using MonoOBSFramework.Functions.Sources;
using MonoOBSFramework.Functions.Build;
using System.Diagnostics;

namespace MonoOSC.Ctrl
{
public partial class PkgBuildStatusArch : UserControl
{
    private string Repo = string.Empty;
    private string Archi = string.Empty;
    private string Pkg = string.Empty;
    private bool _OperationStatusEnd = false;
    int MemHight = 184;

    public PkgBuildStatusArch(string Repository, string Arch, string Package)
    {
        InitializeComponent();
        LblArch.Text = Arch;
        Repo = Repository;
        Archi = Arch;
        Pkg = Package;
        _OperationStatusEnd = false;
        backgroundWorkerBuildStatus.RunWorkerAsync("Status");
    }

    StringBuilder XmlRes;
    private void backgroundWorkerBuildStatus_DoWork(object sender, DoWorkEventArgs e)
    {
        if (backgroundWorkerBuildStatus.CancellationPending == true) return;
        try
        {

            switch (e.Argument.ToString())
            {
            case "Status":
                e.Result = "Status";
                XmlRes = BuildPkgResultStatus.GetBuildPkgResultStatus(Repo, Archi, Pkg);
                //if(!VarGlobal.LessVerbose)Console.WriteLine(XmlRes);
                break;
            case "FsList":
                e.Result = "FsList";
                XmlRes = BuildPkgResultFile.GetBuildPkgResultFile(Repo, Archi, Pkg);
                break;
            default:
                break;
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    private void backgroundWorkerBuildStatus_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        try
        {
            switch (e.Result.ToString())
            {
            case "Status":
                SetStatus(XmlRes);
                _FinishedToAdd = true;
                break;
            case "FsList":
                SetFsList(XmlRes);
                break;
            default:
                break;
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
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
            //TODOS : At this time it consume too mutch bandswitch who made timeout request
            //timer1.Enabled = true;
        }
    }

    delegate void SetStatusCallback(StringBuilder XmlStatus);
    private void SetStatus(StringBuilder XmlStatus)
    {
        try
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (LblStatus.InvokeRequired)
            {
                SetStatusCallback d = new SetStatusCallback(SetStatus);
                LblStatus.Invoke(d, XmlStatus);
            }
            else
            {
                //if(!VarGlobal.LessVerbose)Console.WriteLine(XmlStatus.ToString());
                List<string> Result = ReadXml.GetValue(XmlStatus.ToString(), "status", "code");
                if (Result.Count > 0)
                {
                    string Res = Result[0];
                    List<string> Detail = ReadXml.GetValue(XmlStatus.ToString(), "status", "details");
                    if (Detail.Count > 0) Res += " " + Detail[0];


                    LblStatus.Text = Res;
                    if (Res.IndexOf("succeeded", StringComparison.InvariantCultureIgnoreCase) > -1) LblStatus.ForeColor = Color.Green;
                    if (Res.IndexOf("failed", StringComparison.InvariantCultureIgnoreCase) > -1) LblStatus.ForeColor = Color.Red;
                }
                backgroundWorkerBuildStatus.RunWorkerAsync("FsList");
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    private int FsByteSize = 0;
    delegate void SetFsListCallback(StringBuilder XmlFsList);
    private void SetFsList(StringBuilder XmlFsList)
    {
        try
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (listViewPckg.InvokeRequired)
            {
                SetFsListCallback d = new SetFsListCallback(SetFsList);
                listViewPckg.Invoke(d, XmlFsList);
            }
            else
            {
                //if(!VarGlobal.LessVerbose)Console.WriteLine(XmlFsList.ToString());
                List<string> Result = ReadXml.GetAllFirstAttrValue(XmlFsList.ToString(), "binarylist", "filename");
                listViewPckg.Items.Clear();
                if (Result.Count > 0)
                {
                    foreach (string item in Result)
                    {
                        if (backgroundWorkerBuildStatus.CancellationPending == true) break;
                        Dictionary<string, string> SubResult = ReadXml.GetAllAttrValue(XmlFsList.ToString(), "binarylist", "filename", item);
                        string name = string.Empty;
                        int Size = 0;
                        string FinalSize = string.Empty;
                        string Time = string.Empty;

                        foreach (string KeyName in SubResult.Keys)
                        {
                            switch (KeyName)
                            {
                            case "filename":
                                name = SubResult[KeyName];
                                break;
                            case "size":
                                Size = Convert.ToInt32(SubResult[KeyName]);
                                FsByteSize = Size;
                                FinalSize = Size.ToString() + " Byte";
                                if (Size >= 1024) FinalSize = (Size / 1024).ToString() + " Kb";
                                if (Size >= (1024 * 1024)) FinalSize = (Size / 1024 / 1024).ToString() + " Mo";
                                break;
                            case "mtime":
                                try
                                {
                                    DateTime Dt2 = Convert.ToDateTime("1970-01-01T01:00:00+01:00");
                                    Time = Dt2.AddSeconds(Convert.ToInt64(SubResult[KeyName])).ToString();
                                }
                                catch (ArgumentOutOfRangeException)
                                {
                                    // fileCreationTime is not valid, so re-throw the exception.
                                    throw;
                                }
                                break;
                            default:
                                break;
                            }
                        }
                        ListViewItem ToAdd = new ListViewItem(name);
                        ToAdd.SubItems.AddRange(new string[] { FinalSize, Time });
                        ToAdd.Tag = FsByteSize;
                        listViewPckg.Items.Add(ToAdd);
                    }
                }
                _OperationStatusEnd = true;
                //FIXME : Yet another mono bug, it don't show the last item when there is an scrollbar
                listViewPckg.Items.Add(string.Empty);
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    public bool OperationStatus
    {
        get
        {
            return _OperationStatusEnd;
        }
        set
        {
            _OperationStatusEnd = value;
        }
    }

    //StringBuilder XmlResBuild;
    private void backgroundWorkerBuild_DoWork(object sender, DoWorkEventArgs e)
    {
        if (backgroundWorkerBuild.CancellationPending == true) return;
        try
        {
            switch (e.Argument.ToString())
            {
            case "Rebuild":
                e.Result = "Rebuild";

                break;
            case "Log":
                e.Result = "Log";
                //XmlResBuild = BuildPkgResultLog.GetBuildPkgResultLog(Repo, Archi, Pkg);
                break;
            default:
                break;
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    private void backgroundWorkerBuild_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        try
        {
            switch (e.Result.ToString())
            {
            case "Rebuild":

                break;
            case "Log":
                /* Form BuildLogFrm = new Form();
                 BuildLogFrm.Text = Pkg + " : " + Archi + " : " + Repo + " (With build " + LblStatus.Text + ")";
                 RichTextBox RtfLog = new RichTextBox();
                 RtfLog.Text = XmlResBuild.ToString();
                 RtfLog.Dock = DockStyle.Fill;
                 RtfLog.WordWrap = false;
                 RtfLog.ScrollBars = RichTextBoxScrollBars.ForcedBoth;

                RtfLog.AcceptsTab = true;
                RtfLog.AutoWordSelection = true;
                RtfLog.BackColor = System.Drawing.SystemColors.GradientInactiveCaption;
                RtfLog.EnableAutoDragDrop = true;
                RtfLog.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
                RtfLog.ForeColor = System.Drawing.Color.DarkBlue;
                RtfLog.HideSelection = false;
                RtfLog.Location = new System.Drawing.Point(0, 0);
                RtfLog.ShowSelectionMargin = true;
                RtfLog.Size = new System.Drawing.Size(609, 374);
                RtfLog.TabIndex = 1;

                 BuildLogFrm.Controls.Add(RtfLog);
                 BuildLogFrm.Size = new Size(800, 600);
                 BuildLogFrm.Show();*/
                Log FrmLog = new Log();
                FrmLog._FrmTitle = Pkg + " : " + Archi + " : " + Repo + " (With build " + LblStatus.Text + ")";
                FrmLog._Archi = Archi;
                FrmLog._Pkg = Pkg;
                FrmLog._Repo = Repo;
                FrmLog.Show();
                break;
            default:
                break;
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
        LblBuildLog.Enabled = true;
    }

    long Hours = 0;
    private void timer1_Tick(object sender, EventArgs e)
    {
        if (LblStatus.Text != "disabled" & LblStatus.Text != "succeeded")
            RefreshStatus();
    }

    private void RefreshStatus()
    {
        try
        {
            Hours += 1;
            if (backgroundWorkerBuildStatus.IsBusy == false &
                    backgroundWorkerBuild.IsBusy == false)// & Hours == 180)
            {
                if (_FinishedToAdd == true)
                {
                    LblStatus.Text = "Getting remote status";
                    backgroundWorkerBuildStatus.RunWorkerAsync("Status");
                }
                Hours = 0;
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    private void LblBuildLog_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
    {
        if (backgroundWorkerBuild.IsBusy == false)
        {
            LblBuildLog.Enabled = false;
            backgroundWorkerBuild.RunWorkerAsync("Log");
        }
    }

    private void BtnRebuild_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        MessageBox.Show((RebuildAloneArch.PostRebuildAloneArch(Repo, Archi, Pkg)).ToString(), "Result infos", MessageBoxButtons.OK, MessageBoxIcon.Information);
        Cursor = Cursors.Default;
    }

    private void listViewPckg_DoubleClick(object sender, EventArgs e)
    {
        if (listViewPckg.SelectedItems.Count > 0)
        {
            saveFileDialog1.FileName = listViewPckg.SelectedItems[0].Text;
            if (saveFileDialog1.ShowDialog() == DialogResult.OK)
                BuildProjectPackageFile.GetBuildProjectPackageFile(Repo, Archi, Pkg, listViewPckg.SelectedItems[0].Text, saveFileDialog1.FileName, 4096, (int)listViewPckg.SelectedItems[0].Tag);

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


    private void toolStripMenuItemCopyText_Click(object sender, EventArgs e)
    {
        Clipboard.SetText(LblStatus.Text);
    }

    private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (listViewPckg.SelectedItems.Count > 0)
        {
            string FsName = listViewPckg.SelectedItems[0].Text;
            if (MessageBox.Show("The file " + FsName + " will be permanently delete from the server.", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Warning) == DialogResult.Yes)
            {
                MessageBox.Show((DeleteBuildProjectPackageFile.DeleteBuildPkgResultFile(Repo, Archi, Pkg, FsName)).ToString());
            }

        }
    }

    bool AscDesc = true;
    private void listViewPckg_ColumnClick(object sender, ColumnClickEventArgs e)
    {
        try
        {
            AscDesc = !AscDesc;

            // Set the ListViewItemSorter property to a new ListViewItemComparer
            // object.
            this.listViewPckg.ListViewItemSorter = new ListViewItemComparer(e.Column, AscDesc);
            // Call the sort method to manually sort.
            this.listViewPckg.Sort();

        }
        catch (Exception ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    private void LblStatus_MouseClick(object sender, MouseEventArgs e)
    {
        if (e.Button == MouseButtons.Left)
            RefreshStatus();
    }

    private void BtnHide_Click(object sender, EventArgs e)
    {
        splitContainer1.Panel2Collapsed = !splitContainer1.Panel2Collapsed;
        if (splitContainer1.Panel2Collapsed)
        {
            BtnHide.BackgroundImage = imageList1.Images[1];
            MemHight = this.Height;
            this.Height = 184;
        }
        else
        {
            BtnHide.BackgroundImage = imageList1.Images[0];
            this.Height = MemHight;
            splitContainer1.SplitterDistance = 43;
        }
    }

    private void refreshListToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (!backgroundWorkerBuildStatus.IsBusy) backgroundWorkerBuildStatus.RunWorkerAsync("FsList");
    }

    private void refreshStatusToolStripMenuItem_Click(object sender, EventArgs e)
    {
        RefreshStatus();
    }

}
}
