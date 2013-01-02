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
package org.apache.commons.betwixt.strategy;

import junit.framework.TestCase;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.read.AddressBean;

public class TestValueSuppressionStrategy extends TestCase
{

    public void testALLOW_ALL_VALUESStrategy() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo beanInfo = introspector.introspect(AddressBean.class);
        AttributeDescriptor[] descriptors = beanInfo.getElementDescriptor().getAttributeDescriptors();
        assertTrue(descriptors.length>0);
        for (int i=0; i<descriptors.length; i++)
        {
            assertFalse(ValueSuppressionStrategy.ALLOW_ALL_VALUES.suppressAttribute(descriptors[i], "Arbitrary Value"));
            assertFalse(ValueSuppressionStrategy.ALLOW_ALL_VALUES.suppressAttribute(descriptors[i], ""));
            assertFalse(ValueSuppressionStrategy.ALLOW_ALL_VALUES.suppressAttribute(descriptors[i], null));
        }
    }
}
