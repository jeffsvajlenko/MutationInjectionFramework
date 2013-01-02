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
 * Iterate over resource names.
 * The semantics are somewhat unusual, for better or worse.
 * hasNext is presumed to be destructive to the current state,
 * each call will 'move' the cursor.
 * nextResourceName() MUST BE non-destructive,
 * it does not change the state.
 *
 * TODO: FIX iterator logic/semantics, possibly add 'currentResourceName()'.
 */
public interface ResourceNameIterator
{

    /**
     * Returns true if the iteration has more elements.
     *
     * @return true if the iterator has more elements, false otherwise
     */
    boolean hasNext();

    /**
     * nextResourceName() returns the name of the next resource,
     * and MUST be non-destructive.  Repeated calls
     *
     * @return The next resource name in the iteration
     */
    String nextResourceName();

}
