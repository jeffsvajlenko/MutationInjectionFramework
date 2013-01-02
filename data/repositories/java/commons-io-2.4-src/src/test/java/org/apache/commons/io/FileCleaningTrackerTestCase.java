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

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.testtools.FileBasedTestCase;

/**
 * This is used to test {@link FileCleaningTracker} for correctness.
 *
 * @version $Id: FileCleaningTrackerTestCase.java 1302056 2012-03-18 03:03:38Z ggregory $
 * @see FileCleaningTracker
 */
public class FileCleaningTrackerTestCase extends FileBasedTestCase
{
    protected FileCleaningTracker newInstance()
    {
        return new FileCleaningTracker();
    }

    private File testFile;
    private FileCleaningTracker theInstance;

    public FileCleaningTrackerTestCase(String name)
    {
        super(name);
        testFile = new File(getTestDirectory(), "file-test.txt");
    }

    /** @see junit.framework.TestCase#setUp() */
    @Override
    protected void setUp() throws Exception
    {
        theInstance = newInstance();
        getTestDirectory().mkdirs();
    }

    /** @see junit.framework.TestCase#tearDown() */
    @Override
    protected void tearDown() throws Exception
    {
        FileUtils.deleteDirectory(getTestDirectory());

        // reset file cleaner class, so as not to break other tests

        /**
         * The following block of code can possibly be removed when the
         * deprecated {@link FileCleaner} is gone. The question is, whether
         * we want to support reuse of {@link FileCleaningTracker} instances,
         * which we should, IMO, not.
         */
        {
            theInstance.q = new ReferenceQueue<Object>();
            theInstance.trackers.clear();
            theInstance.deleteFailures.clear();
            theInstance.exitWhenFinished = false;
            theInstance.reaper = null;
        }

        theInstance = null;
    }

    //-----------------------------------------------------------------------
    public void testFileCleanerFile() throws Exception
    {
        String path = testFile.getPath();

        assertFalse(testFile.exists());
        RandomAccessFile r = new RandomAccessFile(testFile, "rw");
        assertTrue(testFile.exists());

        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(path, r);
        assertEquals(1, theInstance.getTrackCount());

        r.close();
        testFile = null;
        r = null;

        waitUntilTrackCount();
        pauseForDeleteToComplete(new File(path));

        assertEquals(0, theInstance.getTrackCount());
        assertEquals(showFailures(), false, new File(path).exists());
    }

    public void testFileCleanerDirectory() throws Exception
    {
        createFile(testFile, 100);
        assertTrue(testFile.exists());
        assertTrue(getTestDirectory().exists());

        Object obj = new Object();
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(getTestDirectory(), obj);
        assertEquals(1, theInstance.getTrackCount());

        obj = null;

        waitUntilTrackCount();

        assertEquals(0, theInstance.getTrackCount());
        assertTrue(testFile.exists());  // not deleted, as dir not empty
        assertTrue(testFile.getParentFile().exists());  // not deleted, as dir not empty
    }

    public void testFileCleanerDirectory_NullStrategy() throws Exception
    {
        createFile(testFile, 100);
        assertTrue(testFile.exists());
        assertTrue(getTestDirectory().exists());

        Object obj = new Object();
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(getTestDirectory(), obj, (FileDeleteStrategy) null);
        assertEquals(1, theInstance.getTrackCount());

        obj = null;

        waitUntilTrackCount();

        assertEquals(0, theInstance.getTrackCount());
        assertTrue(testFile.exists());  // not deleted, as dir not empty
        assertTrue(testFile.getParentFile().exists());  // not deleted, as dir not empty
    }

    public void testFileCleanerDirectory_ForceStrategy() throws Exception
    {
        createFile(testFile, 100);
        assertTrue(testFile.exists());
        assertTrue(getTestDirectory().exists());

        Object obj = new Object();
        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(getTestDirectory(), obj, FileDeleteStrategy.FORCE);
        assertEquals(1, theInstance.getTrackCount());

        obj = null;

        waitUntilTrackCount();
        pauseForDeleteToComplete(testFile.getParentFile());

        assertEquals(0, theInstance.getTrackCount());
        assertEquals(showFailures(), false, new File(testFile.getPath()).exists());
        assertEquals(showFailures(), false, testFile.getParentFile().exists());
    }

    public void testFileCleanerNull() throws Exception
    {
        try
        {
            theInstance.track((File) null, new Object());
            fail();
        }
        catch (NullPointerException ex)
        {
            // expected
        }
        try
        {
            theInstance.track((File) null, new Object(), FileDeleteStrategy.NORMAL);
            fail();
        }
        catch (NullPointerException ex)
        {
            // expected
        }
        try
        {
            theInstance.track((String) null, new Object());
            fail();
        }
        catch (NullPointerException ex)
        {
            // expected
        }
        try
        {
            theInstance.track((String) null, new Object(), FileDeleteStrategy.NORMAL);
            fail();
        }
        catch (NullPointerException ex)
        {
            // expected
        }
    }

    public void testFileCleanerExitWhenFinishedFirst() throws Exception
    {
        assertFalse(theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        assertTrue(theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);

        waitUntilTrackCount();

        assertEquals(0, theInstance.getTrackCount());
        assertTrue(theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);
    }

    public void testFileCleanerExitWhenFinished_NoTrackAfter() throws Exception
    {
        assertFalse(theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        assertTrue(theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);

        String path = testFile.getPath();
        Object marker = new Object();
        try
        {
            theInstance.track(path, marker);
            fail();
        }
        catch (IllegalStateException ex)
        {
            // expected
        }
        assertTrue(theInstance.exitWhenFinished);
        assertEquals(null, theInstance.reaper);
    }

    public void testFileCleanerExitWhenFinished1() throws Exception
    {
        String path = testFile.getPath();

        assertEquals("1-testFile exists", false, testFile.exists());
        RandomAccessFile r = new RandomAccessFile(testFile, "rw");
        assertEquals("2-testFile exists", true, testFile.exists());

        assertEquals("3-Track Count", 0, theInstance.getTrackCount());
        theInstance.track(path, r);
        assertEquals("4-Track Count", 1, theInstance.getTrackCount());
        assertEquals("5-exitWhenFinished", false, theInstance.exitWhenFinished);
        assertEquals("6-reaper.isAlive", true, theInstance.reaper.isAlive());

        assertEquals("7-exitWhenFinished", false, theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        assertEquals("8-exitWhenFinished", true, theInstance.exitWhenFinished);
        assertEquals("9-reaper.isAlive", true, theInstance.reaper.isAlive());

        r.close();
        testFile = null;
        r = null;

        waitUntilTrackCount();
        pauseForDeleteToComplete(new File(path));

        assertEquals("10-Track Count", 0, theInstance.getTrackCount());
        assertEquals("11-testFile exists " + showFailures(), false, new File(path).exists());
        assertEquals("12-exitWhenFinished", true, theInstance.exitWhenFinished);
        assertEquals("13-reaper.isAlive", false, theInstance.reaper.isAlive());
    }

    public void testFileCleanerExitWhenFinished2() throws Exception
    {
        String path = testFile.getPath();

        assertFalse(testFile.exists());
        RandomAccessFile r = new RandomAccessFile(testFile, "rw");
        assertTrue(testFile.exists());

        assertEquals(0, theInstance.getTrackCount());
        theInstance.track(path, r);
        assertEquals(1, theInstance.getTrackCount());
        assertFalse(theInstance.exitWhenFinished);
        assertTrue(theInstance.reaper.isAlive());

        r.close();
        testFile = null;
        r = null;

        waitUntilTrackCount();
        pauseForDeleteToComplete(new File(path));

        assertEquals(0, theInstance.getTrackCount());
        assertEquals(showFailures(), false, new File(path).exists());
        assertFalse(theInstance.exitWhenFinished);
        assertTrue(theInstance.reaper.isAlive());

        assertFalse(theInstance.exitWhenFinished);
        theInstance.exitWhenFinished();
        for (int i = 0; i < 20 && theInstance.reaper.isAlive(); i++)
        {
            Thread.sleep(500L);  // allow reaper thread to die
        }
        assertTrue(theInstance.exitWhenFinished);
        assertFalse(theInstance.reaper.isAlive());
    }

    //-----------------------------------------------------------------------
    private void pauseForDeleteToComplete(File file)
    {
        int count = 0;
        while(file.exists() && count++ < 40)
        {
            try
            {
                Thread.sleep(500L);
            }
            catch (InterruptedException e)
            {
            }
            file = new File(file.getPath());
        }
    }
    private String showFailures() throws Exception
    {
        if (theInstance.deleteFailures.size() == 1)
        {
            return "[Delete Failed: " + theInstance.deleteFailures.get(0) + "]";
        }
        else
        {
            return "[Delete Failures: " + theInstance.deleteFailures.size() + "]";
        }
    }

    private void waitUntilTrackCount() throws Exception
    {
        System.gc();
        Thread.sleep(500);
        int count = 0;
        while(theInstance.getTrackCount() != 0 && count++ < 5)
        {
            List<String> list = new ArrayList<String>();
            try
            {
                long i = 0;
                while (theInstance.getTrackCount() != 0)
                {
                    list.add("A Big String A Big String A Big String A Big String A Big String A Big String A Big String A Big String A Big String A Big String " + (i++));
                }
            }
            catch (Throwable ignored)
            {
            }
            list = null;
            System.gc();
            Thread.sleep(1000);
        }
        if (theInstance.getTrackCount() != 0)
        {
            throw new IllegalStateException("Your JVM is not releasing References, try running the testcase with less memory (-Xmx)");
        }

    }
}
