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


import java.beans.IntrospectionException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Brian Pugh
 */
public class TestMultiMapping extends AbstractTestCase
{

    public TestMultiMapping(String testName)
    {
        super(testName);

    }

    public void testRoundTripWithSingleMappingFile() throws IOException, SAXException, IntrospectionException
    {
        AddressBean addressBean = new AddressBean();
        addressBean.setCity("New York");
        addressBean.setCode("92342");
        addressBean.setCountry("USA");
        addressBean.setStreet("12312 Here");
        PartyBean partyBean = new PartyBean();
        partyBean.setDateOfParty(new Date());
        partyBean.setExcuse("too late");
        partyBean.setFromHour(22);
        partyBean.setVenue(addressBean);

        InputSource source
            = new InputSource(
            new FileReader(getTestFile("src/test/org/apache/commons/betwixt/mapping.xml")));

        StringWriter outputWriter = new StringWriter();
        outputWriter.write("<?xml version='1.0' ?>\n");
        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.setWriteEmptyElements(true);
        beanWriter.getXMLIntrospector().register(source);
        beanWriter.setEndOfLine("\n"); //force to ensure matches on expected
        beanWriter.write(partyBean);
        String expectedOut = "<?xml version='1.0' ?>\n" +
                             "  <party id=\"1\">\n" +
                             "    <the-excuse>too late</the-excuse>\n" +
                             "    <location id=\"2\">\n" +
                             "      <street>12312 Here</street>\n" +
                             "      <city>New York</city>\n" +
                             "      <code>92342</code>\n" +
                             "      <country>USA</country>\n" +
                             "    </location>\n" +
                             "    <time>22</time>\n" +
                             "  </party>\n";
        assertEquals(expectedOut, outputWriter.toString());

        BeanReader beanReader = new BeanReader();
        beanReader.registerMultiMapping(
            new InputSource(
                new FileReader(getTestFile("src/test/org/apache/commons/betwixt/mapping.xml"))));
        StringReader xmlReader = new StringReader(outputWriter.toString());
        //Parse the xml
        PartyBean result = (PartyBean)beanReader.parse(xmlReader);
        assertEquals(partyBean.getExcuse(), result.getExcuse());
        assertEquals(partyBean.getFromHour(), result.getFromHour());
        AddressBean addressResult = result.getVenue();
        assertEquals(addressBean.getCity(), addressResult.getCity());
        assertEquals(addressBean.getCode(), addressResult.getCode());
        assertEquals(addressBean.getCountry(), addressResult.getCountry());
        assertEquals(addressBean.getStreet(), addressResult.getStreet());

    }

}

