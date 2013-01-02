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
 * <p>Class normalization strategy.</p>
 *
 * <p>
 * The normalized Class is the Class that Betwixt should
 * introspect.
 * This strategy class allows the introspected Class to be
 * varied.
 * This implementation simply returns the Class given.
 * </p>
 *
 * <p>
 * Used by Betwixt to allow superclasses or interfaces to be subsittuted
 * before an object is introspected.
 * This allows users to feed in logical interfaces and make Betwixt ignore
 * properties other than those in the interface.
 * It also allows support for <code>Proxy</code>'s.
 * Together, these features allow Betwixt to deal with Entity Beans
 * properly by viewing them through their remote interfaces.
 * </p>
 * @author Robert Burrell Donkin
 * @since 0.5
 */
public class ClassNormalizer
{

    /**
      * Gets the normalized class for the given Object.
      * The normalized Class is the Class that Betwixt should
      * introspect.
      * This strategy class allows the introspected Class to be
      * varied.
      *
      * @param object the <code>Object</code>
      * for which the normalized Class is to be returned.
      * @return the normalized Class
      */
    public Class getNormalizedClass( Object object )
    {
        if ( object == null )
        {
            throw new IllegalArgumentException("Cannot get class for null object.");
        }
        return normalize( object.getClass() );
    }

    /**
      * Normalize given class.
      * The normalized Class is the Class that Betwixt should
      * introspect.
      * This strategy class allows the introspected Class to be
      * varied.
      *
      * @param clazz the class to normalize, not null
      * @return this implementation the same clazz,
      * subclasses may return any compatible class.
      */
    public Class normalize( Class clazz )
    {
        return clazz;
    }
}
