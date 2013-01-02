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
import java.lang.reflect.Method;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** <p><code>MapEntryAdder</code> is used to add entries to a map.</p>
  *
  * <p>
  * <code>MapEntryAdder</code> supplies two updaters:
  * <ul>
  *   <li>{@link #getKeyUpdater()} which allows the entry key to be updated</li>
  *   <li>{@link #getValueUpdater()} which allows the entry value to be updated</li>
  * </ul>
  * When both of these updaters have been called, the entry adder method is called.
  * Once this has happened then the values can be updated again.
  * Note that only the <code>Context</code> passed by the last update will be used.
  * </p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @since 0.5
  */
public class MapEntryAdder
{


    // Class Attributes
    //-------------------------------------------------------------------------

    /** Log used by this class */
    private static Log log = LogFactory.getLog( MapEntryAdder.class );


    // Class Methods
    //-------------------------------------------------------------------------

    /**
     * Sets the logger used by this class.
     *
     * @param newLog log to this
     */
    public static void setLog(Log newLog)
    {
        log = newLog;
    }

    // Attributes
    //-------------------------------------------------------------------------

    /** The method to be called to add a new map entry */
    private Method adderMethod;

    /** Has the entry key been updated? */
    private boolean keyUpdated = false;
    /** The entry key */
    private Object key;

    /** Has the entry value been updated? */
    private boolean valueUpdated = false;
    /** The entry value */
    private Object value;


    // Constructors
    //-------------------------------------------------------------------------

    /**
     * Construct a <code>MapEntryAdder</code> which adds entries to given method.
     *
     * @param method the <code>Method</code> called to add a key-value entry
     * @throws IllegalArgumentException if the given method does not take two parameters
     */
    public MapEntryAdder(Method method)
    {

        Class[] types = method.getParameterTypes();
        if ( types == null || types.length != 2)
        {
            throw new IllegalArgumentException(
                "Method used to add entries to maps must have two parameter.");
        }
        this.adderMethod = method;
    }

    // Properties
    //-------------------------------------------------------------------------

    /**
     * Gets the entry key <code>Updater</code>.
     * This is used to update the entry key value to the read value.
     * If {@link #getValueUpdater} has been called previously,
     * then this trigger the updating of the adder method.
     *
     * @return the <code>Updater</code> which should be used to populate the entry key
     */
    public Updater getKeyUpdater()
    {

        return new Updater()
        {
            public void update( Context context, Object keyValue )
            {
                // might as well make sure that his can only be set once
                if ( !keyUpdated )
                {
                    keyUpdated = true;
                    key = keyValue;
                    if ( log.isTraceEnabled() )
                    {
                        log.trace( "Setting entry key to " + key );
                        log.trace( "Current entry value is " + value );
                    }
                    if ( valueUpdated )
                    {
                        callAdderMethod( context );
                    }
                }
            }
        };
    }

    /**
     * Gets the entry value <code>Updater</code>.
     * This is used to update the entry key value to the read value.
     * If {@link #getKeyUpdater} has been called previously,
     * then this trigger the updating of the adder method.
     *
     * @return the <code>Updater</code> which should be used to populate the entry value
     */
    public Updater getValueUpdater()
    {

        return new Updater()
        {
            public void update( Context context, Object valueValue )
            {
                // might as well make sure that his can only be set once
                if ( !valueUpdated )
                {
                    valueUpdated = true;
                    value = valueValue;
                    if ( log.isTraceEnabled() )
                    {
                        log.trace( "Setting entry value to " + value);
                        log.trace( "Current entry key is " + key );
                    }
                    if ( keyUpdated )
                    {
                        callAdderMethod( context );
                    }
                }
            }
        };
    }



    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Call the adder method on the bean associated with the <code>Context</code>
     * with the key, value entry values stored previously.
     *
     * @param context the Context against whose bean the adder method will be invoked
     */
    private void callAdderMethod(Context context)
    {
        log.trace("Calling adder method");

        // this allows the same instance to be used multiple times.
        keyUpdated = false;
        valueUpdated = false;

        //
        // XXX This is (basically) cut and pasted from the MethodUpdater code
        // I haven't abstracted this code just yet since I think that adding
        // handling for non-beans will mean adding quite a lot more structure
        // and only once this is added will the proper position for this method
        // become clear.
        //

        Class[] types = adderMethod.getParameterTypes();
        // key is first parameter
        Class keyType = types[0];
        // value is the second
        Class valueType = types[1];

        Object bean = context.getBean();
        if ( bean != null )
        {
            if ( key instanceof String )
            {
                // try to convert into primitive types
                key = context.getObjectStringConverter()
                      .stringToObject( (String) key, keyType, context );
            }

            if ( value instanceof String )
            {
                // try to convert into primitive types
                value = context.getObjectStringConverter()
                        .stringToObject( (String) value, valueType, context );
            }

            // special case for collection objects into arrays
            if (value instanceof Collection && valueType.isArray())
            {
                Collection valuesAsCollection = (Collection) value;
                Class componentType = valueType.getComponentType();
                if (componentType != null)
                {
                    Object[] valuesAsArray =
                        (Object[]) Array.newInstance(componentType, valuesAsCollection.size());
                    value = valuesAsCollection.toArray(valuesAsArray);
                }
            }


            Object[] arguments = { key, value };
            try
            {
                if ( log.isTraceEnabled() )
                {
                    log.trace(
                        "Calling adder method: " + adderMethod.getName() + " on bean: " + bean
                        + " with key: " + key + " and value: " + value
                    );
                }
                adderMethod.invoke( bean, arguments );

            }
            catch (Exception e)
            {
                log.warn(
                    "Cannot evaluate adder method: " + adderMethod.getName() + " on bean: " + bean
                    + " of type: " + bean.getClass().getName() + " with value: " + value
                    + " of type: " + valueType + " and key: " + key
                    + " of type: " + keyType
                );
                log.debug(e);
            }
        }
    }
}
