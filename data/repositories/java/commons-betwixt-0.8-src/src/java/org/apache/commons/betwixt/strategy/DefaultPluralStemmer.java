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

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A default implementation of the plural name stemmer which
 * tests for some common english plural/singular patterns and
 * then uses a simple starts-with algorithm
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Revision: 438373 $
 */
public class DefaultPluralStemmer implements PluralStemmer
{

    /** Log used for logging (Doh!) */
    protected static Log log = LogFactory.getLog( DefaultPluralStemmer.class );

    /**
     * <p>Algorithm supports common english plural patterns.</p>
     *
     * <p>First, common english plural constructions will be tried.
     * If the property doesn't end with <code>'y'</code> then this method will look for
     * a property with which has <code>'es'</code> appended.
     * If the property ends with <code>'y'</code> then a property with the <code>'y'</code>
     * replaced by <code>'ies'</code> will be searched for.</p>
     *
     * <p>If no matches are found then - if one exists - a property starting with the
     * singular name will be returned.</p>
     *
     * @param propertyName the property name string to match
     * @param map the <code>Map</code> containing the <code>ElementDescriptor</code>'s
     *        to be searched
     * @return The plural descriptor for the given singular property name.
     *         If more than one descriptor matches, then the best match is returned.
     */
    public ElementDescriptor findPluralDescriptor( String propertyName, Map map )
    {
        int foundKeyCount = 0;
        String keyFound = null;
        ElementDescriptor answer = (ElementDescriptor) map.get( propertyName + "s" );

        if ( answer == null && !propertyName.endsWith( "y" ))
        {
            answer = (ElementDescriptor) map.get( propertyName + "es" );
        }

        if ( answer == null )
        {
            int length = propertyName.length();
            if ( propertyName.endsWith( "y" ) && length > 1 )
            {
                String key = propertyName.substring(0, length - 1) + "ies";
                answer = (ElementDescriptor) map.get( key );
            }

            if ( answer == null )
            {
                // lets find the first one that starts with the propertyName
                for ( Iterator iter = map.keySet().iterator(); iter.hasNext(); )
                {
                    String key = (String) iter.next();
                    if ( key.startsWith( propertyName ) )
                    {
                        if (answer == null)
                        {
                            answer = (ElementDescriptor) map.get(key);
                            if (key.equals(propertyName))
                            {
                                // we found the best match..
                                break;
                            }
                            foundKeyCount++;
                            keyFound = key;

                        }
                        else
                        {
                            // check if we have a better match,,
                            if (keyFound.length() > key.length())
                            {
                                answer = (ElementDescriptor) map.get(key);
                                keyFound = key;
                            }
                            foundKeyCount++;

                        }
                    }
                }
            }
        }
        if (foundKeyCount > 1)
        {
            log.warn("More than one type matches, using closest match "+answer.getQualifiedName());
        }
        return answer;

    }
}
