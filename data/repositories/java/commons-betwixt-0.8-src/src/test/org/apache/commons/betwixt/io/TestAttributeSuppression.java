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
import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.strategy.ValueSuppressionStrategy;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestAttributeSuppression extends AbstractTestCase
{

    public TestAttributeSuppression(String testName)
    {
        super(testName);
    }


    public void testEmptyStringSuppression() throws Exception
    {
        PersonBean bean = new PersonBean("Corwin", null);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);

        writer.write(bean);

        String expected = "<?xml version='1.0'?><PersonBean forenames='Corwin'/>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }

    public void testCustomStrategy() throws Exception
    {
        PersonBean bean = new PersonBean("Zaphod", "Beeblebrox");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getBindingConfiguration().setValueSuppressionStrategy(new ValueSuppressionStrategy()
        {

            public boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value)
            {
                return "Zaphod".equals(value);
            }
        });
        writer.write(bean);

        String expected = "<?xml version='1.0'?><PersonBean surname='Beeblebrox'/>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }
}
