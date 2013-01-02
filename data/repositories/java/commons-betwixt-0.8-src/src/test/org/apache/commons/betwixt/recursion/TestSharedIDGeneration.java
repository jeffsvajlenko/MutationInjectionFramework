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
package org.apache.commons.betwixt.recursion;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestSharedIDGeneration extends AbstractTestCase
{

    public TestSharedIDGeneration(String testName)
    {
        super(testName);
    }

    public void testSharedChild() throws Exception
    {

        NameBean name = new NameBean("Me");

        HybridBean hybrid = new HybridBean(new AlienBean(name), new PersonBean(name));

        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        writer.write(hybrid);

        boolean isAlienBeforePerson = false;
        PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(HybridBean.class).getPropertyDescriptors();

        for(int i=0; i<propertyDescriptors.length; i++)
        {
            String methodName = propertyDescriptors[i].getName();
            if ("alien".equals(methodName))
            {
                isAlienBeforePerson = true;
                break;
            }
            else if ("person".equals(methodName))
            {
                isAlienBeforePerson = false;
                break;
            }
        }
        String expected = "<?xml version='1.0'?><HybridBean id='1'>" +
                          "<person id='2'><name id='3'><moniker>Me</moniker></name></person>" +
                          "<alien id='4'><name idref='3'/></alien>" +
                          "</HybridBean>";

        if (isAlienBeforePerson)
        {
            expected = "<?xml version='1.0'?><HybridBean id='1'>" +
                       "<alien id='2'><name id='3'><moniker>Me</moniker></name></alien>" +
                       "<person id='4'><name idref='3'/></person>" +
                       "</HybridBean>";
        }

        xmlAssertIsomorphic(parseString(expected), parseString(out), true);
    }

}
