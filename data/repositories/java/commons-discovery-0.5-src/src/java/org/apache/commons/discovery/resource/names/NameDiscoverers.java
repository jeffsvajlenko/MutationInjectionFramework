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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.discovery.ResourceNameDiscover;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Holder for multiple ResourceNameDiscover instances.
 *
 * The result is the union of the results from each
 * (not a chained sequence, where results feed the next in line.
 */
public class NameDiscoverers extends ResourceNameDiscoverImpl implements ResourceNameDiscover
{

    private static Log log = LogFactory.getLog(NameDiscoverers.class);

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

    private final List<ResourceNameDiscover> discoverers = new ArrayList<ResourceNameDiscover>();

    /**
     *  Construct a new resource name discoverer
     */
    public NameDiscoverers()
    {
    }

    /**
     * Specify an discover to be used in searching.
     * The order of discover determines the order of the result.
     * It is recommended to add the most specific discover first.
     *
     * @param discover The discover to be added
     */
    public void addResourceNameDiscover(ResourceNameDiscover discover)
    {
        if (discover != null)
        {
            discoverers.add(discover);
        }
    }

    /**
     * Retrieve the discover positioned at the given index.
     *
     * @param idx The discover index position client is requiring
     * @return The discover positioned at the input index
     */
    protected ResourceNameDiscover getResourceNameDiscover(int idx)
    {
        return discoverers.get(idx);
    }

    /**
     * Returns the current size of set discovers.
     *
     * @return The current size of set discovers
     */
    protected int size()
    {
        return discoverers.size();
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

            private int idx = 0;

            private ResourceNameIterator iterator = null;

            public boolean hasNext()
            {
                if (iterator == null  ||  !iterator.hasNext())
                {
                    iterator = getNextIterator();
                    if (iterator == null)
                    {
                        return false;
                    }
                }
                return iterator.hasNext();
            }

            public String nextResourceName()
            {
                return iterator.nextResourceName();
            }

            private ResourceNameIterator getNextIterator()
            {
                while (idx < size())
                {
                    ResourceNameIterator iter =
                        getResourceNameDiscover(idx++).findResourceNames(resourceName);

                    if (iter.hasNext())
                    {
                        return iter;
                    }
                }
                return null;
            }
        };
    }

}
