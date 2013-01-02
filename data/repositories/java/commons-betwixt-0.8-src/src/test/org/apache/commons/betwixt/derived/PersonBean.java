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
package org.apache.commons.betwixt.derived;

/** <p><code>PersonBean</code> is a sample bean for use with the test cases.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @author <a href="mailto:Michael.Davey@coderage.org">Michael Davey</a>
  * @version $Revision: 438373 $
  */
public class PersonBean
{

    private int age;

    private String name;

    public PersonBean() {}

    public PersonBean(int age, String name)
    {
        setAge(age);
        setName(name);
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return "[" + this.getClass().getName() + ": age=" + age + " name=" + name + "]";
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
