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
import java.util.Enumeration;

/**
 * JDK 1.1 Style Hooks implementation.
 */
public class JDK11Hooks extends JDKHooks
{

    private static final ClassLoader systemClassLoader = new PsuedoSystemClassLoader();

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSystemProperty(final String propName)
    {
        return System.getProperty(propName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ClassLoader getThreadContextClassLoader()
    {
        return null;
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

        final Enumeration<URL> rest = loader.getResources(resourceName);

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

}
