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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.xmlunit.XmlTestCase;

/**
  * Test customization of xml to bean mapping using .betwixt files.
  *
  * @author Robert Burrell Donkin
  */
public class TestIntrospection extends XmlTestCase
{

//--------------------------------- Test Suite

    public static Test suite()
    {
        return new TestSuite(TestIntrospection.class);
    }

//--------------------------------- Constructor

    public TestIntrospection(String testName)
    {
        super(testName);
    }

//---------------------------------- Tests

    public void testClassAttribute() throws Exception
    {
        //SimpleLog log = new SimpleLog("[testClassAttribute:ElementRule]");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //ElementRule.setLog(log);

        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo beanInfo = introspector.introspect(ExampleBean.class);
        ElementDescriptor[] elementDescriptors = beanInfo.getElementDescriptor().getElementDescriptors();
        ElementDescriptor elementsElementDescriptor = null;
        for ( int i=0, size = elementDescriptors.length; i<size ; i++ )
        {
            if ( "example".equals( elementDescriptors[i].getLocalName() ) )
            {
                elementsElementDescriptor = elementDescriptors[i];
            }
        }

        assertNotNull("Element descriptor for elements not found", elementsElementDescriptor);
        assertEquals(
            "Class property not set",
            ExampleImpl.class,
            elementsElementDescriptor.getImplementationClass());
    }
}

