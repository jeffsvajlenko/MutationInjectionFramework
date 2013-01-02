// cs0672-3.cs: Member `B.Warning' overrides obsolete member `A.Warning'. Add the Obsolete attribute to `B.Warning'
// Line: 15
// Compiler options: -warnaserror

using System;

public class A
{
    [Obsolete()]
    public virtual string Warning
    {
        get
        {
            return "";
        }
    }
}

public class B : A
{
    public override string Warning
    {
        get
        {
            return "";
        }
    }
}
