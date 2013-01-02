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

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.commons.transaction.util.CommonsLoggingLogger;
import org.apache.commons.transaction.util.LoggerFacade;
import org.apache.commons.transaction.util.RendezvousBarrier;

/**
 * Tests for map wrapper.
 *
 * @version $Id: OptimisticMapWrapperTest.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class OptimisticMapWrapperTest extends MapWrapperTest
{

    private static final Log log = LogFactory.getLog(OptimisticMapWrapperTest.class.getName());
    private static final LoggerFacade sLogger = new CommonsLoggingLogger(log);

    public static Test suite()
    {
        TestSuite suite = new TestSuite(OptimisticMapWrapperTest.class);
        return suite;
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public OptimisticMapWrapperTest(String testName)
    {
        super(testName);
    }

    protected TransactionalMapWrapper getNewWrapper(Map map)
    {
        return new OptimisticMapWrapper(map);
    }

    // XXX no need for this code, just to make clear those tests are run as well
    public void testBasic() throws Throwable
    {
        super.testBasic();
    }

    public void testComplex() throws Throwable
    {
        super.testComplex();
    }

    public void testSets() throws Throwable
    {
        super.testSets();
    }

    public void testMulti() throws Throwable
    {
        log.info("Checking concurrent transaction features");

        final Map map1 = new HashMap();

        final OptimisticMapWrapper txMap1 = (OptimisticMapWrapper) getNewWrapper(map1);

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
        // we have serializable as isolation level, that's why I will still see the old value
        report("value1", (String) txMap1.get("key1"));

        // now when I override it it should of course be my value
        txMap1.put("key1", "value3");
        report("value3", (String) txMap1.get("key1"));

        // after rollback it must be the value written by the other thread
        txMap1.rollbackTransaction();
        report("value2", (String) txMap1.get("key1"));
    }

    public void testConflict() throws Throwable
    {
        log.info("Checking concurrent transaction features");

        final Map map1 = new HashMap();

        final OptimisticMapWrapper txMap1 = (OptimisticMapWrapper) getNewWrapper(map1);

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
        // we have serializable as isolation level, that's why I will still see the old value
        report("value1", (String) txMap1.get("key1"));

        // now when I override it it should of course be my value
        txMap1.put("key1", "value3");
        report("value3", (String) txMap1.get("key1"));

        boolean conflict = false;

        try
        {
            txMap1.commitTransaction();
        }
        catch (ConflictException ce)
        {
            conflict = true;
        }
        assertTrue(conflict);
        // after failed commit it must be the value written by the other thread
        report("value2", (String) map1.get("key1"));

        // force commit anyhow...
        txMap1.commitTransaction(true);
        // after successful commit it must be the value written by this thread
        report("value3", (String) txMap1.get("key1"));
        report("value3", (String) map1.get("key1"));
    }

    public void testTxControl() throws Throwable
    {
        super.testTxControl();
    }

}
