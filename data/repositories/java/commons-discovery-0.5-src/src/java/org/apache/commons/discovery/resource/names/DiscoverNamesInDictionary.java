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

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.commons.discovery.ResourceNameDiscover;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Recover resources from a Dictionary.  This covers Properties as well,
 * since <code>Properties extends Hashtable extends Dictionary</code>.
 *
 * The recovered value is expected to be either a <code>String</code>
 * or a <code>String[]</code>.
 */
public class DiscoverNamesInDictionary extends ResourceNameDiscoverImpl implements ResourceNameDiscover
{

    private static Log log = LogFactory.getLog(DiscoverNamesInDictionary.class);

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

    private Dictionary<String, String[]> dictionary;

    /**
     * Construct a new resource discoverer with an empty Dictionary.
     */
    public DiscoverNamesInDictionary()
    {
        setDictionary(new Hashtable<String, String[]>());
    }

    /**
     * Construct a new resource discoverer with the given Dictionary.
     *
     * @param dictionary The initial Dictionary
     */
    public DiscoverNamesInDictionary(Dictionary<String, String[]> dictionary)
    {
        setDictionary(dictionary);
    }

    /**
     * Returns the current Dictionary for names mapping.
     *
     * @return The current Dictionary for names mapping
     */
    protected Dictionary<String, String[]> getDictionary()
    {
        return dictionary;
    }

    /**
     * Specify the Dictionary for names mapping.
     *
     * @param table The Dictionary for names mapping
     */
    public void setDictionary(Dictionary<String, String[]> table)
    {
        this.dictionary = table;
    }

    /**
     * Add a resource name to a single name mapping.
     *
     * @param resourceName The resource name
     * @param resource The target name
     */
    public void addResource(String resourceName, String resource)
    {
        addResource(resourceName, new String[] { resource });
    }

    /**
     * Add a resource name to multiple names mapping.
     *
     * @param resourceName The resource name
     * @param resources The target names
     */
    public void addResource(String resourceName, String[] resources)
    {
        dictionary.put(resourceName, resources);
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

        final String[] resources = dictionary.get(resourceName);

        return new ResourceNameIterator()
        {
            private int idx = 0;

            public boolean hasNext()
            {
                if (resources != null)
                {
                    while (idx < resources.length  &&  resources[idx] == null)
                    {
                        idx++;
                    }
                    return idx < resources.length;
                }
                return false;
            }

            public String nextResourceName()
            {
                return hasNext() ? resources[idx++] : null;
            }
        };
    }

}
