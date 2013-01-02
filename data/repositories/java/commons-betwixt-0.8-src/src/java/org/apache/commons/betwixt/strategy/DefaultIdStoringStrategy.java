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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.betwixt.expression.Context;

/**
 * <p>Stores every ID that given to it into an internal <code>HashMap</code> and
 * returns it on request.
 * </p><p>
 * {@link #DefaultIdStoringStrategy(Map, Map)} allows the implementations
 * to be specified.
 * For example, those who want to use identity (rather than equality)
 * should pass a <code>IdentityHashMap</code> instance.
 * </p>
 *
 * @author <a href="mailto:christian@wilde-welt.de">Christian Aust</a>
 * @since 0.7
 */
public class DefaultIdStoringStrategy extends IdStoringStrategy
{
    private Map idByBeanMap;
    private Map beanByIdMap;

    /**
     * Constructs a {@link IdStoringStrategy} using a <code>HashMap</code> for
     * storage.
     */
    public DefaultIdStoringStrategy()
    {
        this(new HashMap(), new HashMap());
    }

    /**
     * Constructs a {@link IdStoringStrategy}using the <code>Map</code>
     * implementations provided for storage.
     *
     * @param idByBeanMap <code>Map</code> implementation stores the ID's by bean
     * @param beanByIdMap <code>Map</code> implementation stores the bean's by ID
     * @since 0.8
     */
    public DefaultIdStoringStrategy(Map idByBeanMap, Map beanByIdMap)
    {
        this.idByBeanMap = idByBeanMap;
        this.beanByIdMap = beanByIdMap;
    }


    /**
     * Returns a String id for the given bean if it has been stored previously.
     * Otherwise returns null.
     *
     * @param context
     *            current context, not null
     * @param bean
     *            the instance, not null
     * @return id as String, or null if not found
     * @see org.apache.commons.betwixt.strategy.IdStoringStrategy#getReferenceFor(org.apache.commons.betwixt.expression.Context,
     *      java.lang.Object)
     */
    public String getReferenceFor(Context context, Object bean)
    {
        return (String) idByBeanMap.get(bean);
    }

    /**
     * Stores an ID for the given instance and context. It will check first if
     * this ID has been previously stored and will do nothing in that case.
     *
     * @param context
     *            current context, not null
     * @param bean
     *            current instance, not null
     * @param id
     *            the ID to store
     * @see org.apache.commons.betwixt.strategy.IdStoringStrategy#setReference(org.apache.commons.betwixt.expression.Context,
     *      java.lang.Object, java.lang.String)
     */
    public void setReference(Context context, Object bean, String id)
    {
        if (!idByBeanMap.containsKey(bean))
        {
            idByBeanMap.put(bean, id);
            beanByIdMap.put(id, bean);
        }
    }

    /**
     * Gets an object matching the given reference.
     * @param context <code>Context</code>, not null
     * @param id the reference id
     * @return an bean matching the given reference,
     * or null if there is no bean matching the given reference
     */
    public Object getReferenced(Context context, String id)
    {
        return beanByIdMap.get(id);
    }

    /**
     * Clears all beans.
     */
    public void reset()
    {
        idByBeanMap.clear();
        beanByIdMap.clear();
    }


}
