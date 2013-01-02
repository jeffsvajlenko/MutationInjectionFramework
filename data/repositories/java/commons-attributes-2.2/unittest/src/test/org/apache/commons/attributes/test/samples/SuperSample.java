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
 * @@Dependency ( SampleService.class, "super-sample" )
 */
public class SuperSample
{

    /**
     * @@ThreadSafe ()
     * @@Dependency ( SampleService.class, "super-field" )
     */
    public Object field;

    /**
     * @@Dependency ( SampleService.class, "super-noattrs" )
     */
    public Object noAttributesInSubClass;

    /**
     * @@Dependency ( SampleService.class, "sample-ctor1" )
     */
    public SuperSample ()
    {

    }

    /**
     * @@Dependency ( SampleService.class, "sample-ctor2" )
     * @@.array ThreadSafe()
     */
    public SuperSample (String input, String[][] array)
    {

    }

    /**
     * @@Dependency ( SampleService.class, "super-some-method-sample" )
     * @@ThreadSafe ()
     */
    public void someMethod (int parameter)
    {

    }

    /**
     * @@Dependency ( SampleService.class, "super-privateMethod" )
     */
    private void privateMethod ()
    {

    }

}
