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

import java.io.Serializable;

import org.apache.commons.betwixt.Options;
import org.apache.commons.betwixt.expression.Context;

/**
 * <p>Strategy class for string &lt;-&gt; object conversions.
 * Implementations of this interface are used by Betwixt to perform
 * string &lt;-&gt; object conversions.
 * This performs only the most basic conversions.
 * Most applications will use a subclass.
 * </p>
 * <p>It is strongly recommended that (in order to support round tripping)
 * that <code>objectToString</code> and <code>stringToObject</code>
 * are inverse functions.
 * In other words, given the same flavour, context and type the applying
 * objectToString to the result of stringToObject should be equal to the
 * original input.
 * </p>
 * @author Robert Burrell Donkin
 * @since 0.5
 */
public class ObjectStringConverter implements Serializable
{

    /** Standard name for option giving flavour */
    public static final String FLAVOUR_OPTION_NAME
        = "org.apache.commons.betwixt.flavour";

    /**
      * Converts an object to a string representation.
      * This basic implementation returns object.toString()
      * or an empty string if the given object is null.
      *
      * @param object the object to be converted, possibly null
      * @param type the property class of the object, not null
      * @param flavour a string allow symantic differences in formatting to be communicated
      * @param context the context, not null
      * @deprecated 0.7 use {@link #objectToString(Object, Class, Context)} instead.
      * The preferred way to support flavours is by setting the
      * <code>org.apache.commons.betwixt.FLAVOUR</code> option.
      * This can then be retrieved by calling {@link Context#getOptions()}
      * @return a String representation, not null
      */
    public String objectToString(Object object, Class type, String flavour, Context context)
    {
        if ( object != null )
        {
            return object.toString();
        }
        return "";
    }

    /**
      * Converts a string representation to an object.
      * It is acceptable for an implementation to return the string if it cannot convert
      * the string to the given class type.
      * This basic implementation just returns a string.
      *
      * @param value the String to be converted
      * @param type the property class to be returned (if possible), not null
      * @param flavour a string allow symantic differences in formatting to be communicated
      * @param context the context, not null
      * @deprecated 0.7 use {@link #stringToObject(String, Class, Context)} instead.
      * The preferred way to support flavours is by setting the
      * <code>org.apache.commons.betwixt.FLAVOUR</code> option.
      * This can then be retrieved by calling {@link Context#getOptions()}
      * @return an Object converted from the String, not null
      */
    public Object stringToObject(String value, Class type, String flavour, Context context)
    {
        return value;
    }


    /**
      * Converts an object to a string representation.
      * This basic implementation returns object.toString()
      * or an empty string if the given object is null.
      *
      * @since 0.7
      * @param object the object to be converted, possibly null
      * @param type the property class of the object, not null
      * @param context the context, not null
      * @return a String representation, not null
      */
    public String objectToString(Object object, Class type, Context context)
    {
        String flavour = getFlavour(context);
        return objectToString(object, type, flavour, context);
    }

    /**
      * Converts a string representation to an object.
      * It is acceptable for an implementation to return the string if it cannot convert
      * the string to the given class type.
      * This basic implementation just returns a string.
      *
      * @since 0.7
      * @param value the String to be converted
      * @param type the property class to be returned (if possible), not null
      * @param context the context, not null
      * @return an Object converted from the String, not null
      */
    public Object stringToObject(String value, Class type, Context context)
    {
        String flavour = getFlavour(context);
        return stringToObject(value, type, flavour, context);
    }

    /**
     * Gets the current flavour from the context.
     * @param context <code>Context</code>, not null
     */
    private String getFlavour(Context context)
    {
        String flavour = null;
        Options options = context.getOptions();
        if (options != null)
        {
            flavour = options.getValue(FLAVOUR_OPTION_NAME);
        }
        return flavour;
    }
}
