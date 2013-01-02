//
// Gendarme.Framework.Rocks.AssemblyRocks
//
// Authors:
//	Sebastien Pouliot  <sebastien@ximian.com>
//
// Copyright (C) 2008 Novell, Inc (http://www.novell.com)
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

using Mono.Cecil;

namespace Gendarme.Framework.Rocks
{

public static class AssemblyRocks
{

    /// <summary>
    /// Check if the AssemblyName is reference by the assembly.
    /// </summary>
    /// <param name="self">The AssemblyDefinition on which the extension method can be called.</param>
    /// <param name="assemblyName">AssemblyName of the reference</param>
    /// <returns>True if any of the assembly's modules reference the assembly name, false otherwise.</returns>
    public static bool References (this AssemblyDefinition self, string assemblyName)
    {
        if (assemblyName == null)
            throw new ArgumentNullException ("assemblyName");
        if (self == null)
            return false;

        foreach (ModuleDefinition module in self.Modules)
        {
            foreach (AssemblyNameReference r in module.AssemblyReferences)
            {
                if (r.Name == assemblyName)
                    return true;
            }
        }
        return false;
    }
}
}
