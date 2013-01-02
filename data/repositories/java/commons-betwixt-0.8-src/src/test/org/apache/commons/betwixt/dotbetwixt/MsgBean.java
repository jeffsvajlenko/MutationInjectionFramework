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
package org.apache.commons.betwixt.dotbetwixt;

/**
 * The bean used to identify a problem there was when a dotbetwixt file
 * did not have any update methods on the element, but on the attributes.
 *
 * @author <a href="mstanley@cauldronsolutions.com">Mike Stanley</a>
 * @version $Id: MsgBean.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class MsgBean
{
    private String type;
    private String status;
    private String name;
    private String description;
    private String toAddress;
    private String fromAddress;
    private String optionalField1;
    private String optionalField2;

    /**
     *
     */
    public MsgBean()
    {
        super();
    }

    /**
     * @return
     */
    public String getFromAddress()
    {
        return fromAddress;
    }

    /**
     * @param fromAddress
     */
    public void setFromAddress(String fromAddress)
    {
        this.fromAddress = fromAddress;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status)
    {
        this.status = status;
    }

    /**
     * @return
     */
    public String getToAddress()
    {
        return toAddress;
    }

    /**
     * @param toAddress
     */
    public void setToAddress(String toAddress)
    {
        this.toAddress = toAddress;
    }

    /**
     * @return
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param type
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return
     */
    public String getOptionalField1()
    {
        return optionalField1;
    }

    /**
     * @param optionalField1
     */
    public void setOptionalField1(String optionalField1)
    {
        this.optionalField1 = optionalField1;
    }

    /**
     * @return
     */
    public String getOptionalField2()
    {
        return optionalField2;
    }

    /**
     * @param optionalField2
     */
    public void setOptionalField2(String optionalField2)
    {
        this.optionalField2 = optionalField2;
    }

}
