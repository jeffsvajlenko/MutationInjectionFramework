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
import java.util.Locale;

import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;

/**
 * This test is the result of a problem I had with outputting a bean's class
 * property as XML. I had a request for that feature quite a while ago and the
 * {@link #org.apache.commons.betwixt.strategy.PropertySupressionStretegy}was
 * added to made this possible. It worked quite well, until I used beans
 * described in dot-betwixt files that also output the class property like the
 * following:
 *
 * <pre>
 *   &lt;info primitiveTypes="element"&gt;
 *     &lt;element name="test-class"&gt;
 *       &lt;attribute name="test-prop-1" property="testPropertyOne"/&gt;
 *       &lt;attribute name="test-prop-2" property="testPropertyTwo"/&gt;
 *       &lt;element name="class" property="class"/&gt;
 *     &lt;/element&gt;
 *   &lt;/info&gt;
 * </pre>
 *
 * So it worked without dot-betwixt files, but the seconds test
 * {@link #testHasClassElementWithDotBetwixtFile()}would fail. There was a
 * small block in {@link org.apache.commons.betwixt.digester.ElementRule}that
 * was marked with ToDo, without that block it works.
 *
 * @author Christoph Gaffga, cgaffga@triplemind.com
 */
public class TestClassProperty extends AbstractTestCase
{

    public TestClassProperty(String testName)
    {
        super(testName);
    }

    public void testHasClassElementWithoutDotBetwixtFile() throws Exception
    {
        // configure bean writer with counting suppression strategy...
        StringWriter buffer = new StringWriter();
        BeanWriter beanWriter = new BeanWriter(buffer);
        beanWriter.getXMLIntrospector().getConfiguration().setPropertySuppressionStrategy(
            new PropertySuppressionStrategy()
        {

            public boolean suppressProperty(Class clazz, Class propertyType,
                                            String propertyName)
            {
                return false;
            }
        });

        // test with class without dot-betwixt file...
        Object bean = new Locale("de", "de"); // just a bean with some properties
        beanWriter.write(bean);

        // was the class element written?..
        assertTrue(buffer.toString().indexOf("<class>" + bean.getClass().getName() + "</class>") > 0);
    }

    public void testHasClassElementWithDotBetwixtFile() throws Exception
    {
        // configure bean writer with counting suppression strategy...
        StringWriter buffer = new StringWriter();
        BeanWriter beanWriter = new BeanWriter(buffer);
        beanWriter.getXMLIntrospector().getConfiguration().setPropertySuppressionStrategy(
            new PropertySuppressionStrategy()
        {

            public boolean suppressProperty(Class clazz, Class propertyType,
                                            String propertyName)
            {
                return false;
            }
        });

        // test with class without dot-betwixt file...
        Object bean = new SimpleClass();
        beanWriter.write(bean);

        // was the class element written?..
        assertTrue(buffer.toString().indexOf("<class>" + bean.getClass().getName() + "</class>") > 0);
    }
}
