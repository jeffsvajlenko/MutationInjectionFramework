/*
 * Copyright 2003-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.attributes.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.attributes.Attributes;
import org.apache.commons.attributes.AttributeIndex;
import org.apache.commons.attributes.test.samples.*;
import junit.framework.TestCase;

public class AttributesTestCase extends TestCase
{

    public void testClassAttributes () throws Exception
    {

        /**
         * @Dependency ( SampleService.class, "super-sample" )
         */
        Class c = SuperSample.class;
        assertEquals (1, Attributes.getAttributes (c).size ());
        assertEquals (1, Attributes.getAttributes (c, Dependency.class).size ());
        assertTrue (Attributes.hasAttributeType (c, Dependency.class));
        assertTrue (Attributes.hasAttribute (c, new Dependency ( SampleService.class, "super-sample" )));
    }

    public void testMethodAttributes () throws Exception
    {
        /**
         * @Dependency ( SampleService.class, "super-some-method-sample" )
         * @ThreadSafe ()
         */
        Method m = SuperSample.class.getMethod ("someMethod", new Class[] { Integer.TYPE });
        assertEquals (2, Attributes.getAttributes (m).size ());
        assertEquals (1, Attributes.getAttributes (m, Dependency.class).size ());
        assertEquals (1, Attributes.getAttributes (m, ThreadSafe.class).size ());
        assertTrue (Attributes.hasAttributeType (m, Dependency.class));
        assertTrue (Attributes.hasAttributeType (m, ThreadSafe.class));
        assertTrue (Attributes.hasAttribute (m, new Dependency ( SampleService.class, "super-some-method-sample" )));
        assertTrue (Attributes.hasAttribute (m, new ThreadSafe ()));
    }

    public void testFieldAttributes () throws Exception
    {
        /**
         * @ThreadSafe ()
         * @Dependency ( SampleService.class, "super-field" )
         */
        Field f = SuperSample.class.getField ("field");
        assertEquals (2, Attributes.getAttributes (f).size ());
        assertEquals (1, Attributes.getAttributes (f, ThreadSafe.class).size ());
        assertEquals (1, Attributes.getAttributes (f, Dependency.class).size ());
        assertTrue (Attributes.hasAttribute (f, new ThreadSafe ()));
        assertTrue (Attributes.hasAttribute (f, new Dependency ( SampleService.class, "super-field" ) ));
        assertTrue (Attributes.hasAttributeType (f, ThreadSafe.class));
        assertTrue (Attributes.hasAttributeType (f, Dependency.class));
    }

    public void testDefaultConstructorAttributes () throws Exception
    {
        /**
         * @Dependency ( SampleService.class, "sample-ctor1" )
         */
        Constructor c = SuperSample.class.getDeclaredConstructor (new Class[0]);
        assertEquals (1, Attributes.getAttributes (c).size ());
        assertEquals (1, Attributes.getAttributes (c, Dependency.class).size ());
        assertTrue (Attributes.hasAttributeType (c, Dependency.class));
        assertTrue (Attributes.hasAttribute (c, new Dependency ( SampleService.class, "sample-ctor1" )));
    }

    public void testConstructorAttributes () throws Exception
    {
        /**
         * @Dependency ( SampleService.class, "sample-ctor2" )
         */
        Constructor c = SuperSample.class.getDeclaredConstructor (new Class[] { String.class, (new String[0][0]).getClass () } );
        assertEquals (1, Attributes.getAttributes (c).size ());
        assertEquals (1, Attributes.getAttributes (c, Dependency.class).size ());
        assertTrue (Attributes.hasAttributeType (c, Dependency.class));
        assertTrue (Attributes.hasAttribute (c, new Dependency ( SampleService.class, "sample-ctor2" )));

        assertEquals (1, Attributes.getParameterAttributes (c, 1).size ());
        assertEquals (1, Attributes.getParameterAttributes (c, 1, ThreadSafe.class).size ());
        assertTrue (Attributes.hasParameterAttributeType (c, 1, ThreadSafe.class));
        assertTrue (Attributes.hasParameterAttribute (c, 1, new ThreadSafe ()));
    }

    public void testParameterAndReturnAttributes () throws Exception
    {
        Method m = Sample.class.getMethod ("methodWithAttributes", new Class[] { Integer.TYPE, Integer.TYPE });
        assertEquals (0, Attributes.getAttributes (m).size ());
        assertEquals (2, Attributes.getReturnAttributes (m).size ());
        assertTrue (Attributes.hasReturnAttribute (m, new Dependency ( SampleService.class, "sample-return" ) ));
        assertTrue (Attributes.hasReturnAttribute (m, new Dependency ( SampleService.class, "sample-if-return" ) ));

        assertEquals (0, Attributes.getParameterAttributes (m, 0).size ());
        assertEquals (2, Attributes.getParameterAttributes (m, 1).size ());
        assertTrue (Attributes.hasParameterAttribute (m, 1, new Dependency ( SampleService.class, "sample-if-param-2" ) ));
        assertTrue (Attributes.hasParameterAttribute (m, 1, new ThreadSafe () ));
    }

    public void testNoAttributes () throws Exception
    {
        Method m = Sample.class.getMethod ("methodWithNoAttributes", new Class[0]);
        assertEquals (0, Attributes.getAttributes (m).size ());
    }

    /**
     * Ensure that loading a class with the same name from two different class loaders
     * won't mess up the attribute cache.
     */
    public void testClassLoaderKeying () throws Exception
    {
        URLClassLoader cl1 = new URLClassLoader (new URL[] {new File ("target/cl1/").toURL ()}, getClass().getClassLoader ());
        URLClassLoader cl2 = new URLClassLoader (new URL[] {new File ("target/cl2/").toURL ()}, getClass().getClassLoader ());

        Class cl1Class = cl1.loadClass ("TestClass");
        Class cl2Class = cl2.loadClass ("TestClass");

        assertEquals ("[[TestAttribute 1]]", Attributes.getAttributes (cl1Class).toString ());
        assertEquals ("[[TestAttribute TestClass]]", Attributes.getAttributes (cl2Class).toString ());
    }

    public void testInnerClasses () throws Exception
    {
        Class c = Sample.InnerSample.class;
        assertEquals (1, Attributes.getAttributes (c).size ());
        assertEquals (1, Attributes.getAttributes (c, Dependency.class).size ());
        assertTrue (Attributes.hasAttributeType (c, Dependency.class));
        assertTrue (Attributes.hasAttribute (c, new Dependency ( SampleService.class, "inner-sample" )));
    }

    public void testNamedParameters () throws Exception
    {
        Method m = Sample.class.getMethod ("methodWithNamedParameters", new Class[] { });
        assertEquals (1, Attributes.getAttributes (m).size ());
        assertEquals (1, Attributes.getAttributes (m, BeanAttribute.class).size ());
        assertTrue (Attributes.hasAttributeType (m, BeanAttribute.class));
        BeanAttribute ba = new BeanAttribute (1, "a");
        ba.setName ("Smith, John \"Agent\"");
        ba.setAnotherNumber (42);
        assertTrue (Attributes.hasAttribute (m, ba));
    }
}
