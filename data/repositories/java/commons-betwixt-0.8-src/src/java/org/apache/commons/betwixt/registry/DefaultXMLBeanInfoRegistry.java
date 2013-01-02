package org.apache.commons.betwixt.registry;

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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.io.read.ElementMapping;
import org.apache.commons.betwixt.io.read.ReadContext;

/** The default caching implementation.
  * A hashmap is used.
  *
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Id: DefaultXMLBeanInfoRegistry.java 438373 2006-08-30 05:17:21Z bayard $
  */
public class DefaultXMLBeanInfoRegistry implements XMLBeanInfoRegistry, PolymorphicReferenceResolver
{

    /** Used to associated <code>XMLBeanInfo</code>'s to classes */
    private Map xmlBeanInfos = new HashMap();

    /**
      * Get <code>XMLBeanInfo</code> from cache.
      *
      * @param forThisClass the class for which to find a <code>XMLBeanInfo</code>
      * @return cached <code>XMLBeanInfo</code> associated with given class
      * or <code>null</code> if no <code>XMLBeanInfo</code> has been associated
      */
    public XMLBeanInfo get(Class forThisClass)
    {
        return (XMLBeanInfo) xmlBeanInfos.get(forThisClass);
    }

    /**
      * Put into cache
      *
      * @param forThisClass the class to cache the <code>XMLBeanInfo</code> for
      * @param beanInfo the <code>XMLBeanInfo</code> to cache
      */
    public void put(Class forThisClass, XMLBeanInfo beanInfo)
    {
        xmlBeanInfos.put(forThisClass, beanInfo);
    }

    /**
      * Flush existing cached <code>XMLBeanInfo</code>'s.
      */
    public void flush()
    {
        xmlBeanInfos.clear();
    }

    /**
     * Checks all registered <code>XMLBeanInfo</code>'s for the
     * first suitable match.
     * If a suitable one is found, then the class of that info is used.
     * @see org.apache.commons.betwixt.registry.PolymorphicReferenceResolver#resolveType(org.apache.commons.betwixt.io.read.ElementMapping, org.apache.commons.betwixt.io.read.ReadContext)
     * @since 0.7
     */
    public Class resolveType(ElementMapping mapping, ReadContext context)
    {
        Class result = null;
        Collection cachedClasses = getCachedClasses();
        ElementDescriptor mappedDescriptor = mapping.getDescriptor();
        Class mappedType = mappedDescriptor.getSingularPropertyType();
        if (mappedType == null)
        {
            mappedType = mappedDescriptor.getPropertyType();
        }
        for (Iterator it = cachedClasses.iterator(); it.hasNext();)
        {
            XMLBeanInfo  beanInfo  = get((Class)it.next());
            ElementDescriptor typeDescriptor = beanInfo.getElementDescriptor();
            boolean sameName = mapping.getName().equals(typeDescriptor.getQualifiedName());
            if (sameName)
            {

                boolean compatibleClass = mappedType.isAssignableFrom(beanInfo.getBeanClass());
                if (compatibleClass )
                {
                    result = beanInfo.getBeanClass();
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Gets all classes that are cached in this registry.
     *
     * @return The classes
     */
    private Collection getCachedClasses()
    {
        return xmlBeanInfos.keySet();
    }
}
