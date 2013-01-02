// PkgBuildStatusArch.Designer.cs created with MonoDevelop
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

namespace MonoOSC.Ctrl
{
partial class PkgBuildStatusArch
{
    /// <summary>
    /// Variable nécessaire au concepteur.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    /// Nettoyage des ressources utilisées.
    /// </summary>
    /// <param name="disposing">true si les ressources managées doivent être supprimées ; sinon, false.</param>
    protected override void Dispose(bool disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
            backgroundWorkerBuild.CancelAsync();
            backgroundWorkerBuildStatus.CancelAsync();
        }
        base.Dispose(disposing);
    }

    #region Code généré par le Concepteur de composants

    /// <summary>
    /// Méthode requise pour la prise en charge du concepteur - ne modifiez pas
    /// le contenu de cette méthode avec l'éditeur de code.
    /// </summary>
    private void InitializeComponent()
    {
        this.components = new System.ComponentModel.Container();
        System.Windows.Forms.ListViewItem listViewItem1 = new System.Windows.Forms.ListViewItem(new string[]
        {
            "Try to fetch file(s)"
        }, -1, System.Drawing.SystemColors.HotTrack, System.Drawing.SystemColors.Info, new System.Drawing.Font("Microsoft Sans Serif", 11.25F, ((System.Drawing.FontStyle)((System.Drawing.FontStyle.Bold | System.Drawing.FontStyle.Underline)))));
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(PkgBuildStatusArch));
        this.splitContainer1 = new System.Windows.Forms.SplitContainer();
        this.BtnHide = new System.Windows.Forms.Button();
        this.BtnRebuild = new System.Windows.Forms.Button();
        this.LblStatus = new System.Windows.Forms.Label();
        this.contextMenuStripClipBoard = new System.Windows.Forms.ContextMenuStrip(this.components);
        this.toolStripMenuItemCopyText = new System.Windows.Forms.ToolStripMenuItem();
        this.LblArch = new System.Windows.Forms.Label();
        this.LblBuildLog = new System.Windows.Forms.LinkLabel();
        this.listViewPckg = new System.Windows.Forms.ListView();
        this.ClmPkgFs = new System.Windows.Forms.ColumnHeader();
        this.ClmSize = new System.Windows.Forms.ColumnHeader();
        this.ClmTime = new System.Windows.Forms.ColumnHeader();
        this.contextMenuStripBin = new System.Windows.Forms.ContextMenuStrip(this.components);
        this.downloadToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.deleteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
        this.backgroundWorkerBuildStatus = new System.ComponentModel.BackgroundWorker();
        this.backgroundWorkerBuild = new System.ComponentModel.BackgroundWorker();
        this.timer1 = new System.Windows.Forms.Timer(this.components);
        this.saveFileDialog1 = new System.Windows.Forms.SaveFileDialog();
        this.imageList1 = new System.Windows.Forms.ImageList(this.components);
        this.refreshStatusToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.refreshListToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.splitContainer1.Panel1.SuspendLayout();
        this.splitContainer1.Panel2.SuspendLayout();
        this.splitContainer1.SuspendLayout();
        this.contextMenuStripClipBoard.SuspendLayout();
        this.contextMenuStripBin.SuspendLayout();
        this.SuspendLayout();
        //
        // splitContainer1
        //
        this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainer1.Location = new System.Drawing.Point(0, 0);
        this.splitContainer1.Name = "splitContainer1";
        this.splitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
        //
        // splitContainer1.Panel1
        //
        this.splitContainer1.Panel1.Controls.Add(this.BtnHide);
        this.splitContainer1.Panel1.Controls.Add(this.BtnRebuild);
        this.splitContainer1.Panel1.Controls.Add(this.LblStatus);
        this.splitContainer1.Panel1.Controls.Add(this.LblArch);
        this.splitContainer1.Panel1.Controls.Add(this.LblBuildLog);
        //
        // splitContainer1.Panel2
        //
        this.splitContainer1.Panel2.Controls.Add(this.listViewPckg);
        this.splitContainer1.Size = new System.Drawing.Size(346, 184);
        this.splitContainer1.SplitterDistance = 43;
        this.splitContainer1.TabIndex = 5;
        //
        // BtnHide
        //
        this.BtnHide.BackgroundImage = global::MonoOSC.Properties.Resources.Hide;
        this.BtnHide.Location = new System.Drawing.Point(32, 3);
        this.BtnHide.Name = "BtnHide";
        this.BtnHide.Size = new System.Drawing.Size(23, 23);
        this.BtnHide.TabIndex = 7;
        this.toolTip1.SetToolTip(this.BtnHide, "Hide or show the panel");
        this.BtnHide.UseVisualStyleBackColor = true;
        this.BtnHide.Click += new System.EventHandler(this.BtnHide_Click);
        //
        // BtnRebuild
        //
        this.BtnRebuild.BackgroundImage = global::MonoOSC.Properties.Resources.Rebuild;
        this.BtnRebuild.Location = new System.Drawing.Point(3, 3);
        this.BtnRebuild.Name = "BtnRebuild";
        this.BtnRebuild.Size = new System.Drawing.Size(23, 23);
        this.BtnRebuild.TabIndex = 0;
        this.toolTip1.SetToolTip(this.BtnRebuild, "Rebuild this only");
        this.BtnRebuild.UseVisualStyleBackColor = true;
        this.BtnRebuild.MouseLeave += new System.EventHandler(this.BtnRebuild_MouseLeave);
        this.BtnRebuild.Click += new System.EventHandler(this.BtnRebuild_Click);
        this.BtnRebuild.MouseEnter += new System.EventHandler(this.BtnRebuild_MouseEnter);
        //
        // LblStatus
        //
        this.LblStatus.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                                 | System.Windows.Forms.AnchorStyles.Left)
                                 | System.Windows.Forms.AnchorStyles.Right)));
        this.LblStatus.AutoEllipsis = true;
        this.LblStatus.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
        this.LblStatus.ContextMenuStrip = this.contextMenuStripClipBoard;
        this.LblStatus.Font = new System.Drawing.Font("Microsoft Sans Serif", 10.25F, System.Drawing.FontStyle.Bold);
        this.LblStatus.ForeColor = System.Drawing.Color.BlanchedAlmond;
        this.LblStatus.Location = new System.Drawing.Point(58, 3);
        this.LblStatus.Name = "LblStatus";
        this.LblStatus.Size = new System.Drawing.Size(218, 39);
        this.LblStatus.TabIndex = 1;
        this.LblStatus.Text = "Getting remote status";
        this.LblStatus.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        this.LblStatus.MouseClick += new System.Windows.Forms.MouseEventHandler(this.LblStatus_MouseClick);
        //
        // contextMenuStripClipBoard
        //
        this.contextMenuStripClipBoard.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.toolStripMenuItemCopyText,
            this.refreshStatusToolStripMenuItem
        });
        this.contextMenuStripClipBoard.Name = "contextMenuStripClipBoard";
        this.contextMenuStripClipBoard.Size = new System.Drawing.Size(167, 48);
        //
        // toolStripMenuItemCopyText
        //
        this.toolStripMenuItemCopyText.Name = "toolStripMenuItemCopyText";
        this.toolStripMenuItemCopyText.Size = new System.Drawing.Size(166, 22);
        this.toolStripMenuItemCopyText.Text = "Copy status text";
        this.toolStripMenuItemCopyText.Click += new System.EventHandler(this.toolStripMenuItemCopyText_Click);
        //
        // LblArch
        //
        this.LblArch.AutoSize = true;
        this.LblArch.Location = new System.Drawing.Point(3, 29);
        this.LblArch.Name = "LblArch";
        this.LblArch.Size = new System.Drawing.Size(29, 13);
        this.LblArch.TabIndex = 2;
        this.LblArch.Text = "Arch";
        //
        // LblBuildLog
        //
        this.LblBuildLog.Anchor = System.Windows.Forms.AnchorStyles.Right;
        this.LblBuildLog.AutoSize = true;
        this.LblBuildLog.Location = new System.Drawing.Point(273, 6);
        this.LblBuildLog.Name = "LblBuildLog";
        this.LblBuildLog.Size = new System.Drawing.Size(53, 13);
        this.LblBuildLog.TabIndex = 3;
        this.LblBuildLog.TabStop = true;
        this.LblBuildLog.Text = "[Build log]";
        this.LblBuildLog.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.LblBuildLog_LinkClicked);
        //
        // listViewPckg
        //
        this.listViewPckg.BackColor = System.Drawing.SystemColors.Info;
        this.listViewPckg.Columns.AddRange(new System.Windows.Forms.ColumnHeader[]
        {
            this.ClmPkgFs,
            this.ClmSize,
            this.ClmTime
        });
        this.listViewPckg.ContextMenuStrip = this.contextMenuStripBin;
        this.listViewPckg.Dock = System.Windows.Forms.DockStyle.Fill;
        this.listViewPckg.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, ((System.Drawing.FontStyle)((System.Drawing.FontStyle.Bold | System.Drawing.FontStyle.Underline))));
        this.listViewPckg.ForeColor = System.Drawing.SystemColors.HotTrack;
        this.listViewPckg.FullRowSelect = true;
        this.listViewPckg.GridLines = true;
        this.listViewPckg.Items.AddRange(new System.Windows.Forms.ListViewItem[]
        {
            listViewItem1
        });
        this.listViewPckg.Location = new System.Drawing.Point(0, 0);
        this.listViewPckg.Name = "listViewPckg";
        this.listViewPckg.ShowItemToolTips = true;
        this.listViewPckg.Size = new System.Drawing.Size(346, 137);
        this.listViewPckg.TabIndex = 4;
        this.listViewPckg.UseCompatibleStateImageBehavior = false;
        this.listViewPckg.View = System.Windows.Forms.View.Details;
        this.listViewPckg.DoubleClick += new System.EventHandler(this.listViewPckg_DoubleClick);
        this.listViewPckg.ColumnClick += new System.Windows.Forms.ColumnClickEventHandler(this.listViewPckg_ColumnClick);
        //
        // ClmPkgFs
        //
        this.ClmPkgFs.Text = "Package(s) file(s)";
        this.ClmPkgFs.Width = 277;
        //
        // ClmSize
        //
        this.ClmSize.Text = "Size";
        //
        // ClmTime
        //
        this.ClmTime.Text = "Time";
        this.ClmTime.Width = 205;
        //
        // contextMenuStripBin
        //
        this.contextMenuStripBin.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.refreshListToolStripMenuItem,
            this.downloadToolStripMenuItem,
            this.deleteToolStripMenuItem
        });
        this.contextMenuStripBin.Name = "contextMenuStripBin";
        this.contextMenuStripBin.Size = new System.Drawing.Size(140, 70);
        //
        // downloadToolStripMenuItem
        //
        this.downloadToolStripMenuItem.Name = "downloadToolStripMenuItem";
        this.downloadToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
        this.downloadToolStripMenuItem.Text = "Download";
        this.downloadToolStripMenuItem.Click += new System.EventHandler(this.listViewPckg_DoubleClick);
        //
        // deleteToolStripMenuItem
        //
        this.deleteToolStripMenuItem.Name = "deleteToolStripMenuItem";
        this.deleteToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
        this.deleteToolStripMenuItem.Text = "Delete";
        this.deleteToolStripMenuItem.Visible = false;
        this.deleteToolStripMenuItem.Click += new System.EventHandler(this.deleteToolStripMenuItem_Click);
        //
        // backgroundWorkerBuildStatus
        //
        this.backgroundWorkerBuildStatus.WorkerReportsProgress = true;
        this.backgroundWorkerBuildStatus.WorkerSupportsCancellation = true;
        this.backgroundWorkerBuildStatus.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorkerBuildStatus_DoWork);
        this.backgroundWorkerBuildStatus.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorkerBuildStatus_RunWorkerCompleted);
        //
        // backgroundWorkerBuild
        //
        this.backgroundWorkerBuild.WorkerReportsProgress = true;
        this.backgroundWorkerBuild.WorkerSupportsCancellation = true;
        this.backgroundWorkerBuild.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorkerBuild_DoWork);
        this.backgroundWorkerBuild.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorkerBuild_RunWorkerCompleted);
        //
        // timer1
        //
        this.timer1.Interval = 60000;
        this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
        //
        // saveFileDialog1
        //
        this.saveFileDialog1.DefaultExt = "All Files|*.*";
        this.saveFileDialog1.RestoreDirectory = true;
        this.saveFileDialog1.SupportMultiDottedExtensions = true;
        this.saveFileDialog1.Title = "Choice a destination file name";
        //
        // imageList1
        //
        this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
        this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
        this.imageList1.Images.SetKeyName(0, "Hide.bmp");
        this.imageList1.Images.SetKeyName(1, "Show.bmp");
        //
        // refreshStatusToolStripMenuItem
        //
        this.refreshStatusToolStripMenuItem.Name = "refreshStatusToolStripMenuItem";
        this.refreshStatusToolStripMenuItem.Size = new System.Drawing.Size(166, 22);
        this.refreshStatusToolStripMenuItem.Text = "Refresh status";
        this.refreshStatusToolStripMenuItem.Click += new System.EventHandler(this.refreshStatusToolStripMenuItem_Click);
        //
        // refreshListToolStripMenuItem
        //
        this.refreshListToolStripMenuItem.Name = "refreshListToolStripMenuItem";
        this.refreshListToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
        this.refreshListToolStripMenuItem.Text = "Refresh list";
        this.refreshListToolStripMenuItem.Click += new System.EventHandler(this.refreshListToolStripMenuItem_Click);
        //
        // PkgBuildStatusArch
        //
        this.BackColor = System.Drawing.Color.DarkGray;
        this.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.Controls.Add(this.splitContainer1);
        this.DoubleBuffered = true;
        this.Name = "PkgBuildStatusArch";
        this.Size = new System.Drawing.Size(346, 184);
        this.splitContainer1.Panel1.ResumeLayout(false);
        this.splitContainer1.Panel1.PerformLayout();
        this.splitContainer1.Panel2.ResumeLayout(false);
        this.splitContainer1.ResumeLayout(false);
        this.contextMenuStripClipBoard.ResumeLayout(false);
        this.contextMenuStripBin.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.Button BtnRebuild;
    private System.Windows.Forms.Label LblStatus;
    private System.Windows.Forms.Label LblArch;
    private System.Windows.Forms.LinkLabel LblBuildLog;
    private System.Windows.Forms.ListView listViewPckg;
    private System.Windows.Forms.ColumnHeader ClmPkgFs;
    private System.Windows.Forms.SplitContainer splitContainer1;
    private System.Windows.Forms.ToolTip toolTip1;
    private System.Windows.Forms.ContextMenuStrip contextMenuStripClipBoard;
    private System.Windows.Forms.ToolStripMenuItem toolStripMenuItemCopyText;
    private System.ComponentModel.BackgroundWorker backgroundWorkerBuildStatus;
    private System.ComponentModel.BackgroundWorker backgroundWorkerBuild;
    private System.Windows.Forms.ColumnHeader ClmSize;
    private System.Windows.Forms.ColumnHeader ClmTime;
    private System.Windows.Forms.Timer timer1;
    private System.Windows.Forms.SaveFileDialog saveFileDialog1;
    private System.Windows.Forms.ContextMenuStrip contextMenuStripBin;
    private System.Windows.Forms.ToolStripMenuItem downloadToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem deleteToolStripMenuItem;
    private System.Windows.Forms.ImageList imageList1;
    private System.Windows.Forms.Button BtnHide;
    private System.Windows.Forms.ToolStripMenuItem refreshStatusToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem refreshListToolStripMenuItem;
}
}
