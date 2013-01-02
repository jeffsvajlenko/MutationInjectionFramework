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
package org.apache.commons.net.ftp.parser;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileEntryParser;

import java.util.Calendar;

/**
 * @version $Id: OS400FTPEntryParserTest.java 155429 2005-02-26 13:13:04Z dirkv $
 */

public class OS400FTPEntryParserTest extends CompositeFTPParseTestFramework
{
    private static final String[][] badsamples =
    {
        {
            "PEP              4019 04/03/18 18:58:16 STMF       einladung.zip",
            "PEP               422 03/24 14:06:26 *STMF      readme",
            "PEP              6409 04/03/24 30:06:29 *STMF      build.xml",
            "PEP USR         36864 04/03/24 14:06:34 *DIR       dir1/",
            "PEP             3686404/03/24 14:06:47 *DIR       zdir2/"
        },

        {
            "----rwxr-x   1PEP       0           4019 Mar 18 18:58 einladung.zip",
            "----rwxr-x   1 PEP      0  xx        422 Mar 24 14:06 readme",
            "----rwxr-x   1 PEP      0           8492 Apr 07 30:13 build.xml",
            "d---rwxr-x   2 PEP      0          45056Mar 24 14:06 zdir2"
        }
    };

    private static final String[][] goodsamples =
    {
        {
            "PEP              4019 04/03/18 18:58:16 *STMF      einladung.zip",
            "PEP               422 04/03/24 14:06:26 *STMF      readme",
            "PEP              6409 04/03/24 14:06:29 *STMF      build.xml",
            "PEP             36864 04/03/24 14:06:34 *DIR       dir1/",
            "PEP             36864 04/03/24 14:06:47 *DIR       zdir2/"
        },
        {
            "----rwxr-x   1 PEP      0           4019 Mar 18 18:58 einladung.zip",
            "----rwxr-x   1 PEP      0            422 Mar 24 14:06 readme",
            "----rwxr-x   1 PEP      0           8492 Apr 07 07:13 build.xml",
            "d---rwxr-x   2 PEP      0          45056 Mar 24 14:06 dir1",
            "d---rwxr-x   2 PEP      0          45056 Mar 24 14:06 zdir2"
        }
    };

    /**
     * @see junit.framework.TestCase#TestCase(String)
     */
    public OS400FTPEntryParserTest(String name)
    {
        super(name);
    }

    /**
     * @see FTPParseTestFramework#getBadListing()
     */
    @Override
    protected String[][] getBadListings()
    {
        return badsamples;
    }

    /**
     * @see FTPParseTestFramework#getGoodListing()
     */
    @Override
    protected String[][] getGoodListings()
    {
        return goodsamples;
    }

    /**
     * @see FTPParseTestFramework#getParser()
     */
    @Override
    protected FTPFileEntryParser getParser()
    {
        return new CompositeFileEntryParser(new FTPFileEntryParser[]
                                            {
                                                new OS400FTPEntryParser(),
                                                new UnixFTPEntryParser()
                                            });
    }

    /**
     * @see FTPParseTestFramework#testParseFieldsOnDirectory()
     */
    @Override
    public void testParseFieldsOnDirectory() throws Exception
    {
        FTPFile f = getParser().parseFTPEntry("PEP             36864 04/03/24 14:06:34 *DIR       dir1/");
        assertNotNull("Could not parse entry.",
                      f);
        assertTrue("Should have been a directory.",
                   f.isDirectory());
        assertEquals("PEP",
                     f.getUser());
        assertEquals("dir1",
                     f.getName());
        assertEquals(36864,
                     f.getSize());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MARCH);

        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 6);
        cal.set(Calendar.SECOND, 34);

        assertEquals(df.format(cal.getTime()),
                     df.format(f.getTimestamp().getTime()));
    }

    @Override
    protected void doAdditionalGoodTests(String test, FTPFile f)
    {
        if (test.startsWith("d"))
        {
            assertEquals("directory.type",
                         FTPFile.DIRECTORY_TYPE, f.getType());
        }
    }

    /**
     * @see FTPParseTestFramework#testParseFieldsOnFile()
     */
    @Override
    public void testParseFieldsOnFile() throws Exception
    {
        FTPFile f = getParser().parseFTPEntry("PEP              5000000000 04/03/24 14:06:29 *STMF      build.xml");
        assertNotNull("Could not parse entry.",
                      f);
        assertTrue("Should have been a file.",
                   f.isFile());
        assertEquals("PEP",
                     f.getUser());
        assertEquals("build.xml",
                     f.getName());
        assertEquals(5000000000L,
                     f.getSize());

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.DATE, 24);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 6);
        cal.set(Calendar.SECOND, 29);
        assertEquals(df.format(cal.getTime()),
                     df.format(f.getTimestamp().getTime()));
    }
}
