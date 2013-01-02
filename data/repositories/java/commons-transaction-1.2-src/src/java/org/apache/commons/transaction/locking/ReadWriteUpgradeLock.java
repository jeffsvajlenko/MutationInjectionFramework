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

import org.apache.commons.transaction.util.LoggerFacade;

/**
 * Convenience implementation of a read/write lock with an option for upgrade
 * based on {@link ReadWriteUpgradeLock}.<br>
 * <br>
 * Reads are shared which means there can be any number of concurrent read
 * accesses allowed by this lock. Writes are exclusive. This means when there is
 * a write access no other access neither read nor write are allowed by this
 * lock. <br>
 * <br>
 * <p>
 * The idea (as explained by Jim LoVerde) on an upgrade lock is that only one owner can hold an
 * upgrade lock, but while that is held, it is possible for read locks to exist
 * and/or be obtained, and when the request is made to upgrade to a write lock
 * by the same owner, the lock manager prevents additional read locks until the
 * write lock can be aquired.
 * </p>
 * <p>
 * In this sense the write lock becomes preferred over all other locks when it gets upgraded from
 * a upgrate lock. Preferred means that if it has to wait and others wait as well it will be
 * served before all other none preferred locking requests.
 * </p>
 *
 * Calls to {@link #acquireRead(Object, long)}, {@link #acquireUpgrade(Object, long)} and
 * {@link #acquireWrite(Object, long)} are blocking and reentrant. Blocking
 * means they will wait if they can not acquire the descired access, reentrant
 * means that a lock request by a specific owner will always be compatible with
 * other accesses on this lock by the same owner. E.g. if you already have a
 * lock for writing and you try to acquire write access again you will not be
 * blocked by this first lock, while others of course will be. This is the
 * natural way you already know from Java monitors and synchronized blocks.
 *
 * @version $Id: ReadWriteUpgradeLock.java 493628 2007-01-07 01:42:48Z joerg $
 *
 * @see GenericLock
 * @see org.apache.commons.transaction.locking.ReadWriteLock
 * @see ReadWriteUpgradeLockManager
 * @since 1.1
 */
public class ReadWriteUpgradeLock extends GenericLock
{

    public static final int NO_LOCK = 0;

    public static final int READ_LOCK = 1;

    public static final int UPGRADE_LOCK = 2;

    public static final int WRITE_LOCK = 3;

    /**
     * Creates a new read/write/upgrade lock.
     *
     * @param resourceId
     *            identifier for the resource associated to this lock
     * @param logger
     *            generic logger used for all kind of debug logging
     */
    public ReadWriteUpgradeLock(Object resourceId, LoggerFacade logger)
    {
        super(resourceId, WRITE_LOCK, logger);
    }

    /**
     * Tries to acquire a blocking, reentrant read lock. A read lock is
     * compatible with other read locks, but not with a write lock.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire a
     *            certain lock level on this lock
     * @param timeoutMSecs
     *            if blocking is enabled by the <code>wait</code> parameter
     *            this specifies the maximum wait time in milliseconds
     * @return <code>true</code> if the lock actually was acquired
     * @throws InterruptedException
     *             when the thread waiting on this method is interrupted
     */
    public boolean acquireRead(Object ownerId, long timeoutMSecs) throws InterruptedException
    {
        return acquire(ownerId, READ_LOCK, true, true, timeoutMSecs);
    }

    /**
     * Tries to acquire a reentrant upgrade lock on a resource. <br>
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire a
     *            certain lock level on this lock
     * @param timeoutMSecs
     *            if blocking is enabled by the <code>wait</code> parameter
     *            this specifies the maximum wait time in milliseconds
     * @return <code>true</code> if the lock actually was acquired
     * @throws InterruptedException
     *             when the thread waiting on this method is interrupted
     */
    public boolean acquireUpgrade(Object ownerId, long timeoutMSecs) throws InterruptedException
    {
        return acquire(ownerId, UPGRADE_LOCK, true, true, timeoutMSecs);
    }

    /**
     * Tries to acquire a blocking, reentrant write lock. A write lock is
     * incompatible with any another read or write lock and is thus exclusive.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire a
     *            certain lock level on this lock
     * @param timeoutMSecs
     *            if blocking is enabled by the <code>wait</code> parameter
     *            this specifies the maximum wait time in milliseconds
     * @return <code>true</code> if the lock actually was acquired
     * @throws InterruptedException
     *             when the thread waiting on this method is interrupted
     */
    public boolean acquireWrite(Object ownerId, long timeoutMSecs) throws InterruptedException
    {
        // in case we already had an upgrade lock, this wait lock will become preferred
        boolean preferred = getLockLevel(ownerId) == UPGRADE_LOCK;
        return acquire(ownerId, WRITE_LOCK, true, COMPATIBILITY_REENTRANT, preferred, timeoutMSecs);
    }

    /**
     * @see GenericLock#acquire(Object, int, boolean, int, boolean, long)
     */
    public synchronized boolean acquire(Object ownerId, int targetLockLevel, boolean wait,
                                        int compatibility, boolean preferred, long timeoutMSecs) throws InterruptedException
    {
        if (targetLockLevel == WRITE_LOCK && getLockLevel(ownerId) == UPGRADE_LOCK)
        {
            preferred = true;
        }
        return super.acquire(ownerId, targetLockLevel, wait, compatibility, preferred, timeoutMSecs);
    }

}
