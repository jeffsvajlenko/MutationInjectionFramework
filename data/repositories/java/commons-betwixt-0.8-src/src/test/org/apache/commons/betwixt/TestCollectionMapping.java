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
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tests the multi-mapping of collections with polymorphic entries.
 *
 * @author Thomas Dudziak (tomdz@apache.org)
 */
public class TestCollectionMapping extends AbstractTestCase
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

    public static class ElementC
    {}

    private static final String MAPPING =
        "<?xml version=\"1.0\"?>\n"+
        "<betwixt-config>\n"+
        "  <class name=\"org.apache.commons.betwixt.TestCollectionMapping$Container\">\n"+
        "    <element name=\"container\">\n"+
        "      <element name=\"elements\">\n"+
        "        <element property=\"elements\" updater='addElement'/>\n"+
        "      </element>\n"+
        "    </element>\n"+
        "  </class>\n"+
        "  <class name=\"org.apache.commons.betwixt.TestCollectionMapping$ElementA\">\n"+
        "    <element name=\"elementA\"/>\n"+
        "  </class>\n"+
        "  <class name=\"org.apache.commons.betwixt.TestCollectionMapping$ElementB\">\n"+
        "    <element name=\"elementB\"/>\n"+
        "  </class>\n"+
        "  <class name=\"org.apache.commons.betwixt.TestCollectionMapping$ElementC\">\n"+
        "    <element name=\"elementC\"/>\n"+
        "  </class>\n"+
        "</betwixt-config>";
    private static final String EXPECTED =
        "<?xml version=\"1.0\" ?>\n"+
        "  <container>\n"+
        "    <elements>\n"+
        "      <elementB/>\n"+
        "      <elementA/>\n"+
        "    </elements>\n"+
        "  </container>\n";
    private static final String INVALID_XML =
        "<?xml version=\"1.0\" ?>\n"+
        "  <container>\n"+
        "    <elements>\n"+
        "      <elementC/>\n"+
        "    </elements>\n"+
        "  </container>\n";

    public TestCollectionMapping(String testName)
    {
        super(testName);
    }

    public void testRoundTripWithSingleMappingFile() throws IOException, SAXException, IntrospectionException
    {
        Container container = new Container();

        container.addElement(new ElementB());
        container.addElement(new ElementA());

        StringWriter outputWriter = new StringWriter();

        outputWriter.write("<?xml version=\"1.0\" ?>\n");

        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.setWriteEmptyElements(true);
        beanWriter.getBindingConfiguration().setMapIDs(false);
        beanWriter.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        beanWriter.setEndOfLine("\n"); //force to \n so expected values match for sure
        beanWriter.write(container);

        String output = outputWriter.toString();

        assertEquals(EXPECTED, output);

        BeanReader beanReader = new BeanReader();

        beanReader.registerMultiMapping(new InputSource(new StringReader(MAPPING)));

        StringReader xmlReader = new StringReader(output);

        container = (Container)beanReader.parse(xmlReader);

        Iterator it = container.getElements();

        assertTrue(it.next() instanceof ElementB);
        assertTrue(it.next() instanceof ElementA);
        assertFalse(it.hasNext());
    }

    public void testInvalidXML() throws IOException, IntrospectionException, SAXException
    {
        BeanReader beanReader = new BeanReader();

        beanReader.registerMultiMapping(new InputSource(new StringReader(MAPPING)));

        StringReader xmlReader = new StringReader(INVALID_XML);
        Container    container = (Container)beanReader.parse(xmlReader);

        // either we get an exception in the parse method (would perhaps be better)
        // or the collection is empty (ElementC cannot be added)
        assertFalse(container.getElements().hasNext());
    }
}
