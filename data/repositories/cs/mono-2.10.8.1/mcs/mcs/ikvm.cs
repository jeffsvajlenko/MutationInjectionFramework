//
// ikvm.cs: IKVM.Reflection and IKVM.Reflection.Emit specific implementations
//
// Author: Marek Safar (marek.safar@gmail.com)
//
// Dual licensed under the terms of the MIT X11 or GNU GPL
//
// Copyright 2009-2010 Novell, Inc.
//
//

using System;
using System.Collections.Generic;
using MetaType = IKVM.Reflection.Type;
using IKVM.Reflection;
using IKVM.Reflection.Emit;
using System.IO;
using System.Configuration.Assemblies;

namespace Mono.CSharp
{
#if !STATIC
public class StaticImporter
{
    public StaticImporter (BuildinTypes buildin)
    {
        throw new NotSupportedException ();
    }

    public void ImportAssembly (Assembly assembly, RootNamespace targetNamespace)
    {
        throw new NotSupportedException ();
    }

    public void ImportModule (Module module, RootNamespace targetNamespace)
    {
        throw new NotSupportedException ();
    }

    public TypeSpec ImportType (System.Type type)
    {
        throw new NotSupportedException ();
    }
}

#else

sealed class StaticImporter : MetadataImporter
{
    public StaticImporter ()
    {
    }

    protected override MemberKind DetermineKindFromBaseType (MetaType baseType)
    {
        string name = baseType.Name;

        if (name == "ValueType" && baseType.Namespace == "System")
            return MemberKind.Struct;

        if (name == "Enum" && baseType.Namespace == "System")
            return MemberKind.Enum;

        if (name == "MulticastDelegate" && baseType.Namespace == "System")
            return MemberKind.Delegate;

        return MemberKind.Class;
    }

    protected override bool HasVolatileModifier (MetaType[] modifiers)
    {
        foreach (var t in modifiers)
        {
            if (t.Name == "IsVolatile" && t.Namespace == CompilerServicesNamespace)
                return true;
        }

        return false;
    }

    public override void GetCustomAttributeTypeName (CustomAttributeData cad, out string typeNamespace, out string typeName)
    {
        cad.__ReadTypeName (out typeNamespace, out typeName);
    }

    public void ImportAssembly (Assembly assembly, RootNamespace targetNamespace)
    {
        // It can be used more than once when importing same assembly
        // into 2 or more global aliases
        var definition = GetAssemblyDefinition (assembly);

        var all_types = assembly.GetTypes ();
        ImportTypes (all_types, targetNamespace, definition.HasExtensionMethod);
    }

    public ImportedModuleDefinition ImportModule (Module module, RootNamespace targetNamespace)
    {
        var module_definition = new ImportedModuleDefinition (module, this);
        module_definition.ReadAttributes ();

        var all_types = module.GetTypes ();
        ImportTypes (all_types, targetNamespace, false);

        return module_definition;
    }

    public void InitializeBuildinTypes (BuildinTypes buildin, Assembly corlib)
    {
        //
        // Setup mapping for build-in types to avoid duplication of their definition
        //
        foreach (var type in buildin.AllTypes)
        {
            buildin_types.Add (corlib.GetType (type.FullName), type);
        }
    }
}
#endif

class AssemblyDefinitionStatic : AssemblyDefinition
{
    //
    // Assembly container with file output
    //
    public AssemblyDefinitionStatic (ModuleContainer module, string name, string fileName)
    : base (module, name, fileName)
    {
    }

    //
    // Initializes the code generator
    //
    public bool Create (StaticLoader loader)
    {
        ResolveAssemblySecurityAttributes ();
        var an = CreateAssemblyName ();

        Builder = loader.Domain.DefineDynamicAssembly (an, AssemblyBuilderAccess.Save, Path.GetDirectoryName (file_name));

        if (loader.Corlib != null)
        {
            Builder.__SetImageRuntimeVersion (loader.Corlib.ImageRuntimeVersion, 0x20000);
        }
        else
        {
            // Sets output file metadata version when there is no mscorlib
            switch (RootContext.StdLibRuntimeVersion)
            {
            case RuntimeVersion.v4:
                Builder.__SetImageRuntimeVersion ("v4.0.30319", 0x20000);
                break;
            case RuntimeVersion.v2:
                Builder.__SetImageRuntimeVersion ("v2.0.50727", 0x20000);
                break;
            case RuntimeVersion.v1:
                // Compiler does not do any checks whether the produced metadata
                // are valid in the context of 1.0 stream version
                Builder.__SetImageRuntimeVersion ("v1.1.4322", 0x10000);
                break;
            default:
                throw new NotImplementedException ();
            }
        }

        builder_extra = new AssemblyBuilderIKVM (Builder, Compiler);
        return true;
    }

    public Module IncludeModule (RawModule moduleFile)
    {
        return Builder.__AddModule (moduleFile);
    }

    protected override void SaveModule (PortableExecutableKinds pekind, ImageFileMachine machine)
    {
        module.Builder.__Save (pekind, machine);
    }
}

class StaticLoader : AssemblyReferencesLoader<Assembly>, IDisposable
{
    readonly StaticImporter importer;
    readonly Universe domain;
    Assembly corlib;
    List<Tuple<AssemblyName, string>> loaded_names;
    static readonly Dictionary<SdkVersion, string[]> sdk_directory;

    static StaticLoader ()
    {
        sdk_directory = new Dictionary<SdkVersion, string[]> ();
        sdk_directory.Add (SdkVersion.v2, new string[] { "2.0", "net_2_0", "v2.0.50727" });
        sdk_directory.Add (SdkVersion.v4, new string[] { "4.0", "net_4_0", "v4.0.30319" });
    }

    public StaticLoader (StaticImporter importer, CompilerContext compiler)
    : base (compiler)
    {
        this.importer = importer;
        domain = new Universe ();
        domain.AssemblyResolve += AssemblyReferenceResolver;
        loaded_names = new List<Tuple<AssemblyName, string>> ();

        var corlib_path = Path.GetDirectoryName (typeof (object).Assembly.Location);
        string fx_path = corlib_path.Substring (0, corlib_path.LastIndexOf (Path.DirectorySeparatorChar));
        string sdk_path = null;

        foreach (var dir in sdk_directory[RootContext.SdkVersion])
        {
            sdk_path = Path.Combine (fx_path, dir);
            if (File.Exists (Path.Combine (sdk_path, "mscorlib.dll")))
                break;

            sdk_path = null;
        }

        if (sdk_path == null)
        {
            compiler.Report.Warning (-1, 1, "SDK path could not be resolved");
            sdk_path = corlib_path;
        }

        paths.Add (sdk_path);
    }

    public Assembly Corlib
    {
        get
        {
            return corlib;
        }
        set
        {
            corlib = value;
        }
    }

    public Universe Domain
    {
        get
        {
            return domain;
        }
    }

    Assembly AssemblyReferenceResolver (object sender, IKVM.Reflection.ResolveEventArgs args)
    {
        var refname = args.Name;
        if (refname == "mscorlib")
            return corlib;

        Assembly version_mismatch = null;
        foreach (var assembly in domain.GetAssemblies ())
        {
            // TODO: Cannot handle unification into current assembly yet
            if (assembly is AssemblyBuilder)
                continue;

            AssemblyComparisonResult result;
            if (!Fusion.CompareAssemblyIdentityPure (refname, false, assembly.FullName, false, out result))
            {
                if ((result == AssemblyComparisonResult.NonEquivalentVersion || result == AssemblyComparisonResult.NonEquivalentPartialVersion) &&
                        (version_mismatch == null || version_mismatch.GetName ().Version < assembly.GetName ().Version))
                {
                    version_mismatch = assembly;
                }

                continue;
            }

            if (result == AssemblyComparisonResult.EquivalentFXUnified ||
                    result == AssemblyComparisonResult.EquivalentFullMatch ||
                    result == AssemblyComparisonResult.EquivalentWeakNamed ||
                    result == AssemblyComparisonResult.EquivalentPartialMatch)
            {
                return assembly;
            }

            throw new NotImplementedException ("Assembly equality = " + result.ToString ());
        }

        if (version_mismatch != null)
        {
            var v1 = new AssemblyName (refname).Version;
            var v2 = version_mismatch.GetName ().Version;

            if (v1 > v2)
            {
//					compiler.Report.SymbolRelatedToPreviousError (args.RequestingAssembly.Location);
                compiler.Report.Error (1705, "Assembly `{0}' references `{1}' which has higher version number than imported assembly `{2}'",
                                       args.RequestingAssembly.FullName, refname, version_mismatch.GetName ().FullName);
            }
            else if (v1.Major != v2.Major || v1.Minor != v2.Minor)
            {
                compiler.Report.Warning (1701, 2,
                                         "Assuming assembly reference `{0}' matches assembly `{1}'. You may need to supply runtime policy",
                                         refname, version_mismatch.GetName ().FullName);
            }
            else
            {
                compiler.Report.Warning (1702, 3,
                                         "Assuming assembly reference `{0}' matches assembly `{1}'. You may need to supply runtime policy",
                                         refname, version_mismatch.GetName ().FullName);
            }

            return version_mismatch;
        }

        // AssemblyReference has not been found in the domain
        // create missing reference and continue
        return new MissingAssembly (domain, args.Name);
    }

    public void Dispose ()
    {
        domain.Dispose ();
    }

    protected override string[] GetDefaultReferences ()
    {
        //
        // For now the "default config" is harcoded into the compiler
        // we can move this outside later
        //
        var default_references = new List<string> (4);

        default_references.Add ("System.dll");
        default_references.Add ("System.Xml.dll");

        if (RootContext.Version > LanguageVersion.ISO_2)
            default_references.Add ("System.Core.dll");
        if (RootContext.Version > LanguageVersion.V_3)
            default_references.Add ("Microsoft.CSharp.dll");

        return default_references.ToArray ();
    }

    public override bool HasObjectType (Assembly assembly)
    {
        return assembly.GetType (compiler.BuildinTypes.Object.FullName) != null;
    }

    public override Assembly LoadAssemblyFile (string fileName)
    {
        bool? has_extension = null;
        foreach (var path in paths)
        {
            var file = Path.Combine (path, fileName);
            if (!File.Exists (file))
            {
                if (!has_extension.HasValue)
                    has_extension = fileName.EndsWith (".dll", StringComparison.Ordinal) || fileName.EndsWith (".exe", StringComparison.Ordinal);

                if (has_extension.Value)
                    continue;

                file += ".dll";
                if (!File.Exists (file))
                    continue;
            }

            try
            {
                using (RawModule module = domain.OpenRawModule (file))
                {
                    if (!module.IsManifestModule)
                    {
                        Error_AssemblyIsModule (fileName);
                        return null;
                    }

                    //
                    // check whether the assembly can be actually imported without
                    // collision
                    //
                    var an = module.GetAssemblyName ();
                    foreach (var entry in loaded_names)
                    {
                        var loaded_name = entry.Item1;
                        if (an.Name != loaded_name.Name)
                            continue;

                        if (an.CodeBase == loaded_name.CodeBase)
                            return null;

                        if (((an.Flags | loaded_name.Flags) & AssemblyNameFlags.PublicKey) == 0)
                        {
                            compiler.Report.SymbolRelatedToPreviousError (entry.Item2);
                            compiler.Report.SymbolRelatedToPreviousError (fileName);
                            compiler.Report.Error (1704,
                                                   "An assembly with the same name `{0}' has already been imported. Consider removing one of the references or sign the assembly",
                                                   an.Name);
                            return null;
                        }

                        if ((an.Flags & AssemblyNameFlags.PublicKey) == (loaded_name.Flags & AssemblyNameFlags.PublicKey) && an.Version.Equals (loaded_name.Version))
                        {
                            compiler.Report.SymbolRelatedToPreviousError (entry.Item2);
                            compiler.Report.SymbolRelatedToPreviousError (fileName);
                            compiler.Report.Error (1703,
                                                   "An assembly with the same identity `{0}' has already been imported. Consider removing one of the references",
                                                   an.FullName);
                            return null;
                        }
                    }

                    if (Report.DebugFlags > 0)
                        Console.WriteLine ("Loading assembly `{0}'", fileName);

                    loaded_names.Add (Tuple.Create (an, fileName));
                    return domain.LoadAssembly (module);
                }
            }
            catch
            {
                Error_FileCorrupted (file);
                return null;
            }
        }

        Error_FileNotFound (fileName);
        return null;
    }

    public RawModule LoadModuleFile (string moduleName)
    {
        foreach (var path in paths)
        {
            var file = Path.Combine (path, moduleName);
            if (!File.Exists (file))
            {
                if (moduleName.EndsWith (".netmodule", StringComparison.Ordinal))
                    continue;

                file += ".netmodule";
                if (!File.Exists (file))
                    continue;
            }

            try
            {
                return domain.OpenRawModule (file);
            }
            catch
            {
                Error_FileCorrupted (file);
                return null;
            }
        }

        Error_FileNotFound (moduleName);
        return null;
    }

    //
    // Optimized default assembly loader version
    //
    public override Assembly LoadAssemblyDefault (string assembly)
    {
        foreach (var path in paths)
        {
            var file = Path.Combine (path, assembly);
            if (!File.Exists (file))
                continue;

            try
            {
                if (Report.DebugFlags > 0)
                    Console.WriteLine ("Loading default assembly `{0}'", file);

                var a = domain.LoadFile (file);
                if (a != null)
                {
                    loaded_names.Add (Tuple.Create (a.GetName (), assembly));
                }

                return a;
            }
            catch
            {
                // Default assemblies can fail to load without error
                return null;
            }
        }

        return null;
    }

    public override void LoadReferences (ModuleContainer module)
    {
        List<Tuple<RootNamespace, Assembly>> loaded;
        base.LoadReferencesCore (module, out corlib, out loaded);

        if (corlib != null)
        {
            importer.InitializeBuildinTypes (compiler.BuildinTypes, corlib);
            importer.ImportAssembly (corlib, module.GlobalRootNamespace);
        }

        foreach (var entry in loaded)
        {
            importer.ImportAssembly (entry.Item2, entry.Item1);
        }
    }

    public void LoadModules (AssemblyDefinitionStatic assembly, RootNamespace targetNamespace)
    {
        if (RootContext.Modules.Count == 0)
            return;

        foreach (var moduleName in RootContext.Modules)
        {
            var m = LoadModuleFile (moduleName);
            if (m == null)
                continue;

            if (m.IsManifestModule)
            {
                Error_FileCorrupted (moduleName);
                continue;
            }

            var md = importer.ImportModule (assembly.IncludeModule (m), targetNamespace);
            assembly.AddModule (md);
        }
    }
}

//
// Represents missing assembly reference
//
public class MissingAssembly : Assembly
{
    class MissingModule : Module
    {
        readonly Assembly assembly;

        public MissingModule (Universe universe, Assembly assembly)
        : base (universe)
        {
            this.assembly = assembly;
        }

        public override int MDStreamVersion
        {
            get
            {
                throw new NotImplementedException ();
            }
        }

        public override Assembly Assembly
        {
            get
            {
                return assembly;
            }
        }

        public override string FullyQualifiedName
        {
            get
            {
                throw new NotImplementedException ();
            }
        }

        public override string Name
        {
            get
            {
                throw new NotImplementedException ();
            }
        }

        public override Guid ModuleVersionId
        {
            get
            {
                throw new NotImplementedException ();
            }
        }

        public override MetaType ResolveType (int metadataToken, MetaType[] genericTypeArguments, MetaType[] genericMethodArguments)
        {
            throw new NotImplementedException ();
        }

        public override MethodBase ResolveMethod (int metadataToken, MetaType[] genericTypeArguments, MetaType[] genericMethodArguments)
        {
            throw new NotImplementedException ();
        }

        public override FieldInfo ResolveField (int metadataToken, MetaType[] genericTypeArguments, MetaType[] genericMethodArguments)
        {
            throw new NotImplementedException ();
        }

        public override MemberInfo ResolveMember (int metadataToken, MetaType[] genericTypeArguments, MetaType[] genericMethodArguments)
        {
            throw new NotImplementedException ();
        }

        public override string ResolveString (int metadataToken)
        {
            throw new NotImplementedException ();
        }

        public override MetaType[] __ResolveOptionalParameterTypes (int metadataToken)
        {
            throw new NotImplementedException ();
        }

        public override string ScopeName
        {
            get
            {
                throw new NotImplementedException ();
            }
        }

        internal override MetaType GetTypeImpl (string typeName)
        {
            throw new NotImplementedException ();
        }

        internal override void GetTypesImpl (List<MetaType> list)
        {
            throw new NotImplementedException ();
        }

        public override AssemblyName[] __GetReferencedAssemblies ()
        {
            throw new NotImplementedException ();
        }

        internal override MetaType GetModuleType ()
        {
            throw new NotImplementedException ();
        }

        internal override IKVM.Reflection.Reader.ByteReader GetBlob (int blobIndex)
        {
            throw new NotImplementedException ();
        }
    }

    readonly string full_name;
    readonly Module module;
    Dictionary<string, MetaType> types;

    public MissingAssembly (Universe universe, string fullName)
    : base (universe)
    {
        this.full_name = fullName;
        this.module = new MissingModule (universe, this);
        types = new Dictionary<string, MetaType> ();
    }

    public override MetaType[] GetTypes ()
    {
        throw new NotImplementedException ();
    }

    public override string FullName
    {
        get
        {
            return full_name;
        }
    }

    public override AssemblyName GetName ()
    {
        return new AssemblyName (full_name);
    }

    public override string ImageRuntimeVersion
    {
        get
        {
            throw new NotImplementedException ();
        }
    }

    public override Module ManifestModule
    {
        get
        {
            return module;
        }
    }

    public override MethodInfo EntryPoint
    {
        get
        {
            throw new NotImplementedException ();
        }
    }

    public override string Location
    {
        get
        {
            throw new NotImplementedException ();
        }
    }

    public override AssemblyName[] GetReferencedAssemblies ()
    {
        throw new NotImplementedException ();
    }

    public override Module[] GetModules (bool getResourceModules)
    {
        throw new NotImplementedException ();
    }

    public override Module[] GetLoadedModules (bool getResourceModules)
    {
        throw new NotImplementedException ();
    }

    public override Module GetModule (string name)
    {
        throw new NotImplementedException ();
    }

    public override string[] GetManifestResourceNames ()
    {
        throw new NotImplementedException ();
    }

    public override ManifestResourceInfo GetManifestResourceInfo (string resourceName)
    {
        throw new NotImplementedException ();
    }

    public override Stream GetManifestResourceStream (string resourceName)
    {
        throw new NotImplementedException ();
    }

    internal override MetaType GetTypeImpl (string typeName)
    {
        //
        // We are loading a type from missing reference
        // this itself is fine. The error will be reported
        // later when the type is actually used
        //
        MetaType t;
        if (!types.TryGetValue (typeName, out t))
        {
            t = new MissingType (typeName, this);
            types.Add (typeName, t);
        }

        return t;
    }

    internal override IList<CustomAttributeData> GetCustomAttributesData (MetaType attributeType)
    {
        throw new NotImplementedException ();
    }
}

public class MissingType : MetaType
{
    readonly string full_name;
    readonly MissingAssembly assembly;

    public MissingType (string typeName, MissingAssembly assembly)
    {
        this.full_name = typeName;
        this.assembly = assembly;
    }

    public override TypeAttributes Attributes
    {
        get
        {
            // TODO: Don't know yet
            return TypeAttributes.Public;
        }
    }

    public override MetaType BaseType
    {
        get
        {
            return null;
        }
    }

    public override string FullName
    {
        get
        {
            return full_name;
        }
    }

    internal override MetaType GetGenericTypeArgument (int index)
    {
        return new MissingType ("#" + index.ToString (), assembly);
    }

    public override MetaType GetNestedType (string name, BindingFlags bindingAttr)
    {
        return new MissingType (full_name + name, assembly);
    }

    public override bool IsGenericTypeDefinition
    {
        get
        {
            return full_name.IndexOf ('`') > 0;
        }
    }

    public override Module Module
    {
        get
        {
            return assembly.ManifestModule;
        }
    }
}

class AssemblyBuilderIKVM : AssemblyBuilderExtension
{
    readonly AssemblyBuilder builder;

    public AssemblyBuilderIKVM (AssemblyBuilder builder, CompilerContext ctx)
    : base (ctx)
    {
        this.builder = builder;
    }

    public override void AddTypeForwarder (TypeSpec type, Location loc)
    {
        builder.__AddTypeForwarder (type.GetMetaInfo ());
    }

    public override void DefineWin32IconResource (string fileName)
    {
        builder.__DefineIconResource (File.ReadAllBytes (fileName));
    }

    public override void SetAlgorithmId (uint value, Location loc)
    {
        builder.__SetAssemblyAlgorithmId ((AssemblyHashAlgorithm) value);
    }

    public override void SetCulture (string culture, Location loc)
    {
        builder.__SetAssemblyCulture (culture);
    }

    public override void SetFlags (uint flags, Location loc)
    {
        builder.__SetAssemblyFlags ((AssemblyNameFlags) flags);
    }

    public override void SetVersion (Version version, Location loc)
    {
        builder.__SetAssemblyVersion (version);
    }
}
}
