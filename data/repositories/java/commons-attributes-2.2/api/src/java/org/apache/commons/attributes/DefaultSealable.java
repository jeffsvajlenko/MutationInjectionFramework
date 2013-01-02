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
 * Implementation of the {@link Sealable} interface. Subclasses should call
 * {@link #checkSealed()} before setting any bean properties.
 *
 * @since 2.1
 */
public class DefaultSealable implements Sealable
{

    /**
     * Boolean flag indicating whether the {@link seal()} method
     * has been called.
     */
    private volatile boolean sealed = false;

    /**
     * Default ctor.
     */
    public DefaultSealable ()
    {
    }

    /**
     * Checks if the {@link #seal()} method has been called and throws a
     * <code>IllegalStateException</code> if it has.
     *
     * @throws IllegalStateException if this attribute has been sealed.
     *
     * @since 2.1
     */
    protected void checkSealed () throws IllegalStateException
    {
        if (sealed)
        {
            throw new SealedAttributeException ();
        }
    }

    /**
     * Seals this attribute. Any future calls to the
     * {@link #checkSealed()} method will result in an
     * <code>IllegalStateException</code> being thrown.
     *
     * @since 2.1
     */
    public void seal ()
    {
        this.sealed = true;
    }
}
