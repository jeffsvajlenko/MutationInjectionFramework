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
package org.apache.commons.betwixt.dotbetwixt;

import java.util.ArrayList;
import java.util.List;

/**
  * Bean that has a property that is typed to an interface
  *
  * @author Robert Burrell Donkin
  */
public class ExampleBean
{

    private String name;
    private List examples = new ArrayList();

    public ExampleBean() {}
    public ExampleBean(String name)
    {
        setName(name);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List getExamples()
    {
        return examples;
    }

    public void addExample(IExample example)
    {
        examples.add(example);
    }


    public String toString()
    {
        return "[" + this.getClass().getName() + ": name=" + name + ", examples="
               + examples + "]";
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

