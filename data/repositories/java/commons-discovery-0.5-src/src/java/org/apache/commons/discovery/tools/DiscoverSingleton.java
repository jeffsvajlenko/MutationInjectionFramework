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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.discovery.DiscoveryException;
import org.apache.commons.discovery.jdk.JDKHooks;
import org.apache.commons.discovery.resource.ClassLoaders;

/**
 * <p>Discover singleton service providers.
 * This
 * </p>
 *
 * <p>DiscoverSingleton instances are cached by the Discovery service,
 * keyed by a combination of
 * <ul>
 *   <li>thread context class loader,</li>
 *   <li>groupContext, and</li>
 *   <li>SPI.</li>
 * </ul>
 * This DOES allow multiple instances of a given <i>singleton</i> class
 * to exist for different class loaders and different group contexts.
 * </p>
 *
 * <p>In the context of this package, a service interface is defined by a
 * Service Provider Interface (SPI).  The SPI is expressed as a Java interface,
 * abstract class, or (base) class that defines an expected programming
 * interface.
 * </p>
 *
 * <p>DiscoverSingleton provides the <code>find</code> methods for locating and
 * instantiating a singleton instance of an implementation of a service (SPI).
 * Each form of <code>find</code> varies slightly, but they all perform the
 * same basic function.
 *
 * The simplest <code>find</code> methods are intended for direct use by
 * components looking for a service.  If you are not sure which finder(s)
 * to use, you can narrow your search to one of these:
 * <ul>
 * <li>static &lt;T&gt; T find(Class&lt;T&gt; spi);</li>
 * <li>static &lt;T&gt; T find(Class&lt;T&gt; spi, Properties properties);</li>
 * <li>static &lt;T&gt; T find(Class&lt;T&gt; spi, String defaultImpl);</li>
 * <li>static &lt;T&gt; T find(Class&lt;T&gt; spi,
 *                        Properties properties, String defaultImpl);</li>
 * <li>static &lt;T&gt; T find(Class&lt;T&gt; spi,
 *                        String propertiesFileName, String defaultImpl);</li>
 * <li>static &lt;T&gt; T find(ClassLoaders loaders, SPInterface&lt;T&gt; spi,
 *                        PropertiesHolder holder, DefaultClassHolder&lt;T&gt; holder);</li>
 * </ul>
 *
 * The <code>DiscoverSingleton.find</code> methods proceed as follows:
 * </p>
 * <ul>
 *   <p><li>
 *   Examine an internal cache to determine if the desired service was
 *   previously identified and instantiated.  If found in cache, return it.
 *   </li></p>
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
 *   implementation class (<code>defaultImpl</code>) is null,
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
 *   to the SPI, DiscoverSingleton, and System class loaders.  The assumption
 *   here is that the default implementation is closely associated with the SPI
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
 *   <p><li>
 *   Create an instance of the class.
 *   </li></p>
 * </ul>
 *
 * <p>
 * Variances for various forms of the <code>find</code>
 * methods are discussed with each such method.
 * Variances include the following concepts:
 * <ul>
 *   <li><b>rootFinderClass</b> - a wrapper encapsulating a finder method
 *   (factory or other helper class).  The root finder class is used to
 *   determine the 'real' caller, and hence the caller's class loader -
 *   thereby preserving knowledge that is relevant to finding the
 *   correct/expected implementation class.
 *   </li>
 *   <li><b>propertiesFileName</b> - <code>Properties</code> may be specified
 *   directly, or by property file name.  A property file is loaded using the
 *   same sequence of class loaders used to load the SPI implementation:
 *   <ul>
 *     <li>Thread Context Class Loader</li>
 *     <li>DiscoverSingleton's Caller's Class Loader</li>
 *     <li>SPI's Class Loader</li>
 *     <li>DiscoverSingleton's (this class) Class Loader</li>
 *     <li>System Class Loader</li>
 *   </ul>
 *   </li>
 *   <li><b>groupContext</b> - differentiates service providers for different
 *   logical groups of service users, that might otherwise be forced to share
 *   a common service and, more importantly, a common configuration of that
 *   service.
 *   <p>The groupContext is used to qualify the name of the property file
 *   name: <code>groupContext + '.' + propertiesFileName</code>.  If that
 *   file is not found, then the unqualified propertyFileName is used.
 *   </p>
 *   <p>In addition, groupContext is used to qualify the name of the system
 *   property used to find the service implementation by prepending the value
 *   of <code>groupContext</code> to the property name:
 *   <code>groupContext&gt; + '.' + SPI.class.getName()</code>.
 *   Again, if a system property cannot be found by that name, then the
 *   unqualified property name is used.
 *   </p>
 *   </li>
 * </ul>
 * </p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - This implementation is modelled
 * after the SAXParserFactory and DocumentBuilderFactory implementations
 * (corresponding to the JAXP pluggability APIs) found in Apache Xerces.
 * </p>
 *
 * @version $Revision: 1089242 $ $Date: 2011-04-05 23:33:21 +0200 (Tue, 05 Apr 2011) $
 */
public class DiscoverSingleton
{

    /********************** (RELATIVELY) SIMPLE FINDERS **********************
     *
     * These finders are suitable for direct use in components looking for a
     * service.  If you are not sure which finder(s) to use, you can narrow
     * your search to one of these.
     */

    /**
     * Find implementation of SPI.
     *
     * @param <T>  Service Provider Interface type.
     * @param spiClass Service Provider Interface Class.
     *
     * @return Instance of a class implementing the SPI.
     *
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     */
    public static <T> T find(Class<T> spiClass) throws DiscoveryException
    {
        return find(null,
                    new SPInterface<T>(spiClass),
                    DiscoverClass.nullProperties,
                    (DefaultClassHolder<T>) null);
    }

    /**
     * Find implementation of SPI.
     *
     * @param <T> Service Provider Interface type
     *
     * @param spiClass Service Provider Interface Class.
     *
     * @param properties Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     *
     * @return Instance of a class implementing the SPI.
     *
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     */
    public static <T> T find(Class<T> spiClass, Properties properties) throws DiscoveryException
    {
        return find(null,
                    new SPInterface<T>(spiClass),
                    new PropertiesHolder(properties),
                    (DefaultClassHolder<T>) null);
    }

    /**
     * Find implementation of SPI.
     *
     * @param <T> Service Provider Interface type
     *
     * @param spiClass Service Provider Interface Class.
     *
     * @param defaultImpl Default implementation.
     *
     * @return Instance of a class implementing the SPI.
     *
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     */
    public static <T> T find(Class<T> spiClass, String defaultImpl) throws DiscoveryException
    {
        return find(null,
                    new SPInterface<T>(spiClass),
                    DiscoverClass.nullProperties,
                    new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Find implementation of SPI.
     *
     * @param <T> Service Provider Interface type
     *
     * @param spiClass Service Provider Interface Class.
     *
     * @param properties Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     *
     * @param defaultImpl Default implementation.
     *
     * @return Instance of a class implementing the SPI.
     *
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     */
    public static <T> T find(Class<T> spiClass,
                             Properties properties,
                             String defaultImpl) throws DiscoveryException
    {
        return find(null,
                    new SPInterface<T>(spiClass),
                    new PropertiesHolder(properties),
                    new DefaultClassHolder<T>(defaultImpl));
    }

    /**
     * Find implementation of SPI.
     *
     * @param <T> Service Provider Interface type
     *
     * @param spiClass Service Provider Interface Class.
     *
     * @param propertiesFileName Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     *
     * @param defaultImpl Default implementation.
     *
     * @return Instance of a class implementing the SPI.
     *
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     */
    public static <T> T find(Class<T> spiClass,
                             String propertiesFileName,
                             String defaultImpl) throws DiscoveryException
    {
        return find(null,
                    new SPInterface<T>(spiClass),
                    new PropertiesHolder(propertiesFileName),
                    new DefaultClassHolder<T>(defaultImpl));
    }

    /*************** FINDERS FOR USE IN FACTORY/HELPER METHODS ***************
     */

    /**
     * Find implementation of SPI.
     *
     * @param <T> Service Provider Interface type
     *
     * @param loaders The {@code ClassLoader} holder
     *
     * @param spi Service Provider Interface Class.
     *
     * @param properties Used to determine name of SPI implementation,
     *                   and passed to implementation.init() method if
     *                   implementation implements Service interface.
     *
     * @param defaultImpl Default implementation.
     *
     * @return Instance of a class implementing the SPI.
     *
     * @exception DiscoveryException Thrown if the name of a class implementing
     *            the SPI cannot be found, if the class cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI.
     */
    public static <T> T find(ClassLoaders loaders,
                             SPInterface<T> spi,
                             PropertiesHolder properties,
                             DefaultClassHolder<T> defaultImpl) throws DiscoveryException
    {
        ClassLoader contextLoader = JDKHooks.getJDKHooks().getThreadContextClassLoader();

        @SuppressWarnings("unchecked") // spiName is assignable from stored object class
        T obj = (T) get(contextLoader, spi.getSPName());

        if (obj == null)
        {
            try
            {
                obj = DiscoverClass.newInstance(loaders, spi, properties, defaultImpl);

                if (obj != null)
                {
                    put(contextLoader, spi.getSPName(), obj);
                }
            }
            catch (DiscoveryException de)
            {
                throw de;
            }
            catch (Exception e)
            {
                throw new DiscoveryException("Unable to instantiate implementation class for " + spi.getSPName(), e);
            }
        }

        return obj;
    }

    /********************** CACHE-MANAGEMENT SUPPORT **********************/

    /**
     * Release all internal references to previously created service
     * instances associated with the current thread context class loader.
     * The <code>release()</code> method is called for service instances that
     * implement the <code>Service</code> interface.
     *
     * This is useful in environments like servlet containers,
     * which implement application reloading by throwing away a ClassLoader.
     * Dangling references to objects in that class loader would prevent
     * garbage collection.
     */
    public static synchronized void release()
    {
        EnvironmentCache.release();
    }

    /**
     * Release any internal references to a previously created service
     * instance associated with the current thread context class loader.
     * If the SPI instance implements <code>Service</code>, then call
     * <code>release()</code>.
     *
     * @param spiClass The previously created service
     */
    public static synchronized void release(Class<?> spiClass)
    {
        Map<String, Object> spis = EnvironmentCache.get(JDKHooks.getJDKHooks().getThreadContextClassLoader());

        if (spis != null)
        {
            spis.remove(spiClass.getName());
        }
    }

    /************************* SPI CACHE SUPPORT *************************
     *
     * Cache services by a 'key' unique to the requesting class/environment:
     *
     * When we 'release', it is expected that the caller of the 'release'
     * have the same thread context class loader... as that will be used
     * to identify all cached entries to be released.
     *
     * We will manage synchronization directly, so all caches are implemented
     * as HashMap (unsynchronized).
     *
     * - ClassLoader::groupContext::SPI::Instance Cache
     *         Cache : HashMap
     *         Key   : Thread Context Class Loader (<code>ClassLoader</code>).
     *         Value : groupContext::SPI Cache (<code>HashMap</code>).
     *
     * - groupContext::SPI::Instance Cache
     *         Cache : HashMap
     *         Key   : groupContext (<code>String</code>).
     *         Value : SPI Cache (<code>HashMap</code>).
     *
     * - SPI::Instance Cache
     *         Cache : HashMap
     *         Key   : SPI Class Name (<code>String</code>).
     *         Value : SPI Instance/Implementation (<code>Object</code>.
     */

    /**
     * Implements first two levels of the cache (loader & groupContext).
     * Allows null keys, important as default groupContext is null.
     */

    /**
     * Get service keyed by spi & classLoader.
     *
     * @param classLoader The class loader as key to retrieve the related cache
     * @param spiName The SPI class name
     * @return The object instance associated to the given class loader/SPI name
     */
    private static synchronized Object get(ClassLoader classLoader,
                                           String spiName)
    {
        Map<String, Object> spis = EnvironmentCache.get(classLoader);

        if (spis != null)
        {
            return spis.get(spiName);
        }
        return null;
    }

    /**
     * Put service keyed by spi & classLoader.
     *
     * @param classLoader The {@link EnvironmentCache} key
     * @param spiName The SPI class name
     * @param service The SPI object reference
     */
    private static synchronized void put(ClassLoader classLoader,
                                         String spiName,
                                         Object service)
    {
        if (service != null)
        {
            Map<String, Object> spis = EnvironmentCache.get(classLoader);

            if (spis == null)
            {
                spis = new HashMap<String, Object>(EnvironmentCache.smallHashSize);
                EnvironmentCache.put(classLoader, spis);
            }

            spis.put(spiName, service);
        }
    }

}
