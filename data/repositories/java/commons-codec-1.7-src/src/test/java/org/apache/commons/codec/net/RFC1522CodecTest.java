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

package org.apache.commons.codec.net;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * RFC 1522 compliant codec test cases
 *
 * @version $Id: RFC1522CodecTest.java 1352268 2012-06-20 19:04:08Z ggregory $
 */
public class RFC1522CodecTest
{

    static class RFC1522TestCodec extends RFC1522Codec
    {

        @Override
        protected byte[] doDecoding(byte[] bytes)
        {
            return bytes;
        }

        @Override
        protected byte[] doEncoding(byte[] bytes)
        {
            return bytes;
        }

        @Override
        protected String getEncoding()
        {
            return "T";
        }

    }

    @Test
    public void testNullInput() throws Exception
    {
        RFC1522TestCodec testcodec = new RFC1522TestCodec();
        assertNull(testcodec.decodeText(null));
        assertNull(testcodec.encodeText(null, CharEncoding.UTF_8));
    }

    private void assertExpectedDecoderException(String s) throws Exception
    {
        RFC1522TestCodec testcodec = new RFC1522TestCodec();
        try
        {
            testcodec.decodeText(s);
            fail("DecoderException should have been thrown");
        }
        catch (DecoderException e)
        {
            // Expected.
        }
    }

    @Test
    public void testDecodeInvalid() throws Exception
    {
        assertExpectedDecoderException("whatever");
        assertExpectedDecoderException("=?");
        assertExpectedDecoderException("?=");
        assertExpectedDecoderException("==");
        assertExpectedDecoderException("=??=");
        assertExpectedDecoderException("=?stuff?=");
        assertExpectedDecoderException("=?UTF-8??=");
        assertExpectedDecoderException("=?UTF-8?stuff?=");
        assertExpectedDecoderException("=?UTF-8?T?stuff");
        assertExpectedDecoderException("=??T?stuff?=");
        assertExpectedDecoderException("=?UTF-8??stuff?=");
        assertExpectedDecoderException("=?UTF-8?W?stuff?=");
    }

}
