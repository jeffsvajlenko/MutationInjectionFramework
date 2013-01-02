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
package org.apache.commons.betwixt.io;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.betwixt.AbstractTestCase;
import org.xml.sax.InputSource;

public class TestMapUpdater extends AbstractTestCase
{

    public TestMapUpdater(String testName)
    {
        super(testName);
    }

    public void testMapUpdater() throws Exception
    {
        String config = "<?xml version='1.0'?>"
                        + "<betwixt-config primitiveTypes='attribute'>"
                        + "    <class name='org.apache.commons.betwixt.io.TestMapUpdater$ParentBean'>"
                        + "        <element name='parentBean'>"
                        + "            <element name='pairs'>"
                        + "                <element property='pairs' updater='addPair'/>"
                        + "            </element>"
                        + "            <addDefaults add-properties='true' add-adders='false'/>"
                        + "        </element>" + "    </class>" + "</betwixt-config>";

        String result = "<?xml version=\"1.0\"?>\n"
                        + "  <parentBean id=\"1\">\n" + "    <pairs>\n"
                        + "      <entry id=\"2\">\n" + "        <key>key</key>\n"
                        + "        <value>value</value>\n" + "      </entry>\n"
                        + "    </pairs>\n" + "  </parentBean>\n";

        ParentBean pb = new ParentBean();
        pb.getPairs().put("key", "value");

        StringWriter writer = new StringWriter();
        BeanWriter beanWriter = new BeanWriter(writer);
        beanWriter.enablePrettyPrint();
        beanWriter.getXMLIntrospector().register(
            new InputSource(new StringReader(config)));
        beanWriter.writeXmlDeclaration("<?xml version=\"1.0\"?>");
        beanWriter.write(pb);

        xmlAssertIsomorphic(parseString(result), parseString(writer));

        BeanReader beanReader = new BeanReader();
        beanReader.registerMultiMapping(new InputSource(
                                            new StringReader(config)));

        ParentBean pbRead = (ParentBean) beanReader.parse(new StringReader(
                                writer.toString()));

        StringWriter writer2 = new StringWriter();
        BeanWriter beanWriter2 = new BeanWriter(writer2);
        beanWriter2.enablePrettyPrint();
        beanWriter2.getXMLIntrospector().register(
            new InputSource(new StringReader(config)));
        beanWriter2.writeXmlDeclaration("<?xml version=\"1.0\"?>");
        beanWriter2.write(pbRead);

        xmlAssertIsomorphic(parseString(result), parseString(writer2));

    }

    public static class ParentBean
    {
        private Map pairs = new HashMap();

        public Map getPairs()
        {
            return pairs;
        }

        public void setPairs(Map pairs)
        {
            this.pairs = pairs;
        }

        public void addPair(String key, String value)
        {
            pairs.put(key, value);
        }

    }

}
