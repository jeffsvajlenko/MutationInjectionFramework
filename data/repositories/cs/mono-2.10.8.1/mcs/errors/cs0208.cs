// cs0208.cs: Cannot take the address of, get the size of, or declare a pointer to a managed type `cs208.Foo'
// Line: 20
// Compiler options: -unsafe

namespace cs208
{
public class Foo
{
    public int Add (int a, int b)
    {
        return a + b;
    }
}

public class Bar
{
    unsafe static void Main ()
    {
        Foo f = new Foo ();
        void *s = &f;
    }
}
}
