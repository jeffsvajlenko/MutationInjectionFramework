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

import junit.framework.TestCase;

import org.apache.commons.attributes.Attributes;
import org.apache.commons.attributes.InvalidAttributeTargetError;

public class InnerClassTestCase extends TestCase
{

    public static class AnAttribute {}

    public static class Internal
    {
    }

    public static class InnerClassSample
    {

        public static class InternalOfSample
        {
        }

        /**
         * @@InnerClassTestCase.AnAttribute()
         */
        public void method (Internal i)
        {
        }

        /**
         * @@InnerClassTestCase.AnAttribute()
         */
        public void method2 (InternalOfSample i)
        {
        }

        /**
         * @@InnerClassTestCase.AnAttribute()
         */
        public Internal field;

        /**
         * @@InnerClassTestCase.AnAttribute()
         */
        public InternalOfSample field2;

    }

    public void testInnerClassAttributesOnMethod () throws Exception
    {
        Class sample = InnerClassSample.class;
        Method method = sample.getMethod ("method", new Class[] { Internal.class });
        assertTrue (Attributes.getAttributes (method, AnAttribute.class).size () > 0);
    }

    public void testInnerClassAttributesOnMethod2 () throws Exception
    {
        Class sample = InnerClassSample.class;
        Method method2 = sample.getMethod ("method2", new Class[] { InnerClassSample.InternalOfSample.class });
        assertTrue (Attributes.getAttributes (method2, AnAttribute.class).size () > 0);
    }

    public void testInnerClassAttributesOnField () throws Exception
    {
        Class sample = InnerClassSample.class;
        Field field = sample.getField ("field");
        assertTrue (Attributes.getAttributes (field, AnAttribute.class).size () > 0);
    }

    public void testInnerClassAttributesOnField2 () throws Exception
    {
        Class sample = InnerClassSample.class;
        Field field2 = sample.getField ("field2");
        assertTrue (Attributes.getAttributes (field2, AnAttribute.class).size () > 0);
    }
}
