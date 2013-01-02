/*
 * Created by SharpDevelop.
 * User: jens
 * Date: 17.04.2007
 * Time: 21:25
 *
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */

using System;
using System.Drawing;
using System.Threading;
using System.Windows.Forms;

namespace Greenshot
{
/// <summary>
/// Description of FlashlightForm.
/// </summary>
public partial class FlashlightForm : Form
{
    private int framesPerSecond = 25;
    public FlashlightForm()
    {
        //
        // The InitializeComponent() call is required for Windows Forms designer support.
        //
        InitializeComponent();

        //
        // TODO: Add constructor code after the InitializeComponent() call.
        //
        this.TransparencyKey = Color.Magenta;
    }

    public void FadeIn()
    {
        Opacity = 0;
        Show();
        Fade(2, 0, 1, 2);
    }
    public void FadeOut()
    {
        Fade(6, 1, 0, 2);
        Hide();
    }
    private void Fade(int frames, double startOpacity,  double targetOpacity, double exponent)
    {
        this.Opacity = startOpacity;
        double baseOpacity = Math.Min(startOpacity, targetOpacity);
        double diff = Math.Abs(targetOpacity - startOpacity);
        double stepWidth= (double)(10) / (double)frames;
        double maxValue = Math.Pow(10, exponent);
        for(int i=0; i<=frames; i++)
        {
            double x = ((startOpacity < targetOpacity) ? i : frames -i) * stepWidth;
            double factor = Math.Pow(x,exponent) / 100;
            this.Opacity = baseOpacity + factor * diff;
            Thread.Sleep(1000 / framesPerSecond);
        }
    }
    private void FlashlightForm_Load( object sender, EventArgs e )
    {
        // Set the bounds.
        this.Bounds = MainForm.GetScreenBounds();
    }
}
}
