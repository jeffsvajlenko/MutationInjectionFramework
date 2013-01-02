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
package org.apache.commons.betwixt;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

public class TestStringCollections extends AbstractTestCase
{

    public TestStringCollections(String testName)
    {
        super(testName);
    }

    public void testIntrospection() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo xmlBeanInfo = introspector.introspect(PoemBean.class);
        ElementDescriptor beanDescriptor = xmlBeanInfo.getElementDescriptor();
        ElementDescriptor[] beanChildren = beanDescriptor.getElementDescriptors();
        assertEquals("Only one child", 1, beanChildren.length);
        ElementDescriptor[] linesChildren = beanChildren[0].getElementDescriptors();
        assertEquals("Only one lines child", 1, linesChildren.length);
        assertFalse("Line child is not hollow", linesChildren[0].isHollow());
    }

    public void testWritePoem() throws Exception
    {
        String expected = "<?xml version='1.0'?>" +
                          "<PoemBean>" +
                          "<lines>" +
                          "<line>It is an ancient Mariner,</line>" +
                          "<line>And he stoppeth one of three.</line>" +
                          "<line>\"By thy long grey beard and the glittering eye,</line>" +
                          "<line>Now wherefore stopp'st thou me?\"</line>" +
                          "</lines>" +
                          "</PoemBean>";
        PoemBean bean = new PoemBean();
        bean.addLine("It is an ancient Mariner,");
        bean.addLine("And he stoppeth one of three.");
        bean.addLine("\"By thy long grey beard and the glittering eye,");
        bean.addLine("Now wherefore stopp'st thou me?\"");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(bean);

        String xml = out.toString();
        xmlAssertIsomorphic(parseString(expected), parseString(xml));
    }

    public void testReadPoem() throws Exception
    {
        String xml = "<?xml version='1.0'?>" +
                     "<PoemBean>" +
                     "<lines>" +
                     "<line>It is an ancient Mariner,</line>" +
                     "<line>And he stoppeth one of three.</line>" +
                     "<line>\"By thy long grey beard and the glittering eye,</line>" +
                     "<line>Now wherefore stopp'st thou me?\"</line>" +
                     "</lines>" +
                     "</PoemBean>";
        BeanReader reader = new BeanReader();
        reader.registerBeanClass(PoemBean.class);
        PoemBean bean = (PoemBean) reader.parse(new StringReader(xml));
        assertNotNull("Expected bean to be output");
        Object[] lines = bean.getLines().toArray();
        assertEquals("Expected four lines", 4, lines.length);
        assertEquals("First line of Rime Of The Ancient Mariner", "It is an ancient Mariner,", lines[0]);
        assertEquals("Second line of Rime Of The Ancient Mariner", "And he stoppeth one of three.", lines[1]);
        assertEquals("Third line of Rime Of The Ancient Mariner", "\"By thy long grey beard and the glittering eye,", lines[2]);
        assertEquals("Fourth line of Rime Of The Ancient Mariner", "Now wherefore stopp'st thou me?\"", lines[3]);

    }
}
