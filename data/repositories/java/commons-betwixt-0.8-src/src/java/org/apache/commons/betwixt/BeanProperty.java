package org.apache.commons.betwixt;

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
import java.util.Map;

import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.betwixt.expression.DynaBeanExpression;
import org.apache.commons.betwixt.expression.DynaBeanUpdater;
import org.apache.commons.betwixt.expression.Expression;
import org.apache.commons.betwixt.expression.IteratorExpression;
import org.apache.commons.betwixt.expression.MethodExpression;
import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.betwixt.expression.Updater;
import org.apache.commons.betwixt.strategy.NameMapper;
import org.apache.commons.betwixt.strategy.SimpleTypeMapper;
import org.apache.commons.betwixt.strategy.TypeBindingStrategy;
import org.apache.commons.logging.Log;

/**
  * Betwixt-centric view of a bean (or pseudo-bean) property.
  * This object decouples the way that the (possibly pseudo) property introspection
  * is performed from the results of that introspection.
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public class BeanProperty
{

    /** The bean name for the property (not null) */
    private final String propertyName;
    /** The type of this property (not null) */
    private final Class propertyType;
    /** The Expression used to read values of this property (possibly null) */
    private Expression propertyExpression;
    /** The Updater used to write values of this property (possibly null) */
    private Updater propertyUpdater;

    /**
     * Construct a BeanProperty.
     * @param propertyName not null
     * @param propertyType not null
     * @param propertyExpression the Expression used to read the property,
     * null if the property is not readable
     * @param propertyUpdater the Updater used to write the property,
     * null if the property is not writable
     */
    public BeanProperty (
        String propertyName,
        Class propertyType,
        Expression propertyExpression,
        Updater propertyUpdater)
    {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.propertyExpression = propertyExpression;
        this.propertyUpdater = propertyUpdater;
    }

    /**
     * Constructs a BeanProperty from a <code>PropertyDescriptor</code>.
     * @param descriptor not null
     */
    public BeanProperty(PropertyDescriptor descriptor)
    {
        this.propertyName = descriptor.getName();
        this.propertyType = descriptor.getPropertyType();

        Method readMethod = descriptor.getReadMethod();
        if ( readMethod != null )
        {
            this.propertyExpression = new MethodExpression( readMethod );
        }

        Method writeMethod = descriptor.getWriteMethod();
        if ( writeMethod != null )
        {
            this.propertyUpdater = new MethodUpdater( writeMethod );
        }
    }

    /**
     * Constructs a BeanProperty from a <code>DynaProperty</code>
     * @param dynaProperty not null
     */
    public BeanProperty(DynaProperty dynaProperty)
    {
        this.propertyName = dynaProperty.getName();
        this.propertyType = dynaProperty.getType();
        this.propertyExpression = new DynaBeanExpression( propertyName );
        this.propertyUpdater = new DynaBeanUpdater( propertyName, propertyType );
    }

    /**
      * Gets the bean name for this property.
      * Betwixt will map this to an xml name.
      * @return the bean name for this property, not null
      */
    public String getPropertyName()
    {
        return propertyName;
    }

    /**
      * Gets the type of this property.
      * @return the property type, not null
      */
    public Class getPropertyType()
    {
        return propertyType;
    }

    /**
      * Gets the expression used to read this property.
      * @return the expression to be used to read this property
      * or null if this property is not readable.
      */
    public Expression getPropertyExpression()
    {
        return propertyExpression;
    }

    /**
      * Gets the updater used to write to this properyty.
      * @return the Updater to the used to write to this property
      * or null if this property is not writable.
      */
    public Updater getPropertyUpdater()
    {
        return propertyUpdater;
    }

    /**
     * Create a XML descriptor from a bean one.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param configuration <code>IntrospectionConfiguration</code>, not null
     * @return a correctly configured <code>NodeDescriptor</code> for the property
     */
    public Descriptor createXMLDescriptor( IntrospectionConfiguration configuration )
    {
        Log log = configuration.getIntrospectionLog();
        if (log.isTraceEnabled())
        {
            log.trace("Creating descriptor for property: name="
                      + getPropertyName() + " type=" + getPropertyType());
        }

        NodeDescriptor descriptor = null;
        Expression propertyExpression = getPropertyExpression();
        Updater propertyUpdater = getPropertyUpdater();

        if ( propertyExpression == null )
        {
            if (log.isTraceEnabled())
            {
                log.trace( "No read method for property: name="
                           + getPropertyName() + " type=" + getPropertyType());
            }
            return null;
        }

        if ( log.isTraceEnabled() )
        {
            log.trace( "Property expression=" + propertyExpression );
        }

        // choose response from property type

        //TODO this big conditional should be replaced with subclasses based
        // on the type

        //TODO complete simple type implementation
        TypeBindingStrategy.BindingType bindingType
            = configuration.getTypeBindingStrategy().bindingType( getPropertyType() ) ;
        if ( bindingType.equals( TypeBindingStrategy.BindingType.PRIMITIVE ) )
        {
            descriptor =
                createDescriptorForPrimitive(
                    configuration,
                    propertyExpression,
                    propertyUpdater);

        }
        else if ( configuration.isLoopType( getPropertyType() ) )
        {

            if (log.isTraceEnabled())
            {
                log.trace("Loop type: " + getPropertyName());
                log.trace("Wrap in collections? " + configuration.isWrapCollectionsInElement());
            }

            if ( Map.class.isAssignableFrom( getPropertyType() ))
            {
                descriptor = createDescriptorForMap( configuration, propertyExpression );
            }
            else
            {

                descriptor
                    = createDescriptorForCollective( configuration, propertyUpdater, propertyExpression );
            }
        }
        else
        {
            if (log.isTraceEnabled())
            {
                log.trace( "Standard property: " + getPropertyName());
            }
            descriptor =
                createDescriptorForStandard(
                    propertyExpression,
                    propertyUpdater,
                    configuration);
        }



        if (log.isTraceEnabled())
        {
            log.trace( "Created descriptor:" );
            log.trace( descriptor );
        }
        return descriptor;
    }

    /**
     * Configures descriptor (in the standard way).
     * This sets the common properties.
     *
     * @param propertyName the name of the property mapped to the Descriptor, not null
     * @param propertyType the type of the property mapped to the Descriptor, not null
     * @param descriptor Descriptor to map, not null
     * @param configuration IntrospectionConfiguration, not null
     */
    private void configureDescriptor(
        NodeDescriptor descriptor,
        IntrospectionConfiguration configuration)
    {
        NameMapper nameMapper = configuration.getElementNameMapper();
        if (descriptor instanceof AttributeDescriptor)
        {
            // we want to use the attributemapper only when it is an attribute..
            nameMapper = configuration.getAttributeNameMapper();

        }
        descriptor.setLocalName( nameMapper.mapTypeToElementName( propertyName ));
        descriptor.setPropertyName( getPropertyName() );
        descriptor.setPropertyType( getPropertyType() );
    }

    /**
     * Creates an <code>ElementDescriptor</code> for a standard property
     * @param propertyExpression
     * @param propertyUpdater
     * @return
     */
    private ElementDescriptor createDescriptorForStandard(
        Expression propertyExpression,
        Updater propertyUpdater,
        IntrospectionConfiguration configuration)
    {

        ElementDescriptor result;

        ElementDescriptor elementDescriptor = new ElementDescriptor();
        elementDescriptor.setContextExpression( propertyExpression );
        if ( propertyUpdater != null )
        {
            elementDescriptor.setUpdater( propertyUpdater );
        }

        elementDescriptor.setHollow(true);

        result = elementDescriptor;

        configureDescriptor(result, configuration);
        return result;
    }

    /**
     * Creates an ElementDescriptor for an <code>Map</code> type property
     * @param configuration
     * @param propertyExpression
     * @return
     */
    private ElementDescriptor createDescriptorForMap(
        IntrospectionConfiguration configuration,
        Expression propertyExpression)
    {

        //TODO: need to clean the element descriptors so that the wrappers are plain
        ElementDescriptor result;

        ElementDescriptor entryDescriptor = new ElementDescriptor();
        entryDescriptor.setContextExpression(
            new IteratorExpression( propertyExpression )
        );

        entryDescriptor.setLocalName( "entry" );
        entryDescriptor.setPropertyName( getPropertyName() );
        entryDescriptor.setPropertyType( getPropertyType() );

        // add elements for reading
        ElementDescriptor keyDescriptor = new ElementDescriptor( "key" );
        keyDescriptor.setHollow( true );
        entryDescriptor.addElementDescriptor( keyDescriptor );

        ElementDescriptor valueDescriptor = new ElementDescriptor( "value" );
        valueDescriptor.setHollow( true );
        entryDescriptor.addElementDescriptor( valueDescriptor );


        if ( configuration.isWrapCollectionsInElement() )
        {
            ElementDescriptor wrappingDescriptor = new ElementDescriptor();
            wrappingDescriptor.setElementDescriptors( new ElementDescriptor[] { entryDescriptor } );
            NameMapper nameMapper = configuration.getElementNameMapper();
            wrappingDescriptor.setLocalName( nameMapper.mapTypeToElementName( propertyName ));
            result = wrappingDescriptor;

        }
        else
        {
            result = entryDescriptor;
        }
        result.setCollective(true);
        return result;
    }

    /**
     * Creates an <code>ElementDescriptor</code> for a collective type property
     * @param configuration
     * @param propertyUpdater, <code>Updater</code> for the property, possibly null
     * @param propertyExpression
     * @return
     */
    private ElementDescriptor createDescriptorForCollective(
        IntrospectionConfiguration configuration,
        Updater propertyUpdater,
        Expression propertyExpression)
    {

        ElementDescriptor result;

        ElementDescriptor loopDescriptor = new ElementDescriptor();
        loopDescriptor.setContextExpression(
            new IteratorExpression( propertyExpression )
        );
        loopDescriptor.setPropertyName(getPropertyName());
        loopDescriptor.setPropertyType(getPropertyType());
        loopDescriptor.setHollow(true);
        // set the property updater (if it exists)
        // may be overridden later by the adder
        loopDescriptor.setUpdater(propertyUpdater);
        loopDescriptor.setCollective(true);

        if ( configuration.isWrapCollectionsInElement() )
        {
            // create wrapping desctiptor
            ElementDescriptor wrappingDescriptor = new ElementDescriptor();
            wrappingDescriptor.setElementDescriptors( new ElementDescriptor[] { loopDescriptor } );
            wrappingDescriptor.setLocalName(
                configuration.getElementNameMapper().mapTypeToElementName( propertyName ));
            result = wrappingDescriptor;

        }
        else
        {
            // unwrapped Descriptor
            result = loopDescriptor;
        }
        return result;
    }

    /**
     * Creates a NodeDescriptor for a primitive type node
     * @param configuration
     * @param name
     * @param log
     * @param propertyExpression
     * @param propertyUpdater
     * @return
     */
    private NodeDescriptor createDescriptorForPrimitive(
        IntrospectionConfiguration configuration,
        Expression propertyExpression,
        Updater propertyUpdater)
    {
        Log log = configuration.getIntrospectionLog();
        NodeDescriptor descriptor;
        if (log.isTraceEnabled())
        {
            log.trace( "Primitive type: " + getPropertyName());
        }
        SimpleTypeMapper.Binding binding
            = configuration.getSimpleTypeMapper().bind(
                  propertyName,
                  propertyType,
                  configuration);
        if ( SimpleTypeMapper.Binding.ATTRIBUTE.equals( binding ))
        {
            if (log.isTraceEnabled())
            {
                log.trace( "Adding property as attribute: " + getPropertyName() );
            }
            descriptor = new AttributeDescriptor();
        }
        else
        {
            if (log.isTraceEnabled())
            {
                log.trace( "Adding property as element: " + getPropertyName() );
            }
            descriptor = new ElementDescriptor();
        }
        descriptor.setTextExpression( propertyExpression );
        if ( propertyUpdater != null )
        {
            descriptor.setUpdater( propertyUpdater );
        }
        configureDescriptor(descriptor, configuration);
        return descriptor;
    }
}
