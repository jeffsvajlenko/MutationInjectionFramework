//
// RepoManager.cs
//
// Author:
//       Surfzoid <surfzoid@gmail.com>
//
// Copyright (c) 2009 Surfzoid
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Xml;
using MonoOBSFramework;
using MonoOBSFramework.Functions.PlatformDataLegacy;
using System.Text.RegularExpressions;

namespace MonoOSC.Forms
{
public partial class RepoManager : Form
{
    public class RepoString
    {
        public string name;
        public string project;
        public string repository;
        public List<string> arch;
    }

    public RepoManager()
    {
        InitializeComponent();
        if (BckGrdWorkerWebRepo.IsBusy == false) BckGrdWorkerWebRepo.RunWorkerAsync();
    }

    TreeNode RootNde = new TreeNode();
    private void RepoManager_Shown(object sender, EventArgs e)
    {
        PopulateTreeview();
    }

    //http://support.microsoft.com/kb/317597
    private void PopulateTreeview()
    {
        try
        {
            // SECTION 1. Create a DOM Document and load the XML data into it.
            XmlDocument dom = new XmlDocument();
            dom.Load(VarGlobale.MetaPrjXmlFs);

            // SECTION 2. Initialize the TreeView control.
            treeViewRepo.Nodes.Clear();
            treeViewRepo.Nodes.Add(new TreeNode(dom.DocumentElement.Name));
            TreeNode tNode = new TreeNode();
            tNode = treeViewRepo.Nodes[0];
            treeViewRepo.BeginUpdate();
            RootNde = tNode;

            // SECTION 3. Create a new TreeView Node with only the child nodes.
            XmlNodeList nodelist = dom.SelectNodes("//repository");
            foreach (XmlNode node in nodelist)
            {
                RepoString ToBeTag = new RepoString();
                if (node.Attributes.Count > 0 && null != node.Attributes[0].Value)
                    ToBeTag.name = node.Attributes[0].Value;
                foreach (XmlNode ChildNde in node.ChildNodes)
                {
                    switch (ChildNde.Name)
                    {
                    case "path":
                        foreach (XmlAttribute XAtt in ChildNde.Attributes)
                        {
                            switch (XAtt.Name)
                            {
                            case "project":
                                ToBeTag.project = XAtt.Value;
                                break;
                            case "repository":
                                ToBeTag.repository = XAtt.Value;
                                break;
                            default:
                                break;
                            }
                        }
                        break;
                    case "arch":
                        if (null == ToBeTag.arch) ToBeTag.arch = new List<string>();
                        ToBeTag.arch.Add(ChildNde.InnerText);
                        break;
                    default:
                        break;
                    }
                }
                AddANode(ToBeTag);
            }
            //treeViewRepo.ExpandAll();
        }
        catch (XmlException xmlEx)
        {
            MessageBox.Show(xmlEx.Message + Environment.NewLine + xmlEx.StackTrace);
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace);
        }
        treeViewRepo.EndUpdate();
    }

    private void BtnRemove_Click(object sender, EventArgs e)
    {
        try
        {
            RemoveTreeNode(false);
        }
        catch (Exception)
        {

            throw;
        }
    }

    string XFs = VarGlobal.MonoOBSFrameworkTmpDir + "MonoOSCAviableRepo.xml";
    private void BckGrdWorkerWebRepo_DoWork(object sender, DoWorkEventArgs e)
    {
        string Str = string.Empty;
        if (null == e.Argument)
            Str = MonoOBSFramework.Functions.PlatformDataLegacy.GetDistributions.GetIt().ToString();
        else
            /*Str =
            MonoOBSFramework.Functions.Search.SubProjectList.GetSubProjectList(string.Empty).ToString();*/
            Str = MonoOBSFramework.Functions.PlatformDataLegacy.GetPlatforms.GetIt().ToString();

        File.WriteAllText(XFs, Str);
        e.Result = e.Argument;
    }

    private void BckGrdWorkerWebRepo_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        try
        {
            DataSet Ds = new DataSet();
            Ds.ReadXml(XFs, XmlReadMode.Auto);
            DtGAviableRepo.DataMember = string.Empty;
            DtGAviableRepo.DataSource = null;
            DtGAviableRepo.DataSource = Ds;
            if (null == e.Result)
                DtGAviableRepo.DataMember = "distribution";
            else
                DtGAviableRepo.DataMember = "entry";
            if (this.DtGAviableRepo.ColumnCount > 0)
            {
                this.DtGAviableRepo.Columns[0].Width = 482;
                this.DtGAviableRepo.Sort(this.DtGAviableRepo.Columns[0], ListSortDirection.Ascending);
                //Yet Mono Bug
                //this.DtGAviableRepo.Columns[0].Frozen = true;
                this.DtGAviableRepo.Columns[0].DefaultCellStyle.ForeColor = Color.LemonChiffon;
                this.DtGAviableRepo.Columns[0].DefaultCellStyle.BackColor = Color.Black;
                Font Ft = this.DtGAviableRepo.DefaultCellStyle.Font;
                this.DtGAviableRepo.Columns[0].DefaultCellStyle.Font = new Font(Ft.FontFamily,
                        Ft.Size + 4, FontStyle.Bold);
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }
        BtnAdv.Enabled = true;
        LblStatus.Visible = false;
    }

    private void BtnAdd_Click(object sender, EventArgs e)
    {
        treeViewRepo.BeginUpdate();
        this.treeViewRepo.SuspendLayout();
        foreach (DataGridViewRow DtRw in this.DtGAviableRepo.SelectedRows)
        {
            AddANode(GetNodeValue(DtRw));

        }
        treeViewRepo.EndUpdate();
        this.treeViewRepo.ResumeLayout();
        this.treeViewRepo.Invalidate(true);
        if (this.DtGAviableRepo.SelectedRows.Count <= 0) MessageBox.Show(
                "Please select one or more whole line in available list of repositories");
    }

    private void AddANode(RepoString Result)
    {
        treeViewRepo.BeginUpdate();
        string CheckCurrNde = string.Empty;
        this.treeViewRepo.SelectedNode = this.treeViewRepo.Nodes[0];
        CheckCurrNde = this.treeViewRepo.SelectedNode.Text;
        if (CheckCurrNde == "project")
        {
            TreeNode TrNoAdd = new TreeNode("repositoryrepository");
            TrNoAdd.ForeColor = Color.Blue;
            TrNoAdd.NodeFont = new Font("Arial", 16, FontStyle.Bold, GraphicsUnit.Pixel);
            TreeNode TrNoName = new TreeNode("name");
            TreeNode TrNoPath = new TreeNode("path");
            TreeNode TrNoPrj = new TreeNode("project");
            TreeNode TrNoRepo = new TreeNode("repository");
            TrNoName.Nodes.Add(Result.name);
            TrNoName.LastNode.Tag = Result;
            TrNoName.Nodes[0].ForeColor = Color.Green;
            TrNoName.Nodes[0].NodeFont = new Font("Arial", 14, FontStyle.Bold, GraphicsUnit.Pixel);
            TrNoPrj.Nodes.Add(Result.project);
            TrNoPrj.LastNode.Tag = Result;
            TrNoRepo.Nodes.Add(Result.repository);
            TrNoRepo.LastNode.Tag = Result;
            TrNoPath.Nodes.AddRange(new TreeNode[] { TrNoPrj, TrNoRepo });
            TrNoAdd.Nodes.AddRange(new TreeNode[] { TrNoName, TrNoPath });
            if (null != Result.arch)
            {
                foreach (string item in Result.arch)
                {
                    TreeNode TrNoarch = new TreeNode("arch");
                    TrNoarch.Nodes.Add(item);
                    TrNoarch.LastNode.Tag = Result;
                    TrNoarch.NodeFont = new Font("Arial", 12, FontStyle.Bold, GraphicsUnit.Pixel);
                    TrNoAdd.Nodes.Add(TrNoarch);
                }
            }
            this.treeViewRepo.SelectedNode.Nodes.Add(TrNoAdd);
            this.treeViewRepo.SelectedNode = TrNoAdd;
            TrNoAdd.Text = "repository";
            TrNoAdd.Tag = Result;
            TrNoAdd.ToolTipText = string.Format("{0}{3}{1}{3}{2}{3}", Result.name, Result.project,
                                                Result.repository, Environment.NewLine);
        }
        treeViewRepo.EndUpdate();
    }

    private void DisplayAllNodes(TreeNode Nde)
    {
        foreach (TreeNode item in Nde.Nodes)
        {
            item.Expand();
            item.EnsureVisible();
            DisplayAllNodes(item);
        }
    }

    private RepoString GetNodeValue(DataGridViewRow DtRw)
    {
        RepoString Result = new RepoString();
        if (!string.IsNullOrEmpty(DtRw.Cells[0].Value.ToString()))
        {
            if (DtRw.Cells.Count > 1)
            {
                Result.name = DtRw.Cells[2].Value.ToString();
                Result.project = DtRw.Cells[1].Value.ToString();
                Result.repository = DtRw.Cells[3].Value.ToString();
            }
            else
            {
                string[] NewRepo = DtRw.Cells[0].Value.ToString().Split("/".ToCharArray());
                string Resultproject = string.Empty;
                #region  test of other url of repo
                string Resultrepository = "standard";
                /*if(NewRepo.Length > 1)
                {

                	int Idx = 1;
                	if (NewRepo[0] == "home")
                	{
                		Idx = 2;
                		Resultname = NewRepo[0] + ":" + NewRepo[1];
                	}
                	else
                	{
                		Resultname = NewRepo[0];
                	}
                	for (int i = Idx; i < NewRepo.Length; i++)
                	{
                		Resultrepository += NewRepo[i] + ":";
                	}
                	if(string.IsNullOrEmpty(Resultrepository))
                	    Resultrepository = "standard";
                	else
                	    Resultrepository = Resultrepository.Remove(Resultrepository.Length - 1);
                }       */
                #endregion
                if (NewRepo.Length > 0)
                    Resultproject = NewRepo[0];
                if (NewRepo.Length > 1)
                    Resultrepository = NewRepo[1];

                /*Result.name = ValidateName(Resultname);
                Result.project = ValidatePrjRepo(Resultname);

                Result.repository = ValidatePrjRepo(Resultrepository);*/

                Result.name = ValidateName(DtRw.Cells[0].Value.ToString());
                Result.project = Resultproject;

                Result.repository = Resultrepository;
            }
            if (null == Result.arch) Result.arch = new List<string>();
            Result.arch.Add("i586");
            Result.arch.Add("x86_64");
        }
        else
        {
            MessageBox.Show("Please select the node 'repository' to add this new one at the same level of other 'path'.",
                            "Infos", MessageBoxButtons.OK, MessageBoxIcon.Information);
        }
        return Result;
    }

    private string ValidateName(string Str)
    {
        Str = Str.Replace("+", "_");
        Str = Str.Replace(":", "_");
        Str = ValidatePrjRepo(Str);
        return Str;
    }

    private string ValidatePrjRepo(string Str)
    {
        Str = Str.Replace(@" ", "_");
        Str = Str.Replace(@"/", "_");
        Str = Str.Replace(@"\", "_");
        Str = Str.Replace("-", "_");
        return Str;
        /*Regex regexAlphaNum = new Regex("[^a-zA-Z0-9]");
                            return !regexAlphaNum.IsMatch(Str); */
    }

    private void BtnOk_Click(object sender, EventArgs e)
    {
        ValidChanges();
        Canceled = false;
        this.Close();
        // MessageBox.Show("Need to write the validate function :-)");
    }

    private void ValidChanges()
    {
        #region Create an XmlDoc to work on
        string XmlFs = VarGlobale.MetaPrjXmlFs;
        XmlDocument doc = new XmlDocument();
        try
        {
            doc.Load(XmlFs);

        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }
        #endregion

        if (File.Exists(XmlFs))
        {
            #region Remove all existing repository node in the XML meta
            List<string> Result = ReadXml.GetValue(doc.InnerXml, "project", "repository");
            if (Result.Count > 0)
            {
                Dictionary<string, string> AttribList = new Dictionary<string, string>();
                foreach (string item in Result)
                {
                    AttribList.Clear();
                    AttribList.Add("repository", item);
                    AttribList.Add("arch", "x86_64");
                    WriteXml.RemoveNode(XmlFs, "project", "build", AttribList);
                    WriteXml.RemoveNode(XmlFs, "project", "publish", AttribList);
                    AttribList["arch"] = "i586";
                    WriteXml.RemoveNode(XmlFs, "project", "build", AttribList);
                    WriteXml.RemoveNode(XmlFs, "project", "publish", AttribList);

                    if (WriteXml.RemoveNode(XmlFs, "project", "repository",
                                            "name", item))
                    {

                    }
                }
            }
            #endregion

            #region Refresh the Xml Data
            try
            {
                doc.Load(XmlFs);

            }
            catch (Exception Ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
            }
            #endregion

            #region Add all nodes from Treeview in the Xml Data and write it
            XmlNode RootNde = doc.DocumentElement;
            if (this.treeViewRepo.Nodes.Count > 0 && null != RootNde)
            {
                foreach (TreeNode item in this.treeViewRepo.Nodes[0].Nodes)
                {
                    XmlElement elemtoadd = doc.CreateElement(item.Text);
                    XmlElement Lastelem = elemtoadd;

                    foreach (TreeNode TrNde in item.Nodes)
                    {
                        switch (TrNde.Text)
                        {
                        case "name":
                        case "repository":
                        case "project":
                            XmlAttribute Attrib = doc.CreateAttribute(TrNde.Text);
                            if (TrNde.Nodes.Count > 0) Attrib.InnerText = TrNde.Nodes[0].Text;
                            elemtoadd.Attributes.Append(Attrib);
                            break;
                        default:
                            XmlElement elem = doc.CreateElement(TrNde.Text);
                            if (TrNde.Nodes.Count == 1) elem.InnerText = TrNde.Nodes[0].Text;
                            else
                            {
                                foreach (TreeNode AttribNde in TrNde.Nodes)
                                {
                                    XmlAttribute Attrib2 = doc.CreateAttribute(AttribNde.Text);
                                    if (AttribNde.Nodes.Count > 0) Attrib2.InnerText = AttribNde.Nodes[0].Text;
                                    elem.Attributes.Append(Attrib2);
                                }
                            }
                            elemtoadd.AppendChild(elem);

                            break;
                        }
                    }
                    RootNde.AppendChild(elemtoadd);
                }
            }
            doc.Save(XmlFs);
            #endregion
        }
    }

    private void treeViewRepo_NodeMouseClick(object sender, TreeNodeMouseClickEventArgs e)
    {
        label1.Text = e.Node.FullPath;
    }

    private void BtnNew_Click(object sender, EventArgs e)
    {
        RepoString Result = new RepoString();
        Result.name = "Enter_A_Name";
        Result.project = "Enter_A_project_Name";
        Result.repository = "Enter_A_repository_Name";
        Result.arch = new List<string>();
        AddANode(Result);
    }

    private void addCustomNodeToolStripMenuItem_Click(object sender, EventArgs e)
    {
        treeViewRepo.BeginUpdate();
        if (null != this.treeViewRepo.SelectedNode)
        {
            TreeNode TrNoAdd = new TreeNode();
            TrNoAdd.Text = "New node";
            this.treeViewRepo.SelectedNode.Nodes.Add(TrNoAdd);
        }
        treeViewRepo.EndUpdate();
    }

    private void deleteToolStripMenuItem_Click(object sender, EventArgs e)
    {
        RemoveTreeNode(true);
    }

    void RemoveTreeNode(bool Confirm)
    {
        treeViewRepo.BeginUpdate();
        if (null != this.treeViewRepo.SelectedNode && this.treeViewRepo.SelectedNode != this.treeViewRepo.TopNode)
            if (!Confirm || MessageBox.Show(String.Format("Remove {0} ?", this.treeViewRepo.SelectedNode.Text),
                                            "Question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes)
            {
                if (this.treeViewRepo.SelectedNode.Text == "arch")
                {
                    for (int i = 0; i < this.treeViewRepo.SelectedNode.Nodes.Count; i++)
                    {
                        if (null != this.treeViewRepo.SelectedNode.Nodes[i].Tag)
                            if (null != ((RepoString)this.treeViewRepo.SelectedNode.Nodes[i].Tag).arch)
                                ((RepoString)this.treeViewRepo.SelectedNode.Nodes[i].Tag).arch.Remove(
                                    this.treeViewRepo.SelectedNode.Nodes[i].Text);
                    }
                }
                this.treeViewRepo.SelectedNode.Remove();
            }
        treeViewRepo.EndUpdate();
    }

    private void BtnAdv_Click(object sender, EventArgs e)
    {
        BtnAdv.Enabled = false;
        LblStatus.Visible = true;
        try
        {
            if (!BckGrdWorkerWebRepo.IsBusy)
                if (BtnAdv.Text == "See advanced")
                {
                    BckGrdWorkerWebRepo.RunWorkerAsync(true);
                    BtnAdv.Text = "See public";
                }
                else
                {
                    BckGrdWorkerWebRepo.RunWorkerAsync();
                    BtnAdv.Text = "See advanced";
                }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }
    }

    private bool Canceled = true;
    public bool _Canceled
    {
        get
        {
            return Canceled;
        }
        set
        {
            Canceled = value;
        }
    }

    private void treeViewRepo_AfterLabelEdit(object sender, NodeLabelEditEventArgs e)
    {
        treeViewRepo.BeginUpdate();
        if (null != e.Node.Tag)
        {
            RepoString TheTag = (RepoString)e.Node.Tag;
            switch (e.Node.Parent.Text)
            {
            case "name":
                TheTag.name = e.Label;
                break;
            case "project":
                TheTag.project = e.Label;
                break;
            case "repository":
                TheTag.repository = e.Label;
                break;
            case "arch":
                if (null != TheTag.arch)
                {
                    for (int i = 0; i < TheTag.arch.Count; i++)
                    {
                        if (TheTag.arch[i] == e.Node.Text)
                        {
                            TheTag.arch[i] = e.Label;
                            break;
                        }
                    }
                }
                break;
            default:
                break;
            }
            //((RepoString)e.Node.Tag).name = TheTag.name;
        }
        treeViewRepo.EndUpdate();
    }

    object CopyNode = null;
    private void copyToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (null != this.treeViewRepo.SelectedNode &&
                this.treeViewRepo.SelectedNode != this.treeViewRepo.TopNode)
            Clipboard.SetData("treeViewRepo", this.treeViewRepo.SelectedNode);
        //CopyNode = this.treeViewRepo.SelectedNode.Clone();
    }

    private void cutToolStripMenuItem_Click(object sender, EventArgs e)
    {
        treeViewRepo.BeginUpdate();
        if (null != this.treeViewRepo.SelectedNode &&
                this.treeViewRepo.SelectedNode != this.treeViewRepo.TopNode)
        {
            Clipboard.SetData("treeViewRepo", this.treeViewRepo.SelectedNode);
            //CopyNode = this.treeViewRepo.SelectedNode.Clone();
            RemoveTreeNode(false);
        }
        treeViewRepo.EndUpdate();
    }

    private void pasteToolStripMenuItem_Click(object sender, EventArgs e)
    {
        treeViewRepo.BeginUpdate();
        try
        {
            if (Clipboard.ContainsData("treeViewRepo"))
            {
                TreeNode ToAdd = (TreeNode)((TreeNode)Clipboard.GetData("treeViewRepo")).Clone();
                ToAdd.ExpandAll();
                /*try
                {
                    ToAdd.Tag = treeViewRepo.TopNode.Nodes[ToAdd.Index].Tag;
                }
                catch (Exception Ex)
                {
                    if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
                }*/
                if (null != this.treeViewRepo.SelectedNode)
                    this.treeViewRepo.SelectedNode.Nodes.Add(ToAdd);
                else
                    this.treeViewRepo.TopNode.Nodes.Add(ToAdd);
            }
            /*
            if (null != CopyNode)
            {
                if (null != this.treeViewRepo.SelectedNode)
                    this.treeViewRepo.SelectedNode.Nodes.Add((TreeNode)CopyNode);
                else
                    this.treeViewRepo.TopNode.Nodes.Add((TreeNode)CopyNode);
            }*/
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }
        treeViewRepo.EndUpdate();
    }


    public void treeViewRepo_ItemDrag(object sender, System.Windows.Forms.ItemDragEventArgs e)
    {
        //Set the drag node and initiate the DragDrop
        DoDragDrop(e.Item, DragDropEffects.Move);
    }
    public void treeViewRepo_DragEnter(object sender, System.Windows.Forms.DragEventArgs e)
    {
        //See if there is a TreeNode being dragged
        if (e.Data.GetDataPresent("System.Windows.Forms.TreeNode", true))
        {
            //TreeNode found allow move effect
            e.Effect = DragDropEffects.Move;
        }
        else
        {
            //No TreeNode found, prevent move
            e.Effect = DragDropEffects.None;
        }
    }
    public void treeViewRepo_DragOver(object sender, DragEventArgs e)
    {
        //Check that there is a TreeNode being dragged
        if (e.Data.GetDataPresent("System.Windows.Forms.TreeNode", true) == false) return; // TODO: might not be correct. Was : Exit Sub

        //Get the TreeView raising the event (incase multiple on form)
        TreeView selectedTreeview = (TreeView)sender;
        //As the mouse moves over nodes, provide feedback to
        //the user by highlighting the node that is the
        //current drop target
        Point pt = ((TreeView)sender).PointToClient(new Point(e.X, e.Y));
        TreeNode targetNode = selectedTreeview.GetNodeAt(pt);
        //See if the targetNode is currently selected,
        //if so no need to validate again
        if (!(object.ReferenceEquals(selectedTreeview.SelectedNode, targetNode)))
        {
            //Select the node currently under the cursor
            selectedTreeview.SelectedNode = targetNode;
            //Check that the selected node is not the dropNode and
            //also that it is not a child of the dropNode and
            //therefore an invalid target
            TreeNode dropNode = (TreeNode)e.Data.GetData("System.Windows.Forms.TreeNode");
            while (!(targetNode == null))
            {
                if (object.ReferenceEquals(targetNode, dropNode))
                {
                    e.Effect = DragDropEffects.None;
                    return; // TODO: might not be correct. Was : Exit Sub
                }
                targetNode = targetNode.Parent;
            }
        }
        //Currently selected node is a suitable target
        e.Effect = DragDropEffects.Move;
    }
    public void treeViewRepo_DragDrop(object sender, System.Windows.Forms.DragEventArgs e)
    {
        treeViewRepo.BeginUpdate();
        //Check that there is a TreeNode being dragged
        if (e.Data.GetDataPresent("System.Windows.Forms.TreeNode", true) == false) return; // TODO: might not be correct. Was : Exit Sub

        //Get the TreeView raising the event (incase multiple on form)
        TreeView selectedTreeview = (TreeView)sender;
        //Get the TreeNode being dragged
        TreeNode dropNode = (TreeNode)e.Data.GetData("System.Windows.Forms.TreeNode");
        //The target node should be selected from the DragOver event
        TreeNode targetNode = selectedTreeview.SelectedNode;
        //Remove the drop node from its current location
        dropNode.Remove();
        //If there is no targetNode add dropNode to the bottom of
        //the TreeView root nodes, otherwise add it to the end of
        //the dropNode child nodes
        if (targetNode == null)
        {
            selectedTreeview.Nodes.Add(dropNode);
        }
        else
        {
            targetNode.Nodes.Add(dropNode);
        }
        //Ensure the newley created node is visible to
        //the user and select it
        dropNode.EnsureVisible();
        selectedTreeview.SelectedNode = dropNode;
        treeViewRepo.EndUpdate();
    }


}//class
}//namespace
