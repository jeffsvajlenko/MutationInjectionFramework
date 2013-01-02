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
package org.apache.commons.discovery.jdk;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JDK 1.2 Style Hooks implementation.
 */
public class JDK12Hooks extends JDKHooks
{

    /**
     * Logger
     */
    private static Log log = LogFactory.getLog(JDK12Hooks.class);

    private static final ClassLoader systemClassLoader = findSystemClassLoader();

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
     * {@inheritDoc}
     */
    @Override
    public String getSystemProperty(final String propName)
    {
        return AccessController.doPrivileged(new PrivilegedAction<String>()
        {
            public String run()
            {
                try
                {
                    return System.getProperty(propName);
                }
                catch (SecurityException se)
                {
                    return null;
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getThreadContextClassLoader()
    {
        ClassLoader classLoader;

        try
        {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        catch (SecurityException e)
        {
            /*
             * SecurityException is thrown when
             * a) the context class loader isn't an ancestor of the
             *    calling class's class loader, or
             * b) if security permissions are restricted.
             *
             * For (a), ignore and keep going.  We cannot help but also
             * ignore (b) with the logic below, but other calls elsewhere
             * (to obtain a class loader) will re-trigger this exception
             * where we can make a distinction.
             */
            classLoader = null;  // ignore
        }

        // Return the selected class loader
        return classLoader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getSystemClassLoader()
    {
        return systemClassLoader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enumeration<URL> getResources(ClassLoader loader, String resourceName) throws IOException
    {
        /*
         * The simple answer is/was:
         *    return loader.getResources(resourceName);
         *
         * However, some classloaders overload the behavior of getResource
         * (loadClass, etc) such that the order of returned results changes
         * from normally expected behavior.
         *
         * Example: locate classes/resources from child ClassLoaders first,
         *          parents last (in some J2EE environs).
         *
         * The resource returned by getResource() should be the same as the
         * first resource returned by getResources().  Unfortunately, this
         * is not, and cannot be: getResources() is 'final' in the current
         * JDK's (1.2, 1.3, 1.4).
         *
         * To address this, the implementation of this method will
         * return an Enumeration such that the first element is the
         * results of getResource, and all trailing elements are
         * from getResources.  On each iteration, we check so see
         * if the resource (from getResources) matches the first resource,
         * and eliminate the redundent element.
         */

        final URL first = loader.getResource(resourceName);

        // XXX: Trying to avoid JBoss UnifiedClassLoader problem

        Enumeration<URL> resources;

        if (first == null)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Could not find resource: " + resourceName);
            }
            List<URL> emptyURL = Collections.emptyList();
            resources = Collections.enumeration(emptyURL);

        }
        else
        {

            try
            {

                resources = loader.getResources(resourceName);

            }
            catch (RuntimeException ex)
            {
                log.error("Exception occured during attept to get " + resourceName
                          + " from " + first, ex);
                List<URL> emptyURL = Collections.emptyList();
                resources = Collections.enumeration(emptyURL);
            }

            resources = getResourcesFromUrl(first, resources);
        }

        return resources;
    }

    /**
     * Enumerates resources URL.
     *
     * @param first The first URL in the enumeration sequence
     * @param rest The URL enumeration
     * @return A new resources URL enumeration
     */
    private static Enumeration<URL> getResourcesFromUrl(final URL first, final Enumeration<URL> rest)
    {
        return new Enumeration<URL>()
        {

            private boolean firstDone = (first == null);

            private URL next = getNext();

            public URL nextElement()
            {
                URL o = next;
                next = getNext();
                return o;
            }

            public boolean hasMoreElements()
            {
                return next != null;
            }

            private URL getNext()
            {
                URL n;

                if (!firstDone)
                {
                    /*
                     * First time through, use results of getReference()
                     * if they were non-null.
                     */
                    firstDone = true;
                    n = first;
                }
                else
                {
                    /*
                     * Subsequent times through,
                     * use results of getReferences()
                     * but take out anything that matches 'first'.
                     *
                     * Iterate through list until we find one that
                     * doesn't match 'first'.
                     */
                    n = null;
                    while (rest.hasMoreElements()  &&  n == null)
                    {
                        n = rest.nextElement();
                        if (first != null &&
                                n != null &&
                                n.equals(first))
                        {
                            n = null;
                        }
                    }
                }

                return n;
            }
        };
    }

    /**
     * Find the System {@code ClassLoader}.
     *
     * @return The System {@code ClassLoader}
     */
    static private ClassLoader findSystemClassLoader()
    {

        ClassLoader classLoader;

        try
        {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        catch (SecurityException e)
        {
            /*
             * Ignore and keep going.
             */
            classLoader = null;
        }

        if (classLoader == null)
        {
            SecurityManager security = System.getSecurityManager();
            if (security != null)
            {
                try
                {
                    security.checkCreateClassLoader();
                    classLoader = new PsuedoSystemClassLoader();
                }
                catch (SecurityException se)
                {
                }
            }
        }

        // Return the selected class loader
        return classLoader;
    }

}
