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
package org.apache.commons.betwixt.io.read;

/**
 * Example enum
 *
 * @author Robert Burrell Donkin
 * @version $Id: HouseBean.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class HouseBean
{

    private CompassPoint facing;
    private AddressBean address;
    private PersonBean householder;
    private boolean isTenant;

    public HouseBean() {}

    public AddressBean getAddress()
    {
        return address;
    }

    public void setAddress(AddressBean address)
    {
        this.address = address;
    }

    public PersonBean getHouseholder()
    {
        return householder;
    }

    public void setHouseholder(PersonBean householder)
    {
        this.householder = householder;
    }

    public boolean isTenant()
    {
        return isTenant;
    }

    public void setTenant(boolean isTenant)
    {
        this.isTenant = isTenant;
    }

    public CompassPoint getFacing()
    {
        return facing;
    }

    public void setFacing(CompassPoint facing)
    {
        this.facing = facing;
    }

    public String toString()
    {
        return "[" + this.getClass().getName() + ": address=" + address + " householder=" + householder
               + " facing=" + facing + " tenant=" + isTenant + "]";
    }

    public boolean equals( Object obj )
    {
        if ( obj == null ) return false;
        return this.hashCode() == obj.hashCode();
    }

    public int hashCode()
    {
        return toString().hashCode();
    }
}
