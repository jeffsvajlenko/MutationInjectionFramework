package org.apache.commons.betwixt.scarab;

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

import java.io.FileInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;

/**
 * Test harness which round trips a Scarab's settings xml file
 *
 * @author <a href="mailto:jason@zenplex.com">Jason van Zyl</a>
 * @version $Id: TestScarabSettings.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestScarabSettings extends AbstractTestCase
{
    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    /**
     * A unit test suite for JUnit
     */
    public static Test suite()
    {
        return new TestSuite(TestScarabSettings.class);
    }

    /**
     * Constructor for the TestScarabSettings object
     *
     * @param testName
     */
    public TestScarabSettings(String testName)
    {
        super(testName);
    }

    /**
     * Tests we can round trip from the XML -> bean -> XML -> bean. Ideally this
     * method should test both Project objects are identical
     */
    public void testRoundTrip()
    throws Exception
    {
        BeanReader reader = createBeanReader();

        ScarabSettings ss = (ScarabSettings) reader.parse(
                                new FileInputStream(getTestFile("src/test/org/apache/commons/betwixt/scarab/scarab-settings.xml")));

        // now lets output it to a buffer
        StringWriter buffer = new StringWriter();
        write(ss, buffer);

        // create a new BeanReader
        reader = createBeanReader();

        // now lets try parse the output sing the BeanReader
        String text = buffer.toString();

        System.out.println(text);

        /*
        ScarabSettings newScarabSettings = (ScarabSettings) reader.parse(new StringReader(text));

        // managed to parse it again!
        testScarabSettings(newScarabSettings);
        */
        testScarabSettings(ss);

        // #### should now test the old and new Project instances for equality.
    }


    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Description of the Method
     */
    protected BeanReader createBeanReader()
    throws Exception
    {
        BeanReader reader = new BeanReader();
        reader.setXMLIntrospector(createXMLIntrospector());
        reader.registerBeanClass(ScarabSettings.class);
        return reader;
    }

    /**
     * ### it would be really nice to move this somewhere shareable across Maven
     * / Turbine projects. Maybe a static helper method - question is what to
     * call it???
     */
    protected XMLIntrospector createXMLIntrospector()
    {
        XMLIntrospector introspector = new XMLIntrospector();

        // set elements for attributes to true
        introspector.getConfiguration().setAttributesForPrimitives(false);

        // wrap collections in an XML element
        //introspector.setWrapCollectionsInElement(true);

        // turn bean elements into lower case
        introspector.getConfiguration().setElementNameMapper(new HyphenatedNameMapper());

        return introspector;
    }

    /**
     * Tests the value of the Project object that has just been parsed
     */
    protected void testScarabSettings(ScarabSettings ss)
    throws Exception
    {
        List globalAttributes = ss.getGlobalAttributes();
        GlobalAttribute ga = (GlobalAttribute) globalAttributes.get(1);
        assertEquals("Functional area", ga.getName());

        List globalAttributeOptions = ga.getGlobalAttributeOptions();

        System.out.println( "GlobalAttribute: " + ga);
        System.out.println( "globalAttributeOptions: " + globalAttributeOptions);

        assertEquals(ga.getCreatedDate().getTimestamp(), "2002-05-31 13:29:27.0");

        assertEquals(globalAttributeOptions.size(), 2);
        GlobalAttributeOption gao = (GlobalAttributeOption) globalAttributeOptions.get(0);
        assertEquals("UI", gao.getChildOption());
        gao = (GlobalAttributeOption) globalAttributeOptions.get(1);
        assertEquals("Code", gao.getChildOption());

        List globalIssueTypes = ss.getGlobalIssueTypes();
        GlobalIssueType git = (GlobalIssueType) globalIssueTypes.get(0);
        assertEquals("Defect", git.getName());

        List modules = ss.getModules();
        Module m = (Module) modules.get(0);
        assertEquals("Source", m.getName());
    }

    /**
     * Description of the Method
     */
    protected void write(Object bean, Writer out)
    throws Exception
    {
        BeanWriter writer = new BeanWriter(out);
        writer.setXMLIntrospector(createXMLIntrospector());
        writer.setEndOfLine("\n");
        writer.enablePrettyPrint();
        writer.write(bean);
    }
}

