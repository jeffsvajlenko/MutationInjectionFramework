//
// ImageChooser.cs
//
// Author:
//       Mike Krüger <mkrueger@xamarin.com>
//
// Copyright (c) 2011 Xamarin <http://xamarin.com>
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
using System.ComponentModel;

using Gdk;

using Mono.TextEditor;
using MonoDevelop.Components;
using MonoDevelop.Core;
using Gtk;
using MonoDevelop.Projects;
using MonoDevelop.Ide.Projects;
using MonoDevelop.Ide;
using System.Collections.Generic;
using System.Linq;

namespace MonoDevelop.MacDev.PlistEditor
{
[ToolboxItem(true)]
public class ImageChooser : Button
{
    static Pixbuf WarningIcon = Pixbuf.LoadFromResource ("error.png");

    Size imageSize, displaySize, acceptedSize, recommendedSize;
    Pixbuf scaledPixbuf;
    string description;
    Project project;
    Gtk.Image image;

    Gtk.TargetEntry[] targetEntryTypes = new Gtk.TargetEntry[]
    {
        new Gtk.TargetEntry ("text/uri-list", 0, 100u)
    };

    [Localizable (true)]
    public string Description
    {
        get
        {
            return description;
        }
        set
        {
            if (value == description)
                return;

            description = value;

            UpdateTooltip ();
        }
    }

    public Size DisplaySize
    {
        get
        {
            return displaySize;
        }
        set
        {
            this.displaySize = value;
            image.WidthRequest = value.Width;
            image.HeightRequest = value.Height;
        }
    }

    public Size AcceptedSize
    {
        get
        {
            return acceptedSize;
        }
        set
        {
            if (value == acceptedSize)
                return;

            recommendedSize = value;
            acceptedSize = value;

            UpdateTooltip ();
            QueueDraw ();
        }
    }

    public Size RecommendedSize
    {
        get
        {
            return recommendedSize;
        }
        set
        {
            if (value == recommendedSize)
                return;

            recommendedSize = value;

            UpdateTooltip ();
            QueueDraw ();
        }
    }

    public List<string> SupportedFormats
    {
        get;
        private set;
    }

    /// <summary>
    /// Project virtual path of the selected file.
    /// </summary>
    public FilePath SelectedProjectFile
    {
        get;
        set;
    }

    public void SetDisplayPixbuf (Pixbuf pixbuf)
    {
        DestroyScaledPixbuf ();

        imageSize = pixbuf != null ? new Size (pixbuf.Width, pixbuf.Height) : Size.Empty;

        //scale image down to fit
        if (pixbuf != null && (pixbuf.Width > DisplaySize.Width || pixbuf.Height > DisplaySize.Height))
        {
            if (DisplaySize.Width == 0 || displaySize.Height == 0)
                throw new NotSupportedException ("Display size not set.");
            double aspect = pixbuf.Height / (double)pixbuf.Width;
            int destWidth = Math.Min ((int) (DisplaySize.Height / aspect), pixbuf.Width);
            destWidth = Math.Max (1, Math.Min (DisplaySize.Width, destWidth));
            int destHeight = Math.Min ((int) (DisplaySize.Width * aspect), pixbuf.Height);
            destHeight =  Math.Max (1, Math.Min (DisplaySize.Height, destHeight));
            scaledPixbuf = pixbuf = pixbuf.ScaleSimple (destWidth, destHeight, InterpType.Bilinear);
        }

        image.Pixbuf = pixbuf;

        UpdateTooltip ();
    }

    public ImageChooser ()
    {
        this.image = new Gtk.Image ();
        this.Add (this.image);
        ShowAll ();

        Gtk.Drag.DestSet (this, DestDefaults.Drop, targetEntryTypes, DragAction.Link);
        SupportedFormats = new List<string> { "png" };
    }

    void UpdateTooltip ()
    {
        if (imageSize != Size.Empty && RecommendedSize != Size.Empty && imageSize != RecommendedSize)
        {
            string warning = GettextCatalog.GetString ("<b>WARNING:</b> The size of the current image does not match the recommended size of {0}x{1} pixels.",
                             RecommendedSize.Width, RecommendedSize.Height);

            if (!string.IsNullOrEmpty (description))
                TooltipMarkup = GLib.Markup.EscapeText (description) + "\n\n" + warning;
            else
                TooltipMarkup = warning;
        }
        else if (RecommendedSize != Size.Empty)
        {
            string suggestion = GettextCatalog.GetString ("The recommended size of this image is {0}x{1} pixels.", RecommendedSize.Width, RecommendedSize.Height);

            if (!string.IsNullOrEmpty (description))
                TooltipText = description + "\n\n" + suggestion;
            else
                TooltipText = suggestion;
        }
        else if (string.IsNullOrEmpty (description))
        {
            TooltipText = GettextCatalog.GetString ("Choose an image.");
        }
        else
        {
            TooltipText = description;
        }
    }

    public bool CheckImage (FilePath path)
    {
        int width, height;
        string errorTitle = null;
        string errorMessage = null;

        using (var type = Pixbuf.GetFileInfo (path, out width, out height))
        {
            if (type == null)
            {
                errorTitle = GettextCatalog.GetString ("Invalid file selected");
                errorMessage = GettextCatalog.GetString ("Selected file was not a valid image.");
            }
            else if (type.IsDisabled)
            {
                errorTitle = GettextCatalog.GetString ("Unsupported image selected");
                errorMessage = GettextCatalog.GetString ("Support for loading images of type '{0}' has not been enabled.", type.Name);
            }
            else if (AcceptedSize != Size.Empty && AcceptedSize != new Size (width, height))
            {
                errorTitle = GettextCatalog.GetString ("Incorrect image dimensions");
                errorMessage = GettextCatalog.GetString (
                                   "Only images with size {0}x{1} are allowed. Picture was {2}x{3}.",
                                   AcceptedSize.Width, AcceptedSize.Height, width, height);
            }
            else if (!SupportedFormats.Contains (type.Name))
            {
                var formats = string.Join (", ", SupportedFormats.Select (f => "'" + f + "'"));
                errorTitle = GettextCatalog.GetString ("Invalid image selected");
                errorMessage = GettextCatalog.GetString ("An image of type '{0}' has been selected but you must select an image of type '{1}'.", type.Name, formats);
            }
            else
            {
                return true;
            }

            LoggingService.LogError ("{0}: {1}", errorTitle, errorMessage);
            MessageService.ShowError (errorTitle, errorMessage);
            return false;
        }
    }

    protected override void OnClicked ()
    {
        base.OnClicked ();
        if (project == null)
            return;

        var formats = string.Join ("|", SupportedFormats.Select (f => "*." + f));
        var dialog = new ProjectFileSelectorDialog (project, "All Images", formats);

        try
        {
            if (AcceptedSize.IsEmpty)
                dialog.Title = GettextCatalog.GetString ("Select image...");
            else
                dialog.Title = GettextCatalog.GetString ("Select image ({0}x{1})...", RecommendedSize.Width, RecommendedSize.Height);
            while (MessageService.RunCustomDialog (dialog) == (int)Gtk.ResponseType.Ok && dialog.SelectedFile != null)
            {
                var path = dialog.SelectedFile.FilePath;
                if (!CheckImage (path))
                    continue;

                SelectedProjectFile = dialog.SelectedFile.ProjectVirtualPath;
                OnChanged (EventArgs.Empty);
                break;
            }
        }
        finally
        {
            dialog.Destroy ();
        }
    }

    public void SetProject (Project proj)
    {
        this.project = proj;
    }

    protected override void OnDestroyed ()
    {
        base.OnDestroyed ();
        DestroyScaledPixbuf ();
    }

    protected override void OnRealized ()
    {
        base.OnRealized ();
        if (displaySize.Width <= 0 || displaySize.Height <= 0)
            throw new InvalidOperationException ("Display size not set.");
    }

    protected override bool OnExposeEvent (EventExpose evnt)
    {
        var ret = base.OnExposeEvent (evnt);

        if (image.Pixbuf == null)
        {
            using (var cr = CairoHelper.Create (evnt.Window))
            {
                cr.Rectangle (evnt.Region.Clipbox.X, evnt.Region.Clipbox.Y, evnt.Region.Clipbox.Width, evnt.Region.Clipbox.Height);
                cr.Clip ();

                var imgAlloc = image.Allocation;
                cr.Translate (imgAlloc.X, imgAlloc.Y);

                using (var layout = new Pango.Layout (PangoContext))
                {
                    layout.SetText (string.Format ("({0}x{1})", RecommendedSize.Width, RecommendedSize.Height));
                    layout.Width = (int) (imgAlloc.Width * Pango.Scale.PangoScale);
                    layout.Wrap = Pango.WrapMode.WordChar;
                    layout.Alignment = Pango.Alignment.Center;

                    int pw, ph;
                    layout.GetPixelSize (out pw, out ph);
                    cr.MoveTo (0, (imgAlloc.Height - ph) / 2);
                    cr.Color = new Cairo.Color (0.5, 0.5, 0.5);
                    cr.ShowLayout (layout);
                }

                CairoExtensions.RoundedRectangle (cr, 5, 5, imgAlloc.Width - 10, imgAlloc.Height - 10, 5);
                cr.LineWidth = 3;
                cr.Color = new Cairo.Color (0.8, 0.8, 0.8);
                cr.SetDash (new double[] { 12, 2 }, 0);
                cr.Stroke ();
            }
        }
        else if (RecommendedSize != Size.Empty && imageSize != RecommendedSize)
        {
            using (var cr = CairoHelper.Create (evnt.Window))
            {
                cr.Rectangle (evnt.Region.Clipbox.X, evnt.Region.Clipbox.Y, evnt.Region.Clipbox.Width, evnt.Region.Clipbox.Height);
                cr.Clip ();

                var imgAlloc = image.Allocation;
                cr.Translate (imgAlloc.X + displaySize.Width - WarningIcon.Width - 3, imgAlloc.Y + displaySize.Height - WarningIcon.Height);

                CairoHelper.SetSourcePixbuf (cr, WarningIcon, 0, 0);
                cr.Rectangle (0, 0, WarningIcon.Width, WarningIcon.Height);
                cr.Fill ();
            }
        }

        return ret;
    }

    protected virtual void OnChanged (System.EventArgs e)
    {
        EventHandler handler = this.Changed;
        if (handler != null)
            handler (this, e);
    }

    public event EventHandler Changed;

    void DestroyScaledPixbuf ()
    {
        if (scaledPixbuf != null)
        {
            scaledPixbuf.Dispose ();
            scaledPixbuf = null;
        }
    }

    protected override void OnDragDataReceived (DragContext context, int x, int y, SelectionData selection_data, uint info, uint time_)
    {
        base.OnDragDataReceived (context, x, y, selection_data, info, time_);
        if (info == 100u)
        {
            string fullData = System.Text.Encoding.UTF8.GetString (selection_data.Data);

            foreach (string individualFile in fullData.Split ('\n'))
            {
                string file = individualFile.Trim ();
                if (file.StartsWith ("file://"))
                {
                    file = new Uri(file).LocalPath;
                    if (!CheckImage (file))
                        return;
                    if (project != null)
                        file = project.GetRelativeChildPath (file);
                    SelectedProjectFile = file;
                    OnChanged (EventArgs.Empty);
                }
            }
        }
    }
}
}
