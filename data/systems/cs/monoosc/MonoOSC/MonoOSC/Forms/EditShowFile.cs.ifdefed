// EditShowFile.cs created with MonoDevelop
//
//User: eric at 03:29 10/08/2008
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
using System.IO;

namespace MonoOSC.Forms
{
public partial class EditShowFile : Form
{
    public EditShowFile()
    {
        InitializeComponent();
    }

    private void enregistrerToolStripButton_Click(object sender, EventArgs e)
    {
        saveFileDialog1.FileName = _FsName;
        if (saveFileDialog1.ShowDialog() == DialogResult.OK) File.WriteAllText(saveFileDialog1.FileName, richTextBox1.Text, System.Text.Encoding.Default);
    }

    public string SetText
    {
        get
        {
            return richTextBox1.Text;
        }
        set
        {
            richTextBox1.Text = value;
        }
    }

    private string _FsName = string.Empty;
    public string SetFsName
    {
        get
        {
            return _FsName;
        }
        set
        {
            _FsName = value;
            this.Text = "Edit/Show file " + _FsName;
        }
    }

    private void EditShowFile_Shown(object sender, EventArgs e)
    {
        toolStripComboBox1.SelectedIndex = 2;
        //MakeLineNum();
    }

    private void SearchTxt ()
    {
        int SelStart = richTextBox1.SelectionStart + 1;
        if ((RichTextBoxFinds)toolStripComboBox1.SelectedItem == RichTextBoxFinds.Reverse && richTextBox1.SelectionStart >= 1)
            SelStart = richTextBox1.SelectionStart - 1;
        if (SelStart == 1)
            SelStart = 0;
        if (SelStart > richTextBox1.Text.Length)
            SelStart = richTextBox1.Text.Length;

        int result = richTextBox1.Find (toolStripTextBox1.Text, SelStart, richTextBox1.Text.Length, (RichTextBoxFinds)toolStripComboBox1.SelectedItem);

        if (result == -1 && SelStart > 1)
        {
            //if (MessageBox.Show ("Restart search from the begining ?", "question", MessageBoxButtons.YesNo, MessageBoxIcon.Question) == DialogResult.Yes) {
            richTextBox1.SelectionStart = 0;
            SearchTxt();
            //}
        }
        else
        {
            if(result < 0)result = 0;
            richTextBox1.SelectionStart = result;
            richTextBox1.SelectionLength = toolStripTextBox1.Text.Length;
        }
    }

    private void BtnSearch_Click(object sender, EventArgs e)
    {
        SearchTxt();
    }

    private void toolStripTextBox1_TextChanged(object sender, EventArgs e)
    {
        SearchTxt();
    }

    private void searchToolStripMenuItem_Click(object sender, EventArgs e)
    {
        SearchBar.Visible = searchToolStripMenuItem.Checked;
    }

    private void couperToolStripMenuItem_Click(object sender, EventArgs e)
    {
        Clipboard.SetText(richTextBox1.SelectedText);
        richTextBox1.SelectedText = string.Empty;
    }

    private void copierToolStripMenuItem_Click(object sender, EventArgs e)
    {
        if(!string.IsNullOrEmpty(richTextBox1.SelectedText))
            Clipboard.SetText(richTextBox1.SelectedText);
    }

    private void collerToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox1.SelectedText = Clipboard.GetText();
    }

    private void selectionnertoutToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox1.SelectAll();
    }

    private void annulerToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox1.Undo();
    }

    private void retablirToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox1.Redo();
    }

    private void searchNextToolStripMenuItem_Click(object sender, EventArgs e)
    {
        SearchTxt();
    }

    private void topMostToolStripMenuItem_Click(object sender, EventArgs e)
    {
        this.TopMost = topMostToolStripMenuItem.Checked;
    }

    private void wordWrapToolStripMenuItem_Click(object sender, EventArgs e)
    {
        richTextBox1.WordWrap = wordWrapToolStripMenuItem.Checked;
    }

    private void richTextBox1_TextChanged(object sender, EventArgs e)
    {
        MakeLineNum();
    }

    private void MakeLineNum()
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

}//Class
}//NameSpace
