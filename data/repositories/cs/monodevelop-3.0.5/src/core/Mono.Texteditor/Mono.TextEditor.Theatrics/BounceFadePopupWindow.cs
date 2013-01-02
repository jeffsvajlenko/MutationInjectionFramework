//
// BounceFadePopupWindow.cs
//
// Author:
//       Mike Krüger <mkrueger@novell.com>
//       Michael Hutchinson <mhutchinson@novell.com>
//
// Copyright (c) 2010 Novell, Inc. (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

using System;
using Gdk;

namespace Mono.TextEditor.Theatrics
{
/// <summary>
/// Tooltip that "bounces", then fades away.
/// </summary>
public abstract class BounceFadePopupWindow : Gtk.Window
{
    Stage<BounceFadePopupWindow> stage = new Stage<BounceFadePopupWindow> ();
    Gdk.Pixbuf textImage = null;
    TextEditor editor;

    protected double scale = 0.0;
    protected double opacity = 1.0;

    public BounceFadePopupWindow (TextEditor editor) : base (Gtk.WindowType.Popup)
    {
        if (!IsComposited)
            throw new InvalidOperationException ("Only works with composited screen. Check Widget.IsComposited.");
        if (editor == null)
            throw new ArgumentNullException ("Editor");
        DoubleBuffered = true;
        Decorated = false;
        BorderWidth = 0;
        HasFrame = true;
        this.editor = editor;
        Events = Gdk.EventMask.ExposureMask;
        Duration = 500;
        ExpandWidth = 12;
        ExpandHeight = 2;
        BounceEasing = Easing.Sine;

        var rgbaColormap = Screen.RgbaColormap;
        if (rgbaColormap == null)
            return;
        Colormap = rgbaColormap;

        stage.ActorStep += OnAnimationActorStep;
        stage.Iteration += OnAnimationIteration;
        stage.UpdateFrequency = 10;
    }

    protected TextEditor Editor
    {
        get
        {
            return editor;
        }
    }

    /// <summary>Duration of the animation, in milliseconds.</summary>
    public uint Duration
    {
        get;
        set;
    }

    /// <summary>The number of pixels by which the window's width will expand</summary>
    public uint ExpandWidth
    {
        get;
        set;
    }

    /// <summary>The number of pixels by which the window's height will expand</summary>
    public uint ExpandHeight
    {
        get;
        set;
    }

    /// <summary>The easing used for the bounce part of the animation.</summary>
    public Easing BounceEasing
    {
        get;
        set;
    }

    int x, y;
    protected int width, height;
    double vValue, hValue;
    protected Rectangle bounds;

    public virtual void Popup ()
    {
        editor.GdkWindow.GetOrigin (out x, out y);
        bounds = CalculateInitialBounds ();
        x = x + bounds.X - (int)(ExpandWidth / 2);
        y = y + bounds.Y - (int)(ExpandHeight / 2);
        Move (x, y);

        width = System.Math.Max (1, bounds.Width + (int)ExpandWidth);
        height = System.Math.Max (1, bounds.Height + (int)ExpandHeight);
        Resize (width, height);


        stage.AddOrReset (this, Duration);
        stage.Play ();
        ListenToEvents ();
        Show ();
    }

    protected void ListenToEvents ()
    {
        editor.VAdjustment.ValueChanged += HandleEditorVAdjustmentValueChanged;
        editor.HAdjustment.ValueChanged += HandleEditorHAdjustmentValueChanged;
        vValue = editor.VAdjustment.Value;
        hValue = editor.HAdjustment.Value;
    }

    protected override void OnShown ()
    {
        base.OnShown ();
    }

    protected void DetachEvents ()
    {
        editor.VAdjustment.ValueChanged -= HandleEditorVAdjustmentValueChanged;
        editor.HAdjustment.ValueChanged -= HandleEditorHAdjustmentValueChanged;
    }

    protected override void OnHidden ()
    {
        base.OnHidden ();
        DetachEvents ();
    }

    void HandleEditorVAdjustmentValueChanged (object sender, EventArgs e)
    {
        y += (int)(vValue - editor.VAdjustment.Value);
        Move (x, y);
        vValue = editor.VAdjustment.Value;
    }

    void HandleEditorHAdjustmentValueChanged (object sender, EventArgs e)
    {
        x += (int)(hValue - editor.HAdjustment.Value);
        Move (x, y);
        hValue = editor.HAdjustment.Value;
    }

    void OnAnimationIteration (object sender, EventArgs args)
    {
        QueueDraw ();
    }

    protected virtual bool OnAnimationActorStep (Actor<BounceFadePopupWindow> actor)
    {
        if (actor.Expired)
        {
            OnAnimationCompleted ();
            return false;
        }

        // for the first half, use an easing
        if (actor.Percent < 0.5)
        {
            scale = Choreographer.Compose (actor.Percent * 2, BounceEasing);
            opacity = 1.0;
        }
        //for the second half, vary opacity linearly from 1 to 0.
        else
        {
            scale = Choreographer.Compose (1.0, BounceEasing);
            opacity = 2.0 - actor.Percent * 2;
        }
        return true;
    }

    protected virtual void OnAnimationCompleted ()
    {
        StopPlaying ();
    }

    protected override void OnDestroyed ()
    {
        editor.VAdjustment.ValueChanged -= HandleEditorVAdjustmentValueChanged;
        base.OnDestroyed ();
        StopPlaying ();
    }

    internal virtual void StopPlaying ()
    {
        stage.Playing = false;

        if (textImage != null)
        {
            textImage.Dispose ();
            textImage = null;
        }
    }

    protected abstract Rectangle CalculateInitialBounds ();



}
}
