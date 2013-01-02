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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.discovery.ResourceNameDiscover;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.discovery.tools.ManagedProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Recover resource name from Managed Properties,
 * using OLD property names.
 *
 * This class maintains a mapping between old names and
 * (new) the class names they represent.  The discovery
 * mechanism uses the class names as property names.
 *
 * @see org.apache.commons.discovery.tools.ManagedProperties
 */
public class DiscoverNamesInAlternateManagedProperties
    extends ResourceNameDiscoverImpl
    implements ResourceNameDiscover
{

    private static Log log = LogFactory.getLog(DiscoverNamesInAlternateManagedProperties.class);

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

    private final Map<String, String> mapping = new HashMap<String, String>();

    /**
     * Construct a new resource discoverer.
     */
    public DiscoverNamesInAlternateManagedProperties()
    {
    }

    /**
     * Add a class name/property name mapping.
     *
     * @param className The class name
     * @param propertyName The property name
     */
    public void addClassToPropertyNameMapping(String className, String propertyName)
    {
        mapping.put(className, propertyName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceNameIterator findResourceNames(final String resourceName)
    {
        final String mappedName = mapping.get(resourceName);

        if (log.isDebugEnabled())
        {
            if (mappedName == null)
            {
                log.debug("find: resourceName='" + resourceName + "', no mapping");
            }
            else
            {
                log.debug("find: resourceName='" + resourceName + "', lookup property '" + mappedName + "'");
            }
        }

        return new ResourceNameIterator()
        {
            private String resource =
                (mappedName == null) ? null : ManagedProperties.getProperty(mappedName);

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
