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

import junit.framework.TestCase;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author Brian Pugh
 */
public class TestLoopType extends TestCase
{
    public void testSimpleList() throws Exception
    {
        Father father = new Father();
        father.setSpouse("Julie");
        father.addKid("John");
        father.addKid("Jane");

        StringWriter outputWriter = new StringWriter();

        outputWriter.write("<?xml version='1.0' ?>\n");
        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.getBindingConfiguration().setMapIDs(true);
        beanWriter.write(father);

        BeanReader beanReader = new BeanReader();

        // Configure the reader
        beanReader.registerBeanClass(Father.class);
        StringReader xmlReader = new StringReader(outputWriter.toString());

        //Parse the xml
        Father result = (Father) beanReader.parse(xmlReader);

        assertNotNull("Unexpected null list of children!", result.getKids());
        assertEquals(
            "got wrong number of children",
            father.getKids().size(),
            result.getKids().size());
        assertNull(
            "Spouse should not get set because it is not in the .betwixt file",
            result.getSpouse());
    }

    public void testIgnoredProperty() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo beanInfo = introspector.introspect(IgnoreBean.class);
        ElementDescriptor ignoreDescriptor = beanInfo.getElementDescriptor();

        assertEquals("element name matches", "ignore", ignoreDescriptor.getLocalName());
        ElementDescriptor[] childDescriptors = ignoreDescriptor.getElementDescriptors();
        assertEquals("number of child elements", 1, childDescriptors.length);

    }

    /**
     * Basic test for add-adders attribute of addDefaults tag.
     * @throws Exception
     */
    public void testIgnoredAdders() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        // ignore adders bean uses a dot betwixt file with add-adders false
        XMLBeanInfo beanInfo = introspector.introspect(IgnoreAddersBean.class);
        ElementDescriptor ignoreDescriptor = beanInfo.getElementDescriptor();

        assertEquals("element name matches", "ignore", ignoreDescriptor.getLocalName());
        ElementDescriptor[] childDescriptors = ignoreDescriptor.getElementDescriptors();
        assertEquals("number of child elements", 2, childDescriptors.length);
        for (int i=0; i<childDescriptors.length; i++)
        {
            ElementDescriptor descriptor = childDescriptors[i];
            if (descriptor.getLocalName().equals("gammas"))
            {
                assertNull("Expected descriptor to be null since adders must be explicitly listed.", descriptor.getUpdater());
            }
            else
            {
                assertEquals("alpha", descriptor.getLocalName());
            }
        }
        AttributeDescriptor[] attributes = ignoreDescriptor.getAttributeDescriptors();
        assertEquals(1, attributes.length);
        assertEquals("beta", attributes[0].getLocalName());
    }

    //TODO: complete these tests after refactoring the element descriptors produced is complete
    public void _testAddDefaults() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo beanInfo = introspector.introspect(LibraryBean.class);
        ElementDescriptor libraryDescriptor = beanInfo.getElementDescriptor();

        AttributeDescriptor[] libraryAttributeDescriptors = libraryDescriptor.getAttributeDescriptors();
        assertEquals("Only one attribute", 1, libraryAttributeDescriptors.length);

        ElementDescriptor[] libraryElementDescriptors = libraryDescriptor.getElementDescriptors();
        assertEquals("Only one element", 1, libraryElementDescriptors.length);


    }
}

