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

package org.apache.commons.betwixt.io.read;

import java.io.StringReader;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;

/**
 * Test harness for Mapping Actions.
 *
 * @author Robert Burrell Donkin
 * @version $Id: TestMappingActions.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestMappingActions extends AbstractTestCase
{


    public TestMappingActions(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(TestMappingActions.class);
    }

    public void testSimpleRead() throws Exception
    {

        String xml="<?xml version='1.0'?><AddressBean><street>1 Main Street</street><city>New Town</city>"
                   + "<code>NT1 1AA</code><country>UK</country></AddressBean>";

        //SimpleLog log = new SimpleLog("[test]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //BeanRuleSet.setLog(log);
        BeanReader reader = new BeanReader();
        reader.registerBeanClass(AddressBean.class);
        AddressBean address = (AddressBean) reader.parse(new StringReader(xml));

        assertFalse("Address is mapped", address == null);
        assertEquals("Street", "1 Main Street", address.getStreet());
        assertEquals("City", "New Town", address.getCity());
        assertEquals("Code", "NT1 1AA", address.getCode());
        assertEquals("Country", "UK", address.getCountry());
    }

    public void testPrimitiveCollective() throws Exception
    {

        String xml="<?xml version='1.0'?><SimpleStringCollective><strings>"
                   + "<string>one</string><string>two</string><string>three</string>"
                   + "</strings></SimpleStringCollective>";

        //SimpleLog log = new SimpleLog("[test]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        // BeanRuleSet.setLog(log);
        BeanReader reader = new BeanReader();
        reader.registerBeanClass(SimpleStringCollective.class);
        SimpleStringCollective collective = (SimpleStringCollective) reader.parse(new StringReader(xml));

        assertFalse("SimpleStringCollective mapped", collective == null);
        List strings = collective.getStrings();
        assertEquals("String count", 3, strings.size());
        assertEquals("First string", "one", strings.get(0));
        assertEquals("Second string", "two", strings.get(1));
        assertEquals("Third string", "three", strings.get(2));
    }



    public void testBodyUpdateActionNoMatch() throws Exception
    {
        AddressBean bean = new AddressBean();
        bean.setStreet("DEFAULT");
        bean.setCode("DEFAULT");
        bean.setCountry("DEFAULT");

        XMLIntrospector introspector = new XMLIntrospector();
        ElementDescriptor elementDescriptor = introspector.introspect(AddressBean.class).getElementDescriptor();

        ReadContext context = new ReadContext(new BindingConfiguration(), new ReadConfiguration());
        context.setBean(bean);
        context.markClassMap(AddressBean.class);
        context.pushElement("NoMatch");
        context.setXMLIntrospector(introspector);
        SimpleTypeBindAction action = new SimpleTypeBindAction();
        action.body("Street value", context);
        assertEquals("Street is unset", "DEFAULT", bean.getStreet());
        assertEquals("Country is unset", "DEFAULT", bean.getCountry());
        assertEquals("Code is unset", "DEFAULT", bean.getCode());
    }


    public void testBodyUpdateActionMatch() throws Exception
    {
        AddressBean bean = new AddressBean();
        bean.setStreet("DEFAULT");
        bean.setCode("DEFAULT");
        bean.setCountry("DEFAULT");

        XMLIntrospector introspector = new XMLIntrospector();
        ReadContext context = new ReadContext(new BindingConfiguration(), new ReadConfiguration());
        context.pushBean(bean);
        context.markClassMap(AddressBean.class);
        context.pushElement("street");
        context.setXMLIntrospector(introspector);
        SimpleTypeBindAction action = new SimpleTypeBindAction();
        action.body("Street value", context);
        assertEquals("Street is set", "Street value", bean.getStreet());
        assertEquals("Country is unset", "DEFAULT", bean.getCountry());
        assertEquals("Code is unset", "DEFAULT", bean.getCode());
    }

    public void testCollection() throws Exception
    {
        String xml = "<?xml version='1.0'?>"
                     + "<elements><element><value>alpha</value></element></elements>";
        StringReader in = new StringReader(xml);
        BeanReader reader = new BeanReader();
        reader.registerBeanClass(Elements.class);
        Elements result = (Elements) reader.parse(in);
        assertNotNull("Element alpha exists", result.getElement("alpha"));
    }
}
