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


/** <p><code>ClassNameExpression</code> returns the current class name
  * of the context bean
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @since 0.5
  */
public class ClassNameExpression implements Expression
{

    /** Base constructor */
    public ClassNameExpression()
    {
    }

    /**
     * Evaluate on the current context and return the class name
     *
     * @param context the context against which this expression will be evaluated
     * @return the name of the class of the current contex bean
     */
    public Object evaluate(Context context)
    {
        Object bean = context.getBean();
        if ( bean != null )
        {
            return bean.getClass().getName();
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
     * Returns something useful for logging.
     * @return something useful for logging
     */
    public String toString()
    {
        return "ClassNameExpression";
    }
}
