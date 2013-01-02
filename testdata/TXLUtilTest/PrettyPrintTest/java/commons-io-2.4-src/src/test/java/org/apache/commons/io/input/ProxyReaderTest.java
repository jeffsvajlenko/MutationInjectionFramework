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

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

import junit.framework.TestCase;

/**
 * Test {@link ProxyReader}.
 *
 * @version $Id: ProxyReaderTest.java 1302056 2012-03-18 03:03:38Z ggregory $
 */
public class ProxyReaderTest extends TestCase
{

    public ProxyReaderTest(String name)
    {
        super(name);
    }

    /** Test writing Null Char Array */
    public void testNullCharArray()
    {

        ProxyReader proxy = new ProxyReaderImpl(new CustomNullReader(0));

        try
        {
            proxy.read((char[])null);
        }
        catch(Exception e)
        {
            fail("Writing null String threw " + e);
        }

        try
        {
            proxy.read((char[])null, 0, 0);
        }
        catch(Exception e)
        {
            fail("Writing null String threw " + e);
        }
    }

    /** Test writing Null CharBuffer */
    public void testNullCharBuffer()
    {

        ProxyReader proxy = new ProxyReaderImpl(new CustomNullReader(0));

        try
        {
            proxy.read((CharBuffer)null);
        }
        catch(Exception e)
        {
            fail("Writing null String threw " + e);
        }
    }

    /** ProxyReader implementation */
    private static class ProxyReaderImpl extends ProxyReader
    {
        ProxyReaderImpl(Reader proxy)
        {
            super(proxy);
        }
    }

    /** Custom NullReader implementation */
    private static class CustomNullReader extends NullReader
    {
        CustomNullReader(int len)
        {
            super(len);
        }
        @Override
        public int read(char[] chars) throws IOException
        {
            return chars == null ? 0 : super.read(chars);
        }
        @Override
        public int read(CharBuffer target) throws IOException
        {
            return target == null ? 0 : super.read(target);
        }
    }
}
