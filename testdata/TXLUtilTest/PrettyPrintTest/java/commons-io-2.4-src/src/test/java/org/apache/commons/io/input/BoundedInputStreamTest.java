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
package org.apache.commons.io.input;

import java.io.ByteArrayInputStream;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

/**
 * Tests for {@link BoundedInputStream}.
 *
 * @version $Id: BoundedInputStreamTest.java 1302056 2012-03-18 03:03:38Z ggregory $
 */
public class BoundedInputStreamTest extends TestCase
{

    public BoundedInputStreamTest(String name)
    {
        super(name);
    }

    /**
     * Test {@link BoundedInputStream#read()}.
     */
    public void testReadSingle() throws Exception
    {
        BoundedInputStream bounded = null;
        byte[] helloWorld = "Hello World".getBytes();
        byte[] hello      = "Hello".getBytes();

        // limit = length
        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld), helloWorld.length);
        for (int i = 0; i < helloWorld.length; i++)
        {
            assertEquals("limit = length byte[" + i + "]", helloWorld[i], bounded.read());
        }
        assertEquals("limit = length end", -1, bounded.read());

        // limit > length
        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld), helloWorld.length + 1);
        for (int i = 0; i < helloWorld.length; i++)
        {
            assertEquals("limit > length byte[" + i + "]", helloWorld[i], bounded.read());
        }
        assertEquals("limit > length end", -1, bounded.read());

        // limit < length
        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld), hello.length);
        for (int i = 0; i < hello.length; i++)
        {
            assertEquals("limit < length byte[" + i + "]", hello[i], bounded.read());
        }
        assertEquals("limit < length end", -1, bounded.read());
    }

    /**
     * Test {@link BoundedInputStream#read(byte[], int, int)}.
     */
    public void testReadArray() throws Exception
    {

        BoundedInputStream bounded = null;
        byte[] helloWorld = "Hello World".getBytes();
        byte[] hello      = "Hello".getBytes();

        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld));
        compare("limit = -1", helloWorld, IOUtils.toByteArray(bounded));

        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld), 0);
        compare("limit = 0", new byte[0], IOUtils.toByteArray(bounded));

        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld), helloWorld.length);
        compare("limit = length", helloWorld, IOUtils.toByteArray(bounded));

        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld), helloWorld.length + 1);
        compare("limit > length", helloWorld, IOUtils.toByteArray(bounded));

        bounded = new BoundedInputStream(new ByteArrayInputStream(helloWorld), helloWorld.length - 6);
        compare("limit < length", hello, IOUtils.toByteArray(bounded));
    }

    /**
     * Compare byte arrays.
     */
    private void compare(String msg, byte[] expected, byte[] actual)
    {
        assertEquals(msg + " length", expected.length, actual.length);
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(msg + " byte[" + i + "]", expected[i], actual[i]);
        }
    }
}
