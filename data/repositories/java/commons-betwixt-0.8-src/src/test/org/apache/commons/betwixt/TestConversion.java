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

import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.commons.betwixt.io.BeanReader;

/**
 * Tests conversions.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestConversion extends TestCase
{

    public TestConversion(String name)
    {
        super(name);
    }

    /**
     * Betwixt does not (by default) try to convert nulls and empty strings
     * @throws Exception
     */
    public void testNullTimestampConversion() throws Exception
    {
        String xml = "<?xml version='1.0'?>" +
                     "<EventBean>" +
                     "<type>WARNING</type>" +
                     "<start>2004-02-10 00:00:00.0</start>" +
                     "<end/>" +
                     "</EventBean>";

        StringReader in = new StringReader(xml);
        BeanReader reader = new BeanReader();
        reader.registerBeanClass(EventBean.class);
        EventBean bean = (EventBean) reader.parse(in);

        assertNotNull("Parsing should work", bean);
        assertEquals("Type property", "WARNING", bean.getType());
        assertEquals("Start property", "2004-02-10 00:00:00.0", bean.getStart().toString());
        assertNull("End property", bean.getEnd());
    }

}
