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
package org.apache.commons.discovery;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * 'Resource' located by discovery.
 * Naming of methods becomes a real pain ('getClass()')
 * so I've patterned this after ClassLoader...
 *
 * I think it works well as it will give users a point-of-reference.
 */
public class Resource
{

    protected final String      name;

    protected final URL         resource;

    protected final ClassLoader loader;

    /**
     * Create a new {@link Resource} instance.
     *
     * @param resourceName The resource name has to be located
     * @param resource The resource URL has to be located
     * @param loader The class loader used to locate the given resource
     */
    public Resource(String resourceName, URL resource, ClassLoader loader)
    {
        this.name = resourceName;
        this.resource = resource;
        this.loader = loader;
    }

    /**
     * Get the value of resourceName.
     * @return value of resourceName.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the value of URL.
     * @return value of URL.
     */
    public URL getResource()
    {
        return resource;
    }

    /**
     * Get the value of URL.
     * @return value of URL.
     */
    public InputStream getResourceAsStream()
    {
        try
        {
            return resource.openStream();
        }
        catch (IOException e)
        {
            return null;  // ignore
        }
    }

    /**
     * Get the value of loader.
     * @return value of loader.
     */
    public ClassLoader getClassLoader()
    {
        return loader ;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return "Resource[" + getName() +  ", " + getResource() + ", " + getClassLoader() + "]";
    }

    /**
     * Returns an array containing all of the elements in this {@link ResourceIterator} in proper sequence.
     *
     * @param iterator The {@link ResourceIterator} containing the
     * @return An array containing the elements of the given {@link ResourceIterator}
     */
    public static Resource[] toArray(ResourceIterator iterator)
    {
        List<Resource> resourceList = new LinkedList<Resource>();
        while (iterator.hasNext())
        {
            resourceList.add(iterator.nextResource());
        }
        Resource[] resources = new Resource[resourceList.size()];
        resourceList.toArray(resources);

        return resources;
    }

}
