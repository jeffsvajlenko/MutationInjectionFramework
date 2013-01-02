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
import java.util.Collection;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

public class TestConversionFlavour extends AbstractTestCase
{

    public TestConversionFlavour(String testName)
    {
        super(testName);
    }


    public void testRead() throws Exception
    {
        String xml = "<alpha>" +
                     "        <name>BananasSIX</name>" +
                     "        <betaBean>" +
                     "            <name>PeachONE</name>" +
                     "        </betaBean>" +
                     "        <children>" +
                     "            <child>" +
                     "                <name>PeachTWO</name>" +
                     "            </child>" +
                     "        </children>" +
                     "        <mapped>" +
                     "          <entry>" +
                     "            <key>Key</key>" +
                     "            <value>" +
                     "               <name>PeachTHREE</name>" +
                     "            </value>" +
                     "          </entry>" +
                     "        </mapped>" +
                     "        </alpha>";

        StringReader in = new StringReader(xml);
        BeanReader reader = new BeanReader();
        reader.getBindingConfiguration().setMapIDs(false);
        reader.getBindingConfiguration().setObjectStringConverter(new PrependingConverter());
        reader.registerBeanClass(AlphaBean.class);
        AlphaBean bean = (AlphaBean) reader.parse(in);
        assertNotNull(bean);
        assertEquals("SIX", bean.getName());
        BetaBean betaBean = bean.getBetaBean();
        assertNotNull(betaBean);
        assertEquals("ONE", betaBean.getName());
        Collection children = bean.getChildren();
        assertEquals(1, children.size());
        BetaBean child = (BetaBean) children.iterator().next();
        assertEquals("TWO", child.getName());
    }

    public void testWrite() throws Exception
    {
        AlphaBean alphaBean = new AlphaBean();
        alphaBean.setName("SIX");
        BetaBean betaBeanOne = new BetaBean("ONE");
        alphaBean.setBetaBean(betaBeanOne);
        BetaBean betaBeanTwo = new BetaBean("TWO");
        alphaBean.addChild(betaBeanTwo);
        BetaBean betaBeanThree = new BetaBean("THREE");
        alphaBean.put("Key", betaBeanThree);

        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getBindingConfiguration().setObjectStringConverter(new PrependingConverter());
        writer.write(alphaBean);

        String xml = "<alpha>" +
                     "        <name>BananasSIX</name>" +
                     "        <betaBean>" +
                     "            <name>PeachONE</name>" +
                     "        </betaBean>" +
                     "        <children>" +
                     "            <child>" +
                     "                <name>PeachTWO</name>" +
                     "            </child>" +
                     "        </children>" +
                     "        <mapped>" +
                     "          <entry>" +
                     "            <key>Key</key>" +
                     "            <value>" +
                     "               <name>PeachTHREE</name>" +
                     "            </value>" +
                     "          </entry>" +
                     "        </mapped>" +
                     "        </alpha>";

        xmlAssertIsomorphicContent(parseString(xml), parseString(out), true);
    }


    public static final class PrependingConverter extends DefaultObjectStringConverter
    {

        public String objectToString(Object object, Class type, String flavour,
                                     Context context)
        {
            String result = super.objectToString(object, type, flavour, context);
            if (flavour != null)
            {
                result = flavour + result;
            }
            return result;
        }

        public Object stringToObject(String value, Class type, String flavour,
                                     Context context)
        {
            if (flavour != null)
            {
                value = value.substring(flavour.length());
            }
            return super.stringToObject(value, type, flavour, context);
        }
    }
}
