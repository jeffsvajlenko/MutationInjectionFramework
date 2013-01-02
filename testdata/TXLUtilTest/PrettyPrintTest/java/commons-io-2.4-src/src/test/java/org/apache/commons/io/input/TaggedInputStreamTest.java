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
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import junit.framework.TestCase;

import org.apache.commons.io.TaggedIOException;

/**
 * JUnit Test Case for {@link TaggedInputStream}.
 */
public class TaggedInputStreamTest extends TestCase
{

    public void testEmptyStream()
    {
        try
        {
            InputStream stream = new TaggedInputStream(new ClosedInputStream());
            assertEquals(0, stream.available());
            assertEquals(-1, stream.read());
            assertEquals(-1, stream.read(new byte[1]));
            assertEquals(-1, stream.read(new byte[1], 0, 1));
            stream.close();
        }
        catch (IOException e)
        {
            fail("Unexpected exception thrown");
        }
    }

    public void testNormalStream()
    {
        try
        {
            InputStream stream = new TaggedInputStream(
                new ByteArrayInputStream(new byte[] { 'a', 'b', 'c' }));
            assertEquals(3, stream.available());
            assertEquals('a', stream.read());
            byte[] buffer = new byte[1];
            assertEquals(1, stream.read(buffer));
            assertEquals('b', buffer[0]);
            assertEquals(1, stream.read(buffer, 0, 1));
            assertEquals('c', buffer[0]);
            assertEquals(-1, stream.read());
            stream.close();
        }
        catch (IOException e)
        {
            fail("Unexpected exception thrown");
        }
    }

    public void testBrokenStream()
    {
        IOException exception = new IOException("test exception");
        TaggedInputStream stream =
            new TaggedInputStream(new BrokenInputStream(exception));

        // Test the available() method
        try
        {
            stream.available();
            fail("Expected exception not thrown.");
        }
        catch (IOException e)
        {
            assertTrue(stream.isCauseOf(e));
            try
            {
                stream.throwIfCauseOf(e);
                fail("Expected exception not thrown.");
            }
            catch (IOException e2)
            {
                assertEquals(exception, e2);
            }
        }

        // Test the read() method
        try
        {
            stream.read();
            fail("Expected exception not thrown.");
        }
        catch (IOException e)
        {
            assertTrue(stream.isCauseOf(e));
            try
            {
                stream.throwIfCauseOf(e);
                fail("Expected exception not thrown.");
            }
            catch (IOException e2)
            {
                assertEquals(exception, e2);
            }
        }

        // Test the close() method
        try
        {
            stream.close();
            fail("Expected exception not thrown.");
        }
        catch (IOException e)
        {
            assertTrue(stream.isCauseOf(e));
            try
            {
                stream.throwIfCauseOf(e);
                fail("Expected exception not thrown.");
            }
            catch (IOException e2)
            {
                assertEquals(exception, e2);
            }
        }
    }

    public void testOtherException()
    {
        IOException exception = new IOException("test exception");
        InputStream closed = new ClosedInputStream();
        TaggedInputStream stream = new TaggedInputStream(closed);

        assertFalse(stream.isCauseOf(exception));
        assertFalse(stream.isCauseOf(
                        new TaggedIOException(exception, UUID.randomUUID())));

        try
        {
            stream.throwIfCauseOf(exception);
        }
        catch (IOException e)
        {
            fail("Unexpected exception thrown");
        }

        try
        {
            stream.throwIfCauseOf(
                new TaggedIOException(exception, UUID.randomUUID()));
        }
        catch (IOException e)
        {
            fail("Unexpected exception thrown");
        }
    }

}
