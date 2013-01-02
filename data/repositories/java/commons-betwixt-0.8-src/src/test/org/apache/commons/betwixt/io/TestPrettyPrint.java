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
import org.apache.commons.betwixt.ElementDescriptor;
import org.xml.sax.helpers.AttributesImpl;


public class TestPrettyPrint extends AbstractTestCase
{

    public TestPrettyPrint(String testName)
    {
        super(testName);
    }

    public void testEndElement() throws Exception
    {
        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        writer.enablePrettyPrint();
        writer.setEndTagForEmptyElement(true);

        WriteContext context = new WriteContext()
        {

            public ElementDescriptor getCurrentDescriptor()
            {
                return null;
            }

        };

        writer.startElement(context, "", "emptytag", "emptytag", new AttributesImpl());
        writer.endElement(context, "", "emptytag", "emptytag");

        assertEquals("",  "<emptytag></emptytag>", out.toString().trim());
    }
}
