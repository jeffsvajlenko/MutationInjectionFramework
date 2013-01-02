//
// NRefactoryResolverVisitor.cs
//
// Author:
//       Lluis Sanchez Gual <lluis@novell.com>
//
// Copyright (c) 2010 Novell, Inc (http://www.novell.com)
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
using ICSharpCode.OldNRefactory.Visitors;
using Mono.Debugging.Client;
using System.Collections.Generic;
using System.Text;

namespace Mono.Debugging.Evaluation
{
public class NRefactoryResolverVisitor: AbstractAstVisitor
{
    SourceLocation location;
    DebuggerSession session;
    string expression;
    List<Replacement> replacements = new List<Replacement> ();

    class Replacement
    {
        public int Offset;
        public int Length;
        public string NewText;
    }

    public NRefactoryResolverVisitor (DebuggerSession session, SourceLocation location, string expression)
    {
        this.expression = expression.Replace ("\n","").Replace ("\r","");
        this.session = session;
        this.location = location;
    }

    internal string GetResolvedExpression ()
    {
        if (replacements.Count == 0)
            return expression;

        replacements.Sort (delegate (Replacement r1, Replacement r2)
        {
            return r1.Offset.CompareTo (r2.Offset);
        });
        StringBuilder sb = new StringBuilder ();
        int i = 0;
        foreach (Replacement r in replacements)
        {
            sb.Append (expression, i, r.Offset - i);
            sb.Append (r.NewText);
            i = r.Offset + r.Length;
        }
        Replacement last = replacements [replacements.Count - 1];
        sb.Append (expression, last.Offset + last.Length, expression.Length - (last.Offset + last.Length));

        return sb.ToString ();
    }

    void ResolveType (string typeName, int genericArgCout, int offset, int length)
    {
        if (genericArgCout > 0)
            typeName += "<" + new string (',', genericArgCout - 1) + ">";
        string type = session.ResolveIdentifierAsType (typeName, location);
        if (!string.IsNullOrEmpty (type))
        {
            type = "global::" + type;
            Replacement r = new Replacement ()
            {
                Offset = offset, Length=length, NewText = type
            };
            replacements.Add (r);
        }
    }

    public override object VisitIdentifierExpression (ICSharpCode.OldNRefactory.Ast.IdentifierExpression identifierExpression, object data)
    {
        if (!identifierExpression.StartLocation.IsEmpty)
            ResolveType (identifierExpression.Identifier, 0, identifierExpression.StartLocation.Column - 1, identifierExpression.EndLocation.Column - identifierExpression.StartLocation.Column);
        return null;
    }

    public override object VisitTypeReference (ICSharpCode.OldNRefactory.Ast.TypeReference typeReference, object data)
    {
        if (!typeReference.StartLocation.IsEmpty)
        {
            int offset = typeReference.StartLocation.Column - 1;
            int len = typeReference.EndLocation.Column - typeReference.StartLocation.Column;
            int i = expression.IndexOf ('<', offset);
            if (i != -1 && i < offset + len)
                len = i - offset;
            ResolveType (typeReference.Type, typeReference.GenericTypes.Count, offset, len);
        }
        return base.VisitTypeReference (typeReference, data);
    }

}
}

