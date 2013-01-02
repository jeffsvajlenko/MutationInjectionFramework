/*
 * Copyright 2003-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.attributes;

/**
 * Marks an attribute class as being sealable. When an instance of an attribute
 * class is created it goes through the following phases:
 *
 * <ol>
 * <li>Its constructor is called with all non-named parameters in the attribute declaration.
 * <li>Its setters are called according to the named parameters in the declaration.
 * </ol>
 *
 * This alone poses a security risk, as a client can call setters on an attribute as well,
 * and thus make class attributes mutable. In order to notify the attribute class that construction
 * and initialization is completed, the attribute runtime system will test if it implements Sealable,
 * and of so, invoke {@link #seal()} on the attribute instance.
 *
 * @see DefaultSealable
 * @since 2.1
 */
public interface Sealable
{
    /**
     * Called to indicate that construction and initialization of this attribute instance
     * is completed, and that the attribute instance should become read-only.
     *
    * @since 2.1
     */
    public void seal ();
}
