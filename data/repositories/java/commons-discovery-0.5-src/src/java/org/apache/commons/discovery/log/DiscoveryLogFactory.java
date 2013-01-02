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
package org.apache.commons.discovery.log;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.discovery.DiscoveryException;
import org.apache.commons.discovery.tools.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Simple implementation of Log that sends all enabled log messages,
 * for all defined loggers, to System.err.
 * </p>
 *
 * <p>Hacked from commons-logging SimpleLog for use in discovery.
 * This is intended to be enough of a Log implementation to bootstrap
 * Discovery.
 * </p>
 *
 * <p>One property: <code>org.apache.commons.discovery.log.level</code>.
 * valid values: all, trace, debug, info, warn, error, fatal, off.
 * </p>
 *
 * @deprecated Starting from commons-discovery-05, Log is totally delegated to commons-logging
 * @version $Id: DiscoveryLogFactory.java 1089255 2011-04-05 21:51:05Z simonetripodi $
 */
@Deprecated
public class DiscoveryLogFactory
{

    private static LogFactory logFactory = null;

    private static final Map<Class<?>, Class<?>>  classRegistry = new Hashtable<Class<?>, Class<?>>();

    private static final Class<?>[] setLogParamClasses = new Class<?>[] { Log.class };

    /**
     * Above fields must be initialied before this one..
     */
    private static Log log = DiscoveryLogFactory._newLog(DiscoveryLogFactory.class);

    /**
     * Creates a new {@code Log} instance for the input class.
     *
     * @param clazz The class the log has to be created for
     * @return The input class logger
     */
    public static Log newLog(Class<?> clazz)
    {
        /**
         * Required to implement 'public static void setLog(Log)'
         */
        try
        {
            Method setLog = ClassUtils.findPublicStaticMethod(clazz,
                            void.class,
                            "setLog",
                            setLogParamClasses);

            if (setLog == null)
            {
                String msg = "Internal Error: "
                             + clazz.getName()
                             + " required to implement 'public static void setLog(Log)'";
                log.fatal(msg);
                throw new DiscoveryException(msg);
            }
        }
        catch (SecurityException se)
        {
            String msg = "Required Security Permissions not present";
            log.fatal(msg, se);
            throw new DiscoveryException(msg, se);
        }

        if (log.isDebugEnabled())
        {
            log.debug("Class meets requirements: " + clazz.getName());
        }

        return _newLog(clazz);
    }

    /**
     * This method MUST not invoke any logging..
     *
     * @param clazz The class the log has to be created for
     * @return The input class logger
     */
    public static Log _newLog(Class<?> clazz)
    {
        classRegistry.put(clazz, clazz);

        return (logFactory == null)
               ? new SimpleLog(clazz.getName())
               : logFactory.getInstance(clazz.getName());
    }

    /**
     * Sets the {@code Log} for this class.
     *
     * @param _log This class {@code Log}
     */
    public static void setLog(Log _log)
    {
        log = _log;
    }

    /**
     * Set logFactory, works ONLY on first call.
     *
     * @param factory The log factory
     */
    public static void setFactory(LogFactory factory)
    {
        if (logFactory == null)
        {
            // for future generations.. if any
            logFactory = factory;

            // now, go back and reset loggers for all current classes..
            for (Class<?> clazz : classRegistry.values())
            {

                if (log.isDebugEnabled())
                {
                    log.debug("Reset Log for: " + clazz.getName());
                }

                Method setLog = null;

                // invoke 'setLog(Log)'.. we already know it's 'public static',
                // have verified parameters, and return type..
                try
                {
                    setLog = clazz.getMethod("setLog", setLogParamClasses);
                }
                catch(Exception e)
                {
                    String msg = "Internal Error: pre-check for " + clazz.getName() + " failed?!";
                    log.fatal(msg, e);
                    throw new DiscoveryException(msg, e);
                }

                Object[] setLogParam = new Object[] { factory.getInstance(clazz.getName()) };

                try
                {
                    setLog.invoke(null, setLogParam);
                }
                catch(Exception e)
                {
                    String msg = "Internal Error: setLog failed for " + clazz.getName();
                    log.fatal(msg, e);
                    throw new DiscoveryException(msg, e);
                }
            }
        }
    }

}
