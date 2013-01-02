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

/**
 * <p>Pluggable strategy determines whether introspection or bind time
 * typing should be used when finding mappings.
 * The type of a property is determined at introspection time but
 * the actual type of the instance can differ at bind time (when the
 * xml is actually being processed). This strategy is used to set
 * (at a per-element level) whether the bind or introspection
 * time type should be used to calculate the mapping to be used.
 * </p>
 * <p>
 * <strong>Note:</strong> this strategy is intentionally course.
 * Typically, the best approach is to use a custom strategy to set
 * coursely grained rules (for example: all implemetations of 'IAnimal'
 * use bind time type mapping) and then dot betwixt files to provide
 * refinements.
 * </p>
 * @since 0.7
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>,
 * <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public abstract class MappingDerivationStrategy
{

    /**
     * Implementation that always uses bind time type mapping
     */
    public static final MappingDerivationStrategy USE_BIND_TIME_TYPE
        = new MappingDerivationStrategy()
    {
        public boolean useBindTimeTypeForMapping(Class propertyType, Class singluarPropertyType)
        {
            return true;
        }
    };

    /**
     * Implementation that always uses introspection time type mapping
     */
    public static final MappingDerivationStrategy USE_INTROSPECTION_TIME_TYPE
        = new MappingDerivationStrategy()
    {
        public boolean useBindTimeTypeForMapping(Class propertyType, Class singluarPropertyType)
        {
            return false;
        }
    };

    /**
     * The default Betwixt strategy.
     */
    public static final MappingDerivationStrategy DEFAULT = USE_BIND_TIME_TYPE;

    /**
     * Should bind time type be used for all elements of the given property type?
     * @param propertyType <code>Class</code> typing the property, not null
     * @param singluarPropertyType <code>Class</code> composing the collective
     * or null if the property is not collective
     * @return true if bind time type should be used for the mapping
     */
    public abstract boolean useBindTimeTypeForMapping(Class propertyType, Class singluarPropertyType);
}
