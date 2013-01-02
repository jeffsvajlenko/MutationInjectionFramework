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
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * Test harness for bean creation (during reading).
 *
 * @author Robert Burrell Donkin
 * @version $Id: TestBeanCreation.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestBeanCreation extends AbstractTestCase
{

    public TestBeanCreation(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(TestBeanCreation.class);
    }

    public void testCustomCreatorOne() throws Exception
    {
        HouseBeans houses = new HouseBeans();
        HouseBean houseOne = new HouseBean();
        houseOne.setFacing(CompassPoint.NORTH);
        houseOne.setAddress(new AddressBean("Black Bull, 46 Briggate", "Brighouse", "England", "HD6 1EF"));
        houseOne.setHouseholder(new PersonBean("Samual", "Smith"));
        houseOne.setTenant(false);
        houses.addHouse(houseOne);
        HouseBean houseTwo = new HouseBean();
        houseTwo.setFacing(CompassPoint.SOUTH);
        houseTwo.setAddress(new AddressBean("The Commerical Inn, 1 Gooder Lane", "Brighouse", "England", "HD6 1HT"));
        houseTwo.setHouseholder(new PersonBean("Timothy", "Tayler"));
        houseTwo.setTenant(true);
        houses.addHouse(houseTwo);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        writer.write("houses", houses);

        String xml = "<?xml version='1.0'?><houses>"
                     + "<house tenant='false'>"
                     + "<address street='Black Bull, 46 Briggate' city='Brighouse' country='England' code='HD6 1EF'/>"
                     + "<householder forename='Samual' surname='Smith'/>"
                     + "<facing name='North'/>"
                     + "</house>"
                     + "<house tenant='true'>"
                     + "<address street='The Commerical Inn, 1 Gooder Lane' city='Brighouse'"
                     + " country='England' code='HD6 1HT'/>"
                     + "<householder forename='Timothy' surname='Tayler'/>"
                     + "<facing name='South'/>"
                     + "</house>"
                     + "</houses>";

        xmlAssertIsomorphic(parseString(xml), parseString(out.toString()), true);

        BeanCreationList chain = BeanCreationList.createStandardChain();
        // add a filter that creates enums to the start

        class EnumCreator implements ChainedBeanCreator
        {

            public Object create(ElementMapping mapping, ReadContext context, BeanCreationChain chain)
            {
                if ("facing".equals(mapping.getName()))
                {
                    String value = mapping.getAttributes().getValue("name");
                    if ("North".equals(value))
                    {
                        return CompassPoint.NORTH;
                    }
                    if ("South".equals(value))
                    {
                        return CompassPoint.SOUTH;
                    }
                    if ("East".equals(value))
                    {
                        return CompassPoint.EAST;
                    }
                    if ("West".equals(value))
                    {
                        return CompassPoint.WEST;
                    }
                }
                return chain.create(mapping, context);
            }
        }
        chain.insertBeanCreator(1, new EnumCreator());

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        reader.registerBeanClass("houses", HouseBeans.class);
        reader.getReadConfiguration().setBeanCreationChain(chain);

        StringReader in = new StringReader(xml);
        HouseBeans newHouses = (HouseBeans) reader.parse(in);
        assertNotNull("Parsing should return a bean", newHouses);

        ArrayList houseList = newHouses.houses;
        assertEquals("Should be two houses read", 2, houseList.size());
        HouseBean newOne = (HouseBean) houseList.get(0);
        HouseBean newTwo = (HouseBean) houseList.get(1);
        assertEquals("First house is equal",  houseOne, newOne);
        assertEquals("Second house is equal",  houseTwo, newTwo);

    }

    public void testCustomCreatorTwo() throws Exception
    {
        HouseBeans houses = new HouseBeans();
        HouseBean houseOne = new HouseBean();
        houseOne.setFacing(CompassPoint.NORTH);
        houseOne.setAddress(new AddressBean("Black Bull, 46 Briggate", "Brighouse", "England", "HD6 1EF"));
        houseOne.setHouseholder(new PersonBean("Samual", "Smith"));
        houseOne.setTenant(false);
        houses.addHouse(houseOne);
        HouseBean houseTwo = new HouseBean();
        houseTwo.setFacing(CompassPoint.SOUTH);
        houseTwo.setAddress(new AddressBean("The Commerical Inn, 1 Gooder Lane", "Brighouse", "England", "HD6 1HT"));
        houseTwo.setHouseholder(new PersonBean("Timothy", "Tayler"));
        houseTwo.setTenant(true);
        houses.addHouse(houseTwo);

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        writer.write("houses", houses);

        String xml = "<?xml version='1.0'?><houses>"
                     + "<house tenant='false'>"
                     + "<address street='Black Bull, 46 Briggate' city='Brighouse' country='England' code='HD6 1EF'/>"
                     + "<householder forename='Samual' surname='Smith'/>"
                     + "<facing name='North'/>"
                     + "</house>"
                     + "<house tenant='true'>"
                     + "<address street='The Commerical Inn, 1 Gooder Lane' city='Brighouse'"
                     + " country='England' code='HD6 1HT'/>"
                     + "<householder forename='Timothy' surname='Tayler'/>"
                     + "<facing name='South'/>"
                     + "</house>"
                     + "</houses>";

        xmlAssertIsomorphic(parseString(xml), parseString(out.toString()), true);

        BeanCreationList chain = BeanCreationList.createStandardChain();
        // add a filter that creates enums to the start

        class EnumCreator implements ChainedBeanCreator
        {
            // match by class this time
            public Object create(ElementMapping mapping, ReadContext context, BeanCreationChain chain)
            {
                if (CompassPoint.class.equals(mapping.getType()))
                {
                    String value = mapping.getAttributes().getValue("name");
                    if ("North".equals(value))
                    {
                        return CompassPoint.NORTH;
                    }
                    if ("South".equals(value))
                    {
                        return CompassPoint.SOUTH;
                    }
                    if ("East".equals(value))
                    {
                        return CompassPoint.EAST;
                    }
                    if ("West".equals(value))
                    {
                        return CompassPoint.WEST;
                    }
                }
                return chain.create(mapping, context);
            }
        }
        chain.insertBeanCreator(1, new EnumCreator());

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(false);
        reader.registerBeanClass("houses", HouseBeans.class);
        reader.getReadConfiguration().setBeanCreationChain(chain);

        StringReader in = new StringReader(xml);
        HouseBeans newHouses = (HouseBeans) reader.parse(in);
        assertNotNull("Parsing should return a bean", newHouses);

        ArrayList houseList = newHouses.houses;
        assertEquals("Should be two houses read", 2, houseList.size());
        HouseBean newOne = (HouseBean) houseList.get(0);
        HouseBean newTwo = (HouseBean) houseList.get(1);
        assertEquals("First house is equal",  houseOne, newOne);
        assertEquals("Second house is equal",  houseTwo, newTwo);
    }
}
