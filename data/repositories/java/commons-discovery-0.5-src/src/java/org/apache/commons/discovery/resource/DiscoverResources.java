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
package org.apache.commons.discovery.resource;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

import org.apache.commons.discovery.Resource;
import org.apache.commons.discovery.ResourceDiscover;
import org.apache.commons.discovery.ResourceIterator;
import org.apache.commons.discovery.jdk.JDKHooks;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 *
 */
public class DiscoverResources extends ResourceDiscoverImpl implements ResourceDiscover
{

    private static Log log = LogFactory.getLog(DiscoverResources.class);

    /**
     * Sets the {@code Log} for this class.
     *
     * @param _log This class {@code Log}
     * @deprecated This method is not thread-safe
     */
    @Deprecated
    public static void setLog(Log _log)
    {
        log = _log;
    }

    /**
     * Construct a new resource discoverer.
     */
    public DiscoverResources()
    {
        super();
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param classLoaders The class loaders holder
     */
    public DiscoverResources(ClassLoaders classLoaders)
    {
        super(classLoaders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceIterator findResources(final String resourceName)
    {
        if (log.isDebugEnabled())
        {
            log.debug("find: resourceName='" + resourceName + "'");
        }

        return new ResourceIterator()
        {

            private int idx = 0;

            private ClassLoader loader = null;

            private Enumeration<URL> resources = null;

            private Resource resource = null;

            public boolean hasNext()
            {
                if (resource == null)
                {
                    resource = getNextResource();
                }
                return resource != null;
            }

            @Override
            public Resource nextResource()
            {
                Resource element = resource;
                resource = null;
                return element;
            }

            private Resource getNextResource()
            {
                if (resources == null || !resources.hasMoreElements())
                {
                    resources = getNextResources();
                }

                Resource resourceInfo;
                if (resources != null)
                {
                    URL url = resources.nextElement();

                    if (log.isDebugEnabled())
                    {
                        log.debug("getNextResource: next URL='" + url + "'");
                    }

                    resourceInfo = new Resource(resourceName, url, loader);
                }
                else
                {
                    resourceInfo = null;
                }

                return resourceInfo;
            }

            private Enumeration<URL> getNextResources()
            {
                while (idx < getClassLoaders().size())
                {
                    loader = getClassLoaders().get(idx++);
                    if (log.isDebugEnabled())
                    {
                        log.debug("getNextResources: search using ClassLoader '" + loader + "'");
                    }
                    try
                    {
                        Enumeration<URL> e = JDKHooks.getJDKHooks().getResources(loader, resourceName);
                        if (e != null && e.hasMoreElements())
                        {
                            return e;
                        }
                    }
                    catch( IOException ex )
                    {
                        log.warn("getNextResources: Ignoring Exception", ex);
                    }
                }
                return null;
            }
        };
    }

}
