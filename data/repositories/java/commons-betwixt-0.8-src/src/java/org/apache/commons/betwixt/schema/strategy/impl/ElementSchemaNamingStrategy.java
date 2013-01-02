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
package org.apache.commons.betwixt.schema.strategy.impl;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.schema.strategy.SchemaTypeNamingStrategy;

/**
 * Strategy uses the name of the element for the complex type
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a> of the <a href='http://www.apache.org'>Apache Software Foundation</a>
 * @since 0.8
 */
public class ElementSchemaNamingStrategy extends SchemaTypeNamingStrategy
{

    /**
     * Names the schema from the element
     * @see SchemaTypeNamingStrategy#nameSchemaType(ElementDescriptor)
     *
     */
    public String nameSchemaType(ElementDescriptor descriptor)
    {
        return descriptor.getLocalName();
    }

    public String toString()
    {
        return "Element Schema Type Naming Strategy";
    }
}
