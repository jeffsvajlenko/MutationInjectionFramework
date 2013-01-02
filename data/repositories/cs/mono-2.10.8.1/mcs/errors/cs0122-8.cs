// cs0122-8.cs: `A.member' is inaccessible due to its protection level
// Line: 17
// NOTE: if `member' were a field or a property, this'd be CS1540

using System;

class A
{
    protected event EventHandler member;
}

class B : A
{
    static void Main ()
    {
        A a = new A ();
        a.member += Handler;
    }

    static void Handler (object sender, EventArgs args) {}
}


