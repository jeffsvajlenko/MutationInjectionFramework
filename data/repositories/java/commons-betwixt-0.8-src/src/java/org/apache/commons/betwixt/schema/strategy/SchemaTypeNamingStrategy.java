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
package org.apache.commons.betwixt.schema.strategy;

import org.apache.commons.betwixt.ElementDescriptor;

/**
 * Pluggable strategy for naming schema types.
 * Logical interface.
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a> of the <a href='http://www.apache.org'>Apache Software Foundation</a>
 * @since 0.8
 */
abstract public class SchemaTypeNamingStrategy
{

    /**
     * Names the schema type described.
     * @param descriptor <code>ElementDescriptor</code> describing the element
     * @return the name of the schema
     */
    public abstract String nameSchemaType(ElementDescriptor descriptor);
}
