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

/** <p> Common superclass for <code>ElementDescriptor</code>
  * and <code>AttributeDescriptor</code>.</p>
  *
  * <p> Nodes can have just a local name
  * or they can have a local name, qualified name and a namespace uri.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class NodeDescriptor extends Descriptor
{

    /** The local name of this node without any namespace prefix */
    private String localName;
    /** The qualified name of the xml node associated with this descriptor. */
    private String qualifiedName;
    /** The namespace URI of this node */
    private String uri = "";

    /** Base constructor */
    public NodeDescriptor()
    {
    }

    /**
     * Creates a NodeDescriptor with no namespace URI or prefix.
     *
     * @param localName the (xml) local name of this node.
     * This will be used to set both qualified and local name for this name.
     */
    public NodeDescriptor(String localName)
    {
        this.localName = localName;
        this.qualifiedName = localName;
    }


    /**
     * Creates a NodeDescriptor with namespace URI and qualified name
     * @param localName the (xml) local name of this  node
     * @param qualifiedName the (xml) qualified name of this node
     * @param uri the (xml) namespace prefix of this node
     */
    public NodeDescriptor(String localName, String qualifiedName, String uri)
    {
        this.localName = localName;
        this.qualifiedName = qualifiedName;
        this.uri = uri;
    }

    /**
     * Gets the local name, excluding any namespace prefix
     * @return the (xml) local name of this node
     */
    public String getLocalName()
    {
        return localName;
    }

    /**
     * Sets the local name
     * @param localName the (xml) local name of this node
     */
    public void setLocalName(String localName)
    {
        this.localName = localName;
    }

    /**
     * Gets the qualified name, including any namespace prefix
     * @return the (xml) qualified name of this node. This may be null.
     */
    public String getQualifiedName()
    {
        if ( qualifiedName == null )
        {
            qualifiedName = localName;
        }
        return qualifiedName;
    }

    /**
     * Sets the qualified name
     * @param qualifiedName the new (xml) qualified name for this node
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }

    /**
     * Gets the (xml) namespace URI prefix for this node.
     * @return the namespace URI that this node belongs to
     * or "" if there is no namespace defined
     */
    public String getURI()
    {
        return uri;
    }


    /**
     * Sets the namespace URI that this node belongs to.
     * @param uri the new namespace uri for this node
     */
    public void setURI(String uri)
    {
        if ( uri == null )
        {
            throw new IllegalArgumentException(
                "The namespace URI cannot be null. "
                + "No namespace URI is specified with the empty string"
            );
        }
        this.uri = uri;
    }
}
