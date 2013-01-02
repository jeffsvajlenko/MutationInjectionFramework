/*
  Copyright (C) 2009-2010 Jeroen Frijters

  This software is provided 'as-is', without any express or implied
  warranty.  In no event will the authors be held liable for any damages
  arising from the use of this software.

  Permission is granted to anyone to use this software for any purpose,
  including commercial applications, and to alter it and redistribute it
  freely, subject to the following restrictions:

  1. The origin of this software must not be misrepresented; you must not
     claim that you wrote the original software. If you use this software
     in a product, an acknowledgment in the product documentation would be
     appreciated but is not required.
  2. Altered source versions must be plainly marked as such, and must not be
     misrepresented as being the original software.
  3. This notice may not be removed or altered from any source distribution.

  Jeroen Frijters
  jeroen@frijters.net

*/
using System;
using System.IO;
using System.Text;
using System.Collections.Generic;
using IKVM.Reflection.Metadata;

namespace IKVM.Reflection.Reader
{
sealed class StreamHeader
{
    internal uint Offset;
    internal uint Size;
    internal string Name;

    internal void Read(BinaryReader br)
    {
        Offset = br.ReadUInt32();
        Size = br.ReadUInt32();
        byte[] buf = new byte[32];
        byte b;
        int len = 0;
        while ((b = br.ReadByte()) != 0)
        {
            buf[len++] = b;
        }
        Name = Encoding.UTF8.GetString(buf, 0, len); ;
        int padding = -1 + ((len + 4) & ~3) - len;
        br.BaseStream.Seek(padding, SeekOrigin.Current);
    }
}

sealed class ModuleReader : Module
{
    internal readonly Stream stream;
    private readonly string location;
    private Assembly assembly;
    private readonly PEReader peFile = new PEReader();
    private readonly CliHeader cliHeader = new CliHeader();
    private string imageRuntimeVersion;
    private int metadataStreamVersion;
    private byte[] stringHeap;
    private byte[] blobHeap;
    private byte[] userStringHeap;
    private byte[] guidHeap;
    private TypeDefImpl[] typeDefs;
    private TypeDefImpl moduleType;
    private Assembly[] assemblyRefs;
    private Type[] typeRefs;
    private Type[] typeSpecs;
    private FieldInfo[] fields;
    private MethodBase[] methods;
    private MemberInfo[] memberRefs;
    private Dictionary<int, string> strings = new Dictionary<int, string>();
    private Dictionary<string, Type> types = new Dictionary<string, Type>();
    private Dictionary<string, LazyForwardedType> forwardedTypes = new Dictionary<string, LazyForwardedType>();

    private sealed class LazyForwardedType
    {
        private readonly int assemblyRef;
        private Type type;

        internal LazyForwardedType(int assemblyRef)
        {
            this.assemblyRef = assemblyRef;
        }

        internal Type GetType(ModuleReader module, string typeName)
        {
            if (type == null)
            {
                Assembly asm = module.ResolveAssemblyRef(assemblyRef);
                type = asm.GetType(typeName, true);
            }
            return type;
        }
    }

    internal ModuleReader(AssemblyReader assembly, Universe universe, Stream stream, string location)
    : base(universe)
    {
        this.stream = stream;
        this.location = location;
        Read();
        if (assembly == null && AssemblyTable.records.Length != 0)
        {
            assembly = new AssemblyReader(location, this);
        }
        this.assembly = assembly;
    }

    private void Read()
    {
        BinaryReader br = new BinaryReader(stream);
        peFile.Read(br);
        stream.Seek(peFile.RvaToFileOffset(peFile.GetComDescriptorVirtualAddress()), SeekOrigin.Begin);
        cliHeader.Read(br);
        stream.Seek(peFile.RvaToFileOffset(cliHeader.MetaDataRVA), SeekOrigin.Begin);
        foreach (StreamHeader sh in ReadStreamHeaders(br, out imageRuntimeVersion))
        {
            switch (sh.Name)
            {
            case "#Strings":
                stringHeap = ReadHeap(stream, sh);
                break;
            case "#Blob":
                blobHeap = ReadHeap(stream, sh);
                break;
            case "#US":
                userStringHeap = ReadHeap(stream, sh);
                break;
            case "#GUID":
                guidHeap = ReadHeap(stream, sh);
                break;
            case "#~":
                stream.Seek(peFile.RvaToFileOffset(cliHeader.MetaDataRVA + sh.Offset), SeekOrigin.Begin);
                ReadTables(br);
                break;
            default:
                throw new BadImageFormatException("Unsupported stream: " + sh.Name);
            }
        }
    }

    internal void SetAssembly(Assembly assembly)
    {
        this.assembly = assembly;
    }

    private static StreamHeader[] ReadStreamHeaders(BinaryReader br, out string Version)
    {
        uint Signature = br.ReadUInt32();
        if (Signature != 0x424A5342)
        {
            throw new BadImageFormatException("Invalid metadata signature");
        }
        ushort MajorVersion = br.ReadUInt16();
        ushort MinorVersion = br.ReadUInt16();
        uint Reserved = br.ReadUInt32();
        uint Length = br.ReadUInt32();
        byte[] buf = br.ReadBytes((int)Length);
        Version = Encoding.UTF8.GetString(buf).TrimEnd('\u0000');
        ushort Flags = br.ReadUInt16();
        ushort Streams = br.ReadUInt16();
        StreamHeader[] streamHeaders = new StreamHeader[Streams];
        for (int i = 0; i < streamHeaders.Length; i++)
        {
            streamHeaders[i] = new StreamHeader();
            streamHeaders[i].Read(br);
        }
        return streamHeaders;
    }

    private void ReadTables(BinaryReader br)
    {
        Table[] tables = GetTables();
        uint Reserved0 = br.ReadUInt32();
        byte MajorVersion = br.ReadByte();
        byte MinorVersion = br.ReadByte();
        metadataStreamVersion = MajorVersion << 16 | MinorVersion;
        byte HeapSizes = br.ReadByte();
        byte Reserved7 = br.ReadByte();
        ulong Valid = br.ReadUInt64();
        ulong Sorted = br.ReadUInt64();
        for (int i = 0; i < 64; i++)
        {
            if ((Valid & (1UL << i)) != 0)
            {
                tables[i].RowCount = br.ReadInt32();
            }
            else if (tables[i] != null)
            {
                tables[i].RowCount = 0;
            }
        }
        MetadataReader mr = new MetadataReader(this, br, HeapSizes);
        for (int i = 0; i < 64; i++)
        {
            if ((Valid & (1UL << i)) != 0)
            {
                tables[i].Read(mr);
            }
        }
    }

    private byte[] ReadHeap(Stream stream, StreamHeader sh)
    {
        byte[] buf = new byte[sh.Size];
        stream.Seek(peFile.RvaToFileOffset(cliHeader.MetaDataRVA + sh.Offset), SeekOrigin.Begin);
        for (int pos = 0; pos < buf.Length; )
        {
            int read = stream.Read(buf, pos, buf.Length - pos);
            if (read == 0)
            {
                throw new BadImageFormatException();
            }
            pos += read;
        }
        return buf;
    }

    internal void SeekRVA(int rva)
    {
        stream.Seek(peFile.RvaToFileOffset((uint)rva), SeekOrigin.Begin);
    }

    internal override void GetTypesImpl(List<Type> list)
    {
        PopulateTypeDef();
        foreach (TypeDefImpl type in typeDefs)
        {
            if (type != moduleType)
            {
                list.Add(type);
            }
        }
    }

    private void PopulateTypeDef()
    {
        if (typeDefs == null)
        {
            typeDefs = new TypeDefImpl[TypeDef.records.Length];
            for (int i = 0; i < typeDefs.Length; i++)
            {
                TypeDefImpl type = new TypeDefImpl(this, i);
                typeDefs[i] = type;
                if (type.IsModulePseudoType)
                {
                    moduleType = type;
                }
                else
                {
                    types.Add(type.FullName, type);
                }
            }
            // add forwarded types to forwardedTypes dictionary (because Module.GetType(string) should return them)
            for (int i = 0; i < ExportedType.records.Length; i++)
            {
                int implementation = ExportedType.records[i].Implementation;
                if (implementation >> 24 == AssemblyRefTable.Index)
                {
                    string typeName = GetTypeName(ExportedType.records[i].TypeNamespace, ExportedType.records[i].TypeName);
                    forwardedTypes.Add(typeName, new LazyForwardedType((implementation & 0xFFFFFF) - 1));
                }
            }
        }
    }

    internal string GetString(int index)
    {
        if (index == 0)
        {
            return null;
        }
        string str;
        if (!strings.TryGetValue(index, out str))
        {
            int len = 0;
            while (stringHeap[index + len] != 0)
            {
                len++;
            }
            str = Encoding.UTF8.GetString(stringHeap, index, len);
            strings.Add(index, str);
        }
        return str;
    }

    private static int ReadCompressedInt(byte[] buffer, ref int offset)
    {
        byte b1 = buffer[offset++];
        if (b1 <= 0x7F)
        {
            return b1;
        }
        else if ((b1 & 0xC0) == 0x80)
        {
            byte b2 = buffer[offset++];
            return ((b1 & 0x3F) << 8) | b2;
        }
        else
        {
            byte b2 = buffer[offset++];
            byte b3 = buffer[offset++];
            byte b4 = buffer[offset++];
            return ((b1 & 0x3F) << 24) + (b2 << 16) + (b3 << 8) + b4;
        }
    }

    internal byte[] GetBlobCopy(int blobIndex)
    {
        int len = ReadCompressedInt(blobHeap, ref blobIndex);
        byte[] buf = new byte[len];
        Buffer.BlockCopy(blobHeap, blobIndex, buf, 0, len);
        return buf;
    }

    internal override ByteReader GetBlob(int blobIndex)
    {
        return ByteReader.FromBlob(blobHeap, blobIndex);
    }

    public override string ResolveString(int metadataToken)
    {
        string str;
        if (!strings.TryGetValue(metadataToken, out str))
        {
            if ((metadataToken >> 24) != 0x70)
            {
                throw new ArgumentOutOfRangeException();
            }
            int index = metadataToken & 0xFFFFFF;
            int len = ReadCompressedInt(userStringHeap, ref index) & ~1;
            str = Encoding.Unicode.GetString(userStringHeap, index, len);
            strings.Add(metadataToken, str);
        }
        return str;
    }

    internal Type ResolveType(int metadataToken, IGenericContext context)
    {
        switch (metadataToken >> 24)
        {
        case TypeDefTable.Index:
            PopulateTypeDef();
            return typeDefs[(metadataToken & 0xFFFFFF) - 1];
        case TypeRefTable.Index:
        {
            if (typeRefs == null)
            {
                typeRefs = new Type[TypeRef.records.Length];
            }
            int index = (metadataToken & 0xFFFFFF) - 1;
            if (typeRefs[index] == null)
            {
                int scope = TypeRef.records[index].ResolutionScope;
                switch (scope >> 24)
                {
                case AssemblyRefTable.Index:
                {
                    Assembly assembly = ResolveAssemblyRef((scope & 0xFFFFFF) - 1);
                    string typeName = GetTypeName(TypeRef.records[index].TypeNameSpace, TypeRef.records[index].TypeName);
                    Type type = assembly.GetType(typeName);
                    if (type == null)
                    {
                        throw new TypeLoadException(String.Format("Type '{0}' not found in assembly '{1}'", typeName, assembly.FullName));
                    }
                    typeRefs[index] = type;
                    break;
                }
                case TypeRefTable.Index:
                {
                    Type outer = ResolveType(scope, null);
                    typeRefs[index] = outer.GetNestedType(GetString(TypeRef.records[index].TypeName), BindingFlags.Public | BindingFlags.NonPublic);
                    break;
                }
                case ModuleTable.Index:
                    if (scope != 0 && scope != 1)
                    {
                        throw new NotImplementedException("self reference scope?");
                    }
                    typeRefs[index] = GetType(GetTypeName(TypeRef.records[index].TypeNameSpace, TypeRef.records[index].TypeName));
                    break;
                case ModuleRefTable.Index:
                {
                    Module module = ResolveModuleRef(ModuleRef.records[(scope & 0xFFFFFF) - 1]);
                    string typeName = GetTypeName(TypeRef.records[index].TypeNameSpace, TypeRef.records[index].TypeName);
                    Type type = assembly.GetType(typeName);
                    if (type == null)
                    {
                        throw new TypeLoadException(String.Format("Type '{0}' not found in module '{1}'", typeName, module.Name));
                    }
                    typeRefs[index] = type;
                    break;
                }
                default:
                    throw new NotImplementedException("ResolutionScope = " + scope.ToString("X"));
                }
            }
            return typeRefs[index];
        }
        case TypeSpecTable.Index:
        {
            if (typeSpecs == null)
            {
                typeSpecs = new Type[TypeSpec.records.Length];
            }
            int index = (metadataToken & 0xFFFFFF) - 1;
            Type type = typeSpecs[index];
            if (type == null)
            {
                TrackingGenericContext tc = context == null ? null : new TrackingGenericContext(context);
                type = Signature.ReadTypeSpec(this, ByteReader.FromBlob(blobHeap, TypeSpec.records[index]), tc);
                if (tc == null || !tc.IsUsed)
                {
                    typeSpecs[index] = type;
                }
            }
            return type;
        }
        default:
            throw new NotImplementedException(String.Format("0x{0:X}", metadataToken));
        }
    }

    private Module ResolveModuleRef(int moduleNameIndex)
    {
        string moduleName = GetString(moduleNameIndex);
        Module module = assembly.GetModule(moduleName);
        if (module == null)
        {
            throw new FileNotFoundException(moduleName);
        }
        return module;
    }

    private sealed class TrackingGenericContext : IGenericContext
    {
        private readonly IGenericContext context;
        private bool used;

        internal TrackingGenericContext(IGenericContext context)
        {
            this.context = context;
        }

        internal bool IsUsed
        {
            get
            {
                return used;
            }
        }

        public Type GetGenericTypeArgument(int index)
        {
            used = true;
            return context.GetGenericTypeArgument(index);
        }

        public Type GetGenericMethodArgument(int index)
        {
            used = true;
            return context.GetGenericMethodArgument(index);
        }
    }

    public override Type ResolveType(int metadataToken, Type[] genericTypeArguments, Type[] genericMethodArguments)
    {
        if ((metadataToken >> 24) == TypeSpecTable.Index)
        {
            return ResolveType(metadataToken, new GenericContext(genericTypeArguments, genericMethodArguments));
        }
        else
        {
            return ResolveType(metadataToken, null);
        }
    }

    private string GetTypeName(int typeNamespace, int typeName)
    {
        if (typeNamespace == 0)
        {
            return GetString(typeName);
        }
        else
        {
            return GetString(typeNamespace) + "." + GetString(typeName);
        }
    }

    private Assembly ResolveAssemblyRef(int index)
    {
        if (assemblyRefs == null)
        {
            assemblyRefs = new Assembly[AssemblyRef.RowCount];
        }
        if (assemblyRefs[index] == null)
        {
            assemblyRefs[index] = ResolveAssemblyRefImpl(ref AssemblyRef.records[index]);
        }
        return assemblyRefs[index];
    }

    private Assembly ResolveAssemblyRefImpl(ref AssemblyRefTable.Record rec)
    {
        const int PublicKey = 0x0001;
        string name = String.Format("{0}, Version={1}.{2}.{3}.{4}, Culture={5}, {6}={7}",
                                    GetString(rec.Name),
                                    rec.MajorVersion,
                                    rec.MinorVersion,
                                    rec.BuildNumber,
                                    rec.RevisionNumber,
                                    rec.Culture == 0 ? "neutral" : GetString(rec.Culture),
                                    (rec.Flags & PublicKey) == 0 ? "PublicKeyToken" : "PublicKey",
                                    PublicKeyOrTokenToString(rec.PublicKeyOrToken));
        return universe.Load(name, this.Assembly, true);
    }

    private string PublicKeyOrTokenToString(int publicKeyOrToken)
    {
        if (publicKeyOrToken == 0)
        {
            return "null";
        }
        ByteReader br = GetBlob(publicKeyOrToken);
        if (br.Length == 0)
        {
            return "null";
        }
        StringBuilder sb = new StringBuilder(br.Length * 2);
        while (br.Length > 0)
        {
            sb.AppendFormat("{0:x2}", br.ReadByte());
        }
        return sb.ToString();
    }

    public override Guid ModuleVersionId
    {
        get
        {
            byte[] buf = new byte[16];
            Buffer.BlockCopy(guidHeap, 16 * (ModuleTable.records[0].Mvid - 1), buf, 0, 16);
            return new Guid(buf);
        }
    }

    public override string FullyQualifiedName
    {
        get
        {
            return location ?? "<Unknown>";
        }
    }

    public override string Name
    {
        get
        {
            return location == null ? "<Unknown>" : System.IO.Path.GetFileName(location);
        }
    }

    public override Assembly Assembly
    {
        get
        {
            return assembly;
        }
    }

    internal override Type GetTypeImpl(string typeName)
    {
        PopulateTypeDef();
        Type type;
        if (!types.TryGetValue(typeName, out type))
        {
            LazyForwardedType fw;
            if (forwardedTypes.TryGetValue(typeName, out fw))
            {
                return fw.GetType(this, typeName);
            }
        }
        return type;
    }

    public override MemberInfo ResolveMember(int metadataToken, Type[] genericTypeArguments, Type[] genericMethodArguments)
    {
        switch (metadataToken >> 24)
        {
        case FieldTable.Index:
            return ResolveField(metadataToken, genericTypeArguments, genericMethodArguments);
        case MemberRefTable.Index:
            return GetMemberRef((metadataToken & 0xFFFFFF) - 1, genericTypeArguments, genericMethodArguments);
        case MethodDefTable.Index:
        case MethodSpecTable.Index:
            return ResolveMethod(metadataToken, genericTypeArguments, genericMethodArguments);
        }
        throw new ArgumentOutOfRangeException();
    }

    internal FieldInfo GetFieldAt(TypeDefImpl owner, int index)
    {
        if (fields == null)
        {
            fields = new FieldInfo[Field.records.Length];
        }
        if (fields[index] == null)
        {
            fields[index] = new FieldDefImpl(this, owner ?? FindFieldOwner(index), index);
        }
        return fields[index];
    }

    public override FieldInfo ResolveField(int metadataToken, Type[] genericTypeArguments, Type[] genericMethodArguments)
    {
        if ((metadataToken >> 24) == FieldTable.Index)
        {
            int index = (metadataToken & 0xFFFFFF) - 1;
            return GetFieldAt(null, index);
        }
        else if ((metadataToken >> 24) == MemberRefTable.Index)
        {
            FieldInfo field = GetMemberRef((metadataToken & 0xFFFFFF) - 1, genericTypeArguments, genericMethodArguments) as FieldInfo;
            if (field != null)
            {
                return field;
            }
        }
        throw new ArgumentOutOfRangeException();
    }

    private TypeDefImpl FindFieldOwner(int fieldIndex)
    {
        // TODO use binary search?
        for (int i = 0; i < TypeDef.records.Length; i++)
        {
            int field = TypeDef.records[i].FieldList - 1;
            int end = TypeDef.records.Length > i + 1 ? TypeDef.records[i + 1].FieldList - 1 : Field.records.Length;
            if (field <= fieldIndex && fieldIndex < end)
            {
                PopulateTypeDef();
                return typeDefs[i];
            }
        }
        throw new InvalidOperationException();
    }

    internal MethodBase GetMethodAt(TypeDefImpl owner, int index)
    {
        if (methods == null)
        {
            methods = new MethodBase[MethodDef.records.Length];
        }
        if (methods[index] == null)
        {
            MethodDefImpl method = new MethodDefImpl(this, owner ?? FindMethodOwner(index), index);
            methods[index] = method.IsConstructor ? new ConstructorInfoImpl(method) : (MethodBase)method;
        }
        return methods[index];
    }

    private sealed class GenericContext : IGenericContext
    {
        private readonly Type[] genericTypeArguments;
        private readonly Type[] genericMethodArguments;

        internal GenericContext(Type[] genericTypeArguments, Type[] genericMethodArguments)
        {
            this.genericTypeArguments = genericTypeArguments;
            this.genericMethodArguments = genericMethodArguments;
        }

        public Type GetGenericTypeArgument(int index)
        {
            return genericTypeArguments[index];
        }

        public Type GetGenericMethodArgument(int index)
        {
            return genericMethodArguments[index];
        }
    }

    public override MethodBase ResolveMethod(int metadataToken, Type[] genericTypeArguments, Type[] genericMethodArguments)
    {
        if ((metadataToken >> 24) == MethodDefTable.Index)
        {
            int index = (metadataToken & 0xFFFFFF) - 1;
            return GetMethodAt(null, index);
        }
        else if ((metadataToken >> 24) == MemberRefTable.Index)
        {
            int index = (metadataToken & 0xFFFFFF) - 1;
            MethodBase method = GetMemberRef(index, genericTypeArguments, genericMethodArguments) as MethodBase;
            if (method != null)
            {
                return method;
            }
        }
        else if ((metadataToken >> 24) == MethodSpecTable.Index)
        {
            int index = (metadataToken & 0xFFFFFF) - 1;
            MethodInfo method = (MethodInfo)ResolveMethod(MethodSpec.records[index].Method, genericTypeArguments, genericMethodArguments);
            ByteReader instantiation = ByteReader.FromBlob(blobHeap, MethodSpec.records[index].Instantiation);
            return method.MakeGenericMethod(Signature.ReadMethodSpec(this, instantiation, new GenericContext(genericTypeArguments, genericMethodArguments)));
        }
        throw new ArgumentOutOfRangeException();
    }

    public override Type[] __ResolveOptionalParameterTypes(int metadataToken)
    {
        if ((metadataToken >> 24) == MemberRefTable.Index)
        {
            int index = (metadataToken & 0xFFFFFF) - 1;
            int sig = MemberRef.records[index].Signature;
            return Signature.ReadOptionalParameterTypes(this, GetBlob(sig));
        }
        else if ((metadataToken >> 24) == MethodDefTable.Index)
        {
            // for convenience, we support passing a MethodDef token as well, because in some places
            // it makes sense to have a vararg method that is referred to by its methoddef (e.g. ldftn).
            // Note that MethodSpec doesn't make sense, because generic methods cannot be vararg.
            return Type.EmptyTypes;
        }
        throw new ArgumentOutOfRangeException();
    }

    public override string ScopeName
    {
        get
        {
            return GetString(ModuleTable.records[0].Name);
        }
    }

    private TypeDefImpl FindMethodOwner(int methodIndex)
    {
        // TODO use binary search?
        for (int i = 0; i < TypeDef.records.Length; i++)
        {
            int method = TypeDef.records[i].MethodList - 1;
            int end = TypeDef.records.Length > i + 1 ? TypeDef.records[i + 1].MethodList - 1 : MethodDef.records.Length;
            if (method <= methodIndex && methodIndex < end)
            {
                PopulateTypeDef();
                return typeDefs[i];
            }
        }
        throw new InvalidOperationException();
    }

    private MemberInfo GetMemberRef(int index, Type[] genericTypeArguments, Type[] genericMethodArguments)
    {
        if (memberRefs == null)
        {
            memberRefs = new MemberInfo[MemberRef.records.Length];
        }
        if (memberRefs[index] == null)
        {
            int owner = MemberRef.records[index].Class;
            int sig = MemberRef.records[index].Signature;
            string name = GetString(MemberRef.records[index].Name);
            switch (owner >> 24)
            {
            case MethodDefTable.Index:
                return GetMethodAt(null, (owner & 0xFFFFFF) - 1);
            case ModuleRefTable.Index:
                memberRefs[index] = ResolveTypeMemberRef(ResolveModuleType(owner), name, ByteReader.FromBlob(blobHeap, sig), genericTypeArguments, genericMethodArguments);
                break;
            case TypeDefTable.Index:
            case TypeRefTable.Index:
                memberRefs[index] = ResolveTypeMemberRef(ResolveType(owner), name, ByteReader.FromBlob(blobHeap, sig), genericTypeArguments, genericMethodArguments);
                break;
            case TypeSpecTable.Index:
                return ResolveTypeMemberRef(ResolveType(owner, genericTypeArguments, genericMethodArguments), name, ByteReader.FromBlob(blobHeap, sig), genericTypeArguments, genericMethodArguments);
            default:
                throw new BadImageFormatException();
            }
        }
        return memberRefs[index];
    }

    private Type ResolveModuleType(int token)
    {
        int index = (token & 0xFFFFFF) - 1;
        string name = GetString(ModuleRef.records[index]);
        Module module = assembly.GetModule(name);
        if (module == null || module.IsResource())
        {
            throw new BadImageFormatException();
        }
        return module.GetModuleType();
    }

    private MemberInfo ResolveTypeMemberRef(Type type, string name, ByteReader sig, Type[] genericTypeArguments, Type[] genericMethodArguments)
    {
        IGenericContext context;
        if ((genericTypeArguments == null && genericMethodArguments == null) || type.IsGenericType)
        {
            context = type;
        }
        else
        {
            context = new GenericContext(genericTypeArguments, genericMethodArguments);
        }
        if (sig.PeekByte() == Signature.FIELD)
        {
            Type org = type;
            FieldSignature fieldSig = FieldSignature.ReadSig(this, sig, context);
            do
            {
                FieldInfo field = type.FindField(name, fieldSig);
                if (field != null)
                {
                    return field;
                }
                type = type.BaseType;
            }
            while (type != null);
            throw new MissingFieldException(org.ToString(), name);
        }
        else
        {
            Type org = type;
            MethodSignature methodSig = MethodSignature.ReadSig(this, sig, context);
            do
            {
                MethodBase method = type.FindMethod(name, methodSig);
                if (method != null)
                {
                    return method;
                }
                type = type.BaseType;
            }
            while (type != null);
            throw new MissingMethodException(org.ToString(), name);
        }
    }

    internal new ByteReader ResolveSignature(int metadataToken)
    {
        if ((metadataToken >> 24) == StandAloneSigTable.Index)
        {
            int index = (metadataToken & 0xFFFFFF) - 1;
            return ByteReader.FromBlob(blobHeap, StandAloneSig.records[index]);
        }
        throw new ArgumentOutOfRangeException();
    }

    public override __StandAloneMethodSig __ResolveStandAloneMethodSig(int metadataToken, Type[] genericTypeArguments, Type[] genericMethodArguments)
    {
        return MethodSignature.ReadStandAloneMethodSig(this, ResolveSignature(metadataToken), new GenericContext(genericTypeArguments, genericMethodArguments));
    }

    internal MethodInfo GetEntryPoint()
    {
        if (cliHeader.EntryPointToken != 0 && (cliHeader.Flags & CliHeader.COMIMAGE_FLAGS_NATIVE_ENTRYPOINT) == 0)
        {
            return (MethodInfo)ResolveMethod((int)cliHeader.EntryPointToken);
        }
        return null;
    }

    internal string[] GetManifestResourceNames()
    {
        string[] names = new string[ManifestResource.records.Length];
        for (int i = 0; i < ManifestResource.records.Length; i++)
        {
            names[i] = GetString(ManifestResource.records[i].Name);
        }
        return names;
    }

    internal ManifestResourceInfo GetManifestResourceInfo(string resourceName)
    {
        for (int i = 0; i < ManifestResource.records.Length; i++)
        {
            if (resourceName == GetString(ManifestResource.records[i].Name))
            {
                return new ManifestResourceInfo(this, i);
            }
        }
        return null;
    }

    internal Stream GetManifestResourceStream(string resourceName)
    {
        for (int i = 0; i < ManifestResource.records.Length; i++)
        {
            if (resourceName == GetString(ManifestResource.records[i].Name))
            {
                if (ManifestResource.records[i].Implementation != 0x26000000)
                {
                    throw new NotImplementedException();
                }
                SeekRVA((int)cliHeader.ResourcesRVA + ManifestResource.records[i].Offset);
                BinaryReader br = new BinaryReader(stream);
                int length = br.ReadInt32();
                return new MemoryStream(br.ReadBytes(length));
            }
        }
        throw new FileNotFoundException();
    }

    public override AssemblyName[] __GetReferencedAssemblies()
    {
        List<AssemblyName> list = new List<AssemblyName>();
        for (int i = 0; i < AssemblyRef.records.Length; i++)
        {
            AssemblyName name = new AssemblyName();
            name.Name = GetString(AssemblyRef.records[i].Name);
            name.Version = new Version(
                AssemblyRef.records[i].MajorVersion,
                AssemblyRef.records[i].MinorVersion,
                AssemblyRef.records[i].BuildNumber,
                AssemblyRef.records[i].RevisionNumber);
            if (AssemblyRef.records[i].PublicKeyOrToken != 0)
            {
                byte[] keyOrToken = GetBlobCopy(AssemblyRef.records[i].PublicKeyOrToken);
                const int PublicKey = 0x0001;
                if ((AssemblyRef.records[i].Flags & PublicKey) != 0)
                {
                    name.SetPublicKey(keyOrToken);
                }
                else
                {
                    name.SetPublicKeyToken(keyOrToken);
                }
            }
            if (AssemblyRef.records[i].Culture != 0)
            {
                name.CultureInfo = new System.Globalization.CultureInfo(GetString(AssemblyRef.records[i].Culture));
            }
            else
            {
                name.CultureInfo = System.Globalization.CultureInfo.InvariantCulture;
            }
            name.Flags = (AssemblyNameFlags)AssemblyRef.records[i].Flags;
            list.Add(name);
        }
        return list.ToArray();
    }

    internal override Type GetModuleType()
    {
        PopulateTypeDef();
        return moduleType;
    }

    internal string ImageRuntimeVersion
    {
        get
        {
            return imageRuntimeVersion;
        }
    }

    public override int MDStreamVersion
    {
        get
        {
            return metadataStreamVersion;
        }
    }

    public override void __GetDataDirectoryEntry(int index, out int rva, out int length)
    {
        peFile.GetDataDirectoryEntry(index, out rva, out length);
    }

    public override long __RelativeVirtualAddressToFileOffset(int rva)
    {
        return peFile.RvaToFileOffset((uint)rva);
    }

    public override void GetPEKind(out PortableExecutableKinds peKind, out ImageFileMachine machine)
    {
        peKind = 0;
        if ((cliHeader.Flags & CliHeader.COMIMAGE_FLAGS_ILONLY) != 0)
        {
            peKind |= PortableExecutableKinds.ILOnly;
        }
        if ((cliHeader.Flags & CliHeader.COMIMAGE_FLAGS_32BITREQUIRED) != 0)
        {
            peKind |= PortableExecutableKinds.Required32Bit;
        }
        if (peFile.OptionalHeader.Magic == IMAGE_OPTIONAL_HEADER.IMAGE_NT_OPTIONAL_HDR64_MAGIC)
        {
            peKind |= PortableExecutableKinds.PE32Plus;
        }

        machine = (ImageFileMachine)peFile.FileHeader.Machine;
    }

    public override int __Subsystem
    {
        get
        {
            return peFile.OptionalHeader.Subsystem;
        }
    }

    public override IList<CustomAttributeData> __GetPlaceholderAssemblyCustomAttributes(bool multiple, bool security)
    {
        string typeName;
        switch ((multiple ? 1 : 0) + (security ? 2 : 0))
        {
        case 0:
            typeName = "System.Runtime.CompilerServices.AssemblyAttributesGoHere";
            break;
        case 1:
            typeName = "System.Runtime.CompilerServices.AssemblyAttributesGoHereM";
            break;
        case 2:
            typeName = "System.Runtime.CompilerServices.AssemblyAttributesGoHereS";
            break;
        case 3:
        default:
            typeName = "System.Runtime.CompilerServices.AssemblyAttributesGoHereSM";
            break;
        }
        List<CustomAttributeData> list = new List<CustomAttributeData>();
        for (int i = 0; i < CustomAttribute.records.Length; i++)
        {
            if ((CustomAttribute.records[i].Parent >> 24) == TypeRefTable.Index)
            {
                int index = (CustomAttribute.records[i].Parent & 0xFFFFFF) - 1;
                if (typeName == GetTypeName(TypeRef.records[index].TypeNameSpace, TypeRef.records[index].TypeName))
                {
                    ConstructorInfo constructor = (ConstructorInfo)ResolveMethod(CustomAttribute.records[i].Type);
                    list.Add(new CustomAttributeData(this.Assembly, constructor, GetBlob(CustomAttribute.records[i].Value)));
                }
            }
        }
        return list;
    }

    internal override void Dispose()
    {
        stream.Close();
    }

    internal override void ExportTypes(int fileToken, IKVM.Reflection.Emit.ModuleBuilder manifestModule)
    {
        PopulateTypeDef();
        manifestModule.ExportTypes(typeDefs, fileToken);
    }
}
}
