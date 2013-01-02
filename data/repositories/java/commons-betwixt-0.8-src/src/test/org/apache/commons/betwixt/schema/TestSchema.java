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

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.registry.DefaultXMLBeanInfoRegistry;
import org.apache.commons.betwixt.strategy.DecapitalizeNameMapper;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;

//import org.apache.commons.logging.impl.SimpleLog;
//import org.apache.commons.betwixt.io.BeanRuleSet;

/**
 * This will test betwixt on handling a different kind of xml file, without
 * a "collection" tag.
 *
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Id: TestSchema.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestSchema extends AbstractTestCase
{

    public static Test suite()
    {
        return new TestSuite(TestSchema.class);
    }


    public TestSchema(String testName)
    {
        super(testName);
    }

    /**
     * Test the roundtrip with an xml file that doesn't have
     * collection elements, writes it with collection elements
     * and then compares the 2 object, which should end up
     * equal..
     */
    public void testCombinedRoundTrip()
    throws Exception
    {
//        SimpleLog log = new SimpleLog("[CombinedRoundTrip:BeanRuleSet]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        BeanRuleSet.setLog(log);

//        log = new SimpleLog("[CombinedRoundTrip]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        BeanReader reader = createBeanReader();

        PhysicalSchema schema = (PhysicalSchema) reader.parse(
                                    getTestFileURL("src/test/org/apache/commons/betwixt/schema/schema.xml"));
        StringWriter buffer = new StringWriter();
        write(schema, buffer, true);

//        log.debug(buffer.getBuffer().toString());

        StringReader in = new StringReader(buffer.getBuffer().toString());
        reader = createBeanReader();
        XMLIntrospector intro = createXMLIntrospector();
        DefaultXMLBeanInfoRegistry registry = new DefaultXMLBeanInfoRegistry();
        intro.setRegistry(registry);
        // we have written the xml file back with element collections,
        // so we have to say to the reader we want to use that now
        // (the default when creating in this test is not to use them)
        intro.getConfiguration().setWrapCollectionsInElement(true);
        // first flush the cash, else setting other options, doesn't
        // end up in rereading / mapping the object model.
        registry.flush();
        // set the xmlIntrospector back to the reader
        reader.setXMLIntrospector(intro);
        reader.deregisterBeanClass(PhysicalSchema.class);
        reader.getRules().clear();
        reader.registerBeanClass(PhysicalSchema.class);
        PhysicalSchema schemaSecond = (PhysicalSchema) reader.parse(in);
        buffer.close();
        write(schema,buffer, true);
        assertEquals(schema, schemaSecond);
    }
    /**
     * Tests we can round trip from the XML -> bean -> XML -> bean.
     * It will test if both object are identical.
     * For this to actually work I implemented a details equals in my
     * Beans..
     */
    public void testRoundTripWithoutCollectionElement()
    throws Exception
    {
        BeanReader reader = createBeanReader();
        PhysicalSchema schema = (PhysicalSchema) reader.parse(
                                    getTestFileURL("src/test/org/apache/commons/betwixt/schema/schema.xml"));
        StringWriter buffer = new StringWriter();
        write(schema, buffer, false);
        StringReader in = new StringReader(buffer.getBuffer().toString());
        PhysicalSchema schemaSecond = (PhysicalSchema) reader.parse(in);
        assertEquals(schemaSecond, schema);
    }

    /**
     * Creates a beanReader
     */
    protected BeanReader createBeanReader()
    throws Exception
    {
        BeanReader reader = new BeanReader();
        reader.setXMLIntrospector(createXMLIntrospector());
        // register the class which maps to the root element
        // of the xml file (this depends on the NameMapper used.
        reader.registerBeanClass(PhysicalSchema.class);
        return reader;
    }

    /**
     * Set up the XMLIntroSpector
     */
    protected XMLIntrospector createXMLIntrospector()
    {
        XMLIntrospector introspector = new XMLIntrospector();

        // set elements for attributes to true
        introspector.getConfiguration().setAttributesForPrimitives(true);

        // Since we don't want to have collectionelements
        // line <DBMSS>, we have to set this to false,
        // since the default is true.
        introspector.getConfiguration().setWrapCollectionsInElement(false);

        // We have to use the HyphenatedNameMapper
        // Since we want the names to resolve from eg PhysicalSchema
        // to PHYSICAL_SCHEMA.
        // we pass to the mapper we want uppercase and use _ for name
        // seperation.
        // This will set our ElementMapper.
        introspector.getConfiguration().setElementNameMapper(new HyphenatedNameMapper(true, "_"));
        // since our attribute names will use a different
        // naming convention in our xml file (just all lowercase)
        // we set another mapper for the attributes
        introspector.getConfiguration().setAttributeNameMapper(new DecapitalizeNameMapper());

        return introspector;
    }

    /**
     * Opens a writer and writes an object model according to the
     * retrieved bean
     */
    private void write(Object bean, Writer out, boolean wrapCollectionsInElement)
    throws Exception
    {
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements( true );
        writer.setXMLIntrospector(createXMLIntrospector());
        // specifies weather to use collection elements or not.
        writer.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(wrapCollectionsInElement);
        // we don't want to write Id attributes to every element
        // we just want our opbject model written nothing more..
        writer.getBindingConfiguration().setMapIDs(false);
        // the source has 2 spaces indention and \n as line seperator.
        writer.setIndent("  ");
        writer.setEndOfLine("\n");
        writer.write(bean);
    }
}

