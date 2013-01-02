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

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.examples.rss.Channel;
import org.apache.commons.betwixt.examples.rss.Image;
import org.apache.commons.betwixt.examples.rss.Item;
import org.apache.commons.betwixt.examples.rss.TextInput;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.xml.sax.InputSource;

/**
 * Tests for the validity of the schema produced.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestSchemaValidity extends AbstractTestCase
{

    public TestSchemaValidity(String name)
    {
        super(name);
    }

    private String generateSchema(Class clazz) throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        Schema schema = transcriber.generate(clazz);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();
        return xsd;
    }

    public void testSimplestBeanWithAttributes() throws Exception
    {
        String xsd = generateSchema(SimplestBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getXMLIntrospector().getConfiguration().getPrefixMapper().setPrefix(SchemaTranscriber.W3C_SCHEMA_INSTANCE_URI, "xsi");
        writer.getBindingConfiguration().setMapIDs(false);
        SimplestBean bean = new SimplestBean("Simon");
        writer.write(bean);

        String xml = out.getBuffer().toString();

        xmlAssertIsValid(new InputSource(new StringReader(xml)), new InputSource(new StringReader(xsd)));
    }


    public void testSimplestBeanWithElements() throws Exception
    {
        String xsd = generateSchema(SimplestElementBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getXMLIntrospector().getConfiguration().getPrefixMapper().setPrefix(SchemaTranscriber.W3C_SCHEMA_INSTANCE_URI, "xsi");
        writer.getBindingConfiguration().setMapIDs(false);
        SimplestElementBean bean = new SimplestElementBean("Simon");
        writer.write(bean);

        String xml = out.getBuffer().toString();

        xmlAssertIsValid(new InputSource(new StringReader(xml)), new InputSource(new StringReader(xsd)));
    }


    public void testSimpleBean() throws Exception
    {
        String xsd = generateSchema(SimpleBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getXMLIntrospector().getConfiguration().getPrefixMapper().setPrefix(SchemaTranscriber.W3C_SCHEMA_INSTANCE_URI, "xsi");
        writer.getBindingConfiguration().setMapIDs(false);
        SimpleBean bean = new SimpleBean("One", "Two", "A", "One, Two, Three, Four");
        writer.write(bean);

        String xml = out.getBuffer().toString();

        xmlAssertIsValid(new InputSource(new StringReader(xml)), new InputSource(new StringReader(xsd)));
    }

    private String generateOrderLineSchema() throws Exception
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
        return xsd;
    }

    public void testOrderLine() throws Exception
    {

        String xsd = generateOrderLineSchema();
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getXMLIntrospector().getConfiguration().setAttributeNameMapper(new HyphenatedNameMapper());
        writer.getXMLIntrospector().getConfiguration().getPrefixMapper().setPrefix(SchemaTranscriber.W3C_SCHEMA_INSTANCE_URI, "xsi");
        writer.getBindingConfiguration().setMapIDs(false);
        OrderLineBean bean = new OrderLineBean(3, new ProductBean("00112234", "A11", "Fat Fish", "A Fat Fish"));
        writer.write(bean);

        String xml = out.getBuffer().toString();

        xmlAssertIsValid(new InputSource(new StringReader(xml)), new InputSource(new StringReader(xsd)));
    }

    private String generateOrderSchema() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        transcriber.getXMLIntrospector().getConfiguration().setAttributeNameMapper(new HyphenatedNameMapper());
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        transcriber.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        Schema schema = transcriber.generate(OrderBean.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();
        return xsd;
    }

    public void testOrder() throws Exception
    {
        String xsd = generateOrderSchema();
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        writer.getXMLIntrospector().getConfiguration().setAttributeNameMapper(new HyphenatedNameMapper());
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        writer.getBindingConfiguration().setMapIDs(false);

        OrderBean bean = new OrderBean("XA-2231",
                                       new CustomerBean("PB34", "Mr Abbot", "1, Skipton Road","Shipley", "Merry England", "BD4 8KL"));
        bean.addLine(
            new OrderLineBean(4, new ProductBean("00112234", "A11", "Taylor's Landlord", "Taylor's Landlord")));
        bean.addLine(
            new OrderLineBean(5, new ProductBean("00112235", "A13", "Black Sheep Special", "Black Sheep Special")));
        writer.write(bean);

        String xml = out.getBuffer().toString();

        xmlAssertIsValid(new InputSource(new StringReader(xml)), new InputSource(new StringReader(xsd)));

    }


    private String generateRSSSchema() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        Schema schema = transcriber.generate(Channel.class);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(transcriber.createSchemaBindingConfiguration());
        writer.getXMLIntrospector().setConfiguration(transcriber.createSchemaIntrospectionConfiguration());
        writer.write(schema);

        String xsd = out.getBuffer().toString();
        return xsd;
    }

    public void testRSS() throws Exception
    {
        String xsd = generateRSSSchema();
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);

        Channel channel = new Channel();
        channel.setTitle("Betwixt News");
        channel.setLink("http://jakarta.apache.org/commons/betwixt");
        channel.setDescription("Example feed themed on Betwixt news.");
        channel.setRating("(PICS-1.1 'http://www.rsac.org/ratingsv01.html'" +
                          " 2 gen true comment 'RSACi North America Server'" +
                          " for 'http://www.rsac.org' on '1996.04.16T08:15-0500'" +
                          " r (n 0 s 0 v 0 l 0))");
        channel.setLanguage("en-UK");

        Image image = new Image();
        image.setTitle("Apache Feather");
        image.setURL("http://www.apache.org/images/asf_logo_wide.gif");
        image.setLink("http://www.apache.org");
        image.setWidth(100);
        image.setHeight(30);
        image.setDescription("Example image");
        channel.setImage(image);

        Item itemOne = new Item();
        itemOne.setTitle("Betwixt now generates w3c schema!");
        itemOne.setLink("http://jakarta.apache.org/commons/betwixt");
        itemOne.setDescription("Example description");
        channel.addItem(itemOne);

        Item itemTwo = new Item();
        itemTwo.setTitle("Another News Item");
        itemTwo.setLink("http://jakarta.apache.org/commons/betwixt");
        itemTwo.setDescription("Blah Blah Blah");
        channel.addItem(itemTwo);

        TextInput textInput = new TextInput();
        textInput.setTitle("Send");
        textInput.setDescription("Comments about Betwixt news");
        textInput.setName("Response text");
        textInput.setLink("http://jakarta.apache.org/commons/betwixt");
        channel.setTextInput(textInput);

        writer.write(channel);

        String xml = out.getBuffer().toString();

        xmlAssertIsValid(new InputSource(new StringReader(xml)), new InputSource(new StringReader(xsd)));

    }
}
