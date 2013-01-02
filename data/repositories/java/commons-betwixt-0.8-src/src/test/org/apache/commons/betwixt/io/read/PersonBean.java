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

/** <p><code>PersonBean</code> is a sample bean for use with the test cases.</p>
  *
  * @author Robert Burrell Donkin<
  * @version $Revision: 438373 $
  */
public class PersonBean
{

    private String forename;
    private String surname;

    public PersonBean() {}

    public PersonBean(String forename, String surname)
    {
        setForename(forename);
        setSurname(surname);
    }

    public String getForename()
    {
        return forename;
    }

    public void setForename(String forename)
    {
        this.forename = forename;
    }


    public String getSurname()
    {
        return surname;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public String toString()
    {
        return "[" + this.getClass().getName() + ": forename=" + forename + " surname=" + surname + "]";
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
