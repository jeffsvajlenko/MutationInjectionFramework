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
package org.apache.commons.betwixt.schema;

import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestRecursiveBeanSchemaGeneration extends AbstractTestCase
{

    public TestRecursiveBeanSchemaGeneration(String name)
    {
        super(name);
    }

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    /**
     * A unit test suite for JUnit
     */
    public static Test suite()
    {
        return new TestSuite(TestRecursiveBeanSchemaGeneration.class);
    }

    public void testLoopBeanWithAttributes() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        Schema schema = transcriber.generate(LoopBean.class);
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);
        String xsd = out.getBuffer().toString();

        //The expected schema is manual generated, may not be completely match the betwixt generated
        String expected ="<?xml version='1.0'?><xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                         "<xsd:element name='LoopBean' type='org.apache.commons.betwixt.schema.LoopBean'/>" +
                         "<xsd:complexType name='org.apache.commons.betwixt.schema.LoopBean'>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='friend' type='org.apache.commons.betwixt.schema.LoopBean' minOccurs='0' maxOccurs='1'/>" +
                         "</xsd:sequence>" +
                         "<xsd:attribute name='name' type='xsd:string'/>" +
                         "</xsd:complexType>" +
                         "</xsd:schema>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(xsd));

        LoopBean loopBean = new LoopBean("Harry");
        loopBean.setFriend(new LoopBean("Sally"));

        out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.write(loopBean);

        String xml = out.getBuffer().toString();

        xmlAssertIsValid(xml, xsd);
    }

    public void testCyclicBean() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        Schema schema  = transcriber.generate(CyclicBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());

        writer.write(schema);

        String xsd = out.getBuffer().toString();

        String expected ="<?xml version='1.0'?><xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                         "<xsd:element name='CyclicBean' type='org.apache.commons.betwixt.schema.CyclicBean'/>" +
                         "<xsd:complexType name='org.apache.commons.betwixt.schema.CyclicBean'>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='layers' minOccurs='0' maxOccurs='1'>" +
                         "<xsd:complexType>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='layer' type='org.apache.commons.betwixt.schema.CyclicLayer' minOccurs='0' maxOccurs='unbounded'/>" +
                         "</xsd:sequence>" +
                         "</xsd:complexType>" +
                         "</xsd:element>" +
                         "</xsd:sequence>" +
                         "<xsd:attribute name='name' type='xsd:string'/>" +
                         "</xsd:complexType>" +
                         "<xsd:complexType name='org.apache.commons.betwixt.schema.CyclicLayer'>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='columns' minOccurs='0' maxOccurs='1'>" +
                         "<xsd:complexType>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='column' type='org.apache.commons.betwixt.schema.CyclicColumn' minOccurs='0' maxOccurs='unbounded'/>" +
                         "</xsd:sequence>" +
                         "</xsd:complexType>" +
                         "</xsd:element>" +
                         "</xsd:sequence>" +
                         "<xsd:attribute name='name' type='xsd:string'/>" +
                         "</xsd:complexType>" +
                         "<xsd:complexType name='org.apache.commons.betwixt.schema.CyclicColumn'>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='bean' type='org.apache.commons.betwixt.schema.CyclicBean' minOccurs='0' maxOccurs='1'/>" +
                         "</xsd:sequence>" +
                         "<xsd:attribute name='name' type='xsd:string'/>" +
                         "</xsd:complexType>" +
                         "</xsd:schema>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(xsd));
    }
}
