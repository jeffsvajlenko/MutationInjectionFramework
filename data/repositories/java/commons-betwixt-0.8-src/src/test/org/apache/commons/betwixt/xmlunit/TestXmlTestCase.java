package org.apache.commons.betwixt.xmlunit;

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

import java.io.File;
import java.io.FileInputStream;

import junit.framework.AssertionFailedError;

import org.xml.sax.InputSource;

/**
 * Test harness which test xml unit
 *
 * @author Robert Burrell Donkin
 * @version $Id: TestXmlTestCase.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestXmlTestCase extends XmlTestCase
{

    public TestXmlTestCase(String name)
    {
        super(name);
    }

    public void testXMLUnit() throws Exception
    {
        xmlAssertIsomorphicContent(
            parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example.xml"),
            parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example.xml"));
    }

    public void testXMLUnit2() throws Exception
    {
        boolean failed = false;
        try
        {
            xmlAssertIsomorphicContent(
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example.xml"),
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example-morphed.xml"),
                false);
            failed = true;
        }
        catch (AssertionFailedError er)
        {
            // this is expected
        }
        if (failed)
        {
            fail("Expected unit test to fail!");
        }
    }

    public void testXMLUnit3() throws Exception
    {
        boolean failed = false;
        try
        {
            xmlAssertIsomorphicContent(
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example.xml"),
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example-not.xml"));
            failed = true;
        }
        catch (AssertionFailedError er)
        {
            // this is expected
        }
        if (failed)
        {
            fail("Expected unit test to fail!");
        }
    }


    public void testXMLUnit4() throws Exception
    {
        xmlAssertIsomorphicContent(
            parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example.xml"),
            parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example-morphed.xml"),
            true);
    }


    public void testXMLUnit5() throws Exception
    {
        boolean failed = false;
        try
        {
            xmlAssertIsomorphicContent(
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example.xml"),
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/rss-example-not.xml"),
                true);
            failed = true;
        }
        catch (AssertionFailedError er)
        {
            // this is expected
        }
        if (failed)
        {
            fail("Expected unit test to fail!");
        }
    }


    public void testXMLUnit6() throws Exception
    {
        boolean failed = false;
        try
        {
            xmlAssertIsomorphicContent(
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/scarab-one.xml"),
                parseFile("src/test/org/apache/commons/betwixt/xmlunit/scarab-two.xml"),
                true);
            failed = true;
        }
        catch (AssertionFailedError er)
        {
            // this is expected
        }
        if (failed)
        {
            fail("Expected unit test to fail!");
        }
    }

    public void testValidateSchemaValidOne() throws Exception
    {
        String basedir = System.getProperty("basedir");
        InputSource document = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/valid.xml")));
        InputSource schema = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/test.xsd")));
        assertTrue(isValid(document, schema));
    }


    public void testValidateSchemaInvalidOne() throws Exception
    {
        String basedir = System.getProperty("basedir");
        InputSource document = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/invalid.xml")));
        InputSource schema = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/test.xsd")));
        assertFalse(isValid(document, schema));
    }

    public void testValidateSchemaValidTwo() throws Exception
    {
        String basedir = System.getProperty("basedir");
        InputSource document = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/valid-personnel-schema.xml")));
        InputSource schema = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/personnel.xsd")));
        assertTrue(isValid(document, schema));
    }


    public void testValidateSchemaInvalidTwo() throws Exception
    {
        String basedir = System.getProperty("basedir");
        InputSource document = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/invalid-personnel-schema.xml")));
        InputSource schema = new InputSource(new FileInputStream(
                new File(basedir,"src/test/org/apache/commons/betwixt/xmlunit/personnel.xsd")));
        assertFalse(isValid(document, schema));
    }

}
