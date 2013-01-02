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
package org.apache.commons.betwixt.registry;

import org.apache.commons.betwixt.io.read.ElementMapping;
import org.apache.commons.betwixt.io.read.ReadContext;

/**
 * <p>Resolves polymorphic references.
 * </p><p>
 * A polymorphic reference is an element whose name and type
 * resolution are postponed till bind-time.
 * When the xml is read, the type can then resolved from
 * by calling {@link #resolveType}.
 * </p>
 * @since 0.7
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 */
public interface PolymorphicReferenceResolver
{

    /**
     * Resolves the bind-time type of a polymorphic element.
     * @param mapping <code>ElementMapping</code> describing the (polymorphic) element being mapped,
     * not null
     * @param context <code>ReadContext</code>, not null
     * @return the <code>Class</code> describing the type to which this element should be bound,
     * or null if the reference cannot be resolved
     */
    public Class resolveType(ElementMapping mapping, ReadContext context);
}
