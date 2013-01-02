/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/java/org/apache/commons/collections/primitives/RandomAccessShortList.java,v 1.2 2003/10/14 20:04:26 scolebourne Exp $
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.commons.collections.primitives;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Abstract base class for {@link ShortList}s backed
 * by random access structures like arrays.
 * <p />
 * Read-only subclasses must override {@link #get}
 * and {@link #size}.  Mutable subclasses
 * should also override {@link #set}.  Variably-sized
 * subclasses should also override {@link #add}
 * and {@link #removeElementAt}.  All other methods
 * have at least some base implementation derived from
 * these.  Subclasses may choose to override these methods
 * to provide a more efficient implementation.
 *
 * @since Commons Primitives 0.1
 * @version $Revision: 1.2 $ $Date: 2003/10/14 20:04:26 $
 *
 * @author Rodney Waldhoff
 */
public abstract class RandomAccessShortList extends AbstractShortCollection implements ShortList
{

    // constructors
    //-------------------------------------------------------------------------

    /** Constructs an empty list. */
    protected RandomAccessShortList()
    {
    }

    // fully abstract methods
    //-------------------------------------------------------------------------

    public abstract short get(int index);
    public abstract int size();

    // unsupported in base
    //-------------------------------------------------------------------------

    /**
     * Unsupported in this implementation.
     * @throws UnsupportedOperationException since this method is not supported
     */
    public short removeElementAt(int index)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported in this implementation.
     * @throws UnsupportedOperationException since this method is not supported
     */
    public short set(int index, short element)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported in this implementation.
     * @throws UnsupportedOperationException since this method is not supported
     */
    public void add(int index, short element)
    {
        throw new UnsupportedOperationException();
    }

    //-------------------------------------------------------------------------

    // javadocs here are inherited

    public boolean add(short element)
    {
        add(size(),element);
        return true;
    }

    public boolean addAll(int index, ShortCollection collection)
    {
        boolean modified = false;
        for(ShortIterator iter = collection.iterator(); iter.hasNext(); )
        {
            add(index++,iter.next());
            modified = true;
        }
        return modified;
    }

    public int indexOf(short element)
    {
        int i = 0;
        for(ShortIterator iter = iterator(); iter.hasNext(); )
        {
            if(iter.next() == element)
            {
                return i;
            }
            else
            {
                i++;
            }
        }
        return -1;
    }

    public int lastIndexOf(short element)
    {
        for(ShortListIterator iter = listIterator(size()); iter.hasPrevious(); )
        {
            if(iter.previous() == element)
            {
                return iter.nextIndex();
            }
        }
        return -1;
    }

    public ShortIterator iterator()
    {
        return listIterator();
    }

    public ShortListIterator listIterator()
    {
        return listIterator(0);
    }

    public ShortListIterator listIterator(int index)
    {
        return new RandomAccessShortListIterator(this,index);
    }

    public ShortList subList(int fromIndex, int toIndex)
    {
        return new RandomAccessShortSubList(this,fromIndex,toIndex);
    }

    public boolean equals(Object that)
    {
        if(this == that)
        {
            return true;
        }
        else if(that instanceof ShortList)
        {
            ShortList thatList = (ShortList)that;
            if(size() != thatList.size())
            {
                return false;
            }
            for(ShortIterator thatIter = thatList.iterator(), thisIter = iterator(); thisIter.hasNext();)
            {
                if(thisIter.next() != thatIter.next())
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        int hash = 1;
        for(ShortIterator iter = iterator(); iter.hasNext(); )
        {
            hash = 31*hash + iter.next();
        }
        return hash;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("[");
        for(ShortIterator iter = iterator(); iter.hasNext();)
        {
            buf.append(iter.next());
            if(iter.hasNext())
            {
                buf.append(", ");
            }
        }
        buf.append("]");
        return buf.toString();
    }

    // protected utilities
    //-------------------------------------------------------------------------

    /** Get my count of structural modifications. */
    protected int getModCount()
    {
        return _modCount;
    }

    /** Increment my count of structural modifications. */
    protected void incrModCount()
    {
        _modCount++;
    }

    // attributes
    //-------------------------------------------------------------------------

    private int _modCount = 0;

    // inner classes
    //-------------------------------------------------------------------------

    private static class ComodChecker
    {
        ComodChecker(RandomAccessShortList source)
        {
            _source = source;
            resyncModCount();
        }

        protected RandomAccessShortList getList()
        {
            return _source;
        }

        protected void assertNotComodified() throws ConcurrentModificationException
        {
            if(_expectedModCount != getList().getModCount())
            {
                throw new ConcurrentModificationException();
            }
        }

        protected void resyncModCount()
        {
            _expectedModCount = getList().getModCount();
        }

        private RandomAccessShortList _source = null;
        private int _expectedModCount = -1;
    }

    protected static class RandomAccessShortListIterator extends ComodChecker implements ShortListIterator
    {
        RandomAccessShortListIterator(RandomAccessShortList list, int index)
        {
            super(list);
            if(index < 0 || index > getList().size())
            {
                throw new IndexOutOfBoundsException("Index " + index + " not in [0," + getList().size() + ")");
            }
            else
            {
                _nextIndex = index;
                resyncModCount();
            }
        }

        public boolean hasNext()
        {
            assertNotComodified();
            return _nextIndex < getList().size();
        }

        public boolean hasPrevious()
        {
            assertNotComodified();
            return _nextIndex > 0;
        }

        public int nextIndex()
        {
            assertNotComodified();
            return _nextIndex;
        }

        public int previousIndex()
        {
            assertNotComodified();
            return _nextIndex - 1;
        }

        public short next()
        {
            assertNotComodified();
            if(!hasNext())
            {
                throw new NoSuchElementException();
            }
            else
            {
                short val = getList().get(_nextIndex);
                _lastReturnedIndex = _nextIndex;
                _nextIndex++;
                return val;
            }
        }

        public short previous()
        {
            assertNotComodified();
            if(!hasPrevious())
            {
                throw new NoSuchElementException();
            }
            else
            {
                short val = getList().get(_nextIndex-1);
                _lastReturnedIndex = _nextIndex-1;
                _nextIndex--;
                return val;
            }
        }

        public void add(short value)
        {
            assertNotComodified();
            getList().add(_nextIndex,value);
            _nextIndex++;
            _lastReturnedIndex = -1;
            resyncModCount();
        }

        public void remove()
        {
            assertNotComodified();
            if(-1 == _lastReturnedIndex)
            {
                throw new IllegalStateException();
            }
            else
            {
                getList().removeElementAt(_lastReturnedIndex);
                _lastReturnedIndex = -1;
                _nextIndex--;
                resyncModCount();
            }
        }

        public void set(short value)
        {
            assertNotComodified();
            if(-1 == _lastReturnedIndex)
            {
                throw new IllegalStateException();
            }
            else
            {
                getList().set(_lastReturnedIndex,value);
                resyncModCount();
            }
        }

        private int _nextIndex = 0;
        private int _lastReturnedIndex = -1;
    }

    protected static class RandomAccessShortSubList extends RandomAccessShortList implements ShortList
    {
        RandomAccessShortSubList(RandomAccessShortList list, int fromIndex, int toIndex)
        {
            if(fromIndex < 0 || toIndex > list.size())
            {
                throw new IndexOutOfBoundsException();
            }
            else if(fromIndex > toIndex)
            {
                throw new IllegalArgumentException();
            }
            else
            {
                _list = list;
                _offset = fromIndex;
                _limit = toIndex - fromIndex;
                _comod = new ComodChecker(list);
                _comod.resyncModCount();
            }
        }

        public short get(int index)
        {
            checkRange(index);
            _comod.assertNotComodified();
            return _list.get(toUnderlyingIndex(index));
        }

        public short removeElementAt(int index)
        {
            checkRange(index);
            _comod.assertNotComodified();
            short val = _list.removeElementAt(toUnderlyingIndex(index));
            _limit--;
            _comod.resyncModCount();
            incrModCount();
            return val;
        }

        public short set(int index, short element)
        {
            checkRange(index);
            _comod.assertNotComodified();
            short val = _list.set(toUnderlyingIndex(index),element);
            incrModCount();
            _comod.resyncModCount();
            return val;
        }

        public void add(int index, short element)
        {
            checkRangeIncludingEndpoint(index);
            _comod.assertNotComodified();
            _list.add(toUnderlyingIndex(index),element);
            _limit++;
            _comod.resyncModCount();
            incrModCount();
        }

        public int size()
        {
            _comod.assertNotComodified();
            return _limit;
        }

        private void checkRange(int index)
        {
            if(index < 0 || index >= size())
            {
                throw new IndexOutOfBoundsException("index " + index + " not in [0," + size() + ")");
            }
        }

        private void checkRangeIncludingEndpoint(int index)
        {
            if(index < 0 || index > size())
            {
                throw new IndexOutOfBoundsException("index " + index + " not in [0," + size() + "]");
            }
        }

        private int toUnderlyingIndex(int index)
        {
            return (index + _offset);
        }

        private int _offset = 0;
        private int _limit = 0;
        private RandomAccessShortList _list = null;
        private ComodChecker _comod = null;

    }
}

