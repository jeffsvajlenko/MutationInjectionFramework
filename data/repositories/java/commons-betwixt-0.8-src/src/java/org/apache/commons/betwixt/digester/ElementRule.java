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
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLUtils;
import org.apache.commons.betwixt.expression.ConstantExpression;
import org.apache.commons.betwixt.expression.Expression;
import org.apache.commons.betwixt.expression.IteratorExpression;
import org.apache.commons.betwixt.expression.MethodExpression;
import org.apache.commons.betwixt.expression.MethodUpdater;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * <p>
 * <code>ElementRule</code> the digester Rule for parsing the &lt;element&gt;
 * elements.
 * </p>
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class ElementRule extends MappedPropertyRule
{

    /** Logger */
    private static Log log = LogFactory.getLog(ElementRule.class);

    /**
     * Sets the log for this class
     *
     * @param newLog
     *            the new Log implementation for this class to use
     * @since 0.5
     */
    public static final void setLog(Log newLog)
    {
        log = newLog;
    }

    /** Class for which the .bewixt file is being digested */
    private Class beanClass;

    /** Base constructor */
    public ElementRule()
    {
    }

    // Rule interface
    // -------------------------------------------------------------------------

    /**
     * Process the beginning of this element.
     *
     * @param attributes
     *            The attribute list of this element
     * @throws SAXException
     *             1. If this tag's parent is not either an info or element tag.
     *             2. If the name attribute is not valid XML element name. 3. If
     *             the name attribute is not present 4. If the class attribute
     *             is not a loadable (fully qualified) class name
     */
    public void begin(String name, String namespace, Attributes attributes)
    throws SAXException
    {
        String nameAttributeValue = attributes.getValue("name");

        ElementDescriptor descriptor = new ElementDescriptor();
        descriptor.setLocalName(nameAttributeValue);
        String uri = attributes.getValue("uri");
        String qName = nameAttributeValue;
        if (uri != null && nameAttributeValue != null)
        {
            descriptor.setURI(uri);
            String prefix = getXMLIntrospector().getConfiguration()
                            .getPrefixMapper().getPrefix(uri);
            qName = prefix + ":" + nameAttributeValue;
        }
        descriptor.setQualifiedName(qName);

        String propertyName = attributes.getValue("property");
        descriptor.setPropertyName(propertyName);

        String propertyType = attributes.getValue("type");

        if (log.isTraceEnabled())
        {
            log.trace("(BEGIN) name=" + nameAttributeValue + " uri=" + uri
                      + " property=" + propertyName + " type=" + propertyType);
        }

        // set mapping derivation
        String mappingDerivation = attributes.getValue("mappingDerivation");
        if ("introspection".equals(mappingDerivation))
        {
            descriptor.setUseBindTimeTypeForMapping(false);
        }
        else if ("bind".equals(mappingDerivation))
        {
            descriptor.setUseBindTimeTypeForMapping(true);
        }

        // set the property type using reflection
        descriptor.setPropertyType(getPropertyType(propertyType, beanClass,
                                   propertyName));

        boolean isCollective = getXMLIntrospector().getConfiguration()
                               .isLoopType(descriptor.getPropertyType());

        descriptor.setCollective(isCollective);

        // check that the name attribute is present
        if (!isCollective
                && (nameAttributeValue == null || nameAttributeValue.trim()
                    .equals("")))
        {
            // allow polymorphic mappings but log note for user
            log
            .info("No name attribute has been specified. This element will be polymorphic.");
        }

        // check that name is well formed
        if (nameAttributeValue != null
                && !XMLUtils.isWellFormedXMLName(nameAttributeValue))
        {
            throw new SAXException("'" + nameAttributeValue
                                   + "' would not be a well formed xml element name.");
        }

        String implementationClass = attributes.getValue("class");
        if (log.isTraceEnabled())
        {
            log.trace("'class' attribute=" + implementationClass);
        }
        if (implementationClass != null)
        {
            try
            {

                Class clazz = Class.forName(implementationClass);
                descriptor.setImplementationClass(clazz);

            }
            catch (Exception e)
            {
                if (log.isDebugEnabled())
                {
                    log.debug(
                        "Cannot load class named: " + implementationClass,
                        e);
                }
                throw new SAXException("Cannot load class named: "
                                       + implementationClass);
            }
        }

        if (propertyName != null && propertyName.length() > 0)
        {
            boolean forceAccessible = "true".equals(attributes
                                                    .getValue("forceAccessible"));
            configureDescriptor(descriptor, attributes.getValue("updater"),
                                forceAccessible);

        }
        else
        {
            String value = attributes.getValue("value");
            if (value != null)
            {
                descriptor.setTextExpression(new ConstantExpression(value));
            }
        }

        Object top = digester.peek();
        if (top instanceof XMLBeanInfo)
        {
            XMLBeanInfo beanInfo = (XMLBeanInfo) top;
            beanInfo.setElementDescriptor(descriptor);
            beanClass = beanInfo.getBeanClass();
            descriptor.setPropertyType(beanClass);

        }
        else if (top instanceof ElementDescriptor)
        {
            ElementDescriptor parent = (ElementDescriptor) top;
            parent.addElementDescriptor(descriptor);

        }
        else
        {
            throw new SAXException("Invalid use of <element>. It should "
                                   + "be nested inside <info> or other <element> nodes");
        }

        digester.push(descriptor);
    }

    /**
     * Process the end of this element.
     */
    public void end(String name, String namespace)
    {
        ElementDescriptor descriptor = (ElementDescriptor)digester.pop();

        final Object peek = digester.peek();

        if(peek instanceof ElementDescriptor)
        {
            ElementDescriptor parent = (ElementDescriptor)digester.peek();

            // check for element suppression
            if( getXMLIntrospector().getConfiguration().getElementSuppressionStrategy().suppress(descriptor))
            {
                parent.removeElementDescriptor(descriptor);
            }
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * Sets the Expression and Updater from a bean property name Uses the
     * default updater (from the standard java bean property).
     *
     * @param elementDescriptor
     *            configure this <code>ElementDescriptor</code>
     * @since 0.5
     */
    protected void configureDescriptor(ElementDescriptor elementDescriptor)
    {
        configureDescriptor(elementDescriptor, null);
    }

    /**
     * Sets the Expression and Updater from a bean property name Allows a custom
     * updater to be passed in.
     *
     * @param elementDescriptor
     *            configure this <code>ElementDescriptor</code>
     * @param updateMethodName
     *            custom update method. If null, then use standard
     * @since 0.5
     * @deprecated now calls
     *             <code>#configureDescriptor(ElementDescriptor, String, boolean)</code>
     *             which allow accessibility to be forced. The subclassing API
     *             was not really considered carefully when this class was
     *             created. If anyone subclasses this method please contact the
     *             mailing list and suitable hooks will be placed into the code.
     */
    protected void configureDescriptor(ElementDescriptor elementDescriptor,
                                       String updateMethodName)
    {
        configureDescriptor(elementDescriptor, null, false);
    }

    /**
     * Sets the Expression and Updater from a bean property name Allows a custom
     * updater to be passed in.
     *
     * @param elementDescriptor
     *            configure this <code>ElementDescriptor</code>
     * @param updateMethodName
     *            custom update method. If null, then use standard
     * @param forceAccessible
     *            if true and updateMethodName is not null, then non-public
     *            methods will be searched and made accessible
     *            (Method.setAccessible(true))
     */
    private void configureDescriptor(ElementDescriptor elementDescriptor,
                                     String updateMethodName, boolean forceAccessible)
    {
        Class beanClass = getBeanClass();
        if (beanClass != null)
        {
            String name = elementDescriptor.getPropertyName();
            PropertyDescriptor descriptor = getPropertyDescriptor(beanClass,
                                            name);

            if (descriptor == null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("Cannot find property matching " + name);
                }
            }
            else
            {
                configureProperty(elementDescriptor, descriptor,
                                  updateMethodName, forceAccessible, beanClass);

                getProcessedPropertyNameSet().add(name);
            }
        }
    }

    /**
     * Configure an <code>ElementDescriptor</code> from a
     * <code>PropertyDescriptor</code>. A custom update method may be set.
     *
     * @param elementDescriptor
     *            configure this <code>ElementDescriptor</code>
     * @param propertyDescriptor
     *            configure from this <code>PropertyDescriptor</code>
     * @param updateMethodName
     *            the name of the custom updater method to user. If null, then
     *            then
     * @param forceAccessible
     *            if true and updateMethodName is not null, then non-public
     *            methods will be searched and made accessible
     *            (Method.setAccessible(true))
     * @param beanClass
     *            the <code>Class</code> from which the update method should
     *            be found. This may be null only when
     *            <code>updateMethodName</code> is also null.
     */
    private void configureProperty(ElementDescriptor elementDescriptor,
                                   PropertyDescriptor propertyDescriptor, String updateMethodName,
                                   boolean forceAccessible, Class beanClass)
    {

        Class type = propertyDescriptor.getPropertyType();
        Method readMethod = propertyDescriptor.getReadMethod();
        Method writeMethod = propertyDescriptor.getWriteMethod();

        elementDescriptor.setPropertyType(type);

        // TODO: associate more bean information with the descriptor?
        // nodeDescriptor.setDisplayName( propertyDescriptor.getDisplayName() );
        // nodeDescriptor.setShortDescription(
        // propertyDescriptor.getShortDescription() );

        if (readMethod == null)
        {
            log.trace("No read method");
            return;
        }

        if (log.isTraceEnabled())
        {
            log.trace("Read method=" + readMethod.getName());
        }

        // choose response from property type

        final MethodExpression methodExpression = new MethodExpression(readMethod);
        if (getXMLIntrospector().isPrimitiveType(type))
        {
            elementDescriptor
            .setTextExpression(methodExpression);

        }
        else if (getXMLIntrospector().isLoopType(type))
        {
            log.trace("Loop type ??");

            // don't wrap this in an extra element as its specified in the
            // XML descriptor so no need.
            Expression expression = methodExpression;

            // Support collectives with standard property setters (not adders)
            // that use polymorphism to read objects.
            boolean standardProperty = false;
            if (updateMethodName != null && writeMethod != null && writeMethod.getName().equals(updateMethodName))
            {
                final Class[] parameters = writeMethod.getParameterTypes();
                if (parameters.length == 1)
                {
                    Class setterType = parameters[0];
                    if (type.equals(setterType))
                    {
                        standardProperty = true;
                    }
                }
            }
            if (!standardProperty)
            {
                expression = new IteratorExpression(methodExpression);
            }
            elementDescriptor.setContextExpression(expression);
            elementDescriptor.setHollow(true);

            writeMethod = null;

            if (Map.class.isAssignableFrom(type))
            {
                elementDescriptor.setLocalName("entry");
                // add elements for reading
                ElementDescriptor keyDescriptor = new ElementDescriptor("key");
                keyDescriptor.setHollow(true);
                elementDescriptor.addElementDescriptor(keyDescriptor);

                ElementDescriptor valueDescriptor = new ElementDescriptor(
                    "value");
                valueDescriptor.setHollow(true);
                elementDescriptor.addElementDescriptor(valueDescriptor);
            }

        }
        else
        {
            log.trace("Standard property");
            elementDescriptor.setHollow(true);
            elementDescriptor.setContextExpression(methodExpression);
        }

        // see if we have a custom method update name
        if (updateMethodName == null)
        {
            // set standard write method
            if (writeMethod != null)
            {
                elementDescriptor.setUpdater(new MethodUpdater(writeMethod));
            }

        }
        else
        {
            // see if we can find and set the custom method
            if (log.isTraceEnabled())
            {
                log.trace("Finding custom method: ");
                log.trace("  on:" + beanClass);
                log.trace("  name:" + updateMethodName);
            }

            Method updateMethod;
            boolean isMapTypeProperty = Map.class.isAssignableFrom(type);
            if (forceAccessible)
            {
                updateMethod = findAnyMethod(updateMethodName, beanClass, isMapTypeProperty);
            }
            else
            {
                updateMethod = findPublicMethod(updateMethodName, beanClass, isMapTypeProperty);
            }

            if (updateMethod == null)
            {
                if (log.isInfoEnabled())
                {

                    log.info("No method with name '" + updateMethodName
                             + "' found for update");
                }
            }
            else
            {
                // assign updater to elementDescriptor
                if (Map.class.isAssignableFrom(type))
                {

                    getXMLIntrospector().assignAdder(updateMethod, elementDescriptor);

                }
                else
                {
                    elementDescriptor
                    .setUpdater(new MethodUpdater(updateMethod));
                    Class singularType = updateMethod.getParameterTypes()[0];
                    elementDescriptor.setSingularPropertyType(singularType);
                    if (singularType != null)
                    {
                        boolean isPrimitive = getXMLIntrospector().isPrimitiveType(singularType);
                        if (isPrimitive)
                        {
                            log.debug("Primitive collective: setting hollow to false");
                            elementDescriptor.setHollow(false);
                        }
                    }
                    if (log.isTraceEnabled())
                    {
                        log.trace("Set custom updater on " + elementDescriptor);
                    }
                }
            }
        }
    }

    private Method findPublicMethod(String updateMethodName, Class beanType, boolean isMapTypeProperty)
    {
        Method[] methods = beanType.getMethods();
        Method updateMethod = searchMethodsForMatch(updateMethodName, methods, isMapTypeProperty);
        return updateMethod;
    }

    private Method searchMethodsForMatch(String updateMethodName,
                                         Method[] methods, boolean isMapType)
    {
        Method updateMethod = null;
        for (int i = 0, size = methods.length; i < size; i++)
        {
            Method method = methods[i];
            if (updateMethodName.equals(method.getName()))
            {

                // updater should have one parameter unless type is Map
                int numParams = 1;
                if (isMapType)
                {
                    // updater for Map should have two parameters
                    numParams = 2;
                }

                // we have a matching name
                // check paramters are correct
                if (methods[i].getParameterTypes().length == numParams)
                {
                    // we'll use first match
                    updateMethod = methods[i];
                    if (log.isTraceEnabled())
                    {
                        log.trace("Matched method:" + updateMethod);
                    }
                    // done since we're using the first match
                    break;
                }
            }
        }
        return updateMethod;
    }

    private Method findAnyMethod(String updateMethodName, Class beanType, boolean isMapTypeProperty)
    {
        // TODO: suspect that this algorithm may run into difficulties
        // on older JVMs (particularly with package privilage interfaces).
        // This seems like too esoteric a use case to worry to much about now
        Method updateMethod = null;
        Class classToTry = beanType;
        do
        {
            Method[] methods = classToTry.getDeclaredMethods();
            updateMethod = searchMethodsForMatch(updateMethodName, methods, isMapTypeProperty);

            // try next superclass - Object will return null and end loop if no
            // method is found
            classToTry = classToTry.getSuperclass();
        }
        while (updateMethod == null && classToTry != null);

        if (updateMethod != null)
        {
            boolean isPublic = Modifier.isPublic(updateMethod.getModifiers())
                               && Modifier.isPublic(beanType.getModifiers());
            if (!isPublic)
            {
                updateMethod.setAccessible(true);
            }
        }
        return updateMethod;
    }
}
