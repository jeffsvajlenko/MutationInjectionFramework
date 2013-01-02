// <file>
//     <copyright see="prj:///doc/copyright.txt"/>
//     <license see="prj:///doc/license.txt"/>
//     <owner name="none" email=""/>
//     <version>$Revision: 4482 $</version>
// </file>

using System;

namespace ICSharpCode.OldNRefactory
{
public class BlankLine : AbstractSpecial
{
    public BlankLine(Location point) : base(point)
    {
    }

    public override object AcceptVisitor(ISpecialVisitor visitor, object data)
    {
        return visitor.Visit(this, data);
    }
}
}
