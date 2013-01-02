// InputBoxDialog.cs created with MonoDevelop
// User: eric at 22:38 09/02/2008
//
// Copyright (C) 2007 [Petit Eric  surfzoid@gmail.com]
//
// This program is free software; you can redistribute it and/or modify
//obtaining a copy of this software and associated documentation
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA

// InputBoxDialog.cs
//This original code was find here : http://www.knowdotnet.com/articles/inputbox-printable.html
// I added some modifications : surfzoid@gmail.com
//

using System;
using System.Drawing;
using System.Collections;
using System.ComponentModel;
using System.Windows.Forms;
using System.Diagnostics;

namespace MonoOSC
{
/// <summary>
/// Summary description for InputBox.
/// </summary>
public class InputBoxDialog : System.Windows.Forms.Form
{

    #region Windows Contols and Constructor

    private System.Windows.Forms.Label lblPrompt;
    private System.Windows.Forms.Button btnOK;
    private System.Windows.Forms.Button button1;
    private System.Windows.Forms.TextBox txtInput;

    /// <summary>
    /// Required designer variable.
    /// </summary>
    private System.ComponentModel.Container components = null;

    /// <summary>
    ///
    /// </summary>
    public InputBoxDialog()
    {
        //
        // Required for Windows Form Designer support
        //
        InitializeComponent();

        //
        // TODO: Add any constructor code after InitializeComponent call
        //
    }

    #endregion

    #region Dispose

    /// <summary>
    /// Clean up any resources being used.
    /// </summary>
    /// <param name="disposing"></param>
    protected override void Dispose(bool disposing)
    {
        if (disposing)
        {
            if (components != null)
            {
                components.Dispose();
            }
        }
        base.Dispose(disposing);
    }

    #endregion

    #region Windows Form Designer generated code

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(InputBoxDialog));
        this.lblPrompt = new System.Windows.Forms.Label();
        this.btnOK = new System.Windows.Forms.Button();
        this.button1 = new System.Windows.Forms.Button();
        this.txtInput = new System.Windows.Forms.TextBox();
        this.SuspendLayout();
        //
        // lblPrompt
        //
        this.lblPrompt.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                                 | System.Windows.Forms.AnchorStyles.Left)
                                 | System.Windows.Forms.AnchorStyles.Right)));
        this.lblPrompt.BackColor = System.Drawing.SystemColors.Control;
        this.lblPrompt.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F);
        this.lblPrompt.Location = new System.Drawing.Point(12, 9);
        this.lblPrompt.Name = "lblPrompt";
        this.lblPrompt.Size = new System.Drawing.Size(302, 82);
        this.lblPrompt.TabIndex = 3;
        //
        // btnOK
        //
        this.btnOK.DialogResult = System.Windows.Forms.DialogResult.OK;
        this.btnOK.Location = new System.Drawing.Point(326, 24);
        this.btnOK.Name = "btnOK";
        this.btnOK.Size = new System.Drawing.Size(64, 24);
        this.btnOK.TabIndex = 1;
        this.btnOK.Text = "&OK";
        this.btnOK.Click += new System.EventHandler(this.btnOK_Click);
        //
        // button1
        //
        this.button1.DialogResult = System.Windows.Forms.DialogResult.Cancel;
        this.button1.Location = new System.Drawing.Point(326, 56);
        this.button1.Name = "button1";
        this.button1.Size = new System.Drawing.Size(64, 24);
        this.button1.TabIndex = 2;
        this.button1.Text = "&Cancel";
        this.button1.Click += new System.EventHandler(this.button1_Click);
        //
        // txtInput
        //
        this.txtInput.Location = new System.Drawing.Point(8, 100);
        this.txtInput.Name = "txtInput";
        this.txtInput.Size = new System.Drawing.Size(379, 20);
        this.txtInput.TabIndex = 0;
        //
        // InputBoxDialog
        //
        this.AcceptButton = this.btnOK;
        this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
        this.CancelButton = this.button1;
        this.ClientSize = new System.Drawing.Size(398, 128);
        this.Controls.Add(this.txtInput);
        this.Controls.Add(this.button1);
        this.Controls.Add(this.btnOK);
        this.Controls.Add(this.lblPrompt);
        this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
        this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.Name = "InputBoxDialog";
        this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
        this.Text = "InputBox";
        this.TopMost = true;
        this.Load += new System.EventHandler(this.InputBox_Load);
        this.ResumeLayout(false);
        this.PerformLayout();

    }
    #endregion

    #region Private Variables
    string formCaption = string.Empty;
    string formPrompt = string.Empty;
    string inputResponse = string.Empty;
    string defaultValue = string.Empty;
    #endregion

    #region Public Properties
    /// <summary>
    ///
    /// </summary>
    public string FormCaption
    {
        get
        {
            return formCaption;
        }
        set
        {
            formCaption = value;
        }
    } // property FormCaption
    /// <summary>
    ///
    /// </summary>
    public string FormPrompt
    {
        get
        {
            return formPrompt;
        }
        set
        {
            formPrompt = value;
        }
    } // property FormPrompt
    /// <summary>
    ///
    /// </summary>
    public string InputResponse
    {
        get
        {
            return inputResponse;
        }
        set
        {
            inputResponse = value;
        }
    } // property InputResponse
    /// <summary>
    ///
    /// </summary>
    public string DefaultValue
    {
        get
        {
            return defaultValue;
        }
        set
        {
            defaultValue = value;
        }
    } // property DefaultValue

    #endregion

    #region Form and Control Events
    private void InputBox_Load(object sender, System.EventArgs e)
    {
        try
        {
            this.txtInput.Text = defaultValue;
            this.lblPrompt.Text = formPrompt;
            this.Text = formCaption;
            this.txtInput.SelectionStart = 0;
            this.txtInput.SelectionLength = this.txtInput.Text.Length;
            this.txtInput.Focus();
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }


    private void btnOK_Click(object sender, System.EventArgs e)
    {
        InputResponse = this.txtInput.Text;
        this.Close();
    }

    private void button1_Click(object sender, System.EventArgs e)
    {
        this.Close();
    }
    #endregion


}
}
