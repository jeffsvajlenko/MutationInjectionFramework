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
package org.apache.commons.discovery.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.discovery.DiscoveryException;
import org.apache.commons.discovery.Resource;
import org.apache.commons.discovery.ResourceIterator;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.DiscoverResources;

/**
 * Mechanisms to locate and load a class.
 *
 * The load methods locate a class only.
 * The find methods locate a class and verify that the
 * class implements an given interface or extends a given class.
 */
public class ResourceUtils
{

    /**
     * Get package name.
     *
     * Not all class loaders 'keep' package information,
     * in which case Class.getPackage() returns null.
     * This means that calling Class.getPackage().getName()
     * is unreliable at best.
     *
     * @param clazz The class from which the package has to be extracted
     * @return The string representation of the input class package
     */
    public static String getPackageName(Class<?> clazz)
    {
        Package clazzPackage = clazz.getPackage();
        String packageName;
        if (clazzPackage != null)
        {
            packageName = clazzPackage.getName();
        }
        else
        {
            String clazzName = clazz.getName();
            packageName = new String(clazzName.toCharArray(), 0, clazzName.lastIndexOf('.'));
        }
        return packageName;
    }

    /**
     * Load the resource <code>resourceName</code>.
     *
     * Try each classloader in succession,
     * until first succeeds, or all fail.
     * If all fail and <code>resouceName</code> is not absolute
     * (doesn't start with '/' character), then retry with
     * <code>packageName/resourceName</code> after changing all
     * '.' to '/'.
     *
     * @param spi The SPI type
     * @param resourceName The name of the resource to load.
     * @param loaders the class loaders holder
     * @return The discovered {@link Resource} instance
     * @throws DiscoveryException if the class implementing
     *            the SPI cannot be found, cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI
     */
    public static Resource getResource(Class<?> spi,
                                       String resourceName,
                                       ClassLoaders loaders) throws DiscoveryException
    {
        DiscoverResources explorer = new DiscoverResources(loaders);
        ResourceIterator resources = explorer.findResources(resourceName);

        if (spi != null  &&
                !resources.hasNext()  &&
                resourceName.charAt(0) != '/')
        {
            /**
             * If we didn't find the resource, and if the resourceName
             * isn't an 'absolute' path name, then qualify with
             * package name of the spi.
             */
            resourceName = getPackageName(spi).replace('.','/') + "/" + resourceName;
            resources = explorer.findResources(resourceName);
        }

        return resources.hasNext()
               ? resources.nextResource()
               : null;
    }

    /**
     * Load named property file, optionally qualified by spi's package name
     * as per Class.getResource.
     *
     * A property file is loaded using the following sequence of class loaders:
     *   <ul>
     *     <li>Thread Context Class Loader</li>
     *     <li>DiscoverSingleton's Caller's Class Loader</li>
     *     <li>SPI's Class Loader</li>
     *     <li>DiscoverSingleton's (this class) Class Loader</li>
     *     <li>System Class Loader</li>
     *   </ul>
     *
     * @param spi The SPI type
     * @param propertiesFileName The property file name.
     * @param classLoaders The class loaders holder
     * @return The loaded named property file, in {@code Properties} format
     * @throws DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     */
    public static Properties loadProperties(Class<?> spi,
                                            String propertiesFileName,
                                            ClassLoaders classLoaders) throws DiscoveryException
    {
        Properties properties = null;

        if (propertiesFileName != null)
        {
            try
            {
                Resource resource = getResource(spi, propertiesFileName, classLoaders);
                if (resource != null)
                {
                    InputStream stream = resource.getResourceAsStream();

                    if (stream != null)
                    {
                        properties = new Properties();
                        try
                        {
                            properties.load(stream);
                        }
                        finally
                        {
                            stream.close();
                        }
                    }
                }
            }
            catch (IOException e)
            {
                // ignore
            }
            catch (SecurityException e)
            {
                // ignore
            }
        }

        return properties;
    }

}
