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

import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.Descriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.Options;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.expression.Expression;
import org.apache.commons.betwixt.io.id.SequentialIDGenerator;
import org.apache.commons.collections.ArrayStack;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
  * <p>Abstract superclass for bean writers.
  * This class encapsulates the processing logic.
  * Subclasses provide implementations for the actual expression of the xml.</p>
  * <h5>SAX Inspired Writing API</h5>
  * <p>
  * This class is intended to be used by subclassing:
  * concrete subclasses perform the actual writing by providing
  * suitable implementations for the following methods inspired
  * by <a href='http://www.saxproject.org'>SAX</a>:
  * </p>
  * <ul>
  *     <li> {@link #start} - called when processing begins</li>
  *     <li> {@link #startElement(WriteContext, String, String, String, Attributes)}
  *     - called when the start of an element
  *     should be written</li>
  *     <li> {@link #bodyText(WriteContext, String)}
  *     - called when the start of an element
  *     should be written</li>
  *     <li> {@link #endElement(WriteContext, String, String, String)}
  *     - called when the end of an element
  *     should be written</li>
  *     <li> {@link #end} - called when processing has been completed</li>
  * </ul>
  * <p>
  * <strong>Note</strong> that this class contains many deprecated
  * versions of the writing API. These will be removed soon so care
  * should be taken to use the latest version.
  * </p>
  * <p>
  * <strong>Note</strong> that this class is designed to be used
  * in a single threaded environment. When used in multi-threaded
  * environments, use of a common <code>XMLIntrospector</code>
  * and pooled writer instances should be considered.
  * </p>
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  */
public abstract class AbstractBeanWriter
{

    /** Introspector used */
    private XMLIntrospector introspector = new XMLIntrospector();

    /** Log used for logging (Doh!) */
    private Log log = LogFactory.getLog( AbstractBeanWriter.class );
    /** Stack containing beans - used to detect cycles */
    private ArrayStack beanStack = new ArrayStack();
    /** Used to generate ID attribute values*/
    private IDGenerator idGenerator = new SequentialIDGenerator();
    /** Should empty elements be written out? */
    private boolean writeEmptyElements = true;
    /** Dynamic binding configuration settings */
    private BindingConfiguration bindingConfiguration = new BindingConfiguration();
    /** <code>WriteContext</code> implementation reused curing writing */
    private WriteContextImpl writeContext = new WriteContextImpl();
    /** Collection of namespaces which have already been declared */
    private Collection namespacesDeclared = new ArrayList();

    /**
     * Marks the start of the bean writing.
     * By default doesn't do anything, but can be used
     * to do extra start processing
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     */
    public void start() throws IOException, SAXException
    {
    }

    /**
     * Marks the start of the bean writing.
     * By default doesn't do anything, but can be used
     * to do extra end processing
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     */

    public void end() throws IOException, SAXException
    {
    }

    /**
     * <p> Writes the given bean to the current stream using the XML introspector.</p>
     *
     * <p> This writes an xml fragment representing the bean to the current stream.</p>
     *
     * <p>This method will throw a <code>CyclicReferenceException</code> when a cycle
     * is encountered in the graph <strong>only</strong> if the <code>getMapIDs()</code>
     * setting of the </code>BindingConfiguration</code> is false.</p>
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     *
     * @param bean write out representation of this bean
     */
    public void write(Object bean) throws
        IOException,
        SAXException,
        IntrospectionException
    {
        if (log.isDebugEnabled())
        {
            log.debug( "Writing bean graph..." );
            log.debug( bean );
        }
        start();
        writeBean( null, null, null, bean, makeContext( bean ) );
        end();
        if (log.isDebugEnabled())
        {
            log.debug( "Finished writing bean graph." );
        }
    }

    /**
     * <p>Writes the given bean to the current stream
     * using the given <code>qualifiedName</code>.</p>
     *
     * <p>This method will throw a <code>CyclicReferenceException</code> when a cycle
     * is encountered in the graph <strong>only</strong> if the <code>getMapIDs()</code>
     * setting of the <code>BindingConfiguration</code> is false.</p>
     *
     * @param qualifiedName the string naming root element
     * @param bean the <code>Object</code> to write out as xml
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     */
    public void write(
        String qualifiedName,
        Object bean)
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        start();
        writeBean( "", qualifiedName, qualifiedName, bean, makeContext( bean ) );
        end();
    }

    /**
     * <p>Writes the bean using the mapping specified in the <code>InputSource</code>.
     * </p><p>
     * <strong>Note:</strong> that the custom mapping will <em>not</em>
     * be registered for later use. Please use {@link XMLIntrospector#register}
     * to register the custom mapping for the class and then call
     * {@link #write(Object)}.
     * </p>
     * @see #write(Object) since the standard notes also apply
     * @since 0.7
     * @param bean <code>Object</code> to be written as xml, not null
     * @param source <code>InputSource/code> containing an xml document
     * specifying the mapping to be used (in the usual way), not null
     * @throws IOException
     * @throws SAXException
     * @throws IntrospectionException
     */
    public void write(Object bean, InputSource source)
    throws IOException, SAXException, IntrospectionException
    {
        writeBean(
            null,
            null,
            null,
            bean,
            makeContext( bean ),
            getXMLIntrospector().introspect(bean.getClass(), source));
    }

    /**
     * <p>Writes the given bean to the current stream
     * using the given <code>qualifiedName</code>.</p>
     *
     * <p>This method will throw a <code>CyclicReferenceException</code> when a cycle
     * is encountered in the graph <strong>only</strong> if the <code>getMapIDs()</code>
     * setting of the <code>BindingConfiguration</code> is false.</p>
     *
     * @param namespaceUri the namespace uri
     * @param localName the local name
     * @param qualifiedName the string naming root element
     * @param introspectedBindType the <code>Class</code> of the bean
     * as resolved at introspection time, or null if the type has not been resolved
     * @param bean the <code>Object</code> to write out as xml
     * @param context not null
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     */
    private void writeBean (
        String namespaceUri,
        String localName,
        String qualifiedName,
        Object bean,
        Context context)
    throws
        IOException,
        SAXException,
        IntrospectionException
    {

        if ( log.isTraceEnabled() )
        {
            log.trace( "Writing bean graph (qualified name '" + qualifiedName + "'" );
        }

        // introspect to obtain bean info
        XMLBeanInfo beanInfo = introspector.introspect( bean );
        writeBean(namespaceUri, localName, qualifiedName, bean, context, beanInfo);

        log.trace( "Finished writing bean graph." );
    }


    private void writeBean (
        String namespaceUri,
        String localName,
        String qualifiedName,
        Object bean,
        ElementDescriptor parentDescriptor,
        Context context)
    throws
        IOException,
        SAXException,
        IntrospectionException
    {

        if ( log.isTraceEnabled() )
        {
            log.trace( "Writing bean graph (qualified name '" + qualifiedName + "'" );
        }

        // introspect to obtain bean info
        XMLBeanInfo beanInfo = findXMLBeanInfo(bean, parentDescriptor);
        writeBean(namespaceUri, localName, qualifiedName, bean, context, beanInfo);

        log.trace( "Finished writing bean graph." );
    }

    /**
     * Finds the appropriate bean info for the given (hollow) element.
     * @param bean
     * @param parentDescriptor <code>ElementDescriptor</code>, not null
     * @return <code>XMLBeanInfo</code>, not null
     * @throws IntrospectionException
     */
    private XMLBeanInfo findXMLBeanInfo(Object bean, ElementDescriptor parentDescriptor) throws IntrospectionException
    {
        XMLBeanInfo beanInfo = null;
        Class introspectedBindType = parentDescriptor.getSingularPropertyType();
        if ( introspectedBindType == null )
        {
            introspectedBindType = parentDescriptor.getPropertyType();
        }
        if ( parentDescriptor.isUseBindTimeTypeForMapping() || introspectedBindType == null )
        {
            beanInfo = introspector.introspect( bean );
        }
        else
        {
            beanInfo = introspector.introspect( introspectedBindType );
        }
        return beanInfo;
    }

    /**
     * <p>Writes the given bean to the current stream
     * using the given mapping.</p>
     *
     * <p>This method will throw a <code>CyclicReferenceException</code> when a cycle
     * is encountered in the graph <strong>only</strong> if the <code>getMapIDs()</code>
     * setting of the <code>BindingConfiguration</code> is false.</p>
     *
     * @param namespaceUri the namespace uri, or null to use the automatic binding
     * @param localName the local name  or null to use the automatic binding
     * @param qualifiedName the <code>String</code> naming the root element
     *  or null to use the automatic binding
     * @param bean <code>Object</code> to be written, not null
     * @param context <code>Context</code>, not null
     * @param beanInfo <code>XMLBeanInfo</code>, not null
     * @throws IOException
     * @throws SAXException
     * @throws IntrospectionException
     */
    private void writeBean(
        String namespaceUri,
        String localName,
        String qualifiedName,
        Object bean,
        Context context,
        XMLBeanInfo beanInfo)
    throws IOException, SAXException, IntrospectionException
    {
        if ( beanInfo != null )
        {
            ElementDescriptor elementDescriptor = beanInfo.getElementDescriptor();
            if ( elementDescriptor != null )
            {

                // Construct the options
                Options combinedOptions = new Options();

                // Add options defined by the current bean's element descriptor
                combinedOptions.addOptions(elementDescriptor.getOptions());

                // The parent descriptor may have defined options
                // for the current bean.  These options take precedence
                // over the options of the current class descriptor
                if( context.getOptions() != null)
                {
                    combinedOptions.addOptions(context.getOptions());
                }
                context = context.newContext( bean );
                context.pushOptions(combinedOptions);

                if ( qualifiedName == null )
                {
                    qualifiedName = elementDescriptor.getQualifiedName();
                }
                if ( namespaceUri == null )
                {
                    namespaceUri = elementDescriptor.getURI();
                }
                if ( localName == null )
                {
                    localName = elementDescriptor.getLocalName();
                }

                String ref = null;
                String id = null;

                // simple type should not have IDs
                if ( elementDescriptor.isSimple() )
                {
                    // write without an id
                    writeElement(
                        namespaceUri,
                        localName,
                        qualifiedName,
                        elementDescriptor,
                        context );

                }
                else
                {
                    pushBean ( context.getBean() );
                    if ( getBindingConfiguration().getMapIDs() )
                    {
                        ref = getBindingConfiguration().getIdMappingStrategy().getReferenceFor(context, context.getBean());
                    }
                    if ( ref == null )
                    {
                        // this is the first time that this bean has be written
                        AttributeDescriptor idAttribute = beanInfo.getIDAttribute();
                        if (idAttribute == null)
                        {
                            // use a generated id
                            id = idGenerator.nextId();
                            getBindingConfiguration().getIdMappingStrategy().setReference(context, bean, id);

                            if ( getBindingConfiguration().getMapIDs() )
                            {
                                // write element with id
                                writeElement(
                                    namespaceUri,
                                    localName,
                                    qualifiedName,
                                    elementDescriptor,
                                    context ,
                                    beanInfo.getIDAttributeName(),
                                    id);


                            }
                            else
                            {
                                // write element without ID
                                writeElement(
                                    namespaceUri,
                                    localName,
                                    qualifiedName,
                                    elementDescriptor,
                                    context );
                            }

                        }
                        else
                        {
                            // use id from bean property
                            // it's up to the user to ensure uniqueness
                            Expression idExpression = idAttribute.getTextExpression();
                            if(idExpression == null)
                            {
                                throw new IntrospectionException(
                                    "The specified id property wasn't found in the bean ("
                                    + idAttribute + ").");
                            }
                            Object exp = idExpression.evaluate( context );
                            if (exp == null)
                            {
                                // we'll use a random id
                                log.debug("Using random id");
                                id = idGenerator.nextId();

                            }
                            else
                            {
                                // convert to string
                                id = exp.toString();
                            }
                            getBindingConfiguration().getIdMappingStrategy().setReference(context, bean, id);

                            // the ID attribute should be written automatically
                            writeElement(
                                namespaceUri,
                                localName,
                                qualifiedName,
                                elementDescriptor,
                                context );
                        }
                    }
                    else
                    {

                        if ( !ignoreElement( elementDescriptor, namespaceUri, localName, qualifiedName, context ))
                        {
                            // we've already written this bean so write an IDREF
                            writeIDREFElement(
                                elementDescriptor,
                                namespaceUri,
                                localName,
                                qualifiedName,
                                beanInfo.getIDREFAttributeName(),
                                ref);
                        }
                    }
                    popBean();
                }

                context.popOptions();
            }
        }
    }

    /**
      * Get <code>IDGenerator</code> implementation used to
      * generate <code>ID</code> attribute values .
      *
      * @return implementation used for <code>ID</code> attribute generation
      */
    public IDGenerator getIdGenerator()
    {
        return idGenerator;
    }

    /**
      * Set <code>IDGenerator</code> implementation
      * used to generate <code>ID</code> attribute values.
      * This property can be used to customize the algorithm used for generation.
      *
      * @param idGenerator use this implementation for <code>ID</code> attribute generation
      */
    public void setIdGenerator(IDGenerator idGenerator)
    {
        this.idGenerator = idGenerator;
    }



    /**
     * Gets the dynamic configuration setting to be used for bean reading.
     * @return the BindingConfiguration settings, not null
     * @since 0.5
     */
    public BindingConfiguration getBindingConfiguration()
    {
        return bindingConfiguration;
    }

    /**
     * Sets the dynamic configuration setting to be used for bean reading.
     * @param bindingConfiguration the BindingConfiguration settings, not null
     * @since 0.5
     */
    public void setBindingConfiguration(BindingConfiguration bindingConfiguration)
    {
        this.bindingConfiguration = bindingConfiguration;
    }

    /**
     * <p>Should generated <code>ID</code> attribute values be added to the elements?</p>
     *
     * <p>If IDs are not being written then if a cycle is encountered in the bean graph,
     * then a {@link CyclicReferenceException} will be thrown by the write method.</p>
     *
     * @return true if <code>ID</code> and <code>IDREF</code> attributes are to be written
     * @deprecated 0.5 use {@link BindingConfiguration#getMapIDs}
     */
    public boolean getWriteIDs()
    {
        return getBindingConfiguration().getMapIDs();
    }

    /**
     * Set whether generated <code>ID</code> attribute values should be added to the elements
     * If this property is set to false, then <code>CyclicReferenceException</code>
     * will be thrown whenever a cyclic occurs in the bean graph.
     *
     * @param writeIDs true if <code>ID</code>'s and <code>IDREF</code>'s should be written
     * @deprecated 0.5 use {@link BindingConfiguration#setMapIDs}
     */
    public void setWriteIDs(boolean writeIDs)
    {
        getBindingConfiguration().setMapIDs( writeIDs );
    }

    /**
     * <p>Gets whether empty elements should be written into the output.</p>
     *
     * <p>An empty element is one that has no attributes, no child elements
     * and no body text.
     * For example, <code>&lt;element/&gt;</code> is an empty element but
     * <code>&lt;element attr='value'/&gt;</code> is not.</p>
     *
     * @return true if empty elements will be written into the output
     * @since 0.5
     */
    public boolean getWriteEmptyElements()
    {
        return writeEmptyElements;
    }

    /**
     * <p>Sets whether empty elements should be written into the output.</p>
     *
     * <p>An empty element is one that has no attributes, no child elements
     * and no body text.
     * For example, <code>&lt;element/&gt;</code> is an empty element but
     * <code>&lt;element attr='value'/&gt;</code> is not.
     *
     * @param writeEmptyElements true if empty elements should be written into the output
     * @since 0.5
     */
    public void setWriteEmptyElements(boolean writeEmptyElements)
    {
        this.writeEmptyElements = writeEmptyElements;
    }

    /**
     * <p>Gets the introspector used.</p>
     *
     * <p>The {@link XMLBeanInfo} used to map each bean is
     * created by the <code>XMLIntrospector</code>.
     * One way in which the mapping can be customized is
     * by altering the <code>XMLIntrospector</code>. </p>
     *
     * @return the <code>XMLIntrospector</code> used for introspection
     */
    public XMLIntrospector getXMLIntrospector()
    {
        return introspector;
    }


    /**
     * <p>Sets the introspector to be used.</p>
     *
     * <p>The {@link XMLBeanInfo} used to map each bean is
     * created by the <code>XMLIntrospector</code>.
     * One way in which the mapping can be customized is by
     * altering the <code>XMLIntrospector</code>. </p>
     *
     * @param introspector use this introspector
     */
    public void  setXMLIntrospector(XMLIntrospector introspector)
    {
        this.introspector = introspector;
    }

    /**
     * <p>Gets the current logging implementation.</p>
     *
     * @return the <code>Log</code> implementation which this class logs to
     */
    public final Log getAbstractBeanWriterLog()
    {
        return log;
    }

    /**
     * <p> Set the current logging implementation. </p>
     *
     * @param log <code>Log</code> implementation to use
     */
    public final void setAbstractBeanWriterLog(Log log)
    {
        this.log = log;
    }

    // SAX-style methods
    //-------------------------------------------------------------------------

    /**
     * Writes the start tag for an element.
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qName the element's qualified name
     * @param attr the element's attributes
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @since 0.5
     */
    protected void startElement(
        WriteContext context,
        String uri,
        String localName,
        String qName,
        Attributes attr)
    throws
        IOException,
        SAXException
    {
        // for backwards compatbility call older methods
        startElement(uri, localName, qName, attr);
    }

    /**
     * Writes the end tag for an element
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qName the element's qualified name
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @since 0.5
     */
    protected void endElement(
        WriteContext context,
        String uri,
        String localName,
        String qName)
    throws
        IOException,
        SAXException
    {
        // for backwards compatibility call older interface
        endElement(uri, localName, qName);
    }

    /**
     * Writes body text
     *
     * @param text the body text to be written
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @since 0.5
     */
    protected void bodyText(WriteContext context, String text)
    throws IOException, SAXException
    {
        // for backwards compatibility call older interface
        bodyText(text);
    }

    // Older SAX-style methods
    //-------------------------------------------------------------------------

    /**
     * Writes the start tag for an element.
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qName the element's qualified name
     * @param attr the element's attributes
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 use {@link #startElement(WriteContext, String, String, String, Attributes)}
     */
    protected void startElement(
        String uri,
        String localName,
        String qName,
        Attributes attr)
    throws
        IOException,
        SAXException {}

    /**
     * Writes the end tag for an element
     *
     * @param uri the element's namespace uri
     * @param localName the element's local name
     * @param qName the element's qualified name
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 use {@link #endElement(WriteContext, String, String, String)}
     */
    protected void endElement(
        String uri,
        String localName,
        String qName)
    throws
        IOException,
        SAXException {}

    /**
     * Writes body text
     *
     * @param text the body text to be written
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 use {@link #bodyText(WriteContext, String)}
     */
    protected void bodyText(String text) throws IOException, SAXException {}

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Writes the given element
     *
     * @param namespaceUri the namespace uri
     * @param localName the local name
     * @param qualifiedName qualified name to use for the element
     * @param elementDescriptor the <code>ElementDescriptor</code> describing the element
     * @param context the <code>Context</code> to use to evaluate the bean expressions
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     */
    private void writeElement(
        String namespaceUri,
        String localName,
        String qualifiedName,
        ElementDescriptor elementDescriptor,
        Context context )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        if( log.isTraceEnabled() )
        {
            log.trace( "Writing: " + qualifiedName + " element: " + elementDescriptor );
        }

        if ( !ignoreElement( elementDescriptor, namespaceUri, localName, qualifiedName, context ))
        {
            if ( log.isTraceEnabled() )
            {
                log.trace( "Element " + elementDescriptor + " is empty." );
            }

            Attributes attributes = addNamespaceDeclarations(
                                        new ElementAttributes( elementDescriptor, context ), namespaceUri);
            writeContext.setCurrentDescriptor(elementDescriptor);
            startElement(
                writeContext,
                namespaceUri,
                localName,
                qualifiedName,
                attributes);

            writeElementContent( elementDescriptor, context ) ;
            writeContext.setCurrentDescriptor(elementDescriptor);
            endElement( writeContext, namespaceUri, localName, qualifiedName );
        }
    }

    /**
     * Adds namespace declarations (if any are needed) to the given attributes.
     * @param attributes Attributes, not null
     * @param elementNamespaceUri the URI for the enclosing element, possibly null
     * @return Attributes, not null
     */
    private Attributes addNamespaceDeclarations(Attributes attributes, String elementNamespaceUri)
    {
        Attributes result = attributes;
        AttributesImpl withDeclarations = null;
        for (int i=-1, size=attributes.getLength(); i<size ; i++)
        {
            String uri = null;
            if (i == -1)
            {
                uri = elementNamespaceUri;
            }
            else
            {
                uri = attributes.getURI(i);
            }
            if (uri != null && !"".equals(uri) && !namespacesDeclared.contains(uri))
            {
                if (withDeclarations == null)
                {
                    withDeclarations = new AttributesImpl(attributes);
                }
                withDeclarations.addAttribute("", "", "xmlns:"
                                              + getXMLIntrospector().getConfiguration().getPrefixMapper().getPrefix(uri), "NOTATION", uri);
                namespacesDeclared.add(uri);
            }
        }

        if (withDeclarations != null)
        {
            result = withDeclarations;
        }
        return result;
    }


    /**
     * Writes the given element adding an ID attribute
     *
     * @param namespaceUri the namespace uri
     * @param localName the local name
     * @param qualifiedName the qualified name
     * @param elementDescriptor the ElementDescriptor describing this element
     * @param context the context being evaliated against
     * @param idAttribute the qualified name of the <code>ID</code> attribute
     * @param idValue the value for the <code>ID</code> attribute
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     */
    private void writeElement(
        String namespaceUri,
        String localName,
        String qualifiedName,
        ElementDescriptor elementDescriptor,
        Context context,
        String idAttribute,
        String idValue )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {

        if ( !ignoreElement( elementDescriptor, namespaceUri, localName, qualifiedName, context ) )
        {
            writeContext.setCurrentDescriptor(elementDescriptor);
            Attributes attributes = new IDElementAttributes(
                elementDescriptor,
                context,
                idAttribute,
                idValue );
            startElement(
                writeContext,
                namespaceUri,
                localName,
                qualifiedName,
                addNamespaceDeclarations(attributes, namespaceUri));

            writeElementContent( elementDescriptor, context ) ;
            writeContext.setCurrentDescriptor(elementDescriptor);
            endElement( writeContext, namespaceUri, localName, qualifiedName );
        }
        else if ( log.isTraceEnabled() )
        {
            log.trace( "Element " + qualifiedName + " is empty." );
        }
    }


    /**
     * Write attributes, child elements and element end
     *
     * @param uri the element namespace uri
     * @param localName the local name of the element
     * @param qualifiedName the qualified name of the element
     * @param elementDescriptor the descriptor for this element
     * @param context evaluate against this context
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     */
    private void writeRestOfElement(
        String uri,
        String localName,
        String qualifiedName,
        ElementDescriptor elementDescriptor,
        Context context )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {

        writeElementContent( elementDescriptor, context );
    }

    /**
     * Writes an element with a <code>IDREF</code> attribute
     *
     * @param uri the namespace uri
     * @param localName the local name
     * @param qualifiedName of the element with <code>IDREF</code> attribute
     * @param idrefAttributeName the qualified name of the <code>IDREF</code> attribute
     * @param idrefAttributeValue the value for the <code>IDREF</code> attribute
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     */
    private void writeIDREFElement(
        ElementDescriptor elementDescriptor,
        String uri,
        String localName,
        String qualifiedName,
        String idrefAttributeName,
        String idrefAttributeValue )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {



        // write IDREF element
        AttributesImpl attributes = new AttributesImpl();
        // XXX for the moment, assign IDREF to default namespace
        attributes.addAttribute(
            "",
            idrefAttributeName,
            idrefAttributeName,
            "IDREF",
            idrefAttributeValue);
        writeContext.setCurrentDescriptor(elementDescriptor);
        startElement( writeContext, uri, localName, qualifiedName, addNamespaceDeclarations(attributes, uri));
        endElement( writeContext, uri, localName, qualifiedName );
    }

    /**
     * Writes the element content.
     *
     * @param elementDescriptor the <code>ElementDescriptor</code> to write as xml
     * @param context the <code>Context</code> to use to evaluate the bean expressions
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     */
    private void writeElementContent(
        ElementDescriptor elementDescriptor,
        Context context )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        writeContext.setCurrentDescriptor( elementDescriptor );
        Descriptor[] childDescriptors = elementDescriptor.getContentDescriptors();
        if ( childDescriptors != null && childDescriptors.length > 0 )
        {
            // process child elements
            for ( int i = 0, size = childDescriptors.length; i < size; i++ )
            {
                if (childDescriptors[i] instanceof ElementDescriptor)
                {
                    // Element content
                    ElementDescriptor childDescriptor = (ElementDescriptor) childDescriptors[i];
                    Context childContext = context;
                    childContext.pushOptions(childDescriptor.getOptions());
                    Expression childExpression = childDescriptor.getContextExpression();
                    if ( childExpression != null )
                    {
                        Object childBean = childExpression.evaluate( context );
                        if ( childBean != null )
                        {
                            String qualifiedName = childDescriptor.getQualifiedName();
                            String namespaceUri = childDescriptor.getURI();
                            String localName = childDescriptor.getLocalName();
                            // XXXX: should we handle nulls better
                            if ( childBean instanceof Iterator )
                            {
                                for ( Iterator iter = (Iterator) childBean; iter.hasNext(); )
                                {
                                    Object object = iter.next();
                                    if (object == null)
                                    {
                                        continue;
                                    }
                                    writeBean(
                                        namespaceUri,
                                        localName,
                                        qualifiedName,
                                        object,
                                        childDescriptor,
                                        context );
                                }
                            }
                            else
                            {
                                writeBean(
                                    namespaceUri,
                                    localName,
                                    qualifiedName,
                                    childBean,
                                    childDescriptor,
                                    context );
                            }
                        }
                    }
                    else
                    {
                        writeElement(
                            childDescriptor.getURI(),
                            childDescriptor.getLocalName(),
                            childDescriptor.getQualifiedName(),
                            childDescriptor,
                            childContext );
                    }
                    childContext.popOptions();
                }
                else
                {
                    // Mixed text content
                    // evaluate the body text
                    Expression expression = childDescriptors[i].getTextExpression();
                    if ( expression != null )
                    {
                        Object value = expression.evaluate( context );
                        String text = convertToString(
                                          value,
                                          childDescriptors[i],
                                          context );
                        if ( text != null && text.length() > 0 )
                        {
                            ;
                            bodyText( writeContext, text );
                        }
                    }
                }
            }
        }
        else
        {
            // evaluate the body text
            Expression expression = elementDescriptor.getTextExpression();
            if ( expression != null )
            {
                Object value = expression.evaluate( context );
                String text = convertToString( value, elementDescriptor, context );
                if ( text != null && text.length() > 0 )
                {
                    bodyText( writeContext, text );
                }
            }
        }
    }

    /**
     * Pushes the bean onto the ancestry stack.
     * If IDs are not being written, then check for cyclic references.
     *
     * @param bean push this bean onto the ancester stack
     */
    protected void pushBean( Object bean )
    {
        // check that we don't have a cyclic reference when we're not writing IDs
        if ( !getBindingConfiguration().getMapIDs() )
        {
            Iterator it = beanStack.iterator();
            while ( it.hasNext() )
            {
                Object next = it.next();
                // use absolute equality rather than equals
                // we're only really bothered if objects are actually the same
                if ( bean == next )
                {
                    final String message = "Cyclic reference at bean: " + bean;
                    log.error(message);
                    StringBuffer buffer = new StringBuffer(message);
                    buffer.append(" Stack: ");
                    Iterator errorStack = beanStack.iterator();
                    while ( errorStack.hasNext() )
                    {
                        Object errorObj = errorStack.next();
                        if(errorObj != null)
                        {
                            buffer.append(errorObj.getClass().getName());
                            buffer.append(": ");
                        }
                        buffer.append(errorObj);
                        buffer.append(";");
                    }
                    final String debugMessage = buffer.toString();
                    log.info( debugMessage );
                    throw new CyclicReferenceException( debugMessage );
                }
            }
        }
        if (log.isTraceEnabled())
        {
            log.trace( "Pushing onto object stack: " + bean );
        }
        beanStack.push( bean );
    }

    /**
     * Pops the top bean off from the ancestry stack
     *
     * @return the last object pushed onto the ancester stack
     */
    protected Object popBean()
    {
        Object bean = beanStack.pop();
        if (log.isTraceEnabled())
        {
            log.trace( "Popped from object stack: " + bean );
        }
        return bean;
    }

    /**
     * Should this element (and children) be written out?
     *
     * @param descriptor the <code>ElementDescriptor</code> to evaluate
     * @param context the <code>Context</code> against which the element will be evaluated
     * @return true if this element should be written out
     * @throws IntrospectionException
     */
    private boolean ignoreElement( ElementDescriptor descriptor, String namespaceUri, String localName, String qualifiedName, Context context ) throws IntrospectionException
    {
        if (getBindingConfiguration().getValueSuppressionStrategy().suppressElement(descriptor, namespaceUri, localName, qualifiedName, context.getBean()))
        {
            return true;
        }

        if ( ! getWriteEmptyElements() )
        {
            return isEmptyElement( descriptor, context );
        }
        return false;
    }

    /**
     * <p>Will evaluating this element against this context result in an empty element?</p>
     *
     * <p>An empty element is one that has no attributes, no child elements
     * and no body text.
     * For example, <code>&lt;element/&gt;</code> is an empty element but
     * <code>&lt;element attr='value'/&gt;</code> is not.</p>
     *
     * @param descriptor the <code>ElementDescriptor</code> to evaluate
     * @param context the <code>Context</code> against which the element will be evaluated
     * @return true if this element is empty on evaluation
     * @throws IntrospectionException
     */
    private boolean isEmptyElement( ElementDescriptor descriptor, Context context ) throws IntrospectionException
    {
        //TODO: this design isn't too good
        // to would be much better to render just once
        if ( log.isTraceEnabled() )
        {
            log.trace( "Is " + descriptor + " empty?" );
        }

        // an element which has attributes is not empty
        if ( descriptor.hasAttributes() )
        {
            log.trace( "Element has attributes." );
            return false;
        }

        // an element is not empty if it has a non-empty body
        Expression expression = descriptor.getTextExpression();
        if ( expression != null )
        {
            Object value = expression.evaluate( context );
            String text = convertToString( value, descriptor, context );
            if ( text != null && text.length() > 0 )
            {
                log.trace( "Element has body text which isn't empty." );
                return false;
            }
        }

        // always write out loops - even when they have no elements
        if ( descriptor.isCollective() )
        {
            log.trace("Loop type so not empty.");
            return false;
        }

        // now test child elements
        // an element is empty if it has no non-empty child elements
        if ( descriptor.hasChildren() )
        {
            for ( int i=0, size=descriptor.getElementDescriptors().length; i<size; i++ )
            {
                if ( ! isEmptyElement( descriptor.getElementDescriptors()[i], context ) )
                {
                    log.trace( "Element has child which isn't empty." );
                    return false;
                }
            }
        }

        if ( descriptor.isHollow() )
        {
            Expression contentExpression = descriptor.getContextExpression();
            if (contentExpression != null)
            {
                Object childBean = contentExpression.evaluate(context);
                if (childBean != null)
                {
                    XMLBeanInfo xmlBeanInfo = findXMLBeanInfo(childBean, descriptor);
                    Object currentBean = context.getBean();
                    context.setBean(childBean);
                    boolean result = isEmptyElement(xmlBeanInfo.getElementDescriptor(), context);
                    context.setBean(currentBean);
                    return result;
                }
            }
        }

        log.trace( "Element is empty." );
        return true;
    }


    /**
     * Attributes backed by attribute descriptors.
     * ID/IDREFs not set.
     */
    private class ElementAttributes implements Attributes
    {
        /** Attribute descriptors backing the <code>Attributes</code> */
        private AttributeDescriptor[] attributes;
        /** Context to be evaluated when finding values */
        private Context context;
        /** Cached attribute values */
        private String[] values;
        /** The number of unsuppressed attributes */
        private int length;


        /**
         * Construct attributes for element and context.
         *
         * @param descriptor the <code>ElementDescriptor</code> describing the element
         * @param context evaluate against this context
         */
        ElementAttributes( ElementDescriptor descriptor, Context context )
        {
            this.context = context;
            init(descriptor.getAttributeDescriptors());
        }

        private void init(AttributeDescriptor[] baseAttributes)
        {
            attributes = new AttributeDescriptor[baseAttributes.length];
            values = new String[baseAttributes.length];
            int index = 0;
            for (int i=0, size=baseAttributes.length; i<size; i++)
            {
                AttributeDescriptor baseAttribute = baseAttributes[i];
                String attributeValue = valueAttribute(baseAttribute);
                if (attributeValue != null
                        && !context.getValueSuppressionStrategy()
                        .suppressAttribute(baseAttribute, attributeValue))
                {
                    values[index] = attributeValue;
                    attributes[index] = baseAttribute;
                    index++;
                }
            }
            length = index;
        }

        private String valueAttribute(AttributeDescriptor attribute)
        {
            Expression expression = attribute.getTextExpression();
            if ( expression != null )
            {
                Object value = expression.evaluate( context );
                return convertToString( value, attribute, context );
            }

            return "";
        }

        /**
         * Gets the index of an attribute by qualified name.
         *
         * @param qName the qualified name of the attribute
         * @return the index of the attribute - or -1 if there is no matching attribute
         */
        public int getIndex( String qName )
        {
            for ( int i=0; i<attributes.length; i++ )
            {
                if (attributes[i].getQualifiedName() != null
                        && attributes[i].getQualifiedName().equals( qName ))
                {
                    return i;
                }
            }
            return -1;
        }

        /**
         * Gets the index of an attribute by namespace name.
         *
         * @param uri the namespace uri of the attribute
         * @param localName the local name of the attribute
         * @return the index of the attribute - or -1 if there is no matching attribute
         */
        public int getIndex( String uri, String localName )
        {
            for ( int i=0; i<attributes.length; i++ )
            {
                if (
                    attributes[i].getURI() != null
                    && attributes[i].getURI().equals(uri)
                    && attributes[i].getLocalName() != null
                    && attributes[i].getURI().equals(localName))
                {
                    return i;
                }
            }

            return -1;
        }

        /**
         * Gets the number of attributes in the list.
         *
         * @return the number of attributes in this list
         */
        public int getLength()
        {
            return length;
        }

        /**
         * Gets the local name by index.
         *
         * @param index the attribute index (zero based)
         * @return the attribute local name - or null if the index is out of range
         */
        public String getLocalName( int index )
        {
            if ( indexInRange( index ) )
            {
                return attributes[index].getLocalName();
            }

            return null;
        }

        /**
         * Gets the qualified name by index.
         *
         * @param index the attribute index (zero based)
         * @return the qualified name of the element - or null if the index is our of range
         */
        public String getQName( int index )
        {
            if ( indexInRange( index ) )
            {
                return attributes[index].getQualifiedName();
            }

            return null;
        }

        /**
         * Gets the attribute SAX type by namespace name.
         *
         * @param index the attribute index (zero based)
         * @return the attribute type (as a string) or null if the index is out of range
         */
        public String getType( int index )
        {
            if ( indexInRange( index ) )
            {
                return "CDATA";
            }
            return null;
        }

        /**
         * Gets the attribute SAX type by qualified name.
         *
         * @param qName the qualified name of the attribute
         * @return the attribute type (as a string) or null if the attribute is not in the list
         */
        public String getType( String qName )
        {
            return getType( getIndex( qName ) );
        }

        /**
         * Gets the attribute SAX type by namespace name.
         *
         * @param uri the namespace uri of the attribute
         * @param localName the local name of the attribute
         * @return the attribute type (as a string) or null if the attribute is not in the list
         */
        public String getType( String uri, String localName )
        {
            return getType( getIndex( uri, localName ));
        }

        /**
         * Gets the namespace URI for attribute at the given index.
         *
         * @param index the attribute index (zero-based)
         * @return the namespace URI (empty string if none is available)
         * or null if the index is out of range
         */
        public String getURI( int index )
        {
            if ( indexInRange( index ) )
            {
                return attributes[index].getURI();
            }
            return null;
        }

        /**
         * Gets the value for the attribute at given index.
         *
         * @param index the attribute index (zero based)
         * @return the attribute value or null if the index is out of range
         * @todo add value caching
         */
        public String getValue( int index )
        {
            if ( indexInRange( index ) )
            {
                return values[index];
            }
            return null;
        }

        /**
         * Gets the value for the attribute by qualified name.
         *
         * @param qName the qualified name
         * @return the attribute value or null if there are no attributes
         * with the given qualified name
         * @todo add value caching
         */
        public String getValue( String qName )
        {
            return getValue( getIndex( qName ) );
        }

        /**
         * Gets the value for the attribute by namespace name.
         *
         * @param uri the namespace URI of the attribute
         * @param localName the local name of the attribute
         * @return the attribute value or null if there are not attributes
         * with the given namespace and local name
         * @todo add value caching
         */
        public String getValue( String uri, String localName )
        {
            return getValue( getIndex( uri, localName ) );
        }

        /**
         * Is the given index within the range of the attribute list
         *
         * @param index the index whose range will be checked
         * @return true if the index with within the range of the attribute list
         */
        private boolean indexInRange( int index )
        {
            return ( index >= 0 && index < getLength() );
        }
    }

    /**
     * Attributes with generate ID/IDREF attributes
     * //TODO: refactor the ID/REF generation so that it's fixed at introspection
     * and the generators are placed into the Context.
     * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
     * @version $Revision: 438373 $
     */
    private class IDElementAttributes extends ElementAttributes
    {
        /** ID attribute value */
        private String idValue;
        /** ID attribute name */
        private String idAttributeName;

        private boolean matchingAttribute = false;
        private int length;
        private int idIndex;

        /**
         * Construct attributes for element and context.
         *
         * @param descriptor the <code>ElementDescriptor</code> describing the element
         * @param context evaluate against this context
         * @param idAttributeName the name of the id attribute
         * @param idValue the ID attribute value
         */
        IDElementAttributes(
            ElementDescriptor descriptor,
            Context context,
            String idAttributeName,
            String idValue)
        {
            super(descriptor, context);
            this.idValue = idValue;
            this.idAttributeName = idAttributeName;

            // see if we have already have a matching attribute descriptor
            AttributeDescriptor[] attributeDescriptors = descriptor.getAttributeDescriptors();
            length = super.getLength();
            for (int i=0; i<length; i++)
            {
                if (idAttributeName.equals(attributeDescriptors[i].getQualifiedName()))
                {
                    matchingAttribute = true;
                    idIndex = i;
                    break;
                }
            }
            if (!matchingAttribute)
            {
                length += 1;
                idIndex = length-1;
            }
        }

        public int getIndex(String uri, String localName)
        {
            if (localName.equals(idAttributeName))
            {
                return idIndex;
            }

            return super.getIndex(uri, localName);
        }

        public int getIndex(String qName)
        {
            if (qName.equals(idAttributeName))
            {
                return idIndex;
            }

            return super.getIndex(qName);
        }

        public int getLength()
        {
            return length;
        }

        public String getLocalName(int index)
        {
            if (index == idIndex)
            {
                return idAttributeName;
            }
            return super.getLocalName(index);
        }

        public String getQName(int index)
        {
            if (index == idIndex)
            {
                return idAttributeName;
            }
            return super.getQName(index);
        }

        public String getType(int index)
        {
            if (index == idIndex)
            {
                return "ID";
            }
            return super.getType(index);
        }

        public String getType(String uri, String localName)
        {
            return getType(getIndex(uri, localName));
        }

        public String getType(String qName)
        {
            return getType(getIndex(qName));
        }

        public String getURI(int index)
        {
            //TODO: this is probably wrong
            // probably need to move ID management into introspection
            // before we can handle this namespace bit correctly
            if (index == idIndex)
            {
                return "";
            }
            return super.getURI(index);
        }

        public String getValue(int index)
        {
            if (index == idIndex)
            {
                return idValue;
            }
            return super.getValue(index);
        }

        public String getValue(String uri, String localName)
        {
            return getValue(getIndex(uri, localName));
        }

        public String getValue(String qName)
        {
            return getValue(getIndex(qName));
        }

    }


    // OLD API (DEPRECATED)
    // --------------------------------------------------------------------------------------


    /**
     * Get the indentation for the current element.
     * Used for pretty priting.
     *
     * @return the amount that the current element is indented
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected int getIndentLevel()
    {
        return 0;
    }

    // Expression methods
    //-------------------------------------------------------------------------

    /**
     * Express an element tag start using given qualified name.
     *
     * @param qualifiedName the qualified name of the element to be expressed
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementStart(String qualifiedName)
    throws IOException, SAXException
    {
        // do nothing
    }

    /**
     * Express an element tag start using given qualified name.
     *
     * @param uri the namespace uri
     * @param localName the local name for this element
     * @param qualifiedName the qualified name of the element to be expressed
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementStart(String uri, String localName, String qualifiedName)
    throws IOException, SAXException
    {
        expressElementStart( qualifiedName );
    }

    /**
    * Express a closing tag.
    *
    * @throws IOException if an IO problem occurs during writing
    * @throws SAXException if an SAX problem occurs during writing
    * @deprecated 0.5 replaced by new SAX inspired API
    */
    protected void expressTagClose() throws IOException, SAXException {}

    /**
     * Express an element end tag (with given name)
     *
     * @param qualifiedName the qualified name for the element to be closed
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementEnd(String qualifiedName)
    throws IOException, SAXException
    {
        // do nothing
    }

    /**
     * Express an element end tag (with given name)
     *
     * @param uri the namespace uri of the element close tag
     * @param localName the local name of the element close tag
     * @param qualifiedName the qualified name for the element to be closed
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementEnd(
        String uri,
        String localName,
        String qualifiedName)
    throws
        IOException,
        SAXException
    {
        expressElementEnd(qualifiedName);
    }


    /**
     * Express an empty element end.
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressElementEnd() throws IOException, SAXException {}

    /**
     * Express body text
     *
     * @param text the string to write out as the body of the current element
     *
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressBodyText(String text) throws IOException, SAXException {}

    /**
     * Express an attribute
     *
     * @param qualifiedName the qualified name of the attribute
     * @param value the attribute value
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressAttribute(
        String qualifiedName,
        String value)
    throws
        IOException,
        SAXException
    {
        // Do nothing
    }

    /**
     * Express an attribute
     *
     * @param namespaceUri the namespace uri
     * @param localName the local name
     * @param qualifiedName the qualified name of the attribute
     * @param value the attribute value
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void expressAttribute(
        String namespaceUri,
        String localName,
        String qualifiedName,
        String value)
    throws
        IOException,
        SAXException
    {
        expressAttribute(qualifiedName, value);
    }


    /**
     * Writes the given element
     *
     * @param qualifiedName qualified name to use for the element
     * @param elementDescriptor the <code>ElementDescriptor</code> describing the element
     * @param context the <code>Context</code> to use to evaluate the bean expressions
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void write(
        String qualifiedName,
        ElementDescriptor elementDescriptor,
        Context context )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        writeElement( "", qualifiedName, qualifiedName, elementDescriptor, context );
    }

    /**
     * Writes the given element adding an ID attribute
     *
     * @param qualifiedName qualified name to use for the element
     * @param elementDescriptor the <code>ElementDescriptor</code> describing the element
     * @param context the <code>Context</code> to use to evaluate the bean expressions
     * @param idAttribute the qualified name of the <code>ID</code> attribute
     * @param idValue the value for the <code>ID</code> attribute
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void write(
        String qualifiedName,
        ElementDescriptor elementDescriptor,
        Context context,
        String idAttribute,
        String idValue )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        writeElement(
            "",
            qualifiedName,
            qualifiedName,
            elementDescriptor,
            context,
            idAttribute,
            idValue );
    }

    /**
     * Write attributes, child elements and element end
     *
     * @param qualifiedName qualified name to use for the element
     * @param elementDescriptor the <code>ElementDescriptor</code> describing the element
     * @param context the <code>Context</code> to use to evaluate the bean expressions
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void writeRestOfElement(
        String qualifiedName,
        ElementDescriptor elementDescriptor,
        Context context )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        writeRestOfElement( "", qualifiedName, qualifiedName, elementDescriptor, context );
    }

    /**
     * Writes an element with a <code>IDREF</code> attribute
     *
     * @param qualifiedName of the element with <code>IDREF</code> attribute
     * @param idrefAttributeName the qualified name of the <code>IDREF</code> attribute
     * @param idrefAttributeValue the value for the <code>IDREF</code> attribute
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void writeIDREFElement(
        String qualifiedName,
        String idrefAttributeName,
        String idrefAttributeValue )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        // deprecated
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(
            "",
            idrefAttributeName,
            idrefAttributeName,
            "IDREF",
            idrefAttributeValue);
        startElement( "", qualifiedName, qualifiedName, attributes);
        endElement( "", qualifiedName, qualifiedName );
    }


    /**
     * Writes the element content.
     *
     * @param elementDescriptor the <code>ElementDescriptor</code> to write as xml
     * @param context the <code>Context</code> to use to evaluate the bean expressions
     * @return true if some content was written
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @throws IntrospectionException if a java beans introspection problem occurs
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected boolean writeContent(
        ElementDescriptor elementDescriptor,
        Context context )
    throws
        IOException,
        SAXException,
        IntrospectionException
    {
        return false;
    }


    /**
     * Writes the attribute declarations
     *
     * @param elementDescriptor the <code>ElementDescriptor</code> to be written out as xml
     * @param context the <code>Context</code> to use to evaluation bean expressions
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void writeAttributes(
        ElementDescriptor elementDescriptor,
        Context context )
    throws
        IOException, SAXException
    {
        if (!elementDescriptor.isWrapCollectionsInElement())
        {
            return;
        }

        AttributeDescriptor[] attributeDescriptors = elementDescriptor.getAttributeDescriptors();
        if ( attributeDescriptors != null )
        {
            for ( int i = 0, size = attributeDescriptors.length; i < size; i++ )
            {
                AttributeDescriptor attributeDescriptor = attributeDescriptors[i];
                writeAttribute( attributeDescriptor, context );
            }
        }
    }


    /**
     * Writes an attribute declaration
     *
     * @param attributeDescriptor the <code>AttributeDescriptor</code> to be written as xml
     * @param context the <code>Context</code> to use to evaluation bean expressions
     * @throws IOException if an IO problem occurs during writing
     * @throws SAXException if an SAX problem occurs during writing
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void writeAttribute(
        AttributeDescriptor attributeDescriptor,
        Context context )
    throws
        IOException, SAXException
    {
        Expression expression = attributeDescriptor.getTextExpression();
        if ( expression != null )
        {
            Object value = expression.evaluate( context );
            if ( value != null )
            {
                String text = value.toString();
                if ( text != null && text.length() > 0 )
                {
                    expressAttribute(
                        attributeDescriptor.getURI(),
                        attributeDescriptor.getLocalName(),
                        attributeDescriptor.getQualifiedName(),
                        text);
                }
            }
        }
    }
    /**
     * Writes a empty line.
     * This implementation does nothing but can be overridden by subclasses.
     *
     * @throws IOException if the line cannot be written
     * @deprecated 0.5 replaced by new SAX inspired API
     */
    protected void writePrintln() throws IOException {}

    /**
     * Writes an indentation.
     * This implementation does nothing but can be overridden by subclasses.
     *
     * @throws IOException if the indent cannot be written
     * @deprecated 0.5 replaced by new BeanWriter API
     */
    protected void writeIndent() throws IOException {}

    /**
      * Converts an object to a string.
      *
      * @param value the Object to represent as a String, possibly null
      * @param descriptor writing out this descriptor not null
      * @param context not null
      * @return String representation, not null
      */
    private String convertToString( Object value , Descriptor descriptor, Context context )
    {
        return getBindingConfiguration()
               .getObjectStringConverter()
               .objectToString( value, descriptor.getPropertyType(), context );
    }

    /**
      * Factory method for new contexts.
      * Ensure that they are correctly configured.
      * @param bean make a new Context for this bean
      * @return not null
      */
    private Context makeContext(Object bean)
    {
        return new Context( bean, log, bindingConfiguration );
    }


    /**
     * Basic mutable implementation of <code>WriteContext</code>.
     */
    private static class WriteContextImpl extends WriteContext
    {

        private ElementDescriptor currentDescriptor;

        /**
         * @see org.apache.commons.betwixt.io.WriteContext#getCurrentDescriptor()
         */
        public ElementDescriptor getCurrentDescriptor()
        {
            return currentDescriptor;
        }

        /**
         * Sets the descriptor for the current element.
         * @param currentDescriptor
         */
        public void setCurrentDescriptor(ElementDescriptor currentDescriptor)
        {
            this.currentDescriptor = currentDescriptor;
        }

    }
}
