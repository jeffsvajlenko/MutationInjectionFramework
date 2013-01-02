/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.transaction.file;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * URL encodes a resource id using JDK1.4 functionality.
 *
 * @version $Id: JDK14URLEncodeIdMapper.java 493628 2007-01-07 01:42:48Z joerg $
 * @since 1.1
 */
public class JDK14URLEncodeIdMapper implements ResourceIdToPathMapper
{
    public String getPathForId(Object resourceId)
    {
        String path = resourceId.toString();
        try
        {
            path = URLEncoder.encode(path, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            // we know this will not happen
        }
        return path;
    }
}
