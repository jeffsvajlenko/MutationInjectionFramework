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
import org.apache.commons.attributes.DefaultSealable;
import org.apache.commons.attributes.SealedAttributeException;
import junit.framework.TestCase;

public class SealableTestCase extends TestCase
{

    /**
     * A sample attribute with bean-like properties. Used to test the
     * named parameter syntax.
     */
    public static class SampleSealableAttribute extends DefaultSealable
    {

        private int number;

        public SampleSealableAttribute ()
        {
        }

        public int getNumber ()
        {
            return number;
        }

        public void setNumber (int number)
        {
            checkSealed ();
            this.number = number;
        }
    }

    /** @@SealableTestCase.SampleSealableAttribute (number=1) */
    public static class ClassWithSealable {}

    public void testSealable () throws Exception
    {
        SampleSealableAttribute attribute = (SampleSealableAttribute) Attributes.getAttribute (ClassWithSealable.class, SampleSealableAttribute.class);

        try
        {
            attribute.setNumber (11);
            fail ("Attribute should be sealed!");
        }
        catch (IllegalStateException ise)
        {
            // -- OK, attribute should be sealed.
        }

        assertEquals (1, attribute.getNumber ());
    }

    public void testSealableExceptionType () throws Exception
    {
        SampleSealableAttribute attribute = (SampleSealableAttribute) Attributes.getAttribute (ClassWithSealable.class, SampleSealableAttribute.class);

        try
        {
            attribute.setNumber (11);
            fail ("Attribute should be sealed!");
        }
        catch (SealedAttributeException ise)
        {
            // -- OK, attribute should be sealed and throw a SealedAttributeException.
        }
    }
}
