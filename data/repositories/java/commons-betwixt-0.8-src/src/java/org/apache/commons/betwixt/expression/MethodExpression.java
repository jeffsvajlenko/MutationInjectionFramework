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

/** <p><code>MethodExpression</code> evaluates a method on the current bean context.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 471234 $
  */
public class MethodExpression implements Expression
{

    /** null arguments */
    protected static Object[] NULL_ARGUMENTS;
    /** null classes */
    protected static Class[] NULL_CLASSES;

    /** The method to call on the bean */
    private Method method;

    /** Base constructor */
    public MethodExpression()
    {
    }

    /**
     * Convenience constructor sets method property
     * @param method the Method whose return value when invoked on the bean
     * will the value of this expression
     */
    public MethodExpression(Method method)
    {
        this.method = method;
    }

    /**
     * Evaluate by calling the read method on the current bean
     *
     * @param context the context against which this expression will be evaluated
     * @return the value returned by the method when it's invoked on the context's bean,
     * so long as the method can be invoked.
     * Otherwise, null.
     */
    public Object evaluate(Context context)
    {
        Object bean = context.getBean();
        if ( bean != null )
        {
            Object[] arguments = getArguments();
            try
            {
                return method.invoke( bean, arguments );

            }
            catch (IllegalAccessException e)
            {
                // lets try use another method with the same name
                Method alternate = null;
                try
                {
                    Class type = bean.getClass();
                    alternate = findAlternateMethod( type, method );
                    if ( alternate != null )
                    {
                        try
                        {
                            return alternate.invoke( bean, arguments );
                        }
                        catch (IllegalAccessException ex)
                        {
                            alternate.setAccessible(true);
                            return alternate.invoke( bean, arguments );
                        }
                    }
                    else
                    {
                        method.setAccessible(true);
                        return method.invoke( bean, arguments );
                    }
                }
                catch (Exception e2)
                {
                    handleException(context, e2, alternate);
                }
            }
            catch (Exception e)
            {
                handleException(context, e, method);
            }
        }
        return null;
    }

    /**
     * Do nothing.
     * @see org.apache.commons.betwixt.expression.Expression
     */
    public void update(Context context, String newValue)
    {
        // do nothing
    }

    /**
     * Gets the method used to evaluate this expression.
     * @return the method whose value (when invoked against the context's bean) will be used
     * to evaluate this expression.
     */
    public Method getMethod()
    {
        return method;
    }

    /**
     * Sets the method used to evaluate this expression
     * @param method method whose value (when invoked against the context's bean) will be used
     * to evaluate this expression
     */
    public void setMethod(Method method)
    {
        this.method = method;
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Allows derived objects to create arguments for the method call
     * @return {@link #NULL_ARGUMENTS}
     */
    protected Object[] getArguments()
    {
        return NULL_ARGUMENTS;
    }

    /** Tries to find an alternate method for the given type using interfaces
      * which gets around the problem of inner classes,
      * such as on Map.Entry implementations.
      *
      * @param type the Class whose methods are to be searched
      * @param method the Method for which an alternative is to be search for
      * @return the alternative Method, if one can be found. Otherwise null.
      */
    protected Method findAlternateMethod(
        Class type,
        Method method )
    {
        // XXX
        // Would it be better to use the standard reflection code in eg. lang
        // since this code contains workarounds for common JVM bugs?
        //
        Class[] interfaces = type.getInterfaces();
        if ( interfaces != null )
        {
            String name = method.getName();
            for ( int i = 0, size = interfaces.length; i < size; i++ )
            {
                Class otherType = interfaces[i];
                //
                // catch NoSuchMethodException so that all interfaces will be tried
                try
                {
                    Method alternate = otherType.getMethod( name, NULL_CLASSES );
                    if ( alternate != null && alternate != method )
                    {
                        return alternate;
                    }
                }
                catch (NoSuchMethodException e)
                {
                    // swallow
                }
            }
        }
        return null;
    }

    /**
      * <p>Log error to context's logger.</p>
      *
      * <p>Allows derived objects to handle exceptions differently.</p>
      *
      * @param context the Context being evaluated when the exception occured
      * @param e the exception to handle
      * @since 0.8
      */
    protected void handleException(Context context, Exception e, Method m)
    {
        // use the context's logger to log the problem
        context.getLog().error("[MethodExpression] Cannot evaluate method " + m, e);
    }

    /**
     * <p>Log error to context's logger.</p>
     *
     * <p>Allows derived objects to handle exceptions differently.</p>
     *
     * @param context the Context being evaluated when the exception occured
     * @param e the exception to handle
     */
    protected void handleException(Context context, Exception e)
    {
        // use the context's logger to log the problem
        context.getLog().error("[MethodExpression] Cannot evaluate method ", e);
    }

    /**
     * Returns something useful for logging.
     * @return something useful for logging
     */
    public String toString()
    {
        return "MethodExpression [method=" + method + "]";
    }
}
