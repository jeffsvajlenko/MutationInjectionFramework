//
// NewPkg.cs
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
using MonoOBSFramework.Functions.Sources;
using MonoOBSFramework;

namespace MonoOSC.Forms
{
public partial class NewPkg : Form
{
    public NewPkg()
    {
        InitializeComponent();
    }

    private void BtnOk_Click(object sender, EventArgs e)
    {
        if((MessageBox.Show("Warning, if the package already exist, all flags about Build/Publish setup, will be reset to the default values.","Warning",MessageBoxButtons.OKCancel,MessageBoxIcon.Warning)) == DialogResult.OK)
        {
            string TmpFs = Path.GetTempFileName();
            File.Delete(TmpFs);
            TmpFs = TmpFs.Replace(".tmp", ".xml");
            try
            {
                string XmlTemplate = MonoOSC.Properties.Resources.NewPkgTpl;
                string PkName = this.TxtPkgName.Text.Replace(" ", "_");
                XmlTemplate = XmlTemplate.Replace("PkgName", PkName);
                XmlTemplate = XmlTemplate.Replace("Title of New Package", this.TxtTitle.Text);
                XmlTemplate = XmlTemplate.Replace("LONG DESCRIPTION", this.TxtPkgDescrip.Text);
                XmlTemplate = XmlTemplate.Replace("surfzoid", VarGlobal.User);
                File.WriteAllText(TmpFs, XmlTemplate);
                MessageBox.Show(PutSourceProjectPkgMeta.PutProjectPkgMeta(PkName, TmpFs).ToString(),"Result Info",MessageBoxButtons.OK,MessageBoxIcon.Information);
                _AddPkgToList = PkName;
                this.Close();
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
    private string AddPkgToList = string.Empty;
    public string _AddPkgToList
    {
        get
        {
            return AddPkgToList;
        }
        set
        {
            AddPkgToList = value;
        }
    }

    private void NewPkg_Shown(object sender, EventArgs e)
    {
        try
        {

        }
        catch (Exception)
        {

            throw;
        }
    }
}
}
