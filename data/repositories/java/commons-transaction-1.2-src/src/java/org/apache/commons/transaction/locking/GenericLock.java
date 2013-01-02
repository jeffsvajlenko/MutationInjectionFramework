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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.transaction.util.LoggerFacade;

/**
 * A generic implementain of a simple multi level lock.
 *
 * <p>
 * The idea is to have an ascending number of lock levels ranging from
 * <code>0</code> to <code>maxLockLevel</code> as specified in
 * {@link #GenericLock(Object, int, LoggerFacade)}: the higher the lock level
 * the stronger and more restrictive the lock. To determine which lock may
 * coexist with other locks you have to imagine matching pairs of lock levels.
 * For each pair both parts allow for all lock levels less than or equal to the
 * matching other part. Pairs are composed by the lowest and highest level not
 * yet part of a pair and successively applying this method until no lock level
 * is left. For an even amount of levels each level is part of exactly one pair.
 * For an odd amount the middle level is paired with itself. The highst lock
 * level may coexist with the lowest one (<code>0</code>) which by
 * definition means <code>NO LOCK</code>. This implies that you will have to
 * specify at least one other lock level and thus set <code>maxLockLevel</code>
 * to at least <code>1</code>.
 * </p>
 *
 * <p>
 * Although this may sound complicated, in practice this is quite simple. Let us
 * imagine you have three lock levels:
 * <ul>
 * <li><code>0</code>:<code>NO LOCK</code> (always needed by the
 * implementation of this lock)
 * <li><code>1</code>:<code>SHARED</code>
 * <li><code>2</code>:<code>EXCLUSIVE</code>
 * </ul>
 * Accordingly, you will have to set <code>maxLockLevel</code> to
 * <code>2</code>. Now, there are two pairs of levels
 * <ul>
 * <li><code>NO LOCK</code> with <code>EXCLUSIVE</code>
 * <li><code>SHARED</code> with <code>SHARED</code>
 * </ul>
 * This means when the current highest lock level is <code>NO LOCK</code>
 * everything less or equal to <code>EXCLUSIVE</code> is allowed - which means
 * every other lock level. On the other side <code>EXCLUSIVE</code> allows
 * exacly for <code>NO LOCK</code>- which means nothing else. In conclusion,
 * <code>SHARED</code> allows for <code>SHARED</code> or <code>NO
 * LOCK</code>,
 * but not for <code>EXCLUSIVE</code>. To make this very clear have a look at
 * this table, where <code>o</code> means compatible or can coexist and
 * <code>x</code> means incompatible or can not coexist:
 * </p>
 * <table><tbody>
 * <tr>
 * <td align="center"></td>
 * <td align="center">NO LOCK</td>
 * <td align="center">SHARED</td>
 * <td align="center">EXCLUSIVE</td>
 * </tr>
 * <tr>
 * <td align="center">NO LOCK</td>
 * <td align="center">o</td>
 * <td align="center">o</td>
 * <td align="center">o</td>
 * </tr>
 * <tr>
 * <td align="center">SHARED</td>
 * <td align="center">o</td>
 * <td align="center">o</td>
 * <td align="center">x</td>
 * </tr>
 * <tr>
 * <td align="center">EXCLUSIVE</td>
 * <td align="center" align="center">o</td>
 * <td align="center">x</td>
 * <td align="center">x</td>
 * </tr>
 * </tbody> </table>
 *
 * </p>
 * <p>
 * Additionally, there are preferences for specific locks you can pass to
 * {@link #acquire(Object, int, boolean, int, boolean, long)}.
 * This means whenever more thanone party
 * waits for a lock you can specify which one is to be preferred. This gives you
 * every freedom you might need to specifcy e.g.
 * <ul>
 * <li>priority to parties either applying for higher or lower lock levels
 * <li>priority not only to higher or lower locks, but to a specific level
 * <li>completely random preferences
 * </ul>
 * </p>
 *
 * @version $Id: GenericLock.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class GenericLock implements MultiLevelLock2
{

    protected Object resourceId;
    // XXX needs to be synchronized to allow for unsynchronized access for deadlock detection
    // in getConflictingOwners to avoid deadlocks between lock to acquire and lock to check for
    // deadlocks
    protected Map owners = Collections.synchronizedMap(new HashMap());
    // XXX needs to be synchronized to allow for unsynchronized access for deadlock detection
    // in getConflictingWaiters to avoid deadlocks between lock to acquire and lock to check for
    // deadlocks
    // Note: having this as a list allows for fair mechanisms in sub classes
    protected List waitingOwners = Collections.synchronizedList(new ArrayList());
    private int maxLockLevel;
    protected LoggerFacade logger;
    protected int waiters = 0;

    /**
     * Creates a new lock.
     *
     * @param resourceId identifier for the resource associated to this lock
     * @param maxLockLevel highest allowed lock level as described in class intro
     * @param logger generic logger used for all kind of debug logging
     */
    public GenericLock(Object resourceId, int maxLockLevel, LoggerFacade logger)
    {
        if (maxLockLevel < 1)
            throw new IllegalArgumentException(
                "The maximum lock level must be at least 1 (" + maxLockLevel + " was specified)");
        this.resourceId = resourceId;
        this.maxLockLevel = maxLockLevel;
        this.logger = logger;
    }

    public boolean equals(Object o)
    {
        if (o instanceof GenericLock)
        {
            return ((GenericLock)o).resourceId.equals(resourceId);
        }
        return false;
    }

    public int hashCode()
    {
        return resourceId.hashCode();
    }

    /**
     * @see MultiLevelLock2#test(Object, int, int)
     */
    public boolean test(Object ownerId, int targetLockLevel, int compatibility)
    {
        boolean success = tryLock(ownerId, targetLockLevel, compatibility, false, true);
        return success;
    }

    /**
     * @see MultiLevelLock2#has(Object, int)
     */
    public boolean has(Object ownerId, int lockLevel)
    {
        int level = getLockLevel(ownerId);
        return (lockLevel <= level);
    }

    /**
     * @see org.apache.commons.transaction.locking.MultiLevelLock#acquire(java.lang.Object,
     *      int, boolean, boolean, long)
     */
    public synchronized boolean acquire(Object ownerId, int targetLockLevel, boolean wait,
                                        boolean reentrant, long timeoutMSecs) throws InterruptedException
    {
        return acquire(ownerId, targetLockLevel, wait, reentrant ? COMPATIBILITY_REENTRANT
                       : COMPATIBILITY_NONE, timeoutMSecs);
    }

    /**
     * @see #acquire(Object, int, boolean, int, boolean, long)
     */
    public synchronized boolean acquire(Object ownerId, int targetLockLevel, boolean wait,
                                        int compatibility, long timeoutMSecs) throws InterruptedException
    {
        return acquire(ownerId, targetLockLevel, wait, compatibility, false, timeoutMSecs);
    }

    /**
     * Tries to blockingly acquire a lock which can be preferred.
     *
     * @see #acquire(Object, int, boolean, int, boolean, long)
     * @since 1.1
     */
    public synchronized boolean acquire(Object ownerId, int targetLockLevel, boolean preferred,
                                        long timeoutMSecs) throws InterruptedException
    {
        return acquire(ownerId, targetLockLevel, true, COMPATIBILITY_REENTRANT, preferred,
                       timeoutMSecs);
    }

    /**
     * @see org.apache.commons.transaction.locking.MultiLevelLock2#acquire(Object,
     *      int, boolean, int, boolean, long)
     * @since 1.1
     */
    public synchronized boolean acquire(
        Object ownerId,
        int targetLockLevel,
        boolean wait,
        int compatibility,
        boolean preferred,
        long timeoutMSecs)
    throws InterruptedException
    {

        if (logger.isFinerEnabled())
        {
            logger.logFiner(
                ownerId.toString()
                + " trying to acquire lock for "
                + resourceId.toString()
                + " at level "
                + targetLockLevel
                + " at "
                + System.currentTimeMillis());
        }

        if (tryLock(ownerId, targetLockLevel, compatibility, preferred))
        {

            if (logger.isFinerEnabled())
            {
                logger.logFiner(
                    ownerId.toString()
                    + " actually acquired lock for "
                    + resourceId.toString()
                    + " at "
                    + System.currentTimeMillis());
            }

            return true;
        }
        else
        {
            if (!wait)
            {
                return false;
            }
            else
            {
                long started = System.currentTimeMillis();
                for (long remaining = timeoutMSecs;
                        remaining > 0;
                        remaining = timeoutMSecs - (System.currentTimeMillis() - started))
                {

                    if (logger.isFinerEnabled())
                    {
                        logger.logFiner(
                            ownerId.toString()
                            + " waiting on "
                            + resourceId.toString()
                            + " for msecs "
                            + timeoutMSecs
                            + " at "
                            + System.currentTimeMillis());
                    }

                    LockOwner waitingOwner = new LockOwner(ownerId, targetLockLevel, compatibility,
                                                           preferred);
                    try
                    {
                        registerWaiter(waitingOwner);
                        if (preferred)
                        {
                            // while waiting we already make our claim we are next
                            LockOwner oldLock = null;
                            try
                            {
                                // we need to remember it to restore it after waiting
                                oldLock = (LockOwner) owners.get(ownerId);
                                // this creates a new owner, so we do not need to
                                // copy the old one
                                setLockLevel(ownerId, null, targetLockLevel, compatibility,
                                             preferred);

                                // finally wait
                                wait(remaining);

                            }
                            finally
                            {
                                // we need to restore the old lock in order not to
                                // interfere with the intention lock in the
                                // following check
                                // and not to have it in case of success either
                                // as there will be an ordinary lock then
                                if (oldLock != null)
                                {
                                    owners.put(ownerId, oldLock);
                                }
                                else
                                {
                                    owners.remove(ownerId);
                                }
                            }

                        }
                        else
                        {
                            wait(remaining);
                        }
                    }
                    finally
                    {
                        unregisterWaiter(waitingOwner);
                    }

                    if (tryLock(ownerId, targetLockLevel, compatibility, preferred))
                    {

                        if (logger.isFinerEnabled())
                        {
                            logger.logFiner(
                                ownerId.toString()
                                + " waiting on "
                                + resourceId.toString()
                                + " eventually got the lock at "
                                + System.currentTimeMillis());
                        }

                        return true;
                    }
                }
                return false;
            }
        }
    }

    protected void registerWaiter(LockOwner waitingOwner)
    {
        synchronized (waitingOwners)
        {
            unregisterWaiter(waitingOwner);
            waiters++;
            waitingOwners.add(waitingOwner);
        }
    }

    protected void unregisterWaiter(LockOwner waitingOwner)
    {
        synchronized (waitingOwners)
        {
            if (waitingOwners.remove(waitingOwner))
                waiters--;
        }
    }

    /**
     * @see org.apache.commons.transaction.locking.MultiLevelLock#release(Object)
     */
    public synchronized boolean release(Object ownerId)
    {
        if (owners.remove(ownerId) != null)
        {
            if (logger.isFinerEnabled())
            {
                logger.logFiner(
                    ownerId.toString()
                    + " releasing lock for "
                    + resourceId.toString()
                    + " at "
                    + System.currentTimeMillis());
            }
            notifyAll();
            return true;
        }
        return false;
    }

    /**
     * @see org.apache.commons.transaction.locking.MultiLevelLock#getLockLevel(Object)
     */
    public int getLockLevel(Object ownerId)
    {
        LockOwner owner = (LockOwner) owners.get(ownerId);
        if (owner == null)
        {
            return 0;
        }
        else
        {
            return owner.lockLevel;
        }
    }

    /**
     * Gets the resource assotiated to this lock.
     *
     * @return identifier for the resource associated to this lock
     */
    public Object getResourceId()
    {
        return resourceId;
    }

    /**
     * Gets the lowest lock level possible.
     *
     * @return minimum lock level
     */
    public int getLevelMinLock()
    {
        return 0;
    }

    /**
     * Gets the highst lock level possible.
     *
     * @return maximum lock level
     */
    public int getLevelMaxLock()
    {
        return maxLockLevel;
    }

    public Object getOwner()
    {
        LockOwner owner = getMaxLevelOwner();
        if (owner == null)
            return null;
        return owner.ownerId;
    }

    public synchronized String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append(resourceId.toString()).append(":\n");

        for (Iterator it = owners.values().iterator(); it.hasNext();)
        {
            LockOwner owner = (LockOwner) it.next();
            buf.append("- ").append(owner.toString()).append("\n");
        }

        if (waiters != 0)
        {
            buf.append(waiters).append(" waiting:\n");
            for (Iterator it = waitingOwners.iterator(); it.hasNext();)
            {
                LockOwner owner = (LockOwner) it.next();
                buf.append("- ").append(owner.toString()).append("\n");
            }
        }

        return buf.toString();
    }

    protected synchronized LockOwner getMaxLevelOwner()
    {
        return getMaxLevelOwner(null, -1, false);
    }

    protected synchronized LockOwner getMaxLevelOwner(LockOwner reentrantOwner, boolean preferred)
    {
        return getMaxLevelOwner(reentrantOwner, -1, preferred);
    }

    protected synchronized LockOwner getMaxLevelOwner(int supportLockLevel, boolean preferred)
    {
        return getMaxLevelOwner(null, supportLockLevel, preferred);
    }

    protected synchronized LockOwner getMaxLevelOwner(LockOwner reentrantOwner,
            int supportLockLevel, boolean preferred)
    {
        LockOwner maxOwner = null;
        for (Iterator it = owners.values().iterator(); it.hasNext();)
        {
            LockOwner owner = (LockOwner) it.next();
            if (owner.lockLevel != supportLockLevel && !owner.equals(reentrantOwner)
                    && (maxOwner == null || maxOwner.lockLevel < owner.lockLevel)
                    // if we are a preferred lock we must not interfere with other intention
                    // locks as we otherwise might mututally lock without resolvation
                    && !(preferred && owner.intention))
            {
                maxOwner = owner;
            }
        }
        return maxOwner;
    }

    protected synchronized void setLockLevel(Object ownerId, LockOwner lock, int targetLockLevel,
            int compatibility, boolean intention)
    {
        // be sure there exists at most one lock per owner
        if (lock != null)
        {
            if (logger.isFinestEnabled())
            {
                logger.logFinest(
                    ownerId.toString()
                    + " upgrading lock for "
                    + resourceId.toString()
                    + " to level "
                    + targetLockLevel
                    + " at "
                    + System.currentTimeMillis());
            }
        }
        else
        {
            if (logger.isFinestEnabled())
            {
                logger.logFinest(
                    ownerId.toString()
                    + " getting new lock for "
                    + resourceId.toString()
                    + " at level "
                    + targetLockLevel
                    + " at "
                    + System.currentTimeMillis());
            }
        }
        owners.put(ownerId, new LockOwner(ownerId, targetLockLevel, compatibility, intention));
    }

    protected boolean tryLock(Object ownerId, int targetLockLevel, int compatibility,
                              boolean preferred)
    {
        return tryLock(ownerId, targetLockLevel, compatibility, preferred, false);
    }

    protected synchronized boolean tryLock(Object ownerId, int targetLockLevel, int compatibility,
                                           boolean preferred, boolean tryOnly)
    {

        LockOwner myLock = (LockOwner) owners.get(ownerId);

        // determine highest owner
        LockOwner highestOwner;
        if (compatibility == COMPATIBILITY_REENTRANT)
        {
            if (myLock != null && targetLockLevel <= myLock.lockLevel)
            {
                // we already have it
                return true;
            }
            else
            {
                // our own lock will not be compromised by ourself
                highestOwner = getMaxLevelOwner(myLock, preferred);
            }
        }
        else if (compatibility == COMPATIBILITY_SUPPORT)
        {
            // we are compatible with any other lock owner holding
            // the same lock level
            highestOwner = getMaxLevelOwner(targetLockLevel, preferred);

        }
        else if (compatibility == COMPATIBILITY_REENTRANT_AND_SUPPORT)
        {
            if (myLock != null && targetLockLevel <= myLock.lockLevel)
            {
                // we already have it
                return true;
            }
            else
            {
                // our own lock will not be compromised by ourself and same lock level
                highestOwner = getMaxLevelOwner(myLock, targetLockLevel, preferred);
            }
        }
        else
        {
            highestOwner = getMaxLevelOwner();
        }

        int i;
        // what is our current lock level?
        int currentLockLevel;
        if (highestOwner != null)
        {
            currentLockLevel = highestOwner.lockLevel;
        }
        else
        {
            currentLockLevel = getLevelMinLock();
        }

        // we are only allowed to acquire our locks if we do not compromise locks of any other lock owner
        if (isCompatible(targetLockLevel, currentLockLevel))
        {
            if (!tryOnly)
            {
                // if we really have the lock, it no longer is an intention
                setLockLevel(ownerId, myLock, targetLockLevel, compatibility, false);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    protected boolean isCompatible(int targetLockLevel, int currentLockLevel)
    {
        return (targetLockLevel <= getLevelMaxLock() - currentLockLevel);
    }

    protected Set getConflictingOwners(Object ownerId, int targetLockLevel, int compatibility)
    {

        LockOwner myLock = (LockOwner) owners.get(ownerId);
        if (myLock != null && targetLockLevel <= myLock.lockLevel)
        {
            // shortcut as we already have the lock
            return null;
        }

        LockOwner testLock = new LockOwner(ownerId, targetLockLevel, compatibility, false);
        List ownersCopy;
        synchronized (owners)
        {
            ownersCopy = new ArrayList(owners.values());
        }
        return getConflictingOwners(testLock, ownersCopy);

    }

    protected Collection getConflictingWaiters(Object ownerId)
    {
        LockOwner owner = (LockOwner) owners.get(ownerId);
        if (owner != null)
        {
            List waiterCopy;
            synchronized (waitingOwners)
            {
                waiterCopy = new ArrayList(waitingOwners);
            }
            Collection conflicts = getConflictingOwners(owner, waiterCopy);
            return conflicts;
        }
        return null;
    }

    protected Set getConflictingOwners(LockOwner myOwner, Collection ownersToTest)
    {

        if (myOwner == null) return null;

        Set conflicts = new HashSet();

        // check if any locks conflict with ours
        for (Iterator it = ownersToTest.iterator(); it.hasNext();)
        {
            LockOwner owner = (LockOwner) it.next();

            // we do not interfere with ourselves, except when explicitely said so
            if ((myOwner.compatibility == COMPATIBILITY_REENTRANT || myOwner.compatibility == COMPATIBILITY_REENTRANT_AND_SUPPORT)
                    && owner.ownerId.equals(myOwner.ownerId))
                continue;

            // otherwise find out the lock level of the owner and see if we conflict with it
            int onwerLockLevel = owner.lockLevel;

            if (myOwner.compatibility == COMPATIBILITY_SUPPORT
                    || myOwner.compatibility == COMPATIBILITY_REENTRANT_AND_SUPPORT
                    && myOwner.lockLevel == onwerLockLevel)
                continue;

            if (!isCompatible(myOwner.lockLevel, onwerLockLevel))
            {
                conflicts.add(owner.ownerId);
            }
        }
        return (conflicts.isEmpty() ? null : conflicts);
    }

    protected static class LockOwner
    {
        public final Object ownerId;
        public final int lockLevel;
        public final boolean intention;
        public final int compatibility;

        public LockOwner(Object ownerId, int lockLevel, int compatibility, boolean intention)
        {
            this.ownerId = ownerId;
            this.lockLevel = lockLevel;
            this.intention = intention;
            this.compatibility = compatibility;
        }

        public String toString()
        {
            StringBuffer buf = new StringBuffer();
            buf.append(ownerId.toString()).append(": level ").append(lockLevel).append(", complevel ")
            .append(compatibility).append(intention ? ", intention/preferred" : "");
            return buf.toString();
        }

        public boolean equals(Object o)
        {
            if (o instanceof LockOwner)
            {
                return ((LockOwner)o).ownerId.equals(ownerId);
            }
            return false;
        }

        public int hashCode()
        {
            return ownerId.hashCode();
        }
    }

}
