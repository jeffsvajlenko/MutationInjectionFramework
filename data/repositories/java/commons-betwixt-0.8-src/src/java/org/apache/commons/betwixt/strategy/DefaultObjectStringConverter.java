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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.betwixt.expression.Context;

/**
 * <p>Default string &lt;-&gt; object conversion strategy.</p>
 * <p>
 * This delegates to ConvertUtils except when the type
 * is assignable from <code>java.util.Date</code>
 * but not from <code>java.sql.Date</code>.
 * In this case, the format used is (in SimpleDateFormat terms)
 * <code>EEE MMM dd HH:mm:ss zzz yyyy</code>.
 * This is the same as the output of the toString method on java.util.Date.
 * </p>
 * <p>
 * This should preserve the existing symantic behaviour whilst allowing round tripping of dates
 * (given the default settings).
 * </p>
 * @author Robert Burrell Donkin
 * @since 0.5
 */
public class DefaultObjectStringConverter extends ConvertUtilsObjectStringConverter
{

    /** Formats Dates to Strings and Strings to Dates */
    private final SimpleDateFormat formatter
        = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.UK);

    /**
      * Converts an object to a string representation using ConvertUtils.
      * If the object is a java.util.Date and the type is java.util.Date
      * but not java.sql.Date
      * then SimpleDateFormat formatting to
      * <code>EEE MMM dd HH:mm:ss zzz yyyy</code>
      * will be used.
      * (This is the same as java.util.Date toString would return.)
      *
      * @param object the object to be converted, possibly null
      * @param type the property class of the object, not null
      * @param flavour a string allow symantic differences in formatting
      * to be communicated (ignored)
      * @param context convert against this context not null
      * @return a String representation, not null
      */
    public String objectToString(Object object, Class type, String flavour, Context context)
    {
        if ( object != null )
        {
            if ( object instanceof Class)
            {
                return ((Class) object).getName();
            }

            if ( object instanceof java.util.Date && isUtilDate( type ) )
            {

                return formatter.format( (java.util.Date) object );

            }
            else
            {
                // use ConvertUtils implementation
                return super.objectToString( object, type, flavour, context );
            }
        }
        return "";
    }

    /**
      * Converts an object to a string representation using ConvertUtils.
      *
      * @param value the String to be converted, not null
      * @param type the property class to be returned (if possible), not null
      * @param flavour a string allow symantic differences
      * in formatting to be communicated (ignored)
      * @param context not null
      * @return an Object converted from the String, not null
      */
    public Object stringToObject(String value, Class type, String flavour, Context context)
    {
        if ( isUtilDate( type ) )
        {
            try
            {

                return formatter.parse( value );

            }
            catch ( ParseException ex )
            {
                handleException( ex );
                // this supports any subclasses that do not which to throw exceptions
                // probably will result in a problem when the method will be invoked
                // but never mind
                return value;
            }
        }
        else
        {
            // use ConvertUtils implementation
            return super.stringToObject( value, type, flavour, context );
        }
    }

    /**
      * Allow subclasses to use a different exception handling strategy.
      * This class throws a <code>org.apache.commons.beanutils.ConversionException</code>
      * when conversion fails.
      * @param e the Exception to be handled
      * @throws org.apache.commons.beanutils.ConversionException when conversion fails
      */
    protected void handleException(Exception e)
    {
        throw new ConversionException( "String to object conversion failed: " + e.getMessage(), e );
    }

    /**
      * Is the given type a java.util.Date but not a java.sql.Date?
      * @param type test this class type
      * @return true is this is a until date but not a sql one
      */
    private boolean isUtilDate(Class type)
    {
        return ( java.util.Date.class.isAssignableFrom(type)
                 && !java.sql.Date.class.isAssignableFrom(type)
                 && !java.sql.Time.class.isAssignableFrom(type)
                 && !java.sql.Timestamp.class.isAssignableFrom(type) );
    }
}
