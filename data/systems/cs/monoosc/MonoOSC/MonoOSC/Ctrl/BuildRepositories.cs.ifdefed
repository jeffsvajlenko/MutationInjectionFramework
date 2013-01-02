// BuildRepositories.cs created with MonoDevelop
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
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using MonoOBSFramework;
using MonoOBSFramework.Functions.BuildResults;
using System.IO;

namespace MonoOSC
{
public partial class BuildRepositories : UserControl
{
    public BuildRepositories(string Name, string PathRepository, bool i586, bool x86_64, string RepoEndUrl)
    {
        ToolTip TT = new ToolTip();
        TT.IsBalloon = true;
        TT.ToolTipTitle = "Info";
        TT.ToolTipIcon = ToolTipIcon.Info;
        InitializeComponent();
        LblDistro.Text = Name;
        TT.SetToolTip(LblDistro,Name);
        LblDistroDescr.Text = PathRepository;
        TT.SetToolTip(LblDistroDescr,PathRepository);
        GrpBxI586.Visible = i586;
        GrpBxX86_64.Visible = x86_64;
        LblGotoRepository.Tag += RepoEndUrl;
        TT.SetToolTip(LblGotoRepository,RepoEndUrl);
        ChkBxStates();
        BtnDelRepo.ToolTipText = string.Format("Del {0} and all build RPM", Name);
        BtnRefresh.ToolTipText = string.Format("Refresh {0} build status", Name);
        if (!BckGWStati.IsBusy) BckGWStati.RunWorkerAsync();
    }

    private void LblGotoRepository_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
    {
        System.Diagnostics.Process.Start(LblGotoRepository.Tag.ToString());
    }

    private void ChkBxS_CheckStateChanged(object sender, EventArgs e)
    {
        CheckBox Obj = (CheckBox)sender;
        switch (Obj.CheckState)
        {
        case CheckState.Checked:
            RemovNode(Obj.Text.ToLower(), Obj.Tag.ToString());
            Obj.BackColor = Color.Green;
            break;
        case CheckState.Indeterminate:
            ChangeAttrNode("enable", Obj.Text.ToLower(), Obj.Tag.ToString());
            Obj.BackColor = Color.Yellow;
            break;
        case CheckState.Unchecked:
            ChangeAttrNode("disable", Obj.Text.ToLower(), Obj.Tag.ToString());
            Obj.BackColor = Control.DefaultBackColor;
            break;
        default:
            break;
        }
    }

    private void ChangeAttrNode(string state, string BuildPublish, string Arch)
    {
        if (true)
        {
            WriteXml.NodeExist(VarGlobale.MetaPrjXmlFs, "project", BuildPublish, true);
            Dictionary<string, string> AttribList = new Dictionary<string, string>();
            AttribList.Add("repository", this.LblDistro.Text);
            AttribList.Add("arch", Arch);
            WriteXml.AppendChild(VarGlobale.MetaPrjXmlFs, true, "project", BuildPublish, state, AttribList);
        }
    }

    private void RemovNode(string BuildPublish, string Arch)
    {
        if (true)
        {
            Dictionary<string, string> AttribList = new Dictionary<string, string>();
            AttribList.Add("repository", this.LblDistro.Text);
            AttribList.Add("arch", Arch);
            WriteXml.RemoveNode(VarGlobale.MetaPrjXmlFs, "project", BuildPublish, AttribList);
        }
    }

    private void ChkBxStates()
    {
        string XmlData = File.ReadAllText(VarGlobale.MetaPrjXmlFs);
        this.ChkBxI586Build.CheckState = ReadXml.GetChkState(XmlData, "project", "build", this.LblDistro.Text, "i586");
        this.ChkBxX86_64Build.CheckState = ReadXml.GetChkState(XmlData, "project", "build", this.LblDistro.Text, "x86_64");
        this.ChkBxI586Publish.CheckState = ReadXml.GetChkState(XmlData, "project", "publish", this.LblDistro.Text, "i586");
        this.ChkBxX86_64Publish.CheckState = ReadXml.GetChkState(XmlData, "project", "publish", this.LblDistro.Text, "x86_64");
    }

    private void BuildRepositories_Validated(object sender, EventArgs e)
    {

    }

    private void BckGWStati_DoWork(object sender, DoWorkEventArgs e)
    {
        //X86_64
        int Suc = 0;
        int Building = 0;
        int Dis = 0;
        int scheduled = 0;
        int excluded = 0;
        int broken = 0;
        int failed = 0;
        int expansionerror = 0;
        StringBuilder XmlDt = new StringBuilder();
        List<string> Result = new List<string>();
        if (GrpBxX86_64.Visible)
        {
            XmlDt = BuildRepoArchResultStatus.GetBuildRepoArchResultStatus(LblDistro.Text,
                    "x86_64");
            Result = ReadXml.GetAllAttrValueByName(XmlDt.ToString(), "code");
            foreach (string item in Result)
            {
                //if(!VarGlobal.LessVerbose)Console.WriteLine(item);
                switch (item)
                {
                case "succeeded":
                    Suc += 1;
                    break;
                case "building":
                    Building += 1;
                    break;
                case "disabled":
                    Dis += 1;
                    break;
                case "scheduled":
                    scheduled += 1;
                    break;
                case "excluded":
                    excluded += 1;
                    break;
                case "broken":
                    broken += 1;
                    break;
                case "failed":
                    failed += 1;
                    break;
                case "expansion error":
                    expansionerror += 1;
                    break;
                default:
                    break;
                }
            }

            SetLblText(LblX86_64Suc, Suc.ToString());
            SetLblText(LblX86_64Build, Building.ToString());
            SetLblText(LblX86_64Dis, Dis.ToString());
            SetTxtBxText(TxtX86_64Other, FormatStatus(scheduled, excluded, broken, failed, expansionerror));
        }

        System.Threading.Thread.Sleep(500);

        //i586
        if (GrpBxI586.Visible)
        {
            Suc = 0;
            Building = 0;
            Dis = 0;
            scheduled = 0;
            excluded = 0;
            broken = 0;
            failed = 0;
            expansionerror = 0;
            XmlDt = BuildRepoArchResultStatus.GetBuildRepoArchResultStatus(LblDistro.Text,
                    "i586");
            Result = new List<string>();
            Result = ReadXml.GetAllAttrValueByName(XmlDt.ToString(), "code");
            foreach (string item in Result)
            {
                switch (item)
                {
                case "succeeded":
                    Suc += 1;
                    break;
                case "building":
                    Building += 1;
                    break;
                case "disabled":
                    Dis += 1;
                    break;
                case "scheduled":
                    scheduled += 1;
                    break;
                case "excluded":
                    excluded += 1;
                    break;
                case "broken":
                    broken += 1;
                    break;
                case "failed":
                    failed += 1;
                    break;
                case "expansion error":
                    expansionerror += 1;
                    break;
                default:
                    break;
                }
            }

            SetLblText(Lbli586Suc, Suc.ToString());
            SetLblText(Lbli586Build, Building.ToString());
            SetLblText(Lbli586Dis, Dis.ToString());
            SetTxtBxText(Txti586Other, FormatStatus(scheduled, excluded, broken, failed, expansionerror));
        }
    }

    delegate void SetLblTextCallback(Label Lbl, string Txt);
    private void SetLblText(Label Lbl, string Txt)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (Lbl.InvokeRequired)
        {
            SetLblTextCallback d = new SetLblTextCallback(SetLblText);
            Lbl.Invoke(d, Lbl, Txt);
        }
        else
        {
            try
            {
                Lbl.Text = Txt;
            }
            catch (Exception ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            }
        }
    }

    delegate void SetTxtBxTextCallback(TextBox TxtBx, string Txt);
    private void SetTxtBxText(TextBox TxtBx, string Txt)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (TxtBx.InvokeRequired)
        {
            SetTxtBxTextCallback d = new SetTxtBxTextCallback(SetTxtBxText);
            TxtBx.Invoke(d, TxtBx, Txt);
        }
        else
        {
            try
            {
                TxtBx.Text = Txt;
            }
            catch (Exception ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            }
        }
    }

    private string FormatStatus(int scheduled, int excluded, int broken, int failed, int expansionerror)
    {
        string NL = Environment.NewLine;
        return string.Format("{0} scheduled{1}{2} excluded{3}{4} broken{5}{6} failed{7}{8} expansion error",
                             scheduled.ToString(), NL, excluded.ToString(), NL, broken.ToString(),
                             NL, failed.ToString(), NL, expansionerror.ToString());
    }

    private void BtnHide_Click(object sender, EventArgs e)
    {
        ExpandStatus((Button)sender);
    }

    private void ExpandStatus(Button Btn)
    {
        TextBox TxtBx = new TextBox();
        switch (Btn.Name)
        {
        case "BtnHideI586":
            TxtBx = Txti586Other;
            break;
        case "BtnHideX64":
            TxtBx = TxtX86_64Other;
            break;
        default:
            break;
        }
        if (Btn.ImageIndex == 0)
        {
            Btn.ImageIndex = 1;
            TxtBx.Height = 42;
        }
        else
        {
            Btn.ImageIndex = 0;
            TxtBx.Height = 83;
        }

    }

    private void BtnRefresh_Click(object sender, EventArgs e)
    {
        if (!BckGWStati.IsBusy) BckGWStati.RunWorkerAsync();
    }

    private void BtnDelRepo_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show(string.Format("Really remove the repository {0} and all build RPM ?",
                                          this.LblDistro.Text), "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) ==
                DialogResult.Yes)
        {
            Dictionary<string, string> AttribList = new Dictionary<string, string>();
            AttribList.Add("repository", this.LblDistro.Text);
            AttribList.Add("arch", "x86_64");
            WriteXml.RemoveNode(VarGlobale.MetaPrjXmlFs, "project", "build", AttribList);
            WriteXml.RemoveNode(VarGlobale.MetaPrjXmlFs, "project", "publish", AttribList);
            AttribList["arch"] = "i586";
            WriteXml.RemoveNode(VarGlobale.MetaPrjXmlFs, "project", "build", AttribList);
            WriteXml.RemoveNode(VarGlobale.MetaPrjXmlFs, "project", "publish", AttribList);

            if (WriteXml.RemoveNode(VarGlobale.MetaPrjXmlFs, "project", "repository",
                                    "name", this.LblDistro.Text))
            {
                RaiseBuildRepositoriesDelEvent();
                this.Hide();
                this.Dispose(true);
            }
        }
    }

    /*public class BuildRepositoriesDel
    {*/
    // Declare the delegate (if using non-generic pattern).
    public delegate void BuildRepositoriesDelEventHandler(object sender, EventArgs e);

    // Declare the event.
    public event BuildRepositoriesDelEventHandler BuildRepositoriesDelEvent;

    // Wrap the event in a protected virtual method
    // to enable derived classes to raise the event.
    protected virtual void RaiseBuildRepositoriesDelEvent()
    {
        // Raise the event by using the () operator.
        BuildRepositoriesDelEvent(this, new EventArgs());
    }

    private void BckGWStati_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {

    }

    private void BckGWStati_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {

    }
    //}


}//class
}//namespace
