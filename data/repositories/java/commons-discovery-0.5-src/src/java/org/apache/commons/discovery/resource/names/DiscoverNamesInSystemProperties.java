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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Recover resource name from System Properties.
 */
public class DiscoverNamesInSystemProperties extends ResourceNameDiscoverImpl implements ResourceNameDiscover
{

    private static Log log = LogFactory.getLog(DiscoverNamesInSystemProperties.class);

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
    public DiscoverNamesInSystemProperties()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceNameIterator findResourceNames(final String resourceName)
    {
        if (log.isDebugEnabled())
        {
            log.debug("find: resourceName='" + resourceName + "'");
        }

        return new ResourceNameIterator()
        {

            private String resource = System.getProperty(resourceName);

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
