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
 * @author Brian Pugh
 */
public class Father
{

    private List kids;
    private String spouse;

    public String getSpouse()
    {
        return spouse;
    }

    public void setSpouse(String spouse)
    {
        this.spouse = spouse;
    }

    public List getKids()
    {
        return kids;
    }

    public void addKid(String kid)
    {
        if (this.kids == null)
        {
            this.kids = new ArrayList();
        }
        this.kids.add(kid);
    }

}
