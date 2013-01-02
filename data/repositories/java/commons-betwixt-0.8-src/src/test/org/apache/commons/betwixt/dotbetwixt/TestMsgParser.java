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

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
/**
 * Tests the marshalling and unmarshalling of MsgBeans with Betwixt.
 * The problem tested here is that an element without an updater would
 * not process it's attributes correctly even though they had updaters.
 *
 * @author <a href="mstanley@cauldronsolutions.com">Mike Stanley</a>
 * @version $Id: TestMsgParser.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestMsgParser extends TestCase
{
    private static final String XML_PROLOG = "<?xml version='1.0' ?>\n";
    private MsgBean msg;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        msg = new MsgBean();
        msg.setDescription("Some simple descriptive text");
        msg.setToAddress("mike@somewhere.com");
        msg.setFromAddress("debbie@somwhere.com");
        msg.setName("basicMsg");
        msg.setOptionalField1("7-12-99");
        msg.setOptionalField2("true");
        msg.setStatus("sent");
        msg.setType("spam");
    }

    public void testGetAsXml() throws Exception
    {
        String xmlMsg = null;
        xmlMsg = getAsXml(msg);
        assertNotNull("XML String should not be null", xmlMsg);

    }

    public void testParseMsg() throws Exception
    {
        MsgBean newMsg = null;
        // install request marshall/unmarshall
        String xmlMsg = getAsXml(msg);
        newMsg = parseMsg(xmlMsg);

        assertNotNull("new MsgBean should not be null.", newMsg);
        assertEquals( msg.getDescription(), newMsg.getDescription() );
        assertEquals( msg.getFromAddress(), newMsg.getFromAddress() );
        assertEquals( msg.getName(), newMsg.getName() );
        assertEquals( msg.getOptionalField1(), newMsg.getOptionalField1() );
        assertEquals( msg.getOptionalField2(), newMsg.getOptionalField2() );
        assertEquals( msg.getStatus(), newMsg.getStatus() );
        assertEquals( msg.getToAddress(), newMsg.getToAddress() );
        assertEquals( msg.getType(), newMsg.getType() );
    }

    /**
     * Returns the bean as an xml string.
     *
     * @param msg
     * @return
     * @throws Exception
     */
    public static final String getAsXml(MsgBean msg)
    throws Exception
    {
        StringWriter writer = new StringWriter();

        // Betwixt just writes out the bean as a fragment
        // we want well-formed xml, we need to add the prolog
        writer.write(XML_PROLOG);

        // Create a BeanWriter which writes to our prepared stream
        BeanWriter beanWriter = new BeanWriter(writer);

        // Configure betwixt
        // For more details see java docs or later in the main documentation
        beanWriter.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        beanWriter.getBindingConfiguration().setMapIDs(false);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();

        // Write example bean as base element 'person'
        beanWriter.write("message", msg);
        beanWriter.flush();

        return writer.toString();
    }

    /**
     * Parses the passed in message xml
     *
     * @param xmlMessage
     * @return
     * @throws Exception
     */
    public static final MsgBean parseMsg(String xmlMessage)
    throws Exception
    {
        MsgBean msg = null;
        BeanReader beanReader = new BeanReader();
        // Configure the reader
        beanReader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        // Register beans so that betwixt knows what the xml is
        beanReader.registerBeanClass("message", MsgBean.class);
        StringReader stringReader = new StringReader(xmlMessage);
        return  (MsgBean) beanReader.parse(stringReader);
    }



}
