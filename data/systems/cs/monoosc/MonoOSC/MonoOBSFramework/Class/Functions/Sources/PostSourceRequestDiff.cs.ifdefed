//
// PostSourceRequestDiff.cs
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

namespace MonoOBSFramework.Functions.Sources
{

/// <summary>
/// Write project meta/xml data file.
/// </summary>
public static class PostSourceRequestDiff
{
    /// <summary>
    /// POST https://api.opensuse.org/source/home:coolo:branches:openSUSE:Factory:staging/fam?opackage=fam&oproject=openSUSE:Factory&cmd=diff&rev=2&expand=1
    /// </summary>
    /// <param name="SrcePrj"></param>
    /// <param name="SrcePkg"></param>
    /// <param name="DestPrj"></param>
    /// <param name="DestPkg"></param>
    /// <param name="Rev"></param>
    /// <returns>StringBuilder</returns>
    public static StringBuilder PostRequestDiff(string SrcePrj, string SrcePkg, string DestPrj, string DestPkg,
            string Rev)
    {
        string UrlCmd = "source/" + SrcePrj + "/" + SrcePkg + "?opackage=" + DestPkg + "&oproject=" + DestPrj +
                        "&cmd=diff&rev=" + Rev + "&expand=1";
        return POST.Postit(UrlCmd, VarGlobal.User, VarGlobal.Password);
    }
}
}
