// GetSourceProjectPackageFile.cs created with MonoDevelop
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
using System.Collections.Generic;

namespace MonoOBSFramework.Functions.Sources
{
/// <summary>
/// Download one file in source section.
/// </summary>
public static class SourceProjectPackageFile
{
    /// <summary>
    /// Download one file used to construc/build a package in string format, example mypkg.spec file.
    /// </summary>
    /// <param name="PkgName">Package name</param>
    /// <param name="FileName">File in the package to download, string files only!</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The content of the string file.
    /// </returns>
    /// <example> This sample shows how to call the GetSourceProjectPackageFile method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(SourceProjectPackageFile.GetSourceProjectPackageFile("MonoOSC","MonoOSC.spec").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder GetSourceProjectPackageFile(string PkgName, string FileName)
    {
        return GET.Getit("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "/" + FileName, VarGlobal.User, VarGlobal.Password);
    }
    /// <summary>
    ///
    /// </summary>
    /// <param name="PrjName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="PkgName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="FileName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <returns>
    /// A <see cref="StringBuilder"/>
    /// </returns>
    public static StringBuilder GetSourceProjectPackageFile(string PrjName, string PkgName, string FileName)
    {
        return GET.Getit("source/" + PrjName + "/" + PkgName + "/" + FileName, VarGlobal.User, VarGlobal.Password);
    }
    /// <summary>
    ///
    /// </summary>
    /// <param name="PrjName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="PkgName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="FsList">
    /// A <see cref="List<System.String>"/>
    /// </param>
    /// <param name="DestDir">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="BlockSize">
    /// A <see cref="System.Int32"/>
    /// </param>
    /// <returns>
    /// A <see cref="System.Boolean"/>
    /// </returns>
    public static bool GetSourceProjectPackageFiles(string PrjName, string PkgName, List<string> FsList, string DestDir, int BlockSize)
    {
        return DownloadFsListsync.DownloadFsList("source/" + PrjName + "/" + PkgName + "/", FsList, DestDir, BlockSize);
    }
    /// <summary>
    /// Download one file used to construc/build a package to a destination directory, example mypkg.tar.bz2 file.
    /// </summary>
    /// <param name="PkgName">Package name</param>
    /// <param name="FileName">File in the package to download.</param>
    /// <param name="Dest">Where to store the downloaded file, example "/home/user/tmp"</param>
    /// <param name="BlockSize">The size of each block during download, example 1024</param>
    /// <param name="TotalSize">The length of the file in Byte</param>
    /// <example> This sample shows how to call the GetSourceProjectPackageFile method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         SourceProjectPackageFile.GetSourceProjectPackageFile("MonoOSC","MonoOSC-1.0.1.4.tar.bz2","/home/user/tmp",1024,600);
    ///     }
    /// }
    /// </code>
    /// </example>
    public static void GetSourceProjectPackageFile(string PkgName, string FileName, string Dest, int BlockSize, int TotalSize)
    {
        GETBIN DllFs = new GETBIN();
        DllFs.DownLoadFile("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "/" + FileName, VarGlobal.User, VarGlobal.Password, Dest, BlockSize, TotalSize);
    }
    /// <summary>
    ///
    /// </summary>
    /// <param name="PrjName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="PkgName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="FileName">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="Dest">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="BlockSize">
    /// A <see cref="System.Int32"/>
    /// </param>
    /// <param name="TotalSize">
    /// A <see cref="System.Int32"/>
    /// </param>
    public static void GetSourceProjectPackageFile(string PrjName, string PkgName, string FileName, string Dest, int BlockSize, int TotalSize)
    {
        GETBIN DllFs = new GETBIN();
        DllFs.DownLoadFile("source/" + PrjName  + "/" + PkgName + "/" + FileName, VarGlobal.User, VarGlobal.Password, Dest, BlockSize, TotalSize);
    }

    //https://api.opensuse.org/source/home:surfzoid/Fedora_9/i586/MonoOSC/MonoOSC-1.0.0.0-2.2.i386.rpm
    /// <summary>
    /// Download one file build in a repo/package/arch , example MonoOSC-1.0.0.0-2.2.i386.rpm file.
    /// </summary>
    /// <param name="Repository">repository</param>
    /// <param name="Arch">Arch (i586, x86_64 ...)</param>
    /// <param name="PkgName">Package name</param>
    /// <param name="FileName">File in the package to download.</param>
    /// <param name="Dest">Where to store the downloaded file, example "/home/user/tmp"</param>
    /// <param name="BlockSize">The size of each block during download, example 1024</param>
    /// <param name="TotalSize">The length of the file in Byte</param>
    /// <example> This sample shows how to call the GetSourceProjectPackageFile method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         SourceProjectPackageFile.GetSourceProjectPackageFile("mono","i586","MonoOSC","MonoOSC-1.0.0.0-2.2.i386.rpm","/home/user/tmp",1024,600);
    ///     }
    /// }
    /// </code>
    /// </example>
    public static void GetSourceProjectPackageFile(string Repository, string Arch, string PkgName, string FileName, string Dest, int BlockSize, int TotalSize)
    {
        GETBIN DllFs = new GETBIN();
        DllFs.DownLoadFile("source/" + VarGlobal.PrefixUserName + "/" + Repository + "/" + Arch + "/" + PkgName + "/" + FileName, VarGlobal.User, VarGlobal.Password, Dest, BlockSize, TotalSize);
    }
}
}
