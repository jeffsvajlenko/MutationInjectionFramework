// CustomCommandPanel.cs
//
// Author:
//   Lluis Sanchez Gual <lluis@novell.com>
//
// Copyright (c) 2007 Novell, Inc (http://www.novell.com)
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
using MonoDevelop.Ide.Gui.Dialogs;
using MonoDevelop.Projects;
using MonoDevelop.Core;

namespace MonoDevelop.Ide.Projects.OptionPanels
{
internal class CustomCommandPanel: MultiConfigItemOptionsPanel
{
    CustomCommandPanelWidget widget;
    CustomCommandType[] supportedTypes;

    public CustomCommandPanel (CustomCommandType[] supportedTypes)
    {
        this.supportedTypes = supportedTypes;
    }

    public override Gtk.Widget CreatePanelWidget ()
    {
        return (widget = new CustomCommandPanelWidget ());
    }

    public override void LoadConfigData ()
    {
        widget.Load (ConfiguredSolutionItem, CurrentConfiguration.CustomCommands, CurrentConfiguration.Selector, supportedTypes);
    }

    public override void ApplyChanges ()
    {
        // Do nothing. Changes to cloned configurations are automatically applied.
    }
}

internal class BuildCustomCommandPanel: CustomCommandPanel
{
    public BuildCustomCommandPanel (): base (new CustomCommandType[]
    {
        CustomCommandType.BeforeBuild,
        CustomCommandType.Build,
        CustomCommandType.AfterBuild,
        CustomCommandType.BeforeClean,
        CustomCommandType.Clean,
        CustomCommandType.AfterClean,
        CustomCommandType.Custom
    })
    {
    }
}

internal class ExecutionCustomCommandPanel: CustomCommandPanel
{
    public ExecutionCustomCommandPanel (): base (new CustomCommandType[]
    {
        CustomCommandType.BeforeExecute,
        CustomCommandType.Execute,
        CustomCommandType.AfterExecute
    })
    {
    }
}
}
