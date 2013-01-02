using System;
using System.Drawing;
using System.Windows.Forms;
using System.Runtime.Serialization;
using Greenshot.Helpers;

namespace Greenshot.Drawing
{
/// <summary>
/// Description of RectangleContainer.
/// </summary>
[Serializable()]
public class RectangleContainer : DrawableContainer
{

    public RectangleContainer(Control parent) : base(parent)
    {
        supportedProperties.Add(DrawableContainer.Property.LINECOLOR);
        supportedProperties.Add(DrawableContainer.Property.FILLCOLOR);
        supportedProperties.Add(DrawableContainer.Property.THICKNESS);
    }

    #region serialization
    public RectangleContainer(SerializationInfo info, StreamingContext ctxt) : base(info,ctxt)
    {
    }
    public override void GetObjectData(SerializationInfo info, StreamingContext ctxt)
    {
        base.GetObjectData(info,ctxt);
    }
    #endregion


    public override void Draw(Graphics g, RenderMode rm)
    {
        Pen pen = new Pen(foreColor);
        pen.Width = thickness;
        Brush brush = new SolidBrush(backColor);
        Rectangle rect = GuiRectangle.GetGuiRectangle(this.Left, this.Top, this.Width, this.Height);
        g.FillRectangle(brush, rect);
        g.DrawRectangle(pen, rect);
    }
}
}
