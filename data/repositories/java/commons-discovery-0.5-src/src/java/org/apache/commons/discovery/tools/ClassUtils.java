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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.apache.commons.discovery.DiscoveryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Various utilities to interact with {@code Class} types.
 */
public class ClassUtils
{

    private static Log log = LogFactory.getLog(ClassUtils.class);

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
     * Get package name.
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
            packageName = clazzName.substring(0, clazzName.lastIndexOf('.'));
        }
        return packageName;
    }

    /**
     * Looks for {@code public static returnType methodName(paramTypes)}.
     *
     * @param clazz The class where looking for the method
     * @param returnType The method return type
     * @param methodName The method name
     * @param paramTypes The method arguments types
     * @return Method {@code public static returnType methodName(paramTypes)},
     *         if found to be <strong>directly</strong> implemented by clazz.
     */
    public static Method findPublicStaticMethod(Class<?> clazz,
            Class<?> returnType,
            String methodName,
            Class<?>[] paramTypes)
    {
        boolean problem = false;
        Method method = null;

        // verify '<methodName>(<paramTypes>)' is directly in class.
        try
        {
            method = clazz.getDeclaredMethod(methodName, paramTypes);
        }
        catch(NoSuchMethodException e)
        {
            problem = true;
            log.debug("Class " + clazz.getName() + ": missing method '" + methodName + "(...)", e);
        }

        // verify 'public static <returnType>'
        if (!problem  &&
                !(Modifier.isPublic(method.getModifiers()) &&
                  Modifier.isStatic(method.getModifiers()) &&
                  method.getReturnType() == returnType))
        {
            if (log.isDebugEnabled())
            {
                if (!Modifier.isPublic(method.getModifiers()))
                {
                    log.debug(methodName + "() is not public");
                }
                if (!Modifier.isStatic(method.getModifiers()))
                {
                    log.debug(methodName + "() is not static");
                }
                if (method.getReturnType() != returnType)
                {
                    log.debug("Method returns: "
                              + method.getReturnType().getName()
                              + "@@"
                              + method.getReturnType().getClassLoader());
                    log.debug("Should return:  "
                              + returnType.getName()
                              + "@@"
                              + returnType.getClassLoader());
                }
            }
            problem = true;
            method = null;
        }

        return method;
    }

    /**
     * Creates a new instance of the input class using the following policy:
     *
     * <ul>
     * <li>if <code>paramClasses</code> or <code>params</code> is null,
     * the default constructor will be used;</li>
     * <li>the public constructor with <code>paramClasses</code> arguments type,
     * with <code>params</code> as arguments value, will be used.</li>
     * </ul>
     *
     * @param <T> The class type has to be instantiated
     * @param impl The class has to be instantiated
     * @param paramClasses The constructor arguments types (can be {@code null})
     * @param params The constructor arguments values (can be {@code null})
     * @return A new class instance
     * @throws DiscoveryException if the class implementing
     *            the SPI cannot be found, cannot be loaded and
     *            instantiated, or if the resulting class does not implement
     *            (or extend) the SPI
     * @throws InstantiationException see {@link Class#newInstance()}
     * @throws IllegalAccessException see {@link Class#newInstance()}
     * @throws NoSuchMethodException see {@link Class#newInstance()}
     * @throws InvocationTargetException see {@link Class#newInstance()}
     */
    public static <T> T newInstance(Class<T> impl, Class<?> paramClasses[], Object params[]) throws DiscoveryException,
        InstantiationException,
        IllegalAccessException,
        NoSuchMethodException,
        InvocationTargetException
    {
        if (paramClasses == null || params == null)
        {
            return impl.newInstance();
        }

        Constructor<T> constructor = impl.getConstructor(paramClasses);
        return constructor.newInstance(params);
    }

    /**
     * Throws exception if {@code impl} does not
     * implement or extend the SPI.
     *
     * @param spi The SPI type
     * @param impl The class has to be verified is a SPI implementation/extension
     * @throws DiscoveryException if the input implementation class is not an SPI implementation
     */
    public static void verifyAncestory(Class<?> spi, Class<?> impl) throws DiscoveryException
    {
        if (spi == null)
        {
            throw new DiscoveryException("No interface defined!");
        }

        if (impl == null)
        {
            throw new DiscoveryException("No implementation defined for " + spi.getName());
        }

        if (!spi.isAssignableFrom(impl))
        {
            throw new DiscoveryException("Class "
                                         + impl.getName()
                                         + " does not implement "
                                         + spi.getName());
        }
    }

}
