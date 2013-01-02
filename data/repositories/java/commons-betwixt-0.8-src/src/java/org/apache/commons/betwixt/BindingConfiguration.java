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

import java.io.Serializable;

import org.apache.commons.betwixt.strategy.DefaultObjectStringConverter;
import org.apache.commons.betwixt.strategy.IdStoringStrategy;
import org.apache.commons.betwixt.strategy.ObjectStringConverter;
import org.apache.commons.betwixt.strategy.ValueSuppressionStrategy;

/** <p>Stores mapping phase binding configuration.</p>
  *
  * <p>There are two phase in Betwixt's processing.
  * The first phase is the introspection of the bean.
  * Strutural configuration settings effect this phase.
  * The second phase comes when Betwixt dynamically uses
  * reflection to execute the mapping.
  * This object stores configuration settings pertaining
  * to the second phase.</p>
  *
  * <p>These common settings have been collected into one class
  * to make round tripping easier since the same <code>BindingConfiguration</code>
  * can be shared.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @since 0.5
  */
public class BindingConfiguration implements Serializable
{

    /** Should <code>ID</code>'s and <code>IDREF</code> be used cross-reference matching objects? */
    private boolean mapIDs = true;
    /** Converts objects &lt-&gt; strings */
    private ObjectStringConverter objectStringConverter;
    /** The name of the classname attribute used when creating derived beans */
    private String classNameAttribute = "className";
    /** Strategy for suppressing attributes with certain values when writing */
    private ValueSuppressionStrategy valueSuppressionStrategy  = ValueSuppressionStrategy.DEFAULT;
    /** Strategy for storing and accessing ID values */
    private IdStoringStrategy idStoringStrategy = IdStoringStrategy.createDefault();

    /**
     * Constructs a BindingConfiguration with default properties.
     */
    public BindingConfiguration()
    {
        this(new DefaultObjectStringConverter(), true);
    }

    /**
     * Constructs a BindingConfiguration
     * @param objectStringConverter the <code>ObjectStringConverter</code>
     * to be used to convert Objects &lt;-&gt; Strings
     * @param mapIDs should <code>ID</code>'s and <code>IDREF</code> be used to cross-reference
     */
    public BindingConfiguration(ObjectStringConverter objectStringConverter, boolean mapIDs)
    {
        setObjectStringConverter(objectStringConverter);
        setMapIDs(mapIDs);
    }

    /**
      * Gets the Object &lt;-&gt; String converter.
      * @return the ObjectStringConverter to use, not null
      */
    public ObjectStringConverter getObjectStringConverter()
    {
        return objectStringConverter;
    }

    /**
      * Sets the Object &lt;-&gt; String converter.
      * @param objectStringConverter the ObjectStringConverter to be used, not null
      */
    public void setObjectStringConverter(ObjectStringConverter objectStringConverter)
    {
        this.objectStringConverter = objectStringConverter;
    }

    /**
     * Should <code>ID</code>'s and <code>IDREF</code> attributes
     * be used to cross-reference matching objects?
     *
     * @return true if <code>ID</code> and <code>IDREF</code>
     * attributes should be used to cross-reference instances
     */
    public boolean getMapIDs()
    {
        return mapIDs;
    }

    /**
     *Should <code>ID</code>'s and <code>IDREF</code> attributes
     * be used to cross-reference matching objects?
     *
     * @param mapIDs pass true if <code>ID</code>'s should be used to cross-reference
     */
    public void setMapIDs(boolean mapIDs)
    {
        this.mapIDs = mapIDs;
    }

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


    /**
     * Gets the <code>ValueSuppressionStrategy</code>.
     * This is used to control the expression of attributes with certain values.
     * @since 0.7
     * @return <code>ValueSuppressionStrategy</code>, not null
     */
    public ValueSuppressionStrategy getValueSuppressionStrategy()
    {
        return valueSuppressionStrategy;
    }

    /**
     * Sets the <code>ValueSuppressionStrategy</code>.
     * This is used to control the expression of attributes with certain values.
     * @since 0.7
     * @param valueSuppressionStrategy <code>ValueSuppressionStrategy</code>, not null
     */
    public void setValueSuppressionStrategy(
        ValueSuppressionStrategy valueSuppressionStrategy)
    {
        this.valueSuppressionStrategy = valueSuppressionStrategy;
    }

    /**
     * Gets the strategy used to manage storage and retrieval of id's.
     *
     * @since 0.7
     * @return Returns the <code>IdStoringStrategy</code>, not null
     */
    public IdStoringStrategy getIdMappingStrategy()
    {
        return idStoringStrategy;
    }

    /**
     * Sets the strategy used to manage storage and retrieval of id's.
     *
     * @since 0.7
     * @param idMappingStrategy
     *            <code>IdStoringStrategy</code> to be set, not null
     */
    public void setIdMappingStrategy(IdStoringStrategy idMappingStrategy)
    {
        this.idStoringStrategy = idMappingStrategy;
    }
}
