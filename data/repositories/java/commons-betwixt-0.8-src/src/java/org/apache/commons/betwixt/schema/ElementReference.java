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
public class ElementReference extends GlobalElement
{

    protected String maxOccurs = "1";

    protected int minOccurs = 0;

    public ElementReference(String string, GlobalComplexType complexType)
    {

        super(string, complexType);
    }

    public ElementReference(String name, String type)
    {
        super(name, type);
    }

    public ElementReference(TranscriptionConfiguration configuration, ElementDescriptor elementDescriptor, Schema schema) throws IntrospectionException
    {
        setName(elementDescriptor.getLocalName());
        if (elementDescriptor.isHollow())
        {
            setComplexType( schema.addGlobalComplexType( configuration, elementDescriptor ));
            if (elementDescriptor.isCollective())
            {
                maxOccurs = "unbounded";
            }
        }
        else
        {

            setType("xsd:string");
        }
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
