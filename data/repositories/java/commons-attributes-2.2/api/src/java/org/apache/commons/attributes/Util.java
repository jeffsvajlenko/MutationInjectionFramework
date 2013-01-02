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

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.Map;
import java.util.Collection;

class Util
{

    public static String getSignature (Method m)
    {
        return m.getName () + "(" + getParameterList (m.getParameterTypes ()) + ")";
    }

    public static String getSignature (Constructor c)
    {
        return "(" + getParameterList (c.getParameterTypes ()) + ")";
    }

    public static String decodedClassName (String rawName) throws IllegalArgumentException
    {
        if (!rawName.startsWith ("["))
        {
            return rawName;
        }
        else
        {
            StringBuffer nesting = new StringBuffer ();
            int i = 0;
            while (rawName.charAt (i) == '[')
            {
                nesting.append ("[]");
                i++;
            }
            String type = "";
            switch (rawName.charAt (i))
            {
            case 'B':
                type = "byte";
                break;
            case 'C':
                type = "char";
                break;
            case 'D':
                type = "double";
                break;
            case 'F':
                type = "float";
                break;
            case 'I':
                type = "int";
                break;
            case 'J':
                type = "long";
                break;
            case 'L':
                type = rawName.substring (i + 1, rawName.length () - 1);
                break;
            case 'S':
                type = "short";
                break;
            case 'Z':
                type = "boolean";
                break;
            default:
                throw new IllegalArgumentException ("Can't decode " + rawName);
            }

            return type + nesting.toString ();
        }
    }

    public static String getParameterList (Class[] params)
    {
        StringBuffer sb = new StringBuffer ();
        for (int i = 0; i < params.length; i++)
        {
            if (i > 0)
            {
                sb.append (",");
            }
            sb.append (decodedClassName (params[i].getName ()));
        }
        return sb.toString ();
    }

    private static void checkTarget (int target, Object attribute, String element)
    {
        Target targetAttr = (Target) Attributes.getAttribute (attribute.getClass (), Target.class);
        if (targetAttr == null)
        {
            return;
        }

        if ((targetAttr.getFlags () & target) == 0)
        {
            throw new InvalidAttributeTargetError (attribute.getClass ().getName (), element, targetAttr.getFlags ());
        }
    }

    private static void checkTargets (int target, Collection attributes, String element)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            checkTarget (target, iter.next (), element);
        }
    }

    public static void validateRepository (Class owningClass, AttributeRepositoryClass repo)
    {
        checkTargets (Target.CLASS, repo.getClassAttributes (), owningClass.getName ());

        Map fieldAttrs = repo.getFieldAttributes ();
        for (Iterator iter = fieldAttrs.keySet ().iterator(); iter.hasNext ();)
        {
            String fieldName = (String) iter.next ();

            checkTargets (Target.FIELD, (Collection) fieldAttrs.get (fieldName), owningClass.getName () + "." + fieldName);
        }

        Map ctorAttrs = repo.getConstructorAttributes ();
        for (Iterator iter = ctorAttrs.keySet ().iterator(); iter.hasNext ();)
        {
            String ctorName = (String) iter.next ();
            List bundle = (List) ctorAttrs.get (ctorName);

            for (int i = 0; i < bundle.size (); i++)
            {
                switch (i)
                {
                case 0:
                    checkTargets (Target.CONSTRUCTOR, (Collection) bundle.get (0), owningClass.getName () + "." + ctorName);
                    break;

                default:
                    checkTargets (Target.CONSTRUCTOR_PARAMETER, (Collection) bundle.get (i), "parameter " + (i) + " of " + owningClass.getName () + ctorName);
                }
            }
        }

        Map methodAttrs = repo.getMethodAttributes ();
        for (Iterator iter = methodAttrs.keySet ().iterator(); iter.hasNext ();)
        {
            String methodName = (String) iter.next ();
            List bundle = (List) methodAttrs.get (methodName);

            for (int i = 0; i < bundle.size (); i++)
            {
                switch (i)
                {
                case 0:
                    checkTargets (Target.METHOD, (Collection) bundle.get (0), owningClass.getName () + "." + methodName);
                    break;

                case 1:
                    checkTargets (Target.RETURN, (Collection) bundle.get (1), "return value of " + owningClass.getName () + "." + methodName);
                    break;

                default:
                    checkTargets (Target.METHOD_PARAMETER, (Collection) bundle.get (i), "parameter " + (i - 1) + " of " + owningClass.getName () + "." + methodName);
                }
            }
        }
    }
}
