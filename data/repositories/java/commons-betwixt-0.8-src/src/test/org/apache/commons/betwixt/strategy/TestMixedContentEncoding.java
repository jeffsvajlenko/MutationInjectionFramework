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


import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.io.BeanWriter;


/**
 * Tests for mixed content encoding.
 * Mixed content encoding is the process by which body content
 * is written out (in an escaped form) into a textual output stream.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestMixedContentEncoding extends AbstractTestCase
{

    /** Concrete subclass used for testing */
    static class TestBaseMixedContentEncoding extends BaseMixedContentEncodingStrategy
    {
        boolean encode = false;
        ElementDescriptor element = null;

        TestBaseMixedContentEncoding(boolean encode)
        {
            this.encode = encode;
        }

        protected boolean encodeAsCDATA(ElementDescriptor element)
        {
            this.element = element;
            return encode;
        }
    }

    public TestMixedContentEncoding(String testName)
    {
        super(testName);
    }

    public void testBaseMixedEscapeCharacters()
    {
        BaseMixedContentEncodingStrategy mceStrategy = new TestBaseMixedContentEncoding(false);
        assertEquals("Check basic escaping", "ab&lt;&gt;&amp;ba", mceStrategy.escapeCharacters("ab<>&ba"));
    }

    public void testBaseMixedCDATAEncoding()
    {
        BaseMixedContentEncodingStrategy mceStrategy = new TestBaseMixedContentEncoding(false);
        assertEquals("Check basic escaping", "<![CDATA[<greeting>ab]]&gt;ba</greeting>]]>", mceStrategy.encodeInCDATA("<greeting>ab]]>ba</greeting>"));
    }

    public void testBaseMixedEncode()
    {
        ElementDescriptor descriptor = new ElementDescriptor();
        TestBaseMixedContentEncoding mceStrategy = new TestBaseMixedContentEncoding(false);
        assertEquals(
            "Using character escaping",
            "&lt;exclaim&gt;hello, mum&lt;/exclaim&gt;",
            mceStrategy.encode("<exclaim>hello, mum</exclaim>", descriptor));

        assertEquals("Descriptor set", descriptor, mceStrategy.element);
        mceStrategy = new TestBaseMixedContentEncoding(true);
        assertEquals(
            "Using CDATA encoding",
            "<![CDATA[<exclaim>hello, mum</exclaim>]]>",
            mceStrategy.encode("<exclaim>hello, mum</exclaim>", descriptor));

        assertEquals("Descriptor set", descriptor, mceStrategy.element);
    }

    public void testDefaultImplementation()
    {
        ElementDescriptor descriptor = new ElementDescriptor();
        assertEquals(
            "Default implementation uses character escaping",
            "&lt;proclaim&gt;The King Is Dead, Long Live The King&lt;/proclaim&gt;",
            MixedContentEncodingStrategy.DEFAULT.encode("<proclaim>The King Is Dead, Long Live The King</proclaim>", descriptor));
    }

    public void testEscapedCharactersImplementation()
    {
        ElementDescriptor descriptor = new ElementDescriptor();
        assertEquals(
            "Default implementation uses character escaping",
            "&lt;proclaim&gt;The King Is Dead, Long Live The King&lt;/proclaim&gt;",
            MixedContentEncodingStrategy.ESCAPED_CHARACTERS.encode("<proclaim>The King Is Dead, Long Live The King</proclaim>", descriptor));
    }

    public void testCDATAImplementation()
    {
        ElementDescriptor descriptor = new ElementDescriptor();
        assertEquals(
            "Default implementation uses character escaping",
            "<![CDATA[<proclaim>The King Is Dead, Long Live The King</proclaim>]]>",
            MixedContentEncodingStrategy.CDATA.encode("<proclaim>The King Is Dead, Long Live The King</proclaim>", descriptor));
    }

    public void testDefaultOutput() throws Exception
    {
        Element element = new Element();
        element.setValue("<greeting>What Ho Jeeves!</greeting>");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.setEndOfLine("\n"); //force to \n so expected values match for sure
        writer.write(element);

        String expected = "<?xml version='1.0'?><Element>\n<value>&lt;greeting&gt;What Ho Jeeves!&lt;/greeting&gt;</value>\n</Element>\n";
        String xml = out.getBuffer().toString();

        assertEquals(expected,xml);

    }

    /** Unit test for default output when CDATA option is set */
    public void testDefaultOutputWithCDATAOption() throws Exception
    {
        Element element = new Element();
        element.setValue("<greeting>What Ho Jeeves!</greeting>");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.getBindingConfiguration().setMapIDs(false);
        XMLBeanInfo elementInfo = writer.getXMLIntrospector().introspect(Element.class);
        elementInfo.getElementDescriptor().getElementDescriptors()[0]
        .getOptions().addOption(MixedContentEncodingStrategy.ENCODING_OPTION_NAME, "CDATA");

        writer.setEndOfLine("\n"); //force to \n so expected values match for sure
        writer.write(element);

        String expected = "<?xml version='1.0'?><Element>\n<value><![CDATA[<greeting>What Ho Jeeves!</greeting>]]></value>\n</Element>\n";
        String xml = out.getBuffer().toString();

        assertEquals(expected,xml);

    }

    /** Unit test for default output when character escaping option is set */
    public void testDefaultOutputWithCharacterEscapingOption() throws Exception
    {
        Element element = new Element();
        element.setValue("<greeting>What Ho Jeeves!</greeting>");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.getBindingConfiguration().setMapIDs(false);
        XMLBeanInfo elementInfo = writer.getXMLIntrospector().introspect(Element.class);
        elementInfo.getElementDescriptor().getElementDescriptors()[0]
        .getOptions().addOption("org.apache.commons.betwixt.mixed-content-encoding", "escaped");
        writer.setEndOfLine("\n"); //force to \n so expected values match for sure
        writer.write(element);

        String expected = "<?xml version='1.0'?><Element>\n<value>&lt;greeting&gt;What Ho Jeeves!&lt;/greeting&gt;</value>\n</Element>\n";
        String xml = out.getBuffer().toString();

        assertEquals(expected,xml);
    }

    public void testDefaultOutputWithDotBetwixtOptions() throws Exception
    {
        ABCBean bean = new ABCBean();
        bean.setA("<strong>weak</strong>");
        bean.setB("<strong>weak</strong>");
        bean.setC("<strong>weak</strong>");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.setEndOfLine("\n"); //force to \n so expected values match for sure
        writer.write(bean);

        String expected = "<?xml version='1.0'?>" +
                          "<greek-abc>\n" +
                          "<alpha><![CDATA[<strong>weak</strong>]]></alpha>\n" +
                          "<beta>&lt;strong&gt;weak&lt;/strong&gt;</beta>\n" +
                          "<gamma>&lt;strong&gt;weak&lt;/strong&gt;</gamma>\n" +
                          "</greek-abc>\n";
        String xml = out.getBuffer().toString();

        assertEquals(expected,xml);
    }

    public void testEscapedOutput() throws Exception
    {
        Element element = new Element();
        element.setValue("<greeting>What Ho Jeeves!</greeting>");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.setMixedContentEncodingStrategy(new TestBaseMixedContentEncoding(false));
        writer.setEndOfLine("\n"); //force to \n so expected values match for sure
        writer.write(element);

        String expected = "<?xml version='1.0'?><Element>\n<value>&lt;greeting&gt;What Ho Jeeves!&lt;/greeting&gt;</value>\n</Element>\n";
        String xml = out.getBuffer().toString();

        assertEquals(expected,xml);

    }

    public void testCDATAEncodedOutput() throws Exception
    {
        Element element = new Element();
        element.setValue("<greeting>What Ho Jeeves!</greeting>");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.setMixedContentEncodingStrategy(new TestBaseMixedContentEncoding(true));
        writer.setEndOfLine("\n"); //force to \n so expected values match for sure
        writer.write(element);

        String expected = "<?xml version='1.0'?><Element>\n<value><![CDATA[<greeting>What Ho Jeeves!</greeting>]]></value>\n</Element>\n";
        String xml = out.getBuffer().toString();

        assertEquals(expected,xml);
    }
}
