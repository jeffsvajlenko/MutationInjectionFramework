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
package org.apache.commons.betwixt.versioning;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.Options;
import org.apache.commons.betwixt.strategy.AttributeSuppressionStrategy;
import org.apache.commons.betwixt.strategy.ElementSuppressionStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VersioningStrategy implements ElementSuppressionStrategy,
    AttributeSuppressionStrategy
{
    public static Log log = LogFactory.getLog(VersioningStrategy.class);

    public final static String VERSION_FROM = "version-from";

    public final static String VERSION_UNTIL = "version-until";

    private String version;

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public boolean suppress(ElementDescriptor descr)
    {
        log.info("Checking element " + descr.getLocalName() + " (" + descr + ")");

        if (false == checkVersionFrom(descr.getOptions()))
        {
            log.info("Suppressing element (invalid version/from)");
            return true;
        }

        if (false == checkVersionUntil(descr.getOptions()))
        {
            log.info("Suppressing element (invalid version/until)");
            return true;
        }

        log.info("Showing element");
        return false;
    }

    public boolean suppress(final AttributeDescriptor descr)
    {
        log.info("Checking attribute " + descr.getLocalName() + " (" + descr + ")");

        if (false == checkVersionFrom(descr.getOptions()))
        {
            log.info("Suppressing attribute (invalid version/from)");
            return true;
        }

        if (false == checkVersionUntil(descr.getOptions()))
        {
            log.info("Suppressing attribute (invalid version/until)");
            return true;
        }

        log.info("Showing attribute");
        return false;
    }

    private boolean checkVersionFrom(final Options options)
    {
        log.info("Checking version/from");

        if (options == null)
        {
            log.info("No options");
            return true;
        }

        final String value = options.getValue(VERSION_FROM);

        log.info("value=" + value);
        log.info("version=" + version);
        debugOptions(options);

        if (value == null || value.trim().length() == 0)
        {
            log.info("No attribute \"Version from\"");
            return true;
        }

        final boolean versionOk = value.compareTo(version) <= 0;
        log.info("versionOk=" + versionOk);

        return versionOk;
    }

    private boolean checkVersionUntil(final Options options)
    {
        log.info("Checking version/until");

        if (options == null)
        {
            log.info("No options");
            return true;
        }

        final String value = options.getValue(VERSION_UNTIL);

        log.info("value=" + value);
        log.info("version=" + version);
        debugOptions(options);

        if (value == null || value.trim().length() == 0)
        {
            log.info("No attribute \"Version until\"");
            return true;
        }

        final boolean versionOk = value.compareTo(version) >= 0;
        log.info("versionOk=" + versionOk);

        return versionOk;
    }

    public VersioningStrategy()
    {
        super();
    }

    public VersioningStrategy(final String version)
    {
        super();
        setVersion(version);
    }

    private final void debugOptions(final Options options)
    {
        final String[] names = options.getNames();

        log.info("Names:");

        for (int ii = 0; ii < names.length; ii++)
        {
            final String name = names[ii];

            log.info("  " + ii + ": " + name + "=" + options.getValue(name));
        }
    }
}
