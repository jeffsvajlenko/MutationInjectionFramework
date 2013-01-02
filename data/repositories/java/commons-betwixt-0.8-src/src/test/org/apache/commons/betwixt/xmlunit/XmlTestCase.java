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

package org.apache.commons.betwixt.xmlunit;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.apache.xerces.parsers.SAXParser;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
  * Provides xml test utilities.
  * Hopefully, these might be moved into [xmlunit] sometime.
  *
  * @author Robert Burrell Donkin
  * @author Khaled Noaman, IBM (some portions derived from test code originally contributed to the Apache Xerces project)
  */
public class XmlTestCase extends TestCase
{

    private static final String NAMESPACES_FEATURE_ID
        = "http://xml.org/sax/features/namespaces";

    private static final String NAMESPACE_PREFIXES_FEATURE_ID
        = "http://xml.org/sax/features/namespace-prefixes";

    private static final String VALIDATION_FEATURE_ID
        = "http://xml.org/sax/features/validation";

    private static final String SCHEMA_VALIDATION_FEATURE_ID
        = "http://apache.org/xml/features/validation/schema";

    private static final String SCHEMA_FULL_CHECKING_FEATURE_ID
        = "http://apache.org/xml/features/validation/schema-full-checking";

    private static final String DYNAMIC_VALIDATION_FEATURE_ID
        = "http://apache.org/xml/features/validation/dynamic";

    private static final String NONAMESPACE_SCHEMA_LOCATION_PROPERTY_ID
        = "http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation";


    protected static boolean debug = false;

    DocumentBuilderFactory domFactory;


    public XmlTestCase(String testName)
    {
        super(testName);
    }


    public void xmlAssertIsomorphicContent(
        org.w3c.dom.Document documentOne,
        org.w3c.dom.Document documentTwo)
    throws
        AssertionFailedError
    {
        log("Testing documents:" + documentOne.getDocumentElement().getNodeName()
            + " and " + documentTwo.getDocumentElement().getNodeName());
        xmlAssertIsomorphicContent(documentOne, documentTwo, false);
    }

    public void xmlAssertIsomorphicContent(
        org.w3c.dom.Document documentOne,
        org.w3c.dom.Document documentTwo,
        boolean orderIndependent)
    throws
        AssertionFailedError
    {
        xmlAssertIsomorphicContent(null, documentOne, documentTwo, orderIndependent);
    }

    public void xmlAssertIsomorphicContent(
        String message,
        org.w3c.dom.Document documentOne,
        org.w3c.dom.Document documentTwo)
    throws
        AssertionFailedError
    {

        xmlAssertIsomorphicContent(message, documentOne, documentTwo, false);
    }

    public void xmlAssertIsomorphicContent(
        String message,
        org.w3c.dom.Document documentOne,
        org.w3c.dom.Document documentTwo,
        boolean orderIndependent)
    throws
        AssertionFailedError
    {
        // two documents have isomorphic content iff their root elements
        // are isomophic
        xmlAssertIsomorphic(
            message,
            documentOne.getDocumentElement(),
            documentTwo.getDocumentElement(),
            orderIndependent);
    }


    public void xmlAssertIsomorphic(
        org.w3c.dom.Node rootOne,
        org.w3c.dom.Node rootTwo)
    throws
        AssertionFailedError
    {
        xmlAssertIsomorphic(rootOne, rootTwo, false);
    }

    public void xmlAssertIsomorphic(
        org.w3c.dom.Node rootOne,
        org.w3c.dom.Node rootTwo,
        boolean orderIndependent)
    throws
        AssertionFailedError
    {
        xmlAssertIsomorphic(null, rootOne, rootTwo, orderIndependent);
    }

    public void xmlAssertIsomorphic(
        String message,
        org.w3c.dom.Node rootOne,
        org.w3c.dom.Node rootTwo)
    {

        xmlAssertIsomorphic(message, rootOne, rootTwo, false);

    }

    public void xmlAssertIsomorphic(
        String message,
        org.w3c.dom.Node rootOne,
        org.w3c.dom.Node rootTwo,
        boolean orderIndependent)
    throws
        AssertionFailedError
    {
        // first normalize the xml
        rootOne.normalize();
        rootTwo.normalize();
        // going to use recursion so avoid normalizing each time
        testIsomorphic(message, rootOne, rootTwo, orderIndependent);
    }


    private void testIsomorphic(
        String message,
        org.w3c.dom.Node nodeOne,
        org.w3c.dom.Node nodeTwo)
    throws
        AssertionFailedError
    {

        testIsomorphic(message, nodeOne, nodeTwo, false);
    }


    private void testIsomorphic(
        String message,
        org.w3c.dom.Node nodeOne,
        org.w3c.dom.Node nodeTwo,
        boolean orderIndependent)
    throws
        AssertionFailedError
    {
        try
        {
            if (debug)
            {
                log(
                    "node 1 name=" + nodeOne.getNodeName()
                    + " qname=" + nodeOne.getLocalName());
                log(
                    "node 2 name=" + nodeTwo.getNodeName()
                    + " qname=" + nodeTwo.getLocalName());
            }

            // compare node properties
            log("Comparing node properties");
            assertEquals(
                (null == message ? "(Unequal node types)" : message + "(Unequal node types)"),
                nodeOne.getNodeType(),
                nodeTwo.getNodeType());
            assertEquals(
                (null == message ? "(Unequal node names)" : message + "(Unequal node names)"),
                nodeOne.getNodeName(),
                nodeTwo.getNodeName());
            assertEquals(
                (null == message ? "(Unequal node values)" : message + "(Unequal node values)"),
                trim(nodeOne.getNodeValue()),
                trim(nodeTwo.getNodeValue()));
            assertEquals(
                (null == message ? "(Unequal local names)" : message + "(Unequal local names)"),
                nodeOne.getLocalName(),
                nodeTwo.getLocalName());
            assertEquals(
                (null == message ? "(Unequal namespace)" : message + "(Unequal namespace)"),
                nodeOne.getNamespaceURI(),
                nodeTwo.getNamespaceURI());


            // compare attributes
            log("Comparing attributes");
            // make sure both have them first
            assertEquals(
                (null == message ? "(Unequal attributes)" : message + "(Unequal attributes)"),
                nodeOne.hasAttributes(),
                nodeTwo.hasAttributes());
            if (nodeOne.hasAttributes())
            {
                // do the actual comparison
                // first we check the number of attributes are equal
                // we then check that for every attribute of node one,
                // a corresponding attribute exists in node two
                // (this should be sufficient to prove equality)
                NamedNodeMap attributesOne = nodeOne.getAttributes();
                NamedNodeMap attributesTwo = nodeTwo.getAttributes();

                assertEquals(
                    (null == message ? "(Unequal attributes)" : message + "(Unequal attributes)"),
                    attributesOne.getLength(),
                    attributesTwo.getLength());

                for (int i=0, size=attributesOne.getLength(); i<size; i++)
                {
                    Attr attributeOne = (Attr) attributesOne.item(i);
                    Attr attributeTwo = (Attr) attributesTwo.getNamedItemNS(
                                            attributeOne.getNamespaceURI(),
                                            attributeOne.getLocalName());
                    if (attributeTwo == null)
                    {
                        attributeTwo = (Attr) attributesTwo.getNamedItem(attributeOne.getName());
                    }

                    // check attribute two exists
                    if (attributeTwo == null)
                    {
                        String diagnosis = "[Missing attribute (" + attributeOne.getName() +  ")]";
                        fail((null == message ?  diagnosis : message + diagnosis));
                    }

                    // now check attribute values
                    assertEquals(
                        (null == message ? "(Unequal attribute values)" : message + "(Unequal attribute values)"),
                        attributeOne.getValue(),
                        attributeTwo.getValue());
                }
            }


            // compare children
            log("Comparing children");
            // this time order is important
            // so we can just go down the list and compare node-wise using recursion
            List listOne = sanitize(nodeOne.getChildNodes());
            List listTwo = sanitize(nodeTwo.getChildNodes());

            if (orderIndependent)
            {
                log("[Order Independent]");
                Comparator nodeByName = new NodeByNameComparator();
                Collections.sort(listOne, nodeByName);
                Collections.sort(listTwo, nodeByName);
            }

            Iterator it = listOne.iterator();
            Iterator iter2 = listTwo.iterator();
            while (it.hasNext() & iter2.hasNext())
            {
                Node nextOne = ((Node)it.next());
                Node nextTwo = ((Node)iter2.next());
                log(nextOne.getNodeName() + ":" + nextOne.getNodeValue());
                log(nextTwo.getNodeName() + ":" + nextTwo.getNodeValue());
            }

            assertEquals(
                (null == message ? "(Unequal child nodes@" + nodeOne.getNodeName() +")":
                 message + "(Unequal child nodes @" + nodeOne.getNodeName() +")"),
                listOne.size(),
                listTwo.size());

            it = listOne.iterator();
            iter2 = listTwo.iterator();
            while (it.hasNext() & iter2.hasNext())
            {
                Node nextOne = ((Node)it.next());
                Node nextTwo = ((Node)iter2.next());
                log(nextOne.getNodeName() + " vs " + nextTwo.getNodeName());
                testIsomorphic(message, nextOne, nextTwo, orderIndependent);

            }

        }
        catch (DOMException ex)
        {
            fail((null == message ? "" : message + " ") + "DOM exception" + ex.toString());
        }
    }


    protected DocumentBuilder createDocumentBuilder()
    {
        try
        {

            return getDomFactory().newDocumentBuilder();

        }
        catch (ParserConfigurationException e)
        {
            fail("Cannot create DOM builder: " + e.toString());

        }
        // just to keep the compiler happy
        return null;
    }

    protected DocumentBuilderFactory getDomFactory()
    {
        // lazy creation
        if (domFactory == null)
        {
            domFactory = DocumentBuilderFactory.newInstance();
        }

        return domFactory;
    }

    protected Document parseString(StringWriter writer)
    {
        try
        {

            return createDocumentBuilder().parse(new InputSource(new StringReader(writer.getBuffer().toString())));

        }
        catch (SAXException e)
        {
            fail("Cannot create parse string: " + e.toString());

        }
        catch (IOException e)
        {
            fail("Cannot create parse string: " + e.toString());

        }
        // just to keep the compiler happy
        return null;
    }

    protected Document parseString(String string)
    {
        try
        {

            return createDocumentBuilder().parse(new InputSource(new StringReader(string)));

        }
        catch (SAXException e)
        {
            fail("Cannot create parse string: " + e.toString());

        }
        catch (IOException e)
        {
            fail("Cannot create parse string: " + e.toString());

        }
        // just to keep the compiler happy
        return null;
    }


    protected Document parseFile(String path)
    {
        try
        {

            return createDocumentBuilder().parse(new File(path));

        }
        catch (SAXException e)
        {
            fail("Cannot create parse file: " + e.toString());

        }
        catch (IOException e)
        {
            fail("Cannot create parse file: " + e.toString());

        }
        // just to keep the compiler happy
        return null;
    }

    private void log(String message)
    {
        if (debug)
        {
            System.out.println("[XmlTestCase]" + message);
        }
    }


    private void log(String message, Exception e)
    {
        if (debug)
        {
            System.out.println("[XmlTestCase]" + message);
            e.printStackTrace();
        }
    }

    private String trim(String trimThis)
    {
        if (trimThis == null)
        {
            return trimThis;
        }

        return trimThis.trim();
    }

    private List sanitize(NodeList nodes)
    {
        ArrayList list = new ArrayList();

        for (int i=0, size=nodes.getLength(); i<size; i++)
        {
            if ( nodes.item(i).getNodeType() == Node.TEXT_NODE )
            {
                if ( !( nodes.item(i).getNodeValue() == null ||
                        nodes.item(i).getNodeValue().trim().length() == 0 ))
                {
                    list.add(nodes.item(i));
                }
                else
                {
                    log("Ignoring text node:" + nodes.item(i).getNodeValue());
                }
            }
            else
            {
                list.add(nodes.item(i));
            }
        }
        return list;
    }

    private class NodeByNameComparator implements Comparator
    {
        public int compare(Object objOne, Object objTwo)
        {
            String nameOne = ((Node) objOne).getNodeName();
            String nameTwo = ((Node) objTwo).getNodeName();

            if (nameOne == null)
            {
                if (nameTwo == null)
                {
                    return 0;
                }
                return -1;
            }

            if (nameTwo == null)
            {
                return 1;
            }

            return nameOne.compareTo(nameTwo);
        }
    }


    public void validateWithSchema(InputSource documentSource, final InputSource schemaSource)
    throws ParserConfigurationException, SAXException, IOException
    {
        class XMLUnitHandler extends DefaultHandler
        {
            ArrayList errors = new ArrayList();
            ArrayList warnings = new ArrayList();
            InputSource schemaSource;

            XMLUnitHandler(InputSource schemaSource)
            {
                this.schemaSource = schemaSource;
                schemaSource.setSystemId("schema.xsd");
            }

            public InputSource resolveEntity(String publicId, String systemId)
            {
                return schemaSource;
            }

            public void error(SAXParseException ex)
            {
                errors.add(ex);
            }

            public void warning(SAXParseException ex)
            {
                warnings.add(ex);
            }

            void reportErrors() throws SAXException
            {
                if (errors.size() > 0)
                {
                    throw (SAXException) errors.get(0);
                }
            }

        }

        // it's not all that good to have a concrete dependency on Xerces
        // and a particular version, at that.
        // but schema support in the Xerces series of parsers is variable
        // and some of the configuration details differ.
        // At least this way seems reliable
        SAXParser parser = new SAXParser();

        // Set features
        parser.setFeature(NAMESPACES_FEATURE_ID, true);
        parser.setFeature(NAMESPACE_PREFIXES_FEATURE_ID, false);
        parser.setFeature(VALIDATION_FEATURE_ID, true);
        parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, true);
        parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, false);
        parser.setFeature(DYNAMIC_VALIDATION_FEATURE_ID, false);

        // Set properties
        parser.setProperty(NONAMESPACE_SCHEMA_LOCATION_PROPERTY_ID, "schema.xsd");

        XMLUnitHandler handler = new XMLUnitHandler(schemaSource);

        // Set handlers
        parser.setContentHandler(handler);
        parser.setErrorHandler(handler);
        parser.setEntityResolver(handler);

        // parse document
        parser.parse(documentSource);
        handler.reportErrors();
    }

    public boolean isValid(InputSource documentSource, InputSource schemaSource)
    throws ParserConfigurationException, IOException
    {
        boolean result = false;
        try
        {
            validateWithSchema(documentSource, schemaSource);
            result = true;
        }
        catch (SAXException se)
        {
            log("Validation failed.", se);
        }

        return result;
    }


    public void xmlAssertIsValid(String document, String schema)
    throws ParserConfigurationException, IOException
    {
        xmlAssertIsValid(new InputSource(new StringReader(document)), new InputSource(new StringReader(schema)));
    }

    public void xmlAssertIsValid(InputSource documentSource, InputSource schemaSource)
    throws ParserConfigurationException, IOException
    {
        try
        {
            validateWithSchema(documentSource, schemaSource);
        }
        catch (SAXException se)
        {
            se.printStackTrace();
            fail("Validation failure: " + se.getMessage());
        }
    }
}

