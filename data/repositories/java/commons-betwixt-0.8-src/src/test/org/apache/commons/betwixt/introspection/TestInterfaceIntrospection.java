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

package org.apache.commons.betwixt.introspection;

import junit.framework.TestCase;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestInterfaceIntrospection extends TestCase
{

    public void testSuperInterfaceIntrospection() throws Exception
    {

        XMLIntrospector introspector = new XMLIntrospector();

        XMLBeanInfo beanInfo = introspector.introspect(ICopyableDateRange.class);
        ElementDescriptor[] childDescriptors = beanInfo.getElementDescriptor().getElementDescriptors();

        assertEquals("Date range child elements", 2, childDescriptors.length);
        int code = 0;
        for (int i=0; i<2; i++)
        {
            String name = childDescriptors[i].getPropertyName();
            if ("startDate".equals(name))
            {
                code += 1;
            }
            if ("endDate".equals(name))
            {
                code += 2;
            }
        }
        assertEquals("Expected date range elements", 3, code);
    }


    public void testSuperInterfaceIntrospectionWithDotBetwixt() throws Exception
    {

        XMLIntrospector introspector = new XMLIntrospector();

        XMLBeanInfo beanInfo = introspector.introspect(ILaughingCount.class);
        ElementDescriptor[] childDescriptors = beanInfo.getElementDescriptor().getElementDescriptors();

        assertEquals("Laughing count child elements", 1, childDescriptors.length);
        assertEquals("Laughing count super interface matched", "count", childDescriptors[0].getPropertyName());
        assertEquals("Laughing count super interface matched", Integer.TYPE, childDescriptors[0].getPropertyType());
        assertNotNull("Laughing count updater matched", childDescriptors[0].getUpdater());
    }

}
