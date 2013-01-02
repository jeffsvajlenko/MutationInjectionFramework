namespace Greenshot
{
partial class ColorDialog : System.Windows.Forms.Form
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
        this.btnTransparent = new System.Windows.Forms.Button();
        this.colorPanel = new System.Windows.Forms.Panel();
        this.labelHtmlColor = new System.Windows.Forms.Label();
        this.textBoxHtmlColor = new System.Windows.Forms.TextBox();
        this.labelRed = new System.Windows.Forms.Label();
        this.labelGreen = new System.Windows.Forms.Label();
        this.labelBlue = new System.Windows.Forms.Label();
        this.textBoxRed = new System.Windows.Forms.TextBox();
        this.textBoxGreen = new System.Windows.Forms.TextBox();
        this.textBoxBlue = new System.Windows.Forms.TextBox();
        this.labelRecentColors = new System.Windows.Forms.Label();
        this.textBoxAlpha = new System.Windows.Forms.TextBox();
        this.labelAlpha = new System.Windows.Forms.Label();
        this.btnApply = new System.Windows.Forms.Button();
        this.SuspendLayout();
        //
        // btnTransparent
        //
        this.btnTransparent.BackColor = System.Drawing.Color.Transparent;
        this.btnTransparent.Location = new System.Drawing.Point(210, 4);
        this.btnTransparent.Name = "btnTransparent";
        this.btnTransparent.Size = new System.Drawing.Size(78, 23);
        this.btnTransparent.TabIndex = 0;
        this.btnTransparent.TabStop = false;
        this.btnTransparent.Text = "Transparent";
        this.btnTransparent.UseVisualStyleBackColor = false;
        this.btnTransparent.Click += new System.EventHandler(this.btnTransparentClick);
        //
        // colorPanel
        //
        this.colorPanel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
        this.colorPanel.Location = new System.Drawing.Point(210, 31);
        this.colorPanel.Name = "colorPanel";
        this.colorPanel.Size = new System.Drawing.Size(78, 23);
        this.colorPanel.TabIndex = 1;
        //
        // labelHtmlColor
        //
        this.labelHtmlColor.Font = new System.Drawing.Font("Tahoma", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.World);
        this.labelHtmlColor.Location = new System.Drawing.Point(210, 57);
        this.labelHtmlColor.Name = "labelHtmlColor";
        this.labelHtmlColor.Size = new System.Drawing.Size(78, 17);
        this.labelHtmlColor.TabIndex = 2;
        this.labelHtmlColor.Text = "HTML color";
        //
        // textBoxHtmlColor
        //
        this.textBoxHtmlColor.Location = new System.Drawing.Point(210, 71);
        this.textBoxHtmlColor.Name = "textBoxHtmlColor";
        this.textBoxHtmlColor.Size = new System.Drawing.Size(78, 20);
        this.textBoxHtmlColor.TabIndex = 1;
        this.textBoxHtmlColor.Click += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxHtmlColor.GotFocus += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxHtmlColor.TextChanged += new System.EventHandler(this.TextBoxHexadecimalTextChanged);
        this.textBoxHtmlColor.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBoxKeyDown);
        //
        // labelRed
        //
        this.labelRed.Font = new System.Drawing.Font("Tahoma", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.World);
        this.labelRed.Location = new System.Drawing.Point(210, 98);
        this.labelRed.Name = "labelRed";
        this.labelRed.Size = new System.Drawing.Size(78, 18);
        this.labelRed.TabIndex = 4;
        this.labelRed.Text = "Red";
        //
        // labelGreen
        //
        this.labelGreen.Font = new System.Drawing.Font("Tahoma", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.World);
        this.labelGreen.Location = new System.Drawing.Point(210, 122);
        this.labelGreen.Name = "labelGreen";
        this.labelGreen.Size = new System.Drawing.Size(78, 18);
        this.labelGreen.TabIndex = 5;
        this.labelGreen.Text = "Green";
        //
        // labelBlue
        //
        this.labelBlue.Font = new System.Drawing.Font("Tahoma", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.World);
        this.labelBlue.Location = new System.Drawing.Point(210, 146);
        this.labelBlue.Name = "labelBlue";
        this.labelBlue.Size = new System.Drawing.Size(78, 18);
        this.labelBlue.TabIndex = 6;
        this.labelBlue.Text = "Blue";
        //
        // textBoxRed
        //
        this.textBoxRed.Location = new System.Drawing.Point(258, 95);
        this.textBoxRed.Name = "textBoxRed";
        this.textBoxRed.Size = new System.Drawing.Size(30, 20);
        this.textBoxRed.TabIndex = 2;
        this.textBoxRed.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
        this.textBoxRed.Click += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxRed.GotFocus += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxRed.TextChanged += new System.EventHandler(this.TextBoxRGBTextChanged);
        this.textBoxRed.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBoxKeyDown);
        //
        // textBoxGreen
        //
        this.textBoxGreen.Location = new System.Drawing.Point(258, 119);
        this.textBoxGreen.Name = "textBoxGreen";
        this.textBoxGreen.Size = new System.Drawing.Size(30, 20);
        this.textBoxGreen.TabIndex = 3;
        this.textBoxGreen.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
        this.textBoxGreen.Click += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxGreen.GotFocus += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxGreen.TextChanged += new System.EventHandler(this.TextBoxRGBTextChanged);
        this.textBoxGreen.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBoxKeyDown);
        //
        // textBoxBlue
        //
        this.textBoxBlue.Location = new System.Drawing.Point(258, 143);
        this.textBoxBlue.Name = "textBoxBlue";
        this.textBoxBlue.Size = new System.Drawing.Size(30, 20);
        this.textBoxBlue.TabIndex = 4;
        this.textBoxBlue.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
        this.textBoxBlue.Click += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxBlue.GotFocus += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxBlue.TextChanged += new System.EventHandler(this.TextBoxRGBTextChanged);
        this.textBoxBlue.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBoxKeyDown);
        //
        // labelRecentColors
        //
        this.labelRecentColors.Location = new System.Drawing.Point(3, 175);
        this.labelRecentColors.Name = "labelRecentColors";
        this.labelRecentColors.Size = new System.Drawing.Size(148, 13);
        this.labelRecentColors.TabIndex = 10;
        this.labelRecentColors.Text = "Recently used colors";
        //
        // textBoxAlpha
        //
        this.textBoxAlpha.Location = new System.Drawing.Point(258, 167);
        this.textBoxAlpha.Name = "textBoxAlpha";
        this.textBoxAlpha.Size = new System.Drawing.Size(30, 20);
        this.textBoxAlpha.TabIndex = 5;
        this.textBoxAlpha.TextAlign = System.Windows.Forms.HorizontalAlignment.Right;
        this.textBoxAlpha.Click += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxAlpha.GotFocus += new System.EventHandler(this.TextBoxGotFocus);
        this.textBoxAlpha.TextChanged += new System.EventHandler(this.TextBoxRGBTextChanged);
        this.textBoxAlpha.KeyDown += new System.Windows.Forms.KeyEventHandler(this.TextBoxKeyDown);
        //
        // labelAlpha
        //
        this.labelAlpha.Font = new System.Drawing.Font("Tahoma", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.World);
        this.labelAlpha.Location = new System.Drawing.Point(210, 170);
        this.labelAlpha.Name = "labelAlpha";
        this.labelAlpha.Size = new System.Drawing.Size(78, 18);
        this.labelAlpha.TabIndex = 11;
        this.labelAlpha.Text = "Alpha";
        //
        // btnApply
        //
        this.btnApply.BackColor = System.Drawing.Color.Transparent;
        this.btnApply.Location = new System.Drawing.Point(210, 191);
        this.btnApply.Name = "btnApply";
        this.btnApply.Size = new System.Drawing.Size(78, 23);
        this.btnApply.TabIndex = 12;
        this.btnApply.TabStop = false;
        this.btnApply.Text = "Apply";
        this.btnApply.UseVisualStyleBackColor = false;
        this.btnApply.Click += new System.EventHandler(this.BtnApplyClick);
        //
        // ColorDialog
        //
        this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
        this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        this.ClientSize = new System.Drawing.Size(292, 218);
        this.Controls.Add(this.btnApply);
        this.Controls.Add(this.textBoxAlpha);
        this.Controls.Add(this.labelAlpha);
        this.Controls.Add(this.labelRecentColors);
        this.Controls.Add(this.textBoxBlue);
        this.Controls.Add(this.textBoxGreen);
        this.Controls.Add(this.textBoxRed);
        this.Controls.Add(this.labelBlue);
        this.Controls.Add(this.labelGreen);
        this.Controls.Add(this.labelRed);
        this.Controls.Add(this.textBoxHtmlColor);
        this.Controls.Add(this.labelHtmlColor);
        this.Controls.Add(this.colorPanel);
        this.Controls.Add(this.btnTransparent);
        this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
        this.MaximizeBox = false;
        this.MinimizeBox = false;
        this.Name = "ColorDialog";
        this.ShowIcon = false;
        this.ShowInTaskbar = false;
        this.SizeGripStyle = System.Windows.Forms.SizeGripStyle.Hide;
        this.Text = "TestProject";
        this.ResumeLayout(false);
        this.PerformLayout();
    }
    private System.Windows.Forms.Label labelRed;
    private System.Windows.Forms.Label labelGreen;
    private System.Windows.Forms.Label labelBlue;
    private System.Windows.Forms.TextBox textBoxHtmlColor;
    private System.Windows.Forms.Label labelRecentColors;
    private System.Windows.Forms.Label labelAlpha;
    private System.Windows.Forms.Label labelHtmlColor;
    private System.Windows.Forms.Button btnApply;
    private System.Windows.Forms.TextBox textBoxAlpha;
    private System.Windows.Forms.TextBox textBoxRed;
    private System.Windows.Forms.TextBox textBoxGreen;
    private System.Windows.Forms.TextBox textBoxBlue;
    private System.Windows.Forms.Panel colorPanel;
    private System.Windows.Forms.Button btnTransparent;





}
}
