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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;

/**
 * Models a <code>complexType</code>. Global (top level) complex types are
 * represented by {@link GlobalComplexType}. Locally defined or referenced
 * complex types are represented by {@link LocalComplexType}.
 *
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team </a>
 * @version $Revision: 438373 $
 */
public abstract class ComplexType
{

    protected List elements = new ArrayList();

    protected List attributes = new ArrayList();

    public ComplexType()
    {
    }

    public ComplexType(TranscriptionConfiguration configuration,
                       ElementDescriptor elementDescriptor, Schema schema)
    throws IntrospectionException
    {
        elementDescriptor = fillDescriptor(elementDescriptor, schema);
        init(configuration, elementDescriptor, schema);
    }

    /**
     * Fills the given descriptor
     * @since 0.7
     * @param elementDescriptor
     * @param schema
     * @return @throws
     *         IntrospectionException
     */
    protected ElementDescriptor fillDescriptor(
        ElementDescriptor elementDescriptor, Schema schema)
    throws IntrospectionException
    {
        if (elementDescriptor.isHollow())
        {
            // need to introspector for filled descriptor
            Class type = elementDescriptor.getSingularPropertyType();
            if (type == null)
            {
                type = elementDescriptor.getPropertyType();
            }
            if (type == null)
            {
                // no type!
                // TODO: handle this
                // TODO: add support for logging
                // TODO: maybe should try singular type?
            }
            else
            {
                XMLBeanInfo filledBeanInfo = schema.introspect(type);
                elementDescriptor = filledBeanInfo.getElementDescriptor();
            }
        }
        return elementDescriptor;
    }

    protected void init(TranscriptionConfiguration configuration,
                        ElementDescriptor elementDescriptor, Schema schema)
    throws IntrospectionException
    {

        AttributeDescriptor[] attributeDescriptors = elementDescriptor
                .getAttributeDescriptors();
        for (int i = 0, length = attributeDescriptors.length; i < length; i++)
        {
            //TODO: need to think about computing schema types from descriptors
            // this will probably depend on the class mapped to
            String uri = attributeDescriptors[i].getURI();
            if (!SchemaTranscriber.W3C_SCHEMA_INSTANCE_URI.equals(uri))
            {
                attributes.add(new Attribute(attributeDescriptors[i]));
            }
        }

        //TODO: add support for spacing elements
        ElementDescriptor[] elementDescriptors = elementDescriptor
                .getElementDescriptors();
        for (int i = 0, length = elementDescriptors.length; i < length; i++)
        {
            if (elementDescriptors[i].isHollow())
            {
                elements.add(new ElementReference(configuration,
                                                  elementDescriptors[i], schema));
            }
            else if (elementDescriptors[i].isSimple())
            {
                elements.add(new SimpleLocalElement(configuration,
                                                    elementDescriptors[i], schema));
            }
            else
            {
                elements.add(new ComplexLocalElement(configuration,
                                                     elementDescriptors[i], schema));
            }
        }
    }

    /**
     * Gets the elements contained by this type
     *
     * @return <code>List</code> of contained elements, not null
     */
    public List getElements()
    {
        return elements;
    }

    /**
     * Adds an element to those contained by this type
     *
     * @param element
     */
    public void addElement(ElementReference element)
    {
        elements.add(element);
    }

    /**
     * Adds an element to those contained by this type
     *
     * @param element
     */
    public void addElement(LocalElement element)
    {
        elements.add(element);
    }

    /**
     * Gets the attributes contained by this type.
     *
     * @return <code>List</code> of attributes
     */
    public List getAttributes()
    {
        return attributes;
    }

    /**
     * Adds an attribute to those contained by this type
     *
     * @param attribute
     */
    public void addAttribute(Attribute attribute)
    {
        attributes.add(attribute);
    }

}
