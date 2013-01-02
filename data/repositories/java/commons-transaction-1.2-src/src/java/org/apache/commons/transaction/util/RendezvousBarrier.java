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
package org.apache.commons.transaction.util;

/**
 * Simple barrier that blocks until all parties have either called or have arrived at the meeting point.
 * Very useful for testing or other purposes that require to make concurrent settings deterministic.
 *
 * @version $Id: RendezvousBarrier.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class RendezvousBarrier
{

    public static final int DEFAULT_TIMEOUT = 20000;

    protected final int parties;
    protected final String name;
    protected int count = 0;
    protected long timeout;
    protected LoggerFacade logger;

    public RendezvousBarrier(String name, LoggerFacade logger)
    {
        this(name, DEFAULT_TIMEOUT, logger);
    }

    public RendezvousBarrier(String name, long timeout, LoggerFacade logger)
    {
        this(name, 2, timeout, logger);
    }

    public RendezvousBarrier(String name, int parties, long timeout, LoggerFacade logger)
    {
        this.parties = parties;
        this.name = name;
        this.timeout = timeout;
        this.logger = logger;
    }

    /**
     * Notify the barrier that you (the current thread) will not come to the meeting point.
     * Same thing as {@link #meet()}, but does not not let you wait.
     */
    public synchronized void call()
    {
        count++;
        if (count >= parties)
        {
            if (logger.isFineEnabled())
                logger.logFine("Thread " + Thread.currentThread().getName() + " by CALL COMPLETING barrier " + name);
            notifyAll();
        }
    }

    /**
     * Meet at this barrier. The current thread will either block when there are missing parties for this barrier
     * or it is the last one to complete this meeting and the barrier will release its block.
     * In this case all other waiting threads will be notified.
     *
     * @throws InterruptedException if the current thread is interrupted while waiting
     */
    public synchronized void meet() throws InterruptedException
    {
        count++;
        if (count >= parties)
        {
            if (logger.isFineEnabled())
                logger.logFine("Thread " + Thread.currentThread().getName() + " by MEET COMPLETING barrier " + name);
            notifyAll();
        }
        else
        {
            if (logger.isFineEnabled())
            {
                logger.logFine(
                    "At barrier "
                    + name
                    + " thread "
                    + Thread.currentThread().getName()
                    + " WAITING for "
                    + (parties - count)
                    + " of "
                    + parties
                    + " parties");
            }
            wait(timeout);
            if (count == 0)
            {
                // means the barrier has been reset
            }
            else if (count >= parties)
            {
                if (logger.isFineEnabled())
                    logger.logFine("Thread " + Thread.currentThread().getName() + " CONTINUING at barrier " + name);
            }
            else
            {
                if (logger.isFineEnabled())
                    logger.logFine("Thread " + Thread.currentThread().getName() + " FAILING at barrier " + name);
                notifyAll();
            }
        }
    }

    /**
     * Releases all waiting threads and resets the number of parties already arrived.
     */
    public synchronized void reset()
    {
        if (logger.isFineEnabled()) logger.logFine("Resetting barrier " + name);
        count = 0;
        notifyAll();
    }

}
