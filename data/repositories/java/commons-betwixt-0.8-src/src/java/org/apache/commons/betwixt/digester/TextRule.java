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

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.TextDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.expression.ConstantExpression;
import org.apache.commons.betwixt.expression.MethodExpression;
import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
  * <p>Rule for parsing &lt;text&gt; elements.
  * These allow mixed content text to be specified.
  * A mixed content element example:
  * <pre>
  *     &lt;foo&gt;text&lt;bar/&gt;&lt;/foo&gt;
  * </pre>
  * </p>
  *
  * @author Robert Burrell Donkin
  * @version $Id: TextRule.java 438373 2006-08-30 05:17:21Z bayard $
  */
public class TextRule extends MappedPropertyRule
{

    /** Logger */
    private static final Log log = LogFactory.getLog( TextRule.class );
    /** Base constructor */
    public TextRule() {}

    // Rule interface
    //-------------------------------------------------------------------------

    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     * @throws SAXException 1. If this tag's parent is not an element tag.
     * 2. If this tag has a value attribute together with either a property
     * or type attribute.
     */
    public void begin(String name, String namespace, Attributes attributes) throws SAXException
    {

        TextDescriptor descriptor = new TextDescriptor();

        String value = attributes.getValue( "value" );
        String propertyName = attributes.getValue( "property" );
        String propertyType = attributes.getValue( "type" );

        if ( value != null)
        {
            if ( propertyName != null || propertyType != null )
            {
                // not allowed
                throw new SAXException(
                    "You cannot specify attribute 'value' together with either "
                    + " the 'property' or 'type' attributes");
            }
            // fixed value text
            descriptor.setTextExpression( new ConstantExpression( value ) );

        }
        else
        {
            // property based text
            descriptor.setPropertyName( propertyName );

            Class beanClass = getBeanClass();

            // set the property type using reflection
            descriptor.setPropertyType(
                getPropertyType( propertyType, beanClass, propertyName )
            );

            if ( beanClass != null )
            {
                String descriptorPropertyName = descriptor.getPropertyName();
                PropertyDescriptor propertyDescriptor =
                    getPropertyDescriptor( beanClass, descriptorPropertyName );
                if ( propertyDescriptor != null )
                {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    descriptor.setTextExpression( new MethodExpression( readMethod ) );
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    if (writeMethod != null)
                    {
                        descriptor.setUpdater( new MethodUpdater(writeMethod));
                    }
                    getProcessedPropertyNameSet().add( descriptorPropertyName );
                }
            }
        }

        Object top = digester.peek();
        if ( top instanceof XMLBeanInfo )
        {
            XMLBeanInfo beanInfo = (XMLBeanInfo) top;
            ElementDescriptor elementDescriptor = beanInfo.getElementDescriptor();
            if (elementDescriptor != null)
            {
                elementDescriptor.addContentDescriptor( descriptor );
            }

        }
        else if ( top instanceof ElementDescriptor )
        {
            ElementDescriptor parent = (ElementDescriptor) top;
            parent.addContentDescriptor( descriptor );

        }
        else
        {
            throw new SAXException( "Invalid use of <text>. It should "
                                    + "be nested <text> nodes" );
        }
    }
}
