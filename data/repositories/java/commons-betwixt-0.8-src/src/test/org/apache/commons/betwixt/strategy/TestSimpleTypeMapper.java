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


package org.apache.commons.betwixt.strategy;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.IntrospectionConfiguration;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * Tests for SimpleTypeMapper and the associated strategy.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestSimpleTypeMapper extends AbstractTestCase
{

    public TestSimpleTypeMapper(String name)
    {
        super(name);
    }

    public void testDefaultExceptionType() throws Exception
    {
        assertEquals(TypeBindingStrategy.BindingType.COMPLEX, TypeBindingStrategy.DEFAULT.bindingType(RuntimeException.class));
    }

    public void testNewStrategy() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setSimpleTypeMapper(new StringsAsElementsSimpleTypeMapper());
        introspector.getConfiguration().setWrapCollectionsInElement(true);

        XMLBeanInfo beanInfo = introspector.introspect(TuneBean.class);
        ElementDescriptor tuneBeanDescriptor = beanInfo.getElementDescriptor();

        AttributeDescriptor[] tuneBeanAttributes = tuneBeanDescriptor.getAttributeDescriptors();
        assertEquals("Only expect one attribute", 1, tuneBeanAttributes.length);
        AttributeDescriptor recordedAttribute = tuneBeanAttributes[0];
        assertEquals("Expected recorded to be bound as an attribute", "recorded", recordedAttribute.getLocalName());

        ElementDescriptor[] tuneBeanChildElements = tuneBeanDescriptor.getElementDescriptors();
        assertEquals("Expected three child elements", 3 , tuneBeanChildElements.length);

        int bits = 0;
        for (int i=0, size=tuneBeanChildElements.length; i<size; i++)
        {
            String localName = tuneBeanChildElements[i].getLocalName();
            if ("composers".equals(localName))
            {
                bits = bits | 1;
            }
            if ("artist".equals(localName))
            {
                bits = bits | 2;
            }
            if ("name".equals(localName))
            {
                bits = bits | 4;
            }
        }

        assertEquals("Every element present", 7, bits);
    }

    public void testWrite() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setSimpleTypeMapper(new StringsAsElementsSimpleTypeMapper());
        writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(true);
        writer.getBindingConfiguration().setMapIDs(false);

        TuneBean bean = new TuneBean("On The Run", "Pink Floyd", 1972);
        bean.addComposer(new ComposerBean("David", "Gilmour", 1944));
        bean.addComposer(new ComposerBean("Roger", "Waters", 1944));

        writer.write(bean);

        String xml = out.getBuffer().toString();
        String expected = "<?xml version='1.0'?>" +
                          "<TuneBean recorded='1972'>" +
                          "    <name>On The Run</name>" +
                          "    <artist>Pink Floyd</artist>" +
                          "    <composers>" +
                          "       <composer born='1944'>" +
                          "           <forename>David</forename>" +
                          "           <surname>Gilmour</surname>" +
                          "       </composer>" +
                          "       <composer born='1944'>" +
                          "           <forename>Roger</forename>" +
                          "           <surname>Waters</surname>" +
                          "       </composer>" +
                          "   </composers>" +
                          "</TuneBean>";

        xmlAssertIsomorphicContent(parseString(xml), parseString(expected), true);
    }

    public void testRead() throws Exception
    {

        String xml = "<?xml version='1.0'?>" +
                     "<TuneBean recorded='1972'>" +
                     "    <name>On The Run</name>" +
                     "    <artist>Pink Floyd</artist>" +
                     "    <composers>" +
                     "       <composer born='1944'>" +
                     "           <forename>David</forename>" +
                     "           <surname>Gilmour</surname>" +
                     "       </composer>" +
                     "       <composer born='1944'>" +
                     "           <forename>Roger</forename>" +
                     "           <surname>Waters</surname>" +
                     "       </composer>" +
                     "   </composers>" +
                     "</TuneBean>";
        StringReader in = new StringReader(xml);

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setSimpleTypeMapper(new StringsAsElementsSimpleTypeMapper());
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(true);
        reader.getBindingConfiguration().setMapIDs(false);

        reader.registerBeanClass(TuneBean.class);

        TuneBean bean = (TuneBean) reader.parse(in);

        assertNotNull("Parsing failed", bean);
        assertEquals("Name value", "On The Run", bean.getName());
        assertEquals("Artist value", "Pink Floyd", bean.getArtist());
        assertEquals("Recorded value", 1972, bean.getRecorded());

        Collection expectedComposers = new ArrayList();
        expectedComposers.add(new ComposerBean("David", "Gilmour", 1944));
        expectedComposers.add(new ComposerBean("Roger", "Waters", 1944));

        assertTrue("Right composers", bean.sameComposers(expectedComposers));
    }

    /** Implementation binds strings to elements but everything else to attributes */
    class StringsAsElementsSimpleTypeMapper extends SimpleTypeMapper
    {

        /**
         * Binds strings to elements but everything else to attributes
         */
        public Binding bind(
            String propertyName,
            Class propertyType,
            IntrospectionConfiguration configuration)
        {
            if (String.class.equals(propertyType))
            {
                return SimpleTypeMapper.Binding.ELEMENT;
            }
            return SimpleTypeMapper.Binding.ATTRIBUTE;
        }

    }
}
