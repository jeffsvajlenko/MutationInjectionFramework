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
package org.apache.commons.discovery.ant;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.discovery.jdk.JDKHooks;
import org.apache.commons.discovery.resource.DiscoverResources;

/**
 * Small ant task that will use discovery to locate a particular impl.
 * and display all values.
 *
 * You can execute this and save it with an id, then other classes can use it.
 */
public class ServiceDiscoveryTask
{

    String name;

    int debug=0;

    String[] drivers = null;

    /**
     * Sets the service name has to be discovered.
     *
     * @param name The service name has to be discovered.
     */
    public void setServiceName(String name)
    {
        this.name=name;
    }

    /**
     * Sets the debug level.
     *
     * @param i The debug level
     */
    public void setDebug(int i)
    {
        this.debug=i;
    }

    /**
     * Returns the discovered SPIs name.
     *
     * @return The discovered SPIs name
     */
    public String[] getServiceInfo()
    {
        return drivers;
    }

    /**
     * Executes the Apache Ant task, discovering the set service name
     *
     * @throws Exception if any error occurs
     */
    public void execute() throws Exception
    {
        System.out.printf("Discovering service '%s'...%n", name);

        DiscoverResources disc = new DiscoverResources();
        disc.addClassLoader( JDKHooks.getJDKHooks().getThreadContextClassLoader() );
        disc.addClassLoader( this.getClass().getClassLoader() );

        ResourceNameIterator iterator = disc.findResources(name);

        List<String> resources = new LinkedList<String>();
        while (iterator.hasNext())
        {
            String resourceInfo = iterator.nextResourceName();
            resources.add(resourceInfo);
            if (debug > 0)
            {
                System.out.printf("Found '%s'%n", resourceInfo);
            }
        }

        drivers = new String[resources.size()];
        resources.toArray(drivers);
    }

}
