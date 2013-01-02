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

package org.apache.commons.betwixt;

import java.util.HashMap;
import java.util.Set;

/**
 * Collective for <code>Betwixt</code> optional behaviour hints.
 * An option links a name with a value (both strings).
 *
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @since 0.5
 */
public class Options
{
    /** Empty string array for use with toArray  */
    private static final String[] EMPTY_STRING_ARRAY = {};
    /** Option values indexed by name */
    private HashMap valuesByName = new HashMap();

    /**
     * Gets the value (if any) associated with the given name.
     * @param name <code>String</code>, not null
     * @return the associated value, or null if no value is assocated
     */
    public String getValue(String name)
    {
        return (String) valuesByName.get(name);
    }

    /**
     * Gets the names of each option.
     * @return <code>String</code> array containing the name of each option
     */
    public String[] getNames()
    {
        Set names = valuesByName.keySet();
        return (String[]) names.toArray(EMPTY_STRING_ARRAY);
    }

    /**
     * Adds the option.
     * The rule with options is that the last call to set the
     * value with a given name wins.
     * @param name <code>String</code> name, not null
     * @param value <code>Strong</code> name, not null
     */
    public void addOption(String name, String value)
    {
        valuesByName.put(name, value);
    }

    /**
     * Adds multiple options from an existing <code>Options</code> collection.
     * The rule with options is that the most recently set value for an option
     * wins, so options are potentially overwritten by this call.
     *
     * @param options -
     *            an existing <code>Options</code> collection
     * @since 0.8
     */
    public void addOptions(Options options)
    {
        valuesByName.putAll(options.valuesByName);
    }
}
