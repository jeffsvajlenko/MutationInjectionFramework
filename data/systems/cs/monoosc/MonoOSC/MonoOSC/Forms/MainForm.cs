// MainForm.cs created with MonoDevelop
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
using System.IO;
using MonoOBSFramework;
using MonoOBSFramework.Engine;
using MonoOBSFramework.Functions.Sources;
using MonoOBSFramework.Functions.BuildResults;
using System.Collections;

using System.Xml;
using System.Xml.Serialization;
using System.Diagnostics;
using System.Drawing.Drawing2D;

namespace MonoOSC.Forms
{
public partial class MainForm : Form
{
    public MainForm()
    {
        InitializeComponent();
        try
        {

            //Todo add the copy of syntaxhighlight.dll from gac to app dir

        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
        VarGlobal.NetEvManager.RaiseNetEvent += new EventHandler<NetEventArgs>(NetEv_RaiseNetEvent);

    }

    void NetEv_RaiseNetEvent(object sender, NetEventArgs e)
    {
        try
        {
            SetNetStatusTxt(e.Message);
            SetNetImg();
            /*TimerNetAnim.Enabled = true;
            TimerNetAnim.Start();*/
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
    }

    delegate void SetNetStatusTxtCallback(string Txt);
    private void SetNetStatusTxt(string Txt)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (statusStrip1.InvokeRequired)
        {
            SetNetStatusTxtCallback d = new SetNetStatusTxtCallback(SetNetStatusTxt);
            if (statusStrip1.IsHandleCreated) statusStrip1.Invoke(d, Txt);
        }
        else
        {
            LblNetStatus.Text = Txt;
            //SetFontFam(statusStrip1,LblNetStatus.Font.FontFamily);
            //FIXME , need to find other way, this one complety break perf
            //FontSkinManager();
        }
    }

    delegate void SetNetImgCallback();
    private void SetNetImg()
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (statusStrip1.InvokeRequired)
        {
            SetNetImgCallback d = new SetNetImgCallback(SetNetImg);
            if (statusStrip1.IsHandleCreated) statusStrip1.Invoke(d);
        }
        else
        {
            LblNetLed.Image = MonoOSC.Properties.Resources.NetLedTr;
        }
    }

    private void SettingToolStripMenuItem_Click(object sender, EventArgs e)
    {
        Setting Frm = new Setting();
        Frm.ShowDialog(this);
        string CurPrj = CmBxPrjName.Text;
        CmBxPrjName.Items.Clear();
        CmBxPrjName.Items.AddRange(VarGlobale.SubProject.ToArray());
        VarGlobal.PrefixUserName = CurPrj;
        CmBxPrjName.Text = VarGlobal.PrefixUserName;
        //VarGlobale.SaveParam();
        FontSkinManager();
    }

    private void editPrjMetaToolStripMenuItem_Click(object sender, EventArgs e)
    {
        XmlEditor EdFrm = new MonoOSC.XmlEditor();

        /*string TmpFs = Path.GetTempFileName();
        File.Delete(TmpFs);
        TmpFs = TmpFs.Replace(".tmp", ".xml");
        string XmlTemplate = GetSourceProjectMeta.GetProjectMeta().ToString();
        File.WriteAllText(TmpFs, XmlTemplate);
        EdFrm.XmlFs = TmpFs;*/
        EdFrm.XmlFs = VarGlobale.MetaPrjXmlFs;
        EdFrm.ShowDialog();
        if (MessageBox.Show("Update informations on the server ?", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
        {
            try
            {
                //MessageBox.Show(PutSourceProjectMeta.PutProjectMeta(VarGlobale.MetaPrjXmlFs).ToString(), "Result Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
                //File.Delete(TmpFs);
                WritePrjMeta();
            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
            }
            finally
            {
                //File.Delete(TmpFs);
            }
        }
    }

    private void MainForm_Shown(object sender, EventArgs e)
    {
        FileInfo IfExist = new FileInfo(ConfigReaderWriter.CONFIG_FILE);
        DirectoryInfo DirExist = new DirectoryInfo(IfExist.DirectoryName);
        if (!(DirExist.Exists)) DirExist.Create();
        FontSkinManager();
        if (!(IfExist.Exists))
        {
            Setting Frm = new Setting();
            Frm.ShowDialog(this);
        }
        VarGlobale.LoadParam();
        if (string.IsNullOrEmpty(VarGlobal.Password) == true)
        {
            Setting Frm = new Setting();
            Frm.ShowDialog(this);
        }
        CmBxPrjName.Items.Clear();
        CmBxPrjName.Items.AddRange(VarGlobale.SubProject.ToArray());
        CmBxPrjName.Text = VarGlobal.PrefixUserName;
        if (VarGlobale.AutoCheckUpdate == true)
        {
            checkUpdateToolStripMenuItem.Image = CheckUpdate.CheckIt();
            //checkUpdateToolStripMenuItem.Image = CheckUpdate.IcoUpdate;
        }
        try
        {
            if (File.Exists(VarGlobale.BookMarks))
            {
                CmBxPrjName.AutoCompleteCustomSource =(AutoCompleteStringCollection)
                                                      GenericXmlSerializer.Deserialize(VarGlobale.BookMarks,
                                                              CmBxPrjName.AutoCompleteCustomSource.GetType());
                foreach (string item in CmBxPrjName.AutoCompleteCustomSource)
                {
                    AddABookmark(item);
                }
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }
    }

    private void FontSkinManager()
    {

        if (!File.Exists(VarGlobale.SkinFontPath))
        {
            Dictionary<string, Font> ResXData = new Dictionary<string, Font>();
            foreach (ToolStripMenuItem item in menuStrip1.Items)
            {
                ResXData.Add(item.Name, item.Font);
            }
            RessManager.SaveRess(ResXData, VarGlobale.SkinFontPath);
        }
        else
        {
            Dictionary<string, Font> Result = RessManager.LoadRess(VarGlobale.SkinFontPath);
            string Key = string.Empty;
            /* foreach (KeyValuePair<string, Font> item in Result)
             {
                 menuStrip1.Items[item.Key.ToString()].Font = item.Value;
             }

             SetFontFam(this, menuStrip1.Items[0].Font.FontFamily);*/
            if (Result.Count > 0)
            {
                foreach (ToolStripItem item in menuStrip1.Items)
                {
                    item.Font = Result[menuStrip1.Items[0].Name];
                }
            }
        }
    }

    StringBuilder XmlRes;
    private void BckGrdWorkerDll_DoWork(object sender, DoWorkEventArgs e)
    {
        if (BckGrdWorkerDll.CancellationPending == true) return;
        switch (e.Argument.ToString())
        {
        case "PkgList":
            e.Result = "PkgList";
            XmlRes = UserPackageList.GetUserPackageList();
            break;
        case "buildRepositoriesMeta":
            e.Result = "buildRepositoriesMeta";
            XmlRes = GetSourceProjectMeta.GetProjectMeta();
            File.WriteAllText(VarGlobale.MetaPrjXmlFs, XmlRes.ToString());
            break;
        default:
            break;
        }
    }

    private void BckGrdWorkerDll_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {
    }

    private void BckGrdWorkerDll_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        if (e.Error != null) MessageBox.Show(e.Error.Message);

        if (e.Result != null)
        {
            switch (e.Result.ToString())
            {
            case "PkgList":
                SetListPkg(XmlRes);
                break;
            case "buildRepositoriesMeta":
                SetbuildRepositoriesMeta(XmlRes);
                break;
            default:
                break;
            }
        }
        ItemCheckState(true);
    }

    delegate void SetListPkgCallback(StringBuilder XmlListPkg);
    private void SetListPkg(StringBuilder XmlListPkg)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (this.listViewPckg.InvokeRequired)
        {
            SetListPkgCallback d = new SetListPkgCallback(SetListPkg);
            this.listViewPckg.Invoke(d, XmlListPkg);
        }
        else
        {
            //if(!VarGlobal.LessVerbose)Console.WriteLine(XmlListPkg.ToString());
            List<string> Result = ReadXml.GetValue(XmlListPkg.ToString(), "directory", "entry");
            VarGlobale.PkgList = (object[])Result.ToArray();
            if (Result.Count > 0)
            {
                listViewPckg.Items.Clear();
                foreach (string item in Result)
                {
                    listViewPckg.Items.Add(item);
                }
            }
            BckGrdWorkerDll.RunWorkerAsync("buildRepositoriesMeta");
            FontSkinManager();
        }
    }

    ArrayList AllRepository = new ArrayList();
    delegate void SetbuildRepositoriesMetaCallback(StringBuilder XmlbuildRepositoriesMeta);
    private void SetbuildRepositoriesMeta(StringBuilder XmlbuildRepositoriesMeta)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (this.splitContainer1.Panel2.InvokeRequired)
        {
            SetbuildRepositoriesMetaCallback d = new SetbuildRepositoriesMetaCallback(SetbuildRepositoriesMeta);
            this.splitContainer1.Panel2.Invoke(d, XmlbuildRepositoriesMeta);
        }
        else
        {
            try
            {

                //if(!VarGlobal.LessVerbose)Console.WriteLine(XmlbuildRepositoriesMeta.ToString());
                List<string> Result = ReadXml.GetValue(XmlbuildRepositoriesMeta.ToString(), "project", "repository");
                if (Result.Count > 0)
                {
                    AllRepository.Clear();
                    foreach (string item in Result)
                    {
                        Dictionary<string, string> SubResult = ReadXml.GetValue(XmlbuildRepositoriesMeta.ToString(), "project", "name", item, false);
                        string PathRepository = SubResult["project"] + "/" + SubResult["repository"];
                        bool i586 = false;
                        bool x86_64 = false;
                        foreach (string Arch in SubResult.Values)
                        {
                            switch (Arch)
                            {
                            case "i586":
                                i586 = true;
                                break;
                            case "x86_64":
                                x86_64 = true;
                                break;
                            default:
                                break;
                            }
                        }
                        string RepoEndUrl = VarGlobal.PrefixUserName + "/" + item;
                        BuildRepositories CurCtrl = new BuildRepositories(item, PathRepository, i586, x86_64, RepoEndUrl);
                        //CurCtrl.Top = CurCtrl.Height * splitContainer1.Panel2.Controls.Count;
                        CurCtrl.Top = CurCtrl.Height * panelProjects.Controls.Count;
                        //splitContainer1.Panel2.Controls.Add(CurCtrl);
                        panelProjects.Controls.Add(CurCtrl);
                        CurCtrl.BuildRepositoriesDelEvent += new
                                                             BuildRepositories.BuildRepositoriesDelEventHandler(
                                                                     CurCtrl_BuildRepositoriesDelEvent);
                        SubResult.Add("ItemInfo", item);
                        AllRepository.Add(SubResult);
                    }
                }
                // AddPkgTab();
                AddPrjInf(ReadXml.GetValue(XmlbuildRepositoriesMeta.ToString(), "project", "name", VarGlobal.PrefixUserName, true));
                FontSkinManager();
            }
            catch (Exception ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            }
        }
    }

    void CurCtrl_BuildRepositoriesDelEvent(object sender, EventArgs e)
    {
        WritePrjMeta();
    }

    private void connectToolStripMenuItem_Click(object sender, EventArgs e)
    {
        Connect();
    }

    private void Connect()
    {
        if (string.IsNullOrEmpty(CmBxPrjName.Text))
            MessageBox.Show(
                "The projects field cannot be empty, please, either write an project name manualy or use the refresh button in setting form to fetch sub project list",
                "Information", MessageBoxButtons.OK, MessageBoxIcon.Exclamation);
        else if (connectToolStripMenuItem.Enabled)
        {
            if (VarGlobale.ConfirmWarn == true)
                MessageBox.Show("We are going to generate lot of web traffic, so be patient before see all value, of course it depand of your internet connection performance and OpenSuse server status/aviability."
                                /*+ Environment.NewLine + "I will made an automatic refresh evry 5 minutes."*/, "Infos", MessageBoxButtons.OK, MessageBoxIcon.Information);
            //splitContainer1.Panel2.Controls.Clear();
            panelProjects.Controls.Clear();
            if (BckGrdWorkerDll.IsBusy == false) BckGrdWorkerDll.RunWorkerAsync("PkgList");
            ItemCheckState(true);
        }

        FontSkinManager();
    }

    private void AddPrjInf(Dictionary<string, string> SubResult)
    {
        listViewPrjInf.Items.Clear();
        foreach (string InfKey in SubResult.Keys)
        {
            listViewPrjInf.Items.Add(InfKey).SubItems.Add(SubResult[InfKey]);
        }
        try
        {
            foreach (ColumnHeader var in this.listViewPrjInf.Columns)
            {
                var.AutoResize(ColumnHeaderAutoResizeStyle.ColumnContent);
            }
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private void AddAPkgTab(string PkgName)
    {
        bool AlwaysHere = false;
        foreach (TabPage TbgPage in tabControl1.TabPages)
        {
            if (TbgPage.Name == PkgName)
            {
                //TbgPage.Dispose();
                AlwaysHere = true;
                break;
            }
        }
        if (AlwaysHere == false)
        {
            TabPage NewTbg = new TabPage(PkgName);
            NewTbg.Name = PkgName;
            NewTbg.ToolTipText = PkgName;
            //NewTbg.Paint += new PaintEventHandler(NewTbg_Paint);
            Ctrl.BuildPkg TheCtrl = new Ctrl.BuildPkg(PkgName, AllRepository);
            TheCtrl.Dock = DockStyle.Fill;
            NewTbg.Font = new Font("Microsoft Sans Serif", 9F, FontStyle.Bold, GraphicsUnit.Point, ((byte)(0)));
            NewTbg.Controls.Add(TheCtrl);
            tabControl1.TabPages.Add(NewTbg);
            /*Button CloseBtn = new Button();
            CloseBtn.Text = "Close";
            this.Controls.Add(CloseBtn);
            CloseBtn.Left = NewTbg.Left;
            CloseBtn.Top = NewTbg.Top;*/

        }
        tabControl1.SelectedTab = tabControl1.TabPages[PkgName];
    }

#pragma warning disable 0169
    private void NewTbg_Paint(object sender, PaintEventArgs e)
    {
        Rectangle tabrect = new Rectangle(10, 10, 10, 10);// tabControl1.SelectedTab.Bounds;
        using (Bitmap bmp = MonoOSC.Properties.Resources.Add)
        {
            /*PaintEventArgs tmp = new PaintEventArgs();
            tmp.Graphics.DrawImageUnscaledAndClipped()*/
            e.Graphics.DrawImageUnscaledAndClipped(bmp, tabrect);
            e.Graphics.DrawString("TEST", new Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0))), null, new PointF(25, 25));
            /*Icon Icn = new Icon(@"C:\tmp\Cs-ObexFtp\Images\Master.ico");
            e.Graphics.DrawIcon(Icn, tabrect);*/

        }
    }

    private void CloseTabToolStripMenuItem_Click(object sender, EventArgs e)
    {
        CloseCurTab();
    }

    private void CloseCurTab()
    {
        try
        {
            if (tabControl1.SelectedTab.Name != "TabPgMain")
            {
                VarGlobal.NetEvManager.DoSomething("Closing and cancelling trafic of : " +
                                                   tabControl1.SelectedTab.Name);
                TabPage MemTabPge = tabControl1.SelectedTab;

                //FIXME : mono have a bug to remove a selected tabpage, they fix that in Rev r130931
                tabControl1.SelectedIndex = 0;

                Ctrl.BuildPkg MemWorking = null;

                CloseTabToolStripMenuItem.Enabled = false;
                BtnCloseTab.Enabled = false;
                //((Ctrl.BuildPkg)MemTabPge.Controls[0]).CancelTrafic();
                for (int ix = MemTabPge.Controls.Count - 1; ix >= 0; --ix)
                {
                    Control ctl = MemTabPge.Controls[ix];
                    if (ctl is Ctrl.BuildPkg || ctl is Ctrl.BuildPkg)
                    {
                        MemWorking = (Ctrl.BuildPkg)MemTabPge.Controls[ix];
                        MemWorking.CancelTrafic();
                    }
                }
                if (MemTabPge != null)
                {
                    if (tabControl1.TabPages.Contains(MemTabPge)) tabControl1.TabPages.Remove(MemTabPge);
                    MemTabPge.Dispose();
                }
                CloseTabToolStripMenuItem.Enabled = true;
                BtnCloseTab.Enabled = true;
            }

        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }
    }

    private void AddPkgTab()
    {
        if(!VarGlobal.LessVerbose)Console.WriteLine("AddPkgTab()");
        List<TabPage> TbgColl = new List<TabPage>();
        foreach (ListViewItem item in listViewPckg.Items)
        {
            bool AlwaysHere = false;
            foreach (TabPage TbgPage in tabControl1.TabPages)
            {
                if (TbgPage.Name == item.Text)
                {
                    //TbgPage.Dispose();
                    AlwaysHere = true;
                    break;
                }
            }
            if (AlwaysHere == false)
            {
                TabPage NewTbg = new TabPage(item.Text);
                NewTbg.Name = item.Text;
                Ctrl.BuildPkg TheCtrl = new Ctrl.BuildPkg(item.Text, AllRepository);
                TheCtrl.Dock = DockStyle.Fill;
                NewTbg.Controls.Add(TheCtrl);
                TbgColl.Add(NewTbg);
            }

        }
        if (TbgColl.Count > 0) tabControl1.TabPages.AddRange(ListToTabPage(TbgColl));
    }

    private TabPage[] ListToTabPage(List<TabPage> TheList)
    {
        TabPage[] Result = new TabPage[TheList.Count];
        for (int i = 0; i < Result.Length; i++)
        {
            Result[i] = TheList[i];
        }
        return Result;
    }

    private void toolStripMenuItem1_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("https://build.opensuse.org/");
    }

    private void aboutToolStripMenuItem_Click(object sender, EventArgs e)
    {
        About Frm = new About();
        /*FontFamily FontFam = ((ToolStripMenuItem)sender).Font.FontFamily;
        SetFontFam(Frm, FontFam);*/
        Frm.ShowDialog();
    }

    private void SetFontFam(Control Ctrl, FontFamily FontFam)
    {
        Font NewFont = new Font(FontFam, Ctrl.Font.Size, Ctrl.Font.Style, Ctrl.Font.Unit,
                                Ctrl.Font.GdiCharSet, Ctrl.Font.GdiVerticalFont);
        Ctrl.Font = NewFont;
        foreach (Control item in Ctrl.Controls)
        {
            NewFont = new Font(FontFam, item.Font.Size, item.Font.Style, item.Font.Unit,
                               item.Font.GdiCharSet, item.Font.GdiVerticalFont);
            item.Font = NewFont;
            SetFontFam(item, FontFam);
        }

    }

    private void MainForm_FormClosing(object sender, FormClosingEventArgs e)
    {
        foreach (TabPage item in tabControl1.TabPages)
        {
            try
            {
                if (item.Name != "TabPgMain") ((Ctrl.BuildPkg)item.Controls[0]).CancelTrafic();
            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message);
            }
            BckGrdWorkerDll.CancelAsync();
        }
        try
        {
            if (CmBxPrjName.AutoCompleteCustomSource.Count > 0)
                GenericXmlSerializer.Serialize(CmBxPrjName.AutoCompleteCustomSource,
                                               VarGlobale.BookMarks);
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine,
                        Ex.StackTrace);
        }
        Application.ExitThread();
        this.Dispose(true);
    }

    /*private void DisposeAll(Control Parent)
    {
        if (Parent.HasChildren == true)
        {
            // Recursively call this method for each child control.
            foreach (Control childControl in Parent.Controls)
            {
                DisposeAll(childControl);
                if (childControl.IsDisposed == false) childControl.Dispose();
            }
            try
            {
                if (Parent.IsDisposed == false) Parent.Dispose();
            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);

            }
        }
        else
        {
            try
            {
                if (Parent.IsDisposed == false) Parent.Dispose();
            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);

            }
        }
    }*/

    private void DisposeAll(Control Parent)
    {

    }

    private void createANewPackageToolStripMenuItem_Click(object sender, EventArgs e)
    {
        NewPkg Frm = new NewPkg();
        Frm.ShowDialog();
        if (!string.IsNullOrEmpty(Frm._AddPkgToList)) listViewPckg.Items.Add(Frm._AddPkgToList);
        /*string NewPkgName = VarGlobale.InputBox("Enter the name of the new package you want to create.", "New package", string.Empty);
        if (!string.IsNullOrEmpty(NewPkgName))
            MessageBox.Show(PutCreateSourceProjectPackage.CreatePackage(VarGlobal.User, NewPkgName).ToString(), "Result", MessageBoxButtons.OK, MessageBoxIcon.Information);*/
    }

    private void deleteThisPackageToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (tabControl1.SelectedTab != tabControl1.TabPages[0])
        {
            DeletePkg(tabControl1.SelectedTab.Text);
        }
    }

    private void DeletePkg(string Pkg)
    {
        if (MessageBox.Show("You will permanently delete the package : " + Pkg + "?", "Confirmation", MessageBoxButtons.OKCancel, MessageBoxIcon.Warning) == DialogResult.OK)
        {
            Cursor = Cursors.WaitCursor;
            MessageBox.Show(DeleteSourceProjectPackage.DelPackage(Pkg).ToString(), "Result", MessageBoxButtons.OK, MessageBoxIcon.Information);
            foreach (ListViewItem item in listViewPckg.Items)
            {
                if (item.Text == Pkg)
                {
                    item.Remove();
                    break;
                }
            }
            CloseCurTab();
            Cursor = Cursors.Default;
        }
    }

    private void toolStripMenuItemDelPkg_Click(object sender, EventArgs e)
    {
        DeletePkg(listViewPckg.SelectedItems[0].Text);
    }

    private void toolStripMenuItemEditMeta_Click(object sender, EventArgs e)
    {
        string PkgName = listViewPckg.SelectedItems[0].Text;

        XmlEditor EdFrm = new MonoOSC.XmlEditor();

        string TmpFs = Path.GetTempFileName();
        File.Delete(TmpFs);
        TmpFs = TmpFs.Replace(".tmp", ".xml");
        string XmlTemplate = SourceProjectPackageMeta.GetSourceProjectPackageMeta(PkgName).ToString();
        File.WriteAllText(TmpFs, XmlTemplate);
        EdFrm.XmlFs = TmpFs;
        EdFrm.ShowDialog();
        if (MessageBox.Show("Update informations on the server ?", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
        {
            try
            {
                MessageBox.Show(PutSourceProjectPkgMeta.PutProjectPkgMeta(PkgName, TmpFs).ToString(), "Result Info", MessageBoxButtons.OK, MessageBoxIcon.Information);
                File.Delete(TmpFs);
            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
            }
            finally
            {
                File.Delete(TmpFs);
            }
        }
    }

    private void listViewPckg_MouseClick(object sender, MouseEventArgs e)
    {
        if (e.Button == MouseButtons.Left) AddAPkgTab(listViewPckg.SelectedItems[0].Text);
    }

    private void TimerNetAnim_Tick(object sender, EventArgs e)
    {
        LblNetLed.Image = MonoOSC.Properties.Resources.NetLedZz;
        //TimerNetAnim.Stop();
    }

    private void BtnWritePrjMeta_Click(object sender, EventArgs e)
    {
        WritePrjMeta();
    }

    private void WritePrjMeta()
    {
        Cursor = Cursors.WaitCursor;
        MessageBox.Show(PutSourceProjectMeta.PutProjectMeta(
                            VarGlobale.MetaPrjXmlFs).ToString(), "Result Info",
                        MessageBoxButtons.OK, MessageBoxIcon.Information);
        panelProjects.Controls.Clear();
        if (BckGrdWorkerDll.IsBusy == false) BckGrdWorkerDll.RunWorkerAsync("PkgList");
        Cursor = Cursors.Default;
    }

    private void CmBxPrjName_TextChanged(object sender, EventArgs e)
    {
        if(connectToolStripMenuItem.Enabled)VarGlobal.PrefixUserName = CmBxPrjName.Text;
    }

    private void CmBxPrjName_SelectedIndexChanged(object sender, EventArgs e)
    {
        if(connectToolStripMenuItem.Enabled)VarGlobal.PrefixUserName = CmBxPrjName.Text;
    }

    private void repositoriesManagerToolStripMenuItem_Click(object sender, EventArgs e)
    {
        RepoManager Frm = new RepoManager();
        Frm.ShowDialog();
        if (!Frm._Canceled)
            if (MessageBox.Show("Apply this new configuration by uploading it to the OBS server ?",
                                "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
                WritePrjMeta();
    }

    private void chmToolStripMenuItem_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("http://surfzoid.free.fr/freevbsoft/MonoOSC/Docs/chm/");
    }

    private void htmlToolStripMenuItem_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("http://surfzoid.free.fr/freevbsoft/MonoOSC/Docs/html/");
    }

    private void showGlobalLogToolStripMenuItem_Click(object sender, EventArgs e)
    {
        VarGlobal._ShowLogWindow = true;
    }

    private void tabControl1_ControlRemoved(object sender, ControlEventArgs e)
    {

    }

    private void deleteWipeAllBinaryToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("All the dead (build failled) file(s) of your wole project will be permanently delete (wipe) from the server.", "Confirmation", MessageBoxButtons.YesNo, MessageBoxIcon.Warning) == DialogResult.Yes)
        {
            MessageBox.Show((PostWipeBuildProjectFiles.PostWipeBuildPrjFiles()).ToString());
        }

    }

    bool AscDesc = true;
    private void listViewPrjInf_ColumnClick(object sender, ColumnClickEventArgs e)
    {
        try
        {
            AscDesc = !AscDesc;

            // Set the ListViewItemSorter property to a new ListViewItemComparer
            // object.
            ((ListView)sender).ListViewItemSorter = new ListViewItemComparer(e.Column, AscDesc);
            // Call the sort method to manually sort.
            ((ListView)sender).Sort();

        }
        catch (Exception ex)
        {
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    private void tabControl1_Click(object sender, EventArgs e)
    {
        MouseEventArgs mea = (MouseEventArgs)e;
        // Get the point from the current mouse click
        Point ptClick = new Point(mea.X, mea.Y);

        // Loop on TabPages
        for (int index = 0; index < tabControl1.TabPages.Count; index++)
        {
            // Get the TabPage that contains the click coordinates
            if (tabControl1.GetTabRect(index).Contains(ptClick))
            {
                // Remove the selected TabPage
                //TabControl1.TabPages.RemoveAt(index);
                tabControl1.SelectedIndex = index;
            }
        }

        if (mea.Button == MouseButtons.Middle)
        {
            CloseCurTab();
        }
        if (mea.Button == MouseButtons.Right)
        {
            if (tabControl1.SelectedTab.Name != "TabPgMain")
                CtxMenuCloseTab.Show(tabControl1.SelectedTab, tabControl1.SelectedTab.Bounds.X, tabControl1.SelectedTab.Bounds.Y - 20);
        }
    }

    private void tabControl1_SelectedIndexChanged(object sender, EventArgs e)
    {
        if (tabControl1.SelectedTab.Name == "TabPgMain")
        {
            CloseTabToolStripMenuItem.Visible = false;
            deleteThisPackageToolStripMenuItem.Visible = false;
            BtnCloseTab.Visible = false;
            BtnDelPkg.Visible = false;
            toolStripSeparator2.Visible = false;
        }
        else
        {
            CloseTabToolStripMenuItem.Visible = true;
            deleteThisPackageToolStripMenuItem.Visible = true;
            BtnCloseTab.Visible = true;
            BtnDelPkg.Visible = true;
            toolStripSeparator2.Visible = true;

            closeTabToolStripMenuItem1.Text = string.Format("Close {0} package tab", tabControl1.SelectedTab.Text);
            deletePackageToolStripMenuItem.Text = string.Format("Delete package {0}", tabControl1.SelectedTab.Text);
            closeTabToolStripMenuItem1.ToolTipText = closeTabToolStripMenuItem1.Text;
            deletePackageToolStripMenuItem.ToolTipText = deletePackageToolStripMenuItem.Text;

            CloseTabToolStripMenuItem.Text = closeTabToolStripMenuItem1.Text;
            deleteThisPackageToolStripMenuItem.Text = deletePackageToolStripMenuItem.Text;
            CloseTabToolStripMenuItem.ToolTipText = closeTabToolStripMenuItem1.Text;
            deleteThisPackageToolStripMenuItem.ToolTipText = deletePackageToolStripMenuItem.Text;

            BtnCloseTab.ToolTipText = closeTabToolStripMenuItem1.Text;
            BtnDelPkg.ToolTipText = deletePackageToolStripMenuItem.Text;

        }
    }

    private void checkUpdateToolStripMenuItem_Click(object sender, EventArgs e)
    {
        CheckUpdate.ReturnIfSame = true;
        checkUpdateToolStripMenuItem.Image = CheckUpdate.CheckIt();
        CheckUpdate.ReturnIfSame = false;
        //checkUpdateToolStripMenuItem.Image = CheckUpdate.IcoUpdate;
    }

    private void BtnDisConnect_Click(object sender, EventArgs e)
    {

        BckGrdWorkerDll.CancelAsync();
        // Loop on TabPages
        for (int index = 0; index < tabControl1.TabPages.Count; index++)
        {
            tabControl1.SelectedIndex = index;
            CloseCurTab();
        }
        listViewPckg.Items.Clear();
        listViewPrjInf.Items.Clear();
        //splitContainer1.Panel2.Controls.Clear();
        panelProjects.Controls.Clear();
        ItemCheckState(false);
        VarGlobal.NetEvManager.DoSomething("Disconnected");
    }

    private void exitToolStripMenuItem_Click(object sender, EventArgs e)
    {
        Application.ExitThread();
    }

    private void newPackageUsingSpecFileWizardToolStripMenuItem_Click(object sender, EventArgs e)
    {
        Forms.SpecWizard Frm = new SpecWizard();
        Frm.ShowDialog(this);
        if (Frm._CancelSpec == false)
        {
            listViewPckg.Items.Add(VarGlobale.SpecWizPkgName);
            AddAPkgTab(VarGlobale.SpecWizPkgName);
        }
    }

    private void ItemCheckState(bool Cnted)
    {
        BtnDisConnect.Enabled = Cnted;
        disconnectToolStripMenuItem.Enabled = Cnted;
        BtnConnect.Enabled = !Cnted;
        SettingToolStripMenuItem.Enabled = !Cnted;
        connectToolStripMenuItem.Enabled = !Cnted;
        nouveauToolStripButton.Enabled = Cnted;
        toolStripButton1.Enabled = Cnted;
        createANewPackageToolStripMenuItem.Enabled = Cnted;
        newPackageUsingSpecFileWizardToolStripMenuItem.Enabled = Cnted;
        CmBxPrjName.Enabled = !Cnted;
        collaborationToolStripMenuItem.Enabled = Cnted;
    }

    private void infosToolStripMenuItem_Click(object sender, EventArgs e)
    {
        Infos Frm = new Infos();
        Frm.ShowDialog(this);
    }

    private void ColapseExtend(GroupBox GrpBx, bool ResizeSplit)
    {
        if (autoHideListToolStripMenuItem.Checked == true)
        {
            if (GrpBx.Dock == DockStyle.Fill)
            {
                GrpBx.Dock = DockStyle.None;
                GrpBx.Height = 20;
                if (ResizeSplit == true) splitContainer2.SplitterDistance = 25;

            }
            else
            {
                GrpBx.Dock = DockStyle.Fill;
                if (ResizeSplit == true) splitContainer2.SplitterDistance = splitContainer2.Height / 2;
            }
        }
    }

    private void GrpBxPrjInf_MouseHover(object sender, EventArgs e)
    {
        ColapseExtend(GrpBxPrjInf, true);
    }

    private void groupBox1_MouseHover(object sender, EventArgs e)
    {
        ColapseExtend(groupBox1, false);
    }

    private void iRCChannelToolStripMenuItem_Click(object sender, EventArgs e)
    {
        MessageBox.Show("Your system need to handle the folowing kind of link : irc://irc.freenode.org/MonoOSC",
                        "Informations", MessageBoxButtons.OK, MessageBoxIcon.Information);
        OpenAnURL("irc://irc.freenode.org/MonoOSC");
    }

    private void officialPageToolStripMenuItem_Click(object sender, EventArgs e)
    {
        OpenAnURL("https://sourceforge.net/projects/monoosc/");
    }

    private void OpenAnURL(string URL)
    {
        try
        {
            Process.Start(URL);
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    private void requestsToSubmitToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (!connectToolStripMenuItem.Enabled)
        {
            string PreviousPrefixUserName = VarGlobal.PrefixUserName;
            Collaborate Frm = new Collaborate();
            Frm.ShowDialog(this);
            VarGlobal.PrefixUserName = PreviousPrefixUserName;
        }
        else
        {
            if (MessageBox.Show("To use this feature, you need to be connected first and wait the end of connection process, do you want to connect now ?",
                                "Warning", MessageBoxButtons.YesNo, MessageBoxIcon.Warning) == DialogResult.Yes)
                Connect();
        }
    }

    private void donateToolStripMenuItem_Click(object sender, EventArgs e)
    {
        OpenAnURL("http://sourceforge.net/project/project_donations.php?group_id=236037");
    }

    private void registerToolStripMenuItem_Click(object sender, EventArgs e)
    {
        OpenAnURL("http://monoosc.sourceforge.net/MonoOSCSign.php");
    }

    private bool tabControl1_TabPageClosing(int indx)
    {
        CloseCurTab();
        return default(bool);
    }

    private void splitContainer1_MouseDoubleClick(object sender, MouseEventArgs e)
    {
        SplitContainer Sp = (SplitContainer)sender;
        if (Sp.Orientation == Orientation.Horizontal)
        {
            if (Sp.SplitterRectangle.Y >= e.Y - 4 && Sp.SplitterRectangle.Y <= e.Y + 4) Sp.SplitterDistance =
                    Sp.Height / 2;
        }
        else
        {
            if (Sp.SplitterRectangle.X >= e.X - 4 && Sp.SplitterRectangle.X <= e.X + 4) Sp.SplitterDistance =
                    Sp.Width / 2;
        }
    }

    private void branchAPackageToolStripMenuItem_Click(object sender, EventArgs e)
    {
        BranchPkg Frm = new BranchPkg();
        Frm.ShowDialog(this);
        if (Frm._NewPkg.Count > 0)
        {
            foreach (string item in Frm._NewPkg)
            {
                ListViewItem ToTest = listViewPckg.FindItemWithText(item, true, 0);
                if (ToTest == null) listViewPckg.Items.Add(item);
            }
        }
    }

    private void editPackageToolStripMenuItem_Click(object sender, EventArgs e)
    {
        EditPackage Frm = new EditPackage();
        List<string> ToAdd = new List<string>();
        foreach (ListViewItem item in listViewPckg.Items)
        {
            ToAdd.Add(item.Text);
        }
        Frm.SetPkgList = ToAdd.ToArray();
        Frm.ShowDialog(this);
    }

    private void onlineHelpToolStripMenuItem_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("http://" +
                                         VarGlobale.CurCulture() + ".opensuse.org/MonoOSC/Guide");
    }

    private void serverStatusToolStripMenuItem_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("https://build.opensuse.org/monitor");
    }

    private void MainForm_FormClosed(object sender, FormClosedEventArgs e)
    {
        Application.Exit();
    }

    private void bugRepportToolStripMenuItem_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("https://sourceforge.net/tracker/?group_id=236037&atid=1098752");
    }

    private void featureRequestToolStripMenuItem_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start("https://sourceforge.net/tracker/?group_id=236037&atid=1098755");
    }

    private void addAProjectsubrojectToolStripMenuItem_Click(object sender, EventArgs e)
    {
        NewProject Frm = new NewProject();
        Frm.ShowDialog(this);
        if (!Frm._Canceled)
            if (MessageBox.Show("Apply this new configuration by uploading it to the OBS server ?",
                                "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
            {
                BtnDisConnect_Click(sender, e);
                VarGlobale.SubProject.Add(MonoOBSFramework.VarGlobal.PrefixUserName);
                CmBxPrjName.Items.Add(MonoOBSFramework.VarGlobal.PrefixUserName);
                if (CmBxPrjName.Items.Count > 0)
                    CmBxPrjName.SelectedIndex = CmBxPrjName.Items.Count - 1;
                VarGlobale.SaveParam();
                WritePrjMeta();
            }
    }

    private void deleteThisProjectsubrojectToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (MessageBox.Show("Are you sure you want to delete the full project " +
                            VarGlobal.PrefixUserName, "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question)
                == DialogResult.Yes)
        {
            BtnDisConnect_Click(sender, e);
            MessageBox.Show(DeleteSourceProject.DeleteProject(1).ToString());
            VarGlobale.SubProject.Remove(MonoOBSFramework.VarGlobal.PrefixUserName);
            CmBxPrjName.Items.Remove(CmBxPrjName.SelectedItem);
            if (CmBxPrjName.Items.Count > 0) CmBxPrjName.SelectedIndex = 0;
            VarGlobale.SaveParam();
        }
    }

    private void CmBxPrjName_Validated(object sender, EventArgs e)
    {
        AddItAutoCustomSrce();
    }

    private void CmBxPrjName_KeyUp(object sender, KeyEventArgs e)
    {
        if (e.KeyCode == Keys.Enter) AddItAutoCustomSrce();
    }

    private void AddItAutoCustomSrce()
    {
        if (!CmBxPrjName.AutoCompleteCustomSource.Contains(CmBxPrjName.Text))
            CmBxPrjName.AutoCompleteCustomSource.Add(CmBxPrjName.Text);
    }

    private void manageToolStripMenuItem_Click(object sender, EventArgs e)
    {
        ManageBookMarks Frm = new ManageBookMarks();
        Frm._AutoCompleteSource = CmBxPrjName.AutoCompleteCustomSource;
        Frm.ShowDialog(this);
        CmBxPrjName.AutoCompleteCustomSource = Frm._AutoCompleteSource;
        int Cnt = bookmarksToolStripMenuItem.DropDownItems.Count;
        for (int i = 2; i < Cnt; i++)
        {
            bookmarksToolStripMenuItem.DropDownItems[2].Click -= new EventHandler(It_Click);
            bookmarksToolStripMenuItem.DropDownItems.RemoveAt(2);
        }
        foreach (string item in CmBxPrjName.AutoCompleteCustomSource)
        {
            AddABookmark(item);
        }
    }

    private void AddABookmark(string item)
    {
        ToolStripMenuItem It = new ToolStripMenuItem(item);
        bookmarksToolStripMenuItem.DropDownItems.Add(It);
        It.Click -= new EventHandler(It_Click);
        It.Click += new EventHandler(It_Click);
        It.Image = Properties.Resources.MonoOSCico;
        //It.ImageScaling = ToolStripItemImageScaling.None;
    }

    void It_Click(object sender, EventArgs e)
    {
        if (connectToolStripMenuItem.Enabled)
            CmBxPrjName.Text = ((ToolStripMenuItem)sender).Text;
    }

}//Class
}//NameSpace
