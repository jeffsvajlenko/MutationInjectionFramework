






 

using System;

namespace Greenshot.Helpers
{
 
 
 
public class DrawingHelper
{
    private DrawingHelper()
    {
    }

     
     
     
    public  static double CalculateLinePointDistance(double x1, double y1, double x2,  double y2, double px, double py)
    {
         
         
        x2 -= x1;
        y2 -= y1;
         
        px -= x1;
        py -= y1;
        double dotprod = px * x2  + py * y2;

	 

	

 

         
         
         
        double projlenSq = dotprod  * dotprod / (x2 * x2 + y2  * y2);
	
 
         
         
	

         
	double lenSq = px * px + py * py - projlenSq ;
        if (lenSq < 0)
        {
            lenSq = 0;
        }
        return Math.Sqrt(lenSq);
    }
}
}
