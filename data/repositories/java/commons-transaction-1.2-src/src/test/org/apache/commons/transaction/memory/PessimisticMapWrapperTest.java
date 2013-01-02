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

import org.apache.commons.transaction.locking.LockException;
import org.apache.commons.transaction.util.CommonsLoggingLogger;
import org.apache.commons.transaction.util.LoggerFacade;
import org.apache.commons.transaction.util.RendezvousBarrier;

/**
 * Tests for map wrapper.
 *
 * @version $Id: PessimisticMapWrapperTest.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class PessimisticMapWrapperTest extends MapWrapperTest
{

    private static final Log log = LogFactory.getLog(PessimisticMapWrapperTest.class.getName());
    private static final LoggerFacade sLogger = new CommonsLoggingLogger(log);

    protected static final long TIMEOUT = Long.MAX_VALUE;

    private static int deadlockCnt = 0;

    public static Test suite()
    {
        TestSuite suite = new TestSuite(PessimisticMapWrapperTest.class);
        return suite;
    }

    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    public PessimisticMapWrapperTest(String testName)
    {
        super(testName);
    }

    protected TransactionalMapWrapper getNewWrapper(Map map)
    {
        return new PessimisticMapWrapper(map, sLogger);
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
        sLogger.logInfo("Checking concurrent transaction features");

        final Map map1 = new HashMap();

        final PessimisticMapWrapper txMap1 = (PessimisticMapWrapper) getNewWrapper(map1);

        Thread thread1 = new Thread(new Runnable()
        {
            public void run()
            {
                txMap1.startTransaction();
                txMap1.put("key1", "value2");
                synchronized (txMap1)
                {
                    txMap1.commitTransaction();
                    report("value2", (String) txMap1.get("key1"));
                }
            }
        }, "Thread1");

        txMap1.put("key1", "value1");

        txMap1.startTransaction();

        report("value1", (String) txMap1.get("key1"));

        thread1.start();

        // we have serializable as isolation level, that's why I will still see the old value
        report("value1", (String) txMap1.get("key1"));

        txMap1.put("key1", "value3");

        // after commit it must be our value
        synchronized (txMap1)
        {
            txMap1.commitTransaction();
            report("value3", (String) txMap1.get("key1"));
        }
    }

    public void testConflict() throws Throwable
    {
        sLogger.logInfo("Checking concurrent transaction features");

        final Map map1 = new HashMap();

        final PessimisticMapWrapper txMap1 = (PessimisticMapWrapper) getNewWrapper(map1);

        final RendezvousBarrier restart = new RendezvousBarrier("restart",
                TIMEOUT, sLogger);

        for (int i = 0; i < 25; i++)
        {

            final RendezvousBarrier deadlockBarrier1 = new RendezvousBarrier("deadlock" + i,
                    TIMEOUT, sLogger);

            Thread thread1 = new Thread(new Runnable()
            {
                public void run()
                {
                    txMap1.startTransaction();
                    try
                    {
                        // first both threads get a lock, this one on res2
                        txMap1.put("key2", "value2");
                        synchronized (deadlockBarrier1)
                        {
                            deadlockBarrier1.meet();
                            deadlockBarrier1.reset();
                        }
                        // if I am first, the other thread will be dead, i.e.
                        // exactly one
                        txMap1.put("key1", "value2");
                        txMap1.commitTransaction();
                    }
                    catch (LockException le)
                    {
                        assertEquals(le.getCode(), LockException.CODE_DEADLOCK_VICTIM);
                        deadlockCnt++;
                        txMap1.rollbackTransaction();
                    }
                    catch (InterruptedException ie)
                    {
                    }
                    finally
                    {
                        try
                        {
                            synchronized (restart)
                            {
                                restart.meet();
                                restart.reset();
                            }
                        }
                        catch (InterruptedException ie) {}

                    }
                }
            }, "Thread1");

            thread1.start();

            txMap1.startTransaction();
            try
            {
                // first both threads get a lock, this one on res2
                txMap1.get("key1");
                synchronized (deadlockBarrier1)
                {
                    deadlockBarrier1.meet();
                    deadlockBarrier1.reset();
                }
                //          if I am first, the other thread will be dead, i.e. exactly
                // one
                txMap1.get("key2");
                txMap1.commitTransaction();
            }
            catch (LockException le)
            {
                assertEquals(le.getCode(), LockException.CODE_DEADLOCK_VICTIM);
                deadlockCnt++;
                txMap1.rollbackTransaction();
            }
            finally
            {
                try
                {
                    synchronized (restart)
                    {
                        restart.meet();
                        restart.reset();
                    }
                }
                catch (InterruptedException ie) {}

            }

            // XXX in special scenarios the current implementation might cause both
            // owners to be deadlock victims
            if (deadlockCnt != 1)
            {
                sLogger.logWarning("More than one thread was deadlock victim!");
            }
            assertTrue(deadlockCnt >= 1);
            deadlockCnt = 0;
        }
    }

    public void testTxControl() throws Throwable
    {
        super.testTxControl();
    }

}
