/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.discovery;

/**
 * An exception that is thrown only if a suitable service
 * instance cannot be created by {@code ServiceFactory}.
 *
 * @version $Revision: 1088947 $ $Date: 2011-04-05 11:51:19 +0200 (Tue, 05 Apr 2011) $
 */
public class DiscoveryException extends RuntimeException
{

    /**
     *
     */
    private static final long serialVersionUID = -2518293836976054070L;

    /**
     * Construct a new exception with <code>null</code> as its detail message.
     */
    public DiscoveryException()
    {
        super();
    }

    /**
     * Construct a new exception with the specified detail message.
     *
     * @param message The detail message
     */
    public DiscoveryException(String message)
    {
        super(message);
    }

    /**
     * Construct a new exception with the specified cause and a derived
     * detail message.
     *
     * @param cause The underlying cause
     */
    public DiscoveryException(Throwable cause)
    {
        super(cause);
    }

    /**
     * Construct a new exception with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The underlying cause
     */
    public DiscoveryException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
