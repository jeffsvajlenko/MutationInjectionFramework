// BuildPkg.Designer.cs created with MonoDevelop
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

namespace MonoOSC.Ctrl
{
partial class BuildPkg
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
            backgroundWorkerAddFs.CancelAsync();
            backgroundWorkerBuildStatus.CancelAsync();
            backgroundWorkerFsList.CancelAsync();
            backgroundWorkerPkgFlags.CancelAsync();
            components.Dispose();
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
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(BuildPkg));
        System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
        this.splitContainerBuildPkg = new System.Windows.Forms.SplitContainer();
        this.splitContainerFsFlags = new System.Windows.Forms.SplitContainer();
        this.TbCtrlFsList = new System.Windows.Forms.TabControl();
        this.TbPgFsList = new System.Windows.Forms.TabPage();
        this.toolStripContainer1 = new System.Windows.Forms.ToolStripContainer();
        this.PanelFsList = new System.Windows.Forms.Panel();
        this.toolStripFsList = new System.Windows.Forms.ToolStrip();
        this.BtnRefresh = new System.Windows.Forms.ToolStripButton();
        this.BtnAddFs = new System.Windows.Forms.ToolStripButton();
        this.toolStripProgressBarAddFs = new System.Windows.Forms.ToolStripProgressBar();
        this.LblStatusAddFs = new System.Windows.Forms.ToolStripLabel();
        this.toolStripBtnHide = new System.Windows.Forms.ToolStripButton();
        this.BtnAddTarSvn = new System.Windows.Forms.ToolStripButton();
        this.TbCtrlPkgFlags = new System.Windows.Forms.TabControl();
        this.TbPgPkgFlags = new System.Windows.Forms.TabPage();
        this.DtGridPkgFlag = new System.Windows.Forms.DataGridView();
        this.ClmRepo = new System.Windows.Forms.DataGridViewTextBoxColumn();
        this.CtxMenuSetAll = new System.Windows.Forms.ContextMenuStrip(this.components);
        this.toolStripTextBox1 = new System.Windows.Forms.ToolStripTextBox();
        this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
        this.defaultToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.enableToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.disableToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.ClmBuildi586 = new System.Windows.Forms.DataGridViewCheckBoxColumn();
        this.ClmPublishi586 = new System.Windows.Forms.DataGridViewCheckBoxColumn();
        this.ClmBuildx86_64 = new System.Windows.Forms.DataGridViewCheckBoxColumn();
        this.ClmPublishx86_64 = new System.Windows.Forms.DataGridViewCheckBoxColumn();
        this.toolStrip1 = new System.Windows.Forms.ToolStrip();
        this.BtnRefreshFlags = new System.Windows.Forms.ToolStripButton();
        this.BtnEditMetaPkg = new System.Windows.Forms.ToolStripButton();
        this.BtnWritePkgMeta = new System.Windows.Forms.ToolStripButton();
        this.toolStripBtnHideFlags = new System.Windows.Forms.ToolStripButton();
        this.TbCtrlBuildStatus = new System.Windows.Forms.TabControl();
        this.TbPgBuildStatus = new System.Windows.Forms.TabPage();
        this.tableLayoutPaneBuildStatus = new System.Windows.Forms.TableLayoutPanel();
        this.toolStripContainer2 = new System.Windows.Forms.ToolStripContainer();
        this.toolStrip2 = new System.Windows.Forms.ToolStrip();
        this.BtnRebuild = new System.Windows.Forms.ToolStripButton();
        this.BtnWipePkg = new System.Windows.Forms.ToolStripButton();
        this.BtnRefresAll = new System.Windows.Forms.ToolStripButton();
        this.backgroundWorkerPkgFlags = new System.ComponentModel.BackgroundWorker();
        this.backgroundWorkerBuildStatus = new System.ComponentModel.BackgroundWorker();
        this.backgroundWorkerFsList = new System.ComponentModel.BackgroundWorker();
        this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
        this.backgroundWorkerAddFs = new System.ComponentModel.BackgroundWorker();
        this.imageList1 = new System.Windows.Forms.ImageList(this.components);
        this.splitContainerBuildPkg.Panel1.SuspendLayout();
        this.splitContainerBuildPkg.Panel2.SuspendLayout();
        this.splitContainerBuildPkg.SuspendLayout();
        this.splitContainerFsFlags.Panel1.SuspendLayout();
        this.splitContainerFsFlags.Panel2.SuspendLayout();
        this.splitContainerFsFlags.SuspendLayout();
        this.TbCtrlFsList.SuspendLayout();
        this.TbPgFsList.SuspendLayout();
        this.toolStripContainer1.ContentPanel.SuspendLayout();
        this.toolStripContainer1.TopToolStripPanel.SuspendLayout();
        this.toolStripContainer1.SuspendLayout();
        this.toolStripFsList.SuspendLayout();
        this.TbCtrlPkgFlags.SuspendLayout();
        this.TbPgPkgFlags.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)(this.DtGridPkgFlag)).BeginInit();
        this.CtxMenuSetAll.SuspendLayout();
        this.toolStrip1.SuspendLayout();
        this.TbCtrlBuildStatus.SuspendLayout();
        this.TbPgBuildStatus.SuspendLayout();
        this.toolStripContainer2.ContentPanel.SuspendLayout();
        this.toolStripContainer2.RightToolStripPanel.SuspendLayout();
        this.toolStripContainer2.SuspendLayout();
        this.toolStrip2.SuspendLayout();
        this.SuspendLayout();
        //
        // splitContainerBuildPkg
        //
        this.splitContainerBuildPkg.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainerBuildPkg.Location = new System.Drawing.Point(0, 0);
        this.splitContainerBuildPkg.Name = "splitContainerBuildPkg";
        //
        // splitContainerBuildPkg.Panel1
        //
        this.splitContainerBuildPkg.Panel1.AutoScroll = true;
        this.splitContainerBuildPkg.Panel1.Controls.Add(this.splitContainerFsFlags);
        //
        // splitContainerBuildPkg.Panel2
        //
        this.splitContainerBuildPkg.Panel2.Controls.Add(this.TbCtrlBuildStatus);
        this.splitContainerBuildPkg.Size = new System.Drawing.Size(792, 489);
        this.splitContainerBuildPkg.SplitterDistance = 422;
        this.splitContainerBuildPkg.TabIndex = 0;
        this.splitContainerBuildPkg.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.splitContainerFsFlags_MouseDoubleClick);
        //
        // splitContainerFsFlags
        //
        this.splitContainerFsFlags.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainerFsFlags.Location = new System.Drawing.Point(0, 0);
        this.splitContainerFsFlags.Name = "splitContainerFsFlags";
        this.splitContainerFsFlags.Orientation = System.Windows.Forms.Orientation.Horizontal;
        //
        // splitContainerFsFlags.Panel1
        //
        this.splitContainerFsFlags.Panel1.Controls.Add(this.TbCtrlFsList);
        //
        // splitContainerFsFlags.Panel2
        //
        this.splitContainerFsFlags.Panel2.Controls.Add(this.TbCtrlPkgFlags);
        this.splitContainerFsFlags.Size = new System.Drawing.Size(422, 489);
        this.splitContainerFsFlags.SplitterDistance = 224;
        this.splitContainerFsFlags.TabIndex = 0;
        this.splitContainerFsFlags.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.splitContainerFsFlags_MouseDoubleClick);
        //
        // TbCtrlFsList
        //
        this.TbCtrlFsList.Controls.Add(this.TbPgFsList);
        this.TbCtrlFsList.Dock = System.Windows.Forms.DockStyle.Fill;
        this.TbCtrlFsList.Location = new System.Drawing.Point(0, 0);
        this.TbCtrlFsList.Name = "TbCtrlFsList";
        this.TbCtrlFsList.SelectedIndex = 0;
        this.TbCtrlFsList.Size = new System.Drawing.Size(422, 224);
        this.TbCtrlFsList.TabIndex = 0;
        //
        // TbPgFsList
        //
        this.TbPgFsList.AutoScroll = true;
        this.TbPgFsList.Controls.Add(this.toolStripContainer1);
        this.TbPgFsList.Location = new System.Drawing.Point(4, 22);
        this.TbPgFsList.Name = "TbPgFsList";
        this.TbPgFsList.Padding = new System.Windows.Forms.Padding(3);
        this.TbPgFsList.Size = new System.Drawing.Size(414, 198);
        this.TbPgFsList.TabIndex = 1;
        this.TbPgFsList.Text = "Files list";
        this.TbPgFsList.UseVisualStyleBackColor = true;
        //
        // toolStripContainer1
        //
        //
        // toolStripContainer1.ContentPanel
        //
        this.toolStripContainer1.ContentPanel.Controls.Add(this.PanelFsList);
        this.toolStripContainer1.ContentPanel.Size = new System.Drawing.Size(408, 163);
        this.toolStripContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.toolStripContainer1.Location = new System.Drawing.Point(3, 3);
        this.toolStripContainer1.Name = "toolStripContainer1";
        this.toolStripContainer1.Size = new System.Drawing.Size(408, 192);
        this.toolStripContainer1.TabIndex = 1;
        this.toolStripContainer1.Text = "toolStripContainer1";
        //
        // toolStripContainer1.TopToolStripPanel
        //
        this.toolStripContainer1.TopToolStripPanel.Controls.Add(this.toolStripFsList);
        //
        // PanelFsList
        //
        this.PanelFsList.AutoScroll = true;
        this.PanelFsList.Dock = System.Windows.Forms.DockStyle.Fill;
        this.PanelFsList.Location = new System.Drawing.Point(0, 0);
        this.PanelFsList.Name = "PanelFsList";
        this.PanelFsList.Size = new System.Drawing.Size(408, 163);
        this.PanelFsList.TabIndex = 0;
        //
        // toolStripFsList
        //
        this.toolStripFsList.Dock = System.Windows.Forms.DockStyle.None;
        this.toolStripFsList.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.BtnRefresh,
            this.BtnAddFs,
            this.toolStripProgressBarAddFs,
            this.LblStatusAddFs,
            this.toolStripBtnHide,
            this.BtnAddTarSvn
        });
        this.toolStripFsList.Location = new System.Drawing.Point(0, 0);
        this.toolStripFsList.Name = "toolStripFsList";
        this.toolStripFsList.Size = new System.Drawing.Size(408, 29);
        this.toolStripFsList.Stretch = true;
        this.toolStripFsList.TabIndex = 7;
        //
        // BtnRefresh
        //
        this.BtnRefresh.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnRefresh.Image = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefresh.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnRefresh.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnRefresh.Name = "BtnRefresh";
        this.BtnRefresh.Size = new System.Drawing.Size(26, 26);
        this.BtnRefresh.Text = "Refresh the list of the file(s)";
        this.BtnRefresh.MouseLeave += new System.EventHandler(this.BtnRefresh_MouseLeave);
        this.BtnRefresh.MouseEnter += new System.EventHandler(this.BtnRefresh_MouseEnter);
        this.BtnRefresh.Click += new System.EventHandler(this.BtnRefresh_Click);
        //
        // BtnAddFs
        //
        this.BtnAddFs.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnAddFs.Image = global::MonoOSC.Properties.Resources.Add;
        this.BtnAddFs.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnAddFs.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnAddFs.Name = "BtnAddFs";
        this.BtnAddFs.Size = new System.Drawing.Size(26, 26);
        this.BtnAddFs.Text = "toolStripButton1";
        this.BtnAddFs.ToolTipText = "Add a new file";
        this.BtnAddFs.MouseLeave += new System.EventHandler(this.BtnRefresh_MouseLeave);
        this.BtnAddFs.MouseEnter += new System.EventHandler(this.BtnRefresh_MouseEnter);
        this.BtnAddFs.Click += new System.EventHandler(this.BtnAddFs_Click);
        //
        // toolStripProgressBarAddFs
        //
        this.toolStripProgressBarAddFs.ForeColor = System.Drawing.Color.Lime;
        this.toolStripProgressBarAddFs.Name = "toolStripProgressBarAddFs";
        this.toolStripProgressBarAddFs.Overflow = System.Windows.Forms.ToolStripItemOverflow.Never;
        this.toolStripProgressBarAddFs.Size = new System.Drawing.Size(100, 26);
        this.toolStripProgressBarAddFs.Style = System.Windows.Forms.ProgressBarStyle.Marquee;
        this.toolStripProgressBarAddFs.Visible = false;
        //
        // LblStatusAddFs
        //
        this.LblStatusAddFs.Name = "LblStatusAddFs";
        this.LblStatusAddFs.Size = new System.Drawing.Size(106, 26);
        this.LblStatusAddFs.Text = "                                 ";
        //
        // toolStripBtnHide
        //
        this.toolStripBtnHide.Alignment = System.Windows.Forms.ToolStripItemAlignment.Right;
        this.toolStripBtnHide.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.toolStripBtnHide.Image = global::MonoOSC.Properties.Resources.Hide;
        this.toolStripBtnHide.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.toolStripBtnHide.Name = "toolStripBtnHide";
        this.toolStripBtnHide.Size = new System.Drawing.Size(23, 26);
        this.toolStripBtnHide.Text = "Hide or show the panel";
        this.toolStripBtnHide.Click += new System.EventHandler(this.toolStripBtnHide_Click);
        //
        // BtnAddTarSvn
        //
        this.BtnAddTarSvn.Image = ((System.Drawing.Image)(resources.GetObject("BtnAddTarSvn.Image")));
        this.BtnAddTarSvn.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnAddTarSvn.Name = "BtnAddTarSvn";
        this.BtnAddTarSvn.Size = new System.Drawing.Size(93, 26);
        this.BtnAddTarSvn.Text = "Add from SVN";
        this.BtnAddTarSvn.Click += new System.EventHandler(this.BtnAddTarSvn_Click);
        //
        // TbCtrlPkgFlags
        //
        this.TbCtrlPkgFlags.Controls.Add(this.TbPgPkgFlags);
        this.TbCtrlPkgFlags.Dock = System.Windows.Forms.DockStyle.Fill;
        this.TbCtrlPkgFlags.Location = new System.Drawing.Point(0, 0);
        this.TbCtrlPkgFlags.Name = "TbCtrlPkgFlags";
        this.TbCtrlPkgFlags.SelectedIndex = 0;
        this.TbCtrlPkgFlags.Size = new System.Drawing.Size(422, 261);
        this.TbCtrlPkgFlags.TabIndex = 1;
        //
        // TbPgPkgFlags
        //
        this.TbPgPkgFlags.Controls.Add(this.DtGridPkgFlag);
        this.TbPgPkgFlags.Controls.Add(this.toolStrip1);
        this.TbPgPkgFlags.Location = new System.Drawing.Point(4, 22);
        this.TbPgPkgFlags.Name = "TbPgPkgFlags";
        this.TbPgPkgFlags.Padding = new System.Windows.Forms.Padding(3);
        this.TbPgPkgFlags.Size = new System.Drawing.Size(414, 235);
        this.TbPgPkgFlags.TabIndex = 1;
        this.TbPgPkgFlags.Text = "Pakage flags";
        this.TbPgPkgFlags.UseVisualStyleBackColor = true;
        //
        // DtGridPkgFlag
        //
        this.DtGridPkgFlag.AllowUserToAddRows = false;
        this.DtGridPkgFlag.AllowUserToDeleteRows = false;
        this.DtGridPkgFlag.AllowUserToOrderColumns = true;
        this.DtGridPkgFlag.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        this.DtGridPkgFlag.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[]
        {
            this.ClmRepo,
            this.ClmBuildi586,
            this.ClmPublishi586,
            this.ClmBuildx86_64,
            this.ClmPublishx86_64
        });
        this.DtGridPkgFlag.ContextMenuStrip = this.CtxMenuSetAll;
        this.DtGridPkgFlag.Dock = System.Windows.Forms.DockStyle.Fill;
        this.DtGridPkgFlag.Location = new System.Drawing.Point(3, 34);
        this.DtGridPkgFlag.Name = "DtGridPkgFlag";
        this.DtGridPkgFlag.Size = new System.Drawing.Size(408, 198);
        this.DtGridPkgFlag.TabIndex = 0;
        this.DtGridPkgFlag.CellMouseMove += new System.Windows.Forms.DataGridViewCellMouseEventHandler(this.DtGridPkgFlag_CellMouseMove);
        this.DtGridPkgFlag.CellContentClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.DtGridPkgFlag_CellContentClick);
        //
        // ClmRepo
        //
        this.ClmRepo.ContextMenuStrip = this.CtxMenuSetAll;
        dataGridViewCellStyle1.BackColor = System.Drawing.Color.Bisque;
        dataGridViewCellStyle1.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.ClmRepo.DefaultCellStyle = dataGridViewCellStyle1;
        this.ClmRepo.Frozen = true;
        this.ClmRepo.HeaderText = "Repository";
        this.ClmRepo.Name = "ClmRepo";
        this.ClmRepo.ReadOnly = true;
        this.ClmRepo.Width = 182;
        //
        // CtxMenuSetAll
        //
        this.CtxMenuSetAll.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.toolStripTextBox1,
            this.toolStripSeparator1,
            this.defaultToolStripMenuItem,
            this.enableToolStripMenuItem,
            this.disableToolStripMenuItem
        });
        this.CtxMenuSetAll.Name = "CtxMenuSetAll";
        this.CtxMenuSetAll.Size = new System.Drawing.Size(211, 99);
        this.CtxMenuSetAll.Text = "Set all item of this column";
        //
        // toolStripTextBox1
        //
        this.toolStripTextBox1.AutoToolTip = true;
        this.toolStripTextBox1.Name = "toolStripTextBox1";
        this.toolStripTextBox1.ReadOnly = true;
        this.toolStripTextBox1.Size = new System.Drawing.Size(150, 21);
        this.toolStripTextBox1.Text = "Set all item of this column to :";
        this.toolStripTextBox1.ToolTipText = "Set all item of this column to :";
        //
        // toolStripSeparator1
        //
        this.toolStripSeparator1.Name = "toolStripSeparator1";
        this.toolStripSeparator1.Size = new System.Drawing.Size(207, 6);
        //
        // defaultToolStripMenuItem
        //
        this.defaultToolStripMenuItem.Name = "defaultToolStripMenuItem";
        this.defaultToolStripMenuItem.Size = new System.Drawing.Size(210, 22);
        this.defaultToolStripMenuItem.Text = "Default";
        this.defaultToolStripMenuItem.Click += new System.EventHandler(this.defaultToolStripMenuItem_Click);
        //
        // enableToolStripMenuItem
        //
        this.enableToolStripMenuItem.Name = "enableToolStripMenuItem";
        this.enableToolStripMenuItem.Size = new System.Drawing.Size(210, 22);
        this.enableToolStripMenuItem.Text = "Enable";
        this.enableToolStripMenuItem.Click += new System.EventHandler(this.enableToolStripMenuItem_Click);
        //
        // disableToolStripMenuItem
        //
        this.disableToolStripMenuItem.Name = "disableToolStripMenuItem";
        this.disableToolStripMenuItem.Size = new System.Drawing.Size(210, 22);
        this.disableToolStripMenuItem.Text = "Disable";
        this.disableToolStripMenuItem.Click += new System.EventHandler(this.disableToolStripMenuItem_Click);
        //
        // ClmBuildi586
        //
        this.ClmBuildi586.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.ColumnHeader;
        this.ClmBuildi586.ContextMenuStrip = this.CtxMenuSetAll;
        this.ClmBuildi586.HeaderText = "Build i586";
        this.ClmBuildi586.Name = "ClmBuildi586";
        this.ClmBuildi586.ThreeState = true;
        this.ClmBuildi586.Width = 53;
        //
        // ClmPublishi586
        //
        this.ClmPublishi586.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.ColumnHeader;
        this.ClmPublishi586.ContextMenuStrip = this.CtxMenuSetAll;
        this.ClmPublishi586.HeaderText = "Publish i586";
        this.ClmPublishi586.Name = "ClmPublishi586";
        this.ClmPublishi586.ThreeState = true;
        this.ClmPublishi586.Width = 63;
        //
        // ClmBuildx86_64
        //
        this.ClmBuildx86_64.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.ColumnHeader;
        this.ClmBuildx86_64.ContextMenuStrip = this.CtxMenuSetAll;
        this.ClmBuildx86_64.HeaderText = "Build x86_64";
        this.ClmBuildx86_64.Name = "ClmBuildx86_64";
        this.ClmBuildx86_64.ThreeState = true;
        this.ClmBuildx86_64.Width = 67;
        //
        // ClmPublishx86_64
        //
        this.ClmPublishx86_64.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.ColumnHeader;
        this.ClmPublishx86_64.ContextMenuStrip = this.CtxMenuSetAll;
        this.ClmPublishx86_64.HeaderText = "Publish x86_64";
        this.ClmPublishx86_64.Name = "ClmPublishx86_64";
        this.ClmPublishx86_64.ThreeState = true;
        this.ClmPublishx86_64.Width = 77;
        //
        // toolStrip1
        //
        this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.BtnRefreshFlags,
            this.BtnEditMetaPkg,
            this.BtnWritePkgMeta,
            this.toolStripBtnHideFlags
        });
        this.toolStrip1.Location = new System.Drawing.Point(3, 3);
        this.toolStrip1.Name = "toolStrip1";
        this.toolStrip1.Size = new System.Drawing.Size(408, 31);
        this.toolStrip1.TabIndex = 0;
        this.toolStrip1.Text = "toolStrip1";
        //
        // BtnRefreshFlags
        //
        this.BtnRefreshFlags.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnRefreshFlags.Image = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefreshFlags.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnRefreshFlags.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnRefreshFlags.Name = "BtnRefreshFlags";
        this.BtnRefreshFlags.Size = new System.Drawing.Size(26, 28);
        this.BtnRefreshFlags.Text = "Refresh the list of the flag";
        this.BtnRefreshFlags.ToolTipText = "Refresh the list of the flag";
        this.BtnRefreshFlags.Click += new System.EventHandler(this.BtnRefreshFlags_Click);
        //
        // BtnEditMetaPkg
        //
        this.BtnEditMetaPkg.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnEditMetaPkg.Image = global::MonoOSC.Properties.Resources.EditXml;
        this.BtnEditMetaPkg.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnEditMetaPkg.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnEditMetaPkg.Name = "BtnEditMetaPkg";
        this.BtnEditMetaPkg.Size = new System.Drawing.Size(26, 28);
        this.BtnEditMetaPkg.ToolTipText = "Edit Meta package manualy";
        this.BtnEditMetaPkg.Click += new System.EventHandler(this.BtnEditMetaPkg_Click);
        //
        // BtnWritePkgMeta
        //
        this.BtnWritePkgMeta.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnWritePkgMeta.Image = global::MonoOSC.Properties.Resources.documentsave;
        this.BtnWritePkgMeta.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnWritePkgMeta.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnWritePkgMeta.Name = "BtnWritePkgMeta";
        this.BtnWritePkgMeta.Size = new System.Drawing.Size(28, 28);
        this.BtnWritePkgMeta.Text = "&Save";
        this.BtnWritePkgMeta.ToolTipText = "Write meta data of the package";
        this.BtnWritePkgMeta.Click += new System.EventHandler(this.BtnWritePkgMeta_Click);
        //
        // toolStripBtnHideFlags
        //
        this.toolStripBtnHideFlags.Alignment = System.Windows.Forms.ToolStripItemAlignment.Right;
        this.toolStripBtnHideFlags.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.toolStripBtnHideFlags.Image = global::MonoOSC.Properties.Resources.Hide;
        this.toolStripBtnHideFlags.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.toolStripBtnHideFlags.Name = "toolStripBtnHideFlags";
        this.toolStripBtnHideFlags.Size = new System.Drawing.Size(23, 28);
        this.toolStripBtnHideFlags.Text = "Hide or show the panel";
        this.toolStripBtnHideFlags.Click += new System.EventHandler(this.toolStripBtnHideFlags_Click);
        //
        // TbCtrlBuildStatus
        //
        this.TbCtrlBuildStatus.Controls.Add(this.TbPgBuildStatus);
        this.TbCtrlBuildStatus.Dock = System.Windows.Forms.DockStyle.Fill;
        this.TbCtrlBuildStatus.Location = new System.Drawing.Point(0, 0);
        this.TbCtrlBuildStatus.Name = "TbCtrlBuildStatus";
        this.TbCtrlBuildStatus.SelectedIndex = 0;
        this.TbCtrlBuildStatus.Size = new System.Drawing.Size(366, 489);
        this.TbCtrlBuildStatus.TabIndex = 1;
        //
        // TbPgBuildStatus
        //
        this.TbPgBuildStatus.AutoScroll = true;
        this.TbPgBuildStatus.BackColor = System.Drawing.SystemColors.GradientActiveCaption;
        this.TbPgBuildStatus.Controls.Add(this.tableLayoutPaneBuildStatus);
        this.TbPgBuildStatus.Location = new System.Drawing.Point(4, 22);
        this.TbPgBuildStatus.Name = "TbPgBuildStatus";
        this.TbPgBuildStatus.Padding = new System.Windows.Forms.Padding(3);
        this.TbPgBuildStatus.Size = new System.Drawing.Size(358, 463);
        this.TbPgBuildStatus.TabIndex = 1;
        this.TbPgBuildStatus.Text = "Build Status";
        //
        // tableLayoutPaneBuildStatus
        //
        this.tableLayoutPaneBuildStatus.ColumnCount = 1;
        this.tableLayoutPaneBuildStatus.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 364F));
        this.tableLayoutPaneBuildStatus.Dock = System.Windows.Forms.DockStyle.Top;
        this.tableLayoutPaneBuildStatus.Location = new System.Drawing.Point(3, 3);
        this.tableLayoutPaneBuildStatus.Name = "tableLayoutPaneBuildStatus";
        this.tableLayoutPaneBuildStatus.RowCount = 1;
        this.tableLayoutPaneBuildStatus.RowStyles.Add(new System.Windows.Forms.RowStyle());
        this.tableLayoutPaneBuildStatus.Size = new System.Drawing.Size(352, 259);
        this.tableLayoutPaneBuildStatus.TabIndex = 0;
        //
        // toolStripContainer2
        //
        //
        // toolStripContainer2.ContentPanel
        //
        this.toolStripContainer2.ContentPanel.Controls.Add(this.splitContainerBuildPkg);
        this.toolStripContainer2.ContentPanel.Size = new System.Drawing.Size(792, 489);
        this.toolStripContainer2.Dock = System.Windows.Forms.DockStyle.Fill;
        this.toolStripContainer2.LeftToolStripPanelVisible = false;
        this.toolStripContainer2.Location = new System.Drawing.Point(0, 0);
        this.toolStripContainer2.Name = "toolStripContainer2";
        //
        // toolStripContainer2.RightToolStripPanel
        //
        this.toolStripContainer2.RightToolStripPanel.Controls.Add(this.toolStrip2);
        this.toolStripContainer2.Size = new System.Drawing.Size(823, 489);
        this.toolStripContainer2.TabIndex = 1;
        this.toolStripContainer2.Text = "toolStripContainer2";
        this.toolStripContainer2.TopToolStripPanelVisible = false;
        //
        // toolStrip2
        //
        this.toolStrip2.AllowMerge = false;
        this.toolStrip2.Anchor = System.Windows.Forms.AnchorStyles.Right;
        this.toolStrip2.Dock = System.Windows.Forms.DockStyle.None;
        this.toolStrip2.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.BtnRebuild,
            this.BtnWipePkg,
            this.BtnRefresAll
        });
        this.toolStrip2.Location = new System.Drawing.Point(0, 3);
        this.toolStrip2.Name = "toolStrip2";
        this.toolStrip2.Padding = new System.Windows.Forms.Padding(0);
        this.toolStrip2.RenderMode = System.Windows.Forms.ToolStripRenderMode.Professional;
        this.toolStrip2.Size = new System.Drawing.Size(31, 115);
        this.toolStrip2.TabIndex = 0;
        this.toolStrip2.TabStop = true;
        this.toolStrip2.TextDirection = System.Windows.Forms.ToolStripTextDirection.Vertical270;
        //
        // BtnRebuild
        //
        this.BtnRebuild.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnRebuild.Image = global::MonoOSC.Properties.Resources.Rebuild;
        this.BtnRebuild.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnRebuild.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnRebuild.Name = "BtnRebuild";
        this.BtnRebuild.Size = new System.Drawing.Size(30, 26);
        this.BtnRebuild.Text = "toolStripButton1";
        this.BtnRebuild.ToolTipText = "Rebuild all repository of this package";
        this.BtnRebuild.MouseLeave += new System.EventHandler(this.BtnRefresh_MouseLeave);
        this.BtnRebuild.MouseEnter += new System.EventHandler(this.BtnRefresh_MouseEnter);
        this.BtnRebuild.Click += new System.EventHandler(this.BtnRebuild_Click);
        //
        // BtnWipePkg
        //
        this.BtnWipePkg.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnWipePkg.Image = global::MonoOSC.Properties.Resources.Wipe;
        this.BtnWipePkg.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnWipePkg.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnWipePkg.Name = "BtnWipePkg";
        this.BtnWipePkg.Size = new System.Drawing.Size(30, 26);
        this.BtnWipePkg.Text = "Delete/Wipe all dead file(s) of this package";
        this.BtnWipePkg.MouseLeave += new System.EventHandler(this.BtnRefresh_MouseLeave);
        this.BtnWipePkg.MouseEnter += new System.EventHandler(this.BtnRefresh_MouseEnter);
        this.BtnWipePkg.Click += new System.EventHandler(this.BtnWipePkg_Click);
        //
        // BtnRefresAll
        //
        this.BtnRefresAll.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnRefresAll.Image = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefresAll.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnRefresAll.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnRefresAll.Name = "BtnRefresAll";
        this.BtnRefresAll.Size = new System.Drawing.Size(30, 26);
        this.BtnRefresAll.Text = "Refresh all the build\'s";
        this.BtnRefresAll.MouseLeave += new System.EventHandler(this.BtnRefresh_MouseLeave);
        this.BtnRefresAll.MouseEnter += new System.EventHandler(this.BtnRefresh_MouseEnter);
        this.BtnRefresAll.Click += new System.EventHandler(this.BtnRefresAll_Click);
        //
        // backgroundWorkerPkgFlags
        //
        this.backgroundWorkerPkgFlags.WorkerReportsProgress = true;
        this.backgroundWorkerPkgFlags.WorkerSupportsCancellation = true;
        this.backgroundWorkerPkgFlags.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorkerPkgFlags_DoWork);
        this.backgroundWorkerPkgFlags.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorkerPkgFlags_RunWorkerCompleted);
        this.backgroundWorkerPkgFlags.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorkerPkgFlags_ProgressChanged);
        //
        // backgroundWorkerBuildStatus
        //
        this.backgroundWorkerBuildStatus.WorkerReportsProgress = true;
        this.backgroundWorkerBuildStatus.WorkerSupportsCancellation = true;
        this.backgroundWorkerBuildStatus.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorkerBuildStatus_DoWork);
        this.backgroundWorkerBuildStatus.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorkerBuildStatus_RunWorkerCompleted);
        this.backgroundWorkerBuildStatus.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorkerBuildStatus_ProgressChanged);
        //
        // backgroundWorkerFsList
        //
        this.backgroundWorkerFsList.WorkerReportsProgress = true;
        this.backgroundWorkerFsList.WorkerSupportsCancellation = true;
        this.backgroundWorkerFsList.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorkerFsList_DoWork);
        this.backgroundWorkerFsList.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorkerFsList_RunWorkerCompleted);
        this.backgroundWorkerFsList.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorkerFsList_ProgressChanged);
        //
        // openFileDialog1
        //
        this.openFileDialog1.Filter = "All File|*.*|Spec|*.spec|Archives|*.tar;*.tar.gz;*.gz;*.tar.bz2;*.zip;*.rar|XML|*" +
                                      ".xml;*.xsd;*.xdt|Changelog|*.changelog|control|*.control|Rules|*.rules|DSC|*.dsc" +
                                      "";
        this.openFileDialog1.Multiselect = true;
        this.openFileDialog1.ReadOnlyChecked = true;
        this.openFileDialog1.RestoreDirectory = true;
        this.openFileDialog1.ShowReadOnly = true;
        this.openFileDialog1.SupportMultiDottedExtensions = true;
        this.openFileDialog1.Title = "Open file(s) to upload on the server";
        //
        // backgroundWorkerAddFs
        //
        this.backgroundWorkerAddFs.WorkerReportsProgress = true;
        this.backgroundWorkerAddFs.WorkerSupportsCancellation = true;
        this.backgroundWorkerAddFs.DoWork += new System.ComponentModel.DoWorkEventHandler(this.backgroundWorkerAddFs_DoWork);
        this.backgroundWorkerAddFs.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.backgroundWorkerAddFs_RunWorkerCompleted);
        this.backgroundWorkerAddFs.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.backgroundWorkerAddFs_ProgressChanged);
        //
        // imageList1
        //
        this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
        this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
        this.imageList1.Images.SetKeyName(0, "Hide.bmp");
        this.imageList1.Images.SetKeyName(1, "Show.bmp");
        //
        // BuildPkg
        //
        this.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.Controls.Add(this.toolStripContainer2);
        this.DoubleBuffered = true;
        this.Name = "BuildPkg";
        this.Size = new System.Drawing.Size(823, 489);
        this.Disposed += new System.EventHandler(this.BuildPkg_Disposed);
        this.VisibleChanged += new System.EventHandler(this.BuildPkg_VisibleChanged);
        this.ControlRemoved += new System.Windows.Forms.ControlEventHandler(this.BuildPkg_ControlRemoved);
        this.splitContainerBuildPkg.Panel1.ResumeLayout(false);
        this.splitContainerBuildPkg.Panel2.ResumeLayout(false);
        this.splitContainerBuildPkg.ResumeLayout(false);
        this.splitContainerFsFlags.Panel1.ResumeLayout(false);
        this.splitContainerFsFlags.Panel2.ResumeLayout(false);
        this.splitContainerFsFlags.ResumeLayout(false);
        this.TbCtrlFsList.ResumeLayout(false);
        this.TbPgFsList.ResumeLayout(false);
        this.toolStripContainer1.ContentPanel.ResumeLayout(false);
        this.toolStripContainer1.TopToolStripPanel.ResumeLayout(false);
        this.toolStripContainer1.TopToolStripPanel.PerformLayout();
        this.toolStripContainer1.ResumeLayout(false);
        this.toolStripContainer1.PerformLayout();
        this.toolStripFsList.ResumeLayout(false);
        this.toolStripFsList.PerformLayout();
        this.TbCtrlPkgFlags.ResumeLayout(false);
        this.TbPgPkgFlags.ResumeLayout(false);
        this.TbPgPkgFlags.PerformLayout();
        ((System.ComponentModel.ISupportInitialize)(this.DtGridPkgFlag)).EndInit();
        this.CtxMenuSetAll.ResumeLayout(false);
        this.CtxMenuSetAll.PerformLayout();
        this.toolStrip1.ResumeLayout(false);
        this.toolStrip1.PerformLayout();
        this.TbCtrlBuildStatus.ResumeLayout(false);
        this.TbPgBuildStatus.ResumeLayout(false);
        this.toolStripContainer2.ContentPanel.ResumeLayout(false);
        this.toolStripContainer2.RightToolStripPanel.ResumeLayout(false);
        this.toolStripContainer2.RightToolStripPanel.PerformLayout();
        this.toolStripContainer2.ResumeLayout(false);
        this.toolStripContainer2.PerformLayout();
        this.toolStrip2.ResumeLayout(false);
        this.toolStrip2.PerformLayout();
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.SplitContainer splitContainerBuildPkg;
    private System.Windows.Forms.SplitContainer splitContainerFsFlags;
    private System.Windows.Forms.TabControl TbCtrlFsList;
    private System.Windows.Forms.TabPage TbPgFsList;
    private System.Windows.Forms.TabControl TbCtrlPkgFlags;
    private System.Windows.Forms.TabPage TbPgPkgFlags;
    private System.Windows.Forms.TabControl TbCtrlBuildStatus;
    private System.Windows.Forms.TabPage TbPgBuildStatus;
    private System.ComponentModel.BackgroundWorker backgroundWorkerPkgFlags;
    private System.ComponentModel.BackgroundWorker backgroundWorkerBuildStatus;
    private System.ComponentModel.BackgroundWorker backgroundWorkerFsList;
    private System.Windows.Forms.ToolStrip toolStripFsList;
    private System.Windows.Forms.ToolStripButton BtnRefresh;
    private System.Windows.Forms.ToolStripContainer toolStripContainer1;
    private System.Windows.Forms.Panel PanelFsList;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPaneBuildStatus;
    private System.Windows.Forms.ToolStripButton BtnAddFs;
    private System.Windows.Forms.OpenFileDialog openFileDialog1;
    private System.ComponentModel.BackgroundWorker backgroundWorkerAddFs;
    private System.Windows.Forms.ToolStripLabel LblStatusAddFs;
    private System.Windows.Forms.ToolStripProgressBar toolStripProgressBarAddFs;
    private System.Windows.Forms.DataGridView DtGridPkgFlag;
    private System.Windows.Forms.ToolStrip toolStrip1;
    private System.Windows.Forms.ToolStripButton BtnEditMetaPkg;
    private System.Windows.Forms.ToolStripButton BtnRefreshFlags;
    private System.Windows.Forms.ToolStripButton BtnWritePkgMeta;
    private System.Windows.Forms.ToolStrip toolStrip2;
    private System.Windows.Forms.ToolStripButton BtnWipePkg;
    private System.Windows.Forms.ToolStripContainer toolStripContainer2;
    private System.Windows.Forms.ToolStripButton BtnRebuild;
    private System.Windows.Forms.ToolStripButton BtnRefresAll;
    private System.Windows.Forms.ContextMenuStrip CtxMenuSetAll;
    private System.Windows.Forms.ToolStripMenuItem defaultToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem enableToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem disableToolStripMenuItem;
    private System.Windows.Forms.ToolStripTextBox toolStripTextBox1;
    private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
    private System.Windows.Forms.DataGridViewTextBoxColumn ClmRepo;
    private System.Windows.Forms.DataGridViewCheckBoxColumn ClmBuildi586;
    private System.Windows.Forms.DataGridViewCheckBoxColumn ClmPublishi586;
    private System.Windows.Forms.DataGridViewCheckBoxColumn ClmBuildx86_64;
    private System.Windows.Forms.DataGridViewCheckBoxColumn ClmPublishx86_64;
    private System.Windows.Forms.ToolStripButton toolStripBtnHide;
    private System.Windows.Forms.ToolStripButton toolStripBtnHideFlags;
    private System.Windows.Forms.ImageList imageList1;
    private System.Windows.Forms.ToolStripButton BtnAddTarSvn;
}
}
