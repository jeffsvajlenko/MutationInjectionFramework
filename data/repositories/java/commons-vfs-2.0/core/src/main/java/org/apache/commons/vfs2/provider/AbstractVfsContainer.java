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
package org.apache.commons.vfs2.provider;

import java.util.ArrayList;

import org.apache.commons.vfs2.FileSystemException;

/**
 * A {@link VfsComponent} that contains a set of sub-components.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
public abstract class AbstractVfsContainer
    extends AbstractVfsComponent
{
    /**
     * The components contained by this component.
     */
    private final ArrayList<Object> components = new ArrayList<Object>();

    /**
     * Adds a sub-component to this component.  If the sub-component implements
     * {@link VfsComponent}, it is initialised.  All sub-components are closed
     * when this component is closed.
     */
    protected void addComponent(final Object component)
    throws FileSystemException
    {
        if (!components.contains(component))
        {
            // Initialise
            if (component instanceof VfsComponent)
            {
                VfsComponent vfsComponent = (VfsComponent) component;
                vfsComponent.setLogger(getLogger());
                vfsComponent.setContext(getContext());
                vfsComponent.init();
            }

            // Keep track of component, to close it later
            components.add(component);
        }
    }

    /**
     * Removes a sub-component from this component.
     */
    protected void removeComponent(final Object component)
    {
        components.remove(component);
    }

    /**
     * Closes the sub-components of this component.
     */
    @Override
    public void close()
    {
        // Close all components
        final int count = components.size();
        for (int i = 0; i < count; i++)
        {
            final Object component = components.get(i);
            if (component instanceof VfsComponent)
            {
                final VfsComponent vfsComponent = (VfsComponent) component;
                vfsComponent.close();
            }
        }
        components.clear();
    }
}
