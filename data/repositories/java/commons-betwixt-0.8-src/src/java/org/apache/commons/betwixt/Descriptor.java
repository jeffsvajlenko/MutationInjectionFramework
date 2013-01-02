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

import org.apache.commons.betwixt.expression.Expression;
import org.apache.commons.betwixt.expression.Updater;

/** <p>Describes a content node mapping.</p>
  * Common superclass for types of <code>Descriptor</code></p>
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public abstract class Descriptor
{

    /** the expression used to evaluate the text value of this node */
    private Expression textExpression;
    /** the updater used to update the current bean from the text value of this node */
    private Updater updater;
    /** The property expression to which this node refers to, or null if it is just a constant */
    private String propertyName;
    /** the property type associated with this node, if any */
    private Class propertyType;
    /** the singular property type (i.e. the type ignoring the Collection or Array */
    private Class singularPropertyType;
    /** Options set for this Descriptor */
    private Options options = new Options();


    /** Base constructor */
    public Descriptor()
    {
    }

    /**
     * Gets the expression used to evaluate the text value of this node
     * for a particular <code>Context</code>.
     * @return the expression used to evaluate the text value of this node
     */
    public Expression getTextExpression()
    {
        return textExpression;
    }

    /**
     * Sets the expression used to evaluate the text value of this node
     * for a particular <code>Context</code>
     * @param textExpression the Expression to be used to evaluate the value of this node
     */
    public void setTextExpression(Expression textExpression)
    {
        this.textExpression = textExpression;
    }

    /**
     * Gets the <code>Updater</code> used to update a <code>Context</code> from the text value
     * corresponding to this node in an xml document
     * @return the Update that should be used to update the value of this node
     */
    public Updater getUpdater()
    {
        return updater;
    }

    /**
     * Sets the <code>Updater</code> used to update a <code>Context</code> from the text value
     * corresponding to this node in an xml document
     * @param updater the Updater to be used to update the values of this node
     */
    public void setUpdater(Updater updater)
    {
        this.updater = updater;
    }

    /**
     * Gets the type of the bean property associated with this node, if any
     * @return the property type associated with this node, if any
     */
    public Class getPropertyType()
    {
        return propertyType;
    }

    /**
     * Sets the type of the bean property associated with this node, if any
     * @param propertyType the Class of the bean property
     */
    public void setPropertyType(Class propertyType)
    {
        this.propertyType = propertyType;
    }


    /**
     * Gets the name of the bean property to which this node refers
     * @return the name of the bean property to which this node refers to,
     * or null if it is just a constant
     */
    public String getPropertyName()
    {
        return propertyName;
    }

    /**
     * Sets the name of the bean property to which this node refers
     * @param propertyName the name of the bean property.
     * Or null, if this node is not mapped to to a bean property
     */
    public void setPropertyName(String propertyName)
    {
        this.propertyName = propertyName;
    }

    /**
     * Gets the underlying type ignoring any wrapping a Collection or Array.
     *
     * @return if this property is a 1-N relationship then this returns the type
     * of a single property value.
     */
    public Class getSingularPropertyType()
    {
        if ( singularPropertyType == null )
        {
            return getPropertyType();
        }
        return singularPropertyType;
    }

    /**
     * Sets the underlying type ignoring any wrapping Collection or Array.
     *
     * @param singularPropertyType the Class of the items in the Collection or Array.
     * If node is associated with a collective bean property, then this should not be null.
     */
    public void setSingularPropertyType(Class singularPropertyType)
    {
        this.singularPropertyType = singularPropertyType;
    }


    /**
     * Gets the options for this descriptor.
     * Options are used to communicate non-declarative
     * (optinal) behaviour hints.
     * @return <code>Options</code>, not null
     */
    public Options getOptions()
    {
        return options;
    }

    /**
     * Sets the options for this descriptor.
     * Options are used to communicate non-declarative
     * (optinal) behaviour hints.
     * @param options
     */
    public void setOptions(Options options)
    {
        this.options = options;
    }

}
