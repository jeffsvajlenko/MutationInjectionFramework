// Commit.cs created with MonoDevelop
//
//User: eric at 17:38 18/08/2008
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
using System.IO;
using MonoOBSFramework.Engine;

namespace MonoOBSFramework.Functions.Sources
{
/// <summary>
/// Commit files/changes like with SVN, for a specified package.
/// </summary>
public static class Commit
{
    //POST "https://api.opensuse.org/source/home:surfzoid/MonoOSC?comment=&cmd=commit&rev=upload&user=surfzoid"
    /// <summary>
    /// Commit files/changes like with SVN, for a specified package.
    /// </summary>
    /// <param name="PkgName">Package name</param>
    /// <example> This sample shows how to call the PostCommit method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(Commit.PostCommit().ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static void PostCommit(string PkgName)
    {
        POST.Postit("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "?comment=&cmd=commit&rev=upload&user=" + VarGlobal.User, VarGlobal.User, VarGlobal.Password);
    }
}
}
