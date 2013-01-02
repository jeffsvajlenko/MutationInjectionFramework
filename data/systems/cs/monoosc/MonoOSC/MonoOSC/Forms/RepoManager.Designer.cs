//
// RepoManager.Designer.cs
//
// Author:
//       Surfzoid <surfzoid@gmail.com>
//
// Copyright (c) 2009 Surfzoid
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

namespace MonoOSC.Forms
{
partial class RepoManager
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
        System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle9 = new System.Windows.Forms.DataGridViewCellStyle();
        this.tableLayoutPanelMain = new System.Windows.Forms.TableLayoutPanel();
        this.tableLayoutPanelBottom = new System.Windows.Forms.TableLayoutPanel();
        this.label1 = new System.Windows.Forms.Label();
        this.BtnCancel = new System.Windows.Forms.Button();
        this.LblStatus = new System.Windows.Forms.Label();
        this.BtnAdv = new System.Windows.Forms.Button();
        this.BtnOk = new System.Windows.Forms.Button();
        this.splitContainer1 = new System.Windows.Forms.SplitContainer();
        this.treeViewRepo = new System.Windows.Forms.TreeView();
        this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
        this.addCustomNodeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.deleteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.DtGAviableRepo = new System.Windows.Forms.DataGridView();
        this.BtnNew = new System.Windows.Forms.Button();
        this.BtnRemove = new System.Windows.Forms.Button();
        this.BtnAdd = new System.Windows.Forms.Button();
        this.BckGrdWorkerWebRepo = new System.ComponentModel.BackgroundWorker();
        this.cutToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.copyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.pasteToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
        this.tableLayoutPanelMain.SuspendLayout();
        this.tableLayoutPanelBottom.SuspendLayout();
        this.splitContainer1.Panel1.SuspendLayout();
        this.splitContainer1.Panel2.SuspendLayout();
        this.splitContainer1.SuspendLayout();
        this.contextMenuStrip1.SuspendLayout();
        ((System.ComponentModel.ISupportInitialize)(this.DtGAviableRepo)).BeginInit();
        this.SuspendLayout();
        //
        // tableLayoutPanelMain
        //
        this.tableLayoutPanelMain.ColumnCount = 1;
        this.tableLayoutPanelMain.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanelMain.Controls.Add(this.tableLayoutPanelBottom, 0, 1);
        this.tableLayoutPanelMain.Controls.Add(this.splitContainer1, 0, 0);
        this.tableLayoutPanelMain.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanelMain.Location = new System.Drawing.Point(0, 0);
        this.tableLayoutPanelMain.Name = "tableLayoutPanelMain";
        this.tableLayoutPanelMain.RowCount = 2;
        this.tableLayoutPanelMain.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanelMain.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Absolute, 39F));
        this.tableLayoutPanelMain.Size = new System.Drawing.Size(752, 449);
        this.tableLayoutPanelMain.TabIndex = 0;
        //
        // tableLayoutPanelBottom
        //
        this.tableLayoutPanelBottom.ColumnCount = 5;
        this.tableLayoutPanelBottom.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanelBottom.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 320F));
        this.tableLayoutPanelBottom.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 65F));
        this.tableLayoutPanelBottom.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 96F));
        this.tableLayoutPanelBottom.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 68F));
        this.tableLayoutPanelBottom.Controls.Add(this.label1, 0, 0);
        this.tableLayoutPanelBottom.Controls.Add(this.BtnCancel, 4, 0);
        this.tableLayoutPanelBottom.Controls.Add(this.LblStatus, 1, 0);
        this.tableLayoutPanelBottom.Controls.Add(this.BtnAdv, 3, 0);
        this.tableLayoutPanelBottom.Controls.Add(this.BtnOk, 2, 0);
        this.tableLayoutPanelBottom.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanelBottom.Location = new System.Drawing.Point(3, 413);
        this.tableLayoutPanelBottom.Name = "tableLayoutPanelBottom";
        this.tableLayoutPanelBottom.RowCount = 1;
        this.tableLayoutPanelBottom.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
        this.tableLayoutPanelBottom.Size = new System.Drawing.Size(746, 33);
        this.tableLayoutPanelBottom.TabIndex = 1;
        //
        // label1
        //
        this.label1.AutoEllipsis = true;
        this.label1.AutoSize = true;
        this.label1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.label1.Location = new System.Drawing.Point(3, 0);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(191, 33);
        this.label1.TabIndex = 3;
        this.label1.Text = "label1";
        //
        // BtnCancel
        //
        this.BtnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
        this.BtnCancel.Location = new System.Drawing.Point(685, 3);
        this.BtnCancel.Name = "BtnCancel";
        this.BtnCancel.Size = new System.Drawing.Size(58, 27);
        this.BtnCancel.TabIndex = 2;
        this.BtnCancel.Text = "Cancel";
        this.BtnCancel.UseVisualStyleBackColor = true;
        //
        // LblStatus
        //
        this.LblStatus.AutoEllipsis = true;
        this.LblStatus.AutoSize = true;
        this.LblStatus.Dock = System.Windows.Forms.DockStyle.Fill;
        this.LblStatus.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, ((System.Drawing.FontStyle)((System.Drawing.FontStyle.Bold | System.Drawing.FontStyle.Italic))), System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        this.LblStatus.ForeColor = System.Drawing.Color.Red;
        this.LblStatus.Location = new System.Drawing.Point(200, 0);
        this.LblStatus.Name = "LblStatus";
        this.LblStatus.Size = new System.Drawing.Size(314, 33);
        this.LblStatus.TabIndex = 2;
        this.LblStatus.Text = "Getting aviable repositories ...............";
        this.LblStatus.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
        //
        // BtnAdv
        //
        this.BtnAdv.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnAdv.Location = new System.Drawing.Point(585, 3);
        this.BtnAdv.Name = "BtnAdv";
        this.BtnAdv.Size = new System.Drawing.Size(90, 27);
        this.BtnAdv.TabIndex = 4;
        this.BtnAdv.Text = "See advanced";
        this.BtnAdv.UseVisualStyleBackColor = true;
        this.BtnAdv.Click += new System.EventHandler(this.BtnAdv_Click);
        //
        // BtnOk
        //
        this.BtnOk.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnOk.Location = new System.Drawing.Point(521, 3);
        this.BtnOk.Name = "BtnOk";
        this.BtnOk.Size = new System.Drawing.Size(58, 27);
        this.BtnOk.TabIndex = 1;
        this.BtnOk.Text = "Ok";
        this.BtnOk.UseVisualStyleBackColor = true;
        this.BtnOk.Click += new System.EventHandler(this.BtnOk_Click);
        //
        // splitContainer1
        //
        this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainer1.Location = new System.Drawing.Point(3, 3);
        this.splitContainer1.Name = "splitContainer1";
        //
        // splitContainer1.Panel1
        //
        this.splitContainer1.Panel1.Controls.Add(this.treeViewRepo);
        //
        // splitContainer1.Panel2
        //
        this.splitContainer1.Panel2.Controls.Add(this.DtGAviableRepo);
        this.splitContainer1.Panel2.Controls.Add(this.BtnNew);
        this.splitContainer1.Panel2.Controls.Add(this.BtnRemove);
        this.splitContainer1.Panel2.Controls.Add(this.BtnAdd);
        this.splitContainer1.Size = new System.Drawing.Size(746, 404);
        this.splitContainer1.SplitterDistance = 195;
        this.splitContainer1.TabIndex = 1;
        //
        // treeViewRepo
        //
        this.treeViewRepo.AllowDrop = true;
        this.treeViewRepo.ContextMenuStrip = this.contextMenuStrip1;
        this.treeViewRepo.Dock = System.Windows.Forms.DockStyle.Fill;
        this.treeViewRepo.FullRowSelect = true;
        this.treeViewRepo.HideSelection = false;
        this.treeViewRepo.LabelEdit = true;
        this.treeViewRepo.Location = new System.Drawing.Point(0, 0);
        this.treeViewRepo.Name = "treeViewRepo";
        this.treeViewRepo.ShowNodeToolTips = true;
        this.treeViewRepo.Size = new System.Drawing.Size(195, 404);
        this.treeViewRepo.TabIndex = 4;
        this.treeViewRepo.AfterLabelEdit += new System.Windows.Forms.NodeLabelEditEventHandler(this.treeViewRepo_AfterLabelEdit);
        this.treeViewRepo.DragDrop += new System.Windows.Forms.DragEventHandler(this.treeViewRepo_DragDrop);
        this.treeViewRepo.DragEnter += new System.Windows.Forms.DragEventHandler(this.treeViewRepo_DragEnter);
        this.treeViewRepo.NodeMouseClick += new System.Windows.Forms.TreeNodeMouseClickEventHandler(this.treeViewRepo_NodeMouseClick);
        this.treeViewRepo.ItemDrag += new System.Windows.Forms.ItemDragEventHandler(this.treeViewRepo_ItemDrag);
        this.treeViewRepo.DragOver += new System.Windows.Forms.DragEventHandler(this.treeViewRepo_DragOver);
        //
        // contextMenuStrip1
        //
        this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[]
        {
            this.addCustomNodeToolStripMenuItem,
            this.deleteToolStripMenuItem,
            this.copyToolStripMenuItem,
            this.cutToolStripMenuItem,
            this.pasteToolStripMenuItem
        });
        this.contextMenuStrip1.Name = "contextMenuStrip1";
        this.contextMenuStrip1.Size = new System.Drawing.Size(169, 114);
        //
        // addCustomNodeToolStripMenuItem
        //
        this.addCustomNodeToolStripMenuItem.Name = "addCustomNodeToolStripMenuItem";
        this.addCustomNodeToolStripMenuItem.Size = new System.Drawing.Size(168, 22);
        this.addCustomNodeToolStripMenuItem.Text = "Add custom node";
        this.addCustomNodeToolStripMenuItem.Click += new System.EventHandler(this.addCustomNodeToolStripMenuItem_Click);
        //
        // deleteToolStripMenuItem
        //
        this.deleteToolStripMenuItem.Name = "deleteToolStripMenuItem";
        this.deleteToolStripMenuItem.Size = new System.Drawing.Size(168, 22);
        this.deleteToolStripMenuItem.Text = "Delete";
        this.deleteToolStripMenuItem.Click += new System.EventHandler(this.deleteToolStripMenuItem_Click);
        //
        // DtGAviableRepo
        //
        this.DtGAviableRepo.AllowUserToAddRows = false;
        this.DtGAviableRepo.AllowUserToDeleteRows = false;
        this.DtGAviableRepo.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                                      | System.Windows.Forms.AnchorStyles.Left)
                                      | System.Windows.Forms.AnchorStyles.Right)));
        this.DtGAviableRepo.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridViewCellStyle9.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
        dataGridViewCellStyle9.BackColor = System.Drawing.SystemColors.Window;
        dataGridViewCellStyle9.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
        dataGridViewCellStyle9.ForeColor = System.Drawing.SystemColors.ControlText;
        dataGridViewCellStyle9.NullValue = null;
        dataGridViewCellStyle9.SelectionBackColor = System.Drawing.SystemColors.Highlight;
        dataGridViewCellStyle9.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
        dataGridViewCellStyle9.WrapMode = System.Windows.Forms.DataGridViewTriState.False;
        this.DtGAviableRepo.DefaultCellStyle = dataGridViewCellStyle9;
        this.DtGAviableRepo.Location = new System.Drawing.Point(85, 3);
        this.DtGAviableRepo.Name = "DtGAviableRepo";
        this.DtGAviableRepo.ReadOnly = true;
        this.DtGAviableRepo.Size = new System.Drawing.Size(459, 397);
        this.DtGAviableRepo.TabIndex = 3;
        //
        // BtnNew
        //
        this.BtnNew.Anchor = System.Windows.Forms.AnchorStyles.Left;
        this.BtnNew.Location = new System.Drawing.Point(3, 212);
        this.BtnNew.Name = "BtnNew";
        this.BtnNew.Size = new System.Drawing.Size(76, 24);
        this.BtnNew.TabIndex = 3;
        this.BtnNew.Text = "New";
        this.BtnNew.UseVisualStyleBackColor = true;
        this.BtnNew.Click += new System.EventHandler(this.BtnNew_Click);
        //
        // BtnRemove
        //
        this.BtnRemove.Anchor = System.Windows.Forms.AnchorStyles.Left;
        this.BtnRemove.Location = new System.Drawing.Point(3, 182);
        this.BtnRemove.Name = "BtnRemove";
        this.BtnRemove.Size = new System.Drawing.Size(76, 24);
        this.BtnRemove.TabIndex = 1;
        this.BtnRemove.Text = "Remove >>";
        this.BtnRemove.UseVisualStyleBackColor = true;
        this.BtnRemove.Click += new System.EventHandler(this.BtnRemove_Click);
        //
        // BtnAdd
        //
        this.BtnAdd.Anchor = System.Windows.Forms.AnchorStyles.Left;
        this.BtnAdd.Location = new System.Drawing.Point(3, 152);
        this.BtnAdd.Name = "BtnAdd";
        this.BtnAdd.Size = new System.Drawing.Size(76, 24);
        this.BtnAdd.TabIndex = 0;
        this.BtnAdd.Text = "<< Add";
        this.BtnAdd.UseVisualStyleBackColor = true;
        this.BtnAdd.Click += new System.EventHandler(this.BtnAdd_Click);
        //
        // BckGrdWorkerWebRepo
        //
        this.BckGrdWorkerWebRepo.WorkerReportsProgress = true;
        this.BckGrdWorkerWebRepo.WorkerSupportsCancellation = true;
        this.BckGrdWorkerWebRepo.DoWork += new System.ComponentModel.DoWorkEventHandler(this.BckGrdWorkerWebRepo_DoWork);
        this.BckGrdWorkerWebRepo.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.BckGrdWorkerWebRepo_RunWorkerCompleted);
        //
        // cutToolStripMenuItem
        //
        this.cutToolStripMenuItem.Name = "cutToolStripMenuItem";
        this.cutToolStripMenuItem.Size = new System.Drawing.Size(168, 22);
        this.cutToolStripMenuItem.Text = "Cut";
        this.cutToolStripMenuItem.Click += new System.EventHandler(this.cutToolStripMenuItem_Click);
        //
        // copyToolStripMenuItem
        //
        this.copyToolStripMenuItem.Name = "copyToolStripMenuItem";
        this.copyToolStripMenuItem.Size = new System.Drawing.Size(168, 22);
        this.copyToolStripMenuItem.Text = "Copy";
        this.copyToolStripMenuItem.Click += new System.EventHandler(this.copyToolStripMenuItem_Click);
        //
        // pasteToolStripMenuItem
        //
        this.pasteToolStripMenuItem.Name = "pasteToolStripMenuItem";
        this.pasteToolStripMenuItem.Size = new System.Drawing.Size(168, 22);
        this.pasteToolStripMenuItem.Text = "Paste";
        this.pasteToolStripMenuItem.Click += new System.EventHandler(this.pasteToolStripMenuItem_Click);
        //
        // RepoManager
        //
        this.AcceptButton = this.BtnOk;
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.CancelButton = this.BtnCancel;
        this.ClientSize = new System.Drawing.Size(752, 449);
        this.Controls.Add(this.tableLayoutPanelMain);
        this.Name = "RepoManager";
        this.Text = "Repositories Manager";
        this.Shown += new System.EventHandler(this.RepoManager_Shown);
        this.tableLayoutPanelMain.ResumeLayout(false);
        this.tableLayoutPanelBottom.ResumeLayout(false);
        this.tableLayoutPanelBottom.PerformLayout();
        this.splitContainer1.Panel1.ResumeLayout(false);
        this.splitContainer1.Panel2.ResumeLayout(false);
        this.splitContainer1.ResumeLayout(false);
        this.contextMenuStrip1.ResumeLayout(false);
        ((System.ComponentModel.ISupportInitialize)(this.DtGAviableRepo)).EndInit();
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanelMain;
    private System.Windows.Forms.Button BtnRemove;
    private System.Windows.Forms.Button BtnAdd;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanelBottom;
    private System.Windows.Forms.Button BtnCancel;
    private System.Windows.Forms.Button BtnOk;
    private System.Windows.Forms.DataGridView DtGAviableRepo;
    private System.Windows.Forms.TreeView treeViewRepo;
    private System.ComponentModel.BackgroundWorker BckGrdWorkerWebRepo;
    private System.Windows.Forms.Label LblStatus;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Button BtnNew;
    private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
    private System.Windows.Forms.ToolStripMenuItem addCustomNodeToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem deleteToolStripMenuItem;
    private System.Windows.Forms.Button BtnAdv;
    private System.Windows.Forms.SplitContainer splitContainer1;
    private System.Windows.Forms.ToolStripMenuItem copyToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem cutToolStripMenuItem;
    private System.Windows.Forms.ToolStripMenuItem pasteToolStripMenuItem;
}
}
