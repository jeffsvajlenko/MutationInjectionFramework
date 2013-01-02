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

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaClass;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.betwixt.digester.MultiMappingBeanInfoDigester;
import org.apache.commons.betwixt.digester.XMLBeanInfoDigester;
import org.apache.commons.betwixt.digester.XMLIntrospectorHelper;
import org.apache.commons.betwixt.expression.CollectionUpdater;
import org.apache.commons.betwixt.expression.EmptyExpression;
import org.apache.commons.betwixt.expression.IteratorExpression;
import org.apache.commons.betwixt.expression.MapEntryAdder;
import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.betwixt.expression.StringExpression;
import org.apache.commons.betwixt.registry.DefaultXMLBeanInfoRegistry;
import org.apache.commons.betwixt.registry.PolymorphicReferenceResolver;
import org.apache.commons.betwixt.registry.XMLBeanInfoRegistry;
import org.apache.commons.betwixt.strategy.ClassNormalizer;
import org.apache.commons.betwixt.strategy.DefaultNameMapper;
import org.apache.commons.betwixt.strategy.DefaultPluralStemmer;
import org.apache.commons.betwixt.strategy.NameMapper;
import org.apache.commons.betwixt.strategy.PluralStemmer;
import org.apache.commons.betwixt.strategy.TypeBindingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
  * <p><code>XMLIntrospector</code> an introspector of beans to create a
  * XMLBeanInfo instance.</p>
  *
  * <p>By default, <code>XMLBeanInfo</code> caching is switched on.
  * This means that the first time that a request is made for a <code>XMLBeanInfo</code>
  * for a particular class, the <code>XMLBeanInfo</code> is cached.
  * Later requests for the same class will return the cached value.</p>
  *
  * <p>Note :</p>
  * <p>This class makes use of the <code>java.bean.Introspector</code>
  * class, which contains a BeanInfoSearchPath. To make sure betwixt can
  * do his work correctly, this searchpath is completely ignored during
  * processing. The original values will be restored after processing finished
  * </p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
  */
public class XMLIntrospector
{
    /**
     * Log used for logging (Doh!)
     * @deprecated 0.6 use the {@link #getLog()} property instead
     */
    protected Log log = LogFactory.getLog( XMLIntrospector.class );

    /** Maps classes to <code>XMLBeanInfo</code>'s */
    private XMLBeanInfoRegistry registry;

    /** Digester used to parse the XML descriptor files */
    private XMLBeanInfoDigester digester;

    /** Digester used to parse the multi-mapping XML descriptor files */
    private MultiMappingBeanInfoDigester multiMappingdigester;

    /** Configuration to be used for introspection*/
    private IntrospectionConfiguration configuration;

    /**
     * Resolves polymorphic references.
     * Though this is used only at bind time,
     * it is typically tightly couple to the xml registry.
     * It is therefore convenient to keep both references together.
     */
    private PolymorphicReferenceResolver polymorphicReferenceResolver;

    /** Base constructor */
    public XMLIntrospector()
    {
        this(new IntrospectionConfiguration());
    }

    /**
     * Construct allows a custom configuration to be set on construction.
     * This allows <code>IntrospectionConfiguration</code> subclasses
     * to be easily used.
     * @param configuration IntrospectionConfiguration, not null
     */
    public XMLIntrospector(IntrospectionConfiguration configuration)
    {
        setConfiguration(configuration);
        DefaultXMLBeanInfoRegistry defaultRegistry
            = new DefaultXMLBeanInfoRegistry();
        setRegistry(defaultRegistry);
        setPolymorphicReferenceResolver(defaultRegistry);
    }


    // Properties
    //-------------------------------------------------------------------------

    /**
     * <p>Gets the current logging implementation. </p>
     * @return the Log implementation which this class logs to
     */
    public Log getLog()
    {
        return getConfiguration().getIntrospectionLog();
    }

    /**
     * <p>Sets the current logging implementation.</p>
     * @param log the Log implementation to use for logging
     */
    public void setLog(Log log)
    {
        getConfiguration().setIntrospectionLog(log);
    }

    /**
     * <p>Gets the current registry implementation.
     * The registry is checked to see if it has an <code>XMLBeanInfo</code> for a class
     * before introspecting.
     * After standard introspection is complete, the instance will be passed to the registry.</p>
     *
     * <p>This allows finely grained control over the caching strategy.
     * It also allows the standard introspection mechanism
     * to be overridden on a per class basis.</p>
     *
     * @return the XMLBeanInfoRegistry currently used
     */
    public XMLBeanInfoRegistry getRegistry()
    {
        return registry;
    }

    /**
     * <p>Sets the <code>XMLBeanInfoRegistry</code> implementation.
     * The registry is checked to see if it has an <code>XMLBeanInfo</code> for a class
     * before introspecting.
     * After standard introspection is complete, the instance will be passed to the registry.</p>
     *
     * <p>This allows finely grained control over the caching strategy.
     * It also allows the standard introspection mechanism
     * to be overridden on a per class basis.</p>
     *
     * <p><strong>Note</strong> when using polymophic mapping with a custom
     * registry, a call to
     * {@link #setPolymorphicReferenceResolver(PolymorphicReferenceResolver)}
     * may be necessary.
     * </p>
     * @param registry the XMLBeanInfoRegistry to use
     */
    public void setRegistry(XMLBeanInfoRegistry registry)
    {
        this.registry = registry;
    }

    /**
     * Gets the configuration to be used for introspection.
     * The various introspection-time strategies
     * and configuration variables have been consolidated as properties
     * of this bean.
     * This allows the configuration to be more easily shared.
     * @return IntrospectionConfiguration, not null
     */
    public IntrospectionConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Sets the configuration to be used for introspection.
     * The various introspection-time strategies
     * and configuration variables have been consolidated as properties
     * of this bean.
     * This allows the configuration to be more easily shared.
     * @param configuration IntrospectionConfiguration, not null
     */
    public void setConfiguration(IntrospectionConfiguration configuration)
    {
        this.configuration = configuration;
    }


    /**
      * Gets the <code>ClassNormalizer</code> strategy.
      * This is used to determine the Class to be introspected
      * (the normalized Class).
      *
      * @return the <code>ClassNormalizer</code> used to determine the Class to be introspected
      * for a given Object.
      * @deprecated 0.6 use getConfiguration().getClassNormalizer
      * @since 0.5
      */
    public ClassNormalizer getClassNormalizer()
    {
        return getConfiguration().getClassNormalizer();
    }

    /**
      * Sets the <code>ClassNormalizer</code> strategy.
      * This is used to determine the Class to be introspected
      * (the normalized Class).
      *
      * @param classNormalizer the <code>ClassNormalizer</code> to be used to determine
      * the Class to be introspected for a given Object.
      * @deprecated 0.6 use getConfiguration().setClassNormalizer
      * @since 0.5
      *
      */
    public void setClassNormalizer(ClassNormalizer classNormalizer)
    {
        getConfiguration().setClassNormalizer(classNormalizer);
    }



    /**
     * <p>Gets the resolver for polymorphic references.</p>
     * <p>
     * Though this is used only at bind time,
     * it is typically tightly couple to the xml registry.
     * It is therefore convenient to keep both references together.
     * </p>
     * <p><strong>Note:</strong> though the implementation is
     * set initially to the default registry,
     * this reference is not updated when {@link #setRegistry(XMLBeanInfoRegistry)}
     * is called. Therefore, a call to {@link #setPolymorphicReferenceResolver(PolymorphicReferenceResolver)}
     * with the instance may be necessary.
     * </p>
     * @since 0.7
     * @return <code>PolymorphicReferenceResolver</code>, not null
     */
    public PolymorphicReferenceResolver getPolymorphicReferenceResolver()
    {
        return polymorphicReferenceResolver;
    }

    /**
     * <p>Sets the resolver for polymorphic references.</p>
     * <p>
     * Though this is used only at bind time,
     * it is typically tightly couple to the xml registry.
     * It is therefore convenient to keep both references together.
     * </p>
     * <p><strong>Note:</strong> though the implementation is
     * set initially to the default registry,
     * this reference is not updated when {@link #setRegistry(XMLBeanInfoRegistry)}
     * is called. Therefore, a call to {@link #setPolymorphicReferenceResolver(PolymorphicReferenceResolver)}
     * with the instance may be necessary.
     * </p>
     * @since 0.7
     * @param polymorphicReferenceResolver The polymorphicReferenceResolver to set.
     */
    public void setPolymorphicReferenceResolver(
        PolymorphicReferenceResolver polymorphicReferenceResolver)
    {
        this.polymorphicReferenceResolver = polymorphicReferenceResolver;
    }

    /**
     * Is <code>XMLBeanInfo</code> caching enabled?
     *
     * @deprecated 0.5 replaced by XMlBeanInfoRegistry
     * @return true if caching is enabled
     */
    public boolean isCachingEnabled()
    {
        return true;
    }

    /**
     * Set whether <code>XMLBeanInfo</code> caching should be enabled.
     *
     * @deprecated 0.5 replaced by XMlBeanInfoRegistry
     * @param cachingEnabled ignored
     */
    public void setCachingEnabled(boolean cachingEnabled)
    {
        //
    }


    /**
      * Should attributes (or elements) be used for primitive types.
      * @return true if primitive types will be mapped to attributes in the introspection
      * @deprecated 0.6 use getConfiguration().isAttributesForPrimitives
      */
    public boolean isAttributesForPrimitives()
    {
        return getConfiguration().isAttributesForPrimitives();
    }

    /**
      * Set whether attributes (or elements) should be used for primitive types.
      * @param attributesForPrimitives pass trus to map primitives to attributes,
      *        pass false to map primitives to elements
      * @deprecated 0.6 use getConfiguration().setAttributesForPrimitives
      */
    public void setAttributesForPrimitives(boolean attributesForPrimitives)
    {
        getConfiguration().setAttributesForPrimitives(attributesForPrimitives);
    }

    /**
     * Should collections be wrapped in an extra element?
     *
     * @return whether we should we wrap collections in an extra element?
     * @deprecated 0.6 use getConfiguration().isWrapCollectionsInElement
     */
    public boolean isWrapCollectionsInElement()
    {
        return getConfiguration().isWrapCollectionsInElement();
    }

    /**
     * Sets whether we should we wrap collections in an extra element.
     *
     * @param wrapCollectionsInElement pass true if collections should be wrapped in a
     *        parent element
     * @deprecated 0.6 use getConfiguration().setWrapCollectionsInElement
     */
    public void setWrapCollectionsInElement(boolean wrapCollectionsInElement)
    {
        getConfiguration().setWrapCollectionsInElement(wrapCollectionsInElement);
    }

    /**
     * Get singular and plural matching strategy.
     *
     * @return the strategy used to detect matching singular and plural properties
     * @deprecated 0.6 use getConfiguration().getPluralStemmer
     */
    public PluralStemmer getPluralStemmer()
    {
        return getConfiguration().getPluralStemmer();
    }

    /**
     * Sets the strategy used to detect matching singular and plural properties
     *
     * @param pluralStemmer the PluralStemmer used to match singular and plural
     * @deprecated 0.6 use getConfiguration().setPluralStemmer
     */
    public void setPluralStemmer(PluralStemmer pluralStemmer)
    {
        getConfiguration().setPluralStemmer(pluralStemmer);
    }

    /**
     * Gets the name mapper strategy.
     *
     * @return the strategy used to convert bean type names into element names
     * @deprecated 0.5 getNameMapper is split up in
     * {@link #getElementNameMapper()} and {@link #getAttributeNameMapper()}
     */
    public NameMapper getNameMapper()
    {
        return getElementNameMapper();
    }

    /**
     * Sets the strategy used to convert bean type names into element names
     * @param nameMapper the NameMapper strategy to be used
     * @deprecated 0.5 setNameMapper is split up in
     * {@link #setElementNameMapper(NameMapper)} and {@link #setAttributeNameMapper(NameMapper)}
     */
    public void setNameMapper(NameMapper nameMapper)
    {
        setElementNameMapper(nameMapper);
    }


    /**
     * Gets the name mapping strategy used to convert bean names into elements.
     *
     * @return the strategy used to convert bean type names into element
     * names. If no element mapper is currently defined then a default one is created.
     * @deprecated 0.6 use getConfiguration().getElementNameMapper
     */
    public NameMapper getElementNameMapper()
    {
        return getConfiguration().getElementNameMapper();
    }

    /**
     * Sets the strategy used to convert bean type names into element names
     * @param nameMapper the NameMapper to use for the conversion
     * @deprecated 0.6 use getConfiguration().setElementNameMapper
     */
    public void setElementNameMapper(NameMapper nameMapper)
    {
        getConfiguration().setElementNameMapper( nameMapper );
    }


    /**
     * Gets the name mapping strategy used to convert bean names into attributes.
     *
     * @return the strategy used to convert bean type names into attribute
     * names. If no attributeNamemapper is known, it will default to the ElementNameMapper
     * @deprecated 0.6 getConfiguration().getAttributeNameMapper
     */
    public NameMapper getAttributeNameMapper()
    {
        return getConfiguration().getAttributeNameMapper();
    }


    /**
     * Sets the strategy used to convert bean type names into attribute names
     * @param nameMapper the NameMapper to use for the convertion
     * @deprecated 0.6 use getConfiguration().setAttributeNameMapper
     */
    public void setAttributeNameMapper(NameMapper nameMapper)
    {
        getConfiguration().setAttributeNameMapper( nameMapper );
    }

    /**
     * Should the original <code>java.reflect.Introspector</code> bean info search path be used?
     * By default it will be false.
     *
     * @return boolean if the beanInfoSearchPath should be used.
     * @deprecated 0.6 use getConfiguration().useBeanInfoSearchPath
     */
    public boolean useBeanInfoSearchPath()
    {
        return getConfiguration().useBeanInfoSearchPath();
    }

    /**
     * Specifies if you want to use the beanInfoSearchPath
     * @see java.beans.Introspector for more details
     * @param useBeanInfoSearchPath
     * @deprecated 0.6 use getConfiguration().setUseBeanInfoSearchPath
     */
    public void setUseBeanInfoSearchPath(boolean useBeanInfoSearchPath)
    {
        getConfiguration().setUseBeanInfoSearchPath( useBeanInfoSearchPath );
    }

    // Methods
    //-------------------------------------------------------------------------

    /**
     * Flush existing cached <code>XMLBeanInfo</code>'s.
     *
     * @deprecated 0.5 use flushable registry instead
     */
    public void flushCache() {}


    /** Create a standard <code>XMLBeanInfo</code> by introspection
      * The actual introspection depends only on the <code>BeanInfo</code>
      * associated with the bean.
      *
      * @param bean introspect this bean
      * @return XMLBeanInfo describing bean-xml mapping
      * @throws IntrospectionException when the bean introspection fails
      */
    public XMLBeanInfo introspect(Object bean) throws IntrospectionException
    {
        if (getLog().isDebugEnabled())
        {
            getLog().debug( "Introspecting..." );
            getLog().debug(bean);
        }

        if ( bean instanceof DynaBean )
        {
            // allow DynaBean implementations to be overridden by .betwixt files
            XMLBeanInfo xmlBeanInfo = findByXMLDescriptor( bean.getClass() );
            if (xmlBeanInfo != null)
            {
                return xmlBeanInfo;
            }
            // this is DynaBean use the DynaClass for introspection
            return introspect( ((DynaBean) bean).getDynaClass() );

        }
        else
        {
            // normal bean so normal introspection
            Class normalClass = getClassNormalizer().getNormalizedClass( bean );
            return introspect( normalClass );
        }
    }

    /**
     * Creates XMLBeanInfo by reading the DynaProperties of a DynaBean.
     * Customizing DynaBeans using betwixt is not supported.
     *
     * @param dynaClass the DynaBean to introspect
     *
     * @return XMLBeanInfo for the DynaClass
     */
    public XMLBeanInfo introspect(DynaClass dynaClass)
    {

        // for now this method does not do much, since XMLBeanInfoRegistry cannot
        // use a DynaClass as a key
        // TODO: add caching for DynaClass XMLBeanInfo
        // need to work out if this is possible

        // this line allows subclasses to change creation strategy
        XMLBeanInfo xmlInfo = createXMLBeanInfo( dynaClass );

        // populate the created info with
        DynaClassBeanType beanClass = new DynaClassBeanType( dynaClass );
        populate( xmlInfo, beanClass );

        return xmlInfo;
    }


    /**
     * <p>Introspects the given <code>Class</code> using the dot betwixt
     * document in the given <code>InputSource</code>.
     * </p>
     * <p>
     * <strong>Note:</strong> that the given mapping will <em>not</em>
     * be registered by this method. Use {@link #register(Class, InputSource)}
     * instead.
     * </p>
     * @since 0.7
     * @param aClass <code>Class</code>, not null
     * @param source <code>InputSource</code>, not null
     * @return <code>XMLBeanInfo</code> describing the mapping.
     * @throws SAXException when the input source cannot be parsed
     * @throws IOException
     */
    public synchronized XMLBeanInfo introspect(Class aClass, InputSource source) throws IOException, SAXException
    {
        // need to synchronize since we only use one instance and SAX is essentially one thread only
        configureDigester(aClass);
        XMLBeanInfo result = (XMLBeanInfo) digester.parse(source);
        return result;
    }


    /** Create a standard <code>XMLBeanInfo</code> by introspection.
      * The actual introspection depends only on the <code>BeanInfo</code>
      * associated with the bean.
      *
      * @param aClass introspect this class
      * @return XMLBeanInfo describing bean-xml mapping
      * @throws IntrospectionException when the bean introspection fails
      */
    public XMLBeanInfo introspect(Class aClass) throws IntrospectionException
    {
        // we first reset the beaninfo searchpath.
        String[] searchPath = null;
        if ( !getConfiguration().useBeanInfoSearchPath() )
        {
            try
            {
                searchPath = Introspector.getBeanInfoSearchPath();
                Introspector.setBeanInfoSearchPath(new String[] { });
            }
            catch (SecurityException e)
            {
                // this call may fail in some environments
                getLog().warn("Security manager does not allow bean info search path to be set");
                getLog().debug("Security exception whilst setting bean info search page", e);
            }
        }

        XMLBeanInfo xmlInfo = registry.get( aClass );

        if ( xmlInfo == null )
        {
            // lets see if we can find an XML descriptor first
            if ( getLog().isDebugEnabled() )
            {
                getLog().debug( "Attempting to lookup an XML descriptor for class: " + aClass );
            }

            xmlInfo = findByXMLDescriptor( aClass );
            if ( xmlInfo == null )
            {
                BeanInfo info;
                if(getConfiguration().ignoreAllBeanInfo())
                {
                    info = Introspector.getBeanInfo( aClass, Introspector.IGNORE_ALL_BEANINFO );
                }
                else
                {
                    info = Introspector.getBeanInfo( aClass );
                }
                xmlInfo = introspect( info );
            }

            if ( xmlInfo != null )
            {
                registry.put( aClass, xmlInfo );
            }
        }
        else
        {
            getLog().trace( "Used cached XMLBeanInfo." );
        }

        if ( getLog().isTraceEnabled() )
        {
            getLog().trace( xmlInfo );
        }
        if ( !getConfiguration().useBeanInfoSearchPath() && searchPath != null)
        {
            try
            {
                // we restore the beaninfo searchpath.
                Introspector.setBeanInfoSearchPath( searchPath );
            }
            catch (SecurityException e)
            {
                // this call may fail in some environments
                getLog().warn("Security manager does not allow bean info search path to be set");
                getLog().debug("Security exception whilst setting bean info search page", e);
            }
        }

        return xmlInfo;
    }

    /** Create a standard <code>XMLBeanInfo</code> by introspection.
      * The actual introspection depends only on the <code>BeanInfo</code>
      * associated with the bean.
      *
      * @param beanInfo the BeanInfo the xml-bean mapping is based on
      * @return XMLBeanInfo describing bean-xml mapping
      * @throws IntrospectionException when the bean introspection fails
      */
    public XMLBeanInfo introspect(BeanInfo beanInfo) throws IntrospectionException
    {
        XMLBeanInfo xmlBeanInfo = createXMLBeanInfo( beanInfo );
        populate( xmlBeanInfo, new JavaBeanType( beanInfo ) );
        return xmlBeanInfo;
    }


    /**
     * <p>Registers the class mappings specified in the multi-class document
     * given by the <code>InputSource</code>.
     * </p>
     * <p>
     * <strong>Note:</strong> that this method will override any existing mapping
     * for the speficied classes.
     * </p>
     * @since 0.7
     * @param source <code>InputSource</code>, not null
     * @return <code>Class</code> array containing all mapped classes
     * @throws IntrospectionException
     * @throws SAXException
     * @throws IOException
     */
    public synchronized Class[] register(InputSource source) throws IntrospectionException, IOException, SAXException
    {
        Map xmlBeanInfoByClass = loadMultiMapping(source);
        Set keySet = xmlBeanInfoByClass.keySet();
        Class mappedClasses[] = new Class[keySet.size()];
        int i=0;
        for (Iterator it=keySet.iterator(); it.hasNext(); )
        {
            Class clazz = (Class) it.next();
            mappedClasses[i++] = clazz;
            XMLBeanInfo xmlBeanInfo = (XMLBeanInfo) xmlBeanInfoByClass.get(clazz);
            if (xmlBeanInfo != null)
            {
                getRegistry().put(clazz, xmlBeanInfo);
            }
        }
        return mappedClasses;
    }

    /**
     * Loads the multi-mapping from the given <code>InputSource</code>.
     * @param mapping <code>InputSource</code>, not null
     * @return <code>Map</code> containing <code>XMLBeanInfo</code>'s
     * indexes by the <code>Class</code> they describe
     * @throws IOException
     * @throws SAXException
     */
    private synchronized Map loadMultiMapping(InputSource mapping) throws IOException, SAXException
    {
        // synchronized method so this digester is only used by
        // one thread at once
        if (multiMappingdigester == null)
        {
            multiMappingdigester = new MultiMappingBeanInfoDigester();
            multiMappingdigester.setXMLIntrospector(this);
        }
        Map multiBeanInfoMap = (Map) multiMappingdigester.parse(mapping);
        return multiBeanInfoMap;
    }

    /**
     * <p>Registers the class mapping specified in the standard dot-betwixt file.
     * Subsequent introspections will use this registered mapping for the class.
     * </p>
     * <p>
     * <strong>Note:</strong> that this method will override any existing mapping
     * for this class.
     * </p>
     * @since 0.7
     * @param aClass <code>Class</code>, not null
     * @param source <code>InputSource</code>, not null
     * @throws SAXException when the source cannot be parsed
     * @throws IOException
     */
    public void register(Class aClass, InputSource source) throws IOException, SAXException
    {
        XMLBeanInfo xmlBeanInfo = introspect(aClass, source);
        getRegistry().put(aClass, xmlBeanInfo);
    }

    /**
     * Populates the given <code>XMLBeanInfo</code> based on the given type of bean.
     *
     * @param xmlBeanInfo populate this, not null
     * @param bean the type definition for the bean, not null
     */
    private void populate(XMLBeanInfo xmlBeanInfo, BeanType bean)
    {
        String name = bean.getBeanName();

        ElementDescriptor elementDescriptor = new ElementDescriptor();
        elementDescriptor.setLocalName(
            getElementNameMapper().mapTypeToElementName( name ) );
        elementDescriptor.setPropertyType( bean.getElementType() );

        if (getLog().isTraceEnabled())
        {
            getLog().trace("Populating:" + bean);
        }

        // add default string value for primitive types
        if ( bean.isPrimitiveType() )
        {
            getLog().trace("Bean is primitive");
            elementDescriptor.setTextExpression( StringExpression.getInstance() );

        }
        else
        {

            getLog().trace("Bean is standard type");

            boolean isLoopType = bean.isLoopType();

            List elements = new ArrayList();
            List attributes = new ArrayList();
            List contents = new ArrayList();

            // add bean properties for all collection which are not basic
            if ( !( isLoopType && isBasicCollection( bean.getClass() ) ) )
            {
                addProperties( bean.getProperties(), elements, attributes, contents );
            }

            // add iterator for collections
            if ( isLoopType )
            {
                getLog().trace("Bean is loop");
                ElementDescriptor loopDescriptor = new ElementDescriptor();
                loopDescriptor.setCollective(true);
                loopDescriptor.setHollow(true);
                loopDescriptor.setSingularPropertyType(Object.class);
                loopDescriptor.setContextExpression(
                    new IteratorExpression( EmptyExpression.getInstance() )
                );
                loopDescriptor.setUpdater(CollectionUpdater.getInstance());
                if ( bean.isMapType() )
                {
                    loopDescriptor.setQualifiedName( "entry" );
                }
                elements.add( loopDescriptor );
            }

            int size = elements.size();
            if ( size > 0 )
            {
                ElementDescriptor[] descriptors = new ElementDescriptor[size];
                elements.toArray( descriptors );
                elementDescriptor.setElementDescriptors( descriptors );
            }
            size = attributes.size();
            if ( size > 0 )
            {
                AttributeDescriptor[] descriptors = new AttributeDescriptor[size];
                attributes.toArray( descriptors );
                elementDescriptor.setAttributeDescriptors( descriptors );
            }
            size = contents.size();
            if ( size > 0 )
            {
                if ( size > 0 )
                {
                    Descriptor[] descriptors = new Descriptor[size];
                    contents.toArray( descriptors );
                    elementDescriptor.setContentDescriptors( descriptors );
                }
            }
        }

        xmlBeanInfo.setElementDescriptor( elementDescriptor );

        // default any addProperty() methods
        defaultAddMethods( elementDescriptor, bean.getElementType() );

        if (getLog().isTraceEnabled())
        {
            getLog().trace("Populated descriptor:");
            getLog().trace(elementDescriptor);
        }
    }

    /**
     * <p>Is the given type a basic collection?
     * </p><p>
     * This is used to determine whether a collective type
     * should be introspected as a bean (in addition to a collection).
     * </p>
     * @param type <code>Class</code>, not null
     * @return
     */
    private boolean isBasicCollection( Class type )
    {
        return type.getName().startsWith( "java.util" );
    }

    /**
     * Creates XMLBeanInfo for the given DynaClass.
     *
     * @param dynaClass the class describing a DynaBean
     *
     * @return XMLBeanInfo that describes the properties of the given
     * DynaClass
     */
    protected XMLBeanInfo createXMLBeanInfo(DynaClass dynaClass)
    {
        // XXX is the chosen class right?
        XMLBeanInfo beanInfo = new XMLBeanInfo(dynaClass.getClass());
        return beanInfo;
    }




    /**
     * Create a XML descriptor from a bean one.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param propertyDescriptor create a <code>NodeDescriptor</code> for this property
     * @param useAttributesForPrimitives write primitives as attributes (rather than elements)
     * @return a correctly configured <code>NodeDescriptor</code> for the property
     * @throws IntrospectionException when bean introspection fails
     * @deprecated 0.5 use {@link #createXMLDescriptor}.
     */
    public Descriptor createDescriptor(
        PropertyDescriptor propertyDescriptor,
        boolean useAttributesForPrimitives
    ) throws IntrospectionException
    {
        return createXMLDescriptor( new BeanProperty( propertyDescriptor ) );
    }

    /**
     * Create a XML descriptor from a bean one.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param beanProperty the BeanProperty specifying the property
     * @return a correctly configured <code>NodeDescriptor</code> for the property
     * @since 0.5
     */
    public Descriptor createXMLDescriptor( BeanProperty beanProperty )
    {
        return beanProperty.createXMLDescriptor( configuration );
    }


    /**
     * Add any addPropety(PropertyType) methods as Updaters
     * which are often used for 1-N relationships in beans.
     * This method does not preserve null property names.
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
     * <br>
     * TODO this probably needs refactoring. It probably belongs in the bean wrapper
     * (so that it'll work properly with dyna-beans) and so that the operations can
     * be optimized by caching. Multiple hash maps are created and getMethods is
     * called multiple times. This is relatively expensive and so it'd be better
     * to push into a proper class and cache.
     * <br>
     *
     * @param rootDescriptor add defaults to this descriptor
     * @param beanClass the <code>Class</code> to which descriptor corresponds
     */
    public void defaultAddMethods(
        ElementDescriptor rootDescriptor,
        Class beanClass )
    {
        defaultAddMethods(rootDescriptor, beanClass, false);
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
     * <br>
     * TODO this probably needs refactoring. It probably belongs in the bean wrapper
     * (so that it'll work properly with dyna-beans) and so that the operations can
     * be optimized by caching. Multiple hash maps are created and getMethods is
     * called multiple times. This is relatively expensive and so it'd be better
     * to push into a proper class and cache.
     * <br>
     *
     * @param rootDescriptor add defaults to this descriptor
     * @param beanClass the <code>Class</code> to which descriptor corresponds
     * @since 0.8
     */
    public void defaultAddMethods( ElementDescriptor rootDescriptor, Class beanClass, boolean preservePropertyName )
    {
        // TODO: this probably does work properly with DynaBeans: need to push
        // implementation into an class and expose it on BeanType.

        // lets iterate over all methods looking for one of the form
        // add*(PropertyType)
        if ( beanClass != null )
        {
            ArrayList singleParameterAdders = new ArrayList();
            ArrayList twinParameterAdders = new ArrayList();

            Method[] methods = beanClass.getMethods();
            for ( int i = 0, size = methods.length; i < size; i++ )
            {
                Method method = methods[i];
                String name = method.getName();
                if ( name.startsWith( "add" ))
                {
                    // TODO: should we filter out non-void returning methods?
                    // some beans will return something as a helper
                    Class[] types = method.getParameterTypes();
                    if ( types != null)
                    {
                        if ( getLog().isTraceEnabled() )
                        {
                            getLog().trace("Searching for match for " + method);
                        }

                        switch (types.length)
                        {
                        case 1:
                            singleParameterAdders.add(method);
                            break;
                        case 2:
                            twinParameterAdders.add(method);
                            break;
                        default:
                            // ignore
                            break;
                        }
                    }
                }
            }

            Map elementsByPropertyName = makeElementDescriptorMap( rootDescriptor );

            for (Iterator it=singleParameterAdders.iterator(); it.hasNext();)
            {
                Method singleParameterAdder = (Method) it.next();
                setIteratorAdder(elementsByPropertyName, singleParameterAdder, preservePropertyName);
            }

            for (Iterator it=twinParameterAdders.iterator(); it.hasNext();)
            {
                Method twinParameterAdder = (Method) it.next();
                setMapAdder(elementsByPropertyName, twinParameterAdder);
            }

            // need to call this once all the defaults have been added
            // so that all the singular types have been set correctly
            configureMappingDerivation( rootDescriptor );
        }
    }

    /**
     * Configures the mapping derivation according to the current
     * <code>MappingDerivationStrategy</code> implementation.
     * This method acts recursively.
     * @param rootDescriptor <code>ElementDescriptor</code>, not null
     */
    private void configureMappingDerivation(ElementDescriptor descriptor)
    {
        boolean useBindTime = getConfiguration().getMappingDerivationStrategy()
                              .useBindTimeTypeForMapping(descriptor.getPropertyType(), descriptor.getSingularPropertyType());
        descriptor.setUseBindTimeTypeForMapping(useBindTime);
        ElementDescriptor[] childDescriptors = descriptor.getElementDescriptors();
        for (int i=0, size=childDescriptors.length; i<size; i++)
        {
            configureMappingDerivation(childDescriptors[i]);
        }
    }

    /**
     * Sets the adder method where the corresponding property is an iterator
     * @param rootDescriptor
     * @param singleParameterAdder
     */
    private void setIteratorAdder(
        Map elementsByPropertyName,
        Method singleParameterAdderMethod,
        boolean preserveNullPropertyName)
    {

        String adderName = singleParameterAdderMethod.getName();
        String propertyName = Introspector.decapitalize(adderName.substring(3));
        ElementDescriptor matchingDescriptor = getMatchForAdder(propertyName, elementsByPropertyName);
        if (matchingDescriptor != null)
        {
            //TODO defensive code: probably should check descriptor type

            Class singularType = singleParameterAdderMethod.getParameterTypes()[0];
            if (getLog().isTraceEnabled())
            {
                getLog().trace(adderName + "->" + propertyName);
            }
            // this may match a standard collection or iteration
            getLog().trace("Matching collection or iteration");

            matchingDescriptor.setUpdater( new MethodUpdater( singleParameterAdderMethod ) );
            matchingDescriptor.setSingularPropertyType( singularType );
            matchingDescriptor.setHollow(!isPrimitiveType(singularType));
            String localName = matchingDescriptor.getLocalName();
            if ( !preserveNullPropertyName && ( localName == null || localName.length() == 0 ))
            {
                matchingDescriptor.setLocalName(
                    getConfiguration().getElementNameMapper()
                    .mapTypeToElementName( propertyName ) );
            }

            if ( getLog().isDebugEnabled() )
            {
                getLog().debug( "!! " + singleParameterAdderMethod);
                getLog().debug( "!! " + singularType);
            }
        }
    }

    /**
     * Sets the adder where the corresponding property type is an map
     * @param rootDescriptor
     * @param singleParameterAdder
     */
    private void setMapAdder(
        Map elementsByPropertyName,
        Method twinParameterAdderMethod)
    {
        String adderName = twinParameterAdderMethod.getName();
        String propertyName = Introspector.decapitalize(adderName.substring(3));
        ElementDescriptor matchingDescriptor = getMatchForAdder(propertyName, elementsByPropertyName);
        assignAdder(twinParameterAdderMethod, matchingDescriptor);
    }

    /**
     * Assigns the given method as an adder method to the given descriptor.
     * @param twinParameterAdderMethod adder <code>Method</code>, not null
     * @param matchingDescriptor <code>ElementDescriptor</code> describing the element
     * @since 0.8
     */
    public void assignAdder(Method twinParameterAdderMethod, ElementDescriptor matchingDescriptor)
    {
        if ( matchingDescriptor != null
                && Map.class.isAssignableFrom( matchingDescriptor.getPropertyType() ))
        {
            // this may match a map
            getLog().trace("Matching map");
            ElementDescriptor[] children
                = matchingDescriptor.getElementDescriptors();
            // see if the descriptor's been set up properly
            if ( children.length == 0 )
            {
                getLog().info(
                    "'entry' descriptor is missing for map. "
                    + "Updaters cannot be set");

            }
            else
            {
                assignAdder(twinParameterAdderMethod, children);
            }
        }
    }

    /**
     * Assigns the given method as an adder.
     * @param twinParameterAdderMethod adder <code>Method</code>, not null
     * @param children <code>ElementDescriptor</code> children, not null
     */
    private void assignAdder(Method twinParameterAdderMethod, ElementDescriptor[] children)
    {
        Class[] types = twinParameterAdderMethod.getParameterTypes();
        Class keyType = types[0];
        Class valueType = types[1];

        // loop through children
        // adding updaters for key and value
        MapEntryAdder adder = new MapEntryAdder(twinParameterAdderMethod);
        for (
            int n=0,
            noOfGrandChildren = children.length;
            n < noOfGrandChildren;
            n++ )
        {
            if ( "key".equals( children[n].getLocalName() ) )
            {

                children[n].setUpdater( adder.getKeyUpdater() );
                children[n].setSingularPropertyType(  keyType );
                if (children[n].getPropertyType() == null)
                {
                    children[n].setPropertyType( valueType );
                }
                if ( isPrimitiveType(keyType) )
                {
                    children[n].setHollow(false);
                }
                if ( getLog().isTraceEnabled() )
                {
                    getLog().trace( "Key descriptor: " + children[n]);
                }

            }
            else if ( "value".equals( children[n].getLocalName() ) )
            {

                children[n].setUpdater( adder.getValueUpdater() );
                children[n].setSingularPropertyType( valueType );
                if (children[n].getPropertyType() == null)
                {
                    children[n].setPropertyType( valueType );
                }
                if ( isPrimitiveType( valueType) )
                {
                    children[n].setHollow(false);
                }
                if ( isLoopType( valueType ))
                {
                    // need to attach a hollow descriptor
                    // don't know the element name
                    // so use null name (to match anything)
                    ElementDescriptor loopDescriptor = new ElementDescriptor();
                    loopDescriptor.setHollow(true);
                    loopDescriptor.setSingularPropertyType( valueType );
                    loopDescriptor.setPropertyType( valueType );
                    children[n].addElementDescriptor(loopDescriptor);
                    loopDescriptor.setCollective(true);
                }
                if ( getLog().isTraceEnabled() )
                {
                    getLog().trace( "Value descriptor: " + children[n]);
                }
            }
        }
    }

    /**
     * Gets an ElementDescriptor for the property matching the adder
     * @param adderName
     * @param rootDescriptor
     * @return
     */
    private ElementDescriptor getMatchForAdder(
        String propertyName,
        Map elementsByPropertyName)
    {
        ElementDescriptor matchingDescriptor = null;
        if (propertyName.length() > 0)
        {
            if ( getLog().isTraceEnabled() )
            {
                getLog().trace( "findPluralDescriptor( " + propertyName
                                + " ):root property name=" + propertyName );
            }

            PluralStemmer stemmer = getPluralStemmer();
            matchingDescriptor = stemmer.findPluralDescriptor( propertyName, elementsByPropertyName );

            if ( getLog().isTraceEnabled() )
            {
                getLog().trace(
                    "findPluralDescriptor( " + propertyName
                    + " ):ElementDescriptor=" + matchingDescriptor );
            }
        }
        return matchingDescriptor;
    }

    // Implementation methods
    //-------------------------------------------------------------------------


    /**
     * Creates a map where the keys are the property names and the values are the ElementDescriptors
     */
    private Map makeElementDescriptorMap( ElementDescriptor rootDescriptor )
    {
        Map result = new HashMap();
        String rootPropertyName = rootDescriptor.getPropertyName();
        if (rootPropertyName != null)
        {
            result.put(rootPropertyName, rootDescriptor);
        }
        makeElementDescriptorMap( rootDescriptor, result );
        return result;
    }

    /**
     * Creates a map where the keys are the property names and the values are the ElementDescriptors
     *
     * @param rootDescriptor the values of the maps are the children of this
     * <code>ElementDescriptor</code> index by their property names
     * @param map the map to which the elements will be added
     */
    private void makeElementDescriptorMap( ElementDescriptor rootDescriptor, Map map )
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
     * A Factory method to lazily create a new strategy
     * to detect matching singular and plural properties.
     *
     * @return new defualt PluralStemmer implementation
     * @deprecated 0.6 this method has been moved into IntrospectionConfiguration.
     * Those who need to vary this should subclass that class instead
     */
    protected PluralStemmer createPluralStemmer()
    {
        return new DefaultPluralStemmer();
    }

    /**
     * A Factory method to lazily create a strategy
     * used to convert bean type names into element names.
     *
     * @return new default NameMapper implementation
     * @deprecated 0.6 this method has been moved into IntrospectionConfiguration.
     * Those who need to vary this should subclass that class instead
     */
    protected NameMapper createNameMapper()
    {
        return new DefaultNameMapper();
    }

    /**
     * Attempt to lookup the XML descriptor for the given class using the
     * classname + ".betwixt" using the same ClassLoader used to load the class
     * or return null if it could not be loaded
     *
     * @param aClass digester .betwixt file for this class
     * @return XMLBeanInfo digested from the .betwixt file if one can be found.
     *         Otherwise null.
     */
    protected synchronized XMLBeanInfo findByXMLDescriptor( Class aClass )
    {
        // trim the package name
        String name = aClass.getName();
        int idx = name.lastIndexOf( '.' );
        if ( idx >= 0 )
        {
            name = name.substring( idx + 1 );
        }
        name += ".betwixt";

        URL url = aClass.getResource( name );
        if ( url != null )
        {
            try
            {
                String urlText = url.toString();
                if ( getLog().isDebugEnabled( ))
                {
                    getLog().debug( "Parsing Betwixt XML descriptor: " + urlText );
                }
                // synchronized method so this digester is only used by
                // one thread at once
                configureDigester(aClass);
                return (XMLBeanInfo) digester.parse( urlText );
            }
            catch (Exception e)
            {
                getLog().warn( "Caught exception trying to parse: " + name, e );
            }
        }

        if ( getLog().isTraceEnabled() )
        {
            getLog().trace( "Could not find betwixt file " + name );
        }
        return null;
    }

    /**
     * Configures the single <code>Digester</code> instance used by this introspector.
     * @param aClass <code>Class</code>, not null
     */
    private synchronized void configureDigester(Class aClass)
    {
        if ( digester == null )
        {
            digester = new XMLBeanInfoDigester();
            digester.setXMLIntrospector( this );
        }
        digester.setBeanClass( aClass );
    }

    /**
     * Loop through properties and process each one
     *
     * @param beanInfo the BeanInfo whose properties will be processed
     * @param elements ElementDescriptor list to which elements will be added
     * @param attributes AttributeDescriptor list to which attributes will be added
     * @param contents Descriptor list to which mixed content will be added
     * @throws IntrospectionException if the bean introspection fails
     * @deprecated 0.5 use {@link #addProperties(BeanProperty[], List, List,List)}
     */
    protected void addProperties(
        BeanInfo beanInfo,
        List elements,
        List attributes,
        List contents)
    throws
        IntrospectionException
    {
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        if ( descriptors != null )
        {
            for ( int i = 0, size = descriptors.length; i < size; i++ )
            {
                addProperty(beanInfo, descriptors[i], elements, attributes, contents);
            }
        }
        if (getLog().isTraceEnabled())
        {
            getLog().trace(elements);
            getLog().trace(attributes);
            getLog().trace(contents);
        }
    }
    /**
     * Loop through properties and process each one
     *
     * @param beanProperties the properties to be processed
     * @param elements ElementDescriptor list to which elements will be added
     * @param attributes AttributeDescriptor list to which attributes will be added
     * @param contents Descriptor list to which mixed content will be added
     * @since 0.5
     */
    protected void addProperties(
        BeanProperty[] beanProperties,
        List elements,
        List attributes,
        List contents)
    {
        if ( beanProperties != null )
        {
            if (getLog().isTraceEnabled())
            {
                getLog().trace(beanProperties.length + " properties to be added");
            }
            for ( int i = 0, size = beanProperties.length; i < size; i++ )
            {
                addProperty(beanProperties[i], elements, attributes, contents);
            }
        }
        if (getLog().isTraceEnabled())
        {
            getLog().trace("After properties have been added (elements, attributes, contents):");
            getLog().trace(elements);
            getLog().trace(attributes);
            getLog().trace(contents);
        }
    }


    /**
     * Process a property.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param beanInfo the BeanInfo whose property is being processed
     * @param propertyDescriptor the PropertyDescriptor to process
     * @param elements ElementDescriptor list to which elements will be added
     * @param attributes AttributeDescriptor list to which attributes will be added
     * @param contents Descriptor list to which mixed content will be added
     * @throws IntrospectionException if the bean introspection fails
     * @deprecated 0.5 BeanInfo is no longer required.
     * Use {@link #addProperty(PropertyDescriptor, List, List, List)} instead.
     */
    protected void addProperty(
        BeanInfo beanInfo,
        PropertyDescriptor propertyDescriptor,
        List elements,
        List attributes,
        List contents)
    throws
        IntrospectionException
    {
        addProperty( propertyDescriptor, elements, attributes, contents);
    }

    /**
     * Process a property.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param propertyDescriptor the PropertyDescriptor to process
     * @param elements ElementDescriptor list to which elements will be added
     * @param attributes AttributeDescriptor list to which attributes will be added
     * @param contents Descriptor list to which mixed content will be added
     * @throws IntrospectionException if the bean introspection fails
     * @deprecated 0.5 use {@link #addProperty(BeanProperty, List, List, List)} instead
     */
    protected void addProperty(
        PropertyDescriptor propertyDescriptor,
        List elements,
        List attributes,
        List contents)
    throws
        IntrospectionException
    {
        addProperty(new BeanProperty( propertyDescriptor ), elements, attributes, contents);
    }

    /**
     * Process a property.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param beanProperty the bean property to process
     * @param elements ElementDescriptor list to which elements will be added
     * @param attributes AttributeDescriptor list to which attributes will be added
     * @param contents Descriptor list to which mixed content will be added
     * @since 0.5
     */
    protected void addProperty(
        BeanProperty beanProperty,
        List elements,
        List attributes,
        List contents)
    {
        Descriptor nodeDescriptor = createXMLDescriptor(beanProperty);
        if (nodeDescriptor == null)
        {
            return;
        }
        if (nodeDescriptor instanceof ElementDescriptor)
        {
            elements.add(nodeDescriptor);
        }
        else if (nodeDescriptor instanceof AttributeDescriptor)
        {
            attributes.add(nodeDescriptor);
        }
        else
        {
            contents.add(nodeDescriptor);
        }
    }

    /**
     * Loop through properties and process each one
     *
     * @param beanInfo the BeanInfo whose properties will be processed
     * @param elements ElementDescriptor list to which elements will be added
     * @param attributes AttributeDescriptor list to which attributes will be added
     * @throws IntrospectionException if the bean introspection fails
     * @deprecated 0.5 this method does not support mixed content.
     * Use {@link #addProperties(BeanInfo, List, List, List)} instead.
     */
    protected void addProperties(
        BeanInfo beanInfo,
        List elements,
        List attributes)
    throws
        IntrospectionException
    {
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        if ( descriptors != null )
        {
            for ( int i = 0, size = descriptors.length; i < size; i++ )
            {
                addProperty(beanInfo, descriptors[i], elements, attributes);
            }
        }
        if (getLog().isTraceEnabled())
        {
            getLog().trace(elements);
            getLog().trace(attributes);
        }
    }

    /**
     * Process a property.
     * Go through and work out whether it's a loop property, a primitive or a standard.
     * The class property is ignored.
     *
     * @param beanInfo the BeanInfo whose property is being processed
     * @param propertyDescriptor the PropertyDescriptor to process
     * @param elements ElementDescriptor list to which elements will be added
     * @param attributes AttributeDescriptor list to which attributes will be added
     * @throws IntrospectionException if the bean introspection fails
     * @deprecated 0.5 this method does not support mixed content.
     * Use {@link #addProperty(BeanInfo, PropertyDescriptor, List, List, List)} instead.
     */
    protected void addProperty(
        BeanInfo beanInfo,
        PropertyDescriptor propertyDescriptor,
        List elements,
        List attributes)
    throws
        IntrospectionException
    {
        NodeDescriptor nodeDescriptor = XMLIntrospectorHelper
                                        .createDescriptor(propertyDescriptor,
                                                isAttributesForPrimitives(),
                                                this);
        if (nodeDescriptor == null)
        {
            return;
        }
        if (nodeDescriptor instanceof ElementDescriptor)
        {
            elements.add(nodeDescriptor);
        }
        else
        {
            attributes.add(nodeDescriptor);
        }
    }


    /**
     * Factory method to create XMLBeanInfo instances
     *
     * @param beanInfo the BeanInfo from which the XMLBeanInfo will be created
     * @return XMLBeanInfo describing the bean-xml mapping
     */
    protected XMLBeanInfo createXMLBeanInfo( BeanInfo beanInfo )
    {
        XMLBeanInfo xmlBeanInfo = new XMLBeanInfo( beanInfo.getBeanDescriptor().getBeanClass() );
        return xmlBeanInfo;
    }

    /**
     * Is this class a loop?
     *
     * @param type the Class to test
     * @return true if the type is a loop type
     */
    public boolean isLoopType(Class type)
    {
        return getConfiguration().isLoopType(type);
    }


    /**
     * Is this class a primitive?
     *
     * @param type the Class to test
     * @return true for primitive types
     */
    public boolean isPrimitiveType(Class type)
    {
        // TODO: this method will probably be deprecated when primitive types
        // are subsumed into the simple type concept
        TypeBindingStrategy.BindingType bindingType
            = configuration.getTypeBindingStrategy().bindingType( type ) ;
        boolean result = (bindingType.equals(TypeBindingStrategy.BindingType.PRIMITIVE));
        return result;
    }


    /** Some type of pseudo-bean */
    private abstract class BeanType
    {
        /**
         * Gets the name for this bean type
         * @return the bean type name, not null
         */
        public abstract String getBeanName();

        /**
         * Gets the type to be used by the associated element
         * @return a Class that is the type not null
         */
        public abstract Class getElementType();

        /**
         * Is this type a primitive?
         * @return true if this type should be treated by betwixt as a primitive
         */
        public abstract boolean isPrimitiveType();

        /**
         * is this type a map?
         * @return true this should be treated as a map.
         */
        public abstract boolean isMapType();

        /**
         * Is this type a loop?
         * @return true if this should be treated as a loop
         */
        public abstract boolean isLoopType();

        /**
         * Gets the properties associated with this bean.
         * @return the BeanProperty's, not null
         */
        public abstract BeanProperty[] getProperties();

        /**
         * Create string representation
         * @return something useful for logging
         */
        public String toString()
        {
            return "Bean[name=" + getBeanName() + ", type=" + getElementType();
        }
    }

    /** Supports standard Java Beans */
    private class JavaBeanType extends BeanType
    {
        /** Introspected bean */
        private BeanInfo beanInfo;
        /** Bean class */
        private Class beanClass;
        /** Bean name */
        private String name;
        /** Bean properties */
        private BeanProperty[] properties;

        /**
         * Constructs a BeanType for a standard Java Bean
         * @param beanInfo the BeanInfo describing the standard Java Bean, not null
         */
        public JavaBeanType(BeanInfo beanInfo)
        {
            this.beanInfo = beanInfo;
            BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
            beanClass = beanDescriptor.getBeanClass();
            name = beanDescriptor.getName();
            // Array's contain a bad character
            if (beanClass.isArray())
            {
                // called all array's Array
                name = "Array";
            }

        }

        /** @see BeanType #getElementType */
        public Class getElementType()
        {
            return beanClass;
        }

        /** @see BeanType#getBeanName */
        public String getBeanName()
        {
            return name;
        }

        /** @see BeanType#isPrimitiveType */
        public boolean isPrimitiveType()
        {
            return XMLIntrospector.this.isPrimitiveType( beanClass );
        }

        /** @see BeanType#isLoopType */
        public boolean isLoopType()
        {
            return getConfiguration().isLoopType( beanClass );
        }

        /** @see BeanType#isMapType */
        public boolean isMapType()
        {
            return Map.class.isAssignableFrom( beanClass );
        }

        /** @see BeanType#getProperties */
        public BeanProperty[] getProperties()
        {
            // lazy creation
            if ( properties == null )
            {
                ArrayList propertyDescriptors = new ArrayList();
                // add base bean info
                PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                if ( descriptors != null )
                {
                    for (int i=0, size=descriptors.length; i<size; i++)
                    {
                        if (!getConfiguration().getPropertySuppressionStrategy()
                                .suppressProperty(
                                    beanClass,
                                    descriptors[i].getPropertyType(),
                                    descriptors[i].getName()))
                        {
                            propertyDescriptors.add( descriptors[i] );
                        }
                    }
                }

                // add properties from additional bean infos
                BeanInfo[] additionals = beanInfo.getAdditionalBeanInfo();
                if ( additionals != null )
                {
                    for ( int i=0, outerSize=additionals.length; i<outerSize; i++ )
                    {
                        BeanInfo additionalInfo = additionals[i];
                        descriptors = additionalInfo.getPropertyDescriptors();
                        if ( descriptors != null )
                        {
                            for (int j=0, innerSize=descriptors.length; j<innerSize; j++)
                            {
                                if (!getConfiguration().getPropertySuppressionStrategy()
                                        .suppressProperty(
                                            beanClass,
                                            descriptors[j].getPropertyType(),
                                            descriptors[j].getName()))
                                {
                                    propertyDescriptors.add( descriptors[j] );
                                }
                            }
                        }
                    }
                }

                addAllSuperinterfaces(beanClass, propertyDescriptors);

                // what happens when size is zero?
                properties = new BeanProperty[ propertyDescriptors.size() ];
                int count = 0;
                for ( Iterator it = propertyDescriptors.iterator(); it.hasNext(); count++)
                {
                    PropertyDescriptor propertyDescriptor = (PropertyDescriptor) it.next();
                    properties[count] = new BeanProperty( propertyDescriptor );
                }
            }
            return properties;
        }

        /**
         * Adds all super interfaces.
         * Super interface methods are not returned within the usual
         * bean info for an interface.
         * @param clazz <code>Class</code>, not null
         * @param propertyDescriptors <code>ArrayList</code> of <code>PropertyDescriptor</code>s', not null
         */
        private void addAllSuperinterfaces(Class clazz, ArrayList propertyDescriptors)
        {
            if (clazz.isInterface())
            {
                Class[] superinterfaces = clazz.getInterfaces();
                for (int i=0, size=superinterfaces.length; i<size; i++)
                {
                    try
                    {

                        BeanInfo beanInfo;
                        if( getConfiguration().ignoreAllBeanInfo() )
                        {
                            beanInfo = Introspector.getBeanInfo( superinterfaces[i], Introspector.IGNORE_ALL_BEANINFO );
                        }
                        else
                        {
                            beanInfo = Introspector.getBeanInfo( superinterfaces[i] );
                        }
                        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
                        for (int j=0, descriptorLength=descriptors.length; j<descriptorLength ; j++)
                        {
                            if (!getConfiguration().getPropertySuppressionStrategy()
                                    .suppressProperty(
                                        beanClass,
                                        descriptors[j].getPropertyType(),
                                        descriptors[j].getName()))
                            {
                                propertyDescriptors.add( descriptors[j] );
                            }
                        }
                        addAllSuperinterfaces(superinterfaces[i], propertyDescriptors);

                    }
                    catch (IntrospectionException ex)
                    {
                        log.info("Introspection on superinterface failed.", ex);
                    }
                }
            }
        }

    }

    /** Implementation for DynaClasses */
    private class DynaClassBeanType extends BeanType
    {
        /** BeanType for this DynaClass */
        private DynaClass dynaClass;
        /** Properties extracted in constuctor */
        private BeanProperty[] properties;

        /**
         * Constructs a BeanType for a DynaClass
         * @param dynaClass not null
         */
        public DynaClassBeanType(DynaClass dynaClass)
        {
            this.dynaClass = dynaClass;
            DynaProperty[] dynaProperties = dynaClass.getDynaProperties();
            properties = new BeanProperty[dynaProperties.length];
            for (int i=0, size=dynaProperties.length; i<size; i++)
            {
                properties[i] = new BeanProperty(dynaProperties[i]);
            }
        }

        /** @see BeanType#getBeanName */
        public String getBeanName()
        {
            return dynaClass.getName();
        }
        /** @see BeanType#getElementType */
        public Class getElementType()
        {
            return DynaClass.class;
        }
        /** @see BeanType#isPrimitiveType */
        public boolean isPrimitiveType()
        {
            return false;
        }
        /** @see BeanType#isMapType */
        public boolean isMapType()
        {
            return false;
        }
        /** @see BeanType#isLoopType */
        public boolean isLoopType()
        {
            return false;
        }
        /** @see BeanType#getProperties */
        public BeanProperty[] getProperties()
        {
            return properties;
        }
    }
}
