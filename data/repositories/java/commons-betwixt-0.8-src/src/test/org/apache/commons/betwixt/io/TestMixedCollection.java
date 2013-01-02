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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.betwixt.AbstractTestCase;
import org.xml.sax.InputSource;


public class TestMixedCollection extends AbstractTestCase
{
    public TestMixedCollection(String name)
    {
        super(name);
    }

    public void testWithDefaults() throws Exception
    {
        toXml(true);
    }

    public void testWithoutDefaults() throws Exception
    {
        toXml(false);
    }

    protected void toXml(boolean addAdders) throws Exception
    {
        StringReader configReader = new StringReader(
            "<?xml version='1.0' ?>"
            + "<betwixt-config primitiveTypes='attribute'>"
            + "    <class name='org.apache.commons.betwixt.io.TestMixedCollection$ParentBean'>"
            + "        <element name='parentBean'>"
            + "            <element name='childBeans'>"
            + "                <element property='childBeans'/>"
            + "            </element>"
            + "            <addDefaults add-properties='true' guess-names='false' add-adders='"
            + addAdders
            + "'/>"
            + "        </element>"
            + "    </class>"
            + "    <class name='org.apache.commons.betwixt.io.TestMixedCollection$ChildBean1'>"
            + "        <element name='childBean1'>"
            + "            <addDefaults/>"
            + "        </element>"
            + "    </class>"
            + "    <class name='org.apache.commons.betwixt.io.TestMixedCollection$ChildBean2'>"
            + "        <element name='childBean2'>"
            + "            <addDefaults/>" + "        </element>"
            + "    </class>" + "</betwixt-config>");

        ParentBean pb = new ParentBean();
        pb.setStuff("stuff");
        ChildBean1 cb1 = new ChildBean1();
        pb.getChildBeans().add(cb1);
        ChildBean2 cb2 = new ChildBean2();
        pb.getChildBeans().add(cb2);

        StringWriter writer = new StringWriter();
        BeanWriter beanWriter = new BeanWriter(writer);
        beanWriter.enablePrettyPrint();
        beanWriter.getXMLIntrospector().register(
            new InputSource(configReader));
        beanWriter.writeXmlDeclaration("<?xml version=\"1.0\"?>");
        beanWriter.write(pb);

        String expected = "<?xml version='1.0'?>" +
                          "<parentBean stuff='stuff' id='1'>" +
                          "     <childBeans>" +
                          "            <childBean1/>" +
                          "            <childBean2/>" +
                          "     </childBeans>" +
                          "</parentBean>";

        xmlAssertIsomorphic(parseString(expected), parseString(writer));
    }

    public static class ParentBean
    {
        private List childBeans = new ArrayList();

        private String stuff = null;

        public List getChildBeans()
        {
            return childBeans;
        }

        public void setChildBeans(List childBeans)
        {
            this.childBeans = childBeans;
        }

        public void addChildBean(ChildBean childBean)
        {
            getChildBeans().add(childBean);
        }

        public String getStuff()
        {
            return stuff;
        }

        public void setStuff(String stuff)
        {
            this.stuff = stuff;
        }
    }

    public static abstract class ChildBean
    {
    }

    public static class ChildBean1 extends ChildBean
    {
    }

    public static class ChildBean2 extends ChildBean
    {
    }
}
