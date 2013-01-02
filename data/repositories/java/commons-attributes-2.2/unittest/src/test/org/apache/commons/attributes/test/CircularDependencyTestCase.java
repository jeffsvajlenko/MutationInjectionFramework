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
import org.apache.commons.attributes.CircularDependencyError;

public class CircularDependencyTestCase extends TestCase
{

    /**
     * @@CircularDependencyTestCase.AttributeTwo()
     */
    public static class AttributeOne {}

    public static class AttributeTwo extends AttributeOne {}

    public void testCircularDependencies () throws Exception
    {
        try
        {
            // This should fail, because when loading attributes for Two,
            // the runtime has to get inheritable attributes from One.
            // This means it has to check if Two is Inheritable. But it
            // is already loading Two -> Circular dependency.
            Attributes.getAttributes (AttributeTwo.class);

            fail ();
        }
        catch (CircularDependencyError cde)
        {
            // OK.
            assertEquals ("org.apache.commons.attributes.test.CircularDependencyTestCase$AttributeTwo:" +
                          "org.apache.commons.attributes.test.CircularDependencyTestCase$AttributeTwo -> " +
                          "org.apache.commons.attributes.test.CircularDependencyTestCase$AttributeOne -> " +
                          "org.apache.commons.attributes.test.CircularDependencyTestCase$AttributeTwo", cde.getMessage ());
        }
    }
}
