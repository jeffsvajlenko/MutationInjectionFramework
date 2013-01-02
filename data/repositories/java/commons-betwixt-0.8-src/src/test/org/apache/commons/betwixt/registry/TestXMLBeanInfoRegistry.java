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
package org.apache.commons.betwixt.registry;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.XMLBeanInfo;

/** Test harness for the XMLBeanInfoRegistry
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public class TestXMLBeanInfoRegistry extends AbstractTestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestXMLBeanInfoRegistry.class);
    }

    public TestXMLBeanInfoRegistry(String testName)
    {
        super(testName);
    }

    public void testNoCache() throws Exception
    {
        XMLBeanInfoRegistry registry = new NoCacheRegistry();

        XMLBeanInfo xbi = new XMLBeanInfo(Long.class);

        assertNull("No cache XML register (1)", registry.get(Long.class));

        registry.put(Long.class, xbi);

        assertNull("No cache XML register (2)", registry.get(Long.class));
    }

    public void testDefault() throws Exception
    {

        XMLBeanInfoRegistry registry = new DefaultXMLBeanInfoRegistry();

        XMLBeanInfo xbi = new XMLBeanInfo(Long.class);

        assertNull("Default XML register (1)", registry.get(Long.class));

        registry.put(Long.class, xbi);

        assertEquals("Default XML register (2)", xbi, registry.get(Long.class));
    }
}

