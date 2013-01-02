// TestForm.Designer.cs created with MonoDevelop
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
partial class TestForm
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
        this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
        this.BtnTest = new System.Windows.Forms.Button();
        this.CmBxFunct = new System.Windows.Forms.ComboBox();
        this.TxtOut = new System.Windows.Forms.TextBox();
        this.splitContainer1 = new System.Windows.Forms.SplitContainer();
        this.label4 = new System.Windows.Forms.Label();
        this.label3 = new System.Windows.Forms.Label();
        this.label2 = new System.Windows.Forms.Label();
        this.label1 = new System.Windows.Forms.Label();
        this.TxtPass = new System.Windows.Forms.TextBox();
        this.TxtUser = new System.Windows.Forms.TextBox();
        this.TxtProject = new System.Windows.Forms.TextBox();
        this.splitContainer1.Panel1.SuspendLayout();
        this.splitContainer1.Panel2.SuspendLayout();
        this.splitContainer1.SuspendLayout();
        this.SuspendLayout();
        //
        // BtnTest
        //
        this.BtnTest.Location = new System.Drawing.Point(618, 12);
        this.BtnTest.Name = "BtnTest";
        this.BtnTest.Size = new System.Drawing.Size(78, 27);
        this.BtnTest.TabIndex = 0;
        this.BtnTest.Text = "Test";
        this.BtnTest.UseVisualStyleBackColor = true;
        this.BtnTest.Click += new System.EventHandler(this.BtnTest_Click);
        //
        // CmBxFunct
        //
        this.CmBxFunct.FormattingEnabled = true;
        this.CmBxFunct.Items.AddRange(new object[]
        {
            "GET about",
            "GET source/home:<UserName>",
            "GET source/home:<UserName>/_pubkey",
            "POST source/home:<UserName>/?cmd=createkey",
            "GET source",
            "GET source/<project>/_tags",
            "GET /source/<project>/_meta"
        });
        this.CmBxFunct.Location = new System.Drawing.Point(373, 16);
        this.CmBxFunct.Name = "CmBxFunct";
        this.CmBxFunct.Size = new System.Drawing.Size(205, 21);
        this.CmBxFunct.TabIndex = 1;
        this.CmBxFunct.Text = "GET about";
        //
        // TxtOut
        //
        this.TxtOut.Dock = System.Windows.Forms.DockStyle.Fill;
        this.TxtOut.Location = new System.Drawing.Point(0, 0);
        this.TxtOut.Multiline = true;
        this.TxtOut.Name = "TxtOut";
        this.TxtOut.ScrollBars = System.Windows.Forms.ScrollBars.Both;
        this.TxtOut.Size = new System.Drawing.Size(708, 341);
        this.TxtOut.TabIndex = 2;
        this.TxtOut.WordWrap = false;
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
        this.splitContainer1.Panel1.Controls.Add(this.label4);
        this.splitContainer1.Panel1.Controls.Add(this.label3);
        this.splitContainer1.Panel1.Controls.Add(this.label2);
        this.splitContainer1.Panel1.Controls.Add(this.label1);
        this.splitContainer1.Panel1.Controls.Add(this.TxtPass);
        this.splitContainer1.Panel1.Controls.Add(this.TxtUser);
        this.splitContainer1.Panel1.Controls.Add(this.TxtProject);
        this.splitContainer1.Panel1.Controls.Add(this.CmBxFunct);
        this.splitContainer1.Panel1.Controls.Add(this.BtnTest);
        //
        // splitContainer1.Panel2
        //
        this.splitContainer1.Panel2.Controls.Add(this.TxtOut);
        this.splitContainer1.Size = new System.Drawing.Size(708, 421);
        this.splitContainer1.SplitterDistance = 76;
        this.splitContainer1.TabIndex = 3;
        //
        // label4
        //
        this.label4.AutoSize = true;
        this.label4.Location = new System.Drawing.Point(298, 52);
        this.label4.Name = "label4";
        this.label4.Size = new System.Drawing.Size(40, 13);
        this.label4.TabIndex = 16;
        this.label4.Text = "Project";
        //
        // label3
        //
        this.label3.AutoSize = true;
        this.label3.Location = new System.Drawing.Point(298, 22);
        this.label3.Name = "label3";
        this.label3.Size = new System.Drawing.Size(67, 13);
        this.label3.TabIndex = 7;
        this.label3.Text = "Function List";
        //
        // label2
        //
        this.label2.AutoSize = true;
        this.label2.Location = new System.Drawing.Point(12, 52);
        this.label2.Name = "label2";
        this.label2.Size = new System.Drawing.Size(53, 13);
        this.label2.TabIndex = 6;
        this.label2.Text = "Password";
        //
        // label1
        //
        this.label1.AutoSize = true;
        this.label1.Location = new System.Drawing.Point(12, 26);
        this.label1.Name = "label1";
        this.label1.Size = new System.Drawing.Size(57, 13);
        this.label1.TabIndex = 5;
        this.label1.Text = "UserName";
        //
        // TxtPass
        //
        this.TxtPass.Location = new System.Drawing.Point(76, 45);
        this.TxtPass.Name = "TxtPass";
        this.TxtPass.PasswordChar = '*';
        this.TxtPass.Size = new System.Drawing.Size(121, 20);
        this.TxtPass.TabIndex = 4;
        this.TxtPass.Validated += new System.EventHandler(this.TxtUser_Validated);
        //
        // TxtUser
        //
        this.TxtUser.Location = new System.Drawing.Point(76, 19);
        this.TxtUser.Name = "TxtUser";
        this.TxtUser.Size = new System.Drawing.Size(121, 20);
        this.TxtUser.TabIndex = 3;
        this.TxtUser.Text = "surfzoid";
        this.TxtUser.Validated += new System.EventHandler(this.TxtUser_Validated);
        //
        // TxtProject
        //
        this.TxtProject.Location = new System.Drawing.Point(373, 45);
        this.TxtProject.Name = "TxtProject";
        this.TxtProject.Size = new System.Drawing.Size(121, 20);
        this.TxtProject.TabIndex = 15;
        this.TxtProject.Validated += new System.EventHandler(this.TxtUser_Validated);
        //
        // TestForm
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(708, 421);
        this.Controls.Add(this.splitContainer1);
        this.Name = "TestForm";
        this.Text = "MonoOSC";
        this.splitContainer1.Panel1.ResumeLayout(false);
        this.splitContainer1.Panel1.PerformLayout();
        this.splitContainer1.Panel2.ResumeLayout(false);
        this.splitContainer1.Panel2.PerformLayout();
        this.splitContainer1.ResumeLayout(false);
        this.ResumeLayout(false);

    }

    #endregion

    private System.Windows.Forms.Button BtnTest;
    private System.Windows.Forms.ComboBox CmBxFunct;
    private System.Windows.Forms.TextBox TxtOut;
    private System.Windows.Forms.SplitContainer splitContainer1;
    private System.Windows.Forms.TextBox TxtPass;
    private System.Windows.Forms.TextBox TxtUser;
    private System.Windows.Forms.TextBox TxtProject;
    private System.Windows.Forms.Label label4;
    private System.Windows.Forms.Label label3;
    private System.Windows.Forms.Label label2;
    private System.Windows.Forms.Label label1;
}
}

