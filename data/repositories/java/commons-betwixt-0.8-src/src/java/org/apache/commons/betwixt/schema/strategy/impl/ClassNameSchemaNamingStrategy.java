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
 * Names schema types from the property type of the descriptor
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a> of the <a href='http://www.apache.org'>Apache Software Foundation</a>
 * @since 0.8
 */
public class ClassNameSchemaNamingStrategy  extends SchemaTypeNamingStrategy
{

    /**
     * Names the schema type from the type of the property.
     * @see SchemaTypeNamingStrategy#nameSchemaType(ElementDescriptor)
     */
    public String nameSchemaType(ElementDescriptor elementDescriptor)
    {
        // TODO: this is probably wrong. needs more thought but this stuff is still experiemental
        String result="xsd:anyType";
        Class type = elementDescriptor.getPropertyType();
        if (type != null)
        {
            String fullName = elementDescriptor.getPropertyType().getName();
            int lastIndexOf = fullName.lastIndexOf('.');
            result = fullName.substring(++lastIndexOf);
        }
        return result;
    }

    /**
     * Outputs brief description.
     */
    public String toString()
    {
        return "Simple Class Name";
    }
}
