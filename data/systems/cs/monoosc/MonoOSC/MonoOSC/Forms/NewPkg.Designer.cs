//
// NewPkg.Designer.cs
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
partial class NewPkg
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
        this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
        this.TxtTitle = new System.Windows.Forms.TextBox();
        this.TxtPkgName = new System.Windows.Forms.TextBox();
        this.label1 = new System.Windows.Forms.Label();
        this.label2 = new System.Windows.Forms.Label();
        this.TxtPkgDescrip = new System.Windows.Forms.TextBox();
        this.label3 = new System.Windows.Forms.Label();
        this.tableLayoutPanel2 = new System.Windows.Forms.TableLayoutPanel();
        this.tableLayoutPanel3 = new System.Windows.Forms.TableLayoutPanel();
        this.BtnCancel = new System.Windows.Forms.Button();
        this.BtnOk = new System.Windows.Forms.Button();
        this.tableLayoutPanel1.SuspendLayout();
        this.tableLayoutPanel2.SuspendLayout();
        this.tableLayoutPanel3.SuspendLayout();
        this.SuspendLayout();
        //
        // tableLayoutPanel1
        //
        this.tableLayoutPanel1.ColumnCount = 2;
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
        this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
        this.tableLayoutPanel1.Controls.Add(this.TxtTitle, 1, 1);
        this.tableLayoutPanel1.Controls.Add(this.TxtPkgName, 1, 0);
        this.tableLayoutPanel1.Controls.Add(this.label1, 0, 0);
        this.tableLayoutPanel1.Controls.Add(this.label2, 0, 1);
        this.tableLayoutPanel1.Controls.Add(this.TxtPkgDescrip, 1, 2);
        this.tableLayoutPanel1.Controls.Add(this.label3, 0, 2);
        this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanel1.Location = new System.Drawing.Point(3, 3);
        this.tableLayoutPanel1.Name = "tableLayoutPanel1";
        this.tableLayoutPanel1.RowCount = 3;
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 33.33333F));
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 33.33333F));
        this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 33.33333F));
        this.tableLayoutPanel1.Size = new System.Drawing.Size(438, 194);
        this.tableLayoutPanel1.TabIndex = 0;
        //
        // TxtTitle
        //
        this.TxtTitle.Location = new System.Drawing.Point(222, 67);
        this.TxtTitle.Name = "TxtTitle";
        this.TxtTitle.Size = new System.Drawing.Size(213, 20);
        this.TxtTitle.TabIndex = 5;
        this.TxtTitle.Text = "package title";
        //
        // TxtPkgName
        //
        this.TxtPkgName.Location = new System.Drawing.Point(222, 3);
        this.TxtPkgName.Name = "TxtPkgName";
        this.TxtPkgName.Size = new System.Drawing.Size(213, 20);
        this.TxtPkgName.TabIndex = 1;
        this.TxtPkgName.Text = "package name";
        //
        // label1
        //
        this.label1.AutoSize = true;
        this.label1.Location = new System.Drawing.Point(3, 0);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(35, 13);
        this.label1.TabIndex = 2;
        this.label1.Text = "Name";
        //
        // label2
        //
        this.label2.AutoSize = true;
        this.label2.Location = new System.Drawing.Point(3, 64);
        this.label2.Name = "label2";
        this.label2.Size = new System.Drawing.Size(27, 13);
        this.label2.TabIndex = 3;
        this.label2.Text = "Title";
        //
        // TxtPkgDescrip
        //
        this.TxtPkgDescrip.Location = new System.Drawing.Point(222, 131);
        this.TxtPkgDescrip.Multiline = true;
        this.TxtPkgDescrip.Name = "TxtPkgDescrip";
        this.TxtPkgDescrip.ScrollBars = System.Windows.Forms.ScrollBars.Both;
        this.TxtPkgDescrip.Size = new System.Drawing.Size(213, 39);
        this.TxtPkgDescrip.TabIndex = 0;
        this.TxtPkgDescrip.Text = "package long description";
        //
        // label3
        //
        this.label3.AutoSize = true;
        this.label3.Location = new System.Drawing.Point(3, 128);
        this.label3.Name = "label3";
        this.label3.Size = new System.Drawing.Size(60, 13);
        this.label3.TabIndex = 4;
        this.label3.Text = "Description";
        //
        // tableLayoutPanel2
        //
        this.tableLayoutPanel2.ColumnCount = 1;
        this.tableLayoutPanel2.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
        this.tableLayoutPanel2.Controls.Add(this.tableLayoutPanel1, 0, 0);
        this.tableLayoutPanel2.Controls.Add(this.tableLayoutPanel3, 0, 1);
        this.tableLayoutPanel2.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanel2.Location = new System.Drawing.Point(0, 0);
        this.tableLayoutPanel2.Name = "tableLayoutPanel2";
        this.tableLayoutPanel2.RowCount = 2;
        this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        this.tableLayoutPanel2.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        this.tableLayoutPanel2.Size = new System.Drawing.Size(444, 401);
        this.tableLayoutPanel2.TabIndex = 1;
        //
        // tableLayoutPanel3
        //
        this.tableLayoutPanel3.ColumnCount = 2;
        this.tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 75.11416F));
        this.tableLayoutPanel3.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 24.88585F));
        this.tableLayoutPanel3.Controls.Add(this.BtnCancel, 1, 0);
        this.tableLayoutPanel3.Controls.Add(this.BtnOk, 0, 0);
        this.tableLayoutPanel3.Dock = System.Windows.Forms.DockStyle.Fill;
        this.tableLayoutPanel3.Location = new System.Drawing.Point(3, 203);
        this.tableLayoutPanel3.Name = "tableLayoutPanel3";
        this.tableLayoutPanel3.RowCount = 1;
        this.tableLayoutPanel3.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 50F));
        this.tableLayoutPanel3.Size = new System.Drawing.Size(438, 195);
        this.tableLayoutPanel3.TabIndex = 1;
        //
        // BtnCancel
        //
        this.BtnCancel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnCancel.DialogResult = System.Windows.Forms.DialogResult.Cancel;
        this.BtnCancel.Location = new System.Drawing.Point(352, 157);
        this.BtnCancel.Name = "BtnCancel";
        this.BtnCancel.Size = new System.Drawing.Size(83, 35);
        this.BtnCancel.TabIndex = 1;
        this.BtnCancel.Text = "Cancel";
        this.BtnCancel.UseVisualStyleBackColor = true;
        //
        // BtnOk
        //
        this.BtnOk.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnOk.Location = new System.Drawing.Point(242, 157);
        this.BtnOk.Name = "BtnOk";
        this.BtnOk.Size = new System.Drawing.Size(83, 35);
        this.BtnOk.TabIndex = 0;
        this.BtnOk.Text = "Ok";
        this.BtnOk.UseVisualStyleBackColor = true;
        this.BtnOk.Click += new System.EventHandler(this.BtnOk_Click);
        //
        // NewPkg
        //
        this.AcceptButton = this.BtnOk;
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.CancelButton = this.BtnCancel;
        this.ClientSize = new System.Drawing.Size(444, 401);
        this.Controls.Add(this.tableLayoutPanel2);
        this.Name = "NewPkg";
        this.Text = "New package";
        this.Shown += new System.EventHandler(this.NewPkg_Shown);
        this.tableLayoutPanel1.ResumeLayout(false);
        this.tableLayoutPanel1.PerformLayout();
        this.tableLayoutPanel2.ResumeLayout(false);
        this.tableLayoutPanel3.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    private System.Windows.Forms.TextBox TxtPkgName;
    private System.Windows.Forms.TextBox TxtPkgDescrip;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel2;
    private System.Windows.Forms.TableLayoutPanel tableLayoutPanel3;
    private System.Windows.Forms.Button BtnCancel;
    private System.Windows.Forms.Button BtnOk;
    private System.Windows.Forms.TextBox TxtTitle;
    private System.Windows.Forms.Label label3;
}
}
