/*
 * Benutzer: thomas
 * Datum: 20.03.2007
 * Zeit: 21:54
 *
 */

using System;
using System.Drawing;

namespace Greenshot.Helpers
{
/// <summary>
/// Description of GuiRectangle.
/// </summary>
public class GuiRectangle
{
    private GuiRectangle()
    {

    }

    public static Rectangle GetGuiRectangle(int x, int y, int w, int h)
    {
        if (w < 0)
        {
            x = x + w;
            w = -w;
        }
        if (h < 0)
        {
            y = y + h;
            h = -h;
        }
        return new Rectangle(x, y, w, h);
    }

}
}
