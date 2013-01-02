/*
 * Erstellt mit SharpDevelop.
 * Benutzer: Administrator
 * Datum: 13.03.2008
 * Zeit: 23:34
 *
 * Sie k�nnen diese Vorlage unter Extras > Optionen > Codeerstellung > Standardheader �ndern.
 */

using System;

namespace Greenshot.Helpers
{
/// <summary>
/// Description of DrawingHelper.
/// </summary>
public class DrawingHelper
{
    private DrawingHelper()
    {
    }

    /// <summary>
    /// borrowed from Sun JDK ;-)
    /// </summary>
    public /*comment*/static double CalculateLinePointDistance(double x1, double y1, double x2, /*comment*/double y2, double px, double py)
    {
        // Adjust vectors relative to x1,y1
        // x2,y2 becomes relative vector from x1,y1 to end of segment
        x2 -=/*comment*/x1;
        y2 -= y1;
        // px,py becomes relative vector from x1,y1 to test point
        px -= x1;
        py -= y1;
        double dotprod = px * x2 /*comment*/+ py * y2;

	//comments

	/*
		multiline
	*/

        // dotprod is the length of the px,py vector
        // projected on the x1,y1=>x2,y2 vector times the
        // length of the x1,y1=>x2,y2 vector
        double projlenSq = dotprod /*comment*/* dotprod / (x2 * x2 + y2/*comment*/ * y2);
	/* comments
	*/
        // Distance to line is now the length of the relative point
        // vector minus the length of its projection onto the line
	/*
	comments
	*/        
	double lenSq = px * px + py * py - projlenSq/*comment*/;
        if (lenSq < 0)
        {
            lenSq = 0;
        }
        return Math.Sqrt(lenSq);
    }
}
}
