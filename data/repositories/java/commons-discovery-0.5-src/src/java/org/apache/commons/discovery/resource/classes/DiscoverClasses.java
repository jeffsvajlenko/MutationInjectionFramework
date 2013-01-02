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

import java.net.URL;
import java.security.CodeSource;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.discovery.ResourceClass;
import org.apache.commons.discovery.ResourceClassDiscover;
import org.apache.commons.discovery.ResourceClassIterator;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The findResources() method will check every loader.
 *
 * @param <T> The SPI type
 */
public class DiscoverClasses<T> extends ResourceClassDiscoverImpl<T> implements ResourceClassDiscover<T>
{

    private static Log log = LogFactory.getLog(DiscoverClasses.class);

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
     * Construct a new resource discoverer
     */
    public DiscoverClasses()
    {
        super();
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param classLoaders The class loaders holder
     */
    public DiscoverClasses(ClassLoaders classLoaders)
    {
        super(classLoaders);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceClassIterator<T> findResourceClasses(final String className)
    {
        final String resourceName = className.replace('.','/') + ".class";

        if (log.isDebugEnabled())
        {
            log.debug("find: className='" + className + "'");
        }

        return new ResourceClassIterator<T>()
        {

            private final Set<URL> history = new HashSet<URL>();

            private int idx = 0;

            private ResourceClass<T> resource = null;

            public boolean hasNext()
            {
                if (resource == null)
                {
                    resource = getNextClass();
                }
                return resource != null;
            }

            @Override
            public ResourceClass<T> nextResourceClass()
            {
                ResourceClass<T> element = resource;
                resource = null;
                return element;
            }

            private ResourceClass<T> getNextClass()
            {
                while (idx < getClassLoaders().size())
                {
                    ClassLoader loader = getClassLoaders().get(idx++);

                    URL url = null;

                    try
                    {
                        url = loader.getResource(resourceName);
                    }
                    catch (UnsupportedOperationException e)
                    {
                        // ignore
                    }

                    if (url == null)
                    {
                        try
                        {
                            CodeSource codeSource = loader.loadClass(className)
                                                    .getProtectionDomain()
                                                    .getCodeSource();
                            if (codeSource != null)
                            {
                                url = new URL(codeSource.getLocation(), resourceName);
                            }
                            // else keep url null
                        }
                        catch (Exception le)
                        {
                            // keep url null
                        }
                    }

                    if (url != null)
                    {
                        if (history.add(url))
                        {
                            if (log.isDebugEnabled())
                            {
                                log.debug("getNextClass: next URL='" + url + "'");
                            }

                            return new ResourceClass<T>(className, url, loader);
                        }
                        if (log.isDebugEnabled())
                        {
                            log.debug("getNextClass: duplicate URL='" + url + "'");
                        }
                    }
                    else
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("getNextClass: loader " + loader + ": '" + resourceName + "' not found");
                        }
                    }
                }
                return null;
            }
        };
    }

}
