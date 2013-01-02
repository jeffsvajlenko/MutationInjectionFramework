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
import junit.framework.TestCase;

public class AttributeIndexTestCase extends TestCase
{

    private URLClassLoader cl = null;
    private AttributeIndex index = null;
    private Class TESTCLASS = null;
    private Class TESTCLASS_INNER = null;
    private Class TESTATTRIBUTE = null;

    public void setUp () throws Exception
    {
        cl = new URLClassLoader (new URL[] {new File ("target/cl2/cl2.jar").toURL ()}, getClass().getClassLoader ());
        index = new AttributeIndex (cl);
        TESTCLASS = cl.loadClass ("TestClass");
        TESTCLASS_INNER = cl.loadClass ("TestClass$Inner");
        TESTATTRIBUTE = cl.loadClass ("TestAttribute");
    }

    public void testAttributeIndexCompatible () throws Exception
    {
        Collection classes = index.getClassesWithAttribute ("TestAttribute");
        System.out.println (classes);
        assertEquals (2, classes.size ());
        assertTrue (classes.contains ("TestClass"));
        assertTrue (classes.contains ("TestClass.Inner"));
    }

    public void testClasses () throws Exception
    {
        Collection classes = index.getClasses (TESTATTRIBUTE);
        System.out.println (classes);
        assertEquals (2, classes.size ());
        assertTrue (classes.contains (TESTCLASS));
        assertTrue (classes.contains (TESTCLASS_INNER));
    }

    public void testMethods () throws Exception
    {
        Collection methods = index.getMethods (TESTATTRIBUTE);
        System.out.println (methods);
        assertEquals (1, methods.size ());
        assertTrue (methods.contains (TESTCLASS.getDeclaredMethods()[0]));
    }

    public void testConstructors () throws Exception
    {
        Collection ctors = index.getConstructors (TESTATTRIBUTE);
        System.out.println (ctors);
        assertEquals (1, ctors.size ());
        assertTrue (ctors.contains (TESTCLASS.getDeclaredConstructors()[0]));
    }

    public void testConstructorParameters () throws Exception
    {
        Collection ctors = index.getConstructorParameters (TESTATTRIBUTE);
        System.out.println (ctors);
        assertEquals (1, ctors.size ());
        assertTrue (ctors.contains (new AttributeIndex.ConstructorParameter (TESTCLASS.getDeclaredConstructors()[0], 0)));
    }

    public void testMethodParameters () throws Exception
    {
        Collection methods = index.getMethodParameters (TESTATTRIBUTE);
        System.out.println (methods);
        assertEquals (1, methods.size ());
        assertTrue (methods.contains (new AttributeIndex.MethodParameter (TESTCLASS.getDeclaredMethods()[0], 0)));
    }

    public void testMethodsReturning () throws Exception
    {
        Collection methods = index.getMethodsReturning (TESTATTRIBUTE);
        System.out.println (methods);
        assertEquals (1, methods.size ());
        assertTrue (methods.contains (TESTCLASS.getDeclaredMethods()[0]));
    }

    public void testFields () throws Exception
    {
        Collection fields = index.getFields (TESTATTRIBUTE);
        System.out.println (fields);
        assertEquals (1, fields.size ());
        assertTrue (fields.contains (TESTCLASS.getDeclaredFields()[0]));
    }
}
