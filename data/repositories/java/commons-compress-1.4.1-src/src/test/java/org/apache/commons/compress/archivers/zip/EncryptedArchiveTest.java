/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.apache.commons.compress.archivers.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import junit.framework.TestCase;

public class EncryptedArchiveTest extends TestCase
{

    public void testReadPasswordEncryptedEntryViaZipFile()
    throws IOException, URISyntaxException
    {
        URL zip = getClass().getResource("/password-encrypted.zip");
        File file = new File(new URI(zip.toString()));
        ZipFile zf = null;
        try
        {
            zf = new ZipFile(file);
            ZipArchiveEntry zae = zf.getEntry("LICENSE.txt");
            assertTrue(zae.getGeneralPurposeBit().usesEncryption());
            assertFalse(zae.getGeneralPurposeBit().usesStrongEncryption());
            assertFalse(zf.canReadEntryData(zae));
            try
            {
                zf.getInputStream(zae);
                fail("expected an exception");
            }
            catch (UnsupportedZipFeatureException ex)
            {
                assertSame(UnsupportedZipFeatureException.Feature.ENCRYPTION,
                           ex.getFeature());
            }
        }
        finally
        {
            ZipFile.closeQuietly(zf);
        }
    }

    public void testReadPasswordEncryptedEntryViaStream()
    throws IOException, URISyntaxException
    {
        URL zip = getClass().getResource("/password-encrypted.zip");
        File file = new File(new URI(zip.toString()));
        ZipArchiveInputStream zin = null;
        try
        {
            zin = new ZipArchiveInputStream(new FileInputStream(file));
            ZipArchiveEntry zae = zin.getNextZipEntry();
            assertEquals("LICENSE.txt", zae.getName());
            assertTrue(zae.getGeneralPurposeBit().usesEncryption());
            assertFalse(zae.getGeneralPurposeBit().usesStrongEncryption());
            assertFalse(zin.canReadEntryData(zae));
            try
            {
                byte[] buf = new byte[1024];
                zin.read(buf, 0, buf.length);
                fail("expected an exception");
            }
            catch (UnsupportedZipFeatureException ex)
            {
                assertSame(UnsupportedZipFeatureException.Feature.ENCRYPTION,
                           ex.getFeature());
            }
        }
        finally
        {
            if (zin != null)
            {
                zin.close();
            }
        }
    }
}
