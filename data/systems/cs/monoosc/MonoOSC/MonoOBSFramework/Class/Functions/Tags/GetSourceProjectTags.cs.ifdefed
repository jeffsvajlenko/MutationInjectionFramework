// GetSourceProjectTags.cs created with MonoDevelop
//
//User: eric at 15:07 05/08/2008
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

namespace MonoOBSFramework.Functions.Tags
{

/// <summary>
/// Get tags by project.
/// </summary>
public static class GetSourceProjectTags
{
    /// <summary>
    /// Get tags by project.
    /// </summary>
    /// <returns>
    /// A <see cref="StringBuilder"/>The tags of project in XML format.
    /// </returns>
    /// <example> This sample shows how to call the GetTags method.
    /// <code>
    /// using System;
    /// using MonoOBSFramework.Functions.Tags
    /// class TestClass
    /// {
    ///     static int Main()
    ///     {
    ///         if(!VarGlobal.LessVerbose)Console.WriteLine(GetSourceProjectTags.GetTags().ToString());
    ///     }
    /// }
    /// </code>
    /// </example>
    public static StringBuilder GetTags()
    {
        return GET.Getit("source/" + VarGlobal.PrefixUserName + "/" + VarGlobal.Project + "/_tags", VarGlobal.User, VarGlobal.Password);
    }

}
}
