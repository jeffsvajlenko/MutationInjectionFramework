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
package org.apache.commons.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.testtools.FileBasedTestCase;
import org.apache.commons.io.testtools.YellOnCloseInputStream;
import org.apache.commons.io.testtools.YellOnFlushAndCloseOutputStream;

@SuppressWarnings("deprecation") // these are test cases for the deprecated CopyUtils

/**
 * JUnit tests for CopyUtils.
 *
 * @version $Id: CopyUtilsTest.java 1302056 2012-03-18 03:03:38Z ggregory $
 * @see CopyUtils
 */
public class CopyUtilsTest extends FileBasedTestCase
{

    /*
     * NOTE this is not particularly beautiful code. A better way to check for
     * flush and close status would be to implement "trojan horse" wrapper
     * implementations of the various stream classes, which set a flag when
     * relevant methods are called. (JT)
     */

    private static final int FILE_SIZE = 1024 * 4 + 1;


    private byte[] inData = generateTestData(FILE_SIZE);

    public CopyUtilsTest(String testName)
    {
        super(testName);
    }

    // ----------------------------------------------------------------
    // Setup
    // ----------------------------------------------------------------

    @Override
    public void setUp() throws Exception
    {
    }

    @Override
    public void tearDown() throws Exception
    {
    }

    // ----------------------------------------------------------------
    // Tests
    // ----------------------------------------------------------------

    public void testCtor()
    {
        new CopyUtils();
        // Nothing to assert, the constructor is public and does not blow up.
    }

    public void testCopy_byteArrayToOutputStream() throws Exception
    {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

        CopyUtils.copy(inData, out);

        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }

    public void testCopy_byteArrayToWriter() throws Exception
    {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);
        Writer writer = new java.io.OutputStreamWriter(out, "US-ASCII");

        CopyUtils.copy(inData, writer);
        writer.flush();

        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }

    public void testCopy_byteArrayToWriterWithEncoding() throws Exception
    {
        String inDataStr = "data";
        String charsetName = "UTF-8";
        StringWriter writer = new StringWriter();
        CopyUtils.copy(inDataStr.getBytes(charsetName), writer, charsetName);
        assertEquals(inDataStr, writer.toString());
    }

    public void testCopy_inputStreamToOutputStream() throws Exception
    {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStream(in);

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

        int count = CopyUtils.copy(in, out);

        assertEquals("Not all bytes were read", 0, in.available());
        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
        assertEquals(inData.length, count);
    }

    public void testCopy_inputStreamToWriter() throws Exception
    {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStream(in);

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);
        Writer writer = new java.io.OutputStreamWriter(out, "US-ASCII");

        CopyUtils.copy(in, writer);
        writer.flush();

        assertEquals("Not all bytes were read", 0, in.available());
        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }

    public void testCopy_inputStreamToWriterWithEncoding() throws Exception
    {
        String inDataStr = "data";
        String charsetName = "UTF-8";
        StringWriter writer = new StringWriter();
        CopyUtils.copy(new ByteArrayInputStream(inDataStr.getBytes(charsetName)), writer, charsetName);
        assertEquals(inDataStr, writer.toString());
    }

    public void testCopy_readerToOutputStream() throws Exception
    {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStream(in);
        Reader reader = new java.io.InputStreamReader(in, "US-ASCII");

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

        CopyUtils.copy(reader, out);
        //Note: this method *does* flush. It is equivalent to:
        //  OutputStreamWriter _out = new OutputStreamWriter(fout);
        //  IOUtils.copy( fin, _out, 4096 ); // copy( Reader, Writer, int );
        //  _out.flush();
        //  out = fout;

        // Note: rely on the method to flush
        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }

    public void testCopy_readerToWriter() throws Exception
    {
        InputStream in = new ByteArrayInputStream(inData);
        in = new YellOnCloseInputStream(in);
        Reader reader = new java.io.InputStreamReader(in, "US-ASCII");

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);
        Writer writer = new java.io.OutputStreamWriter(out, "US-ASCII");

        int count = CopyUtils.copy(reader, writer);
        writer.flush();
        assertEquals(
            "The number of characters returned by copy is wrong",
            inData.length,
            count);
        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }

    public void testCopy_stringToOutputStream() throws Exception
    {
        String str = new String(inData, "US-ASCII");

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);

        CopyUtils.copy(str, out);
        //Note: this method *does* flush. It is equivalent to:
        //  OutputStreamWriter _out = new OutputStreamWriter(fout);
        //  IOUtils.copy( str, _out, 4096 ); // copy( Reader, Writer, int );
        //  _out.flush();
        //  out = fout;
        // note: we don't flush here; this IOUtils method does it for us

        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }

    public void testCopy_stringToWriter() throws Exception
    {
        String str = new String(inData, "US-ASCII");

        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        OutputStream out = new YellOnFlushAndCloseOutputStream(baout, false, true);
        Writer writer = new java.io.OutputStreamWriter(out, "US-ASCII");

        CopyUtils.copy(str, writer);
        writer.flush();

        assertEquals("Sizes differ", inData.length, baout.size());
        assertTrue("Content differs", Arrays.equals(inData, baout.toByteArray()));
    }

} // CopyUtilsTest
