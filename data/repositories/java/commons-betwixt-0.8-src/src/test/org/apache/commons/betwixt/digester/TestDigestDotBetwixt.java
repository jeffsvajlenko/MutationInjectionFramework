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

import junit.framework.TestCase;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.dotbetwixt.ExampleBean;

/**
 * Tests for reading dot betwist files.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestDigestDotBetwixt extends TestCase
{


    public void testDigestWithOptions() throws Exception
    {
        String xml = "<?xml version='1.0'?>" +
                     "<info>" +
                     "    <element name='example-bean'>" +
                     "        <option>" +
                     "           <name>one</name>" +
                     "           <value>value one</value>" +
                     "        </option>" +
                     "        <option>" +
                     "           <name>two</name>" +
                     "           <value>value two</value>" +
                     "        </option>" +
                     "        <element name='example' property='examples'>" +
                     "          <option>" +
                     "             <name>three</name>" +
                     "             <value>value three</value>" +
                     "          </option>" +
                     "        </element>" +
                     "    </element>" +
                     "</info>";

        XMLBeanInfoDigester digester = new XMLBeanInfoDigester();
        digester.setXMLIntrospector(new XMLIntrospector());
        digester.setBeanClass(ExampleBean.class);
        XMLBeanInfo xmlBeanInfo = (XMLBeanInfo) digester.parse(new StringReader(xml));
        ElementDescriptor baseDescriptor = xmlBeanInfo.getElementDescriptor();

        assertEquals("Value one set on base", "value one",  baseDescriptor.getOptions().getValue("one"));
        assertEquals("Value two set on base", "value two",  baseDescriptor.getOptions().getValue("two"));
        assertNull("Value three not set on base",  baseDescriptor.getOptions().getValue("three"));

        assertEquals("Number of child elements", 1, baseDescriptor.getElementDescriptors().length);

        ElementDescriptor childDescriptor = baseDescriptor.getElementDescriptors()[0];
        assertNull("Value one set on base",  childDescriptor.getOptions().getValue("one"));
        assertNull("Value two set on base",  childDescriptor.getOptions().getValue("two"));
        assertEquals("Value three set on child", "value three",  childDescriptor.getOptions().getValue("three"));
    }
}
