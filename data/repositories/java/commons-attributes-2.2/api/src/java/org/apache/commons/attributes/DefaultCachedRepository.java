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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;

/**
 *
 * @since 2.1
 */
class DefaultCachedRepository implements CachedRepository
{

    private final static Collection EMPTY_COLLECTION = new ArrayList (0);

    private final Collection classAttributes;
    private final Map fields = new HashMap ();
    private final Map methods = new HashMap ();
    private final Map constructors = new HashMap ();

    private static class MethodAttributeBundle
    {
        private Collection attributes = EMPTY_COLLECTION;
        private List parameterAttributes = new ArrayList ();
        private Collection returnAttributes = EMPTY_COLLECTION;
        private final String methodName;

        public MethodAttributeBundle (String methodName)
        {
            this.methodName = methodName;
        }

        public Collection getAttributes ()
        {
            return attributes;
        }

        public Collection getReturnAttributes ()
        {
            return returnAttributes;
        }

        public Collection getParameterAttributes (int index)
        {
            if (index < 0 || index >= parameterAttributes.size ())
            {
                throw new ParameterIndexOutOfBoundsException (methodName, index, parameterAttributes.size ());
            }
            return (Collection) parameterAttributes.get (index);
        }

        public void setAttributes (Collection attributes)
        {
            this.attributes = Collections.unmodifiableCollection (attributes);
        }

        public void setReturnAttributes (Collection returnAttributes)
        {
            this.returnAttributes = Collections.unmodifiableCollection (returnAttributes);
        }

        public void addParameterAttributes (Collection parameterAttributes)
        {
            this.parameterAttributes.add (Collections.unmodifiableCollection (parameterAttributes));
        }
    }

    public DefaultCachedRepository (Class clazz, AttributeRepositoryClass repo)
    {

        // ---- Fix up class attributes
        Set tempClassAttributes = new HashSet ();

        tempClassAttributes.addAll (repo.getClassAttributes ());
        tempClassAttributes.addAll (getInheritableClassAttributes (clazz.getSuperclass ()));
        Class[] ifs = clazz.getInterfaces ();
        for (int i = 0; i < ifs.length; i++)
        {
            tempClassAttributes.addAll (getInheritableClassAttributes (ifs[i]));
        }
        this.classAttributes = Collections.unmodifiableCollection (tempClassAttributes);

        // ---- Fix up method attributes
        Method[] methods = clazz.getDeclaredMethods ();
        for (int i = 0; i < methods.length; i++)
        {

            Method m = methods[i];
            String key = Util.getSignature (m);
            MethodAttributeBundle bundle = new MethodAttributeBundle (m.toString ());

            List attributeBundle = null;
            if (repo.getMethodAttributes ().containsKey (key))
            {
                attributeBundle = (List) repo.getMethodAttributes ().get (key);
            }

            Set attributes = new HashSet ();
            if (attributeBundle != null)
            {
                attributes.addAll ((Collection) attributeBundle.get (0));
            }
            attributes.addAll (getInheritableMethodAttributes (clazz.getSuperclass (), m.getName (), m.getParameterTypes ()));
            for (int j = 0; j < ifs.length; j++)
            {
                attributes.addAll (getInheritableMethodAttributes (ifs[j], m.getName (), m.getParameterTypes ()));
            }

            if (attributes.size () > 0)
            {
                bundle.setAttributes (attributes);
            }

            // ---- Return value attributes
            attributes = new HashSet ();
            if (attributeBundle != null)
            {
                attributes.addAll ((Collection) attributeBundle.get (1));
            }
            attributes.addAll (getInheritableReturnAttributes (clazz.getSuperclass (), m.getName (), m.getParameterTypes ()));
            for (int j = 0; j < ifs.length; j++)
            {
                attributes.addAll (getInheritableReturnAttributes (ifs[j], m.getName (), m.getParameterTypes ()));
            }
            if (attributes.size () > 0)
            {
                bundle.setReturnAttributes (attributes);
            }

            // ---- Parameter attributes
            int numParameters = m.getParameterTypes().length;
            for (int k = 0; k < numParameters; k++)
            {
                attributes = new HashSet ();
                if (attributeBundle != null)
                {
                    attributes.addAll ((Collection) attributeBundle.get (k + 2));
                }
                attributes.addAll (getInheritableMethodParameterAttributes (clazz.getSuperclass (), m.getName (), m.getParameterTypes (), k));
                for (int j = 0; j < ifs.length; j++)
                {
                    attributes.addAll (getInheritableMethodParameterAttributes (ifs[j], m.getName (), m.getParameterTypes (), k));
                }

                bundle.addParameterAttributes (attributes);
            }

            this.methods.put (m, bundle);
        }

        // --- Just copy constructor attributes (they aren't inherited)
        Constructor[] constructors = clazz.getDeclaredConstructors ();
        for (int i = 0; i < constructors.length; i++)
        {
            Constructor ctor = constructors[i];
            String key = Util.getSignature (ctor);

            if (repo.getConstructorAttributes ().containsKey (key))
            {
                List attributeBundle = null;
                attributeBundle = (List) repo.getConstructorAttributes ().get (key);
                MethodAttributeBundle bundle = new MethodAttributeBundle (ctor.toString ());

                bundle.setAttributes ((Collection) attributeBundle.get (0));
                // ---- Parameter attributes
                int numParameters = ctor.getParameterTypes().length;
                for (int k = 0; k < numParameters; k++)
                {
                    bundle.addParameterAttributes ((Collection) attributeBundle.get (k + 1));
                }
                this.constructors.put (ctor, bundle);
            }
        }

        // --- Just copy field attributes (they aren't inherited)
        Field[] fields = clazz.getDeclaredFields ();
        for (int i = 0; i < fields.length; i++)
        {
            Field f = fields[i];
            String key = f.getName ();
            if (repo.getFieldAttributes ().containsKey (key))
            {
                this.fields.put (f, Collections.unmodifiableCollection ((Collection) repo.getFieldAttributes ().get (key)));
            }
        }
    }

    private static Collection getInheritableAttributes (Collection attrs)
    {
        HashSet result = new HashSet ();

        Iterator iter = attrs.iterator ();
        while (iter.hasNext ())
        {
            Object attr = iter.next ();
            if (Attributes.hasAttributeType (attr.getClass (), Inheritable.class))
            {
                result.add (attr);
            }
        }
        return result;
    }

    private static Collection getInheritableClassAttributes (Class c)
    {
        if (c == null)
        {
            return new ArrayList (0);
        }

        HashSet result = new HashSet ();
        result.addAll (getInheritableAttributes (Attributes.getAttributes (c)));

        // Traverse the class hierarchy
        result.addAll (getInheritableClassAttributes (c.getSuperclass ()));

        // Traverse the interface hierarchy
        Class[] ifs = c.getInterfaces ();
        for (int i = 0; i < ifs.length; i++)
        {
            result.addAll (getInheritableClassAttributes (ifs[i]));
        }

        return result;
    }

    private static Collection getInheritableMethodAttributes (Class c, String methodName, Class[] methodParams)
    {
        if (c == null)
        {
            return new ArrayList (0);
        }

        HashSet result = new HashSet ();

        try
        {
            // Get equivalent method in c
            Method m = c.getDeclaredMethod (methodName, methodParams);
            if ((m.getModifiers () & Modifier.PRIVATE) == 0)
            {
                result.addAll (getInheritableAttributes (Attributes.getAttributes (m)));
            }
        }
        catch (NoSuchMethodException nsme)
        {
        }

        // Traverse the class hierarchy
        result.addAll (getInheritableMethodAttributes (c.getSuperclass (), methodName, methodParams));

        // Traverse the interface hierarchy
        Class[] ifs = c.getInterfaces ();
        for (int i = 0; i < ifs.length; i++)
        {
            result.addAll (getInheritableMethodAttributes (ifs[i], methodName, methodParams));
        }

        return result;
    }

    private static Collection getInheritableMethodParameterAttributes (Class c, String methodName, Class[] methodParams, int parameter)
    {
        if (c == null)
        {
            return new ArrayList (0);
        }

        HashSet result = new HashSet ();

        try
        {
            // Get equivalent method in c
            Method m = c.getDeclaredMethod (methodName, methodParams);
            if ((m.getModifiers () & Modifier.PRIVATE) == 0)
            {
                result.addAll (getInheritableAttributes (Attributes.getParameterAttributes (m, parameter)));
            }
        }
        catch (NoSuchMethodException nsme)
        {
        }

        // Traverse the class hierarchy
        result.addAll (getInheritableMethodParameterAttributes (c.getSuperclass (), methodName, methodParams, parameter));

        // Traverse the interface hierarchy
        Class[] ifs = c.getInterfaces ();
        for (int i = 0; i < ifs.length; i++)
        {
            result.addAll (getInheritableMethodParameterAttributes (ifs[i], methodName, methodParams, parameter));
        }

        return result;
    }

    private static Collection getInheritableReturnAttributes (Class c, String methodName, Class[] methodParams)
    {
        if (c == null)
        {
            return new ArrayList (0);
        }

        HashSet result = new HashSet ();

        try
        {
            // Get equivalent method in c
            Method m = c.getDeclaredMethod (methodName, methodParams);
            if ((m.getModifiers () & Modifier.PRIVATE) == 0)
            {
                result.addAll (getInheritableAttributes (Attributes.getReturnAttributes (m)));
            }
        }
        catch (NoSuchMethodException nsme)
        {
        }

        // Traverse the class hierarchy
        result.addAll (getInheritableReturnAttributes (c.getSuperclass (), methodName, methodParams));

        // Traverse the interface hierarchy
        Class[] ifs = c.getInterfaces ();
        for (int i = 0; i < ifs.length; i++)
        {
            result.addAll (getInheritableReturnAttributes (ifs[i], methodName, methodParams));
        }

        return result;
    }

    public Collection getAttributes ()
    {
        return classAttributes;
    }

    public Collection getAttributes (Field f)
    {
        if (fields.containsKey (f))
        {
            return (Collection) fields.get (f);
        }
        else
        {
            return EMPTY_COLLECTION;
        }

    }

    public Collection getAttributes (Method m)
    {
        if (methods.containsKey (m))
        {
            return ((MethodAttributeBundle) methods.get (m)).getAttributes ();
        }
        else
        {
            return EMPTY_COLLECTION;
        }
    }

    public Collection getParameterAttributes (Constructor c, int parameter)
    {
        if (constructors.containsKey (c))
        {
            return ((MethodAttributeBundle) constructors.get (c)).getParameterAttributes (parameter);
        }
        else
        {
            return EMPTY_COLLECTION;
        }
    }

    public Collection getParameterAttributes (Method m, int parameter)
    {
        if (methods.containsKey (m))
        {
            return ((MethodAttributeBundle) methods.get (m)).getParameterAttributes (parameter);
        }
        else
        {
            return EMPTY_COLLECTION;
        }
    }

    public Collection getReturnAttributes (Method m)
    {
        if (methods.containsKey (m))
        {
            return ((MethodAttributeBundle) methods.get (m)).getReturnAttributes ();
        }
        else
        {
            return EMPTY_COLLECTION;
        }
    }

    public Collection getAttributes (Constructor c)
    {
        if (constructors.containsKey (c))
        {
            return ((MethodAttributeBundle) constructors.get (c)).getAttributes ();
        }
        else
        {
            return EMPTY_COLLECTION;
        }
    }
}
