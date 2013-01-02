using System;
using System.Drawing;
using System.Windows.Forms;
using System.Collections.Generic;
using System.Globalization;
using Greenshot.Configuration;

namespace Greenshot.Forms
{
/// <summary>
/// Description of LanguageDialog.
/// </summary>
public partial class LanguageDialog : Form
{
    public LanguageDialog()
    {
        //
        // The InitializeComponent() call is required for Windows Forms designer support.
        //
        InitializeComponent();
        FillLanguageCombo();

    }

    public string Language
    {
        get
        {
            return comboBoxLanguage.SelectedValue.ToString();
        }
    }

    private void FillLanguageCombo()
    {
        List<CultureInfo> langs = new List<CultureInfo>();
        for(int i=0; i<RuntimeConfig.SupportedLanguages.Length; i++)
        {
            CultureInfo ci = new CultureInfo(RuntimeConfig.SupportedLanguages[i]);
            langs.Add(ci);
        }
        comboBoxLanguage.DataSource = langs;
        comboBoxLanguage.DisplayMember = "NativeName";
        comboBoxLanguage.ValueMember = "Name";
    }

    void BtnOKClick(object sender, EventArgs e)
    {
        this.Close();
    }
}
}
