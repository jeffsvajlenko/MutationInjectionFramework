// GETBIN.cs created with MonoDevelop
//
//User: eric at 18:16 04/08/2008
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
using System.Text;
using System.Net;
using System.IO;
using System.Security.Cryptography.X509Certificates;
using System.ComponentModel;
using System.Collections;
using System.Windows.Forms;
using System.Drawing;

namespace MonoOBSFramework.Engine
{
/// <summary>
///
/// </summary>
public class GETBIN
{
    private BackgroundWorker backgroundWorker = new BackgroundWorker();
    private Progress ProgressFrm = new Progress();
    /// <summary>
    ///
    /// </summary>
    /// <param name="FuncAndArgs"></param>
    /// <param name="User"></param>
    /// <param name="Password"></param>
    /// <param name="Dest"></param>
    /// <param name="BlockSize"></param>
    /// <param name="TotalSize">The length of the file in Byte</param>
    public void DownLoadFile(string FuncAndArgs, string User, string Password, string Dest, int BlockSize, int TotalSize)
    {
        if (backgroundWorker.IsBusy == false)
        {
            try
            {
                FileInfo IfExist = new FileInfo(Dest);
                if (IfExist.Exists == true) IfExist.Delete();
                //if(ProgressFrm == null)
                ProgressFrm = new Progress();
                ProgressFrm._DestDir = IfExist.DirectoryName;
                ProgressFrm._DestFile = IfExist.Name;
                ProgressFrm._TotalSize = TotalSize;
                ProgressFrm._Title = "Downloading " + IfExist.Name;
                ProgressFrm.Show();
                backgroundWorker.DoWork += new DoWorkEventHandler(backgroundWorker_DoWork);
                backgroundWorker.ProgressChanged += new ProgressChangedEventHandler(backgroundWorker_ProgressChanged);

            }
            catch (Exception ex)
            {
                VarGlobal.NetEvManager.DoSomething(ex.Message);
                if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            }

            ArrayList ObJWork = new ArrayList();
            ObJWork.Add(FuncAndArgs);
            ObJWork.Add(User);
            ObJWork.Add(Password);
            ObJWork.Add(Dest);
            ObJWork.Add(BlockSize);
            VarGlobal.NetEvManager.DoSomething(string.Format("GETBIN {0}", VarGlobal.OpenSuseApiUrl + FuncAndArgs));

            backgroundWorker.RunWorkerAsync(ObJWork);
        }
    }


    //int ProgressDll = 0;
    private void backgroundWorker_DoWork(object sender, DoWorkEventArgs e)
    {
        try
        {
            ArrayList ArgList = (ArrayList)e.Argument;
            string FuncAndArgs = (string)ArgList[0];
            string User = (string)ArgList[1];
            string Password = (string)ArgList[2];
            string Dest = (string)ArgList[3];
            int BlockSize = (int)ArgList[4];

            HttpWebRequest request = WebRequest.Create(VarGlobal.OpenSuseApiUrl + FuncAndArgs) as HttpWebRequest;
            if(!VarGlobal.LessVerbose)Console.WriteLine("GETBIN {0}",VarGlobal.OpenSuseApiUrl + FuncAndArgs);

            //If proxy is not null, add it
            if(VarGlobal.Proxy != null) request.Proxy = VarGlobal.Proxy;

            // Add authentication to request
            request.Credentials = new NetworkCredential(User, Password);
            request.Timeout = VarGlobal.TimeOut;
            WebResponse response = request.GetResponse();
            Stream responseStream = response.GetResponseStream();

            FileStream LocalStream = new FileStream(Dest, FileMode.OpenOrCreate, FileAccess.Write, FileShare.Read);
            BinaryWriter LocalStreamW = new BinaryWriter(LocalStream);
            byte[] buff = new byte[BlockSize - 1];

            int iBytesRead = 1;

            //Form TheProgress = ShowProgress("Downloading file to " + Dest);
            //TheProgress.Show();
            int ProgressDll = 0;

            while (iBytesRead > 0)
            {
                iBytesRead = responseStream.Read(buff, 0, buff.Length);
                LocalStreamW.Write(buff, 0, iBytesRead);
                ProgressDll += iBytesRead;
                SetProgressVal(ProgressDll);
                //TheProgress.Refresh();
                //TheProgress.Update();
            }

            //TheProgress.Close();

            response.Close();
            responseStream.Close();
            responseStream.Dispose();
            LocalStreamW.Close();
            LocalStream.Close();
            LocalStream.Dispose();
        }
        catch (Exception ex)
        {
            VarGlobal.NetEvManager.DoSomething(ex.Message);
            if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
        }
    }

    private void backgroundWorker_ProgressChanged(object sender, ProgressChangedEventArgs e)
    {
        SetProgressVal(e.ProgressPercentage);
    }

    delegate void SetProgressValCallback(int ProgressVal);
    private void SetProgressVal(int ProgressVal)
    {
        if (ProgressFrm.IsHandleCreated == true)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (ProgressFrm.InvokeRequired)
            {
                SetProgressValCallback d = new SetProgressValCallback(SetProgressVal);
                ProgressFrm.Invoke(d, ProgressVal);
            }
            else
            {
                try
                {
                    ProgressFrm._CurentSize = ProgressVal;
                }
                catch (Exception ex)
                {
                    VarGlobal.NetEvManager.DoSomething(ex.Message);
                    if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
                }
            }
        }
    }

    /*private Form ShowProgress(string Txt)
    {
        Form FrmProg = new Form();
        FrmProg.TopMost = true;
        FrmProg.ShowInTaskbar = false;
        FrmProg.FormBorderStyle = FormBorderStyle.FixedDialog;
        FrmProg.Text = "Download informations";
        FrmProg.Size = new Size(256, 64);
        Label Progress = new Label();
        Progress.Text = Txt;
        Progress.Dock = DockStyle.Fill;
        FrmProg.Controls.Add(Progress);
        return FrmProg;
    }*/

}//Class
}//NameSpace
