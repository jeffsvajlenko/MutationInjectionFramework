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

import java.util.Map;

import org.apache.commons.betwixt.ElementDescriptor;

/**
 * A plugin Strategy pattern which will detect the plural property which
 * maps to a singular property name.
 * This Strategy is used when composite properties (such as properties
 * of type Collection, List, Iterator, Enumeration) are used as we need to
 * match the adder-method, which typically uses a singular name.
 * This interface allows a variety of different implementations to be used.
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision: 438373 $
 */
public interface PluralStemmer
{

    /**
     * Find the plural descriptor for a singular property.
     *
     * @return the plural descriptor for the given singular property name
     *         or <code>null</code> is no matching descriptor can be found
     * @param propertyName is the singular property name, from the adder method
     * @param map is a map with the keys are the property names of the available
     *  descriptors and the values are the descriptors. This may not be null.
     */
    ElementDescriptor findPluralDescriptor( String propertyName, Map map );

}
