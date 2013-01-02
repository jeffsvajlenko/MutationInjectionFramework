//
// Log.cs
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
using MonoOBSFramework.Functions.BuildResults;
using MonoOBSFramework;

namespace MonoOSC
{
/// <summary>
///
/// </summary>
public partial class Log : Form
{
    /// <summary>
    ///
    /// </summary>
    public Log()
    {
        InitializeComponent();
    }
    private string AddTxt = string.Empty;
    /// <summary>
    ///
    /// </summary>
    public string _AddTxt
    {
        get
        {
            return AddTxt;
        }
        set
        {
            AddTxt = value;
            SetText(AddTxt, true);
        }
    }

    private string Repo = string.Empty;
    /// <summary>
    ///
    /// </summary>
    public string _Repo
    {
        get
        {
            return Repo;
        }
        set
        {
            Repo = value;
        }
    }

    private string Archi = string.Empty;
    /// <summary>
    ///
    /// </summary>
    public string _Archi
    {
        get
        {
            return Archi;
        }
        set
        {
            Archi = value;
        }
    }

    private string Pkg = string.Empty;
    /// <summary>
    ///
    /// </summary>
    public string _Pkg
    {
        get
        {
            return Pkg;
        }
        set
        {
            Pkg = value;
        }
    }

    private string FrmTitle = string.Empty;
    /// <summary>
    ///
    /// </summary>
    public string _FrmTitle
    {
        get
        {
            return FrmTitle;
        }
        set
        {
            FrmTitle = value;
            this.Text = FrmTitle;
        }
    }


    private void Log_Shown(object sender, EventArgs e)
    {
        try
        {
            if (backgroundWorker1.IsBusy == false) backgroundWorker1.RunWorkerAsync();

            toolStripComboBox1.SelectedIndex = 2;
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message);
        }
    }

    private void BackgroundWorker1RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
    {
        if (backgroundWorker1.IsBusy == false &&
                autoRefreshToolStripMenuItem.Checked) backgroundWorker1.RunWorkerAsync();
    }

    delegate void SetTextCallback(string Txt, bool Append);
    private void SetText(string Txt, bool Append)
    {
        // InvokeRequired required compares the thread ID of the
        // calling thread to the thread ID of the creating thread.
        // If these threads are different, it returns true.
        if (this.InvokeRequired)
        {
            SetTextCallback d = new SetTextCallback(SetText);
            this.Invoke(d, Txt, Append);
        }
        else
        {
            try
            {
                if (Append == true) richTextBox1.Text += Txt;
                else richTextBox1.Text = Txt;
            }
            catch (Exception ex)
            {
                if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            }
        }
    }

    private void richTextBox1_LinkClicked(object sender, LinkClickedEventArgs e)
    {
        System.Diagnostics.Process.Start(e.LinkText);
    }

    private void alwaysOnTopToolStripMenuItem_Click(object sender, EventArgs e)
    {
        this.TopMost = alwaysOnTopToolStripMenuItem.Checked;
    }

    private void backgroundWorker1_DoWork(object sender, DoWorkEventArgs e)
    {
        string ResBuild = string.Empty;
        int LastLength = 0;
        while (RefreshOne == true || autoRefreshToolStripMenuItem.Checked)
        {
            RefreshOne = false;
            //System.Threading.Thread.Sleep(10000);
            ResBuild = BuildPkgResultLog.GetBuildPkgResultLog(Repo, Archi, Pkg).ToString();
            if (ResBuild.Length > LastLength)
            {
                LastLength = ResBuild.Length;
                SetText(ResBuild, false);
            }
            if (backgroundWorker1.CancellationPending == true ||
                    this.Visible == false) break;
        }
    }

    private void Log_FormClosed(object sender, FormClosedEventArgs e)
    {
        RefreshOne = false;
        this.backgroundWorker1.CancelAsync();
        this.backgroundWorker1.Dispose();
        this.Dispose(true);
    }

    private void Log_FormClosing(object sender, FormClosingEventArgs e)
    {
        autoRefreshToolStripMenuItem.Checked = false;
        RefreshOne = false;
        this.backgroundWorker1.CancelAsync();
        this.backgroundWorker1.Dispose();
        this.Dispose(true);
    }

    private bool RefreshOne = false;
    private void refreshToolStripMenuItem_Click(object sender, EventArgs e)
    {
        RefreshOne = true;
        backgroundWorker1.CancelAsync();
        richTextBox1.ResetText();
        richTextBox1.Text = "Try to fetch the log file.................";
        if (backgroundWorker1.IsBusy == false) backgroundWorker1.RunWorkerAsync();
    }

    private void autorefreshToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if (!autoRefreshToolStripMenuItem.Checked)
            backgroundWorker1.CancelAsync();
        else if (backgroundWorker1.IsBusy == false) backgroundWorker1.RunWorkerAsync();
    }

    private void toolStripMenuItem1_Click(object sender, EventArgs e)
    {
        SearchBar.Visible = toolStripMenuItem1.Checked;
    }

    private void SearchTxt()
    {
        int SelStart = richTextBox1.SelectionStart + 1;
        if ((RichTextBoxFinds)toolStripComboBox1.SelectedItem == RichTextBoxFinds.Reverse && richTextBox1.SelectionStart >= 1)
            SelStart = richTextBox1.SelectionStart - 1;
        if (SelStart == 1)
            SelStart = 0;
        if (SelStart > richTextBox1.Text.Length)
            SelStart = richTextBox1.Text.Length;

        int result = richTextBox1.Find(toolStripTextBox1.Text, SelStart, richTextBox1.Text.Length, (RichTextBoxFinds)toolStripComboBox1.SelectedItem);

        if (result == -1 && SelStart > 1)
        {
            //if (MessageBox.Show ("Restart search from the begining ?", "question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes) {
            richTextBox1.SelectionStart = 0;
            SearchTxt();
            //}
        }
        else
        {
            if (result < 0) result = 0;
            richTextBox1.SelectionStart = result;
            richTextBox1.SelectionLength = toolStripTextBox1.Text.Length;
        }
    }

    private void BtnSearch_Click(object sender, EventArgs e)
    {
        SearchTxt();
    }

    private void toolStripTextBox2_TextChanged(object sender, EventArgs e)
    {
        SearchTxt();
    }

    private void toolStripMenuItem3_Click(object sender, EventArgs e)
    {
        if(!string.IsNullOrEmpty(richTextBox1.SelectedText))
            Clipboard.SetText(richTextBox1.SelectedText);
    }

    private void toolStripMenuItem7_Click(object sender, EventArgs e)
    {
        richTextBox1.SelectAll();
    }

    private void searchNextToolStripMenuItem_Click(object sender, EventArgs e)
    {
        SearchTxt();
    }

    private void wordWrapToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox1.WordWrap = wordWrapToolStripMenuItem.Checked;
    }

    private void richTextBox1_TextChanged(object sender, EventArgs e)
    {
        MakeLineNum();
        if (AutoSrcollToolStripMenuItem.Checked == true)
        {
            try
            {
                //Display the last line of the textbox
                this.richTextBox1.Focus();
                this.richTextBox1.Select(this.richTextBox1.Text.Length, 0);
                this.richTextBox1.ScrollToCaret();
                this.richTextBox1.Refresh();
                this.richTextBox1.Update();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message + Environment.NewLine + ex.StackTrace, "Error", MessageBoxButtons.OK, MessageBoxIcon.Error);
            }
        }
    }

    private void MakeLineNum()
    {
        if (lineNumberToolStripMenuItem.Checked)
        {
            //we get index of first visible char and
            //number of first visible line
            Point pos = new Point(0, 1);
            int firstIndex = richTextBox1.GetCharIndexFromPosition(pos);
            int firstLine = richTextBox1.GetLineFromCharIndex(firstIndex);

            //now we get index of last visible char
            //and number of last visible line
            pos.X = ClientRectangle.Width;
            pos.Y = ClientRectangle.Height;
            int lastIndex = richTextBox1.GetCharIndexFromPosition(pos);
            int lastLine = richTextBox1.GetLineFromCharIndex(lastIndex);

            //this is point position of last visible char, we'll
            //use its Y value for calculating numberLabel size
            pos = richTextBox1.GetPositionFromCharIndex(lastIndex);

            //finally, renumber label
            richTextBox2.Text = string.Empty;
            for (int i = firstLine; i <= lastLine + 1; i++)
            {
                richTextBox2.Text += i + 1 + Environment.NewLine;
            }
            Size textSize = TextRenderer.MeasureText(richTextBox1.Lines.LongLength.ToString() + Environment.NewLine, richTextBox2.Font);
            splitContainer1.SplitterDistance = textSize.Width;
        }
        else
        {
            splitContainer1.SplitterDistance = 0;
        }
    }

    private void richTextBox1_VScroll(object sender, EventArgs e)
    {
        MakeLineNum();
    }

    private void richTextBox1_SelectionChanged(object sender, EventArgs e)
    {
        this.StatusLblPos.Text = " Line : " + (this.richTextBox1.GetLineFromCharIndex(this.richTextBox1.SelectionStart) + 1);
        MakeLineNum();
    }

    private void richTextBox1_Resize(object sender, EventArgs e)
    {
        MakeLineNum();
    }

    private void lineNumberToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox2.Visible = lineNumberToolStripMenuItem.Checked;
        MakeLineNum();
    }
}//class globalog
}//namespace
