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

import java.util.Set;

/**
 * Extended version of a lock manager that also has global knowledge or all locks and should be
 * used as a delegate for all locking requests. This allows for things like deadlock detection.
 *
 * @version $Id: LockManager2.java 493628 2007-01-07 01:42:48Z joerg $
 * @see MultiLevelLock
 * @see MultiLevelLock2
 * @see LockManager
 * @see GenericLockManager
 * @see GenericLock
 * @since 1.1
 */
public interface LockManager2
{

    /**
     * Determines if a lock is owner by an owner. <br>
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to check this
     *            lock
     * @param resourceId
     *            the resource to get the level for
     * @param lockLevel
     *            the lock level to check
     * @return <code>true</code> if the owner has the lock, <code>false</code> otherwise
     *
     */
    public boolean hasLock(Object ownerId, Object resourceId, int lockLevel);

    /**
     * Determines if a lock <em>could</em> be acquire <em>without</em> actually acquiring it. <br>
     * <br>
     * This method does not block, but immediatly returns.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to check this
     *            lock
     * @param resourceId
     *            the resource to get the level for
     * @param targetLockLevel
     *            the lock level to check
     * @param reentrant
     *            <code>true</code> if this request shall not be influenced by
     *            other locks held by the same owner
     * @return <code>true</code> if the lock could be acquired, <code>false</code> otherwise
     *
     */
    public boolean checkLock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant);

    /**
     * Tries to acquire a lock on a resource. <br>
     * <br>
     * This method does not block, but immediatly returns. If a lock is not
     * available <code>false</code> will be returned.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param resourceId
     *            the resource to get the level for
     * @param targetLockLevel
     *            the lock level to acquire
     * @param reentrant
     *            <code>true</code> if this request shall not be influenced by
     *            other locks held by the same owner
     * @return <code>true</code> if the lock has been acquired, <code>false</code> otherwise
     *
     */
    public boolean tryLock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant);

    /**
     * Tries to acquire a lock on a resource. <br>
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
     * @param targetLockLevel
     *            the lock level to acquire
     * @param reentrant
     *            <code>true</code> if this request shall not be blocked by
     *            other locks held by the same owner
     * @throws LockException
     *             will be thrown when the lock can not be acquired
     */
    public void lock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant)
    throws LockException;

    /**
     * Tries to acquire a lock on a resource. <br>
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
     * @param targetLockLevel
     *            the lock level to acquire
     * @param reentrant
     *            <code>true</code> if this request shall not be blocked by
     *            other locks held by the same owner
     * @param timeoutMSecs
     *            specifies the maximum wait time in milliseconds
     * @throws LockException
     *             will be thrown when the lock can not be acquired
     */
    public void lock(Object ownerId, Object resourceId, int targetLockLevel, boolean reentrant,
                     long timeoutMSecs) throws LockException;

    /**
     * Most flexible way to acquire a lock on a resource. <br>
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
     * @param targetLockLevel
     *            the lock level to acquire
     * @param compatibility
     *            {@link GenericLock#COMPATIBILITY_NONE}if no additional compatibility is
     *            desired (same as reentrant set to false) ,
     *            {@link GenericLock#COMPATIBILITY_REENTRANT}if lock level by the same
     *            owner shall not affect compatibility (same as reentrant set to
     *            true), or {@link GenericLock#COMPATIBILITY_SUPPORT}if lock levels that
     *            are the same as the desired shall not affect compatibility, or
     *            finally {@link GenericLock#COMPATIBILITY_REENTRANT_AND_SUPPORT}which is
     *            a combination of reentrant and support
     * @param preferred
     *            in case this lock request is incompatible with existing ones
     *            and we wait, it shall be granted before other waiting requests
     *            that are not preferred
     * @param timeoutMSecs
     *            specifies the maximum wait time in milliseconds
     * @throws LockException
     *             will be thrown when the lock can not be acquired
     */
    public void lock(Object ownerId, Object resourceId, int targetLockLevel, int compatibility,
                     boolean preferred, long timeoutMSecs) throws LockException;

    /**
     * Starts a global timeout for an owner. This is especially usefull, when the owner is a
     * transaction. After a global timeout occurs all of the owner's lock will be released and
     * the owner will not be allowed to access any
     * locks before before calling {@link #releaseAll(Object)}.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to acquire this
     *            lock
     * @param timeoutMSecs
     *            specifies the global timeout in milliseconds
     */
    public void startGlobalTimeout(Object ownerId, long timeoutMSecs);

    /**
     * Gets the lock level held by certain owner on a certain resource.
     *
     * @param ownerId the id of the owner of the lock
     * @param resourceId the resource to get the level for
     */
    public int getLevel(Object ownerId, Object resourceId);

    /**
     * Releases all locks for a certain resource held by a certain owner.
     *
     * @param ownerId the id of the owner of the lock
     * @param resourceId the resource to releases the lock for
     * @return <code>true</code> if the lock actually was released, <code>false</code> in case
     * there was no lock held by the owner
     */
    public boolean release(Object ownerId, Object resourceId);

    /**
     * Releases all locks (partially) held by an owner.
     *
     * @param ownerId the id of the owner
     */
    public void releaseAll(Object ownerId);

    /**
     * Gets all locks (partially) held by an owner.
     *
     * @param ownerId the id of the owner
     * @return all locks held by ownerId
     */
    public Set getAll(Object ownerId);


    /**
     * Gets an existing lock on the specified resource. If none exists it returns <code>null</code>.
     *
     * @param resourceId the resource to get the lock for
     * @return the lock on the specified resource
     *
     */
    public MultiLevelLock getLock(Object resourceId);

    /**
     * Removes the specified lock from the associated resource.
     *
     * <em>Caution:</em> This does not release the lock, but only moves it out
     * of the scope of this manager. Use {@link #release(Object, Object)} for that.
     *
     * @param lock the lock to be removed
     */
    public void removeLock(MultiLevelLock lock);

}
