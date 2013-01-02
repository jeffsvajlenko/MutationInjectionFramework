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

import org.apache.commons.betwixt.expression.Context;

/**
 * Pluggable strategy for id storage management.
 * It is possible to use this strategy for innovative
 * active storage storage strategies as well as passive ones.
 * For example, it is possible to have some beans map to
 * references without ever being fully mapped.
 *
 * @author <a href="mailto:christian@wilde-welt.de">Christian Aust </a>
 * @since 0.7
 */
public abstract class IdStoringStrategy
{

    /**
     * Default storage strategy
     *
     * @deprecated do not use this singleton since it
     * creates a static Map of all objects ever written.
     * Use {@link #createDefault} instead
     */
    public static IdStoringStrategy DEFAULT = new DefaultIdStoringStrategy();

    /**
     * Factory method creates the default <code>Betwixt</code> implementation.
     * The implementation created may vary if the default implementation changes.
     * @return <code>IdStoringStrategy</code> used as default
     * @since 0.8
     */
    public static IdStoringStrategy createDefault()
    {
        return new DefaultIdStoringStrategy();
    }


    /**
     * Retrieves a reference for the given instance.
     * If a not null value is returned from this method,
     * then the bean content will not be written.
     * Use {@link org.apache.commons.betwixt.io.IDGenerator} strategy to vary the values
     * written for a bean.
     *
     * @param context
     *            current context, not null
     * @param bean
     *            the instance, not null
     * @return id as String when this bean has already been reference,
     * or null to indicate that this bean is not yet reference
     */
    public abstract String getReferenceFor(Context context, Object bean);

    /**
     * Stores an instance reference for later retrieval.
     * This method is shared by writing and reading.
     *
     * @param context
     *            current context, not null
     * @param bean
     *            the instance, not null
     * @param id
     *            the id to use
     */
    public abstract void setReference(Context context, Object bean, String id);

    /**
     * Gets an object matching the given reference.
     * @param context <code>Context</code>, not null
     * @param id the reference id
     * @return an bean matching the given reference,
     * or null if there is no bean matching the given reference
     */
    public abstract Object getReferenced(Context context, String id);

    /**
     * Reset to the initial state.
     *
     */
    public abstract void reset();

}
