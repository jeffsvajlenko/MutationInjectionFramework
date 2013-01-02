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
import java.util.Date;

/**
 * Determines the way that a type (of object) should be bound
 * by Betwixt.
 *
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public abstract class TypeBindingStrategy
{

    /**
     * The default Betwixt <code>TypeBindingStrategy</code> implementation.
     * Since the default implementation has no state,
     * a singleton instance can be provided.
     */
    public static final TypeBindingStrategy DEFAULT = new Default();

    /**
     * Gets the binding type to be used for the given Java type.
     * @param type <code>Class</code> for which the binding type is to be determined,
     * not null
     * @return <code>BindingType</code> enumeration indicating the type of binding,
     * not null
     */
    public abstract BindingType bindingType(Class type);


    /**
     * Enumerates the possible general ways that Betwixt can map a Java type to an XML type.
     * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
     */
    public static final class BindingType implements Serializable
    {

        private static final int COMPLEX_INDICATOR = 1;
        private static final int PRIMITIVE_INDICATOR = 2;

        /**
         * Indicates that the java type should be bound to a complex xml type.
         * A complex xml type may have child elements and attributes.
         * Betwixt determines the mapping for a java bean bound to a complex type.
         */
        public static final BindingType COMPLEX = new BindingType(COMPLEX_INDICATOR);

        /**
         * Indicates that the type should be bound as a Java primitive.
         * Betwixt may bind this to an attribute or a simple xml type.
         * Which is determined by the configuration for binding primitives.
         */
        public static final BindingType PRIMITIVE = new BindingType(PRIMITIVE_INDICATOR);

        private int type;

        private BindingType(int type)
        {
            this.type = type;
        }


        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object object)
        {
            boolean result = false;
            if (object instanceof BindingType)
            {
                BindingType bindingType = (BindingType) object;
                result = (type == bindingType.type);
            }
            return result;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        public int hashCode()
        {
            return type;
        }

        /**
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("BindingType: ");
            switch (type)
            {
            case (COMPLEX_INDICATOR):
                buffer.append("COMPLEX");
                break;

            case (PRIMITIVE_INDICATOR):
                buffer.append("PRIMITIVE");
                break;
            }

            return buffer.toString();
        }
    }

    /**
     * The default <code>TypeBindingStrategy</code> used by Betwixt.
     * This implementation recognizes all the usual Java primitive wrappers
     * (plus a few more that will in most typical use cases be regarded in the same way).
     * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
     */
    public static final class Default extends TypeBindingStrategy
    {

        /**
         * Class who are simple and whose subclass are also simple
         */
        private static final Class[] INHERITED_SIMPLE =
        {
            Number.class,
            String.class,
            Date.class,
            java.sql.Date.class,
            java.sql.Time.class,
            java.sql.Timestamp.class,
            java.math.BigDecimal.class,
            java.math.BigInteger.class
        };

        /**
         * Classes who are complex and whose subclasses are also complex
         */
        private static final Class[] INHERITED_COMPLEX =
        {
            Throwable.class
        };

        /**
         * Gets the binding type to be used for the given Java type.
         * This implementation recognizes all the usual Java primitive wrappers
         * (plus a few more that will in most typical use cases be regarded in the same way).
         * @param type <code>Class</code> for which the binding type is to be determined,
         * not null
         * @return <code>BindingType</code> enumeration indicating the type of binding,
         * not null
         */
        public BindingType bindingType(Class type)
        {
            BindingType result =  BindingType.COMPLEX;
            if (isStandardPrimitive(type))
            {
                result = BindingType.PRIMITIVE;
            }

            return result;
        }

        /**
         * is the given type one of the standard Betwixt primitives?
         * @param type <code>Class</code>, not null
         * @return true if the type is one of the standard Betwixt primitives
         */
        protected boolean isStandardPrimitive(Class type)
        {
            if ( type == null )
            {
                return false;

            }
            else if ( type.isPrimitive() )
            {
                return true;

            }
            else if ( type.equals( Object.class ) )
            {
                return false;
            }
            for ( int i=0, size=INHERITED_SIMPLE.length; i<size; i++ )
            {
                if ( INHERITED_SIMPLE[i].equals( type ) )
                {
                    return true;
                }
            }

            for ( int i=0, size=INHERITED_COMPLEX.length; i<size; i++ )
            {
                if ( INHERITED_COMPLEX[i].equals( type ) )
                {
                    return false;
                }
            }

            for ( int i=0, size=INHERITED_COMPLEX.length; i<size; i++ )
            {
                if ( INHERITED_COMPLEX[i].isAssignableFrom( type ) )
                {
                    return false;
                }
            }

            if (type.getName().startsWith( "java.lang." ))
            {
                return true;
            }

            for ( int i=0, size=INHERITED_SIMPLE.length; i<size; i++ )
            {
                if ( INHERITED_SIMPLE[i].isAssignableFrom( type ) )
                {
                    return true;
                }
            }
            return false;
        }
    }
}
