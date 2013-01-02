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

import java.beans.IntrospectionException;

import org.apache.commons.betwixt.ElementDescriptor;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class LocalElement implements Element
{

    protected String name;

    protected String maxOccurs = "1";

    protected int minOccurs = 0;

    public LocalElement(String name)
    {
        this.name = name;
    }

    public LocalElement(ElementDescriptor descriptor, Schema schema) throws IntrospectionException
    {
        setName(descriptor.getLocalName());
        if (descriptor.isCollective())
        {
            setMaxOccurs("unbounded");
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName(String string)
    {
        name = string;
    }

    public int getMinOccurs()
    {
        return minOccurs;
    }

    public void setMinOccurs(int minOccurs)
    {
        this.minOccurs = minOccurs;
    }

    public String getMaxOccurs()
    {
        return maxOccurs;
    }

    public void setMaxOccurs(String maxOccurs)
    {
        this.maxOccurs = maxOccurs;
    }

}
