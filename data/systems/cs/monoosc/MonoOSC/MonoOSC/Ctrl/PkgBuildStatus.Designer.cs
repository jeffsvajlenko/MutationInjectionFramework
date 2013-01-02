// PkgBuildStatus.Designer.cs created with MonoDevelop
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
partial class PkgBuildStatus
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
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(PkgBuildStatus));
        this.splitContainer1 = new System.Windows.Forms.SplitContainer();
        this.BtnHide = new System.Windows.Forms.Button();
        this.BtnRefresh = new System.Windows.Forms.Button();
        this.BtnRebuild = new System.Windows.Forms.Button();
        this.LblRepository = new System.Windows.Forms.Label();
        this.panel1 = new System.Windows.Forms.Panel();
        this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
        this.imageList1 = new System.Windows.Forms.ImageList(this.components);
        this.splitContainer1.Panel1.SuspendLayout();
        this.splitContainer1.Panel2.SuspendLayout();
        this.splitContainer1.SuspendLayout();
        this.SuspendLayout();
        //
        // splitContainer1
        //
        this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainer1.IsSplitterFixed = true;
        this.splitContainer1.Location = new System.Drawing.Point(0, 0);
        this.splitContainer1.Name = "splitContainer1";
        this.splitContainer1.Orientation = System.Windows.Forms.Orientation.Horizontal;
        //
        // splitContainer1.Panel1
        //
        this.splitContainer1.Panel1.Controls.Add(this.BtnHide);
        this.splitContainer1.Panel1.Controls.Add(this.BtnRefresh);
        this.splitContainer1.Panel1.Controls.Add(this.BtnRebuild);
        this.splitContainer1.Panel1.Controls.Add(this.LblRepository);
        this.splitContainer1.Panel1MinSize = 30;
        //
        // splitContainer1.Panel2
        //
        this.splitContainer1.Panel2.Controls.Add(this.panel1);
        this.splitContainer1.Size = new System.Drawing.Size(349, 35);
        this.splitContainer1.SplitterDistance = 30;
        this.splitContainer1.TabIndex = 5;
        //
        // BtnHide
        //
        this.BtnHide.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnHide.BackgroundImage = global::MonoOSC.Properties.Resources.Hide;
        this.BtnHide.Location = new System.Drawing.Point(320, 4);
        this.BtnHide.Name = "BtnHide";
        this.BtnHide.Size = new System.Drawing.Size(23, 23);
        this.BtnHide.TabIndex = 6;
        this.toolTip1.SetToolTip(this.BtnHide, "Hide or show the panel");
        this.BtnHide.UseVisualStyleBackColor = true;
        this.BtnHide.Click += new System.EventHandler(this.BtnHide_Click);
        //
        // BtnRefresh
        //
        this.BtnRefresh.BackgroundImage = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefresh.Location = new System.Drawing.Point(32, 3);
        this.BtnRefresh.Name = "BtnRefresh";
        this.BtnRefresh.Size = new System.Drawing.Size(23, 23);
        this.BtnRefresh.TabIndex = 5;
        this.toolTip1.SetToolTip(this.BtnRefresh, "Refresh all building status and files of this");
        this.BtnRefresh.UseVisualStyleBackColor = true;
        this.BtnRefresh.MouseLeave += new System.EventHandler(this.BtnRebuild_MouseLeave);
        this.BtnRefresh.Click += new System.EventHandler(this.BtnRefresh_Click);
        this.BtnRefresh.MouseHover += new System.EventHandler(this.BtnRebuild_MouseEnter);
        this.BtnRefresh.MouseEnter += new System.EventHandler(this.BtnRebuild_MouseEnter);
        //
        // BtnRebuild
        //
        this.BtnRebuild.BackgroundImage = global::MonoOSC.Properties.Resources.Rebuild;
        this.BtnRebuild.Location = new System.Drawing.Point(3, 3);
        this.BtnRebuild.Name = "BtnRebuild";
        this.BtnRebuild.Size = new System.Drawing.Size(23, 23);
        this.BtnRebuild.TabIndex = 3;
        this.toolTip1.SetToolTip(this.BtnRebuild, "Rebuild all of this OS");
        this.BtnRebuild.UseVisualStyleBackColor = true;
        this.BtnRebuild.MouseLeave += new System.EventHandler(this.BtnRebuild_MouseLeave);
        this.BtnRebuild.Click += new System.EventHandler(this.BtnRebuild_Click);
        this.BtnRebuild.MouseHover += new System.EventHandler(this.BtnRebuild_MouseEnter);
        this.BtnRebuild.MouseEnter += new System.EventHandler(this.BtnRebuild_MouseEnter);
        //
        // LblRepository
        //
        this.LblRepository.AutoSize = true;
        this.LblRepository.Font = new System.Drawing.Font("Microsoft Sans Serif", 14F, System.Drawing.FontStyle.Bold);
        this.LblRepository.ForeColor = System.Drawing.Color.Chocolate;
        this.LblRepository.Location = new System.Drawing.Point(61, 6);
        this.LblRepository.Name = "LblRepository";
        this.LblRepository.Size = new System.Drawing.Size(109, 24);
        this.LblRepository.TabIndex = 4;
        this.LblRepository.Text = "Repository";
        //
        // panel1
        //
        this.panel1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.panel1.Location = new System.Drawing.Point(0, 0);
        this.panel1.Name = "panel1";
        this.panel1.Size = new System.Drawing.Size(349, 25);
        this.panel1.TabIndex = 0;
        //
        // imageList1
        //
        this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
        this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
        this.imageList1.Images.SetKeyName(0, "Hide.bmp");
        this.imageList1.Images.SetKeyName(1, "Show.bmp");
        //
        // PkgBuildStatus
        //
        this.BackColor = System.Drawing.SystemColors.ButtonFace;
        this.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.Controls.Add(this.splitContainer1);
        this.DoubleBuffered = true;
        this.ForeColor = System.Drawing.SystemColors.Desktop;
        this.Name = "PkgBuildStatus";
        this.Size = new System.Drawing.Size(349, 35);
        this.splitContainer1.Panel1.ResumeLayout(false);
        this.splitContainer1.Panel1.PerformLayout();
        this.splitContainer1.Panel2.ResumeLayout(false);
        this.splitContainer1.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.Button BtnRebuild;
    private System.Windows.Forms.Label LblRepository;
    private System.Windows.Forms.ToolTip toolTip1;
    private System.Windows.Forms.SplitContainer splitContainer1;
    private System.Windows.Forms.Button BtnRefresh;
    private System.Windows.Forms.Panel panel1;
    private System.Windows.Forms.Button BtnHide;
    private System.Windows.Forms.ImageList imageList1;

}
}
