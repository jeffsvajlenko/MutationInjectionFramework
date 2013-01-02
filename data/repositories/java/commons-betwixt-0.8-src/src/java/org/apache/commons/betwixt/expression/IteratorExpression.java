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

package org.apache.commons.betwixt.expression;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


/** <p><code>IteratorExpression</code> returns an iterator over the current context.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class IteratorExpression implements Expression
{

    /** Use this <code>Expression</code> to perform initial evaluation*/
    private Expression expression;

    /**
     * Construct <code>IteratorExpression</code> using given expression for initial evaluation.
     * @param expression this expression will be evaluated and the result converted to an
     *        iterator.
     */
    public IteratorExpression(Expression expression)
    {
        this.expression = expression;
    }

    /**
     * Returns an interator over the current context
     * @see org.apache.commons.betwixt.expression.Expression
     */
    public Object evaluate(Context context)
    {
        // evaluate wrapped expression against context
        Object value = expression.evaluate( context );

        // based on the class of the result,
        // return an appropriate iterator
        if ( value instanceof Iterator )
        {
            // if the value is an iterator, we're done
            return (Iterator) value;

        }
        else if ( value instanceof Collection )
        {
            // if it's a collection, return an iterator for that collection
            Collection collection = (Collection) value;
            return collection.iterator();

        }
        else if ( value instanceof Map )
        {
            // if it's a map, return an iterator for the map entries
            Map map = (Map) value;
            return map.entrySet().iterator();

        }
        else if ( value instanceof Enumeration )
        {
            // if it's an enumeration, wrap it in an EnumerationIterator
            return new EnumerationIterator( (Enumeration) value );

        }
        else if ( value != null )
        {
            // if we have an array return an ArrayIterator
            Class type = value.getClass();
            if ( type.isArray() )
            {
                return new ArrayIterator( value );
            }
        }

        // we've got something we can't deal with
        // so return an empty iterator
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * Do nothing
     * @see org.apache.commons.betwixt.expression.Expression
     */
    public void update(Context context, String newValue)
    {
        // do nothing
    }

    /**
     * Returns something useful for logging
     * @return string useful for logging
     */
    public String toString()
    {
        return "IteratorExpression [expression=" + expression + "]";
    }


    /**
     * <code>ArrayIterator</code> originated in commons-collections. Added
     * as a private inner class to break dependency.
     *
     * @author James Strachan
     * @author Mauricio S. Moura
     * @author Michael A. Smith
     * @author Neil O'Toole
     * @author Stephen Colebourne
     */
    private static final class ArrayIterator implements Iterator
    {

        /** The array to iterate over */
        protected Object array;

        /** The start index to loop from */
        protected int startIndex = 0;

        /** The end index to loop to */
        protected int endIndex = 0;

        /** The current iterator index */
        protected int index = 0;

        // Constructors
        // ----------------------------------------------------------------------
        /**
         * Constructor for use with <code>setArray</code>.
         * <p>
         * Using this constructor, the iterator is equivalent to an empty
         * iterator until {@link #setArray(Object)}is called to establish the
         * array to iterate over.
         */
        public ArrayIterator()
        {
            super();
        }

        /**
         * Constructs an ArrayIterator that will iterate over the values in the
         * specified array.
         *
         * @param array
         *            the array to iterate over.
         * @throws IllegalArgumentException
         *             if <code>array</code> is not an array.
         * @throws NullPointerException
         *             if <code>array</code> is <code>null</code>
         */
        public ArrayIterator(final Object array)
        {
            super();
            setArray(array);
        }

        /**
         * Constructs an ArrayIterator that will iterate over the values in the
         * specified array from a specific start index.
         *
         * @param array
         *            the array to iterate over.
         * @param startIndex
         *            the index to start iterating at.
         * @throws IllegalArgumentException
         *             if <code>array</code> is not an array.
         * @throws NullPointerException
         *             if <code>array</code> is <code>null</code>
         * @throws IndexOutOfBoundsException
         *             if the index is invalid
         */
        public ArrayIterator(final Object array, final int startIndex)
        {
            super();
            setArray(array);
            checkBound(startIndex, "start");
            this.startIndex = startIndex;
            this.index = startIndex;
        }

        /**
         * Construct an ArrayIterator that will iterate over a range of values
         * in the specified array.
         *
         * @param array
         *            the array to iterate over.
         * @param startIndex
         *            the index to start iterating at.
         * @param endIndex
         *            the index to finish iterating at.
         * @throws IllegalArgumentException
         *             if <code>array</code> is not an array.
         * @throws NullPointerException
         *             if <code>array</code> is <code>null</code>
         * @throws IndexOutOfBoundsException
         *             if either index is invalid
         */
        public ArrayIterator(final Object array, final int startIndex,
                             final int endIndex)
        {
            super();
            setArray(array);
            checkBound(startIndex, "start");
            checkBound(endIndex, "end");
            if (endIndex < startIndex)
            {
                throw new IllegalArgumentException(
                    "End index must not be less than start index.");
            }
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.index = startIndex;
        }

        /**
         * Checks whether the index is valid or not.
         *
         * @param bound
         *            the index to check
         * @param type
         *            the index type (for error messages)
         * @throws IndexOutOfBoundsException
         *             if the index is invalid
         */
        protected void checkBound(final int bound, final String type)
        {
            if (bound > this.endIndex)
            {
                throw new ArrayIndexOutOfBoundsException(
                    "Attempt to make an ArrayIterator that " + type
                    + "s beyond the end of the array. ");
            }
            if (bound < 0)
            {
                throw new ArrayIndexOutOfBoundsException(
                    "Attempt to make an ArrayIterator that " + type
                    + "s before the start of the array. ");
            }
        }

        // Iterator interface
        //-----------------------------------------------------------------------
        /**
         * Returns true if there are more elements to return from the array.
         *
         * @return true if there is a next element to return
         */
        public boolean hasNext()
        {
            return (index < endIndex);
        }

        /**
         * Returns the next element in the array.
         *
         * @return the next element in the array
         * @throws NoSuchElementException
         *             if all the elements in the array have already been
         *             returned
         */
        public Object next()
        {
            if (hasNext() == false)
            {
                throw new NoSuchElementException();
            }
            return Array.get(array, index++);
        }

        /**
         * Throws {@link UnsupportedOperationException}.
         *
         * @throws UnsupportedOperationException
         *             always
         */
        public void remove()
        {
            throw new UnsupportedOperationException(
                "remove() method is not supported");
        }

        // Properties
        //-----------------------------------------------------------------------
        /**
         * Gets the array that this iterator is iterating over.
         *
         * @return the array this iterator iterates over, or <code>null</code>
         *         if the no-arg constructor was used and
         *         {@link #setArray(Object)}has never been called with a valid
         *         array.
         */
        public Object getArray()
        {
            return array;
        }

        /**
         * Sets the array that the ArrayIterator should iterate over.
         * <p>
         * If an array has previously been set (using the single-arg constructor
         * or this method) then that array is discarded in favour of this one.
         * Iteration is restarted at the start of the new array. Although this
         * can be used to reset iteration, the {@link #reset()}method is a more
         * effective choice.
         *
         * @param array
         *            the array that the iterator should iterate over.
         * @throws IllegalArgumentException
         *             if <code>array</code> is not an array.
         * @throws NullPointerException
         *             if <code>array</code> is <code>null</code>
         */
        public void setArray(final Object array)
        {
            // Array.getLength throws IllegalArgumentException if the object is
            // not
            // an array or NullPointerException if the object is null. This call
            // is made before saving the array and resetting the index so that
            // the
            // array iterator remains in a consistent state if the argument is
            // not
            // an array or is null.
            this.endIndex = Array.getLength(array);
            this.startIndex = 0;
            this.array = array;
            this.index = 0;
        }

        /**
         * Resets the iterator back to the start index.
         */
        public void reset()
        {
            this.index = this.startIndex;
        }

    }


    /**
     * Adapter to make {@link Enumeration Enumeration}instances appear to be
     * {@link Iterator Iterator}instances. Originated in commons-collections.
     * Added as a private inner class to break dependency.
     *
     * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
     * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall </a>
     */
    private static final class EnumerationIterator implements Iterator
    {

        /** The collection to remove elements from */
        private Collection collection;

        /** The enumeration being converted */
        private Enumeration enumeration;

        /** The last object retrieved */
        private Object last;

        // Constructors
        //-----------------------------------------------------------------------
        /**
         * Constructs a new <code>EnumerationIterator</code> that will not
         * function until {@link #setEnumeration(Enumeration)} is called.
         */
        public EnumerationIterator()
        {
            this(null, null);
        }

        /**
         * Constructs a new <code>EnumerationIterator</code> that provides
         * an iterator view of the given enumeration.
         *
         * @param enumeration  the enumeration to use
         */
        public EnumerationIterator(final Enumeration enumeration)
        {
            this(enumeration, null);
        }

        /**
         * Constructs a new <code>EnumerationIterator</code> that will remove
         * elements from the specified collection.
         *
         * @param enumeration  the enumeration to use
         * @param collection  the collection to remove elements form
         */
        public EnumerationIterator(final Enumeration enumeration,
                                   final Collection collection)
        {
            super();
            this.enumeration = enumeration;
            this.collection = collection;
            this.last = null;
        }

        // Iterator interface
        //-----------------------------------------------------------------------
        /**
         * Returns true if the underlying enumeration has more elements.
         *
         * @return true if the underlying enumeration has more elements
         * @throws NullPointerException  if the underlying enumeration is null
         */
        public boolean hasNext()
        {
            return enumeration.hasMoreElements();
        }

        /**
         * Returns the next object from the enumeration.
         *
         * @return the next object from the enumeration
         * @throws NullPointerException if the enumeration is null
         */
        public Object next()
        {
            last = enumeration.nextElement();
            return last;
        }

        /**
         * Removes the last retrieved element if a collection is attached.
         * <p>
         * Functions if an associated <code>Collection</code> is known.
         * If so, the first occurrence of the last returned object from this
         * iterator will be removed from the collection.
         *
         * @exception IllegalStateException <code>next()</code> not called.
         * @exception UnsupportedOperationException if no associated collection
         */
        public void remove()
        {
            if (collection != null)
            {
                if (last != null)
                {
                    collection.remove(last);
                }
                else
                {
                    throw new IllegalStateException(
                        "next() must have been called for remove() to function");
                }
            }
            else
            {
                throw new UnsupportedOperationException(
                    "No Collection associated with this Iterator");
            }
        }

        // Properties
        //-----------------------------------------------------------------------
        /**
         * Returns the underlying enumeration.
         *
         * @return the underlying enumeration
         */
        public Enumeration getEnumeration()
        {
            return enumeration;
        }

        /**
         * Sets the underlying enumeration.
         *
         * @param enumeration  the new underlying enumeration
         */
        public void setEnumeration(final Enumeration enumeration)
        {
            this.enumeration = enumeration;
        }
    }

}
