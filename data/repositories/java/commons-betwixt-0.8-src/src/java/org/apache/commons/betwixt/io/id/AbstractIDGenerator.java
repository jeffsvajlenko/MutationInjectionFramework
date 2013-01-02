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

import org.apache.commons.betwixt.io.IDGenerator;

/** <p>Abstract superclass for {@link IDGenerator} implementations.</p>
  *
  * <p>It implements the entire <code>IDGenerator</code> interface.
  * When <code>nextId</code> is called,
  * this class sets the <code>LastId</code> property (as well
  * as returning the value).
  * Subclasses should override {@link #nextIdImpl}.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public abstract class AbstractIDGenerator implements IDGenerator
{

    /** Last <code>ID</code> returned */
    private String lastId = "0";

    /**
     * Gets last <code>ID</code> returned.
     *
     * @return the last id created by the generated
     */
    public final String getLastId()
    {
        return lastId;
    }

    /**
      * <p>Generate next <code>ID</code>.</p>
      *
      * <p>This method obtains the next <code>ID</code> from subclass
      * and then uses this to set the <code>LastId</code> property.</p>
      *
      * @return the next id generated
      */
    public final String nextId()
    {
        lastId = nextIdImpl();
        return lastId;
    }

    /**
      * Subclasses should <strong>provide an implementation</strong> for this method.
      * This implementation needs only provide the next <code>ID</code>
      * value (according to it's algorithm).
      * Setting the <code>LastId</code> property can be left to this class.
      *
      * @return the next id generated
      */
    protected abstract String nextIdImpl();
}
