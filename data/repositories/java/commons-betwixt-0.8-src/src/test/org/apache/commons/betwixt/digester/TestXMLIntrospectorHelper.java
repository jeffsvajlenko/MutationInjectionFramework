
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
package org.apache.commons.betwixt.digester;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.BeanProperty;
import org.apache.commons.betwixt.CustomerBean;
import org.apache.commons.betwixt.NodeDescriptor;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;

/** Test harness for the XMLIntrospectorHelper
  *
  * @author <a href="mailto:cyu77@yahoo.com">Calvin Yu</a>
  * @version $Revision: 438373 $
  */
public class TestXMLIntrospectorHelper extends TestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestXMLIntrospectorHelper.class);
    }

    public TestXMLIntrospectorHelper(String testName)
    {
        super(testName);
    }

    /**
     * Test the helper's <code>createDescriptor</code> method when a hyphenated name
     * mapper is set.
     */
    public void testCreateDescriptorWithHyphenatedElementNameMapper() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setAttributesForPrimitives(false);
        introspector.getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        BeanInfo beanInfo = Introspector.getBeanInfo(CustomerBean.class);

        NodeDescriptor nickNameProperty = createDescriptor("nickName", beanInfo, introspector);
        assertNotNull("nickName property not found", nickNameProperty);
        assertEquals("nick name property", "nick-name", nickNameProperty.getLocalName());

        NodeDescriptor projectNamesProperty = createDescriptor("projectNames", beanInfo, introspector);
        assertNotNull("projectNames property not found", projectNamesProperty);
        assertEquals("project names property", "project-names", projectNamesProperty.getLocalName());
    }

    public void testNullParameters() throws Exception
    {
        new XMLIntrospector().isLoopType(null);
    }

    /**
     * Find the specified property and convert it into a descriptor.
     */
    private NodeDescriptor createDescriptor(String propertyName, BeanInfo beanInfo, XMLIntrospector introspector)
    throws IntrospectionException
    {
        PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
        for (int i=0; i<properties.length; i++)
        {
            if (propertyName.equals(properties[i].getName()))
            {
                NodeDescriptor desc = (NodeDescriptor) introspector
                                      .createXMLDescriptor(new BeanProperty(properties[i]));
                return desc;
            }
        }
        return null;
    }

}
