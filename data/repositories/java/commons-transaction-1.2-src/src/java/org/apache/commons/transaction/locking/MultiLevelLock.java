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
 * A multi level lock. Depending on the implementation more than one owner may own a certain lock level on the same lock.
 *
 * @version $Id: MultiLevelLock.java 493628 2007-01-07 01:42:48Z joerg $
 * @see LockManager
 */
public interface MultiLevelLock
{

    /**
     * Tries to acquire a certain lock level on this lock.
     *
     * @param ownerId a unique id identifying the entity that wants to acquire a certain lock level on this lock
     * @param targetLockLevel the lock level to acquire
     * @param wait <code>true</code> if this method shall block when the desired lock level can not be acquired
     * @param reentrant <code>true</code> if lock levels of the same entity acquired earlier
     * should not restrict compatibility with the lock level desired now
     * @param timeoutMSecs if blocking is enabled by the <code>wait</code> parameter this specifies the maximum wait time in milliseconds
     * @return <code>true</code> if the lock actually was acquired
     * @throws InterruptedException when the thread waiting on this method is interrupted
     */
    public boolean acquire(Object ownerId, int targetLockLevel, boolean wait, boolean reentrant, long timeoutMSecs)
    throws InterruptedException;

    /**
     * Releases any lock levels the specified owner may hold on this lock.
     *
     * @param ownerId a unique id identifying the entity that wants to release all lock levels
     * @return <code>true</code> if the lock actually was released, <code>false</code> in case
     * there was no lock held by the owner
     */
    public boolean release(Object ownerId);

    /**
     * Retuns the highest lock level the specified owner holds on this lock or <code>0</code> if it holds no locks at all.
     *
     * @param ownerId a unique id identifying the entity that wants to know its highest lock level
     * @return the highest lock level
     */
    public int getLockLevel(Object ownerId);
}
