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

import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.io.read.BeanBindAction;
import org.apache.commons.betwixt.io.read.MappingAction;
import org.apache.commons.betwixt.io.read.ReadConfiguration;
import org.apache.commons.betwixt.io.read.ReadContext;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;

/** <p>Sets <code>Betwixt</code> digestion rules for a bean class.</p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
  * @since 0.5
  */
public class BeanRuleSet implements RuleSet
{

    /** Logger */
    private static Log log = LogFactory.getLog(BeanRuleSet.class);

    /**
    * Set log to be used by <code>BeanRuleSet</code> instances
    * @param aLog the <code>Log</code> implementation for this class to log to
    */
    public static void setLog(Log aLog)
    {
        log = aLog;
    }

    /** The base path under which the rules will be attached */
    private String basePath;
    /** The element descriptor for the base  */
    private ElementDescriptor baseElementDescriptor;
    /** The (empty) base context from which all Contexts
    with beans are (directly or indirectly) obtained */
    private DigesterReadContext context;
    /** allows an attribute to be specified to overload the types of beans used */
    private String classNameAttribute = "className";

    /**
     * Base constructor.
     *
     * @param introspector the <code>XMLIntrospector</code> used to introspect
     * @param basePath specifies the (Digester-style) path under which the rules will be attached
     * @param baseElementDescriptor the <code>ElementDescriptor</code> used to create the rules
     * @param baseBeanClass the <code>Class</code> whose mapping rules will be created
     * @param matchIDs should ID/IDREFs be used to match beans?
     * @deprecated 0.5 use constructor which takes a ReadContext instead
     */
    public BeanRuleSet(
        XMLIntrospector introspector,
        String basePath,
        ElementDescriptor baseElementDescriptor,
        Class baseBeanClass,
        boolean matchIDs)
    {
        this.basePath = basePath;
        this.baseElementDescriptor = baseElementDescriptor;
        BindingConfiguration bindingConfiguration = new BindingConfiguration();
        bindingConfiguration.setMapIDs(matchIDs);
        context =
            new DigesterReadContext(
            log,
            bindingConfiguration,
            new ReadConfiguration());
        context.setRootClass(baseBeanClass);
        context.setXMLIntrospector(introspector);
    }

    /**
     * Base constructor.
     *
     * @param introspector the <code>XMLIntrospector</code> used to introspect
     * @param basePath specifies the (Digester-style) path under which the rules will be attached
     * @param baseElementDescriptor the <code>ElementDescriptor</code> used to create the rules
     * @param context the root Context that bean carrying Contexts should be obtained from,
     * not null
     * @deprecated 0.6 use the constructor which takes a ReadContext instead
     */
    public BeanRuleSet(
        XMLIntrospector introspector,
        String basePath,
        ElementDescriptor baseElementDescriptor,
        Context context)
    {

        this.basePath = basePath;
        this.baseElementDescriptor = baseElementDescriptor;
        this.context =
            new DigesterReadContext(context, new ReadConfiguration());
        this.context.setRootClass(
            baseElementDescriptor.getSingularPropertyType());
        this.context.setXMLIntrospector(introspector);
    }

    /**
     * Base constructor.
     *
     * @param introspector the <code>XMLIntrospector</code> used to introspect
     * @param basePath specifies the (Digester-style) path under which the rules will be attached
     * @param baseElementDescriptor the <code>ElementDescriptor</code> used to create the rules
     * @param baseBeanClass the <code>Class</code> whose mapping rules will be created
     * @param context the root Context that bean carrying Contexts should be obtained from,
     * not null
     * @deprecated 0.5 use the constructor which takes a ReadContext instead
     */
    public BeanRuleSet(
        XMLIntrospector introspector,
        String basePath,
        ElementDescriptor baseElementDescriptor,
        Class baseBeanClass,
        Context context)
    {
        this(
            introspector,
            basePath,
            baseElementDescriptor,
            baseBeanClass,
            new ReadContext( context, new ReadConfiguration() ));
    }

    /**
     * Base constructor.
     *
     * @param introspector the <code>XMLIntrospector</code> used to introspect
     * @param basePath specifies the (Digester-style) path under which the rules will be attached
     * @param baseElementDescriptor the <code>ElementDescriptor</code> used to create the rules
     * @param baseBeanClass the <code>Class</code> whose mapping rules will be created
     * @param baseContext the root Context that bean carrying Contexts should be obtained from,
     * not null
     */
    public BeanRuleSet(
        XMLIntrospector introspector,
        String basePath,
        ElementDescriptor baseElementDescriptor,
        Class baseBeanClass,
        ReadContext baseContext)
    {
        this.basePath = basePath;
        this.baseElementDescriptor = baseElementDescriptor;
        this.context = new DigesterReadContext(baseContext);
        this.context.setRootClass(baseBeanClass);
        this.context.setXMLIntrospector(introspector);
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
        return context.getClassNameAttribute();
    }

    /**
     * Sets the name of the attribute which can be specified in
     * the XML to override the type of a bean used at a certain
     * point in the schema.
     *
     * <p>The default value is 'className'.</p>
     *
     * @param classNameAttribute The name of the attribute used to overload the class name of a bean
     * @deprecated 0.5 set the <code>ReadContext</code> property instead
     */
    public void setClassNameAttribute(String classNameAttribute)
    {
        context.setClassNameAttribute(classNameAttribute);
    }

    //-------------------------------- Ruleset implementation

    /**
     * <p>Gets the namespace associated with this ruleset.</p>
     *
     * <p><strong>Note</strong> namespaces are not currently supported.</p>
     *
     * @return null
     */
    public String getNamespaceURI()
    {
        return null;
    }

    /**
     * Add rules for bean to given <code>Digester</code>.
     *
     * @param digester the <code>Digester</code> to which the rules for the bean will be added
     */
    public void addRuleInstances(Digester digester)
    {
        if (log.isTraceEnabled())
        {
            log.trace("Adding rules to:" + digester);
        }

        context.setDigester(digester);

        // if the classloader is not set, set to the digester classloader
        if (context.getClassLoader() == null)
        {
            context.setClassLoader(digester.getClassLoader());
        }

        // TODO: need to think about strategy for paths
        // may need to provide a default path and then allow the user to override
        digester.addRule("!" + basePath + "/*", new ActionMappingRule());
    }

    /**
     * Single rule that is used to map all elements.
     *
     * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
     */
    private final class ActionMappingRule extends Rule
    {

        /**
          * Processes the start of a new <code>Element</code>.
          * The actual processing is delegated to <code>MappingAction</code>'s.
          * @see Rule#begin(String, String, Attributes)
          */
        public void begin(String namespace, String name, Attributes attributes)
        throws Exception
        {

            if (log.isTraceEnabled())
            {
                int attributesLength = attributes.getLength();
                if (attributesLength > 0)
                {
                    log.trace("Attributes:");
                }
                for (int i = 0, size = attributesLength; i < size; i++)
                {
                    log.trace("Local:" + attributes.getLocalName(i));
                    log.trace("URI:" + attributes.getURI(i));
                    log.trace("QName:" + attributes.getQName(i));
                }
            }

            context.pushElement(name);

            MappingAction nextAction =
                nextAction(namespace, name, attributes, context);

            context.pushMappingAction(nextAction);
        }

        /**
         * Gets the next action to be executed
         * @param namespace the element's namespace, not null
         * @param name the element name, not null
         * @param attributes the element's attributes, not null
         * @param context the <code>ReadContext</code> against which the xml is being mapped.
         * @return the initialized <code>MappingAction</code>, not null
         * @throws Exception
         */
        private MappingAction nextAction(
            String namespace,
            String name,
            Attributes attributes,
            ReadContext context)
        throws Exception
        {

            MappingAction result = null;
            MappingAction lastAction = context.currentMappingAction();
            if (lastAction == null)
            {
                result =  BeanBindAction.INSTANCE;
            }
            else
            {

                result = lastAction.next(namespace, name, attributes, context);
            }
            return result.begin(namespace, name, attributes, context);
        }



        /**
        * Processes the body text for the current element.
        * This is delegated to the current <code>MappingAction</code>.
        * @see Rule#body(String, String, String)
        */
        public void body(String namespace, String name, String text)
        throws Exception
        {

            if (log.isTraceEnabled()) log.trace("[BRS] Body with text " + text);
            if (digester.getCount() > 0)
            {
                MappingAction action = context.currentMappingAction();
                action.body(text, context);
            }
            else
            {
                log.trace("[BRS] ZERO COUNT");
            }
        }

        /**
        * Process the end of this element.
        * This is delegated to the current <code>MappingAction</code>.
        */
        public void end(String namespace, String name) throws Exception
        {

            MappingAction action = context.popMappingAction();
            action.end(context);
        }

        /**
         * Tidy up.
         */
        public void finish()
        {
            //
            // Clear indexed beans so that we're ready to process next document
            //
            context.clearBeans();
        }

    }

    /**
     * Specialization of <code>ReadContext</code> when reading from <code>Digester</code>.
     * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
     * @version $Revision: 438373 $
     */
    private static class DigesterReadContext extends ReadContext
    {

        private Digester digester;

        /**
         * @param context
         * @param readConfiguration
         */
        public DigesterReadContext(
            Context context,
            ReadConfiguration readConfiguration)
        {
            super(context, readConfiguration);
            // TODO Auto-generated constructor stub
        }

        /**
         * @param bindingConfiguration
         * @param readConfiguration
         */
        public DigesterReadContext(
            BindingConfiguration bindingConfiguration,
            ReadConfiguration readConfiguration)
        {
            super(bindingConfiguration, readConfiguration);
        }

        /**
         * @param log
         * @param bindingConfiguration
         * @param readConfiguration
         */
        public DigesterReadContext(
            Log log,
            BindingConfiguration bindingConfiguration,
            ReadConfiguration readConfiguration)
        {
            super(log, bindingConfiguration, readConfiguration);
        }

        /**
         * @param log
         * @param bindingConfiguration
         * @param readConfiguration
         */
        public DigesterReadContext(ReadContext readContext)
        {
            super(readContext);
        }

        public Digester getDigester()
        {
            // TODO: replace with something better
            return digester;
        }

        public void setDigester(Digester digester)
        {
            // TODO: replace once moved to single Rule
            this.digester = digester;
        }

        /* (non-Javadoc)
         * @see org.apache.commons.betwixt.io.read.ReadContext#pushBean(java.lang.Object)
         */
        public void pushBean(Object bean)
        {
            super.pushBean(bean);
            digester.push(bean);
        }

        /* (non-Javadoc)
         * @see org.apache.commons.betwixt.io.read.ReadContext#putBean(java.lang.Object)
         */
        public Object popBean()
        {
            Object bean = super.popBean();
            // don't pop the last from the stack
            if (digester.getCount() > 0)
            {
                digester.pop();
            }
            return bean;
        }
    }

}
