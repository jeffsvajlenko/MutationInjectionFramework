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


package org.apache.commons.betwixt.strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TuneBean
{

    private ArrayList composers = new ArrayList();
    private String name;
    private String artist;
    private int recorded;

    public TuneBean() {}
    public TuneBean(String name, String artist, int recorded)
    {
        setName(name);
        setArtist(artist);
        setRecorded(recorded);
    }

    public String getArtist()
    {
        return artist;
    }

    public Iterator getComposers()
    {
        return composers.iterator();
    }

    public String getName()
    {
        return name;
    }

    public int getRecorded()
    {
        return recorded;
    }

    public void setArtist(String string)
    {
        artist = string;
    }

    public void addComposer(ComposerBean composer)
    {
        composers.add(composer);
    }

    public void setName(String string)
    {
        name = string;
    }

    public void setRecorded(int i)
    {
        recorded = i;
    }

    public boolean sameComposers(Collection otherComposers)
    {
        // doesn't check cardinality but should be ok
        if (otherComposers.size() != composers.size())
        {
            return false;
        }
        for (Iterator it=composers.iterator(); it.hasNext();)
        {
            Object object = it.next();
            if (!otherComposers.contains(object))
            {
                return false;
            }
        }
        return true;
    }
}
