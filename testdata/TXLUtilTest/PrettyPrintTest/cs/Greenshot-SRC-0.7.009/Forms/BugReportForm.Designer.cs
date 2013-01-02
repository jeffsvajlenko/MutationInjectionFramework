/*
 * Erstellt mit SharpDevelop.
 * Benutzer: jens
 * Datum: 03.06.2008
 * Zeit: 21:34
 *
 * Sie können diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader ändern.
 */
namespace Greenshot.Forms
{
partial class BugReportForm
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
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(BugReportForm));
        this.labelBugReportInfo = new System.Windows.Forms.Label();
        this.textBoxDescription = new System.Windows.Forms.TextBox();
        this.btnClose = new System.Windows.Forms.Button();
        this.linkLblBugs = new System.Windows.Forms.LinkLabel();
        this.SuspendLayout();
        //
        // labelBugReportInfo
        //
        this.labelBugReportInfo.Location = new System.Drawing.Point(12, 9);
        this.labelBugReportInfo.Name = "labelBugReportInfo";
        this.labelBugReportInfo.Size = new System.Drawing.Size(481, 112);
        this.labelBugReportInfo.TabIndex = 0;
        this.labelBugReportInfo.Text = resources.GetString("labelBugReportInfo.Text");
        //
        // textBoxDescription
        //
        this.textBoxDescription.Location = new System.Drawing.Point(12, 153);
        this.textBoxDescription.Multiline = true;
        this.textBoxDescription.Name = "textBoxDescription";
        this.textBoxDescription.Size = new System.Drawing.Size(481, 190);
        this.textBoxDescription.TabIndex = 1;
        //
        // btnClose
        //
        this.btnClose.DialogResult = System.Windows.Forms.DialogResult.Cancel;
        this.btnClose.Location = new System.Drawing.Point(354, 349);
        this.btnClose.Name = "btnClose";
        this.btnClose.Size = new System.Drawing.Size(139, 23);
        this.btnClose.TabIndex = 2;
        this.btnClose.Text = "Close";
        this.btnClose.UseVisualStyleBackColor = true;
        //
        // linkLblBugs
        //
        this.linkLblBugs.Location = new System.Drawing.Point(12, 127);
        this.linkLblBugs.Name = "linkLblBugs";
        this.linkLblBugs.Size = new System.Drawing.Size(465, 23);
        this.linkLblBugs.TabIndex = 9;
        this.linkLblBugs.TabStop = true;
        this.linkLblBugs.Text = "http://sourceforge.net/tracker/?group_id=191585&atid=937972";
        this.linkLblBugs.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.LinkLblBugsLinkClicked);
        //
        // BugReportForm
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.CancelButton = this.btnClose;
        this.ClientSize = new System.Drawing.Size(505, 384);
        this.Controls.Add(this.linkLblBugs);
        this.Controls.Add(this.btnClose);
        this.Controls.Add(this.textBoxDescription);
        this.Controls.Add(this.labelBugReportInfo);
        this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
        this.Name = "BugReportForm";
        this.Text = "Error";
        this.ResumeLayout(false);
        this.PerformLayout();
    }
    private System.Windows.Forms.LinkLabel linkLblBugs;
    private System.Windows.Forms.Button btnClose;
    private System.Windows.Forms.TextBox textBoxDescription;
    private System.Windows.Forms.Label labelBugReportInfo;
}
}
