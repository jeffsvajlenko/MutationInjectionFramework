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
package org.apache.commons.betwixt.digester;

import java.io.StringReader;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.dotbetwixt.ExampleBean;

/**
 * Tests for reading dot betwist files.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 155402 $
 */
public class TestDigestMultiMapping extends TestCase
{


    public void testDigestWithOptions() throws Exception
    {
        String xml = "<?xml version='1.0'?>" +
                     "<betwixt-config>" +
                     "  <class name=\"" + TestDigestMultiMapping.class.getName() + "\">" +
                     "    <element name=\"test-multi-mapping\">" +
                     "      <option>" +
                     "        <name>test-key-a</name>" +
                     "        <value>test-value-a</value>" +
                     "      </option>" +
                     "      <option>" +
                     "        <name>test-key-b</name>" +
                     "        <value>test-value-b</value>" +
                     "      </option>" +
                     "      <element name=\"maps\" property=\"maps\">" +
                     "        <option>" +
                     "          <name>test-key-c</name>" +
                     "          <value>test-value-c</value>" +
                     "        </option>" +
                     "      </element>" +
                     "    </element>" +
                     "  </class>" +
                     "</betwixt-config>";

        MultiMappingBeanInfoDigester digester = new MultiMappingBeanInfoDigester();
        digester.setXMLIntrospector(new XMLIntrospector());
        digester.setBeanClass(ExampleBean.class);

        digester.parse(new StringReader(xml));
        Map beanInfoMap = digester.getBeanInfoMap();
        assertTrue( beanInfoMap.containsKey(TestDigestMultiMapping.class));

        XMLBeanInfo xmlBeanInfo = (XMLBeanInfo) beanInfoMap.get(TestDigestMultiMapping.class);
        assertNotNull(xmlBeanInfo);

        ElementDescriptor baseDescriptor = xmlBeanInfo.getElementDescriptor();

        assertEquals("Value one set on base", "test-value-a",  baseDescriptor.getOptions().getValue("test-key-a"));
        assertEquals("Value two set on base", "test-value-b",  baseDescriptor.getOptions().getValue("test-key-b"));
        assertNull("Value three not set on base",  baseDescriptor.getOptions().getValue("three"));

        assertEquals("Number of child elements", 1, baseDescriptor.getElementDescriptors().length);

        ElementDescriptor childDescriptor = baseDescriptor.getElementDescriptors()[0];
        assertNull("Value one set on base",  childDescriptor.getOptions().getValue("test-key-a"));
        assertNull("Value two set on base",  childDescriptor.getOptions().getValue("test-key-b"));
        assertEquals("Value three set on child", "test-value-c",  childDescriptor.getOptions().getValue("test-key-c"));
    }
}
