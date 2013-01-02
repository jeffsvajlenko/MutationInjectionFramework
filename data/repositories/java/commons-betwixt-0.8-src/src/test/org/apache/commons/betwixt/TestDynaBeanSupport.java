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
package org.apache.commons.betwixt;

import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.DecapitalizeNameMapper;

/** Test harness for the DynaBeans support
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public class TestDynaBeanSupport extends AbstractTestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestDynaBeanSupport.class);
    }

    public TestDynaBeanSupport(String testName)
    {
        super(testName);
    }

    public void testIntrospectDynaBean() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setAttributesForPrimitives(false);
        XMLBeanInfo beanInfo = introspector.introspect(createDynasaurClass());
        ElementDescriptor baseElement = beanInfo.getElementDescriptor();
        // no attributes
        assertEquals("Correct number of attributes", 0, baseElement.getAttributeDescriptors().length);
        ElementDescriptor[] descriptors = baseElement.getElementDescriptors();
        assertEquals("Correct number of elements", 3, descriptors.length);

        boolean matchedSpecies = false;
        boolean matchedIsRaptor = false;
        boolean matchedPeriod = false;

        for (int i=0, size = descriptors.length; i< size; i++)
        {
            if ("Species".equals(descriptors[i].getPropertyName()))
            {
                matchedSpecies = true;
            }

            if ("isRaptor".equals(descriptors[i].getPropertyName()))
            {
                matchedIsRaptor = true;
            }

            if ("Period".equals(descriptors[i].getPropertyName()))
            {
                matchedPeriod = true;
            }
        }

        assertTrue("Species descriptor not found", matchedSpecies);
        assertTrue("isRaptor descriptor not found", matchedIsRaptor);
        assertTrue("Period descriptor not found", matchedPeriod);
    }

    public void testWriteDynaBean() throws Exception
    {
        DynaBean dynasaur = createDynasaurClass().newInstance();
        dynasaur.set("Species", "Allosaurus");
        dynasaur.set("isRaptor", Boolean.TRUE);
        dynasaur.set("Period", "Jurassic");

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setElementNameMapper(new DecapitalizeNameMapper());
        writer.write(dynasaur);

        String xml = "<?xml version='1.0'?><dynasaur><species>Allosaurus</species>"
                     + "<isRaptor>true</isRaptor><period>Jurassic</period></dynasaur>";

        xmlAssertIsomorphicContent(
            "Test write dyna beans",
            parseString(xml),
            parseString(out.getBuffer().toString()),
            true);
    }

    public void testOverrideWithDotBetwixt() throws Exception
    {
        DynaWithDotBetwixt bean = new DynaWithDotBetwixt("Tweedledum","Tweedledee");
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().getConfiguration().setElementNameMapper(new DecapitalizeNameMapper());
        writer.write("bean", bean);

        String xml = "<?xml version='1.0'?><bean><ndp>Tweedledum</ndp></bean>";
        xmlAssertIsomorphicContent(
            "Test write dyna beans with dt betwixt",
            parseString(xml),
            parseString(out.getBuffer().toString()),
            true);

    }

    private DynaClass createDynasaurClass() throws Exception
    {
        DynaClass dynaClass = new BasicDynaClass
        ("Dynasaur", null,
         new DynaProperty[]
         {
             new DynaProperty("Species", String.class),
             new DynaProperty("isRaptor", Boolean.TYPE),
             new DynaProperty("Period", String.class),
         });
        return (dynaClass);
    }
}


