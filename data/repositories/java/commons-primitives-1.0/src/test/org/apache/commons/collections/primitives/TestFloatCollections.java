/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/test/org/apache/commons/collections/primitives/TestFloatCollections.java,v 1.1 2003/10/29 20:07:55 rwaldhoff Exp $
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
 * USE, DATA, OR PROFITS; OR BUSINESS FLOATERRUPTION) HOWEVER CAUSED AND
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @version $Revision: 1.1 $ $Date: 2003/10/29 20:07:55 $
 * @author Rodney Waldhoff
 */
public class TestFloatCollections extends TestCase
{

    //------------------------------------------------------------ Conventional

    public TestFloatCollections(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(TestFloatCollections.class);
    }

    //---------------------------------------------------------------- Tests

    public void testSingletonFloatListIterator()
    {
        FloatListIterator iter = FloatCollections.singletonFloatListIterator((float)17);
        assertTrue(!iter.hasPrevious());
        assertTrue(iter.hasNext());
        assertEquals(17,iter.next(),(float)0);
        assertTrue(iter.hasPrevious());
        assertTrue(!iter.hasNext());
        assertEquals(17,iter.previous(),(float)0);
        try
        {
            iter.set((float)18);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testSingletonFloatIterator()
    {
        FloatIterator iter = FloatCollections.singletonFloatIterator((float)17);
        assertTrue(iter.hasNext());
        assertEquals(17,iter.next(),(float)0);
        assertTrue(!iter.hasNext());
        try
        {
            iter.remove();
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testSingletonFloatList()
    {
        FloatList list = FloatCollections.singletonFloatList((float)17);
        assertEquals(1,list.size());
        assertEquals(17,list.get(0),(float)0);
        try
        {
            list.add((float)18);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testUnmodifiableFloatListNull()
    {
        try
        {
            FloatCollections.unmodifiableFloatList(null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {
            // expected
        }
    }

    public void testEmptyFloatList()
    {
        assertSame(FloatCollections.EMPTY_FLOAT_LIST,FloatCollections.getEmptyFloatList());
        assertTrue(FloatCollections.EMPTY_FLOAT_LIST.isEmpty());
        try
        {
            FloatCollections.EMPTY_FLOAT_LIST.add((float)1);
            fail("Expected UnsupportedOperationExcpetion");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testUnmodifiableFloatIteratorNull()
    {
        try
        {
            FloatCollections.unmodifiableFloatIterator(null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {
            // expected
        }
    }

    public void testEmptyFloatIterator()
    {
        assertSame(FloatCollections.EMPTY_FLOAT_ITERATOR,FloatCollections.getEmptyFloatIterator());
        assertTrue(! FloatCollections.EMPTY_FLOAT_ITERATOR.hasNext());
        try
        {
            FloatCollections.EMPTY_FLOAT_ITERATOR.remove();
            fail("Expected UnsupportedOperationExcpetion");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testUnmodifiableFloatListIteratorNull()
    {
        try
        {
            FloatCollections.unmodifiableFloatListIterator(null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {
            // expected
        }
    }

    public void testEmptyFloatListIterator()
    {
        assertSame(FloatCollections.EMPTY_FLOAT_LIST_ITERATOR,FloatCollections.getEmptyFloatListIterator());
        assertTrue(! FloatCollections.EMPTY_FLOAT_LIST_ITERATOR.hasNext());
        assertTrue(! FloatCollections.EMPTY_FLOAT_LIST_ITERATOR.hasPrevious());
        try
        {
            FloatCollections.EMPTY_FLOAT_LIST_ITERATOR.add((float)1);
            fail("Expected UnsupportedOperationExcpetion");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }
}

