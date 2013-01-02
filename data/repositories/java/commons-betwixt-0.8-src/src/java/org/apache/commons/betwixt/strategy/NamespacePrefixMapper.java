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


package org.apache.commons.betwixt.strategy;

import java.util.HashMap;

/**
 * <p>Maps namespace <code>URI</code>'s to prefixes.
 * </p><p>
 * When validating xml documents including namespaces,
 * the issue of prefixes (the short expression before the colon in a universal name)
 * becomes important.
 * DTDs are not namespace aware and so a fixed prefixed must be chosen
 * and used consistently.
 * This class is used to supply consistent, user specified prefixes.
 * </p>
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class NamespacePrefixMapper
{

    private int count = 0;
    private HashMap prefixesByUri = new HashMap();

    /**
     * Gets the prefix to be used with the given namespace URI
     * @param namespaceUri
     * @return prefix, not null
     */
    public String getPrefix(String namespaceUri)
    {
        String prefix = (String) prefixesByUri.get(namespaceUri);
        if (prefix == null)
        {
            prefix = generatePrefix(namespaceUri);
            setPrefix(namespaceUri, prefix);
        }
        return prefix;
    }

    /**
     * Sets the prefix to be used for the given namespace URI.
     * This method does not check for clashes amongst the namespaces.
     * Possibly it should.
     * @param namespaceUri
     * @param prefix
     */
    public void setPrefix(String namespaceUri, String prefix)
    {
        prefixesByUri.put(namespaceUri, prefix);
    }

    /**
     * Generates a prefix for the given namespace Uri.
     * Used to assign prefixes for unassigned namespaces.
     * Subclass may wish to override this method to provide more
     * sophisticated implementations.
     * @param namespaceUri URI, not null
     * @return prefix, not null
     */
    protected String generatePrefix(String namespaceUri)
    {
        String prefix = "bt" + ++count;
        if (prefixesByUri.values().contains(prefix))
        {
            prefix = generatePrefix(namespaceUri);
        }
        return prefix;
    }

}
