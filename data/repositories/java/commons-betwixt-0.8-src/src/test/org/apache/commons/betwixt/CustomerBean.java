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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** <p><code>CustomerBean</code> is a sample bean for use by the test cases.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @author <a href="mailto:michael.davey@coderage.org">Michael Davey</a>
  * @version $Revision: 438373 $
  */
public class CustomerBean implements Serializable
{

    /** Logger */
    private static final Log log = LogFactory.getLog( CustomerBean.class );

    private String id;
    private String name;
    private String nickName;
    private String[] emails;
    private int[] numbers;
    private AddressBean address;
    private Map projectMap;
    private List locations = new ArrayList();
    private Date date;
    private Time time;
    private Timestamp timestamp;
    private BigDecimal bigDecimal;
    private BigInteger bigInteger;

    public CustomerBean()
    {
    }

    public String getID()
    {
        return id;
    }

    public String getNickName()
    {
        return nickName;
    }


    public String getName()
    {
        return name;
    }

    public String[] getEmails()
    {
        return emails;
    }

    public int[] getNumbers()
    {
        return numbers;
    }

    public AddressBean getAddress()
    {
        return address;
    }

    public Map getProjectMap()
    {
        return projectMap;
    }

    public Iterator getProjectNames()
    {
        if ( projectMap == null )
        {
            return null;
        }
        return projectMap.keySet().iterator();
    }

    public Enumeration getProjectURLs()
    {
        if ( projectMap == null )
        {
            return null;
        }
        return new IteratorEnumeration( projectMap.values().iterator() );
    }

    public List getLocations()
    {
        return locations;
    }

    /** An indexed property */
    public String getLocation(int index)
    {
        return (String) locations.get(index);
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public void setEmails(String[] emails)
    {
        this.emails = emails;
    }

    public void addEmail(String email)
    {
        int newLength = (emails == null) ? 1 : emails.length+1;
        String[] newArray = new String[newLength];
        for (int i=0; i< newLength-1; i++)
        {
            newArray[i] = emails[i];
        }
        newArray[newLength-1] = email;
        emails = newArray;
    }

    public void setNumbers(int[] numbers)
    {
        this.numbers = numbers;
    }

    public void addNumber(int number)
    {
        if ( log.isDebugEnabled() )
        {
            log.debug( "Adding number: " + number );
        }

        int newLength = (numbers == null) ? 1 : numbers.length+1;
        int[] newArray = new int[newLength];
        for (int i=0; i< newLength-1; i++)
        {
            newArray[i] = numbers[i];
        }
        newArray[newLength-1] = number;
        numbers = newArray;
    }

    public void setAddress(AddressBean address)
    {
        this.address = address;

        if ( log.isDebugEnabled() )
        {
            log.debug( "Setting the address to be: " + address );
        }
    }

    public void setProjectMap(Map projectMap)
    {
        this.projectMap = projectMap;
    }

    public void addLocation(String location)
    {
        locations.add(location);
    }

    /** An indexed property */
    public void setLocation(int index, String location)
    {
        if ( index == locations.size() )
        {
            locations.add( location );
        }
        else
        {
            locations.set(index, location);
        }
    }

    public String toString()
    {
        return "[" + this.getClass().getName() + ": ID=" + id + ", name=" + name
               + ", address=" + address + "]";
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
    /**
     * Returns the date.
     * @return Date
     */
    public Date getDate()
    {
        return date;
    }

    /**
     * Returns the time.
     * @return Time
     */
    public Time getTime()
    {
        return time;
    }

    /**
     * Returns the timestamp.
     * @return Timestamp
     */
    public Timestamp getTimestamp()
    {
        return timestamp;
    }

    /**
     * Sets the date.
     * @param date The date to set
     */
    public void setDate(Date date)
    {
        this.date = date;
    }

    /**
     * Sets the time.
     * @param time The time to set
     */
    public void setTime(Time time)
    {
        this.time = time;
    }

    /**
     * Sets the timestamp.
     * @param timestamp The timestamp to set
     */
    public void setTimestamp(Timestamp timestamp)
    {
        this.timestamp = timestamp;
    }

    /**
     * Returns the bigDecimal.
     * @return BigDecimal
     */
    public BigDecimal getBigDecimal()
    {
        return bigDecimal;
    }

    /**
     * Returns the bigInteger.
     * @return BigInteger
     */
    public BigInteger getBigInteger()
    {
        return bigInteger;
    }

    /**
     * Sets the bigDecimal.
     * @param bigDecimal The bigDecimal to set
     */
    public void setBigDecimal(BigDecimal bigDecimal)
    {
        this.bigDecimal = bigDecimal;
    }

    /**
     * Sets the bigInteger.
     * @param bigInteger The bigInteger to set
     */
    public void setBigInteger(BigInteger bigInteger)
    {
        this.bigInteger = bigInteger;
    }

    /**
     * Adapter to make an {@link Iterator Iterator} instance appear to be
     * an {@link Enumeration Enumeration} instance.
     * Originate in commons collections
     *
     * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
     */
    private static final class IteratorEnumeration implements Enumeration
    {

        /** The iterator being decorated. */
        private Iterator iterator;

        /**
         * Constructs a new <code>IteratorEnumeration</code> that will not
         * function until {@link #setIterator(Iterator) setIterator} is
         * invoked.
         */
        public IteratorEnumeration()
        {
            super();
        }

        /**
         * Constructs a new <code>IteratorEnumeration</code> that will use
         * the given iterator.
         *
         * @param iterator  the iterator to use
         */
        public IteratorEnumeration( Iterator iterator )
        {
            super();
            this.iterator = iterator;
        }

        // Iterator interface
        //-------------------------------------------------------------------------

        /**
         *  Returns true if the underlying iterator has more elements.
         *
         *  @return true if the underlying iterator has more elements
         */
        public boolean hasMoreElements()
        {
            return iterator.hasNext();
        }

        /**
         *  Returns the next element from the underlying iterator.
         *
         *  @return the next element from the underlying iterator.
         *  @throws java.util.NoSuchElementException  if the underlying iterator has no
         *    more elements
         */
        public Object nextElement()
        {
            return iterator.next();
        }

        // Properties
        //-------------------------------------------------------------------------

        /**
         *  Returns the underlying iterator.
         *
         *  @return the underlying iterator
         */
        public Iterator getIterator()
        {
            return iterator;
        }

        /**
         *  Sets the underlying iterator.
         *
         *  @param iterator  the new underlying iterator
         */
        public void setIterator( Iterator iterator )
        {
            this.iterator = iterator;
        }

    }


}
