using System;

class Foo
{
    public virtual void Dyn (out dynamic o)
    {
        o = null;
    }
}

class Bar : Foo
{
    public override void Dyn (out dynamic o)
    {
        base.Dyn (out o);
    }
}

public class C
{
    public void Method_A (ref int i)
    {
    }

    public void Method_B (ref dynamic i)
    {
    }

    public static int M (string a, string b)
    {
        return 5;
    }

    public static int M (ref object o, out dynamic d)
    {
        d = null;
        return 1;
    }
}

class D
{
    public static int Foo (dynamic d)
    {
        return 1;
    }

    public static int Foo (params object[] o)
    {
        return 2;
    }
}

class E
{
    public static int Foo (int i, dynamic d)
    {
        return 1;
    }

    public static int Foo (double d, object i)
    {
        return 2;
    }
}

class Program
{
    static void DynOut (out dynamic d)
    {
        d = null;
    }

    static void DynRef (ref object d)
    {
        d = null;
    }

    static int DynParams (int a, int b, params int[] arr)
    {
        return arr [1] + b;
    }

    void TestErrorVersions ()
    {
        var c = new C ();
        dynamic d = null;
        c.Method_A (d);
        c.Method_B (d);
    }

    static int Main ()
    {
        object o;
        DynOut (out o);

        dynamic d = null;
        DynRef (ref d);

        dynamic d1 = 1, d2;

        // This should not involve runtime binder
        if (C.M (ref d1, out d2) != 1)
            return 1;

        dynamic d3 = 5;
        dynamic d4 = -9;
        if (DynParams (1, 2, d3, d4) != -7)
            return 2;

        if (DynParams (1, 2, 3, d4) != -7)
            return 3;

        d = 44;
        if (D.Foo (d) != 1)
            return 4;

        if (E.Foo (0, 0) != 1)
            return 5;

        return 0;
    }
}
