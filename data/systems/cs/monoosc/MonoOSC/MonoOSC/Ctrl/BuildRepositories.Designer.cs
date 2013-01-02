// BuildRepositories.Designer.cs created with MonoDevelop
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

namespace MonoOSC
{
partial class BuildRepositories
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
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(BuildRepositories));
        this.LblDistro = new System.Windows.Forms.LinkLabel();
        this.LblDistroDescr = new System.Windows.Forms.LinkLabel();
        this.GrpBxName = new System.Windows.Forms.GroupBox();
        this.toolStripContainer1 = new System.Windows.Forms.ToolStripContainer();
        this.LblGotoRepository = new System.Windows.Forms.LinkLabel();
        this.toolStrip2 = new System.Windows.Forms.ToolStrip();
        this.BtnDelRepo = new System.Windows.Forms.ToolStripButton();
        this.BtnRefresh = new System.Windows.Forms.ToolStripButton();
        this.GrpBxI586 = new System.Windows.Forms.GroupBox();
        this.BtnHideI586 = new System.Windows.Forms.Button();
        this.imageList1 = new System.Windows.Forms.ImageList(this.components);
        this.Txti586Other = new System.Windows.Forms.TextBox();
        this.Lbli586Other = new System.Windows.Forms.Label();
        this.LblI586Building = new System.Windows.Forms.LinkLabel();
        this.Lbli586Dis = new System.Windows.Forms.Label();
        this.ChkBxI586Publish = new System.Windows.Forms.CheckBox();
        this.ChkBxI586Build = new System.Windows.Forms.CheckBox();
        this.Lbli586Build = new System.Windows.Forms.Label();
        this.LblI586Succes = new System.Windows.Forms.LinkLabel();
        this.LblI586Disable = new System.Windows.Forms.LinkLabel();
        this.Lbli586Suc = new System.Windows.Forms.Label();
        this.GrpBxX86_64 = new System.Windows.Forms.GroupBox();
        this.BtnHideX64 = new System.Windows.Forms.Button();
        this.TxtX86_64Other = new System.Windows.Forms.TextBox();
        this.LblX86_64Dis = new System.Windows.Forms.Label();
        this.LblX86_64Build = new System.Windows.Forms.Label();
        this.LblX86_64Suc = new System.Windows.Forms.Label();
        this.LblX86_64Building = new System.Windows.Forms.LinkLabel();
        this.ChkBxX86_64Publish = new System.Windows.Forms.CheckBox();
        this.ChkBxX86_64Build = new System.Windows.Forms.CheckBox();
        this.LblX86_64Succes = new System.Windows.Forms.LinkLabel();
        this.LblX86_64Disable = new System.Windows.Forms.LinkLabel();
        this.BckGWStati = new System.ComponentModel.BackgroundWorker();
        this.GrpBxName.SuspendLayout();
        this.toolStripContainer1.ContentPanel.SuspendLayout();
        this.toolStripContainer1.TopToolStripPanel.SuspendLayout();
        this.toolStripContainer1.SuspendLayout();
        this.toolStrip2.SuspendLayout();
        this.GrpBxI586.SuspendLayout();
        this.GrpBxX86_64.SuspendLayout();
        this.SuspendLayout();
        //
        // LblDistro
        //
        this.LblDistro.AutoEllipsis = true;
        this.LblDistro.Font = new System.Drawing.Font("Microsoft Sans Serif", 11.25F, System.Drawing.FontStyle.Bold);
        this.LblDistro.LinkColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(0)))));
        this.LblDistro.Location = new System.Drawing.Point(0, 0);
        this.LblDistro.Name = "LblDistro";
        this.LblDistro.Size = new System.Drawing.Size(250, 40);
        this.LblDistro.TabIndex = 0;
        this.LblDistro.TabStop = true;
        this.LblDistro.Text = "linkLabel1";
        //
        // LblDistroDescr
        //
        this.LblDistroDescr.AutoEllipsis = true;
        this.LblDistroDescr.LinkColor = System.Drawing.Color.SaddleBrown;
        this.LblDistroDescr.Location = new System.Drawing.Point(6, 40);
        this.LblDistroDescr.Name = "LblDistroDescr";
        this.LblDistroDescr.Size = new System.Drawing.Size(244, 43);
        this.LblDistroDescr.TabIndex = 1;
        this.LblDistroDescr.TabStop = true;
        this.LblDistroDescr.Text = "linkLabel1";
        //
        // GrpBxName
        //
        this.GrpBxName.Controls.Add(this.toolStripContainer1);
        this.GrpBxName.Location = new System.Drawing.Point(3, 3);
        this.GrpBxName.Name = "GrpBxName";
        this.GrpBxName.Size = new System.Drawing.Size(270, 162);
        this.GrpBxName.TabIndex = 2;
        this.GrpBxName.TabStop = false;
        this.GrpBxName.Text = "Repository";
        //
        // toolStripContainer1
        //
        //
        // toolStripContainer1.ContentPanel
        //
        this.toolStripContainer1.ContentPanel.Controls.Add(this.LblGotoRepository);
        this.toolStripContainer1.ContentPanel.Controls.Add(this.LblDistro);
        this.toolStripContainer1.ContentPanel.Controls.Add(this.LblDistroDescr);
        this.toolStripContainer1.ContentPanel.Size = new System.Drawing.Size(264, 114);
        this.toolStripContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.toolStripContainer1.Location = new System.Drawing.Point(3, 16);
        this.toolStripContainer1.Name = "toolStripContainer1";
        this.toolStripContainer1.Size = new System.Drawing.Size(264, 143);
        this.toolStripContainer1.TabIndex = 5;
        this.toolStripContainer1.Text = "toolStripContainer1";
        //
        // toolStripContainer1.TopToolStripPanel
        //
        this.toolStripContainer1.TopToolStripPanel.Controls.Add(this.toolStrip2);
        //
        // LblGotoRepository
        //
        this.LblGotoRepository.AutoSize = true;
        this.LblGotoRepository.Location = new System.Drawing.Point(3, 97);
        this.LblGotoRepository.Name = "LblGotoRepository";
        this.LblGotoRepository.Size = new System.Drawing.Size(83, 13);
        this.LblGotoRepository.TabIndex = 2;
        this.LblGotoRepository.TabStop = true;
        this.LblGotoRepository.Tag = "http://download.opensuse.org/repositories/";
        this.LblGotoRepository.Text = "Goto Repository";
        this.LblGotoRepository.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.LblGotoRepository_LinkClicked);
        //
        // toolStrip2
        //
        this.toolStrip2.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                  | System.Windows.Forms.AnchorStyles.Right)));
        this.toolStrip2.AutoSize = false;
        this.toolStrip2.Dock = System.Windows.Forms.DockStyle.None;
        this.toolStrip2.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.BtnDelRepo,
            this.BtnRefresh
        });
        this.toolStrip2.Location = new System.Drawing.Point(3, 0);
        this.toolStrip2.MinimumSize = new System.Drawing.Size(261, 29);
        this.toolStrip2.Name = "toolStrip2";
        this.toolStrip2.Size = new System.Drawing.Size(261, 29);
        this.toolStrip2.TabIndex = 3;
        this.toolStrip2.Text = "toolStrip2";
        //
        // BtnDelRepo
        //
        this.BtnDelRepo.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnDelRepo.Image = global::MonoOSC.Properties.Resources.Del;
        this.BtnDelRepo.ImageScaling = System.Windows.Forms.ToolStripItemImageScaling.None;
        this.BtnDelRepo.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnDelRepo.Name = "BtnDelRepo";
        this.BtnDelRepo.Size = new System.Drawing.Size(26, 26);
        this.BtnDelRepo.ToolTipText = "Del this repository and all build RPM";
        this.BtnDelRepo.Click += new System.EventHandler(this.BtnDelRepo_Click);
        //
        // BtnRefresh
        //
        this.BtnRefresh.Alignment = System.Windows.Forms.ToolStripItemAlignment.Right;
        this.BtnRefresh.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
        this.BtnRefresh.Image = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefresh.ImageTransparentColor = System.Drawing.Color.Magenta;
        this.BtnRefresh.Name = "BtnRefresh";
        this.BtnRefresh.Size = new System.Drawing.Size(23, 26);
        this.BtnRefresh.Text = "Refresh the build status";
        this.BtnRefresh.ToolTipText = "Refresh the build status";
        this.BtnRefresh.Click += new System.EventHandler(this.BtnRefresh_Click);
        //
        // GrpBxI586
        //
        this.GrpBxI586.Controls.Add(this.BtnHideI586);
        this.GrpBxI586.Controls.Add(this.Txti586Other);
        this.GrpBxI586.Controls.Add(this.Lbli586Other);
        this.GrpBxI586.Controls.Add(this.LblI586Building);
        this.GrpBxI586.Controls.Add(this.Lbli586Dis);
        this.GrpBxI586.Controls.Add(this.ChkBxI586Publish);
        this.GrpBxI586.Controls.Add(this.ChkBxI586Build);
        this.GrpBxI586.Controls.Add(this.Lbli586Build);
        this.GrpBxI586.Controls.Add(this.LblI586Succes);
        this.GrpBxI586.Controls.Add(this.LblI586Disable);
        this.GrpBxI586.Controls.Add(this.Lbli586Suc);
        this.GrpBxI586.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.GrpBxI586.Location = new System.Drawing.Point(279, 3);
        this.GrpBxI586.Name = "GrpBxI586";
        this.GrpBxI586.Size = new System.Drawing.Size(174, 162);
        this.GrpBxI586.TabIndex = 3;
        this.GrpBxI586.TabStop = false;
        this.GrpBxI586.Text = "I586";
        this.GrpBxI586.Visible = false;
        //
        // BtnHideI586
        //
        this.BtnHideI586.BackgroundImage = global::MonoOSC.Properties.Resources.Hide;
        this.BtnHideI586.ImageIndex = 0;
        this.BtnHideI586.ImageList = this.imageList1;
        this.BtnHideI586.Location = new System.Drawing.Point(145, 30);
        this.BtnHideI586.Name = "BtnHideI586";
        this.BtnHideI586.Size = new System.Drawing.Size(21, 24);
        this.BtnHideI586.TabIndex = 8;
        this.BtnHideI586.UseVisualStyleBackColor = true;
        this.BtnHideI586.Click += new System.EventHandler(this.BtnHide_Click);
        //
        // imageList1
        //
        this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
        this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
        this.imageList1.Images.SetKeyName(0, "Hide.bmp");
        this.imageList1.Images.SetKeyName(1, "Show.bmp");
        //
        // Txti586Other
        //
        this.Txti586Other.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                    | System.Windows.Forms.AnchorStyles.Right)));
        this.Txti586Other.BackColor = System.Drawing.SystemColors.Highlight;
        this.Txti586Other.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.Txti586Other.ForeColor = System.Drawing.Color.Yellow;
        this.Txti586Other.Location = new System.Drawing.Point(6, 57);
        this.Txti586Other.Multiline = true;
        this.Txti586Other.Name = "Txti586Other";
        this.Txti586Other.ReadOnly = true;
        this.Txti586Other.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
        this.Txti586Other.Size = new System.Drawing.Size(162, 83);
        this.Txti586Other.TabIndex = 14;
        this.Txti586Other.Text = "000 scheduled\r\n000 excluded\r\n000 broken\r\n000 failed\r\n000 expansion error";
        this.Txti586Other.WordWrap = false;
        //
        // Lbli586Other
        //
        this.Lbli586Other.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.Lbli586Other.AutoSize = true;
        this.Lbli586Other.Location = new System.Drawing.Point(42, 72);
        this.Lbli586Other.Name = "Lbli586Other";
        this.Lbli586Other.Size = new System.Drawing.Size(0, 13);
        this.Lbli586Other.TabIndex = 13;
        this.Lbli586Other.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
        //
        // LblI586Building
        //
        this.LblI586Building.AutoSize = true;
        this.LblI586Building.Location = new System.Drawing.Point(6, 28);
        this.LblI586Building.Name = "LblI586Building";
        this.LblI586Building.Size = new System.Drawing.Size(60, 13);
        this.LblI586Building.TabIndex = 5;
        this.LblI586Building.TabStop = true;
        this.LblI586Building.Tag = "i586";
        this.LblI586Building.Text = "Building :";
        //
        // Lbli586Dis
        //
        this.Lbli586Dis.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.Lbli586Dis.AutoSize = true;
        this.Lbli586Dis.Location = new System.Drawing.Point(76, 42);
        this.Lbli586Dis.Name = "Lbli586Dis";
        this.Lbli586Dis.Size = new System.Drawing.Size(28, 13);
        this.Lbli586Dis.TabIndex = 11;
        this.Lbli586Dis.Text = "000";
        this.Lbli586Dis.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
        //
        // ChkBxI586Publish
        //
        this.ChkBxI586Publish.AutoSize = true;
        this.ChkBxI586Publish.BackColor = System.Drawing.Color.Green;
        this.ChkBxI586Publish.Checked = true;
        this.ChkBxI586Publish.CheckState = System.Windows.Forms.CheckState.Checked;
        this.ChkBxI586Publish.Location = new System.Drawing.Point(79, 142);
        this.ChkBxI586Publish.Name = "ChkBxI586Publish";
        this.ChkBxI586Publish.Size = new System.Drawing.Size(67, 17);
        this.ChkBxI586Publish.TabIndex = 4;
        this.ChkBxI586Publish.Tag = "i586";
        this.ChkBxI586Publish.Text = "Publish";
        this.ChkBxI586Publish.ThreeState = true;
        this.ChkBxI586Publish.UseVisualStyleBackColor = false;
        this.ChkBxI586Publish.CheckStateChanged += new System.EventHandler(this.ChkBxS_CheckStateChanged);
        //
        // ChkBxI586Build
        //
        this.ChkBxI586Build.AutoSize = true;
        this.ChkBxI586Build.BackColor = System.Drawing.Color.Green;
        this.ChkBxI586Build.Checked = true;
        this.ChkBxI586Build.CheckState = System.Windows.Forms.CheckState.Checked;
        this.ChkBxI586Build.Location = new System.Drawing.Point(16, 142);
        this.ChkBxI586Build.Name = "ChkBxI586Build";
        this.ChkBxI586Build.Size = new System.Drawing.Size(54, 17);
        this.ChkBxI586Build.TabIndex = 3;
        this.ChkBxI586Build.Tag = "i586";
        this.ChkBxI586Build.Text = "Build";
        this.ChkBxI586Build.ThreeState = true;
        this.ChkBxI586Build.UseVisualStyleBackColor = false;
        this.ChkBxI586Build.CheckStateChanged += new System.EventHandler(this.ChkBxS_CheckStateChanged);
        //
        // Lbli586Build
        //
        this.Lbli586Build.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.Lbli586Build.AutoSize = true;
        this.Lbli586Build.Location = new System.Drawing.Point(76, 28);
        this.Lbli586Build.Name = "Lbli586Build";
        this.Lbli586Build.Size = new System.Drawing.Size(28, 13);
        this.Lbli586Build.TabIndex = 10;
        this.Lbli586Build.Text = "000";
        this.Lbli586Build.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
        //
        // LblI586Succes
        //
        this.LblI586Succes.AutoSize = true;
        this.LblI586Succes.Location = new System.Drawing.Point(6, 13);
        this.LblI586Succes.Name = "LblI586Succes";
        this.LblI586Succes.Size = new System.Drawing.Size(57, 13);
        this.LblI586Succes.TabIndex = 2;
        this.LblI586Succes.TabStop = true;
        this.LblI586Succes.Tag = "i586";
        this.LblI586Succes.Text = "Succes :";
        //
        // LblI586Disable
        //
        this.LblI586Disable.AutoSize = true;
        this.LblI586Disable.Location = new System.Drawing.Point(6, 43);
        this.LblI586Disable.Name = "LblI586Disable";
        this.LblI586Disable.Size = new System.Drawing.Size(57, 13);
        this.LblI586Disable.TabIndex = 1;
        this.LblI586Disable.TabStop = true;
        this.LblI586Disable.Tag = "i586";
        this.LblI586Disable.Text = "Disable :";
        //
        // Lbli586Suc
        //
        this.Lbli586Suc.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.Lbli586Suc.AutoSize = true;
        this.Lbli586Suc.Location = new System.Drawing.Point(76, 13);
        this.Lbli586Suc.Name = "Lbli586Suc";
        this.Lbli586Suc.Size = new System.Drawing.Size(28, 13);
        this.Lbli586Suc.TabIndex = 9;
        this.Lbli586Suc.Text = "000";
        this.Lbli586Suc.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
        //
        // GrpBxX86_64
        //
        this.GrpBxX86_64.Controls.Add(this.BtnHideX64);
        this.GrpBxX86_64.Controls.Add(this.TxtX86_64Other);
        this.GrpBxX86_64.Controls.Add(this.LblX86_64Dis);
        this.GrpBxX86_64.Controls.Add(this.LblX86_64Build);
        this.GrpBxX86_64.Controls.Add(this.LblX86_64Suc);
        this.GrpBxX86_64.Controls.Add(this.LblX86_64Building);
        this.GrpBxX86_64.Controls.Add(this.ChkBxX86_64Publish);
        this.GrpBxX86_64.Controls.Add(this.ChkBxX86_64Build);
        this.GrpBxX86_64.Controls.Add(this.LblX86_64Succes);
        this.GrpBxX86_64.Controls.Add(this.LblX86_64Disable);
        this.GrpBxX86_64.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.GrpBxX86_64.Location = new System.Drawing.Point(453, 3);
        this.GrpBxX86_64.Name = "GrpBxX86_64";
        this.GrpBxX86_64.Size = new System.Drawing.Size(174, 162);
        this.GrpBxX86_64.TabIndex = 4;
        this.GrpBxX86_64.TabStop = false;
        this.GrpBxX86_64.Text = "X86_64";
        this.GrpBxX86_64.Visible = false;
        //
        // BtnHideX64
        //
        this.BtnHideX64.BackgroundImage = global::MonoOSC.Properties.Resources.Hide;
        this.BtnHideX64.ImageIndex = 0;
        this.BtnHideX64.ImageList = this.imageList1;
        this.BtnHideX64.Location = new System.Drawing.Point(144, 30);
        this.BtnHideX64.Name = "BtnHideX64";
        this.BtnHideX64.Size = new System.Drawing.Size(21, 24);
        this.BtnHideX64.TabIndex = 9;
        this.BtnHideX64.UseVisualStyleBackColor = true;
        this.BtnHideX64.Click += new System.EventHandler(this.BtnHide_Click);
        //
        // TxtX86_64Other
        //
        this.TxtX86_64Other.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                      | System.Windows.Forms.AnchorStyles.Right)));
        this.TxtX86_64Other.BackColor = System.Drawing.SystemColors.Highlight;
        this.TxtX86_64Other.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.TxtX86_64Other.ForeColor = System.Drawing.Color.Yellow;
        this.TxtX86_64Other.Location = new System.Drawing.Point(6, 57);
        this.TxtX86_64Other.Multiline = true;
        this.TxtX86_64Other.Name = "TxtX86_64Other";
        this.TxtX86_64Other.ReadOnly = true;
        this.TxtX86_64Other.ScrollBars = System.Windows.Forms.ScrollBars.Vertical;
        this.TxtX86_64Other.Size = new System.Drawing.Size(162, 83);
        this.TxtX86_64Other.TabIndex = 15;
        this.TxtX86_64Other.Text = "000 scheduled\r\n000 excluded\r\n000 broken\r\n000 failed\r\n000 expansion error";
        this.TxtX86_64Other.WordWrap = false;
        //
        // LblX86_64Dis
        //
        this.LblX86_64Dis.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.LblX86_64Dis.AutoSize = true;
        this.LblX86_64Dis.Location = new System.Drawing.Point(74, 42);
        this.LblX86_64Dis.Name = "LblX86_64Dis";
        this.LblX86_64Dis.Size = new System.Drawing.Size(28, 13);
        this.LblX86_64Dis.TabIndex = 8;
        this.LblX86_64Dis.Text = "000";
        this.LblX86_64Dis.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
        //
        // LblX86_64Build
        //
        this.LblX86_64Build.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.LblX86_64Build.AutoSize = true;
        this.LblX86_64Build.Location = new System.Drawing.Point(74, 28);
        this.LblX86_64Build.Name = "LblX86_64Build";
        this.LblX86_64Build.Size = new System.Drawing.Size(28, 13);
        this.LblX86_64Build.TabIndex = 7;
        this.LblX86_64Build.Text = "000";
        this.LblX86_64Build.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
        //
        // LblX86_64Suc
        //
        this.LblX86_64Suc.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.LblX86_64Suc.AutoSize = true;
        this.LblX86_64Suc.Location = new System.Drawing.Point(74, 13);
        this.LblX86_64Suc.Name = "LblX86_64Suc";
        this.LblX86_64Suc.Size = new System.Drawing.Size(28, 13);
        this.LblX86_64Suc.TabIndex = 6;
        this.LblX86_64Suc.Text = "000";
        this.LblX86_64Suc.TextAlign = System.Drawing.ContentAlignment.MiddleRight;
        //
        // LblX86_64Building
        //
        this.LblX86_64Building.AutoSize = true;
        this.LblX86_64Building.Location = new System.Drawing.Point(6, 28);
        this.LblX86_64Building.Name = "LblX86_64Building";
        this.LblX86_64Building.Size = new System.Drawing.Size(60, 13);
        this.LblX86_64Building.TabIndex = 6;
        this.LblX86_64Building.TabStop = true;
        this.LblX86_64Building.Tag = "x86_64";
        this.LblX86_64Building.Text = "Building :";
        //
        // ChkBxX86_64Publish
        //
        this.ChkBxX86_64Publish.AutoSize = true;
        this.ChkBxX86_64Publish.BackColor = System.Drawing.Color.Green;
        this.ChkBxX86_64Publish.Checked = true;
        this.ChkBxX86_64Publish.CheckState = System.Windows.Forms.CheckState.Checked;
        this.ChkBxX86_64Publish.Location = new System.Drawing.Point(91, 142);
        this.ChkBxX86_64Publish.Name = "ChkBxX86_64Publish";
        this.ChkBxX86_64Publish.Size = new System.Drawing.Size(67, 17);
        this.ChkBxX86_64Publish.TabIndex = 4;
        this.ChkBxX86_64Publish.Tag = "x86_64";
        this.ChkBxX86_64Publish.Text = "Publish";
        this.ChkBxX86_64Publish.ThreeState = true;
        this.ChkBxX86_64Publish.UseVisualStyleBackColor = false;
        this.ChkBxX86_64Publish.CheckStateChanged += new System.EventHandler(this.ChkBxS_CheckStateChanged);
        //
        // ChkBxX86_64Build
        //
        this.ChkBxX86_64Build.AutoSize = true;
        this.ChkBxX86_64Build.BackColor = System.Drawing.Color.Green;
        this.ChkBxX86_64Build.Checked = true;
        this.ChkBxX86_64Build.CheckState = System.Windows.Forms.CheckState.Checked;
        this.ChkBxX86_64Build.Location = new System.Drawing.Point(19, 142);
        this.ChkBxX86_64Build.Name = "ChkBxX86_64Build";
        this.ChkBxX86_64Build.Size = new System.Drawing.Size(54, 17);
        this.ChkBxX86_64Build.TabIndex = 3;
        this.ChkBxX86_64Build.Tag = "x86_64";
        this.ChkBxX86_64Build.Text = "Build";
        this.ChkBxX86_64Build.ThreeState = true;
        this.ChkBxX86_64Build.UseVisualStyleBackColor = false;
        this.ChkBxX86_64Build.CheckStateChanged += new System.EventHandler(this.ChkBxS_CheckStateChanged);
        //
        // LblX86_64Succes
        //
        this.LblX86_64Succes.AutoSize = true;
        this.LblX86_64Succes.Location = new System.Drawing.Point(6, 13);
        this.LblX86_64Succes.Name = "LblX86_64Succes";
        this.LblX86_64Succes.Size = new System.Drawing.Size(57, 13);
        this.LblX86_64Succes.TabIndex = 2;
        this.LblX86_64Succes.TabStop = true;
        this.LblX86_64Succes.Tag = "x86_64";
        this.LblX86_64Succes.Text = "Succes :";
        //
        // LblX86_64Disable
        //
        this.LblX86_64Disable.AutoSize = true;
        this.LblX86_64Disable.Location = new System.Drawing.Point(6, 43);
        this.LblX86_64Disable.Name = "LblX86_64Disable";
        this.LblX86_64Disable.Size = new System.Drawing.Size(57, 13);
        this.LblX86_64Disable.TabIndex = 1;
        this.LblX86_64Disable.TabStop = true;
        this.LblX86_64Disable.Tag = "x86_64";
        this.LblX86_64Disable.Text = "Disable :";
        //
        // BckGWStati
        //
        this.BckGWStati.WorkerReportsProgress = true;
        this.BckGWStati.WorkerSupportsCancellation = true;
        this.BckGWStati.DoWork += new System.ComponentModel.DoWorkEventHandler(this.BckGWStati_DoWork);
        this.BckGWStati.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.BckGWStati_RunWorkerCompleted);
        this.BckGWStati.ProgressChanged += new System.ComponentModel.ProgressChangedEventHandler(this.BckGWStati_ProgressChanged);
        //
        // BuildRepositories
        //
        this.Controls.Add(this.GrpBxX86_64);
        this.Controls.Add(this.GrpBxI586);
        this.Controls.Add(this.GrpBxName);
        this.DoubleBuffered = true;
        this.Name = "BuildRepositories";
        this.Size = new System.Drawing.Size(635, 173);
        this.Validated += new System.EventHandler(this.BuildRepositories_Validated);
        this.GrpBxName.ResumeLayout(false);
        this.toolStripContainer1.ContentPanel.ResumeLayout(false);
        this.toolStripContainer1.ContentPanel.PerformLayout();
        this.toolStripContainer1.TopToolStripPanel.ResumeLayout(false);
        this.toolStripContainer1.ResumeLayout(false);
        this.toolStripContainer1.PerformLayout();
        this.toolStrip2.ResumeLayout(false);
        this.toolStrip2.PerformLayout();
        this.GrpBxI586.ResumeLayout(false);
        this.GrpBxI586.PerformLayout();
        this.GrpBxX86_64.ResumeLayout(false);
        this.GrpBxX86_64.PerformLayout();
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.LinkLabel LblDistro;
    private System.Windows.Forms.LinkLabel LblDistroDescr;
    private System.Windows.Forms.GroupBox GrpBxName;
    private System.Windows.Forms.GroupBox GrpBxI586;
    private System.Windows.Forms.LinkLabel LblI586Disable;
    private System.Windows.Forms.LinkLabel LblI586Succes;
    private System.Windows.Forms.CheckBox ChkBxI586Publish;
    private System.Windows.Forms.CheckBox ChkBxI586Build;
    private System.Windows.Forms.GroupBox GrpBxX86_64;
    private System.Windows.Forms.CheckBox ChkBxX86_64Publish;
    private System.Windows.Forms.CheckBox ChkBxX86_64Build;
    private System.Windows.Forms.LinkLabel LblX86_64Succes;
    private System.Windows.Forms.LinkLabel LblX86_64Disable;
    private System.Windows.Forms.LinkLabel LblGotoRepository;
    private System.Windows.Forms.LinkLabel LblI586Building;
    private System.Windows.Forms.LinkLabel LblX86_64Building;
    private System.Windows.Forms.Label LblX86_64Dis;
    private System.Windows.Forms.Label LblX86_64Build;
    private System.Windows.Forms.Label LblX86_64Suc;
    private System.Windows.Forms.Label Lbli586Dis;
    private System.Windows.Forms.Label Lbli586Build;
    private System.Windows.Forms.Label Lbli586Suc;
    private System.ComponentModel.BackgroundWorker BckGWStati;
    private System.Windows.Forms.Label Lbli586Other;
    private System.Windows.Forms.TextBox Txti586Other;
    private System.Windows.Forms.TextBox TxtX86_64Other;
    private System.Windows.Forms.ImageList imageList1;
    private System.Windows.Forms.Button BtnHideI586;
    private System.Windows.Forms.Button BtnHideX64;
    private System.Windows.Forms.ToolStrip toolStrip2;
    private System.Windows.Forms.ToolStripButton BtnDelRepo;
    private System.Windows.Forms.ToolStripContainer toolStripContainer1;
    private System.Windows.Forms.ToolStripButton BtnRefresh;
}
}
