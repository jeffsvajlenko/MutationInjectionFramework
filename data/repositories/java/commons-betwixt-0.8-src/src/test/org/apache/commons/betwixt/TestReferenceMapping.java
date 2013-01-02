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

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tests the multi-mapping of polymorphic references.
 *
 * @author Thomas Dudziak (tomdz at apache.org)
 */
public class TestReferenceMapping extends AbstractTestCase
{
    public static class Container
    {
        private Element _element1;
        private Element _element2;

        public Element getElement1()
        {
            return _element1;
        }

        public void setElement1(Element element)
        {
            _element1 = element;
        }

        public Element getElement2()
        {
            return _element2;
        }

        public void setElement2(Element element)
        {
            _element2 = element;
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
        "  <class name=\"org.apache.commons.betwixt.TestReferenceMapping$Container\">\n"+
        "    <element name=\"container\">\n"+
        "      <element property=\"element1\"/>\n"+
        "      <element name=\"element2\" property=\"element2\"/>\n"+
        "    </element>\n"+
        "  </class>\n"+
        "  <class name=\"org.apache.commons.betwixt.TestReferenceMapping$ElementA\">\n"+
        "    <element name=\"elementA\"/>\n"+
        "  </class>\n"+
        "  <class name=\"org.apache.commons.betwixt.TestReferenceMapping$ElementB\">\n"+
        "    <element name=\"elementB\"/>\n"+
        "  </class>\n"+
        "</betwixt-config>";
    private static final String EXPECTED =
        "<?xml version=\"1.0\" ?>\n"+
        "  <container>\n"+
        "    <elementB/>\n"+
        "    <element2/>\n"+
        "  </container>\n";

    public TestReferenceMapping(String testName)
    {
        super(testName);
    }

    public void testRoundTripWithSingleMappingFile() throws IOException, SAXException, IntrospectionException
    {
        Container container = new Container();

        container.setElement1(new ElementB());
        container.setElement2(new ElementA());

        StringWriter outputWriter = new StringWriter();

        outputWriter.write("<?xml version=\"1.0\" ?>\n");

        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.setWriteEmptyElements(true);
        beanWriter.getBindingConfiguration().setMapIDs(false);
        beanWriter.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        beanWriter.setEndOfLine("\n"); //force to ensure matches on expected
        beanWriter.write(container);

        String output = outputWriter.toString();

        assertEquals(EXPECTED, output);

        BeanReader beanReader = new BeanReader();

        beanReader.registerMultiMapping(new InputSource(new StringReader(MAPPING)));

        StringReader xmlReader = new StringReader(output);

        container = (Container)beanReader.parse(xmlReader);

        assertTrue(container.getElement1() instanceof ElementB);
        // betwixt cannot know which type the element has
        assertNull(container.getElement2());
    }
}
