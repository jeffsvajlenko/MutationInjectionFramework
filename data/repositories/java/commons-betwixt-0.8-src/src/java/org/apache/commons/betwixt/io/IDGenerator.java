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


/** <p>Interface allowing pluggable <code>ID</code> attribute value generators.</p>
  *
  * <p> <code>IDGenerator</code>'s are used to generate <code>ID</code>
  * attribute values by <code>BeanWriter</code>.
  * A user can specify the generation mechanism by passing an implementation to
  * {@link BeanWriter#setIdGenerator}.</p>
  *
  * <p>Standard implementations are included with that supply random and sequantial values.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public interface IDGenerator
{

    /**
      * Get the last <code>ID</code> value generated.
      *
      * @return the last value generated
      */
    public String getLastId();

    /**
      * Generate a new  <code>ID</code> attribute value.
      *
      * @return next value
      */
    public String nextId();
}
