/*
 * Benutzer: thomas
 * Datum: 20.03.2007
 * Zeit: 22:05
 *
 */

using System;
using System.Drawing;
using System.Drawing.Imaging;
using System.Windows.Forms;
using System.Reflection;
using Greenshot;
using Greenshot.Configuration;
using Greenshot.UnmanagedHelpers;
using System.Diagnostics;
using System.IO;

namespace Greenshot.Helpers
{
/// <summary>
/// Description of WindowCapture.
/// </summary>
public class WindowCapture
{
    private WindowCapture()
    {
    }

    public static Image CaptureWindow()
    {
        return CaptureWindow(User32.GetDesktopWindow(), MainForm.GetScreenBounds());
    }

    /*public static Image CaptureWindow(IntPtr handle)
    {
        User32.RECT windowRect = new User32.RECT();
        User32.GetWindowRect(handle,ref windowRect);
        Rectangle r = new Rectangle(
            0,
            0,
            windowRect.right - windowRect.left,
            windowRect.bottom - windowRect.top
       	);
    	return CaptureWindow(handle, r);
    }*/

    public static Image CaptureWindow(IntPtr handle, Rectangle rect)
    {
        // get te hDC of the target window
        IntPtr hdcSrc = User32.GetWindowDC(handle);
        // get the size
        int left = rect.X;
        int top = rect.Y;
        int width = rect.Width;
        int height = rect.Height;
        // create a device context we can copy to
        IntPtr hdcDest = GDI32.CreateCompatibleDC(hdcSrc);
        // create a bitmap we can copy it to,
        // using GetDeviceCaps to get the width/height
        IntPtr hBitmap = GDI32.CreateCompatibleBitmap(hdcSrc,width,height);
        // select the bitmap object
        IntPtr hOld = GDI32.SelectObject(hdcDest,hBitmap);
        // bitblt over
        GDI32.BitBlt(hdcDest,0,0,width,height,hdcSrc,left,top,GDI32.SRCCOPY);
        // restore selection
        GDI32.SelectObject(hdcDest,hOld);
        // clean up
        GDI32.DeleteDC(hdcDest);
        User32.ReleaseDC(handle,hdcSrc);
        // get a .NET image object for it
        Image img = Image.FromHbitmap(hBitmap);
        // free up the Bitmap object
        GDI32.DeleteObject(hBitmap);
        return img;
    }
}
}
