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
package org.apache.commons.betwixt.digester;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** <p>Factors out common code used by Betwixt rules that access bean properties.
  * Maybe a lot of this should be moved into <code>BeanUtils</code>.</p>
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public abstract class MappedPropertyRule extends RuleSupport
{

    /** Logger */
    private static final Log log = LogFactory.getLog( MappedPropertyRule.class );
    /** Classloader used to load classes by name */
    private ClassLoader classLoader;
    /** Base constructor */
    public MappedPropertyRule()
    {
        this.classLoader = getClass().getClassLoader();
    }



    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Returns the property descriptor for the class and property name.
     * Note that some caching could be used to improve performance of
     * this method. Or this method could be added to PropertyUtils.
     *
     * @param beanClass descriptor for property in this class
     * @param propertyName descriptor for property with this name
     * @return property descriptor for the named property in the given class
     */
    protected PropertyDescriptor getPropertyDescriptor( Class beanClass,
            String propertyName )
    {
        if ( beanClass != null && propertyName != null )
        {
            if (log.isTraceEnabled())
            {
                log.trace("Searching for property " + propertyName + " on " + beanClass);
            }
            try
            {
                // TODO: replace this call to introspector to an object call
                // which finds all property descriptors for a class
                // this allows extra property descriptors to be added
                BeanInfo beanInfo;
                if( getXMLIntrospector().getConfiguration().ignoreAllBeanInfo() )
                {
                    beanInfo = Introspector.getBeanInfo( beanClass, Introspector.IGNORE_ALL_BEANINFO );
                }
                else
                {
                    beanInfo = Introspector.getBeanInfo( beanClass );
                }
                PropertyDescriptor[] descriptors =
                    beanInfo.getPropertyDescriptors();
                if ( descriptors != null )
                {
                    for ( int i = 0, size = descriptors.length; i < size; i++ )
                    {
                        PropertyDescriptor descriptor = descriptors[i];
                        if ( propertyName.equals( descriptor.getName() ) )
                        {
                            log.trace("Found matching method.");
                            return descriptor;
                        }
                    }
                }
                // for interfaces, check all super interfaces
                if (beanClass.isInterface())
                {
                    Class[] superinterfaces = beanClass.getInterfaces();
                    for (int i=0, size=superinterfaces.length; i<size; i++)
                    {
                        PropertyDescriptor descriptor = getPropertyDescriptor(superinterfaces[i], propertyName);
                        if (descriptor != null)
                        {
                            return descriptor;
                        }
                    }
                }

                log.trace("No match found.");
                return null;
            }
            catch (Exception e)
            {
                log.warn( "Caught introspection exception", e );
            }
        }
        return null;
    }


    /**
     * Gets the type of a property
     *
     * @param propertyClassName class name for property type (may be null)
     * @param beanClass class that has property
     * @param propertyName the name of the property whose type is to be determined
     * @return property type
     */
    protected Class getPropertyType( String propertyClassName,
                                     Class beanClass, String propertyName )
    {
        // XXX: should use a ClassLoader to handle
        //      complex class loading situations
        if ( propertyClassName != null )
        {
            try
            {
                Class answer = classLoader.loadClass(propertyClassName);
                if (answer != null)
                {
                    if (log.isTraceEnabled())
                    {
                        log.trace("Used specified type " + answer);
                    }
                    return answer;
                }
            }
            catch (Exception e)
            {
                log.warn("Cannot load specified type", e);
            }
        }

        PropertyDescriptor descriptor =
            getPropertyDescriptor( beanClass, propertyName );
        if ( descriptor != null )
        {
            return descriptor.getPropertyType();
        }

        if (log.isTraceEnabled())
        {
            log.trace("Cannot find property type.");
            log.trace("  className=" + propertyClassName
                      + " base=" + beanClass + " name=" + propertyName);
        }
        return null;
    }
}
