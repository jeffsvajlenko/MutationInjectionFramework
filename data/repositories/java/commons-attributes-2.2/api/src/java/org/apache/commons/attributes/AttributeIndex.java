/*
 * Copyright 2003-2005 The Apache Software Foundation
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

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * An index providing a list of elements with given attributes. This
 * requires that the attribute is {@link Indexed} and that the
 * attribute indexer tool has been run on the jar file containing the
 * classes.
 *
 * @since 2.1
 */
public class AttributeIndex
{

    /**
     * Reference to a method parameter. A method parameter
     * is defined by the Method object it is defined in, and the index
     * of the parameter in the method's parameter list.
     *
     * @since 2.1
     */
    public static class MethodParameter
    {

        private final Method method;
        private final int index;

        /**
         * Constructs a new MethodParameter.
         *
         * @since 2.1
         */
        public MethodParameter (Method method, int index)
        {
            this.method = method;
            this.index = index;
        }

        /**
         * Get the method this parameter is defined in.
         *
         * @since 2.1
         */
        public Method getMethod ()
        {
            return method;
        }

        /**
         * Get the index of this parameter in the parameter list of the method.
         *
         * @since 2.1
         */
        public int getIndex ()
        {
            return index;
        }

        /**
         * Compares two <code>MethodParameter</code>s for equality.
         * They must point to the same method and have the same index.
         *
         * @since 2.1
         */
        public boolean equals (Object o)
        {
            return o != null && o instanceof MethodParameter &&
                   method.equals (((MethodParameter) o).method) &&
                   index == ((MethodParameter) o).index;
        }

        /**
         * Computes the hashCode.
         *
         * @since 2.1
         */
        public int hashCode ()
        {
            return method.hashCode () + index;
        }

        /**
         * Converts this method parameter into a human-readable string.
         *
         * @since 2.1
         */
        public String toString ()
        {
            return method.toString () + ":" + index;
        }
    }

    /**
     * A constructor parameter. A method parameter
     * is defined by the Method object it is defined in, and the index
     * of the parameter in the method's parameter list.
     *
     * @since 2.1
     */
    public static class ConstructorParameter
    {

        private final Constructor ctor;
        private final int index;


        /**
         * Constructs a new ConstructorParameter.
         *
         * @since 2.1
         */
        public ConstructorParameter (Constructor ctor, int index)
        {
            this.ctor = ctor;
            this.index = index;
        }

        /**
         * Get the constructor this parameter is defined in.
         *
         * @since 2.1
         */
        public Constructor getConstructor ()
        {
            return ctor;
        }

        /**
         * Get the index of this parameter in the parameter list of the constructor.
         *
         * @since 2.1
         */
        public int getIndex ()
        {
            return index;
        }

        /**
         * Compares two <code>ConstructorParameter</code>s for equality.
         * They must point to the same constructor and have the same index.
         *
         * @since 2.1
         */
        public boolean equals (Object o)
        {
            return o != null && o instanceof ConstructorParameter &&
                   ctor.equals (((ConstructorParameter) o).ctor) &&
                   index == ((ConstructorParameter) o).index;
        }

        /**
         * Computes the hashCode.
         *
         * @since 2.1
         */
        public int hashCode ()
        {
            return ctor.hashCode () + index;
        }

        /**
         * Converts this constructor parameter into a human-readable string.
         *
         * @since 2.1
         */
        public String toString ()
        {
            return ctor.toString () + ":" + index;
        }
    }

    private static class IndexNode
    {
        public Collection classes = new HashSet ();
        public Collection fields = new HashSet ();
        public Collection methods = new HashSet ();
        public Collection constructors = new HashSet ();
        public Collection returnValues = new HashSet ();
        public Collection constructorParameters = new HashSet ();
        public Collection methodParameters = new HashSet ();

        public void seal ()
        {
            classes = seal (classes);
            fields = seal (fields);
            methods = seal (methods);
            constructors = seal (constructors);
            returnValues = seal (returnValues);
            constructorParameters = seal (constructorParameters);
            methodParameters = seal (methodParameters);
        }

        private Collection seal (Collection coll)
        {
            return Collections.unmodifiableCollection (coll);
        }
    }

    private final HashMap index = new HashMap ();
    private final ClassLoader classLoader;

    /**
     * Creates a new AttributeIndex for the given ClassLoader.
     *
     * @since 2.1
     */
    public AttributeIndex (ClassLoader cl) throws Exception
    {
        this.classLoader = cl;
        Enumeration e = cl.getResources ("META-INF/attrs.index");
        while (e.hasMoreElements ())
        {
            URL url = (URL) e.nextElement ();
            loadFromURL (url);
        }

        Iterator iter = index.values ().iterator ();
        while (iter.hasNext ())
        {
            ((IndexNode) iter.next ()).seal ();
        }
    }

    private IndexNode getNode (Class attributeClass)
    {
        IndexNode node = (IndexNode) index.get (attributeClass.getName ());
        if (node == null)
        {
            node = new IndexNode ();
            index.put (attributeClass.getName (), node);
        }

        return node;
    }

    private void addIndex (Collection attributes, Class clazz)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            getNode (iter.next ().getClass ()).classes.add (clazz);
        }
    }

    private void addIndex (Collection attributes, Field field)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            getNode (iter.next ().getClass ()).fields.add (field);
        }
    }

    private void addIndex (Collection attributes, Method method)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            getNode (iter.next ().getClass ()).methods.add (method);
        }
    }

    private void addIndex (Collection attributes, Constructor constructor)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            getNode (iter.next ().getClass ()).constructors.add (constructor);
        }
    }

    private void addReturnIndex (Collection attributes, Method method)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            getNode (iter.next ().getClass ()).returnValues.add (method);
        }
    }

    private void addIndex (Collection attributes, Method method, int parameter)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            getNode (iter.next ().getClass ()).methodParameters.add (new MethodParameter (method, parameter));
        }
    }

    private void addIndex (Collection attributes, Constructor ctor, int parameter)
    {
        Iterator iter = attributes.iterator ();
        while (iter.hasNext ())
        {
            getNode (iter.next ().getClass ()).constructorParameters.add (new ConstructorParameter (ctor, parameter));
        }
    }

    /**
     * Add a class to the index.
     */
    private void addClass (String clazzName) throws Exception
    {
        Class clazz = classLoader.loadClass (clazzName);

        // Get the attributes attached to the class itself...
        Collection coll = Attributes.getAttributes (clazz);

        coll = AttributeUtil.getObjectsWithAttributeType (coll, Indexed.class);
        addIndex (coll, clazz);

        Field[] fields = clazz.getDeclaredFields ();
        for (int i = 0; i < fields.length; i++)
        {
            coll = Attributes.getAttributes (fields[i]);

            coll = AttributeUtil.getObjectsWithAttributeType (coll, Indexed.class);
            addIndex (coll, fields[i]);
        }

        Method[] methods = clazz.getDeclaredMethods ();
        for (int i = 0; i < methods.length; i++)
        {
            coll = Attributes.getAttributes (methods[i]);

            coll = AttributeUtil.getObjectsWithAttributeType (coll, Indexed.class);
            addIndex (coll, methods[i]);

            // Return values
            coll = Attributes.getReturnAttributes (methods[i]);

            coll = AttributeUtil.getObjectsWithAttributeType (coll, Indexed.class);
            addReturnIndex (coll, methods[i]);

            // Parameters
            int numParameters = methods[i].getParameterTypes().length;
            for (int j = 0; j < numParameters; j++)
            {
                coll = Attributes.getParameterAttributes (methods[i], j);

                coll = AttributeUtil.getObjectsWithAttributeType (coll, Indexed.class);
                addIndex (coll, methods[i], j);
            }
        }

        Constructor[] ctors = clazz.getDeclaredConstructors ();
        for (int i = 0; i < ctors.length; i++)
        {
            coll = Attributes.getAttributes (ctors[i]);

            coll = AttributeUtil.getObjectsWithAttributeType (coll, Indexed.class);
            addIndex (coll, ctors[i]);

            // Parameters
            int numParameters = ctors[i].getParameterTypes().length;
            for (int j = 0; j < numParameters; j++)
            {
                coll = Attributes.getParameterAttributes (ctors[i], j);

                coll = AttributeUtil.getObjectsWithAttributeType (coll, Indexed.class);
                addIndex (coll, ctors[i], j);
            }
        }
    }

    /**
     * Load the attrs.index from a given URL.
     *
     * @since 2.1
     */
    private void loadFromURL (URL url) throws Exception
    {
        URLConnection connection = url.openConnection ();
        BufferedReader br = new BufferedReader (new InputStreamReader (connection.getInputStream ()));
        try
        {
            String currentAttributeClass = null;
            String line = null;
            while ((line = br.readLine ()) != null)
            {
                if (line.startsWith ("Class: "))
                {
                    String className = line.substring ("Class: ".length ()).trim ();
                    addClass (className);
                }
            }
        }
        finally
        {
            br.close ();
        }
    }

    /**
     * Gets a Collection of the classes that have an attribute of the specified class.
     * The Collection contains the class names (String).
     *
     * @deprecated Use the getClasses(Class) method instead.
     *
     * @since 2.1
     */
    public Collection getClassesWithAttribute (String attributeClass)
    {
        if (index.containsKey (attributeClass))
        {
            Collection classes = ((IndexNode) index.get (attributeClass)).classes;
            Iterator iter = classes.iterator ();
            Collection converted = new ArrayList (classes.size ());
            while (iter.hasNext ())
            {
                converted.add (((Class) iter.next ()).getName ().replace ('$', '.'));
            }
            return converted;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Gets a Collection of the classes that have an attribute of the specified class.
     * The Collection contains the class names (String).
     *
     * @deprecated Use the getClasses(Class) method instead.
     */
    public Collection getClassesWithAttribute (Class attributeClass)
    {
        return getClassesWithAttribute (attributeClass.getName ());
    }

    /**
     * Gets a Collection of the <code>Class</code>es that have an attribute of the specified class.
     * The Collection contains the classes (Class).
     *
     * @since 2.1
     */
    public Collection getClasses (Class attributeClass)
    {
        if (index.containsKey (attributeClass.getName ()))
        {
            return ((IndexNode) index.get (attributeClass.getName ())).classes;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Gets a Collection of the <code>Method</code>s that have an attribute of the specified class.
     * The Collection contains the methods (java.lang.reflect.Method).
     *
     * @since 2.1
     */
    public Collection getMethods (Class attributeClass)
    {
        if (index.containsKey (attributeClass.getName ()))
        {
            return ((IndexNode) index.get (attributeClass.getName ())).methods;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Gets a Collection of the <code>Method</code>s whose return value has an attribute of the specified class.
     * The Collection contains the methods (java.lang.reflect.Method).
     *
     * @since 2.1
     */
    public Collection getMethodsReturning (Class attributeClass)
    {
        if (index.containsKey (attributeClass.getName ()))
        {
            return ((IndexNode) index.get (attributeClass.getName ())).returnValues;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Gets a Collection of the <code>Field</code>s that have an attribute of the specified class.
     * The Collection contains the methods (java.lang.reflect.Field).
     *
     * @since 2.1
     */
    public Collection getFields (Class attributeClass)
    {
        if (index.containsKey (attributeClass.getName ()))
        {
            return ((IndexNode) index.get (attributeClass.getName ())).fields;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Gets a Collection of the <code>Constructor</code>s that have an attribute of the specified class.
     * The Collection contains the methods (java.lang.reflect.Constructor).
     *
     * @since 2.1
     */
    public Collection getConstructors (Class attributeClass)
    {
        if (index.containsKey (attributeClass.getName ()))
        {
            return ((IndexNode) index.get (attributeClass.getName ())).constructors;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Gets a Collection of the <code>ConstructorParameter</code>s that have an attribute of the specified class.
     * The Collection contains the methods ({@link AttributeIndex.ConstructorParameter}).
     *
     * @since 2.1
     */
    public Collection getConstructorParameters (Class attributeClass)
    {
        if (index.containsKey (attributeClass.getName ()))
        {
            return ((IndexNode) index.get (attributeClass.getName ())).constructorParameters;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Gets a Collection of the <code>MethodParameter</code>s that have an attribute of the specified class.
     * The Collection contains the methods ({@link AttributeIndex.MethodParameter}).
     *
     * @since 2.1
     */
    public Collection getMethodParameters (Class attributeClass)
    {
        if (index.containsKey (attributeClass.getName ()))
        {
            return ((IndexNode) index.get (attributeClass.getName ())).methodParameters;
        }
        else
        {
            return Collections.EMPTY_SET;
        }
    }
}
