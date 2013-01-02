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

/**
 *
 * Extended multi level lock. Compared to basic {@link MultiLevelLock} allows for more flexible
 * locking including preference and more compatibility modes.
 *
 * @version $Id: MultiLevelLock2.java 493628 2007-01-07 01:42:48Z joerg $
 * @see LockManager2
 * @see MultiLevelLock
 * @see GenericLock
 * @since 1.1
 */
public interface MultiLevelLock2 extends MultiLevelLock
{

    /**
     * Compatibility mode: none reentrant. Lock level by the same owner <em>shall</em>
     * affect compatibility.
     */
    public static final int COMPATIBILITY_NONE = 0;

    /**
     * Compatibility mode: reentrant. Lock level by the same owner <em>shall not</em>
     * affect compatibility.
     */
    public static final int COMPATIBILITY_REENTRANT = 1;

    /**
     * Compatibility mode: supporting. Lock levels that are the same as the
     * desired <em>shall not</em> affect compatibility, but lock level held by the same
     * owner <em>shall</em>.
     */
    public static final int COMPATIBILITY_SUPPORT = 2;

    /**
     * Compatibility mode: reentrant and supporting. Lock levels that are the same as the
     * desired and lock levels held by the same
     * owner <em>shall not</em> affect compatibility.
     */
    public static final int COMPATIBILITY_REENTRANT_AND_SUPPORT = 3;

    /**
     * Tests if a certain lock level is owned by an owner.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to check a
     *            certain lock level on this lock
     * @param lockLevel
     *            the lock level to test
     * @return <code>true</code> if the lock could be acquired at the time
     *         this method was called
     */
    public boolean has(Object ownerId, int lockLevel);

    /**
     * Tests if a certain lock level <em>could</em> be acquired. This method
     * tests only and does <em>not actually acquire</em> the lock.
     *
     * @param ownerId
     *            a unique id identifying the entity that wants to test a
     *            certain lock level on this lock
     * @param targetLockLevel
     *            the lock level to acquire
     * @param compatibility
     *            {@link #COMPATIBILITY_NONE}if no additional compatibility is
     *            desired (same as reentrant set to false) ,
     *            {@link #COMPATIBILITY_REENTRANT}if lock level by the same
     *            owner shall not affect compatibility (same as reentrant set to
     *            true), or {@link #COMPATIBILITY_SUPPORT}if lock levels that
     *            are the same as the desired shall not affect compatibility, or
     *            finally {@link #COMPATIBILITY_REENTRANT_AND_SUPPORT}which is
     *            a combination of reentrant and support
     * @return <code>true</code> if the lock could be acquired at the time
     *         this method was called
     */
    public boolean test(Object ownerId, int targetLockLevel, int compatibility);

    /**
     * Tries to acquire a certain lock level on this lock. Does the same as
     * {@link org.apache.commons.transaction.locking.MultiLevelLock#acquire(java.lang.Object, int, boolean, boolean, long)}
     * except that it allows for different compatibility settings. There is an
     * additional compatibility mode {@link #COMPATIBILITY_SUPPORT}that allows
     * equal lock levels not to interfere with each other. This is like an
     * additional shared compatibility and useful when you only want to make
     * sure not to interfer with lowe levels, but are fine with the same.
     *
     * @param ownerId a unique id identifying the entity that wants to acquire a certain lock level on this lock
     * @param targetLockLevel the lock level to acquire
     * @param wait <code>true</code> if this method shall block when the desired lock level can not be acquired
     * @param compatibility
     *            {@link #COMPATIBILITY_NONE}if no additional compatibility is
     *            desired (same as reentrant set to false) ,
     *            {@link #COMPATIBILITY_REENTRANT}if lock level by the same
     *            owner shall not affect compatibility (same as reentrant set to
     *            true), or {@link #COMPATIBILITY_SUPPORT}if lock levels that
     *            are the same as the desired shall not affect compatibility, or
     *            finally {@link #COMPATIBILITY_REENTRANT_AND_SUPPORT}which is
     *            a combination of reentrant and support
     *
     * @param preferred
     *            in case this lock request is incompatible with existing ones
     *            and we wait, it shall be granted before other waiting requests
     *            that are not preferred
     * @param timeoutMSecs if blocking is enabled by the <code>wait</code> parameter this specifies the maximum wait time in milliseconds
     * @return <code>true</code> if the lock actually was acquired
     * @throws InterruptedException when the thread waiting on this method is interrupted
     *
     */
    public boolean acquire(Object ownerId, int targetLockLevel, boolean wait, int compatibility,
                           boolean preferred, long timeoutMSecs) throws InterruptedException;

}
