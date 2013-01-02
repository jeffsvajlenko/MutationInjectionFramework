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

import java.util.Properties;

import org.apache.commons.discovery.resource.ClassLoaders;

/**
 * Holder for a default class.
 *
 * Class may be specified by name (String) or class (Class).
 * Using the holder complicates the users job, but minimized # of API's.
 */
public class PropertiesHolder
{

    private Properties properties;

    private final String propertiesFileName;

    /**
     * Creates a new {@code PropertiesHolder} instance given an
     * already load {@code Properties} set.
     *
     * @param properties The already load {@code Properties} set
     */
    public PropertiesHolder(Properties properties)
    {
        this.properties = properties;
        this.propertiesFileName = null;
    }

    /**
     * Creates a new {@code PropertiesHolder} instance given a
     * property file name.
     *
     * @param propertiesFileName The property file name
     */
    public PropertiesHolder(String propertiesFileName)
    {
        this.properties = null;
        this.propertiesFileName = propertiesFileName;
    }

    /**
     * Returns the {@code Properties} instance, loaded if necessary from {@code propertiesFileName}.
     *
     * @param spi Optional SPI (may be null).
     *            If provided, an attempt is made to load the
     *            property file as-per Class.getResource().
     *
     * @param loaders Used only if properties need to be loaded.
     *
     * @return The {@code Properties}, loaded if necessary.
     */
    public Properties getProperties(SPInterface<?> spi, ClassLoaders loaders)
    {
        if (properties == null)
        {
            properties = ResourceUtils.loadProperties(spi.getSPClass(), getPropertiesFileName(), loaders);
        }
        return properties;
    }

    /**
     * Returns the property file name
     *
     * @return The property file name
     */
    public String getPropertiesFileName()
    {
        return propertiesFileName;
    }

}
