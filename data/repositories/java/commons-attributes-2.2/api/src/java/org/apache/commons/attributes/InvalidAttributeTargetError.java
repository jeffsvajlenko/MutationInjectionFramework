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
package org.apache.commons.attributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Thrown when an attribute has a {@link Target} declaration that forbids
 * it being applied to the program element it has been applied to.
 *
 * <p>For example:
 *
 * <pre><code>
 * &#x2f;**
 *  * This attribute can only be applied to Classes.
 *  * Target(Target.CLASS)
 *  *&#x2f;
 * public class MyAttribute {}
 *
 * public class MyClass {
 *     &#x2f;**
 *       * Error: Can't apply MyAttribute to a field!
 *       * &#x40;&#x40;MyAttribute()
 *       *&#x2f;
 *     private String myField;
 * }
 * </code></pre>
 *
 * @since 2.1
 */
public class InvalidAttributeTargetError extends Error
{

    /**
     * Creates a new InvalidAttributeTargetError.
     *
     * @param attributeClass name of the type of the attribute that was applied to the wrong target.
     * @param element        the element that the user tried to apply the attribute to.
     * @param targetFlags    the targets (bitwise OR of the {@link Target}.XXX flags that the attributeClass
     *                       can be applied to.
     * @since 2.1
     */
    public InvalidAttributeTargetError (String attributeClass, String element, int targetFlags)
    {
        super ("Attributes of type " + attributeClass + " can't be applied to " + element + ". " +
               "They can only be applied to: " + flagsToString (targetFlags));
    }

    private final static String flagsToString (int flags)
    {
        List targetNames = new ArrayList ();
        if ((flags & Target.CLASS) > 0)
        {
            targetNames.add ("CLASS");
        }
        if ((flags & Target.FIELD) > 0)
        {
            targetNames.add ("FIELD");
        }
        if ((flags & Target.METHOD) > 0)
        {
            targetNames.add ("METHOD");
        }
        if ((flags & Target.CONSTRUCTOR) > 0)
        {
            targetNames.add ("CONSTRUCTOR");
        }
        if ((flags & Target.METHOD_PARAMETER) > 0)
        {
            targetNames.add ("METHOD_PARAMETER");
        }
        if ((flags & Target.CONSTRUCTOR_PARAMETER) > 0)
        {
            targetNames.add ("CONSTRUCTOR_PARAMETER");
        }
        if ((flags & Target.RETURN) > 0)
        {
            targetNames.add ("RETURN");
        }

        StringBuffer sb = new StringBuffer ();
        for (int i = 0; i < targetNames.size (); i++)
        {
            sb.append (targetNames.get (i));
            if (i < targetNames.size () - 1)
            {
                sb.append (" | ");
            }
        }
        return sb.toString ();
    }

}
