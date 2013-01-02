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
package org.apache.commons.betwixt.dotbetwixt;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.InputSource;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestCustomDotBetwixt extends AbstractTestCase
{

    public TestCustomDotBetwixt(String testName)
    {
        super(testName);
    }

    public void testIntrospectWithCustomDotBetwixt() throws Exception
    {
        StringReader reader = new StringReader(
            "<?xml version='1.0' ?>" +
            "<info>" +
            "    <element name='jelly'>" +
            "        <element name='wibble' property='alpha'/>" +
            "        <element name='wobble' property='beta'/>" +
            "    </element>" +
            "</info>");
        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo xmlBeanInfo = introspector.introspect(SimpleTestBean.class, new InputSource(reader));

        ElementDescriptor elementDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Root is jelly", "jelly", elementDescriptor.getLocalName());
        ElementDescriptor[] childDescriptors = elementDescriptor.getElementDescriptors();
        assertEquals("Expected two child elements", 2, childDescriptors.length);
        assertEquals("Wibble comes first", "wibble", childDescriptors[0].getLocalName());
        assertEquals("Wobble comes last", "wobble", childDescriptors[1].getLocalName());

        reader = new StringReader(
            "<?xml version='1.0' ?>" +
            "<info>" +
            "    <element name='not-jelly'>" +
            "        <element name='no-wibble' property='alpha'/>" +
            "        <element name='no-wobble' property='beta'/>" +
            "    </element>" +
            "</info>");

        xmlBeanInfo = introspector.introspect(SimpleTestBean.class, new InputSource(reader));

        elementDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Root is not-jelly", "not-jelly", elementDescriptor.getLocalName());
        childDescriptors = elementDescriptor.getElementDescriptors();
        assertEquals("Expected two child elements", 2, childDescriptors.length);
        assertEquals("No wibble comes first", "no-wibble", childDescriptors[0].getLocalName());
        assertEquals("No wobble comes last", "no-wobble", childDescriptors[1].getLocalName());
    }


    public void testRegisterCustomDotBetwixt() throws Exception
    {
        StringReader reader = new StringReader(
            "<?xml version='1.0' ?>" +
            "<info>" +
            "    <element name='jelly'>" +
            "        <element name='wibble' property='alpha'/>" +
            "        <element name='wobble' property='beta'/>" +
            "    </element>" +
            "</info>");
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.register(SimpleTestBean.class, new InputSource(reader));
        XMLBeanInfo xmlBeanInfo = introspector.introspect(SimpleTestBean.class);

        ElementDescriptor elementDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Root is jelly", "jelly", elementDescriptor.getLocalName());
        ElementDescriptor[] childDescriptors = elementDescriptor.getElementDescriptors();
        assertEquals("Expected two child elements", 2, childDescriptors.length);
        assertEquals("Wibble comes first", "wibble", childDescriptors[0].getLocalName());
        assertEquals("Wobble comes last", "wobble", childDescriptors[1].getLocalName());
    }

    public void testWriteCustomDotBetwixt() throws Exception
    {
        StringReader reader = new StringReader(
            "<?xml version='1.0' ?>" +
            "<info>" +
            "    <element name='jelly'>" +
            "        <element name='wibble' property='alpha'/>" +
            "        <element name='wobble' property='beta'/>" +
            "    </element>" +
            "</info>");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        SimpleTestBean bean = new SimpleTestBean("one", "two", "three");

        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(bean, new InputSource(reader));

        String expected = "<?xml version='1.0'?>" +
                          "<jelly><wibble>one</wibble><wobble>two</wobble></jelly>";
        xmlAssertIsomorphic(parseString(expected), parseString(out));
    }


    public void testReadCustomDotBetwixt() throws Exception
    {
        String xml = "<?xml version='1.0'?>" +
                     "<jelly><wibble>one</wibble><wobble>two</wobble></jelly>";
        StringReader in = new StringReader(xml);

        StringReader dotBetwixt = new StringReader(
            "<?xml version='1.0' ?>" +
            "<info>" +
            "    <element name='jelly'>" +
            "        <element name='wibble' property='alpha'/>" +
            "        <element name='wobble' property='beta'/>" +
            "    </element>" +
            "</info>");

        BeanReader reader = new BeanReader();
        reader.getBindingConfiguration().setMapIDs(false);
        reader.registerBeanClass(new InputSource(dotBetwixt), SimpleTestBean.class);
        SimpleTestBean bean = (SimpleTestBean) reader.parse(in);
        assertNotNull("Bean not mapped", bean);
        assertEquals("Property alpha mapping", "one", bean.getAlpha());
        assertEquals("Property beta mapping", "two", bean.getBeta());
    }
}
