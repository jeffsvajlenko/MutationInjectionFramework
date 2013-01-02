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

import javax.transaction.Status;
import javax.transaction.xa.Xid;

/**
 * Rudimentary abstract implementation of {@link TransactionalResource} for specific implementations to base upon.
 *
 * @version $Id: AbstractTransactionalResource.java 493628 2007-01-07 01:42:48Z joerg $
 */
public abstract class AbstractTransactionalResource implements TransactionalResource, Status
{
    protected Xid xid;

    protected int status;

    public AbstractTransactionalResource(Xid xid)
    {
        this.xid = xid;
        status = STATUS_ACTIVE;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Xid getXid()
    {
        return xid;
    }
}
