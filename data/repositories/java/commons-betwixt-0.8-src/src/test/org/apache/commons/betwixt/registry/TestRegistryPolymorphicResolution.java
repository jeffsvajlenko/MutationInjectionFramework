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
package org.apache.commons.betwixt.registry;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.read.ElementMapping;
import org.apache.commons.betwixt.io.read.ReadConfiguration;
import org.apache.commons.betwixt.io.read.ReadContext;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author Thomas Dudziak (tomdz@apache.org)
 */
public class TestRegistryPolymorphicResolution extends TestCase
{

    public static class Container
    {
        private List _elements = new ArrayList();

        public Iterator getElements()
        {
            return _elements.iterator();
        }

        public void addElement(Element element)
        {
            _elements.add(element);
        }
    }

    public static interface Element
    {}

    public static class ElementA implements Element
    {}

    public static class ElementB implements Element
    {}

    private static final String MAPPING =
        "<?xml version=\"1.0\"?>\n"+
        "<betwixt-config>\n"+
        "  <class name=\"org.apache.commons.betwixt.registry.TestRegistryPolymorphicResolution$Container\">\n"+
        "    <element name=\"container\">\n"+
        "      <element name=\"elements\">\n"+
        "        <element property=\"elements\"/>\n"+
        "      </element>\n"+
        "    </element>\n"+
        "  </class>\n"+
        "  <class name=\"org.apache.commons.betwixt.registry.TestRegistryPolymorphicResolution$ElementA\">\n"+
        "    <element name=\"elementA\"/>\n"+
        "  </class>\n"+
        "  <class name=\"org.apache.commons.betwixt.registry.TestRegistryPolymorphicResolution$ElementB\">\n"+
        "    <element name=\"elementB\"/>\n"+
        "  </class>\n"+
        "</betwixt-config>";

    public void testRegisterThenResolve() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.register(new InputSource(new StringReader(MAPPING)));


        ElementDescriptor descriptor = introspector.introspect(Element.class).getElementDescriptor();
        ElementMapping elementMapping = new ElementMapping();
        elementMapping.setAttributes(new AttributesImpl());
        elementMapping.setName("Bogus");
        elementMapping.setDescriptor(descriptor);
        elementMapping.setType(Iterator.class);
        ReadContext readContext = new ReadContext(new BindingConfiguration(), new ReadConfiguration());

        assertNull(introspector.getPolymorphicReferenceResolver().resolveType(elementMapping, readContext));

        elementMapping.setName("elementA");
        Class resolution = introspector.getPolymorphicReferenceResolver().resolveType(elementMapping, readContext);
        assertEquals("Should resolve to the element about", ElementA.class, resolution);
    }
}
