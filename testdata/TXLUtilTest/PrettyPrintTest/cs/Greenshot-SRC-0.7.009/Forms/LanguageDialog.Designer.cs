/*
 * Erstellt mit SharpDevelop.
 * Benutzer: jens
 * Datum: 02.12.2007
 * Zeit: 22:24
 *
 * Sie können diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader ändern.
 */
namespace Greenshot.Forms
{
partial class LanguageDialog
{
    /// <summary>
    /// Designer variable used to keep track of non-visual components.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    /// Disposes resources used by the form.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
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

    /// <summary>
    /// This method is required for Windows Forms designer support.
    /// Do not change the method contents inside the source code editor. The Forms designer might
    /// not be able to load this method if it was changed manually.
    /// </summary>
    private void InitializeComponent()
    {
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(LanguageDialog));
        this.lblSelectLanguage = new System.Windows.Forms.Label();
        this.comboBoxLanguage = new System.Windows.Forms.ComboBox();
        this.btnOK = new System.Windows.Forms.Button();
        this.SuspendLayout();
        //
        // lblSelectLanguage
        //
        this.lblSelectLanguage.Location = new System.Drawing.Point(12, 9);
        this.lblSelectLanguage.Name = "lblSelectLanguage";
        this.lblSelectLanguage.Size = new System.Drawing.Size(300, 73);
        this.lblSelectLanguage.TabIndex = 0;
        this.lblSelectLanguage.Text = "Please choose your language. (You can change this setting later on in the \"Prefer" +
                                      "ences\" dialog.)\n\nBitte wählen Sie Ihre Sprache. (Sie können diese Einstellung sp" +
                                      "äter im Dialog \"Einstellungen\" ändern.)";
        //
        // comboBoxLanguage
        //
        this.comboBoxLanguage.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
        this.comboBoxLanguage.FormattingEnabled = true;
        this.comboBoxLanguage.Location = new System.Drawing.Point(13, 85);
        this.comboBoxLanguage.Name = "comboBoxLanguage";
        this.comboBoxLanguage.Size = new System.Drawing.Size(299, 21);
        this.comboBoxLanguage.TabIndex = 1;
        //
        // btnOK
        //
        this.btnOK.Location = new System.Drawing.Point(126, 112);
        this.btnOK.Name = "btnOK";
        this.btnOK.Size = new System.Drawing.Size(75, 23);
        this.btnOK.TabIndex = 2;
        this.btnOK.Text = "OK";
        this.btnOK.UseVisualStyleBackColor = true;
        this.btnOK.Click += new System.EventHandler(this.BtnOKClick);
        //
        // LanguageDialog
        //
        this.AcceptButton = this.btnOK;
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(327, 147);
        this.ControlBox = false;
        this.Controls.Add(this.btnOK);
        this.Controls.Add(this.comboBoxLanguage);
        this.Controls.Add(this.lblSelectLanguage);
        this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
        this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.Name = "LanguageDialog";
        this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
        this.Text = "Choose language / Sprache wählen";
        this.ResumeLayout(false);
    }
    private System.Windows.Forms.Label lblSelectLanguage;
    private System.Windows.Forms.Button btnOK;
    private System.Windows.Forms.ComboBox comboBoxLanguage;
}
}
