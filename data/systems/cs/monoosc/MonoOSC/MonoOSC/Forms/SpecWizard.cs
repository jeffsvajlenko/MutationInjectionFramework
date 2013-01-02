//
// SpecWizard.cs
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
using System.Diagnostics;
using System.IO;
using MonoOBSFramework;

namespace MonoOSC.Forms
{
public partial class SpecWizard : Form
{
    public SpecWizard()
    {
        InitializeComponent();
    }

    private void linkLabel1_LinkClicked(object sender, LinkLabelLinkClickedEventArgs e)
    {
        try
        {
            string target = e.Link.LinkData as string;
            Process.Start(target);
        }
        catch (Exception ex)
        {
            MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            Debug.WriteLine(DateTime.Now + ": " + ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    string TarBz2Name = string.Empty;
    private void BtnChoiceTarBz2Fs_Click(object sender, EventArgs e)
    {
        if (openFileDialog1.ShowDialog(this) == DialogResult.OK)
        {
            BtnOk.Enabled = true;
            this.TxtBxTarBz2.Text = openFileDialog1.FileName;
            VarGlobale.SpecWizTarBz2Fs = openFileDialog1.FileName;
            TarBz2Name = Path.GetFileName(Path.GetFileNameWithoutExtension(openFileDialog1.FileName)).Replace(".tar", string.Empty).Replace(".Tar", string.Empty).Replace(".TAR", string.Empty);
            string[] Part = TarBz2Name.Split((char)(45));
            if (Part != null && Part.Length > 0)
            {
                for (int i = 0; i < Part.Length; i++)
                {
                    if (i < Part.Length - 1)
                    {
                        this.TxtName.Text += Part[i] + (char)(45);
                    }
                    else
                    {
                        this.TxtVers.Text = Part[i];
                        if (this.TxtName.Text.Length > 0) this.TxtName.Text = this.TxtName.Text.Remove(this.TxtName.Text.Length - 1, 1);
                    }

                }


            }
        }
    }

    enum Months
    {
        Jan = 1, Feb, March, April,
        May, June, July, August, Sept, Oct, Nov, Dec
    };

    private void BtnOk_Click(object sender, EventArgs e)
    {
        if (!string.IsNullOrEmpty(TxtBxTarBz2.Text) && !string.IsNullOrEmpty(TxtName.Text))
        {
            VarGlobale.SpecWizCont = Properties.Resources.DefSpecFs.Replace("RpmSpecManagerInternalName", this.TxtName.Text);
            VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("RpmSpecManagerInternalVersion", this.TxtVers.Text);
            VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("RpmSpecManagerInternalLicence", this.CmbLic.Text);
            VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("RpmSpecManagerInternalPkgGroup", this.CmbGrp.Text);
            VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("RpmSpecManagerInternalShortSumarry", this.TxtSum.Text);
            VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("RpmSpecManagerInternalDesciption", this.TxtDesc.Text);
            VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("surfzoid2002@yahoo.fr", this.TxtEmail.Text);
            FileInfo FsInf = new FileInfo(TxtBxTarBz2.Text);
            if (FsInf.Exists)
            {
                string Ext = FsInf.Extension;
                if (TxtBxTarBz2.Text.IndexOf(".tar.") > -1) Ext = ".tar" + Ext;
                VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("RpmSpecManagerInternalZipExt", Ext);
            }
            string ToDay = DateTime.Today.DayOfWeek.ToString().Substring(0, 3) + " " +
                           (Months)DateTime.Today.Month + " " +
                           DateTime.Today.Day.ToString() + " " +
                           DateTime.Today.Year.ToString();
            VarGlobale.SpecWizCont = VarGlobale.SpecWizCont.Replace("Fri Dec 26 2008", ToDay);
            VarGlobale.SpecWizSpecFs = VarGlobal.MonoOBSFrameworkTmpDir + this.TxtName.Text + ".spec";
            VarGlobale.SpecWizCont += Environment.NewLine;
            File.WriteAllText(VarGlobale.SpecWizSpecFs, VarGlobale.SpecWizCont);
            WriteXmlTemplate();
            Forms.UploadNewPkg Frm = new UploadNewPkg();
            Frm.ShowDialog(this);
            CancelSpec = false;
            this.Close();
        }
        else
        {
            MessageBox.Show("Source tarball and package name/title cannot be empty !", "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
        }
    }

    private bool CancelSpec = true;
    public bool _CancelSpec
    {
        get
        {
            return CancelSpec;
        }
        set
        {
            CancelSpec = value;
        }
    }

    private void BtnCancel_Click(object sender, EventArgs e)
    {
        CancelSpec = true;
    }

    private void WriteXmlTemplate()
    {
        VarGlobale.SpecWizXmlTemplateFs = VarGlobal.MonoOBSFrameworkTmpDir + this.TxtName.Text + ".xml"; ;
        try
        {
            string XmlTemplate = MonoOSC.Properties.Resources.NewPkgTpl;
            VarGlobale.SpecWizPkgName = this.TxtName.Text.Replace(" ", "_");
            XmlTemplate = XmlTemplate.Replace("PkgName", VarGlobale.SpecWizPkgName);
            XmlTemplate = XmlTemplate.Replace("Title of New Package", this.TxtName.Text);
            XmlTemplate = XmlTemplate.Replace("LONG DESCRIPTION", this.TxtDesc.Text);
            XmlTemplate = XmlTemplate.Replace("surfzoid", VarGlobal.User);
            File.WriteAllText(VarGlobale.SpecWizXmlTemplateFs, XmlTemplate);
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message + Environment.NewLine + Ex.StackTrace);
        }
        finally
        {

        }
    }
}
}
