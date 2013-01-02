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

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.NodeDescriptor;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.expression.IteratorExpression;
import org.apache.commons.betwixt.expression.MapEntryAdder;
import org.apache.commons.betwixt.expression.MethodExpression;
import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.betwixt.strategy.PluralStemmer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
  * <p><code>XMLIntrospectorHelper</code> a helper class for
  * common code shared between the digestor and introspector.</p>
  *
  * TODO this class will be deprecated soon
  * need to move the isLoop and isPrimitiveType but probably need to
  * think about whether they need replacing with something different.
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
  *
  * @deprecated
  */
public class XMLIntrospectorHelper
{

    /** Log used for logging (Doh!) */
    protected static Log log = LogFactory.getLog( XMLIntrospectorHelper.class );

    /** Base constructor */
    public XMLIntrospectorHelper()
    {
    }

    /**
     * <p>Gets the current logging implementation.</p>
     *
     * @return current log
     */
    public static Log getLog()
    {
        return log;
    }

    /**
     * <p>Sets the current logging implementation.</p>
     *
     * @param aLog use this <code>Log</code>
     */
    public static void setLog(Log aLog)
    {
        log = aLog;
    }



    /**
     * Process a property.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param propertyDescriptor create a <code>NodeDescriptor</code> for this property
     * @param useAttributesForPrimitives write primitives as attributes (rather than elements)
     * @param introspector use this <code>XMLIntrospector</code>
     * @return a correctly configured <code>NodeDescriptor</code> for the property
     * @throws IntrospectionException when bean introspection fails
     * @deprecated 0.5 this method has been replaced by {@link XMLIntrospector#createDescriptor}
     */
    public static NodeDescriptor createDescriptor(
        PropertyDescriptor propertyDescriptor,
        boolean useAttributesForPrimitives,
        XMLIntrospector introspector
    ) throws IntrospectionException
    {
        String name = propertyDescriptor.getName();
        Class type = propertyDescriptor.getPropertyType();

        if (log.isTraceEnabled())
        {
            log.trace("Creating descriptor for property: name="
                      + name + " type=" + type);
        }

        NodeDescriptor nodeDescriptor = null;
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();

        if ( readMethod == null )
        {
            if (log.isTraceEnabled())
            {
                log.trace( "No read method for property: name="
                           + name + " type=" + type);
            }
            return null;
        }

        if ( log.isTraceEnabled() )
        {
            log.trace( "Read method=" + readMethod.getName() );
        }

        // choose response from property type

        // XXX: ignore class property ??
        if ( Class.class.equals( type ) && "class".equals( name ) )
        {
            log.trace( "Ignoring class property" );
            return null;
        }
        if ( isPrimitiveType( type ) )
        {
            if (log.isTraceEnabled())
            {
                log.trace( "Primitive type: " + name);
            }
            if ( useAttributesForPrimitives )
            {
                if (log.isTraceEnabled())
                {
                    log.trace( "Adding property as attribute: " + name );
                }
                nodeDescriptor = new AttributeDescriptor();
            }
            else
            {
                if (log.isTraceEnabled())
                {
                    log.trace( "Adding property as element: " + name );
                }
                nodeDescriptor = new ElementDescriptor(true);
            }
            nodeDescriptor.setTextExpression( new MethodExpression( readMethod ) );

            if ( writeMethod != null )
            {
                nodeDescriptor.setUpdater( new MethodUpdater( writeMethod ) );
            }
        }
        else if ( isLoopType( type ) )
        {
            if (log.isTraceEnabled())
            {
                log.trace("Loop type: " + name);
                log.trace("Wrap in collections? " + introspector.isWrapCollectionsInElement());
            }
            ElementDescriptor loopDescriptor = new ElementDescriptor();
            loopDescriptor.setContextExpression(
                new IteratorExpression( new MethodExpression( readMethod ) )
            );
            loopDescriptor.setWrapCollectionsInElement(
                introspector.isWrapCollectionsInElement());
            // XXX: need to support some kind of 'add' or handle arrays, Lists or indexed properties
            //loopDescriptor.setUpdater( new MethodUpdater( writeMethod ) );
            if ( Map.class.isAssignableFrom( type ) )
            {
                loopDescriptor.setQualifiedName( "entry" );
                // add elements for reading
                loopDescriptor.addElementDescriptor( new ElementDescriptor( "key" ) );
                loopDescriptor.addElementDescriptor( new ElementDescriptor( "value" ) );
            }

            ElementDescriptor elementDescriptor = new ElementDescriptor();
            elementDescriptor.setWrapCollectionsInElement(
                introspector.isWrapCollectionsInElement());
            elementDescriptor.setElementDescriptors( new ElementDescriptor[] { loopDescriptor } );

            nodeDescriptor = elementDescriptor;
        }
        else
        {
            if (log.isTraceEnabled())
            {
                log.trace( "Standard property: " + name);
            }
            ElementDescriptor elementDescriptor = new ElementDescriptor();
            elementDescriptor.setContextExpression( new MethodExpression( readMethod ) );
            if ( writeMethod != null )
            {
                elementDescriptor.setUpdater( new MethodUpdater( writeMethod ) );
            }

            nodeDescriptor = elementDescriptor;
        }

        if (nodeDescriptor instanceof AttributeDescriptor)
        {
            // we want to use the attributemapper only when it is an attribute..
            nodeDescriptor.setLocalName(
                introspector.getAttributeNameMapper().mapTypeToElementName( name ) );
        }
        else
        {
            nodeDescriptor.setLocalName(
                introspector.getElementNameMapper().mapTypeToElementName( name ) );
        }

        nodeDescriptor.setPropertyName( propertyDescriptor.getName() );
        nodeDescriptor.setPropertyType( type );

        // XXX: associate more bean information with the descriptor?
        //nodeDescriptor.setDisplayName( propertyDescriptor.getDisplayName() );
        //nodeDescriptor.setShortDescription( propertyDescriptor.getShortDescription() );

        if (log.isTraceEnabled())
        {
            log.trace("Created descriptor:");
            log.trace(nodeDescriptor);
        }
        return nodeDescriptor;
    }

    /**
     * Configure an <code>ElementDescriptor</code> from a <code>PropertyDescriptor</code>.
     * This uses default element updater (the write method of the property).
     *
     * @param elementDescriptor configure this <code>ElementDescriptor</code>
     * @param propertyDescriptor configure from this <code>PropertyDescriptor</code>
     * @deprecated 0.6 unused
     */
    public static void configureProperty(
        ElementDescriptor elementDescriptor,
        PropertyDescriptor propertyDescriptor )
    {

        configureProperty( elementDescriptor, propertyDescriptor, null, null);
    }

    /**
     * Configure an <code>ElementDescriptor</code> from a <code>PropertyDescriptor</code>.
     * A custom update method may be set.
     *
     * @param elementDescriptor configure this <code>ElementDescriptor</code>
     * @param propertyDescriptor configure from this <code>PropertyDescriptor</code>
     * @param updateMethodName the name of the custom updater method to user.
     * If null, then then
     * @param beanClass the <code>Class</code> from which the update method should be found.
     * This may be null only when <code>updateMethodName</code> is also null.
     * @since 0.5
     * @deprecated 0.6 moved into ElementRule
     */
    public static void configureProperty(
        ElementDescriptor elementDescriptor,
        PropertyDescriptor propertyDescriptor,
        String updateMethodName,
        Class beanClass )
    {

        Class type = propertyDescriptor.getPropertyType();
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();

        elementDescriptor.setLocalName( propertyDescriptor.getName() );
        elementDescriptor.setPropertyType( type );

        // XXX: associate more bean information with the descriptor?
        //nodeDescriptor.setDisplayName( propertyDescriptor.getDisplayName() );
        //nodeDescriptor.setShortDescription( propertyDescriptor.getShortDescription() );

        if ( readMethod == null )
        {
            log.trace( "No read method" );
            return;
        }

        if ( log.isTraceEnabled() )
        {
            log.trace( "Read method=" + readMethod.getName() );
        }

        // choose response from property type

        // XXX: ignore class property ??
        if ( Class.class.equals( type ) && "class".equals( propertyDescriptor.getName() ) )
        {
            log.trace( "Ignoring class property" );
            return;
        }
        if ( isPrimitiveType( type ) )
        {
            elementDescriptor.setTextExpression( new MethodExpression( readMethod ) );

        }
        else if ( isLoopType( type ) )
        {
            log.trace("Loop type ??");

            // don't wrap this in an extra element as its specified in the
            // XML descriptor so no need.
            elementDescriptor.setContextExpression(
                new IteratorExpression( new MethodExpression( readMethod ) )
            );

            writeMethod = null;
        }
        else
        {
            log.trace( "Standard property" );
            elementDescriptor.setContextExpression( new MethodExpression( readMethod ) );
        }

        // see if we have a custom method update name
        if (updateMethodName == null)
        {
            // set standard write method
            if ( writeMethod != null )
            {
                elementDescriptor.setUpdater( new MethodUpdater( writeMethod ) );
            }

        }
        else
        {
            // see if we can find and set the custom method
            if ( log.isTraceEnabled() )
            {
                log.trace( "Finding custom method: " );
                log.trace( "  on:" + beanClass );
                log.trace( "  name:" + updateMethodName );
            }

            Method updateMethod = null;
            Method[] methods = beanClass.getMethods();
            for ( int i = 0, size = methods.length; i < size; i++ )
            {
                Method method = methods[i];
                if ( updateMethodName.equals( method.getName() ) )
                {
                    // we have a matching name
                    // check paramters are correct
                    if (methods[i].getParameterTypes().length == 1)
                    {
                        // we'll use first match
                        updateMethod = methods[i];
                        if ( log.isTraceEnabled() )
                        {
                            log.trace("Matched method:" + updateMethod);
                        }
                        // done since we're using the first match
                        break;
                    }
                }
            }

            if (updateMethod == null)
            {
                if ( log.isInfoEnabled() )
                {

                    log.info("No method with name '" + updateMethodName + "' found for update");
                }
            }
            else
            {

                elementDescriptor.setUpdater( new MethodUpdater( updateMethod ) );
                elementDescriptor.setSingularPropertyType( updateMethod.getParameterTypes()[0] );
                if ( log.isTraceEnabled() )
                {
                    log.trace( "Set custom updater on " + elementDescriptor);
                }
            }
        }
    }

    /**
     * Configure an <code>AttributeDescriptor</code> from a <code>PropertyDescriptor</code>
     *
     * @param attributeDescriptor configure this <code>AttributeDescriptor</code>
     * @param propertyDescriptor configure from this <code>PropertyDescriptor</code>
     * @deprecated 0.6 moved into AttributeRule
     */
    public static void configureProperty(
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

        // XXX: ignore class property ??
        if ( Class.class.equals( type ) && "class".equals( propertyDescriptor.getName() ) )
        {
            log.trace( "Ignoring class property" );
            return;
        }
        if ( isLoopType( type ) )
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

        attributeDescriptor.setLocalName( propertyDescriptor.getName() );
        attributeDescriptor.setPropertyType( type );

        // XXX: associate more bean information with the descriptor?
        //nodeDescriptor.setDisplayName( propertyDescriptor.getDisplayName() );
        //nodeDescriptor.setShortDescription( propertyDescriptor.getShortDescription() );
    }


    /**
     * Add any addPropety(PropertyType) methods as Updaters
     * which are often used for 1-N relationships in beans.
     * <br>
     * The tricky part here is finding which ElementDescriptor corresponds
     * to the method. e.g. a property 'items' might have an Element descriptor
     * which the method addItem() should match to.
     * <br>
     * So the algorithm we'll use
     * by default is to take the decapitalized name of the property being added
     * and find the first ElementDescriptor that matches the property starting with
     * the string. This should work for most use cases.
     * e.g. addChild() would match the children property.
     *
     * @param introspector use this <code>XMLIntrospector</code> for introspection
     * @param rootDescriptor add defaults to this descriptor
     * @param beanClass the <code>Class</code> to which descriptor corresponds
     * @deprecated 0.6 use the method in XMLIntrospector instead
     */
    public static void defaultAddMethods(
        XMLIntrospector introspector,
        ElementDescriptor rootDescriptor,
        Class beanClass )
    {
        // lets iterate over all methods looking for one of the form
        // add*(PropertyType)
        if ( beanClass != null )
        {
            Method[] methods = beanClass.getMethods();
            for ( int i = 0, size = methods.length; i < size; i++ )
            {
                Method method = methods[i];
                String name = method.getName();
                if ( name.startsWith( "add" ) )
                {
                    // XXX: should we filter out non-void returning methods?
                    // some beans will return something as a helper
                    Class[] types = method.getParameterTypes();
                    if ( types != null)
                    {
                        if ( log.isTraceEnabled() )
                        {
                            log.trace("Searching for match for " + method);
                        }

                        if ( ( types.length == 1 ) || types.length == 2 )
                        {
                            String propertyName = Introspector.decapitalize( name.substring(3) );
                            if (propertyName.length() == 0)
                                continue;
                            if ( log.isTraceEnabled() )
                            {
                                log.trace( name + "->" + propertyName );
                            }

                            // now lets try find the ElementDescriptor which displays
                            // a property which starts with propertyName
                            // and if so, we'll set a new Updater on it if there
                            // is not one already
                            ElementDescriptor descriptor =
                                findGetCollectionDescriptor(
                                    introspector,
                                    rootDescriptor,
                                    propertyName );

                            if ( log.isDebugEnabled() )
                            {
                                log.debug( "!! " + propertyName + " -> " + descriptor );
                                log.debug( "!! " + name + " -> "
                                           + (descriptor!=null?descriptor.getPropertyName():"") );
                            }
                            if ( descriptor != null )
                            {
                                boolean isMapDescriptor
                                    = Map.class.isAssignableFrom( descriptor.getPropertyType() );
                                if ( !isMapDescriptor && types.length == 1 )
                                {
                                    // this may match a standard collection or iteration
                                    log.trace("Matching collection or iteration");

                                    descriptor.setUpdater( new MethodUpdater( method ) );
                                    descriptor.setSingularPropertyType( types[0] );

                                    if ( log.isDebugEnabled() )
                                    {
                                        log.debug( "!! " + method);
                                        log.debug( "!! " + types[0]);
                                    }

                                    // is there a child element with no localName
                                    ElementDescriptor[] children
                                        = descriptor.getElementDescriptors();
                                    if ( children != null && children.length > 0 )
                                    {
                                        ElementDescriptor child = children[0];
                                        String localName = child.getLocalName();
                                        if ( localName == null || localName.length() == 0 )
                                        {
                                            child.setLocalName(
                                                introspector.getElementNameMapper()
                                                .mapTypeToElementName( propertyName ) );
                                        }
                                    }

                                }
                                else if ( isMapDescriptor && types.length == 2 )
                                {
                                    // this may match a map
                                    log.trace("Matching map");
                                    ElementDescriptor[] children
                                        = descriptor.getElementDescriptors();
                                    // see if the descriptor's been set up properly
                                    if ( children.length == 0 )
                                    {

                                        log.info(
                                            "'entry' descriptor is missing for map. "
                                            + "Updaters cannot be set");

                                    }
                                    else
                                    {
                                        // loop through grandchildren
                                        // adding updaters for key and value
                                        ElementDescriptor[] grandchildren
                                            = children[0].getElementDescriptors();
                                        MapEntryAdder adder = new MapEntryAdder(method);
                                        for (
                                            int n=0,
                                            noOfGrandChildren = grandchildren.length;
                                            n < noOfGrandChildren;
                                            n++ )
                                        {
                                            if ( "key".equals(
                                                        grandchildren[n].getLocalName() ) )
                                            {

                                                grandchildren[n].setUpdater(
                                                    adder.getKeyUpdater() );
                                                grandchildren[n].setSingularPropertyType(
                                                    types[0] );
                                                if ( log.isTraceEnabled() )
                                                {
                                                    log.trace(
                                                        "Key descriptor: " + grandchildren[n]);
                                                }

                                            }
                                            else if (
                                                "value".equals(
                                                    grandchildren[n].getLocalName() ) )
                                            {

                                                grandchildren[n].setUpdater(
                                                    adder.getValueUpdater() );
                                                grandchildren[n].setSingularPropertyType(
                                                    types[1] );
                                                if ( log.isTraceEnabled() )
                                                {
                                                    log.trace(
                                                        "Value descriptor: " + grandchildren[n]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            else
                            {
                                if ( log.isDebugEnabled() )
                                {
                                    log.debug(
                                        "Could not find an ElementDescriptor with property name: "
                                        + propertyName + " to attach the add method: " + method
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Is this a loop type class?
     *
     * @param type is this <code>Class</code> a loop type?
     * @return true if the type is a loop type, or if type is null
     * @deprecated 0.7 replaced by {@link org.apache.commons.betwixt.IntrospectionConfiguration#isLoopType(Class)}
     */
    public static boolean isLoopType(Class type)
    {
        // check for NPEs
        if (type == null)
        {
            log.trace("isLoopType: type is null");
            return false;
        }
        return type.isArray()
               || Map.class.isAssignableFrom( type )
               || Collection.class.isAssignableFrom( type )
               || Enumeration.class.isAssignableFrom( type )
               || Iterator.class.isAssignableFrom( type );
    }


    /**
     * Is this a primitive type?
     *
     * TODO: this method will probably be removed when primitive types
     * are subsumed into the simple type concept.
     * This needs moving into XMLIntrospector so that the list of simple
     * type can be varied.
     * @param type is this <code>Class<code> a primitive type?
     * @return true for primitive types
     * @deprecated 0.6 replaced by {@link org.apache.commons.betwixt.strategy.TypeBindingStrategy}
     */
    public static boolean isPrimitiveType(Class type)
    {
        if ( type == null )
        {
            return false;

        }
        else if ( type.isPrimitive() )
        {
            return true;

        }
        else if ( type.equals( Object.class ) )
        {
            return false;
        }
        return type.getName().startsWith( "java.lang." )
               || Number.class.isAssignableFrom( type )
               || String.class.isAssignableFrom( type )
               || Date.class.isAssignableFrom( type )
               || java.sql.Date.class.isAssignableFrom( type )
               || java.sql.Time.class.isAssignableFrom( type )
               || java.sql.Timestamp.class.isAssignableFrom( type )
               || java.math.BigDecimal.class.isAssignableFrom( type )
               || java.math.BigInteger.class.isAssignableFrom( type );
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Attempts to find the element descriptor for the getter property that
     * typically matches a collection or array. The property name is used
     * to match. e.g. if an addChild() method is detected the
     * descriptor for the 'children' getter property should be returned.
     *
     * @param introspector use this <code>XMLIntrospector</code>
     * @param rootDescriptor the <code>ElementDescriptor</code> whose child element will be
     * searched for a match
     * @param propertyName the name of the 'adder' method to match
     * @return <code>ElementDescriptor</code> for the matching getter
     * @deprecated 0.6 moved into XMLIntrospector
     */
    protected static ElementDescriptor findGetCollectionDescriptor(
        XMLIntrospector introspector,
        ElementDescriptor rootDescriptor,
        String propertyName )
    {
        // create the Map of propertyName -> descriptor that the PluralStemmer will choose
        Map map = new HashMap();
        //String propertyName = rootDescriptor.getPropertyName();
        if ( log.isTraceEnabled() )
        {
            log.trace( "findPluralDescriptor( " + propertyName
                       + " ):root property name=" + rootDescriptor.getPropertyName() );
        }

        if (rootDescriptor.getPropertyName() != null)
        {
            map.put(propertyName, rootDescriptor);
        }
        makeElementDescriptorMap( rootDescriptor, map );

        PluralStemmer stemmer = introspector.getPluralStemmer();
        ElementDescriptor elementDescriptor = stemmer.findPluralDescriptor( propertyName, map );

        if ( log.isTraceEnabled() )
        {
            log.trace(
                "findPluralDescriptor( " + propertyName
                + " ):ElementDescriptor=" + elementDescriptor );
        }

        return elementDescriptor;
    }

    /**
     * Creates a map where the keys are the property names and the values are the ElementDescriptors
     *
     * @param rootDescriptor the values of the maps are the children of this
     * <code>ElementDescriptor</code> index by their property names
     * @param map the map to which the elements will be added
     * @deprecated 0.6 moved into XMLIntrospector
     */
    protected static void makeElementDescriptorMap( ElementDescriptor rootDescriptor, Map map )
    {
        ElementDescriptor[] children = rootDescriptor.getElementDescriptors();
        if ( children != null )
        {
            for ( int i = 0, size = children.length; i < size; i++ )
            {
                ElementDescriptor child = children[i];
                String propertyName = child.getPropertyName();
                if ( propertyName != null )
                {
                    map.put( propertyName, child );
                }
                makeElementDescriptorMap( child, map );
            }
        }
    }

    /**
     * Traverse the tree of element descriptors and find the oldValue and swap it with the newValue.
     * This would be much easier to do if ElementDescriptor supported a parent relationship.
     *
     * @param rootDescriptor traverse child graph for this <code>ElementDescriptor</code>
     * @param oldValue replace this <code>ElementDescriptor</code>
     * @param newValue replace with this <code>ElementDescriptor</code>
     * @deprecated 0.6 now unused
     */
    protected static void swapDescriptor(
        ElementDescriptor rootDescriptor,
        ElementDescriptor oldValue,
        ElementDescriptor newValue )
    {
        ElementDescriptor[] children = rootDescriptor.getElementDescriptors();
        if ( children != null )
        {
            for ( int i = 0, size = children.length; i < size; i++ )
            {
                ElementDescriptor child = children[i];
                if ( child == oldValue )
                {
                    children[i] = newValue;
                    break;
                }
                swapDescriptor( child, oldValue, newValue );
            }
        }
    }
}
