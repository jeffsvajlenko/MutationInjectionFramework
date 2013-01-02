/*
 * Erstellt mit SharpDevelop.
 * Benutzer: jens
 * Datum: 11.02.2008
 * Zeit: 22:12
 *
 * Sie können diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader ändern.
 */

using System;
using System.Drawing;

namespace Greenshot.Helpers
{
/// <summary>
/// Offers a few helper functions for scaling/aligning an element with another element
/// </summary>
public class ScaleHelper
{
    private ScaleHelper()
    {
    }

    /// <summary>
    /// calculates the Size an element must be resized to, in order to fit another element, keeping aspect ratio
    /// </summary>
    /// <param name="currentSize">the size of the element to be resized</param>
    /// <param name="targetSize">the target size of the element</param>
    /// <param name="crop">in case the aspect ratio of currentSize and targetSize differs: shall the scaled size fit into targetSize (i.e. that one of its dimensions is smaller - false) or vice versa (true)</param>
    /// <returns>a new SizeF object indicating the width and height the element should be scaled to</returns>
    public static SizeF GetScaledSize(SizeF currentSize, SizeF targetSize, bool crop)
    {
        float wFactor = targetSize.Width/currentSize.Width;
        float hFactor = targetSize.Height/currentSize.Height;

        float factor = crop ? Math.Max(wFactor, hFactor) : Math.Min(wFactor, hFactor);
        //System.Diagnostics.Debug.WriteLine(currentSize.Width+"..."+targetSize.Width);
        //System.Diagnostics.Debug.WriteLine(wFactor+"..."+hFactor+">>>"+factor);
        return new SizeF(currentSize.Width * factor, currentSize.Height * factor);
    }

    /// <summary>
    /// calculates the position of an element depending on the desired alignment within a RectangleF
    /// </summary>
    /// <param name="currentRect">the bounds of the element to be aligned</param>
    /// <param name="targetRect">the rectangle reference for aligment of the element</param>
    /// <param name="alignment">the System.Drawing.ContentAlignment value indicating how the element is to be aligned should the width or height differ from targetSize</param>
    /// <returns>a new RectangleF object with Location aligned aligned to targetRect</returns>
    public static RectangleF GetAlignedRectangle(RectangleF currentRect, RectangleF targetRect, ContentAlignment alignment)
    {
        RectangleF newRect = new RectangleF(targetRect.Location, currentRect.Size);
        switch(alignment)
        {
        case ContentAlignment.TopCenter:
            newRect.X = (targetRect.Width - currentRect.Width) / 2;
            break;
        case ContentAlignment.TopRight:
            newRect.X = (targetRect.Width - currentRect.Width);
            break;
        case ContentAlignment.MiddleLeft:
            newRect.Y = (targetRect.Height - currentRect.Height) / 2;
            break;
        case ContentAlignment.MiddleCenter:
            newRect.Y = (targetRect.Height - currentRect.Height) / 2;
            newRect.X = (targetRect.Width - currentRect.Width) / 2;
            break;
        case ContentAlignment.MiddleRight:
            newRect.Y = (targetRect.Height - currentRect.Height) / 2;
            newRect.X = (targetRect.Width - currentRect.Width);
            break;
        case ContentAlignment.BottomLeft:
            newRect.Y = (targetRect.Height - currentRect.Height);
            break;
        case ContentAlignment.BottomCenter:
            newRect.Y = (targetRect.Height - currentRect.Height);
            newRect.X = (targetRect.Width - currentRect.Width) / 2;
            break;
        case ContentAlignment.BottomRight:
            newRect.Y = (targetRect.Height - currentRect.Height);
            newRect.X = (targetRect.Width - currentRect.Width);
            break;
        }
        return newRect;
    }

    /// <summary>
    /// calculates the Rectangle an element must be resized an positioned to, in ordder to fit another element, keeping aspect ratio
    /// </summary>
    /// <param name="currentRect">the rectangle of the element to be resized/repositioned</param>
    /// <param name="targetRect">the target size/position of the element</param>
    /// <param name="crop">in case the aspect ratio of currentSize and targetSize differs: shall the scaled size fit into targetSize (i.e. that one of its dimensions is smaller - false) or vice versa (true)</param>
    /// <param name="alignment">the System.Drawing.ContentAlignment value indicating how the element is to be aligned should the width or height differ from targetSize</param>
    /// <returns>a new RectangleF object indicating the width and height the element should be scaled to and the position that should be applied to it for proper alignment</returns>
    public static RectangleF GetScaledRectangle(RectangleF currentRect, RectangleF targetRect, bool crop, ContentAlignment alignment)
    {
        SizeF newSize = GetScaledSize(currentRect.Size, targetRect.Size, crop);
        RectangleF newRect = new RectangleF(new Point(0,0), newSize);
        return GetAlignedRectangle(newRect, targetRect, alignment);
    }
}
}
