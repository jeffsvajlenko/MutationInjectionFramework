//
// PostRebuildAloneArch.cs
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

namespace MonoOBSFramework.Functions.BuildResults
{
/// <summary>
/// Triggers package rebuild for the repositories with specified architecture, of the package specified
/// </summary>
public static class RebuildAloneArch
{
    // POST https://api.opensuse.org/build/home:surfzoid?cmd=rebuild&arch=i586&repository=mono&package=MonoOSC
    /// <summary>
    /// Triggers package rebuild for the repositories with specified architecture, of the package specified
    /// </summary>
    /// <param name="Repository">repository</param>
    /// <param name="Arch">Arch (i586, x86_64 ...)</param>
    /// <param name="Package">Package name</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>Status of the command in XML structure.
    /// </returns>
    /// <example> This sample shows how to call the PostRebuildAloneArch method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.BuildResults
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(RebuildAloneArch.PostRebuildAloneArch("mono","i586","MonoOSC").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder PostRebuildAloneArch(string Repository, string Arch, string Package)
    {
        return POST.Postit("build/" + VarGlobal.PrefixUserName + "?cmd=rebuild&arch=" + Arch + "&repository=" + Repository + "&package=" + Package, VarGlobal.User, VarGlobal.Password);
    }
}
}
