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

package org.apache.commons.betwixt.io;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class MovieBean
{

    private String name;
    private int year;
    private PersonBean director;
    private Collection actors = new ArrayList();

    public MovieBean() {}
    public MovieBean(String name, int year, PersonBean director)
    {
        setName(name);
        setYear(year);
        setDirector(director);
    }

    public Iterator getActors()
    {
        return actors.iterator();
    }

    public PersonBean getDirector()
    {
        return director;
    }

    public String getName()
    {
        return name;
    }


    public int getYear()
    {
        return year;
    }

    public void addActor(PersonBean actor)
    {
        actors.add(actor);
    }

    public void setDirector(PersonBean bean)
    {
        director = bean;
    }

    public void setName(String string)
    {
        name = string;
    }


    public void setYear(int i)
    {
        year = i;
    }

}
