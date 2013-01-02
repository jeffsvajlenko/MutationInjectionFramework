//
// SubmitPrjPkgCreate.cs
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
using MonoOBSFramework.Functions.Sources;
using MonoOBSFramework;
using MonoOBSFramework.Functions.Collaborate;

namespace MonoOSC.Ctrl.SubmitReq
{
public partial class SubmitPrjPkgCreate : UserControl
{
    public SubmitPrjPkgCreate()
    {
        InitializeComponent();
        this.CmbxPrjSrce.Text = VarGlobal.PrefixUserName;
        CmbxPkgListSrce.Items.Clear();
        CmbxPkgListSrce.Items.AddRange(VarGlobale.PkgList);
        if (CmbxPkgListSrce.Items.Count > 0) CmbxPkgListSrce.SelectedIndex = 0;
        CmbxPrjSrce.Items.AddRange(VarGlobale.SubProject.ToArray());
        if (CmbxPrjSrce.Items.Count > 0) CmbxPrjSrce.SelectedIndex = 0;
    }

    private void BtnRefresh_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        CmbxPkgListSrce.Items.Clear();
        List<string> Result = ReadXml.GetValue(UserPackageList.GetUserPackageList().ToString(), "directory", "entry");
        CmbxPkgListSrce.Items.AddRange((object[])Result.ToArray());
        if (CmbxPkgListSrce.Items.Count > 0) CmbxPkgListSrce.SelectedIndex = 0;
        Cursor = Cursors.Default;

    }

    private void CmbxPrjSrce_TextChanged(object sender, EventArgs e)
    {
        VarGlobal.PrefixUserName = CmbxPrjSrce.Text;
        CmbxPkgListSrce.Items.Clear();
        CmbxPkgListSrce.Text = string.Empty;
    }
    public event ReturnResultDelegate ReturnResult;
    public delegate void ReturnResultDelegate(StringBuilder Result);

    private void BtnDoIt_Click(object sender, EventArgs e)
    {
        Cursor = Cursors.WaitCursor;
        ReturnResult.Invoke(PostRequest.Create(CmbxPrjSrce.Text,
                                               CmbxPkgListSrce.Text,CmbxPrjDest.Text,CmbxPkgListDest.Text,TxtMess.Text));
        Cursor = Cursors.Default;
    }
}
}
