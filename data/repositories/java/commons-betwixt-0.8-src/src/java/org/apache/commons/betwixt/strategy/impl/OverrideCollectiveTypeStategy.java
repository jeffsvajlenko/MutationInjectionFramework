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
package org.apache.commons.betwixt.strategy.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.betwixt.strategy.CollectiveTypeStrategy;

/**
 * Strategy that allows specific classes to be marked as
 * collective ({@link #overrideCollective(Class)})
 * or not collective ({@link #overrideNotCollective(Class)}).
 * @since 0.8
 */
public class OverrideCollectiveTypeStategy extends CollectiveTypeStrategy
{

    private final CollectiveTypeStrategy delegate;

    private final Collection collectiveClasses;
    private final Collection notCollectiveClasses;

    /**
     * Constructs a strategy which delegates to CollectiveTypeStrategy#DEFAULT
     *
     */
    public OverrideCollectiveTypeStategy()
    {
        this(CollectiveTypeStrategy.DEFAULT);
    }

    /**
     * Constructs a strategy which delegates all those that it does not override.
     * @param delegate
     */
    public OverrideCollectiveTypeStategy(CollectiveTypeStrategy delegate)
    {
        super();
        this.delegate = delegate;
        collectiveClasses = new ArrayList();
        notCollectiveClasses = new ArrayList();
    }

    /**
     * Marks the given type to be treated as collective.
     * @param type <code>Class</code>, not null
     */
    public void overrideCollective(Class type)
    {
        collectiveClasses.add(type);
    }

    /**
     * Marks the given type to be treated as not collective
     * @param type
     */
    public void overrideNotCollective(Class type)
    {
        notCollectiveClasses.add(type);
    }

    /**
     * @see CollectiveTypeStrategy#isCollective(Class)
     */
    public boolean isCollective(Class type)
    {
        boolean result = delegate.isCollective(type);
        if (collectiveClasses.contains(type))
        {
            result = true;
        }
        else if (notCollectiveClasses.contains(type))
        {
            result = false;
        }
        return result;
    }

}
