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

/**
 * Models a simpleType tag in an XML schema.
 * A simple type is an element that cannot have element content
 * and which has no attributes.
 *
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class SimpleType
{
    private String name;

    /**
     * Gets the name
     * @return the name of this type
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof SimpleType)
        {
            SimpleType simpleType = (SimpleType) obj;
            result = isEqual(name, simpleType.name);
        }
        return result;
    }

    public int hashCode()
    {
        return 0;
    }


    /**
     * Null safe equals method
     * @param one
     * @param two
     * @return
     */
    private boolean isEqual(String one, String two)
    {
        boolean result = false;
        if (one == null)
        {
            result = (two == null);
        }
        else
        {
            result = one.equals(two);
        }

        return result;
    }

    public String toString()
    {
        return "<xsd:simpleType name='" + name +  "'/>";
    }
}
