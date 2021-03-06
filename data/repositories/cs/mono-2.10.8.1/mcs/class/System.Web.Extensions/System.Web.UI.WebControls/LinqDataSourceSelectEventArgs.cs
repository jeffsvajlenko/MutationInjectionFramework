//
// LinqDataSourceSelectEventArgs.cs
//
// Author:
//   Atsushi Enomoto  <atsushi@ximian.com>
//
// Copyright (C) 2008 Novell, Inc  http://novell.com
//

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
#if NET_3_5
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.ComponentModel;
using System.Security.Permissions;
using System.Web.UI;

namespace System.Web.UI.WebControls
{
[AspNetHostingPermission (SecurityAction.InheritanceDemand, Level = AspNetHostingPermissionLevel.Minimal)]
[AspNetHostingPermission (SecurityAction.LinkDemand, Level = AspNetHostingPermissionLevel.Minimal)]
public class LinqDataSourceSelectEventArgs : CancelEventArgs
{
    public LinqDataSourceSelectEventArgs (
        DataSourceSelectArguments arguments,
        IDictionary<string, object> whereParameters,
        IOrderedDictionary orderByParameters,
        IDictionary<string, object> groupByParameters,
        IDictionary<string, object> orderGroupsByParameters,
        IDictionary<string, object> selectParameters)
    {
        // all nullable.
        Arguments = arguments;
        WhereParameters = whereParameters;
        OrderByParameters = orderByParameters;
        GroupByParameters = groupByParameters;
        OrderGroupsByParameters = orderGroupsByParameters;
        SelectParameters = selectParameters;
    }

    public DataSourceSelectArguments Arguments
    {
        get;
        private set;
    }

    public IDictionary<string, object> GroupByParameters
    {
        get;
        private set;
    }

    public IOrderedDictionary OrderByParameters
    {
        get;
        private set;
    }

    public IDictionary<string, object> OrderGroupsByParameters
    {
        get;
        private set;
    }

    public object Result
    {
        get;
        set;
    }

    public IDictionary<string, object> SelectParameters
    {
        get;
        private set;
    }

    public IDictionary<string, object> WhereParameters
    {
        get;
        private set;
    }
}
}
#endif
