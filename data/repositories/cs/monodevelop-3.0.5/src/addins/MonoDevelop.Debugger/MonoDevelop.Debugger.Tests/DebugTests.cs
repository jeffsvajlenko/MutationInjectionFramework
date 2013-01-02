//
// DebugTests.cs
//
// Author:
//       Lluis Sanchez Gual <lluis@novell.com>
//
// Copyright (c) 2009 Novell, Inc (http://www.novell.com)
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
using NUnit.Framework;
using UnitTests;
using Mono.Debugging.Client;
using MonoDevelop.Core;
using MonoDevelop.Core.Execution;
using System.IO;
using System.Threading;
using MonoDevelop.Projects.Text;

namespace MonoDevelop.Debugger.Tests
{
public abstract class DebugTests: TestBase
{
    string eid;
    DebuggerEngine engine;

    public DebugTests (string engineId)
    {
        eid = engineId;
    }

    public override void Setup ()
    {
        base.Setup ();
        foreach (DebuggerEngine e in DebuggingService.GetDebuggerEngines ())
        {
            if (e.Id == eid)
            {
                engine = e;
                break;
            }
        }
    }


    protected DebuggerSession Start (string test)
    {
        DotNetExecutionCommand cmd = new DotNetExecutionCommand ();
        cmd.Command = Path.Combine (Path.GetDirectoryName (GetType ().Assembly.Location), "MonoDevelop.Debugger.Tests.TestApp.exe");
        cmd.Arguments = test;

        DebuggerStartInfo si = engine.CreateDebuggerStartInfo (cmd);
        DebuggerSession session = engine.CreateSession ();
        DebuggerSessionOptions ops = new DebuggerSessionOptions ();
        ops.EvaluationOptions = EvaluationOptions.DefaultOptions;
        ops.EvaluationOptions.EvaluationTimeout = 100000;

        FilePath path = Util.TestsRootDir;
        path = path.ParentDirectory.Combine ("src","addins","MonoDevelop.Debugger","MonoDevelop.Debugger.Tests.TestApp","Main.cs").FullPath;
        TextFile file = TextFile.ReadFile (path);
        int i = file.Text.IndexOf ("void " + test);
        if (i == -1)
            throw new Exception ("Test not found: " + test);
        i = file.Text.IndexOf ("/*break*/", i);
        if (i == -1)
            throw new Exception ("Break marker not found: " + test);
        int line, col;
        file.GetLineColumnFromPosition (i, out line, out col);
        Breakpoint bp = session.Breakpoints.Add (path, line);
        bp.Enabled = true;

        ManualResetEvent done = new ManualResetEvent (false);

        session.OutputWriter = delegate (bool isStderr, string text)
        {
            Console.WriteLine ("PROC:" + text);
        };

        session.TargetHitBreakpoint += delegate
        {
            done.Set ();
        };

        session.Run (si, ops);
        if (!done.WaitOne (3000))
            throw new Exception ("Timeout while waiting for initial breakpoint");

        return session;
    }
}

static class EvalHelper
{
    public static ObjectValue Sync (this ObjectValue val)
    {
        if (!val.IsEvaluating)
            return val;

        object locker = new object ();
        EventHandler h = delegate
        {
            lock (locker)
            {
                Monitor.PulseAll (locker);
            }
        };

        val.ValueChanged += h;

        lock (locker)
        {
            while (val.IsEvaluating)
            {
                if (!Monitor.Wait (locker, 4000))
                    throw new Exception ("Timeout while waiting for value evaluation");
            }
        }

        val.ValueChanged -= h;
        return val;
    }
}
}
