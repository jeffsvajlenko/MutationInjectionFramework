using System;
using System.Drawing;
using System.Drawing.Drawing2D;
using System.Windows.Forms;
using System.Runtime.Serialization;
using System.Collections.Generic;
using Greenshot.Helpers;

namespace Greenshot.Drawing
{
/// <summary>
/// Description of LineContainer.
/// </summary>
[Serializable()]
public class LineContainer : DrawableContainer
{

    public bool HasStartPointArrowHead = false;
    public bool HasEndPointArrowHead = false;

    public LineContainer(Control parent) : base(parent)
    {
        supportedProperties.Add(DrawableContainer.Property.LINECOLOR);
        supportedProperties.Add(DrawableContainer.Property.ARROWHEADS);
        supportedProperties.Add(DrawableContainer.Property.THICKNESS);
        grippers[1].Enabled = false;
        grippers[2].Enabled = false;
        grippers[3].Enabled = false;
        grippers[5].Enabled = false;
        grippers[6].Enabled = false;
        grippers[7].Enabled = false;
    }

    #region serialization
    public LineContainer(SerializationInfo info, StreamingContext ctxt) : base(info,ctxt)
    {
        HasStartPointArrowHead = info.GetBoolean("hasStartPointArrowHead");
        HasEndPointArrowHead = info.GetBoolean("hasEndPointArrowHead");

    }
    public override void GetObjectData(SerializationInfo info, StreamingContext ctxt)
    {
        base.GetObjectData(info,ctxt);
        info.AddValue("hasStartPointArrowHead", HasStartPointArrowHead);
        info.AddValue("hasEndPointArrowHead", HasEndPointArrowHead);
    }
    #endregion

    public override void Draw(Graphics g, RenderMode rm)
    {
        g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.HighQuality;
        Pen pen = new Pen(foreColor);
        pen.Width = thickness;

        AdjustableArrowCap aac = new AdjustableArrowCap(4,6);
        if(HasStartPointArrowHead) pen.CustomStartCap = aac;
        if(HasEndPointArrowHead) pen.CustomEndCap = aac;

        g.DrawLine(pen, this.Left, this.Top, this.Left + this.Width, this.Top + this.Height);
    }

    public override bool ClickableAt(int x, int y)
    {
        if (!base.ClickableAt(x, y))
        {
            return false;
        }
        double distance = DrawingHelper.CalculateLinePointDistance(this.Left, this.Top, this.Left + this.Width, this.Top + this.Height, x, y);
        return distance < 5;
    }

}
}
