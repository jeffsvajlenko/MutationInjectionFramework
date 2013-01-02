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
package org.apache.commons.transaction.file;

/**
 * Maps transaction ids to paths and back.
 *
 * @version $Id: TransactionIdToPathMapper.java 493632 2007-01-07 01:57:31Z joerg $
 * @since 1.2
 */
public interface TransactionIdToPathMapper
{

    /**
     * Maps the transaction id object to a path string.
     *
     * @param txId the transaction id
     * @return the path string
     */
    String getPathForId(Object txId);

    /**
     * Maps the path string to a transaction id object.
     *
     * @param path the path
     * @return the path string
     */
    Object getIdForPath(String path);

}
