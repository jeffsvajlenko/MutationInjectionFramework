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

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class CustomerBean
{

    private String code;
    private String name;
    private String street;
    private String town;
    private String country;
    private String postcode;

    public CustomerBean () {}

    public CustomerBean(String code, String name, String street,
                        String town, String country, String postcode)
    {
        setCode(code);
        setName(name);
        setStreet(street);
        setTown(town);
        setPostcode(postcode);
        setCountry(country);
    }

    public String getCode()
    {
        return code;
    }


    public String getCountry()
    {
        return country;
    }


    public String getName()
    {
        return name;
    }

    public String getPostcode()
    {
        return postcode;
    }

    public String getStreet()
    {
        return street;
    }

    public String getTown()
    {
        return town;
    }

    public void setCode(String string)
    {
        code = string;
    }

    public void setCountry(String string)
    {
        country = string;
    }

    public void setName(String string)
    {
        name = string;
    }

    public void setPostcode(String string)
    {
        postcode = string;
    }

    public void setStreet(String string)
    {
        street = string;
    }

    public void setTown(String string)
    {
        town = string;
    }

}
