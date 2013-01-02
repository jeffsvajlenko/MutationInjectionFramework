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

package org.apache.commons.betwixt.versioning;

import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.Options;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestVersioning extends AbstractTestCase
{
    public static Log log = LogFactory.getLog(TestVersioning.class);

    public TestVersioning(String testName)
    {
        super(testName);
    }

    private void configure(BindingConfiguration configuration)
    {
        configuration.setMapIDs(false);
    }

    public void testIntrospection() throws Exception
    {
        log.info("testIntrospection() started");

        XMLIntrospector introspector = new XMLIntrospector();
        XMLBeanInfo beanInfo = introspector
                               .introspect(VersioningTestData.class);

        // 2 Element descriptors
        ElementDescriptor[] elementDescriptors = beanInfo
                .getElementDescriptor().getElementDescriptors();
        assertEquals("Need 2 element descriptors", 2, elementDescriptors.length);

        ElementDescriptor element1Descriptor = beanInfo.getElementDescriptor()
                                               .getElementDescriptor("element1");
        log.info("element1Descriptor: " + element1Descriptor);
        debugOptions(element1Descriptor.getOptions());
        assertNotNull(element1Descriptor);
        assertEquals("1", element1Descriptor.getOptions().getValue(
                         "version-from"));
        assertNull(element1Descriptor.getOptions().getValue("version-until"));

        ElementDescriptor element2Descriptor = beanInfo.getElementDescriptor()
                                               .getElementDescriptor("element2");
        log.info("element2Descriptor: " + element2Descriptor);
        debugOptions(element2Descriptor.getOptions());
        assertNotNull(element2Descriptor);
        assertEquals("2", element2Descriptor.getOptions().getValue(
                         "version-from"));
        assertNull(element2Descriptor.getOptions().getValue("version-until"));

        // 2 Attribute descriptors
        AttributeDescriptor[] attributeDescriptors = beanInfo
                .getElementDescriptor().getAttributeDescriptors();
        assertEquals("Need 2 attribute descriptors", 2,
                     attributeDescriptors.length);

        AttributeDescriptor attribute1Descriptor = beanInfo
                .getElementDescriptor().getAttributeDescriptor("attribute1");
        log.info("attribute1Descriptor: " + attribute1Descriptor);
        debugOptions(attribute1Descriptor.getOptions());
        assertNotNull(attribute1Descriptor);
        assertEquals("2", attribute1Descriptor.getOptions().getValue(
                         "version-from"));
        assertNull(attribute1Descriptor.getOptions().getValue("version-until"));

        AttributeDescriptor attribute2Descriptor = beanInfo
                .getElementDescriptor().getAttributeDescriptor("attribute2");
        log.info("attribute2Descriptor: " + attribute2Descriptor);
        debugOptions(attribute2Descriptor.getOptions());
        assertNotNull(attribute2Descriptor);
        assertEquals("1", attribute2Descriptor.getOptions().getValue(
                         "version-from"));
        assertEquals("2", attribute2Descriptor.getOptions().getValue(
                         "version-until"));

        log.info("testIntrospection() complete");
    }

    /**
     * Simple test case with no version specified: All elements/attributes will
     * be written.
     *
     * @throws Exception
     */
    public void testWrite1() throws Exception
    {
        log.info("testWrite1() started");

        final VersioningTestData data = new VersioningTestData();
        data.setAttribute1("attributevalue1");
        data.setAttribute2("attributevalue2");
        data.setElement1("elementvalue1");
        data.setElement2("elementvalue2");

        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);
        configure(writer.getBindingConfiguration());
        writer.write(data);

        final String written = out.toString();
        log.info("Written:\n" + written);

        final String expected = "<VersioningTestData attribute1=\"attributevalue1\" attribute2=\"attributevalue2\"><element1>elementvalue1</element1><element2>elementvalue2</element2></VersioningTestData>";
        xmlAssertIsomorphicContent(parseString(expected), parseString(written),
                                   true);

        log.info("testWrite1() complete");
    }

    /**
     * Version = 1
     *
     * <ul>
     * <li>Attribute1 (2-/): Not written
     * <li>Attribute2 (1-2): Written
     * <li>Element1 (1-/): Written
     * <li>Element2 (2-/): Not written
     * </ul>
     *
     * @throws Exception
     */
    public void testWrite2() throws Exception
    {
        log.info("testWrite2() started");

        final VersioningTestData data = new VersioningTestData();
        data.setAttribute1("attributevalue1");
        data.setAttribute2("attributevalue2");
        data.setElement1("elementvalue1");
        data.setElement2("elementvalue2");

        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);

        final VersioningStrategy versioningStrategy = new VersioningStrategy(
            "1");
        writer.getXMLIntrospector().getConfiguration()
        .setAttributeSuppressionStrategy(versioningStrategy);
        writer.getXMLIntrospector().getConfiguration()
        .setElementSuppressionStrategy(versioningStrategy);

        configure(writer.getBindingConfiguration());
        writer.write(data);

        final String written = out.toString();
        log.info("Written:\n" + written);

        final String expected = "<VersioningTestData attribute2=\"attributevalue2\"><element1>elementvalue1</element1></VersioningTestData>";
        xmlAssertIsomorphicContent(parseString(expected), parseString(written),
                                   true);

        log.info("testWrite1() complete");
    }

    private final void debugOptions(final Options options)
    {
        final String[] names = options.getNames();

        log.info("Names:");

        for (int ii = 0; ii < names.length; ii++)
        {
            final String name = names[ii];

            log.info("  Name " + ii + ": " + name + "="
                     + options.getValue(name));
        }
    }


    /**
     * Version = 2
     *
     * <ul>
     * <li>Attribute1 (2-/): written
     * <li>Attribute2 (1-2): Written
     * <li>Element1 (1-/): Written
     * <li>Element2 (2-/): written
     * </ul>
     *
     * @throws Exception
     */
    public void testWrite3() throws Exception
    {
        log.info("testWrite2() started");

        final VersioningTestData data = new VersioningTestData();
        data.setAttribute1("attributevalue1");
        data.setAttribute2("attributevalue2");
        data.setElement1("elementvalue1");
        data.setElement2("elementvalue2");

        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);

        final VersioningStrategy versioningStrategy = new VersioningStrategy(
            "2");
        writer.getXMLIntrospector().getConfiguration()
        .setAttributeSuppressionStrategy(versioningStrategy);
        writer.getXMLIntrospector().getConfiguration()
        .setElementSuppressionStrategy(versioningStrategy);

        configure(writer.getBindingConfiguration());
        writer.write(data);

        final String written = out.toString();
        log.info("Written:\n" + written);

        final String expected = "<VersioningTestData attribute1=\"attributevalue1\" attribute2=\"attributevalue2\"><element1>elementvalue1</element1><element2>elementvalue2</element2></VersioningTestData>";
        xmlAssertIsomorphicContent(parseString(expected), parseString(written),
                                   true);

        log.info("testWrite1() complete");
    }


    /**
     * Version = 3
     *
     * <ul>
     * <li>Attribute1 (2-/): written
     * <li>Attribute2 (1-2): Not Written
     * <li>Element1 (1-/): Written
     * <li>Element2 (2-/): written
     * </ul>
     *
     * @throws Exception
     */
    public void testWrite4() throws Exception
    {
        log.info("testWrite2() started");

        final VersioningTestData data = new VersioningTestData();
        data.setAttribute1("attributevalue1");
        data.setAttribute2("attributevalue2");
        data.setElement1("elementvalue1");
        data.setElement2("elementvalue2");

        StringWriter out = new StringWriter();
        BeanWriter writer = new BeanWriter(out);

        final VersioningStrategy versioningStrategy = new VersioningStrategy(
            "3");
        writer.getXMLIntrospector().getConfiguration()
        .setAttributeSuppressionStrategy(versioningStrategy);
        writer.getXMLIntrospector().getConfiguration()
        .setElementSuppressionStrategy(versioningStrategy);

        configure(writer.getBindingConfiguration());
        writer.write(data);

        final String written = out.toString();
        log.info("Written:\n" + written);

        final String expected = "<VersioningTestData attribute1=\"attributevalue1\"><element1>elementvalue1</element1><element2>elementvalue2</element2></VersioningTestData>";
        xmlAssertIsomorphicContent(parseString(expected), parseString(written),
                                   true);

        log.info("testWrite1() complete");
    }
}
