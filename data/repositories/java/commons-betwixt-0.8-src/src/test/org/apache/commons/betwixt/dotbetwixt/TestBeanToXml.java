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

import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.apache.commons.betwixt.xmlunit.XmlTestCase;


/**
  * Provides xml test utilities.
  * Hopefully, these might be moved into [xmlunit] sometime.
  *
  * @author Robert Burrell Donkin
  */
public class TestBeanToXml extends XmlTestCase
{

//--------------------------------- Test Suite

    public static Test suite()
    {
        return new TestSuite(TestBeanToXml.class);
    }

//--------------------------------- Constructor

    public TestBeanToXml(String testName)
    {
        super(testName);
    }

//---------------------------------- Tests

    public void testOne() throws Exception
    {
        // THIS TEST FAILS IN MAVEN
        xmlAssertIsomorphicContent(
            parseFile("src/test/org/apache/commons/betwixt/dotbetwixt/rbean-result.xml"),
            parseFile("src/test/org/apache/commons/betwixt/dotbetwixt/rbean-result.xml"));
    }

    public void testSimpleBean() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0' encoding='UTF-8'?>");
//        SimpleLog log = new SimpleLog("[testSimpleBean:XMLIntrospector]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements( true );
//        writer.getXMLIntrospector().setLog(log);

//        log = new SimpleLog("[testSimpleBean:XMLIntrospectorHelper]");
//        XMLIntrospectorHelper.setLog(log);

        writer.getBindingConfiguration().setMapIDs(false);
        SimpleTestBean bean = new SimpleTestBean("alpha-value","beta-value","gamma-value");
        writer.write(bean);
        out.flush();
        String xml = out.toString();

        xmlAssertIsomorphicContent(
            parseFile("src/test/org/apache/commons/betwixt/dotbetwixt/simpletestone.xml"),
            parseString(xml));

    }

    public void testWriteRecursiveBean() throws Exception
    {
        /*
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0' encoding='UTF-8'?>");
        BeanWriter writer = new BeanWriter(out);
        RecursiveBean bean
            = new RecursiveBean(
                "alpha",
                new RecursiveBean(
                    "beta",
                    new RecursiveBean("gamma")));
        writer.setWriteIDs(false);
        writer.write(bean);
        out.flush();
        String xml = out.toString();

        if (debug) {
            System.out.println(xml);
        }


        xmlAssertIsomorphicContent(
                    parseFile("src/test/org/apache/commons/betwixt/dotbetwixt/rbean-result.xml"),
                    parseString(xml));
        */
    }

    /**
     * This tests that only well formed names for elements and attributes are allowed by .betwixt files
     */
    public void testBadDotBetwixtNames() throws Exception
    {
        // this will work by testing that the output is well formed

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0' encoding='UTF-8'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements( true );
        writer.write(new BadDotBetwixtNamesBean("one", "two"));

//        System.out.println(out.toString());

        // this should fail if the output is not well formed
        parseString(out.toString());
    }

    /** Test output of bean with mixed content */
    public void testMixedContent() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0' encoding='UTF-8'?>");
        BeanWriter writer = new BeanWriter( out );
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write( new MixedContentBean("First", "Last", "Always") );

        String xml = "<?xml version='1.0' encoding='UTF-8'?><foo version='1.0'>"
                     + "<bar version='First'>Fiddle sticks<baa>Last</baa>Always</bar></foo>";

        xmlAssertIsomorphicContent(
            parseString(xml),
            parseString(out.toString()));
    }

    /** Test output of bean with mixed content */
    public void testSimpleMixedContent() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0' encoding='UTF-8'?>");
        BeanWriter writer = new BeanWriter( out );
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write( new MixedContentOne("Life,", "The Universe And Everything", 42) );

        String xml = "<?xml version='1.0' encoding='UTF-8'?><deep-thought alpha='Life,' gamma='42'>"
                     + "The Universe And Everything</deep-thought>";

        xmlAssertIsomorphicContent(
            parseString(xml),
            parseString(out.toString()));
    }

    /** Tests basic use of an implementation for an interface */
    public void testBasicInterfaceImpl() throws Exception
    {
        ExampleBean bean = new ExampleBean("Alice");
        bean.addExample(new ExampleImpl(1, "Mad Hatter"));
        bean.addExample(new ExampleImpl(2, "March Hare"));
        bean.addExample(new ExampleImpl(3, "Dormouse"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0' encoding='UTF-8'?>");

        BeanWriter writer = new BeanWriter( out );
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);

        writer.write( bean );

        String xml = "<?xml version='1.0' encoding='UTF-8'?>"
                     + "<example-bean><name>Alice</name>"
                     + "<example><id>1</id><name>Mad Hatter</name></example>"
                     + "<example><id>2</id><name>March Hare</name></example>"
                     + "<example><id>3</id><name>Dormouse</name></example>"
                     + "</example-bean>";

        xmlAssertIsomorphicContent(
            parseString(xml),
            parseString(out.toString()),
            true);
    }
}

