
using System;
using Gtk;
using Gdk;
using System.Text;

namespace Stetic.Editor
{
public class StringArray: PropertyEditorCell
{
    protected override string GetValueText ()
    {
        string[] val = (string[]) Value;
        return  val == null ? string.Empty : "(Collection)";
    }

    protected override IPropertyEditor CreateEditor (Gdk.Rectangle cell_area, Gtk.StateType state)
    {
        return new StringArrayEditor ();
    }
}

public class StringArrayEditor: Gtk.HBox, IPropertyEditor
{
    Gtk.Entry label;
    Gtk.Button button;
    PropertyDescriptor prop;
    object obj;
    string[] strings;

    public StringArrayEditor()
    {
        label = new Gtk.Entry ();
        label.IsEditable = false;
        PackStart (label, true, true, 0);
        button = new Button ("...");
        PackStart (button, false, false, 3);
        button.Clicked += ButtonClicked;
        ShowAll ();
    }

    void ButtonClicked (object s, EventArgs a)
    {
        using (TextEditorDialog dlg = new TextEditorDialog ())
        {
            dlg.Text = strings != null ? string.Join ("\n", strings) : "";
            dlg.SetTranslatable (prop.Translatable);
            dlg.TransientFor = this.Toplevel as Gtk.Window;
            if (prop.Translatable)
            {
                dlg.Translated = prop.IsTranslated (obj);
                dlg.ContextHint = prop.TranslationContext (obj);
                dlg.Comment = prop.TranslationComment (obj);
            }
            if (dlg.Run () == (int) ResponseType.Ok)
            {
                if (prop.Translatable)
                {
                    prop.SetTranslated (obj, dlg.Translated);
                    if (dlg.Translated)
                    {
                        prop.SetTranslationComment (obj, dlg.Comment);
                        prop.SetTranslationContext (obj, dlg.ContextHint);
                    }
                }
                if (dlg.Text.Length == 0)
                    strings = null;
                else
                    strings = dlg.Text.Split ('\n');
                UpdateLabel ();
                if (ValueChanged != null)
                    ValueChanged (this, EventArgs.Empty);
            }
        }
    }

    public void Initialize (PropertyDescriptor descriptor)
    {
        if (descriptor.PropertyType != typeof(string[]))
            throw new InvalidOperationException ("StringArrayEditor can only edit string[] properties");
        prop = descriptor;
    }

    public void AttachObject (object obj)
    {
        this.obj = obj;
    }

    public object Value
    {
        get
        {
            return strings;
        }
        set
        {
            strings = (string[]) value;
            UpdateLabel ();
        }
    }

    void UpdateLabel ()
    {
        label.Text = strings != null ? "(Collection)" : System.String.Empty;
    }

    public event EventHandler ValueChanged;
}
}
