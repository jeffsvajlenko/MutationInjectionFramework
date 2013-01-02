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

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;

/**
 * Determines whether the expression of an attribute with a values
 * should be suppressed.
 *
 * @since 0.7
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public abstract class ValueSuppressionStrategy
{

    /**
     * Strategy allows all values to be expressed for all attributes
     */
    public static final ValueSuppressionStrategy ALLOW_ALL_VALUES = new ValueSuppressionStrategy()
    {
        public boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value)
        {
            return false;
        }
    };

    /**
     * Suppresses all null values.
     */
    public static final ValueSuppressionStrategy SUPPRESS_EMPTY = new ValueSuppressionStrategy()
    {
        public boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value)
        {
            return "".equals(value);
        }
    };

    /**
     * Default strategy is {@link #SUPPRESS_EMPTY}.
     */
    public static final ValueSuppressionStrategy DEFAULT = SUPPRESS_EMPTY;


    /**
     * Should the given attribute value be suppressed?
     * @param attributeDescriptor <code>AttributeDescriptor</code> describing the attribute, not null
     * @param value <code>Object</code> value, possibly null
     * @return true if the attribute should not be written for the given value
     */
    public abstract boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value);

    /**
     * <p>
     * Should be given element value be suppressed?
     * </p><p>
     * <strong>Note:</strong> to preserve binary compatibility,
     * this method contains an implementation that returns false.
     * Subclasses should not rely upon this behaviour as (in future)
     * this may be made abstract.
     * </p>
     * @param element <code>ElementDescriptor</code> describing the element, not null
     * @param namespaceUri the namespace of the element to be written
     * @param localName the local name of the element to be written
     * @param qualifiedName the qualified name of the element to be written
     * @param value <code>Object</code> value, possibly null
     * @return true if the element should be suppressed (in other words, not written)
     * for the given value
     * @since 0.8
     */
    public boolean suppressElement(ElementDescriptor element, String namespaceUri, String localName, String qualifiedName, Object value)
    {
        return false;
    }
}
