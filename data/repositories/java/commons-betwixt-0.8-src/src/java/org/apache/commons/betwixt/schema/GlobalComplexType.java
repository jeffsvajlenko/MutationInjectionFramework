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
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.betwixt.ElementDescriptor;

/**
 * Models a <code>complexType</code> from an XML schema.
 * A complex type may contain element content and may have attributes.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class GlobalComplexType extends ComplexType
{

    private String name;
    private TranscriptionConfiguration configuration;

    public GlobalComplexType() {}

    /**
     * Constructs a new ComplexType from the descriptor given.
     * @param elementDescriptor
     */
    public GlobalComplexType(TranscriptionConfiguration configuration, ElementDescriptor elementDescriptor, Schema schema) throws IntrospectionException
    {
        super(configuration, elementDescriptor, schema);
    }

    protected void init(TranscriptionConfiguration configuration, ElementDescriptor elementDescriptor, Schema schema) throws IntrospectionException
    {
        this.configuration = configuration;
        setName(nameFromDescriptor( elementDescriptor ));
    }

    /**
     * Fills the complex type description.
     * @since 0.7
     * @param configuration
     * @param elementDescriptor
     * @param schema
     * @throws IntrospectionException
     */
    protected void fill(TranscriptionConfiguration configuration, ElementDescriptor elementDescriptor, Schema schema) throws IntrospectionException
    {
        elementDescriptor = fillDescriptor(elementDescriptor, schema);
        super.init(configuration, elementDescriptor, schema);
    }

    private String nameFromDescriptor( ElementDescriptor elementDescriptor )
    {
        return configuration.getSchemaTypeNamingStrategy().nameSchemaType(elementDescriptor);
    }

    /**
     * Does the given element descriptor match this complex type?
     * @since 0.7
     * @param elementDescriptor
     * @return true if the descriptor matches
     */
    public boolean matches(ElementDescriptor elementDescriptor)
    {
        String nameFromDescriptor = nameFromDescriptor ( elementDescriptor );
        return nameFromDescriptor.equals(getName());
    }

    /**
     * Gets the name of this type.
     * @return the name of this type
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this type.
     * @param string
     */
    public void setName(String string)
    {
        name = string;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof GlobalComplexType)
        {
            GlobalComplexType complexType = (GlobalComplexType) obj;
            result =  isEqual(name, complexType.name) &&
                      equalContents(attributes, complexType.attributes) &&
                      equalContents(elements, complexType.elements);

        }
        return result;
    }

    public int hashCode()
    {
        return 0;
    }

    private boolean equalContents(Collection one, Collection two)
    {
        // doesn't check cardinality but should be ok
        if (one.size() != two.size())
        {
            return false;
        }
        for (Iterator it=one.iterator(); it.hasNext();)
        {
            Object object = it.next();
            if (!two.contains(object))
            {
                return false;
            }
        }
        return true;
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
        buffer.append("<xsd:complexType name='");
        buffer.append(name);
        buffer.append("'>");
        buffer.append("<xsd:sequence>");
        for (Iterator it=elements.iterator(); it.hasNext();)
        {
            buffer.append(it.next());
        }
        buffer.append("</xsd:sequence>");

        for (Iterator it=attributes.iterator(); it.hasNext();)
        {
            buffer.append(it.next());
        }
        buffer.append("</xsd:complexType>");
        return buffer.toString();
    }
}
