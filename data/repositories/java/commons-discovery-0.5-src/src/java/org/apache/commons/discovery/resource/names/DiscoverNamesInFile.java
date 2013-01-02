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
package org.apache.commons.discovery.resource.names;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.discovery.Resource;
import org.apache.commons.discovery.ResourceDiscover;
import org.apache.commons.discovery.ResourceIterator;
import org.apache.commons.discovery.ResourceNameDiscover;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.DiscoverResources;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Discover ALL files of a given name, and return resource names
 * contained within the set of files:
 * <ul>
 *   <li>one resource name per line,</li>
 *   <li>whitespace ignored,</li>
 *   <li>comments begin with '#'</li>
 * </ul>
 *
 * Default discoverer is DiscoverClassLoaderResources,
 * but it can be set to any other.
 */
public class DiscoverNamesInFile extends ResourceNameDiscoverImpl implements ResourceNameDiscover
{

    private static Log log = LogFactory.getLog(DiscoverNamesInFile.class);

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

    private ResourceDiscover _discoverResources;

    private final String _prefix;

    private final String _suffix;

    /**
     * Construct a new resource discoverer.
     */
    public DiscoverNamesInFile()
    {
        _discoverResources = new DiscoverResources();
        _prefix = null;
        _suffix = null;
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param prefix The resource name prefix
     * @param suffix The resource name suffix
     */
    public DiscoverNamesInFile(String prefix, String suffix)
    {
        _discoverResources = new DiscoverResources();
        _prefix = prefix;
        _suffix = suffix;
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param loaders The class loaders holder
     */
    public DiscoverNamesInFile(ClassLoaders loaders)
    {
        _discoverResources = new DiscoverResources(loaders);
        _prefix = null;
        _suffix = null;
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param loaders The class loaders holder
     * @param prefix The resource name prefix
     * @param suffix The resource name suffix
     */
    public DiscoverNamesInFile(ClassLoaders loaders, String prefix, String suffix)
    {
        _discoverResources = new DiscoverResources(loaders);
        _prefix = prefix;
        _suffix = suffix;
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param discoverer The discoverer to resolve resources
     */
    public DiscoverNamesInFile(ResourceDiscover discoverer)
    {
        _discoverResources = discoverer;
        _prefix = null;
        _suffix = null;
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param discoverer The discoverer to resolve resources
     * @param prefix The resource name prefix
     * @param suffix The resource name suffix
     */
    public DiscoverNamesInFile(ResourceDiscover discoverer, String prefix, String suffix)
    {
        _discoverResources = discoverer;
        _prefix = prefix;
        _suffix = suffix;
    }

    /**
     * Set the discoverer to resolve resources.
     *
     * @param discover The discoverer to resolve resources
     */
    public void setDiscoverer(ResourceDiscover discover)
    {
        _discoverResources = discover;
    }

    /**
     * Return the discoverer to resolve resources.
     *
     * To be used by downstream elements...
     *
     * @return The discoverer to resolve resources
     */
    public ResourceDiscover getDiscover()
    {
        return _discoverResources;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceNameIterator findResourceNames(final String serviceName)
    {
        String fileName;
        if (_prefix != null && _prefix.length() > 0)
        {
            fileName = _prefix + serviceName;
        }
        else
        {
            fileName = serviceName;
        }

        if (_suffix != null && _suffix.length() > 0)
        {
            fileName = fileName + _suffix;
        }

        if (log.isDebugEnabled())
        {
            if (_prefix != null  &&  _suffix != null)
            {
                log.debug("find: serviceName='" + serviceName + "' as '" + fileName + "'");
            }
            else
            {
                log.debug("find: serviceName = '" + fileName + "'");
            }
        }


        final ResourceIterator files =
            getDiscover().findResources(fileName);

        return new ResourceNameIterator()
        {

            private int idx = 0;

            private List<String> classNames = null;

            private String resource = null;

            public boolean hasNext()
            {
                if (resource == null)
                {
                    resource = getNextClassName();
                }
                return resource != null;
            }

            public String nextResourceName()
            {
                String element = resource;
                resource = null;
                return element;
            }

            private String getNextClassName()
            {
                if (classNames == null || idx >= classNames.size())
                {
                    classNames = getNextClassNames();
                    idx = 0;
                    if (classNames == null)
                    {
                        return null;
                    }
                }

                String className = classNames.get(idx++);

                if (log.isDebugEnabled())
                {
                    log.debug("getNextClassResource: next class='" + className + "'");
                }

                return className;
            }

            private List<String> getNextClassNames()
            {
                while (files.hasNext())
                {
                    List<String> results = readServices(files.nextResource());
                    if (results != null  &&  results.size() > 0)
                    {
                        return results;
                    }
                }
                return null;
            }
        };
    }

    /**
     * Parses the resource info file and store all the defined SPI implementation classes
     *
     * @param info The resource file
     * @return The list with all SPI implementation names
     */
    private List<String> readServices(final Resource info)
    {
        List<String> results = new ArrayList<String>();

        InputStream is = info.getResourceAsStream();

        if (is != null)
        {
            try
            {
                try
                {
                    // This code is needed by EBCDIC and other
                    // strange systems.  It's a fix for bugs
                    // reported in xerces
                    BufferedReader rd;
                    try
                    {
                        rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    }
                    catch (java.io.UnsupportedEncodingException e)
                    {
                        rd = new BufferedReader(new InputStreamReader(is));
                    }

                    try
                    {
                        String serviceImplName;
                        while( (serviceImplName = rd.readLine()) != null)
                        {
                            int idx = serviceImplName.indexOf('#');
                            if (idx >= 0)
                            {
                                serviceImplName = serviceImplName.substring(0, idx);
                            }
                            serviceImplName = serviceImplName.trim();

                            if (serviceImplName.length() != 0)
                            {
                                results.add(serviceImplName);
                            }
                        }
                    }
                    finally
                    {
                        rd.close();
                    }
                }
                finally
                {
                    is.close();
                }
            }
            catch (IOException e)
            {
                // ignore
            }
        }

        return results;
    }

}
