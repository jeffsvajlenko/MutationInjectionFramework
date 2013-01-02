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
package org.apache.commons.betwixt.io;

/**
  * <p>Thrown when bean evaluation finds a cycle reference.</p>
  *
  * <p>There are two possible behaviours that <code>Betwixt</code> adopts when
  * a cycle in the object graph is encountered.
  *
  * <p>If <code>ID</code> attributes are being generated,
  * then the recursion will stop and the <code>IDREF</code> attribute will be
  * written.
  * In this case, <em>no exception will be thrown</em>.</p>
  *
  * <p>If <code>ID</code> are <strong>not</strong> being generated,
  * then this exception will be thrown.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public class CyclicReferenceException extends RuntimeException
{

    /** Message used with empty constructor */
    private static final String DEFAULT_MESSAGE
        = "Bean graph contains a cyclic reference";

    /** Construct exception with default message.
      */
    public CyclicReferenceException()
    {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Construct exception with message
     *
     * @param message the detailed message string
     */
    public CyclicReferenceException(String message)
    {
        super(message);
    }
}
