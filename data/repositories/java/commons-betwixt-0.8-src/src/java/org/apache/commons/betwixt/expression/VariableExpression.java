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

/** <p><code>VariableExpression</code> represents a variable expression such as
  * <code>$foo</code> which returns the value of the given variable.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class VariableExpression implements Expression
{

    /** The variable name */
    private String variableName;

    /** Base constructor */
    public VariableExpression()
    {
    }

    /**
     * Convenience constructor sets <code>VariableName</code> property
     * @param variableName the name of the context variable
     * whose value will be returned by an evaluation
     */
    public VariableExpression(String variableName)
    {
        this.variableName = variableName;
    }

    /** Return the value of a context variable.
      *
      * @param context evaluate against this context
      * @return the value of the context variable named by the <code>VariableName</code> property
      */
    public Object evaluate(Context context)
    {
        return context.getVariable( variableName );
    }

    /**
     * Gets the variable name
     * @return the name of the context variable whose value will be returned by an evaluation
     */
    public String getVariableName()
    {
        return variableName;
    }

    /**
     * Sets the variable name
     * @param variableName the name of the context variable
     * whose value will be returned by an evaluation
     */
    public void setVariableName(String variableName)
    {
        this.variableName = variableName;
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
     * Returns something useful for logging
     * @return something useful for logging
     */
    public String toString()
    {
        return "VariableExpression [variable name=" + variableName + "]";
    }
}
