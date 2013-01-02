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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.registry.DefaultXMLBeanInfoRegistry;
import org.apache.commons.betwixt.registry.NoCacheRegistry;
import org.apache.commons.betwixt.strategy.ClassNormalizer;
import org.apache.commons.betwixt.strategy.ListedClassNormalizer;
import org.apache.commons.digester.rss.Channel;


/** Test harness for the XMLIntrospector
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class TestXMLIntrospector extends AbstractTestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestXMLIntrospector.class);
    }

    public TestXMLIntrospector(String testName)
    {
        super(testName);
    }

    public void testIntrospector() throws Exception
    {
        //SimpleLog log = new SimpleLog("testIntrospector:introspector");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        XMLIntrospector introspector = new XMLIntrospector();
        //introspector.setLog(log);

        introspector.getConfiguration().setAttributesForPrimitives(true);

        Object bean = createBean();

        XMLBeanInfo info = introspector.introspect( bean );

        assertTrue( "Found XMLBeanInfo", info != null );

        ElementDescriptor descriptor = info.getElementDescriptor();

        assertTrue( "Found root element descriptor", descriptor != null );

        AttributeDescriptor[] attributes = descriptor.getAttributeDescriptors();

        assertTrue( "Found attributes", attributes != null && attributes.length > 0 );

        // test second introspection with caching on
        info = introspector.introspect( bean );

        assertTrue( "Found XMLBeanInfo", info != null );

        descriptor = info.getElementDescriptor();

        assertTrue( "Found root element descriptor", descriptor != null );

        attributes = descriptor.getAttributeDescriptors();

        assertTrue( "Found attributes", attributes != null && attributes.length > 0 );


        // test introspection with caching off
        //introspector.setCachingEnabled(false);
        introspector.setRegistry(new NoCacheRegistry());
        info = introspector.introspect( bean );

        assertTrue( "Found XMLBeanInfo", info != null );

        descriptor = info.getElementDescriptor();

        assertTrue( "Found root element descriptor", descriptor != null );

        attributes = descriptor.getAttributeDescriptors();

        assertTrue( "Found attributes", attributes != null && attributes.length > 0 );


        // test introspection after flushing cache
//        introspector.setCachingEnabled(true);
        introspector.setRegistry(new DefaultXMLBeanInfoRegistry());
        //introspector.flushCache();
        info = introspector.introspect( bean );

        assertTrue( "Found XMLBeanInfo", info != null );

        descriptor = info.getElementDescriptor();

        assertTrue( "Found root element descriptor", descriptor != null );

        attributes = descriptor.getAttributeDescriptors();

        assertTrue( "Found attributes", attributes != null && attributes.length > 0 );

    }

    public void testBeanWithBeanInfo() throws Exception
    {

        // let's check that bean info's ok
        BeanInfo bwbiBeanInfo = Introspector.getBeanInfo(BeanWithBeanInfoBean.class);

        PropertyDescriptor[] propertyDescriptors = bwbiBeanInfo.getPropertyDescriptors();

        assertEquals("Wrong number of properties", 2 , propertyDescriptors.length);

        // order of properties isn't guarenteed
        if ("alpha".equals(propertyDescriptors[0].getName()))
        {

            assertEquals("Second property name", "gamma" , propertyDescriptors[1].getName());

        }
        else
        {

            assertEquals("First property name", "gamma" , propertyDescriptors[0].getName());
            assertEquals("Second property name", "alpha" , propertyDescriptors[1].getName());
        }

        // finished with the descriptors
        propertyDescriptors = null;

//        SimpleLog log = new SimpleLog("[testBeanWithBeanInfo:XMLIntrospector]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setAttributesForPrimitives(false);
//        introspector.setLog(log);

        XMLBeanInfo xmlBeanInfo = introspector.introspect(BeanWithBeanInfoBean.class);

        ElementDescriptor[] elementDescriptors = xmlBeanInfo.getElementDescriptor().getElementDescriptors();

//        log = new SimpleLog("[testBeanWithBeanInfo]");
//        log.setLevel(SimpleLog.LOG_LEVEL_DEBUG);

//        log.debug("XMLBeanInfo:");
//        log.debug(xmlBeanInfo);
//        log.debug("Elements:");
//        log.debug(elementDescriptors[0].getPropertyName());
//        log.debug(elementDescriptors[1].getPropertyName());

        assertEquals("Wrong number of elements", 2 , elementDescriptors.length);

        // order of properties isn't guarenteed
        boolean alphaFirst = true;
        if ("alpha".equals(elementDescriptors[0].getPropertyName()))
        {

            assertEquals("Second element name", "gamma" , elementDescriptors[1].getPropertyName());

        }
        else
        {
            alphaFirst = false;
            assertEquals("First element name", "gamma" , elementDescriptors[0].getPropertyName());
            assertEquals("Second element name", "alpha" , elementDescriptors[1].getPropertyName());
        }

        // might as well give test output
        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        BeanWithBeanInfoBean bean = new BeanWithBeanInfoBean("alpha value","beta value","gamma value");
        writer.write(bean);

        if (alphaFirst)
        {

            xmlAssertIsomorphicContent(
                parseFile("src/test/org/apache/commons/betwixt/introspection/test-bwbi-output-a.xml"),
                parseString(out.toString()));

        }
        else
        {
            xmlAssertIsomorphicContent(
                parseFile("src/test/org/apache/commons/betwixt/introspection/test-bwbi-output-g.xml"),
                parseString(out.toString()));
        }
    }

    public void testDefaultClassNormalizer() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();

        FaceImpl face = new FaceImpl();
        XMLBeanInfo info = introspector.introspect( face );
        ElementDescriptor elementDescriptor = info.getElementDescriptor();

        AttributeDescriptor[] attributeDescriptor = elementDescriptor.getAttributeDescriptors();
        ElementDescriptor[] children = elementDescriptor.getElementDescriptors();

        assertEquals("Expected no attributes", 0, attributeDescriptor.length);
        assertEquals("Expected two elements", 2, children.length);
    }

    public void testClassNormalizer() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setClassNormalizer( new ClassNormalizer()
        {

            public Class normalize(Class clazz)
            {
                if (IFace.class.isAssignableFrom( clazz ))
                {
                    return IFace.class;
                }
                return super.normalize( clazz );
            }
        });

        FaceImpl face = new FaceImpl();
        XMLBeanInfo info = introspector.introspect( face );
        ElementDescriptor elementDescriptor = info.getElementDescriptor();
        assertEquals("Expected only itself", 1, elementDescriptor.getElementDescriptors().length);

        AttributeDescriptor[] attributeDescriptor = elementDescriptor.getAttributeDescriptors();
        ElementDescriptor[] children = elementDescriptor.getElementDescriptors();

        assertEquals("Expected no attributes", 0, attributeDescriptor.length);
        assertEquals("Expected one elements", 1, children.length);
        assertEquals("Expected element", "name", children[0].getLocalName());
    }

    public void testListedClassNormalizer() throws Exception
    {
        ListedClassNormalizer classNormalizer = new ListedClassNormalizer();
        classNormalizer.addSubstitution( IFace.class );
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setClassNormalizer(classNormalizer);

        FaceImpl face = new FaceImpl();

        XMLBeanInfo info = introspector.introspect( face );
        ElementDescriptor elementDescriptor = info.getElementDescriptor();
        AttributeDescriptor[] attributeDescriptor = elementDescriptor.getAttributeDescriptors();
        ElementDescriptor[] children = elementDescriptor.getElementDescriptors();

        assertEquals("Expected no attributes", 0, attributeDescriptor.length);
        assertEquals("Expected one elements", 1, children.length);
        assertEquals("Expected element", "name", children[0].getLocalName());
    }

    public void testListedClassNormalizerWrite() throws Exception
    {
        ListedClassNormalizer classNormalizer = new ListedClassNormalizer();
        classNormalizer.addSubstitution( IFace.class );

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter( out );
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setClassNormalizer( classNormalizer );
        FaceImpl bean = new FaceImpl();
        bean.setName("Old Tom Cobbly");
        writer.write(bean);

        String xml="<?xml version='1.0'?><IFace><name>Old Tom Cobbly</name></IFace>";
        xmlAssertIsomorphicContent(
            parseString(out.getBuffer().toString()),
            parseString(xml),
            true);
    }

    public void testBetwixtFileType() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo info = introspector.introspect( Channel.class );

        ElementDescriptor elementDescriptor = info.getElementDescriptor();

        Class clazz = elementDescriptor.getSingularPropertyType();
        assertEquals( "Element type correct", Channel.class , clazz);

        assertEquals( "Element name correct", "rss", elementDescriptor.getLocalName());
    }

    public void testIgnoreAllBeanInfo() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setIgnoreAllBeanInfo( false );
        introspector.setRegistry(new NoCacheRegistry());
        XMLBeanInfo info = introspector.introspect( BeanWithBeanInfoBean.class );
        ElementDescriptor[] elementDescriptors = info.getElementDescriptor().getElementDescriptors();
        // When BeanInfo is used the properties alpha and gamma will be found
        if ("alpha".equals(elementDescriptors[0].getPropertyName()))
        {
            assertEquals("Second element name", "gamma" , elementDescriptors[1].getPropertyName());
        }
        else
        {
            assertEquals("First element name", "gamma" , elementDescriptors[0].getPropertyName());
            assertEquals("Second element name", "alpha" , elementDescriptors[1].getPropertyName());
        }

        introspector.getConfiguration().setIgnoreAllBeanInfo( true );
        info = introspector.introspect( BeanWithBeanInfoBean.class );
        elementDescriptors = info.getElementDescriptor().getElementDescriptors();
        // When BeanInfo is ignored the properties alpha and beta will be found
        if ("alpha".equals(elementDescriptors[0].getPropertyName()))
        {
            assertEquals("Second element name", "beta" , elementDescriptors[1].getPropertyName());
        }
        else
        {
            assertEquals("First element name", "beta" , elementDescriptors[0].getPropertyName());
            assertEquals("Second element name", "alpha" , elementDescriptors[1].getPropertyName());
        }
    }


}

