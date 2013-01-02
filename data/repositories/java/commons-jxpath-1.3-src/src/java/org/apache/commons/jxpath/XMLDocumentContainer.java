/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath;

import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;

import org.apache.commons.jxpath.xml.DocumentContainer;
import org.w3c.dom.Document;

/**
 * An XML document container reads and parses XML only when it is
 * accessed.  JXPath traverses Containers transparently -
 * you use the same paths to access objects in containers as you
 * do to access those objects directly.  You can create
 * XMLDocumentContainers for various XML documents that may or
 * may not be accessed by XPaths.  If they are, they will be automatically
 * read, parsed and traversed. If they are not - they won't be
 * read at all.
 *
 * @deprecated 1.1 Please use {@link DocumentContainer}
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class XMLDocumentContainer implements Container
{

    private DocumentContainer delegate;
    private Object document;
    private URL xmlURL;
    private Source source;

    /**
     * Create a new XMLDocumentContainer.
     * @param xmlURL a URL for an XML file. Use getClass().getResource(resourceName)
     *               to load XML from a resource file.
     */
    public XMLDocumentContainer(URL xmlURL)
    {
        this.xmlURL = xmlURL;
        delegate = new DocumentContainer(xmlURL);
    }

    /**
     * Create a new XMLDocumentContainer.
     * @param source XML source
     */
    public XMLDocumentContainer(Source source)
    {
        this.source = source;
        if (source == null)
        {
            throw new RuntimeException("Source is null");
        }
    }

    /**
     * Reads XML, caches it internally and returns the Document.
     * @return Object value
     */
    public Object getValue()
    {
        if (document == null)
        {
            try
            {
                if (source != null)
                {
                    DOMResult result = new DOMResult();
                    Transformer trans =
                        TransformerFactory.newInstance().newTransformer();
                    trans.transform(source, result);
                    document = (Document) result.getNode();
                }
                else
                {
                    document = delegate.getValue();
                }
            }
            catch (Exception ex)
            {
                throw new JXPathException(
                    "Cannot read XML from: "
                    + (xmlURL != null
                       ? xmlURL.toString()
                       : (source != null
                          ? source.getSystemId()
                          : "<<undefined source>>")),
                    ex);
            }
        }
        return document;
    }

    /**
     * Throws an UnsupportedOperationException
     * @param value to set
     */
    public void setValue(Object value)
    {
        throw new UnsupportedOperationException();
    }
}
