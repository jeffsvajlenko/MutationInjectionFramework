/*
 * Erstellt mit SharpDevelop.
 * Benutzer: jens
 * Datum: 11.02.2008
 * Zeit: 22:53
 *
 * Sie können diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader ändern.
 */
namespace Greenshot.Forms
{
partial class PrintOptionsDialog
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
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(PrintOptionsDialog));
        this.checkbox_dontaskagain = new System.Windows.Forms.CheckBox();
        this.checkboxAllowShrink = new System.Windows.Forms.CheckBox();
        this.checkboxAllowEnlarge = new System.Windows.Forms.CheckBox();
        this.checkboxAllowCenter = new System.Windows.Forms.CheckBox();
        this.checkboxAllowRotate = new System.Windows.Forms.CheckBox();
        this.button_ok = new System.Windows.Forms.Button();
        this.SuspendLayout();
        //
        // checkbox_dontaskagain
        //
        this.checkbox_dontaskagain.CheckAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkbox_dontaskagain.ImageAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkbox_dontaskagain.Location = new System.Drawing.Point(12, 138);
        this.checkbox_dontaskagain.Name = "checkbox_dontaskagain";
        this.checkbox_dontaskagain.Size = new System.Drawing.Size(331, 39);
        this.checkbox_dontaskagain.TabIndex = 19;
        this.checkbox_dontaskagain.Text = "Save as default and do not ask again.";
        this.checkbox_dontaskagain.TextAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkbox_dontaskagain.UseVisualStyleBackColor = true;
        //
        // checkboxAllowShrink
        //
        this.checkboxAllowShrink.CheckAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowShrink.ImageAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowShrink.Location = new System.Drawing.Point(12, 20);
        this.checkboxAllowShrink.Name = "checkboxAllowShrink";
        this.checkboxAllowShrink.Size = new System.Drawing.Size(331, 39);
        this.checkboxAllowShrink.TabIndex = 21;
        this.checkboxAllowShrink.Text = "Shrink large printouts to paper size.";
        this.checkboxAllowShrink.TextAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowShrink.UseVisualStyleBackColor = true;
        //
        // checkboxAllowEnlarge
        //
        this.checkboxAllowEnlarge.CheckAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowEnlarge.ImageAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowEnlarge.Location = new System.Drawing.Point(12, 43);
        this.checkboxAllowEnlarge.Name = "checkboxAllowEnlarge";
        this.checkboxAllowEnlarge.Size = new System.Drawing.Size(331, 39);
        this.checkboxAllowEnlarge.TabIndex = 22;
        this.checkboxAllowEnlarge.Text = "Enlarge small printouts to paper size.";
        this.checkboxAllowEnlarge.TextAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowEnlarge.UseVisualStyleBackColor = true;
        //
        // checkboxAllowCenter
        //
        this.checkboxAllowCenter.CheckAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowCenter.ImageAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowCenter.Location = new System.Drawing.Point(12, 90);
        this.checkboxAllowCenter.Name = "checkboxAllowCenter";
        this.checkboxAllowCenter.Size = new System.Drawing.Size(331, 29);
        this.checkboxAllowCenter.TabIndex = 24;
        this.checkboxAllowCenter.Text = "Align printouts centered on the page.";
        this.checkboxAllowCenter.TextAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowCenter.UseVisualStyleBackColor = true;
        //
        // checkboxAllowRotate
        //
        this.checkboxAllowRotate.CheckAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowRotate.ImageAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowRotate.Location = new System.Drawing.Point(12, 67);
        this.checkboxAllowRotate.Name = "checkboxAllowRotate";
        this.checkboxAllowRotate.Size = new System.Drawing.Size(331, 39);
        this.checkboxAllowRotate.TabIndex = 23;
        this.checkboxAllowRotate.Text = "Rotate printouts to page orientation.";
        this.checkboxAllowRotate.TextAlign = System.Drawing.ContentAlignment.TopLeft;
        this.checkboxAllowRotate.UseVisualStyleBackColor = true;
        //
        // button_ok
        //
        this.button_ok.DialogResult = System.Windows.Forms.DialogResult.Cancel;
        this.button_ok.Location = new System.Drawing.Point(268, 170);
        this.button_ok.Name = "button_ok";
        this.button_ok.Size = new System.Drawing.Size(75, 23);
        this.button_ok.TabIndex = 25;
        this.button_ok.Text = "OK";
        this.button_ok.UseVisualStyleBackColor = true;
        //
        // PrintOptionsDialog
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(355, 205);
        this.Controls.Add(this.button_ok);
        this.Controls.Add(this.checkbox_dontaskagain);
        this.Controls.Add(this.checkboxAllowCenter);
        this.Controls.Add(this.checkboxAllowRotate);
        this.Controls.Add(this.checkboxAllowEnlarge);
        this.Controls.Add(this.checkboxAllowShrink);
        this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.Name = "PrintOptionsDialog";
        this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
        this.Text = "PrintOptionsDialog";
        this.ResumeLayout(false);
    }
    private System.Windows.Forms.Button button_ok;
    private System.Windows.Forms.CheckBox checkboxAllowRotate;
    private System.Windows.Forms.CheckBox checkboxAllowCenter;
    private System.Windows.Forms.CheckBox checkboxAllowEnlarge;
    private System.Windows.Forms.CheckBox checkboxAllowShrink;
    private System.Windows.Forms.CheckBox checkbox_dontaskagain;
}
}
