// cs0128.cs: A local variable named `x' is already defined in this scope
// Line:

class x
{
    static int y ()
    {
        int x = 1;
        int x = 2;

        return x + x;
    }
}
