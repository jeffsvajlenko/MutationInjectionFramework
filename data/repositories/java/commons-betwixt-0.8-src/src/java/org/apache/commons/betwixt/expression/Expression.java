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

/** <p><code>Expression</code> represents an arbitrary expression on a bean.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  */
public interface Expression
{

    /** Evaluates the expression on the bean with the given context
     * and returns the result
     *
     * @param context evaluate against this <code>Context</code>
     * @return the value of the expression
     */
    public Object evaluate(Context context);


    /* XXX
       Is update actually useful?
       None of the expression implementations i can find do anything when it's called.
       I suspect that it's been replaced by Updater
       but i maybe i'm missing something subtle
    */
    /** <p>Updates the current bean context with a new String value.
     * This is typically used when parsing XML and updating a beans value
     * from XML.<p>
     *
     * @param context update this <code>Context</code>
     * @param newValue the new value for this expression
     * @deprecated 0.5 use {@link Updater} instead
     */
    public void update(Context context, String newValue);
}
