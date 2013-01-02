using System;
using System.Drawing;
using System.Windows.Forms;
using System.Runtime.Serialization;
using Greenshot.Helpers;

namespace Greenshot.Drawing
{
/// <summary>
/// Description of EllipseContainer.
/// </summary>
[Serializable()]
public class EllipseContainer : DrawableContainer
{

    public EllipseContainer(Control parent) : base(parent)
    {
        supportedProperties.Add(DrawableContainer.Property.LINECOLOR);
        supportedProperties.Add(DrawableContainer.Property.FILLCOLOR);
        supportedProperties.Add(DrawableContainer.Property.THICKNESS);
    }

    #region serialization
    public EllipseContainer(SerializationInfo info, StreamingContext ctxt) : base(info,ctxt)
    {
    }
    public override void GetObjectData(SerializationInfo info, StreamingContext ctxt)
    {
        base.GetObjectData(info,ctxt);
    }
    #endregion

    public override void Draw(Graphics g, RenderMode rm)
    {
        g.SmoothingMode = System.Drawing.Drawing2D.SmoothingMode.HighQuality;
        Pen pen = new Pen(foreColor);
        pen.Width = thickness;
        Brush brush = new SolidBrush(backColor);
        Rectangle rect = GuiRectangle.GetGuiRectangle(this.Left, this.Top, this.Width, this.Height);
        g.FillEllipse(brush, rect);
        g.DrawEllipse(pen, rect);
    }
}
}
