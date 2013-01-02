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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.discovery.jdk.JDKHooks;

/**
 * There are many different contexts in which
 * loaders can be used.  This provides a holder
 * for a set of class loaders, so that they
 * don't have to be build back up everytime...
 */
public class ClassLoaders
{

    protected List<ClassLoader> classLoaders = new LinkedList<ClassLoader>();

    /**
     * Construct a new class loader set.
     */
    public ClassLoaders()
    {
    }

    /**
     * Returns the size of class loaders set.
     *
     * @return The size of class loaders set
     */
    public int size()
    {
        return classLoaders.size();
    }

    /**
     * Returns the class loader positioned at the given index.
     *
     * @param idx The index the class loader has to be retrieved from
     * @return The class loader positioned at the given index
     */
    public ClassLoader get(int idx)
    {
        return classLoaders.get(idx);
    }

    /**
     * Specify a new class loader to be used in searching.
     *
     * The order of loaders determines the order of the result.
     * It is recommended to add the most specific loaders first;
     * {@code null} class loaders are discarded.
     *
     * @param classLoader The class loader has to added in the set
     */
    public void put(ClassLoader classLoader)
    {
        if (classLoader != null)
        {
            classLoaders.add(classLoader);
        }
    }

    /**
     * Specify a new class loader to be used in searching.
     * The order of loaders determines the order of the result.
     * It is recommended to add the most specific loaders first;
     * {@code null} class loaders are discarded.
     *
     * @param classLoader  The class loader has to added in the set
     * @param prune if true, verify that the class loader is
     *              not an Ancestor (@see isAncestor) before
     *              adding it to our list.
     */
    public void put(ClassLoader classLoader, boolean prune)
    {
        if (classLoader != null  &&  !(prune && isAncestor(classLoader)))
        {
            classLoaders.add(classLoader);
        }
    }

    /**
     * Check to see if <code>classLoader</code> is an
     * ancestor of any contained class loader.
     *
     * This can be used to eliminate redundant class loaders
     * IF all class loaders defer to parent class loaders
     * before resolving a class.
     *
     * It may be that this is not always true.  Therefore,
     * this check is not done internally to eliminate
     * redundant class loaders, but left to the discretion
     * of the user.
     *
     * @param classLoader The class loader under test
     * @return true, if the class loader under test is an ancestor
     *         of any contained class loader, false otherwise
     */
    public boolean isAncestor(final ClassLoader classLoader)
    {
        /* bootstrap classloader, at root of all trees! */
        if (classLoader == null)
        {
            return true;
        }

        for (int idx = 0; idx < size(); idx++)
        {
            for (ClassLoader walker = get(idx);
                    walker != null;
                    walker = walker.getParent())
            {
                if (walker == classLoader)
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Utility method.  Returns a preloaded ClassLoaders instance
     * containing the following class loaders, in order:
     *
     * <ul>
     *   <li>spi.getClassLoader</li>
     *   <li>seeker.getClassLoader</li>
     *   <li>System Class Loader</li>
     * </ul>
     *
     * Note that the thread context class loader is NOT present.
     * This is a reasonable set of loaders to try if the resource to be found
     * should be restricted to a libraries containing the SPI and Factory.
     *
     * @param spi WHAT is being looked for (an implementation of this class,
     *            a default property file related to this class).
     * @param factory WHO is performing the lookup.
     * @param prune Determines if ancestors are allowed to be loaded or not.
     * @return The class loaders holder
     */
    public static ClassLoaders getLibLoaders(Class<?> spi, Class<?> factory, boolean prune)
    {
        ClassLoaders loaders = new ClassLoaders();

        if (spi != null)
        {
            loaders.put(spi.getClassLoader());
        }
        if (factory != null)
        {
            loaders.put(factory.getClassLoader(), prune);
        }
        loaders.put(JDKHooks.getJDKHooks().getSystemClassLoader(), prune);

        return loaders;
    }

    /**
     * Utility method.  Returns a preloaded ClassLoaders instance
     * containing the following class loaders, in order:
     *
     * <ul>
     *   <li>Thread Context Class Loader</li>
     *   <li>spi.getClassLoader</li>
     *   <li>seeker.getClassLoader</li>
     *   <li>System Class Loader</li>
     * </ul>
     *
     * Note that the thread context class loader IS  present.
     * This is a reasonable set of loaders to try if the resource to be found
     * may be provided by an application.
     *
     * @param spi WHAT is being looked for (an implementation of this class,
     *            a default property file related to this class).
     * @param factory WHO is performing the lookup (factory).
     * @param prune Determines if ancestors are allowed to be loaded or not.
     * @return The class loaders holder
     */
    public static ClassLoaders getAppLoaders(Class<?> spi, Class<?> factory, boolean prune)
    {
        ClassLoaders loaders = new ClassLoaders();

        loaders.put(JDKHooks.getJDKHooks().getThreadContextClassLoader());
        if (spi != null)
        {
            loaders.put(spi.getClassLoader(), prune);
        }
        if (factory != null)
        {
            loaders.put(factory.getClassLoader(), prune);
        }
        loaders.put(JDKHooks.getJDKHooks().getSystemClassLoader(), prune);

        return loaders;
    }

}
