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

import junit.framework.TestCase;

import org.apache.commons.attributes.Attributes;
import org.apache.commons.attributes.Inheritable;

public class OverrideTestCase extends TestCase
{

    /**
     * @@OverrideTestCase.OverrideAttribute(1)
     */
    public static class OverrideSuper
    {
    }

    /**
     * @@Inheritable()
     */
    public static class OverrideAttribute
    {

        private final int value;

        public OverrideAttribute (int value)
        {
            this.value = value;
        }

        public int getValue ()
        {
            return value;
        }

        public int hashCode ()
        {
            return 1;
        }

        public boolean equals (Object o)
        {
            return o instanceof OverrideAttribute;
        }

        public String toString ()
        {
            return "OverrideAttribute: " + value;
        }
    }

    /**
     * @@OverrideTestCase.OverrideAttribute(2)
     */
    public static class OverrideDerived extends OverrideSuper
    {
    }

    public void testOverride () throws Exception
    {
        System.out.println (Attributes.getAttributes (OverrideDerived.class));
        OverrideAttribute attr = (OverrideAttribute)
                                 Attributes.getAttribute (OverrideDerived.class, OverrideAttribute.class);

        assertEquals (2, attr.getValue ());
    }
}

