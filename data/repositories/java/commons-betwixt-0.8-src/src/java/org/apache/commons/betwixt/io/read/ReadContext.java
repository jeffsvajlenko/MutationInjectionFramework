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
package org.apache.commons.betwixt.io.read;

import java.beans.IntrospectionException;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.Options;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.expression.Updater;
import org.apache.commons.betwixt.registry.PolymorphicReferenceResolver;
import org.apache.commons.betwixt.strategy.ActionMappingStrategy;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/**
  * <p>Extends <code>Context</code> to provide read specific functionality.</p>
  * <p>
  * Three stacks are used to manage the reading:
  * </p>
  * <ul>
  *     <li><strong>Action mapping stack</strong> contains the {@link MappingAction}'s
  * used to execute the mapping of the current element and it's ancesters back to the
  * document root.</li>
  *     <li><strong>Result stack</strong> contains the objects which are bound
  * to the current element and to each of it's ancester's back to the root</li>
  *     <li><strong>Element mapping stack</strong> records the names of the element
  * and the classes to which they are bound</li>
  * </ul>
  * @author Robert Burrell Donkina
  * @since 0.5
  */
public class ReadContext extends Context
{
    ;
    /** Classloader to be used to load beans during reading */
    private ClassLoader classLoader;
    /** The read specific configuration */
    private ReadConfiguration readConfiguration;
    /** Records the element path together with the locations where classes were mapped*/
    private ArrayStack elementMappingStack = new ArrayStack();
    /** Contains actions for each element */
    private ArrayStack actionMappingStack = new ArrayStack();
    /** Stack contains all beans created */
    private ArrayStack objectStack = new ArrayStack();
    /** Stack contains element descriptors */
    private ArrayStack descriptorStack = new ArrayStack();
    /** Stack contains updaters */
    private ArrayStack updaterStack = new ArrayStack();

    private Class rootClass;
    /** The <code>XMLIntrospector</code> to be used to map the xml*/
    private XMLIntrospector xmlIntrospector;

    /**
      * Constructs a <code>ReadContext</code> with the same settings
      * as an existing <code>Context</code>.
      * @param context not null
      * @param readConfiguration not null
      */
    public ReadContext(Context context, ReadConfiguration readConfiguration)
    {
        super(context);
        this.readConfiguration = readConfiguration;
    }

    /**
      * Constructs a <code>ReadContext</code> with standard log.
      * @param bindingConfiguration the dynamic configuration, not null
      * @param readConfiguration the extra read configuration not null
      */
    public ReadContext(
        BindingConfiguration bindingConfiguration,
        ReadConfiguration readConfiguration)
    {
        this(
            LogFactory.getLog(ReadContext.class),
            bindingConfiguration,
            readConfiguration);
    }

    /**
      * Base constructor
      * @param log log to this Log
      * @param bindingConfiguration the dynamic configuration, not null
      * @param readConfiguration the extra read configuration not null
      */
    public ReadContext(
        Log log,
        BindingConfiguration bindingConfiguration,
        ReadConfiguration readConfiguration)
    {
        super(null, log, bindingConfiguration);
        this.readConfiguration = readConfiguration;
    }

    /**
      * Constructs a <code>ReadContext</code>
      * with the same settings as an existing <code>Context</code>.
      * @param readContext not null
      */
    public ReadContext(ReadContext readContext)
    {
        super(readContext);
        classLoader = readContext.classLoader;
        readConfiguration = readContext.readConfiguration;
    }

    /**
     * Puts a bean into storage indexed by an (xml) ID.
     *
     * @param id the ID string of the xml element associated with the bean
     * @param bean the Object to store, not null
     */
    public void putBean(String id, Object bean)
    {
        getIdMappingStrategy().setReference(this, bean, id);
    }

    /**
     * Gets a bean from storage by an (xml) ID.
     *
     * @param id the ID string of the xml element associated with the bean
     * @return the Object that the ID references, otherwise null
     */
    public Object getBean(String id)
    {
        return getIdMappingStrategy().getReferenced(this, id);
    }

    /**
     * Clears the beans indexed by id.
     */
    public void clearBeans()
    {
        getIdMappingStrategy().reset();
    }

    /**
      * Gets the classloader to be used.
      * @return the classloader that should be used to load all classes, possibly null
      */
    public ClassLoader getClassLoader()
    {
        return classLoader;
    }

    /**
      * Sets the classloader to be used.
      * @param classLoader the ClassLoader to be used, possibly null
      */
    public void setClassLoader(ClassLoader classLoader)
    {
        this.classLoader = classLoader;
    }

    /**
      * Gets the <code>BeanCreationChange</code> to be used to create beans
      * when an element is mapped.
      * @return the BeanCreationChain not null
      */
    public BeanCreationChain getBeanCreationChain()
    {
        return readConfiguration.getBeanCreationChain();
    }

    /**
     * Gets the strategy used to define default mappings actions
     * for elements.
     * @return <code>ActionMappingStrategy</code>. not null
     */
    public ActionMappingStrategy getActionMappingStrategy()
    {
        return readConfiguration.getActionMappingStrategy();
    }

    /**
      * Pops the top element from the element mapping stack.
      * Also removes any mapped class marks below the top element.
      *
      * @return the name of the element popped
      * if there are any more elements on the stack, otherwise null.
      * This is the local name if the parser is namespace aware, otherwise the name
      */
    public String popElement()
    {
        // since the descriptor stack is populated by pushElement,
        // need to ensure that it's correct popped by popElement
        if (!descriptorStack.isEmpty())
        {
            descriptorStack.pop();
        }

        if (!updaterStack.isEmpty())
        {
            updaterStack.pop();
        }

        popOptions();

        Object top = null;
        if (!elementMappingStack.isEmpty())
        {
            top = elementMappingStack.pop();
            if (top != null)
            {
                if (!(top instanceof String))
                {
                    return popElement();
                }
            }
        }

        return (String) top;
    }

    /**
     * Gets the element name for the currently mapped element.
     * @return the name of the currently mapped element,
     * or null if there has been no element mapped
     */
    public String getCurrentElement()
    {
        String result = null;
        int stackSize = elementMappingStack.size();
        int i = 0;
        while ( i < stackSize )
        {
            Object mappedElement = elementMappingStack.peek(i);
            if (mappedElement instanceof String)
            {
                result  = (String) mappedElement;
                break;
            }
            ++i;
        }
        return result;
    }

    /**
      * Gets the Class that was last mapped, if there is one.
      *
      * @return the Class last marked as mapped
      * or null if no class has been mapped
      */
    public Class getLastMappedClass()
    {
        Class lastMapped = null;
        for (int i = 0, size = elementMappingStack.size();
                i < size;
                i++)
        {
            Object entry = elementMappingStack.peek(i);
            if (entry instanceof Class)
            {
                lastMapped = (Class) entry;
                break;
            }
        }
        return lastMapped;
    }

    private ElementDescriptor getParentDescriptor() throws IntrospectionException
    {
        ElementDescriptor result = null;
        if (descriptorStack.size() > 1)
        {
            result = (ElementDescriptor) descriptorStack.peek(1);
        }
        return result;
    }


    /**
      * Pushes the given element onto the element mapping stack.
      *
      * @param elementName the local name if the parser is namespace aware,
      * otherwise the full element name. Not null
      */
    public void pushElement(String elementName) throws Exception
    {

        elementMappingStack.push(elementName);
        // special case to ensure that root class is appropriately marked
        //TODO: is this really necessary?
        ElementDescriptor nextDescriptor = null;
        if (elementMappingStack.size() == 1 && rootClass != null)
        {
            markClassMap(rootClass);
            XMLBeanInfo rootClassInfo
                = getXMLIntrospector().introspect(rootClass);
            nextDescriptor = rootClassInfo.getElementDescriptor();
        }
        else
        {
            ElementDescriptor currentDescriptor = getCurrentDescriptor();
            if (currentDescriptor != null)
            {
                nextDescriptor = currentDescriptor.getElementDescriptor(elementName);
            }
        }
        Updater updater = null;
        Options options = null;
        if (nextDescriptor != null)
        {
            updater = nextDescriptor.getUpdater();
            options = nextDescriptor.getOptions();
        }
        updaterStack.push(updater);
        descriptorStack.push(nextDescriptor);
        pushOptions(options);
    }

    /**
      * Marks the element name stack with a class mapping.
      * Relative paths and last mapped class are calculated using these marks.
      *
      * @param mappedClazz the Class which has been mapped at the current path, not null
      */
    public void markClassMap(Class mappedClazz) throws IntrospectionException
    {
        if (mappedClazz.isArray())
        {
            mappedClazz = mappedClazz.getComponentType();
        }
        elementMappingStack.push(mappedClazz);

        XMLBeanInfo mappedClassInfo = getXMLIntrospector().introspect(mappedClazz);
        ElementDescriptor mappedElementDescriptor = mappedClassInfo.getElementDescriptor();
        descriptorStack.push(mappedElementDescriptor);

        Updater updater = mappedElementDescriptor.getUpdater();
        updaterStack.push(updater);
    }

    /**
     * Pops an action mapping from the stack
     * @return <code>MappingAction</code>, not null
     */
    public MappingAction popMappingAction()
    {
        return (MappingAction) actionMappingStack.pop();
    }

    /**
     * Pushs an action mapping onto the stack
     * @param mappingAction
     */
    public void pushMappingAction(MappingAction mappingAction)
    {
        actionMappingStack.push(mappingAction);
    }

    /**
     * Gets the current mapping action
     * @return MappingAction
     */
    public MappingAction currentMappingAction()
    {
        if (actionMappingStack.size() == 0)
        {
            return null;
        }
        return (MappingAction) actionMappingStack.peek();
    }

    public Object getBean()
    {
        return objectStack.peek();
    }

    public void setBean(Object bean)
    {
        // TODO: maybe need to deprecate the set bean method
        // and push into subclass
        // for now, do nothing
    }

    /**
     * Pops the last mapping <code>Object</code> from the
     * stack containing beans that have been mapped.
     * @return the last bean pushed onto the stack
     */
    public Object popBean()
    {
        return objectStack.pop();
    }

    /**
     * Pushs a newly mapped <code>Object</code> onto the mapped bean stack.
     * @param bean
     */
    public void pushBean(Object bean)
    {
        objectStack.push(bean);
    }

    /**
     * Gets the <code>XMLIntrospector</code> to be used to create
     * the mappings for the xml.
     * @return <code>XMLIntrospector</code>, not null
     */
    public XMLIntrospector getXMLIntrospector()
    {
        // read context is not intended to be used by multiple threads
        // so no need to worry about lazy creation
        if (xmlIntrospector == null)
        {
            xmlIntrospector = new XMLIntrospector();
        }
        return xmlIntrospector;
    }

    /**
     * Sets the <code>XMLIntrospector</code> to be used to create
     * the mappings for the xml.
     * @param xmlIntrospector <code>XMLIntrospector</code>, not null
     */
    public void setXMLIntrospector(XMLIntrospector xmlIntrospector)
    {
        this.xmlIntrospector = xmlIntrospector;
    }

    public Class getRootClass()
    {
        return rootClass;
    }

    public void setRootClass(Class rootClass)
    {
        this.rootClass = rootClass;
    }

    /**
     * Gets the <code>ElementDescriptor</code> that describes the
     * mapping for the current element.
     * @return <code>ElementDescriptor</code> or null if there is no
     * current mapping
     * @throws Exception
     */
    public ElementDescriptor getCurrentDescriptor() throws Exception
    {
        ElementDescriptor result = null;
        if (!descriptorStack.empty())
        {
            result = (ElementDescriptor) descriptorStack.peek();
        }
        return result;
    }

    /**
     * Populates the object mapped by the <code>AttributeDescriptor</code>s
     * with the values in the given <code>Attributes</code>.
     * @param attributeDescriptors <code>AttributeDescriptor</code>s, not null
     * @param attributes <code>Attributes</code>, not null
     */
    public void populateAttributes(
        AttributeDescriptor[] attributeDescriptors,
        Attributes attributes)
    {

        Log log = getLog();
        if (attributeDescriptors != null)
        {
            for (int i = 0, size = attributeDescriptors.length;
                    i < size;
                    i++)
            {
                AttributeDescriptor attributeDescriptor =
                    attributeDescriptors[i];

                // The following isn't really the right way to find the attribute
                // but it's quite robust.
                // The idea is that you try both namespace and local name first
                // and if this returns null try the qName.
                String value =
                    attributes.getValue(
                        attributeDescriptor.getURI(),
                        attributeDescriptor.getLocalName());

                if (value == null)
                {
                    value =
                        attributes.getValue(
                            attributeDescriptor.getQualifiedName());
                }

                if (log.isTraceEnabled())
                {
                    log.trace("Attr URL:" + attributeDescriptor.getURI());
                    log.trace(
                        "Attr LocalName:" + attributeDescriptor.getLocalName());
                    log.trace(value);
                }

                Updater updater = attributeDescriptor.getUpdater();
                log.trace(updater);
                if (updater != null && value != null)
                {
                    updater.update(this, value);
                }
            }
        }
    }

    /**
     * <p>Pushes an <code>Updater</code> onto the stack.</p>
     * <p>
     * <strong>Note</strong>Any action pushing an <code>Updater</code> onto
     * the stack should take responsibility for popping
     * the updater from the stack at an appropriate time.
     * </p>
     * <p>
     * <strong>Usage:</strong> this may be used by actions
     * which require a temporary object to be updated.
     * Pushing an updater onto the stack allow actions
     * downstream to transparently update the temporary proxy.
     * </p>
     * @param updater Updater, possibly null
     */
    public void pushUpdater(Updater updater)
    {
        updaterStack.push(updater);
    }

    /**
     * Pops the top <code>Updater</code> from the stack.
     * <p>
     * <strong>Note</strong>Any action pushing an <code>Updater</code> onto
     * the stack should take responsibility for popping
     * the updater from the stack at an appropriate time.
     * </p>
     * @return <code>Updater</code>, possibly null
     */
    public Updater popUpdater()
    {
        return (Updater) updaterStack.pop();
    }

    /**
     * Gets the current <code>Updater</code>.
     * This may (or may not) be the updater for the current
     * descriptor.
     * If the current descriptor is a bean child,
     * the the current updater will (most likely)
     * be the updater for the property.
     * Actions (that, for example, use proxy objects)
     * may push updaters onto the stack.
     * @return Updater, possibly null
     */
    public Updater getCurrentUpdater()
    {
        // TODO: think about whether this is right
        //       it makes some sense to look back up the
        //       stack until a non-empty updater is found.
        //       actions who need to put a stock to this
        //       behaviour can always use an ignoring implementation.
        Updater result = null;
        if (!updaterStack.empty())
        {
            result = (Updater) updaterStack.peek();
            if ( result == null && updaterStack.size() >1 )
            {
                result = (Updater) updaterStack.peek(1);
            }
        }
        return result;
    }

    /**
     * Resolves any polymorphism in the element mapping.
     * @param mapping <code>ElementMapping</code> describing the mapped element
     * @return <code>null</code> if the type cannot be resolved
     * or if the current descriptor is not polymorphic
     * @since 0.8
     */
    public Class resolvePolymorphicType(ElementMapping mapping)
    {
        Class result = null;
        Log log = getLog();
        try
        {
            ElementDescriptor currentDescriptor = getCurrentDescriptor();
            if (currentDescriptor != null)
            {
                if (currentDescriptor.isPolymorphic())
                {
                    PolymorphicReferenceResolver resolver = getXMLIntrospector().getPolymorphicReferenceResolver();
                    result = resolver.resolveType(mapping, this);
                    if (result == null)
                    {
                        // try the other polymorphic descriptors
                        ElementDescriptor parent = getParentDescriptor();
                        if (parent != null)
                        {
                            ElementDescriptor[] descriptors = parent.getElementDescriptors();
                            ElementDescriptor originalDescriptor = mapping.getDescriptor();
                            boolean resolved = false;
                            for (int i=0; i<descriptors.length; i++)
                            {
                                ElementDescriptor descriptor = descriptors[i];
                                if (descriptor.isPolymorphic())
                                {
                                    mapping.setDescriptor(descriptor);
                                    result = resolver.resolveType(mapping, this);
                                    if (result != null)
                                    {
                                        resolved = true;
                                        descriptorStack.pop();
                                        popOptions();
                                        descriptorStack.push(descriptor);
                                        pushOptions(descriptor.getOptions());
                                        Updater originalUpdater = originalDescriptor.getUpdater();
                                        Updater newUpdater = descriptor.getUpdater();
                                        substituteUpdater(originalUpdater, newUpdater);
                                        break;
                                    }
                                }
                            }
                            if (resolved)
                            {
                                log.debug("Resolved polymorphic type");
                            }
                            else
                            {
                                log.debug("Failed to resolve polymorphic type");
                                mapping.setDescriptor(originalDescriptor);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.info("Failed to resolved polymorphic type");
            log.debug(mapping, e);
        }
        return result;
    }

    /**
     * Substitutes one updater in the stack for another.
     * @param originalUpdater <code>Updater</code> possibly null
     * @param newUpdater <code>Updater</code> possibly null
     */
    private void substituteUpdater(Updater originalUpdater, Updater newUpdater)
    {
        // recursively pop elements off the stack until the first match is found
        // TODO: may need to consider using custom NILL object and match descriptors
        if (!updaterStack.isEmpty())
        {
            Updater updater = (Updater) updaterStack.pop();
            if (originalUpdater == null && updater == null)
            {
                updaterStack.push(newUpdater);
            }
            else if (originalUpdater.equals(updater))
            {
                updaterStack.push(newUpdater);
            }
            else
            {
                substituteUpdater(originalUpdater, newUpdater);
                updaterStack.push(updater);
            }
        }
    }

}
