//
// GetSubmitreqLog.cs
//
// Author:
//       Surfzoid <surfzoid@gmail.com>
//
// Copyright (c) 2009 Surfzoid
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
using System.Text;
using MonoOBSFramework.Engine;

namespace MonoOBSFramework.Functions.Search
{
/// <summary>
/// will show the history of the given ID.
/// </summary>
public static class SubmitreqLog
{
    /// <summary>
    /// GET /search/request?match=submit/target/@project='home:surfzoid:'
    /// will show the history of the given ID.
    /// </summary>
    /// <param name="ID">Project ID</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The log.
    /// </returns>
    /// <example> This sample shows how to call the GetSubmitreqLog method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Search
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(SubmitreqLog.GetSubmitreqLog("8658").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder GetSubmitreqLog(string ID)
    {
        return GET.Getit("request/" + ID , VarGlobal.User, VarGlobal.Password);
    }
}
}
