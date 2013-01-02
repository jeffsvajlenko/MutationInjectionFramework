
using System;
using MonoDevelop.Ide.Templates;
using MonoDevelop.Projects;
using MonoDevelop.Core;
using Gtk;

namespace MonoDevelop.Autotools
{
class MakefileIntegrationFeature: ISolutionItemFeature
{
    public string Title
    {
        get
        {
            return GettextCatalog.GetString ("Makefile Integration");
        }
    }

    public string Description
    {
        get
        {
            return string.Empty;
        }
    }

    public FeatureSupportLevel GetSupportLevel (SolutionFolder parentCombine, SolutionItem entry)
    {
        if (entry is Project)
            return FeatureSupportLevel.SupportedByDefault;
        else
            return FeatureSupportLevel.NotSupported;
    }

    public Widget CreateFeatureEditor (SolutionFolder parentCombine, SolutionItem entry)
    {
        return new MakefileIntegrationFeatureWidget ((Project)entry);
    }

    public void ApplyFeature (SolutionFolder parentCombine, SolutionItem entry, Widget editor)
    {
        ((MakefileIntegrationFeatureWidget)editor).Store ();
    }

    public string Validate (SolutionFolder parentCombine, SolutionItem entry, Gtk.Widget editor)
    {
        return null;
    }
}
}
