// PutPerson.cs created with MonoDevelop
//
//User: eric at 14:57 05/08/2008
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

namespace MonoOBSFramework.Functions.User.Data
{

/// <summary>
/// Write user data.
/// </summary>
public static class PutPerson
{
    /// <summary>
    /// Write user data.
    /// </summary>
    /// <param name="XmlFs">The file path and name who contain the XML meta data, use get person to have an existing one or a new.</param>
    /// <returns>
    /// A <see cref="StringBuilder"/>The status of the operation in XML format.
    /// </returns>
    /// <example> This sample shows how to call the PutPersonData method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.User.Data
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(PutPerson.PutPersonData("home/user/tmp/TmpMeta.xml").ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder PutPersonData(string XmlFs)
    {
        //return PUT.Putit("person/" + UserName + "?" + ArgName + "=" + ArgValue, VarGlobal.User, VarGlobal.Password);
        if (Environment.OSVersion.Platform == PlatformID.Unix)
        {
            return  PUT.UploadFileCore("person/", VarGlobal.User, VarGlobal.Password, XmlFs);
        }
        else
        {
            return PUT.Putit("person/", VarGlobal.User, VarGlobal.Password, XmlFs);
        }
    }
}
}
