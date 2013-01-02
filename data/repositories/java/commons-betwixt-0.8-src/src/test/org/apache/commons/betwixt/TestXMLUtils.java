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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** Test harness for the XMLUtils
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class TestXMLUtils extends AbstractTestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestXMLUtils.class);
    }

    public TestXMLUtils(String testName)
    {
        super(testName);
    }

    /**
     * Test for some common xml naming
     */
    public void testXMLNameTest()
    {
        // just go through some common cases to make sure code is working
        assertEquals("Testing name 'Name<'", false, XMLUtils.isWellFormedXMLName("Name<"));
        assertEquals("Testing name 'Name>'", false, XMLUtils.isWellFormedXMLName("Name>"));
        assertEquals("Testing name 'Name''", false, XMLUtils.isWellFormedXMLName("Name'"));
        assertEquals("Testing name 'Name_:-.'", true, XMLUtils.isWellFormedXMLName("Name_:-."));
        assertEquals("Testing name '.Name'", false, XMLUtils.isWellFormedXMLName(".Name"));
        assertEquals("Testing name '-Name'", false, XMLUtils.isWellFormedXMLName("-Name"));
        assertEquals("Testing name ':Name'", true, XMLUtils.isWellFormedXMLName(":Name"));
        assertEquals("Testing name '_Name'", true, XMLUtils.isWellFormedXMLName("_Name"));
        assertEquals("Testing name 'A0123456789Name", true, XMLUtils.isWellFormedXMLName("A0123456789Name"));
    }

    /** Test attribute escaping */
    public void testAttributeEscaping()
    {
        assertEquals("Escaping: <", "&lt;", XMLUtils.escapeAttributeValue("<"));
        assertEquals("Escaping: >", "&gt;", XMLUtils.escapeAttributeValue(">"));
        assertEquals("Escaping: '", "&apos;", XMLUtils.escapeAttributeValue("'"));
        assertEquals("Escaping: \"", "&quot;", XMLUtils.escapeAttributeValue("\""));
        assertEquals("Escaping: &", "&amp;", XMLUtils.escapeAttributeValue("&"));
        assertEquals("Escaping: 1<", "1&lt;", XMLUtils.escapeAttributeValue("1<"));
        assertEquals("Escaping: 1>", "1&gt;", XMLUtils.escapeAttributeValue("1>"));
        assertEquals("Escaping: 1'", "1&apos;", XMLUtils.escapeAttributeValue("1'"));
        assertEquals("Escaping: 1\"", "1&quot;", XMLUtils.escapeAttributeValue("1\""));
        assertEquals("Escaping: 1&", "1&amp;", XMLUtils.escapeAttributeValue("1&"));
        assertEquals("Escaping: <2", "&lt;2", XMLUtils.escapeAttributeValue("<2"));
        assertEquals("Escaping: >2", "&gt;2", XMLUtils.escapeAttributeValue(">2"));
        assertEquals("Escaping: '2", "&apos;2", XMLUtils.escapeAttributeValue("'2"));
        assertEquals("Escaping: \"2", "&quot;2", XMLUtils.escapeAttributeValue("\"2"));
        assertEquals("Escaping: &2", "&amp;2", XMLUtils.escapeAttributeValue("&2"));
        assertEquals("Escaping: a<b", "a&lt;b", XMLUtils.escapeAttributeValue("a<b"));
        assertEquals("Escaping: a>b", "a&gt;b", XMLUtils.escapeAttributeValue("a>b"));
        assertEquals("Escaping: a'b", "a&apos;b", XMLUtils.escapeAttributeValue("a'b"));
        assertEquals("Escaping: a\"b", "a&quot;b", XMLUtils.escapeAttributeValue("a\"b"));
        assertEquals("Escaping: a&b", "a&amp;b", XMLUtils.escapeAttributeValue("a&b"));
        assertEquals("Escaping: <<abba", "&lt;&lt;abba", XMLUtils.escapeAttributeValue("<<abba"));
        assertEquals("Escaping: >>abba", "&gt;&gt;abba", XMLUtils.escapeAttributeValue(">>abba"));
        assertEquals("Escaping: ''abba", "&apos;&apos;abba", XMLUtils.escapeAttributeValue("''abba"));
        assertEquals("Escaping: \"\"abba", "&quot;&quot;abba", XMLUtils.escapeAttributeValue("\"\"abba"));
        assertEquals("Escaping: &&abba", "&amp;&amp;abba", XMLUtils.escapeAttributeValue("&&abba"));
        assertEquals(
            "Escaping: a<>b''c\"e>f'&g",
            "a&lt;&gt;b&apos;&apos;c&quot;e&gt;f&apos;&amp;g",
            XMLUtils.escapeAttributeValue("a<>b''c\"e>f'&g"));

    }

    /**
     * Test CDATA escaping
     * Within a CDATA section, only the CDEnd
     * string ']]>' is recognized as markup.
     * Angle brackets and amphersands may occur in their literal form.
     */
    public void testCDATAEscaping()
    {
        assertEquals("Escaping: <", "<", XMLUtils.escapeCDATAContent("<"));
        assertEquals("Escaping: >", ">", XMLUtils.escapeCDATAContent(">"));
        assertEquals("Escaping: '", "'", XMLUtils.escapeCDATAContent("'"));
        assertEquals("Escaping: \"", "\"", XMLUtils.escapeCDATAContent("\""));
        assertEquals("Escaping: &", "&", XMLUtils.escapeCDATAContent("&"));
        assertEquals("Escaping: ]]", "]]", XMLUtils.escapeCDATAContent("]]"));
        assertEquals("Escaping: ]>", "]>", XMLUtils.escapeCDATAContent("]>"));
        assertEquals("Escaping: ]]>", "]]&gt;", XMLUtils.escapeCDATAContent("]]>"));
        assertEquals("Escaping: ]]>]]>", "]]&gt;]]&gt;", XMLUtils.escapeCDATAContent("]]>]]>"));
        assertEquals("Escaping: ]>]]>", "]>]]&gt;", XMLUtils.escapeCDATAContent("]>]]>"));
        assertEquals("Escaping: ]]>]]]>", "]]&gt;]]]&gt;", XMLUtils.escapeCDATAContent("]]>]]]>"));
        assertEquals("Escaping: ", "", XMLUtils.escapeCDATAContent(""));
    }


}

