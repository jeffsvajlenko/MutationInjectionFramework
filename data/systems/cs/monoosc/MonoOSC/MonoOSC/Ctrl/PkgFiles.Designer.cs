// PkgFiles.Designer.cs created with MonoDevelop
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

namespace MonoOSC.Ctrl
{
partial class PkgFiles
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
        this.LblFsName = new System.Windows.Forms.LinkLabel();
        this.LblRemove = new System.Windows.Forms.LinkLabel();
        this.LblShowEdit = new System.Windows.Forms.LinkLabel();
        this.LblDownload = new System.Windows.Forms.LinkLabel();
        this.LblFsTime = new System.Windows.Forms.LinkLabel();
        this.LblFsSize = new System.Windows.Forms.LinkLabel();
        this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
        this.toolTip1 = new System.Windows.Forms.ToolTip(this.components);
        this.saveFileDialog1 = new System.Windows.Forms.SaveFileDialog();
        this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
        this.showEditToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.downloadToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.removeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.tableLayoutPanel1.SuspendLayout();
        this.contextMenuStrip1.SuspendLayout();
        this.SuspendLayout();
        //
        // LblFsName
        //
        this.LblFsName.AutoSize = true;
        this.LblFsName.BackColor = System.Drawing.SystemColors.Control;
        this.LblFsName.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.LblFsName.ContextMenuStrip = this.contextMenuStrip1;
        this.LblFsName.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblFsName.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F);
        this.LblFsName.LinkColor = System.Drawing.Color.Green;
        this.LblFsName.Location = new System.Drawing.Point(3, 0);
        this.LblFsName.Name = "LblFsName";
        this.LblFsName.Size = new System.Drawing.Size(216, 25);
        this.LblFsName.TabIndex = 0;
        this.LblFsName.TabStop = true;
        this.LblFsName.Text = "FsName";
        this.toolTip1.SetToolTip(this.LblFsName, "FsName");
        //
        // LblRemove
        //
        this.LblRemove.AutoSize = true;
        this.LblRemove.BackColor = System.Drawing.SystemColors.Control;
        this.LblRemove.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.LblRemove.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblRemove.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F);
        this.LblRemove.Location = new System.Drawing.Point(601, 0);
        this.LblRemove.Name = "LblRemove";
        this.LblRemove.Size = new System.Drawing.Size(57, 25);
        this.LblRemove.TabIndex = 1;
        this.LblRemove.TabStop = true;
        this.LblRemove.Text = "Remove";
        this.LblRemove.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.LblRemove_LinkClicked);
        //
        // LblShowEdit
        //
        this.LblShowEdit.AutoSize = true;
        this.LblShowEdit.BackColor = System.Drawing.SystemColors.Control;
        this.LblShowEdit.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.LblShowEdit.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblShowEdit.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F);
        this.LblShowEdit.Location = new System.Drawing.Point(453, 0);
        this.LblShowEdit.Name = "LblShowEdit";
        this.LblShowEdit.Size = new System.Drawing.Size(71, 25);
        this.LblShowEdit.TabIndex = 2;
        this.LblShowEdit.TabStop = true;
        this.LblShowEdit.Text = "Show/Edit";
        this.LblShowEdit.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.LblShowEdit_LinkClicked);
        //
        // LblDownload
        //
        this.LblDownload.AutoSize = true;
        this.LblDownload.BackColor = System.Drawing.SystemColors.Control;
        this.LblDownload.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.LblDownload.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblDownload.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F);
        this.LblDownload.Location = new System.Drawing.Point(530, 0);
        this.LblDownload.Name = "LblDownload";
        this.LblDownload.Size = new System.Drawing.Size(65, 25);
        this.LblDownload.TabIndex = 3;
        this.LblDownload.TabStop = true;
        this.LblDownload.Text = "Download";
        this.LblDownload.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.LblDownload_LinkClicked);
        //
        // LblFsTime
        //
        this.LblFsTime.AutoSize = true;
        this.LblFsTime.BackColor = System.Drawing.SystemColors.Control;
        this.LblFsTime.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.LblFsTime.ContextMenuStrip = this.contextMenuStrip1;
        this.LblFsTime.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblFsTime.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.LblFsTime.LinkColor = System.Drawing.Color.Black;
        this.LblFsTime.Location = new System.Drawing.Point(295, 0);
        this.LblFsTime.Name = "LblFsTime";
        this.LblFsTime.Size = new System.Drawing.Size(152, 25);
        this.LblFsTime.TabIndex = 4;
        this.LblFsTime.TabStop = true;
        this.LblFsTime.Text = "9999/99/99 99:99:99";
        //
        // LblFsSize
        //
        this.LblFsSize.AutoSize = true;
        this.LblFsSize.BackColor = System.Drawing.SystemColors.Control;
        this.LblFsSize.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.LblFsSize.ContextMenuStrip = this.contextMenuStrip1;
        this.LblFsSize.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblFsSize.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F);
        this.LblFsSize.LinkColor = System.Drawing.Color.Black;
        this.LblFsSize.Location = new System.Drawing.Point(225, 0);
        this.LblFsSize.Name = "LblFsSize";
        this.LblFsSize.Size = new System.Drawing.Size(64, 25);
        this.LblFsSize.TabIndex = 5;
        this.LblFsSize.TabStop = true;
        this.LblFsSize.Text = "FsSize";
        //
        // tableLayoutPanel1
        //
        this.tableLayoutPanel1.BackColor = System.Drawing.Color.Aqua;
        this.tableLayoutPanel1.ColumnCount = 6;
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 49.33036F));
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 15.625F));
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 35.04464F));
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 77F));
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 71F));
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 62F));
        this.tableLayoutPanel1.Controls.Add(this.LblFsName, 0, 0);
        this.tableLayoutPanel1.Controls.Add(this.LblRemove, 5, 0);
        this.tableLayoutPanel1.Controls.Add(this.LblShowEdit, 3, 0);
        this.tableLayoutPanel1.Controls.Add(this.LblDownload, 4, 0);
        this.tableLayoutPanel1.Controls.Add(this.LblFsTime, 2, 0);
        this.tableLayoutPanel1.Controls.Add(this.LblFsSize, 1, 0);
        this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
        this.tableLayoutPanel1.Name = "tableLayoutPanel1";
        this.tableLayoutPanel1.RowCount = 1;
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanel1.Size = new System.Drawing.Size(661, 25);
        this.tableLayoutPanel1.TabIndex = 6;
        //
        // toolTip1
        //
        this.toolTip1.IsBalloon = true;
        this.toolTip1.ToolTipIcon = System.Windows.Forms.ToolTipIcon.Info;
        //
        // saveFileDialog1
        //
        this.saveFileDialog1.Filter = "All Files|*.*";
        this.saveFileDialog1.RestoreDirectory = true;
        this.saveFileDialog1.SupportMultiDottedExtensions = true;
        this.saveFileDialog1.Title = "Choice a destination file name";
        //
        // contextMenuStrip1
        //
        this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.showEditToolStripMenuItem,
            this.downloadToolStripMenuItem,
            this.removeToolStripMenuItem
        });
        this.contextMenuStrip1.Name = "contextMenuStrip1";
        this.contextMenuStrip1.Size = new System.Drawing.Size(134, 70);
        //
        // showEditToolStripMenuItem
        //
        this.showEditToolStripMenuItem.Name = "showEditToolStripMenuItem";
        this.showEditToolStripMenuItem.Size = new System.Drawing.Size(133, 22);
        this.showEditToolStripMenuItem.Text = "Show/Edit";
        this.showEditToolStripMenuItem.Click += new System.EventHandler(this.showEditToolStripMenuItem_Click);
        //
        // downloadToolStripMenuItem
        //
        this.downloadToolStripMenuItem.Name = "downloadToolStripMenuItem";
        this.downloadToolStripMenuItem.Size = new System.Drawing.Size(133, 22);
        this.downloadToolStripMenuItem.Text = "Download";
        this.downloadToolStripMenuItem.Click += new System.EventHandler(this.downloadToolStripMenuItem_Click);
        //
        // removeToolStripMenuItem
        //
        this.removeToolStripMenuItem.Name = "removeToolStripMenuItem";
        this.removeToolStripMenuItem.Size = new System.Drawing.Size(133, 22);
        this.removeToolStripMenuItem.Text = "Remove";
        this.removeToolStripMenuItem.Click += new System.EventHandler(this.removeToolStripMenuItem_Click);
        //
        // PkgFiles
        //
        this.BackColor = System.Drawing.Color.DarkGray;
        this.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D;
        this.Controls.Add(this.tableLayoutPanel1);
        this.DoubleBuffered = true;
        this.Name = "PkgFiles";
        this.Size = new System.Drawing.Size(661, 25);
        this.tableLayoutPanel1.ResumeLayout(false);
        this.tableLayoutPanel1.PerformLayout();
        this.contextMenuStrip1.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.LinkLabel LblFsName;
    private System.Windows.Forms.LinkLabel LblRemove;
    private System.Windows.Forms.LinkLabel LblShowEdit;
    private System.Windows.Forms.LinkLabel LblDownload;
    private System.Windows.Forms.LinkLabel LblFsTime;
    private System.Windows.Forms.LinkLabel LblFsSize;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    private System.Windows.Forms.ToolTip toolTip1;
    private System.Windows.Forms.SaveFileDialog saveFileDialog1;
    private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
    private System.Windows.Forms.ToolStripMenuItem showEditToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem downloadToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem removeToolStripMenuItem;
}
}
