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

import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;

/**
 */
public class TestWriteIDs extends AbstractTestCase
{

    public TestWriteIDs(String testName)
    {
        super(testName);
    }
    public void testTest() throws Exception
    {
        PersonBean bean = new PersonBean();
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter beanWriter = new BeanWriter(out);
        beanWriter.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        beanWriter.getBindingConfiguration().setMapIDs(true);
        beanWriter.enablePrettyPrint();
        beanWriter.write("bean", bean);

        String expected = "<?xml version='1.0'?><bean id='1'/>";
        xmlAssertIsomorphic(parseString(expected), parseString(out), true);
    }
}
