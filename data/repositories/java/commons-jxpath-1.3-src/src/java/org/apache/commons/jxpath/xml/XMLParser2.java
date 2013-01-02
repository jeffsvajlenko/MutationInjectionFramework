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
package org.apache.commons.jxpath.xml;

import java.io.InputStream;

/**
 * The abstract superclass of XML parsers that produce DOM Documents.
 * The features have the same defaults as {@link javax.xml.parsers.DocumentBuilderFactory}.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public abstract class XMLParser2 implements XMLParser
{
    private boolean validating = false;
    private boolean namespaceAware = true;
    private boolean whitespace = false;
    private boolean expandEntityRef = true;
    private boolean ignoreComments = false;
    private boolean coalescing = false;

    /**
     * Set whether the underlying parser should be validating.
     * @param validating flag
     * @see javax.xml.parsers.DocumentBuilderFactory#setValidating(boolean)
     */
    public void setValidating(boolean validating)
    {
        this.validating = validating;
    }

    /**
     * Learn whether the underlying parser is validating.
     * @return boolean
     * @see javax.xml.parsers.DocumentBuilderFactory#isValidating()
     */
    public boolean isValidating()
    {
        return validating;
    }

    /**
     * Learn whether the underlying parser is ns-aware.
     * @return boolean
     * @see javax.xml.parsers.DocumentBuilderFactory#isNamespaceAware()
     */
    public boolean isNamespaceAware()
    {
        return namespaceAware;
    }

    /**
     * Set whether the underlying parser is ns-aware.
     * @param namespaceAware flag
     * @see javax.xml.parsers.DocumentBuilderFactory#setNamespaceAware(boolean)
     */
    public void setNamespaceAware(boolean namespaceAware)
    {
        this.namespaceAware = namespaceAware;
    }

    /**
     * Set whether the underlying parser is ignoring whitespace.
     * @param whitespace flag
     * @see javax.xml.parsers.DocumentBuilderFactory#setIgnoringElementContentWhitespace(boolean)
     */
    public void setIgnoringElementContentWhitespace(boolean whitespace)
    {
        this.whitespace = whitespace;
    }

    /**
     * Learn whether the underlying parser is ignoring whitespace.
     * @return boolean
     * @see javax.xml.parsers.DocumentBuilderFactory#isIgnoringElementContentWhitespace()
     */
    public boolean isIgnoringElementContentWhitespace()
    {
        return whitespace;
    }

    /**
     * Learn whether the underlying parser expands entity references.
     * @return boolean
     * @see javax.xml.parsers.DocumentBuilderFactory#isExpandEntityReferences()
     */
    public boolean isExpandEntityReferences()
    {
        return expandEntityRef;
    }

    /**
     * Set whether the underlying parser expands entity references.
     * @param expandEntityRef flag
     * @see javax.xml.parsers.DocumentBuilderFactory#setExpandEntityReferences(boolean)
     */
    public void setExpandEntityReferences(boolean expandEntityRef)
    {
        this.expandEntityRef = expandEntityRef;
    }

    /**
     * Learn whether the underlying parser ignores comments.
     * @return boolean
     * @see javax.xml.parsers.DocumentBuilderFactory#isIgnoringComments()
     */
    public boolean isIgnoringComments()
    {
        return ignoreComments;
    }

    /**
     * Set whether the underlying parser ignores comments.
     * @param ignoreComments flag
     * @see javax.xml.parsers.DocumentBuilderFactory#setIgnoringComments(boolean)
     */
    public void setIgnoringComments(boolean ignoreComments)
    {
        this.ignoreComments = ignoreComments;
    }

    /**
     * Learn whether the underlying parser is coalescing.
     * @return boolean
     * @see javax.xml.parsers.DocumentBuilderFactory#isCoalescing()
     */
    public boolean isCoalescing()
    {
        return coalescing;
    }

    /**
     * Set whether the underlying parser is coalescing.
     * @param coalescing flag
     * @see javax.xml.parsers.DocumentBuilderFactory#setCoalescing(boolean)
     */
    public void setCoalescing(boolean coalescing)
    {
        this.coalescing = coalescing;
    }

    public abstract Object parseXML(InputStream stream);
}
