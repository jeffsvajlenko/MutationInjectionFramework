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
package org.apache.commons.transaction.memory;


/**
 * Exception displaying a lock problem in pessimistic transactions.
 *
 * @version $Id: LockException.java 493628 2007-01-07 01:42:48Z joerg $
 * @see OptimisticMapWrapper
 * @see org.apache.commons.transaction.locking.LockException
 * @deprecated no longer used as it has been replaced by the more general
 * version in {@link org.apache.commons.transaction.locking.LockException}
 */
public class LockException extends RuntimeException /* FIXME Exception */
{

    public static final int CODE_INTERRUPTED = 1;

    public static final int CODE_TIMED_OUT = 2;

    public static final int CODE_DEADLOCK_VICTIM = 3;

    protected Object key;

    protected String reason;

    protected int code;

    public LockException(String reason, int code, Object key)
    {
        this.reason = reason;
        this.key = key;
        this.code = code;
    }
}
