using System;
using System.Collections;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Xml;
using System.Xml.Serialization;

using MonoDevelop.Prj2Make.Schema.Csproj;
using MonoDevelop.Projects;
using MonoDevelop.Projects.Text;
using MonoDevelop.Core;
using MonoDevelop.Ide.Gui;

using MonoDevelop.Core.Assemblies;
using MonoDevelop.CSharp.Project;


namespace MonoDevelop.Prj2Make
{
public class SlnMaker
{
    public static string slash;
    Hashtable projNameInfo = new Hashtable();
    Hashtable projGuidInfo = new Hashtable();
    private string prjxFileName;
    private string m_strSlnVer;
    private string m_strCsprojVer;
    private bool m_bIsUnix;
    private bool m_bIsMcs;
    private bool m_bIsUsingLib;

    TargetFramework fx = Runtime.SystemAssemblyService.GetTargetFramework (TargetFrameworkMoniker.NET_1_1);

    // Flag use to determine if the LIB variable will be used in
    // the Makefile that prj2make generates
    public bool IsUsingLib
    {
        get
        {
            return m_bIsUsingLib;
        }
        set
        {
            m_bIsUsingLib = value;
        }
    }


    // Determines if the makefile is intended for nmake or gmake for urposes of path separator character
    public bool IsUnix
    {
        get
        {
            return m_bIsUnix;
        }
        set
        {
            m_bIsUnix = value;
        }
    }

    // Determines if using MCS or CSC
    public bool IsMcs
    {
        get
        {
            return m_bIsMcs;
        }
        set
        {
            m_bIsMcs = value;
        }
    }

    public string SlnVersion
    {
        get
        {
            return m_strSlnVer;
        }
        set
        {
            m_strSlnVer = value;
        }
    }

    public string CsprojVersion
    {
        get
        {
            return m_strCsprojVer;
        }
        set
        {
            m_strCsprojVer = value;
        }
    }

    // Shuld contain the file name
    // of the most resent prjx generation
    public string PrjxFileName
    {
        get
        {
            return prjxFileName;
        }
    }

    // Default constructor
    public SlnMaker()
    {
        m_bIsUnix = false;
        m_bIsMcs = false;
        m_bIsUsingLib = false;
    }

    // Utility function to determine the sln file version
    protected string GetSlnFileVersion(string strInSlnFile)
    {
        string strVersion = null;
        string strInput = null;
        Match match;
        FileStream fis = new FileStream(strInSlnFile, FileMode.Open, FileAccess.Read, FileShare.Read);
        StreamReader reader = new StreamReader(fis);
        strInput = reader.ReadLine();

        match = SlnVersionRegex.Match(strInput);
        if (match.Success)
        {
            strVersion = match.Groups[1].Value;
        }

        // Close the stream
        reader.Close();

        // Close the File Stream
        fis.Close();

        return strVersion;
    }

    // Utility function to determine the csproj file version
    protected string GetCsprojFileVersion(string strInCsprojFile)
    {
        string strRetVal = null;
        XmlDocument xmlDoc = new XmlDocument();

        xmlDoc.Load(strInCsprojFile);
        strRetVal = xmlDoc.SelectSingleNode("/VisualStudioProject/CSHARP/@ProductVersion").Value;

        return strRetVal;
    }

    protected void ParseMsCsProj(string fname)
    {
        string projectName = System.IO.Path.GetFileNameWithoutExtension (fname);
        string csprojPath = System.IO.Path.GetFileName (fname);
        string projectGuid = "";

        CsprojInfo pi = new CsprojInfo (m_bIsUnix, m_bIsMcs, projectName, projectGuid, csprojPath);

        projNameInfo[projectName] = pi;
        projGuidInfo[projectGuid] = pi;
    }

    protected void ParseSolution(string fname, IProgressMonitor monitor)
    {
        FileStream fis = new FileStream(fname,FileMode.Open, FileAccess.Read, FileShare.Read);
        using (StreamReader reader = new StreamReader(fis))
        {
            while (true)
            {
                string s = reader.ReadLine();
                Match match;

                match = ProjectRegex.Match(s);
                if (match.Success)
                {
                    string projectName = match.Groups[2].Value;
                    string csprojPath = match.Groups[3].Value;
                    string projectGuid = match.Groups[4].Value;

                    try
                    {
                        if (csprojPath.EndsWith (".csproj") && !csprojPath.StartsWith("http://"))
                        {
                            csprojPath = MapPath (Path.GetDirectoryName (fname), csprojPath);
                            CsprojInfo pi = new CsprojInfo (m_bIsUnix, m_bIsMcs, projectName, projectGuid, csprojPath);
                            projNameInfo[projectName] = pi;
                            projGuidInfo[projectGuid] = pi;
                        }
                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine (GettextCatalog.GetString ("Could not import project:") + csprojPath);
                        Console.WriteLine (ex.ToString ());
                        monitor.ReportError (GettextCatalog.GetString ("Could not import project:") + csprojPath, ex);
                        throw;
                    }
                }

                if (s.StartsWith("Global"))
                {
                    break;
                }
            }
        }
    }

    public SolutionEntityItem CreatePrjxFromCsproj (string csprojFileName, IProgressMonitor monitor)
    {
        try
        {
            FileFormat format = Services.ProjectService.FileFormats.GetFileFormatForFile (csprojFileName, typeof(SolutionEntityItem));
            if (format != null && format.Id != "VS2003ProjectFileFormat")
                return (SolutionEntityItem) Services.ProjectService.ReadSolutionItem (monitor, csprojFileName);

            MonoDevelop.Prj2Make.Schema.Csproj.VisualStudioProject csprojObj = null;

            monitor.BeginTask (GettextCatalog.GetString ("Importing project: ") + csprojFileName, 4);

            DotNetAssemblyProject prjxObj = new DotNetAssemblyProject ("C#", null, null);

            prjxFileName = String.Format ("{0}.mdp",
                                          Path.Combine (Path.GetDirectoryName (csprojFileName),
                                                  Path.GetFileNameWithoutExtension (csprojFileName))
                                         );

            // Load the csproj
            using (TextFileReader fsIn = new TextFileReader (csprojFileName))
            {
                XmlSerializer xmlDeSer = new XmlSerializer (typeof(VisualStudioProject));
                csprojObj = (VisualStudioProject) xmlDeSer.Deserialize (fsIn);
            }

            monitor.Step (1);

            // Begin prjxObj population
            prjxObj.FileName = prjxFileName;
            prjxObj.Name = Path.GetFileNameWithoutExtension(csprojFileName);
            prjxObj.Description = "";
            prjxObj.NewFileSearch = NewFileSearch.None;
            prjxObj.DefaultNamespace = csprojObj.CSHARP.Build.Settings.RootNamespace;

            GetContents (prjxObj, csprojObj.CSHARP.Files.Include, prjxObj.Files, monitor);
            monitor.Step (1);

            GetReferences (prjxObj, csprojObj.CSHARP.Build.References, prjxObj.References, monitor);
            monitor.Step (1);

            prjxObj.Configurations.Clear ();
            foreach (Config cblock in csprojObj.CSHARP.Build.Settings.Config)
            {
                prjxObj.Configurations.Add (CreateConfigurationBlock (
                                                prjxObj,
                                                cblock,
                                                csprojObj.CSHARP.Build.Settings.AssemblyName,
                                                csprojObj.CSHARP.Build.Settings.OutputType,
                                                monitor
                                            ));
            }
            monitor.Step (1);
            return prjxObj;

        }
        catch (Exception ex)
        {
            monitor.ReportError (GettextCatalog.GetString ("Could not import project:") + csprojFileName, ex);
            throw;
        }
        finally
        {
            monitor.EndTask ();
        }
    }

    public Solution MsSlnToCmbxHelper (string slnFileName, IProgressMonitor monitor)
    {
        Solution solution = new Solution();

        monitor.BeginTask (GettextCatalog.GetString ("Importing solution"), 2);
        try
        {
            // We invoke the ParseSolution
            // by passing the file obtained
            ParseSolution (slnFileName, monitor);

            // Create all of the prjx files form the csproj files
            monitor.BeginTask (null, projNameInfo.Values.Count * 2);

            foreach (CsprojInfo pi in projNameInfo.Values)
            {
                string mappedPath = MapPath (Path.GetDirectoryName (slnFileName), pi.csprojpath);
                if (mappedPath == null)
                {
                    monitor.Step (2);
                    monitor.ReportWarning (GettextCatalog.GetString ("Project file not found: ") + pi.csprojpath);
                    continue;
                }
                SolutionEntityItem prj;
                if (pi.NeedsConversion)
                    prj = CreatePrjxFromCsproj (mappedPath, monitor);
                else
                    prj = (DotNetProject) Services.ProjectService.ReadSolutionItem (monitor, mappedPath);

                if (prj == null)
                    return null;

                monitor.Step (1);
                solution.RootFolder.Items.Add (prj);
                foreach (ItemConfiguration conf in prj.Configurations)
                {
                    if (!solution.GetConfigurations ().Contains (conf.Id))
                        solution.AddConfiguration (conf.Id, false);
                }
                monitor.Step (1);
            }

            monitor.EndTask ();
            monitor.Step (1);

            solution.SetLocation (Path.GetDirectoryName (slnFileName), Path.GetFileNameWithoutExtension(slnFileName));

            monitor.Step (1);
            return solution;
        }
        catch (Exception e)
        {
            monitor.ReportError (GettextCatalog.GetString ("The solution could not be imported."), e);
            throw;
        }
        finally
        {
            monitor.EndTask ();
        }
    }

    protected void GetReferences (Project project, MonoDevelop.Prj2Make.Schema.Csproj.Reference[] References, ProjectReferenceCollection references, IProgressMonitor monitor)
    {
        if (References == null || References.Length == 0)
            return;

        monitor.BeginTask (null, 5 + References.Length);

        try
        {
            // Get the GAC path
            string strBasePathMono1_0 = GetPackageDirectory ("mono", "mono/1.0");

            monitor.Step (1);

            string strBasePathGtkSharp = GetPackageDirectory ("gtk-sharp", "mono/gtk-sharp");

            monitor.Step (1);

            string strBasePathGtkSharp2_0 = GetPackageDirectory ("gtk-sharp-2.0", "mono/gtk-sharp-2.0");

            monitor.Step (1);

            string strBasePathGeckoSharp = GetPackageDirectory ("gecko-sharp", "mono/gecko-sharp");

            monitor.Step (1);

            string strBasePathGeckoSharp2_0 = GetPackageDirectory ("gecko-sharp-2.0", "mono/gecko-sharp-2.0");

            string[] monoLibs = new string []
            {
                strBasePathMono1_0,
                strBasePathGtkSharp2_0,
                strBasePathGtkSharp,
                strBasePathGeckoSharp2_0,
                strBasePathGeckoSharp
            };

            // Iterate through the reference collection of the csproj file
            foreach (MonoDevelop.Prj2Make.Schema.Csproj.Reference rf in References)
            {
                monitor.Step (1);

                ProjectReference rfOut = null;

                if (rf.Package != null && rf.Package.Length != 0)
                {
                    rfOut = new ProjectReference (MonoDevelop.Projects.ReferenceType.Project, Path.GetFileName (rf.Name));
                    rfOut.LocalCopy = true;
                    references.Add (rfOut);
                }
                else if (rf.AssemblyName != null)
                {
                    string rname = rf.AssemblyName;
                    if (rname == "System.XML")
                        rname = "System.Xml";

                    string oref = Runtime.SystemAssemblyService.DefaultAssemblyContext.GetAssemblyFullName (rname, fx);
                    if (oref == null)
                    {
                        if (rf.HintPath != null)
                        {
                            string asm = MapPath (project.ItemDirectory, rf.HintPath);
                            if (!System.IO.File.Exists (asm))
                                monitor.ReportWarning (GettextCatalog.GetString ("Referenced assembly not found: ") + asm);
                            ProjectReference aref = new ProjectReference (MonoDevelop.Projects.ReferenceType.Assembly, asm);
                            references.Add (aref);
                            continue;
                        }
                        monitor.ReportWarning (GettextCatalog.GetString ("Assembly reference could not be imported: ") + rf.AssemblyName);
                        continue;
                    }
                    rfOut = new ProjectReference (MonoDevelop.Projects.ReferenceType.Package, oref);
                    rfOut.LocalCopy = true;
                    references.Add (rfOut);
                }
                else if (rf.HintPath != null)
                {
                    // HACK - under Unix filenames are case sensitive
                    // Under Windows there's no agreement on Xml vs XML ;-)
                    if (Path.GetFileName (rf.HintPath) == "System.XML.dll")
                    {
                        ProjectReference pref = GetMonoReferece (strBasePathMono1_0, "System.Xml.dll");
                        if (pref != null)
                        {
                            references.Add (pref);
                            continue;
                        }
                    }
                    else
                    {
                        foreach (string libDir in monoLibs)
                        {
                            if (libDir == null) continue;
                            if (rf.HintPath == null)
                                continue;
                            rfOut = GetMonoReferece (libDir, rf.HintPath);
                            if (rfOut != null)
                                break;
                        }

                        if (rfOut == null)
                        {
                            rfOut = new ProjectReference (MonoDevelop.Projects.ReferenceType.Package, Path.GetFileName (rf.HintPath));
                            rfOut.LocalCopy = true;
                        }
                        references.Add (rfOut);
                    }
                }
                else
                {
                    monitor.ReportWarning (GettextCatalog.GetString ("Assembly reference could not be imported: ") + rf.Name);
                }
            }
        }
        finally
        {
            monitor.EndTask ();
        }
    }

    string GetPackageDirectory (string package, string subdir)
    {
        string dir = MonoDevelop.Prj2Make.PkgConfigInvoker.GetPkgVariableValue (package, "libdir");
        return dir != null ? Path.Combine (dir.TrimEnd(), subdir) : null;
    }

    ProjectReference GetMonoReferece (string libPath, string reference)
    {
        string strRefFileName = Path.Combine (libPath, Path.GetFileName (reference));

        // Test to see if file exist in GAC location
        if (System.IO.File.Exists (strRefFileName))
        {
            ProjectReference rfOut = new ProjectReference (MonoDevelop.Projects.ReferenceType.Package, Runtime.SystemAssemblyService.DefaultAssemblyContext.GetAssemblyFullName (strRefFileName, fx));
            rfOut.LocalCopy = true;
            return rfOut;
        }
        return null;
    }

    protected void GetContents (MonoDevelop.Projects.Project project, MonoDevelop.Prj2Make.Schema.Csproj.File[] Include, ProjectFileCollection files, IProgressMonitor monitor)
    {
        if (Include == null || Include.Length == 0)
            return;

        // Iterate through the file collection of the csproj file
        foreach(MonoDevelop.Prj2Make.Schema.Csproj.File fl in Include)
        {
            ProjectFile flOut = new ProjectFile ();

            string name;
            if ((fl.Link == null) || (fl.Link.Length == 0))
            {
                name = MapPath (project.BaseDirectory, fl.RelPath);
            }
            else
            {
                name = MapPath (null, fl.Link);
            }

            if (name == null)
            {
                monitor.ReportWarning (GettextCatalog.GetString ("Can't import file: ") + fl.RelPath);
                continue;
            }
            flOut.Name = name;
            // Adding here as GetDefaultResourceIdInternal needs flOut.Project
            files.Add (flOut);

            switch (fl.SubType)
            {
            case "Code":
                flOut.Subtype = Subtype.Code;
                break;
            }

            switch (fl.BuildAction)
            {
            case MonoDevelop.Prj2Make.Schema.Csproj.FileBuildAction.Compile:
                flOut.BuildAction = BuildAction.Compile;
                break;
            case MonoDevelop.Prj2Make.Schema.Csproj.FileBuildAction.Content:
                flOut.BuildAction = BuildAction.Content;
                break;
            case MonoDevelop.Prj2Make.Schema.Csproj.FileBuildAction.EmbeddedResource:
                flOut.BuildAction = BuildAction.EmbeddedResource;
                break;
            case MonoDevelop.Prj2Make.Schema.Csproj.FileBuildAction.None:
                flOut.BuildAction = BuildAction.None;
                break;
            }
            // DependentUpon is relative to flOut
            flOut.DependsOn = MapPath (Path.GetDirectoryName (flOut.Name), fl.DependentUpon);
            flOut.Data = "";
        }
    }

    protected SolutionItemConfiguration CreateConfigurationBlock (MonoDevelop.Projects.DotNetProject project, Config ConfigBlock, string AssemblyName, string OuputType, IProgressMonitor monitor)
    {
        DotNetProjectConfiguration confObj = project.CreateConfiguration (ConfigBlock.Name) as DotNetProjectConfiguration;

        confObj.RunWithWarnings = false;
        confObj.DebugMode = ConfigBlock.DebugSymbols;
        project.CompileTarget = (CompileTarget) Enum.Parse (typeof(CompileTarget), OuputType, true);

        string dir = MapPath (project.BaseDirectory, ConfigBlock.OutputPath);
        if (dir == null)
        {
            dir = Path.Combine ("bin", ConfigBlock.Name);
            monitor.ReportWarning (string.Format (GettextCatalog.GetString ("Output directory '{0}' can't be mapped to a local directory. The directory '{1}' will be used instead"), ConfigBlock.OutputPath, dir));
        }
        confObj.OutputDirectory = dir;
        confObj.OutputAssembly = AssemblyName;

        CSharpCompilerParameters compilerParams = new CSharpCompilerParameters ();
        compilerParams.WarningLevel = ConfigBlock.WarningLevel;
        compilerParams.NoWarnings = "";
        compilerParams.Optimize = ConfigBlock.Optimize;
        compilerParams.DefineSymbols = ConfigBlock.DefineConstants;
        compilerParams.UnsafeCode = ConfigBlock.AllowUnsafeBlocks;
        compilerParams.GenerateOverflowChecks = ConfigBlock.CheckForOverflowUnderflow;

        return confObj;
    }

    internal static string MapPath (string basePath, string relPath)
    {
        if (relPath == null || relPath.Length == 0)
            return null;

        string path = relPath;
        if (Path.DirectorySeparatorChar != '\\')
            path = path.Replace ("\\", "/");

        if (char.IsLetter (path [0]) && path.Length > 1 && path[1] == ':')
            return null;

        if (basePath != null)
            path = Path.Combine (basePath, path);

        if (System.IO.File.Exists (path))
        {
            return Path.GetFullPath (path);
        }

        if (Path.IsPathRooted (path))
        {

            // Windows paths are case-insensitive. When mapping an absolute path
            // we can try to find the correct case for the path.

            string[] names = path.Substring (1).Split ('/');
            string part = "/";

            for (int n=0; n<names.Length; n++)
            {
                string[] entries;

                if (names [n] == "..")
                {
                    part = Path.GetFullPath (part + "/..");
                    continue;
                }

                entries = Directory.GetFileSystemEntries (part);

                string fpath = null;
                foreach (string e in entries)
                {
                    if (string.Compare (Path.GetFileName (e), names[n], true) == 0)
                    {
                        fpath = e;
                        break;
                    }
                }
                if (fpath == null)
                {
                    // Part of the path does not exist. Can't do any more checking.
                    part = Path.GetFullPath (part);
                    for (; n < names.Length; n++)
                        part += "/" + names[n];
                    return part;
                }

                part = fpath;
            }
            return Path.GetFullPath (part);
        }
        else
        {
            return Path.GetFullPath (path);
        }
    }

    // static regexes
    static Regex projectRegex = null;
    internal static Regex ProjectRegex
    {
        get
        {
            if (projectRegex == null)
                projectRegex = new Regex(@"Project\(""(\{[^}]*\})""\) = ""(.*)"", ""(.*)"", ""(\{[^{]*\})""");
            return projectRegex;
        }
    }

    static Regex slnVersionRegex = null;
    internal static Regex SlnVersionRegex
    {
        get
        {
            if (slnVersionRegex == null)
                slnVersionRegex = new Regex (@"Microsoft Visual Studio Solution File, Format Version (\d?\d.\d\d)");
            return slnVersionRegex;
        }
    }
}
}
