// Setting.cs created with MonoDevelop
//
//User: eric at 03:35 08/08/2008
//
// Copyright (C) 2008 [Petit Eric, surfzoid@gmail.com]
//
//Permission is hereby granted, free of charge, to any person
//obtaining a copy of this software and associated documentation
//files (the "Software"), to deal in the Software without
//restriction, including without limitation the rights to use,
//copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the
//Software is furnished to do so, subject to the following
//conditions:
//
//The above copyright notice and this permission notice shall be
//included in all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//OTHER DEALINGS IN THE SOFTWARE.
//

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using MonoOBSFramework;
using MonoOBSFramework.Functions.Search;
using System.IO;
using System.Net;
using System.Diagnostics;

namespace MonoOSC.Forms
{
public partial class Setting : Form
{
    public Setting()
    {
        InitializeComponent();
    }

    private void BtnOk_Click(object sender, EventArgs e)
    {
        if (VarGlobal.User != this.TxtUser.Text | VarGlobale.FirstUse)
            try
            {
                if (PingHost("monoosc.sourceforge.net") != "error")
                {
                    if (MessageBox.Show(string.Format("Welcome {0}, would you like to register as new user of MonoOSC to give some force for the developper ?"
                                                      , this.TxtUser.Text), "Please!", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
                    {
                        Process.Start("http://monoosc.sourceforge.net/MonoOSCSign.php");
                        WebClient WebC = new WebClient();
                        WebC.DownloadData("http://monoosc.sourceforge.net/CountUser.php?username=" + this.TxtUser.Text);
                        if(!VarGlobal.LessVerbose)Console.WriteLine("Welcome {0}", this.TxtUser.Text);
                    }
                }
            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message);
            }
        SaveParam();

        this.Close();
    }

    private void SaveParam()
    {
        VarGlobal.Password = this.TxtPass.Text;
        //VarGlobal.PrefixUserName = this.TxtPrefix.Text;
        VarGlobal.User = this.TxtUser.Text;
        if (!this.TxtAlternateURL.Text.EndsWith("/")) this.TxtAlternateURL.Text += "/";
        VarGlobal.OpenSuseApiUrl = this.TxtAlternateURL.Text;
        VarGlobale.RpmSpecMPath = this.TxtRpmSpecM.Text;
        VarGlobale.ConfirmWarn = this.ChckConfirm.Checked;
        VarGlobale.AutoCheckUpdate = this.ChkUpdate.Checked;

        //Manage a proxy
        VarGlobale.ProxyEnable = this.ChkProxyEnable.Checked;
        VarGlobale.ProxyPassword = this.TxtProxyPass.Text;
        VarGlobale.ProxyPort = Convert.ToInt32(this.TxtProxyPort.Text);
        VarGlobale.ProxyURL = this.TxtProxyIP.Text;
        VarGlobale.ProxyUserName = this.TxtProxyUser.Text;
        VarGlobal.LessVerbose = this.ChkCkLessVerb.Checked;

        if (VarGlobale.ProxyEnable == true) VarGlobal.DefineProxy(VarGlobale.ProxyURL, VarGlobale.ProxyPort, true, VarGlobale.ProxyUserName, VarGlobale.ProxyPassword);

        SaveFont();

        if(!File.Exists(TxtLogFs.Text))
            try
            {
                File.WriteAllText(TxtLogFs.Text,string.Empty);
                VarGlobal.GlobalLogFs = TxtLogFs.Text;
            }
            catch (IOException Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{1}{2}", Ex.Message,
                            Environment.NewLine, Ex.StackTrace);
            }

        VarGlobale.SaveParam();

    }

    // Pass host name or IP Address.
    private string PingHost(string host)
    {
        try
        {
            System.Net.NetworkInformation.Ping ping = new System.Net.NetworkInformation.Ping();
            System.Net.NetworkInformation.PingReply pingReply = ping.Send(host);

            return ("Status: " + pingReply.Status.ToString());
        }
        catch (System.Net.NetworkInformation.PingException e)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(e.Message);
            return ("error");
        }
    }

    private void LoadParam()
    {
        VarGlobale.LoadParam();
        this.TxtPass.Text = VarGlobal.Password;
        this.TxtPrefix.Text = VarGlobal.PrefixUserName;
        this.TxtUser.Text = VarGlobal.User;
        if (!VarGlobal.OpenSuseApiUrl.EndsWith("/")) VarGlobal.OpenSuseApiUrl += "/";
        this.TxtAlternateURL.Text = VarGlobal.OpenSuseApiUrl;
        this.TxtRpmSpecM.Text = VarGlobale.RpmSpecMPath;
        this.ChckConfirm.Checked = VarGlobale.ConfirmWarn;
        this.ChkUpdate.Checked = VarGlobale.AutoCheckUpdate;
        this.ChkCkLessVerb.Checked = VarGlobal.LessVerbose;

        //Manage a proxy
        this.ChkProxyEnable.Checked = VarGlobale.ProxyEnable;
        this.TxtProxyPass.Text = VarGlobale.ProxyPassword;
        this.TxtProxyPort.Text = VarGlobale.ProxyPort.ToString();
        this.TxtProxyIP.Text = VarGlobale.ProxyURL;
        this.TxtProxyUser.Text = VarGlobale.ProxyUserName;

        EnableProxy(this.ChkProxyEnable.Checked);
        CmBxSubPrj.Items.Clear();
        CmBxSubPrj.Items.AddRange(VarGlobale.SubProject.ToArray());
        CmBxSubPrj.Text = VarGlobal.PrefixUserName;
        TxtLogFs.Text = VarGlobal.GlobalLogFs;
        LoadFont();
    }

    private void LoadFont()
    {
        if (File.Exists(VarGlobale.SkinFontPath))
        {
            BtnChangeFont.Enabled = true;
            Dictionary<string, Font> Result = RessManager.LoadRess(VarGlobale.SkinFontPath);
            ResXData.Clear();
            foreach (KeyValuePair<string, Font> item in Result)
            {
                ResXData.Add(item.Key.ToString(), item.Value);
                fontDialog1.Font = item.Value;
                LblFontChoice.Font = item.Value;
                LblFontChoice.Text = item.Value.ToString();
            }
        }
    }

    Dictionary<string, Font> ResXData = new Dictionary<string, Font>();
    private void SaveFont()
    {
        RessManager.SaveRess(ResXData, VarGlobale.SkinFontPath);
    }

    private void Setting_Shown(object sender, EventArgs e)
    {
        //RessManager.MonoBug();
        LoadParam();
    }

    private void Setting_Load(object sender, EventArgs e)
    {

    }

    private void TxtUser_TextChanged(object sender, EventArgs e)
    {
        TxtPrefix.Text = "home:" + TxtUser.Text;
    }

    private void BtnChoiceRpmSpecManag_Click(object sender, EventArgs e)
    {
        FileInfo FsInf = new FileInfo(this.TxtRpmSpecM.Text);
        if (FsInf.Exists == true) openFileDialog1.InitialDirectory = FsInf.DirectoryName;
        if (openFileDialog1.ShowDialog() == DialogResult.OK) TxtRpmSpecM.Text = openFileDialog1.FileName;
    }

    private void ChkProxyEnable_CheckedChanged(object sender, EventArgs e)
    {
        EnableProxy(this.ChkProxyEnable.Checked);
    }

    private void EnableProxy(bool CheckSte)
    {
        this.TxtProxyIP.Enabled = CheckSte;
        this.TxtProxyPass.Enabled = CheckSte;
        this.TxtProxyPort.Enabled = CheckSte;
        this.TxtProxyUser.Enabled = CheckSte;
    }

    private void BtnRefreshSubPrj_Click(object sender, EventArgs e)
    {
        SaveParam();
        //We are ready to fetch the list of subproject
        if (MessageBox.Show("Should i try to fetch subproject list ?", "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
        {
            StringBuilder XmlDt = SubProjectList.GetSubProjectList(TxtPrefix.Text);
            List<string> Result = ReadXml.GetValue(XmlDt.ToString(), "collection", "project");
            VarGlobale.SubProject = Result;
            CmBxSubPrj.Items.Clear();
            CmBxSubPrj.Items.AddRange(VarGlobale.SubProject.ToArray());
            CmBxSubPrj.Text = VarGlobal.PrefixUserName;
        }
    }

    private void BtnChangeFont_Click(object sender, EventArgs e)
    {
        if (fontDialog1.ShowDialog(this)== DialogResult.OK)
        {
            LblFontChoice.Font = fontDialog1.Font;
            Dictionary<string, Font> ToAdd = new Dictionary<string, Font>();
            foreach (KeyValuePair<string, Font> item in ResXData)
            {
                ToAdd.Add(item.Key.ToString(),fontDialog1.Font);
            }
            ResXData = ToAdd;
            LblFontChoice.Text = fontDialog1.Font.ToString();
        }
    }

    private void BtnChoiceLogFs_Click(object sender, EventArgs e)
    {
        if (openFileDialog2.ShowDialog(this) == DialogResult.OK)
            TxtLogFs.Text = openFileDialog2.FileName;
    }

    private void BtnHelp_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("http://" +
                                         VarGlobale.CurCulture() + ".opensuse.org/MonoOSC/Guide/Start");
    }

}//Class
}//NameSpace
