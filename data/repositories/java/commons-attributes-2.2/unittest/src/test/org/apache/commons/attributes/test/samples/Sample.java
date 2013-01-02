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
package org.apache.commons.attributes.test.samples;

/**
 * @@ThreadSafe ()
 * @@Dependency ( SampleService.class, "sample" )
 */
public class Sample extends SuperSample implements SampleIFJoin
{

    /**
     * @@ThreadSafe ()
     */
    public Object field;

    public Object noAttributesInSubClass;

    /**
     * @@Dependency ( SampleService.class, "sample-some-method1" )
     */
    public void someMethod ()
    {

    }

    /**
     * @@.param2 ThreadSafe ()
     * @@.return Dependency ( SampleService.class, "sample-return" )
     */
    public Integer methodWithAttributes (int param1, int param2)
    {
        return null;
    }

    /**
     * @@Dependency ( SampleService.class, "sample-some-method2" )
     */
    public void someMethod (int parameter)
    {

    }

    /**
     * @@BeanAttribute ( 1, "a", anotherNumber = (56 - 14), name = "Smith, John \"Agent\"" )
     */
    public void methodWithNamedParameters ()
    {

    }

    public void methodWithNoAttributes ()
    {
    }

    /**
     * @@Dependency ( SampleService.class, "inner-sample" )
     */
    public static class InnerSample
    {
    }

    /**
     * @@Dependency ( SampleService.class, "sample-privateMethod" )
     */
    private void privateMethod ()
    {

    }
}
