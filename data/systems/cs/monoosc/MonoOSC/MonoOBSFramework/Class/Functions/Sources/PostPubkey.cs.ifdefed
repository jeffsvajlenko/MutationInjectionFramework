// PutPubkey.cs created with MonoDevelop
//
//User: eric at 18:16 04/08/2008
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
using System.Collections.Generic;
using System.Text;
using MonoOBSFramework.Engine;

namespace MonoOBSFramework.Functions.Sources
{
/// <summary>
/// Generate a new gpg key. If the project already has an own gpg key, the old key is discarded.0
/// </summary>
public static class PostPubkey
{
    /// <summary>
    /// Generate a new gpg key. If the project already has an own gpg key, the old key is discarded.0
    /// </summary>
    /// <returns>
    /// A <see cref="StringBuilder"/>The status of the operation in XML format.
    /// </returns>
    /// <example> This sample shows how to call the PostNewKey method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Sources
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(PostPubkey.PostNewKey().ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder PostNewKey()
    {
        return POST.Postit("source/" + VarGlobal.PrefixUserName + "?cmd=createkey", VarGlobal.User, VarGlobal.Password);
    }
}
}
