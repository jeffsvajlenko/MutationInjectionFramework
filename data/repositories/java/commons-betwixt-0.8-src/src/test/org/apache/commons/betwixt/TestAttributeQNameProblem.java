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

import org.apache.commons.betwixt.io.SAXBeanWriter;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * I would SAX 'start element' event's attributes always expect to have qName
 * equal to localName for simple, unprefixed XML tags. But that seems not to be
 * true for betwixt output and breaks my application completely. <br>
 * For the debugging output to STDOUT I would expect output like:
 *
 * <pre>
 *   XML: start document event
 *   XML: start element qName 'test-class', localName 'test-class', URI:
 *        - Attribute qName 'test-prop-1', localName 'test-prop-1' of CDATA: abc
 *        - Attribute qName 'test-prop-2', localName 'test-prop-2' of CDATA: 12
 *        - Attribute qName 'id', localName 'id' of ID: 1
 *   XML: end element 'test-class'
 *   XML: end document event
 * </pre>
 *
 * but I get (the attributes local names differ from the qnames):
 *
 * <pre>
 *   XML: start document event
 *   XML: start element qName 'test-class', localName 'test-class', URI:
 *        - Attribute qName 'test-prop-1', localName 'testPropertyOne' of CDATA: abc
 * </pre>
 *
 * got only the first two lines here beacuase assertEquals fails there.
 *
 * @author Christoph Gaffga, cgaffga@triplemind.com
 */
public class TestAttributeQNameProblem extends AbstractTestCase
{

    public TestAttributeQNameProblem(String testName)
    {
        super(testName);
    }

    public static class StdOutContentHandler implements ContentHandler
    {

        public void setDocumentLocator(Locator locator) {}

        public void startDocument() throws SAXException
        {
            System.out.println("XML: start document event");
        }

        public void endDocument() throws SAXException
        {
            System.out.println("XML: end document event");
        }

        public void startPrefixMapping(String prefix, String uri) throws SAXException
        {
            System.out.println("XML: start prefix '" + prefix + "' mapping, URI: " + uri);
        }

        public void endPrefixMapping(String prefix) throws SAXException
        {
            System.out.println("XML: end prefix '" + prefix + "' mapping");
        }

        public void startElement(String uri, String localName, String qName, Attributes atts)
        throws SAXException
        {
            System.out.println("XML: start element qName '" + qName + "', localName '" + localName
                               + "', URI:" + uri);
            for (int i = 0; i < atts.getLength(); i++)
            {
                System.out.println("     - Attribute qName '" + atts.getQName(i) + "', localName '"
                                   + atts.getLocalName(i) + "' of " + atts.getType(i) + ": "
                                   + atts.getValue(i));
                assertEquals(atts.getQName(i), atts.getLocalName(i));
            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            System.out.println("XML: end element '" + qName + "'");
        }

        public void characters(char[] ch, int start, int length) throws SAXException
        {
            System.out.println("XML: characters: from " + start + ", length " + length);
        }

        public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}

        public void processingInstruction(String target, String data) throws SAXException
        {
            System.out.println("XML: processing instruction, target '" + target + "': " + data);
        }

        public void skippedEntity(String name) throws SAXException {}

    }

    public void testAttributeOutput()
    {
        try
        {
            SAXBeanWriter beanWriter = new SAXBeanWriter(new StdOutContentHandler());
            Object bean = new SimpleClass();
            beanWriter.write(bean);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
