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

package org.apache.commons.betwixt.io.read;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.LibraryBeanWithArraySetter;

/**
 * Test harness for ReadContext
 *
 * @author Robert Burrell Donkin
 * @version $Id: TestReadContext.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestReadContext extends AbstractTestCase
{

    public TestReadContext(String name)
    {
        super(name);
    }

    public static Test suite()
    {
        return new TestSuite(TestReadContext.class);
    }

    public void testElementStackPushPop() throws Exception
    {
        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());
        context.pushElement("alpha");
        assertEquals("Push then pop", "alpha", context.popElement());
        assertEquals("Push then pop at bottom", null, context.popElement());

        context.pushElement("beta");
        context.pushElement("delta");
        context.pushElement("gamma");
        assertEquals("Triple push (1)", "gamma", context.popElement());
        assertEquals("Triple push (2)", "delta", context.popElement());
        assertEquals("Triple push (3)", "beta", context.popElement());
        assertEquals("Triple push at bottom", null, context.popElement());

    }

    public void testElementStackMarkedPushPop() throws Exception
    {
        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());

        context.pushElement("beta");
        context.pushElement("delta");
        context.markClassMap(Object.class);
        context.pushElement("gamma");
        assertEquals("One mark (1)", "gamma", context.popElement());
        assertEquals("One mark (2)", "delta", context.popElement());
        assertEquals("One mark (3)", "beta", context.popElement());
        assertEquals("One mark at bottom", null, context.popElement());

        context.markClassMap(Object.class);
        context.pushElement("beta");
        context.pushElement("delta");
        context.markClassMap(Object.class);
        context.pushElement("gamma");
        context.markClassMap(Object.class);
        assertEquals("Three marks (1)", "gamma", context.popElement());
        assertEquals("Three marks (2)", "delta", context.popElement());
        assertEquals("Three marks (3)", "beta", context.popElement());
        assertEquals("Three marks at bottom", null, context.popElement());
    }

    public void testLastMappedClassNoClass() throws Exception
    {
        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());
        context.pushElement("beta");
        context.pushElement("delta");
        context.pushElement("gamma");
        assertEquals("No class", null, context.getLastMappedClass());
    }

    public void testGetCurrentElement() throws Exception
    {
        ReadContext context = new ReadContext(new BindingConfiguration(), new ReadConfiguration());
        context.pushElement("element");
        context.markClassMap(String.class);
        assertEquals("Current element: ", "element", context.getCurrentElement());
    }

    public void testLastMappedClassBottomClass() throws Exception
    {
        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());

        context.markClassMap(Object.class);
        context.pushElement("beta");
        context.pushElement("delta");
        context.pushElement("gamma");
        assertEquals("One classes", Object.class, context.getLastMappedClass());
    }

    public void testLastMappedClassTwoClasses() throws Exception
    {

        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());
        context.markClassMap(Object.class);
        context.pushElement("beta");
        context.pushElement("delta");
        context.markClassMap(String.class);
        context.pushElement("gamma");
        assertEquals("Two classes", String.class, context.getLastMappedClass());
    }

    public void testLastMappedClassTopClass() throws Exception
    {
        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());
        context.markClassMap(Object.class);
        context.pushElement("beta");
        context.pushElement("delta");
        context.markClassMap(String.class);
        context.pushElement("gamma");
        context.markClassMap(Integer.class);
        assertEquals("Top class", Integer.class, context.getLastMappedClass());
    }


    public void testNullElementNameMatchesAll() throws Exception
    {

        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());

        context.pushElement("LibraryBeanWithArraySetter");
        context.markClassMap(LibraryBeanWithArraySetter.class);
        context.pushElement("books");
        context.pushElement("whatever");
        assertNotNull("Null name should match any new element", context.getCurrentDescriptor());
    }


    /* Sad to say that the method tested has had to be made private.
     * Maybe would be good to find a way to test the
        public void testRelativeElementPathBase()
        {
            ReadContext context = new ReadContext(
                        new BindingConfiguration(),
                        new ReadConfiguration());
            ArrayList elements = new ArrayList();

            context.pushElement("alpha");
            context.markClassMap(Object.class);
            context.pushElement("beta");
            context.pushElement("delta");
            context.pushElement("gamma");
        	CollectionUtils.addAll(elements, context.getRelativeElementPathIterator());

            assertEquals("Path element count (1)", 3 , elements.size());
            assertEquals("Element name 0", "beta", elements.get(0));
            assertEquals("Element name 1", "delta", elements.get(1));
            assertEquals("Element name 2", "gamma", elements.get(2));
        }


        public void testRelativeElementPathTwoMarks()
        {
            ReadContext context = new ReadContext(
                        new BindingConfiguration(),
                        new ReadConfiguration());
            ArrayList elements = new ArrayList();

            context.pushElement("alpha");
            context.markClassMap(Object.class);
            context.pushElement("beta");
            context.pushElement("delta");
            context.markClassMap(Object.class);
            context.pushElement("gamma");
        	CollectionUtils.addAll(elements, context.getRelativeElementPathIterator());

            assertEquals("Path element count (1)", 1 , elements.size());
            assertEquals("Element name", "gamma", elements.get(0));
        }


        public void testRelativeElementPathTopMark()
        {
            ReadContext context = new ReadContext(
                        new BindingConfiguration(),
                        new ReadConfiguration());
            ArrayList elements = new ArrayList();

            context.pushElement("alpha");
            context.pushElement("beta");
            context.pushElement("delta");
            context.pushElement("gamma");
            context.markClassMap(Object.class);
        	CollectionUtils.addAll(elements, context.getRelativeElementPathIterator());

            assertEquals("Path element count (0)", 0 , elements.size());
        }

        public void testRelativeElementPathRootMark()
        {
            ReadContext context = new ReadContext(
                        new BindingConfiguration(),
                        new ReadConfiguration());
            ArrayList elements = new ArrayList();

            context.markClassMap(Object.class);
            context.pushElement("alpha");
            context.pushElement("beta");
            context.pushElement("delta");
            context.pushElement("gamma");
        	CollectionUtils.addAll(elements, context.getRelativeElementPathIterator());

            assertEquals("Path element count (4)", 4 , elements.size());
            assertEquals("Element name (0)", "alpha", elements.get(0));
            assertEquals("Element name (1)", "beta", elements.get(1));
            assertEquals("Element name (2)", "delta", elements.get(2));
            assertEquals("Element name (3)", "gamma", elements.get(3));

        }

        public void testRelativeElementPathNoMark()
        {
            ReadContext context = new ReadContext(
                        new BindingConfiguration(),
                        new ReadConfiguration());
            ArrayList elements = new ArrayList();

            context.pushElement("alpha");
            context.pushElement("beta");
            context.pushElement("delta");
            context.pushElement("gamma");
        	CollectionUtils.addAll(elements, context.getRelativeElementPathIterator());

            assertEquals("Path element count (4)", 4 , elements.size());
            assertEquals("Element name (0)", "alpha", elements.get(0));
            assertEquals("Element name (1)", "beta", elements.get(1));
            assertEquals("Element name (2)", "delta", elements.get(2));
            assertEquals("Element name (3)", "gamma", elements.get(3));

        }
        */
}
