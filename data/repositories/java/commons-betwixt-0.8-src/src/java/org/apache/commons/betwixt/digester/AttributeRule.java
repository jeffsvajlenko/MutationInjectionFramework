package org.apache.commons.betwixt.digester;

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
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLUtils;
import org.apache.commons.betwixt.expression.ConstantExpression;
import org.apache.commons.betwixt.expression.MethodExpression;
import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
  * <p><code>AttributeRule</code> the digester Rule for parsing the
  * &lt;attribute&gt; elements.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Id: AttributeRule.java 438373 2006-08-30 05:17:21Z bayard $
  */
public class AttributeRule extends RuleSupport
{

    /** Logger */
    private static final Log log = LogFactory.getLog( AttributeRule.class );
    /** This loads all classes created by name. Defaults to this class's classloader */
    private ClassLoader classLoader;
    /** The <code>Class</code> whose .betwixt file is being digested */
    private Class beanClass;

    /** Base constructor */
    public AttributeRule()
    {
        this.classLoader = getClass().getClassLoader();
    }

    // Rule interface
    //-------------------------------------------------------------------------

    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     * @throws SAXException 1. If the attribute tag is not inside an element tag.
     * 2. If the name attribute is not valid XML attribute name.
     */
    public void begin(String name, String namespace, Attributes attributes) throws SAXException
    {

        AttributeDescriptor descriptor = new AttributeDescriptor();
        String nameAttributeValue = attributes.getValue( "name" );

        // check that name is well formed
        if ( !XMLUtils.isWellFormedXMLName( nameAttributeValue ) )
        {
            throw new SAXException("'" + nameAttributeValue + "' would not be a well formed xml attribute name.");
        }

        String qName = nameAttributeValue;
        descriptor.setLocalName( nameAttributeValue );
        String uri = attributes.getValue( "uri" );
        if ( uri != null )
        {
            descriptor.setURI( uri );
            String prefix = getXMLIntrospector().getConfiguration().getPrefixMapper().getPrefix(uri);
            qName = prefix + ":" + nameAttributeValue;
        }
        descriptor.setQualifiedName( qName );

        String propertyName = attributes.getValue( "property" );
        descriptor.setPropertyName( propertyName );
        descriptor.setPropertyType( loadClass( attributes.getValue( "type" ) ) );

        if ( propertyName != null && propertyName.length() > 0 )
        {
            configureDescriptor(descriptor);
        }
        else
        {
            String value = attributes.getValue( "value" );
            if ( value != null )
            {
                descriptor.setTextExpression( new ConstantExpression( value ) );
            }
        }

        Object top = digester.peek();
        if ( top instanceof ElementDescriptor )
        {
            ElementDescriptor parent = (ElementDescriptor) top;
            parent.addAttributeDescriptor( descriptor );
        }
        else
        {
            throw new SAXException( "Invalid use of <attribute>. It should "
                                    + "be nested inside an <element> element" );
        }

        digester.push(descriptor);
    }


    /**
     * Process the end of this element.
     */
    public void end(String name, String namespace)
    {
        AttributeDescriptor descriptor = (AttributeDescriptor)digester.pop();
        ElementDescriptor parent = (ElementDescriptor)digester.peek();

        // check for attribute suppression
        if( getXMLIntrospector().getConfiguration().getAttributeSuppressionStrategy().suppress(descriptor))
        {
            parent.removeAttributeDescriptor(descriptor);
        }
    }


    // Implementation methods
    //-------------------------------------------------------------------------
    /**
     * Loads a class (using the appropriate classloader)
     *
     * @param name the name of the class to load
     * @return the class instance loaded by the appropriate classloader
     */
    protected Class loadClass( String name )
    {
        // XXX: should use a ClassLoader to handle complex class loading situations
        if ( name != null )
        {
            try
            {
                return classLoader.loadClass(name);
            }
            catch (Exception e)     // SWALLOW
            {
            }
        }
        return null;
    }

    /**
     * Set the Expression and Updater from a bean property name
     * @param attributeDescriptor configure this <code>AttributeDescriptor</code>
     * from the property with a matching name in the bean class
     */
    protected void configureDescriptor(AttributeDescriptor attributeDescriptor)
    {
        Class beanClass = getBeanClass();
        if ( beanClass != null )
        {
            String name = attributeDescriptor.getPropertyName();
            try
            {
                BeanInfo beanInfo;
                if( getXMLIntrospector().getConfiguration().ignoreAllBeanInfo() )
                {
                    beanInfo = Introspector.getBeanInfo( beanClass, Introspector.IGNORE_ALL_BEANINFO );
                }
                else
                {
                    beanInfo = Introspector.getBeanInfo( beanClass );
                }
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                if ( descriptors != null )
                {
                    for ( int i = 0, size = descriptors.length; i < size; i++ )
                    {
                        PropertyDescriptor descriptor = descriptors[i];
                        if ( name.equals( descriptor.getName() ) )
                        {
                            configureProperty( attributeDescriptor, descriptor );
                            getProcessedPropertyNameSet().add( name );
                            break;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                log.warn( "Caught introspection exception", e );
            }
        }
    }

    /**
     * Configure an <code>AttributeDescriptor</code> from a <code>PropertyDescriptor</code>
     *
     * @param attributeDescriptor configure this <code>AttributeDescriptor</code>
     * @param propertyDescriptor configure from this <code>PropertyDescriptor</code>
     */
    private void configureProperty(
        AttributeDescriptor attributeDescriptor,
        PropertyDescriptor propertyDescriptor )
    {
        Class type = propertyDescriptor.getPropertyType();
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();

        if ( readMethod == null )
        {
            log.trace( "No read method" );
            return;
        }

        if ( log.isTraceEnabled() )
        {
            log.trace( "Read method=" + readMethod );
        }

        // choose response from property type
        if ( getXMLIntrospector().isLoopType( type ) )
        {
            log.warn( "Using loop type for an attribute. Type = "
                      + type.getName() + " attribute: " + attributeDescriptor.getQualifiedName() );
        }

        log.trace( "Standard property" );
        attributeDescriptor.setTextExpression( new MethodExpression( readMethod ) );

        if ( writeMethod != null )
        {
            attributeDescriptor.setUpdater( new MethodUpdater( writeMethod ) );
        }

        attributeDescriptor.setPropertyName( propertyDescriptor.getName() );
        attributeDescriptor.setPropertyType( type );

        // XXX: associate more bean information with the descriptor?
        //nodeDescriptor.setDisplayName( propertyDescriptor.getDisplayName() );
        //nodeDescriptor.setShortDescription( propertyDescriptor.getShortDescription() );
    }

}
