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
 * Interface representing a mapping
 * from a set of source resource names
 * to a resultant set of resource names.
 */
public interface ResourceNameDiscover
{

    /**
     * Locate names of resources that are bound to {@code resourceName}.
     *
     * @param resourceName The resource has to be located
     * @return The bound resources name iterator
     */
    ResourceNameIterator findResourceNames(String resourceName);

    /**
     * Locate names of resources that are bound to {@code resourceNames}.
     *
     * @param resourceNames The resources has to be located
     * @return The bound resources name iterator
     */
    ResourceNameIterator findResourceNames(ResourceNameIterator resourceNames);

}
