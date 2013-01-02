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

package org.apache.commons.betwixt.strategy;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.TestCase;

import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;


/**
 * Tests streaming/destreaming of an <code>Elements</code> bean,
 * a container for <code>Element</code> instances, using various name mappers
 * The objective of this is to verify that containers whose names
 * are plurals of their contents can be written and read back successfully.
 *
 * @author <a href="mailto:tima@intalio.com">Tim Anderson</a>
 */
public class TestElementsIO extends TestCase
{

//    private SimpleLog testLog;

    public TestElementsIO(String name)
    {
        super(name);
//        testLog = new SimpleLog("[TextElementsIO]");
//        testLog.setLevel(SimpleLog.LOG_LEVEL_TRACE);
    }

    public void testCapitalizeNameMapper() throws Exception
    {
//        testLog.debug("Testing capitalize name mapper");
        doTest(new CapitalizeNameMapper(), "capitalize name mapper");
    }

    public void testDecapitalizeNameMapper() throws Exception
    {
//        testLog.debug("Testing decapitalize name mapper");
        doTest(new DecapitalizeNameMapper(), "decapitalize name mapper");
    }

    public void testDefaultElementMapper() throws Exception
    {
//        testLog.debug("Testing default name mapper");
        doTest(new DefaultNameMapper(), "default name mapper");
    }

    public void testHyphenatedNameMapper() throws Exception
    {
//        testLog.debug("Testing hyphenated name mapper");
        doTest(new HyphenatedNameMapper(), "hyphenated name mapper");
    }

    private void doTest(NameMapper mapper, String testName) throws Exception
    {
        Elements elements = new Elements();
        elements.addElement(new Element("a"));
        elements.addElement(new Element("b"));
        elements.addElement(new Element("c"));

        StringWriter out = new StringWriter();
        BeanWriter writer = newBeanWriter(out, mapper);
        writer.setWriteEmptyElements(true);
        writer.write(elements);
        writer.flush();

        String xmlOut = out.toString();

//        testLog.debug(xmlOut);

        StringReader in = new StringReader(xmlOut);

//        SimpleLog log = new SimpleLog("[TextElementsIO:BeanReader]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        BeanReader reader = new BeanReader();
//        reader.setLog(log);

//        log = new SimpleLog("[TextElementsIO:BeanReader]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        BeanCreateRule.setLog(log);

        reader.setXMLIntrospector(newXMLIntrospector(mapper));
        reader.registerBeanClass(Elements.class);
        Elements result = (Elements) reader.parse(in);

        assertNotNull("Element 'a' is null (" + testName + ")", result.getElement("a"));
        assertNotNull("Element 'b' is null (" + testName + ")", result.getElement("b"));
        assertNotNull("Element 'c' is null (" + testName + ")", result.getElement("c"));
    }

    private BeanWriter newBeanWriter(Writer writer, NameMapper mapper)
    {
        BeanWriter result = new BeanWriter(writer);
        result.setWriteEmptyElements(true);

        result.setXMLIntrospector(newXMLIntrospector(mapper));
        result.setEndOfLine("\n");
        result.enablePrettyPrint();
        result.getBindingConfiguration().setMapIDs(false);
        return result;
    }

    private XMLIntrospector newXMLIntrospector(NameMapper mapper)
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setAttributesForPrimitives(true);
        introspector.getConfiguration().setWrapCollectionsInElement(false);
        introspector.getConfiguration().setElementNameMapper(mapper);
        return introspector;
    }
}

