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

import java.io.IOException;
import java.io.OutputStream;

import junit.framework.TestCase;

/**
 * JUnit Test Case for {@link CloseShieldOutputStream}.
 */
public class ProxyOutputStreamTest extends TestCase
{

    private ByteArrayOutputStream original;

    private OutputStream proxied;

    @Override
    protected void setUp()
    {
        original = new ByteArrayOutputStream()
        {
            @Override
            public void write(byte[] ba) throws IOException
            {
                if (ba != null)
                {
                    super.write(ba);
                }
            }
        };
        proxied = new ProxyOutputStream(original);
    }

    public void testWrite() throws Exception
    {
        proxied.write('y');
        assertEquals(1, original.size());
        assertEquals('y', original.toByteArray()[0]);
    }

    public void testWriteNullBaSucceeds() throws Exception
    {
        byte[] ba = null;
        original.write(ba);
        proxied.write(ba);
    }
}
