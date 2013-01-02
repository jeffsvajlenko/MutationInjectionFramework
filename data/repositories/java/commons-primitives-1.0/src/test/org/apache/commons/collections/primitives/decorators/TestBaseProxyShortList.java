/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/test/org/apache/commons/collections/primitives/decorators/TestBaseProxyShortList.java,v 1.1 2003/10/29 18:57:15 rwaldhoff Exp $
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
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

package org.apache.commons.collections.primitives.decorators;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortList;
import org.apache.commons.collections.primitives.ShortListIterator;

/**
 * @version $Revision: 1.1 $ $Date: 2003/10/29 18:57:15 $
 * @author Rodney Waldhoff
 */
public class TestBaseProxyShortList extends TestCase
{

    // conventional
    // ------------------------------------------------------------------------

    public TestBaseProxyShortList(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(TestBaseProxyShortList.class);
    }

    // tests
    // ------------------------------------------------------------------------

    public void testListCallsAreProxied()
    {
        final InvocationCounter proxied = new InvocationCounter();
        BaseProxyShortList list = new BaseProxyShortList()
        {
            protected ShortList getProxiedList()
            {
                return proxied;
            }
        };

        assertSame(list.getProxiedList(),list.getProxiedCollection());

        assertEquals(0,proxied.getAddCount());
        list.add(1,(short)1);
        assertEquals(1,proxied.getAddCount());

        assertEquals(0,proxied.getAddAllCount());
        list.addAll(1,null);
        assertEquals(1,proxied.getAddAllCount());

        assertEquals(0,proxied.getGetCount());
        list.get(1);
        assertEquals(1,proxied.getGetCount());

        assertEquals(0,proxied.getIndexOfCount());
        list.indexOf((short)1);
        assertEquals(1,proxied.getIndexOfCount());

        assertEquals(0,proxied.getLastIndexOfCount());
        list.lastIndexOf((short)1);
        assertEquals(1,proxied.getLastIndexOfCount());

        assertEquals(0,proxied.getListIteratorCount());
        list.listIterator();
        assertEquals(1,proxied.getListIteratorCount());

        assertEquals(0,proxied.getListIteratorFromCount());
        list.listIterator(1);
        assertEquals(1,proxied.getListIteratorFromCount());

        assertEquals(0,proxied.getRemoveElementAtCount());
        list.removeElementAt(1);
        assertEquals(1,proxied.getRemoveElementAtCount());

        assertEquals(0,proxied.getSetCount());
        list.set(1,(short)1);
        assertEquals(1,proxied.getSetCount());

        assertEquals(0,proxied.getSubListCount());
        list.subList(1,2);
        assertEquals(1,proxied.getSubListCount());
    }

    // inner classes
    // ------------------------------------------------------------------------

    static class InvocationCounter extends TestBaseProxyShortCollection.InvocationCounter implements ShortList
    {
        private int addCount;
        private int addAllCount;
        private int getCount;
        private int indexOfCount;
        private int lastIndexOfCount;
        private int listIteratorCount;
        private int listIteratorFromCount;
        private int removeElementAtCount;
        private int setCount;
        private int subListCount;

        public void add(int index, short element)
        {
            addCount++;
        }

        public boolean addAll(int index, ShortCollection collection)
        {
            addAllCount++;
            return false;
        }

        public short get(int index)
        {
            getCount++;
            return 0;
        }

        public int indexOf(short element)
        {
            indexOfCount++;
            return 0;
        }

        public int lastIndexOf(short element)
        {
            lastIndexOfCount++;
            return 0;
        }

        public ShortListIterator listIterator()
        {
            listIteratorCount++;
            return null;
        }

        public ShortListIterator listIterator(int index)
        {
            listIteratorFromCount++;
            return null;
        }

        public short removeElementAt(int index)
        {
            removeElementAtCount++;
            return 0;
        }

        public short set(int index, short element)
        {
            setCount++;
            return 0;
        }

        public ShortList subList(int fromIndex, int toIndex)
        {
            subListCount++;
            return null;
        }

        public int getAddAllCount()
        {
            return addAllCount;
        }

        public int getAddCount()
        {
            return addCount;
        }

        public int getGetCount()
        {
            return getCount;
        }

        public int getIndexOfCount()
        {
            return indexOfCount;
        }

        public int getLastIndexOfCount()
        {
            return lastIndexOfCount;
        }

        public int getListIteratorCount()
        {
            return listIteratorCount;
        }

        public int getListIteratorFromCount()
        {
            return listIteratorFromCount;
        }

        public int getRemoveElementAtCount()
        {
            return removeElementAtCount;
        }

        public int getSetCount()
        {
            return setCount;
        }

        public int getSubListCount()
        {
            return subListCount;
        }

    }
}
