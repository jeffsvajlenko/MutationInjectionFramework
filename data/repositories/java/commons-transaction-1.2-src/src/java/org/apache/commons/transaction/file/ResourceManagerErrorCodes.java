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
package org.apache.commons.transaction.file;

/**
 * Error codes for resource managers.
 *
 * @version $Id: ResourceManagerErrorCodes.java 493628 2007-01-07 01:42:48Z joerg $
 * @see ResourceManager
 * @see ResourceManagerException
 * @see ResourceManagerSystemException
 */
public interface ResourceManagerErrorCodes
{

    /**
     * Error code: unknown error
     */
    public static final int ERR_UNKNOWN = -1;

    /**
     * Error code: general system error
     */
    public static final int ERR_SYSTEM = 1;

    /**
     * Error code: global inconsistent data system error
     */
    public static final int ERR_SYSTEM_INCONSISTENT = ERR_SYSTEM + 1;

    /**
     * Error code: inconsistent transaction data system error
     */
    public static final int ERR_TX_INCONSISTENT = ERR_SYSTEM + 2;

    /**
     * Error code: no transaction error
     */
    public static final int ERR_NO_TX = 1000;

    /**
     * Error code: transaction identifier invalid error
     */
    public static final int ERR_TXID_INVALID = ERR_NO_TX + 1;

    /**
     * Error code: transaction inactive error
     */
    public static final int ERR_TX_INACTIVE = ERR_NO_TX + 2;

    /**
     * Error code: transaction identifier already exists error
     */
    public static final int ERR_DUP_TX = ERR_NO_TX + 4;

    /**
     * Error code: calling thread is not owner of transaction error (only in single thread implementations)
     */
    public static final int ERR_THREAD_INVALID = ERR_NO_TX + 5;

    /**
     * Error code: requested isolation level is not supported for this transaction error
     */
    public static final int ERR_ISOLATION_LEVEL_UNSUPPORTED = ERR_NO_TX + 6;

    /**
     * Error code: operation not possible as transaction is alredy marked for rollback error
     */
    public static final int ERR_MARKED_FOR_ROLLBACK = ERR_NO_TX + 7;

    /**
     * Error code: resource identifier invalid error
     */
    public static final int ERR_RESOURCEID_INVALID = 4000;

    /**
     * Error code: resource already exists error
     */
    public static final int ERR_RESOURCE_EXISTS = ERR_RESOURCEID_INVALID + 1;

    /**
     * Error code: resource does not exist error
     */
    public static final int ERR_NO_SUCH_RESOURCE = ERR_RESOURCEID_INVALID + 2;

    /**
     * Error code: general lock error
     */
    public static final int ERR_LOCK = 5000;

    /**
     * Error code: lock could not be acquired error
     */
    public static final int ERR_NO_LOCK = ERR_LOCK + 1;

    /**
     * Error code: lock could not be acquired error
     */
    public static final int ERR_DEAD_LOCK = ERR_LOCK + 2;


}
