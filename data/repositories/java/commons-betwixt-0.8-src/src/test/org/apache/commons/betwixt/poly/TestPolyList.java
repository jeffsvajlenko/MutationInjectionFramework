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

package org.apache.commons.betwixt.poly;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;



public class TestPolyList extends AbstractTestCase
{
    public static Log log = LogFactory.getLog(TestPolyList.class);

    public TestPolyList(String testName)
    {
        super(testName);

        log.info("Mapping:\n" + MAPPING);
    }

    private static final String XML = "<AlphaList><AlphaOneImpl><one>1</one></AlphaOneImpl><AlphaTwoImpl><two>2</two></AlphaTwoImpl></AlphaList>";

    private static final String MAPPING = "<?xml version='1.0'?>"
                                          + "<betwixt-config>"
                                          + "  <class name='org.apache.commons.betwixt.poly.AlphaOneImpl'>"
                                          + "    <element name='AlphaOneImpl'>"
                                          + "      <element name='one' property='one'/>"
                                          + "    </element>"
                                          + "  </class>"
                                          + "  <class name='org.apache.commons.betwixt.poly.AlphaTwoImpl'>"
                                          + "    <element name='AlphaTwoImpl'>"
                                          + "      <element name='two' property='two'/>"
                                          + "    </element>"
                                          + "  </class>"
                                          + "</betwixt-config>";

    public void testWrite() throws Exception
    {
        AlphaList bean = new AlphaList();
        AlphaOneImpl one = new AlphaOneImpl("1");
        bean.add(one);
        AlphaTwoImpl two = new AlphaTwoImpl("2");
        bean.add(two);

        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        StringReader mapping = new StringReader(MAPPING);
        writer.getXMLIntrospector().register(new InputSource(mapping));
        configure(writer.getBindingConfiguration());
        writer.write(bean);

        String written = out.getBuffer().toString();
        log.info("Written:\n" + written);

        xmlAssertIsomorphicContent(
            parseString(XML),
            parseString(written),
            true);
    }

    public void testRead() throws Exception
    {
        StringReader in = new StringReader(XML);
        BeanReader reader = new BeanReader();
        StringReader mapping = new StringReader(MAPPING);
        reader.registerMultiMapping(new InputSource(mapping));
        reader.registerBeanClass(AlphaList.class);
        configure(reader.getBindingConfiguration());
        Object bean = reader.parse(in);
        assertTrue(bean instanceof AlphaList);
        AlphaList list = (AlphaList) bean;
        assertEquals(2, list.size());

        assertTrue(list.get(0) instanceof AlphaOneImpl);
        AlphaOneImpl one = (AlphaOneImpl)list.get(0);
        assertEquals("1", one.alpha());

        assertTrue(list.get(1) instanceof AlphaTwoImpl);
        AlphaTwoImpl two = (AlphaTwoImpl)list.get(1);
        assertEquals("2", two.alpha());
    }

    public void testIntrospection() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo beanInfo = introspector.introspect(AlphaList.class);
        ElementDescriptor[] descriptors = beanInfo.getElementDescriptor().getElementDescriptors();
        assertEquals("One descriptor", 1, descriptors.length);
        assertTrue(descriptors[0].isHollow());
        assertNotNull(descriptors[0].getContextExpression());
        assertNotNull(descriptors[0].getUpdater());
        assertEquals("A list can contain any object", Object.class, descriptors[0].getSingularPropertyType());
    }

    private void configure(BindingConfiguration configuration)
    {
        configuration.setMapIDs(false);
    }
}
