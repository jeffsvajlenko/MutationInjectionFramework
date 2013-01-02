//
// XibReference.cs
//
// Author:
//       Michael Hutchinson <mhutchinson@novell.com>
//
// Copyright (c) 2009 Novell, Inc. (http://www.novell.com)
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

using System;

namespace MonoDevelop.MacDev.InterfaceBuilder
{
public class IBReference
{
    public int Id
    {
        get;
        private set;
    }

    public IBReference (int refId)
    {
        this.Id = refId;
    }

    public object Reference
    {
        get;
        internal set;
    }
}

public interface IReferenceResolver
{
    void Add (IBObject resolveable);
    void Add (IBReference reference);
    void Add (int id, object primitive);
    object Resolve (int id);
}

public sealed class Unref<T> where T : class
{
    object unresolved;
    T resolved;

    public T Value
    {
        get
        {
            return resolved ?? (resolved = (T) ResolveIfReference (unresolved));
        }
    }

    public Unref (object unresolved)
    {
        this.unresolved = unresolved;
    }

    static object ResolveIfReference (object o)
    {
        var r = o as IBReference;
        if (r != null)
            return ResolveIfReference (r.Reference);
        else
            return o;
    }
}
}
