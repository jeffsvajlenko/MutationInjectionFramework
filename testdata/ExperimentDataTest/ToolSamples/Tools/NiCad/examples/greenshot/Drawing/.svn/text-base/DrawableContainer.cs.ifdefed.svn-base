using System;
using System.Drawing;
using System.Runtime.Serialization;
using System.Windows.Forms;
using Greenshot.Helpers;
using Greenshot.Configuration;
using System.Collections.Generic;
using System.Diagnostics;

namespace Greenshot.Drawing
{
/// <summary>
/// represents a rectangle, ellipse or label
/// serializable for clipboard support
/// </summary>
[Serializable()]
public abstract class DrawableContainer : ISerializable
{
    public enum RenderMode {EDIT, EXPORT};
    public enum Property {LINECOLOR, FILLCOLOR, THICKNESS, ARROWHEADS};

    protected Color foreColor = Color.Red;
    public virtual Color ForeColor
    {
        get
        {
            return foreColor;
        }
        set
        {
            foreColor = value;
            if (parent != null) parent.Invalidate();
        }
    }
    protected Color backColor = Color.Transparent;
    public Color BackColor
    {
        get
        {
            return backColor;
        }
        set
        {
            backColor = value;
            if (parent != null) parent.Invalidate();
        }
    }
    protected int thickness = 1;
    public int Thickness
    {
        get
        {
            return thickness;
        }
        set
        {
            thickness = value;
            if (parent != null) parent.Invalidate();
        }
    }

    protected List<Property> supportedProperties = new List<Property>();

    public bool PropertySupported(Property prop)
    {
        return (supportedProperties.Contains(prop));
    }

    [NonSerialized]
    protected Control parent;
    public Control Parent
    {
        get
        {
            return parent;
        }
        set
        {
            SwitchParent(value);
        }
    }
    [NonSerialized]
    protected Label[] grippers;
    private bool layoutSuspended = false;
    [NonSerialized]
    protected Label childLabel = new Label();

    [NonSerialized]
    public bool Selected = false;

    private int left = 0;
    public int Left
    {
        get
        {
            return left;
        }
        set
        {
            left = value;
            DoLayout();
        }
    }

    private int top = 0;
    public int Top
    {
        get
        {
            return top;
        }
        set
        {
            top = value;
            DoLayout();
        }
    }

    private int width = 0;
    public int Width
    {
        get
        {
            return width;
        }
        set
        {
            width = value;
            DoLayout();
        }
    }

    private int height = 0;
    public int Height
    {
        get
        {
            return height;
        }
        set
        {
            height = value;
            DoLayout();
        }
    }

    public virtual void GetObjectData(SerializationInfo info, StreamingContext ctxt)
    {
        info.AddValue("foreColor", foreColor);
        info.AddValue("backColor", backColor);
        info.AddValue("thickness", thickness);
        info.AddValue("supportedProperties", supportedProperties);
        info.AddValue("left", left);
        info.AddValue("top", top);
        info.AddValue("width", width);
        info.AddValue("height", height);
    }
    public DrawableContainer(SerializationInfo info, StreamingContext ctxt)
    {
        childLabel.ForeColor = foreColor = (Color)info.GetValue("foreColor", typeof(Color));
        backColor = (Color)info.GetValue("backColor", typeof(Color));
        thickness = info.GetInt16("thickness");
        supportedProperties = (List<Property>)info.GetValue("supportedProperties",typeof(List<Property>));
        left = info.GetInt16("left");
        top = info.GetInt16("top");
        width = info.GetInt16("width");
        height = info.GetInt16("height");
    }

    public DrawableContainer(Control parent)
    {
        this.parent = parent;
        InitControls();
    }

    public Label GetLabel()
    {
        return childLabel;
    }

    public virtual bool InitContent()
    {
        return true;
    }

    public virtual void OnDoubleClick() {}

    private void InitControls()
    {
        grippers = new Label[8];
        for(int i=0; i<grippers.Length; i++)
        {
            grippers[i] = new Label();
            grippers[i].Name = "gripper" + i;
            grippers[i].Width = grippers[i].Height = 5;
            grippers[i].BackColor = Color.Black;
            grippers[i].MouseDown += new MouseEventHandler(gripperMouseDown);
            grippers[i].MouseUp += new MouseEventHandler(gripperMouseUp);
            grippers[i].MouseMove += new MouseEventHandler(gripperMouseMove);
            grippers[i].Visible = false;
        }
        grippers[1].Cursor = Cursors.SizeNS;
        grippers[3].Cursor = Cursors.SizeWE;
        grippers[5].Cursor = Cursors.SizeNS;
        grippers[7].Cursor = Cursors.SizeWE;
        parent.Controls.AddRange(grippers);

        childLabel.BackColor = Color.Transparent;
        childLabel.BorderStyle = BorderStyle.None;
        childLabel.Cursor = Cursors.SizeAll;
        childLabel.MouseDown += new MouseEventHandler(gripperMouseDown);
        childLabel.MouseUp += new MouseEventHandler(gripperMouseUp);
        childLabel.MouseMove += new MouseEventHandler(childLabelMouseMove);

        DoLayout();
    }

    public void SuspendLayout()
    {
        layoutSuspended = true;
        childLabel.SuspendLayout();
    }

    public void ResumeLayout()
    {
        layoutSuspended = false;
        DoLayout();
        childLabel.ResumeLayout();
    }

    private void DoLayout()
    {
        if (!layoutSuspended)
        {
            int[] xChoords = new int[] {this.Left-2,this.Left+this.Width/2-2,this.Left+this.Width-2};
            int[] yChoords = new int[] {this.Top-2,this.Top+this.Height/2-2,this.Top+this.Height-2};

            grippers[0].Left = xChoords[0];
            grippers[0].Top = yChoords[0];
            grippers[1].Left = xChoords[1];
            grippers[1].Top = yChoords[0];
            grippers[2].Left = xChoords[2];
            grippers[2].Top = yChoords[0];
            grippers[3].Left = xChoords[2];
            grippers[3].Top = yChoords[1];
            grippers[4].Left = xChoords[2];
            grippers[4].Top = yChoords[2];
            grippers[5].Left = xChoords[1];
            grippers[5].Top = yChoords[2];
            grippers[6].Left = xChoords[0];
            grippers[6].Top = yChoords[2];
            grippers[7].Left = xChoords[0];
            grippers[7].Top = yChoords[1];

            if((grippers[0].Left < grippers[4].Left && grippers[0].Top < grippers[4].Top) ||
                    grippers[0].Left > grippers[4].Left && grippers[0].Top > grippers[4].Top)
            {
                grippers[0].Cursor = Cursors.SizeNWSE;
                grippers[2].Cursor = Cursors.SizeNESW;
                grippers[4].Cursor = Cursors.SizeNWSE;
                grippers[6].Cursor = Cursors.SizeNESW;
            }
            else if((grippers[0].Left > grippers[4].Left && grippers[0].Top < grippers[4].Top) ||
                    grippers[0].Left < grippers[4].Left && grippers[0].Top > grippers[4].Top)
            {
                grippers[0].Cursor = Cursors.SizeNESW;
                grippers[2].Cursor = Cursors.SizeNWSE;
                grippers[4].Cursor = Cursors.SizeNESW;
                grippers[6].Cursor = Cursors.SizeNWSE;
            }
            else if (grippers[0].Left == grippers[4].Left)
            {
                grippers[0].Cursor = Cursors.SizeNS;
                grippers[4].Cursor = Cursors.SizeNS;
            }
            else if (grippers[0].Top == grippers[4].Top)
            {
                grippers[0].Cursor = Cursors.SizeWE;
                grippers[4].Cursor = Cursors.SizeWE;
            }

            childLabel.Left = this.Left;
            childLabel.Top = this.Top;
            childLabel.Width = this.Width;
            childLabel.Height = this.Height;
        }
    }

    public void Dispose()
    {
        for(int i=0; i<grippers.Length; i++)
        {
            grippers[i].Dispose();
        }
        childLabel.Dispose();
    }

    int mx;
    int my;
    bool mouseDown = false;
    private void gripperMouseDown(object sender, MouseEventArgs e)
    {
        mx = e.X;
        my = e.Y;
        mouseDown = true;
    }

    private void gripperMouseUp(object sender, MouseEventArgs e)
    {
        mouseDown = false;
    }

    private void gripperMouseMove(object sender, MouseEventArgs e)
    {
        if(mouseDown)
        {
            SuspendLayout();
            Label gr = (Label)sender;
            int gripperIndex = Int16.Parse(gr.Name.Substring(7));
            if(gripperIndex <= 2)   // top row
            {
                this.Top += e.Y - my;
                this.Height -= e.Y - my;
            }
            else if(gripperIndex >= 4 && gripperIndex <= 6)     // bottom row
            {
                this.Height += e.Y - my;
            }
            if(gripperIndex >=2 && gripperIndex <= 4)   // right row
            {
                this.Width += e.X - mx;
            }
            else if(gripperIndex >=6 || gripperIndex == 0)     // left row
            {
                this.Left += e.X - mx;
                this.Width -= e.X - mx;
            }
            ResumeLayout();
            parent.Invalidate();
        }
    }

    private void childLabelMouseMove(object sender, MouseEventArgs e)
    {
        if (mouseDown)
        {
            SuspendLayout();
            this.Left += e.X - mx;
            this.Top += e.Y - my;
            ResumeLayout();
            parent.Invalidate();
        }
    }

    public abstract void Draw(Graphics g, RenderMode renderMode);

    public void Draw(Graphics g)
    {
        Draw(g, RenderMode.EDIT);
    }

    public virtual bool ClickableAt(int x, int y)
    {
        Rectangle r = GuiRectangle.GetGuiRectangle(Left, Top, Width, Height);
        r.Inflate(5, 5);
        return r.Contains(x, y);
    }

    protected void DrawSelectionBorder(Graphics g, Rectangle rect)
    {
        Pen pen = new Pen(Color.MediumSeaGreen);
        pen.DashPattern = new float[] {1,2};
        pen.Width = 1;
        g.DrawRectangle(pen, rect);
    }

    public void ShowGrippers()
    {
        for(int i=0; i<grippers.Length; i++)
        {
            if(grippers[i].Enabled) grippers[i].Show();
            else grippers[i].Hide();
        }
        this.ResumeLayout();
    }

    public void HideGrippers()
    {
        this.SuspendLayout();
        for(int i=0; i<grippers.Length; i++)
        {
            grippers[i].Hide();
        }
    }

    public void MoveBy(int dx, int dy)
    {
        this.SuspendLayout();
        this.Left += dx;
        this.Top += dy;
        this.ResumeLayout();
    }

    private void SwitchParent(Control newParent)
    {
        if(parent != null)
        {
            for(int i=0; i<grippers.Length; i++)
            {
                parent.Controls.Remove(grippers[i]);
            }
        }
        parent = newParent;
        InitControls();
    }
}
}
