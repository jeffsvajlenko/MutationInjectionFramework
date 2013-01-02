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
 * Tests the mapping of a property to the inner text of an element.
 *
 * @author Thomas Dudziak (tomdz@apache.org)
 */
public class TestTextMapping extends AbstractTestCase
{
    public static class Element
    {
        private String value;

        public String getValue()
        {
            return value;
        }
        public void setValue(String value)
        {
            this.value = value;
        }
    }

    private static final String MAPPING =
        "<?xml version=\"1.0\"?>\n"+
        "<betwixt-config>\n"+
        "  <class name=\"org.apache.commons.betwixt.TestTextMapping$Element\">\n"+
        "    <element name=\"element\">\n"+
        "      <text property=\"value\"/>\n"+
        "    </element>\n"+
        "  </class>\n"+
        "</betwixt-config>";
    private static final String EXPECTED =
        "<?xml version=\"1.0\" ?>\n"+
        "  <element>Some text</element>\n";

    public TestTextMapping(String testName)
    {
        super(testName);
    }

    public void testRoundTripWithSingleMappingFile() throws IOException, SAXException, IntrospectionException
    {
        Element element = new Element();

        element.setValue("Some text");

        StringWriter outputWriter = new StringWriter();

        outputWriter.write("<?xml version=\"1.0\" ?>\n");

        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.setWriteEmptyElements(true);
        beanWriter.getBindingConfiguration().setMapIDs(false);
        beanWriter.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        beanWriter.setEndOfLine("\n"); //force to \n so expected values match for sure
        beanWriter.write(element);

        String output = outputWriter.toString();

        assertEquals(EXPECTED, output);

        BeanReader beanReader = new BeanReader();

        beanReader.registerMultiMapping(new InputSource(new StringReader(MAPPING)));

        StringReader xmlReader = new StringReader(output);

        element = (Element)beanReader.parse(xmlReader);

        assertEquals("Some text",
                     element.getValue());
    }

}
