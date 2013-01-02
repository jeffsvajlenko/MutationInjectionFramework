// OptionsDialog.cs
//
// Author:
//   Lluis Sanchez Gual <lluis@novell.com>
//
// Copyright (c) 2008 Novell, Inc (http://www.novell.com)
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
//
//

using System;
using System.Collections.Generic;
using Gtk;
using Mono.Addins;
using MonoDevelop.Core;
using MonoDevelop.Ide.Extensions;
using MonoDevelop.Ide.Gui.Components;

namespace MonoDevelop.Ide.Gui.Dialogs
{

public partial class OptionsDialog : Gtk.Dialog
{
    protected TreeStore store;
    Dictionary<OptionsDialogSection, SectionPage> pages = new Dictionary<OptionsDialogSection, SectionPage> ();
    Dictionary<OptionsPanelNode, PanelInstance> panels = new Dictionary<OptionsPanelNode,PanelInstance> ();
    object mainDataObject;
    string extensionPath;
    ExtensionContext extensionContext;
    HashSet<object> modifiedObjects = new HashSet<object> ();
    bool removeEmptySections;

    const string emptyCategoryIcon = "md-empty-category";
    const Gtk.IconSize treeIconSize = IconSize.Menu;
    const Gtk.IconSize headerIconSize = IconSize.Button;

    public object DataObject
    {
        get
        {
            return mainDataObject;
        }
    }

    public ExtensionContext ExtensionContext
    {
        get
        {
            return extensionContext;
        }
    }

    public HashSet<object> ModifiedObjects
    {
        get
        {
            return modifiedObjects;
        }
    }

    public OptionsDialog (string extensionPath): this (null, null, extensionPath)
    {
    }

    public OptionsDialog (Gtk.Window parentWindow, object dataObject, string extensionPath) : this (parentWindow, dataObject, extensionPath, true)
    {}

    public OptionsDialog (Gtk.Window parentWindow, object dataObject, string extensionPath, bool removeEmptySections)
    {
        this.Build();
        this.removeEmptySections = removeEmptySections;
        extensionContext = AddinManager.CreateExtensionContext ();

        this.mainDataObject = dataObject;
        this.extensionPath = extensionPath;

        if (parentWindow != null)
            TransientFor = parentWindow;

        ImageService.EnsureStockIconIsLoaded (emptyCategoryIcon, treeIconSize);
        ImageService.EnsureStockIconIsLoaded (emptyCategoryIcon, headerIconSize);

        store = new TreeStore (typeof(OptionsDialogSection));
        tree.Model = store;
        tree.HeadersVisible = false;

        TreeViewColumn col = new TreeViewColumn ();
        var crp = new CellRendererPixbuf ();
        col.PackStart (crp, false);
        col.SetCellDataFunc (crp, PixbufCellDataFunc);
        var crt = new CellRendererText ();
        col.PackStart (crt, true);
        col.SetCellDataFunc (crt, TextCellDataFunc);
        tree.AppendColumn (col);

        tree.Selection.Changed += OnSelectionChanged;

        Child.ShowAll ();

        InitializeContext (extensionContext);

        FillTree ();
        ExpandCategories ();
        this.DefaultResponse = Gtk.ResponseType.Ok;
    }

    void PixbufCellDataFunc (TreeViewColumn col, CellRenderer cell, TreeModel model, TreeIter iter)
    {
        TreeIter parent;
        bool toplevel = !model.IterParent (out parent, iter);

        var crp = (CellRendererPixbuf) cell;
        crp.Visible = !toplevel;

        if (toplevel)
        {
            return;
        }

        var section = (OptionsDialogSection) model.GetValue (iter, 0);

        //HACK: The mimetype panels can't register a single fake stock ID for all the possible image size.
        // Instead, give this some awareness of the mime system.
        var mimeSection = section as MonoDevelop.Ide.Projects.OptionPanels.MimetypeOptionsDialogSection;
        if (mimeSection != null && !string.IsNullOrEmpty (mimeSection.MimeType))
        {
            var pix = DesktopService.GetPixbufForType (mimeSection.MimeType, treeIconSize);
            if (pix != null)
            {
                crp.Pixbuf = pix;
            }
            else
            {
                crp.Pixbuf = ImageService.GetPixbuf (emptyCategoryIcon, treeIconSize);
            }
        }
        else
        {
            string icon = section.Icon.IsNull? emptyCategoryIcon : section.Icon.ToString ();
            crp.Pixbuf = ImageService.GetPixbuf (icon, treeIconSize);
        }
    }

    void TextCellDataFunc (TreeViewColumn col, CellRenderer cell, TreeModel model, TreeIter iter)
    {
        TreeIter parent;
        bool toplevel = !model.IterParent (out parent, iter);

        var crt = (CellRendererText) cell;
        var section = (OptionsDialogSection) model.GetValue (iter, 0);

        if (toplevel)
        {
            crt.Markup = "<b>" + GLib.Markup.EscapeText (section.Label) + "</b>";
        }
        else
        {
            crt.Text = section.Label;
        }
    }

    protected Alignment MainBox
    {
        get
        {
            return alignment;
        }
    }

    protected void ExpandCategories ()
    {
        TreeIter it;
        if (store.GetIterFirst (out it))
        {
            tree.Selection.SelectIter (it);
            do
            {
                tree.ExpandRow (store.GetPath (it), false);
            }
            while (store.IterNext (ref it));
        }
    }

    public void FillTree ()
    {
        DetachWidgets ();
        pages.Clear ();
        store.Clear ();
        foreach (OptionsDialogSection section in extensionContext.GetExtensionNodes (extensionPath))
        {
            AddSection (section, mainDataObject);
        }
    }

    protected virtual void InitializeContext (ExtensionContext extensionContext)
    {
    }

    protected override void OnDestroyed()
    {
        foreach (PanelInstance pi in panels.Values)
        {
            if (pi.Widget != null)
                pi.Widget.Destroy ();
            IDisposable disp = pi.Panel as IDisposable;
            if (disp != null)
                disp.Dispose ();
        }
        base.OnDestroyed ();
    }

    public void SelectPanel (string id)
    {
        foreach (OptionsDialogSection section in pages.Keys)
        {
            if (section.Id == id)
            {
                ShowPage (section);
                return;
            }
        }
    }

    public void AddChildSection (IOptionsPanel parent, OptionsDialogSection section, object dataObject)
    {
        foreach (SectionPage page in pages.Values)
        {
            foreach (PanelInstance pi in page.Panels)
            {
                if (pi.Panel == parent)
                {
                    AddSection (page.Iter, section, dataObject);
                    return;
                }
            }
        }
        throw new InvalidOperationException ("Parent options panel not found in the dialog.");
    }

    public void RemoveSection (OptionsDialogSection section)
    {
        SectionPage page;
        if (pages.TryGetValue (section, out page))
            RemoveSection (page.Iter);
    }

    void RemoveSection (Gtk.TreeIter it)
    {
        Gtk.TreeIter ci;
        if (store.IterChildren (out ci, it))
        {
            do
            {
                RemoveSection (ci);
            }
            while (store.IterNext (ref ci));
        }

        // If the this panel is the selection, select the parent before removing
        Gtk.TreeIter itsel;
        if (tree.Selection.GetSelected (out itsel))
        {
            if (store.GetPath (itsel).Equals (store.GetPath (it)))
            {
                Gtk.TreeIter pi;
                if (store.IterParent (out pi, it))
                    tree.Selection.SelectIter (pi);
            }
        }
        OptionsDialogSection section = (OptionsDialogSection) store.GetValue (it, 0);
        if (section != null)
        {
            SectionPage page;
            if (pages.TryGetValue (section, out page))
            {
                foreach (PanelInstance pi in page.Panels)
                {
                    panels.Remove (pi.Node);
                    IDisposable d = pi.Panel as IDisposable;
                    if (d != null)
                        d.Dispose ();
                }
                pages.Remove (section);
                if (page.Widget != null)
                    page.Widget.Destroy ();
            }
        }
        store.Remove (ref it);
    }

    protected TreeIter AddSection (OptionsDialogSection section, object dataObject)
    {
        return AddSection (TreeIter.Zero, section, dataObject);
    }

    protected TreeIter AddSection (TreeIter parentIter, OptionsDialogSection section, object dataObject)
    {
        TreeIter it;
        if (parentIter.Equals (TreeIter.Zero))
        {
            it = store.AppendValues (section);
        }
        else
        {
            it = store.AppendValues (parentIter,section);
        }

        if (!section.CustomNode)
            AddChildSections (it, section, dataObject);

        // Remove the section if it doesn't have children nor panels
        SectionPage page = CreatePage (it, section, dataObject);
        TreeIter cit;
        if (removeEmptySections && page.Panels.Count == 0 && !store.IterChildren (out cit, it))
        {
            store.Remove (ref it);
            return TreeIter.Zero;
        }
        return it;
    }

    protected virtual void AddChildSections (TreeIter parentIter, OptionsDialogSection section, object dataObject)
    {
        foreach (ExtensionNode nod in section.ChildNodes)
        {
            if (nod is OptionsDialogSection)
                AddSection (parentIter, (OptionsDialogSection) nod, dataObject);
        }
    }

    void DetachWidgets ()
    {
        foreach (Gtk.Widget w in pageFrame.Children)
            pageFrame.Remove (w);

        foreach (SectionPage sp in pages.Values)
        {
            foreach (PanelInstance pi in sp.Panels)
            {
                if (pi.Widget != null && pi.Widget.Parent != null)
                    ((Gtk.Container)pi.Widget.Parent).Remove (pi.Widget);
            }
            if (sp.Widget != null)
            {
                sp.Widget.Destroy ();
                sp.Widget = null;
            }
        }
    }

    protected override void OnResponse (ResponseType resp)
    {
        base.OnResponse (resp);
        DetachWidgets ();
    }

    protected virtual bool ValidateChanges ()
    {
        foreach (SectionPage sp in pages.Values)
        {
            if (sp.Widget == null)
                continue;
            foreach (PanelInstance pi in sp.Panels)
            {
                if (!pi.Panel.ValidateChanges ())
                    return false; // Not valid
            }
        }
        return true;
    }

    protected virtual void ApplyChanges ()
    {
        foreach (SectionPage sp in pages.Values)
        {
            if (sp.Widget == null)
                continue;
            foreach (PanelInstance pi in sp.Panels)
                pi.Panel.ApplyChanges ();
        }
    }


    void OnSelectionChanged (object s, EventArgs a)
    {
        TreeIter it;
        if (tree.Selection.GetSelected (out it))
        {
            OptionsDialogSection section = (OptionsDialogSection) store.GetValue (it, 0);
            ShowPage (section);
        }
    }

    bool HasVisiblePanel (OptionsDialogSection section)
    {
        SectionPage page;
        if (!pages.TryGetValue (section, out page))
            return false;
        if (page.Panels.Count > 0)
            return true;
        foreach (ExtensionNode node in section.ChildNodes)
        {
            if (node is OptionsDialogSection)
            {
                if (HasVisiblePanel ((OptionsDialogSection) node))
                    return true;
            }
        }
        return false;
    }

    public void ShowPage (OptionsDialogSection section)
    {
        SectionPage page;
        if (!pages.TryGetValue (section, out page))
            return;

        if (page.Panels.Count == 0)
        {
            foreach (ExtensionNode node in section.ChildNodes)
            {
                if (node is OptionsDialogSection)
                {
                    if (HasVisiblePanel ((OptionsDialogSection) node))
                    {
                        ShowPage ((OptionsDialogSection) node);
                        return;
                    }
                }
            }
        }

        foreach (Gtk.Widget w in pageFrame.Children)
        {
            Container cc = w as Gtk.Container;
            if (cc != null)
            {
                foreach (Gtk.Widget cw in cc)
                    cw.Hide ();
            }
            pageFrame.Remove (w);
        }

        if (page.Widget == null)
            CreatePageWidget (page);

        labelTitle.Markup = "<span weight=\"bold\" size=\"x-large\">" + GLib.Markup.EscapeText (section.Label) + "</span>";

        //HACK: mimetype panels can't provide stock ID for mimetype images. Give this some awareness of mimetypes.
        var mimeSection = section as MonoDevelop.Ide.Projects.OptionPanels.MimetypeOptionsDialogSection;
        if (mimeSection != null && !string.IsNullOrEmpty (mimeSection.MimeType))
        {
            var pix = DesktopService.GetPixbufForType (mimeSection.MimeType, headerIconSize);
            if (pix != null)
            {
                image.Pixbuf = pix;
            }
            else
            {
                image.Pixbuf = ImageService.GetPixbuf (emptyCategoryIcon, headerIconSize);
            }
        }
        else
        {
            string icon = section.Icon.IsNull? emptyCategoryIcon : section.Icon.ToString ();
            image.Pixbuf = ImageService.GetPixbuf (icon, headerIconSize);
        }

        pageFrame.PackStart (page.Widget, true, true, 0);

        // Ensures that the Shown event is fired for each panel
        Container c = page.Widget as Gtk.Container;
        if (c != null)
        {
            foreach (Gtk.Widget cw in c)
                cw.Show ();
            //HACK: weird bug - switching page away and back selects last tab. should preserve the selection.
            if (c is Notebook)
                ((Notebook)c).Page = 0;
        }

        tree.ExpandToPath (store.GetPath (page.Iter));
        tree.Selection.SelectIter (page.Iter);
    }

    SectionPage CreatePage (TreeIter it, OptionsDialogSection section, object dataObject)
    {
        SectionPage page;
        if (pages.TryGetValue (section, out page))
            return page;

        page = new SectionPage ();
        page.Iter = it;
        page.Panels = new List<PanelInstance> ();
        pages [section] = page;

        List<OptionsPanelNode> nodes = new List<OptionsPanelNode> ();
        if (!string.IsNullOrEmpty (section.TypeName) || section.CustomNode)
            nodes.Add (section);

        if (!section.CustomNode)
        {
            foreach (ExtensionNode nod in section.ChildNodes)
            {
                OptionsPanelNode node = nod as OptionsPanelNode;
                if (node != null && !(node is OptionsDialogSection))
                    nodes.Add (node);
            }
        }

        foreach (OptionsPanelNode node in nodes)
        {
            PanelInstance pi = null;
            if (panels.TryGetValue (node, out pi))
            {
                if (pi.DataObject == dataObject)
                {
                    // The panel can be reused
                    if (!pi.Panel.IsVisible ())
                        continue;
                }
                else
                {
                    // If the data object has changed, this panel instance can't be
                    // reused anymore. Destroy it.
                    panels.Remove (node);
                    if (pi.Widget != null)
                        pi.Widget.Destroy ();
                    IDisposable d = pi.Panel as IDisposable;
                    if (d != null)
                        d.Dispose ();
                    pi = null;
                }
            }
            if (pi == null)
            {
                IOptionsPanel panel = node.CreatePanel ();
                pi = new PanelInstance ();
                pi.Panel = panel;
                pi.Node = node;
                pi.DataObject = dataObject;
                page.Panels.Add (pi);
                panel.Initialize (this, dataObject);
                if (!panel.IsVisible ())
                {
                    page.Panels.Remove (pi);
                    continue;
                }
                panels [node] = pi;
            }
            else
                page.Panels.Add (pi);
        }
        return page;
    }

    void CreatePageWidget (SectionPage page)
    {
        List<PanelInstance> boxPanels = new List<PanelInstance> ();
        List<PanelInstance> tabPanels = new List<PanelInstance> ();

        foreach (PanelInstance pi in page.Panels)
        {
            if (pi.Widget == null)
            {
                pi.Widget = pi.Panel.CreatePanelWidget ();
                //HACK: work around bug 469427 - broken themes match on widget names
                if (pi.Widget.Name.IndexOf ("Panel") > 0)
                    pi.Widget.Name = pi.Widget.Name.Replace ("Panel", "_");
            }
            else if (pi.Widget.Parent != null)
                ((Gtk.Container) pi.Widget.Parent).Remove (pi.Widget);

            if (pi.Node.Grouping == PanelGrouping.Tab)
                tabPanels.Add (pi);
            else
                boxPanels.Add (pi);
        }

        // Try to fit panels with grouping=box or auto in the main page.
        // If they don't fit. Move auto panels to its own tab page.

        int mainPageSize;
        bool fits;
        do
        {
            PanelInstance lastAuto = null;
            mainPageSize = 0;
            foreach (PanelInstance pi in boxPanels)
            {
                if (pi.Node.Grouping == PanelGrouping.Auto)
                    lastAuto = pi;
                mainPageSize += pi.Widget.SizeRequest ().Height + 6;
            }
            fits = mainPageSize <= pageFrame.Allocation.Height;
            if (!fits)
            {
                if (lastAuto != null && boxPanels.Count > 1)
                {
                    boxPanels.Remove (lastAuto);
                    tabPanels.Insert (0, lastAuto);
                }
                else
                {
                    fits = true;
                }
            }
        }
        while (!fits);

        Gtk.VBox box = new VBox (false, 12);
        box.Show ();
        for (int n=0; n<boxPanels.Count; n++)
        {
            if (n != 0)
            {
                HSeparator sep = new HSeparator ();
                sep.Show ();
                box.PackStart (sep, false, false, 0);
            }
            PanelInstance pi = boxPanels [n];
            box.PackStart (pi.Widget, pi.Node.Fill, pi.Node.Fill, 0);
            pi.Widget.Show ();
        }

        if (tabPanels.Count > 0)
        {
            Gtk.Notebook nb = new Notebook ();
            nb.Show ();
            Gtk.Label blab = new Gtk.Label (GettextCatalog.GetString ("General"));
            blab.Show ();
            box.BorderWidth = 9;
            nb.InsertPage (box, blab, -1);
            foreach (PanelInstance pi in tabPanels)
            {
                Gtk.Label lab = new Gtk.Label (GettextCatalog.GetString (pi.Node.Label));
                lab.Show ();
                Gtk.Alignment a = new Alignment (0, 0, 1, 1);
                a.BorderWidth = 9;
                a.Show ();
                a.Add (pi.Widget);
                nb.InsertPage (a, lab, -1);
                pi.Widget.Show ();
            }
            page.Widget = nb;
        }
        else
        {
            page.Widget = box;
        }
    }

    protected virtual void OnButtonOkClicked (object sender, System.EventArgs e)
    {
        // Validate changes before saving
        if (!ValidateChanges ())
            return;

        // Now save
        ApplyChanges ();

        if (DataObject != null)
            modifiedObjects.Add (DataObject);

        this.Respond (ResponseType.Ok);
    }

    class PanelInstance
    {
        public IOptionsPanel Panel;
        public Gtk.Widget Widget;
        public OptionsPanelNode Node;
        public object DataObject;
    }

    class SectionPage
    {
        public List<PanelInstance> Panels;
        public Gtk.Widget Widget;
        public Gtk.TreeIter Iter;
    }
}
}
