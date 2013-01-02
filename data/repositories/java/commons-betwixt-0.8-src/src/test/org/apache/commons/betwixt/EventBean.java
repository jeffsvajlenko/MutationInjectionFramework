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

import java.sql.Timestamp;

/**
 * Represent some kind of event.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class EventBean
{

    private Timestamp start;
    private Timestamp end;
    private String type;
    private String description;

    public EventBean() {}

    public EventBean(String type, String description, Timestamp start, Timestamp end)
    {
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
    }


    public String getDescription()
    {
        return description;
    }

    public Timestamp getEnd()
    {
        return end;
    }

    public Timestamp getStart()
    {
        return start;
    }

    public String getType()
    {
        return type;
    }

    public void setDescription(String string)
    {
        description = string;
    }

    public void setEnd(Timestamp timestamp)
    {
        end = timestamp;
    }

    public void setStart(Timestamp timestamp)
    {
        start = timestamp;
    }

    public void setType(String string)
    {
        type = string;
    }

}
