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

import org.apache.commons.betwixt.ElementDescriptor;

/**
 * <p>Context against which content should be written.</p>
 * <p>
 * <strong>Usage:</strong>
 * This (logical) interface is a <em>Parameter Object</em>
 * allowing additional, <code>Betwixt</code>-specific information
 * to be passed through the SAX-inspired writing API.
 * </p>
 * <p>
 * It is likely that access will be required to methods in the
 * <code>Context</code> used by the {@link AbstractBeanWriter}
 * but it seems better to add these as neccessary using delegation
 * rather than extending {@link org.apache.commons.betwixt.expression.Context}.
 * </p>
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @since 0.5
 */
public abstract class WriteContext
{

    /**
     * Gets the descriptor for the element who content
     * is currently being created.
     *
     * @return the <code>ElementDescriptor</code> for the
     * current element (or null if there is no current element)
     */
    public abstract ElementDescriptor getCurrentDescriptor();

}
