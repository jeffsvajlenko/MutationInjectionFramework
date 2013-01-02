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
package org.apache.commons.jxpath.ri.model;

import org.apache.commons.jxpath.TestBean;

/**
 *
 * @author <a href="mailto:dmitri@apache.org">Dmitri Plotnikov</a>
 * @version $Id: ExceptionPropertyTestBean.java 480417 2006-11-29 05:37:40Z bayard $
 */
public class ExceptionPropertyTestBean
{

    public String getErrorString()
    {
        throw new RuntimeException("errorString");
    }

    public String[] getErrorStringArray()
    {
        throw new RuntimeException("errorStringArray");
    }

    public TestBean getErrorBean()
    {
        throw new RuntimeException("errorBean");
    }
}
