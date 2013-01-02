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


package org.apache.commons.betwixt.io.read;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestMaps extends AbstractTestCase
{

    public TestMaps(String testName)
    {
        super(testName);
    }

    public void testWriteConcreateMapImplementation() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        writer.getBindingConfiguration().setMapIDs(false);
        BeanWithConcreteMap bean = new BeanWithConcreteMap();
        bean.addSomeThingy("Aethelred", "The Unready");
        bean.addSomeThingy("Swein", "Forkbeard");
        bean.addSomeThingy("Thorkell", "The Tall");
        writer.write(bean);
        String xml = out.getBuffer().toString();

        StringBuffer buffer = new StringBuffer("<?xml version='1.0'?><BeanWithConcreteMap>");
        for (Iterator it=bean.getSomeThingies().keySet().iterator(); it.hasNext();)
        {
            String key = (String) it.next();
            if ("Aethelred".equals(key))
            {
                buffer.append(
                    "<entry>" +
                    "<key>Aethelred</key>" +
                    "<value>The Unready</value>" +
                    "</entry>");

            }
            else if ("Swein".equals(key))
            {
                buffer.append(
                    "<entry>" +
                    "<key>Swein</key>" +
                    "<value>Forkbeard</value>" +
                    "</entry>");

            }
            else if ("Thorkell".equals(key))
            {
                buffer.append(
                    "<entry>" +
                    "<key>Thorkell</key>" +
                    "<value>The Tall</value>" +
                    "</entry>");

            }
        }
        buffer.append("</BeanWithConcreteMap>");

        String expected = buffer.toString();

        xmlAssertIsomorphicContent(parseString(expected), parseString(xml), true);
    }


    public void testReadConcreateMapImplementation() throws Exception
    {
        StringReader in =  new StringReader("<?xml version='1.0'?><BeanWithConcreteMap>" +
                                            "<entry>" +
                                            "<key>Swein</key>" +
                                            "<value>Forkbeard</value>" +
                                            "</entry>" +
                                            "<entry>" +
                                            "<key>Thorkell</key>" +
                                            "<value>The Tall</value>" +
                                            "</entry>" +
                                            "<entry>" +
                                            "<key>Aethelred</key>" +
                                            "<value>The Unready</value>" +
                                            "</entry>" +
                                            "</BeanWithConcreteMap>");

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        reader.getBindingConfiguration().setMapIDs(false);
        reader.registerBeanClass(BeanWithConcreteMap.class);


        BeanWithConcreteMap bean = (BeanWithConcreteMap) reader.parse(in);
        assertNotNull("Parse failed", bean);

        Map map = bean.getSomeThingies();

        Set keyset = map.keySet();
        assertEquals("Three entries", 3, keyset.size());
        assertEquals("Aethelred The Unready", "The Unready", map.get("Aethelred"));
        assertEquals("Swein Forkbeardy", "Forkbeard", map.get("Swein"));
        assertEquals("Thorkell The Tall", "The Tall", map.get("Thorkell"));

    }

    public void testMapWithArray() throws Exception
    {

        AddressBook addressBook = new AddressBook();
        AddressBean[] johnsAddresses = new AddressBean[2];
        johnsAddresses[0] = new AddressBean("12 here", "Chicago", "USA", "1234");
        johnsAddresses[1] =
            new AddressBean("333 there", "Los Angeles", "USA", "99999");
        String name = "John";
        addressBook.addAddressBookItem(name, johnsAddresses);
        StringWriter outputWriter = new StringWriter();
        outputWriter.write("<?xml version='1.0' ?>\n");
        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.write(addressBook);

        String xml =
            "<?xml version='1.0' ?>\n"
            + "  <AddressBook id=\"1\">\n"
            + "    <addressBookItems>\n"
            + "      <entry id=\"2\">\n"
            + "        <key>John</key>\n"
            + "        <value id=\"3\">\n"
            + "          <AddressBean id=\"4\">\n"
            + "            <city>Chicago</city>\n"
            + "            <code>1234</code>\n"
            + "            <country>USA</country>\n"
            + "            <street>12 here</street>\n"
            + "          </AddressBean>\n"
            + "          <AddressBean id=\"5\">\n"
            + "            <city>Los Angeles</city>\n"
            + "            <code>99999</code>\n"
            + "            <country>USA</country>\n"
            + "            <street>333 there</street>\n"
            + "          </AddressBean>\n"
            + "        </value>\n"
            + "      </entry>\n"
            + "    </addressBookItems>\n"
            + "  </AddressBook>\n";

        xmlAssertIsomorphicContent(parseString(xml), parseString(outputWriter.toString()), true);
        BeanReader reader = new BeanReader();
        reader.registerBeanClass(AddressBook.class);
        StringReader xmlReader = new StringReader(outputWriter.toString());
        AddressBook result = (AddressBook) reader.parse(xmlReader);
        assertNotNull("Expected to get an AddressBook!", result);
        assertNotNull(
            "Expected AddressBook to have some address entryitems!",
            result.getAddressBookItems());
        AddressBean[] resultAddresses =
            (AddressBean[]) result.getAddressBookItems().get(name);
        assertNotNull(
            "Expected to have some addresses for " + name,
            resultAddresses);
        assertEquals(
            "Got wrong city in first address for " + name,
            johnsAddresses[0].getCity(),
            resultAddresses[0].getCity());
        assertEquals(
            "Got wrong city in second address for " + name,
            johnsAddresses[1].getCity(),
            resultAddresses[1].getCity());
    }
}
