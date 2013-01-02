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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;

/**
 * Tests for the generation of schema from the object models.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestSchemaGeneration extends AbstractTestCase
{

    public TestSchemaGeneration(String name)
    {
        super(name);
    }

    public void testSimplestBeanWithAttributes() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        Schema schema = transcriber.generate(SimplestBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();

        String expected ="<?xml version='1.0'?><xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                         "<xsd:element name='SimplestBean' type='org.apache.commons.betwixt.schema.SimplestBean'/>" +
                         "<xsd:complexType name='org.apache.commons.betwixt.schema.SimplestBean'>" +
                         "<xsd:sequence/>" +
                         "<xsd:attribute name='name' type='xsd:string'/>" +
                         "</xsd:complexType>" +
                         "</xsd:schema>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(xsd));
    }


    public void testSimplestBeanWithElement() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        Schema schema = transcriber.generate(SimplestElementBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();

        String expected ="<?xml version='1.0'?><xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                         "<xsd:element name='SimplestBean' type='org.apache.commons.betwixt.schema.SimplestElementBean'/>" +
                         "<xsd:complexType name='org.apache.commons.betwixt.schema.SimplestElementBean'>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='name' type='xsd:string' minOccurs='0' maxOccurs='1'/>" +
                         "</xsd:sequence>" +
                         "</xsd:complexType>" +
                         "</xsd:schema>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(xsd));
    }

    public void testSimpleBean() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        Schema schema = transcriber.generate(SimpleBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();

        String expected ="<?xml version='1.0'?><xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                         "<xsd:element name='simple' type='org.apache.commons.betwixt.schema.SimpleBean'/>" +
                         "<xsd:complexType name='org.apache.commons.betwixt.schema.SimpleBean'>" +
                         "<xsd:sequence>" +
                         "<xsd:element name='three' type='xsd:string' minOccurs='0' maxOccurs='1'/>" +
                         "<xsd:element name='four' type='xsd:string' minOccurs='0' maxOccurs='1'/>" +
                         "</xsd:sequence>" +
                         "<xsd:attribute name='one' type='xsd:string'/>" +
                         "<xsd:attribute name='two' type='xsd:string'/>" +
                         "</xsd:complexType>" +
                         "</xsd:schema>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(xsd));
    }


    public void testOrderLineBean() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        transcriber.getXMLIntrospector().getConfiguration().setAttributeNameMapper(new HyphenatedNameMapper());
        Schema schema = transcriber.generate(OrderLineBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();

        // different JVMs may return different orders during reflection
        StringBuffer buffer = new StringBuffer("<?xml version='1.0'?><xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                                               "<xsd:element name='OrderLineBean' type='org.apache.commons.betwixt.schema.OrderLineBean'/>" +
                                               "<xsd:complexType name='org.apache.commons.betwixt.schema.OrderLineBean'>" +
                                               "<xsd:sequence>" +
                                               "<xsd:element name='product' type='org.apache.commons.betwixt.schema.ProductBean' minOccurs='0' maxOccurs='1'/>" +
                                               "</xsd:sequence>" +
                                               "<xsd:attribute name='quantity' type='xsd:string'/>" +
                                               "</xsd:complexType>" +
                                               "<xsd:complexType name='org.apache.commons.betwixt.schema.ProductBean'>" +
                                               "<xsd:sequence/>");

        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(ProductBean.class).getPropertyDescriptors();
        for (int i=0; i<propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if ("barcode".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='barcode' type='xsd:string'/>");
            }
            else if ("code".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='code' type='xsd:string'/>");
            }
            else if ("displayName".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='display-name' type='xsd:string'/>");
            }
            else if ("name".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='name' type='xsd:string'/>");
            }
        }
        buffer.append("</xsd:complexType>" +
                      "</xsd:schema>");

        String expected = buffer.toString();

        xmlAssertIsomorphicContent(parseString(expected), parseString(xsd), true);
    }

    public void testOrder() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration()
        .setElementNameMapper(new HyphenatedNameMapper());
        transcriber.getXMLIntrospector().getConfiguration()
        .setAttributeNameMapper(new HyphenatedNameMapper());
        transcriber.getXMLIntrospector().getConfiguration()
        .setAttributesForPrimitives(true);
        transcriber.getXMLIntrospector().getConfiguration()
        .setWrapCollectionsInElement(false);
        Schema schema = transcriber.generate(OrderBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber
                                       .createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(
            transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();

        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(
                    OrderBean.class).getPropertyDescriptors();
        boolean linesFirst = false;
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if ("lines".equals(propertyDescriptor.getName()))
            {
                linesFirst = true;
                break;
            }
            else if ("customer".equals(propertyDescriptor.getName()))
            {
                linesFirst = false;
                break;
            }
        }


        StringBuffer buffer = new StringBuffer(
            "<?xml version='1.0'?><xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>"
            + "<xsd:element name='order-bean' type='org.apache.commons.betwixt.schema.OrderBean'/>"
            + ""
            + "<xsd:complexType name='org.apache.commons.betwixt.schema.OrderBean'>"
            + "	<xsd:sequence>");

        if (linesFirst)
        {
            buffer.append("     <xsd:element name='line' type='org.apache.commons.betwixt.schema.OrderLineBean' minOccurs='0' maxOccurs='unbounded'/>");
            buffer.append("        <xsd:element name='customer' type='org.apache.commons.betwixt.schema.CustomerBean' minOccurs='0' maxOccurs='1'/>");
        }
        else
        {
            buffer.append("        <xsd:element name='customer' type='org.apache.commons.betwixt.schema.CustomerBean' minOccurs='0' maxOccurs='1'/>");
            buffer.append("     <xsd:element name='line' type='org.apache.commons.betwixt.schema.OrderLineBean' minOccurs='0' maxOccurs='unbounded'/>");
        }

        buffer.append("	</xsd:sequence>"
                      + "	<xsd:attribute name='code' type='xsd:string'/>"
                      + "</xsd:complexType>"
                      + "");

        if (linesFirst)
        {
            writeExpectedOrderLineBeanType(buffer);
            writeExpectedCustomerBeanType(buffer);
        }
        else
        {
            writeExpectedCustomerBeanType(buffer);
            writeExpectedOrderLineBeanType(buffer);
        }

        buffer.append("</xsd:schema>");

        String expected = buffer.toString();

        xmlAssertIsomorphicContent(parseString(xsd), parseString(expected), true);
    }

    /**
     * @param buffer
     * @throws IntrospectionException
     */
    private void writeExpectedOrderLineBeanType(StringBuffer buffer) throws IntrospectionException
    {
        PropertyDescriptor[] propertyDescriptors;
        buffer.append("<xsd:complexType name='org.apache.commons.betwixt.schema.OrderLineBean'>"
                      + "	<xsd:sequence>"
                      + "		<xsd:element name='product' type='org.apache.commons.betwixt.schema.ProductBean' minOccurs='0' maxOccurs='1'/>"
                      + "	</xsd:sequence>"
                      + "	<xsd:attribute name='quantity' type='xsd:string'/>"
                      + "</xsd:complexType>"
                      + ""
                      + "<xsd:complexType name='org.apache.commons.betwixt.schema.ProductBean'>"
                      + "	<xsd:sequence/>");

        propertyDescriptors = Introspector.getBeanInfo(ProductBean.class)
                              .getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if ("barcode".equals(propertyDescriptor.getName()))
            {
                buffer
                .append("<xsd:attribute name='barcode' type='xsd:string'/>");
            }
            else if ("code".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='code' type='xsd:string'/>");
            }
            else if ("displayName".equals(propertyDescriptor.getName()))
            {
                buffer
                .append("<xsd:attribute name='display-name' type='xsd:string'/>");
            }
            else if ("name".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='name' type='xsd:string'/>");
            }
        }
        buffer.append("	</xsd:complexType>");
    }

    /**
     * @param buffer
     * @throws IntrospectionException
     */
    private void writeExpectedCustomerBeanType(StringBuffer buffer) throws IntrospectionException
    {
        PropertyDescriptor[] propertyDescriptors;
        buffer.append("<xsd:complexType name='org.apache.commons.betwixt.schema.CustomerBean'>"
                      + "	<xsd:sequence/>");

        propertyDescriptors = Introspector.getBeanInfo(CustomerBean.class)
                              .getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++)
        {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            if ("code".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='code' type='xsd:string'/>");
            }
            else if ("country".equals(propertyDescriptor.getName()))
            {
                buffer
                .append("<xsd:attribute name='country' type='xsd:string'/>");
            }
            else if ("name".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='name' type='xsd:string'/>");
            }
            else if ("postcode".equals(propertyDescriptor.getName()))
            {
                buffer
                .append("<xsd:attribute name='postcode' type='xsd:string'/>");
            }
            else if ("street".equals(propertyDescriptor.getName()))
            {
                buffer
                .append("<xsd:attribute name='street' type='xsd:string'/>");
            }
            else if ("town".equals(propertyDescriptor.getName()))
            {
                buffer.append("<xsd:attribute name='town' type='xsd:string'/>");
            }
        }

        buffer.append("</xsd:complexType>"
                      + "");
    }

}
