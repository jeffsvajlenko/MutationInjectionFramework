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
package org.apache.commons.betwixt.schema;

/**
 * <p> This is a bean specifically designed to test cyclic references.
 * The idea is that there's a count that counts every time <code>getFriend</code>
 * gets called and throws a <code>RuntimeException</code> if the count gets too high.</p>
 *
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class CyclicColumn extends ParentColumn
{
    private static int count = 0;
    private static final int max_count = 100;

    private CyclicBean bean;


    public CyclicColumn(String name)
    {
        super(name);
    }

    public CyclicBean getBean()
    {
        if (++count > max_count)
        {
            throw new RuntimeException("Cyclic Reference!");
        }
        return bean;
    }

    public void setBean(CyclicBean aBean)
    {
        this.bean = aBean;
    }

    public String toString()
    {
        return "[CyclicColumn] name=" + getName();
    }

}
