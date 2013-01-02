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
package org.apache.commons.betwixt.io;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.digester.XMLIntrospectorHelper;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.betwixt.expression.Updater;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.Rules;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/** <p><code>BeanCreateRule</code> is a Digester Rule for creating beans
  * from the betwixt XML metadata.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
  * @deprecated 0.5 this Rule does not allowed good integration with other Rules -
  * use {@link BeanRuleSet} instead.
  */
public class BeanCreateRule extends Rule
{

    /** Logger */
    private static Log log = LogFactory.getLog( BeanCreateRule.class );

    /**
     * Set log to be used by <code>BeanCreateRule</code> instances
     * @param aLog the <code>Log</code> implementation for this class to log to
     */
    public static void setLog(Log aLog)
    {
        log = aLog;
    }

    /** The descriptor of this element */
    private ElementDescriptor descriptor;
    /** The Context used when evaluating Updaters */
    private Context context;
    /** Have we added our child rules to the digester? */
    private boolean addedChildren;
    /** In this begin-end loop did we actually create a new bean */
    private boolean createdBean;
    /** The type of the bean to create */
    private Class beanClass;
    /** The prefix added to digester rules */
    private String pathPrefix;
    /** Use id's to match beans? */
    private boolean matchIDs = true;
    /** allows an attribute to be specified to overload the types of beans used */
    private String classNameAttribute = "className";

    /**
     * Convenience constructor which uses <code>ID's</code> for matching.
     *
     * @param descriptor the <code>ElementDescriptor</code> describing the element mapped
     * @param beanClass the <code>Class</code> to be created
     * @param pathPrefix the digester style path
     */
    public BeanCreateRule(
        ElementDescriptor descriptor,
        Class beanClass,
        String pathPrefix )
    {
        this( descriptor, beanClass, pathPrefix, true );
    }

    /**
     * Constructor taking a class.
     *
     * @param descriptor the <code>ElementDescriptor</code> describing the element mapped
     * @param beanClass the <code>Class</code> to be created
     * @param pathPrefix the digester style path
     * @param matchIDs should <code>ID</code>/<code>IDREF</code>'s be used for matching
     */
    public BeanCreateRule(
        ElementDescriptor descriptor,
        Class beanClass,
        String pathPrefix,
        boolean matchIDs )
    {
        this(
            descriptor,
            beanClass,
            new Context(),
            pathPrefix,
            matchIDs);
    }

    /**
     * Convenience constructor which uses <code>ID's</code> for matching.
     *
     * @param descriptor the <code>ElementDescriptor</code> describing the element mapped
     * @param beanClass the <code>Class</code> to be created
     */
    public BeanCreateRule( ElementDescriptor descriptor, Class beanClass )
    {
        this( descriptor, beanClass, true );
    }

    /**
     * Constructor uses standard qualified name.
     *
     * @param descriptor the <code>ElementDescriptor</code> describing the element mapped
     * @param beanClass the <code>Class</code> to be created
     * @param matchIDs should <code>ID</code>/<code>IDREF</code>'s be used for matching
     */
    public BeanCreateRule( ElementDescriptor descriptor, Class beanClass, boolean matchIDs )
    {
        this( descriptor, beanClass, descriptor.getQualifiedName() + "/" , matchIDs );
    }

    /**
     * Convenience constructor which uses <code>ID's</code> for match.
     *
     * @param descriptor the <code>ElementDescriptor</code> describing the element mapped
     * @param context the <code>Context</code> to be used to evaluate expressions
     * @param pathPrefix the digester path prefix
     */
    public BeanCreateRule(
        ElementDescriptor descriptor,
        Context context,
        String pathPrefix )
    {
        this( descriptor, context, pathPrefix, true );
    }

    /**
     * Constructor taking a context.
     *
     * @param descriptor the <code>ElementDescriptor</code> describing the element mapped
     * @param context the <code>Context</code> to be used to evaluate expressions
     * @param pathPrefix the digester path prefix
     * @param matchIDs should <code>ID</code>/<code>IDREF</code>'s be used for matching
     */
    public BeanCreateRule(
        ElementDescriptor descriptor,
        Context context,
        String pathPrefix,
        boolean matchIDs )
    {
        this(
            descriptor,
            descriptor.getSingularPropertyType(),
            context,
            pathPrefix,
            matchIDs );
    }

    /**
     * Base constructor (used by other constructors).
     *
     * @param descriptor the <code>ElementDescriptor</code> describing the element mapped
     * @param beanClass the <code>Class</code> of the bean to be created
     * @param context the <code>Context</code> to be used to evaluate expressions
     * @param pathPrefix the digester path prefix
     * @param matchIDs should <code>ID</code>/<code>IDREF</code>'s be used for matching
     */
    private BeanCreateRule(
        ElementDescriptor descriptor,
        Class beanClass,
        Context context,
        String pathPrefix,
        boolean matchIDs )
    {
        this.descriptor = descriptor;
        this.context = context;
        this.beanClass = beanClass;
        this.pathPrefix = pathPrefix;
        this.matchIDs = matchIDs;
        if (log.isTraceEnabled())
        {
            log.trace("Created bean create rule");
            log.trace("Descriptor=" + descriptor);
            log.trace("Class=" + beanClass);
            log.trace("Path prefix=" + pathPrefix);
        }
    }



    // Rule interface
    //-------------------------------------------------------------------------

    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     */
    public void begin(Attributes attributes)
    {
        log.debug( "Called with descriptor: " + descriptor
                   + " propertyType: " + descriptor.getPropertyType() );

        if (log.isTraceEnabled())
        {
            int attributesLength = attributes.getLength();
            if (attributesLength > 0)
            {
                log.trace("Attributes:");
            }
            for (int i=0, size=attributesLength; i<size; i++)
            {
                log.trace("Local:" + attributes.getLocalName(i));
                log.trace("URI:" + attributes.getURI(i));
                log.trace("QName:" + attributes.getQName(i));
            }
        }



        // XXX: if a single rule instance gets reused and nesting occurs
        // XXX: we should probably use a stack of booleans to test if we created a bean
        // XXX: or let digester take nulls, which would be easier for us ;-)
        createdBean = false;

        Object instance = null;
        if ( beanClass != null )
        {
            instance = createBean(attributes);
            if ( instance != null )
            {
                createdBean = true;

                context.setBean( instance );
                digester.push(instance);


                // if we are a reference to a type we should lookup the original
                // as this ElementDescriptor will be 'hollow' and have no child attributes/elements.
                // XXX: this should probably be done by the NodeDescriptors...
                ElementDescriptor typeDescriptor = getElementDescriptor( descriptor );
                //ElementDescriptor typeDescriptor = descriptor;

                // iterate through all attributes
                AttributeDescriptor[] attributeDescriptors
                    = typeDescriptor.getAttributeDescriptors();
                if ( attributeDescriptors != null )
                {
                    for ( int i = 0, size = attributeDescriptors.length; i < size; i++ )
                    {
                        AttributeDescriptor attributeDescriptor = attributeDescriptors[i];

                        // The following isn't really the right way to find the attribute
                        // but it's quite robust.
                        // The idea is that you try both namespace and local name first
                        // and if this returns null try the qName.
                        String value = attributes.getValue(
                                           attributeDescriptor.getURI(),
                                           attributeDescriptor.getLocalName()
                                       );

                        if (value == null)
                        {
                            value = attributes.getValue(attributeDescriptor.getQualifiedName());
                        }

                        if (log.isTraceEnabled())
                        {
                            log.trace("Attr URL:" + attributeDescriptor.getURI());
                            log.trace("Attr LocalName:" + attributeDescriptor.getLocalName() );
                            log.trace(value);
                        }

                        Updater updater = attributeDescriptor.getUpdater();
                        log.trace(updater);
                        if ( updater != null && value != null )
                        {
                            updater.update( context, value );
                        }
                    }
                }

                addChildRules();

                // add bean for ID matching
                if ( matchIDs )
                {
                    // XXX need to support custom ID attribute names
                    // XXX i have a feeling that the current mechanism might need to change
                    // XXX so i'm leaving this till later
                    String id = attributes.getValue( "id" );
                    if ( id != null )
                    {
                        getBeansById().put( id, instance );
                    }
                }
            }
        }
    }

    /**
     * Process the end of this element.
     */
    public void end()
    {
        if ( createdBean )
        {

            // force any setters of the parent bean to be called for this new bean instance
            Updater updater = descriptor.getUpdater();
            Object instance = context.getBean();

            Object top = digester.pop();
            if (digester.getCount() == 0)
            {
                context.setBean(null);
            }
            else
            {
                context.setBean( digester.peek() );
            }

            if ( updater != null )
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug( "Calling updater for: " + descriptor + " with: "
                               + instance + " on bean: " + context.getBean() );
                }
                updater.update( context, instance );
            }
            else
            {
                if ( log.isDebugEnabled() )
                {
                    log.debug( "No updater for: " + descriptor + " with: "
                               + instance + " on bean: " + context.getBean() );
                }
            }
        }
    }

    /**
     * Tidy up.
     */
    public void finish() {}


    // Properties
    //-------------------------------------------------------------------------


    /**
     * The name of the attribute which can be specified in the XML to override the
     * type of a bean used at a certain point in the schema.
     *
     * <p>The default value is 'className'.</p>
     *
     * @return The name of the attribute used to overload the class name of a bean
     */
    public String getClassNameAttribute()
    {
        return classNameAttribute;
    }

    /**
     * Sets the name of the attribute which can be specified in
     * the XML to override the type of a bean used at a certain
     * point in the schema.
     *
     * <p>The default value is 'className'.</p>
     *
     * @param classNameAttribute The name of the attribute used to overload the class name of a bean
     */
    public void setClassNameAttribute(String classNameAttribute)
    {
        this.classNameAttribute = classNameAttribute;
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Factory method to create new bean instances
     *
     * @param attributes the <code>Attributes</code> used to match <code>ID/IDREF</code>
     * @return the created bean
     */
    protected Object createBean(Attributes attributes)
    {
        //
        // See if we've got an IDREF
        //
        // XXX This should be customizable but i'm not really convinced by the existing system
        // XXX maybe it's going to have to change so i'll use 'idref' for nows
        //
        if ( matchIDs )
        {
            String idref = attributes.getValue( "idref" );
            if ( idref != null )
            {
                // XXX need to check up about ordering
                // XXX this is a very simple system that assumes that id occurs before idrefs
                // XXX would need some thought about how to implement a fuller system
                log.trace( "Found IDREF" );
                Object bean = getBeansById().get( idref );
                if ( bean != null )
                {
                    if (log.isTraceEnabled())
                    {
                        log.trace( "Matched bean " + bean );
                    }
                    return bean;
                }
                log.trace( "No match found" );
            }
        }

        Class theClass = beanClass;
        try
        {

            String className = attributes.getValue(classNameAttribute);
            if (className != null)
            {
                // load the class we should instantiate
                theClass = getDigester().getClassLoader().loadClass(className);
            }
            if (log.isTraceEnabled())
            {
                log.trace( "Creating instance of " + theClass );
            }
            return theClass.newInstance();

        }
        catch (Exception e)
        {
            log.warn( "Could not create instance of type: " + theClass.getName() );
            return null;
        }
    }

    /** Adds the rules to the digester for all child elements */
    protected void addChildRules()
    {
        if ( ! addedChildren )
        {
            addedChildren = true;

            addChildRules( pathPrefix, descriptor );
        }
    }

    /**
     * Add child rules for given descriptor at given prefix
     *
     * @param prefix add child rules at this (digester) path prefix
     * @param currentDescriptor add child rules for this descriptor
     */
    protected void addChildRules(String prefix, ElementDescriptor currentDescriptor )
    {

        if (log.isTraceEnabled())
        {
            log.trace("Adding child rules for " + currentDescriptor + "@" + prefix);
        }

        // if we are a reference to a type we should lookup the original
        // as this ElementDescriptor will be 'hollow' and have no child attributes/elements.
        // XXX: this should probably be done by the NodeDescriptors...
        ElementDescriptor typeDescriptor = getElementDescriptor( currentDescriptor );
        //ElementDescriptor typeDescriptor = descriptor;


        ElementDescriptor[] childDescriptors = typeDescriptor.getElementDescriptors();
        if ( childDescriptors != null )
        {
            for ( int i = 0, size = childDescriptors.length; i < size; i++ )
            {
                final ElementDescriptor childDescriptor = childDescriptors[i];
                if (log.isTraceEnabled())
                {
                    log.trace("Processing child " + childDescriptor);
                }

                String qualifiedName = childDescriptor.getQualifiedName();
                if ( qualifiedName == null )
                {
                    log.trace( "Ignoring" );
                    continue;
                }
                String path = prefix + qualifiedName;
                // this code is for making sure that recursive elements
                // can also be used..

                if ( qualifiedName.equals( currentDescriptor.getQualifiedName() )
                        && currentDescriptor.getPropertyName() != null )
                {
                    log.trace("Creating generic rule for recursive elements");
                    int index = -1;
                    if (childDescriptor.isWrapCollectionsInElement())
                    {
                        index = prefix.indexOf(qualifiedName);
                        if (index == -1)
                        {
                            // shouldn't happen..
                            log.debug( "Oops - this shouldn't happen" );
                            continue;
                        }
                        int removeSlash = prefix.endsWith("/")?1:0;
                        path = "*/" + prefix.substring(index, prefix.length()-removeSlash);
                    }
                    else
                    {
                        // we have a element/element type of thing..
                        ElementDescriptor[] desc = currentDescriptor.getElementDescriptors();
                        if (desc.length == 1)
                        {
                            path = "*/"+desc[0].getQualifiedName();
                        }
                    }
                    Rule rule = new BeanCreateRule( childDescriptor, context, path, matchIDs);
                    addRule(path, rule);
                    continue;
                }
                if ( childDescriptor.getUpdater() != null )
                {
                    if (log.isTraceEnabled())
                    {
                        log.trace("Element has updater "
                                  + ((MethodUpdater) childDescriptor.getUpdater()).getMethod().getName());
                    }
                    if ( childDescriptor.isPrimitiveType() )
                    {
                        addPrimitiveTypeRule(path, childDescriptor);

                    }
                    else
                    {
                        // add the first child to the path
                        ElementDescriptor[] grandChildren = childDescriptor.getElementDescriptors();
                        if ( grandChildren != null && grandChildren.length > 0 )
                        {
                            ElementDescriptor grandChild = grandChildren[0];
                            String grandChildQName = grandChild.getQualifiedName();
                            if ( grandChildQName != null && grandChildQName.length() > 0 )
                            {
                                if (childDescriptor.isWrapCollectionsInElement())
                                {
                                    path += '/' + grandChildQName;

                                }
                                else
                                {
                                    path = prefix + (prefix.endsWith("/")?"":"/") + grandChildQName;
                                }
                            }
                        }

                        // maybe we are adding a primitve type to a collection/array
                        Class beanClass = childDescriptor.getSingularPropertyType();
                        if ( XMLIntrospectorHelper.isPrimitiveType( beanClass ) )
                        {
                            addPrimitiveTypeRule(path, childDescriptor);

                        }
                        else
                        {
                            Rule rule = new BeanCreateRule(
                                childDescriptor,
                                context,
                                path + '/',
                                matchIDs );
                            addRule( path, rule );
                        }
                    }
                }
                else
                {
                    log.trace("Element does not have updater");
                }

                ElementDescriptor[] grandChildren = childDescriptor.getElementDescriptors();
                if ( grandChildren != null && grandChildren.length > 0 )
                {
                    log.trace("Adding grand children");
                    addChildRules( path + '/', childDescriptor );
                }
            }
        }
    }

    /**
     * Get the associated bean reader.
     *
     * @return the <code>BeanReader</code digesting the xml
     */
    protected BeanReader getBeanReader()
    {
        // XXX this breaks the rule contact
        // XXX maybe the reader should be passed in the constructor
        return (BeanReader) getDigester();
    }

    /**
     * Allows the navigation from a reference to a property object to the descriptor defining what
     * the property is. In other words, doing the join from a reference to a type to lookup its descriptor.
     * This could be done automatically by the NodeDescriptors. Refer to TODO.txt for more info.
     *
     * @param propertyDescriptor find descriptor for property object referenced by this descriptor
     * @return descriptor for the singular property class type referenced.
     */
    protected ElementDescriptor getElementDescriptor( ElementDescriptor propertyDescriptor )
    {
        Class beanClass = propertyDescriptor.getSingularPropertyType();
        if ( beanClass != null )
        {
            XMLIntrospector introspector = getBeanReader().getXMLIntrospector();
            try
            {
                XMLBeanInfo xmlInfo = introspector.introspect( beanClass );
                return xmlInfo.getElementDescriptor();

            }
            catch (Exception e)
            {
                log.warn( "Could not introspect class: " + beanClass, e );
            }
        }
        // could not find a better descriptor so use the one we've got
        return propertyDescriptor;
    }

    /**
     * Adds a new Digester rule to process the text as a primitive type
     *
     * @param path digester path where this rule will be attached
     * @param childDescriptor update this <code>ElementDescriptor</code> with the body text
     */
    protected void addPrimitiveTypeRule(String path, final ElementDescriptor childDescriptor)
    {
        Rule rule = new Rule()
        {
            public void body(String text) throws Exception
            {
                childDescriptor.getUpdater().update( context, text );
            }
        };
        addRule( path, rule );
    }

    /**
     * Safely add a rule with given path.
     *
     * @param path the digester path to add rule at
     * @param rule the <code>Rule</code> to add
     */
    protected void addRule(String path, Rule rule)
    {
        Rules rules = digester.getRules();
        List matches = rules.match(null, path);
        if ( matches.isEmpty() )
        {
            if ( log.isDebugEnabled() )
            {
                log.debug( "Adding digester rule for path: " + path + " rule: " + rule );
            }
            digester.addRule( path, rule );

        }
        else
        {
            if ( log.isDebugEnabled() )
            {
                log.debug( "Ignoring duplicate digester rule for path: "
                           + path + " rule: " + rule );
                log.debug( "New rule (not added): " + rule );
                log.debug( "Existing rule:" + matches.get(0) );
            }
        }
    }

    /**
     * Get the map used to index beans (previously read in) by id.
     * This is stored in the evaluation context.
     *
     * @return map indexing beans created by id
     */
    protected Map getBeansById()
    {
        //
        // we need a single index for beans read in by id
        // so that we can use them for idref-matching
        // store this in the context
        //
        Map beansById = (Map) context.getVariable( "beans-index" );
        if ( beansById == null )
        {
            // lazy creation
            beansById = new HashMap();
            context.setVariable( "beans-index", beansById );
            log.trace( "Created new index-by-id map" );
        }

        return beansById;
    }

    /**
     * Return something meaningful for logging.
     *
     * @return something useful for logging
     */
    public String toString()
    {
        return "BeanCreateRule [path prefix=" + pathPrefix + " descriptor=" + descriptor + "]";
    }

}
