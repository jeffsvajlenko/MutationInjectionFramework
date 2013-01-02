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


package org.apache.commons.betwixt.dotbetwixt;

import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestDotBetwixtNamespace extends AbstractTestCase
{

    public TestDotBetwixtNamespace(String name)
    {
        super(name);
    }


    public void testWriteSimpleDotBetwixtWithNamespaces() throws Exception
    {
        PersonWithNamespace bean = new PersonWithNamespace("Robert", "Burrell", "Donkin");
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().getPrefixMapper()
        .setPrefix("http://jakarta.apache.org/commons/betwixt/PersonWithNamespaceExample", "pn");
        writer.write(bean);

        String xml = out.getBuffer().toString();

        String expected = "<?xml version='1.0'?>" +
                          "<pn:person " +
                          "xmlns:pn='http://jakarta.apache.org/commons/betwixt/PersonWithNamespaceExample' " +
                          "pn:middle='Burrell'>" +
                          "<forename>Robert</forename>" +
                          "<pn:surname>Donkin</pn:surname></pn:person>";

        xmlAssertIsomorphicContent(parseString(xml), parseString(expected));
    }
}
