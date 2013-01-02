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

package org.apache.commons.betwixt;

import org.apache.commons.betwixt.strategy.AttributeSuppressionStrategy;
import org.apache.commons.betwixt.strategy.ClassNormalizer;
import org.apache.commons.betwixt.strategy.CollectiveTypeStrategy;
import org.apache.commons.betwixt.strategy.DefaultNameMapper;
import org.apache.commons.betwixt.strategy.DefaultPluralStemmer;
import org.apache.commons.betwixt.strategy.ElementSuppressionStrategy;
import org.apache.commons.betwixt.strategy.MappingDerivationStrategy;
import org.apache.commons.betwixt.strategy.NameMapper;
import org.apache.commons.betwixt.strategy.NamespacePrefixMapper;
import org.apache.commons.betwixt.strategy.PluralStemmer;
import org.apache.commons.betwixt.strategy.PropertySuppressionStrategy;
import org.apache.commons.betwixt.strategy.SimpleTypeMapper;
import org.apache.commons.betwixt.strategy.StandardSimpleTypeMapper;
import org.apache.commons.betwixt.strategy.TypeBindingStrategy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>Stores introspection phase binding configuration.</p>
 * <p>
 * There are two phase in Betwixt's processing.
 * The first phase is the introspection of the bean.
 * Strutural configuration settings effect this phase.
 * The second phase comes when Betwixt dynamically uses reflection
 * to execute the mapping.
 * This object stores configuration settings pertaining to the first phase.
 * </p>
 * <p>
 * These common settings have been collected into one class so that they can
 * be more easily shared not only between the objects that execute the introspection
 * but also (by a user) between different <code>XMLIntrospector</code>s.
 * </p>
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 471234 $
 */
public class IntrospectionConfiguration
{

    /** should attributes or elements be used for primitive types */
    private boolean attributesForPrimitives = false;

    /** should we wrap collections in an extra element? */
    private boolean wrapCollectionsInElement = true;

    /** Should the existing bean info search path for java.reflect.Introspector be used? */
    private boolean useBeanInfoSearchPath = false;

    /** Should existing BeanInfo classes be used at all for java.reflect.Introspector */
    private boolean ignoreAllBeanInfo = false;

    // pluggable strategies
    /** The strategy used to detect matching singular and plural properties */
    private PluralStemmer pluralStemmer;

    /** The strategy used to convert bean type names into element names */
    private NameMapper elementNameMapper;

    /** Strategy normalizes the Class of the Object before introspection */
    private ClassNormalizer classNormalizer = new ClassNormalizer();

    /** Log for introspection messages */
    private Log introspectionLog = LogFactory.getLog(XMLIntrospector.class);

    /**
     * The strategy used to convert bean type names into attribute names
     * It will default to the normal nameMapper.
     */
    private NameMapper attributeNameMapper;

    /** Prefix naming strategy */
    private NamespacePrefixMapper prefixMapper = new NamespacePrefixMapper();
    /** Mapping strategy for simple types */
    private SimpleTypeMapper simpleTypeMapper = new StandardSimpleTypeMapper();
    /** Binding strategy for Java type */
    private TypeBindingStrategy typeBindingStrategy = TypeBindingStrategy.DEFAULT;
    /** Strategy used for determining which types are collective */
    private CollectiveTypeStrategy collectiveTypeStrategy = CollectiveTypeStrategy.DEFAULT;

    /** Strategy for suppressing attributes */
    private AttributeSuppressionStrategy attributeSuppressionStrategy = AttributeSuppressionStrategy.DEFAULT;
    /** Strategy for suppressing elements */
    private ElementSuppressionStrategy elementSuppressionStrategy = ElementSuppressionStrategy.DEFAULT;


    /**
     * Strategy used to determine whether the bind or introspection time type is to be used to
     * determine the mapping.
     */
    private MappingDerivationStrategy mappingDerivationStrategy = MappingDerivationStrategy.DEFAULT;

    /**
     * Strategy used to determine which properties should be ignored
     */
    private PropertySuppressionStrategy propertySuppressionStrategy = PropertySuppressionStrategy.DEFAULT;

    /**
      * Gets the <code>ClassNormalizer</code> strategy.
      * This is used to determine the Class to be introspected
      * (the normalized Class).
      *
      * @return the <code>ClassNormalizer</code> used to determine the Class to be introspected
      * for a given Object.
      */
    public ClassNormalizer getClassNormalizer()
    {
        return classNormalizer;
    }

    /**
      * Sets the <code>ClassNormalizer</code> strategy.
      * This is used to determine the Class to be introspected
      * (the normalized Class).
      *
      * @param classNormalizer the <code>ClassNormalizer</code> to be used to determine
      * the Class to be introspected for a given Object.
      */
    public void setClassNormalizer(ClassNormalizer classNormalizer)
    {
        this.classNormalizer = classNormalizer;
    }

    /**
      * Should attributes (or elements) be used for primitive types.
      * @return true if primitive types will be mapped to attributes in the introspection
      */
    public boolean isAttributesForPrimitives()
    {
        return attributesForPrimitives;
    }

    /**
      * Set whether attributes (or elements) should be used for primitive types.
      * @param attributesForPrimitives pass trus to map primitives to attributes,
      *        pass false to map primitives to elements
      */
    public void setAttributesForPrimitives(boolean attributesForPrimitives)
    {
        this.attributesForPrimitives = attributesForPrimitives;
    }

    /**
     * Should collections be wrapped in an extra element?
     *
     * @return whether we should we wrap collections in an extra element?
     */
    public boolean isWrapCollectionsInElement()
    {
        return wrapCollectionsInElement;
    }

    /**
     * Sets whether we should we wrap collections in an extra element.
     *
     * @param wrapCollectionsInElement pass true if collections should be wrapped in a
     *        parent element
     */
    public void setWrapCollectionsInElement(boolean wrapCollectionsInElement)
    {
        this.wrapCollectionsInElement = wrapCollectionsInElement;
    }

    /**
     * Get singular and plural matching strategy.
     *
     * @return the strategy used to detect matching singular and plural properties
     */
    public PluralStemmer getPluralStemmer()
    {
        if ( pluralStemmer == null )
        {
            pluralStemmer = createPluralStemmer();
        }
        return pluralStemmer;
    }

    /**
     * Sets the strategy used to detect matching singular and plural properties
     *
     * @param pluralStemmer the PluralStemmer used to match singular and plural
     */
    public void setPluralStemmer(PluralStemmer pluralStemmer)
    {
        this.pluralStemmer = pluralStemmer;
    }

    /**
     * Gets the name mapping strategy used to convert bean names into elements.
     *
     * @return the strategy used to convert bean type names into element
     * names. If no element mapper is currently defined then a default one is created.
     */
    public NameMapper getElementNameMapper()
    {
        if ( elementNameMapper == null )
        {
            elementNameMapper = createNameMapper();
        }
        return elementNameMapper;
    }

    /**
     * Sets the strategy used to convert bean type names into element names
     * @param nameMapper the NameMapper to use for the conversion
     */
    public void setElementNameMapper(NameMapper nameMapper)
    {
        this.elementNameMapper = nameMapper;
    }

    /**
     * Gets the name mapping strategy used to convert bean names into attributes.
     *
     * @return the strategy used to convert bean type names into attribute
     * names. If no attributeNamemapper is known, it will default to the ElementNameMapper
     */
    public NameMapper getAttributeNameMapper()
    {
        if (attributeNameMapper == null)
        {
            attributeNameMapper = createNameMapper();
        }
        return attributeNameMapper;
    }


    /**
     * Sets the strategy used to convert bean type names into attribute names
     * @param nameMapper the NameMapper to use for the convertion
     */
    public void setAttributeNameMapper(NameMapper nameMapper)
    {
        this.attributeNameMapper = nameMapper;
    }

    /**
     * <p>Should the original <code>java.reflect.Introspector</code> bean info search path be used?</p>
     * <p>
     * Default is false.
     * </p>
     *
     * @return boolean if the beanInfoSearchPath should be used.
     */
    public boolean useBeanInfoSearchPath()
    {
        return useBeanInfoSearchPath;
    }

    /**
     * Specifies if you want to use the beanInfoSearchPath
     * @see java.beans.Introspector for more details
     * @param useBeanInfoSearchPath
     */
    public void setUseBeanInfoSearchPath(boolean useBeanInfoSearchPath)
    {
        this.useBeanInfoSearchPath = useBeanInfoSearchPath;
    }

    /**
     * <p>Should existing BeanInfo classes be ignored by <code>java.reflect.Introspector</code>.</p>
     * <p>
     * Default is false.
     * </p>
     *
     * @return boolean if the BeanInfo classes should be used.
     */
    public boolean ignoreAllBeanInfo()
    {
        return ignoreAllBeanInfo;
    }

    /**
     * Specifies if you want to ignore existing BeanInfo classes at all for introspection
     * @see java.beans.Introspector for more details
     * @param ignoreAllBeanInfo set to true to ignore all BeanInfo classes
     * @since 0.8
     */
    public void setIgnoreAllBeanInfo(boolean ignoreAllBeanInfo)
    {
        this.ignoreAllBeanInfo = ignoreAllBeanInfo;
    }


    /**
     * A Factory method to lazily create a new strategy
     * to detect matching singular and plural properties.
     *
     * @return new defualt PluralStemmer implementation
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
     */
    protected NameMapper createNameMapper()
    {
        return new DefaultNameMapper();
    }

    /**
     * Gets the common Log used for introspection.
     * It is more convenient to use a single Log
     * that can be easily configured.
     * @return Log, not null
     */
    public Log getIntrospectionLog()
    {
        return introspectionLog;
    }

    /**
     * Sets the common Log used by introspection.
     * It is more convenient to use a single Log
     * that can be easily configured.
     * @param log Log, not null
     */
    public void setIntrospectionLog(Log log)
    {
        introspectionLog = log;
    }


    /**
     * Gets the <code>NamespacePrefixMapper</code> used to convert namespace URIs
     * into prefixes.
     * @return NamespacePrefixMapper, not null
     */
    public NamespacePrefixMapper getPrefixMapper()
    {
        return prefixMapper;
    }

    /**
     * Sets the <code>NamespacePrefixMapper</code> used to convert namespave URIs
     * into prefixes.
     * @param mapper NamespacePrefixMapper, not null
     */
    public void setPrefixMapper(NamespacePrefixMapper mapper)
    {
        prefixMapper = mapper;
    }


    /**
     * Gets the simple type binding strategy.
     * @return SimpleTypeMapper, not null
     */
    public SimpleTypeMapper getSimpleTypeMapper()
    {
        return simpleTypeMapper;
    }

    /**
     * Sets the simple type binding strategy.
     * @param mapper SimpleTypeMapper, not null
     */
    public void setSimpleTypeMapper(SimpleTypeMapper mapper)
    {
        simpleTypeMapper = mapper;
    }

    /**
     * Gets the <code>TypeBindingStrategy</code> to be used
     * to determine the binding for Java types.
     * @return the <code>TypeBindingStrategy</code> to be used,
     * not null
     */
    public TypeBindingStrategy getTypeBindingStrategy()
    {
        return typeBindingStrategy;
    }

    /**
     * Sets the <code>TypeBindingStrategy</code> to be used
     * to determine the binding for Java types.
     * @param typeBindingStrategy the <code>TypeBindingStrategy</code> to be used,
     * not null
     */
    public void setTypeBindingStrategy(TypeBindingStrategy typeBindingStrategy)
    {
        this.typeBindingStrategy = typeBindingStrategy;
    }


    /**
     * Gets the <code>MappingDerivationStrategy</code>
     * used to determine whether the bind or introspection time
     * type should determine the mapping.
     * @since 0.7
     * @return <code>MappingDerivationStrategy</code>, not null
     */
    public MappingDerivationStrategy getMappingDerivationStrategy()
    {
        return mappingDerivationStrategy;
    }
    /**
     * Sets the <code>MappingDerivationStrategy</code>
     * used to determine whether the bind or introspection time
     * type should determine the mapping.
     * @since 0.7
     * @param mappingDerivationStrategy <code>MappingDerivationStrategy</code>, not null
     */
    public void setMappingDerivationStrategy(
        MappingDerivationStrategy mappingDerivationStrategy)
    {
        this.mappingDerivationStrategy = mappingDerivationStrategy;
    }

    /**
     * Gets the strategy which determines the properties to be ignored.
     * @since 0.7
     * @return the <code>PropertySuppressionStrategy</code> to be used for introspection, not null
     */
    public PropertySuppressionStrategy getPropertySuppressionStrategy()
    {
        return propertySuppressionStrategy;
    }

    /**
     * Sets the strategy which determines the properties to be ignored.
     * @since 0.7
     * @param propertySuppressionStrategy the <code>PropertySuppressionStrategy</code> to be used for introspection, not null
     */
    public void setPropertySuppressionStrategy(
        PropertySuppressionStrategy propertySuppressionStrategy)
    {
        this.propertySuppressionStrategy = propertySuppressionStrategy;
    }

    /**
     * Gets the strategy used to determine which types are collective.
     * @return <code>CollectiveTypeStrategy</code>, not null
     * @since 0.8
     */
    public CollectiveTypeStrategy getCollectiveTypeStrategy()
    {
        return collectiveTypeStrategy;
    }

    /**
     * Sets the strategy used to determine which types are collective.
     * @param collectiveTypeStrategy <code>CollectiveTypeStrategy</code>, not null
     * @since 0.8
     */
    public void setCollectiveTypeStrategy(
        CollectiveTypeStrategy collectiveTypeStrategy)
    {
        this.collectiveTypeStrategy = collectiveTypeStrategy;
    }

    /**
     * Is this a loop type class?
     * @since 0.7
     * @param type is this <code>Class</code> a loop type?
     * @return true if the type is a loop type, or if type is null
     */
    public boolean isLoopType(Class type)
    {
        return getCollectiveTypeStrategy().isCollective(type);
    }


    /**
     * Returns the <code>AttributeSuppressionStrategy</code>.
     * This is used to suppress attributes, e.g. for versioning.
     *
     * @since 0.8
     * @return the strategy
     */
    public AttributeSuppressionStrategy getAttributeSuppressionStrategy()
    {
        return attributeSuppressionStrategy;
    }

    /**
     * Sets the <code>AttributeSuppressionStrategy</code>.
     * This is used to suppress attributes, e.g. for versioning.
     *
     * @since 0.8
     * @param attributeSuppressionStrategy the strategy
     */
    public void setAttributeSuppressionStrategy(
        AttributeSuppressionStrategy attributeSuppressionStrategy)
    {
        this.attributeSuppressionStrategy = attributeSuppressionStrategy;
    }

    /**
     * Returns the <code>ElementSuppressionStrategy</code>.
     * This is used to suppress elements, e.g. for versioning.
     *
     * @since 0.8
     * @return the strategy
     */
    public ElementSuppressionStrategy getElementSuppressionStrategy()
    {
        return elementSuppressionStrategy;
    }

    /**
     * Sets the <code>ElementSuppressionStrategy</code>.
     * This is used to suppress elements, e.g. for versioning.
     *
     * @since 0.8
     * @param elementSuppressionStrategy the strategy
     */
    public void setElementSuppressionStrategy(
        ElementSuppressionStrategy elementSuppressionStrategy)
    {
        this.elementSuppressionStrategy = elementSuppressionStrategy;
    }
}
