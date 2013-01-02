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
package org.apache.commons.betwixt.io.id;


/** <p>Generates <code>ID</code>'s in numeric sequence.
  * A simple counter is used.
  * Every time that {@link #nextIdImpl} is called,
  * this counter is incremented.</p>
  *
  * <p>By default, the counter starts at zero.
  * A user can set the initial value by using the
  * {@link #SequentialIDGenerator(int start)} constructor.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public final class SequentialIDGenerator extends AbstractIDGenerator
{

    /** Counter used to assign <code>ID</code>'s */
    private int counter = 0;

    /**
      * Base constructor.
      * Counter starts at zero.
      */
    public SequentialIDGenerator() {}

    /**
     * Constructor sets the start value for the counter.
     *
     * <p><strong>Note</strong> since the counter increments
     * before returning the next value,
     * first <code>ID</code> generated will be <em>one more</em>
     * than the given <code>start</code> parameter.</p>
     *
     * @param start start the counting at this value
     */
    public SequentialIDGenerator(int start)
    {
        this.counter = start;
    }

    /**
      * Increment counter and then return value.
      *
      * @return one more than the current counter (converted to a string)
      */
    public String nextIdImpl()
    {
        return Integer.toString(++counter);
    }

    /**
      * Gets the current counter value
      *
      * @return the last ID in the sequence
      */
    public int getCount()
    {
        return counter;
    }
}
