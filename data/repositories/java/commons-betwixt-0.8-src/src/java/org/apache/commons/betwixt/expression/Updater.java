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

/** <p><code>Updater</code> acts like an lvalue which updates the current
  * context bean from some text from an XML attribute or element.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public interface Updater
{

    /** Updates the current bean context with a new String value.
     * This is typically used when parsing XML and updating a bean value
     * from XML
     *
     * @param context update the bean in this <code>Context</code>
     * @param newValue set value to this <code>Object</code>
     */
    public void update(Context context, Object newValue);
}
