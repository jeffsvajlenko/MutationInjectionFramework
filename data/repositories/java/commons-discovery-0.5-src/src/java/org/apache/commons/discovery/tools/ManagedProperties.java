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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.discovery.jdk.JDKHooks;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>This class may disappear in the future, or be moved to another project..
 * </p>
 *
 * <p>Extend the concept of System properties to a hierarchical scheme
 * based around class loaders.  System properties are global in nature,
 * so using them easily violates sound architectural and design principles
 * for maintaining separation between components and runtime environments.
 * Nevertheless, there is a need for properties broader in scope than
 * class or class instance scope.
 * </p>
 *
 * <p>This class is one solution.
 * </p>
 *
 * <p>Manage properties according to a secure
 * scheme similar to that used by classloaders:
 * <ul>
 *   <li><code>ClassLoader</code>s are organized in a tree hierarchy.</li>
 *   <li>each <code>ClassLoader</code> has a reference
 *       to a parent <code>ClassLoader</code>.</li>
 *   <li>the root of the tree is the bootstrap <code>ClassLoader</code>er.</li>
 *   <li>the youngest decendent is the thread context class loader.</li>
 *   <li>properties are bound to a <code>ClassLoader</code> instance
 *   <ul>
 *     <li><i>non-default</i> properties bound to a parent <code>ClassLoader</code>
 *         instance take precedence over all properties of the same name bound
 *         to any decendent.
 *         Just to confuse the issue, this is the default case.</li>
 *     <li><i>default</i> properties bound to a parent <code>ClassLoader</code>
 *         instance may be overriden by (default or non-default) properties of
 *         the same name bound to any decendent.
 *         </li>
 *   </ul>
 *   </li>
 *   <li>System properties take precedence over all other properties</li>
 * </ul>
 * </p>
 *
 * <p>This is not a perfect solution, as it is possible that
 * different <code>ClassLoader</code>s load different instances of
 * <code>ScopedProperties</code>.  The 'higher' this class is loaded
 * within the <code>ClassLoader</code> hierarchy, the more usefull
 * it will be.
 * </p>
 */
public class ManagedProperties
{

    private static Log log = LogFactory.getLog(ManagedProperties.class);

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
     * Cache of Properties, keyed by (thread-context) class loaders.
     * Use <code>HashMap</code> because it allows 'null' keys, which
     * allows us to account for the (null) bootstrap classloader.
     */
    private static final Map<ClassLoader, Map<String, Value>> propertiesCache =
        new HashMap<ClassLoader, Map<String, Value>>();

    /**
     * Get value for property bound to the current thread context class loader.
     *
     * @param propertyName property name.
     * @return property value if found, otherwise default.
     */
    public static String getProperty(String propertyName)
    {
        return getProperty(getThreadContextClassLoader(), propertyName);
    }

    /**
     * Get value for property bound to the current thread context class loader.
     * If not found, then return default.
     *
     * @param propertyName property name.
     * @param dephault default value.
     * @return property value if found, otherwise default.
     */
    public static String getProperty(String propertyName, String dephault)
    {
        return getProperty(getThreadContextClassLoader(), propertyName, dephault);
    }

    /**
     * Get value for property bound to the class loader.
     *
     * @param classLoader The classloader used to load resources.
     * @param propertyName property name.
     * @return property value if found, otherwise default.
     */
    public static String getProperty(ClassLoader classLoader, String propertyName)
    {
        String value = JDKHooks.getJDKHooks().getSystemProperty(propertyName);
        if (value == null)
        {
            Value val = getValueProperty(classLoader, propertyName);
            if (val != null)
            {
                value = val.value;
            }
        }
        else if (log.isDebugEnabled())
        {
            log.debug("found System property '" + propertyName + "'" +
                      " with value '" + value + "'.");
        }
        return value;
    }

    /**
     * Get value for property bound to the class loader.
     * If not found, then return default.
     *
     * @param classLoader The classloader used to load resources.
     * @param propertyName property name.
     * @param dephault default value.
     * @return property value if found, otherwise default.
     */
    public static String getProperty(ClassLoader classLoader, String propertyName, String dephault)
    {
        String value = getProperty(classLoader, propertyName);
        return (value == null) ? dephault : value;
    }

    /**
     * Set value for property bound to the current thread context class loader.
     * @param propertyName property name
     * @param value property value (non-default)  If null, remove the property.
     */
    public static void setProperty(String propertyName, String value)
    {
        setProperty(propertyName, value, false);
    }

    /**
     * Set value for property bound to the current thread context class loader.
     * @param propertyName property name
     * @param value property value.  If null, remove the property.
     * @param isDefault determines if property is default or not.
     *        A non-default property cannot be overriden.
     *        A default property can be overriden by a property
     *        (default or non-default) of the same name bound to
     *        a decendent class loader.
     */
    public static void setProperty(String propertyName, String value, boolean isDefault)
    {
        if (propertyName != null)
        {
            synchronized (propertiesCache)
            {
                ClassLoader classLoader = getThreadContextClassLoader();
                Map<String, Value> properties = propertiesCache.get(classLoader);

                if (value == null)
                {
                    if (properties != null)
                    {
                        properties.remove(propertyName);
                    }
                }
                else
                {
                    if (properties == null)
                    {
                        properties = new HashMap<String, Value>();
                        propertiesCache.put(classLoader, properties);
                    }

                    properties.put(propertyName, new Value(value, isDefault));
                }
            }
        }
    }

    /**
     * Set property values for <code>Properties</code> bound to the
     * current thread context class loader.
     *
     * @param newProperties name/value pairs to be bound
     */
    public static void setProperties(Map<?, ?> newProperties)
    {
        setProperties(newProperties, false);
    }

    /**
     * Set property values for <code>Properties</code> bound to the
     * current thread context class loader.
     *
     * @param newProperties name/value pairs to be bound
     * @param isDefault determines if properties are default or not.
     *        A non-default property cannot be overriden.
     *        A default property can be overriden by a property
     *        (default or non-default) of the same name bound to
     *        a decendent class loader.
     */
    public static void setProperties(Map<?, ?> newProperties, boolean isDefault)
    {
        /**
         * Each entry must be mapped to a Property.
         * 'setProperty' does this for us.
         */
        for (Map.Entry<?, ?> entry : newProperties.entrySet())
        {
            setProperty( String.valueOf(entry.getKey()),
                         String.valueOf(entry.getValue()),
                         isDefault);
        }
    }

    /**
     * Return list of all property names.  This is an expensive
     * operation: ON EACH CALL it walks through all property lists
     * associated with the current context class loader upto
     * and including the bootstrap class loader.
     *
     * @return The list of all property names
     */
    public static Enumeration<String> propertyNames()
    {
        Map<String, Value> allProps = new Hashtable<String, Value>();

        ClassLoader classLoader = getThreadContextClassLoader();

        /**
         * Order doesn't matter, we are only going to use
         * the set of all keys...
         */
        while (true)
        {
            Map<String, Value> properties = null;

            synchronized (propertiesCache)
            {
                properties = propertiesCache.get(classLoader);
            }

            if (properties != null)
            {
                allProps.putAll(properties);
            }

            if (classLoader == null)
            {
                break;
            }

            classLoader = getParent(classLoader);
        }

        return Collections.enumeration(allProps.keySet());
    }

    /**
     * This is an expensive operation.
     * ON EACH CALL it walks through all property lists
     * associated with the current context class loader upto
     * and including the bootstrap class loader.
     *
     * @return Returns a <code>java.util.Properties</code> instance
     * that is equivalent to the current state of the scoped
     * properties, in that getProperty() will return the same value.
     * However, this is a copy, so setProperty on the
     * returned value will not effect the scoped properties.
     */
    public static Properties getProperties()
    {
        Properties p = new Properties();

        Enumeration<String> names = propertyNames();
        while (names.hasMoreElements())
        {
            String name = names.nextElement();
            p.put(name, getProperty(name));
        }

        return p;
    }

    /***************** INTERNAL IMPLEMENTATION *****************/

    private static class Value
    {
        final String value;
        final boolean isDefault;

        /**
         * Creates a new Value instance with string value and
         * the flag to mark is default value or not.
         *
         * @param value String representation of this value
         * @param isDefault The default flag
         */
        Value(String value, boolean isDefault)
        {
            this.value = value;
            this.isDefault = isDefault;
        }
    }

    /**
     * Get value for properties bound to the class loader.
     * Explore up the tree first, as higher-level class
     * loaders take precedence over lower-level class loaders.
     *
     *
     * @param classLoader The class loader as key
     * @param propertyName The property name to lookup
     * @return The Value associated to the input class loader and property name
     */
    private static final Value getValueProperty(ClassLoader classLoader, String propertyName)
    {
        Value value = null;

        if (propertyName != null)
        {
            /**
             * If classLoader isn't bootstrap loader (==null),
             * then get up-tree value.
             */
            if (classLoader != null)
            {
                value = getValueProperty(getParent(classLoader), propertyName);
            }

            if (value == null  ||  value.isDefault)
            {
                synchronized (propertiesCache)
                {
                    Map<String, Value> properties = propertiesCache.get(classLoader);

                    if (properties != null)
                    {
                        Value altValue = properties.get(propertyName);

                        // set value only if override exists..
                        // otherwise pass default (or null) on..
                        if (altValue != null)
                        {
                            value = altValue;

                            if (log.isDebugEnabled())
                            {
                                log.debug("found Managed property '" + propertyName + "'" +
                                          " with value '" + value + "'" +
                                          " bound to classloader " + classLoader + ".");
                            }
                        }
                    }
                }
            }
        }

        return value;
    }

    /**
     * Returns the thread context class loader.
     *
     * @return The thread context class loader
     */
    private static final ClassLoader getThreadContextClassLoader()
    {
        return JDKHooks.getJDKHooks().getThreadContextClassLoader();
    }

    /**
     * Return the parent class loader of the given class loader.
     *
     * @param classLoader The class loader from wich the parent has to be extracted
     * @return The parent class loader of the given class loader
     */
    private static final ClassLoader getParent(final ClassLoader classLoader)
    {
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
        {
            public ClassLoader run()
            {
                try
                {
                    return classLoader.getParent();
                }
                catch (SecurityException se)
                {
                    return null;
                }
            }
        });
    }

}
