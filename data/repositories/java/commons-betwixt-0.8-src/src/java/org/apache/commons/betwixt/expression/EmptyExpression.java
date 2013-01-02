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

/** <p><code>EmptyExpression</code> returns the same value as is passed in. </p>
  *
  * <p> See {@link #evaluate}. </p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class EmptyExpression implements Expression
{

    /** Don't need more than one <code>EmptyExpression</code>*/
    private static final EmptyExpression singleton = new EmptyExpression();

    /**
     * Gets the singleton instance.
     * @return the EmptyExpression singleton.
     */
    public static EmptyExpression getInstance()
    {
        return singleton;
    }

    /** Should this be private?
     */
    public EmptyExpression()
    {
    }

    /** Return the bean we're evaluating.
     * @see org.apache.commons.betwixt.expression.Expression
     */
    public Object evaluate(Context context)
    {
        return context.getBean();
    }

    /** Do nothing
     * @see org.apache.commons.betwixt.expression.Expression
     */
    public void update(Context context, String newValue)
    {
        // do nothing
    }

    /**
     * Return something useful for logging.
     * @return short name for this class
     */
    public String toString()
    {
        return "EmptyExpression";
    }

}
