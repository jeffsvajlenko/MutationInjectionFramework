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


import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** <p><code>MethodUpdater</code> updates the current bean context
  * by calling a WriteMethod with the String value from the XML attribute
  * or element.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class MethodUpdater extends TypedUpdater
{

    /** Logger */
    private static Log log = LogFactory.getLog( MethodUpdater.class );

    /**
     * Programmatically set log
     * @param aLog the implementation to which this class should log
     */
    public static void setLog( Log aLog )
    {
        log = aLog;
    }

    /** The method to call on the bean */
    private Method method;
    /** Base constructor */
    public MethodUpdater()
    {
    }

    /**
     * Convenience constructor sets method property
     * @param method the Method to be invoked on the context's bean in the update
     */
    public MethodUpdater(Method method)
    {
        setMethod( method );
    }

    /**
     * Gets the method which will be invoked by the update
     *
     * @return the Method to be invoked by the update
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * Sets the constant value of this expression
     * @param method the Method to be invoked by the update
     */
    public void setMethod(Method method)
    {
        this.method = method;
        Class[] types = method.getParameterTypes();
        if ( types == null || types.length <= 0 )
        {
            throw new IllegalArgumentException( "The Method must have at least one parameter" );
        }
        setValueType(types[0]);
    }

    // Implementation methods
    //-------------------------------------------------------------------------



    /**
     * Returns something useful for logging.
     * @return something useful for logging
     */
    public String toString()
    {
        return "MethodUpdater [method=" + method + "]";
    }

    /**
     * Updates the bean by method invocation.
     * @since 0.7
     */
    protected void executeUpdate(Context context, Object bean, Object newValue) throws Exception
    {
        if ( log.isDebugEnabled() )
        {
            log.debug(
                "Calling setter method: " + method.getName() + " on bean: " + bean
                + " with new value: " + newValue
            );
        }
        Object[] arguments = { newValue };
        try
        {
            method.invoke( bean, arguments );
        }
        catch (IllegalAccessException e)
        {
            method.setAccessible(true);
            method.invoke( bean, arguments );
        }
    }
}
