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

package org.apache.commons.proxy;

import java.lang.reflect.Method;

/**
 * Represents a method invocation.
 *
 * @author James Carman
 * @since 1.0
 */
public interface Invocation
{
    /**
     * Returns the method being called.
     * @return the method being called
     */
    public Method getMethod();

    /**
     * Returns the arguments being passed to this method invocation.  Changes in the elements of this array will be
     * propagated to the recipient of this invocation.
     *
     * @return the arguments being passed to this method invocation
     */
    public Object[] getArguments();

    /**
     * Returns the proxy object on which this invocation was invoked.
     * @return the proxy object on which this invocation was invoked
     */
    public Object getProxy();

    /**
     * Called in order to let the invocation proceed.
     * @return the return value of the invocation
     * @throws Throwable any exception or error that was thrown as a result of this invocation
     */
    public Object proceed() throws Throwable;
}
