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

import org.apache.commons.betwixt.AttributeDescriptor;


/**
 * Models the attribute element in an XML schema.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class Attribute
{

    private String name;
    private String type;


    public Attribute() {}

    public Attribute(String name, String type)
    {
        setName(name);
        setType(type);
    }

    public Attribute(AttributeDescriptor attributeDescriptor)
    {
        this(attributeDescriptor.getQualifiedName(),"xsd:string");
    }


    /**
     * Gets the attribute name
     * @return name of this attribute, not null
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the attribute name
     * @param string the name for this attribute, not null
     */
    public void setName(String string)
    {
        name = string;
    }

    /**
     * Gets the attribute type
     * @return the type of this attribute
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the attribute type
     * @param string the attribute type
     */
    public void setType(String string)
    {
        type = string;
    }

    public int hashCode()
    {
        return 0;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof Attribute)
        {
            Attribute attribute = (Attribute) obj;
            result = isEqual(type, attribute.type) &&
                     isEqual(name, attribute.name);
        }
        return result;
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
        return "<xsd:attribute name='" + name + "' type='" + type + "'/>";
    }

}
