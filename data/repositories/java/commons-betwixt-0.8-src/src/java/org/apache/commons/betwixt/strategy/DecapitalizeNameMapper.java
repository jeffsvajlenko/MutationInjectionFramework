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

import java.beans.Introspector;

/**
 * <p>A name mapper which converts types to a decapitalized String.</p>
 *
 * <p>This conversion decapitalizes in the standard java beans way
 * (as per <code>java.beans.Introspector</code>).
 * This means that the first letter only will be decapitalized except
 * for the case where the first and second characters are both upper case.
 * When both are upper case, then the name will be left alown.</p>
 *
 * <p>So a bean type of <code>Foo</code> will be converted to the element name <code>"foo"</code.
 * <code>FooBar</code> will be converted to <code>"fooBar"</code>.
 * But <code>URL</code> will remain as <code>"URL"</code>.</p>
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision: 471234 $
 */
public class DecapitalizeNameMapper implements NameMapper
{

    /**
     * Decapitalize first letter unless both are upper case.
     * (As per standard java beans behaviour.)
     *
     * @param typeName the string to convert
     * @return decapitalized name as per <code>java.beans.Introspector</code>
     */
    public String mapTypeToElementName(String typeName)
    {
        return Introspector.decapitalize( typeName );
    }

    /**
     * Outputs a brief description.
     * @since 0.8
     */
    public String toString()
    {
        return "Decapitalize Type Name Mapper";
    }
}
