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

/**
 * <p>A default implementation of the name mapper.
 * This mapper simply returns the unmodified type name.</p>
 *
 * <p>For example, <code>PropertyName</code> would be converted to <code>PropertyName</code>.
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision: 438373 $
 */
public class DefaultNameMapper implements NameMapper
{

    /** Used to convert bad character in the name */
    private static final BadCharacterReplacingNMapper badCharacterReplacementNMapper
        = new BadCharacterReplacingNMapper( new PlainMapper() );

    /** Base implementation chained by bad character replacement mapper */
    private static final class PlainMapper implements NameMapper
    {
        /**
        * This implementation returns the parameter passed in without modification.
        *
        * @param typeName the string to convert
        * @return the typeName parameter without modification
        */
        public String mapTypeToElementName( String typeName )
        {
            return typeName ;
        }
    }

    /**
     * This implementation returns the parameter passed after
     * deleting any characters which the XML specification does not allow
     * in element names.
     *
     * @param typeName the string to convert
     * @return the typeName parameter without modification
     */
    public String mapTypeToElementName( String typeName )
    {
        return badCharacterReplacementNMapper.mapTypeToElementName( typeName );
    }
}
