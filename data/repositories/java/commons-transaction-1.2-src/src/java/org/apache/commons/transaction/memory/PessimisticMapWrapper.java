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
import java.util.Map;
import java.util.Set;

import org.apache.commons.transaction.locking.ReadWriteLockManager;
import org.apache.commons.transaction.util.LoggerFacade;

/**
 * Wrapper that adds transactional control to all kinds of maps that implement the {@link Map} interface. By using
 * pessimistic transaction control (blocking locks) this wrapper has better isolation than {@link TransactionalMapWrapper}, but
 * also has less possible concurrency and may even deadlock. A commit, however, will never fail.
 * <br>
 * Start a transaction by calling {@link #startTransaction()}. Then perform the normal actions on the map and
 * finally either call {@link #commitTransaction()} to make your changes permanent or {@link #rollbackTransaction()} to
 * undo them.
 * <br>
 * <em>Caution:</em> Do not modify values retrieved by {@link #get(Object)} as this will circumvent the transactional mechanism.
 * Rather clone the value or copy it in a way you see fit and store it back using {@link #put(Object, Object)}.
 * <br>
 * <em>Note:</em> This wrapper guarantees isolation level <code>SERIALIZABLE</code>.
 *
 * @version $Id: PessimisticMapWrapper.java 493628 2007-01-07 01:42:48Z joerg $
 * @see TransactionalMapWrapper
 * @see OptimisticMapWrapper
 */
public class PessimisticMapWrapper extends TransactionalMapWrapper
{

    protected static final int READ = 1;
    protected static final int WRITE = 2;

    protected static final Object GLOBAL_LOCK = "GLOBAL";

    protected ReadWriteLockManager lockManager;
//    protected MultiLevelLock globalLock;
    protected long readTimeOut = 60000; /* FIXME: pass in ctor */

    /**
     * Creates a new pessimistic transactional map wrapper. Temporary maps and sets to store transactional
     * data will be instances of {@link java.util.HashMap} and {@link java.util.HashSet}.
     *
     * @param wrapped map to be wrapped
     * @param logger
     *            generic logger used for all kinds of logging
     */
    public PessimisticMapWrapper(Map wrapped, LoggerFacade logger)
    {
        this(wrapped, new HashMapFactory(), new HashSetFactory(), logger);
    }

    /**
     * Creates a new pessimistic transactional map wrapper. Temporary maps and sets to store transactional
     * data will be created and disposed using {@link MapFactory} and {@link SetFactory}.
     *
     * @param wrapped map to be wrapped
     * @param mapFactory factory for temporary maps
     * @param setFactory factory for temporary sets
     * @param logger
     *            generic logger used for all kinds of logging
     */
    public PessimisticMapWrapper(Map wrapped, MapFactory mapFactory, SetFactory setFactory, LoggerFacade logger)
    {
        super(wrapped, mapFactory, setFactory);
        lockManager = new ReadWriteLockManager(logger, readTimeOut);
//        globalLock = new GenericLock(GLOBAL_LOCK_NAME, WRITE, logger);
    }

    public void startTransaction()
    {
        if (getActiveTx() != null)
        {
            throw new IllegalStateException(
                "Active thread " + Thread.currentThread() + " already associated with a transaction!");
        }
        LockingTxContext context = new LockingTxContext();
        setActiveTx(context);
    }

    public Collection values()
    {
        assureGlobalReadLock();
        return super.values();
    }

    public Set entrySet()
    {
        assureGlobalReadLock();
        return super.entrySet();
    }

    public Set keySet()
    {
        assureGlobalReadLock();
        return super.keySet();
    }

    public Object remove(Object key)
    {
        // assure we get a write lock before super can get a read lock to avoid lots
        // of deadlocks
        assureWriteLock(key);
        return super.remove(key);
    }

    public Object put(Object key, Object value)
    {
        // assure we get a write lock before super can get a read lock to avoid lots
        // of deadlocks
        assureWriteLock(key);
        return super.put(key, value);
    }

    protected void assureWriteLock(Object key)
    {
        LockingTxContext txContext = (LockingTxContext) getActiveTx();
        if (txContext != null)
        {
            lockManager.writeLock(txContext, key);
            // XXX fake intention lock (prohibits global WRITE)
            lockManager.readLock(txContext, GLOBAL_LOCK);
        }
    }

    protected void assureGlobalReadLock()
    {
        LockingTxContext txContext = (LockingTxContext) getActiveTx();
        if (txContext != null)
        {
            // XXX fake intention lock (prohibits global WRITE)
            lockManager.readLock(txContext, GLOBAL_LOCK);
        }
    }

    public class LockingTxContext extends TxContext
    {

        protected Set keys()
        {
            lockManager.readLock(this, GLOBAL_LOCK);
            return super.keys();
        }

        protected Object get(Object key)
        {
            lockManager.readLock(this, key);
            // XXX fake intention lock (prohibits global WRITE)
            lockManager.readLock(this, GLOBAL_LOCK);
            return super.get(key);
        }

        protected void put(Object key, Object value)
        {
            lockManager.writeLock(this, key);
            // XXX fake intention lock (prohibits global WRITE)
            lockManager.readLock(this, GLOBAL_LOCK);
            super.put(key, value);
        }

        protected void remove(Object key)
        {
            lockManager.writeLock(this, key);
            // XXX fake intention lock (prohibits global WRITE)
            lockManager.readLock(this, GLOBAL_LOCK);
            super.remove(key);
        }

        protected int size()
        {
            // XXX this is bad luck, we need a global read lock just for the size :( :( :(
            lockManager.readLock(this, GLOBAL_LOCK);
            return super.size();
        }

        protected void clear()
        {
            lockManager.writeLock(this, GLOBAL_LOCK);
            super.clear();
        }

        protected void dispose()
        {
            super.dispose();
            lockManager.releaseAll(this);
        }

        protected void finalize() throws Throwable
        {
            dispose();
            super.finalize();
        }
    }

}
