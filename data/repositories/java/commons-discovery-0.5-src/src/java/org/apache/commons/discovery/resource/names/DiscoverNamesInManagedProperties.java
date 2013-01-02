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

import org.apache.commons.discovery.ResourceNameDiscover;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.discovery.tools.ManagedProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Recover resource name from Managed Properties.
 * @see org.apache.commons.discovery.tools.ManagedProperties
 */
public class DiscoverNamesInManagedProperties extends ResourceNameDiscoverImpl implements ResourceNameDiscover
{

    private static Log log = LogFactory.getLog(DiscoverNamesInManagedProperties.class);

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

    private final String _prefix;

    private final String _suffix;

    /**
     * Construct a new resource discoverer.
     */
    public DiscoverNamesInManagedProperties()
    {
        this(null, null);
    }

    /**
     * Construct a new resource discoverer.
     *
     * @param prefix The resource name prefix
     * @param suffix The resource name suffix
     */
    public DiscoverNamesInManagedProperties(String prefix, String suffix)
    {
        _prefix = prefix;
        _suffix = suffix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceNameIterator findResourceNames(final String resourceName)
    {
        String name;
        if (_prefix != null && _prefix.length() > 0)
        {
            name = _prefix + resourceName;
        }
        else
        {
            name = resourceName;
        }

        if (_suffix != null && _suffix.length() > 0)
        {
            name = name + _suffix;
        }

        if (log.isDebugEnabled())
        {
            if (_prefix != null  &&  _suffix != null)
            {
                log.debug("find: resourceName='" + resourceName + "' as '" + name + "'");
            }
            else
            {
                log.debug("find: resourceName = '" + name + "'");
            }
        }

        final String newResourcName = name;

        return new ResourceNameIterator()
        {

            private String resource = ManagedProperties.getProperty(newResourcName);

            public boolean hasNext()
            {
                return resource != null;
            }

            public String nextResourceName()
            {
                String element = resource;
                resource = null;
                return element;
            }
        };
    }

}
