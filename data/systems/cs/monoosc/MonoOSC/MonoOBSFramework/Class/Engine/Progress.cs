//
// Progress.cs
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

namespace MonoOBSFramework.Engine
{
/// <summary>
///
/// </summary>
public partial class Progress : Form
{
    /// <summary>
    ///
    /// </summary>
    public Progress()
    {
        InitializeComponent();
    }

    private string Title = string.Empty;
    private string DestDir = string.Empty;
    private string DestFile = string.Empty;
    private int TotalSize = 0;
    private int CurentSize = 0;
    private string PathSep = Path.DirectorySeparatorChar.ToString();

    /// <summary>
    ///
    /// </summary>
    public string _Title
    {
        get
        {
            return Title;
        }
        set
        {
            Title = value;
            this.Text = Title;
        }
    }
    /// <summary>
    ///
    /// </summary>
    public string _DestDir
    {
        get
        {
            return DestDir;
        }
        set
        {
            DestDir = value;
            if (DestDir.Substring(DestDir.Length - 1, 1) != PathSep) DestDir += PathSep;
            TxtDestDir.Text = DestDir + DestFile;
        }
    }
    /// <summary>
    ///
    /// </summary>
    public string _DestFile
    {
        get
        {
            return DestFile;
        }
        set
        {
            DestFile = value;
            if (DestDir.Substring(DestDir.Length - 1, 1) != PathSep) DestDir += PathSep;
            TxtDestDir.Text = DestDir + DestFile;
        }
    }
    /// <summary>
    /// In byte
    /// </summary>
    public int _TotalSize
    {
        get
        {
            return TotalSize;
        }
        set
        {
            TotalSize = value;
            if(TotalSize > 0)progressBar1.Maximum = TotalSize;
        }
    }

    private int PourCent = 0;
    /// <summary>
    ///  In byte
    /// </summary>
    public int _CurentSize
    {
        get
        {
            return CurentSize;
        }
        set
        {
            CurentSize = value;
            if (CurentSize < TotalSize) progressBar1.Value = CurentSize;
            double Memo = Convert.ToDouble(CurentSize) / Convert.ToDouble(TotalSize);
            if (TotalSize > 0) PourCent = Convert.ToInt16(Memo * 100.00);
            CalculateSpeed();
            if (CurentSize >= TotalSize)
            {
                this.Close();
                this.Dispose(true);
            }
        }
    }

    private void CalculateSpeed()
    {
        double Total = Convert.ToDouble(TotalSize);
        double Curent = Convert.ToDouble(CurentSize);
        LblKoS.Text = Math.Round((Curent / 1024 / 1024),2).ToString() + " Mo of " + Math.Round((Total / 1024 / 1024),2).ToString() + " Mo ==> " + PourCent.ToString() + " % at " + FinalSize;
    }

    private void BtnOpenDestDir_Click(object sender, EventArgs e)
    {
        System.Diagnostics.Process.Start(DestDir);
    }

    int LastSize = 0;
    int Previous = 0;
    string FinalSize = string.Empty;
    private void timer1_Tick(object sender, EventArgs e)
    {
        LastSize = CurentSize - Previous;
        Previous = CurentSize;
        FinalSize = (LastSize / 1024 ).ToString() + " Ko/s";


    }

}
}
