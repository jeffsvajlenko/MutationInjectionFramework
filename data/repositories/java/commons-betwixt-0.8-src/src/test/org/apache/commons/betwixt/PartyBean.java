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

import java.util.Date;


/** Simple bean
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public class PartyBean
{

    private String excuse;
    private Date dateOfParty;
    private AddressBean venue;
    private int fromHour;

    public PartyBean () {}

    public PartyBean(String excuse, Date date, int fromHour, AddressBean venue)
    {
        setExcuse(excuse);
        setDateOfParty(date);
        setVenue(venue);
        setFromHour(fromHour);
    }

    public String getExcuse()
    {
        return excuse;
    }

    public void setExcuse(String excuse)
    {
        this.excuse = excuse;
    }

    public Date getDateOfParty()
    {
        return dateOfParty;
    }

    public void setDateOfParty(Date dateOfParty)
    {
        this.dateOfParty = dateOfParty;
    }

    public AddressBean getVenue()
    {
        return venue;
    }

    public void setVenue(AddressBean venue)
    {
        this.venue = venue;
    }

    public int getFromHour()
    {
        return fromHour;
    }

    public void setFromHour(int fromHour)
    {
        this.fromHour = fromHour;
    }
}

