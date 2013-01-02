//
// GlobalLog.cs
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
public partial class GlobalLog : Form
{
    /// <summary>
    ///
    /// </summary>
    public GlobalLog()
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
            SetText(AddTxt,true);
        }
    }

    private void GlobalLog_FormClosing(object sender, FormClosingEventArgs e)
    {
        this.Hide();
        e.Cancel = true;

    }

    private void GlobalLog_Shown(object sender, EventArgs e)
    {
        try
        {
            FileStream LogFsStream = new FileStream(VarGlobal.LogFsStream.Name, FileMode.Open, FileAccess.Read, FileShare.ReadWrite);
            LogFsStream.Position = 0;
            byte[] Buff = new byte[1];
            int IByteRead = 1;
            StringBuilder Result = new StringBuilder();
            while (IByteRead > 0)
            {
                IByteRead = LogFsStream.Read(Buff, 0, Buff.Length);
                if(Buff[0] > 0)Result.Append((char)Buff[0]);
            }

            SetText(Result.ToString(), false);

        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(Ex.Message);
        }
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
            for (int i = firstLine; i <= lastLine; i++)
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
        MakeLineNum();
    }

    private void richTextBox1_Resize(object sender, EventArgs e)
    {
        MakeLineNum();
    }

    private void wordWrapToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox1.WordWrap = wordWrapToolStripMenuItem.Checked;
    }

    private void lineNumberToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox2.Visible = lineNumberToolStripMenuItem.Checked;
        MakeLineNum();
    }
}//class globalog
}//namespace
