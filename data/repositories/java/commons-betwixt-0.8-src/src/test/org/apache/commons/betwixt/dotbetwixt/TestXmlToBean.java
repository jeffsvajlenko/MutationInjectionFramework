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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.apache.commons.betwixt.xmlunit.XmlTestCase;

/**
  * Test customization of xml to bean mapping using .betwixt files.
  *
  * @author Robert Burrell Donkin
  */
public class TestXmlToBean extends XmlTestCase
{

//--------------------------------- Test Suite

    public static Test suite()
    {
        return new TestSuite(TestXmlToBean.class);
    }

//--------------------------------- Constructor

    public TestXmlToBean(String testName)
    {
        super(testName);
    }

//---------------------------------- Tests

    public void testForceAccessibleSuper() throws Exception
    {
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(MixedUpdatersBean.class);
        ElementDescriptor[] descriptors = xmlBeanInfo.getElementDescriptor().getElementDescriptors();
        boolean propertyFound = false;
        for (int i=0; i<descriptors.length ; i++)
        {
            ElementDescriptor descriptor = descriptors[i];
            if ("private-super".equals(descriptor.getLocalName()))
            {
                propertyFound = true;
                assertNotNull("Updater found", descriptor.getUpdater());
                assertNotNull("Expression found", descriptor.getTextExpression());
            }
        }
        assertTrue("Found inaccessible super methods", propertyFound);
    }

    public void testCustomUpdaters() throws Exception
    {
        // might as well check writer whilst we're at it
        MixedUpdatersBean bean = new MixedUpdatersBean("Lov");
        bean.badNameSetter("Hate");
        bean.addItem("White");
        bean.badItemAdder("Black");
        bean.addItem("Life");
        bean.badItemAdder("Death");
        bean.privatePropertyWorkaroundSetter("Private");
        bean.getPrivateItems().add("private item 1");
        bean.privateField = 100;

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);

        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(bean);

        String xml = "<?xml version='1.0'?><mixed><name>Lov</name><bad-name>Hate</bad-name>"
                     + "<items><item>White</item><item>Life</item></items>"
                     + "<bad-items><bad-item>Black</bad-item><bad-item>Death</bad-item></bad-items>"
                     + "<private-property>Private</private-property>"
                     + "<private-items><private-item>private item 1</private-item></private-items>" +
                     "<private-super>100</private-super>"
                     + "</mixed>";

        xmlAssertIsomorphicContent(
            parseString(xml),
            parseString(out.toString()),
            true);

        // now we'll test reading via round tripping
        BeanReader reader = new BeanReader();
        reader.getBindingConfiguration().setMapIDs(false);
        reader.registerBeanClass("mixed", MixedUpdatersBean.class);
        bean = (MixedUpdatersBean) reader.parse(new StringReader(xml));

        assertEquals("Name incorrect", "Lov", bean.getName());
        assertEquals("BadName incorrect", "Hate", bean.getBadName());
        List items = bean.getItems();
        assertEquals("Wrong number of items", 2, items.size());
        assertEquals("Item one wrong", "White", items.get(0));
        assertEquals("Item two wrong", "Life", items.get(1));
        List badItems = bean.getBadItems();
        assertEquals("Wrong number of bad items", 2, badItems.size());
        // awaiting implementation
        //assertEquals("Bad item one wrong", "Black", badItems.get(0));
        //assertEquals("Bad item two wrong", "Death", badItems.get(1));
        assertEquals("Private property incorrect", "Private", bean.getPrivateProperty());

        //this shows that a private adder can be utilized
        List privateItems = bean.getPrivateItems();
        assertEquals("Wrong number of private items", 1, privateItems.size());
        //TODO can't assert contents - gets the right number of items, but each is null (badItems, too)
        assertEquals("Private property accessed on super", 100, bean.privateField);
    }


    /** Test output of bean with mixed content */
    public void testMixedContent() throws Exception
    {

        StringReader xml = new StringReader(
            "<?xml version='1.0' encoding='UTF-8'?><deep-thought alpha='Life' gamma='42'>"
            + "The Universe And Everything</deep-thought>");

        BeanReader reader = new BeanReader();
        reader.registerBeanClass(MixedContentOne.class);
        Object resultObject = reader.parse(xml);
        assertEquals("Object is MixedContentOne", true, resultObject instanceof MixedContentOne);
        MixedContentOne result = (MixedContentOne) resultObject;
        assertEquals("Property Alpha matches", "Life", result.getAlpha());
        assertEquals("Property Beta matches", "The Universe And Everything", result.getBeta());
        assertEquals("Property Gamma matches", 42, result.getGamma());
    }


    /** Tests basic use of an implementation for an interface */
    public void _testBasicInterfaceImpl() throws Exception
    {

        ExampleBean bean = new ExampleBean("Alice");
        bean.addExample(new ExampleImpl(1, "Mad Hatter"));
        bean.addExample(new ExampleImpl(2, "March Hare"));
        bean.addExample(new ExampleImpl(3, "Dormouse"));

        String xml = "<?xml version='1.0' encoding='UTF-8'?>"
                     + "<example-bean><name>Alice</name>"
                     + "<example><id>1</id><name>Mad Hatter</name></example>"
                     + "<example><id>2</id><name>March Hare</name></example>"
                     + "<example><id>3</id><name>Dormouse</name></example>"
                     + "</example-bean>";


        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        reader.registerBeanClass( ExampleBean.class );

        StringReader in = new StringReader( xml );
        ExampleBean out = (ExampleBean) reader.parse( in );
        assertEquals("Interface read failed", bean, out);

    }
}

