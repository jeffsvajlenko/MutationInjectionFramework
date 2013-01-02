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

/**
 * <p><code>GlobalAttributeOption</code> is a sample bean for use by the test cases.</p>
 *
 * @author <a href="mailto:jason@zenplex.com">Jason van Zyl</a>
 * @version $Id: GlobalAttributeOption.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class GlobalAttributeOption implements Serializable
{
    private String name;
    private String preferredOrder;
    private String childOption;

    /**
     * Constructor for the ScarabSettings object
     */
    public GlobalAttributeOption()
    {
    }

    public String toString()
    {
        return super.toString() + "[name=" + name + ";childOption=" + childOption + "]";
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setChildOption(String name)
    {
        this.childOption = name;
    }

    public String getChildOption()
    {
        return childOption;
    }

    public String getPreferredOrder()
    {
        return preferredOrder;
    }

    public void setPreferredOrder(String value)
    {
        this.preferredOrder = value;
    }
}
