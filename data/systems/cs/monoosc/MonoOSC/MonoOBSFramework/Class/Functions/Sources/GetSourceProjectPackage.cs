// GetSourceProjectPackage.cs created with MonoDevelop
//
//User: eric at 23:58 08/08/2008
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

namespace MonoOBSFramework.Functions.Sources
{
/// <summary>
/// Get directory listing of all source files in the package
/// </summary>
public static class GetSourceProjectPackage
{
    //source/<project>/<package>
    /// <summary>
    /// Get directory listing of all source files in the package.
    /// </summary>
    /// <param name="PkgName">Package name</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The list of all files in the package, in XML format.
    /// </returns>
    /// <example> This sample shows how to call the GetFileList method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(GetSourceProjectPackage.GetFileList("MonoOSC").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder GetFileList(string PkgName)
    {
        return GET.Getit("source/" + VarGlobal.PrefixUserName + "/" + PkgName, VarGlobal.User, VarGlobal.Password);
    }

    /// <summary>
    /// Get directory listing of all source files in the package
    /// </summary>
    /// <param name="Project"></param>
    /// <param name="PkgName"></param>
    /// <returns></returns>
    public static StringBuilder GetFileList(string Project,string PkgName)
    {
        return GET.Getit("source/" + Project + "/" + PkgName, VarGlobal.User, VarGlobal.Password);
    }
}
}
