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

import java.lang.reflect.Array;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Abstracts common features for strongly typed <code>Updater</code>'s.
 * Strongly type <code>Updater</code>'s perform conversions based on this
 * the expected type before the bean update is invoked.
 * @since 0.7
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public abstract class TypedUpdater implements Updater
{

    /** Logger */
    private static final Log log = LogFactory.getLog( TypedUpdater.class );


    /** The type of the first parameter of the method */
    private Class valueType;

    /**
     * Updates the current bean context with the given String value
     * @param context the Context to be updated
     * @param newValue the update to this new value
     */
    public void update(Context context, Object newValue)
    {
        Object bean = context.getBean();
        if ( bean != null )
        {
            if ( newValue instanceof String )
            {
                // try to convert into primitive types
                if ( log.isTraceEnabled() )
                {
                    log.trace("Converting primitive to " + valueType);
                }
                newValue = context.getObjectStringConverter()
                           .stringToObject( (String) newValue, valueType, context );
            }
            if ( newValue != null )
            {
                // check that it is of the correct type
                /*
                                if ( ! valueType.isAssignableFrom( newValue.getClass() ) ) {
                                    log.warn(
                                        "Cannot call setter method: " + method.getName() + " on bean: " + bean
                                        + " with type: " + bean.getClass().getName()
                                        + " as parameter should be of type: " + valueType.getName()
                                        + " but is: " + newValue.getClass().getName()
                                    );
                                    return;
                                }
                */
            }
            // special case for collection objects into arrays
            if (newValue instanceof Collection && valueType.isArray())
            {
                Collection valuesAsCollection = (Collection) newValue;
                Class componentType = valueType.getComponentType();
                if (componentType != null)
                {
                    Object[] valuesAsArray =
                        (Object[]) Array.newInstance(componentType, valuesAsCollection.size());
                    newValue = valuesAsCollection.toArray(valuesAsArray);
                }
            }

            ;
            try
            {
                executeUpdate( context, bean, newValue );

            }
            catch (Exception e)
            {
                String valueTypeName = (newValue != null) ? newValue.getClass().getName() : "null";
                log.warn(
                    "Cannot evaluate: " + this.toString() + " on bean: " + bean
                    + " of type: " + bean.getClass().getName() + " with value: " + newValue
                    + " of type: " + valueTypeName
                );
                handleException(context, e);
            }
        }
    }



    /**
     * Gets the type expected.
     * The value passed into {@link #update}
     * will be converted on the basis of this type
     * before being passed to {@link #executeUpdate}.
     * @return <code>Class</code> giving expected type, not null
     */
    public Class getValueType()
    {
        return valueType;
    }

    /**
     * Sets the type expected.
     * The value passed into {@link #update}
     * will be converted on the basis of this type
     * before being passed to {@link #executeUpdate}.
     * @param valueType <code>Class</code> giving expected type, not null
     */
    public void setValueType(Class valueType)
    {
        this.valueType = valueType;
    }

    /**
     * Updates the bean with the given value.
     * @param bean
     * @param value value after type conversion
     */
    protected abstract void executeUpdate(Context context, Object bean, Object value) throws Exception;

    /**
     * Strategy method to allow derivations to handle exceptions differently.
     * @param context the Context being updated when this exception occured
     * @param e the Exception that occured during the update
     */
    protected void handleException(Context context, Exception e)
    {
        log.info( "Caught exception: " + e, e );
    }

}
