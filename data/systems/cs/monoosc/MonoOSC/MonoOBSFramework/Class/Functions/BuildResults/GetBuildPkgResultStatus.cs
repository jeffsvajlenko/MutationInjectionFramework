// BuildPkgResultStatus.cs created with MonoDevelop
//
//User: eric at 23:59 08/08/2008
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

using System.Text;
using MonoOBSFramework.Engine;

namespace MonoOBSFramework.Functions.BuildResults
{
/// <summary>
/// Get build status of the specified project/package/repo/arch combination.
/// </summary>
public static class BuildPkgResultStatus
{
    /// <summary>
    /// GET /build/&lt;project&gt;/&lt;repository&gt;/&lt;arch&gt;/&lt;package&gt;/_status
    /// Get build status of the specified project/package/repo/arch combination.
    /// </summary>
    /// <param name="Repository">repository</param>
    /// <param name="Arch">Arch (i586, x86_64 ...)</param>
    /// <param name="Package">Package name</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>buildstatus in an XML structure.
    /// </returns>
    /// <example> This sample shows how to call the BuildPkgResultStatus method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.BuildResults
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(BuildPkgResultStatus.GetBuildPkgResultStatus("mono","i586","MonoOSC").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder GetBuildPkgResultStatus(string Repository, string Arch, string Package)
    {
        return GET.Getit("build/" + VarGlobal.PrefixUserName + "/" + Repository + "/" + Arch + "/" + Package + "/_status", VarGlobal.User, VarGlobal.Password);
    }
}
}
