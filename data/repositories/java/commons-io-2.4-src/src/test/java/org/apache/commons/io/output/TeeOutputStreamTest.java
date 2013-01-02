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
package org.apache.commons.io.output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.junit.Assert;

/**
 * @version $Id: TeeOutputStreamTest.java 1302056 2012-03-18 03:03:38Z ggregory $
 */

public class TeeOutputStreamTest extends TestCase
{

    private static class ExceptionOnCloseByteArrayOutputStream extends ByteArrayOutputStream
    {

        @Override
        public void close() throws IOException
        {
            throw new IOException();
        }
    }

    private static class RecordCloseByteArrayOutputStream extends ByteArrayOutputStream
    {

        boolean closed;

        @Override
        public void close() throws IOException
        {
            super.close();
            closed = true;
        }
    }

    public TeeOutputStreamTest(String name)
    {
        super(name);
    }

    /**
     * Tests that the branch {@code OutputStream} is closed when closing the main {@code OutputStream} throws an
     * exception on {@link TeeOutputStream#close()}.
     */
    public void testCloseBranchIOException()
    {
        ByteArrayOutputStream badOs = new ExceptionOnCloseByteArrayOutputStream();
        RecordCloseByteArrayOutputStream goodOs = new RecordCloseByteArrayOutputStream();
        TeeOutputStream tos = new TeeOutputStream(goodOs, badOs);
        try
        {
            tos.close();
            Assert.fail("Expected " + IOException.class.getName());
        }
        catch (IOException e)
        {
            Assert.assertTrue(goodOs.closed);
        }
    }

    /**
     * Tests that the main {@code OutputStream} is closed when closing the branch {@code OutputStream} throws an
     * exception on {@link TeeOutputStream#close()}.
     */
    public void testCloseMainIOException()
    {
        ByteArrayOutputStream badOs = new ExceptionOnCloseByteArrayOutputStream();
        RecordCloseByteArrayOutputStream goodOs = new RecordCloseByteArrayOutputStream();
        TeeOutputStream tos = new TeeOutputStream(badOs, goodOs);
        try
        {
            tos.close();
            Assert.fail("Expected " + IOException.class.getName());
        }
        catch (IOException e)
        {
            Assert.assertTrue(goodOs.closed);
        }
    }

    public void testTee() throws IOException
    {
        ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        TeeOutputStream tos = new TeeOutputStream(baos1, baos2);
        for (int i = 0; i < 20; i++)
        {
            tos.write(i);
        }
        assertByteArrayEquals("TeeOutputStream.write(int)", baos1.toByteArray(), baos2.toByteArray());

        byte[] array = new byte[10];
        for (int i = 20; i < 30; i++)
        {
            array[i - 20] = (byte) i;
        }
        tos.write(array);
        assertByteArrayEquals("TeeOutputStream.write(byte[])", baos1.toByteArray(), baos2.toByteArray());

        for (int i = 25; i < 35; i++)
        {
            array[i - 25] = (byte) i;
        }
        tos.write(array, 5, 5);
        assertByteArrayEquals("TeeOutputStream.write(byte[], int, int)", baos1.toByteArray(), baos2.toByteArray());
        tos.flush();
        tos.close();
    }

    private void assertByteArrayEquals(String msg, byte[] array1, byte[] array2)
    {
        assertEquals(msg + ": array size mismatch", array1.length, array2.length);
        for (int i = 0; i < array1.length; i++)
        {
            assertEquals(msg + ": array[ " + i + "] mismatch", array1[i], array2[i]);
        }
    }

}
