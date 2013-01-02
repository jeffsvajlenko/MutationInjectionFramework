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

import org.apache.commons.discovery.DiscoveryException;

/**
 * Represents a Service Programming Interface (spi).
 * - SPI's name
 * - SPI's (provider) class
 * - SPI's (alternate) override property name
 *
 * In addition, while there are many cases where this is NOT
 * usefull, for those in which it is:
 *
 * - expected constructor argument types and parameters values.
 *
 * @param <T> The SPI type
 */
public class SPInterface<T>
{

    /**
     * Construct object representing Class {@code provider}.
     *
     * @param <T> The SPI type
     * @param provider The SPI class
     * @return A new object representing Class {@code provider}
     * @since 0.5
     */
    public static <T> SPInterface<T> newSPInterface(Class<T> provider)
    {
        return newSPInterface(provider, provider.getName());
    }

    /**
     * Construct object representing Class {@code provider}.
     *
     * @param <T> The SPI type
     * @param provider The SPI class
     * @param propertyName when looking for the name of a class implementing
     *        the provider class, a discovery strategy may involve looking for
     *        (system or other) properties having either the name of the class
     *        (provider) or the <code>propertyName</code>.
     * @return A new object representing Class {@code provider}
     * @since 0.5
     */
    public static <T> SPInterface<T> newSPInterface(Class<T> provider, String propertyName)
    {
        return new SPInterface<T>(provider, propertyName);
    }

    /**
     * Construct object representing Class {@code provider}.
     *
     * @param <T> The SPI type
     * @param provider The SPI class
     * @param constructorParamClasses classes representing the
     *        constructor argument types
     * @param constructorParams objects representing the
     *        constructor arguments
     * @return A new object representing Class {@code provider}
     * @since 0.5
     */
    public static <T> SPInterface<T> newSPInterface(Class<T> provider,
            Class<?> constructorParamClasses[],
            Object constructorParams[])
    {
        return newSPInterface(provider, provider.getName(), constructorParamClasses, constructorParams);
    }

    /**
     * Construct object representing Class {@code provider}.
     *
     * @param <T> The SPI type
     * @param provider The SPI class
     * @param propertyName when looking for the name of a class implementing
     *        the provider class, a discovery strategy may involve looking for
     *        (system or other) properties having either the name of the class
     *        (provider) or the <code>propertyName</code>.
     * @param constructorParamClasses classes representing the
     *        constructor argument types
     * @param constructorParams objects representing the
     *        constructor arguments
     * @return A new object representing Class {@code provider}
     * @since 0.5
     */
    public static <T> SPInterface<T> newSPInterface(Class<T> provider,
            String propertyName,
            Class<?> constructorParamClasses[],
            Object constructorParams[])
    {
        return new SPInterface<T>(provider, propertyName, constructorParamClasses, constructorParams);
    }

    /**
     * The service programming interface: intended to be
     * an interface or abstract class, but not limited
     * to those two.
     */
    private final Class<T> spi;

    /**
     * The property name to be used for finding the name of
     * the SPI implementation class.
     */
    private final String propertyName;

    private final Class<?>  paramClasses[];

    private final Object params[];

    /**
     * Construct object representing Class <code>provider</code>.
     *
     * @param provider The SPI class
     */
    public SPInterface(Class<T> provider)
    {
        this(provider, provider.getName());
    }

    /**
     * Construct object representing Class <code>provider</code>.
     *
     * @param spi The SPI class
     *
     * @param propertyName when looking for the name of a class implementing
     *        the provider class, a discovery strategy may involve looking for
     *        (system or other) properties having either the name of the class
     *        (provider) or the <code>propertyName</code>.
     */
    public SPInterface(Class<T> spi, String propertyName)
    {
        this.spi = spi;
        this.propertyName = propertyName;
        this.paramClasses = null;
        this.params = null;
    }

    /**
     * Construct object representing Class <code>provider</code>.
     *
     * @param provider The SPI class
     *
     * @param constructorParamClasses classes representing the
     *        constructor argument types.
     *
     * @param constructorParams objects representing the
     *        constructor arguments.
     */
    public SPInterface(Class<T> provider,
                       Class<?> constructorParamClasses[],
                       Object constructorParams[])
    {
        this(provider,
             provider.getName(),
             constructorParamClasses,
             constructorParams);
    }

    /**
     * Construct object representing Class <code>provider</code>.
     *
     * @param spi The SPI class
     *
     * @param propertyName when looking for the name of a class implementing
     *        the provider class, a discovery strategy may involve looking for
     *        (system or other) properties having either the name of the class
     *        (provider) or the <code>propertyName</code>.
     *
     * @param constructorParamClasses classes representing the
     *        constructor argument types.
     *
     * @param constructorParams objects representing the
     *        constructor arguments.
     */
    public SPInterface(Class<T> spi,
                       String propertyName,
                       Class<?> constructorParamClasses[],
                       Object constructorParams[])
    {
        this.spi = spi;
        this.propertyName = propertyName;
        this.paramClasses = constructorParamClasses;
        this.params = constructorParams;
    }

    /**
     * Returns the SPI class name.
     *
     * @return The SPI class name
     */
    public String getSPName()
    {
        return spi.getName();
    }

    /**
     * Returns the SPI class.
     *
     * @return The SPI class
     */
    public Class<T> getSPClass()
    {
        return spi;
    }

    /**
     * Returns the property name to be used for finding
     * the name of the SPI implementation class.
     *
     * @return The property name to be used for finding
     *         the name of the SPI implementation class
     */
    public String getPropertyName()
    {
        return propertyName;
    }

    /**
     * Creates a new instance of the given SPI class.
     *
     * @param <S> Any type extends T
     * @param impl The SPI class has to be instantiated
     * @return A new instance of the given SPI class
     * @throws DiscoveryException if the class implementing
     *            the SPI cannot be found, cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public <S extends T> S newInstance(Class<S> impl)
    throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        verifyAncestory(impl);

        return ClassUtils.newInstance(impl, paramClasses, params);
    }

    /**
     * Verifies the given SPI implementation is a SPI specialization.
     *
     * @param <S> Any type extends T
     * @param impl The SPI instantance
     */
    public <S extends T> void verifyAncestory(Class<S> impl)
    {
        ClassUtils.verifyAncestory(spi, impl);
    }

}
