//
// System.Web.UI.Design.WebControls.DataProviderNameConverter.cs
//
// Author:
//          Chris Toshok (toshok@ximian.com)
//
// Copyright (C) 2005 Novell, Inc (http://www.novell.com)
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
//

#if NET_2_0

using System.ComponentModel;
using System.Windows.Forms.Design;
using System.Data.Common;
using System.Data;

namespace System.Web.UI.Design.WebControls
{

public class DataProviderNameConverter : StringConverter
{
    public override StandardValuesCollection GetStandardValues (ITypeDescriptorContext context)
    {
        DataTable table = DbProviderFactories.GetFactoryClasses();
        if (table == null)
            return new StandardValuesCollection (new string[0]);

        string[] names = new string[table.Rows.Count];
        int i = 0;
        foreach (DataRow row in table.Rows)
        {
            names[i++] = (string)row ["Name"];
        }

        return new StandardValuesCollection (names);
    }

    public override bool GetStandardValuesExclusive (ITypeDescriptorContext context)
    {
        return true;
    }

    public override bool GetStandardValuesSupported (ITypeDescriptorContext context)
    {
        return false;
    }
}
}

#endif