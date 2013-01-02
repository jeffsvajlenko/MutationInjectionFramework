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

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;

/**
 * Tests for <code>DataTypeMapper</code>
 * both usages and implementations.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestDataTypeMapper extends TestCase
{

    public TestDataTypeMapper(String testName)
    {
        super(testName);
    }

    public void testDefaultDataTypeMapping() throws Exception
    {
        DefaultDataTypeMapper mapper = new DefaultDataTypeMapper();
        assertEquals("java.lang.String", "xsd:string", mapper.toXMLSchemaDataType(String.class));
        assertEquals("java.math.BigInteger", "xsd:integer", mapper.toXMLSchemaDataType(BigInteger.class));
        assertEquals("java.math.BigDecimal", "xsd:decimal", mapper.toXMLSchemaDataType(BigDecimal.class));
        assertEquals("Integer", "xsd:int", mapper.toXMLSchemaDataType(Integer.TYPE));
        assertEquals("int", "xsd:int", mapper.toXMLSchemaDataType(Integer.class));
        assertEquals("Long", "xsd:long", mapper.toXMLSchemaDataType(Long.TYPE));
        assertEquals("long", "xsd:long", mapper.toXMLSchemaDataType(Long.class));
        assertEquals("Short", "xsd:short", mapper.toXMLSchemaDataType(Short.TYPE));
        assertEquals("short", "xsd:short", mapper.toXMLSchemaDataType(Short.class));
        assertEquals("Float", "xsd:float", mapper.toXMLSchemaDataType(Float.TYPE));
        assertEquals("float", "xsd:float", mapper.toXMLSchemaDataType(Float.class));
        assertEquals("Double", "xsd:double", mapper.toXMLSchemaDataType(Double.TYPE));
        assertEquals("double", "xsd:double", mapper.toXMLSchemaDataType(Double.class));
        assertEquals("Boolean", "xsd:boolean", mapper.toXMLSchemaDataType(Boolean.TYPE));
        assertEquals("boolean", "xsd:boolean", mapper.toXMLSchemaDataType(Boolean.class));
        assertEquals("Byte", "xsd:byte", mapper.toXMLSchemaDataType(Byte.TYPE));
        assertEquals("byte", "xsd:byte", mapper.toXMLSchemaDataType(byte.class));
        assertEquals("java.util.Date", "xsd:dateTime", mapper.toXMLSchemaDataType(java.util.Date.class));
        assertEquals("java.sql.Date", "xsd:date", mapper.toXMLSchemaDataType(java.sql.Date.class));
        assertEquals("java.sql.Time", "xsd:time", mapper.toXMLSchemaDataType(java.sql.Time.class));
    }

    public void testDefaultDataTypeTransciption() throws Exception
    {
        Schema expected = new Schema();

        GlobalComplexType allSimplesBeanType = new GlobalComplexType();
        allSimplesBeanType.setName("org.apache.commons.betwixt.schema.AllSimplesBean");
        allSimplesBeanType.addElement(new SimpleLocalElement("string", "xsd:string"));
        allSimplesBeanType.addElement(new SimpleLocalElement("bigInteger", "xsd:integer"));
        allSimplesBeanType.addElement(new SimpleLocalElement("primitiveInt", "xsd:int"));
        allSimplesBeanType.addElement(new SimpleLocalElement("objectInt", "xsd:int"));
        allSimplesBeanType.addElement(new SimpleLocalElement("primitiveLong", "xsd:long"));
        allSimplesBeanType.addElement(new SimpleLocalElement("objectLong", "xsd:long"));
        allSimplesBeanType.addElement(new SimpleLocalElement("primitiveShort", "xsd:short"));
        allSimplesBeanType.addElement(new SimpleLocalElement("objectShort", "xsd:short"));
        allSimplesBeanType.addElement(new SimpleLocalElement("bigDecimal", "xsd:decimal"));
        allSimplesBeanType.addElement(new SimpleLocalElement("primitiveFloat", "xsd:float"));
        allSimplesBeanType.addElement(new SimpleLocalElement("objectFloat", "xsd:float"));
        allSimplesBeanType.addElement(new SimpleLocalElement("primitiveDouble", "xsd:double"));
        allSimplesBeanType.addElement(new SimpleLocalElement("objectDouble", "xsd:double"));
        allSimplesBeanType.addElement(new SimpleLocalElement("primitiveBoolean", "xsd:boolean"));
        allSimplesBeanType.addElement(new SimpleLocalElement("objectBoolean", "xsd:boolean"));
        allSimplesBeanType.addElement(new SimpleLocalElement("primitiveByte", "xsd:byte"));
        allSimplesBeanType.addElement(new SimpleLocalElement("objectByte", "xsd:byte"));
        allSimplesBeanType.addElement(new SimpleLocalElement("utilDate", "xsd:dateTime"));
        allSimplesBeanType.addElement(new SimpleLocalElement("sqlDate", "xsd:date"));
        allSimplesBeanType.addElement(new SimpleLocalElement("sqlTime", "xsd:time"));

        GlobalElement root = new GlobalElement("AllSimplesBean", "org.apache.commons.betwixt.schema.AllSimplesBean");
        expected.addComplexType(allSimplesBeanType);
        expected.addElement(root);

        SchemaTranscriber transcriber = new SchemaTranscriber();
        transcriber.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        Schema out = transcriber.generate(AllSimplesBean.class);

        assertEquals("AllSimplesBean schema", expected, out);
    }
}
