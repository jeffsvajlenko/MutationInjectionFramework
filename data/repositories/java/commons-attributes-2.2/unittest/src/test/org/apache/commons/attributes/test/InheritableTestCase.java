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

public class InheritableTestCase extends TestCase
{

    public static interface SampleService {}

    /**
     * @@InheritableTestCase.InheritableAttribute ("super-sample")
     */
    public static class SuperClass
    {

        /**
         * @@InheritableTestCase.NonInheritableAttribute ()
         * @@InheritableTestCase.InheritableAttribute ( "super-field" )
         */
        public Object field;

        /**
         * @@InheritableTestCase.InheritableAttribute ( "super-noattrs" )
         */
        public Object noAttributesInSubClass;

        /**
         * @@InheritableTestCase.InheritableAttribute ( "sample-ctor1" )
         */
        public SuperClass ()
        {

        }

        /**
         * @@InheritableTestCase.InheritableAttribute( "sample-ctor2" )
         * @@.array InheritableTestCase.NonInheritableAttribute()
         */
        public SuperClass (String input, String[][] array)
        {

        }

        /**
         * @@InheritableTestCase.InheritableAttribute( "super-some-method-sample" )
         * @@InheritableTestCase.NonInheritableAttribute()
         */
        public void someMethod (int parameter)
        {

        }

        /**
         * @@InheritableTestCase.InheritableAttribute( "super-privateMethod" )
         */
        private void privateMethod ()
        {

        }

    }

    public static interface JoinedInterface extends InterfaceOne, InterfaceTwo
    {
    }

    /**
     * @@InheritableTestCase.InheritableAttribute( "sample-if-2-c" )
     */
    public static interface InterfaceTwo
    {

        /**
         * @@InheritableTestCase.InheritableAttribute( "sample-if-2" )
         * @@InheritableTestCase.NonInheritableAttribute()
         */
        public void someMethod (int parameter);
    }

    /**
     * @@InheritableTestCase.InheritableAttribute( "sample-if-1-c" )
     */
    public static interface InterfaceOne
    {

        /**
         * @@InheritableTestCase.InheritableAttribute( "sample-if-1" )
         * @@InheritableTestCase.NonInheritableAttribute()
         */
        public void someMethod (int parameter);

        /**
         * @@.return InheritableTestCase.InheritableAttribute("sample-if-return" )
         * @@.param2 InheritableTestCase.InheritableAttribute("sample-if-param-2" )
         */
        public Integer methodWithAttributes (int param1, int param2);
    }

    /**
     * @@org.apache.commons.attributes.Inheritable()
     */
    public static class InheritableAttribute
    {
        private final String name;
        public InheritableAttribute(String name)
        {
            this.name = name;
        }
        public String getInheritableAttributeName ()
        {
            return name;
        }

        public boolean equals (Object o)
        {
            return o instanceof InheritableAttribute &&
                   ((InheritableAttribute) o).name.equals (name);
        }
        public int hashCode ()
        {
            return name.hashCode ();
        }
        public String toString ()
        {
            return "[InheritableAttribute \"" + name + "\"]";
        }
    }

    public static class NonInheritableAttribute
    {
        public NonInheritableAttribute() {}
        public boolean equals (Object o)
        {
            return o instanceof NonInheritableAttribute;
        }
        public int hashCode ()
        {
            return 0;
        }
        public String toString ()
        {
            return "[NonInheritableAttribute]";
        }
    }

    /** @@InheritableTestCase.NonInheritableAttribute()
     * @@InheritableTestCase.InheritableAttribute( "sample" ) */
    public static class Sample extends InheritableTestCase.SuperClass implements JoinedInterface
    {
        /** @@InheritableTestCase.NonInheritableAttribute() */
        public Object field;
        public Object noAttributesInSubClass;
        /** @@InheritableTestCase.InheritableAttribute( "sample-some-method1" ) */
        public void someMethod () {}
        /** @@InheritableTestCase.InheritableAttribute( "sample-some-method2" ) */
        public void someMethod (int parameter) {}
        /** @@.param2 InheritableTestCase.NonInheritableAttribute ()
         * @@.return InheritableTestCase.InheritableAttribute("sample-return") */
        public Integer methodWithAttributes (int param1, int param2)
        {
            return null;
        }
        public void methodWithNoAttributes () {}
        /** @@InheritableTestCase.InheritableAttribute( "inner-sample" ) */
        public static class InnerSample {}
        /** @@InheritableTestCase.InheritableAttribute( "sample-privateMethod" ) */
        private void privateMethod () {}
    }

    public void testClassInheritance () throws Exception
    {
        Class c = Sample.class;
        assertEquals (5, Attributes.getAttributes (c).size ());
        assertEquals (4, Attributes.getAttributes (c, InheritableAttribute.class).size ());
        assertTrue (Attributes.hasAttributeType (c, InheritableAttribute.class));
        assertTrue (Attributes.hasAttributeType (c, NonInheritableAttribute.class));
        assertTrue (Attributes.hasAttribute (c, new InheritableAttribute( "sample" )));
        assertTrue (Attributes.hasAttribute (c, new InheritableAttribute( "super-sample" )));
        assertTrue (Attributes.hasAttribute (c, new InheritableAttribute( "sample-if-1-c" )));
        assertTrue (Attributes.hasAttribute (c, new InheritableAttribute( "sample-if-2-c" )));
        assertTrue (Attributes.hasAttribute (c, new NonInheritableAttribute()));
    }

    public void testMethodInheritance () throws Exception
    {
        Method m = Sample.class.getMethod ("someMethod", new Class[] { Integer.TYPE });
        assertEquals (4, Attributes.getAttributes (m).size ());
        assertEquals (4, Attributes.getAttributes (m, InheritableAttribute.class).size ());
        assertTrue (Attributes.hasAttributeType (m, InheritableAttribute.class));
        assertTrue (Attributes.hasAttribute (m, new InheritableAttribute( "super-some-method-sample" )));
        assertTrue (Attributes.hasAttribute (m, new InheritableAttribute( "sample-some-method2" ) ));
        assertTrue (Attributes.hasAttribute (m, new InheritableAttribute( "sample-if-1" ) ));
        assertTrue (Attributes.hasAttribute (m, new InheritableAttribute( "sample-if-2" ) ));
    }

    public void testPrivateMethodNonInheritance () throws Exception
    {
        Method m = Sample.class.getDeclaredMethod ("privateMethod", new Class[] {});
        assertEquals (1, Attributes.getAttributes (m).size ());
        assertEquals (1, Attributes.getAttributes (m, InheritableAttribute.class).size ());
        assertTrue (Attributes.hasAttributeType (m, InheritableAttribute.class));
        assertTrue (Attributes.hasAttribute (m, new InheritableAttribute( "sample-privateMethod" ) ));

        m = SuperClass.class.getDeclaredMethod ("privateMethod", new Class[] {});
        assertEquals (1, Attributes.getAttributes (m).size ());
        assertEquals (1, Attributes.getAttributes (m, InheritableAttribute.class).size ());
        assertTrue (Attributes.hasAttributeType (m, InheritableAttribute.class));
        assertTrue (Attributes.hasAttribute (m, new InheritableAttribute( "super-privateMethod" ) ));
    }

    public void testFieldNonInheritance () throws Exception
    {
        Field f = SuperClass.class.getField ("field");
        assertEquals (2, Attributes.getAttributes (f).size ());
        assertEquals (1, Attributes.getAttributes (f, NonInheritableAttribute.class).size ());
        assertEquals (1, Attributes.getAttributes (f, InheritableAttribute.class).size ());
        assertTrue (Attributes.hasAttributeType (f, NonInheritableAttribute.class));
        assertTrue (Attributes.hasAttributeType (f, InheritableAttribute.class));
        assertTrue (Attributes.hasAttribute (f, new NonInheritableAttribute()));
        assertTrue (Attributes.hasAttribute (f, new InheritableAttribute( "super-field" )));

        f = Sample.class.getField ("field");
        assertEquals (1, Attributes.getAttributes (f).size ());
        assertEquals (1, Attributes.getAttributes (f, NonInheritableAttribute.class).size ());
        assertTrue (Attributes.hasAttributeType (f, NonInheritableAttribute.class));
        assertTrue (Attributes.hasAttribute (f, new NonInheritableAttribute()));

        f = SuperClass.class.getField ("noAttributesInSubClass");
        assertEquals (1, Attributes.getAttributes (f).size ());
        assertEquals (1, Attributes.getAttributes (f, InheritableAttribute.class).size ());
        assertTrue (Attributes.hasAttribute (f, new InheritableAttribute( "super-noattrs" )));
        assertTrue (Attributes.hasAttributeType (f, InheritableAttribute.class));

        f = Sample.class.getField ("noAttributesInSubClass");
        assertEquals (0, Attributes.getAttributes (f).size ());
        assertEquals (0, Attributes.getAttributes (f, InheritableAttribute.class).size ());
        assertTrue (!Attributes.hasAttribute (f, new InheritableAttribute( "super-noattrs" )));
        assertTrue (!Attributes.hasAttributeType (f, InheritableAttribute.class));
    }
}
