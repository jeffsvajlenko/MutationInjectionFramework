//
// SubmitPrjPkgList.cs
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
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;
using MonoOBSFramework;
using MonoOBSFramework.Functions.Sources;
using MonoOBSFramework.Functions.Search;

namespace MonoOSC.Ctrl.SubmitReq
{
public partial class SubmitPrjPkgList : UserControl
{
    public SubmitPrjPkgList()
    {
        InitializeComponent();
        this.CmbxPrj.Text = VarGlobal.PrefixUserName;
        CmbxPkgList.Items.Clear();
        CmbxPkgList.Items.AddRange(VarGlobale.PkgList);
        if (CmbxPkgList.Items.Count > 0) CmbxPkgList.SelectedIndex = 0;
        if (string.IsNullOrEmpty(TxtUser.Text)) TxtUser.Text = VarGlobal.User;
        CmbxPrj.Items.AddRange(VarGlobale.SubProject.ToArray());
        if (CmbxPrj.Items.Count > 0) CmbxPrj.SelectedIndex = 0;
    }

    private void button1_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        string Pkg = string.Empty;
        if (!ChkBxShowAllReq.Checked) Pkg = this.CmbxPkgList.Text;
        ParseResult(SubmitreqList.GetSubmitreqList(this.CmbxPrj.Text, Pkg));
        Cursor = Cursors.Default;
    }

    public event ReturnResultDelegate ReturnResult;
    public delegate void ReturnResultDelegate(StringBuilder Result);

    private void ParseResult(StringBuilder Result)
    {
        string Filter = "history";
        if (radioButtonState.Checked) Filter = "state";
        switch (CmBxFilter.Text)
        {
        case "All":
            break;
        case "new":
            Result = ReadXml.GetAllNodeOfAnAttrVal(Result.ToString(), Filter, "new");
            break;
        case "deleted":
            Result = ReadXml.GetAllNodeOfAnAttrVal(Result.ToString(), Filter, "deleted");
            break;
        case "revoked":
            Result = ReadXml.GetAllNodeOfAnAttrVal(Result.ToString(), Filter, "revoked");
            break;
        case "declined":
            Result = ReadXml.GetAllNodeOfAnAttrVal(Result.ToString(), Filter, "declined");
            break;
        case "accepted":
            Result = ReadXml.GetAllNodeOfAnAttrVal(Result.ToString(), Filter, "accepted");
            break;
        default:
            break;
        }
        ReturnResult.Invoke(Result);
    }
    private void BtnRefresh_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        CmbxPkgList.Items.Clear();
        List<string> Result = ReadXml.GetValue(UserPackageList.GetUserPackageList().ToString(), "directory", "entry");
        CmbxPkgList.Items.AddRange((object[])Result.ToArray());
        if (CmbxPkgList.Items.Count > 0) CmbxPkgList.SelectedIndex = 0;
        Cursor = Cursors.Default;
    }

    private void ChkBxShowAllReq_CheckedChanged(object sender, EventArgs e)
    {
        BtnRefresh.Enabled = !ChkBxShowAllReq.Checked;
        CmbxPkgList.Enabled = !ChkBxShowAllReq.Checked;
    }

    private void BtnGetState_Click(object sender, EventArgs e)
    {
        if (!string.IsNullOrEmpty(TxtUser.Text) && !string.IsNullOrEmpty(CmbxState.Text))
        {
            Cursor = Cursors.WaitCursor;
            ReturnResult.Invoke(SubmitreqList.GetSubmitreqListByUserName(this.TxtUser.Text, CmbxState.Text));
            Cursor = Cursors.Default;
        }
    }

    private void CmbxPrj_TextChanged(object sender, EventArgs e)
    {
        VarGlobal.PrefixUserName = CmbxPrj.Text;
        CmbxPkgList.Items.Clear();
        CmbxPkgList.Text = string.Empty;
    }

    private void CmBxFilter_SelectedIndexChanged(object sender, EventArgs e)
    {
        radioButtonState.Enabled = true;
        radioButtonHisto.Enabled = true;
        switch (CmBxFilter.Text)
        {
        case "All":
            radioButtonState.Enabled = false;
            radioButtonHisto.Enabled = false;
            break;
        default:
            break;
        }
    }
}
}
