// PutSourceProjectMeta.cs created with MonoDevelop
//
//User: eric at 23:30 06/08/2008
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
using System.Net;

namespace MonoOBSFramework.Functions.Sources
{

/// <summary>
/// Write package meta/xml data file.
/// </summary>
public static class PutSourceProjectPkgMeta
{
    /// <summary>
    /// Write package meta/xml data file(either if it not exist, you can use a get meta before to generate it, this is the way used to create a new package).
    /// </summary>
    /// <param name="PkgName">
    /// Package name to create or edit.
    /// </param>
    /// <param name="XmlFs">
    /// The file path and name who contain the XML meta data.
    /// </param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The status of the operation in XML format.
    /// </returns>
    /// <example> This sample shows how to call the PutProjectPkgMeta method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(PutProjectPkgMeta.PutSourceProjectPkgMeta("MonoOSC","home/user/tmp/TmpMeta.xml").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder PutProjectPkgMeta(string PkgName, string XmlFs)
    {

        if (Environment.OSVersion.Platform == PlatformID.Unix)
        {
            return  PUT.UploadFileCore("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "/_meta", VarGlobal.User, VarGlobal.Password, XmlFs);
        }
        else
        {
            return PUT.Putit("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "/_meta", VarGlobal.User, VarGlobal.Password, XmlFs);
        }
    }
}
}
