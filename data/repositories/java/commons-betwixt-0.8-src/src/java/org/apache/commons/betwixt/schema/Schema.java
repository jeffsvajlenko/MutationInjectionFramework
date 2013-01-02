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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;

/**
 * Model for top level element in an XML Schema
 *
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class Schema
{

    private List elements = new ArrayList();
    private List complexTypes = new ArrayList();
    private List simpleTypes = new ArrayList();

    private XMLIntrospector introspector;

    public Schema()
    {
        this(new XMLIntrospector());
    }

    public Schema(XMLIntrospector introspector)
    {
        this.introspector = introspector;
    }

    /**
     * Introspects the given type giving an <code>XMLBeanInfo</code>.
     * @param type Class to introspect, not null
     * @return <code>XMLBeanInfo</code>, not null
     * @throws IntrospectionException
     */
    public XMLBeanInfo introspect(Class type) throws IntrospectionException
    {
        return introspector.introspect(type);
    }

    /**
     * Gets the complex types defined
     * @return list of <code>ComplexType</code>'s not null
     */
    public List getComplexTypes()
    {
        return complexTypes;
    }


    /**
     * Adds a new complex type to those defined
     * @param complexType not null
     */
    public void addComplexType(GlobalComplexType complexType)
    {
        complexTypes.add(complexType);
    }


    /**
     * Gets the elements definied
     * @return list of <code>Element</code>s not null
     */
    public List getElements()
    {
        return elements;
    }

    /**
     * Adds a new element to those defined.
     * @param element not null
     */
    public void addElement(GlobalElement element)
    {
        elements.add(element);
    }

    /**
     * Gets the simple types defined.
     * @return list of <code>SimpleType</code>s not null
     */
    public List getSimpleTypes()
    {
        return simpleTypes;
    }

    /**
     * Adds a new simple type to those defined.
     * @param simpleType
     */
    public void addSimpleType(SimpleType simpleType)
    {
        simpleTypes.add(simpleType);
    }


    /**
     * Adds global (top level) element and type declarations matching the given descriptor.
     * @param elementDescriptor ElementDescriptor not null
     */
    public void addGlobalElementType(TranscriptionConfiguration configuration, ElementDescriptor elementDescriptor) throws IntrospectionException
    {
        // need to create a global element declaration and a complex type
        // use the fully qualified class name as the type name
        GlobalElement element = new GlobalElement(
            elementDescriptor.getLocalName(),
            configuration.getSchemaTypeNamingStrategy().nameSchemaType(elementDescriptor));
        addElement(element);
        addGlobalComplexType(configuration, elementDescriptor);
    }

    /**
     * Adds a new global complex type definition matching the given element descriptor.
     * If this element descriptor has already been mapped to a global type then
     * that is returned.
     * @since 0.7
     * @param configuration <code>TranscriptionConfiguration</code>, not null
     * @param elementDescriptor <code>ElementDescriptor</code>, not null
     * @return <code>GlobalComplexType</code>
     * @throws IntrospectionException
     */
    public GlobalComplexType addGlobalComplexType(TranscriptionConfiguration configuration, ElementDescriptor elementDescriptor) throws IntrospectionException
    {
        GlobalComplexType type = null;
        for (Iterator it = complexTypes.iterator(); it.hasNext();)
        {
            GlobalComplexType complexType = (GlobalComplexType) it.next();
            if (complexType.matches( elementDescriptor ))
            {
                type = complexType;
                break;
            }
        }
        if (type == null)
        {
            type = new GlobalComplexType(configuration, elementDescriptor, this);
            addComplexType(type);
            type.fill(configuration, elementDescriptor, this);
        }
        return type;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof Schema)
        {
            Schema schema = (Schema) obj;
            result =
                equalContents(elements, schema.elements) &&
                equalContents(complexTypes, schema.complexTypes) &&
                equalContents(simpleTypes, schema.simpleTypes);
        }
        return result;
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

    public int hashCode()
    {
        return 0;
    }



    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version='1.0'?>");
        buffer.append("<xsd:schema xmlns:xsd='http://www.w3c.org/2001/XMLSchema'>");

        for (Iterator it=simpleTypes.iterator(); it.hasNext();)
        {
            buffer.append(it.next());
        }

        for (Iterator it=complexTypes.iterator(); it.hasNext();)
        {
            buffer.append(it.next());
        }


        for (Iterator it=elements.iterator(); it.hasNext();)
        {
            buffer.append(it.next());
        }
        buffer.append("</xsd:schema>");
        return buffer.toString();
    }
}
