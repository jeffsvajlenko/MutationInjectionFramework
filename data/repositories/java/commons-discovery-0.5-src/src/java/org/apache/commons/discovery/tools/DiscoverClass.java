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

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.discovery.DiscoveryException;
import org.apache.commons.discovery.ResourceClass;
import org.apache.commons.discovery.ResourceClassIterator;
import org.apache.commons.discovery.ResourceNameIterator;
import org.apache.commons.discovery.resource.ClassLoaders;
import org.apache.commons.discovery.resource.classes.DiscoverClasses;
import org.apache.commons.discovery.resource.names.DiscoverServiceNames;

/**
 * <p>Discover class that implements a given service interface,
 * with discovery and configuration features similar to that employed
 * by standard Java APIs such as JAXP.
 * </p>
 *
 * <p>In the context of this package, a service interface is defined by a
 * Service Provider Interface (SPI).  The SPI is expressed as a Java interface,
 * abstract class, or (base) class that defines an expected programming
 * interface.
 * </p>
 *
 * <p>DiscoverClass provides the <code>find</code> methods for locating a
 * class that implements a service interface (SPI).  Each form of
 * <code>find</code> varies slightly, but they all perform the same basic
 * function.
 *
 * The <code>DiscoverClass.find</code> methods proceed as follows:
 * </p>
 * <ul>
 *   <p><li>
 *   Get the name of an implementation class.  The name is the first
 *   non-null value obtained from the following resources:
 *   <ul>
 *     <li>
 *     The value of the (scoped) system property whose name is the same as
 *     the SPI's fully qualified class name (as given by SPI.class.getName()).
 *     The <code>ScopedProperties</code> class provides a way to bind
 *     properties by classloader, in a secure hierarchy similar in concept
 *     to the way classloader find class and resource files.
 *     See <code>ScopedProperties</code> for more details.
 *     <p>If the ScopedProperties are not set by users, then behaviour
 *     is equivalent to <code>System.getProperty()</code>.
 *     </p>
 *     </li>
 *     <p><li>
 *     The value of a <code>Properties properties</code> property, if provided
 *     as a parameter, whose name is the same as the SPI's fully qualifed class
 *     name (as given by SPI.class.getName()).
 *     </li></p>
 *     <p><li>
 *     The value obtained using the JDK1.3+ 'Service Provider' specification
 *     (http://java.sun.com/j2se/1.3/docs/guide/jar/jar.html) to locate a
 *     service named <code>SPI.class.getName()</code>.  This is implemented
 *     internally, so there is not a dependency on JDK 1.3+.
 *     </li></p>
 *   </ul>
 *   </li></p>
 *   <p><li>
 *   If the name of the implementation class is non-null, load that class.
 *   The class loaded is the first class loaded by the following sequence
 *   of class loaders:
 *   <ul>
 *     <li>Thread Context Class Loader</li>
 *     <li>DiscoverSingleton's Caller's Class Loader</li>
 *     <li>SPI's Class Loader</li>
 *     <li>DiscoverSingleton's (this class or wrapper) Class Loader</li>
 *     <li>System Class Loader</li>
 *   </ul>
 *   An exception is thrown if the class cannot be loaded.
 *   </li></p>
 *   <p><li>
 *   If the name of the implementation class is null, AND the default
 *   implementation class name (<code>defaultImpl</code>) is null,
 *   then an exception is thrown.
 *   </li></p>
 *   <p><li>
 *   If the name of the implementation class is null, AND the default
 *   implementation class (<code>defaultImpl</code>) is non-null,
 *   then load the default implementation class.  The class loaded is the
 *   first class loaded by the following sequence of class loaders:
 *   <ul>
 *     <li>SPI's Class Loader</li>
 *     <li>DiscoverSingleton's (this class or wrapper) Class Loader</li>
 *     <li>System Class Loader</li>
 *   </ul>
 *   <p>
 *   This limits the scope in which the default class loader can be found
 *   to the SPI, DiscoverSingleton, and System class loaders.  The assumption here
 *   is that the default implementation is closely associated with the SPI
 *   or system, and is not defined in the user's application space.
 *   </p>
 *   <p>
 *   An exception is thrown if the class cannot be loaded.
 *   </p>
 *   </li></p>
 *   <p><li>
 *   Verify that the loaded class implements the SPI: an exception is thrown
 *   if the loaded class does not implement the SPI.
 *   </li></p>
 * </ul>
 * </p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - This implementation is modelled
 * after the SAXParserFactory and DocumentBuilderFactory implementations
 * (corresponding to the JAXP pluggability APIs) found in Apache Xerces.
 * </p>
 *
 * @version $Revision: 1090010 $ $Date: 2011-04-07 23:05:58 +0200 (Thu, 07 Apr 2011) $
 */
public class DiscoverClass
{

    /**
     * Readable placeholder for a null value.
     */
    public static final PropertiesHolder nullProperties = null;

    /**
     * The class loaders holder.
     */
    private final ClassLoaders classLoaders;

    /**
     * Create a class instance with dynamic environment
     * (thread context class loader is determined on each call).
     *
     * Dynamically construct class loaders on each call.
     */
    public DiscoverClass()
    {
        this(null);
    }

    /**
     * Create a class instance with dynamic environment
     * (thread context class loader is determined on each call).
     *
     * Cache static list of class loaders for each call.
     *
     * @param classLoaders The class loaders holder
     */
    public DiscoverClass(ClassLoaders classLoaders)
    {
        this.classLoaders = classLoaders;
    }

    /**
     * Return the class loaders holder for the given SPI.
     *
     * @param spiClass The SPI type
     * @return The class loaders holder for the given SPI
     */
    public ClassLoaders getClassLoaders(@SuppressWarnings("unused") Class<?> spiClass)
    {
        return classLoaders;
    }

    /**
     * Find class implementing SPI.
     *
     * @param <T> The SPI type
     * @param <S> Any class extending T
     * @param spiClass Service Provider Interface Class.
     * @return Class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded, or if
     *            the resulting class does not implement (or extend) the SPI.
     */
    public <T, S extends T> Class<S> find(Class<T> spiClass) throws DiscoveryException
    {
        return find(getClassLoaders(spiClass),
                    new SPInterface<T>(spiClass),
                    nullProperties,
                    (DefaultClassHolder<T>) null);
    }

    /**
     * Find class implementing SPI.
     *
     * @param <T> The SPI type
     * @param <S> Any class extending T
     * @param spiClass Service Provider Interface Class.
     * @param properties Used to determine name of SPI implementation.
     * @return Class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded, or if
     *            the resulting class does not implement (or extend) the SPI.
     */
    public <T, S extends T> Class<S> find(Class<T> spiClass, Properties properties) throws DiscoveryException
    {
        return find(getClassLoaders(spiClass),
                    new SPInterface<T>(spiClass),
                    new PropertiesHolder(properties),
                    (DefaultClassHolder<T>) null);
    }

    /**
     * Find class implementing SPI.
     *
     * @param <T> The SPI type
     * @param <S> Any class extending T
     * @param spiClass Service Provider Interface Class.
     * @param defaultImpl Default implementation name.
     * @return Class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded, or if
     *            the resulting class does not implement (or extend) the SPI.
     */
    public <T, S extends T> Class<S> find(Class<T> spiClass, String defaultImpl) throws DiscoveryException
    {
        return find(getClassLoaders(spiClass),
                    new SPInterface<T>(spiClass),
                    nullProperties,
                    new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Find class implementing SPI.
     *
     * @param <T> The SPI type
     * @param <S> Any class extending T
     * @param spiClass Service Provider Interface Class.
     * @param properties Used to determine name of SPI implementation,.
     * @param defaultImpl Default implementation class.
     * @return Class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded, or if
     *            the resulting class does not implement (or extend) the SPI.
     */
    public <T, S extends T> Class<S> find(Class<T> spiClass, Properties properties, String defaultImpl)
    throws DiscoveryException
    {
        return find(getClassLoaders(spiClass),
                    new SPInterface<T>(spiClass),
                    new PropertiesHolder(properties),
                    new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Find class implementing SPI.
     *
     * @param <T> The SPI type
     * @param <S> Any class extending T
     * @param spiClass Service Provider Interface Class.
     * @param propertiesFileName Used to determine name of SPI implementation,.
     * @param defaultImpl Default implementation class.
     * @return Class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded, or if
     *            the resulting class does not implement (or extend) the SPI.
     */
    public <T, S extends T> Class<S> find(Class<T> spiClass, String propertiesFileName, String defaultImpl)
    throws DiscoveryException
    {
        return find(getClassLoaders(spiClass),
                    new SPInterface<T>(spiClass),
                    new PropertiesHolder(propertiesFileName),
                    new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Find class implementing SPI.
     *
     * @param <T> The SPI type
     * @param <S> Any class extending T
     * @param loaders The class loaders holder
     * @param spi Service Provider Interface Class.
     * @param properties Used to determine name of SPI implementation,.
     * @param defaultImpl Default implementation class.
     * @return Class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded, or if
     *            the resulting class does not implement (or extend) the SPI.
     */
    public static <T, S extends T> Class<S> find(ClassLoaders loaders,
            SPInterface<T> spi,
            PropertiesHolder properties,
            DefaultClassHolder<T> defaultImpl) throws DiscoveryException
    {
        if (loaders == null)
        {
            loaders = ClassLoaders.getLibLoaders(spi.getSPClass(),
                                                 DiscoverClass.class,
                                                 true);
        }

        Properties props = (properties == null)
                           ? null
                           : properties.getProperties(spi, loaders);

        String[] classNames = discoverClassNames(spi, props);
        Exception error = null;

        if (classNames.length > 0)
        {
            DiscoverClasses<T> classDiscovery = new DiscoverClasses<T>(loaders);

            for (String className : classNames)
            {
                ResourceClassIterator<T> classes =
                    classDiscovery.findResourceClasses(className);

                // If it's set as a property.. it had better be there!
                if (classes.hasNext())
                {
                    ResourceClass<T> info = classes.nextResourceClass();
                    try
                    {
                        return info.loadClass();
                    }
                    catch (Exception e)
                    {
                        error = e;
                    }
                }
            }
        }
        else
        {
            ResourceNameIterator classIter =
                (new DiscoverServiceNames(loaders)).findResourceNames(spi.getSPName());

            ResourceClassIterator<T> classes =
                (new DiscoverClasses<T>(loaders)).findResourceClasses(classIter);

            if (!classes.hasNext()  &&  defaultImpl != null)
            {
                return defaultImpl.getDefaultClass(spi, loaders);
            }

            // Services we iterate through until we find one that loads..
            while (classes.hasNext())
            {
                ResourceClass<T> info = classes.nextResourceClass();
                try
                {
                    return info.loadClass();
                }
                catch (Exception e)
                {
                    error = e;
                }
            }
        }

        throw new DiscoveryException("No implementation defined for " + spi.getSPName(), error);
        // return null;
    }

    /**
     * Create new instance of class implementing SPI.
     *
     * @param <T> The SPI type
     * @param spiClass Service Provider Interface Class.
     * @return Instance of a class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public <T> T newInstance(Class<T> spiClass)
    throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        return newInstance(getClassLoaders(spiClass),
                           new SPInterface<T>(spiClass),
                           nullProperties,
                           (DefaultClassHolder<T>) null);
    }

    /**
     * Create new instance of class implementing SPI.
     *
     * @param <T> The SPI type
     * @param spiClass Service Provider Interface Class.
     * @param properties Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     * @return Instance of a class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public <T> T newInstance(Class<T> spiClass, Properties properties) throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        return newInstance(getClassLoaders(spiClass),
                           new SPInterface<T>(spiClass),
                           new PropertiesHolder(properties),
                           (DefaultClassHolder<T>) null);
    }

    /**
     * Create new instance of class implementing SPI.
     *
     * @param <T> The SPI type
     * @param spiClass Service Provider Interface Class.
     * @param defaultImpl Default implementation.
     * @return Instance of a class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public <T> T newInstance(Class<T> spiClass, String defaultImpl) throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        return newInstance(getClassLoaders(spiClass),
                           new SPInterface<T>(spiClass),
                           nullProperties,
                           new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Create new instance of class implementing SPI.
     *
     * @param <T> The SPI type
     * @param spiClass Service Provider Interface Class.
     * @param properties Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     * @param defaultImpl Default implementation.
     * @return Instance of a class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public <T> T newInstance(Class<T> spiClass, Properties properties, String defaultImpl) throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        return newInstance(getClassLoaders(spiClass),
                           new SPInterface<T>(spiClass),
                           new PropertiesHolder(properties),
                           new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Create new instance of class implementing SPI.
     *
     * @param <T> The SPI type
     * @param spiClass Service Provider Interface Class.
     * @param propertiesFileName Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     * @param defaultImpl Default implementation.
     * @return Instance of a class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public <T> T newInstance(Class<T> spiClass, String propertiesFileName, String defaultImpl)
    throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        return newInstance(getClassLoaders(spiClass),
                           new SPInterface<T>(spiClass),
                           new PropertiesHolder(propertiesFileName),
                           new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Create new instance of class implementing SPI.
     *
     * @param <T> The SPI type
     * @param loaders The class loaders holder
     * @param spi Service Provider Interface Class.
     * @param properties Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     * @param defaultImpl Default implementation.
     * @return Instance of a class implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public static <T> T newInstance(ClassLoaders loaders,
                                    SPInterface<T> spi,
                                    PropertiesHolder properties,
                                    DefaultClassHolder<T> defaultImpl) throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        return spi.newInstance(find(loaders, spi, properties, defaultImpl));
    }

    /**
     * <p>Discover names of SPI implementation Classes from properties.
     * The names are the non-null values, in order, obtained from the following
     * resources:
     *   <ul>
     *     <li>ManagedProperty.getProperty(SPI.class.getName());</li>
     *     <li>properties.getProperty(SPI.class.getName());</li>
     *   </ul>
     *
     * @param <T> The SPI type
     * @param spi The SPI representation
     * @param properties Properties that may define the implementation
     *                   class name(s).
     * @return String[] Name of classes implementing the SPI.
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found.
     */
    public static <T> String[] discoverClassNames(SPInterface<T> spi,
            Properties properties)
    {
        List<String> names = new LinkedList<String>();

        String spiName = spi.getSPName();
        String propertyName = spi.getPropertyName();

        boolean includeAltProperty = !spiName.equals(propertyName);

        // Try the (managed) system property spiName
        String className = getManagedProperty(spiName);
        if (className != null)
        {
            names.add(className);
        }

        if (includeAltProperty)
        {
            // Try the (managed) system property propertyName
            className = getManagedProperty(propertyName);
            if (className != null)
            {
                names.add(className);
            }
        }

        if (properties != null)
        {
            // Try the properties parameter spiName
            className = properties.getProperty(spiName);
            if (className != null)
            {
                names.add(className);
            }

            if (includeAltProperty)
            {
                // Try the properties parameter propertyName
                className = properties.getProperty(propertyName);
                if (className != null)
                {
                    names.add(className);
                }
            }
        }

        String[] results = new String[names.size()];
        names.toArray(results);

        return results;
    }

    /**
     * Load the class whose name is given by the value of a (Managed)
     * System Property.
     *
     * @param propertyName the name of the system property whose value is
     *        the name of the class to load.
     * @return The managed property value
     * @see ManagedProperties
     */
    public static String getManagedProperty(String propertyName)
    {
        String value;
        try
        {
            value = ManagedProperties.getProperty(propertyName);
        }
        catch (SecurityException e)
        {
            value = null;
        }
        return value;
    }

}
