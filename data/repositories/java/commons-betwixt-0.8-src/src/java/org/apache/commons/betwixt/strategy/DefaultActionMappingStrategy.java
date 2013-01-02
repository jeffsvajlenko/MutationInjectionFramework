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

package org.apache.commons.betwixt.strategy;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.io.read.ArrayBindAction;
import org.apache.commons.betwixt.io.read.BeanBindAction;
import org.apache.commons.betwixt.io.read.MappingAction;
import org.apache.commons.betwixt.io.read.ReadContext;
import org.apache.commons.betwixt.io.read.SimpleTypeBindAction;
import org.xml.sax.Attributes;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class DefaultActionMappingStrategy extends ActionMappingStrategy
{

    /**
     * Gets the mapping action to map the given element.
     * @param namespace not null
     * @param name not null
     * @param attributes <code>Attributes</code>, not null
     * @param context <code>ReadContext</code>, not null
     * @return <code>MappingAction</code>, not null
     * @throws Exception
     */
    public MappingAction getMappingAction(
        String namespace,
        String name,
        Attributes attributes,
        ReadContext context)
    throws Exception
    {
        MappingAction result = MappingAction.EMPTY;

        ElementDescriptor activeDescriptor = context.getCurrentDescriptor();
        if (activeDescriptor != null)
        {
            if (activeDescriptor.isHollow())
            {
                if (isArrayDescriptor(activeDescriptor))
                {
                    result = ArrayBindAction.createMappingAction(activeDescriptor);
                }
                else
                {
                    result = BeanBindAction.INSTANCE;
                }
            }
            else if (activeDescriptor.isSimple())
            {
                result = SimpleTypeBindAction.INSTANCE;
            }
            else
            {
                ElementDescriptor[] descriptors
                    = activeDescriptor.getElementDescriptors();
                if (descriptors.length == 1)
                {
                    ElementDescriptor childDescriptor = descriptors[0];
                    if (childDescriptor.isHollow()
                            && isArrayDescriptor(childDescriptor))
                    {
                        result = ArrayBindAction.createMappingAction(childDescriptor);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Is the give
     * @param descriptor <code>ElementDescriptor</code>, possibly null
     * @return true if the descriptor describes an array property, if null returns false
     */
    private boolean isArrayDescriptor(ElementDescriptor descriptor)
    {
        boolean result = false;
        if (descriptor != null)
        {
            Class propertyType = descriptor.getPropertyType();
            if (propertyType != null)
            {
                result = propertyType.isArray();
            }
        }
        return result;
    }
}
