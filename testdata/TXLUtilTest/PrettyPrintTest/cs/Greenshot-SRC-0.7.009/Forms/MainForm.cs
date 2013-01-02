/*
 * Benutzer: thomas
 * Datum: 20.03.2007
 * Zeit: 20:27
 *
 */

using System;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Media;
using System.Windows.Forms;
using System.Diagnostics;
using System.Drawing.Drawing2D;
using System.Drawing.Printing;
using System.Reflection;
using System.Threading;
using Greenshot.Forms;
using Greenshot.Helpers;
using Greenshot.Help;
using Greenshot.UnmanagedHelpers;
using Greenshot.Configuration;

namespace Greenshot
{
/// <summary>
/// Description of MainForm.
/// </summary>
public partial class MainForm : Form
{
    static Mutex applicationMutex = null;

    [STAThread]
    public static void Main(string[] args)
    {
        Application.ThreadException += new System.Threading.ThreadExceptionEventHandler(Application_ThreadException);
        AppDomain.CurrentDomain.UnhandledException += new UnhandledExceptionEventHandler(CurrentDomain_UnhandledException);
        try
        {
            bool grantedOwnership;
            try
            {
                applicationMutex = new Mutex(true, @"Global\F48E86D3-E34C-4DB7-8F8F-9A0EA55F0D08", out grantedOwnership);
            }
            catch (UnauthorizedAccessException)
            {
                // mutex was reserved by another user -> application already running
                grantedOwnership = false;
            }

            // unregister application on uninstall call
            if (args.Length > 0 && args[0].Contains("uninstall"))
            {
                try
                {
                    ShortcutManager.removeShortcut(Environment.SpecialFolder.Startup);
                    ShortcutManager.removeShortcut(Environment.SpecialFolder.Desktop);
                    Application.Exit();
                }
                catch (Exception)
                {
                }
                return;
            }

            // check whether there's an instance running already
            if (!grantedOwnership)
            {
                AppConfig conf = AppConfig.GetInstance();
                Language lang = Language.GetInstance();
                MessageBox.Show(lang.GetString("error_multipleinstances"), lang.GetString("error"));
                Application.Exit();
                return;
            }
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new MainForm());
        }
        catch(Exception ex)
        {
            Application_ThreadException(MainForm.ActiveForm, new ThreadExceptionEventArgs(ex));
        }
    }


    private int mX;
    private int mY;
    private Point cursorPos = new Point(0, 0);
    private bool mouseDown = false;
    private FlashlightForm flashlightForm;
    private SoundPlayer soundPlayer;
    private Brush OverlayBrush = new SolidBrush(Color.FromArgb(50, Color.MediumSeaGreen));
    private Pen OverlayPen = new Pen(Color.FromArgb(50, Color.Black));
    private bool capturingWindows = false;
    private Queue windows = new Queue();
    private int mouseOverHwnd;
    private Rectangle captureRect;

    private AppConfig conf = AppConfig.GetInstance();
    private Language lang;
    private int hotKeyCounter = 0;

    private ToolTip tooltip;

    public MainForm()
    {
        //
        // The InitializeComponent() call is required for Windows Forms designer support.
        //
        InitializeComponent();
        lang = Language.GetInstance();
        if((bool)conf.General_RegisterHotkeys)
        {
            RegisterHotkeys();
        }
        UpdateUI();
        InitializeQuickSettingsMenu();
        tooltip = new ToolTip();
        if((bool)conf.General_IsFirstLaunch)
        {
            DoFirstLaunchThings();
            conf.General_IsFirstLaunch = false;
            conf.Store();
        }
    }

    private void DoFirstLaunchThings()
    {
        // create a shortcut in autostart dir
        ShortcutManager.createShortcut(Environment.SpecialFolder.Startup);
        // todo: display basic instructions
    }

    #region hotkeys
    protected override void WndProc(ref Message m)
    {
        if (m.Msg == User32.WM_HOTKEY)
        {
            if(Keys.Alt == Control.ModifierKeys)
            {
                initCapture(true);
            }
            else if(Keys.Control == Control.ModifierKeys)
            {
                CaptureImage(WindowCapture.CaptureWindow());
            }
            else if(Keys.Shift == Control.ModifierKeys)
            {
                if(!RuntimeConfig.LastCapturedRegion.Equals(Rectangle.Empty))
                {
                    CaptureRectangularArea(RuntimeConfig.LastCapturedRegion);
                }
            }
            else
            {
                initCapture();
            }
        }
        base.WndProc(ref m);
    }

    private void RegisterHotkeys()
    {
        bool suc = RegisterHotKey(User32.MOD_NONE, User32.VK_SNAPSHOT);
        suc &= RegisterHotKey(User32.MOD_ALT, User32.VK_SNAPSHOT);
        suc &= RegisterHotKey(User32.MOD_CTRL, User32.VK_SNAPSHOT);
        suc &= RegisterHotKey(User32.MOD_SHIFT, User32.VK_SNAPSHOT);
        if(!suc)
        {
            MessageBox.Show(lang.GetString("warning_hotkeys"),lang.GetString("warning"));
        }
    }
    private bool RegisterHotKey(int modifierKeyCode, int virtualKeyCode)
    {
        int s = User32.RegisterHotKey((int) this.Handle, hotKeyCounter, modifierKeyCode, virtualKeyCode);
        if(s != 0)
        {
            hotKeyCounter++;
            return true;
        }
        else
        {
            return false;
        }
    }

    private void UnregisterHotkeys()
    {
        for(int i=0; i<hotKeyCounter; i++)
        {
            User32.UnregisterHotKey((int) this.Handle, i);
        }
    }
    #endregion

    private void UpdateUI()
    {
        this.Text = lang.GetString("application_title");
        this.contextmenu_settings.Text = lang.GetString("contextmenu_settings");
        this.contextmenu_capturearea.Text = lang.GetString("contextmenu_capturearea");
        this.contextmenu_capturelastregion.Text = lang.GetString("contextmenu_capturelastregion");
        this.contextmenu_capturewindow.Text = lang.GetString("contextmenu_capturewindow");
        this.contextmenu_capturefullscreen.Text = lang.GetString("contextmenu_capturefullscreen");
        this.contextmenu_quicksettings.Text = lang.GetString("contextmenu_quicksettings");
        this.contextmenu_help.Text = lang.GetString("contextmenu_help");
        this.contextmenu_about.Text = lang.GetString("contextmenu_about");
        this.contextmenu_exit.Text = lang.GetString("contextmenu_exit");
    }

    #region capture
    void initCapture(bool capturingWindows)
    {
        windows.Clear();
        User32.EnumWindowsProc ewp = new User32.EnumWindowsProc(EvalWindow);
        User32.EnumWindows(ewp, 0);
        this.capturingWindows = capturingWindows;
        System.Threading.Thread.Sleep(200);
        this.SuspendLayout();
        this.Visible = false;
        this.Bounds = GetScreenBounds();
        Image img = WindowCapture.CaptureWindow();
        pictureBox.Image = img;
        this.Visible = true;
        this.ResumeLayout();

        string tt = capturingWindows ? lang.GetString("capture_tooltip_windowmode") : lang.GetString("capture_tooltip_regionmode");
        tooltip.SetToolTip(pictureBox,tt);
    }
    void initCapture()
    {
        initCapture(false);
    }

    void CaptureRectangularArea(Rectangle rect)
    {
        CaptureImage(WindowCapture.CaptureWindow(User32.GetDesktopWindow(), rect));
        // save for re-capturing later and show recapture context menu option
        RuntimeConfig.LastCapturedRegion = rect;
        this.contextmenu_capturelastregion.Enabled = true;
    }

    void CaptureImage(Image img)
    {
        DoCaptureFeedback();
        this.Hide();
        string fullPath = null;
        ImageOutput.PrepareClipboardObject();
        if ((conf.Output_Destinations & ScreenshotDestinations.FileDefault) == ScreenshotDestinations.FileDefault)
        {
            string filename = FilenameHelper.GetFilenameFromPattern(conf.Output_File_FilenamePattern, conf.Output_File_Format);
            fullPath = Path.Combine(conf.Output_File_Path,filename);
            ImageOutput.Save(img, fullPath);
        }
        if ((conf.Output_Destinations & ScreenshotDestinations.FileWithDialog) == ScreenshotDestinations.FileWithDialog)
        {
            fullPath = ImageOutput.SaveWithDialog(img);
        }
        if ((conf.Output_Destinations & ScreenshotDestinations.Clipboard) == ScreenshotDestinations.Clipboard)
        {
            ImageOutput.CopyToClipboard(img);
        }
        if ((conf.Output_Destinations & ScreenshotDestinations.Printer) == ScreenshotDestinations.Printer)
        {
            new PrintHelper(img).PrintWithDialog();
        }
        if ((conf.Output_Destinations & ScreenshotDestinations.Editor) == ScreenshotDestinations.Editor)
        {
            ImageEditorForm editor = new ImageEditorForm();
            editor.SetImage(img);
            if(fullPath != null) editor.SetImagePath(fullPath);
            editor.Show();
        }
    }

    void DoCaptureFeedback()
    {
        if((bool)conf.Ui_Effects_CameraSound)
        {
            if(soundPlayer == null)
            {
                soundPlayer = new SoundPlayer();
                SoundPlayer sp = new SoundPlayer();
                System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(FlashlightForm));
                byte[] barr = (byte[])resources.GetObject("camera");
                soundPlayer.Stream = new MemoryStream(barr);
            }
            soundPlayer.Play();
        }
        if((bool)conf.Ui_Effects_Flashlight)
        {
            if(flashlightForm == null)
            {
                flashlightForm = new FlashlightForm();
            }
            flashlightForm.FadeIn();
            flashlightForm.FadeOut();
        }
    }

    /// <summary>
    /// Get the bounds of all screens combined.
    /// </summary>
    /// <returns>A Rectangle of the bounds of the entire display area.</returns>
    public static Rectangle GetScreenBounds()
    {
        Point topLeft       = new Point(0, 0);
        Point bottomRight   = new Point(0, 0);
        foreach (Screen screen in Screen.AllScreens)
        {
            if (screen.Bounds.X < topLeft.X) topLeft.X = screen.Bounds.X;
            if (screen.Bounds.Y < topLeft.Y) topLeft.Y = screen.Bounds.Y;
            if ((screen.Bounds.X + screen.Bounds.Width) > bottomRight.X) bottomRight.X = screen.Bounds.X + screen.Bounds.Width;
            if ((screen.Bounds.Y + screen.Bounds.Height) > bottomRight.Y) bottomRight.Y = screen.Bounds.Y + screen.Bounds.Height;
        }
        return new Rectangle(topLeft.X, topLeft.Y, (bottomRight.X + Math.Abs(topLeft.X)), (bottomRight.Y + Math.Abs(topLeft.Y)));
    }
    #endregion

    #region pictureBox events
    void PictureBoxMouseDown(object sender, MouseEventArgs e)
    {
        if (e.Button == MouseButtons.Left)
        {
            mX = e.X;
            mY = e.Y;
            mouseDown = true;
            PictureBoxMouseMove(this, e);
        }
    }

    void PictureBoxMouseUp(object sender, MouseEventArgs e)
    {
        if (mouseDown)
        {
            mouseDown = false;
            capturingWindows = false;
            cursorPos.X = 0;
            cursorPos.Y = 0;
            pictureBox.Refresh();
            CaptureRectangularArea(captureRect);
        }
    }

    void PictureBoxMouseMove(object sender, MouseEventArgs e)
    {
        cursorPos.X = e.X;
        cursorPos.Y = e.Y;

        if (capturingWindows)
        {
            IEnumerator enumerator = windows.GetEnumerator();
            while (enumerator.MoveNext())
            {
                KeyValuePair<int, Rectangle> kv = (KeyValuePair<int, Rectangle>) enumerator.Current;
                int hwnd = kv.Key;
                Rectangle r = kv.Value;
                if (r.Contains(Cursor.Position))
                {
                    mouseOverHwnd = hwnd;
                    captureRect = r;
                    break;
                }
            }
        }
        else if (mouseDown)
        {
            Rectangle r = GuiRectangle.GetGuiRectangle(e.X + this.Left, e.Y + this.Top, mX - e.X, mY - e.Y);
            captureRect = r;
        }

        pictureBox.Invalidate();
    }

    void PictureBoxPaint(object sender, PaintEventArgs e)
    {
        Graphics g = e.Graphics;

        if (mouseDown || capturingWindows)
        {
            Rectangle screenbounds = GetScreenBounds();
            captureRect.Intersect(screenbounds); // crop what is outside the screen
            Rectangle fixedRect = new Rectangle( captureRect.X, captureRect.Y, captureRect.Width, captureRect.Height );
            fixedRect.X += Math.Abs( screenbounds.X );
            fixedRect.Y += Math.Abs( screenbounds.Y );

            g.FillRectangle( OverlayBrush, fixedRect );
            g.DrawRectangle( OverlayPen, fixedRect );

            // rulers
            int dist = 8;
            Pen rulerPen = new Pen(Color.SeaGreen);
            Brush bgBrush = new SolidBrush(Color.FromArgb(200, 217, 240, 227));
            Font f = new Font(FontFamily.GenericSansSerif, 8);
            int hSpace = TextRenderer.MeasureText(captureRect.Width.ToString(), f).Width + 3;
            int vSpace = TextRenderer.MeasureText(captureRect.Height.ToString(), f).Height + 3;

            // horizontal ruler
            if (fixedRect.Width > hSpace + 3)
            {
                GraphicsPath p = Drawing.RoundedRectangle.Create2(
                                     fixedRect.X + (fixedRect.Width / 2 - hSpace / 2) + 3,
                                     fixedRect.Y - dist - 7,
                                     TextRenderer.MeasureText(captureRect.Width.ToString(), f).Width - 3,
                                     TextRenderer.MeasureText(captureRect.Width.ToString(), f).Height,
                                     3);
                g.FillPath(bgBrush, p);
                g.DrawPath(rulerPen, p);
                g.DrawString(captureRect.Width.ToString(), f, rulerPen.Brush, fixedRect.X + (fixedRect.Width / 2 - hSpace / 2) + 3, fixedRect.Y - dist - 7);
                g.DrawLine(rulerPen, fixedRect.X, fixedRect.Y - dist, fixedRect.X + (fixedRect.Width / 2 - hSpace / 2), fixedRect.Y - dist);
                g.DrawLine(rulerPen, fixedRect.X + (fixedRect.Width / 2 + hSpace / 2), fixedRect.Y - dist, fixedRect.X + fixedRect.Width, fixedRect.Y - dist);
                g.DrawLine(rulerPen, fixedRect.X, fixedRect.Y - dist - 3, fixedRect.X, fixedRect.Y - dist + 3);
                g.DrawLine(rulerPen, fixedRect.X + fixedRect.Width, fixedRect.Y - dist - 3, fixedRect.X + fixedRect.Width, fixedRect.Y - dist + 3);
            }

            // vertical ruler
            if (fixedRect.Height > vSpace + 3)
            {
                GraphicsPath p = Drawing.RoundedRectangle.Create2(
                                     fixedRect.X - (TextRenderer.MeasureText(captureRect.Height.ToString(), f).Width) + 1,
                                     fixedRect.Y + (fixedRect.Height / 2 - vSpace / 2) + 2,
                                     TextRenderer.MeasureText(captureRect.Height.ToString(), f).Width - 3,
                                     TextRenderer.MeasureText(captureRect.Height.ToString(), f).Height - 1,
                                     3);
                g.FillPath(bgBrush, p);
                g.DrawPath(rulerPen, p);
                g.DrawString(captureRect.Height.ToString(), f, rulerPen.Brush, fixedRect.X - (TextRenderer.MeasureText(captureRect.Height.ToString(), f).Width) + 1, fixedRect.Y + (fixedRect.Height / 2 - vSpace / 2) + 2);
                g.DrawLine(rulerPen, fixedRect.X - dist, fixedRect.Y, fixedRect.X - dist, fixedRect.Y + (fixedRect.Height / 2 - vSpace / 2));
                g.DrawLine(rulerPen, fixedRect.X - dist, fixedRect.Y + (fixedRect.Height / 2 + vSpace / 2), fixedRect.X - dist, fixedRect.Y + fixedRect.Height);
                g.DrawLine(rulerPen, fixedRect.X - dist - 3, fixedRect.Y, fixedRect.X - dist + 3, fixedRect.Y);
                g.DrawLine(rulerPen, fixedRect.X - dist - 3, fixedRect.Y + fixedRect.Height, fixedRect.X - dist + 3, fixedRect.Y + fixedRect.Height);
            }

            // Display size of selected rectangle
            // Prepare the font and text.
            f = new Font( FontFamily.GenericSansSerif, 12 );
            string t = captureRect.Width + " x " + captureRect.Height;

            // Calculate the scaled font size.
            SizeF extent = g.MeasureString( t, f );
            float hRatio = captureRect.Height / (extent.Height * 2);
            float wRatio = captureRect.Width / (extent.Width * 2);
            float ratio = ( hRatio < wRatio ? hRatio : wRatio );
            float newSize = f.Size * ratio;

            if ( newSize >= 4 ) // Only show if 4pt or larger.
            {
                if (newSize > 20) newSize = 20;
                // Draw the size.
                f = new Font(FontFamily.GenericSansSerif, newSize, FontStyle.Bold);
                g.DrawString(t, f, Brushes.LightSeaGreen, new PointF( fixedRect.X + (
                                 captureRect.Width / 2) - (TextRenderer.MeasureText(t, f).Width / 2),
                             fixedRect.Y + (captureRect.Height / 2) - (f.GetHeight() / 2)));
            }
        }
        else
        {
            if (cursorPos.X > 0 || cursorPos.Y > 0)
            {
                Pen p = new Pen(Color.LightSeaGreen);
                p.DashStyle = DashStyle.Dot;
                Rectangle b = MainForm.GetScreenBounds();
                g.DrawLine(p, cursorPos.X, b.Y, cursorPos.X, b.Height);
                g.DrawLine(p, b.X, cursorPos.Y, b.Width, cursorPos.Y);

                Font f = new Font(FontFamily.GenericSansSerif, 8);
                string xy = cursorPos.X.ToString() + " x " + cursorPos.Y.ToString();
                Brush bgBrush = new SolidBrush(Color.FromArgb(200, 217, 240, 227));
                GraphicsPath gp = Drawing.RoundedRectangle.Create2(
                                      cursorPos.X + 5,
                                      cursorPos.Y + 5,
                                      TextRenderer.MeasureText(xy, f).Width - 3,
                                      TextRenderer.MeasureText(xy, f).Height,
                                      3);
                g.FillPath(bgBrush, gp);
                g.DrawPath(new Pen(Color.SeaGreen), gp);
                g.DrawString(xy, f, new Pen(Color.SeaGreen).Brush, cursorPos.X + 5, cursorPos.Y + 5);
            }
        }
    }
    #endregion

    #region mainform events
    void MainFormFormClosing(object sender, FormClosingEventArgs e)
    {
        conf.Store();
        UnregisterHotkeys();
        if (applicationMutex != null)
        {
            applicationMutex.ReleaseMutex();
        }
    }

    void MainFormShown(object sender, EventArgs e)
    {
        this.Hide();
    }

    void MainFormKeyDown(object sender, KeyEventArgs e)
    {
        if (e.KeyCode == Keys.Escape)
        {
            this.Hide();
            this.mouseDown = false;
        }
        else if (e.KeyCode == Keys.Space)
        {
            capturingWindows = true;
        }
        else if (e.KeyCode == Keys.Return)
        {
            CaptureImage(WindowCapture.CaptureWindow());
        }
    }
    #endregion

    private bool EvalWindow(int hWnd, int lParam)
    {
        if (!User32.IsWindowVisible(hWnd)) return true;
        if (this.Handle == new IntPtr(hWnd)) return false;

        /*StringBuilder title = new StringBuilder(256);
        User32.GetWindowText(hWnd, title, 256);
        if (title.Length == 0) return true;
        System.Diagnostics.Debug.Print(title.ToString());*/

        /*User32.EnumWindowsProc ewp = new User32.EnumWindowsProc(EvalWindow);
        User32.EnumChildWindows(hWnd, ewp, 0);*/

        User32.RECT rect = new User32.RECT();
        User32.GetWindowRect(new IntPtr(hWnd), ref rect);
        Rectangle r = GuiRectangle.GetGuiRectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);
        windows.Enqueue(new KeyValuePair<int, Rectangle>(hWnd, r));

        return true;
    }

    #region contextmenu
    void CaptureAreaToolStripMenuItemClick(object sender, EventArgs e)
    {
        initCapture();
    }

    void CaptureFullScreenToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        System.Threading.Thread.Sleep(200);
        CaptureImage(WindowCapture.CaptureWindow());
    }

    void Contextmenu_capturelastregionClick(object sender, System.EventArgs e)
    {
        if(!RuntimeConfig.LastCapturedRegion.Equals(Rectangle.Empty))
        {
            System.Threading.Thread.Sleep(200);
            CaptureRectangularArea(RuntimeConfig.LastCapturedRegion);
        }
    }
    void CaptureWindowToolStripMenuItemClick(object sender, System.EventArgs e)
    {
        initCapture(true);
    }

    void Contextmenu_settingsClick(object sender, System.EventArgs e)
    {
        SettingsForm settings = new SettingsForm();
        settings.ShowDialog();
        InitializeQuickSettingsMenu();
        this.Hide();
    }

    void Contextmenu_aboutClick(object sender, EventArgs e)
    {
        new AboutForm().Show();
    }

    void Contextmenu_helpClick(object sender, System.EventArgs e)
    {
        HelpBrowserForm hpf = new HelpBrowserForm(conf.Ui_Language);
        hpf.Show();
    }

    void Contextmenu_exitClick(object sender, EventArgs e)
    {
        Application.Exit();
    }

    private void InitializeQuickSettingsMenu()
    {
        this.contextmenu_quicksettings.DropDownItems.Clear();
        // screenshot destination
        ToolStripMenuSelectList sel = new ToolStripMenuSelectList("destination",true);
        sel.Text = lang.GetString("settings_destination");
        sel.AddItem(lang.GetString("settings_destination_editor"), ScreenshotDestinations.Editor, (conf.Output_Destinations&ScreenshotDestinations.Editor)==ScreenshotDestinations.Editor);
        sel.AddItem(lang.GetString("settings_destination_clipboard"), ScreenshotDestinations.Clipboard, (conf.Output_Destinations&ScreenshotDestinations.Clipboard)==ScreenshotDestinations.Clipboard);
        sel.AddItem(lang.GetString("quicksettings_destination_file"), ScreenshotDestinations.FileDefault, (conf.Output_Destinations&ScreenshotDestinations.FileDefault)==ScreenshotDestinations.FileDefault);
        sel.AddItem(lang.GetString("settings_destination_fileas"), ScreenshotDestinations.FileWithDialog, (conf.Output_Destinations&ScreenshotDestinations.FileWithDialog)==ScreenshotDestinations.FileWithDialog);
        sel.AddItem(lang.GetString("settings_destination_printer"), ScreenshotDestinations.Printer, (conf.Output_Destinations&ScreenshotDestinations.Printer)==ScreenshotDestinations.Printer);
        sel.CheckedChanged += new EventHandler(this.QuickSettingItemChanged);
        this.contextmenu_quicksettings.DropDownItems.Add(sel);
        // print options
        sel = new ToolStripMenuSelectList("printoptions",true);
        sel.Text = lang.GetString("settings_printoptions");
        sel.AddItem(lang.GetString("printoptions_allowshrink"), "AllowPrintShrink", (bool)conf.Output_Print_AllowShrink);
        sel.AddItem(lang.GetString("printoptions_allowenlarge"), "AllowPrintEnlarge", (bool)conf.Output_Print_AllowEnlarge);
        sel.AddItem(lang.GetString("printoptions_allowrotate"), "AllowPrintRotate", (bool)conf.Output_Print_AllowRotate);
        sel.AddItem(lang.GetString("printoptions_allowcenter"), "AllowPrintCenter", (bool)conf.Output_Print_Center);
        sel.CheckedChanged += new EventHandler(this.QuickSettingItemChanged);
        this.contextmenu_quicksettings.DropDownItems.Add(sel);
        // effects
        sel = new ToolStripMenuSelectList("effects",true);
        sel.Text = lang.GetString("settings_visualization");
        sel.AddItem(lang.GetString("settings_playsound"), "PlaySound", (bool)conf.Ui_Effects_CameraSound);
        sel.AddItem(lang.GetString("settings_showflashlight"), "ShowFlashlight", (bool)conf.Ui_Effects_Flashlight);
        sel.CheckedChanged += new EventHandler(this.QuickSettingItemChanged);
        this.contextmenu_quicksettings.DropDownItems.Add(sel);
    }

    void QuickSettingItemChanged(object sender, EventArgs e)
    {
        ToolStripMenuSelectList selectList = (ToolStripMenuSelectList)sender;
        ToolStripMenuSelectListItem item = ((ItemCheckedChangedEventArgs)e).Item;;
        if(selectList.Identifier.Equals("destination"))
        {
            IEnumerator en = selectList.DropDownItems.GetEnumerator();
            ScreenshotDestinations dest = 0;
            while(en.MoveNext())
            {
                ToolStripMenuSelectListItem i = (ToolStripMenuSelectListItem)en.Current;
                if(i.Checked) dest |= (ScreenshotDestinations)i.Data;
            }
            conf.Output_Destinations = dest;
            conf.Store();
        }
        else if(selectList.Identifier.Equals("printoptions"))
        {
            if(item.Data.Equals("AllowPrintShrink")) conf.Output_Print_AllowShrink = (bool?)item.Checked;
            else if(item.Data.Equals("AllowPrintEnlarge")) conf.Output_Print_AllowEnlarge = (bool?)item.Checked;
            else if(item.Data.Equals("AllowPrintRotate")) conf.Output_Print_AllowRotate = (bool?)item.Checked;
            else if(item.Data.Equals("AllowPrintCenter")) conf.Output_Print_Center = (bool?)item.Checked;
            conf.Store();
        }
        else if(selectList.Identifier.Equals("effects"))
        {
            if(item.Data.Equals("PlaySound")) conf.Ui_Effects_CameraSound = (bool?)item.Checked;
            else if(item.Data.Equals("ShowFlashlight")) conf.Ui_Effects_Flashlight = (bool?)item.Checked;
            conf.Store();
        }
    }
    #endregion

    private static void CurrentDomain_UnhandledException(object sender, UnhandledExceptionEventArgs e)
    {
        new BugReportForm(e.ExceptionObject as Exception).ShowDialog();
    }

    private static void Application_ThreadException(object sender, ThreadExceptionEventArgs e)
    {
        new BugReportForm(e.Exception).ShowDialog();
    }
}
}
