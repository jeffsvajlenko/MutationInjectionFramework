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

import java.io.IOException;
import java.io.Writer;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Simple SAXContentHandler to test the SAXBeanWriter
 *
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Id: SAXContentHandler.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class SAXContentHandler extends DefaultHandler
{

    private Writer out;
    /**
     * Constructor for SAXContentHandler.
     */
    public SAXContentHandler(Writer out)
    {
        this.out = out;
    }

    /**
     * @see org.xml.sax.ContentHandler#characters(char[], int, int)
     */
    public void characters(char[] ch, int start, int length)
    throws SAXException
    {
        try
        {
            out.write("  "+new String(ch, start, length)+"\n");
        }
        catch(IOException ioe)
        {
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#endElement(String, String, String)
     */
    public void endElement(String namespaceURI, String localName, String qName)
    throws SAXException
    {
        try
        {
            out.write("</"+qName+">\n");
        }
        catch (IOException e)
        {
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startDocument()
     */
    public void startDocument() throws SAXException
    {
        try
        {
            out.write("<?xml version=\"1.0\"?>\n");
        }
        catch (IOException e)
        {
        }
    }

    /**
     * @see org.xml.sax.ContentHandler#startElement(String, String, String, Attributes)
     */
    public void startElement(
        String namespaceURI,
        String localName,
        String qName,
        Attributes atts)
    throws SAXException
    {
        try
        {
            StringBuffer sb = new StringBuffer();
            sb.append("<"+qName);
            for (int i=0; i < atts.getLength(); i++)
            {
                sb.append(" "+atts.getQName(i));
                sb.append("=\"");
                sb.append(atts.getValue(i));
                sb.append("\"");
            }
            sb.append(">\n");
            out.write(sb.toString());
        }
        catch (IOException e)
        {
        }
    }

}
