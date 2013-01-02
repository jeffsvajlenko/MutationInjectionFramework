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
package org.apache.commons.betwixt.derived;

import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;
import org.xml.sax.InputSource;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestWriteClass extends AbstractTestCase
{

    public TestWriteClass(String testName)
    {
        super(testName);
    }

    public void testDotBetwixtClass() throws Exception
    {
        String customDotBetwixt = "<?xml version='1.0'?><info primitiveTypes='attribute'>" +
                                  "<element name='type'>" +
                                  "<attribute property='class' name='classname'/>" +
                                  "</element>" +
                                  "</info>";

        EmployeeBean employeeBean = new EmployeeBean();
        employeeBean.setAge(32);
        employeeBean.setName("AN Other");
        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().register(EmployeeBean.class, new InputSource(new StringReader(customDotBetwixt)));
        writer.write(employeeBean);

        String expected = "<?xml version='1.0'?><type classname='org.apache.commons.betwixt.derived.EmployeeBean'/>";

        xmlAssertIsomorphicContent("Expected only class name to be mapped", parseString(expected), parseString(out.toString()));
    }

    public void testPropertySuppressionStrategy() throws Exception
    {

        BeanWithSecrets bean = new BeanWithSecrets("Surveyor Of The Queen's Pictures", "Queen Elizabeth II",
                "Sir Anthony Federick Blunt", "Fourth Man", "Soviet Union");
        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setPropertySuppressionStrategy(
            new PropertySuppressionStrategy()
        {

            public boolean suppressProperty(Class classContainingThePropety, Class propertyType, String propertyName)
            {
                if ("class".equals(propertyName))
                {
                    return true;
                }
                if (propertyName.startsWith("secret"))
                {
                    return true;
                }
                return false;
            }

        });
        writer.write("normal-person", bean);

        String expected = "<?xml version='1.0'?><normal-person>" +
                          "<employer>Queen Elizabeth II</employer>" +
                          "<job>Surveyor Of The Queen's Pictures</job>" +
                          "<name>Sir Anthony Federick Blunt</name>" +
                          "</normal-person>";

        xmlAssertIsomorphicContent("Expected secrets to be supressed", parseString(expected), parseString(out.toString()), true);

    }
}
