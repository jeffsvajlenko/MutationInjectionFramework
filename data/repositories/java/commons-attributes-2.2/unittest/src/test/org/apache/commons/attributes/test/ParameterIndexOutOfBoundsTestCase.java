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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.apache.commons.attributes.Attributes;
import org.apache.commons.attributes.ParameterIndexOutOfBoundsException;

public class ParameterIndexOutOfBoundsTestCase extends TestCase
{

    public static class Attr {}

    public static class Inner
    {
        /**
         * @@ParameterIndexOutOfBoundsTestCase.Attr()
         */
        public Inner (int arg1) {}
        public void method (int arg1, int arg2) {}
    }

    private Method m;
    private Constructor c;

    public void setUp () throws Exception
    {
        m = Inner.class.getMethod ("method", new Class[] { Integer.TYPE, Integer.TYPE });
        c = Inner.class.getConstructor (new Class[] { Integer.TYPE });
    }

    public void testMethodInRange () throws Exception
    {
        Attributes.getParameterAttributes (m, 0);
        Attributes.getParameterAttributes (m, 1);
    }

    public void testConstructorInRange () throws Exception
    {
        Attributes.getParameterAttributes (c, 0);
    }

    public void testMethodOutOfRange () throws Exception
    {
        try
        {
            Attributes.getParameterAttributes (m, -1);

            fail ();
        }
        catch (ParameterIndexOutOfBoundsException e)
        {
            assertEquals ("-1", e.getMessage ());
        }

        try
        {
            Attributes.getParameterAttributes (m, 2);

            fail ();
        }
        catch (ParameterIndexOutOfBoundsException e)
        {
            assertEquals ("2. public void org.apache.commons.attributes.test.ParameterIndexOutOfBoundsTestCase$Inner.method(int,int) has 2 parameters.", e.getMessage ());
        }
    }

    public void testConstructorOutOfRange () throws Exception
    {
        try
        {
            Attributes.getParameterAttributes (c, -1);

            fail ();
        }
        catch (ParameterIndexOutOfBoundsException e)
        {
            assertEquals ("-1", e.getMessage ());
        }

        try
        {
            Attributes.getParameterAttributes (c, 1);

            fail ();
        }
        catch (ParameterIndexOutOfBoundsException e)
        {
            assertEquals ("1. public org.apache.commons.attributes.test.ParameterIndexOutOfBoundsTestCase$Inner(int) has 1 parameters.", e.getMessage ());
        }
    }

}
