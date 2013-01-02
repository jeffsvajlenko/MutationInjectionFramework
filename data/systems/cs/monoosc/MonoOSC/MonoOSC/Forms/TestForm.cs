// MainForm.cs created with MonoDevelop
//
//User: eric at 18:17 04/08/2008
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
using MonoOBSFramework.Functions.About;
using MonoOBSFramework.Functions.Sources;
using MonoOBSFramework.Functions.Tags;
using MonoOBSFramework.Functions.User.Data;

namespace MonoOSC
{
public partial class TestForm : Form
{
    public TestForm()
    {
        InitializeComponent();
    }

#pragma warning disable 0169
    private void OldTest()
    {
        StringBuilder XmlRes = About.GetAbout();
        List<string> Result = ReadXml.GetValue(XmlRes.ToString(), "about", "title");
        if (Result.Count > 0) if(!VarGlobal.LessVerbose)Console.WriteLine("Title : " + Result[0]);
        /*GetPubkey.GetKey();
        PutPubkey.PUTNewKey();
        GetPubkey.GetKey();*/
        XmlRes = GetAllProject.GetProjectsList();
        Result = ReadXml.GetValue(XmlRes.ToString(), "directory", "count");
        if (Result.Count > 0) if(!VarGlobal.LessVerbose)Console.WriteLine("count : " + Result[0]);
        Result = ReadXml.GetValue(XmlRes.ToString(), "directory", "entry");
        if (Result.Count > 0) if(!VarGlobal.LessVerbose)Console.WriteLine("Project : " + Result[0]);
    }

    private void TxtUser_Validated(object sender, EventArgs e)
    {
        VarGlobal.User = this.TxtUser.Text;
        VarGlobal.Password = this.TxtPass.Text;
        VarGlobal.Project = this.TxtProject.Text;
    }

    private void BtnTest_Click(object sender, EventArgs e)
    {
        /* bool IsGET = false;
         IsGET = this.CmBxFunct.Text.StartsWith("GET ", StringComparison.CurrentCultureIgnoreCase);*/
        string Cmd = this.CmBxFunct.Text;
        StringBuilder XmlRes;

        switch (Cmd)
        {
        case "GET about":
            XmlRes = About.GetAbout();
            WriteOutTxt(XmlRes, "about", "title");
            WriteOutTxt(XmlRes, "about", "description");
            WriteOutTxt(XmlRes, "about", "revision");
            WriteOutTxt(XmlRes, "about", "documentation");
            break;
        case "GET source/home:<UserName>":
            XmlRes = UserPackageList.GetUserPackageList();
            WriteOutTxt(XmlRes, "directory", "count");
            WriteOutTxt(XmlRes, "directory", "entry");
            break;
        case "GET source/home:<UserName>/_pubkey":
            this.TxtOut.AppendText(GetPubkey.GetKey().ToString());
            break;
        case "POST source/home:<UserName>/?cmd=createkey":
            this.TxtOut.AppendText(PostPubkey.PostNewKey().ToString());
            break;
        case "GET source":
            XmlRes = GetAllProject.GetProjectsList();
            WriteOutTxt(XmlRes, "directory", "count");
            WriteOutTxt(XmlRes, "directory", "entry");
            break;
        case "GET source/<project>/_tags":
            XmlRes = GetSourceProjectTags.GetTags();
            WriteOutTxt(XmlRes, "tags", "title");
            WriteOutTxt(XmlRes, "tags", "user");
            WriteOutTxt(XmlRes, "tags", "package");
            WriteOutTxt(XmlRes, "tags", "project");
            break;
        case "GET /source/<project>/_meta":
            this.TxtOut.AppendText(GetSourceProjectMeta.GetProjectMeta().ToString());
            break;
        default:
            break;
        }
    }

    private void WriteOutTxt(StringBuilder XmlRes, string Root, string Key)
    {
        List<string> Result = ReadXml.GetValue(XmlRes.ToString(), Root, Key);
        if (Result.Count > 0)
        {
            StringBuilder Buffer = new StringBuilder();
            foreach (string CurVal in Result)
            {
                Buffer.AppendLine(Key + " : " + CurVal);
            }
            this.TxtOut.AppendText(Buffer.ToString());
        }
    }
}//class
}//namespace
