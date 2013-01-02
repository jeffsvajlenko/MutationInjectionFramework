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
package org.apache.commons.betwixt;

/** <p>Describes mixed-content text.
  * A mixed content element contains elements mixed with text.
  * For example:
  * <pre>
  *    &lt;foo&gt;middle&lt;bar/&gt;&lt;/foo&gt;
  * </pre>
  * In the above example, a <code>TextDescriptor</code> could be used
  * to allow the mixed content text <code>middle</code> to be mapped.</p>
  *
  * <p>This is really just a marker class - all functionality is inherited.</p>
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public class TextDescriptor extends Descriptor
{

    /** Base constructor */
    public TextDescriptor()
    {
    }

}
