/*
 * Created by SharpDevelop.
 * User: jens
 * Date: 24.03.2007
 * Time: 12:43
 *
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */

using System;
using System.Collections.Generic;
using System.Drawing;
using System.Globalization;
using System.Windows.Forms;
using System.IO;
using System.Threading;
using Greenshot.Configuration;
using Greenshot.Helpers;

namespace Greenshot
{
/// <summary>
/// Description of SettingsForm.
/// </summary>
public partial class SettingsForm : Form
{
    Language lang;
    AppConfig conf;
    private ToolTip toolTip;

    public SettingsForm()
    {
        InitializeComponent();
        conf = AppConfig.GetInstance();
        lang = Language.GetInstance();
        toolTip = new ToolTip();
        UpdateUI();
        FillLanguageCombo();
        this.combobox_primaryimageformat.Items.AddRange(RuntimeConfig.SupportedImageFormats);
        DisplaySettings();
    }

    private void UpdateUI()
    {
        this.Text = lang.GetString("settings_title");

        this.tab_general.Text = lang.GetString("settings_general");
        this.tab_output.Text = lang.GetString("settings_output");

        this.groupbox_applicationsettings.Text = lang.GetString("settings_applicationsettings");
        this.label_language.Text = lang.GetString("settings_language");
        toolTip.SetToolTip(label_language, lang.GetString("settings_tooltip_language"));

        this.checkbox_registerhotkeys.Text = lang.GetString("settings_registerhotkeys");
        toolTip.SetToolTip(checkbox_registerhotkeys, lang.GetString("settings_tooltip_registerhotkeys"));
        this.checkbox_autostartshortcut.Text = lang.GetString("settings_autostartshortcut");
        this.checkbox_desktopshortcut.Text = lang.GetString("settings_desktopshortcut");

        this.groupbox_destination.Text = lang.GetString("settings_destination");
        this.checkbox_clipboard.Text = lang.GetString("settings_destination_clipboard");
        this.checkbox_printer.Text = lang.GetString("settings_destination_printer");
        this.checkbox_file.Text = lang.GetString("settings_destination_file");
        this.checkbox_fileas.Text = lang.GetString("settings_destination_fileas");
        this.checkbox_editor.Text = lang.GetString("settings_destination_editor");

        this.groupbox_preferredfilesettings.Text = lang.GetString("settings_preferredfilesettings");

        this.label_storagelocation.Text = lang.GetString("settings_storagelocation");
        toolTip.SetToolTip(label_storagelocation, lang.GetString("settings_tooltip_storagelocation"));

        this.label_screenshotname.Text = lang.GetString("settings_filenamepattern");
        toolTip.SetToolTip(label_screenshotname, lang.GetString("settings_tooltip_filenamepattern"));

        this.label_primaryimageformat.Text = lang.GetString("settings_primaryimageformat");
        this.checkbox_copypathtoclipboard.Text = lang.GetString("settings_copypathtoclipboard");
        toolTip.SetToolTip(label_primaryimageformat, lang.GetString("settings_tooltip_primaryimageformat"));

        this.groupbox_jpegsettings.Text = lang.GetString("settings_jpegsettings");
        this.label_jpegquality.Text = lang.GetString("settings_jpegquality");
        this.checkbox_alwaysshowjpegqualitydialog.Text = lang.GetString("settings_alwaysshowjpegqualitydialog");

        this.groupbox_visualisation.Text = lang.GetString("settings_visualization");
        this.checkbox_showflashlight.Text = lang.GetString("settings_showflashlight");
        this.checkbox_playsound.Text = lang.GetString("settings_playsound");

        this.groupbox_printoptions.Text = lang.GetString("settings_printoptions");
        this.checkboxAllowCenter.Text = lang.GetString("printoptions_allowcenter");
        this.checkboxAllowEnlarge.Text = lang.GetString("printoptions_allowenlarge");
        this.checkboxAllowRotate.Text = lang.GetString("printoptions_allowrotate");
        this.checkboxAllowShrink.Text = lang.GetString("printoptions_allowshrink");
        this.checkbox_alwaysshowprintoptionsdialog.Text = lang.GetString("settings_alwaysshowprintoptionsdialog");


    }

    private void DisplaySettings()
    {
        combobox_language.SelectedValue = conf.Ui_Language;
        checkbox_registerhotkeys.Checked = (bool)conf.General_RegisterHotkeys;
        textbox_storagelocation.Text = conf.Output_File_Path;
        textbox_screenshotname.Text = conf.Output_File_FilenamePattern;
        combobox_primaryimageformat.Text = conf.Output_File_Format.ToString();
        checkbox_copypathtoclipboard.Checked = (bool)conf.Output_File_CopyPathToClipboard;
        trackBarJpegQuality.Value = conf.Output_File_JpegQuality;
        textBoxJpegQuality.Text = conf.Output_File_JpegQuality+"%";
        checkbox_alwaysshowjpegqualitydialog.Checked = (bool)conf.Output_File_PromptJpegQuality;
        checkbox_showflashlight.Checked = (bool)conf.Ui_Effects_Flashlight;
        checkbox_playsound.Checked = (bool)conf.Ui_Effects_CameraSound;

        checkbox_clipboard.Checked = (conf.Output_Destinations&ScreenshotDestinations.Clipboard) == ScreenshotDestinations.Clipboard;
        checkbox_file.Checked = (conf.Output_Destinations&ScreenshotDestinations.FileDefault) == ScreenshotDestinations.FileDefault;
        checkbox_fileas.Checked = (conf.Output_Destinations&ScreenshotDestinations.FileWithDialog) == ScreenshotDestinations.FileWithDialog;
        checkbox_printer.Checked = (conf.Output_Destinations&ScreenshotDestinations.Printer) == ScreenshotDestinations.Printer;
        checkbox_editor.Checked = (conf.Output_Destinations&ScreenshotDestinations.Editor) == ScreenshotDestinations.Editor;

        checkboxAllowCenter.Checked = (bool)conf.Output_Print_Center;
        checkboxAllowEnlarge.Checked = (bool)conf.Output_Print_AllowEnlarge;
        checkboxAllowRotate.Checked = (bool)conf.Output_Print_AllowRotate;
        checkboxAllowShrink.Checked = (bool)conf.Output_Print_AllowShrink;
        checkbox_alwaysshowprintoptionsdialog.Checked = (bool)conf.Output_Print_PromptOptions;

        checkbox_autostartshortcut.Checked = ShortcutManager.shortcutExists(Environment.SpecialFolder.Startup);
        checkbox_desktopshortcut.Checked = ShortcutManager.shortcutExists(Environment.SpecialFolder.Desktop);

    }
    private void SaveSettings()
    {
        conf.Ui_Language = combobox_language.SelectedValue.ToString();
        conf.General_RegisterHotkeys = (bool?)checkbox_registerhotkeys.Checked;
        conf.Output_File_Path = textbox_storagelocation.Text;
        conf.Output_File_FilenamePattern = textbox_screenshotname.Text;
        conf.Output_File_Format = combobox_primaryimageformat.Text;
        conf.Output_File_CopyPathToClipboard = (bool?)checkbox_copypathtoclipboard.Checked;
        conf.Output_File_JpegQuality = trackBarJpegQuality.Value;
        conf.Output_File_PromptJpegQuality = (bool?)checkbox_alwaysshowjpegqualitydialog.Checked;
        conf.Ui_Effects_Flashlight = (bool?)checkbox_showflashlight.Checked;
        conf.Ui_Effects_CameraSound = (bool?)checkbox_playsound.Checked;
        ScreenshotDestinations dest = 0;
        if(checkbox_clipboard.Checked) dest |= ScreenshotDestinations.Clipboard;
        if(checkbox_file.Checked) dest |= ScreenshotDestinations.FileDefault;
        if(checkbox_fileas.Checked) dest |= ScreenshotDestinations.FileWithDialog;
        if(checkbox_printer.Checked) dest |= ScreenshotDestinations.Printer;
        if(checkbox_editor.Checked) dest |= ScreenshotDestinations.Editor;
        conf.Output_Destinations = dest;
        conf.Output_Print_Center = (bool?)checkboxAllowCenter.Checked;
        conf.Output_Print_AllowEnlarge = (bool?)checkboxAllowEnlarge.Checked;
        conf.Output_Print_AllowRotate = (bool?)checkboxAllowRotate.Checked;
        conf.Output_Print_AllowShrink = (bool?)checkboxAllowShrink.Checked;
        conf.Output_Print_PromptOptions = (bool?)checkbox_alwaysshowprintoptionsdialog.Checked;

        conf.Store();

        if(checkbox_autostartshortcut.Checked) ShortcutManager.createShortcut(Environment.SpecialFolder.Startup);
        else ShortcutManager.removeShortcut(Environment.SpecialFolder.Startup);
        if(checkbox_desktopshortcut.Checked) ShortcutManager.createShortcut(Environment.SpecialFolder.Desktop);
        else ShortcutManager.removeShortcut(Environment.SpecialFolder.Desktop);
    }

    private void FillLanguageCombo()
    {
        List<CultureInfo> langs = new List<CultureInfo>();
        for(int i=0; i<RuntimeConfig.SupportedLanguages.Length; i++)
        {
            CultureInfo ci = new CultureInfo(RuntimeConfig.SupportedLanguages[i]);
            langs.Add(ci);
        }
        combobox_language.DataSource = langs;
        combobox_language.DisplayMember = "NativeName";
        combobox_language.ValueMember = "Name";
    }

    void Settings_cancelClick(object sender, System.EventArgs e)
    {
        this.Close();
    }

    void Settings_okayClick(object sender, System.EventArgs e)
    {
        SaveSettings();
        this.Close();
    }

    void BrowseClick(object sender, System.EventArgs e)
    {
        this.folderBrowserDialog1.SelectedPath = this.textbox_storagelocation.Text;
        if(this.folderBrowserDialog1.ShowDialog() == DialogResult.OK)
        {
            this.textbox_storagelocation.Text = this.folderBrowserDialog1.SelectedPath;
        }
    }

    void TrackBarJpegQualityScroll(object sender, System.EventArgs e)
    {
        textBoxJpegQuality.Text = trackBarJpegQuality.Value.ToString();
    }


    void BtnPatternHelpClick(object sender, EventArgs e)
    {
        MessageBox.Show(lang.GetString("settings_message_filenamepattern"),lang.GetString("settings_filenamepattern"));
    }
}
}
