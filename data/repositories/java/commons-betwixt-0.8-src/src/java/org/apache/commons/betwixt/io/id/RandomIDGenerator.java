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

import java.util.Random;

/** <p>Generates <code>ID</code>'s at random.
  * The random number source is <code>java.util.Random</code>.</p>
  *
  * <p>Random <code>ID</code>'s are very useful if you're inserting
  * elements created by <code>Betwixt</code> into a stream with existing
  * elements.
  * Using random <code>ID</code>'s should reduce the danger of collision
  * with existing element <code>ID</code>'s.</p>
  *
  * <p>This class can generate positive-only ids (the default)
  * or it can generate a mix of negative and postive ones.
  * This behaviour can be set by {@link #setPositiveIds}
  * or by using the {@link #RandomIDGenerator(boolean onlyPositiveIds)}
  * constructor.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public final class RandomIDGenerator extends AbstractIDGenerator
{

    /** Use simple java.util.Random as the source for our numbers */
    private Random random = new Random();
    /** Should only positive id's be generated? */
    private boolean onlyPositiveIds = true;

    /**
      * Constructor sets the <code>PositiveIds</code> property to <code>true</code>.
      */
    public RandomIDGenerator() {}

    /**
      * Constructor sets <code>PositiveIds</code> property.
      *
      * @param onlyPositiveIds set <code>PositiveIds</code> property to this value
      */
    public RandomIDGenerator(boolean onlyPositiveIds)
    {
        setPositiveIds(onlyPositiveIds);
    }

    /**
      * <p>Generates a random <code>ID</code>.</p>
      *
      * <p>If the <code>PositiveIds</code> property is true,
      * then this method will recursively call itself if the random
      * <code>ID</code> is less than zero.</p>
      *
      * @return a random integer (converted to a string)
      */
    public String nextIdImpl()
    {
        int next = random.nextInt();
        if (onlyPositiveIds && next<0)
        {
            // it's negative and we're ignoring them so get another
            return nextIdImpl();
        }
        return Integer.toString(next);
    }

    /**
     * Gets whether only positive <code>ID</code>'s should be generated
     *
     * @return whether only positive IDs should be generated
     */
    public boolean getPositiveIds()
    {
        return onlyPositiveIds;
    }

    /**
     * Sets whether only positive <code>ID</code>'s should be generated
     *
     * @param onlyPositiveIds pass true if only positive IDs should be generated
     */
    public void setPositiveIds(boolean onlyPositiveIds)
    {
        this.onlyPositiveIds = onlyPositiveIds;
    }
}
