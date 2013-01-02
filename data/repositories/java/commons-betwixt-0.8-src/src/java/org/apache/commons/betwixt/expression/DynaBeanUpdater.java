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
import org.apache.commons.beanutils.DynaProperty;

/**
 * Updates <code>DynaBean</code>'s.
 * @since 0.7
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class DynaBeanUpdater extends TypedUpdater
{

    /** The name of the dyna property to be updated */
    private final String propertyName;

    /**
     * Constructs a <code>DynaBeanUpdater</code>
     * for given <code>DynaProperty</code>.
     * @param dynaProperty <code>DyanProperty</code>, not null
     */
    public DynaBeanUpdater(DynaProperty dynaProperty)
    {
        this(dynaProperty.getName(), dynaProperty.getType());
    }

    /**
     * Constructs a <code>DynaBeanUpdater</code>
     * for the given type and property name.
     * @param propertyName name of the dyan property
     * @param type type of the dyna property
     */
    public DynaBeanUpdater(String propertyName, Class type)
    {
        this.propertyName = propertyName;
        setValueType(type);
    }


    /**
     * Executes the update on the given code>DynaBean</code>
     * @see org.apache.commons.betwixt.expression.TypedUpdater#executeUpdate(Context, java.lang.Object, java.lang.Object)
     */
    protected void executeUpdate(Context context, Object bean, Object value) throws Exception
    {
        if (bean instanceof DynaBean)
        {
            DynaBean dynaBean = (DynaBean) bean;
            dynaBean.set(propertyName, value);
        }
        else
        {
            handleException(context, new IllegalArgumentException("DynaBean required."));
        }
    }

    /**
     * Outputs something suitable for logging.
     */
    public String toString()
    {
        return "DynaBeanUpdater [property=" + propertyName + "]";
    }

}
