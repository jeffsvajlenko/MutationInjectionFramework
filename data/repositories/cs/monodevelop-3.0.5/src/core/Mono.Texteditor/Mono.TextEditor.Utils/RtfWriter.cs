//
// RtfWriter.cs
//
// Author:
//       Mike Krüger <mkrueger@xamarin.com>
//
// Copyright (c) 2012 Xamarin Inc. (http://xamarin.com)
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
using System.Text;
using System.Collections.Generic;

namespace Mono.TextEditor.Utils
{
public static class RtfWriter
{
    static string CreateColorTable (List<Gdk.Color> colorList)
    {
        var colorTable = new StringBuilder ();
        colorTable.Append (@"{\colortbl ;");
        for (int i = 0; i < colorList.Count; i++)
        {
            Gdk.Color color = colorList [i];
            colorTable.Append (@"\red");
            colorTable.Append (color.Red / 256);
            colorTable.Append (@"\green");
            colorTable.Append (color.Green / 256);
            colorTable.Append (@"\blue");
            colorTable.Append (color.Blue / 256);
            colorTable.Append (";");
        }
        colorTable.Append ("}");
        return colorTable.ToString ();
    }

    public static string GenerateRtf (TextEditorData data)
    {
        return GenerateRtf (data.Document, data.Document.SyntaxMode, data.ColorStyle, data.Options);
    }

    static void AppendRtfText (StringBuilder rtfText, TextDocument doc, int start, int end, ref bool appendSpace)
    {
        for (int i = start; i < end; i++)
        {
            char ch = doc.GetCharAt (i);
            switch (ch)
            {
            case '\\':
                rtfText.Append (@"\\");
                break;
            case '{':
                rtfText.Append (@"\{");
                break;
            case '}':
                rtfText.Append (@"\}");
                break;
            case '\t':
                rtfText.Append (@"\tab");
                appendSpace = true;
                break;
            default:
                if (appendSpace)
                {
                    rtfText.Append (' ');
                    appendSpace = false;
                }
                rtfText.Append (ch);
                break;
            }
        }
    }
    public static string GenerateRtf (TextDocument doc, Mono.TextEditor.Highlighting.ISyntaxMode mode, Mono.TextEditor.Highlighting.ColorScheme style, ITextEditorOptions options)
    {
        var rtfText = new StringBuilder ();
        var colorList = new List<Gdk.Color> ();

        var selection = new TextSegment (0, doc.TextLength);
        int startLineNumber = doc.OffsetToLineNumber (selection.Offset);
        int endLineNumber = doc.OffsetToLineNumber (selection.EndOffset);

        bool isItalic = false;
        bool isBold = false;
        int curColor = -1;
        foreach (var line in doc.GetLinesBetween (startLineNumber, endLineNumber))
        {
            bool appendSpace = false;
            if (mode == null)
            {
                AppendRtfText (rtfText, doc, System.Math.Max (selection.Offset, line.Offset), System.Math.Min (line.EndOffset, selection.EndOffset), ref appendSpace);
                continue;
            }
            foreach (var chunk in mode.GetChunks (style, line, line.Offset, line.Length))
            {
                int start = System.Math.Max (selection.Offset, chunk.Offset);
                int end = System.Math.Min (chunk.EndOffset, selection.EndOffset);
                var chunkStyle = style.GetChunkStyle (chunk);
                if (start < end)
                {
                    if (isBold != chunkStyle.Bold)
                    {
                        rtfText.Append (chunkStyle.Bold ? @"\b" : @"\b0");
                        isBold = chunkStyle.Bold;
                        appendSpace = true;
                    }
                    if (isItalic != chunkStyle.Italic)
                    {
                        rtfText.Append (chunkStyle.Italic ? @"\i" : @"\i0");
                        isItalic = chunkStyle.Italic;
                        appendSpace = true;
                    }
                    if (!colorList.Contains (chunkStyle.Color))
                        colorList.Add (chunkStyle.Color);
                    int color = colorList.IndexOf (chunkStyle.Color);
                    if (curColor != color)
                    {
                        curColor = color;
                        rtfText.Append (@"\cf" + (curColor + 1));
                        appendSpace = true;
                    }
                    AppendRtfText (rtfText, doc, start, end, ref appendSpace);
                }
            }
            rtfText.Append (@"\par");
            rtfText.AppendLine ();
        }

        var rtf = new StringBuilder();

        rtf.Append (@"{\rtf1\ansi\deff0\adeflang1025");

        // font table
        rtf.Append (@"{\fonttbl");

        rtf.Append (@"{\f0\fnil\fprq1\fcharset128 " + options.Font.Family + ";}");

        rtf.Append ("}");

        rtf.Append (CreateColorTable (colorList));

        rtf.Append (@"\viewkind4\uc1\pard");

        rtf.Append (@"\f0");
        try
        {
            string fontName = options.Font.ToString ();
            double fontSize = Double.Parse (fontName.Substring (fontName.LastIndexOf (' ')  + 1), System.Globalization.CultureInfo.InvariantCulture) * 2;
            rtf.Append (@"\fs");
            rtf.Append (fontSize);
        }
        catch (Exception) {};
        rtf.Append (@"\cf1");
        rtf.Append (rtfText.ToString ());
        rtf.Append("}");
        return rtf.ToString ();
    }
}
}

