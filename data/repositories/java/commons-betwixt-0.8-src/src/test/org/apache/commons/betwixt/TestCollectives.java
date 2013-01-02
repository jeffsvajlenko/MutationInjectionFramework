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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

import org.apache.commons.betwixt.expression.IteratorExpression;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.CapitalizeNameMapper;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestCollectives extends AbstractTestCase
{

    private IntrospectionConfiguration categoriesIntrospectionConfiguration = new IntrospectionConfiguration();
    private BindingConfiguration noIDsBindingConfiguration = new BindingConfiguration();

    public TestCollectives(String testName)
    {
        super(testName);

        CapitalizeNameMapper capitalizeNameMapper = new CapitalizeNameMapper();
        categoriesIntrospectionConfiguration.setAttributesForPrimitives(false);
        categoriesIntrospectionConfiguration.setElementNameMapper(capitalizeNameMapper);
        categoriesIntrospectionConfiguration.setAttributeNameMapper(capitalizeNameMapper);
        categoriesIntrospectionConfiguration.setWrapCollectionsInElement(false);

        noIDsBindingConfiguration.setMapIDs(false);
    }


    public void testWriteCategories() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().setConfiguration(categoriesIntrospectionConfiguration);
        writer.setBindingConfiguration(noIDsBindingConfiguration);

        Categories categories = new Categories();
        categories.addCategory(new Category("Runs"));
        categories.addCategory(new Category("Innings"));
        categories.addCategory(new Category("Dismissals"));
        categories.addCategory(new Category("High Score"));
        categories.addCategory(new Category("Average"));

        writer.write(categories);

        String xml = out.getBuffer().toString();
        String expected = "<?xml version='1.0'?><Categories>" +
                          "<Category><Name>Runs</Name></Category>" +
                          "<Category><Name>Innings</Name></Category>" +
                          "<Category><Name>Dismissals</Name></Category>" +
                          "<Category><Name>High Score</Name></Category>" +
                          "<Category><Name>Average</Name></Category>" +
                          "</Categories>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(xml));
    }

    public void testReadCategories() throws Exception
    {
        BeanReader beanReader = new BeanReader();
        beanReader.getXMLIntrospector().setConfiguration(categoriesIntrospectionConfiguration);
        beanReader.setBindingConfiguration(noIDsBindingConfiguration);
        beanReader.registerBeanClass(Categories.class);

        String xml = "<?xml version='1.0'?><Categories>" +
                     "<Category><Name>Runs</Name></Category>" +
                     "<Category><Name>Innings</Name></Category>" +
                     "<Category><Name>Dismissals</Name></Category>" +
                     "<Category><Name>High Score</Name></Category>" +
                     "<Category><Name>Average</Name></Category>" +
                     "</Categories>";

        StringReader in = new StringReader(xml);

        Categories bean = (Categories) beanReader.parse(in);

        assertEquals("5 categories", 5, bean.size());

        Iterator it = bean.getCategories();
        assertEquals("Runs category", new Category("Runs"), it.next());
        assertEquals("Runs category", new Category("Innings"), it.next());
        assertEquals("Runs category", new Category("Dismissals"), it.next());
        assertEquals("Runs category", new Category("High Score"), it.next());
        assertEquals("Runs category", new Category("Average"), it.next());

    }

    public void testIntrospectListExtension() throws Exception
    {
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        XMLBeanInfo beanInfo = xmlIntrospector.introspect(ArrayListExtender.class);

        ElementDescriptor elementDescriptor = beanInfo.getElementDescriptor();
        ElementDescriptor[] childDescriptors = elementDescriptor.getElementDescriptors();
        assertEquals(2, childDescriptors.length);
        assertEquals("another", childDescriptors[0].getPropertyName());
        assertTrue(childDescriptors[1].getContextExpression() instanceof IteratorExpression);
    }

    public void testWriteListExtension() throws Exception
    {
        ArrayListExtender bean = new ArrayListExtender("Whatever");
        bean.add(new Long(11));
        bean.add(new Long(12));
        bean.add(new Long(13));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs( false );
        writer.write(bean);

        String expected = "<?xml version='1.0'?><ArrayListExtender><another>Whatever</another>" +
                          "<Long>11</Long><Long>12</Long><Long>13</Long></ArrayListExtender>";

        xmlAssertIsomorphicContent(parseString( expected ), parseString( out ));
    }


    public void testReadListExtension() throws Exception
    {
        String xml = "<?xml version='1.0'?><ArrayListExtender><another>Whatever</another>" +
                     "<Long>11</Long><Long>12</Long><Long>13</Long></ArrayListExtender>";

        StringReader in = new StringReader( xml );

        BeanReader reader = new BeanReader();
        reader.getBindingConfiguration().setMapIDs( false );

        reader.registerBeanClass( ArrayListExtender.class );
        ArrayListExtender bean = (ArrayListExtender) reader.parse( in );

        assertEquals("Whatever", bean.getAnother());
    }
}
