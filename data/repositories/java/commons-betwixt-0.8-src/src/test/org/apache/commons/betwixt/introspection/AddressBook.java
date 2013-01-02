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


package org.apache.commons.betwixt.introspection;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.betwixt.AddressBean;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class AddressBook
{

    private Map addressesByName = new HashMap();
    private Map namesByAddress = new HashMap();

    public AddressBook() {}

    public Map getAddressesByName()
    {
        return addressesByName;
    }

    public void addAddress(String name, AddressBean address)
    {
        addressesByName.put(name, address);
    }

    public Map getNamesByAddress()
    {
        return namesByAddress;
    }

    public void addName(AddressBean address, String name)
    {
        namesByAddress.put(address, name);
    }

}
