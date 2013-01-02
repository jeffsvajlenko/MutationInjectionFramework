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
package org.apache.commons.transaction.util.xa;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

/**
 * Interface for something that makes up a transactional resource.
 *
 * @version $Id: TransactionalResource.java 493628 2007-01-07 01:42:48Z joerg $
 */
public interface TransactionalResource
{

    /**
     * Commits the changes done inside this transaction reasource. This can mean
     * to call commit on a connection associated to the resource or any other
     * action that needs to be taken to make changes in this resource permanent.
     *
     * @throws XAException
     *             when anything goes wrong the error must be described in XA
     *             notation
     */
    public void commit() throws XAException;

    /**
     * Prepares the changes done inside this transaction reasource. Same
     * semantics as {@link javax.transaction.xa.XAResource#prepare(Xid)}.
     *
     * @throws XAException
     *             when anything goes wrong the error must be described in XA
     *             notation
     */
    public int prepare() throws XAException;

    /**
     * Rolls back the changes done inside this transaction reasource. This can mean
     * to call roll back on a connection associated to the resource or any other
     * action that needs to be taken to undo the changes in this resource permanent.
     *
     * @throws XAException
     *             when anything goes wrong the error must be described in XA
     *             notation
     */
    public void rollback() throws XAException;

    public void begin() throws XAException;
    public void suspend() throws XAException;
    public void resume() throws XAException;

    /**
     * Returns the current status of this transaction resource.
     *
     * @return the current status of this resource as defined by {@link javax.transaction.Status}.
     */
    public int getStatus();

    /**
     * Sets the status of this transctional resource. The status set by this method
     * must be available over {@link #getStatus()} afterwards.
     * @param status the status to be set
     */
    public void setStatus(int status);

    /**
     * Returns the Xid this transctional resource is associated with. This might have been set in
     * the constructor of implementing classes.
     *
     * @return the xid this transctional resource is associated with
     */
    public Xid getXid();
}
