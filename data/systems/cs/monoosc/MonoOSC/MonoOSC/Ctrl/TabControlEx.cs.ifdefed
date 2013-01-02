using System;
using System.Windows.Forms;
using System.Drawing;

namespace MonoOSC
{
public delegate bool PreRemoveTab(int indx);
public class TabControlEx : TabControl
{
    public TabControlEx()
    : base()
    {
        PreRemoveTabPage = null;
        this.DrawMode = TabDrawMode.OwnerDrawFixed;
    }

    public PreRemoveTab PreRemoveTabPage;

    protected override void OnDrawItem(DrawItemEventArgs e)
    {
        this.TabPages[e.Index].SuspendLayout();

        Brush b = new SolidBrush(Color.Blue);
        Rectangle r = e.Bounds;
        r = GetTabRect(e.Index);
        if (e.Index == 0)
            r.Offset(-15, 2);
        else
        {
            r.Offset(2, 2);
            r.Height = 18;
            r.Width = 18;
            Pen p = new Pen(b);
            e.Graphics.DrawIcon(Properties.Resources.CloseIco, r);
            e.Graphics.DrawRectangle(p, r);
        }

        string titel = this.TabPages[e.Index].Text;
        Font f = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0))); //this.Font;
        b = new SolidBrush(Color.Black);
        e.Graphics.DrawString(titel, f, b, new PointF(r.X + 18, r.Y), StringFormat.GenericTypographic);
        this.TabPages[e.Index].ResumeLayout();
    }

    protected override void OnMouseClick(MouseEventArgs e)
    {
        Point p = e.Location;
        for (int i = 0; i < TabCount; i++)
        {
            if (i > 0)
            {
                Rectangle r = GetTabRect(i);
                r.Offset(2, 2);
                r.Width = 18;
                r.Height = 18;
                if (r.Contains(p))
                {
                    CloseTab(i);
                }
            }
        }
    }

    //public delegate TabPageClosing();
    public event PreRemoveTab TabPageClosing;
    private void CloseTab(int i)
    {
        TabPageClosing.Invoke(i);
        if (PreRemoveTabPage != null)
        {
            bool closeIt = PreRemoveTabPage(i);
            if (!closeIt)
                return;
        }
        //TabPages.Remove(TabPages[i]);
    }
}
}
