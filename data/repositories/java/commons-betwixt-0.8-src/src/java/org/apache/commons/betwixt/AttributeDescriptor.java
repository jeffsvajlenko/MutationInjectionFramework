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

/** <p><code>AttributeDescriptor</code> describes the XML attributes
  * to be created for a bean instance.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class AttributeDescriptor extends NodeDescriptor
{

    /** Base constructor */
    public AttributeDescriptor()
    {
    }

    /**
     * Creates a AttributeDescriptor with no namespace URI or prefix
     *
     * @param localName the local name for the attribute, excluding any namespace prefix
     */
    public AttributeDescriptor(String localName)
    {
        super( localName );
    }

    /**
     * Creates a AttributeDescriptor with namespace URI and qualified name
     *
     * @param localName the local name for the attribute, excluding any namespace prefix
     * @param qualifiedName the fully quanified name, including the namespace prefix
     * @param uri the namespace for the attribute - or "" for no namespace
     */
    public AttributeDescriptor(String localName, String qualifiedName, String uri)
    {
        super(localName, qualifiedName, uri);
    }

    /**
     * Return something useful for logging
     *
     * @return something useful for logging
     */
    public String toString()
    {
        return "AttributeDescriptor[qname=" + getQualifiedName()
               + ",class=" + getPropertyType() + "]";
    }
}
