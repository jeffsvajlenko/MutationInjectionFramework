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
package org.apache.commons.betwixt.recursion;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class HybridBean
{
    private AlienBean alienBean;
    private PersonBean personBean;

    public HybridBean(AlienBean alienBean, PersonBean personBean)
    {
        setAlien(alienBean);
        setPerson(personBean);
    }

    public HybridBean () {}


    public AlienBean getAlien()
    {
        return alienBean;
    }

    public void setAlien(AlienBean alienBean)
    {
        this.alienBean = alienBean;
    }

    public PersonBean getPerson()
    {
        return personBean;
    }

    public void setPerson(PersonBean personBean)
    {
        this.personBean = personBean;
    }
}
