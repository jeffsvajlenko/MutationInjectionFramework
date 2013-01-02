package org.apache.commons.betwixt.registry;

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

import org.apache.commons.betwixt.XMLBeanInfo;

/** <p>Plug in registry for <code>XMLBeanInfo</code>'s.</p>
  *
  * <p>This decouples the implementation of the <code>XMLBeanInfo</code> cache.
  * Users can plug in the standard implementations found
  * in this package to switch caching on and off.
  * Alternatively, they can plug-in an exotic cache of their own devising.</p>
  *
  * <p>Users can also prime a cache with <code>XMLBeanInfo</code>
  * classes created programmatically.</p>
  *
  * <p>To find a <code>XMLBeanInfo</code> for a class,
  * <code>XMLIntrospector</code> checks in the registry first
  * before starting introspection.
  * If the registry returns an <code>XMLBeanInfo</code>, then that's used.
  * Otherwise, the <code>XMLBeanInfo</code> will be found by standard introspection
  * and then {@link #put} will be called so that the registry
  * can cache - if it wished - the <code>XMLBeanInfo</code>.
  * </p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Id: XMLBeanInfoRegistry.java 438373 2006-08-30 05:17:21Z bayard $
  */
public interface XMLBeanInfoRegistry
{

    /**
      * Get the <code>XMLBeanInfo</code> for the given class.
      *
      * @param forThisClass get <code>XMLBeanInfo</code> for this class
      *
      * @return <code>null</code> if fresh introspection should be used
      * to find the <code>XMLBeanInfo</code>
      */
    public XMLBeanInfo get(Class forThisClass);

    /**
      * Associate a class with it's <code>XMLBeanInfo</code>.
      *
      * @param forThisClass the class to associate with the given bean info
      * @param beanInfo the <code>XMLBeanInfo</code> to use for the given class
      */
    public void put(Class forThisClass, XMLBeanInfo beanInfo);

    /**
     * Flush or resets the current registry to it's original state.
     * It has to be implemented, however could be an empty implementation
     */
    public void flush();
}
