/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/test/org/apache/commons/collections/primitives/TestCharCollections.java,v 1.1 2003/10/29 19:20:07 rwaldhoff Exp $
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
 * USE, DATA, OR PROFITS; OR BUSINESS CHARERRUPTION) HOWEVER CAUSED AND
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
 * @version $Revision: 1.1 $ $Date: 2003/10/29 19:20:07 $
 * @author Rodney Waldhoff
 */
public class TestCharCollections extends TestCase
{

    //------------------------------------------------------------ Conventional

    public TestCharCollections(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(TestCharCollections.class);
    }

    //---------------------------------------------------------------- Tests

    public void testSingletonCharListIterator()
    {
        CharListIterator iter = CharCollections.singletonCharListIterator((char)17);
        assertTrue(!iter.hasPrevious());
        assertTrue(iter.hasNext());
        assertEquals(17,iter.next());
        assertTrue(iter.hasPrevious());
        assertTrue(!iter.hasNext());
        assertEquals(17,iter.previous());
        try
        {
            iter.set((char)18);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testSingletonCharIterator()
    {
        CharIterator iter = CharCollections.singletonCharIterator((char)17);
        assertTrue(iter.hasNext());
        assertEquals(17,iter.next());
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

    public void testSingletonCharList()
    {
        CharList list = CharCollections.singletonCharList((char)17);
        assertEquals(1,list.size());
        assertEquals(17,list.get(0));
        try
        {
            list.add((char)18);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testUnmodifiableCharListNull()
    {
        try
        {
            CharCollections.unmodifiableCharList(null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {
            // expected
        }
    }

    public void testEmptyCharList()
    {
        assertSame(CharCollections.EMPTY_CHAR_LIST,CharCollections.getEmptyCharList());
        assertTrue(CharCollections.EMPTY_CHAR_LIST.isEmpty());
        try
        {
            CharCollections.EMPTY_CHAR_LIST.add((char)1);
            fail("Expected UnsupportedOperationExcpetion");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testUnmodifiableCharIteratorNull()
    {
        try
        {
            CharCollections.unmodifiableCharIterator(null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {
            // expected
        }
    }

    public void testEmptyCharIterator()
    {
        assertSame(CharCollections.EMPTY_CHAR_ITERATOR,CharCollections.getEmptyCharIterator());
        assertTrue(! CharCollections.EMPTY_CHAR_ITERATOR.hasNext());
        try
        {
            CharCollections.EMPTY_CHAR_ITERATOR.remove();
            fail("Expected UnsupportedOperationExcpetion");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    public void testUnmodifiableCharListIteratorNull()
    {
        try
        {
            CharCollections.unmodifiableCharListIterator(null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {
            // expected
        }
    }

    public void testEmptyCharListIterator()
    {
        assertSame(CharCollections.EMPTY_CHAR_LIST_ITERATOR,CharCollections.getEmptyCharListIterator());
        assertTrue(! CharCollections.EMPTY_CHAR_LIST_ITERATOR.hasNext());
        assertTrue(! CharCollections.EMPTY_CHAR_LIST_ITERATOR.hasPrevious());
        try
        {
            CharCollections.EMPTY_CHAR_LIST_ITERATOR.add((char)1);
            fail("Expected UnsupportedOperationExcpetion");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }
}

