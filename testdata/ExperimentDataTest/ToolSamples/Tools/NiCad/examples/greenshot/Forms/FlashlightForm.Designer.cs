/*
 * Created by SharpDevelop.
 * User: jens
 * Date: 17.04.2007
 * Time: 21:25
 *
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
namespace Greenshot
{
partial class FlashlightForm : System.Windows.Forms.Form
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
        this.SuspendLayout();
        //
        // FlashlightForm
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.BackColor = System.Drawing.Color.White;
        this.ClientSize = new System.Drawing.Size(292, 266);
        this.ControlBox = false;
        this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.None;
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.Name = "FlashlightForm";
        this.ShowIcon = false;
        this.ShowInTaskbar = false;
        this.Text = "FlashlightForm";
        this.TopMost = true;
        this.Load += new System.EventHandler(this.FlashlightForm_Load);
        this.ResumeLayout(false);
    }
}
}
