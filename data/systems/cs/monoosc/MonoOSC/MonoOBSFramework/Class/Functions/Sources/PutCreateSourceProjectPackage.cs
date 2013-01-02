// DeleteSourceProjectPackageFile.cs created with MonoDevelop
//
//User: eric at 03:30 10/08/2008
//
// Copyright (C) 2008 [Petit Eric, surfzoid@gmail.com]
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http://www.gnu.org/licenses/>.
//

using System;
using System.Text;
using MonoOSCFramework.Engine;

namespace MonoOSCFramework.Functions.Sources
{
public static class PutCreateSourceProjectPackage
{
    /// <summary>
    ///
    /// </summary>
    /// <param name="UserName"></param>
    /// <param name="PkgName"></param>
    /// <param name="FileName"></param>
    /// <returns></returns>
    public static StringBuilder CreatePackage(string UserName, string PkgName)
    {
        StringBuilder Result = PUT.Putit("source/" + VarGlobal.PrefixUserName + UserName + "/" + PkgName + "/_meta", VarGlobal.User, VarGlobal.Password);
        return Result;
    }
}
}
