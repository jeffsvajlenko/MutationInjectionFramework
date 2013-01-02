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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>ClassNormalizer that uses a list of substitutions.</p>
 * <p>
 * This <code>ClassNormalizer</code> checks a list (in order) to find a matching
 * Class.
 * This match can be performed either strictly (using equality) or taking into account
 * inheritance and implementation.
 * If a match is found then the first substituted class is returned as the normalization.
 * </p>
 * @author Robert Burrell Donkin
 * @since 0.5
 */
public class ListedClassNormalizer extends ClassNormalizer
{

    /** Entries to be normalized */
    private ArrayList normalizations = new ArrayList();
    /** Should the equality (rather than isAssignabledFrom) be used to check */
    private boolean strickCheck = false;

    /**
      * Is strict checking of substitutions on?
      * @return true is equality is used to compare classes when considering substition,
      * otherwise isAssignableFrom will be used so that super classes and super interfaces
      * will be matched.
      */
    public boolean isStrickCheck()
    {
        return strickCheck;
    }

    /**
      * Sets strict checking of substitutions?
      * @param strickCheck if true then equality will be used to compare classes
      * when considering substition,
      * otherwise isAssignableFrom will be used so that super classes and super interfaces
      * will be matched.
      */
    public void setStrickCheck(boolean strickCheck)
    {
        this.strickCheck = strickCheck;
    }

    /**
      * Adds this given substitution to the list.
      * No warning is given if the match has already been added to the list.
      * @param match if any classes matching this then the normal class will be substituted
      * @param substitute the normalized Class if the primary class is matched
      */
    public void addSubstitution( Class match, Class substitute )
    {
        normalizations.add( new ListEntry( match, substitute ));
    }

    /**
      * Adds the given substitute to the list.
      * This is a convenience method useful when {@link #isStrickCheck} is false.
      * In this case, any subclasses (if this is a class) or implementating classes
      * if this is an interface) will be subsituted with this value.
      * @param substitute sustitude this Class
      */
    public void addSubstitution( Class substitute )
    {
        addSubstitution( substitute, substitute );
    }

    /**
      * Normalize given class.
      * The normalized Class is the Class that Betwixt should
      * introspect.
      * This strategy class allows the introspected Class to be
      * varied.
      *
      * @param clazz the class to normalize, not null
      * @return this implementation check it's list of substitutations in order
      * and returns the first that matchs. If {@link #isStrickCheck} then equality
      * is used otherwise isAssignableFrom is used (so that super class and interfaces are matched).
      */
    public Class normalize( Class clazz )
    {
        Iterator it = normalizations.iterator();
        while ( it.hasNext() )
        {
            ListEntry entry = (ListEntry) it.next();
            if ( strickCheck )
            {
                if ( entry.match.equals( clazz ) )
                {
                    return entry.substitute;
                }
            }
            else
            {
                if ( entry.match.isAssignableFrom( clazz ))
                {
                    return entry.substitute;
                }
            }
        }

        return clazz;
    }

    /** Holds list entries */
    private class ListEntry
    {
        /** Class to be check */
        Class match;
        /** Substituted to be returned */
        Class substitute;

        /**
          * Base constructor
          * @param match match this Class
          * @param subsistute substitute matches with this Class
          */
        ListEntry( Class match, Class substitute )
        {
            this.match = match;
            this.substitute = substitute;
        }
    }
}
