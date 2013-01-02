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

package org.apache.commons.betwixt.schema;

import java.beans.IntrospectionException;

import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.IntrospectionConfiguration;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;

/**
 * <p>Generates XML Schemas for Betwixt mappings.
 *
 * </p><p>
 * The basic idea is that an object model for the schema will be created
 * and Betwixt can be used to output this to xml.
 * This should allow both SAX and text.
 * </p>
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class SchemaTranscriber
{

    public static final String W3C_SCHEMA_URI = "http://www.w3.org/2001/XMLSchema";
    public static final String W3C_SCHEMA_INSTANCE_URI= "http://www.w3.org/2001/XMLSchema-instance";

    /** Used to introspect beans in order to generate XML */
    private XMLIntrospector introspector = new XMLIntrospector();
    private TranscriptionConfiguration configuration = new TranscriptionConfiguration();

    public SchemaTranscriber() {}

    /**
     * Gets the configuration for the XMLBeanInfo to XML schema transcription.
     * @return TranscriptionConfiguration, not null
     */
    public TranscriptionConfiguration getConfiguration()
    {
        return configuration;
    }

    /**
     * Sets the configuration for the XMLBeanInfo to XML schema transcription.
     * @param configuration TranscriptionConfiguration, not null
     */
    public void setConfiguration(TranscriptionConfiguration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Gets the XMLIntrospector used to create XMLInfoBean's.
     * @return XMLIntrospector used to create XMLInfoBean's used to generate schema, not null
     */
    public XMLIntrospector getXMLIntrospector()
    {
        return introspector;
    }

    /**
     * <p>Sets the XMLIntrospector used to create XMLInfoBeans.
     * </p></p>
     * <strong>Note:</strong> certain properties will be reconfigured so that
     * the introspection will produce correct results.
     * </p>
     * @param introspector XMLIntrospector used to create XMLInfoBean's used to generate schema, not null
     */
    public void setXMLIntrospector(XMLIntrospector introspector)
    {
        this.introspector = introspector;
    }

    /**
     * Generates an XML Schema model for the given class.
     * @param clazz not null
     * @return Schema model, not null
     */
    public Schema generate(Class clazz) throws IntrospectionException
    {
        XMLBeanInfo beanInfo = introspector.introspect(clazz);
        return generate(beanInfo);
    }

    /**
     * Generates an XML Schema model from the given XMLBeanInfo
     * @param xmlBeanInfo not null
     * @return Schema model, not null
     */
    public Schema generate(XMLBeanInfo xmlBeanInfo) throws IntrospectionException
    {
        ElementDescriptor elementDescriptor = xmlBeanInfo.getElementDescriptor();
        Schema schema = new Schema(introspector);
        schema.addGlobalElementType(configuration, elementDescriptor);
        return schema;
    }

    /**
     * <p>Gets an <code>IntrospectionConfiguration</code> that is suitable
     * for introspecting {@link Schema}.
     * </p><p>
     * <strong>Note:</strong> A new instance is created each time this method is called.
     * It can therefore be safely be modified.
     * </p>
     *
     * @return IntrospectionConfiguration, not null
     */
    public IntrospectionConfiguration createSchemaIntrospectionConfiguration()
    {
        IntrospectionConfiguration configuration = new IntrospectionConfiguration();
        configuration.getPrefixMapper().setPrefix(W3C_SCHEMA_URI, "xsd");
        configuration.getPrefixMapper().setPrefix(W3C_SCHEMA_INSTANCE_URI, "xsi");
        return configuration;
    }

    /**
     * <p>Gets a <code>BindingConfiguration</code> that is suitable for mapping {@link Schema}.
     * </p><p>
     * <strong>Note:</strong> A new instance is created each time this method is called.
     * It can therefore be safely be modified.
     * </p>
     * @return BindingConfiguration, not null
     */
    public BindingConfiguration createSchemaBindingConfiguration()
    {
        BindingConfiguration configuration = new BindingConfiguration();
        configuration.setMapIDs(false);
        return configuration;
    }
}
