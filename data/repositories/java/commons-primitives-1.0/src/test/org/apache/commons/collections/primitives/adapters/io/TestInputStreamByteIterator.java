/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/test/org/apache/commons/collections/primitives/adapters/io/TestInputStreamByteIterator.java,v 1.1 2003/10/13 22:46:53 scolebourne Exp $
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

package org.apache.commons.collections.primitives.adapters.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.collections.primitives.ByteIterator;
import org.apache.commons.collections.primitives.TestByteIterator;

/**
 * @version $Revision: 1.1 $ $Date: 2003/10/13 22:46:53 $
 * @author Rodney Waldhoff
 */
public class TestInputStreamByteIterator extends TestByteIterator
{

    // conventional
    // ------------------------------------------------------------------------

    public TestInputStreamByteIterator(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(TestInputStreamByteIterator.class);
    }

    // ------------------------------------------------------------------------

    public boolean supportsRemove()
    {
        return false;
    }

    protected ByteIterator makeEmptyByteIterator()
    {
        return new InputStreamByteIterator(new ByteArrayInputStream(new byte[0]));
    }

    protected ByteIterator makeFullByteIterator()
    {
        return new InputStreamByteIterator(new ByteArrayInputStream(getFullElements()));
    }

    protected byte[] getFullElements()
    {
        byte[] bytes = new byte[256];
        for(int i=0; i < 256; i++)
        {
            bytes[i] = (byte)(i-128);
        }
        return bytes;
    }


    // ------------------------------------------------------------------------

    public void testErrorThrowingStream()
    {
        InputStream errStream = new InputStream()
        {
            public int read() throws IOException
            {
                throw new IOException();
            }
        };

        ByteIterator iter = new InputStreamByteIterator(errStream);
        try
        {
            iter.hasNext();
            fail("Expected RuntimeException");
        }
        catch(RuntimeException e)
        {
            // expected
        }
        try
        {
            iter.next();
            fail("Expected RuntimeException");
        }
        catch(RuntimeException e)
        {
            // expected
        }
    }

    public void testAdaptNull()
    {
        assertNull(InputStreamByteIterator.adapt(null));
    }

    public void testAdaptNonNull()
    {
        assertNotNull(InputStreamByteIterator.adapt(new ByteArrayInputStream(new byte[0])));
    }
}
