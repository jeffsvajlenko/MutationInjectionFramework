// UnixShell.cs created with MonoDevelop
//
//User: eric at 22:06 21/07/2008
//
// Copyright (C) 2008 [Petit Eric, surfzoid@gmail.com]
//
//Permission is hereby granted, free of charge, to any person
//obtaining a copy of this software and associated documentation
//files (the "Software"), to deal in the Software without
//restriction, including without limitation the rights to use,
//copy, modify, merge, publish, distribute, sublicense, and/or sell
//copies of the Software, and to permit persons to whom the
//Software is furnished to do so, subject to the following
//conditions:
//
//The above copyright notice and this permission notice shall be
//included in all copies or substantial portions of the Software.
//
//THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//OTHER DEALINGS IN THE SOFTWARE.
//




using System;
using System.Diagnostics;
using MonoOBSFramework;

namespace MonoOSC
{


static public class UnixShell
{

    /// <summary>
    /// Start the Unix Shell command
    /// </summary>
    /// <param name="Proc">Executable file</param>
    /// <param name="Args">Argument of the executable</param>
    /// <param name="WorkDir">The working directory, can be the executable one</param>
    /// <param name="RedirectOut">If set to true it return the console line result of the command, else it only display in the console</param>
    /// <returns></returns>
    public static string StartProcess(string Proc, string Args, string WorkDir, bool RedirectOut)
    {
        try
        {
            ShellOutPut = string.Empty;
            ShellErrorOutPut = string.Empty;
            Process UnixProcess = new Process();
            UnixProcess.StartInfo.FileName = Proc;
            UnixProcess.StartInfo.Arguments = Args;
            UnixProcess.StartInfo.WorkingDirectory = WorkDir;

            // Set UseShellExecute to false for redirection.
            UnixProcess.StartInfo.UseShellExecute = false;

            UnixProcess.StartInfo.CreateNoWindow = true;
            UnixProcess.StartInfo.WindowStyle = ProcessWindowStyle.Hidden;

            // Redirect the standard output of the sort command.
            // This stream is read asynchronously using an event handler.
            if (RedirectOut == true)
            {
                UnixProcess.StartInfo.RedirectStandardOutput = true;
                UnixProcess.StartInfo.RedirectStandardError = true;

                // Set our event handler to asynchronously read the sort output.
                UnixProcess.OutputDataReceived += new DataReceivedEventHandler(SortOutputHandler);
                UnixProcess.ErrorDataReceived += new DataReceivedEventHandler(SortOutputErrorHandler);
            }

            UnixProcess.Start();

            if (RedirectOut == false) UnixProcess.WaitForExit();

            // Start the asynchronous read of the sort output stream.
            if (RedirectOut == true)
            {
                UnixProcess.BeginOutputReadLine();
                UnixProcess.BeginErrorReadLine();
                UnixProcess.WaitForExit();
                if(!VarGlobal.LessVerbose)Console.WriteLine(RetShellVal);
                return RetShellVal;
            }
        }
        catch (Exception ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine(ex.Message + Environment.NewLine + ex.StackTrace);
            ShellErrorOutPut += ex.Message + Environment.NewLine + ex.StackTrace;
        }
        return "Nothing !";
    }

    static string RetShellVal = string.Empty;
    public static string ShellOutPut = string.Empty;
    public static string ShellErrorOutPut = string.Empty;
    static private void SortOutputHandler(object sendingProcess, DataReceivedEventArgs outLine)
    {
        // Collect the sort command output.
        if (!String.IsNullOrEmpty(outLine.Data))
        {
            RetShellVal = outLine.Data;
            ShellOutPut += outLine.Data + Environment.NewLine;
            //if(!VarGlobal.LessVerbose)Console.WriteLine(RetShellVal);
        }
    }

    static private void SortOutputErrorHandler(object sendingProcess, DataReceivedEventArgs outLine)
    {
        // Collect the sort command output.
        if (!String.IsNullOrEmpty(outLine.Data))
        {
            RetShellVal = outLine.Data;
            ShellErrorOutPut += outLine.Data + Environment.NewLine;
            //if(!VarGlobal.LessVerbose)Console.WriteLine(RetShellVal);
        }
    }

}//Class
}//NameSpace
