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

import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestMaps extends AbstractTestCase
{

    public TestMaps(String testName)
    {
        super(testName);
    }

    public void testHashMapWriteEmpty() throws Exception
    {

        Map hash = new Hashtable();
        hash.put("one", "un");
        hash.put("two", "deux");
        hash.put("three", "trois");

        String expected = "<?xml version='1.0'?>" +
                          "<Hashtable>" +
                          "	<empty>false</empty>" +
                          "    <entry>" +
                          "      <key>two</key>" +
                          "      <value>deux</value>" +
                          "    </entry>" +
                          "   <entry>" +
                          "      <key>one</key>" +
                          "      <value>un</value>" +
                          "    </entry>" +
                          "    <entry>" +
                          "      <key>three</key>" +
                          "      <value>trois</value>" +
                          "    </entry>" +
                          "  </Hashtable>";

        StringWriter out = new StringWriter();

        BeanWriter beanWriter = new BeanWriter(out);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.setWriteEmptyElements(false);
        beanWriter.getBindingConfiguration().setMapIDs(false);
        beanWriter.setXMLIntrospector(new XMLIntrospector());
        beanWriter.write(hash);

        xmlAssertIsomorphic(parseString(expected), parseString(out));
    }

    public void testHashMapWriteNotEmpty() throws Exception
    {

        Map hash = new Hashtable();
        hash.put("one", "un");
        hash.put("two", "deux");
        hash.put("three", "trois");

        String expected = "<?xml version='1.0'?>" +
                          "<Hashtable>" +
                          "	<empty>false</empty>" +
                          "    <entry>" +
                          "      <key>two</key>" +
                          "      <value>deux</value>" +
                          "    </entry>" +
                          "   <entry>" +
                          "      <key>one</key>" +
                          "      <value>un</value>" +
                          "    </entry>" +
                          "    <entry>" +
                          "      <key>three</key>" +
                          "      <value>trois</value>" +
                          "    </entry>" +
                          "  </Hashtable>";

        StringWriter out = new StringWriter();

        BeanWriter beanWriter = new BeanWriter(out);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.setWriteEmptyElements(true);
        beanWriter.getBindingConfiguration().setMapIDs(false);
        beanWriter.setXMLIntrospector(new XMLIntrospector());
        beanWriter.write(hash);

        xmlAssertIsomorphic(parseString(expected), parseString(out));
    }

}
