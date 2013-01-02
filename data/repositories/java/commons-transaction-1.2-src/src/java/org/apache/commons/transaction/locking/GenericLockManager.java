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
package org.apache.commons.transaction.locking;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.transaction.util.LoggerFacade;

/**
 * Manager for {@link GenericLock}s on resources. This implementation includes
 * <ul>
 * <li>deadlock detection, which is configurable to come into effect after an initial short waiting
 * lock request; this is useful as it is somewhat expensive
 * <li>global transaction timeouts that actively revoke granted rights from transactions
 * </ul>
 *
 * @version $Id: GenericLockManager.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class GenericLockManager implements LockManager, LockManager2
{

    public static final long DEFAULT_TIMEOUT = 30000;
    public static final long DEFAULT_CHECK_THRESHHOLD = 500;

    /** Maps onwerId to locks it (partially) owns. */
    protected Map globalOwners = Collections.synchronizedMap(new HashMap());

    /** Maps resourceId to lock. */
    protected Map globalLocks = new HashMap();

    /** Maps onwerId to global effective time outs (i.e. the time the lock will time out). */
    protected Map effectiveGlobalTimeouts = Collections.synchronizedMap(new HashMap());

    protected Set timedOutOwners = Collections.synchronizedSet(new HashSet());

    protected int maxLockLevel = -1;
    protected LoggerFacade logger;
    protected long globalTimeoutMSecs;
    protected long checkThreshhold;

    /**
     * Creates a new generic lock manager.
     *
     * @param maxLockLevel
     *            highest allowed lock level as described in {@link GenericLock}
     *            's class intro
     * @param logger
     *            generic logger used for all kind of debug logging
     * @param timeoutMSecs
     *            specifies the maximum time to wait for a lock in milliseconds
     * @param checkThreshholdMSecs
     *            specifies a special wait threshhold before deadlock and
     *            timeout detection come into play or <code>-1</code> switch
     *            it off and check for directly
     * @throws IllegalArgumentException
     *             if maxLockLevel is less than 1
     *
     * @since 1.1
     */
    public GenericLockManager(int maxLockLevel, LoggerFacade logger, long timeoutMSecs,
                              long checkThreshholdMSecs) throws IllegalArgumentException
    {
        if (maxLockLevel < 1)
            throw new IllegalArgumentException("The maximum lock level must be at least 1 ("
                                               + maxLockLevel + " was specified)");
        this.maxLockLevel = maxLockLevel;
        this.logger = logger.createLogger("Locking");
        this.globalTimeoutMSecs = timeoutMSecs;
        this.checkThreshhold = checkThreshholdMSecs;
    }

    public GenericLockManager(int maxLockLevel, LoggerFacade logger, long timeoutMSecs)
    throws IllegalArgumentException
    {
        this(maxLockLevel, logger, timeoutMSecs, DEFAULT_CHECK_THRESHHOLD);
    }

    public GenericLockManager(int maxLockLevel, LoggerFacade logger)
    throws IllegalArgumentException
    {
        this(maxLockLevel, logger, DEFAULT_TIMEOUT);
    }

    /**
     * @see LockManager2#startGlobalTimeout(Object, long)
     * @since 1.1
     */
    public void startGlobalTimeout(Object ownerId, long timeoutMSecs)
    {
        long now = System.currentTimeMillis();
        long timeout = now + timeoutMSecs;
        effectiveGlobalTimeouts.put(ownerId, new Long(timeout));
    }

    /**
     * @see LockManager2#tryLock(Object, Object, int, boolean)
     * @since 1.1
     */
    public boolean tryLock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant)
    {
        timeoutCheck(ownerId);

        GenericLock lock = (GenericLock) atomicGetOrCreateLock(resourceId);
        boolean acquired = lock.tryLock(ownerId, targetLockLevel,
                                        reentrant ? GenericLock.COMPATIBILITY_REENTRANT : GenericLock.COMPATIBILITY_NONE,
                                        false);

        if (acquired)
        {
            addOwner(ownerId, lock);
        }
        return acquired;
    }

    /**
     * @see LockManager2#checkLock(Object, Object, int, boolean)
     * @since 1.1
     */
    public boolean checkLock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant)
    {
        timeoutCheck(ownerId);
        boolean possible = true;

        GenericLock lock = (GenericLock) getLock(resourceId);
        if (lock != null)
        {
            possible = lock.test(ownerId, targetLockLevel,
                                 reentrant ? GenericLock.COMPATIBILITY_REENTRANT
                                 : GenericLock.COMPATIBILITY_NONE);
        }
        return possible;
    }

    /**
     * @see LockManager2#hasLock(Object, Object, int)
     * @since 1.1
     */
    public boolean hasLock(Object ownerId, Object resourceId, int lockLevel)
    {
        timeoutCheck(ownerId);
        boolean owned = false;

        GenericLock lock = (GenericLock) getLock(resourceId);
        if (lock != null)
        {
            owned = lock.has(ownerId, lockLevel);
        }
        return owned;
    }

    /**
     * @see LockManager2#lock(Object, Object, int, boolean)
     * @since 1.1
     */
    public void lock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant)
    throws LockException
    {
        lock(ownerId, resourceId, targetLockLevel, reentrant, globalTimeoutMSecs);
    }

    /**
     * @see LockManager2#lock(Object, Object, int, boolean, long)
     * @since 1.1
     */
    public void lock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant,
                     long timeoutMSecs) throws LockException
    {
        lock(ownerId, resourceId, targetLockLevel, reentrant ? GenericLock.COMPATIBILITY_REENTRANT
             : GenericLock.COMPATIBILITY_NONE, false, timeoutMSecs);
    }

    /**
     * @see LockManager2#lock(Object, Object, int, int, boolean, long)
     * @since 1.1
     */
    public void lock(Object ownerId, Object resourceId, int targetLockLevel, int compatibility,
                     boolean preferred, long timeoutMSecs) throws LockException
    {
        timeoutCheck(ownerId);
        GenericLock lock = (GenericLock) atomicGetOrCreateLock(resourceId);
        doLock(lock, ownerId, resourceId, targetLockLevel, compatibility, preferred, timeoutMSecs);
    }

    protected void doLock(GenericLock lock, Object ownerId, Object resourceId, int targetLockLevel,
                          int compatibility, boolean preferred, long timeoutMSecs)
    {
        long now = System.currentTimeMillis();
        long waitEnd = now + timeoutMSecs;

        timeoutCheck(ownerId);

        GenericLock.LockOwner lockWaiter = new GenericLock.LockOwner(ownerId, targetLockLevel,
                compatibility, preferred);

        boolean acquired = false;
        try
        {

            // detection for deadlocks and time outs is rather expensive,
            // so we wait for the lock for a
            // short time (<5 seconds) to see if we get it without checking;
            // if not we still can check what the reason for this is
            if (checkThreshhold != -1 && timeoutMSecs > checkThreshhold)
            {
                acquired = lock
                           .acquire(ownerId, targetLockLevel, true, compatibility,
                                    preferred, checkThreshhold);
                timeoutMSecs -= checkThreshhold;
            }
            else
            {
                acquired = lock
                           .acquire(ownerId, targetLockLevel, false, compatibility,
                                    preferred, checkThreshhold);

            }
            if (acquired)
            {
                addOwner(ownerId, lock);
                return;
            }
        }
        catch (InterruptedException e)
        {
            throw new LockException("Interrupted", LockException.CODE_INTERRUPTED, resourceId);
        }
        try
        {
            lock.registerWaiter(lockWaiter);

            boolean deadlock = wouldDeadlock(ownerId, new HashSet());
            if (deadlock)
            {
                throw new LockException("Lock would cause deadlock",
                                        LockException.CODE_DEADLOCK_VICTIM, resourceId);
            }

            now = System.currentTimeMillis();
            while (!acquired && waitEnd > now)
            {

                // first be sure all locks are stolen from owners that have already timed out
                releaseTimedOutOwners();

                // if there are owners we conflict with lets see if one of them globally times
                // out earlier than this lock, if so we will wake up then to check again
                Set conflicts = lock.getConflictingOwners(ownerId, targetLockLevel, compatibility);
                long nextConflictTimeout = getNextGlobalConflictTimeout(conflicts);
                if (nextConflictTimeout != -1 && nextConflictTimeout < waitEnd)
                {
                    timeoutMSecs = nextConflictTimeout - now;
                    // XXX add 10% to ensure the lock really is timed out
                    timeoutMSecs += timeoutMSecs / 10;
                }
                else
                {
                    timeoutMSecs = waitEnd - now;
                }

                // XXX acquire will remove us as a waiter, but it is important to remain us such
                // to constantly indicate it to other owners, otherwise there might be undetected
                // deadlocks
                synchronized (lock)
                {
                    acquired = lock.acquire(ownerId, targetLockLevel, true, compatibility,
                                            preferred, timeoutMSecs);
                    lock.registerWaiter(lockWaiter);
                }
                now = System.currentTimeMillis();
            }
            if (!acquired)
            {
                throw new LockException("Lock wait timed out", LockException.CODE_TIMED_OUT,
                                        resourceId);
            }
            else
            {
                addOwner(ownerId, lock);
            }
        }
        catch (InterruptedException e)
        {
            throw new LockException("Interrupted", LockException.CODE_INTERRUPTED, resourceId);
        }
        finally
        {
            lock.unregisterWaiter(lockWaiter);
        }
    }

    /**
     * @see LockManager2#getLevel(Object, Object)
     * @since 1.1
     */
    public int getLevel(Object ownerId, Object resourceId)
    {
        timeoutCheck(ownerId);
        GenericLock lock = (GenericLock) getLock(resourceId);
        if (lock != null)
        {
            return lock.getLockLevel(ownerId);
        }
        else
        {
            return 0;
        }
    }

    /**
     * @see LockManager2#release(Object, Object)
     * @since 1.1
     */
    public boolean release(Object ownerId, Object resourceId)
    {
        timeoutCheck(ownerId);
        boolean released = false;

        GenericLock lock = (GenericLock) getLock(resourceId);
        if (lock != null)
        {
            released = lock.release(ownerId);
            removeOwner(ownerId, lock);
        }
        return released;
    }

    /**
     * @see LockManager2#releaseAll(Object)
     * @since 1.1
     */
    public void releaseAll(Object ownerId)
    {
        releaseAllNoTimeOutReset(ownerId);
        // reset time out status for this owner
        timedOutOwners.remove(ownerId);
        effectiveGlobalTimeouts.remove(ownerId);
    }

    protected void releaseAllNoTimeOutReset(Object ownerId)
    {
        Set locks = (Set) globalOwners.get(ownerId);
        if (locks != null)
        {
            Collection locksCopy;
            // need to copy in order not to interfere with wouldDeadlock
            // possibly called by
            // other threads
            synchronized (locks)
            {
                locksCopy = new ArrayList(locks);
            }
            for (Iterator it = locksCopy.iterator(); it.hasNext();)
            {
                GenericLock lock = (GenericLock) it.next();
                lock.release(ownerId);
                locks.remove(lock);
            }
        }
    }

    /**
     * @see LockManager2#getAll(Object)
     * @since 1.1
     */
    public Set getAll(Object ownerId)
    {
        Set locks = (Set) globalOwners.get(ownerId);
        if (locks == null)
        {
            return new HashSet();
        }
        else
        {
            return locks;
        }
    }

    protected void addOwner(Object ownerId, GenericLock lock)
    {
        synchronized (globalOwners)
        {
            Set locks = (Set) globalOwners.get(ownerId);
            if (locks == null)
            {
                locks = Collections.synchronizedSet(new HashSet());
                globalOwners.put(ownerId, locks);
            }
            locks.add(lock);
        }
    }

    protected void removeOwner(Object ownerId, GenericLock lock)
    {
        Set locks = (Set) globalOwners.get(ownerId);
        if (locks != null)
        {
            locks.remove(lock);
        }
    }

    /**
     * Checks if an owner is deadlocked. <br>
     * <br>
     * We traverse the tree recursively formed by owners, locks held by them and
     * then again owners waiting for the locks. If there is a cycle in one of
     * the paths from the root to a leaf we have a deadlock. <br>
     * <br>
     * A more detailed discussion on deadlocks and definitions and how to detect
     * them can be found in <a
     * href="http://www.onjava.com/pub/a/onjava/2004/10/20/threads2.html?page=1">this
     * nice article </a>. <br>
     * <em>Caution:</em> This computation can be very expensive with many
     * owners and locks. Worst (unlikely) case is exponential.
     *
     * @param ownerId
     *            the owner to check for being deadlocked
     * @param path
     *            initially should be called with an empty set
     * @return <code>true</code> if the owner is deadlocked,
     *         <code>false</code> otherwise
     */
    protected boolean wouldDeadlock(Object ownerId, Set path)
    {
        path.add(ownerId);
        // these are our locks
        Set locks = (Set) globalOwners.get(ownerId);
        if (locks != null)
        {
            Collection locksCopy;
            // need to copy in order not to interfere with releaseAll possibly called by
            // other threads
            synchronized (locks)
            {
                locksCopy = new ArrayList(locks);
            }
            for (Iterator i = locksCopy.iterator(); i.hasNext();)
            {
                GenericLock mylock = (GenericLock) i.next();
                // these are the ones waiting for one of our locks
                Collection conflicts = mylock.getConflictingWaiters(ownerId);
                if (conflicts != null)
                {
                    for (Iterator j = conflicts.iterator(); j.hasNext();)
                    {
                        Object waitingOwnerId = j.next();
                        if (path.contains(waitingOwnerId))
                        {
                            return true;
                        }
                        else if (wouldDeadlock(waitingOwnerId, path))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        path.remove(ownerId);
        return false;
    }

    protected boolean releaseTimedOutOwners()
    {
        boolean released = false;
        synchronized (effectiveGlobalTimeouts)
        {
            for (Iterator it = effectiveGlobalTimeouts.entrySet().iterator(); it.hasNext();)
            {
                Map.Entry entry = (Map.Entry) it.next();
                Object ownerId = entry.getKey();
                long timeout = ((Long) entry.getValue()).longValue();
                long now = System.currentTimeMillis();
                if (timeout < now)
                {
                    releaseAllNoTimeOutReset(ownerId);
                    timedOutOwners.add(ownerId);
                    released = true;
                }
            }
        }
        return released;
    }

    protected boolean timeOut(Object ownerId)
    {
        Long timeout = (Long)effectiveGlobalTimeouts.get(ownerId);
        long now = System.currentTimeMillis();
        if (timeout != null && timeout.longValue() < now)
        {
            releaseAll(ownerId);
            timedOutOwners.add(ownerId);
            return true;
        }
        else
        {
            return false;
        }
    }

    protected long getNextGlobalConflictTimeout(Set conflicts)
    {
        long minTimeout = -1;
        long now = System.currentTimeMillis();
        if (conflicts != null)
        {
            synchronized (effectiveGlobalTimeouts)
            {
                for (Iterator it = effectiveGlobalTimeouts.entrySet().iterator(); it.hasNext();)
                {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object ownerId = entry.getKey();
                    if (conflicts.contains(ownerId))
                    {
                        long timeout = ((Long) entry.getValue()).longValue();
                        if (minTimeout == -1 || timeout < minTimeout)
                        {
                            minTimeout = timeout;
                        }
                    }
                }
            }
        }
        return minTimeout;
    }

    public MultiLevelLock getLock(Object resourceId)
    {
        synchronized (globalLocks)
        {
            return (MultiLevelLock) globalLocks.get(resourceId);
        }
    }

    public MultiLevelLock atomicGetOrCreateLock(Object resourceId)
    {
        synchronized (globalLocks)
        {
            MultiLevelLock lock = getLock(resourceId);
            if (lock == null)
            {
                lock = createLock(resourceId);
            }
            return lock;
        }
    }

    public void removeLock(MultiLevelLock lock)
    {
        synchronized (globalLocks)
        {
            globalLocks.remove(lock);
        }
    }

    /**
     * Gets all locks as orignials, <em>no copies</em>.
     *
     * @return collection holding all locks.
     */
    public Collection getLocks()
    {
        synchronized (globalLocks)
        {
            return globalLocks.values();
        }
    }

    public synchronized String toString()
    {
        StringBuffer buf = new StringBuffer(1000);
        for (Iterator it = globalLocks.values().iterator(); it.hasNext();)
        {
            GenericLock lock = (GenericLock) it.next();
            buf.append(lock.toString()).append('\n');
        }
        return buf.toString();
    }

    protected GenericLock createLock(Object resourceId)
    {
        synchronized (globalLocks)
        {
            GenericLock lock = new GenericLock(resourceId, maxLockLevel, logger);
            globalLocks.put(resourceId, lock);
            return lock;
        }
    }

    protected void timeoutCheck(Object ownerId) throws LockException
    {
        timeOut(ownerId);
        if (timedOutOwners.contains(ownerId))
        {
            throw new LockException(
                "All locks of owner "
                + ownerId
                + " have globally timed out."
                + " You will not be able to to continue with this owner until you call releaseAll.",
                LockException.CODE_TIMED_OUT, null);
        }
    }


}
