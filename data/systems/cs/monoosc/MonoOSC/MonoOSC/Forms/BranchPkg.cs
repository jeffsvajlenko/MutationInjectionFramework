using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using MonoOBSFramework.Functions.Search;
using MonoOBSFramework;
using MonoOBSFramework.Functions.Sources;
using System.IO;

namespace MonoOSC
{
public partial class BranchPkg : Form
{
    public BranchPkg()
    {
        InitializeComponent();
    }

    private void BtnGetList_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("Should i try to fetch project list (this could take few minutes) ?", "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
        {
            Cursor = Cursors.AppStarting;
            Application.DoEvents();
            StringBuilder XmlDt = SubProjectList.GetSubProjectList(string.Empty);
            Application.DoEvents();
            List<string> Result = ReadXml.GetValue(XmlDt.ToString(), "collection", "project");
            CmBxSubPrj.Items.Clear();
            CmBxSubPrj.Items.AddRange(Result.ToArray());
            if (CmBxSubPrj.Items.Count > 0) CmBxSubPrj.SelectedIndex = 0;
            Cursor = Cursors.Default;
        }
    }

    private void BtnGetListPkg_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.AppStarting;
        Application.DoEvents();
        StringBuilder XmlRes = UserPackageList.GetUserPackageList(CmBxSubPrj.Text);
        Application.DoEvents();
        List<string> Result = ReadXml.GetValue(XmlRes.ToString(), "directory", "entry");
        if (Result.Count > 0)
        {
            CmBxSubPkg.Items.Clear();
            CmBxSubPkg.Items.AddRange(Result.ToArray());
            if (CmBxSubPkg.Items.Count > 0) CmBxSubPkg.SelectedIndex = 0;
        }
        Cursor = Cursors.Default;
    }

    private void BranchPkg_Shown(object sender, EventArgs e)
    {
        CmbxCurSubPrj.Items.Clear();
        CmbxCurSubPrj.Items.AddRange(VarGlobale.SubProject.ToArray());
        CmbxCurSubPrj.Text = VarGlobal.PrefixUserName;
    }

    private void BtnCurSubPrj_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("Should i try to fetch subproject list ?", "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
        {
            Cursor = Cursors.AppStarting;
            Application.DoEvents();
            StringBuilder XmlDt = SubProjectList.GetSubProjectList(VarGlobal.PrefixUserName);
            Application.DoEvents();
            List<string> Result = ReadXml.GetValue(XmlDt.ToString(), "collection", "project");
            VarGlobale.SubProject = Result;
            CmbxCurSubPrj.Items.Clear();
            CmbxCurSubPrj.Items.AddRange(VarGlobale.SubProject.ToArray());
            CmbxCurSubPrj.Text = VarGlobal.PrefixUserName;
            Cursor = Cursors.Default;
        }
    }

    bool CancelClose = false;
    string CmBxSubPrjText = string.Empty;
    string CmBxSubPkgText = string.Empty;
    string CmbxCurSubPrjText = string.Empty;
    string TxtPkgDestText = string.Empty;
    private void BtnBranch_Click(object sender, EventArgs e)
    {
        if (!string.IsNullOrEmpty(CmBxSubPrj.Text) &&
                !string.IsNullOrEmpty(CmBxSubPkg.Text) &&
                !string.IsNullOrEmpty(CmbxCurSubPrj.Text) &&
                !string.IsNullOrEmpty(TxtPkgDest.Text))
        {
            if (!BckGrWork.IsBusy)
            {
                CmBxSubPrjText = CmBxSubPrj.Text;
                CmBxSubPkgText = CmBxSubPkg.Text;
                CmbxCurSubPrjText = CmbxCurSubPrj.Text;
                TxtPkgDestText = TxtPkgDest.Text;
                BtnBranch.Enabled = false;
                BckGrWork.RunWorkerAsync();
            }
        }
        else
        {
            MessageBox.Show("All field must be filled!");
            CancelClose = true;
        }
    }

    List<string> NewPkg = new List<string>();
    public List<string> _NewPkg
    {
        get
        {
            return NewPkg;
        }
    }

    private void BranchPkg_FormClosing(object sender, FormClosingEventArgs e)
    {
        e.Cancel = CancelClose;
        CancelClose = false;
    }

    private void BckGrWork_DoWork(object sender, DoWorkEventArgs e)
    {
        ProgressStruc ToSend = new ProgressStruc();
        BckGrWork.ReportProgress(0, SetProgressStruc(ToSend, "Total", "I will Create the link", 1));
        Branch.PostBranch(CmBxSubPrjText, CmBxSubPkgText, CmbxCurSubPrjText, TxtPkgDestText);
        BckGrWork.ReportProgress(1, SetProgressStruc(ToSend, "Total", "Link created", 1));

        if (MessageBox.Show(string.Format("Done! Do you want to imediatly branch also all files of {0} in {1}",
                                          CmBxSubPrjText + ":" + CmBxSubPkgText, CmbxCurSubPrjText + ":" +
                                          TxtPkgDestText), "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) ==
                DialogResult.Yes)
        {
            BckGrWork.ReportProgress(1, SetProgressStruc(ToSend, "Total", "Get the list of file(s)", 3));
            string PkgSourceDir = VarGlobal.MonoOBSFrameworkTmpDir + CmBxSubPkgText +
                                  Path.DirectorySeparatorChar.ToString();
            if (!Directory.Exists(PkgSourceDir)) Directory.CreateDirectory(PkgSourceDir);
            StringBuilder Result = GetSourceProjectPackage.GetFileList(CmBxSubPrjText,
                                   CmBxSubPkgText);
            List<string> FsLs = ReadXml.GetAllFirstAttrValue(Result.ToString(),
                                "directory", "name");
            BckGrWork.ReportProgress(2, SetProgressStruc(ToSend, "Total", "Find " + FsLs.Count +
                                     " file(s)", 3));

            int Cnt = 1;
            if (FsLs.Count > 0)
            {
                List<string> CurItem = new List<string>();
                foreach (string item in FsLs)
                {
                    BckGrWork.ReportProgress(Cnt, SetProgressStruc(ToSend, "Cur"
                                             , string.Format("Download file {0} {1}/{2}" ,item, Cnt ,FsLs.Count), FsLs.Count));
                    Cnt += 1;
                    CurItem.Clear();
                    CurItem.Add(item);
                    SourceProjectPackageFile.GetSourceProjectPackageFiles(
                        CmBxSubPrjText, CmBxSubPkgText, CurItem, PkgSourceDir, 4096);
                    BckGrWork.ReportProgress(Cnt, SetProgressStruc(ToSend, "Cur"
                                             , "File downloaded", FsLs.Count));
                }

                Cnt = 1;
                foreach (string FsPathName in Directory.GetFiles(PkgSourceDir))
                {
                    BckGrWork.ReportProgress(Cnt, SetProgressStruc(ToSend,"Cur"
                                             , string.Format("Upload file {0} {1}/{2}" ,FsLs[Cnt], Cnt ,FsLs.Count), FsLs.Count));
                    Cnt += 1;
                    PutSourceProjectPackageFile.PutFile(CmbxCurSubPrjText, TxtPkgDestText, FsPathName);
                    BckGrWork.ReportProgress(Cnt, SetProgressStruc(ToSend, "Cur"
                                             , "Uploaded !", FsLs.Count));
                }
            }
            BckGrWork.ReportProgress(3, SetProgressStruc(ToSend, "Total", "Done !", 3));
        }
    }

    private void BckGrWork_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {
        ProgressStruc ToGet = (ProgressStruc)e.UserState;
        switch (ToGet.ProgressChoice)
        {
        case "Total":
            LblTotal.Text = "Total : " + ToGet.ProgressMessage;
            PrBarTotal.Maximum = ToGet.ProgressTotal;
            PrBarTotal.Value = e.ProgressPercentage;
            break;
        case "Cur":
            LblCur.Text = "Current : " + ToGet.ProgressMessage;
            PrBarCur.Maximum = ToGet.ProgressTotal;
            PrBarCur.Value = e.ProgressPercentage;
            break;
        default:
            break;
        }
    }

    private void BckGrWork_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        if (!NewPkg.Contains(TxtPkgDestText)) NewPkg.Add(TxtPkgDestText);
        BtnBranch.Enabled = true;
    }

    struct ProgressStruc
    {
        public string ProgressChoice;
        public string ProgressMessage;
        public int ProgressTotal;
    }

    ProgressStruc SetProgressStruc(ProgressStruc PrStruc, string ProgressChoice,
                                   string ProgressMessage, int ProgressTotal)
    {
        PrStruc.ProgressChoice = ProgressChoice;
        PrStruc.ProgressTotal = ProgressTotal;
        PrStruc.ProgressMessage = ProgressMessage;
        return PrStruc;
    }
}
}
