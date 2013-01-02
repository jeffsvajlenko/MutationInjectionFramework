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
package org.apache.commons.betwixt.io.read;

/**
  * Creator of beans that may delegate responsibility to members down the chain.
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public interface ChainedBeanCreator
{

    /**
      * Creates a bean either directly or by delegating the responsibility to the other
      * members of the chain.
      *
      * @param elementMapping  specifies the mapping between the type and element.
      * <strong>Note</strong> it is recommended that classes do not store a permenant
      * reference to this object since these objects may later be reused.
      * Not null
      * @param context the context in which this converision happens, not null
      * @param chain not null
      * @return the Object created, possibly null
      */

    // TODO: is element mapping really necessary here?
    // TODO: what about exception handling?
    public Object create(
        ElementMapping elementMapping,
        ReadContext context,
        BeanCreationChain chain);

}
