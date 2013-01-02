// <file>
//     <copyright see="prj:///doc/copyright.txt"/>
//     <license see="prj:///doc/license.txt"/>
//     <owner name="Mike Krüger" email="mike@icsharpcode.net"/>
//     <version>$Revision: 4482 $</version>
// </file>

using System;

namespace ICSharpCode.OldNRefactory.PrettyPrinter
{
/// <summary>
/// Description of VBNetPrettyPrintOptions.
/// </summary>
public class VBNetPrettyPrintOptions : AbstractPrettyPrintOptions
{
    /// <summary>
    /// Gets/Sets if the optional "ByVal" modifier should be written.
    /// </summary>
    public bool OutputByValModifier
    {
        get;
        set;
    }
}
}
