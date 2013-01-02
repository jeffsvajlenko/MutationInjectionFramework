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
  * A Chain of bean creators.
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public abstract class BeanCreationChain
{


//-------------------------------------------------------- Class Methods

    /**
     * Creates the default <code>BeanCreationChain</code> used when reading beans.
     * @return a <code>BeanCreationList</code> with the default creators loader in order, not null
     */
    public static final BeanCreationChain createDefaultChain()
    {
        // this delegates to the list but this is the canonical version
        // the implementation may change later
        return BeanCreationList.createStandardChain();
    }

//-------------------------------------------------------- Instance Methods

    /**
      * Create a bean for the given mapping in the given context.
      *
      * @param elementMapping specifies the mapping between the type and element.
      * <strong>Note</strong> it is recommended that classes do not store a permenant
      * reference to this object since these objects may later be reused.
      * Not null
      * @param context the context in which this creation happens, not null
      * @return the bean, possibly null
      */
    public abstract Object create(ElementMapping elementMapping, ReadContext context);

}
