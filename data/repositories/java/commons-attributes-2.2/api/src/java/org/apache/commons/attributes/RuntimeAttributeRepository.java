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
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to define attributes programmatically for a class.
 * It is recommended that this class is used in the static initializer for
 * a class:
 * <pre><code>
 * public class RuntimeSample extends SuperSample implements SampleIFJoin {
 *
 *     static {
 *         try {
 *             RuntimeAttributeRepository rar =
 *                 new RuntimeAttributeRepository (RuntimeSample.class);
 *
 *             rar.addClassAttribute (new ThreadSafe ());
 *
 *             rar.addFieldAttribute ("field", new ThreadSafe ());
 *
 *             rar.addMethodAttribute ("someMethod", new Class[]{},
 *                 new Dependency ( SampleService.class, "sample-some-method1" ));
 *
 *             rar.addParameterAttribute ("methodWithAttributes",
 *                 new Class[]{ Integer.TYPE, Integer.TYPE }, 1,
 *                 new ThreadSafe ());
 *
 *             rar.addReturnAttribute ("methodWithAttributes",
 *                 new Class[]{ Integer.TYPE, Integer.TYPE },
 *                 new Dependency ( SampleService.class, "sample-return" ));
 *
 *             rar.addMethodAttribute ("someMethod",
 *                 new Class[]{ Integer.TYPE },
 *                 new Dependency ( SampleService.class, "sample-some-method2" ));
 *
 *             Attributes.setAttributes (rar);
 *         } catch (Exception e) {
 *             throw new Error ("Unable to set attribute information: " + e.toString ());
 *         }
 *     }
 * </code></pre>
 *
 * @since 2.1
 */
public class RuntimeAttributeRepository implements AttributeRepositoryClass
{

    /**
     * Flag indicating whether this repository is modifiable.
     * Once sealed, a repository can't be un-sealed.
     */
    private volatile boolean sealed = false;

    /**
     * Set of class attributes. See javadoc for {@link AttributeRepositoryClass} for structure.
     */
    private final Set classAttributes = new HashSet ();

    /**
     * Set of field attributes. See javadoc for {@link AttributeRepositoryClass} for structure.
     */
    private final Map fieldAttributes = new HashMap ();

    /**
     * Set of ctor attributes. See javadoc for {@link AttributeRepositoryClass} for structure.
     */
    private final Map constructorAttributes = new HashMap ();

    /**
     * Set of method attributes. See javadoc for {@link AttributeRepositoryClass} for structure.
     */
    private final Map methodAttributes = new HashMap ();

    /**
     * Class we are defining attributes for.
     */
    private final Class clazz;

    /**
     * Synchronization lock for sealing the repository. We use this instead of
     * synchronizing on the instance (this), since the sync:ing was added
     * in 2.2 and we don't want to break any code.
     *
     * @since 2.2
     */
    private final Object syncLock = new Object ();

    /**
     * Create a new runtime repository.
     *
     * @since 2.1
     */
    public RuntimeAttributeRepository (Class clazz)
    {
        this.clazz = clazz;
    }

    /**
     * Adds a new attribute to the class itself.
     *
     * @since 2.1
     */
    public void addClassAttribute (Object attribute)
    {
        checkSealed ();
        classAttributes.add (attribute);
    }

    /**
     * Convenience function to check if the repository is sealed.
     *
     * @throws IllegalStateException if sealed
     *
     * @since 2.1
     */
    private void checkSealed () throws IllegalStateException
    {
        synchronized (syncLock)
        {
            if (sealed)
            {
                throw new IllegalStateException ("RuntimeAttributeRepository has been sealed.");
            }
        }
    }

    /**
     * Convenience method to get and initialize an enry in the method or
     * constructor attribute map.
     *
     * @since 2.1
     */
    private List getMethodOrConstructorAttributeBundle (Map map, String signature, int numSlots)
    {
        List bundle = (List) map.get (signature);
        if (bundle == null)
        {
            bundle = new ArrayList ();
            map.put (signature, bundle);

            for (int i = 0; i < numSlots; i++)
            {
                bundle.add (new HashSet ());
            }
        }

        return bundle;
    }

    /**
     * Convenience method to get and initialize an entry in the method map.
     *
     * @return a fully initialized List (as defined for the {@link AttributeRepositoryClass} interface)
     *         for the given method.
     *
     * @since 2.1
     */
    private List getMethodAttributeBundle (Method m)
    {
        String signature = Util.getSignature (m);
        if (m.getDeclaringClass () != clazz)
        {
            throw new IllegalArgumentException ("There is no " + signature + " in " + clazz.getName () + ". It is defined in " +
                                                m.getDeclaringClass ().getName ());
        }

        return getMethodOrConstructorAttributeBundle (methodAttributes, signature, m.getParameterTypes ().length + 2);
    }

    /**
     * Convenience method to get and initialize an entry in the constructor map.
     *
     * @return a fully initialized List (as defined for the {@link AttributeRepositoryClass} interface)
     *         for the given constructor.
     *
     * @since 2.1
     */
    private List getConstructorAttributeBundle (Constructor c)
    {
        String signature = Util.getSignature (c);
        if (c.getDeclaringClass () != clazz)
        {
            throw new IllegalArgumentException ("There is no " + signature + " in " + clazz.getName () + ". It is defined in " +
                                                c.getDeclaringClass ().getName ());
        }

        return getMethodOrConstructorAttributeBundle (constructorAttributes, signature, c.getParameterTypes ().length + 1);
    }

    /**
     * Adds an attribute to a field.
     *
     * @since 2.1
     */
    public void addFieldAttribute (String name, Object attribute) throws NoSuchFieldException, SecurityException
    {
        addFieldAttribute (clazz.getDeclaredField (name), attribute);
    }

    /**
     * Adds an attribute to a field.
     *
     * @since 2.1
     */
    public void addFieldAttribute (Field f, Object attribute)
    {
        checkSealed ();
        String signature = f.getName ();
        if (f.getDeclaringClass () != clazz)
        {
            throw new IllegalArgumentException ("There is no " + signature + " in " + clazz.getName () + ". It is defined in " +
                                                f.getDeclaringClass ().getName ());
        }

        Set attributeSet = (Set) fieldAttributes.get (signature);
        if (attributeSet == null)
        {
            attributeSet = new HashSet ();
            fieldAttributes.put (signature, attributeSet);
        }

        attributeSet.add (attribute);
    }

    /**
     * Adds an attribute to a constructor. The constructor is obtained via the getDeclaredConstrutor method
     * of the class this repository defines.
     *
     * @since 2.1
     */
    public void addConstructorAttribute (Class[] parameters, Object attribute) throws NoSuchMethodException, SecurityException
    {
        addConstructorAttribute (clazz.getDeclaredConstructor (parameters), attribute);
    }

    /**
     * Adds an attribute to a constructor.
     *
     * @since 2.1
     */
    public void addConstructorAttribute (Constructor c, Object attribute)
    {
        checkSealed ();
        List bundle = getConstructorAttributeBundle (c);
        Set ctorAttrs = (Set) bundle.get (0);
        ctorAttrs.add (attribute);
    }

    /**
     * Adds an attribute to a method. The method is obtained via the getDeclaredMethod method
     * of the class this repository defines.
     *
     * @since 2.1
     */
    public void addMethodAttribute (String name, Class[] parameters, Object attribute) throws NoSuchMethodException, SecurityException
    {
        addMethodAttribute (clazz.getDeclaredMethod (name, parameters), attribute);
    }

    /**
     * Adds an attribute to a method.
     *
     * @since 2.1
     */
    public void addMethodAttribute (Method m, Object attribute)
    {
        checkSealed ();
        List bundle = getMethodAttributeBundle (m);
        Set methodAttrs = (Set) bundle.get (0);
        methodAttrs.add (attribute);
    }

    /**
     * Adds an attribute to a parameter of a constructor. The constructor is obtained via the getDeclaredConstrutor method
     * of the class this repository defines.
     *
     * @since 2.1
     */
    public void addParameterAttribute (Class[] parameters, int parameterIndex, Object attribute) throws NoSuchMethodException, SecurityException
    {
        addParameterAttribute (clazz.getDeclaredConstructor (parameters), parameterIndex, attribute);
    }

    /**
     * Adds an attribute to a parameter of a constructor.
     *
     * @since 2.1
     */
    public void addParameterAttribute (Constructor c, int parameterIndex, Object attribute)
    {
        checkSealed ();
        List bundle = getConstructorAttributeBundle (c);

        Set parameterAttrs = (Set) bundle.get (parameterIndex + 1);
        parameterAttrs.add (attribute);
    }


    /**
     * Adds an attribute to a parameter of a method. The method is obtained via the getDeclaredMethod method
     * of the class this repository defines.
     *
     * @since 2.1
     */
    public void addParameterAttribute (String name, Class[] parameters, int parameterIndex, Object attribute) throws NoSuchMethodException, SecurityException
    {
        addParameterAttribute (clazz.getDeclaredMethod (name, parameters), parameterIndex, attribute);
    }

    /**
     * Adds an attribute to a parameter of a method. The method is obtained via the getDeclaredMethod method
     * of the class this repository defines.
     *
     * @since 2.1
     */
    public void addParameterAttribute (Method m, int parameterIndex, Object attribute)
    {
        checkSealed ();
        List bundle = getMethodAttributeBundle (m);

        Set parameterAttrs = (Set) bundle.get (parameterIndex + 2);
        parameterAttrs.add (attribute);
    }

    /**
     * Adds an attribute to the return value of a method. The method is obtained via the getDeclaredMethod method
     * of the class this repository defines.
     *
     * @since 2.1
     */
    public void addReturnAttribute (String name, Class[] parameters, Object attribute) throws NoSuchMethodException, SecurityException
    {
        addReturnAttribute (clazz.getDeclaredMethod (name, parameters), attribute);
    }

    /**
     * Adds an attribute to the return value of a method. The method is obtained via the getDeclaredMethod method
     * of the class this repository defines.
     *
     * @since 2.1
     */
    public void addReturnAttribute (Method m, Object attribute)
    {
        checkSealed ();

        List bundle = getMethodAttributeBundle (m);

        Set returnAttrs = (Set) bundle.get (1);
        returnAttrs.add (attribute);
    }

    /**
     * Gets the class this repository defines attributes for.
     *
     * @since 2.1
     */
    public Class getDefinedClass ()
    {
        return clazz;
    }

    public Set getClassAttributes ()
    {
        return classAttributes;
    }

    public Map getFieldAttributes ()
    {
        return fieldAttributes;
    }

    public Map getMethodAttributes ()
    {
        return methodAttributes;
    }

    public Map getConstructorAttributes ()
    {
        return constructorAttributes;
    }

    /**
     * Seals this repository. A sealed repository can't be modified.
     *
     * @since 2.1
     */
    public void seal ()
    {
        synchronized (syncLock)
        {
            sealed = true;
        }
    }
}
