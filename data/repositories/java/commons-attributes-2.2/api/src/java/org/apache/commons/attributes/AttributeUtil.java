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
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Commonly used convenience functions.
 *
 * @since 2.1
 */
public class AttributeUtil
{

    /**
     * Filters a Collection of <code>Class</code> objects. The returned collection
     * only contains those classes that have an attribute of the specified type.
     * That is, for each Class clazz in the
     * returned collection,
     * <code>clazz != null && Attributes.hasAttributeType (clazz, attributeClass)</code>
     * is true.
     *
     * @param classes Collection of Classes to filter. May contain <code>null</code> references,
     *                but may not itself be <code>null</code>.
     * @param attributeClass the class to filter based on.
     *
     * @since 2.1
     */
    public static Collection getClassesWithAttributeType (Collection classes, Class attributeClass)
    {
        if (classes == null)
        {
            throw new NullPointerException ("classes");
        }

        if (attributeClass == null)
        {
            throw new NullPointerException ("attributeClass");
        }

        ArrayList result = new ArrayList ();
        Iterator iter = classes.iterator ();
        while (iter.hasNext ())
        {
            Class clazz = (Class) iter.next ();
            if (clazz != null)
            {
                if (Attributes.hasAttributeType (clazz, attributeClass))
                {
                    result.add (clazz);
                }
            }
        }

        return result;
    }

    /**
     * Filters a collection of objects. The returned collection
     * only contains those objects whose class have an attribute
     * of the specified type. That is, for each Object o in the
     * returned collection,
     * <code>o != null && Attributes.hasAttributeType (o.getClass (), attributeClass)</code>
     * is true.
     *
     * @param objects Collection of objects to filter. May contain <code>null</code> references,
     *                but may not itself be <code>null</code>.
     * @param attributeClass the class to filter based on.
     *
     * @since 2.1
     */
    public static Collection getObjectsWithAttributeType (Collection objects, Class attributeClass)
    {
        if (objects == null)
        {
            throw new NullPointerException ("objects");
        }

        if (attributeClass == null)
        {
            throw new NullPointerException ("attributeClass");
        }

        ArrayList result = new ArrayList ();
        Iterator iter = objects.iterator ();
        while (iter.hasNext ())
        {
            Object object = iter.next ();
            if (object != null)
            {
                Class clazz = object.getClass ();
                if (Attributes.hasAttributeType (clazz, attributeClass))
                {
                    result.add (object);
                }
            }
        }

        return result;
    }
}
