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

import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.discovery.ResourceNameDiscover;
import org.apache.commons.discovery.ResourceNameIterator;
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
public class DiscoverMappedNames extends ResourceNameDiscoverImpl implements ResourceNameDiscover
{

    private static Log log = LogFactory.getLog(DiscoverMappedNames.class);

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
     * The String name ==> String[] newNames mapping
     */
    private final Map<String, String[]> mapping = new Hashtable<String, String[]>();

    /**
     * Construct a new resource discoverer
     */
    public DiscoverMappedNames()
    {
    }

    /**
     * Maps a name to another name.
     *
     * @param fromName The name has to be mapped
     * @param toName The mapping target
     */
    public void map(String fromName, String toName)
    {
        map(fromName, new String[] { toName });
    }

    /**
     * Maps a name to multiple names.
     *
     * @param fromName The name has to be mapped
     * @param toNames The mapping targets
     */
    public void map(String fromName, String [] toNames)
    {
        mapping.put(fromName, toNames);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceNameIterator findResourceNames(final String resourceName)
    {
        if (log.isDebugEnabled())
        {
            log.debug("find: resourceName='" + resourceName + "', mapping to constants");
        }

        final String[] names = mapping.get(resourceName);

        return new ResourceNameIterator()
        {

            private int idx = 0;

            public boolean hasNext()
            {
                if (names != null)
                {
                    while (idx < names.length  &&  names[idx] == null)
                    {
                        idx++;
                    }
                    return idx < names.length;
                }
                return false;
            }

            public String nextResourceName()
            {
                return hasNext() ? names[idx++] : null;
            }
        };
    }

}
