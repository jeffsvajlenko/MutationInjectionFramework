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

package org.apache.commons.betwixt.recursion;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.io.CyclicReferenceException;

/**
 * This will test the recursive behaviour of betwixt.
 *
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt </a>
 * @version $Id: TestRecursion.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestRecursion extends AbstractTestCase
{

    public TestRecursion(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(TestRecursion.class);
    }

    /**
     * This will test reading a simple recursive xml file
     *
     */
    public void testReadwithCollectionsInElementRoundTrip() throws Exception
    {
        //SimpleLog log = new
        // SimpleLog("[testReadwithCollectionsInElementRoundTrip:XMLIntrospectorHelper]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //XMLIntrospectorHelper.setLog(log);

        //log = new
        // SimpleLog("[testReadwithCollectionsInElementRoundTrip:XMLIntrospector]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        XMLIntrospector intro = createXMLIntrospector();
        //intro.setLog(log);
        intro.getConfiguration().setWrapCollectionsInElement(true);

        //log = new
        // SimpleLog("[testReadwithCollectionsInElementRoundTrip:BeanReader]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        BeanReader reader = new BeanReader();
        reader.setXMLIntrospector(intro);
        //reader.setLog(log);
        reader.registerBeanClass(ElementBean.class);

        ElementBean bean = (ElementBean) reader
                           .parse(getTestFileURL("src/test/org/apache/commons/betwixt/recursion/recursion.xml"));

        List elements = bean.getElements();
        assertEquals("Root elements size", 2, elements.size());
        Element elementOne = (Element) elements.get(0);
        assertEquals("Element one name", "element1", elementOne.getName());
        Element elementTwo = (Element) elements.get(1);
        assertEquals("Element two name", "element2", elementTwo.getName());
        assertEquals("Element two children", 0, elementTwo.getElements().size());
        elements = elementOne.getElements();
        assertEquals("Element one children", 2, elements.size());
        Element elementOneOne = (Element) elements.get(0);
        assertEquals("Element one one name", "element11", elementOneOne
                     .getName());
        Element elementOneTwo = (Element) elements.get(1);
        assertEquals("Element one two name", "element12", elementOneTwo
                     .getName());
        assertEquals("Element one two children", 0, elementOneTwo.getElements()
                     .size());
        elements = elementOneOne.getElements();
        assertEquals("Element one one children", 2, elements.size());
        Element elementOneOneOne = (Element) elements.get(0);
        assertEquals("Element one one one name", "element111", elementOneOneOne
                     .getName());
        Element elementOneOneTwo = (Element) elements.get(1);
        assertEquals("Element one one two name", "element112", elementOneOneTwo
                     .getName());

        StringWriter buffer = new StringWriter();
        write(bean, buffer, true);

        String xml = "<?xml version='1.0'?><ElementBean><elements><element name='element1'>"
                     + "<elements><element name='element11'><elements><element name='element111'>"
                     + "<elements/></element><element name='element112'><elements/></element>"
                     + "</elements></element><element name='element12'><elements/></element>"
                     + "</elements></element><element name='element2'><elements/>"
                     + "</element></elements></ElementBean>";

        xmlAssertIsomorphic(parseString(xml), parseString(buffer.getBuffer()
                            .toString()), true);
    }

    /**
     * This will test reading a simple recursive xml file
     */
    public void testReadWithoutCollectionsInElementRoundTrip() throws Exception
    {
        //        SimpleLog log = new
        // SimpleLog("[testReadWithoutCollectionsInElementRoundTrip:BeanRuleSet]");
        //        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //        BeanRuleSet.setLog(log);

        //	log = new
        // SimpleLog("[testReadWithoutCollectionsInElementRoundTrip:XMLIntrospector]");
        //        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        XMLIntrospector intro = createXMLIntrospector();
        intro.getConfiguration().setWrapCollectionsInElement(false);
        //        intro.setLog(log);
        //        log = new
        // SimpleLog("[testReadWithoutCollectionsInElementRoundTrip:XMLIntrospectorHelper]");
        //        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //        XMLIntrospectorHelper.setLog(log);
        BeanReader reader = new BeanReader();
        //        log = new
        // SimpleLog("[testReadWithoutCollectionsInElementRoundTrip:BeanReader]");
        //        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //        reader.setLog(log);
        reader.setXMLIntrospector(intro);
        reader.registerBeanClass(ElementBean.class);
        ElementBean bean = (ElementBean) reader
                           .parse(getTestFileURL("src/test/org/apache/commons/betwixt/recursion/recursion2.xml"));
        List elements = bean.getElements();
        assertEquals("Number of elements in root bean", 2, elements.size());
        Element elementOne = (Element) bean.elements.get(0);
        assertEquals("First element name", "element1", elementOne.getName());
        Element elementTwo = (Element) bean.elements.get(1);
        assertEquals("Second element name", "element2", elementTwo.getName());

        elements = elementOne.getElements();
        assertEquals("Number of child elements in first element", 2, elements
                     .size());
        Element elementOneOne = (Element) elements.get(0);
        assertEquals("11 element name", "element11", elementOneOne.getName());
        Element elementOneTwo = (Element) elements.get(1);
        assertEquals("12 element name", "element12", elementOneTwo.getName());

        elements = elementOneOne.getElements();
        assertEquals("Number of child elements in element 11", 2, elements
                     .size());
        Element elementOneOneOne = (Element) elements.get(0);
        assertEquals("111 element name", "element111", elementOneOneOne
                     .getName());

        assertEquals("111 child elements ", 0, elementOneOneOne.getElements()
                     .size());

        Element elementOneOneTwo = (Element) elements.get(1);
        assertEquals("112 element name", "element112", elementOneOneTwo
                     .getName());
        assertEquals("112 child elements ", 0, elementOneOneTwo.getElements()
                     .size());

        elements = elementOneTwo.getElements();
        assertEquals("Number of child elements in element 12", 0, elements
                     .size());

        elements = elementTwo.getElements();
        assertEquals("Number of child elements in element 2", 0, elements
                     .size());

        StringWriter buffer = new StringWriter();
        buffer.write("<?xml version='1.0'?>");
        write(bean, buffer, false);

        String xml = "<ElementBean><element name='element1'><element name='element11'><element name='element111' />"
                     + "<element name='element112' /> </element><element name='element12' /> </element>"
                     + "<element name='element2' /> </ElementBean>";

        xmlAssertIsomorphic(parseString(xml), parseString(buffer.getBuffer()
                            .toString()), true);

    }

    /**
     * Opens a writer and writes an object model according to the retrieved bean
     */
    private void write(Object bean, Writer out, boolean wrapIt)
    throws Exception
    {
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements(true);
        writer.setXMLIntrospector(createXMLIntrospector());
        // specifies weather to use collection elements or not.
        writer.getXMLIntrospector().getConfiguration()
        .setWrapCollectionsInElement(wrapIt);
        // we don't want to write Id attributes to every element
        // we just want our opbject model written nothing more..
        writer.getBindingConfiguration().setMapIDs(false);
        // the source has 2 spaces indention and \n as line seperator.
        writer.setIndent("  ");
        writer.setEndOfLine("\n");
        writer.write(bean);
    }

    /**
     * Set up the XMLIntroSpector
     */
    protected XMLIntrospector createXMLIntrospector()
    {
        XMLIntrospector introspector = new XMLIntrospector();

        // set elements for attributes to true
        introspector.getConfiguration().setAttributesForPrimitives(true);
        introspector.getConfiguration().setWrapCollectionsInElement(false);

        return introspector;
    }

    /**
     */
    public void testBeanWithIdProperty() throws Exception
    {
        IdBean bean = new IdBean("Hello, World");
        bean.setNotId("Not ID");
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements(true);
        writer.getXMLIntrospector().getConfiguration()
        .setAttributesForPrimitives(true);
        writer.getBindingConfiguration().setMapIDs(true);
        writer.write(bean);

        String xml = "<?xml version='1.0'?><IdBean notId='Not ID' id='Hello, World'/>";

        xmlAssertIsomorphic(parseString(xml), parseString(out.getBuffer()
                            .toString()), true);
    }

    /**
     * Check that a cyclic reference exception is not thrown in this case
     */
    public void testCyclicReferenceStack1() throws Exception
    {
        Element alpha = new Element("Alpha");
        Element beta = new Element("Beta");
        Element gamma = new Element("Gamma");
        Element epsilon = new Element("Epsilon");

        alpha.addElement(beta);
        beta.addElement(gamma);
        gamma.addElement(epsilon);
        alpha.addElement(epsilon);

        StringWriter stringWriter = new StringWriter();
        BeanWriter writer = new BeanWriter(stringWriter);
        writer.setWriteEmptyElements(true);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(alpha);

        String xml = "<?xml version='1.0'?><Element><name>Alpha</name><elements><element>"
                     + "<name>Beta</name><elements><element><name>Gamma</name><elements>"
                     + "<element><name>Epsilon</name><elements/></element></elements>"
                     + "</element></elements></element><element><name>Epsilon</name>"
                     + "<elements/></element></elements></Element>";

        xmlAssertIsomorphic(parseString(xml), parseString(stringWriter
                            .getBuffer().toString()), true);
    }

    /**
     * This should throw a cyclic reference
     */
    public void testCyclicReferenceStack2() throws Exception
    {
        Element alpha = new Element("Alpha");
        Element beta = new Element("Beta");
        Element gamma = new Element("Gamma");
        Element epsilon = new Element("Epsilon");

        alpha.addElement(beta);
        beta.addElement(gamma);
        gamma.addElement(epsilon);
        epsilon.addElement(beta);

        StringWriter stringWriter = new StringWriter();
        BeanWriter writer = new BeanWriter(stringWriter);
        writer.setWriteEmptyElements(true);
        writer.getBindingConfiguration().setMapIDs(false);

        //SimpleLog log = new
        // SimpleLog("[testCyclicReferenceStack2:BeanWriter]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //writer.setLog(log);

        //log = new SimpleLog("[testCyclicReferenceStack2:BeanWriter]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //writer.setAbstractBeanWriterLog(log);

        try
        {
            writer.write(alpha);
            fail("Cycle was not detected!");

        }
        catch (CyclicReferenceException e)
        {
            // that's what we expected!
        }
    }

    /** Tests for a stack overflow bug */
    public void testRegisterOverflow() throws Exception
    {
        BeanReader reader = new BeanReader();
        try
        {
            reader.registerBeanClass(NorthWind.class);
        }
        catch (StackOverflowError e)
        {
            e.printStackTrace();
            fail("Expected registration to succeed");
        }
    }

    public void testRegisterOverflow2() throws Exception
    {
        BeanReader beanReader = new BeanReader();
        try
        {
            beanReader.registerBeanClass(PersonTest.class);
        }
        catch (StackOverflowError e)
        {
            e.printStackTrace();
            fail("Expected registration to succeed");
        }
    }

    public void testCycleReferences() throws Exception
    {
        PersonTest person = new PersonTest();
        person.setName("John Doe");
        AddressTest address = new AddressTest();
        address.setStreetAddress("1221 Washington Street");
        person.setAddress(address);
        ReferenceTest reference = new ReferenceTest();
        reference.setPerson(person);
        address.setReference(reference);

        StringWriter outputWriter = new StringWriter();

        outputWriter.write("<?xml version='1.0' ?>\n");
        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.getBindingConfiguration().setMapIDs(true);
        beanWriter.write(person);

        BeanReader beanReader = new BeanReader();
        beanReader.getBindingConfiguration().setMapIDs(true);

        // Configure the reader
        beanReader.registerBeanClass(PersonTest.class);
        beanReader.registerBeanClass(AddressTest.class);
        beanReader.registerBeanClass(ReferenceTest.class);

        String out = outputWriter.toString();
        StringReader xmlReader = new StringReader(out);

        //Parse the xml
        PersonTest result = (PersonTest) beanReader.parse(xmlReader);
        assertSame("Cycle did not result in the same reference", result, result
                   .getAddress().getReference().getPerson());

    }

}

