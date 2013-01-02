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

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;

/**
 * Tests for the SchemaTranscriber.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestSchemaTranscriber extends AbstractTestCase
{

    public TestSchemaTranscriber(String testName)
    {
        super(testName);
    }

    public void testEmpty() {}

    public void testSimplestBeanAttribute() throws Exception
    {
        Schema expected = new Schema();

        GlobalComplexType simplestBeanType = new GlobalComplexType();
        simplestBeanType.setName("org.apache.commons.betwixt.schema.SimplestBean");
        simplestBeanType.addAttribute(new Attribute("name", "xsd:string"));

        GlobalElement root = new GlobalElement("SimplestBean", "org.apache.commons.betwixt.schema.SimplestBean");
        expected.addComplexType(simplestBeanType);
        expected.addElement(root);

        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        Schema out = transcriber.generate(SimplestBean.class);

        assertEquals("Simplest bean schema", expected, out);
    }

    public void testSimplestBeanElement() throws Exception
    {
        Schema expected = new Schema();

        GlobalComplexType simplestBeanType = new GlobalComplexType();
        simplestBeanType.setName("org.apache.commons.betwixt.schema.SimplestElementBean");
        simplestBeanType.addElement(new SimpleLocalElement("name", "xsd:string"));

        GlobalElement root = new GlobalElement("SimplestBean", "org.apache.commons.betwixt.schema.SimplestElementBean");
        expected.addComplexType(simplestBeanType);
        expected.addElement(root);

        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        Schema out = transcriber.generate(SimplestElementBean.class);

        assertEquals("Simplest bean schema", expected, out);
    }

    public void testSimpleBean() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        Schema out = transcriber.generate(SimpleBean.class);

        Schema expected = new Schema();
        GlobalComplexType simpleBeanType = new GlobalComplexType();
        simpleBeanType.setName("org.apache.commons.betwixt.schema.SimpleBean");
        simpleBeanType.addAttribute(new Attribute("one", "xsd:string"));
        simpleBeanType.addAttribute(new Attribute("two", "xsd:string"));
        simpleBeanType.addElement(new SimpleLocalElement("three", "xsd:string"));
        simpleBeanType.addElement(new SimpleLocalElement("four", "xsd:string"));
        expected.addComplexType(simpleBeanType);
        expected.addElement(new GlobalElement("simple", "org.apache.commons.betwixt.schema.SimpleBean"));

        assertEquals("Simple bean schema", expected, out);

    }

    public void testOrderLine() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributeNameMapper(new HyphenatedNameMapper());
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        Schema out = transcriber.generate(OrderLineBean.class);

        Schema expected = new Schema();

        GlobalComplexType productBeanType = new GlobalComplexType();
        productBeanType.setName(ProductBean.class.getName());
        productBeanType.addAttribute(new Attribute("barcode", "xsd:string"));
        productBeanType.addAttribute(new Attribute("code", "xsd:string"));
        productBeanType.addAttribute(new Attribute("name", "xsd:string"));
        productBeanType.addAttribute(new Attribute("display-name", "xsd:string"));
        expected.addComplexType(productBeanType);

        GlobalComplexType orderLineType = new GlobalComplexType();
        orderLineType.setName(OrderLineBean.class.getName());
        orderLineType.addAttribute(new Attribute("quantity", "xsd:string"));
        orderLineType.addElement(new ElementReference("product", productBeanType));
        expected.addComplexType(orderLineType);
        expected.addElement(new GlobalElement("OrderLineBean", OrderLineBean.class.getName()));

        assertEquals("Transcriber schema", expected, out);
    }


    public void testOrder() throws Exception
    {
        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        transcriber.getXMLIntrospector().getConfiguration().setAttributeNameMapper(new HyphenatedNameMapper());
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        transcriber.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        Schema out = transcriber.generate(OrderBean.class);

        Schema expected = new Schema();


        GlobalComplexType customerBeanType = new GlobalComplexType();
        customerBeanType.setName(CustomerBean.class.getName());
        customerBeanType.addAttribute(new Attribute("code", "xsd:string"));
        customerBeanType.addAttribute(new Attribute("name", "xsd:string"));
        customerBeanType.addAttribute(new Attribute("street", "xsd:string"));
        customerBeanType.addAttribute(new Attribute("town", "xsd:string"));
        customerBeanType.addAttribute(new Attribute("country", "xsd:string"));
        customerBeanType.addAttribute(new Attribute("postcode", "xsd:string"));
        expected.addComplexType(customerBeanType);

        GlobalComplexType productBeanType = new GlobalComplexType();
        productBeanType.setName(ProductBean.class.getName());
        productBeanType.addAttribute(new Attribute("barcode", "xsd:string"));
        productBeanType.addAttribute(new Attribute("code", "xsd:string"));
        productBeanType.addAttribute(new Attribute("name", "xsd:string"));
        productBeanType.addAttribute(new Attribute("display-name", "xsd:string"));
        expected.addComplexType(productBeanType);

        GlobalComplexType orderLineType = new GlobalComplexType();
        orderLineType.setName(OrderLineBean.class.getName());
        orderLineType.addAttribute(new Attribute("quantity", "xsd:string"));
        orderLineType.addElement(new ElementReference("product", productBeanType));
        expected.addComplexType(orderLineType);

        GlobalComplexType orderType = new GlobalComplexType();
        orderType.setName(OrderBean.class.getName());
        orderType.addAttribute(new Attribute("code", "xsd:string"));
        orderType.addElement(new ElementReference("customer", customerBeanType));
        orderType.addElement(new ElementReference("line", orderLineType));
        expected.addComplexType(orderType);
        expected.addElement(new GlobalElement("order-bean", OrderBean.class.getName()));

        assertEquals("Transcriber schema", expected, out);
    }

}
