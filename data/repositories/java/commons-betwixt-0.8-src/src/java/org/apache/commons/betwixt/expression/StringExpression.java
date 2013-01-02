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

/** <p><code>StringExpression</code> returns the current context object as a string.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class StringExpression implements Expression
{

    /** We only need only <code>StringExpression</code> */
    private static final StringExpression singleton = new StringExpression();

    /**
     * Gets the singleton
     * @return the singleton <code>StringExpression</code> instance
     */
    public static StringExpression getInstance()
    {
        return singleton;
    }

    /** Base constructor. Should this be private? */
    public StringExpression()
    {
    }

    /** Return the context bean as a string
      *
      * @param context evaluate expression against this context
      * @return the <code>toString()</code> representation of the context bean
      */
    public Object evaluate(Context context)
    {
        Object value = context.getBean();
        if ( value != null )
        {
            return value.toString();
        }
        return null;
    }

    /**
     * Do nothing
     * @see org.apache.commons.betwixt.expression.Expression
     */
    public void update(Context context, String newValue)
    {
        // do nothing
    }

    /**
     * Returns something useful for logging.
     * @return the (short) class name
     */
    public String toString()
    {
        return "StringExpression";
    }

}
