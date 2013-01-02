/*
 * Erstellt mit SharpDevelop.
 * Benutzer: jens
 * Datum: 03.06.2008
 * Zeit: 21:34
 *
 * Sie können diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader ändern.
 */

using System;
using System.Drawing;
using System.Windows.Forms;
using System.Net;
using System.Web;
using Greenshot.Configuration;
using Greenshot.Helpers;

namespace Greenshot.Forms
{
public partial class BugReportForm : Form
{
    private Language lang;
    private BugReportForm()
    {
        //
        // The InitializeComponent() call is required for Windows Forms designer support.
        //
        InitializeComponent();
        lang = Language.GetInstance();
        UpdateUI();
    }



    public BugReportForm(Exception ex)
    {
        InitializeComponent();
        lang = Language.GetInstance();
        UpdateUI();
        this.textBoxDescription.Text = EnvironmentInfo.EnvironmentToString()
                                       + "\r\n"
                                       + EnvironmentInfo.ExceptionToString(ex);
    }

    void UpdateUI()
    {
        this.Text = lang.GetString("bugreport_title");
        this.labelBugReportInfo.Text = lang.GetString("bugreport_info");
        this.btnClose.Text = lang.GetString("bugreport_cancel");
    }

    void LinkLblBugsLinkClicked(object sender, System.Windows.Forms.LinkLabelLinkClickedEventArgs e)
    {
        openLink((LinkLabel)sender);
    }
    private void openLink(LinkLabel link)
    {
        try
        {
            link.LinkVisited = true;
            System.Diagnostics.Process.Start(link.Text);
        }
        catch (Exception)
        {
            MessageBox.Show(lang.GetString("error_openlink"),lang.GetString("error"));
        }
    }
}
}
