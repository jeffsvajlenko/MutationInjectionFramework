package org.apache.commons.betwixt.scarab;

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

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><code>CreatedDate</code> is a sample bean for use by the test cases.</p>
 *
 * @author <a href="mailto:jon@latchkey.com">Jon Scott Stevens</a>
 * @version $Id: CreatedDate.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class CreatedDate implements Serializable
{
    /**
     * Logger
     */
    private final static Log log = LogFactory.getLog(CreatedDate.class);

    private String format;
    private String timestamp;

    public void setFormat(String format)
    {
        this.format = format;
    }

    public String getFormat()
    {
        return this.format;
    }

    public void setTimestamp(String timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getTimestamp()
    {
        return this.timestamp;
    }

    public String toString()
    {
        return "format=" + format + ";timestamp=" + timestamp;
    }
}
