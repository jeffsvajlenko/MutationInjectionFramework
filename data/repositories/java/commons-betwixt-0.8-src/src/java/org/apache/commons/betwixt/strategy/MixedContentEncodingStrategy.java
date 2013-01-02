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

/**
 * <p>Encodes body content.
 * </p><p>
 * <strong>Usage:</strong>
 * Used by {@link org.apache.commons.betwixt.io.BeanWriter} to encode body content before it is written
 * into the textual output.
 * This gives flexibility in this stage allowing (for example)
 * some properties to use character escaping whilst others
 * use <code>CDATA</code> wrapping.
 * </p>
 * <p><strong>Note:</strong> the word <code>encoding</code> here is used
 * in the sense of escaping a sequence of character data.
 * </p>
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @since 0.5
 */
public abstract class MixedContentEncodingStrategy
{

    /**
     * The name of the option used to specify encoding on a per-element
     * basis is
     * <code>org.apache.commons.betwixt.mixed-content-encoding</code>
     */
    public static final String ENCODING_OPTION_NAME
        = "org.apache.commons.betwixt.mixed-content-encoding";
    /** The option value for CDATA */
    public static final String CDATA_ENCODING = "CDATA";

    /**
     * The standard implementation used by Betwixt by default.
     * The default is to escape as character data unless
     * the <code>ElementDescriptor</code> contains
     * an option with name
     * <code>org.apache.commons.betwixt.mixed-content-encoding</code>
     * and value <code>CDATA</code>.
     * This is a singleton.
     */
    public static final MixedContentEncodingStrategy DEFAULT
        = new BaseMixedContentEncodingStrategy()
    {
        /**
         * Encode by escaping character data unless
         * the <code>ElementDescriptor</code> contains
         * an option with name
         * <code>org.apache.commons.betwixt.mixed-content-encoding</code>
         * and value <code>CDATA</code>.
         */
        protected boolean encodeAsCDATA(ElementDescriptor element)
        {
            boolean result = false;
            if (element != null )
            {
                String optionValue = element.getOptions().getValue(ENCODING_OPTION_NAME);
                result = CDATA_ENCODING.equals(optionValue);
            }
            return result;
        }
    };

    /**
     * Encodes element content within a <code>CDATA</code> section.
     * This is a singleton.
     */
    public static final MixedContentEncodingStrategy CDATA
        = new BaseMixedContentEncodingStrategy()
    {
        /**
         * Always encode by escaping character data.
         */
        protected boolean encodeAsCDATA(ElementDescriptor element)
        {
            return true;
        }
    };

    /**
      * Encodes by escaping character data.
      * This is a singleton.
      */
    public static final MixedContentEncodingStrategy ESCAPED_CHARACTERS
        = new BaseMixedContentEncodingStrategy()
    {
        /**
         * Always encode by escaping character data.
         */
        protected boolean encodeAsCDATA(ElementDescriptor element)
        {
            return false;
        }
    };


    /**
     * Encodes the body content into a form suitable for output as
     * (textual) xml.
     * @param bodyContent the raw (unescaped) character data, not null
     * @param element the <code>ElementDescriptor</code> describing the element
     * whose content is being encoded.
     * @return the encoded (escaped) character data, not null
     */
    public abstract String encode(String bodyContent, ElementDescriptor element);
}
