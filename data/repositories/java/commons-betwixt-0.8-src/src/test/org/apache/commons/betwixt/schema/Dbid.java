/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.betwixt.schema;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Id: Dbid.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class Dbid
{
    private ArrayList dbDataTypeCollection;

    public Dbid()
    {
        dbDataTypeCollection = new ArrayList();
    }

    public void addDbDataType(DbDataType dbDataType)
    {
        dbDataTypeCollection.add(dbDataType);
    }

    public List getDbDataTypes()
    {
        return dbDataTypeCollection;
    }

    public boolean equals(Object object)
    {
        if (object == null)
        {
            return false;
        }

        if (object instanceof Dbid)
        {
            Dbid dbid = (Dbid) object;
            if (dbid.getDbDataTypes().equals(this.getDbDataTypes()))
            {
                return true;
            }
        }
        return false;
    }
}

