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
 * Simple turn based barrier to make a sequence of calls from different threads deterministic.
 * This is very useful for testing where you want to have a continuous flow throughout
 * different threads. The idea is to have an ordered sequence of steps where step n can not be
 * executed before n-1.
 *
 * @version $Id: TurnBarrier.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class TurnBarrier
{

    public static final long DEFAULT_TIMEOUT = Long.MAX_VALUE;

    protected final String name;

    protected int currentNumber;

    protected final int startNumber;

    protected final long timeout;

    protected LoggerFacade logger;

    /**
     * Creates a new turn barrier starting with turn 0 with an unlimited timeout.
     *
     * @param name the name of the barrier
     * @param logger logger for debug output
     */
    public TurnBarrier(String name, LoggerFacade logger)
    {
        this(name, DEFAULT_TIMEOUT, logger);
    }

    /**
     * Creates a new turn barrier starting with turn 0.
     *
     * @param name the name of the barrier
     * @param timeout timeout for threads to wait for their turn
     * @param logger logger for debug output
     */
    public TurnBarrier(String name, long timeout, LoggerFacade logger)
    {
        this(name, timeout, logger, 0);
    }

    /**
     * Creates a new turn barrier.
     *
     * @param name the name of the barrier
     * @param timeout timeout for threads to wait for their turn
     * @param logger logger for debug output
     * @param startTurn the turn to start with
     */
    public TurnBarrier(String name, long timeout, LoggerFacade logger, int startTurn)
    {
        this.name = name;
        this.timeout = timeout;
        this.logger = logger;
        this.startNumber = startTurn;
        this.currentNumber = startTurn;
    }

    /**
     * Blockingly waits for the given turn. If a timeout occurs a runtime exception will be thrown.
     *
     * @param turnNumber the turn number to wait for
     * @throws InterruptedException thrown if the thread is interrupted while waiting
     * @throws RuntimeException thrown when timed out
     */
    public synchronized void waitForTurn(int turnNumber) throws InterruptedException,
        RuntimeException
    {
        if (turnNumber > currentNumber)
        {
            long started = System.currentTimeMillis();
            for (long remaining = timeout; remaining > 0 && turnNumber > currentNumber; remaining = timeout
                    - (System.currentTimeMillis() - started))
            {
                wait(remaining);
            }
        }
        if (turnNumber > currentNumber)
        {
            throw new RuntimeException("Timed out while waiting for our turn");
        }
    }

    /**
     * Signals the next turn. Any thread waiting for the turn will be allowed to continue.
     *
     * @param turnNumber the next turn number
     */
    public synchronized void signalTurn(int turnNumber)
    {
        currentNumber = turnNumber;
        notifyAll();
    }

    /**
     * Starts the barrier over again. The next turn will be the one the barrier was started with.
     *
     */
    public synchronized void reset()
    {
        signalTurn(startNumber);
    }
}
