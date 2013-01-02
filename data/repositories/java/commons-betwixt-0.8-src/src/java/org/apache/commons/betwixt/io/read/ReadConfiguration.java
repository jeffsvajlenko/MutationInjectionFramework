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

import org.apache.commons.betwixt.strategy.ActionMappingStrategy;

/**
  * Stores mapping phase configuration settings that apply only for bean reading.
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public class ReadConfiguration
{

    /** Chain used to create beans defaults to BeanCreationChain.createDefaultChain() */
    private BeanCreationChain beanCreationChain = BeanCreationChain.createDefaultChain();
    /** Pluggable strategy used to determine free mappings */
    private ActionMappingStrategy actionMappingStrategy = ActionMappingStrategy.DEFAULT;

    /**
      * Gets the BeanCreationChain that should be used to construct beans.
      * @return the BeanCreationChain to use, not null
      */
    public BeanCreationChain getBeanCreationChain()
    {
        return beanCreationChain;
    }

    /**
      * Sets the BeanCreationChain that should be used to construct beans.
      * @param beanCreationChain the BeanCreationChain to use, not null
      */
    public void setBeanCreationChain( BeanCreationChain beanCreationChain )
    {
        this.beanCreationChain = beanCreationChain;
    }

    /**
     * Gets the <code>ActionMappingStrategy</code> used to define
     * default mapping actions.
     * @return <code>ActionMappignStrategy</code>, not null
     */
    public ActionMappingStrategy getActionMappingStrategy()
    {
        return actionMappingStrategy;
    }

    /**
     * Sets the <code>ActionMappingStrategy</code> used to define
     * default mapping acitons.
     * @param actionMappingStrategy <code>ActionMappignStrategy</code>, not null
     */
    public void setActionMappingStrategy(ActionMappingStrategy actionMappingStrategy)
    {
        this.actionMappingStrategy = actionMappingStrategy;
    }

}
