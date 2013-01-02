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
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Id: PhysicalSchema.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class PhysicalSchema
{

    private ArrayList dbmsCollection;


    private boolean autoCreate = false;

    public PhysicalSchema()
    {
        dbmsCollection = new ArrayList();
    }
    public PhysicalSchema(String autoCreate)
    {
        this.autoCreate = autoCreate.equalsIgnoreCase("yes");
    }
    public void setAutocreate(String autoCreate)
    {
        this.autoCreate = (autoCreate.equalsIgnoreCase("yes"));
    }

    public String getAutocreate()
    {
        return this.autoCreate?"yes":"no";
    }

    public void addDbms(Dbms dbms)
    {
        dbmsCollection.add(dbms);
    }

    public List getDbmss()
    {
        return dbmsCollection;
    }

    public boolean equals(Object object)
    {
        if (object == null)
        {
            return false;
        }
        if (object instanceof PhysicalSchema)
        {
            PhysicalSchema schema = (PhysicalSchema) object;
            if (schema.getAutocreate().equals(this.getAutocreate()))
            {
                int count = 0;
                Iterator it = schema.getDbmss().iterator();
                while ( it.hasNext() )
                {
                    if (count >= dbmsCollection.size() )
                    {
                        return false;
                    }
                    if (! it.next().equals( dbmsCollection.get(count++) ) )
                    {
                        return false;
                    }
                }

                return true;
            }
        }
        return false;
    }

    public String toString()
    {
        return "[PhysicalSchema] autocreate=" + getAutocreate() + " dbmass=" + getDbmss();
    }
}

