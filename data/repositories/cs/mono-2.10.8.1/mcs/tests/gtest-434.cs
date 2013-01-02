// Compiler options: -r:gtest-434-lib.dll

using System;
using System.Reflection;

namespace testcase
{
public class Init : ConfigurationExpression, IInitializationExpression
{
}

public class Program
{
    private static int Main ()
    {
        var t = typeof (Init);
        var m = t.GetMethod ("testcase.IInitializationExpression.AddRegistry", BindingFlags.NonPublic | BindingFlags.Instance);
        Console.WriteLine (m.Attributes);

        if (m.Attributes != (MethodAttributes.PrivateScope | MethodAttributes.Virtual | MethodAttributes.HideBySig | MethodAttributes.VtableLayoutMask | MethodAttributes.CheckAccessOnOverride))
            return 1;

        IInitializationExpression expression = new Init ();
        expression.AddRegistry<string> (11);
        return 0;
    }
}
}
