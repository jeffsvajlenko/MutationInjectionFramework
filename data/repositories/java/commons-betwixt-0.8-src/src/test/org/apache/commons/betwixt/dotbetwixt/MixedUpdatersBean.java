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
  * This is a simple bean used to test customized updaters.
  *
  * @author Robert Burrell Donkin
  */
public class MixedUpdatersBean extends PrivateMethodsBean
{

//-------------------------- Attributes
    private String name;
    private String badName = "**UNSET**";
    private List items = new ArrayList();
    private List badItems = new ArrayList();
    private String privateProperty;
    private List privateItems = new ArrayList(3);

//-------------------------- Constructors

    public MixedUpdatersBean() {}

    public MixedUpdatersBean(String name)
    {
        setName(name);
    }

//--------------------------- Properties

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List getItems()
    {
        return items;
    }

    public void addItem(String item)
    {
        items.add(item);
    }

    public String getBadName()
    {
        return badName;
    }

    public void badNameSetter(String badName)
    {
        this.badName = badName;
    }

    public List getBadItems()
    {
        return badItems;
    }

    public void badItemAdder(String badItem)
    {
        badItems.add(badItem);
    }

    public String getPrivateProperty()
    {
        return privateProperty;
    }

    protected void setPrivateProperty(String privateProp)
    {
        this.privateProperty = privateProp;
    }
    public void privatePropertyWorkaroundSetter(String privateProp)
    {
        this.privateProperty = privateProp;
    }

    public List getPrivateItems()
    {
        return privateItems;
    }

    private void addPrivateItem(String item)
    {
        privateItems.add(item);
    }
}
