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

package org.apache.commons.pool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Object factory with configurable latencies for object lifecycle methods.
 * This factory will also track and enforce maxActive, maxActivePerKey contracts.
 * If the factory's maxActive / maxActivePerKey are set to match those of the
 * pool, makeObject will throw IllegalStateException if the number of makes - destroys
 * (per key) exceeds the configured max.
 *
 */
public class WaiterFactory<K> implements PoolableObjectFactory<Waiter>,
    KeyedPoolableObjectFactory<K, Waiter>
{

    /** Latency of activateObject */
    private final long activateLatency;

    /** Latency of destroyObject */
    private final long destroyLatency;

    /** Latency of makeObject */
    private final long makeLatency;

    /** Latency of passivateObject */
    private final long passivateLatency;

    /** Latency of validateObject */
    private final long validateLatency;

    /** Latency of doWait for Waiter instances created by this factory */
    private final long waiterLatency;

    /** Probability that passivation will invalidate Waiter instances */
    private final double passivateInvalidationProbability;

    /** Count of (makes - destroys) since last reset */
    private long activeCount = 0;

    /** Count of (makes - destroys) per key since last reset */
    private Map<K, Integer> activeCounts = new HashMap<K, Integer>();

    /** Maximum of (makes - destroys) - if exceeded IllegalStateException */
    private final long maxActive;  // GKOP 1.x calls this maxTotal

    /** Maximum of (makes - destroys) per key */
    private final long maxActivePerKey;  // GKOP 1.x calls this maxActive

    public WaiterFactory(long activateLatency, long destroyLatency,
                         long makeLatency, long passivateLatency, long validateLatency,
                         long waiterLatency,long maxActive, long maxActivePerKey,
                         double passivateInvalidationProbability)
    {
        this.activateLatency = activateLatency;
        this.destroyLatency = destroyLatency;
        this.makeLatency = makeLatency;
        this.passivateLatency = passivateLatency;
        this.validateLatency = validateLatency;
        this.waiterLatency = waiterLatency;
        this.maxActive = maxActive;
        this.maxActivePerKey = maxActivePerKey;
        this.passivateInvalidationProbability = passivateInvalidationProbability;
    }

    public WaiterFactory(long activateLatency, long destroyLatency,
                         long makeLatency, long passivateLatency, long validateLatency,
                         long waiterLatency)
    {
        this(activateLatency, destroyLatency, makeLatency, passivateLatency,
             validateLatency, waiterLatency, Long.MAX_VALUE, Long.MAX_VALUE, 0);
    }

    public WaiterFactory(long activateLatency, long destroyLatency,
                         long makeLatency, long passivateLatency, long validateLatency,
                         long waiterLatency,long maxActive)
    {
        this(activateLatency, destroyLatency, makeLatency, passivateLatency,
             validateLatency, waiterLatency, maxActive, Long.MAX_VALUE, 0);
    }

    public void activateObject(Waiter obj) throws Exception
    {
        doWait(activateLatency);
        obj.setActive(true);
    }

    public void destroyObject(Waiter obj) throws Exception
    {
        doWait(destroyLatency);
        obj.setValid(false);
        obj.setActive(false);
        // Decrement *after* destroy
        synchronized (this)
        {
            activeCount--;
        }
    }

    public Waiter makeObject() throws Exception
    {
        // Increment and test *before* make
        synchronized (this)
        {
            if (activeCount >= maxActive)
            {
                throw new IllegalStateException("Too many active instances: " +
                                                activeCount + " in circulation with maxActive = " + maxActive);
            }
            else
            {
                activeCount++;
            }
        }
        doWait(makeLatency);
        return new Waiter(false, true, waiterLatency);
    }

    public void passivateObject(Waiter arg0) throws Exception
    {
        arg0.setActive(false);
        doWait(passivateLatency);
        if (Math.random() < passivateInvalidationProbability)
        {
            arg0.setValid(false);
        }
    }

    public boolean validateObject(Waiter arg0)
    {
        doWait(validateLatency);
        return arg0.isValid();
    }

    protected void doWait(long latency)
    {
        try
        {
            Thread.sleep(latency);
        }
        catch (InterruptedException ex)
        {
            // ignore
        }
    }

    public synchronized void reset()
    {
        activeCount = 0;
        if (activeCounts.isEmpty())
        {
            return;
        }
        Iterator<K> it = activeCounts.keySet().iterator();
        while (it.hasNext())
        {
            K key = it.next();
            activeCounts.put(key, new Integer (0));
        }
    }

    /**
     * @return the maxActive
     */
    public synchronized long getMaxActive()
    {
        return maxActive;
    }

    // KeyedPoolableObjectFactory methods

    public void activateObject(K key, Waiter obj) throws Exception
    {
        activateObject(obj);
    }

    public void destroyObject(K key, Waiter obj) throws Exception
    {
        destroyObject(obj);
        synchronized (this)
        {
            Integer count = activeCounts.get(key);
            activeCounts.put(key, new Integer(count.intValue() - 1));
        }
    }

    public Waiter makeObject(K key) throws Exception
    {
        synchronized (this)
        {
            Integer count = activeCounts.get(key);
            if (count == null)
            {
                count = new Integer(1);
                activeCounts.put(key, count);
            }
            else
            {
                if (count.intValue() >= maxActivePerKey)
                {
                    throw new IllegalStateException("Too many active " +
                                                    "instances for key = " + key + ": " + count.intValue() +
                                                    " in circulation " + "with maxActivePerKey = " +
                                                    maxActivePerKey);
                }
                else
                {
                    activeCounts.put(key, new Integer(count.intValue() + 1));
                }
            }
        }
        return makeObject();
    }

    public void passivateObject(K key, Waiter obj) throws Exception
    {
        passivateObject(obj);
    }

    public boolean validateObject(K key, Waiter obj)
    {
        return validateObject(obj);
    }

}
