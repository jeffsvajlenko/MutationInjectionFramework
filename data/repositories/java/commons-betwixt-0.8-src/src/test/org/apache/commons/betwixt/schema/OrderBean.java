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


package org.apache.commons.betwixt.schema;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class OrderBean
{

    private String code;
    private List lines = new ArrayList();
    private CustomerBean customer;

    public OrderBean() {}

    public OrderBean(String code, CustomerBean customer)
    {
        this.customer = customer;
        this.code = code;
    }


    public String getCode()
    {
        return code;
    }

    public CustomerBean getCustomer()
    {
        return customer;
    }

    public Iterator getLines()
    {
        return lines.iterator();
    }

    public void setCode(String string)
    {
        code = string;
    }

    public void setCustomer(CustomerBean bean)
    {
        customer = bean;
    }

    public void addLine(OrderLineBean line)
    {
        lines.add(line);
    }
}
