/**
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
package org.apache.commons.transaction.locking;

import java.io.PrintWriter;

import junit.framework.TestCase;

import org.apache.commons.transaction.util.LoggerFacade;
import org.apache.commons.transaction.util.PrintWriterLogger;

/**
 * Tests for repeatable read in locks as investigated for OJB.
 *
 * @version $Id: LockTestRepeatableReads.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class LockTestRepeatableReads extends TestCase
{
    private static final LoggerFacade logFacade = new PrintWriterLogger(new PrintWriter(System.out),
            LockTestRepeatableReads.class.getName(), false);

    protected static final long TIMEOUT = 1000000;

    public static void main(String[] args)
    {
        String[] arr = {LockTestRepeatableReads.class.getName()};
        junit.textui.TestRunner.main(arr);
    }

    public LockTestRepeatableReads(String name)
    {
        super(name);
    }

    Object tx1;
    Object tx2;
    Object obj;
    ReadWriteUpgradeLockManager lockManager;

    public void setUp() throws Exception
    {
        super.setUp();

        lockManager = new ReadWriteUpgradeLockManager(logFacade, TIMEOUT);

        // initialize the dummies
        tx2 = new Object();
        tx1 = new Object();
        obj = new Object();
    }

    public void tearDown() throws Exception
    {
        try
        {
            lockManager.release(tx1, obj);
            lockManager.release(tx2, obj);
        }
        finally
        {
            super.tearDown();
        }
    }

    /**
     * Test 19
     */
    public void testWriteReleaseCheckRead()
    {
        assertTrue(lockManager.tryWriteLock(tx2, obj));
        assertTrue(lockManager.checkReadLock(tx2, obj));
        assertTrue(lockManager.release(tx2, obj));
        assertFalse(lockManager.hasReadLock(tx2, obj));
    }

    /**
     * Test 20
     */
    public void testReadWriteReleaseCheckRead()
    {
        assertTrue(lockManager.tryReadLock(tx2, obj));
        assertTrue(lockManager.tryWriteLock(tx2, obj));
        assertTrue(lockManager.checkReadLock(tx2, obj));
        assertTrue(lockManager.release(tx2, obj));
        assertFalse(lockManager.hasReadLock(tx2, obj));
    }

    /**
     * Test 1
     */
    public void testSingleReadLock()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
    }

    /**
     * Test 2
     */
    public void testUpgradeReadLock()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryUpgradeLock(tx1, obj));
    }

    /**
     * Test3
     */
    public void testReadThenWrite()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryWriteLock(tx1, obj));
    }

    /**
     * Test 4
     */
    public void testSingleWriteLock()
    {
        assertTrue(lockManager.tryWriteLock(tx1, obj));
    }

    /**
     * Test 5
     */
    public void testWriteThenRead()
    {
        assertTrue(lockManager.tryWriteLock(tx1, obj));
        assertTrue(lockManager.tryReadLock(tx1, obj));
    }

    /**
     * Test 6
     */
    public void testMultipleReadLock()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryReadLock(tx2, obj));
    }

    /**
     * Test 7
     */
    public void testUpgradeWithExistingReader()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryUpgradeLock(tx2, obj));
    }

    /**
     * Test 8
     */
    public void testWriteWithExistingReader()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertFalse(lockManager.tryWriteLock(tx2, obj));
    }

    /**
     * Test 9
     */
    public void testUpgradeWithMultipleReaders()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryReadLock(tx2, obj));
        assertTrue(lockManager.tryUpgradeLock(tx2, obj));
    }

    /**
     * Test 10
     */
    public void testWriteWithMultipleReaders()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryReadLock(tx2, obj));
        assertTrue(!lockManager.tryWriteLock(tx2, obj));
    }

    /**
     * Test 11
     */
    public void testUpgradeWithMultipleReadersOn1()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryReadLock(tx2, obj));
        assertTrue(lockManager.tryUpgradeLock(tx1, obj));
    }

    /**
     * Test 12
     */
    public void testWriteWithMultipleReadersOn1()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryReadLock(tx2, obj));
        assertFalse(lockManager.tryWriteLock(tx1, obj));
    }

    /**
     * Test 13
     */
    public void testReadWithExistingWriter()
    {
        assertTrue(lockManager.tryWriteLock(tx1, obj));
        assertFalse(lockManager.tryReadLock(tx2, obj));
    }

    /**
     * Test 14
     */
    public void testMultipleWriteLock()
    {
        assertTrue(lockManager.tryWriteLock(tx1, obj));
        assertFalse(lockManager.tryWriteLock(tx2, obj));
    }

    /**
     * Test 15
     */
    public void testReleaseReadLock()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.release(tx1, obj));
        assertTrue(lockManager.tryWriteLock(tx2, obj));
    }

    /**
     * Test 16
     */
    public void testReleaseUpgradeLock()
    {
        assertTrue(lockManager.tryUpgradeLock(tx1, obj));
        assertTrue(lockManager.release(tx1, obj));
        assertTrue(lockManager.tryWriteLock(tx2, obj));
    }

    /**
     * Test 17
     */
    public void testReleaseWriteLock()
    {
        assertTrue(lockManager.tryWriteLock(tx1, obj));
        assertTrue(lockManager.release(tx1, obj));
        assertTrue(lockManager.tryWriteLock(tx2, obj));
    }

    /**
     * Test 18
     */
    public void testReadThenRead()
    {
        assertTrue(lockManager.tryReadLock(tx1, obj));
        assertTrue(lockManager.tryReadLock(tx1, obj));
    }
}
