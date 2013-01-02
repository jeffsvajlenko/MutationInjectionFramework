// NRefactoryEvaluator.cs
//
// Authors: Lluis Sanchez Gual <lluis@novell.com>
//          Jeffrey Stedfast <jeff@xamarin.com>
//
// Copyright (c) 2008 Novell, Inc (http://www.novell.com)
// Copyright (c) 2012 Xamarin Inc. (http://www.xamarin.com)
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
//
//

using System;
using System.Collections.Generic;
using System.IO;
using System.Runtime.InteropServices;
using DC = Mono.Debugging.Client;
using ICSharpCode.OldNRefactory;
using ICSharpCode.OldNRefactory.Parser;
using ICSharpCode.OldNRefactory.Visitors;
using ICSharpCode.OldNRefactory.Ast;
using System.Reflection;
using Mono.Debugging.Client;
using Mono.Debugging.Backend;

namespace Mono.Debugging.Evaluation
{
public class NRefactoryEvaluator: ExpressionEvaluator
{
    Dictionary<string,ValueReference> userVariables = new Dictionary<string, ValueReference> ();

    public override ValueReference Evaluate (EvaluationContext ctx, string exp, object expectedType)
    {
        return Evaluate (ctx, exp, expectedType, false);
    }

    ValueReference Evaluate (EvaluationContext ctx, string exp, object expectedType, bool tryTypeOf)
    {
        exp = exp.TrimStart ();
        if (exp.StartsWith ("?"))
            exp = exp.Substring (1).Trim ();
        if (exp.StartsWith ("var "))
        {
            exp = exp.Substring (4).Trim (' ','\t');
            string var = null;
            for (int n=0; n<exp.Length; n++)
            {
                if (!char.IsLetterOrDigit (exp[n]) && exp[n] != '_')
                {
                    var = exp.Substring (0, n);
                    if (!exp.Substring (n).Trim (' ','\t').StartsWith ("="))
                        var = null;
                    break;
                }
                if (n == exp.Length - 1)
                {
                    var = exp;
                    exp = null;
                    break;
                }
            }
            if (!string.IsNullOrEmpty (var))
                userVariables [var] = new UserVariableReference (ctx, var);
            if (exp == null)
                return null;
        }

        exp = ReplaceExceptionTag (exp, ctx.Options.CurrentExceptionTag);

        StringReader codeStream = new StringReader (exp);
        IParser parser = ParserFactory.CreateParser (SupportedLanguage.CSharp, codeStream);
        Expression expObj = parser.ParseExpression ();
        if (expObj == null)
            throw new EvaluatorException ("Could not parse expression '{0}'", exp);

        try
        {
            EvaluatorVisitor ev = new EvaluatorVisitor (ctx, exp, expectedType, userVariables, tryTypeOf);
            return (ValueReference) expObj.AcceptVisitor (ev, null);
        }
        catch
        {
            if (!tryTypeOf && (expObj is BinaryOperatorExpression) && IsTypeName (exp))
            {
                // This is a hack to be able to parse expressions such as "List<string>". The NRefactory parser
                // can parse a single type name, so a solution is to wrap it around a typeof(). We do it if
                // the evaluation fails.
                return Evaluate (ctx, "typeof(" + exp + ")", expectedType, true);
            }
            else
                throw;
        }
    }

    public override string Resolve (DebuggerSession session, SourceLocation location, string exp)
    {
        return Resolve (session, location, exp, false);
    }

    string Resolve (DebuggerSession session, SourceLocation location, string exp, bool tryTypeOf)
    {
        exp = exp.TrimStart ();
        if (exp.StartsWith ("?"))
            return "?" + Resolve (session, location, exp.Substring (1).Trim ());
        if (exp.StartsWith ("var "))
            return "var " + Resolve (session, location, exp.Substring (4).Trim (' ','\t'));

        exp = ReplaceExceptionTag (exp, session.Options.EvaluationOptions.CurrentExceptionTag);

        StringReader codeStream = new StringReader (exp);
        IParser parser = ParserFactory.CreateParser (SupportedLanguage.CSharp, codeStream);
        Expression expObj = parser.ParseExpression ();
        if (expObj == null)
            return exp;
        NRefactoryResolverVisitor ev = new NRefactoryResolverVisitor (session, location, exp);
        expObj.AcceptVisitor (ev, null);
        string r = ev.GetResolvedExpression ();
        if (r == exp && !tryTypeOf && (expObj is BinaryOperatorExpression) && IsTypeName (exp))
        {
            // This is a hack to be able to parse expressions such as "List<string>". The NRefactory parser
            // can parse a single type name, so a solution is to wrap it around a typeof(). We do it if
            // the evaluation fails.
            string res = Resolve (session, location, "typeof(" + exp + ")", true);
            return res.Substring (7, res.Length - 8);
        }
        return r;
    }

    public override ValidationResult ValidateExpression (EvaluationContext ctx, string exp)
    {
        exp = exp.TrimStart ();
        if (exp.StartsWith ("?"))
            exp = exp.Substring (1).Trim ();
        if (exp.StartsWith ("var "))
            exp = exp.Substring (4).Trim ();

        exp = ReplaceExceptionTag (exp, ctx.Options.CurrentExceptionTag);

        // Required as a workaround for a bug in the parser (it won't parse simple expressions like numbers)
        if (!exp.EndsWith (";"))
            exp += ";";

        StringReader codeStream = new StringReader (exp);
        IParser parser = ParserFactory.CreateParser (SupportedLanguage.CSharp, codeStream);

        string errorMsg = null;
        parser.Errors.Error = delegate (int line, int col, string msg)
        {
            if (errorMsg == null)
                errorMsg = msg;
        };

        parser.ParseExpression ();

        if (errorMsg != null)
            return new ValidationResult (false, errorMsg);
        else
            return new ValidationResult (true, null);
    }

    string ReplaceExceptionTag (string exp, string tag)
    {
        // FIXME: Don't replace inside string literals
        return exp.Replace (tag, "__EXCEPTION_OBJECT__");
    }

    bool IsTypeName (string name)
    {
        int pos = 0;
        bool res = ParseTypeName (name + "$", ref pos);
        return res && pos >= name.Length;
    }

    bool ParseTypeName (string name, ref int pos)
    {
        EatSpaces (name, ref pos);
        if (!ParseName (name, ref pos))
            return false;
        EatSpaces (name, ref pos);
        if (!ParseGenericArgs (name, ref pos))
            return false;
        EatSpaces (name, ref pos);
        if (!ParseIndexer (name, ref pos))
            return false;
        EatSpaces (name, ref pos);
        return true;
    }

    void EatSpaces (string name, ref int pos)
    {
        while (char.IsWhiteSpace (name[pos]))
            pos++;
    }

    bool ParseName (string name, ref int pos)
    {
        if (name[0] == 'g' && pos < name.Length - 8 && name.Substring (pos, 8) == "global::")
            pos += 8;
        do
        {
            int oldp = pos;
            while (char.IsLetterOrDigit (name[pos]))
                pos++;
            if (oldp == pos)
                return false;
            if (name[pos] != '.')
                return true;
            pos++;
        }
        while (true);
    }

    bool ParseGenericArgs (string name, ref int pos)
    {
        if (name [pos] != '<')
            return true;
        pos++;
        EatSpaces (name, ref pos);
        while (true)
        {
            if (!ParseTypeName (name, ref pos))
                return false;
            EatSpaces (name, ref pos);
            char c = name [pos++];
            if (c == '>')
                return true;
            else if (c == ',')
                continue;
            else
                return false;
        }
    }

    bool ParseIndexer (string name, ref int pos)
    {
        if (name [pos] != '[')
            return true;
        do
        {
            pos++;
            EatSpaces (name, ref pos);
        }
        while (name [pos] == ',');
        return name [pos++] == ']';
    }
}

class EvaluatorVisitor: AbstractAstVisitor
{
    EvaluationContext ctx;
    EvaluationOptions options;
    string name;
    object expectedType;
    bool tryTypeOf;
    Dictionary<string,ValueReference> userVariables;

    public EvaluatorVisitor (EvaluationContext ctx, string name, object expectedType, Dictionary<string,ValueReference> userVariables, bool tryTypeOf)
    {
        this.ctx = ctx;
        this.name = name;
        this.expectedType = expectedType;
        this.userVariables = userVariables;
        this.options = ctx.Options;
        this.tryTypeOf = tryTypeOf;
    }

    long GetInteger (object val)
    {
        try
        {
            return Convert.ToInt64 (val);
        }
        catch
        {
            throw CreateParseError ("Expected integer value");
        }
    }

    public override object VisitUnaryOperatorExpression (ICSharpCode.OldNRefactory.Ast.UnaryOperatorExpression unaryOperatorExpression, object data)
    {
        ValueReference vref = (ValueReference) unaryOperatorExpression.Expression.AcceptVisitor (this, null);
        object val = vref.ObjectValue;

        switch (unaryOperatorExpression.Op)
        {
        case UnaryOperatorType.BitNot:
        {
            long num = GetInteger (val);
            num = ~num;
            val = Convert.ChangeType (num, val.GetType ());
            break;
        }
        case UnaryOperatorType.Minus:
        {
            long num = GetInteger (val);
            num = -num;
            val = Convert.ChangeType (num, val.GetType ());
            break;
        }
        case UnaryOperatorType.Not:
        {
            if (!(val is bool))
                throw CreateParseError ("Expected boolean type in Not operator");

            val = !(bool) val;
            break;
        }
        case UnaryOperatorType.Decrement:
        {
            long num = GetInteger (val);
            val = Convert.ChangeType (num - 1, val.GetType ());
            vref.Value = ctx.Adapter.CreateValue (ctx, val);
            break;
        }
        case UnaryOperatorType.Increment:
        {
            long num = GetInteger (val);
            val = Convert.ChangeType (num + 1, val.GetType ());
            vref.Value = ctx.Adapter.CreateValue (ctx, val);
            break;
        }
        case UnaryOperatorType.PostDecrement:
        {
            long num = GetInteger (val);
            object newVal = Convert.ChangeType (num - 1, val.GetType ());
            vref.Value = ctx.Adapter.CreateValue (ctx, newVal);
            break;
        }
        case UnaryOperatorType.PostIncrement:
        {
            long num = GetInteger (val);
            object newVal = Convert.ChangeType (num + 1, val.GetType ());
            vref.Value = ctx.Adapter.CreateValue (ctx, newVal);
            break;
        }
        case UnaryOperatorType.Plus:
            break;
        default:
            throw CreateNotSupportedError ();
        }

        return LiteralValueReference.CreateObjectLiteral (ctx, name, val);
    }

    public override object VisitTypeReference (ICSharpCode.OldNRefactory.Ast.TypeReference typeReference, object data)
    {
        object type = ToTargetType (typeReference);
        if (type != null)
            return new TypeValueReference (ctx, type);
        else
            throw CreateParseError ("Unknown type: " + typeReference.Type);
    }

    public override object VisitTypeReferenceExpression (ICSharpCode.OldNRefactory.Ast.TypeReferenceExpression typeReferenceExpression, object data)
    {
        if (typeReferenceExpression.TypeReference.IsGlobal || typeReferenceExpression.TypeReference.IsKeyword)
        {
            string name = typeReferenceExpression.TypeReference.Type;
            object type = ctx.Options.AllowImplicitTypeLoading ? ctx.Adapter.ForceLoadType (ctx, name) : ctx.Adapter.GetType (ctx, name);
            if (type != null)
                return new TypeValueReference (ctx, type);

            if (!ctx.Options.AllowImplicitTypeLoading)
            {
                string[] namespaces = ctx.Adapter.GetImportedNamespaces (ctx);
                if (namespaces.Length > 0)
                {
                    // Look in namespaces
                    foreach (string ns in namespaces)
                    {
                        if (name == ns || ns.StartsWith (name + "."))
                            return new NamespaceValueReference (ctx, name);
                    }
                }
            }
            else
            {
                // Assume it is a namespace.
                return new NamespaceValueReference (ctx, name);
            }
        }
        throw CreateNotSupportedError ();
    }

    object ToTargetType (TypeReference type)
    {
        if (type.IsNull)
            throw CreateParseError ("Invalid type reference");
        if (type.GenericTypes.Count == 0)
            return ctx.Adapter.GetType (ctx, type.Type);
        else
        {
            object[] args = new object [type.GenericTypes.Count];
            for (int n=0; n<args.Length; n++)
            {
                object t = ToTargetType (type.GenericTypes [n]);
                if (t == null)
                    return null;
                args [n] = t;
            }
            return ctx.Adapter.GetType (ctx, type.Type + "`" + args.Length, args);
        }
    }

    public override object VisitTypeOfExpression (ICSharpCode.OldNRefactory.Ast.TypeOfExpression typeOfExpression, object data)
    {
        if (tryTypeOf)
        {
            // The parser is trying to evaluate a type name, but since NRefactory has problems parsing generic types,
            // it has to do it by wrapping it with a typeof(). In this case, it sets tryTypeOf=true, meaning that
            // typeof in this case has to be evaluated in a special way: as a type reference.
            return typeOfExpression.TypeReference.AcceptVisitor (this, data);
        }
        object type = ToTargetType (typeOfExpression.TypeReference);
        if (type == null)
            throw CreateParseError ("Unknown type: " + typeOfExpression.TypeReference.Type);
        object ob = ctx.Adapter.CreateTypeObject (ctx, type);
        if (ob != null)
            return LiteralValueReference.CreateTargetObjectLiteral (ctx, typeOfExpression.TypeReference.Type, ob);
        else
            throw CreateNotSupportedError ();
    }

    public override object VisitThisReferenceExpression (ICSharpCode.OldNRefactory.Ast.ThisReferenceExpression thisReferenceExpression, object data)
    {
        ValueReference val = ctx.Adapter.GetThisReference (ctx);
        if (val != null)
            return val;
        else
            throw CreateParseError ("'this' reference not available in the current evaluation context.");
    }

    public override object VisitPrimitiveExpression (ICSharpCode.OldNRefactory.Ast.PrimitiveExpression primitiveExpression, object data)
    {
        if (primitiveExpression.Value != null)
            return LiteralValueReference.CreateObjectLiteral (ctx, name, primitiveExpression.Value);
        else if (expectedType != null)
            return new NullValueReference (ctx, expectedType);
        else
            return new NullValueReference (ctx, ctx.Adapter.GetType (ctx, "System.Object"));
    }

    public override object VisitParenthesizedExpression (ICSharpCode.OldNRefactory.Ast.ParenthesizedExpression parenthesizedExpression, object data)
    {
        return parenthesizedExpression.Expression.AcceptVisitor (this, data);
    }

    public override object VisitObjectCreateExpression (ICSharpCode.OldNRefactory.Ast.ObjectCreateExpression objectCreateExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitInvocationExpression (ICSharpCode.OldNRefactory.Ast.InvocationExpression invocationExpression, object data)
    {
        if (!options.AllowMethodEvaluation)
            throw CreateNotSupportedError ();

        bool invokeBaseMethod = false;
        ValueReference target = null;
        string methodName;

        object[] argtypes = new object[invocationExpression.Arguments.Count];
        object[] args = new object[invocationExpression.Arguments.Count];
        for (int n=0; n<args.Length; n++)
        {
            Expression exp = invocationExpression.Arguments [n];
            ValueReference vref = (ValueReference) exp.AcceptVisitor (this, data);
            args [n] = vref.Value;
            argtypes [n] = ctx.Adapter.GetValueType (ctx, args [n]);
        }

        if (invocationExpression.TargetObject is MemberReferenceExpression)
        {
            MemberReferenceExpression field = (MemberReferenceExpression)invocationExpression.TargetObject;
            target = (ValueReference) field.TargetObject.AcceptVisitor (this, data);
            if (field.TargetObject is BaseReferenceExpression)
                invokeBaseMethod = true;
            methodName = field.MemberName;
        }
        else if (invocationExpression.TargetObject is IdentifierExpression)
        {
            IdentifierExpression exp = (IdentifierExpression) invocationExpression.TargetObject;
            methodName = exp.Identifier;
            ValueReference vref = ctx.Adapter.GetThisReference (ctx);
            if (vref != null && ctx.Adapter.HasMethod (ctx, vref.Type, methodName, BindingFlags.Instance))
            {
                // There is an instance method for 'this', although it may not have an exact signature match. Check it now.
                if (ctx.Adapter.HasMethod (ctx, vref.Type, methodName, argtypes, BindingFlags.Instance))
                    target = vref;
                else
                {
                    // There isn't an instance method with exact signature match.
                    // If there isn't a static method, then use the instance method,
                    // which will report the signature match error when invoked
                    object etype = ctx.Adapter.GetEnclosingType (ctx);
                    if (!ctx.Adapter.HasMethod (ctx, etype, methodName, argtypes, BindingFlags.Static))
                        target = vref;
                }
            }
            else
            {
                if (ctx.Adapter.HasMethod (ctx, ctx.Adapter.GetEnclosingType (ctx), methodName, argtypes, BindingFlags.Instance))
                    throw new EvaluatorException ("Can't invoke an instance method from a static method.");
                target = null;
            }
        }
        else
            throw CreateNotSupportedError ();

        object vtype = target != null ? target.Type : ctx.Adapter.GetEnclosingType (ctx);
        object vtarget = (target is TypeValueReference) || target == null ? null : target.Value;

        if (invokeBaseMethod)
            vtype = ctx.Adapter.GetBaseType (ctx, vtype);

        object res = ctx.Adapter.RuntimeInvoke (ctx, vtype, vtarget, methodName, argtypes, args);
        if (res != null)
            return LiteralValueReference.CreateTargetObjectLiteral (ctx, name, res);
        else
            return LiteralValueReference.CreateVoidReturnLiteral (ctx, name);
    }

    public override object VisitInnerClassTypeReference (ICSharpCode.OldNRefactory.Ast.InnerClassTypeReference innerClassTypeReference, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitIndexerExpression (ICSharpCode.OldNRefactory.Ast.IndexerExpression indexerExpression, object data)
    {
        ValueReference val = (ValueReference) indexerExpression.TargetObject.AcceptVisitor (this, data);
        if (val is TypeValueReference)
            throw CreateNotSupportedError ();
        if (ctx.Adapter.IsArray (ctx, val.Value))
        {
            int[] indexes = new int [indexerExpression.Indexes.Count];
            for (int n=0; n<indexes.Length; n++)
            {
                ValueReference vi = (ValueReference) indexerExpression.Indexes[n].AcceptVisitor (this, data);
                indexes [n] = (int) Convert.ChangeType (vi.ObjectValue, typeof(int));
            }
            return new ArrayValueReference (ctx, val.Value, indexes);
        }

        object[] args = new object [indexerExpression.Indexes.Count];
        for (int n=0; n<args.Length; n++)
            args [n] = ((ValueReference) indexerExpression.Indexes[n].AcceptVisitor (this, data)).Value;

        ValueReference res = ctx.Adapter.GetIndexerReference (ctx, val.Value, args);
        if (res != null)
            return res;

        throw CreateNotSupportedError ();
    }

    public override object VisitIdentifierExpression (ICSharpCode.OldNRefactory.Ast.IdentifierExpression identifierExpression, object data)
    {
        return VisitIdentifier (identifierExpression.Identifier);
    }

    object VisitIdentifier (string name)
    {
        // Exception tag

        if (name == "__EXCEPTION_OBJECT__")
            return ctx.Adapter.GetCurrentException (ctx);

        // Look in user defined variables

        ValueReference userVar;
        if (userVariables.TryGetValue (name, out userVar))
            return userVar;

        // Look in variables

        ValueReference var = ctx.Adapter.GetLocalVariable (ctx, name);
        if (var != null)
            return var;

        // Look in parameters

        var = ctx.Adapter.GetParameter (ctx, name);
        if (var != null)
            return var;

        // Look in fields and properties

        ValueReference thisobj = ctx.Adapter.GetThisReference (ctx);
        object thistype = thisobj != null ? thisobj.Type : ctx.Adapter.GetEnclosingType (ctx);

        var = ctx.Adapter.GetMember (ctx, thisobj, thistype, thisobj != null ? thisobj.Value : null, name);
        if (var != null)
            return var;

        // Look in types

        object vtype = ctx.Adapter.GetType (ctx, name);
        if (vtype != null)
            return new TypeValueReference (ctx, vtype);

        // Look in nested types

        vtype = ctx.Adapter.GetEnclosingType (ctx);
        if (vtype != null)
        {
            foreach (object ntype in ctx.Adapter.GetNestedTypes (ctx, vtype))
            {
                if (TypeValueReference.GetTypeName (ctx.Adapter.GetTypeName (ctx, ntype)) == name)
                    return new TypeValueReference (ctx, ntype);
            }

            string[] namespaces = ctx.Adapter.GetImportedNamespaces (ctx);
            if (namespaces.Length > 0)
            {
                // Look in namespaces
                foreach (string ns in namespaces)
                {
                    string nm = ns + "." + name;
                    vtype = ctx.Options.AllowImplicitTypeLoading ? ctx.Adapter.ForceLoadType (ctx, nm) : ctx.Adapter.GetType (ctx, nm);
                    if (vtype != null)
                        return new TypeValueReference (ctx, vtype);
                }
                foreach (string ns in namespaces)
                {
                    if (ns == name || ns.StartsWith (name + "."))
                        return new NamespaceValueReference (ctx, name);
                }
            }
        }

        if (thisobj == null && ctx.Adapter.HasMember (ctx, thistype, name, BindingFlags.Public | BindingFlags.NonPublic | BindingFlags.Instance))
        {
            string message = string.Format ("An object reference is required for the non-static field, method, or property '{0}.{1}'",
                                            ctx.Adapter.GetDisplayTypeName (ctx, thistype), name);
            throw CreateParseError (message);
        }

        throw CreateParseError ("Unknown identifier: {0}", name);
    }

    public override object VisitMemberReferenceExpression (MemberReferenceExpression memberReferenceExpression, object data)
    {
        ValueReference vref = (ValueReference) memberReferenceExpression.TargetObject.AcceptVisitor (this, data);
        ValueReference ch = vref.GetChild (memberReferenceExpression.MemberName, ctx.Options);
        if (ch == null)
            throw CreateParseError ("Unknown member: {0}", memberReferenceExpression.MemberName);
        return ch;
    }

    public override object VisitConditionalExpression (ICSharpCode.OldNRefactory.Ast.ConditionalExpression conditionalExpression, object data)
    {
        ValueReference val = (ValueReference) conditionalExpression.Condition.AcceptVisitor (this, data);
        if (val is TypeValueReference)
            throw CreateNotSupportedError ();

        bool cond = (bool) val.ObjectValue;
        if (cond)
            return conditionalExpression.TrueExpression.AcceptVisitor (this, data);
        else
            return conditionalExpression.FalseExpression.AcceptVisitor (this, data);
    }

    public override object VisitClassReferenceExpression (ICSharpCode.OldNRefactory.Ast.ClassReferenceExpression classReferenceExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitCastExpression (ICSharpCode.OldNRefactory.Ast.CastExpression castExpression, object data)
    {
        ValueReference val = (ValueReference)castExpression.Expression.AcceptVisitor (this, data);
        TypeValueReference type = castExpression.CastTo.AcceptVisitor (this, data) as TypeValueReference;
        if (type == null)
            throw CreateParseError ("Invalid cast type.");
        object ob = ctx.Adapter.TryCast (ctx, val.Value, type.Type);
        if (ob == null)
        {
            if (castExpression.CastType == CastType.TryCast)
                return new NullValueReference (ctx, type.Type);
            else
                throw CreateParseError ("Invalid cast.");
        }
        return LiteralValueReference.CreateTargetObjectLiteral (ctx, name, ob);
    }

    public override object VisitBinaryOperatorExpression (ICSharpCode.OldNRefactory.Ast.BinaryOperatorExpression binaryOperatorExpression, object data)
    {
        ValueReference left = (ValueReference) binaryOperatorExpression.Left.AcceptVisitor (this, data);
        return EvaluateBinaryOperatorExpression (left, binaryOperatorExpression.Right, binaryOperatorExpression.Op, data);
    }

    static string GetCommonOperationType (object v1, object v2)
    {
        if (v1 is double || v2 is double)
            return "System.Double";

        if (v1 is float || v2 is float)
            return "System.Double";

        return "System.Int64";
    }

    object EvaluateOperation (BinaryOperatorType op, double v1, double v2)
    {
        switch (op)
        {
        case BinaryOperatorType.Add:
            return v1 + v2;
        case BinaryOperatorType.DivideInteger:
        case BinaryOperatorType.Divide:
            return v1 / v2;
        case BinaryOperatorType.Multiply:
            return v1 * v2;
        case BinaryOperatorType.Subtract:
            return v1 - v2;
        case BinaryOperatorType.GreaterThan:
            return v1 > v2;
        case BinaryOperatorType.GreaterThanOrEqual:
            return v1 >= v2;
        case BinaryOperatorType.LessThan:
            return v1 < v2;
        case BinaryOperatorType.LessThanOrEqual:
            return v1 <= v2;
        case BinaryOperatorType.ReferenceEquality:
        case BinaryOperatorType.Equality:
            return v1 == v2;
        case BinaryOperatorType.ReferenceInequality:
        case BinaryOperatorType.InEquality:
            return v1 != v2;
        default:
            throw CreateParseError ("Invalid binary operator.");
        }
    }

    object EvaluateOperation (BinaryOperatorType op, long v1, long v2)
    {
        switch (op)
        {
        case BinaryOperatorType.Add:
            return v1 + v2;
        case BinaryOperatorType.BitwiseAnd:
            return v1 & v2;
        case BinaryOperatorType.BitwiseOr:
            return v1 | v2;
        case BinaryOperatorType.ExclusiveOr:
            return v1 ^ v2;
        case BinaryOperatorType.DivideInteger:
        case BinaryOperatorType.Divide:
            return v1 / v2;
        case BinaryOperatorType.Modulus:
            return v1 % v2;
        case BinaryOperatorType.Multiply:
            return v1 * v2;
        case BinaryOperatorType.Power:
            return v1 ^ v2;
        case BinaryOperatorType.ShiftLeft:
            return v1 << (int) v2;
        case BinaryOperatorType.ShiftRight:
            return v1 >> (int) v2;
        case BinaryOperatorType.Subtract:
            return v1 - v2;
        case BinaryOperatorType.GreaterThan:
            return v1 > v2;
        case BinaryOperatorType.GreaterThanOrEqual:
            return v1 >= v2;
        case BinaryOperatorType.LessThan:
            return v1 < v2;
        case BinaryOperatorType.LessThanOrEqual:
            return v1 <= v2;
        case BinaryOperatorType.ReferenceEquality:
        case BinaryOperatorType.Equality:
            return v1 == v2;
        case BinaryOperatorType.ReferenceInequality:
        case BinaryOperatorType.InEquality:
            return v1 != v2;
        default:
            throw CreateParseError ("Invalid binary operator.");
        }
    }

    void ConvertValues<T> (EvaluationContext ctx, object actualV1, object actualV2, object toType, out T v1, out T v2)
    {
        try
        {
            object c1 = ctx.Adapter.Cast (ctx, actualV1, toType);
            v1 = (T) ctx.Adapter.TargetObjectToObject (ctx, c1);

            object c2 = ctx.Adapter.Cast (ctx, actualV2, toType);
            v2 = (T) ctx.Adapter.TargetObjectToObject (ctx, c2);
        }
        catch
        {
            throw CreateParseError ("Invalid operands in binary operator");
        }
    }

    bool CheckEquality (object v1, object v2)
    {
        object targetType = ctx.Adapter.GetValueType (ctx, v1);
        object[] argTypes = new object[]
        {
            ctx.Adapter.GetType (ctx, "System.Object")
        };
        object[] args = new object[]
        {
            v2
        };

        object result = ctx.Adapter.RuntimeInvoke (ctx, targetType, v1, "Equals", argTypes, args);
        var literal = LiteralValueReference.CreateTargetObjectLiteral (ctx, "result", result);

        return (bool) literal.ObjectValue;
    }

    object EvaluateBinaryOperatorExpression (ValueReference left, ICSharpCode.OldNRefactory.Ast.Expression rightExp, BinaryOperatorType oper, object data)
    {
        // Shortcut ops

        switch (oper)
        {
        case BinaryOperatorType.LogicalAnd:
        {
            object val = left.ObjectValue;
            if (!(val is bool))
                throw CreateParseError ("Left operand of logical And must be a boolean");
            if (!(bool)val)
                return LiteralValueReference.CreateObjectLiteral (ctx, name, false);
            ValueReference vr = (ValueReference) rightExp.AcceptVisitor (this, data);
            if (vr == null || ctx.Adapter.GetTypeName (ctx, vr.Type) != "System.Boolean")
                throw CreateParseError ("Right operand of logical And must be a boolean");
            return vr;
        }
        case BinaryOperatorType.LogicalOr:
        {
            object val = left.ObjectValue;
            if (!(val is bool))
                throw CreateParseError ("Left operand of logical Or must be a boolean");
            if ((bool)val)
                return LiteralValueReference.CreateObjectLiteral (ctx, name, true);
            ValueReference vr = (ValueReference) rightExp.AcceptVisitor (this, data);
            if (vr == null || ctx.Adapter.GetTypeName (ctx, vr.Type) != "System.Boolean")
                throw CreateParseError ("Right operand of logical Or must be a boolean");
            return vr;
        }
        }

        ValueReference right = (ValueReference) rightExp.AcceptVisitor (this, data);
        object targetVal1 = left.Value;
        object targetVal2 = right.Value;
        object val1 = left.ObjectValue;
        object val2 = right.ObjectValue;

        if (oper == BinaryOperatorType.Add || oper == BinaryOperatorType.Concat)
        {
            if (val1 is string || val2 is string)
            {
                if (!(val1 is string) && val1 != null)
                    val1 = ctx.Adapter.CallToString (ctx, targetVal1);
                if (!(val2 is string) && val2 != null)
                    val2 = ctx.Adapter.CallToString (ctx, targetVal2);
                return LiteralValueReference.CreateObjectLiteral (ctx, name, (string) val1 + (string) val2);
            }
        }

        if ((oper == BinaryOperatorType.ExclusiveOr) && (val1 is bool) && (val2 is bool))
            return LiteralValueReference.CreateObjectLiteral (ctx, name, (bool)val1 ^ (bool)val2);

        if ((val1 == null || !ctx.Adapter.IsPrimitive (ctx, targetVal1)) && (val2 == null || !ctx.Adapter.IsPrimitive (ctx, targetVal2)))
        {
            switch (oper)
            {
            case BinaryOperatorType.Equality:
                if (val1 == null || val2 == null)
                    return LiteralValueReference.CreateObjectLiteral (ctx, name, val1 == val2);
                return LiteralValueReference.CreateObjectLiteral (ctx, name, CheckEquality (targetVal1, targetVal2));
            case BinaryOperatorType.InEquality:
                if (val1 == null || val2 == null)
                    return LiteralValueReference.CreateObjectLiteral (ctx, name, val1 != val2);
                return LiteralValueReference.CreateObjectLiteral (ctx, name, !CheckEquality (targetVal1, targetVal2));
            case BinaryOperatorType.ReferenceEquality:
                return LiteralValueReference.CreateObjectLiteral (ctx, name, val1 == val2);
            case BinaryOperatorType.ReferenceInequality:
                return LiteralValueReference.CreateObjectLiteral (ctx, name, val1 != val2);
            case BinaryOperatorType.Concat:
                throw CreateParseError ("Invalid binary operator.");
            }
        }

        if (val1 == null || val2 == null || (val1 is bool) || (val2 is bool))
            throw CreateParseError ("Invalid operands in binary operator");

        string opTypeName = GetCommonOperationType (val1, val2);
        object opType = ctx.Adapter.GetType (ctx, opTypeName);
        object res;

        if (opTypeName == "System.Double")
        {
            double v1, v2;

            ConvertValues<double> (ctx, targetVal1, targetVal2, opType, out v1, out v2);
            res = EvaluateOperation (oper, v1, v2);
        }
        else
        {
            long v1, v2;

            ConvertValues<long> (ctx, targetVal1, targetVal2, opType, out v1, out v2);
            res = EvaluateOperation (oper, v1, v2);
        }

        if (!(res is bool))
        {
            if (ctx.Adapter.IsEnum (ctx, targetVal1))
            {
                object tval = ctx.Adapter.Cast (ctx, ctx.Adapter.CreateValue (ctx, res), ctx.Adapter.GetValueType (ctx, targetVal1));
                return LiteralValueReference.CreateTargetObjectLiteral (ctx, name, tval);
            }

            if (ctx.Adapter.IsEnum (ctx, targetVal2))
            {
                object tval = ctx.Adapter.Cast (ctx, ctx.Adapter.CreateValue (ctx, res), ctx.Adapter.GetValueType (ctx, targetVal2));
                return LiteralValueReference.CreateTargetObjectLiteral (ctx, name, tval);
            }

            res = Convert.ChangeType (res, GetCommonType (val1, val2));
        }

        return LiteralValueReference.CreateObjectLiteral (ctx, name, res);
    }

    Type GetCommonType (object v1, object v2)
    {
        int s1 = Marshal.SizeOf (v1);
        if (IsUnsigned (s1))
            s1 += 8;
        int s2 = Marshal.SizeOf (v2);
        if (IsUnsigned (s2))
            s2 += 8;
        if (s1 > s2)
            return v1.GetType ();
        else
            return v2.GetType ();
    }

    bool IsUnsigned (object v)
    {
        return (v is byte) || (v is ushort) || (v is uint) || (v is ulong);
    }

    public override object VisitBaseReferenceExpression (ICSharpCode.OldNRefactory.Ast.BaseReferenceExpression baseReferenceExpression, object data)
    {
        ValueReference thisobj = ctx.Adapter.GetThisReference (ctx);
        if (thisobj != null)
            return LiteralValueReference.CreateTargetBaseObjectLiteral (ctx, name, thisobj.Value);
        else
            throw CreateParseError ("'base' reference not available in static methods.");
    }

    public override object VisitAssignmentExpression (ICSharpCode.OldNRefactory.Ast.AssignmentExpression assignmentExpression, object data)
    {
        if (!options.AllowMethodEvaluation)
            throw CreateNotSupportedError ();

        ValueReference left = (ValueReference) assignmentExpression.Left.AcceptVisitor (this, data);

        if (assignmentExpression.Op == AssignmentOperatorType.Assign)
        {
            ValueReference right = (ValueReference) assignmentExpression.Right.AcceptVisitor (this, data);
            left.Value = right.Value;
        }
        else
        {
            BinaryOperatorType bop = BinaryOperatorType.None;
            switch (assignmentExpression.Op)
            {
            case AssignmentOperatorType.Add:
                bop = BinaryOperatorType.Add;
                break;
            case AssignmentOperatorType.BitwiseAnd:
                bop = BinaryOperatorType.BitwiseAnd;
                break;
            case AssignmentOperatorType.BitwiseOr:
                bop = BinaryOperatorType.BitwiseOr;
                break;
            case AssignmentOperatorType.ConcatString:
                bop = BinaryOperatorType.Concat;
                break;
            case AssignmentOperatorType.Divide:
                bop = BinaryOperatorType.Divide;
                break;
            case AssignmentOperatorType.DivideInteger:
                bop = BinaryOperatorType.DivideInteger;
                break;
            case AssignmentOperatorType.ExclusiveOr:
                bop = BinaryOperatorType.ExclusiveOr;
                break;
            case AssignmentOperatorType.Modulus:
                bop = BinaryOperatorType.Modulus;
                break;
            case AssignmentOperatorType.Multiply:
                bop = BinaryOperatorType.Multiply;
                break;
            case AssignmentOperatorType.Power:
                bop = BinaryOperatorType.Power;
                break;
            case AssignmentOperatorType.ShiftLeft:
                bop = BinaryOperatorType.ShiftLeft;
                break;
            case AssignmentOperatorType.ShiftRight:
                bop = BinaryOperatorType.ShiftRight;
                break;
            case AssignmentOperatorType.Subtract:
                bop = BinaryOperatorType.Subtract;
                break;
            }
            ValueReference val = (ValueReference) EvaluateBinaryOperatorExpression (left, assignmentExpression.Right, bop, data);
            left.Value = val.Value;
        }
        return left;
    }

    public override object VisitArrayCreateExpression (ICSharpCode.OldNRefactory.Ast.ArrayCreateExpression arrayCreateExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    #region Unsupported expressions

    public override object VisitPointerReferenceExpression (ICSharpCode.OldNRefactory.Ast.PointerReferenceExpression pointerReferenceExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitSizeOfExpression (ICSharpCode.OldNRefactory.Ast.SizeOfExpression sizeOfExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitTypeOfIsExpression (ICSharpCode.OldNRefactory.Ast.TypeOfIsExpression typeOfIsExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitYieldStatement (ICSharpCode.OldNRefactory.Ast.YieldStatement yieldStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitWithStatement (ICSharpCode.OldNRefactory.Ast.WithStatement withStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitVariableDeclaration (ICSharpCode.OldNRefactory.Ast.VariableDeclaration variableDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitUsing (ICSharpCode.OldNRefactory.Ast.Using @using, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitUsingStatement (ICSharpCode.OldNRefactory.Ast.UsingStatement usingStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitUsingDeclaration (ICSharpCode.OldNRefactory.Ast.UsingDeclaration usingDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitUnsafeStatement (ICSharpCode.OldNRefactory.Ast.UnsafeStatement unsafeStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitUncheckedStatement (ICSharpCode.OldNRefactory.Ast.UncheckedStatement uncheckedStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitTypeDeclaration (ICSharpCode.OldNRefactory.Ast.TypeDeclaration typeDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitTryCatchStatement (ICSharpCode.OldNRefactory.Ast.TryCatchStatement tryCatchStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitThrowStatement (ICSharpCode.OldNRefactory.Ast.ThrowStatement throwStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitTemplateDefinition (ICSharpCode.OldNRefactory.Ast.TemplateDefinition templateDefinition, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitSwitchStatement (ICSharpCode.OldNRefactory.Ast.SwitchStatement switchStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitSwitchSection (ICSharpCode.OldNRefactory.Ast.SwitchSection switchSection, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitStopStatement (ICSharpCode.OldNRefactory.Ast.StopStatement stopStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitStackAllocExpression (ICSharpCode.OldNRefactory.Ast.StackAllocExpression stackAllocExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitReturnStatement (ICSharpCode.OldNRefactory.Ast.ReturnStatement returnStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitResumeStatement (ICSharpCode.OldNRefactory.Ast.ResumeStatement resumeStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitRemoveHandlerStatement (ICSharpCode.OldNRefactory.Ast.RemoveHandlerStatement removeHandlerStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitReDimStatement (ICSharpCode.OldNRefactory.Ast.ReDimStatement reDimStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitRaiseEventStatement (ICSharpCode.OldNRefactory.Ast.RaiseEventStatement raiseEventStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitPropertySetRegion (ICSharpCode.OldNRefactory.Ast.PropertySetRegion propertySetRegion, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitPropertyGetRegion (ICSharpCode.OldNRefactory.Ast.PropertyGetRegion propertyGetRegion, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitPropertyDeclaration (ICSharpCode.OldNRefactory.Ast.PropertyDeclaration propertyDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitParameterDeclarationExpression (ICSharpCode.OldNRefactory.Ast.ParameterDeclarationExpression parameterDeclarationExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitOptionDeclaration (ICSharpCode.OldNRefactory.Ast.OptionDeclaration optionDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitOperatorDeclaration (ICSharpCode.OldNRefactory.Ast.OperatorDeclaration operatorDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitOnErrorStatement (ICSharpCode.OldNRefactory.Ast.OnErrorStatement onErrorStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitNamespaceDeclaration (ICSharpCode.OldNRefactory.Ast.NamespaceDeclaration namespaceDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitNamedArgumentExpression (ICSharpCode.OldNRefactory.Ast.NamedArgumentExpression namedArgumentExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitMethodDeclaration (ICSharpCode.OldNRefactory.Ast.MethodDeclaration methodDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitLockStatement (ICSharpCode.OldNRefactory.Ast.LockStatement lockStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitLocalVariableDeclaration (ICSharpCode.OldNRefactory.Ast.LocalVariableDeclaration localVariableDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitLabelStatement (ICSharpCode.OldNRefactory.Ast.LabelStatement labelStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitInterfaceImplementation (ICSharpCode.OldNRefactory.Ast.InterfaceImplementation interfaceImplementation, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitIndexerDeclaration (ICSharpCode.OldNRefactory.Ast.IndexerDeclaration indexerDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitIfElseStatement (ICSharpCode.OldNRefactory.Ast.IfElseStatement ifElseStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitGotoStatement (ICSharpCode.OldNRefactory.Ast.GotoStatement gotoStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitGotoCaseStatement (ICSharpCode.OldNRefactory.Ast.GotoCaseStatement gotoCaseStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitForStatement (ICSharpCode.OldNRefactory.Ast.ForStatement forStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitForNextStatement (ICSharpCode.OldNRefactory.Ast.ForNextStatement forNextStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitForeachStatement (ICSharpCode.OldNRefactory.Ast.ForeachStatement foreachStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitFixedStatement (ICSharpCode.OldNRefactory.Ast.FixedStatement fixedStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitFieldDeclaration (ICSharpCode.OldNRefactory.Ast.FieldDeclaration fieldDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitExpressionStatement (ICSharpCode.OldNRefactory.Ast.ExpressionStatement expressionStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitExitStatement (ICSharpCode.OldNRefactory.Ast.ExitStatement exitStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitEventRemoveRegion (ICSharpCode.OldNRefactory.Ast.EventRemoveRegion eventRemoveRegion, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitEventRaiseRegion (ICSharpCode.OldNRefactory.Ast.EventRaiseRegion eventRaiseRegion, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitEventDeclaration (ICSharpCode.OldNRefactory.Ast.EventDeclaration eventDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitEventAddRegion (ICSharpCode.OldNRefactory.Ast.EventAddRegion eventAddRegion, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitErrorStatement (ICSharpCode.OldNRefactory.Ast.ErrorStatement errorStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitEraseStatement (ICSharpCode.OldNRefactory.Ast.EraseStatement eraseStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitEndStatement (ICSharpCode.OldNRefactory.Ast.EndStatement endStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitEmptyStatement (ICSharpCode.OldNRefactory.Ast.EmptyStatement emptyStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitElseIfSection (ICSharpCode.OldNRefactory.Ast.ElseIfSection elseIfSection, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitDoLoopStatement (ICSharpCode.OldNRefactory.Ast.DoLoopStatement doLoopStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitDirectionExpression (ICSharpCode.OldNRefactory.Ast.DirectionExpression directionExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitDestructorDeclaration (ICSharpCode.OldNRefactory.Ast.DestructorDeclaration destructorDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitDelegateDeclaration (ICSharpCode.OldNRefactory.Ast.DelegateDeclaration delegateDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitDefaultValueExpression (ICSharpCode.OldNRefactory.Ast.DefaultValueExpression defaultValueExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitDeclareDeclaration (ICSharpCode.OldNRefactory.Ast.DeclareDeclaration declareDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitContinueStatement (ICSharpCode.OldNRefactory.Ast.ContinueStatement continueStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitConstructorInitializer (ICSharpCode.OldNRefactory.Ast.ConstructorInitializer constructorInitializer, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitConstructorDeclaration (ICSharpCode.OldNRefactory.Ast.ConstructorDeclaration constructorDeclaration, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitCompilationUnit (ICSharpCode.OldNRefactory.Ast.CompilationUnit compilationUnit, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitCheckedStatement (ICSharpCode.OldNRefactory.Ast.CheckedStatement checkedStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitCatchClause (ICSharpCode.OldNRefactory.Ast.CatchClause catchClause, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitCaseLabel (ICSharpCode.OldNRefactory.Ast.CaseLabel caseLabel, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitBreakStatement (ICSharpCode.OldNRefactory.Ast.BreakStatement breakStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitAttributeSection (ICSharpCode.OldNRefactory.Ast.AttributeSection attributeSection, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitAttribute (ICSharpCode.OldNRefactory.Ast.Attribute attribute, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitAnonymousMethodExpression (ICSharpCode.OldNRefactory.Ast.AnonymousMethodExpression anonymousMethodExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitAddressOfExpression (ICSharpCode.OldNRefactory.Ast.AddressOfExpression addressOfExpression, object data)
    {
        throw CreateNotSupportedError ();
    }

    public override object VisitAddHandlerStatement (ICSharpCode.OldNRefactory.Ast.AddHandlerStatement addHandlerStatement, object data)
    {
        throw CreateNotSupportedError ();
    }

    #endregion

    Exception CreateParseError (string message, params object[] args)
    {
        return new EvaluatorException (message, args);
    }

    Exception CreateNotSupportedError ()
    {
        return new NotSupportedExpressionException ();
    }
}
}
