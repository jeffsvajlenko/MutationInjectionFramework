/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.betwixt.io.read;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.logging.Log;

/**
  * Group of factory methods for <code>ChainedBeanCreator</code>'s.
  * The standard implementations used by Betwixt are present here.
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public class ChainedBeanCreatorFactory
{

    private static final Class[] EMPTY_CLASS_ARRAY = {};
    private static final Object[] EMPTY_OBJECT_ARRAY = {};

    /** Singleton instance for creating derived beans */
    private static final ChainedBeanCreator derivedBeanCreator
        = new ChainedBeanCreator()
    {
        public Object create(
            ElementMapping elementMapping,
            ReadContext context,
            BeanCreationChain chain)
        {

            Log log = context.getLog();
            String className
                = elementMapping
                  .getAttributes().getValue( context.getClassNameAttribute() );
            if ( className != null )
            {
                try
                {
                    // load the class we should instantiate
                    ClassLoader classLoader = context.getClassLoader();
                    Class clazz = null;
                    if ( classLoader == null )
                    {
                        log.warn("Read context classloader not set." );
                    }
                    else
                    {
                        try
                        {
                            clazz = classLoader.loadClass( className );
                        }
                        catch (ClassNotFoundException e)
                        {
                            log.info("Class not found in context classloader:");
                            log.debug(clazz, e);
                        }
                    }
                    if (clazz == null)
                    {
                        clazz = Class.forName(className);
                    }
                    return newInstance(clazz, log);

                }
                catch (Exception e)
                {
                    // it would be nice to have a pluggable strategy for exception management
                    log.warn( "Could not create instance of type: " + className );
                    log.debug( "Create new instance failed: ", e );
                    return null;
                }

            }
            else
            {
                // pass responsibility down the chain
                return chain.create( elementMapping, context );
            }
        }
    };

    /**
      * Creates a <code>ChainedBeanCreator</code> that constructs derived beans.
      * These have their classname set by an xml attribute.
      * @return <code>ChainedBeanCreator</code> that implements Derived beans logic, not null
      */
    public static final ChainedBeanCreator createDerivedBeanCreator()
    {
        return derivedBeanCreator;
    }

    /**
     * Constructs a new instance of the given class.
     * Access is forced.
     * @param theClass <code>Class</code>, not null
     * @param log <code>Log</code>, not null
     * @return <code>Object</code>, an instance of the given class
     * @throws Exception
     */
    private static final Object newInstance(Class theClass, Log log) throws Exception
    {
        Object result = null;
        try
        {
            Constructor constructor = theClass.getConstructor(EMPTY_CLASS_ARRAY);
            if (!constructor.isAccessible())
            {
                constructor.setAccessible(true);
            }
            result = constructor.newInstance(EMPTY_OBJECT_ARRAY);
        }
        catch (SecurityException e)
        {
            log.debug("Cannot force accessibility to constructor", e);

        }
        catch (NoSuchMethodException e)
        {
            if (log.isDebugEnabled())
            {
                log.debug("Class " + theClass + " has no empty constructor.");
            }
        }

        if (result == null)
        {
            result = theClass.newInstance();
        }
        return result;
    }

    /** Singleton instance that creates beans based on type */
    private static final ChainedBeanCreator elementTypeBeanCreator
        = new ChainedBeanCreator()
    {
        public Object create(
            ElementMapping element,
            ReadContext context,
            BeanCreationChain chain)
        {

            Log log = context.getLog();
            Class theClass = null;

            ElementDescriptor descriptor = element.getDescriptor();
            if ( descriptor != null )
            {
                // check for polymorphism
                theClass = context.resolvePolymorphicType(element);

                if (theClass == null)
                {
                    // created based on implementation class
                    theClass = descriptor.getImplementationClass();
                }
            }

            if ( theClass == null )
            {
                // create based on type
                theClass = element.getType();
            }

            if (descriptor != null && descriptor.isPolymorphic())
            {
                // check that the type is suitably named
                try
                {
                    XMLBeanInfo xmlBeanInfo = context.getXMLIntrospector().introspect(theClass);
                    String namespace = element.getNamespace();
                    String name = element.getName();
                    if (namespace == null)
                    {
                        if (!name.equals(xmlBeanInfo.getElementDescriptor().getQualifiedName()))
                        {
                            context.getLog().debug("Polymorphic type does not match element");
                            return null;
                        }
                    }
                    else if (!namespace.equals(xmlBeanInfo.getElementDescriptor().getURI())
                             || !name.equals(xmlBeanInfo.getElementDescriptor().getLocalName()))
                    {
                        context.getLog().debug("Polymorphic type does not match element");
                        return null;
                    }
                }
                catch (IntrospectionException e)
                {
                    context.getLog().warn(
                        "Could not introspect type to test introspection: " + theClass.getName() );
                    context.getLog().debug( "Introspection failed: ", e );
                    return null;
                }

            }

            if ( log.isTraceEnabled() )
            {
                log.trace(
                    "Creating instance of class " + theClass.getName()
                    + " for element " + element.getName());
            }

            try
            {

                Object result = newInstance(theClass, log);
                return result;

            }
            catch (Exception e)
            {
                // it would be nice to have a pluggable strategy for exception management
                context.getLog().warn(
                    "Could not create instance of type: " + theClass.getName() );
                context.getLog().debug( "Create new instance failed: ", e );
                return null;
            }
        }
    };

    /**
      * Creates a <code>ChainedBeanCreator</code> that constructs beans based on element type.
      * @return <code>ChainedBeanCreator</code> that implements load by type beans logic, not null
      */
    public static final ChainedBeanCreator createElementTypeBeanCreator()
    {
        return elementTypeBeanCreator;
    }

    /** Singleton instance that creates beans based on IDREF */
    private static final ChainedBeanCreator idRefBeanCreator
        = new ChainedBeanCreator()
    {
        public Object create(
            ElementMapping elementMapping,
            ReadContext context,
            BeanCreationChain chain)
        {
            if ( context.getMapIDs() )
            {
                String idref = elementMapping.getAttributes().getValue( "idref" );
                if ( idref != null )
                {
                    // XXX need to check up about ordering
                    // XXX this is a very simple system that assumes that
                    // XXX id occurs before idrefs
                    // XXX would need some thought about how to implement a fuller system
                    context.getLog().trace( "Found IDREF" );
                    Object bean = context.getBean( idref );
                    if ( bean != null )
                    {
                        if ( context.getLog().isTraceEnabled() )
                        {
                            context.getLog().trace( "Matched bean " + bean );
                        }
                        return bean;
                    }
                    context.getLog().trace( "No match found" );
                }
            }
            return chain.create( elementMapping, context );
        }
    };

    /**
      * Creates a <code>ChainedBeanCreator</code> that finds existing beans based on their IDREF.
      * @return <code>ChainedBeanCreator</code> that implements IDREF beans logic, not null
      */
    public static final ChainedBeanCreator createIDREFBeanCreator()
    {
        return idRefBeanCreator;
    }
}
