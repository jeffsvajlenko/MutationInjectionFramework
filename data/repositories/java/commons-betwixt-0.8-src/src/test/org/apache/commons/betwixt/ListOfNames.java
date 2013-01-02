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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** <p>A simple collection of <code>NameBean</code>'s.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  */
public class ListOfNames
{

    private List names = new ArrayList();

    public ListOfNames() {}

    public void addName(NameBean name)
    {
        names.add(name);
    }

    public List getNames()
    {
        return names;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("[");
        buffer.append("ListOfNames: ");
        boolean first = true;
        Iterator it = names.iterator();
        while ( it.hasNext() )
        {
            if ( first )
            {
                first = !first;
            }
            else
            {
                buffer.append(',');
            }
            buffer.append("'");
            buffer.append( ((NameBean) it.next()).getName() );
            buffer.append("'");
        }

        buffer.append("]");

        return buffer.toString();
    }

    public boolean equals( Object obj )
    {
        if ( obj == null ) return false;
        if (obj instanceof ListOfNames)
        {
            ListOfNames otherList = (ListOfNames) obj;
            int count = 0;
            Iterator it = otherList.getNames().iterator();
            while (it.hasNext())
            {
                if (! names.get(count++).equals(it.next()))
                {
                    return false;
                }
            }

            return true;
        }

        return false;
    }
}
