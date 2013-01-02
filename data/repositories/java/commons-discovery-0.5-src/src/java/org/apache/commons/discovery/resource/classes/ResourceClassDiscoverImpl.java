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
package org.apache.commons.discovery.resource.classes;

import org.apache.commons.discovery.ResourceClass;
import org.apache.commons.discovery.ResourceClassDiscover;
import org.apache.commons.discovery.ResourceClassIterator;
import org.apache.commons.discovery.ResourceIterator;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.ResourceDiscoverImpl;

/**
 * Default {@link ResourceClassDiscover} implementation.
 *
 * @param <T> The SPI type
 */
public abstract class ResourceClassDiscoverImpl<T> extends ResourceDiscoverImpl implements ResourceClassDiscover<T>
{

    /**
     * Construct a new resource discoverer.
     */
    public ResourceClassDiscoverImpl()
    {
        super();
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param classLoaders The class loaders holder
     */
    public ResourceClassDiscoverImpl(ClassLoaders classLoaders)
    {
        super(classLoaders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceNameIterator findResourceNames(String resourceName)
    {
        return findResourceClasses(resourceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceNameIterator findResourceNames(ResourceNameIterator resourceNames)
    {
        return findResourceClasses(resourceNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceIterator findResources(String resourceName)
    {
        return findResourceClasses(resourceName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceIterator findResources(ResourceNameIterator resourceNames)
    {
        return findResourceClasses(resourceNames);
    }

    /**
     * Locate class resources that are bound to <code>className</code>.
     *
     * @param className The class name has to be located
     * @return The located resources iterator
     */
    public abstract ResourceClassIterator<T> findResourceClasses(String className);

    /**
     * Locate class resources that are bound to {@code resourceNames}.
     *
     * @param inputNames The resource name iterator
     * @return a new {@link ResourceClassIterator} over the given resource name iterator
     */
    public ResourceClassIterator<T> findResourceClasses(final ResourceNameIterator inputNames)
    {
        return new ResourceClassIterator<T>()
        {
            private ResourceClassIterator<T> classes = null;
            private ResourceClass<T> resource = null;

            public boolean hasNext()
            {
                if (resource == null)
                {
                    resource = getNextResource();
                }
                return resource != null;
            }

            @Override
            public ResourceClass<T> nextResourceClass()
            {
                ResourceClass<T> rsrc = resource;
                resource = null;
                return rsrc;
            }

            private ResourceClass<T> getNextResource()
            {
                while (inputNames.hasNext() &&
                        (classes == null  ||  !classes.hasNext()))
                {
                    classes =
                        findResourceClasses(inputNames.nextResourceName());
                }

                return (classes != null  &&  classes.hasNext())
                       ? classes.nextResourceClass()
                       : null;
            }
        };
    }

}
