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

import org.apache.commons.attributes.DefaultSealable;
import org.apache.commons.attributes.Sealable;

/**
 * A sample attribute with bean-like properties. Used to test the
 * named parameter syntax.
 */
public class BeanAttribute extends DefaultSealable
{

    private final int number;
    private final String string;
    private String name;
    private int anotherNumber;

    public BeanAttribute (int number, String string)
    {
        this.number = number;
        this.string = string;
    }

    public int getNumber ()
    {
        return number;
    }

    public String getString ()
    {
        return string;
    }

    public String getName ()
    {
        return name;
    }

    public int getAnotherNumber ()
    {
        return anotherNumber;
    }

    public void setAnotherNumber (int anotherNumber)
    {
        checkSealed ();
        this.anotherNumber = anotherNumber;
    }

    public void setName (String name)
    {
        checkSealed ();
        this.name = name;
    }

    public boolean equals (Object o)
    {
        return o instanceof BeanAttribute &&
               ((BeanAttribute) o).string.equals (string) &&
               ((BeanAttribute) o).anotherNumber == anotherNumber &&
               ((BeanAttribute) o).number == number &&
               ((BeanAttribute) o).name.equals (name);
    }

    public int hashCode ()
    {
        return string.hashCode ();
    }

    public String toString ()
    {
        return "[BeanAttribute " + number + ", " + string + "; " + name + ", " + anotherNumber + "\"]";
    }
}
