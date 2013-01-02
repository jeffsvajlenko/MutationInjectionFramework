/*
 * Erstellt mit SharpDevelop.
 * Benutzer: thomas
 * Datum: 30.03.2007
 * Zeit: 19:53
 *
 * Sie k�nnen diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader �ndern.
 */

using System;
using System.Drawing;
using System.Windows.Forms;
using Greenshot.Configuration;

namespace Greenshot
{
/// <summary>
/// Description of TextInputForm.
/// </summary>
public partial class TextInputForm : Form
{

    public TextInputForm()
    {
        //
        // The InitializeComponent() call is required for Windows Forms designer support.
        //
        InitializeComponent();

        FontFamily[] fontFamilies = FontFamily.Families;
        for (int i = 0; i < fontFamilies.Length; i++)
        {
            FontFamily family = fontFamilies[i];
            int n = comboFonts.Items.Add(family.Name);
            if (InputText.Font.FontFamily.Equals(family))
            {
                comboFonts.SelectedIndex = n;
            }
        }
        int b = 0;
        for (int i = 8; i < 20; i += 2)
        {
            comboFontSize.Items.Add(i.ToString());
            if (InputText.Font.Size == (float) i)
            {
                comboFontSize.SelectedIndex = b;
            }
            b++;
        }
        btnBold.Checked = InputText.Font.Bold;
        btnItalic.Checked = InputText.Font.Italic;
        btnUnderline.Checked = InputText.Font.Underline;

        Font font = AppConfig.GetInstance().Editor_Font;
        if (font != null)
        {
            updateFromFont(font);
        }
        this.updateUI();
    }

    void BtnCancelClick(object sender, EventArgs e)
    {
        this.DialogResult = DialogResult.Cancel;
        InputText.Text = "";
        this.Hide();
    }

    void BtnOkClick(object sender, EventArgs e)
    {
        AppConfig.GetInstance().Editor_Font = InputText.Font;
        AppConfig.GetInstance().Store();
        this.DialogResult = DialogResult.OK;
        this.Hide();
    }

    private void setFontStyle()
    {
        FontStyle style = FontStyle.Regular;
        if (btnBold.Checked) style |= FontStyle.Bold;
        if (btnItalic.Checked) style |= FontStyle.Italic;
        if (btnUnderline.Checked) style |= FontStyle.Underline;
        InputText.Font = new Font(InputText.Font, style);
    }

    public void UpdateFromLabel(Label label)
    {
        updateFromFont(label.Font);
        InputText.ForeColor = label.ForeColor;
        InputText.Text = label.Text;
    }

    private void updateFromFont(Font font)
    {
        comboFonts.Text = font.Name;
        comboFontSize.Text = font.Size.ToString();
        btnBold.Checked = font.Bold;
        btnItalic.Checked = font.Italic;
        btnUnderline.Checked = font.Underline;
        InputText.Font = font;
    }

    void BtnBoldClick(object sender, EventArgs e)
    {
        setFontStyle();
    }

    void BtnItalicClick(object sender, EventArgs e)
    {
        setFontStyle();
    }

    void BtnUnderlineClick(object sender, EventArgs e)
    {
        setFontStyle();
    }

    void ComboFontsSelectedIndexChanged(object sender, EventArgs e)
    {
        InputText.Font = new Font(comboFonts.SelectedItem.ToString(), InputText.Font.Size);
        setFontStyle();
    }

    void ComboFontSizeTextChanged(object sender, EventArgs e)
    {
        int result = 0;
        if (int.TryParse(comboFontSize.Text, out result))
        {
            InputText.Font = new Font(InputText.Font.FontFamily, int.Parse(comboFontSize.Text));
            setFontStyle();
        }
    }

    void BtnColorClick(object sender, System.EventArgs e)
    {
        ColorDialog colorDialog = ColorDialog.GetInstance();
        colorDialog.ShowDialog();
        if(colorDialog.DialogResult != DialogResult.Cancel)
        {
            InputText.ForeColor = colorDialog.Color;
        }
    }

    void InputTextKeyDown (object sender, System.Windows.Forms.KeyEventArgs e)
    {
        if(e.Modifiers.Equals(Keys.Control) && e.KeyCode == Keys.Return)
        {
            this.BtnOkClick(sender, e);
        }
    }

    private void updateUI()
    {
        Language lang = Language.GetInstance();
        this.Text = lang.GetString("texteditor_title");
        this.btnBold.Text = lang.GetString("texteditor_bold");
        this.btnItalic.Text = lang.GetString("texteditor_italic");
        this.btnUnderline.Text = lang.GetString("texteditor_underline");
        this.btnColor.Text = lang.GetString("texteditor_color");
        this.btnOk.Text = lang.GetString("texteditor_apply");
        this.btnCancel.Text = lang.GetString("texteditor_cancel");
    }
}
}
