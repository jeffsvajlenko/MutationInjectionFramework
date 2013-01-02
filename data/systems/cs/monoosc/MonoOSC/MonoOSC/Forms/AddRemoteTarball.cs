using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;
using MonoOBSFramework;

namespace MonoOSC
{
public partial class AddRemoteTarball : Form
{
    public AddRemoteTarball()
    {
        InitializeComponent();
    }

    private void Btncreate_Click(object sender, EventArgs e)
    {
        if (!backgroundWorker1.IsBusy) backgroundWorker1.RunWorkerAsync();
    }

    public string TheTarBallFs = string.Empty;
    public string _TheTarBallFs
    {
        get
        {
            return TheTarBallFs;
        }
        set
        {
            TheTarBallFs = value;
        }
    }

    private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
    {

        string WorkingDir = Path.GetTempPath() +
                            "MonoOSCTmpTar" + Path.DirectorySeparatorChar.ToString();
        if (Directory.Exists(WorkingDir))
        {
            SetText("Delete old cache" + Environment.NewLine, true);
            UnixShell.StartProcess("rm", "-f -r " + WorkingDir, WorkingDir, true);
            SetText(UnixShell.ShellOutPut, true);
            SetText(UnixShell.ShellErrorOutPut, true);
        }

        Directory.CreateDirectory(WorkingDir);

        string TarName = CmbxTarName.Text.Replace(".tar.bz2", string.Empty);

        SetText("Getting files with svn co " + CmbxSvnUrl.Text + Environment.NewLine, true);
        UnixShell.StartProcess("svn", "co " + CmbxSvnUrl.Text, WorkingDir, true);
        SetText(UnixShell.ShellOutPut, true);
        SetText(UnixShell.ShellErrorOutPut, true);

        string[] DirToTar = Directory.GetDirectories(WorkingDir, "*", SearchOption.TopDirectoryOnly);
        if (DirToTar.Length > 0)
        {
            Directory.Move(DirToTar[0], WorkingDir + TarName);
            SetText("Make the tarball " + CmbxTarName.Text + Environment.NewLine, true);
            UnixShell.StartProcess("tar", "cvfj " + WorkingDir + CmbxTarName.Text + " " +
                                   TarName + " --exclude=.svn", WorkingDir, true);
            SetText(UnixShell.ShellOutPut, true);
            SetText(UnixShell.ShellErrorOutPut, true);
            TheTarBallFs = WorkingDir + CmbxTarName.Text;
            SetText(string.Format("{0}{1}{2}{1}", TheTarBallFs, Environment.NewLine, "Done!"), true);
        }
        else
        {
            SetText("There was a problem with the SVN CO", true);
        }
    }

    delegate void SetTextCallback(string Txt, bool Append);
    private void SetText(string Txt, bool Append)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (TxtDebugOut.InvokeRequired)
        {
            SetTextCallback d = new SetTextCallback(SetText);
            this.Invoke(d, Txt, Append);
        }
        else
        {
            try
            {
                if (Append == true)
                    TxtDebugOut.Text += Txt;
                else TxtDebugOut.Text = Txt;
                if (TxtDebugOut.TextLength > 0) TxtDebugOut.SelectionStart = TxtDebugOut.TextLength - 1;
            }
            catch (Exception ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            }
        }
    }

    private void CmbxSvnUrl_Validated(object sender, EventArgs e)
    {
        if (!string.IsNullOrEmpty(CmbxSvnUrl.Text))
        {
            if (!CmbxSvnUrl.AutoCompleteCustomSource.Contains(CmbxSvnUrl.Text))
                CmbxSvnUrl.AutoCompleteCustomSource.Add(CmbxSvnUrl.Text);
            if (!CmbxSvnUrl.Items.Contains(CmbxSvnUrl.Text))
                CmbxSvnUrl.Items.Add(CmbxSvnUrl.Text);
        }
    }

    private void CmbxTarName_Validated(object sender, EventArgs e)
    {
        if (!string.IsNullOrEmpty(CmbxTarName.Text))
        {
            if (!CmbxTarName.AutoCompleteCustomSource.Contains(CmbxTarName.Text))
                CmbxTarName.AutoCompleteCustomSource.Add(CmbxTarName.Text);
            if (!CmbxTarName.Items.Contains(CmbxTarName.Text))
                CmbxTarName.Items.Add(CmbxTarName.Text);
        }
    }

    private void BtnCancel_Click(object sender, EventArgs e)
    {
        TheTarBallFs = string.Empty;
    }

    private void backgroundWorker1_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        BtnOk.Enabled = true;
    }

    private void AddRemoteTarball_Shown(object sender, EventArgs e)
    {
        if (File.Exists(VarGlobale.AutoCompleteSrcePath))
        {
            Dictionary<string, string[]> AutoCompSrce =
                RessManager.LoadStringRess(VarGlobale.AutoCompleteSrcePath);

            if (AutoCompSrce.ContainsKey("URL"))
                CmbxSvnUrl.AutoCompleteCustomSource.AddRange(AutoCompSrce["URL"]);
            if (AutoCompSrce.ContainsKey("TarBallLs"))
                CmbxTarName.AutoCompleteCustomSource.AddRange(AutoCompSrce["TarBallLs"]);

            CmbxSvnUrl.Items.Clear();
            string[] ToAdd = new string[CmbxSvnUrl.AutoCompleteCustomSource.Count];
            CmbxSvnUrl.AutoCompleteCustomSource.CopyTo(ToAdd, 0);
            CmbxSvnUrl.Items.AddRange(ToAdd);

            CmbxTarName.Items.Clear();
            ToAdd = new string[CmbxTarName.AutoCompleteCustomSource.Count];
            CmbxTarName.AutoCompleteCustomSource.CopyTo(ToAdd, 0);
            CmbxTarName.Items.AddRange(ToAdd);
        }
    }

    private void AddRemoteTarball_FormClosing(object sender, FormClosingEventArgs e)
    {
        Dictionary<string, string[]> ToAdd = new
        Dictionary<string, string[]>();
        string[] CustomSrce = new string[CmbxSvnUrl.AutoCompleteCustomSource.Count];
        CmbxSvnUrl.AutoCompleteCustomSource.CopyTo(CustomSrce, 0);
        ToAdd.Add("URL", CustomSrce);
        CustomSrce = new string[CmbxTarName.AutoCompleteCustomSource.Count];
        CmbxTarName.AutoCompleteCustomSource.CopyTo(CustomSrce, 0);
        ToAdd.Add("TarBallLs", CustomSrce);
        RessManager.SaveRess(ToAdd, VarGlobale.AutoCompleteSrcePath);
    }

}
}
