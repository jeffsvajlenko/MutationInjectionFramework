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

package org.apache.commons.betwixt.strategy;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLUtils;

/**
 * <p>Basic implementation for {@link MixedContentEncodingStrategy}
 * supports variations of most common use case.
 * </p>
 * <p>This supports subclasses that choose to encode body content
 * either as a <code>CDATA</code> section or by escaping the characters.
 * Implementations should override {@link #encodeAsCDATA}
 * with an appropriate decision algorithm.
 * </p>
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @since 0.5
 */
public abstract class BaseMixedContentEncodingStrategy
    extends MixedContentEncodingStrategy
{

    /**
     * Escapes a sequence of body content.
     * @param bodyContent the content whose character data should be escaped,
     * not null
     * @return the escaped character data, not null
     */
    protected String escapeCharacters(String bodyContent)
    {
        return XMLUtils.escapeBodyValue(bodyContent);
    }

    /**
     * Wraps the given content into a CDATA section.
     * @param bodyContent the content to be encoded into a CDATA
     * section
     * @return the content wrapped inside a CDATA section, not null
     */
    protected String encodeInCDATA(String bodyContent)
    {
        StringBuffer buffer = new StringBuffer(bodyContent);
        buffer.ensureCapacity(12);
        XMLUtils.escapeCDATAContent(buffer);
        return buffer.insert(0, "<![CDATA[").append("]]>").toString();
    }

    /**
     * Encodes the given body content by either escaping the character data
     * or by encoding within a <code>CDATA</code> section.
     * The algorithm used to decide whether a particular element's mixed
     * should be escaped is delegated to the concrete subclass through
     * {@link #encodeAsCDATA}
     * @see org.apache.commons.betwixt.strategy.MixedContentEncodingStrategy#encode(java.lang.String, org.apache.commons.betwixt.ElementDescriptor)
     */
    public String encode(String bodyContent, ElementDescriptor element)
    {
        if (encodeAsCDATA(element))
        {
            return encodeInCDATA(bodyContent);
        }

        return escapeCharacters(bodyContent);
    }

    /**
     * <p>Should the element described by the given
     * <code>ElementDescriptor</code> be encoded as a <code>CDATA</code>
     * section?
     * </p>
     * <p><strong>Usage:</strong> subclasses should provide a strategy
     * to determine whether an element should be encoded using a
     * <code>CDATA</code> section.
     * </p>
     *
     * @param element <code>ElementDescriptor</code>, not null
     * @return true if the element should be encoded
     * as a <code>CDATA</code> section
     */
    protected abstract boolean encodeAsCDATA(ElementDescriptor element);

}
