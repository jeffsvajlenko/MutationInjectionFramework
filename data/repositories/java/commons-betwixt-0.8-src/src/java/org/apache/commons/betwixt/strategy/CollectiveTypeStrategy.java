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

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * Specifies which types should be regarded as collective
 * @since 0.8
 */
public abstract class CollectiveTypeStrategy
{

    /**
     * Default collective type strategy
     */
    public static final CollectiveTypeStrategy DEFAULT = new Default();

    /**
     * Is this a loop type class?
     * @since 0.7
     * @param type is this <code>Class</code> a loop type?
     * @return true if the type is a loop type, or if type is null
     */
    public abstract boolean isCollective(Class type);

    /**
     * Default collective type strategy
     */
    public static class Default extends CollectiveTypeStrategy
    {

        /**
         * Basic implementation returns true for all the standard java
         * collective types and their subclasses.
         */
        public boolean isCollective(Class type)
        {
            // consider: should this be factored into a pluggable strategy?
            // check for NPEs
            if (type == null)
            {
                return false;
            }
            return type.isArray()
                   || Map.class.isAssignableFrom( type )
                   || Collection.class.isAssignableFrom( type )
                   || Enumeration.class.isAssignableFrom( type )
                   || Iterator.class.isAssignableFrom( type )
                   || Map.Entry.class.isAssignableFrom( type ) ;

        }

    }
}
