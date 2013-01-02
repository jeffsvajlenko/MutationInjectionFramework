/*
 * Benutzer: thomas
 * Datum: 20.03.2007
 * Zeit: 20:29
 *
 */

using System;
using System.Text;
using System.Runtime.InteropServices;

namespace Greenshot.UnmanagedHelpers
{
/// <summary>
/// Description of User32.
/// </summary>
public class User32
{
    [StructLayout(LayoutKind.Sequential)]
    public struct RECT
    {
        public int left;
        public int top;
        public int right;
        public int bottom;
    }
    [DllImport("user32.dll")]
    public static extern IntPtr GetDesktopWindow();
    [DllImport("user32.dll")]
    public static extern IntPtr GetWindowDC(IntPtr hWnd);
    [DllImport("user32.dll")]
    public static extern IntPtr ReleaseDC(IntPtr hWnd,IntPtr hDC);
    [DllImport("user32.dll")]
    public static extern IntPtr GetWindowRect(IntPtr hWnd,ref RECT rect);
    [DllImport("user32.dll")]
    public static extern IntPtr WindowFromPoint(int xPoint, int yPoint);

    public const int WM_HOTKEY = 0x312;
    public const int VK_SNAPSHOT = 0x2C;
    public const int MOD_NONE = 0;
    public const int MOD_ALT = 1;
    public const int MOD_CTRL = 2;
    public const int MOD_SHIFT = 4;

    [DllImport("user32.dll")]
    public static extern int RegisterHotKey (int hwnd, int id, int fsModifiers, int vk);
    [DllImport("user32.dll")]
    public static extern int UnregisterHotKey (int hwnd, int id);

    [DllImport("User32.dll")]
    public static extern IntPtr GetDC(IntPtr hwnd);
    [DllImport("User32.dll")]
    public static extern void ReleaseDC(IntPtr dc);

    [DllImport("user32.dll")]
    public static extern int GetWindowText(int hWnd, StringBuilder title, int size);
    [DllImport("user32.dll")]
    public static extern int EnumWindows(EnumWindowsProc ewp, int lParam);
    [DllImport("user32.dll")]
    public static extern bool IsWindowVisible(int hWnd);

    [DllImport("user32.dll")]
    public static extern int EnumChildWindows(int hWndParent, EnumWindowsProc ewp, int lParam);

    // delegate used for EnumWindows() callback function
    public delegate bool EnumWindowsProc(int hWnd, int lParam);

}
}
