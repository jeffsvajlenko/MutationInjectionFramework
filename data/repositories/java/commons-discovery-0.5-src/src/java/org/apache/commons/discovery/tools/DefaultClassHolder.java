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
package org.apache.commons.discovery.tools;

import org.apache.commons.discovery.ResourceClass;
import org.apache.commons.discovery.ResourceClassIterator;
import org.apache.commons.discovery.resource.classes.DiscoverClasses;
import org.apache.commons.discovery.resource.ClassLoaders;

/**
 * Holder for a default class.
 *
 * Class may be specified by name (String) or class (Class).
 * Using the holder complicates the users job, but minimized # of API's.
 */
public class DefaultClassHolder<T>
{

    private Class<? extends T> defaultClass;

    private final String defaultName;

    /**
     * Creates a new holder implementation given
     * the input SPI implementation/extension class.
     *
     * @param <S> Any type extends the SPI type
     * @param defaultClass The hold class
     */
    public <S extends T> DefaultClassHolder(Class<S> defaultClass)
    {
        this.defaultClass = defaultClass;
        this.defaultName = defaultClass.getName();
    }

    /**
     * Creates a new holder implementation given
     * the input SPI implementation/extension class name.
     *
     * @param defaultName The hold class name
     */
    public DefaultClassHolder(String defaultName)
    {
        this.defaultClass = null;
        this.defaultName = defaultName;
    }

    /**
     * Returns the default class, loading it if necessary
     * and verifying that it implements the SPI
     * (this forces the check, no way out..).
     *
     * @param <S>  Any type extends the SPI type
     * @param spi non-null SPI
     * @param loaders Used only if class needs to be loaded.
     * @return The default Class.
     */
    public <S extends T> Class<S> getDefaultClass(SPInterface<T> spi, ClassLoaders loaders)
    {
        if (defaultClass == null)
        {
            DiscoverClasses<T> classDiscovery = new DiscoverClasses<T>(loaders);
            ResourceClassIterator<T> classes = classDiscovery.findResourceClasses(getDefaultName());
            if (classes.hasNext())
            {
                ResourceClass<T> info = classes.nextResourceClass();
                try
                {
                    defaultClass = info.loadClass();
                }
                catch (Exception e)
                {
                    // ignore
                }
            }
        }

        if (defaultClass != null)
        {
            spi.verifyAncestory(defaultClass);
        }

        @SuppressWarnings("unchecked") // the SPInterface.verifyAncestory already asserted
        Class<S> returned = (Class<S>) defaultClass;
        return returned;
    }

    /**
     * Returns the hold class name.
     *
     * @return The hold class name
     */
    public String getDefaultName()
    {
        return defaultName;
    }

}
