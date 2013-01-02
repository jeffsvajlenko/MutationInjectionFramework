// About.Designer.cs created with MonoDevelop
//
//User: eric at 03:29 10/08/2008
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

namespace MonoOSC.Forms
{
partial class About
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

    #region Code généré par le Concepteur Windows Form

    /// <summary>
    /// Méthode requise pour la prise en charge du concepteur - ne modifiez pas
    /// le contenu de cette méthode avec l'éditeur de code.
    /// </summary>
    private void InitializeComponent()
    {
        this.components = new System.ComponentModel.Container();
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(About));
        this.ClmFsName = new System.Windows.Forms.ColumnHeader();
        this.richTextBox1 = new System.Windows.Forms.RichTextBox();
        this.ClmFsVers = new System.Windows.Forms.ColumnHeader();
        this.tabPage1 = new System.Windows.Forms.TabPage();
        this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
        this.MonoLogo = new System.Windows.Forms.PictureBox();
        this.listView1 = new System.Windows.Forms.ListView();
        this.ClmBuildVers = new System.Windows.Forms.ColumnHeader();
        this.ClmCpRight = new System.Windows.Forms.ColumnHeader();
        this.ClmPath = new System.Windows.Forms.ColumnHeader();
        this.richTextBox2 = new System.Windows.Forms.RichTextBox();
        this.tabPage2 = new System.Windows.Forms.TabPage();
        this.tabPage3 = new System.Windows.Forms.TabPage();
        this.LblVers = new System.Windows.Forms.Label();
        this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
        this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
        this.tabControl1 = new System.Windows.Forms.TabControl();
        this.tabPage4 = new System.Windows.Forms.TabPage();
        this.imageList1 = new System.Windows.Forms.ImageList(this.components);
        this.tabPage1.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)(this.MonoLogo)).BeginInit();
        this.tabPage2.SuspendLayout();
        this.tabPage3.SuspendLayout();
        this.tableLayoutPanel1.SuspendLayout();
        this.tableLayoutPanel2.SuspendLayout();
        this.tabControl1.SuspendLayout();
        this.SuspendLayout();
        //
        // ClmFsName
        //
        this.ClmFsName.Text = "File name";
        this.ClmFsName.Width = 185;
        //
        // richTextBox1
        //
        this.richTextBox1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.richTextBox1.Location = new System.Drawing.Point(3, 3);
        this.richTextBox1.Name = "richTextBox1";
        this.richTextBox1.ReadOnly = true;
        this.richTextBox1.Size = new System.Drawing.Size(952, 459);
        this.richTextBox1.TabIndex = 0;
        this.richTextBox1.Text = "";
        this.richTextBox1.LinkClicked += new System.Windows.Forms.LinkClickedEventHandler(this.richTextBox1_LinkClicked);
        //
        // ClmFsVers
        //
        this.ClmFsVers.Text = "File Version";
        this.ClmFsVers.Width = 150;
        //
        // tabPage1
        //
        this.tabPage1.Controls.Add(this.richTextBox1);
        this.tabPage1.Location = new System.Drawing.Point(4, 22);
        this.tabPage1.Name = "tabPage1";
        this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage1.Size = new System.Drawing.Size(958, 465);
        this.tabPage1.TabIndex = 0;
        this.tabPage1.Text = "About";
        this.tabPage1.UseVisualStyleBackColor = true;
        //
        // MonoLogo
        //
        this.MonoLogo.Cursor = System.Windows.Forms.Cursors.Hand;
        this.MonoLogo.Image = global::MonoOSC.Properties.Resources.Monopoweredbig;
        this.MonoLogo.ImeMode = System.Windows.Forms.ImeMode.NoControl;
        this.MonoLogo.Location = new System.Drawing.Point(864, 3);
        this.MonoLogo.Name = "MonoLogo";
        this.MonoLogo.Size = new System.Drawing.Size(99, 44);
        this.MonoLogo.TabIndex = 0;
        this.MonoLogo.TabStop = false;
        this.toolTip1.SetToolTip(this.MonoLogo, "Visite Mono Website");
        this.MonoLogo.Click += new System.EventHandler(this.MonoLogo_Click);
        //
        // listView1
        //
        this.listView1.Columns.AddRange(new System.Windows.Forms.ColumnHeader[]
        {
            this.ClmFsName,
            this.ClmBuildVers,
            this.ClmFsVers,
            this.ClmCpRight,
            this.ClmPath
        });
        this.listView1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.listView1.FullRowSelect = true;
        this.listView1.GridLines = true;
        this.listView1.HideSelection = false;
        this.listView1.Location = new System.Drawing.Point(3, 3);
        this.listView1.Name = "listView1";
        this.listView1.ShowItemToolTips = true;
        this.listView1.Size = new System.Drawing.Size(952, 459);
        this.listView1.Sorting = System.Windows.Forms.SortOrder.Ascending;
        this.listView1.TabIndex = 0;
        this.listView1.UseCompatibleStateImageBehavior = false;
        this.listView1.View = System.Windows.Forms.View.Details;
        this.listView1.ColumnClick += new System.Windows.Forms.ColumnClickEventHandler(this.listView1_ColumnClick);
        //
        // ClmBuildVers
        //
        this.ClmBuildVers.Text = "Build Version";
        this.ClmBuildVers.Width = 90;
        //
        // ClmCpRight
        //
        this.ClmCpRight.Text = "Copyright";
        this.ClmCpRight.Width = 166;
        //
        // ClmPath
        //
        this.ClmPath.Text = "Path";
        this.ClmPath.Width = 345;
        //
        // richTextBox2
        //
        this.richTextBox2.BackColor = System.Drawing.Color.Beige;
        this.richTextBox2.Dock = System.Windows.Forms.DockStyle.Fill;
        this.richTextBox2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.richTextBox2.ForeColor = System.Drawing.Color.RoyalBlue;
        this.richTextBox2.Location = new System.Drawing.Point(3, 3);
        this.richTextBox2.Name = "richTextBox2";
        this.richTextBox2.Size = new System.Drawing.Size(952, 459);
        this.richTextBox2.TabIndex = 0;
        this.richTextBox2.Text = resources.GetString("richTextBox2.Text");
        //
        // tabPage2
        //
        this.tabPage2.Controls.Add(this.listView1);
        this.tabPage2.Location = new System.Drawing.Point(4, 22);
        this.tabPage2.Name = "tabPage2";
        this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage2.Size = new System.Drawing.Size(958, 465);
        this.tabPage2.TabIndex = 1;
        this.tabPage2.Text = "Versions";
        this.tabPage2.UseVisualStyleBackColor = true;
        //
        // tabPage3
        //
        this.tabPage3.Controls.Add(this.richTextBox2);
        this.tabPage3.Location = new System.Drawing.Point(4, 22);
        this.tabPage3.Name = "tabPage3";
        this.tabPage3.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage3.Size = new System.Drawing.Size(958, 465);
        this.tabPage3.TabIndex = 2;
        this.tabPage3.Text = "Thank\'s";
        this.tabPage3.UseVisualStyleBackColor = true;
        //
        // LblVers
        //
        this.LblVers.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Left | System.Windows.Forms.AnchorStyles.Right)));
        this.LblVers.AutoEllipsis = true;
        this.LblVers.AutoSize = true;
        this.LblVers.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Bold);
        this.LblVers.ImeMode = System.Windows.Forms.ImeMode.NoControl;
        this.LblVers.Location = new System.Drawing.Point(108, 17);
        this.LblVers.Name = "LblVers";
        this.LblVers.Size = new System.Drawing.Size(750, 15);
        this.LblVers.TabIndex = 1;
        this.LblVers.Text = "label1";
        this.LblVers.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        //
        // tableLayoutPanel1
        //
        this.tableLayoutPanel1.ColumnCount = 1;
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanel1.Controls.Add(this.tableLayoutPanel2, 0, 1);
        this.tableLayoutPanel1.Controls.Add(this.tabControl1, 0, 0);
        this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
        this.tableLayoutPanel1.Name = "tableLayoutPanel1";
        this.tableLayoutPanel1.RowCount = 2;
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 56F));
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 20F));
        this.tableLayoutPanel1.Size = new System.Drawing.Size(972, 553);
        this.tableLayoutPanel1.TabIndex = 3;
        //
        // tableLayoutPanel2
        //
        this.tableLayoutPanel2.ColumnCount = 3;
        this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 105F));
        this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 105F));
        this.tableLayoutPanel2.Controls.Add(this.MonoLogo, 2, 0);
        this.tableLayoutPanel2.Controls.Add(this.LblVers, 1, 0);
        this.tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanel2.Location = new System.Drawing.Point(3, 500);
        this.tableLayoutPanel2.Name = "tableLayoutPanel2";
        this.tableLayoutPanel2.RowCount = 1;
        this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 50F));
        this.tableLayoutPanel2.Size = new System.Drawing.Size(966, 50);
        this.tableLayoutPanel2.TabIndex = 0;
        //
        // tabControl1
        //
        this.tabControl1.Controls.Add(this.tabPage1);
        this.tabControl1.Controls.Add(this.tabPage2);
        this.tabControl1.Controls.Add(this.tabPage3);
        this.tabControl1.Controls.Add(this.tabPage4);
        this.tabControl1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tabControl1.Location = new System.Drawing.Point(3, 3);
        this.tabControl1.Name = "tabControl1";
        this.tabControl1.SelectedIndex = 0;
        this.tabControl1.Size = new System.Drawing.Size(966, 491);
        this.tabControl1.TabIndex = 3;
        //
        // tabPage4
        //
        this.tabPage4.BackgroundImage = global::MonoOSC.Properties.Resources.Mono_project_logo;
        this.tabPage4.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
        this.tabPage4.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.tabPage4.Location = new System.Drawing.Point(4, 22);
        this.tabPage4.Name = "tabPage4";
        this.tabPage4.Padding = new System.Windows.Forms.Padding(3);
        this.tabPage4.Size = new System.Drawing.Size(958, 465);
        this.tabPage4.TabIndex = 3;
        this.tabPage4.Text = "Contrib";
        this.tabPage4.UseVisualStyleBackColor = true;
        //
        // imageList1
        //
        this.imageList1.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("imageList1.ImageStream")));
        this.imageList1.TransparentColor = System.Drawing.Color.Transparent;
        this.imageList1.Images.SetKeyName(0, "Mono-powered-big.png");
        //
        // About
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(972, 553);
        this.Controls.Add(this.tableLayoutPanel1);
        this.Height = 833;
        this.Width = 646;
        this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
        this.Name = "About";
        this.Text = "About";
        this.Shown += new System.EventHandler(this.About_Shown);
        this.tabPage1.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)(this.MonoLogo)).EndInit();
        this.tabPage2.ResumeLayout(false);
        this.tabPage3.ResumeLayout(false);
        this.tableLayoutPanel1.ResumeLayout(false);
        this.tableLayoutPanel2.ResumeLayout(false);
        this.tableLayoutPanel2.PerformLayout();
        this.tabControl1.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.ColumnHeader ClmFsName;
    private System.Windows.Forms.RichTextBox richTextBox1;
    private System.Windows.Forms.ColumnHeader ClmFsVers;
    private System.Windows.Forms.TabPage tabPage1;
    private System.Windows.Forms.ToolTip toolTip1;
    private System.Windows.Forms.ColumnHeader ClmPath;
    private System.Windows.Forms.ListView listView1;
    private System.Windows.Forms.RichTextBox richTextBox2;
    private System.Windows.Forms.PictureBox MonoLogo;
    private System.Windows.Forms.TabPage tabPage2;
    private System.Windows.Forms.TabPage tabPage3;
    private System.Windows.Forms.Label LblVers;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
    private System.Windows.Forms.TabControl tabControl1;
    private System.Windows.Forms.ImageList imageList1;
    private System.Windows.Forms.TabPage tabPage4;
    private System.Windows.Forms.ColumnHeader ClmBuildVers;
    private System.Windows.Forms.ColumnHeader ClmCpRight;
}
}
