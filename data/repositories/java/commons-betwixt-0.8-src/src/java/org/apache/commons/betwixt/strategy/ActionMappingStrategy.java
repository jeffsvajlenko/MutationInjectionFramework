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

import org.apache.commons.betwixt.io.read.MappingAction;
import org.apache.commons.betwixt.io.read.ReadContext;
import org.xml.sax.Attributes;

/**
 * <p>
 * Pluggable strategy interface used for free mappings.
 * </p>
 * <p>
 * Free mappings (ones where the current mapping )
 * are executed by calling a <code>ActionMappingStrategy</code>
 * implementation.
 * So, using a custom strategy is an easy way to
 * customize the mapping.
 * </p>
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public abstract class ActionMappingStrategy
{

    /**
     * Default <code>ActionMappingStrategy</code>
     * used by betwixt
     */
    public static final ActionMappingStrategy DEFAULT
        = new DefaultActionMappingStrategy();

    /**
     * Gets the mapping action to map the given element.
     * @param namespace not null
     * @param name not null
     * @param attributes <code>Attributes</code>, not null
     * @param context <code>ReadContext</code>, not null
     * @return <code>MappingAction</code>, not null
     * @throws Exception
     */
    public abstract MappingAction getMappingAction(
        String namespace,
        String name,
        Attributes attributes,
        ReadContext context)
    throws Exception;
}
