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
import org.apache.commons.betwixt.LoopBean;

/**
 */
public class TestIgnoreEmptyElements extends AbstractTestCase
{


    public TestIgnoreEmptyElements(String testName)
    {
        super(testName);
    }

    public void testWritePersonBean() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements(false);
        SidekickBean sidekick = new SidekickBean("Robin");
        SuperheroBean superhero = new SuperheroBean(sidekick);
        writer.write(superhero);
        String expected = "<?xml version='1.0'?>" +
                          "<SuperheroBean id='1'>" +
                          "  <sidekick id='2'><nickname>Robin</nickname></sidekick>" +
                          "</SuperheroBean>";
        String xml = out.toString();
        xmlAssertIsomorphic(parseString(expected), parseString(xml));
    }


    /** Test nested case for writing empty elements */
    public void testNestedWriteEmptyElements() throws Exception
    {

        // write same bean both times
        LoopBean root = new LoopBean("base");
        LoopBean middle = new LoopBean(null);
        root.setFriend(middle);
        middle.setFriend(new LoopBean(null));

        // test output when writing empty elements
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements(true);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(root);
        String xml = "<?xml version='1.0'?><LoopBean><name>base</name><friend><name/><friend><name/></friend>"
                     + "</friend></LoopBean>";
        xmlAssertIsomorphicContent(parseString(out.getBuffer().toString()),parseString(xml), true);

        // test output when not writing empty elements
        out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        writer = new BeanWriter(out);
        writer.setWriteEmptyElements(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(root);
        xml = "<?xml version='1.0'?><LoopBean><name>base</name></LoopBean>";
        xmlAssertIsomorphicContent(parseString(out.getBuffer().toString()),parseString(xml), true);

    }
}
