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

/**
 * Strategy to determine whether to show an attribute at all.
 * @since 0.8
 */
public interface AttributeSuppressionStrategy
{
    /**
     * Should the attribute described as given be suppressed during introspection?
     * @param descriptor <code>AttributeDescriptor</code>, not null
     * @return true if the attribute should be ignore,
     * false otherwise
     */
    public boolean suppress(AttributeDescriptor descriptor);

    /**
     * Default strategy: show all attributes.
     */
    public final static AttributeSuppressionStrategy DEFAULT = new AttributeSuppressionStrategy()
    {
        public boolean suppress(AttributeDescriptor description)
        {
            return false;
        }

    };
}
