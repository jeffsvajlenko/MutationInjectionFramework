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
 * Declares a dependency.
 *
 * @@org.apache.commons.attributes.Inheritable()
 */
public class Dependency
{

    private final Class clazz;
    private final String name;

    public Dependency (Class clazz, String name)
    {
        this.clazz = clazz;
        this.name = name;
    }

    public Class getDependencyClass ()
    {
        return clazz;
    }

    public String getDependencyName ()
    {
        return name;
    }

    public boolean equals (Object o)
    {
        return o instanceof Dependency &&
               ((Dependency) o).clazz == clazz &&
               ((Dependency) o).name.equals (name);
    }

    public int hashCode ()
    {
        return clazz.hashCode () ^ name.hashCode ();
    }

    public String toString ()
    {
        return "[Dependency on " + clazz.getName () + " via name \"" + name + "\"]";
    }
}
