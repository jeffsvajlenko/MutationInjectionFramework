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
package org.apache.commons.betwixt.strategy.impl.propertysuppression;

import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;

/**
 * Suppresses properties based on the package of their type.
 * Limited regex is supported. If the package name ends in <code>.*</code>
 * them all child packages will be suppressed.
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a> of the <a href='http://www.apache.org'>Apache Software Foundation</a>
 * @since 0.8
 */
public class PackageSuppressor extends PropertySuppressionStrategy
{

    /** Package to be suppressed */
    private final String suppressedPackage;
    private final boolean exact;

    /**
     * Constructs a suppressor for packages.
     * @param suppressedPackage package name (for exact match)
     * or base package and <code>.*</code>to suppress children
     */
    public  PackageSuppressor(String suppressedPackage)
    {
        if (suppressedPackage.endsWith(".*"))
        {
            exact = false;
            suppressedPackage = suppressedPackage.substring(0, suppressedPackage.length()-2);
        }
        else
        {
            exact =true;
        }
        this.suppressedPackage = suppressedPackage;
    }

    public boolean suppressProperty(Class classContainingTheProperty, Class propertyType, String propertyName)
    {
        boolean result = false;
        if (propertyType != null)
        {
            Package propertyTypePackage = propertyType.getPackage();
            if (propertyTypePackage != null)
            {
                String packageName = propertyTypePackage.getName();
                if (exact)
                {
                    result = suppressedPackage.equals(packageName);
                }
                else if (packageName != null)
                {
                    result = packageName.startsWith(suppressedPackage);
                }
            }
        }
        return result;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Suppressing package " );
        buffer.append(suppressedPackage);
        if (exact)
        {
            buffer.append("(exact)");
        }
        return buffer.toString();
    }
}
