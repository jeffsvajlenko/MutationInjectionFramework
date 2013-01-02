//
// SubmitPrjPkgList.Designer.cs
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
partial class SubmitPrjPkgList
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
        this.label1 = new System.Windows.Forms.Label();
        this.label2 = new System.Windows.Forms.Label();
        this.button1 = new System.Windows.Forms.Button();
        this.CmbxPkgList = new System.Windows.Forms.ComboBox();
        this.BtnRefresh = new System.Windows.Forms.Button();
        this.ChkBxShowAllReq = new System.Windows.Forms.CheckBox();
        this.label3 = new System.Windows.Forms.Label();
        this.TxtUser = new System.Windows.Forms.TextBox();
        this.CmbxState = new System.Windows.Forms.ComboBox();
        this.BtnGetState = new System.Windows.Forms.Button();
        this.label4 = new System.Windows.Forms.Label();
        this.groupBox1 = new System.Windows.Forms.GroupBox();
        this.groupBox2 = new System.Windows.Forms.GroupBox();
        this.groupBox3 = new System.Windows.Forms.GroupBox();
        this.CmBxFilter = new System.Windows.Forms.ComboBox();
        this.radioButtonHisto = new System.Windows.Forms.RadioButton();
        this.radioButtonState = new System.Windows.Forms.RadioButton();
        this.CmbxPrj = new System.Windows.Forms.ComboBox();
        this.groupBox1.SuspendLayout();
        this.groupBox2.SuspendLayout();
        this.groupBox3.SuspendLayout();
        this.SuspendLayout();
        //
        // label1
        //
        this.label1.AutoSize = true;
        this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.label1.Location = new System.Drawing.Point(14, 27);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(60, 17);
        this.label1.TabIndex = 1;
        this.label1.Text = "Project :";
        //
        // label2
        //
        this.label2.AutoSize = true;
        this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.label2.Location = new System.Drawing.Point(14, 62);
        this.label2.Name = "label2";
        this.label2.Size = new System.Drawing.Size(71, 17);
        this.label2.TabIndex = 3;
        this.label2.Text = "Package :";
        //
        // button1
        //
        this.button1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.button1.Location = new System.Drawing.Point(396, 105);
        this.button1.Name = "button1";
        this.button1.Size = new System.Drawing.Size(59, 23);
        this.button1.TabIndex = 4;
        this.button1.Text = "Get !";
        this.button1.UseVisualStyleBackColor = true;
        this.button1.Click += new System.EventHandler(this.button1_Click);
        //
        // CmbxPkgList
        //
        this.CmbxPkgList.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                   | System.Windows.Forms.AnchorStyles.Right)));
        this.CmbxPkgList.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmbxPkgList.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmbxPkgList.FormattingEnabled = true;
        this.CmbxPkgList.Location = new System.Drawing.Point(91, 59);
        this.CmbxPkgList.MaxDropDownItems = 20;
        this.CmbxPkgList.Name = "CmbxPkgList";
        this.CmbxPkgList.Size = new System.Drawing.Size(303, 21);
        this.CmbxPkgList.TabIndex = 5;
        //
        // BtnRefresh
        //
        this.BtnRefresh.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.BtnRefresh.BackgroundImage = global::MonoOSC.Properties.Resources.Refresh;
        this.BtnRefresh.Location = new System.Drawing.Point(400, 59);
        this.BtnRefresh.Name = "BtnRefresh";
        this.BtnRefresh.Size = new System.Drawing.Size(24, 23);
        this.BtnRefresh.TabIndex = 6;
        this.BtnRefresh.UseVisualStyleBackColor = true;
        this.BtnRefresh.Click += new System.EventHandler(this.BtnRefresh_Click);
        //
        // ChkBxShowAllReq
        //
        this.ChkBxShowAllReq.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
        this.ChkBxShowAllReq.AutoSize = true;
        this.ChkBxShowAllReq.Location = new System.Drawing.Point(6, 15);
        this.ChkBxShowAllReq.Name = "ChkBxShowAllReq";
        this.ChkBxShowAllReq.Size = new System.Drawing.Size(160, 17);
        this.ChkBxShowAllReq.TabIndex = 7;
        this.ChkBxShowAllReq.Text = "Show all package(s) request";
        this.ChkBxShowAllReq.UseVisualStyleBackColor = true;
        this.ChkBxShowAllReq.CheckedChanged += new System.EventHandler(this.ChkBxShowAllReq_CheckedChanged);
        //
        // label3
        //
        this.label3.AutoSize = true;
        this.label3.Location = new System.Drawing.Point(7, 25);
        this.label3.Name = "label3";
        this.label3.Size = new System.Drawing.Size(47, 13);
        this.label3.TabIndex = 8;
        this.label3.Text = "For User";
        //
        // TxtUser
        //
        this.TxtUser.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.TxtUser.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.TxtUser.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.TxtUser.Location = new System.Drawing.Point(68, 19);
        this.TxtUser.Name = "TxtUser";
        this.TxtUser.Size = new System.Drawing.Size(108, 23);
        this.TxtUser.TabIndex = 9;
        //
        // CmbxState
        //
        this.CmbxState.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmbxState.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmbxState.FormattingEnabled = true;
        this.CmbxState.Items.AddRange(new object[]
        {
            "new",
            "deleted",
            "revoked",
            "declined",
            "accepted"
        });
        this.CmbxState.Location = new System.Drawing.Point(252, 19);
        this.CmbxState.MaxDropDownItems = 20;
        this.CmbxState.Name = "CmbxState";
        this.CmbxState.Size = new System.Drawing.Size(138, 21);
        this.CmbxState.TabIndex = 10;
        this.CmbxState.Text = "new";
        //
        // BtnGetState
        //
        this.BtnGetState.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F);
        this.BtnGetState.Location = new System.Drawing.Point(396, 17);
        this.BtnGetState.Name = "BtnGetState";
        this.BtnGetState.Size = new System.Drawing.Size(59, 23);
        this.BtnGetState.TabIndex = 11;
        this.BtnGetState.Text = "Get !";
        this.BtnGetState.UseVisualStyleBackColor = true;
        this.BtnGetState.Click += new System.EventHandler(this.BtnGetState_Click);
        //
        // label4
        //
        this.label4.AutoSize = true;
        this.label4.Location = new System.Drawing.Point(178, 25);
        this.label4.Name = "label4";
        this.label4.Size = new System.Drawing.Size(72, 13);
        this.label4.TabIndex = 12;
        this.label4.Text = "where state is";
        //
        // groupBox1
        //
        this.groupBox1.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                 | System.Windows.Forms.AnchorStyles.Right)));
        this.groupBox1.Controls.Add(this.TxtUser);
        this.groupBox1.Controls.Add(this.label4);
        this.groupBox1.Controls.Add(this.label3);
        this.groupBox1.Controls.Add(this.BtnGetState);
        this.groupBox1.Controls.Add(this.CmbxState);
        this.groupBox1.Location = new System.Drawing.Point(3, 168);
        this.groupBox1.Name = "groupBox1";
        this.groupBox1.Size = new System.Drawing.Size(468, 56);
        this.groupBox1.TabIndex = 13;
        this.groupBox1.TabStop = false;
        this.groupBox1.Text = "By User";
        //
        // groupBox2
        //
        this.groupBox2.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                                 | System.Windows.Forms.AnchorStyles.Right)));
        this.groupBox2.Controls.Add(this.groupBox3);
        this.groupBox2.Controls.Add(this.CmbxPrj);
        this.groupBox2.Controls.Add(this.BtnRefresh);
        this.groupBox2.Controls.Add(this.label1);
        this.groupBox2.Controls.Add(this.label2);
        this.groupBox2.Controls.Add(this.CmbxPkgList);
        this.groupBox2.Controls.Add(this.button1);
        this.groupBox2.Location = new System.Drawing.Point(3, 3);
        this.groupBox2.Name = "groupBox2";
        this.groupBox2.Size = new System.Drawing.Size(468, 162);
        this.groupBox2.TabIndex = 14;
        this.groupBox2.TabStop = false;
        this.groupBox2.Text = "By Project";
        //
        // groupBox3
        //
        this.groupBox3.Controls.Add(this.CmBxFilter);
        this.groupBox3.Controls.Add(this.radioButtonHisto);
        this.groupBox3.Controls.Add(this.ChkBxShowAllReq);
        this.groupBox3.Controls.Add(this.radioButtonState);
        this.groupBox3.Location = new System.Drawing.Point(91, 86);
        this.groupBox3.Name = "groupBox3";
        this.groupBox3.Size = new System.Drawing.Size(225, 72);
        this.groupBox3.TabIndex = 16;
        this.groupBox3.TabStop = false;
        this.groupBox3.Text = "Filter";
        //
        // CmBxFilter
        //
        this.CmBxFilter.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmBxFilter.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmBxFilter.FormattingEnabled = true;
        this.CmBxFilter.Items.AddRange(new object[]
        {
            "All",
            "new",
            "deleted",
            "revoked",
            "declined",
            "accepted"
        });
        this.CmBxFilter.Location = new System.Drawing.Point(82, 38);
        this.CmBxFilter.MaxDropDownItems = 20;
        this.CmBxFilter.Name = "CmBxFilter";
        this.CmBxFilter.Size = new System.Drawing.Size(138, 21);
        this.CmBxFilter.TabIndex = 13;
        this.CmBxFilter.Text = "All";
        this.CmBxFilter.SelectedIndexChanged += new System.EventHandler(this.CmBxFilter_SelectedIndexChanged);
        //
        // radioButtonHisto
        //
        this.radioButtonHisto.AutoSize = true;
        this.radioButtonHisto.Enabled = false;
        this.radioButtonHisto.Location = new System.Drawing.Point(6, 49);
        this.radioButtonHisto.Name = "radioButtonHisto";
        this.radioButtonHisto.Size = new System.Drawing.Size(70, 17);
        this.radioButtonHisto.TabIndex = 15;
        this.radioButtonHisto.Text = "By history";
        this.radioButtonHisto.UseVisualStyleBackColor = true;
        //
        // radioButtonState
        //
        this.radioButtonState.AutoSize = true;
        this.radioButtonState.Checked = true;
        this.radioButtonState.Enabled = false;
        this.radioButtonState.Location = new System.Drawing.Point(6, 32);
        this.radioButtonState.Name = "radioButtonState";
        this.radioButtonState.Size = new System.Drawing.Size(63, 17);
        this.radioButtonState.TabIndex = 14;
        this.radioButtonState.TabStop = true;
        this.radioButtonState.Text = "By state";
        this.radioButtonState.UseVisualStyleBackColor = true;
        //
        // CmbxPrj
        //
        this.CmbxPrj.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left)
                               | System.Windows.Forms.AnchorStyles.Right)));
        this.CmbxPrj.AutoCompleteMode = System.Windows.Forms.AutoCompleteMode.SuggestAppend;
        this.CmbxPrj.AutoCompleteSource = System.Windows.Forms.AutoCompleteSource.AllSystemSources;
        this.CmbxPrj.FormattingEnabled = true;
        this.CmbxPrj.Location = new System.Drawing.Point(91, 26);
        this.CmbxPrj.MaxDropDownItems = 20;
        this.CmbxPrj.Name = "CmbxPrj";
        this.CmbxPrj.Size = new System.Drawing.Size(321, 21);
        this.CmbxPrj.TabIndex = 8;
        this.CmbxPrj.TextChanged += new System.EventHandler(this.CmbxPrj_TextChanged);
        //
        // SubmitPrjPkgList
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.Controls.Add(this.groupBox2);
        this.Controls.Add(this.groupBox1);
        this.Name = "SubmitPrjPkgList";
        this.Size = new System.Drawing.Size(480, 232);
        this.groupBox1.ResumeLayout(false);
        this.groupBox1.PerformLayout();
        this.groupBox2.ResumeLayout(false);
        this.groupBox2.PerformLayout();
        this.groupBox3.ResumeLayout(false);
        this.groupBox3.PerformLayout();
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.Button button1;
    private System.Windows.Forms.ComboBox CmbxPkgList;
    private System.Windows.Forms.Button BtnRefresh;
    private System.Windows.Forms.CheckBox ChkBxShowAllReq;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.TextBox TxtUser;
    private System.Windows.Forms.ComboBox CmbxState;
    private System.Windows.Forms.Button BtnGetState;
    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.GroupBox groupBox1;
    private System.Windows.Forms.GroupBox groupBox2;
    private System.Windows.Forms.ComboBox CmbxPrj;
    private System.Windows.Forms.ComboBox CmBxFilter;
    private System.Windows.Forms.RadioButton radioButtonHisto;
    private System.Windows.Forms.RadioButton radioButtonState;
    private System.Windows.Forms.GroupBox groupBox3;
}
}
