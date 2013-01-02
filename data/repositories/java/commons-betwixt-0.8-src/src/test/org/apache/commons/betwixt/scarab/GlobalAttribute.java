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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><code>GlobalAttribute</code> is a sample bean for use by the test cases.</p>
 *
 * @author <a href="mailto:jason@zenplex.com">Jason van Zyl</a>
 * @version $Id: GlobalAttribute.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class GlobalAttribute implements Serializable
{
    /**
     * Logger
     */
    private final static Log log = LogFactory.getLog(GlobalAttribute.class);

    private List globalAttributeOptions;

    private String name;
    private CreatedDate createdDate;

    /**
     * Constructor for the ScarabSettings object
     */
    public GlobalAttribute()
    {
        globalAttributeOptions = new ArrayList();
    }

    public String toString()
    {
        return super.toString() + "[name=" + name + ";createdDate=" + createdDate + ";options=" + globalAttributeOptions + "]";
    }

    public void addGlobalAttributeOption(GlobalAttributeOption globalAttributeOption)
    {
        globalAttributeOptions.add(globalAttributeOption);
    }

    public List getGlobalAttributeOptions()
    {
        return globalAttributeOptions;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void addCreatedDate(CreatedDate cd)
    {
        this.createdDate = cd;
    }

    public CreatedDate getCreatedDate()
    {
        return this.createdDate;
    }
}
