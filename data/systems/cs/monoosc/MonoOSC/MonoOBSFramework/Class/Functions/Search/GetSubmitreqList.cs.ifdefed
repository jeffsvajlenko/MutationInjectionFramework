//
// GetSubmitreqList.cs
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
/// Get the wole metada of an project list who match with the given project name.
/// </summary>
public static class SubmitreqList
{
    /// <summary>
    /// GET /search/request?match=submit/target/@project='home:surfzoid:'
    /// lists open requests attached to a project or package.
    /// </summary>
    /// <param name="Project">Project name(often home: + username)</param>
    /// <param name="package">If null or empty return all the package either only return result for the package</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The list of all medata aviables.
    /// </returns>
    /// <example> This sample shows how to call the GetSubmitreqList method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Search
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(SubmitreqList.GetSubmitreqList("home:surfzoid").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder GetSubmitreqList(string Project,string package)
    {
        if (string.IsNullOrEmpty(package))
        {
            return GET.Getit("search/request?match=submit/target/@project='" + Project + "'", VarGlobal.User, VarGlobal.Password);
        }
        else
        {
            return GET.Getit("search/request?match=submit/target/@project='" + Project + "' and submit/target/@package='" + package + "'", VarGlobal.User, VarGlobal.Password);
        }
    }

    /// <summary>
    /// GET https://api.opensuse.org/search/request?match=state/@name='Status' and state/@who='rwooninck'
    /// </summary>
    /// <param name="User">The user name to request</param>
    /// <param name="Status">could be : new, delete, revoked, declined and accepted</param>
    /// <returns></returns>
    public static StringBuilder GetSubmitreqListByUserName(string User, string Status)
    {
        return GET.Getit("search/request?match=state/@name='" + Status + "' and state/@who='" + User + "'", VarGlobal.User, VarGlobal.Password);
    }

}//class
}//Namespace
