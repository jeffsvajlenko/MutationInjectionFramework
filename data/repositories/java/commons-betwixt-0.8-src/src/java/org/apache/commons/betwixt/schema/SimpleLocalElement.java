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

import java.beans.IntrospectionException;

import org.apache.commons.betwixt.ElementDescriptor;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class SimpleLocalElement extends LocalElement
{

    private String type;

    public SimpleLocalElement(String name, String type)
    {
        super(name);
        setType(type);
    }

    public SimpleLocalElement(
        TranscriptionConfiguration configuration,
        ElementDescriptor descriptor,
        Schema schema)
    throws IntrospectionException
    {
        super(descriptor, schema);
        setType(configuration.getDataTypeMapper().toXMLSchemaDataType(descriptor.getPropertyType()));
    }

    public String getType()
    {
        return type;
    }

    public void setType(String string)
    {
        type = string;
    }

    public int hashCode()
    {
        return getName().hashCode();
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof SimpleLocalElement)
        {
            SimpleLocalElement simpleLocalElement = (SimpleLocalElement) obj;
            result =
                isEqual(getName(), simpleLocalElement.getName()) &&
                isEqual(getType(), simpleLocalElement.getType());
        }
        return result;
    }

    private boolean isEqual(String one, String two)
    {
        if (one == null)
        {
            return (two == null);
        }

        return one.equals(two);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<element name='");
        buffer.append(getName());
        buffer.append("' type='");
        buffer.append(getType());
        buffer.append("'/>");
        return buffer.toString();
    }
}
