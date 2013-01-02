/*
 * Erstellt mit SharpDevelop.
 * Benutzer: thomas
 * Datum: 22.03.2007
 * Zeit: 23:09
 *
 * Sie k�nnen diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader �ndern.
 */

using System;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Windows.Forms;
using System.Collections;
using System.Collections.Generic;
using Greenshot.Help;
using Greenshot.Helpers;
using Greenshot.Configuration;
using Greenshot.Drawing;
using System.Drawing.Printing;
using System.Drawing.Imaging;
using System.Resources;
using System.Diagnostics;
using System.IO;
using System.Text.RegularExpressions;

namespace Greenshot
{
/// <summary>
/// Description of ImageEditorForm.
/// </summary>
public partial class ImageEditorForm : Form
{
    private AppConfig conf = AppConfig.GetInstance();
    private Language lang;

    private ColorDialog colorDialog = ColorDialog.GetInstance();

    private string lastSaveFullPath;

    private Surface surface;

    public ImageEditorForm()
    {
        //
        // The InitializeComponent() call is required for Windows Forms designer support.
        //
        InitializeComponent();

        // init surface
        surface = new Surface();
        surface.SizeMode = PictureBoxSizeMode.AutoSize;
        surface.TabStop = false;
        surface.MovingElementChanged += new SurfaceElementEventHandler(surfaceMovingElementChanged);
        panel1.Controls.Add(surface);

        if (conf.Editor_WindowSize != null)
        {
            Size = (Size) conf.Editor_WindowSize;
        }
        lang = Language.GetInstance();
        updateUI();
        this.colorDialog.RecentColors = conf.Editor_RecentColors;
        this.comboBoxThickness.Text = conf.Editor_Thickness.ToString();
    }

    private void updateUI()
    {

        this.Text = lang.GetString("editor_title");
        this.fileStripMenuItem.Text = lang.GetString("editor_file");
        this.btnSave.Text = lang.GetString("editor_save");
        this.saveToolStripMenuItem.Text = lang.GetString("editor_save");
        this.saveAsToolStripMenuItem.Text = lang.GetString("editor_saveas");
        this.btnClipboard.Text = lang.GetString("editor_copyimagetoclipboard");
        this.copyImageToClipboardToolStripMenuItem.Text = lang.GetString("editor_copyimagetoclipboard");

        this.btnPrint.Text = lang.GetString("editor_print");
        this.printToolStripMenuItem.Text = lang.GetString("editor_print")+"...";
        this.closeToolStripMenuItem.Text = lang.GetString("editor_close");

        this.editToolStripMenuItem.Text = lang.GetString("editor_edit");
        this.btnCursor.Text = lang.GetString("editor_cursortool");
        this.btnRect.Text = lang.GetString("editor_drawrectangle");
        this.addRectangleToolStripMenuItem.Text = lang.GetString("editor_drawrectangle");
        this.btnEllipse.Text = lang.GetString("editor_drawellipse");
        this.addEllipseToolStripMenuItem.Text = lang.GetString("editor_drawellipse");
        this.btnText.Text = lang.GetString("editor_drawtextbox");
        this.addTextBoxToolStripMenuItem.Text = lang.GetString("editor_drawtextbox");
        this.btnLine.Text = lang.GetString("editor_drawline");
        this.drawLineToolStripMenuItem.Text = lang.GetString("editor_drawline");
        this.btnArrow.Text = lang.GetString("editor_drawarrow");
        this.drawArrowToolStripMenuItem.Text = lang.GetString("editor_drawarrow");
        this.btnDelete.Text = lang.GetString("editor_deleteelement");
        this.removeObjectToolStripMenuItem.Text = lang.GetString("editor_deleteelement");
        this.btnSettings.Text = lang.GetString("contextmenu_settings");
        this.preferencesToolStripMenuItem.Text = lang.GetString("contextmenu_settings");

        this.objectToolStripMenuItem.Text = lang.GetString("editor_object");
        this.btnCut.Text = lang.GetString("editor_cuttoclipboard");
        this.cutToolStripMenuItem.Text = lang.GetString("editor_cuttoclipboard");
        this.btnCopy.Text = lang.GetString("editor_copytoclipboard");
        this.copyToolStripMenuItem.Text = lang.GetString("editor_copytoclipboard");
        this.btnPaste.Text = lang.GetString("editor_pastefromclipboard");
        this.pasteToolStripMenuItem.Text = lang.GetString("editor_pastefromclipboard");
        this.duplicateToolStripMenuItem.Text = lang.GetString("editor_duplicate");

        this.arrangeToolStripMenuItem.Text = lang.GetString("editor_arrange");
        this.upToTopToolStripMenuItem.Text = lang.GetString("editor_uptotop");
        this.upOneLevelToolStripMenuItem.Text = lang.GetString("editor_uponelevel");
        this.downOneLevelToolStripMenuItem.Text = lang.GetString("editor_downonelevel");
        this.downToBottomToolStripMenuItem.Text = lang.GetString("editor_downtobottom");
        this.btnBorderColor.Text = lang.GetString("editor_forecolor");
        this.borderColorToolStripMenuItem.Text = lang.GetString("editor_forecolor");
        this.btnBackColor.Text = lang.GetString("editor_backcolor");
        this.backgroundColorToolStripMenuItem.Text = lang.GetString("editor_backcolor");
        this.labelLineThickness.Text = lang.GetString("editor_thickness");
        this.lineThicknessToolStripMenuItem.Text = lang.GetString("editor_thickness");
        this.btnArrowHeads.Text = lang.GetString("editor_arrowheads");
        this.arrowHeadsToolStripMenuItem.Text = lang.GetString("editor_arrowheads");
        this.arrowHeadsStartPointToolStripMenuItem.Text = lang.GetString("editor_arrowheadsstartpoint");
        this.arrowHeadsStartPointToolStripMenuItem1.Text = lang.GetString("editor_arrowheadsstartpoint");
        this.arrowHeadsEndPointToolStripMenuItem.Text = lang.GetString("editor_arrowheadsendpoint");
        this.arrowHeadsEndPointToolStripMenuItem1.Text = lang.GetString("editor_arrowheadsendpoint");
        this.arrowHeadsBothToolStripMenuItem.Text = lang.GetString("editor_arrowheadsboth");
        this.arrowHeadsBothToolStripMenuItem1.Text = lang.GetString("editor_arrowheadsboth");
        this.arrowHeadsNoneToolStripMenuItem.Text = lang.GetString("editor_arrowheadsnone");
        this.arrowHeadsNoneToolStripMenuItem1.Text = lang.GetString("editor_arrowheadsnone");

        this.helpToolStripMenuItem.Text = lang.GetString("contextmenu_help");
        this.helpToolStripMenuItem1.Text = lang.GetString("contextmenu_help");
        this.btnHelp.Text = lang.GetString("contextmenu_help");

        this.aboutToolStripMenuItem.Text = lang.GetString("contextmenu_about");

        this.copyPathMenuItem.Text = lang.GetString("editor_copypathtoclipboard");
        this.openDirectoryMenuItem.Text = lang.GetString("editor_opendirinexplorer");
    }

    public void SetImage(Image img)
    {
        surface.Image = img;
    }
    public void SetImagePath(string fullpath)
    {
        this.lastSaveFullPath = fullpath;
        if(fullpath == null) return;
        updateStatusLabel(lang.GetString("editor_imagesaved").Replace("%storagelocation%",fullpath),fileSavedStatusContextMenu);
        this.Text = lang.GetString("editor_title") + " - " + Path.GetFileName(fullpath);
        this.saveToolStripMenuItem.Enabled = true;
    }

    private void surfaceMovingElementChanged(object sender, DrawableContainerList selectedElements)
    {
        bool elementSelected = (selectedElements.Count > 0);
        this.btnCopy.Enabled = elementSelected;
        this.btnCut.Enabled = elementSelected;
        this.btnDelete.Enabled = elementSelected;
        this.copyToolStripMenuItem.Enabled = elementSelected;
        this.cutToolStripMenuItem.Enabled = elementSelected;
        this.duplicateToolStripMenuItem.Enabled = elementSelected;
        this.removeObjectToolStripMenuItem.Enabled = elementSelected;
        this.borderColorToolStripMenuItem.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.LINECOLOR));
        this.btnBorderColor.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.LINECOLOR));
        this.backgroundColorToolStripMenuItem.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.FILLCOLOR));
        this.btnBackColor.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.FILLCOLOR));
        this.lineThickness1ToolStripMenuItem.Enabled = this.lineThicknessToolStripMenuItem.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.THICKNESS));
        this.comboBoxThickness.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.THICKNESS));
        this.btnArrowHeads.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.ARROWHEADS));
        this.arrowHeadsToolStripMenuItem.Enabled = (elementSelected && selectedElements.PropertySupported(DrawableContainer.Property.ARROWHEADS));

        bool push = surface.CanPushSelectionDown();
        bool pull = surface.CanPullSelectionUp();
        this.arrangeToolStripMenuItem.Enabled = (push || pull);
        if(this.arrangeToolStripMenuItem.Enabled)
        {
            this.upToTopToolStripMenuItem.Enabled = pull;
            this.upOneLevelToolStripMenuItem.Enabled = pull;
            this.downToBottomToolStripMenuItem.Enabled = push;
            this.downOneLevelToolStripMenuItem.Enabled = push;
        }
    }

    #region filesystem options
    void SaveToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        try
        {
            ImageOutput.Save(surface.GetImageForExport(), lastSaveFullPath);
            updateStatusLabel(lang.GetString("editor_imagesaved").Replace("%storagelocation%",lastSaveFullPath),fileSavedStatusContextMenu);
        }
        catch(System.Runtime.InteropServices.ExternalException)
        {
            MessageBox.Show(lang.GetString("error_nowriteaccess").Replace("%path%",lastSaveFullPath).Replace(@"\\",@"\"), lang.GetString("error"));
        }
    }
    void BtnSaveClick(object sender, EventArgs e)
    {
        if (lastSaveFullPath != null) SaveToolStripMenuItemClick(sender,e);
        else SaveAsToolStripMenuItemClick(sender, e);
    }

    void SaveAsToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        lastSaveFullPath = ImageOutput.SaveWithDialog(surface.GetImageForExport());
        if(lastSaveFullPath != null)
        {
            SetImagePath(lastSaveFullPath);
            updateStatusLabel(lang.GetString("editor_imagesaved").Replace("%storagelocation%",lastSaveFullPath),fileSavedStatusContextMenu);
        }
        else
        {
            clearStatusLabel();
        }
    }

    void CopyImageToClipboardToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        try
        {
            ImageOutput.PrepareClipboardObject();
            ImageOutput.CopyToClipboard(surface.GetImageForExport());
            updateStatusLabel(lang.GetString("editor_storedtoclipboard"));
        }
        catch (Exception)
        {
            updateStatusLabel(lang.GetString("editor_clipboardfailed"));
        }
    }
    void BtnClipboardClick(object sender, EventArgs e)
    {
        this.CopyImageToClipboardToolStripMenuItemClick(sender, e);
    }
    void PrintToolStripMenuItemClick(object sender, EventArgs e)
    {
        PrintHelper ph = new PrintHelper(surface.GetImageForExport());
        PrinterSettings ps = ph.PrintWithDialog();
        if(ps != null)
        {
            updateStatusLabel(lang.GetString("editor_senttoprinter").Replace("%printername%",ps.PrinterName));
        }
    }
    void BtnPrintClick(object sender, EventArgs e)
    {
        PrintToolStripMenuItemClick(sender, e);
    }

    void CloseToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        this.Close();
    }
    #endregion

    #region drawing options
    void BtnEllipseClick(object sender, EventArgs e)
    {
        surface.DrawingMode = Surface.DrawingModes.Ellipse;
        btnCursor.Checked = false;
        btnRect.Checked = false;
        btnEllipse.Checked = true;
        btnText.Checked = false;
        btnLine.Checked = false;
        btnArrow.Checked = false;
    }

    void BtnCursorClick(object sender, EventArgs e)
    {
        surface.DrawingMode = Surface.DrawingModes.None;
        btnCursor.Checked = true;
        btnRect.Checked = false;
        btnEllipse.Checked = false;
        btnText.Checked = false;
        btnLine.Checked = false;
        btnArrow.Checked = false;
    }

    void BtnRectClick(object sender, EventArgs e)
    {
        surface.DrawingMode = Surface.DrawingModes.Rect;
        btnCursor.Checked = false;
        btnRect.Checked = true;
        btnEllipse.Checked = false;
        btnText.Checked = false;
        btnLine.Checked = false;
        btnArrow.Checked = false;
    }

    void BtnTextClick(object sender, EventArgs e)
    {
        surface.DrawingMode = Surface.DrawingModes.Text;
        btnCursor.Checked = false;
        btnRect.Checked = false;
        btnEllipse.Checked = false;
        btnText.Checked = true;
        btnLine.Checked = false;
        btnArrow.Checked = false;
    }

    void BtnLineClick(object sender, EventArgs e)
    {
        surface.DrawingMode = Surface.DrawingModes.Line;
        btnCursor.Checked = false;
        btnRect.Checked = false;
        btnEllipse.Checked = false;
        btnText.Checked = false;
        btnLine.Checked = true;
        btnArrow.Checked = false;
    }

    void BtnArrowClick(object sender, EventArgs e)
    {
        surface.DrawingMode = Surface.DrawingModes.Arrow;
        btnCursor.Checked = false;
        btnRect.Checked = false;
        btnEllipse.Checked = false;
        btnText.Checked = false;
        btnLine.Checked = false;
        btnArrow.Checked = true;
    }

    void AddRectangleToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        BtnRectClick(sender, e);
    }

    void AddEllipseToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        BtnEllipseClick(sender, e);
    }

    void AddTextBoxToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        BtnTextClick(sender, e);
    }

    void DrawLineToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        BtnLineClick(sender, e);
    }

    void DrawArrowToolStripMenuItemClick(object sender, EventArgs e)
    {
        BtnArrowClick(sender, e);
    }


    void RemoveObjectToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        surface.RemoveSelectedElements();
    }
    void BtnDeleteClick(object sender, EventArgs e)
    {
        RemoveObjectToolStripMenuItemClick(sender, e);
    }
    #endregion

    #region copy&paste options
    void CutToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        if (surface.CutSelectedElements())
        {
            this.btnPaste.Enabled = true;
            this.pasteToolStripMenuItem.Enabled = true;
        }
    }
    void BtnCutClick(object sender, System.EventArgs e)
    {
        CutToolStripMenuItemClick(sender, e);
    }

    void CopyToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        if (surface.CopySelectedElements())
        {
            this.btnPaste.Enabled = true;
            this.pasteToolStripMenuItem.Enabled = true;
        }
    }
    void BtnCopyClick(object sender, System.EventArgs e)
    {
        CopyToolStripMenuItemClick(sender, e);
    }

    void PasteToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        surface.PasteElementFromClipboard();
    }
    void BtnPasteClick(object sender, System.EventArgs e)
    {
        PasteToolStripMenuItemClick(sender, e);
    }

    void DuplicateToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        surface.DuplicateSelectedElements();
    }
    #endregion

    #region element properties
    void UpOneLevelToolStripMenuItemClick(object sender, EventArgs e)
    {
        surface.PullElementsUp();
    }

    void DownOneLevelToolStripMenuItemClick(object sender, EventArgs e)
    {
        surface.PushElementsDown();
    }

    void UpToTopToolStripMenuItemClick(object sender, EventArgs e)
    {
        surface.PullElementsToTop();
    }

    void DownToBottomToolStripMenuItemClick(object sender, EventArgs e)
    {
        surface.PushElementsToBottom();
    }

    void BtnBorderColorClick(object sender, System.EventArgs e)
    {
        SelectBorderColorToolStripMenuItemClick(sender, e);
    }
    void BtnBackColorClick(object sender, System.EventArgs e)
    {
        SelectBackgroundColorToolStripMenuItemClick(sender, e);
    }

    void SelectBorderColorToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        colorDialog.Color = surface.ForeColor;
        colorDialog.ShowDialog();
        if (colorDialog.DialogResult != DialogResult.Cancel)
        {
            conf.Editor_ForeColor = colorDialog.Color;
            conf.Editor_RecentColors = colorDialog.RecentColors;
            conf.Store();
            surface.ForeColor = colorDialog.Color;
        }
    }

    void SelectBackgroundColorToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        colorDialog.Color = surface.BackColor;
        colorDialog.ShowDialog();
        if (colorDialog.DialogResult != DialogResult.Cancel)
        {
            conf.Editor_BackColor = colorDialog.Color;
            conf.Editor_RecentColors = colorDialog.RecentColors;
            conf.Store();
            surface.BackColor = colorDialog.Color;
        }
    }
    void ThicknessComboBoxChanged(object sender, System.EventArgs e)
    {
        ToolStripComboBox cb = (ToolStripComboBox)sender;
        try
        {
            int t = Int32.Parse(cb.Text);
            surface.Thickness = t;
            conf.Editor_Thickness = t;
            conf.Store();
        }
        catch (Exception )
        {
            cb.Text = conf.Editor_Thickness.ToString();
        }
    }

    void LineThicknessValueToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        ToolStripMenuItem cb = (ToolStripMenuItem)sender;
        try
        {
            int t = Int32.Parse(cb.Text);
            surface.Thickness = t;
            conf.Editor_Thickness = t;
            conf.Store();
        }
        catch (Exception )
        {
            cb.Text = conf.Editor_Thickness.ToString();
        }
    }

    void ArrowHeadsStartPointToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        surface.ArrowHead = Surface.ArrowHeads.Start;
    }

    void ArrowHeadsEndPointToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        surface.ArrowHead = Surface.ArrowHeads.End;
    }

    void ArrowHeadsBothToolStripMenuItemClick(object sender, EventArgs e)
    {
        surface.ArrowHead = Surface.ArrowHeads.Both;
    }

    void ArrowHeadsNoneToolStripMenuItemClick(object sender, EventArgs e)
    {
        surface.ArrowHead = Surface.ArrowHeads.None;
    }
    #endregion

    #region help
    void HelpToolStripMenuItem1Click(object sender, System.EventArgs e)
    {
        new HelpBrowserForm(conf.Ui_Language).Show();
    }
    void AboutToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        new AboutForm().Show();
    }
    void PreferencesToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        new SettingsForm().Show();
    }
    void BtnSettingsClick(object sender, System.EventArgs e)
    {
        PreferencesToolStripMenuItemClick(sender, e);
    }
    void BtnHelpClick(object sender, System.EventArgs e)
    {
        HelpToolStripMenuItem1Click(sender, e);
    }
    #endregion

    #region image editor event handlers
    void ImageEditorFormActivated(object sender, EventArgs e)
    {
        bool b = checkClipboardContents();
        this.btnPaste.Enabled = b;
        this.pasteToolStripMenuItem.Enabled = b;
    }

    void ImageEditorFormFormClosing(object sender, FormClosingEventArgs e)
    {
        conf.Editor_WindowSize = Size;
        conf.Store();
        System.GC.Collect();
    }

    void ImageEditorFormKeyUp(object sender, KeyEventArgs e)
    {
        if (Keys.Escape.Equals(e.KeyCode))
        {
            BtnCursorClick(sender, e);
        }
        else if (Keys.R.Equals(e.KeyCode))
        {
            BtnRectClick(sender, e);
        }
        else if (Keys.E.Equals(e.KeyCode))
        {
            BtnEllipseClick(sender, e);
        }
        else if (Keys.L.Equals(e.KeyCode))
        {
            BtnLineClick(sender, e);
        }
        else if (Keys.A.Equals(e.KeyCode))
        {
            BtnArrowClick(sender, e);
        }
        else if (Keys.T.Equals(e.KeyCode))
        {
            BtnTextClick(sender, e);
        }
    }
    #endregion

    #region cursor key strokes
    protected override bool ProcessCmdKey(ref Message msg, Keys k)
    {
        surface.ProcessCmdKey(k);
        return base.ProcessCmdKey(ref msg, k);
    }
    #endregion

    #region helpers
    private bool checkClipboardContents()
    {
        IDataObject ido = Clipboard.GetDataObject();
        string [] df = ido.GetFormats();
        //for(int i=0; i< df.Length; i++) Debug.WriteLine(df[i]);
        return ido.GetDataPresent(typeof(DrawableContainer));
    }
    #endregion



    #region status label handling
    private void updateStatusLabel(string text, ContextMenuStrip contextMenu)
    {
        statusLabel.Text = text;
        statusStrip1.ContextMenuStrip = contextMenu;
    }

    private void updateStatusLabel(string text)
    {
        updateStatusLabel(text, null);
    }
    private void clearStatusLabel()
    {
        updateStatusLabel(null, null);
    }

    void StatusLabelClicked(object sender, MouseEventArgs e)
    {
        ToolStrip ss = (StatusStrip)((ToolStripStatusLabel)sender).Owner;
        if(ss.ContextMenuStrip != null)
        {
            ss.ContextMenuStrip.Show(ss, e.X, e.Y);
        }
    }

    void CopyPathMenuItemClick(object sender, EventArgs e)
    {
        Clipboard.SetText(lastSaveFullPath);
    }

    void OpenDirectoryMenuItemClick(object sender, EventArgs e)
    {
        ProcessStartInfo psi = new ProcessStartInfo("explorer");
        psi.Arguments = Path.GetDirectoryName(lastSaveFullPath);
        psi.UseShellExecute = false;
        Process p = new Process();
        p.StartInfo = psi;
        p.Start();
    }
    #endregion



}
}
