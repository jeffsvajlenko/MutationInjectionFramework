//
// enum.cs: Enum handling.
//
// Author: Miguel de Icaza (miguel@gnu.org)
//         Ravi Pratap     (ravi@ximian.com)
//         Marek Safar     (marek.safar@seznam.cz)
//
// Dual licensed under the terms of the MIT X11 or GNU GPL
//
// Copyright 2001 Ximian, Inc (http://www.ximian.com)
// Copyright 2003-2003 Novell, Inc (http://www.novell.com)
//

using System;

#if STATIC
using MetaType = IKVM.Reflection.Type;
using IKVM.Reflection;
#else
using MetaType = System.Type;
using System.Reflection;
#endif

namespace Mono.CSharp
{

public class EnumMember : Const
{
    class EnumTypeExpr : TypeExpr
    {
        protected override TypeExpr DoResolveAsTypeStep (IMemberContext ec)
        {
            type = ec.CurrentType;
            return this;
        }

        public override TypeExpr ResolveAsTypeTerminal (IMemberContext ec, bool silent)
        {
            return DoResolveAsTypeStep (ec);
        }
    }

    public EnumMember (Enum parent, MemberName name, Attributes attrs)
    : base (parent, new EnumTypeExpr (), Modifiers.PUBLIC, name, attrs)
    {
    }

    static bool IsValidEnumType (TypeSpec t)
    {
        return (t == TypeManager.int32_type || t == TypeManager.uint32_type || t == TypeManager.int64_type ||
                t == TypeManager.byte_type || t == TypeManager.sbyte_type || t == TypeManager.short_type ||
                t == TypeManager.ushort_type || t == TypeManager.uint64_type || t == TypeManager.char_type ||
                TypeManager.IsEnumType (t));
    }

    public override Constant ConvertInitializer (ResolveContext rc, Constant expr)
    {
        if (expr is EnumConstant)
            expr = ((EnumConstant) expr).Child;

        var underlying = ((Enum) Parent).UnderlyingType;
        if (expr != null)
        {
            expr = expr.ImplicitConversionRequired (rc, underlying, Location);
            if (expr != null && !IsValidEnumType (expr.Type))
            {
                Enum.Error_1008 (Location, Report);
                expr = null;
            }
        }

        if (expr == null)
            expr = New.Constantify (underlying, Location);

        return new EnumConstant (expr, MemberType).Resolve (rc);
    }

    public override bool Define ()
    {
        if (!ResolveMemberType ())
            return false;

        const FieldAttributes attr = FieldAttributes.Public | FieldAttributes.Static | FieldAttributes.Literal;
        FieldBuilder = Parent.TypeBuilder.DefineField (Name, MemberType.GetMetaInfo (), attr);
        spec = new ConstSpec (Parent.Definition, this, MemberType, FieldBuilder, ModFlags, initializer);

        Parent.MemberCache.AddMember (spec);
        return true;
    }
}

/// <summary>
///   Enumeration container
/// </summary>
public class Enum : TypeContainer
{
    //
    // Implicit enum member initializer, used when no constant value is provided
    //
    class ImplicitInitializer : Expression
    {
        readonly EnumMember prev;
        readonly EnumMember current;

        public ImplicitInitializer (EnumMember current, EnumMember prev)
        {
            this.current = current;
            this.prev = prev;
        }

        public override Expression CreateExpressionTree (ResolveContext ec)
        {
            throw new NotSupportedException ("Missing Resolve call");
        }

        protected override Expression DoResolve (ResolveContext rc)
        {
            // We are the first member
            if (prev == null)
            {
                return New.Constantify (current.Parent.Definition, Location).Resolve (rc);
            }

            var c = ((ConstSpec) prev.Spec).GetConstant (rc) as EnumConstant;
            try
            {
                return c.Increment ().Resolve (rc);
            }
            catch (OverflowException)
            {
                rc.Report.Error (543, current.Location,
                                 "The enumerator value `{0}' is outside the range of enumerator underlying type `{1}'",
                                 current.GetSignatureForError (), ((Enum) current.Parent).UnderlyingType.GetSignatureForError ());

                return New.Constantify (current.Parent.Definition, current.Location).Resolve (rc);
            }
        }

        public override void Emit (EmitContext ec)
        {
            throw new NotSupportedException ("Missing Resolve call");
        }
    }

    public static readonly string UnderlyingValueField = "value__";

    const Modifiers AllowedModifiers =
        Modifiers.NEW |
        Modifiers.PUBLIC |
        Modifiers.PROTECTED |
        Modifiers.INTERNAL |
        Modifiers.PRIVATE;

    public Enum (NamespaceEntry ns, DeclSpace parent, TypeExpression type,
                 Modifiers mod_flags, MemberName name, Attributes attrs)
    : base (ns, parent, name, attrs, MemberKind.Enum)
    {
        base_type_expr = type;
        var accmods = IsTopLevel ? Modifiers.INTERNAL : Modifiers.PRIVATE;
        ModFlags = ModifiersExtensions.Check (AllowedModifiers, mod_flags, accmods, Location, Report);
        spec = new EnumSpec (null, this, null, null, ModFlags);
    }

    #region Properties

    public override AttributeTargets AttributeTargets
    {
        get
        {
            return AttributeTargets.Enum;
        }
    }

    public TypeExpr BaseTypeExpression
    {
        get
        {
            return base_type_expr;
        }
    }

    protected override TypeAttributes TypeAttr
    {
        get
        {
            return ModifiersExtensions.TypeAttr (ModFlags, IsTopLevel) |
                   TypeAttributes.Class | TypeAttributes.Sealed | base.TypeAttr;
        }
    }

    public TypeSpec UnderlyingType
    {
        get
        {
            return ((EnumSpec) spec).UnderlyingType;
        }
    }

    #endregion

    public void AddEnumMember (EnumMember em)
    {
        if (em.Name == UnderlyingValueField)
        {
            Report.Error (76, em.Location, "An item in an enumeration cannot have an identifier `{0}'",
                          UnderlyingValueField);
            return;
        }

        AddConstant (em);
    }

    public static void Error_1008 (Location loc, Report Report)
    {
        Report.Error (1008, loc,
                      "Type byte, sbyte, short, ushort, int, uint, long or ulong expected");
    }

    protected override bool DefineNestedTypes ()
    {
        ((EnumSpec) spec).UnderlyingType = base_type_expr == null ? TypeManager.int32_type : base_type_expr.Type;

        TypeBuilder.DefineField (UnderlyingValueField, UnderlyingType.GetMetaInfo (),
                                 FieldAttributes.Public | FieldAttributes.SpecialName | FieldAttributes.RTSpecialName);

        if (!RootContext.StdLib)
            Module.hack_corlib_enums.Add (this);

        return true;
    }

    protected override bool DoDefineMembers ()
    {
        if (constants != null)
        {
            for (int i = 0; i < constants.Count; ++i)
            {
                EnumMember em = (EnumMember) constants [i];
                if (em.Initializer == null)
                {
                    em.Initializer = new ImplicitInitializer (em, i == 0 ? null : (EnumMember) constants[i - 1]);
                }

                em.Define ();
            }
        }

        return true;
    }

    public override bool IsUnmanagedType ()
    {
        return true;
    }

    protected override TypeExpr[] ResolveBaseTypes (out TypeExpr base_class)
    {
        base_type = TypeManager.enum_type;
        base_class = base_type_expr;
        return null;
    }

    protected override bool VerifyClsCompliance ()
    {
        if (!base.VerifyClsCompliance ())
            return false;

        if (UnderlyingType == TypeManager.uint32_type ||
                UnderlyingType == TypeManager.uint64_type ||
                UnderlyingType == TypeManager.ushort_type)
        {
            Report.Warning (3009, 1, Location, "`{0}': base type `{1}' is not CLS-compliant", GetSignatureForError (), TypeManager.CSharpName (UnderlyingType));
        }

        return true;
    }
}

class EnumSpec : TypeSpec
{
    TypeSpec underlying;

    public EnumSpec (TypeSpec declaringType, ITypeDefinition definition, TypeSpec underlyingType, MetaType info, Modifiers modifiers)
    : base (MemberKind.Enum, declaringType, definition, info, modifiers | Modifiers.SEALED)
    {
        this.underlying = underlyingType;
    }

    public TypeSpec UnderlyingType
    {
        get
        {
            return underlying;
        }
        set
        {
            if (underlying != null)
                throw new InternalErrorException ("UnderlyingType reset");

            underlying = value;
        }
    }

    public static TypeSpec GetUnderlyingType (TypeSpec t)
    {
        return ((EnumSpec) t.GetDefinition ()).UnderlyingType;
    }
}
}
