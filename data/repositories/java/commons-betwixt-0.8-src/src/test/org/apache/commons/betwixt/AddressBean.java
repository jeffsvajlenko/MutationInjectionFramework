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
package org.apache.commons.betwixt;

import java.io.Serializable;

/** <p><code>CustomerBean</code> is a sample bean for use by the test cases.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @author <a href="mailto:michael.davey@coderage.org">Michael Davey</a>
  * @version $Revision: 438373 $
  */
public class AddressBean implements Serializable
{

    private String street;
    private String city;
    private String code;
    private String country;

    public AddressBean()
    {
    }

    public AddressBean(String street, String city, String country, String code)
    {
        setStreet(street);
        setCity(city);
        setCode(code);
        setCountry(country);
    }

    public String getStreet()
    {
        return street;
    }

    public String getCity()
    {
        return city;
    }

    public String getCode()
    {
        return code;
    }

    public String getCountry()
    {
        return country;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String toString()
    {
        return "[" + this.getClass().getName() + ": street=" + street + ", city="
               + city+ ", country=" + country + "]";
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
