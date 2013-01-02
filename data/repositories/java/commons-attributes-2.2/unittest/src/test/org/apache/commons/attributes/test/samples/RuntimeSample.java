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

import org.apache.commons.attributes.Attributes;
import org.apache.commons.attributes.RuntimeAttributeRepository;

public class RuntimeSample extends SuperSample implements SampleIFJoin
{

    static
    {
        try
        {
            RuntimeAttributeRepository rar = new RuntimeAttributeRepository (RuntimeSample.class);
            rar.addClassAttribute (new ThreadSafe ());
            rar.addClassAttribute (new Dependency ( SampleService.class, "sample" ));

            rar.addFieldAttribute ("field", new ThreadSafe ());

            rar.addMethodAttribute ("someMethod", new Class[] {}, new Dependency ( SampleService.class, "sample-some-method1" ));

            rar.addParameterAttribute ("methodWithAttributes", new Class[] { Integer.TYPE, Integer.TYPE }, 1, new ThreadSafe ());
            rar.addReturnAttribute ("methodWithAttributes", new Class[] { Integer.TYPE, Integer.TYPE }, new Dependency ( SampleService.class, "sample-return" ));

            rar.addMethodAttribute ("someMethod", new Class[] { Integer.TYPE }, new Dependency ( SampleService.class, "sample-some-method2" ));

            BeanAttribute ba = new BeanAttribute (1, "a");
            ba.setAnotherNumber (56 - 14);
            ba.setName ("Smith, John \"Agent\"");
            rar.addMethodAttribute ("methodWithNamedParameters", new Class[] {}, ba);

            rar.addMethodAttribute ("privateMethod", new Class[] {}, new Dependency ( SampleService.class, "sample-privateMethod" ));

            Attributes.setAttributes (rar);
        }
        catch (Exception e)
        {
            throw new Error ("Unable to set attribute information: " + e.toString ());
        }
    }

    public Object field;

    public Object noAttributesInSubClass;

    public void someMethod ()
    {

    }

    public Integer methodWithAttributes (int param1, int param2)
    {
        return null;
    }

    public void someMethod (int parameter)
    {

    }

    public void methodWithNamedParameters ()
    {

    }

    public void methodWithNoAttributes ()
    {
    }

    private void privateMethod ()
    {
    }

    public static class InnerSample
    {
        static
        {
            try
            {
                RuntimeAttributeRepository rar = new RuntimeAttributeRepository (RuntimeSample.InnerSample.class);
                rar.addClassAttribute (new Dependency ( SampleService.class, "inner-sample" ));
                Attributes.setAttributes (rar);
            }
            catch (Exception e)
            {
                throw new Error ("Unable to set attribute information: " + e.toString ());
            }
        }
    }
}
