// WebDeployCommands.cs
//
// Author:
//   Michael Hutchinson <mhutchinson@novell.com>
//
// Copyright (C) 2007 Novell, Inc (http://www.novell.com)
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


using MonoDevelop.Components.Commands;
using MonoDevelop.AspNet;
using MonoDevelop.Ide;

namespace MonoDevelop.AspNet.Deployment
{

public enum WebDeployCommands
{
    DeployProject,
}

class ProjectDeployHandler : CommandHandler
{
    protected override void Run ()
    {
        AspNetAppProject project = (AspNetAppProject) IdeApp.ProjectOperations.CurrentSelectedProject;
        WebDeployService.DeployDialog (project);
    }

    protected override void Update (CommandInfo info)
    {
        AspNetAppProject project = IdeApp.ProjectOperations.CurrentSelectedProject as AspNetAppProject;
        info.Visible = (project != null);
        info.Enabled = (project != null);
    }
}
}
