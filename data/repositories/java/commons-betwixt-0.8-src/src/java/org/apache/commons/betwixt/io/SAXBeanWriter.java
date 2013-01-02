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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

// FIX ME
// At the moment, namespaces are NOT supported!

/**
 * The SAXBeanwriter will send events to a ContentHandler
 *
 * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 */
public class SAXBeanWriter extends AbstractBeanWriter
{

    /** Where the output goes */
    private ContentHandler contentHandler;
    /** Log used for logging (Doh!) */
    private Log log = LogFactory.getLog( SAXBeanWriter.class );
    /** Should document events (ie. start and end) be called? */
    private boolean callDocumentEvents = true;

    /**
     * <p> Constructor sets writer used for output.</p>
     *
     * @param contentHandler feed events to this content handler
     */
    public SAXBeanWriter(ContentHandler contentHandler)
    {
        this.contentHandler = contentHandler;
    }

    /**
     * Should document events (ie start and end) be called?
     *
     * @return true if this SAXWriter should call start
     * and end of the content handler
     * @since 0.5
     */
    public boolean getCallDocumentEvents()
    {
        return callDocumentEvents;
    }

    /**
     * Sets whether the document events (ie start and end) should be called.
     *
     * @param callDocumentEvents should document events be called
     * @since 0.5
     */
    public void setCallDocumentEvents(boolean callDocumentEvents)
    {
        this.callDocumentEvents = callDocumentEvents;
    }

    /**
     * <p> Set the log implementation used. </p>
     *
     * @return <code>Log</code> implementation that this class logs to
     */
    public Log getLog()
    {
        return log;
    }

    /**
     * <p> Set the log implementation used. </p>
     *
     * @param log <code>Log</code> implementation to use
     */
    public void setLog(Log log)
    {
        this.log = log;
    }


    // Expression methods
    //-------------------------------------------------------------------------

    // Replaced by new API

    // New API
    // -------------------------------------------------------------------------


    /**
     * Writes the start tag for an element.
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qName the element's qualified name
     * @param attributes the element's attributes
     * @throws SAXException if an SAX problem occurs during writing
     * @since 0.5
     */
    protected void startElement(
        WriteContext context,
        String uri,
        String localName,
        String qName,
        Attributes attributes)
    throws
        SAXException
    {
        contentHandler.startElement(
            uri,
            localName,
            qName,
            attributes);
    }

    /**
     * Writes the end tag for an element
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qName the element's qualified name
     * @throws SAXException if an SAX problem occurs during writing
     * @since 0.5
     */
    protected void endElement(
        WriteContext context,
        String uri,
        String localName,
        String qName)
    throws
        SAXException
    {
        contentHandler.endElement(
            uri,
            localName,
            qName);
    }

    /**
     * Express body text
     * @param text the element body text
     * @throws SAXException if the <code>ContentHandler</code> has a problem
     * @since 0.5
     */
    protected void bodyText(WriteContext context, String text) throws SAXException
    {
        //TODO:
        // FIX ME
        // CHECK UNICODE->CHAR CONVERSION!
        // THIS WILL QUITE POSSIBLY BREAK FOR NON-ROMAN
        char[] body = text.toCharArray();
        contentHandler.characters(body, 0, body.length);
    }

    /**
     * This will announce the start of the document
     * to the contenthandler.
     *
     * @see org.apache.commons.betwixt.io.AbstractBeanWriter#end()
     */
    public void start() throws SAXException
    {
        if ( callDocumentEvents )
        {
            contentHandler.startDocument();
        }
    }

    /**
     * This method will announce the end of the document to
     * the contenthandler.
     *
     * @see org.apache.commons.betwixt.io.AbstractBeanWriter#start()
     */
    public void end() throws SAXException
    {
        if ( callDocumentEvents )
        {
            contentHandler.endDocument();
        }
    }
}
