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

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;


/** <p>Test bean which extends DynaBean but has a .betwixt file.</p>
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class DynaWithDotBetwixt implements DynaBean
{

    private String notDynaProperty;
    private String dynaProperty;

    public DynaWithDotBetwixt()
    {
        this("DEFAUL_NOT_DYNA", "DEFAULT_DYNA");
    }


    public DynaWithDotBetwixt(String notDynaProperty, String dynaProperty)
    {
        this.notDynaProperty = notDynaProperty;
        this.dynaProperty = dynaProperty;
    }

    public String getNotDynaProperty()
    {
        return notDynaProperty;
    }

    public String fiddleDyna()
    {
        return dynaProperty;
    }

    public boolean contains(String name, String key)
    {
        return false;
    }

    public Object get(String name)
    {
        return dynaProperty;
    }

    public Object get(String name, int index)
    {
        return dynaProperty;
    }

    public Object get(String name, String key)
    {
        return dynaProperty;
    }

    public DynaClass getDynaClass()
    {
        return new DynaClass()
        {
            public DynaProperty[] getDynaProperties()
            {
                DynaProperty[] properties = {new DynaProperty("DynaProp", String.class)};
                return properties;
            }

            public String getName()
            {
                return "DynaWithDotBetwixtClass";
            }

            public DynaBean newInstance()
            {
                return new DynaWithDotBetwixt();
            }

            public DynaProperty getDynaProperty(String name)
            {
                if ("DynaProp".equals(name))
                {
                    return new DynaProperty("DynaProp", String.class);
                }
                return null;
            }
        };
    }

    public void remove(String name, String key) {}

    public void set(String name, Object value) {}

    public void set(String name, int index, Object value) {}

    public void set(String name, String key, Object value) {}

}

