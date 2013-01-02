//
// SubmitPrjPkgCreate.Designer.cs
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

namespace MonoOSC.Ctrl.SubmitReq
{
partial class SubmitPrjPkgCreate
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
        this.splitContainer1 = new System.Windows.Forms.SplitContainer();
        this.groupBox3 = new System.Windows.Forms.GroupBox();
        this.CmbxPkgListDest = new System.Windows.Forms.ComboBox();
        this.CmbxPrjDest = new System.Windows.Forms.ComboBox();
        this.label2 = new System.Windows.Forms.Label();
        this.label5 = new System.Windows.Forms.Label();
        this.groupBox2 = new System.Windows.Forms.GroupBox();
        this.CmbxPrjSrce = new System.Windows.Forms.ComboBox();
        this.CmbxPkgListSrce = new System.Windows.Forms.ComboBox();
        this.BtnRefresh = new System.Windows.Forms.Button();
        this.label4 = new System.Windows.Forms.Label();
        this.label3 = new System.Windows.Forms.Label();
        this.BtnDoIt = new System.Windows.Forms.Button();
        this.TxtMess = new System.Windows.Forms.TextBox();
        this.label1 = new System.Windows.Forms.Label();
        this.groupBox1 = new System.Windows.Forms.GroupBox();
        this.splitContainer1.Panel1.SuspendLayout();
        this.splitContainer1.Panel2.SuspendLayout();
        this.splitContainer1.SuspendLayout();
        this.groupBox3.SuspendLayout();
        this.groupBox2.SuspendLayout();
        this.groupBox1.SuspendLayout();
        this.SuspendLayout();
        //
        // splitContainer1
        //
        this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.splitContainer1.Location = new System.Drawing.Point(3, 16);
        this.splitContainer1.Name = "splitContainer1";
        //
        // splitContainer1.Panel1
        //
        this.splitContainer1.Panel1.Controls.Add(this.groupBox3);
        this.splitContainer1.Panel1.Controls.Add(this.groupBox2);
        this.splitContainer1.Panel1.Controls.Add(this.BtnDoIt);
        //
        // splitContainer1.Panel2
        //
        this.splitContainer1.Panel2.Controls.Add(this.TxtMess);
        this.splitContainer1.Panel2.Controls.Add(this.label1);
        this.splitContainer1.Size = new System.Drawing.Size(652, 227);
        this.splitContainer1.SplitterDistance = 360;
        this.splitContainer1.TabIndex = 16;
        //
        // groupBox3
        //
        this.groupBox3.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                 | System.Windows.Forms.AnchorStyles.Right)));
        this.groupBox3.Controls.Add(this.CmbxPkgListDest);
        this.groupBox3.Controls.Add(this.CmbxPrjDest);
        this.groupBox3.Controls.Add(this.label2);
        this.groupBox3.Controls.Add(this.label5);
        this.groupBox3.Location = new System.Drawing.Point(3, 93);
        this.groupBox3.Name = "groupBox3";
        this.groupBox3.Size = new System.Drawing.Size(350, 84);
        this.groupBox3.TabIndex = 21;
        this.groupBox3.TabStop = false;
        this.groupBox3.Text = "Target";
        //
        // CmbxPkgListDest
        //
        this.CmbxPkgListDest.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                       | System.Windows.Forms.AnchorStyles.Right)));
        this.CmbxPkgListDest.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmbxPkgListDest.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmbxPkgListDest.FormattingEnabled = true;
        this.CmbxPkgListDest.Location = new System.Drawing.Point(78, 50);
        this.CmbxPkgListDest.MaxDropDownItems = 20;
        this.CmbxPkgListDest.Name = "CmbxPkgListDest";
        this.CmbxPkgListDest.Size = new System.Drawing.Size(266, 21);
        this.CmbxPkgListDest.TabIndex = 21;
        //
        // CmbxPrjDest
        //
        this.CmbxPrjDest.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                   | System.Windows.Forms.AnchorStyles.Right)));
        this.CmbxPrjDest.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmbxPrjDest.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmbxPrjDest.FormattingEnabled = true;
        this.CmbxPrjDest.Location = new System.Drawing.Point(78, 15);
        this.CmbxPrjDest.MaxDropDownItems = 20;
        this.CmbxPrjDest.Name = "CmbxPrjDest";
        this.CmbxPrjDest.Size = new System.Drawing.Size(266, 21);
        this.CmbxPrjDest.TabIndex = 20;
        //
        // label2
        //
        this.label2.AutoSize = true;
        this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.label2.Location = new System.Drawing.Point(4, 51);
        this.label2.Name = "label2";
        this.label2.Size = new System.Drawing.Size(71, 17);
        this.label2.TabIndex = 18;
        this.label2.Text = "Package :";
        //
        // label5
        //
        this.label5.AutoSize = true;
        this.label5.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.label5.Location = new System.Drawing.Point(4, 16);
        this.label5.Name = "label5";
        this.label5.Size = new System.Drawing.Size(60, 17);
        this.label5.TabIndex = 17;
        this.label5.Text = "Project :";
        //
        // groupBox2
        //
        this.groupBox2.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                 | System.Windows.Forms.AnchorStyles.Right)));
        this.groupBox2.Controls.Add(this.CmbxPrjSrce);
        this.groupBox2.Controls.Add(this.CmbxPkgListSrce);
        this.groupBox2.Controls.Add(this.BtnRefresh);
        this.groupBox2.Controls.Add(this.label4);
        this.groupBox2.Controls.Add(this.label3);
        this.groupBox2.Location = new System.Drawing.Point(3, 3);
        this.groupBox2.Name = "groupBox2";
        this.groupBox2.Size = new System.Drawing.Size(350, 84);
        this.groupBox2.TabIndex = 20;
        this.groupBox2.TabStop = false;
        this.groupBox2.Text = "Source";
        //
        // CmbxPrjSrce
        //
        this.CmbxPrjSrce.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                   | System.Windows.Forms.AnchorStyles.Right)));
        this.CmbxPrjSrce.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmbxPrjSrce.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmbxPrjSrce.FormattingEnabled = true;
        this.CmbxPrjSrce.Location = new System.Drawing.Point(78, 15);
        this.CmbxPrjSrce.MaxDropDownItems = 20;
        this.CmbxPrjSrce.Name = "CmbxPrjSrce";
        this.CmbxPrjSrce.Size = new System.Drawing.Size(266, 21);
        this.CmbxPrjSrce.TabIndex = 19;
        this.CmbxPrjSrce.TextChanged += new System.EventHandler(this.CmbxPrjSrce_TextChanged);
        //
        // CmbxPkgListSrce
        //
        this.CmbxPkgListSrce.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                       | System.Windows.Forms.AnchorStyles.Right)));
        this.CmbxPkgListSrce.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmbxPkgListSrce.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmbxPkgListSrce.FormattingEnabled = true;
        this.CmbxPkgListSrce.Location = new System.Drawing.Point(78, 47);
        this.CmbxPkgListSrce.MaxDropDownItems = 20;
        this.CmbxPkgListSrce.Name = "CmbxPkgListSrce";
        this.CmbxPkgListSrce.Size = new System.Drawing.Size(232, 21);
        this.CmbxPkgListSrce.TabIndex = 17;
        //
        // BtnRefresh
        //
        this.BtnRefresh.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnRefresh.BackgroundImage = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefresh.Location = new System.Drawing.Point(320, 47);
        this.BtnRefresh.Name = "BtnRefresh";
        this.BtnRefresh.Size = new System.Drawing.Size(24, 23);
        this.BtnRefresh.TabIndex = 18;
        this.BtnRefresh.UseVisualStyleBackColor = true;
        this.BtnRefresh.Click += new System.EventHandler(this.BtnRefresh_Click);
        //
        // label4
        //
        this.label4.AutoSize = true;
        this.label4.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.label4.Location = new System.Drawing.Point(4, 51);
        this.label4.Name = "label4";
        this.label4.Size = new System.Drawing.Size(71, 17);
        this.label4.TabIndex = 16;
        this.label4.Text = "Package :";
        //
        // label3
        //
        this.label3.AutoSize = true;
        this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.label3.Location = new System.Drawing.Point(4, 16);
        this.label3.Name = "label3";
        this.label3.Size = new System.Drawing.Size(60, 17);
        this.label3.TabIndex = 15;
        this.label3.Text = "Project :";
        //
        // BtnDoIt
        //
        this.BtnDoIt.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.BtnDoIt.Location = new System.Drawing.Point(10, 183);
        this.BtnDoIt.Name = "BtnDoIt";
        this.BtnDoIt.Size = new System.Drawing.Size(59, 23);
        this.BtnDoIt.TabIndex = 11;
        this.BtnDoIt.Text = "Do it !";
        this.BtnDoIt.UseVisualStyleBackColor = true;
        this.BtnDoIt.Click += new System.EventHandler(this.BtnDoIt_Click);
        //
        // TxtMess
        //
        this.TxtMess.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                               | System.Windows.Forms.AnchorStyles.Left)
                               | System.Windows.Forms.AnchorStyles.Right)));
        this.TxtMess.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.TxtMess.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.TxtMess.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.TxtMess.Location = new System.Drawing.Point(3, 19);
        this.TxtMess.Multiline = true;
        this.TxtMess.Name = "TxtMess";
        this.TxtMess.ScrollBars = System.Windows.Forms.ScrollBars.Both;
        this.TxtMess.Size = new System.Drawing.Size(282, 205);
        this.TxtMess.TabIndex = 12;
        this.TxtMess.WordWrap = false;
        //
        // label1
        //
        this.label1.AutoSize = true;
        this.label1.Location = new System.Drawing.Point(12, 3);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(50, 13);
        this.label1.TabIndex = 13;
        this.label1.Text = "Message";
        //
        // groupBox1
        //
        this.groupBox1.Controls.Add(this.splitContainer1);
        this.groupBox1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.groupBox1.Location = new System.Drawing.Point(0, 0);
        this.groupBox1.Name = "groupBox1";
        this.groupBox1.Size = new System.Drawing.Size(658, 246);
        this.groupBox1.TabIndex = 17;
        this.groupBox1.TabStop = false;
        this.groupBox1.Text = "Create";
        //
        // SubmitPrjPkgCreate
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.Controls.Add(this.groupBox1);
        this.Name = "SubmitPrjPkgCreate";
        this.Size = new System.Drawing.Size(658, 246);
        this.splitContainer1.Panel1.ResumeLayout(false);
        this.splitContainer1.Panel2.ResumeLayout(false);
        this.splitContainer1.Panel2.PerformLayout();
        this.splitContainer1.ResumeLayout(false);
        this.groupBox3.ResumeLayout(false);
        this.groupBox3.PerformLayout();
        this.groupBox2.ResumeLayout(false);
        this.groupBox2.PerformLayout();
        this.groupBox1.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.GroupBox groupBox1;
    private System.Windows.Forms.SplitContainer splitContainer1;
    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.TextBox TxtMess;
    private System.Windows.Forms.GroupBox groupBox2;
    private System.Windows.Forms.ComboBox CmbxPrjSrce;
    private System.Windows.Forms.ComboBox CmbxPkgListSrce;
    private System.Windows.Forms.Button BtnRefresh;
    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.GroupBox groupBox3;
    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.Label label5;
    private System.Windows.Forms.ComboBox CmbxPkgListDest;
    private System.Windows.Forms.ComboBox CmbxPrjDest;
    private System.Windows.Forms.Button BtnDoIt;

}
}
