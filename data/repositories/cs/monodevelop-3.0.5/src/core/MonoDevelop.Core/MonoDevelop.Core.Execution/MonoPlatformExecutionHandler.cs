//
// MonoPlatformExecutionHandler.cs
//
// Author:
//   Lluis Sanchez Gual
//
// Copyright (C) 2005 Novell, Inc (http://www.novell.com)
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

namespace MonoDevelop.Core.Execution
{
public class MonoPlatformExecutionHandler: NativePlatformExecutionHandler
{
    string monoPath = "mono";

    public MonoPlatformExecutionHandler ()
    {
    }

    public MonoPlatformExecutionHandler (string monoPath, IDictionary<string, string> defaultEnvironmentVariables): base (defaultEnvironmentVariables)
    {
        this.monoPath = monoPath;
    }

    public override IProcessAsyncOperation Execute (ExecutionCommand command, IConsole console)
    {
        DotNetExecutionCommand dotcmd = (DotNetExecutionCommand) command;

        string runtimeArgs = string.IsNullOrEmpty (dotcmd.RuntimeArguments) ? "--debug" : dotcmd.RuntimeArguments;

        string args = string.Format ("{2} \"{0}\" {1}", dotcmd.Command, dotcmd.Arguments, runtimeArgs);
        NativeExecutionCommand cmd = new NativeExecutionCommand (monoPath, args, dotcmd.WorkingDirectory, dotcmd.EnvironmentVariables);

        return base.Execute (cmd, console);
    }

    public override bool CanExecute (ExecutionCommand command)
    {
        return command is DotNetExecutionCommand;
    }

}
}
