/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.transaction.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.transaction.Status;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.transaction.util.CommonsLoggingLogger;
import org.apache.commons.transaction.util.LoggerFacade;
import org.apache.commons.transaction.util.RendezvousBarrier;

/**
 * Tests for map wrapper.
 *
 * @version $Id: MapWrapperTest.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class MapWrapperTest extends TestCase
{

    private static final Log log = LogFactory.getLog(MapWrapperTest.class.getName());
    private static final LoggerFacade sLogger = new CommonsLoggingLogger(log);

    protected static final long BARRIER_TIMEOUT = 20000;

    // XXX need this, as JUnit seems to print only part of these strings
    protected static void report(String should, String is)
    {
        if (!should.equals(is))
        {
            fail("\nWrong output:\n'" + is + "'\nShould be:\n'" + should + "'\n");
        }
    }

    protected static void checkCollection(Collection col, Object[] values)
    {
        int cnt = 0;
        int trueCnt = 0;

        for (Iterator it = col.iterator(); it.hasNext();)
        {
            cnt++;
            Object value1 = it.next();
            for (int i = 0; i < values.length; i++)
            {
                Object value2 = values[i];
                if (value2.equals(value1))
                    trueCnt++;
            }
        }
        assertEquals(cnt, values.length);
        assertEquals(trueCnt, values.length);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite(MapWrapperTest.class);
        return suite;
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public MapWrapperTest(String testName)
    {
        super(testName);
    }

    protected TransactionalMapWrapper getNewWrapper(Map map)
    {
        return new TransactionalMapWrapper(map);
    }

    public void testBasic() throws Throwable
    {

        sLogger.logInfo("Checking basic transaction features");

        final Map map1 = new HashMap();

        final TransactionalMapWrapper txMap1 = getNewWrapper(map1);

        assertTrue(txMap1.isEmpty());

        // make sure changes are propagated to wrapped map outside tx
        txMap1.put("key1", "value1");
        report("value1", (String) map1.get("key1"));
        assertFalse(txMap1.isEmpty());

        // make sure changes are progated to wrapped map only after commit
        txMap1.startTransaction();
        assertFalse(txMap1.isEmpty());
        txMap1.put("key1", "value2");
        report("value1", (String) map1.get("key1"));
        report("value2", (String) txMap1.get("key1"));
        txMap1.commitTransaction();
        report("value2", (String) map1.get("key1"));
        report("value2", (String) txMap1.get("key1"));

        // make sure changes are reverted after rollback
        txMap1.startTransaction();
        txMap1.put("key1", "value3");
        txMap1.rollbackTransaction();
        report("value2", (String) map1.get("key1"));
        report("value2", (String) txMap1.get("key1"));
    }

    public void testContainsKeyWithNullValue() throws Throwable
    {

        sLogger.logInfo("Checking containsKey returns true when the value is null");

        final Map map1 = new HashMap();

        final TransactionalMapWrapper txMap1 = getNewWrapper(map1);

        assertTrue(txMap1.isEmpty());

        // make sure changes are propagated to wrapped map outside tx
        txMap1.put("key1", null);
        assertTrue(txMap1.containsKey("key1"));

        // make sure changes are progated to wrapped map after commit
        txMap1.startTransaction();
        txMap1.put("key2", null);
        assertTrue(txMap1.containsKey("key2"));
        txMap1.remove("key1");
        assertTrue(map1.containsKey("key1"));
        txMap1.commitTransaction();
        assertTrue(txMap1.containsKey("key2"));
        assertFalse(txMap1.containsKey("key1"));

        txMap1.startTransaction();
        assertTrue(txMap1.containsKey("key2"));
        txMap1.remove("key2");
        assertFalse(txMap1.containsKey("key2"));
        txMap1.commitTransaction();
    }

    public void testComplex() throws Throwable
    {

        sLogger.logInfo("Checking advanced and complex transaction features");

        final Map map1 = new HashMap();

        final TransactionalMapWrapper txMap1 = getNewWrapper(map1);

        // first fill in some global values:
        txMap1.put("key1", "value1");
        txMap1.put("key2", "value2");

        // let's see if we have all values:
        sLogger.logInfo("Checking if global values are present");

        assertTrue(txMap1.containsValue("value1"));
        assertTrue(txMap1.containsValue("value2"));
        assertFalse(txMap1.containsValue("novalue"));

        // ... and all keys
        sLogger.logInfo("Checking if global keys are present");
        assertTrue(txMap1.containsKey("key1"));
        assertTrue(txMap1.containsKey("key2"));
        assertFalse(txMap1.containsKey("nokey"));

        // and now some inside a transaction
        txMap1.startTransaction();
        txMap1.put("key3", "value3");
        txMap1.put("key4", "value4");

        // let's see if we have all values:
        sLogger.logInfo("Checking if values inside transactions are present");
        assertTrue(txMap1.containsValue("value1"));
        assertTrue(txMap1.containsValue("value2"));
        assertTrue(txMap1.containsValue("value3"));
        assertTrue(txMap1.containsValue("value4"));
        assertFalse(txMap1.containsValue("novalue"));

        // ... and all keys
        sLogger.logInfo("Checking if keys inside transactions are present");
        assertTrue(txMap1.containsKey("key1"));
        assertTrue(txMap1.containsKey("key2"));
        assertTrue(txMap1.containsKey("key3"));
        assertTrue(txMap1.containsKey("key4"));
        assertFalse(txMap1.containsKey("nokey"));

        // now let's delete some old stuff
        sLogger.logInfo("Checking remove inside transactions");
        txMap1.remove("key1");
        assertFalse(txMap1.containsKey("key1"));
        assertFalse(txMap1.containsValue("value1"));
        assertNull(txMap1.get("key1"));
        assertEquals(3, txMap1.size());

        // and some newly created
        txMap1.remove("key3");
        assertFalse(txMap1.containsKey("key3"));
        assertFalse(txMap1.containsValue("value3"));
        assertNull(txMap1.get("key3"));
        assertEquals(2, txMap1.size());

        sLogger.logInfo("Checking remove and propagation after commit");
        txMap1.commitTransaction();

        txMap1.remove("key1");
        assertFalse(txMap1.containsKey("key1"));
        assertFalse(txMap1.containsValue("value1"));
        assertNull(txMap1.get("key1"));
        assertFalse(txMap1.containsKey("key3"));
        assertFalse(txMap1.containsValue("value3"));
        assertNull(txMap1.get("key3"));
        assertEquals(2, txMap1.size());
    }

    public void testSets() throws Throwable
    {

        sLogger.logInfo("Checking set opertaions");

        final Map map1 = new HashMap();

        final TransactionalMapWrapper txMap1 = getNewWrapper(map1);

        // first fill in some global values:
        txMap1.put("key1", "value1");
        txMap1.put("key2", "value200");

        // and now some inside a transaction
        txMap1.startTransaction();
        txMap1.put("key2", "value2"); // modify
        txMap1.put("key3", "value3");
        txMap1.put("key4", "value4");

        // check entry set
        boolean key1P, key2P, key3P, key4P;
        key1P = key2P = key3P = key4P = false;
        int cnt = 0;
        for (Iterator it = txMap1.entrySet().iterator(); it.hasNext();)
        {
            cnt++;
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals("key1") && entry.getValue().equals("value1"))
                key1P = true;
            else if (entry.getKey().equals("key2") && entry.getValue().equals("value2"))
                key2P = true;
            else if (entry.getKey().equals("key3") && entry.getValue().equals("value3"))
                key3P = true;
            else if (entry.getKey().equals("key4") && entry.getValue().equals("value4"))
                key4P = true;
        }
        assertEquals(cnt, 4);
        assertTrue(key1P && key2P && key3P && key4P);

        checkCollection(txMap1.values(), new String[] { "value1", "value2", "value3", "value4" });
        checkCollection(txMap1.keySet(), new String[] { "key1", "key2", "key3", "key4" });

        txMap1.commitTransaction();

        // check again after commit (should be the same)
        key1P = key2P = key3P = key4P = false;
        cnt = 0;
        for (Iterator it = txMap1.entrySet().iterator(); it.hasNext();)
        {
            cnt++;
            Map.Entry entry = (Map.Entry) it.next();
            if (entry.getKey().equals("key1") && entry.getValue().equals("value1"))
                key1P = true;
            else if (entry.getKey().equals("key2") && entry.getValue().equals("value2"))
                key2P = true;
            else if (entry.getKey().equals("key3") && entry.getValue().equals("value3"))
                key3P = true;
            else if (entry.getKey().equals("key4") && entry.getValue().equals("value4"))
                key4P = true;
        }
        assertEquals(cnt, 4);
        assertTrue(key1P && key2P && key3P && key4P);

        checkCollection(txMap1.values(), new String[] { "value1", "value2", "value3", "value4" });
        checkCollection(txMap1.keySet(), new String[] { "key1", "key2", "key3", "key4" });

        // now try clean

        txMap1.startTransaction();

        // add
        txMap1.put("key5", "value5");
        // modify
        txMap1.put("key4", "value400");
        // delete
        txMap1.remove("key1");

        assertEquals(txMap1.size(), 4);

        txMap1.clear();
        assertEquals(txMap1.size(), 0);
        assertEquals(map1.size(), 4);

        // add
        txMap1.put("key5", "value5");
        // delete
        txMap1.remove("key1");

        // adding one, not removing anything gives size 1
        assertEquals(txMap1.size(), 1);
        assertEquals(map1.size(), 4);
        assertNull(txMap1.get("key4"));
        assertNotNull(txMap1.get("key5"));

        txMap1.commitTransaction();

        // after commit clear must have been propagated to wrapped map:
        assertEquals(txMap1.size(), 1);
        assertEquals(map1.size(), 1);
        assertNull(txMap1.get("key4"));
        assertNotNull(txMap1.get("key5"));
        assertNull(map1.get("key4"));
        assertNotNull(map1.get("key5"));
    }

    public void testMulti() throws Throwable
    {
        sLogger.logInfo("Checking concurrent transaction features");

        final Map map1 = new HashMap();

        final TransactionalMapWrapper txMap1 = getNewWrapper(map1);

        final RendezvousBarrier beforeCommitBarrier =
            new RendezvousBarrier("Before Commit", 2, BARRIER_TIMEOUT, sLogger);

        final RendezvousBarrier afterCommitBarrier = new RendezvousBarrier("After Commit", 2, BARRIER_TIMEOUT, sLogger);

        Thread thread1 = new Thread(new Runnable()
        {
            public void run()
            {
                txMap1.startTransaction();
                try
                {
                    beforeCommitBarrier.meet();
                    txMap1.put("key1", "value2");
                    txMap1.commitTransaction();
                    afterCommitBarrier.call();
                }
                catch (InterruptedException e)
                {
                    sLogger.logWarning("Thread interrupted", e);
                    afterCommitBarrier.reset();
                    beforeCommitBarrier.reset();
                }
            }
        }, "Thread1");

        txMap1.put("key1", "value1");

        txMap1.startTransaction();
        thread1.start();

        report("value1", (String) txMap1.get("key1"));
        beforeCommitBarrier.call();
        afterCommitBarrier.meet();
        // we have read committed as isolation level, that's why I will see the new value of the other thread now
        report("value2", (String) txMap1.get("key1"));

        // now when I override it it should of course be my value again
        txMap1.put("key1", "value3");
        report("value3", (String) txMap1.get("key1"));

        // after rollback it must be the value written by the other thread again
        txMap1.rollbackTransaction();
        report("value2", (String) txMap1.get("key1"));
    }

    public void testTxControl() throws Throwable
    {
        sLogger.logInfo("Checking advanced transaction control (heavily used in JCA implementation)");

        final Map map1 = new HashMap();

        final TransactionalMapWrapper txMap1 = getNewWrapper(map1);

        assertEquals(txMap1.getTransactionState(), Status.STATUS_NO_TRANSACTION);
        txMap1.startTransaction();
        assertEquals(txMap1.getTransactionState(), Status.STATUS_ACTIVE);

        assertTrue(txMap1.isReadOnly());
        txMap1.put("key", "value");
        assertFalse(txMap1.isReadOnly());

        assertFalse(txMap1.isTransactionMarkedForRollback());
        txMap1.markTransactionForRollback();
        assertTrue(txMap1.isTransactionMarkedForRollback());

        boolean failed = false;
        try
        {
            txMap1.commitTransaction();
        }
        catch (IllegalStateException ise)
        {
            failed = true;
        }
        assertTrue(failed);
        txMap1.rollbackTransaction();
        assertEquals(txMap1.getTransactionState(), Status.STATUS_NO_TRANSACTION);

        txMap1.startTransaction();
        final TransactionalMapWrapper.TxContext ctx = txMap1.suspendTransaction();
        final RendezvousBarrier afterSuspendBarrier =
            new RendezvousBarrier("After Suspend", 2, BARRIER_TIMEOUT, sLogger);

        new Thread(new Runnable()
        {
            public void run()
            {
                txMap1.resumeTransaction(ctx);
                txMap1.put("key2", "value2");
                txMap1.suspendTransaction();
                afterSuspendBarrier.call();
            }
        }).start();

        afterSuspendBarrier.meet();
        txMap1.resumeTransaction(ctx);

        assertEquals(txMap1.size(), 1);
        txMap1.put("key3", "value3");
        assertEquals(txMap1.size(), 2);
        assertEquals(map1.size(), 0);

        txMap1.commitTransaction();
        assertEquals(txMap1.size(), 2);
        assertEquals(map1.size(), 2);
    }

}
