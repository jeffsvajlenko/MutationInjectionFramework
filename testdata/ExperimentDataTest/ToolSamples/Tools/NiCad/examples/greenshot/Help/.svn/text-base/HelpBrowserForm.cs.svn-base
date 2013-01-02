/*
 * Created by SharpDevelop.
 * User: jens
 * Date: 03.05.2007
 * Time: 21:49
 *
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */

using System;
using System.Drawing;
using System.Windows.Forms;
using System.Threading;
using System.Resources;
using System.Diagnostics;

namespace Greenshot.Help
{
/// <summary>
/// Description of HelpBrowserForm.
/// </summary>
public partial class HelpBrowserForm
{
    private Language lang;
    public HelpBrowserForm(string language)
    {
        //
        // The InitializeComponent() call is required for Windows Forms designer support.
        //
        InitializeComponent();

        //
        // TODO: Add constructor code after the InitializeComponent() call.
        //
        ResourceManager rm = new ResourceManager("Greenshot.Help.HelpBrowserForm", System.Reflection.Assembly.GetExecutingAssembly());
        System.Text.ASCIIEncoding enc = new System.Text.ASCIIEncoding();
        //Debug.WriteLine("index." + Thread.CurrentThread.CurrentUICulture);
        string helpHtml = enc.GetString((byte[])rm.GetObject("index." + Thread.CurrentThread.CurrentUICulture));
        webBrowser1.DocumentText = helpHtml;

        lang = Language.GetInstance();
        updateUI();
    }

    private void updateUI()
    {

        this.Text = lang.GetString("help_title");
    }
}
}
