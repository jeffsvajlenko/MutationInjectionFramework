//
// ProjectInformationManager.cs
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
/// <summary>
/// Singleton class to manage the navigation information of each project
/// </summary>
public class ProjectInformationManager
{
    private static ProjectInformationManager instance;
    private List<ProjectInformation> projects = new List<ProjectInformation> ();

    private ProjectInformationManager ()
    {
    }

    public ProjectInformation Get (Project project)
    {
        foreach (ProjectInformation p in projects)
        {
            if (p.Project == project ||
                    (null != project && project.Equals (p.Project)))
            {
                return p;
            }
        }

        ProjectInformation newinfo = new ProjectInformation (project);
        projects.Add (newinfo);

        return newinfo;
    }

    public static ProjectInformationManager Instance
    {
        get
        {
            if (instance == null)
                instance = new ProjectInformationManager ();

            return instance;
        }
    }
}
}
