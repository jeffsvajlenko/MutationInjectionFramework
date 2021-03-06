#region MIT license
//
// MIT license
//
// Copyright (c) 2007-2008 Jiri Moudry, Pascal Craponne
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
//
#endregion

using System.Collections.Generic;

using DbLinq.Data.Linq.Sql;
using DbLinq.Data.Linq.Sugar.Expressions;

#if MONO_STRICT
using System.Data.Linq;
#else
using DbLinq.Data.Linq;
#endif

namespace DbLinq.Data.Linq.Sugar
{
internal class UpsertQuery : ParameterizedQuery
{
    /// <summary>
    /// Output parameters, intstance based (the instance being the entity)
    /// </summary>
    public IList<ObjectOutputParameterExpression> OutputParameters
    {
        get;
        private set;
    }

    /// <summary>
    /// Primary key parameters, to read out the instance in the case of IsDbGenerated and AutoSync parameters
    /// </summary>
    public IList<ObjectInputParameterExpression> PrimaryKeyParameters
    {
        get;
        private set;
    }

    /// <summary>
    /// This statement returns the generated PKs
    /// </summary>
    public SqlStatement IdQuerySql
    {
        get;
        private set;
    }

    public UpsertQuery(DataContext dataContext, SqlStatement sql, SqlStatement idQuerySql, IList<ObjectInputParameterExpression> inputParameters,
                       IList<ObjectOutputParameterExpression> outputParameters, IList<ObjectInputParameterExpression> primaryKeyParameters)
    : base(dataContext, sql,inputParameters)
    {
        OutputParameters = outputParameters;
        PrimaryKeyParameters = primaryKeyParameters;
        IdQuerySql = idQuerySql;
    }
}
}
