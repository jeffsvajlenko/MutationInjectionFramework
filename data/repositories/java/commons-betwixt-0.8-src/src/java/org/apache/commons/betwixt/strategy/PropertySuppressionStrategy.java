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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Pluggable strategy specifying whether property's should be suppressed.
 * Implementations can be used to give rules about which properties
 * should be ignored by Betwixt when introspecting.
 * @since 0.7
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public abstract class PropertySuppressionStrategy
{

    /**
     * Default implementation.
     * @see #DEFAULT
     */
    public static class Default extends PropertySuppressionStrategy
    {
        public boolean suppressProperty(Class clazz, Class propertyType, String propertyName)
        {
            boolean result = false;
            // ignore class properties
            if ( Class.class.equals( propertyType) && "class".equals( propertyName ) )
            {
                result = true;
            }
            // ignore isEmpty for collection subclasses
            if ( "empty".equals( propertyName ) && Collection.class.isAssignableFrom( clazz ))
            {
                result = true;
            }

            return result;
        }

        public String toString()
        {
            return "Default Properties Suppressed";
        }
    }

    /**
     * Implementation delegates to a list of strategies
     */
    public static class Chain extends PropertySuppressionStrategy
    {

        private final List strategies = new ArrayList();

        /**
         * @see #suppressProperty(Class, Class, String)
         */
        public boolean suppressProperty(Class classContainingTheProperty, Class propertyType, String propertyName)
        {
            boolean result = false;
            for (Iterator it=strategies.iterator(); it.hasNext();)
            {
                PropertySuppressionStrategy strategy = (PropertySuppressionStrategy) it.next();
                if (strategy.suppressProperty(classContainingTheProperty, propertyType, propertyName))
                {
                    result = true;
                    break;
                }

            }
            return result;
        }

        /**
         * Adds a strategy to the list
         * @param strategy <code>PropertySuppressionStrategy</code>, not null
         */
        public void addStrategy(PropertySuppressionStrategy strategy)
        {
            strategies.add(strategy);
        }
    }

    /**
     * Default implementation suppresses the class property
     * found on every object. Also, the <code>isEmpty</code>
     * property is supressed for implementations of <code>Collection</code>.
     */
    public static final PropertySuppressionStrategy DEFAULT = new Default();

    /**
     * Should the given property be suppressed?
     * @param classContainingTheProperty <code>Class</code> giving the type of the bean containing the property <code>propertyName</code>
     * @param propertyType <code>Class</code> giving the type of the property, not null
     * @param propertyName the name of the property, not null
     * @return true when the given property should be suppressed
     */
    public abstract boolean suppressProperty(Class classContainingTheProperty, Class propertyType, String propertyName);
}
