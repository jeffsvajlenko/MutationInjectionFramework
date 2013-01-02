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

import org.apache.commons.betwixt.XMLUtils;

/**
 * <code>NameMapper</code> implementation that processes a name by replacing or stripping
 * illegal characters before passing result down the chain.
 *
 * @author Robert Burrell Donkin
 * @since 0.5
 */
public class BadCharacterReplacingNMapper implements NameMapper
{
    /** Next mapper in chain, possibly null */
    private NameMapper chainedMapper;
    /** Replacement character, possibly null */
    private Character replacement = null;

    /**
      * Constructs a replacing mapper which delegates to given mapper.
      * @param chainedMapper next link in processing chain, possibly null
      */
    public BadCharacterReplacingNMapper(NameMapper chainedMapper)
    {
        this.chainedMapper = chainedMapper;
    }

    /**
      * Gets the character that should be used to replace bad characters
      * if null then bad characters will be deleted.
      * @return the replacement Character possibly null
      */
    public Character getReplacement()
    {
        return replacement;
    }

    /**
      * Sets the character that should be used to replace bad characters.
      * @param replacement the Charcter to be used for replacement if not null.
      * Otherwise, indicates that illegal characters should be deleted.
      */
    public void setReplacement( Character replacement )
    {
        this.replacement = replacement;
    }

    /**
     * This implementation processes characters which are not allowed in xml
     * element names and then returns the result from the next link in the chain.
     * This processing consists of deleting them if no replacement character
     * has been set.
     * Otherwise, the character will be replaced.
     *
     * @param typeName the string to convert
     * @return the processed input
     */
    public String mapTypeToElementName(String typeName)
    {

        StringBuffer buffer = new StringBuffer( typeName );
        for (int i=0, size = buffer.length(); i< size; i++)
        {
            char nextChar = buffer.charAt( i );
            boolean bad = false;
            if ( i==0 )
            {
                bad = !XMLUtils.isNameStartChar( nextChar );
            }
            else
            {
                bad = !XMLUtils.isNameChar( nextChar );
            }

            if (bad)
            {
                if ( replacement != null )
                {
                    buffer.setCharAt( i, replacement.charValue() );
                }
                else
                {
                    // delete
                    buffer.deleteCharAt( i );
                    i--;
                    size--;
                }
            }
        }

        if ( buffer.length() == 0 )
        {
            throw new IllegalArgumentException(
                "Element name contains no legal characters and no replacements have been set.");
        }

        typeName = buffer.toString();

        if ( chainedMapper == null )
        {
            return typeName;
        }
        return chainedMapper.mapTypeToElementName( typeName );
    }
}
