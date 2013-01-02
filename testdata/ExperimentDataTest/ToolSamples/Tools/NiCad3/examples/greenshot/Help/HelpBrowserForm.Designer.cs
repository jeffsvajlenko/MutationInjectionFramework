/*
 * Created by SharpDevelop.
 * User: jens
 * Date: 03.05.2007
 * Time: 21:49
 *
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
namespace Greenshot.Help
{
partial class HelpBrowserForm : System.Windows.Forms.Form
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
        System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(HelpBrowserForm));
        this.webBrowser1 = new System.Windows.Forms.WebBrowser();
        this.SuspendLayout();
        //
        // webBrowser1
        //
        this.webBrowser1.Dock = System.Windows.Forms.DockStyle.Fill;
        this.webBrowser1.Location = new System.Drawing.Point(0, 0);
        this.webBrowser1.MinimumSize = new System.Drawing.Size(20, 20);
        this.webBrowser1.Name = "webBrowser1";
        this.webBrowser1.Size = new System.Drawing.Size(412, 560);
        this.webBrowser1.TabIndex = 0;
        //
        // HelpBrowserForm
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(412, 560);
        this.Controls.Add(this.webBrowser1);
        this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
        this.Name = "HelpBrowserForm";
        this.Text = "Greenshot Help";
        this.ResumeLayout(false);
    }
    private System.Windows.Forms.WebBrowser webBrowser1;
}
}
