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
 * JDK Hooks to extract properties/resources.
 */
public abstract class JDKHooks
{

    private static final JDKHooks jdkHooks;

    static
    {
        jdkHooks = new JDK12Hooks();
    }

    /**
     * Hidden constructor, this class can't be directly instantiated.
     */
    protected JDKHooks() { }

    /**
     * Return singleton object representing JVM hooks/tools.
     *
     * TODO: add logic to detect JDK level.
     *
     * @return The detected {@code JDKHooks}
     */
    public static final JDKHooks getJDKHooks()
    {
        return jdkHooks;
    }

    /**
     * Get the system property
     *
     * @param propName name of the property
     * @return value of the property
     */
    public abstract String getSystemProperty(final String propName);

    /**
     * The thread context class loader is available for JDK 1.2
     * or later, if certain security conditions are met.
     *
     * @return The thread context class loader, if available.
     *         Otherwise return null.
     */
    public abstract ClassLoader getThreadContextClassLoader();

    /**
     * The system class loader is available for JDK 1.2
     * or later, if certain security conditions are met.
     *
     * @return The system class loader, if available.
     *         Otherwise return null.
     */
    public abstract ClassLoader getSystemClassLoader();

    /**
     * Resolve resource with given names and make them available in
     * the returned iterator.
     *
     * @param loader The class loader used to resolve resources
     * @param resourceName The resource name to resolve
     * @return The iterator over the URL resolved resources
     * @throws IOException if any error occurs while loading the resource
     */
    public abstract Enumeration<URL> getResources(ClassLoader loader, String resourceName) throws IOException;

}
