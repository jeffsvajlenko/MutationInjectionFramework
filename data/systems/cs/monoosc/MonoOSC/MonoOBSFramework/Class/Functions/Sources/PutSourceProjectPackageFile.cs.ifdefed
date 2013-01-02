// PutSourceProjectPackageFile.cs created with MonoDevelop
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
using System.IO;
using MonoOBSFramework.Engine;
using System.Net;

namespace MonoOBSFramework.Functions.Sources
{
/// <summary>
/// Upload a file in the package source or build area of the server.
/// </summary>
public static class PutSourceProjectPackageFile
{
    /// <summary>
    /// Upload a file in the package source area of the server.
    /// </summary>
    /// <param name="PkgName">Package name</param>
    /// <param name="FileName">File name, example MonoOSC.tar.bz2 .</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The status of the operation in XML format.
    /// </returns>
    /// <example> This sample shows how to call the PutFile method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(PutSourceProjectPackageFile.PutFile("MonoOSC","MonoOSC.tar.bz2").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder PutFile(string PkgName, string FileName)
    {
        System.IO.FileInfo FsInf = new System.IO.FileInfo(FileName);
        StringBuilder Result = null;
        if (Environment.OSVersion.Platform == PlatformID.Unix)
        {
            Result = PUT.UploadFileCore("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "/" + FsInf.Name + "?rev=upload", VarGlobal.User, VarGlobal.Password, FileName);
        }
        else
        {
            //FIXME : Putit replaced by UploadFileCore
            Result = PUT.UploadFileCore("source/" + VarGlobal.PrefixUserName + "/" + PkgName + "/" + FsInf.Name + "?rev=upload", VarGlobal.User, VarGlobal.Password, FileName);
        }
        Commit.PostCommit(PkgName);
        return Result;
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
    public static StringBuilder PutFile(string PrjName, string PkgName, string FileName)
    {
        System.IO.FileInfo FsInf = new System.IO.FileInfo(FileName);
        StringBuilder Result = null;
        if (Environment.OSVersion.Platform == PlatformID.Unix)
        {
            Result = PUT.UploadFileCore("source/" + PrjName + "/" + PkgName + "/" + FsInf.Name + "?rev=upload", VarGlobal.User, VarGlobal.Password, FileName);
        }
        else
        {
            //FIXME : Putit replaced by UploadFileCore
            Result = PUT.UploadFileCore("source/" + PrjName + "/" + PkgName + "/" + FsInf.Name + "?rev=upload", VarGlobal.User, VarGlobal.Password, FileName);
        }
        Commit.PostCommit(PkgName);
        return Result;
    }

    // https://api.opensuse.org/build/home:surfzoid/Fedora_9/i586/MonoOSC/MonoOSC-1.0.0.0-2.2.i386.rpm
    /// <summary>
    /// Upload a file in the package build area of the server.
    /// </summary>
    /// <param name="Repository">repository</param>
    /// <param name="Arch">Arch (i586, x86_64 ...)</param>
    /// <param name="PkgName">Package name</param>
    /// <param name="FileName">File in the package to download.</param>
    /// <param name="Dest">Where to store the downloaded file, example "/home/user/tmp"</param>
    /// <param name="BlockSize">The size of each block during download, example 1024</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The status of the operation in XML format.
    /// </returns>
    /// <example> This sample shows how to call the PutFile method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         PutSourceProjectPackageFile.PutFile("mono","i586","MonoOSC","MonoOSC-1.0.0.0-2.2.i386.rpm","/home/user/tmp",1024);
    ///     }
    /// }
    /// </code>
    /// </example>
    public static void PutFile(string Repository, string Arch, string PkgName, string FileName, string Dest, int BlockSize)
    {
        System.IO.FileInfo FsInf = new System.IO.FileInfo(FileName);
        PUT.Putit("source/" + VarGlobal.PrefixUserName + "/" + Repository + "/" + Arch + "/" + PkgName + "/" + FsInf.Name + "?rev=upload", VarGlobal.User, VarGlobal.Password,FileName);
    }


}//class
}//Namespace
