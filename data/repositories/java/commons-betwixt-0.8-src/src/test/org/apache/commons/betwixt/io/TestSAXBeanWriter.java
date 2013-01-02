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
package org.apache.commons.betwixt.io;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.PersonBean;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Test harness for SAXBeanWriter.
 *
 * @author <a href="mailto:contact@hdietrich.net">Harald Dietrich</a>
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Id: TestSAXBeanWriter.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestSAXBeanWriter extends AbstractTestCase
{

    public static final String XML = "<?xml version='1.0'?><PersonBean id='1'><age>35</age><name>John Smith</name></PersonBean>";

    public TestSAXBeanWriter(String name)
    {
        super(name);
    }

    public void testWrite() throws Exception
    {
        PersonBean bean = new PersonBean(35, "John Smith");

        // writer bean into string
        StringWriter out = new StringWriter();

        //SimpleLog log = new SimpleLog("[TestWrite:SAXBeanWriter]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        SAXBeanWriter writer = new SAXBeanWriter(new SAXContentHandler(out));
        //writer.setLog(log);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(bean);
        String beanString = out.getBuffer().toString();
        String xml = "<?xml version='1.0'?><PersonBean><age>35</age>"
                     + "<name>John Smith</name></PersonBean>";


        xmlAssertIsomorphicContent(
            parseString(xml),
            parseString(beanString),
            true);

        // test the result
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        factory.setIgnoringElementContentWhitespace(true);
        InputSource in = new InputSource();
        StringReader reader = new StringReader(beanString);
        in.setCharacterStream(reader);
        Document doc = builder.parse(in);
        assertNotNull("Document missing", doc);
        Element root = doc.getDocumentElement();
        assertNotNull("Document root missing", root);
        assertEquals("Document root name wrong", "PersonBean", root.getNodeName());
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++)
        {
            Node child = children.item(i);
            if (child.getNodeName().equals("age"))
            {
                assertNotNull("Person age missing", child.getFirstChild());
                assertEquals("Person age wrong", "35", child.getFirstChild().getNodeValue().trim());
            }
            else if (child.getNodeName().equals("name"))
            {
                assertNotNull("Person name missing", child.getFirstChild());
                assertEquals("Person name wrong", "John Smith", child.getFirstChild().getNodeValue().trim());
            }
            else
            {
                if (child.getNodeName().equals("#text"))
                {
                    // now check if the textNode is empty after a trim.
                    String value = child.getNodeValue();
                    if (value != null)
                    {
                        value = value.trim();
                    }
                    if (value.length() != 0)
                    {
                        fail("Text should not contain content in node " + child.getNodeName());
                    }
                }
                else
                {
                    fail("Invalid node " + child.getNodeName());
                }

            }
        }
    }

    public void testDocumentElements() throws Exception
    {

        class TestDocHandler extends DefaultHandler
        {

            boolean startCalled = false;
            boolean endCalled = false;

            public void startDocument()
            {
                startCalled = true;
            }

            public void endDocument()
            {
                endCalled = true;
            }

        }

        PersonBean bean = new PersonBean(35, "John Smith");

        TestDocHandler handler = new TestDocHandler();
        SAXBeanWriter writer = new SAXBeanWriter(handler);
        writer.setCallDocumentEvents(true);
        writer.write(bean);

        assertEquals("Start not called", handler.startCalled , true);
        assertEquals("End not called", handler.endCalled , true);

        handler = new TestDocHandler();
        writer = new SAXBeanWriter(handler);
        writer.setCallDocumentEvents(false);
        writer.write(bean);

        assertEquals("Start called", handler.startCalled , false);
        assertEquals("End called", handler.endCalled , false);
    }

    /** This tests whether local names and qNames match */
    public void testLocalNames() throws Exception
    {

        class TestNames extends DefaultHandler
        {
            boolean namesMatch = true;

            public void startElement(String uri, String localName, String qName, Attributes attributes)
            {
                if (!localName.equals(qName))
                {
                    namesMatch = false;
                }

                for (int i=0, size=attributes.getLength(); i<size; i++)
                {
                    if (!attributes.getLocalName(i).equals(attributes.getQName(i)))
                    {
                        namesMatch = false;
                    }
                }
            }

            public void endElement(String uri, String localName, String qName)
            {
                if (!localName.equals(qName))
                {
                    namesMatch = false;
                }
            }
        }

        PersonBean bean = new PersonBean(24, "vikki");
        TestNames testHandler = new TestNames();
        SAXBeanWriter writer = new SAXBeanWriter(testHandler);
        writer.write(bean);

        assertEquals("Local names match QNames", testHandler.namesMatch, true);
    }


    public static Test suite()
    {
        return new TestSuite(TestSAXBeanWriter.class);
    }

    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }
}
