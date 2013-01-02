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
 * Manager for {@link org.apache.commons.transaction.locking.ReadWriteLock}s on resources.
 *
 * @version $Id: ReadWriteLockManager.java 493628 2007-01-07 01:42:48Z joerg $
 * @since 1.1
 */
public class ReadWriteLockManager extends GenericLockManager
{

    /**
     * Creates a new read/write lock manager.
     *
     * @param logger generic logger used for all kind of debug logging
     * @param timeoutMSecs specifies the maximum time to wait for a lock in milliseconds
     */
    public ReadWriteLockManager(LoggerFacade logger, long timeoutMSecs)
    {
        super(ReadWriteLock.WRITE_LOCK, logger, timeoutMSecs);
    }

    protected ReadWriteLockManager(int maxLockLevel, LoggerFacade logger, long timeoutMSecs)
    throws IllegalArgumentException
    {
        super(maxLockLevel, logger, timeoutMSecs);
    }

    /**
     * Tries to acquire a shared, reentrant read lock on a resource. <br>
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
    public boolean tryReadLock(Object ownerId, Object resourceId)
    {
        return tryLock(ownerId, resourceId, ReadWriteLock.READ_LOCK, true);
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
        return tryLock(ownerId, resourceId, ReadWriteLock.WRITE_LOCK, true);
    }

    /**
     * Determines if a shared, reentrant read lock on a resource
     * <em>could</em> be acquired without actually acquiring it. <br>
     * <br>
     * This method does not block, but immediatly returns. If a lock is not
     * available <code>false</code> will be returned.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the lock for
     * @return <code>true</code> if the lock could be acquired, <code>false</code> otherwise
     */
    public boolean checkReadLock(Object ownerId, Object resourceId)
    {
        return checkLock(ownerId, resourceId, ReadWriteLock.READ_LOCK, true);
    }

    /**
     * Determines if an exclusive, reentrant write lock on a resource
     * is held by an owner. <br>
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to check this
     *            lock
     * @param resourceId
     *            the resource to get the lock for
     * @return <code>true</code> if the lock is held by the owner, <code>false</code> otherwise
     */
    public boolean hasWriteLock(Object ownerId, Object resourceId)
    {
        return hasLock(ownerId, resourceId, ReadWriteLock.WRITE_LOCK);
    }

    /**
     * Determines if a shared, reentrant read lock on a resource
     * is held by an owner. <br>
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to check this
     *            lock
     * @param resourceId
     *            the resource to get the lock for
     * @return <code>true</code> if the lock is held by the owner, <code>false</code> otherwise
     */
    public boolean hasReadLock(Object ownerId, Object resourceId)
    {
        return hasLock(ownerId, resourceId, ReadWriteLock.READ_LOCK);
    }

    /**
     * Determines if an exclusive, reentrant write lock on a resource
     * <em>could</em> be acquired without actually acquiring it. <br>
     * <br>
     * This method does not block, but immediatly returns. If a lock is not
     * available <code>false</code> will be returned.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the lock for
     * @return <code>true</code> if the lock could be acquired, <code>false</code> otherwise
     */
    public boolean checkWriteLock(Object ownerId, Object resourceId)
    {
        return checkLock(ownerId, resourceId, ReadWriteLock.WRITE_LOCK, true);
    }

    /**
     * Tries to acquire a shared, reentrant read lock on a resource. <br>
     * <br>
     * This method blocks and waits for the lock in case it is not avaiable. If
     * there is a timeout or a deadlock or the thread is interrupted a
     * LockException is thrown.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the lock for
     * @throws LockException
     *             will be thrown when the lock can not be acquired
     */
    public void readLock(Object ownerId, Object resourceId) throws LockException
    {
        lock(ownerId, resourceId, ReadWriteLock.READ_LOCK, GenericLock.COMPATIBILITY_REENTRANT,
             false, globalTimeoutMSecs);
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
     *            the resource to get the lock for
     * @throws LockException
     *             will be thrown when the lock can not be acquired
     */
    public void writeLock(Object ownerId, Object resourceId) throws LockException
    {
        lock(ownerId, resourceId, ReadWriteLock.WRITE_LOCK, GenericLock.COMPATIBILITY_REENTRANT,
             true, globalTimeoutMSecs);
    }

    protected GenericLock createLock(Object resourceId)
    {
        synchronized (globalLocks)
        {
            GenericLock lock = new ReadWriteLock(resourceId, logger);
            globalLocks.put(resourceId, lock);
            return lock;
        }
    }

}
