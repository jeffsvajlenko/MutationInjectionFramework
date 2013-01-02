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
 * Manager for
 * {@link org.apache.commons.transaction.locking.ReadWriteUpgradeLock}s on
 * resources. <br>
 * <br>
 * <p>
 * The idea (as explained by Jim LoVerde) is that only one owner can hold an
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
 * @version $Id: ReadWriteUpgradeLockManager.java 493628 2007-01-07 01:42:48Z joerg $
 *
 * @see ReadWriteUpgradeLock
 * @since 1.1
 */
public class ReadWriteUpgradeLockManager extends ReadWriteLockManager
{

    /**
     * Creates a new read/write/upgrade lock manager.
     *
     * @param logger generic logger used for all kind of debug logging
     * @param timeoutMSecs specifies the maximum time to wait for a lock in milliseconds
     */
    public ReadWriteUpgradeLockManager(LoggerFacade logger, long timeoutMSecs)
    {
        super(ReadWriteUpgradeLock.WRITE_LOCK, logger, timeoutMSecs);
    }

    /**
     * Tries to acquire a reentrant upgrade lock on a resource. <br>
     * <br>
     * This method does not block, but immediatly returns. If a lock is not
     * available <code>false</code> will be returned.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the lock for
     * @return <code>true</code> if the lock has been acquired, <code>false</code> otherwise
     */
    public boolean tryUpgradeLock(Object ownerId, Object resourceId)
    {
        return tryLock(ownerId, resourceId, ReadWriteUpgradeLock.UPGRADE_LOCK, true);
    }

    /**
     * Tries to acquire an exclusive, reentrant write lock on a resource. <br>
     * <br>
     * This method does not block, but immediatly returns. If a lock is not
     * available <code>false</code> will be returned.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the lock for
     * @return <code>true</code> if the lock has been acquired, <code>false</code> otherwise
     */
    public boolean tryWriteLock(Object ownerId, Object resourceId)
    {
        return tryLock(ownerId, resourceId, ReadWriteUpgradeLock.WRITE_LOCK, true);
    }

    /**
     * Tries to acquire a reentrant upgrade lock on a resource. <br>
     * <br>
     * This method blocks and waits for the lock in case it is not avaiable. If
     * there is a timeout or a deadlock or the thread is interrupted a
     * LockException is thrown.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the level for
     * @throws LockException
     *             will be thrown when the lock can not be acquired
     */
    public void upgradeLock(Object ownerId, Object resourceId) throws LockException
    {
        super.lock(ownerId, resourceId, ReadWriteUpgradeLock.UPGRADE_LOCK, true);
    }

    /**
     * Tries to acquire an exclusive, reentrant write lock on a resource. <br>
     * <br>
     * This method blocks and waits for the lock in case it is not avaiable. If
     * there is a timeout or a deadlock or the thread is interrupted a
     * LockException is thrown.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the level for
     * @throws LockException
     *             will be thrown when the lock can not be acquired
     */
    public void writeLock(Object ownerId, Object resourceId) throws LockException
    {
        super.lock(ownerId, resourceId, ReadWriteUpgradeLock.WRITE_LOCK, true);
    }

    protected GenericLock createLock(Object resourceId)
    {
        synchronized (globalLocks)
        {
            GenericLock lock = new ReadWriteUpgradeLock(resourceId, logger);
            globalLocks.put(resourceId, lock);
            return lock;
        }
    }

}
