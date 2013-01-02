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

/** <p><code>ConstantExpression</code> represents a constant expression.</p>
  *
  * <p> In other words, {@link #evaluate} returns a value independent of the context. </p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class ConstantExpression implements Expression
{

    /** The value of this expression */
    private Object value;

    /** Base constructor
     */
    public ConstantExpression()
    {
    }

    /**
      * Convenience constructor sets <code>value</code> property.
      * @param value the Object which is the constant value for this expression
      */
    public ConstantExpression(Object value)
    {
        this.value = value;
    }

    /**
      * Evaluate expression against given context.
      *
      * @param context evaluate expression against this context
      * @return current value of <code>value</code> property
      */
    public Object evaluate(Context context)
    {
        return value;
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
     * Gets the constant value of this expression
     * @return this expression's constant value
     */
    public Object getValue()
    {
        return value;
    }

    /**
     * Sets the constant value of this expression
     * @param value the constant value for this expression
     */
    public void setValue(Object value)
    {
        this.value = value;
    }

    /**
     * Returns something useful for logging
     * @return something useful for logging
     */
    public String toString()
    {
        return "ConstantExpression [value=" + value + "]";
    }
}
