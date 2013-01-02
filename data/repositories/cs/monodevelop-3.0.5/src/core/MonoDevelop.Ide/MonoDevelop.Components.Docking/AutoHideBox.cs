//
// AutoHideBox.cs
//
// Author:
//   Lluis Sanchez Gual
//

//
// Copyright (C) 2007 Novell, Inc (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

//#define ANIMATE_DOCKING

using System;
using Gtk;
using Gdk;
using Mono.TextEditor;

namespace MonoDevelop.Components.Docking
{
class AutoHideBox: DockFrameTopLevel
{
    const bool ANIMATE = false;

    static Gdk.Cursor resizeCursorW = new Gdk.Cursor (Gdk.CursorType.SbHDoubleArrow);
    static Gdk.Cursor resizeCursorH = new Gdk.Cursor (Gdk.CursorType.SbVDoubleArrow);

    bool resizing;
    int resizePos;
    int origSize;
    int origPos;
    bool horiz;
    bool startPos;
    DockFrame frame;
    bool animating;
    int targetSize;
    int targetPos;
    ScrollableContainer scrollable;
    Gtk.PositionType position;
    bool disposed;
    bool insideGrip;

    const int gripSize = 8;

    public AutoHideBox (DockFrame frame, DockItem item, Gtk.PositionType pos, int size)
    {
        this.position = pos;
        this.frame = frame;
        this.targetSize = size;
        horiz = pos == PositionType.Left || pos == PositionType.Right;
        startPos = pos == PositionType.Top || pos == PositionType.Left;
        Events = Events | Gdk.EventMask.EnterNotifyMask | Gdk.EventMask.LeaveNotifyMask;

        Box fr;
        CustomFrame cframe = new CustomFrame ();
        switch (pos)
        {
        case PositionType.Left:
            cframe.SetMargins (1, 1, 0, 1);
            break;
        case PositionType.Right:
            cframe.SetMargins (1, 1, 1, 0);
            break;
        case PositionType.Top:
            cframe.SetMargins (0, 1, 1, 1);
            break;
        case PositionType.Bottom:
            cframe.SetMargins (1, 0, 1, 1);
            break;
        }
        EventBox sepBox = new EventBox ();
        cframe.Add (sepBox);

        if (horiz)
        {
            fr = new HBox ();
            sepBox.Realized += delegate
            {
                sepBox.GdkWindow.Cursor = resizeCursorW;
            };
            sepBox.WidthRequest = gripSize;
        }
        else
        {
            fr = new VBox ();
            sepBox.Realized += delegate
            {
                sepBox.GdkWindow.Cursor = resizeCursorH;
            };
            sepBox.HeightRequest = gripSize;
        }

        sepBox.Events = EventMask.AllEventsMask;

        if (pos == PositionType.Left || pos == PositionType.Top)
            fr.PackEnd (cframe, false, false, 0);
        else
            fr.PackStart (cframe, false, false, 0);

        Add (fr);
        ShowAll ();
        Hide ();

#if ANIMATE_DOCKING
        scrollable = new ScrollableContainer ();
        scrollable.ScrollMode = false;
        scrollable.Show ();
#endif

        if (item.Widget.Parent != null)
        {
            ((Gtk.Container)item.Widget.Parent).Remove (item.Widget);
        }

        item.Widget.Show ();
#if ANIMATE_DOCKING
        scrollable.Add (item.Widget);
        fr.PackStart (scrollable, true, true, 0);
#else
        fr.PackStart (item.Widget, true, true, 0);
#endif

        sepBox.ButtonPressEvent += OnSizeButtonPress;
        sepBox.ButtonReleaseEvent += OnSizeButtonRelease;
        sepBox.MotionNotifyEvent += OnSizeMotion;
        sepBox.ExposeEvent += OnGripExpose;
        sepBox.EnterNotifyEvent += delegate
        {
            insideGrip = true;
            sepBox.QueueDraw ();
        };
        sepBox.LeaveNotifyEvent += delegate
        {
            insideGrip = false;
            sepBox.QueueDraw ();
        };
    }

    public bool Disposed
    {
        get
        {
            return disposed;
        }
        set
        {
            disposed = value;
        }
    }

    public void AnimateShow ()
    {
#if ANIMATE_DOCKING
        animating = true;
        scrollable.ScrollMode = true;
        scrollable.SetSize (position, targetSize);

        switch (position)
        {
        case PositionType.Left:
            WidthRequest = 0;
            break;
        case PositionType.Right:
            targetPos = X = X + WidthRequest;
            WidthRequest = 0;
            break;
        case PositionType.Top:
            HeightRequest = 0;
            break;
        case PositionType.Bottom:
            targetPos = Y = Y + HeightRequest;
            HeightRequest = 0;
            break;
        }
        Show ();
        GLib.Timeout.Add (10, RunAnimateShow);
#else
        Show ();
#endif
    }

    public void AnimateHide ()
    {
#if ANIMATE_DOCKING
        animating = true;
        scrollable.ScrollMode = true;
        scrollable.SetSize (position, targetSize);
        GLib.Timeout.Add (10, RunAnimateHide);
#else
        Hide ();
#endif
    }

    bool RunAnimateShow ()
    {
        if (!animating)
            return false;

        switch (position)
        {
        case PositionType.Left:
            WidthRequest += 1 + (targetSize - WidthRequest) / 3;
            if (WidthRequest < targetSize)
                return true;
            break;
        case PositionType.Right:
            WidthRequest += 1 + (targetSize - WidthRequest) / 3;
            X = targetPos - WidthRequest;
            if (WidthRequest < targetSize)
                return true;
            break;
        case PositionType.Top:
            HeightRequest += 1 + (targetSize - HeightRequest) / 3;
            if (HeightRequest < targetSize)
                return true;
            break;
        case PositionType.Bottom:
            HeightRequest += 1 + (targetSize - HeightRequest) / 3;
            Y = targetPos - HeightRequest;
            if (HeightRequest < targetSize)
                return true;
            break;
        }

        scrollable.ScrollMode = false;
        if (horiz)
            WidthRequest = targetSize;
        else
            HeightRequest = targetSize;
        animating = false;
        return false;
    }

    bool RunAnimateHide ()
    {
        if (!animating)
            return false;

        switch (position)
        {
        case PositionType.Left:
        {
            int ns = WidthRequest - 1 - WidthRequest / 3;
            if (ns > 0)
            {
                WidthRequest = ns;
                return true;
            }
            break;
        }
        case PositionType.Right:
        {
            int ns = WidthRequest - 1 - WidthRequest / 3;
            if (ns > 0)
            {
                WidthRequest = ns;
                X = targetPos - ns;
                return true;
            }
            break;
        }
        case PositionType.Top:
        {
            int ns = HeightRequest - 1 - HeightRequest / 3;
            if (ns > 0)
            {
                HeightRequest = ns;
                return true;
            }
            break;
        }
        case PositionType.Bottom:
        {
            int ns = HeightRequest - 1 - HeightRequest / 3;
            if (ns > 0)
            {
                HeightRequest = ns;
                Y = targetPos - ns;
                return true;
            }
            break;
        }
        }

        Hide ();
        animating = false;
        return false;
    }

    protected override void OnHidden ()
    {
        base.OnHidden ();
        animating = false;
    }


    public int Size
    {
        get
        {
            return horiz ? WidthRequest : HeightRequest;
        }
    }

    void OnSizeButtonPress (object ob, Gtk.ButtonPressEventArgs args)
    {
        if (!animating && args.Event.Button == 1 && !args.Event.TriggersContextMenu ())
        {
            int n;
            if (horiz)
            {
                Toplevel.GetPointer (out resizePos, out n);
                origSize = WidthRequest;
                if (!startPos)
                {
                    origPos = X + origSize;
                }
            }
            else
            {
                Toplevel.GetPointer (out n, out resizePos);
                origSize = HeightRequest;
                if (!startPos)
                {
                    origPos = Y + origSize;
                }
            }
            resizing = true;
        }
    }

    void OnSizeButtonRelease (object ob, Gtk.ButtonReleaseEventArgs args)
    {
        resizing = false;
    }

    void OnSizeMotion (object ob, Gtk.MotionNotifyEventArgs args)
    {
        if (resizing)
        {
            int newPos, n;
            if (horiz)
            {
                Toplevel.GetPointer (out newPos, out n);
                int diff = startPos ? (newPos - resizePos) : (resizePos - newPos);
                int newSize = origSize + diff;
                if (newSize < Child.SizeRequest ().Width)
                    newSize = Child.SizeRequest ().Width;
                if (!startPos)
                {
                    X = origPos - newSize;
                }
                WidthRequest = newSize;
            }
            else
            {
                Toplevel.GetPointer (out n, out newPos);
                int diff = startPos ? (newPos - resizePos) : (resizePos - newPos);
                int newSize = origSize + diff;
                if (newSize < Child.SizeRequest ().Height)
                    newSize = Child.SizeRequest ().Height;
                if (!startPos)
                {
                    Y = origPos - newSize;
                }
                HeightRequest = newSize;
            }
            frame.QueueResize ();
        }
    }

    void OnGripExpose (object sender, Gtk.ExposeEventArgs args)
    {
        var w = (EventBox) sender;
        StateType s = insideGrip ? StateType.Prelight : StateType.Normal;

        using (var ctx = CairoHelper.Create (args.Event.Window))
        {
            ctx.Color = (HslColor) (w.Style.Background (s));
            ctx.Paint ();
        }
    }
}

class ScrollableContainer: EventBox
{
    PositionType expandPos;
    bool scrollMode;
    int targetSize;

    public bool ScrollMode
    {
        get
        {
            return scrollMode;
        }
        set
        {
            scrollMode = value;
            QueueResize ();
        }
    }

    public void SetSize (PositionType expandPosition, int targetSize)
    {
        this.expandPos = expandPosition;
        this.targetSize = targetSize;
        QueueResize ();
    }

    protected override void OnSizeRequested (ref Requisition req)
    {
        base.OnSizeRequested (ref req);
        if (scrollMode || Child == null)
        {
            req.Width = 0;
            req.Height = 0;
        }
        else
            req = Child.SizeRequest ();
    }

    protected override void OnSizeAllocated (Rectangle alloc)
    {
        if (scrollMode && Child != null)
        {
            switch (expandPos)
            {
            case PositionType.Bottom:
                alloc = new Rectangle (alloc.X, alloc.Y, alloc.Width, targetSize);
                break;
            case PositionType.Top:
                alloc = new Rectangle (alloc.X, alloc.Y - targetSize + alloc.Height, alloc.Width, targetSize);
                break;
            case PositionType.Right:
                alloc = new Rectangle (alloc.X, alloc.Y, targetSize, alloc.Height);
                break;
            case PositionType.Left:
                alloc = new Rectangle (alloc.X - targetSize + alloc.Width, alloc.Y, targetSize, alloc.Height);
                break;
            }
        }
        base.OnSizeAllocated (alloc);
    }
}

}
