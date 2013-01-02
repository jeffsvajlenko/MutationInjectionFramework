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

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.InputSource;

/**
 */
public class TestIdStorageStrategy extends AbstractTestCase
{

    public TestIdStorageStrategy(String testName)
    {
        super(testName);
    }

    public void testWrite() throws Exception
    {

        final Element alpha = new Element("ONE");
        Element beta = new Element("TWO");
        ElementsList elements = new ElementsList();
        elements.addElement(alpha);
        elements.addElement(beta);

        IdStoringStrategy storingStrategy = new DefaultIdStoringStrategy()
        {

            public String getReferenceFor(Context context, Object bean)
            {
                String result = null;
                if (bean == alpha)
                {
                    result = "ALPHA";
                }
                else
                {
                    result = super.getReferenceFor(context, bean);
                }
                return result;
            }

            public void setReference(Context context, Object bean, String id)
            {
                if (bean != alpha)
                {
                    super.setReference(context, bean, id);
                }
            }
        };

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setIdMappingStrategy(storingStrategy);
        writer.write(elements);

        String expected = "<?xml version='1.0'?>" +
                          "<ElementsList id='1'>" +
                          "   <elements>" +
                          "       <element idref='ALPHA'/>" +
                          "       <element id='2'>" +
                          "           <value>TWO</value>" +
                          "       </element>" +
                          "   </elements>" +
                          "</ElementsList>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }

    public void testRead() throws Exception
    {

        String xml = "<?xml version='1.0'?>" +
                     "<ElementsList id='1'>" +
                     "   <elements>" +
                     "       <element idref='ALPHA'/>" +
                     "       <element id='2'>" +
                     "           <value>TWO</value>" +
                     "       </element>" +
                     "   </elements>" +
                     "</ElementsList>";

        final Element alpha = new Element("ONE");

        IdStoringStrategy storingStrategy = new DefaultIdStoringStrategy()
        {

            public void setReference(Context context, Object bean, String id)
            {
                if (bean != alpha)
                {
                    super.setReference(context, bean, id);
                }
            }

            public Object getReferenced(Context context, String id)
            {
                if ("ALPHA".equals(id))
                {
                    return alpha;
                }
                return getReferenced(context, id);
            }

        };

        BeanReader reader = new BeanReader();
        reader.getBindingConfiguration().setIdMappingStrategy(storingStrategy);
        reader.registerBeanClass(ElementsList.class);
        ElementsList elements = (ElementsList) reader.parse(new StringReader(xml));
        assertNotNull(elements);
        Element one = elements.get(0);
        assertTrue(one == alpha);
        Element two = elements.get(1);
        assertNotNull(two);
    }


    public void testWriteWithOptions() throws Exception
    {

        final Element alpha = new Element("ONE");
        Element beta = new Element("TWO");
        ElementsList elements = new ElementsList();
        elements.addElement(alpha);
        elements.addElement(beta);

        String MAPPING = "<?xml version='1.0'?>" +
                         "<betwixt-config>" +
                         "  <class name=\"" + ElementsList.class.getName() + "\">" +
                         "    <element name=\"ElementsList\">" +
                         "      <option>" +
                         "        <name>id-strategy-prefix</name>" +
                         "        <value>alice</value>" +
                         "      </option>" +
                         "      <element name=\"elements\">" +
                         "        <element property=\"elements\">" +
                         "          <option>" +
                         "            <name>id-strategy-prefix</name>" +
                         "            <value>bob</value>" +
                         "          </option>" +
                         "        </element>" +
                         "      </element>" +
                         "    </element>" +
                         "  </class>" +
                         "</betwixt-config>";

        IdStoringStrategy storingStrategy = new DefaultIdStoringStrategy()
        {

            public String getReferenceFor(Context context, Object bean)
            {
                String result = null;
                if( bean instanceof ElementsList)
                {
                    assertNotNull( context.getOptions() );
                    assertEquals("Checking ElementsList option","alice",context.getOptions().getValue("id-strategy-prefix"));
                }
                if( bean instanceof Element)
                {
                    assertNotNull( context.getOptions() );
                    assertEquals("Checking Element option","bob",context.getOptions().getValue("id-strategy-prefix"));
                }
                if (bean == alpha)
                {
                    result = "ALPHA";
                }
                else
                {
                    result = super.getReferenceFor(context, bean);
                }
                return result;
            }

            public void setReference(Context context, Object bean, String id)
            {
                if (bean != alpha)
                {
                    super.setReference(context, bean, id);
                }
            }
        };

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setIdMappingStrategy(storingStrategy);
        writer.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        writer.write(elements);

        String expected = "<?xml version='1.0'?>" +
                          "<ElementsList id='1'>" +
                          "   <elements>" +
                          "       <Element idref='ALPHA'/>" +
                          "       <Element id='2'>" +
                          "           <value>TWO</value>" +
                          "       </Element>" +
                          "   </elements>" +
                          "</ElementsList>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }

    public void testWriteWithParentOptions() throws Exception
    {

        AlphaBean alpha = new AlphaBean();
        alpha.setName("apple");
        BetaBean beta = new BetaBean();
        beta.setName("banana");
        alpha.setBetaBean(beta);

        String MAPPING = "<?xml version='1.0'?>" +
                         "<betwixt-config>" +
                         "  <class name=\"" + AlphaBean.class.getName() + "\">" +
                         "    <element name=\"alpha\">" +
                         "      <element name=\"name\" property=\"name\" />" +
                         "      <element property=\"betaBean\">" +
                         "        <option>" +
                         "          <name>id-strategy-prefix</name>" +
                         "          <value>parent</value>" +
                         "        </option>" +
                         "      </element>" +
                         "    </element>" +
                         "  </class>" +
                         "  <class name=\"" + BetaBean.class.getName() + "\">" +
                         "    <element name=\"beta\">" +
                         "      <element name=\"name\" property=\"name\" />" +
                         "    </element>" +
                         "  </class>" +
                         "</betwixt-config>";

        IdStoringStrategy storingStrategy = new DefaultIdStoringStrategy()
        {
            public String getReferenceFor(Context context, Object bean)
            {
                if( bean instanceof BetaBean)
                {
                    assertNotNull( context.getOptions() );
                    assertEquals("Checking BetaBean option","parent",context.getOptions().getValue("id-strategy-prefix"));
                }
                return super.getReferenceFor(context, bean);
            }
        };

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setIdMappingStrategy(storingStrategy);
        writer.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        writer.write(alpha);

        String expected = "<?xml version='1.0'?>" +
                          "<alpha id=\"1\">" +
                          "  <name>apple</name>" +
                          "  <beta id=\"2\">" +
                          "    <name>banana</name>"+
                          "  </beta>" +
                          "</alpha>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }

    public void testWriteWithTargetOptions() throws Exception
    {

        AlphaBean alpha = new AlphaBean();
        alpha.setName("apple");
        BetaBean beta = new BetaBean();
        beta.setName("banana");
        alpha.setBetaBean(beta);

        String MAPPING = "<?xml version='1.0'?>" +
                         "<betwixt-config>" +
                         "  <class name=\"" + AlphaBean.class.getName() + "\">" +
                         "    <element name=\"alpha\">" +
                         "      <element name=\"name\" property=\"name\" />" +
                         "      <element property=\"betaBean\" />" +
                         "    </element>" +
                         "  </class>" +
                         "  <class name=\"" + BetaBean.class.getName() + "\">" +
                         "    <element name=\"beta\">" +
                         "      <option>" +
                         "        <name>id-strategy-prefix</name>" +
                         "        <value>target</value>" +
                         "      </option>" +
                         "      <element name=\"name\" property=\"name\" />" +
                         "    </element>" +
                         "  </class>" +
                         "</betwixt-config>";

        IdStoringStrategy storingStrategy = new DefaultIdStoringStrategy()
        {
            public String getReferenceFor(Context context, Object bean)
            {
                if( bean instanceof BetaBean)
                {
                    assertNotNull( context.getOptions() );
                    assertEquals("Checking BetaBean option","target",context.getOptions().getValue("id-strategy-prefix"));
                }
                return super.getReferenceFor(context, bean);
            }
        };

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setIdMappingStrategy(storingStrategy);
        writer.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        writer.write(alpha);

        String expected = "<?xml version='1.0'?>" +
                          "<alpha id=\"1\">" +
                          "  <name>apple</name>" +
                          "  <beta id=\"2\">" +
                          "    <name>banana</name>"+
                          "  </beta>" +
                          "</alpha>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }

    public void testWriteWithParentAndTargetOptions() throws Exception
    {

        AlphaBean alpha = new AlphaBean();
        alpha.setName("apple");
        BetaBean beta = new BetaBean();
        beta.setName("banana");
        alpha.setBetaBean(beta);

        String MAPPING = "<?xml version='1.0'?>" +
                         "<betwixt-config>" +
                         "  <class name=\"" + AlphaBean.class.getName() + "\">" +
                         "    <element name=\"alpha\">" +
                         "      <element name=\"name\" property=\"name\" />" +
                         "      <element property=\"betaBean\">" +
                         "        <option>" +
                         "          <name>id-strategy-prefix</name>" +
                         "          <value>parent</value>" +
                         "        </option>" +
                         "      </element>" +
                         "    </element>" +
                         "  </class>" +
                         "  <class name=\"" + BetaBean.class.getName() + "\">" +
                         "    <element name=\"beta\">" +
                         "      <option>" +
                         "        <name>id-strategy-prefix</name>" +
                         "        <value>target</value>" +
                         "      </option>" +
                         "      <element name=\"name\" property=\"name\" />" +
                         "    </element>" +
                         "  </class>" +
                         "</betwixt-config>";

        IdStoringStrategy storingStrategy = new DefaultIdStoringStrategy()
        {
            public String getReferenceFor(Context context, Object bean)
            {
                if( bean instanceof BetaBean)
                {
                    assertNotNull( context.getOptions() );
                    assertEquals("Checking BetaBean option","parent",context.getOptions().getValue("id-strategy-prefix"));
                }
                return super.getReferenceFor(context, bean);
            }
        };

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setIdMappingStrategy(storingStrategy);
        writer.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        writer.write(alpha);

        String expected = "<?xml version='1.0'?>" +
                          "<alpha id=\"1\">" +
                          "  <name>apple</name>" +
                          "  <beta id=\"2\">" +
                          "    <name>banana</name>"+
                          "  </beta>" +
                          "</alpha>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }
}
