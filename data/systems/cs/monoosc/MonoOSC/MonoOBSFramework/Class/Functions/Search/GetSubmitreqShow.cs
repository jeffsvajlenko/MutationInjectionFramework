//
// GetSubmitreqShow.cs
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
using MonoOBSFramework.Functions.Sources;

namespace MonoOBSFramework.Functions.Search
{
/// <summary>
/// will show the request itself, and generate a diff for review, if used with the --diff option.
/// </summary>
public static class SubmitreqShow
{
    /// <summary>
    /// will show the request itself, and generate a diff for review, if used with the --diff option.
    /// </summary>
    /// <param name="ID">
    /// A <see cref="System.String"/>
    /// </param>
    /// <param name="ShowDiff">
    /// A <see cref="System.Boolean"/>
    /// </param>
    /// <returns>
    /// A <see cref="StringBuilder"/>
    /// </returns>
    public static StringBuilder GetSubmitreqShow(string ID, bool ShowDiff)
    {
        StringBuilder Result = new StringBuilder();
        Result = GET.Getit("request/" + ID, VarGlobal.User, VarGlobal.Password);
        try
        {
            if (ShowDiff)
            {
                string SrcePrj = ReadXml.ReadAttrValue(Result.ToString(), "/request/submit/source", "project");
                string SrcePkg = ReadXml.ReadAttrValue(Result.ToString(), "/request/submit/source", "package");
                string DestPrj = ReadXml.ReadAttrValue(Result.ToString(), "/request/submit/target", "project");
                string DestPkg = ReadXml.ReadAttrValue(Result.ToString(), "/request/submit/target", "package");
                string Rev = ReadXml.ReadAttrValue(Result.ToString(), "/request/submit/source", "rev");
                Result.Append(PostSourceRequestDiff.PostRequestDiff(SrcePrj, SrcePkg, DestPrj,
                              DestPkg, Rev).ToString());
            }
        }
        catch (Exception Ex)
        {
            if(!VarGlobal.LessVerbose)Console.WriteLine("{0}{1}{2}", Ex.Message, Environment.NewLine, Ex.StackTrace);
        }
        return Result;
    }
}
}
