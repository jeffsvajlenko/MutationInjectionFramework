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
import org.apache.commons.attributes.test.samples.RuntimeSample;
import org.apache.commons.attributes.test.samples.Sample;
import junit.framework.TestCase;

public class RuntimeAttributesTestCase extends TestCase
{

    protected void collectionsEquals (Collection a, Collection b)
    {
        if (a.size () != b.size ())
        {
            fail ("A=" + a + " B=" + b);
        }

        Iterator iter = a.iterator ();
        while (iter.hasNext ())
        {
            Object o = iter.next ();
            if (!b.contains (o))
            {
                fail ("B does not contain " + o);
            }
        }

        iter = b.iterator ();
        while (iter.hasNext ())
        {
            Object o = iter.next ();
            if (!a.contains (o))
            {
                fail ("A does not contain " + o);
            }
        }
    }

    public void testRuntimeAttributesEqual () throws Exception
    {
        Class clazz1 = Sample.class;
        Class clazz2 = RuntimeSample.class;

        collectionsEquals (Attributes.getAttributes (clazz1), Attributes.getAttributes (clazz2));

        Method[] methods1 = clazz1.getDeclaredMethods ();

        for (int i = 0; i < methods1.length; i++)
        {
            Method m1 = methods1[i];
            Method m2 = clazz2.getDeclaredMethod (m1.getName (), m1.getParameterTypes ());

            collectionsEquals (Attributes.getAttributes (m1), Attributes.getAttributes (m2));

            int numParameters = m1.getParameterTypes().length;
            for (int j = 0; j < numParameters; j++)
            {
                collectionsEquals (Attributes.getParameterAttributes (m1, j), Attributes.getParameterAttributes (m2, j));
            }

            collectionsEquals (Attributes.getReturnAttributes (m1), Attributes.getReturnAttributes (m2));
        }

        Constructor[] ctors1 = clazz1.getDeclaredConstructors ();
        for (int i = 0; i < ctors1.length; i++)
        {
            Constructor c1 = ctors1[i];
            Constructor c2 = clazz2.getDeclaredConstructor (c1.getParameterTypes ());

            collectionsEquals (Attributes.getAttributes (c1), Attributes.getAttributes (c2));

            int numParameters = c1.getParameterTypes().length;
            for (int j = 0; j < numParameters; j++)
            {
                collectionsEquals (Attributes.getParameterAttributes (c1, j), Attributes.getParameterAttributes (c2, j));
            }
        }

        Field[] fields1 = clazz1.getDeclaredFields ();
        for (int i = 0; i < fields1.length; i++)
        {
            Field f1 = fields1[i];
            Field f2 = clazz2.getField (f1.getName ());
            collectionsEquals (Attributes.getAttributes (f1), Attributes.getAttributes (f2));
        }
    }
}
