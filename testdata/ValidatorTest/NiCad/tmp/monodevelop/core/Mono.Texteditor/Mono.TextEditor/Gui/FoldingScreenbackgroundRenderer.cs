//
// FoldingScreenbackgroundRenderer.cs
//
// Author:
//       Mike Krüger <mkrueger@novell.com>
//
// Copyright (c) 2009 Novell, Inc (http://www.novell.com)
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
using Mono.TextEditor.Highlighting;

namespace Mono.TextEditor
{


public class FoldingScreenbackgroundRenderer : IBackgroundRenderer
{
    TextEditor editor;
    List<FoldSegment> foldSegments;
    Roles[] roles;
    [Flags]
    enum Roles
    {
        Between = 0,
        Start = 1,
        End = 2
    }
    TextDocument Document
    {
        get
        {
            return editor.Document;
        }
    }

    public FoldingScreenbackgroundRenderer (TextEditor editor, IEnumerable<FoldSegment> foldSegments)
    {
        this.editor = editor;
        this.foldSegments = new List<FoldSegment> (foldSegments);
        this.roles = new Roles[this.foldSegments.Count];
    }

    public void Draw (Cairo.Context cr, Cairo.Rectangle area, DocumentLine lineSegment, double x, double y, double lineHeight)
    {
        int foundSegment = -1;
        if (lineSegment != null)
        {
            for (int i = 0; i < foldSegments.Count; i++)
            {
                FoldSegment segment = foldSegments [i];
                if (segment.StartLine.Offset <= lineSegment.Offset && lineSegment.EndOffsetIncludingDelimiter <= segment.EndLine.EndOffsetIncludingDelimiter)
                {
                    foundSegment = i;
                    roles [i] = Roles.Between;
                    if (segment.StartLine.Offset == lineSegment.Offset)
                    {
                        roles [i] |= Roles.Start;
                        if (segment.IsFolded)
                            roles [i] |= Roles.End;
                    }
                    if (segment.EndLine.Offset == lineSegment.Offset)
                        roles [i] |= Roles.End;
                }
            }
        }
        TextViewMargin textViewMargin = editor.TextViewMargin;
        ISyntaxMode mode = Document.SyntaxMode != null && editor.Options.EnableSyntaxHighlighting ? Document.SyntaxMode : new SyntaxMode (Document);

        //	Gdk.Rectangle lineArea = new Gdk.Rectangle (textViewMargin.XOffset, y, editor.Allocation.Width - textViewMargin.XOffset, editor.LineHeight);
        //	Gdk.GC gc = new Gdk.GC (drawable);
        TextViewMargin.LayoutWrapper lineLayout = null;
        double brightness = HslColor.Brightness (editor.ColorStyle.Default.BackgroundColor);

        int colorCount = foldSegments.Count + 2;
        for (int segment = -1; segment <= foundSegment; segment++)
        {
            HslColor hslColor = new HslColor (editor.ColorStyle.Default.BackgroundColor);
            int colorPosition = segment + 1;
            if (segment == foldSegments.Count - 1)
                colorPosition += 2;
            if (brightness < 0.5)
            {
                hslColor.L = hslColor.L * 0.81 + hslColor.L * 0.25 * (colorCount - colorPosition) / colorCount;
            }
            else
            {
                hslColor.L = hslColor.L * 0.86 + hslColor.L * 0.1 * colorPosition / colorCount;
            }

            Roles role = Roles.Between;
            double xPos = textViewMargin.XOffset;
            double rectangleWidth = editor.Allocation.Width - xPos;
            if (segment >= 0)
            {
                DocumentLine segmentStartLine = foldSegments [segment].StartLine;
                lineLayout = textViewMargin.CreateLinePartLayout (mode, segmentStartLine, segmentStartLine.Offset, segmentStartLine.Length, -1, -1);
                Pango.Rectangle rectangle = lineLayout.Layout.IndexToPos (GetFirstNonWsIdx (lineLayout.Layout.Text));
                xPos = System.Math.Max (textViewMargin.XOffset, (textViewMargin.XOffset + rectangle.X / Pango.Scale.PangoScale - editor.HAdjustment.Value));

                DocumentLine segmentEndLine = foldSegments [segment].EndLine;
                lineLayout = textViewMargin.CreateLinePartLayout (mode, segmentEndLine, segmentEndLine.Offset, segmentEndLine.Length, -1, -1);
                rectangle = lineLayout.Layout.IndexToPos (GetFirstNonWsIdx (lineLayout.Layout.Text));
                xPos = System.Math.Min (xPos, System.Math.Max (textViewMargin.XOffset, (textViewMargin.XOffset + rectangle.X / Pango.Scale.PangoScale - editor.HAdjustment.Value)));

                int width = editor.Allocation.Width;
                if (editor.HAdjustment.Upper > width)
                {
                    width = (int)(textViewMargin.XOffset + editor.HAdjustment.Upper - editor.HAdjustment.Value);
                }
                rectangleWidth = (int)(width - xPos - 6 * (segment + 1));
                role = roles [segment];
            }
            DrawRoundRectangle (cr, (role & Roles.Start) == Roles.Start, (role & Roles.End) == Roles.End, xPos, y, editor.LineHeight / 2, rectangleWidth, lineHeight);
            cr.Color = ColorScheme.ToCairoColor (hslColor);
            cr.Fill ();
            /*		if (segment == foldSegments.Count - 1) {
            	cr.Color = new Cairo.Color (0.5, 0.5, 0.5, 1);
            	cr.Stroke ();
            }*/
            if (lineLayout != null && lineLayout.IsUncached)
            {
                lineLayout.Dispose ();
                lineLayout = null;
            }
        }
        //		gc.Dispose ();
    }

    public static void DrawRoundRectangle (Cairo.Context cr, bool upperRound, bool lowerRound, double x, double y, double r, double w, double h)
    {
        DrawRoundRectangle (cr, upperRound, upperRound, lowerRound, lowerRound, x, y, r, w, h);
    }

    public static void DrawRoundRectangle (Cairo.Context cr, bool topLeftRound, bool topRightRound, bool bottomLeftRound, bool bottomRightRound,  double x, double y, double r, double w, double h)
    {
        //  UA****BQ
        //  H      C
        //  *      *
        //  G      D
        //  TF****ES

        cr.NewPath ();

        if (topLeftRound)
        {
            cr.MoveTo (x + r, y);                 // Move to A
        }
        else
        {
            cr.MoveTo (x, y);             // Move to U
        }

        if (topRightRound)
        {
            cr.LineTo (x + w - r, y);             // Straight line to B

            cr.CurveTo (x + w, y,
                        x + w, y,
                        x + w, y + r); // Curve to C, Control points are both at Q
        }
        else
        {
            cr.LineTo (x + w, y);         // Straight line to Q
        }

        if (bottomRightRound)
        {
            cr.LineTo (x + w, y + h - r);                              // Move to D

            cr.CurveTo (x + w, y + h,
                        x + w, y + h,
                        x + w - r, y + h); // Curve to E
        }
        else
        {
            cr.LineTo (x + w, y + h); // Move to S
        }

        if (bottomLeftRound)
        {
            cr.LineTo (x + r, y + h);                      // Line to F
            cr.CurveTo (x, y + h,
                        x, y + h ,
                        x, y + h - r); // Curve to G
        }
        else
        {
            cr.LineTo (x, y + h); // Line to T
        }

        if (topLeftRound)
        {
            cr.LineTo (x, y + r);              // Line to H
            cr.CurveTo (x, y,
                        x , y,
                        x + r, y); // Curve to A
        }
        else
        {
            cr.LineTo (x, y); // Line to U
        }
        cr.ClosePath ();
    }

    int GetFirstNonWsIdx (string text)
    {
        for (int i = 0; i < text.Length; i++)
        {
            if (!Char.IsWhiteSpace (text[i]))
                return i;
        }
        return 0;
    }

}
}
