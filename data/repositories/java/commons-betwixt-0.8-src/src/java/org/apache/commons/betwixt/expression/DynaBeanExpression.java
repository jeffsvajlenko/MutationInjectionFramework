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

package org.apache.commons.betwixt.expression;

import org.apache.commons.beanutils.DynaBean;

/**
 * An Expression that gets a property value from a DynaBean.
 *
 * @see org.apache.commons.beanutils.DynaBean
 *
 * @author Michael Becke
 * @since 0.5
 */
public class DynaBeanExpression implements Expression
{

    /** The name of the DynaBean property to get */
    private String propertyName;

    /**
     * Crates a new DynaBeanExpression.
     */
    public DynaBeanExpression()
    {
        super();
    }

    /**
     * Crates a new DynaBeanExpression.
     *
     * @param propertyName the name of the DynaBean property to use
     */
    public DynaBeanExpression(String propertyName)
    {
        super();
        setPropertyName(propertyName);
    }

    /**
     * Returns the value of a DynaBean property from the bean stored in
     * the Context.  Returns <code>null</code> if no DynaBean is stored
     * in the Context or if the propertyName has not been set.
     *
     * @param context the content containing the DynaBean
     *
     * @return the DynaBean property value or <code>null</code>
     */
    public Object evaluate(Context context)
    {

        if (context.getBean() instanceof DynaBean && propertyName != null)
        {
            return ((DynaBean)context.getBean()).get(propertyName);
        }
        else
        {
            return null;
        }
    }

    /**
     * Do nothing.
     * @see Expression#update
     */
    public void update(Context context, String newValue)
    {
        // do nothing
    }

    /**
     * Gets the name of the property to get from the DynaBean.
     * @return the name of the property that this expression reads
     */
    public String getPropertyName()
    {
        return propertyName;
    }

    /**
     * Sets the name of the property to get from the DynaBean.
     * @param propertyName the property that this expression reads, not null
     */
    public void setPropertyName(String propertyName)
    {
        if (propertyName == null)
        {
            throw new IllegalArgumentException("propertyName is null");
        }
        this.propertyName = propertyName;
    }

}
