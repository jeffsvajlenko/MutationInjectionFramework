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
package org.apache.commons.betwixt.io.read;

import org.apache.commons.betwixt.ElementDescriptor;
import org.xml.sax.Attributes;

/**
  * Describes a mapping between an xml element and a betwixt element.
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public class ElementMapping
{

    /** Namespace of the xml element */
    private String namespace;
    /** Name of the element */
    private String name;
    /** Attributes associated with this element */
    private Attributes attributes;
    /** The base type of the mapped bean */
    private Class type;
    /** The mapped descriptor */
    private ElementDescriptor descriptor;

    /** Base constructor */
    public ElementMapping() {}

    /**
      * Gets the namespace URI or an empty string if the parser is not namespace aware
      * or the element has no namespace.
      * @return namespace possibly null
      */
    public String getNamespace()
    {
        return namespace;
    }

    /**
      * Sets the namespace URI for this element
      * @param namespace the namespace uri, possibly null
      */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
      * Gets the local name if the parser is namespace aware, otherwise the name.
      * @return the element name, possibly null
      */
    public String getName()
    {
        return name;
    }

    /**
      * Sets the local name for this element.
      * @param name the element name, possibly null
      */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
      * Gets the element's attributes.
      * @return the Attributes for this element, possibly null.
      */
    public Attributes getAttributes()
    {
        return attributes;
    }

    /**
      * Sets the element's attributes
      * @param attributes the element's attributes, possibly null
      */
    public void setAttributes(Attributes attributes)
    {
        this.attributes = attributes;
    }

    /**
      * Gets the base type for this element.
      * The base type may - or may not - correspond to the created type.
      * @return the Class of the base type for this element
      */
    public Class getType()
    {
        return type;
    }

    /**
      * Sets the base type for this element.
      * The base type may - or may not - correspond to the created type.
      * @param type the Class of the base type for this element
      */
    public void setType(Class type)
    {
        this.type = type;
    }

    /**
      * Gets the mapped element descriptor.
      * @return the mapped ElementDescriptor
      */
    public ElementDescriptor getDescriptor()
    {
        return descriptor;
    }

    /**
      * Sets the mapped element descriptor.
      * @param descriptor set this descriptor
      */
    public void setDescriptor(ElementDescriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    /**
     * Returns something useful for logging.
     * @since 0.8
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ElementMapping[");
        buffer.append(name);
        buffer.append(" -> ");
        buffer.append(type);
        buffer.append("]");
        return buffer.toString();
    }
}
