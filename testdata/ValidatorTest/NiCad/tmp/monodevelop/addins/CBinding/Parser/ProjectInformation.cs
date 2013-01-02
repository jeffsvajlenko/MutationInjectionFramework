//
// ProjectInformation.cs
//
// Authors:
//   Marcos David Marin Amador <MarcosMarin@gmail.com>
//
// Copyright (C) 2007 Marcos David Marin Amador
//
//
// This source code is licenced under The MIT License:
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

using System;
using System.Collections.Generic;

using MonoDevelop.Projects;

using CBinding.Navigation;

namespace CBinding.Parser
{
public class FileInformation
{
    protected Project project;

    protected List<Namespace> namespaces = new List<Namespace> ();
    protected List<Function> functions = new List<Function> ();
    protected List<Class> classes = new List<Class> ();
    protected List<Structure> structures = new List<Structure> ();
    protected List<Member> members = new List<Member> ();
    protected List<Variable> variables = new List<Variable> ();
    protected List<Macro> macros = new List<Macro> ();
    protected List<Enumeration> enumerations = new List<Enumeration> ();
    protected List<Enumerator> enumerators = new List<Enumerator> ();
    protected List<Union> unions = new List<Union> ();
    protected List<Typedef> typedefs = new List<Typedef> ();
    protected List<Local> locals = new List<Local> ();

    private string file_name;
    private bool is_filled = false;

    public FileInformation (Project project)
    {
        this.project = project;
        this.file_name = null;
    }

    public FileInformation (Project project, string filename)
    {
        this.project = project;
        this.file_name = filename;
    }

    public void Clear ()
    {
        namespaces.Clear ();
        functions.Clear ();
        classes.Clear ();
        structures.Clear ();
        members.Clear ();
        variables.Clear ();
        macros.Clear ();
        enumerations.Clear ();
        enumerators.Clear ();
        unions.Clear ();
        typedefs.Clear ();
        locals.Clear ();
    }

    /// <summary>
    /// Remove a file's parse information from the database.
    /// </summary>
    public void RemoveFileInfo(string filename)
    {
        namespaces.RemoveAll(delegate(Namespace item)
        {
            return item.File == filename;
        });
        functions.RemoveAll(delegate(Function item)
        {
            return item.File == filename;
        });
        classes.RemoveAll(delegate(Class item)
        {
            return item.File == filename;
        });
        structures.RemoveAll(delegate(Structure item)
        {
            return item.File == filename;
        });
        members.RemoveAll(delegate(Member item)
        {
            return item.File == filename;
        });
        variables.RemoveAll(delegate(Variable item)
        {
            return item.File == filename;
        });
        macros.RemoveAll(delegate(Macro item)
        {
            return item.File == filename;
        });
        enumerations.RemoveAll(delegate(Enumeration item)
        {
            return item.File == filename;
        });
        enumerators.RemoveAll(delegate(Enumerator item)
        {
            return item.File == filename;
        });
        unions.RemoveAll(delegate(Union item)
        {
            return item.File == filename;
        });
        typedefs.RemoveAll(delegate(Typedef item)
        {
            return item.File == filename;
        });
        locals.RemoveAll(delegate(Local item)
        {
            return item.File == filename;
        });
    }

    public IEnumerable<LanguageItem> Containers ()
    {
        List<LanguageItem> containers = new List<LanguageItem> ();

        containers.AddRange ((LanguageItem[])namespaces.ToArray());
        containers.AddRange ((LanguageItem[])classes.ToArray());
        containers.AddRange ((LanguageItem[])structures.ToArray());
        containers.AddRange ((LanguageItem[])enumerations.ToArray());
        containers.AddRange ((LanguageItem[])unions.ToArray());

        return containers;
    }

    // Functions, fields
    public IEnumerable<LanguageItem> InstanceMembers ()
    {
        List<LanguageItem> instanceMembers = new List<LanguageItem> ();

        instanceMembers.AddRange ((LanguageItem[])functions.ToArray());
        instanceMembers.AddRange ((LanguageItem[])members.ToArray());

        return instanceMembers;
    }

    // All items except macros
    public IEnumerable<LanguageItem> AllItems ()
    {
        List<LanguageItem> allItems = new List<LanguageItem> ();

        allItems.AddRange (Containers ());
        allItems.AddRange (InstanceMembers ());
        allItems.AddRange ((LanguageItem[])variables.ToArray());
        allItems.AddRange ((LanguageItem[])enumerators.ToArray());
        allItems.AddRange ((LanguageItem[])typedefs.ToArray());
        allItems.AddRange ((LanguageItem[])locals.ToArray());

        return allItems;
    }

    public Project Project
    {
        get
        {
            return project;
        }
    }

    public List<Namespace> Namespaces
    {
        get
        {
            return namespaces;
        }
    }

    public List<Function> Functions
    {
        get
        {
            return functions;
        }
    }

    public List<Class> Classes
    {
        get
        {
            return classes;
        }
    }

    public List<Structure> Structures
    {
        get
        {
            return structures;
        }
    }

    public List<Member> Members
    {
        get
        {
            return members;
        }
    }

    public List<Variable> Variables
    {
        get
        {
            return variables;
        }
    }

    public List<Macro> Macros
    {
        get
        {
            return macros;
        }
    }

    public List<Enumeration> Enumerations
    {
        get
        {
            return enumerations;
        }
    }

    public List<Enumerator> Enumerators
    {
        get
        {
            return enumerators;
        }
    }

    public List<Union> Unions
    {
        get
        {
            return unions;
        }
    }

    public List<Typedef> Typedefs
    {
        get
        {
            return typedefs;
        }
    }

    public List<Local> Locals
    {
        get
        {
            return locals;
        }
    }

    public string FileName
    {
        get
        {
            return file_name;
        }
        set
        {
            file_name = value;
        }
    }

    public bool IsFilled
    {
        get
        {
            return is_filled;
        }
        set
        {
            is_filled = value;
        }
    }
}

// TODO: Update this such that it either supports multiple configurations - or is used in a configuration specific manner.
public class ProjectInformation : FileInformation
{
    private Globals globals;
    private MacroDefinitions macroDefs;

    private Dictionary<string, List<FileInformation>> includedFiles = new Dictionary<string, List<FileInformation>> ();

    public ProjectInformation (Project project) : base (project)
    {
        globals = new Globals (project);
        macroDefs = new MacroDefinitions (project);
    }

    public Globals Globals
    {
        get
        {
            return globals;
        }
    }

    public MacroDefinitions MacroDefinitions
    {
        get
        {
            return macroDefs;
        }
    }

    public Dictionary<string, List<FileInformation>> IncludedFiles
    {
        get
        {
            return includedFiles;
        }
    }
}
}
