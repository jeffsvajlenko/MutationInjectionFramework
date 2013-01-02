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
 * Models a global definition of an <code>element</code>.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class GlobalElement implements Element
{
    //TODO: going to ignore the issue of namespacing for the moment
    public static final String STRING_SIMPLE_TYPE="xsd:string";

    private String name;
    private String type;

    private GlobalComplexType complexType;

    public GlobalElement() {}

    public GlobalElement(String name, String type)
    {
        setName(name);
        setType(type);
    }

    public GlobalElement(String name, GlobalComplexType complexType)
    {
        setName(name);
        setComplexType(complexType);
    }




    /**
     * Gets the element name
     * @return element name, not null
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the element name
     * @param string not null
     */
    public void setName(String string)
    {
        name = string;
    }

    /**
     * Gets the element type
     * @return the type of the element
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the element type
     * @param string
     */
    public void setType(String string)
    {
        type = string;
    }


    /**
     * Gets the anonymous type definition for this element, if one exists.
     * @return ComplexType, null if there is no associated anonymous type definition
     */
    public GlobalComplexType getComplexType()
    {
        return complexType;
    }

    /**
     * Sets the anonymous type definition for this element
     * @param type ComplexType to be set as the anonymous type definition,
     * null if the type is to be referenced
     */
    public void setComplexType(GlobalComplexType type)
    {
        this.type = type.getName();
        complexType = type;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof GlobalElement)
        {
            GlobalElement element = (GlobalElement) obj;
            result = isEqual(type, element.type) &&
                     isEqual(name, element.name);
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
        StringBuffer buffer = new StringBuffer();
        buffer.append("<xsd:element name='");
        buffer.append(name);
        buffer.append("' type='");
        buffer.append(type);
        buffer.append("'>");

        if (complexType != null)
        {
            buffer.append(complexType);
        }
        buffer.append("</xsd:element>");
        return buffer.toString();
    }


}
