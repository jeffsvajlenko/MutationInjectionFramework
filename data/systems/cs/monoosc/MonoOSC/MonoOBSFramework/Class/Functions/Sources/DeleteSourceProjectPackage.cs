// DeleteSourceProjectPackageFile.cs created with MonoDevelop
//
//User: eric at 03:30 10/08/2008
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
/// Delete a package.
/// </summary>
public static class DeleteSourceProjectPackage
{
    /// <summary>
    /// Delete a package.
    /// </summary>
    /// <param name="PkgName">Package name</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The status of the operation in XML format.
    /// </returns>
    /// <example> This sample shows how to call the DelPackage method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(DeleteSourceProjectPackage.DelPackage("MonoOSC").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder DelPackage(string PkgName)
    {
        //Local OBS don't know "rev" param !!
        //StringBuilder Result = DELETE.DeleteitUnix("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "?rev=upload", VarGlobal.User, VarGlobal.Password);
        StringBuilder Result = DELETE.DeleteitUnix("source/" + VarGlobal.PrefixUserName + "/" + PkgName, VarGlobal.User, VarGlobal.Password);
        Commit.PostCommit(PkgName);
        return Result;
    }
}
}
