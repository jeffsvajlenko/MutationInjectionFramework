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
package org.apache.commons.betwixt;


import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.betwixt.io.BeanReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tests the multi-mapping of collections with polymorphic entries.
 *
 * @author Thomas Dudziak (tomdz@apache.org)
 */
public class TestCollectionMapping2 extends AbstractTestCase
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

    public static class Element
    {
        private List _subElements = new ArrayList();

        public Iterator getSubElements()
        {
            return _subElements.iterator();
        }

        public void addSubElement(SubElement subElement)
        {
            _subElements.add(subElement);
        }
    }

    public static interface SubElement
    {}

    public static class SubElementA implements SubElement
    {}

    public static class SubElementB implements SubElement
    {}

    private static final String MAPPING =
        "<?xml version='1.0'?>\n"+
        "<betwixt-config>\n"+
        "  <class name='"+Container.class.getName()+"'>\n"+
        "    <element name='container'>\n"+
        "      <element property='elements' updater='addElement'/>\n"+
        "    </element>\n"+
        "  </class>\n"+
        "  <class name='"+Element.class.getName()+"'>\n"+
        "    <element name='element'>\n"+
        "      <element property='subElements' updater='addSubElement'/>\n"+
        "    </element>\n"+
        "  </class>\n"+
        "  <class name='"+SubElementA.class.getName()+"'>\n"+
        "    <element name='subElementA'/>\n"+
        "  </class>\n"+
        "  <class name='"+SubElementB.class.getName()+"'>\n"+
        "    <element name='subElementB'/>\n"+
        "  </class>\n"+
        "</betwixt-config>";
    private static final String INVALID_XML =
        "<?xml version=\"1.0\" ?>\n"+
        "  <container>\n"+
        "    <subElementB/>\n"+
        "  </container>\n";

    public TestCollectionMapping2(String testName)
    {
        super(testName);
    }

    public void testInvalidXML() throws IOException, IntrospectionException, SAXException
    {
        BeanReader beanReader = new BeanReader();

        beanReader.registerMultiMapping(new InputSource(new StringReader(MAPPING)));

        StringReader xmlReader = new StringReader(INVALID_XML);
        Container     database  = (Container) beanReader.parse(xmlReader);

        // either we get an exception in the parse method (would perhaps be better)
        // or the collection is empty (SubElementB cannot be added to Container)
        assertFalse(database.getElements().hasNext());
    }
}
