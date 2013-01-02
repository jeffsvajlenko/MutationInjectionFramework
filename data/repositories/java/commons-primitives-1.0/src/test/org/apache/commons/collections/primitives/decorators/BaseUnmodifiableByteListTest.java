/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/test/org/apache/commons/collections/primitives/decorators/BaseUnmodifiableByteListTest.java,v 1.1 2003/10/29 18:33:10 rwaldhoff Exp $
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

import junit.framework.TestCase;

import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.ByteList;
import org.apache.commons.collections.primitives.ByteListIterator;

/**
 * @version $Revision: 1.1 $ $Date: 2003/10/29 18:33:10 $
 * @author Rodney Waldhoff
 */
public abstract class BaseUnmodifiableByteListTest extends TestCase
{

    // conventional
    // ------------------------------------------------------------------------

    public BaseUnmodifiableByteListTest(String testName)
    {
        super(testName);
    }

    // framework
    // ------------------------------------------------------------------------

    protected abstract ByteList makeUnmodifiableByteList();

    protected ByteList makeByteList()
    {
        ByteList list = new ArrayByteList();
        for(byte i=0; i<10; i++)
        {
            list.add(i);
        }
        return list;
    }

    // tests
    // ------------------------------------------------------------------------

    public final void testNotModifiable() throws Exception
    {
        assertListNotModifiable(makeUnmodifiableByteList());
    }

    public final void testSublistNotModifiable() throws Exception
    {
        ByteList list = makeUnmodifiableByteList();
        assertListNotModifiable(list.subList(0,list.size()-2));
    }

    public final void testIteratorNotModifiable() throws Exception
    {
        ByteList list = makeUnmodifiableByteList();
        assertIteratorNotModifiable(list.iterator());
        assertIteratorNotModifiable(list.subList(0,list.size()-2).iterator());
    }

    public final void testListIteratorNotModifiable() throws Exception
    {
        ByteList list = makeUnmodifiableByteList();
        assertListIteratorNotModifiable(list.listIterator());
        assertListIteratorNotModifiable(list.subList(0,list.size()-2).listIterator());
        assertListIteratorNotModifiable(list.listIterator(1));
        assertListIteratorNotModifiable(list.subList(0,list.size()-2).listIterator(1));
    }

    // util
    // ------------------------------------------------------------------------

    private void assertListIteratorNotModifiable(ByteListIterator iter) throws Exception
    {
        assertIteratorNotModifiable(iter);

        assertTrue(iter.hasPrevious());

        try
        {
            iter.set((byte)2);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            iter.add((byte)2);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }

    private void assertIteratorNotModifiable(ByteIterator iter) throws Exception
    {
        assertTrue(iter.hasNext());
        iter.next();

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

    private void assertListNotModifiable(ByteList list) throws Exception
    {
        try
        {
            list.add((byte)1);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.add(1,(byte)2);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.addAll(makeByteList());
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.addAll(1,makeByteList());
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.removeElementAt(1);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.removeElement((byte)1);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.removeAll(makeByteList());
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.retainAll(makeByteList());
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.clear();
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            list.set(1,(byte)2);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }
    }
}
