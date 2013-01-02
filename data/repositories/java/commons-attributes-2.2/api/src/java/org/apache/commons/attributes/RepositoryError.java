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
 * Thrown when an attribute repository class can't be
 * loaded or instantiated.
 *
 * @since 2.1
 */
public class RepositoryError extends Error
{

    private final Throwable nested;

    /**
     * Create a new RepositoryError with no message or nested Throwable.
     *
     * @since 2.1
     */
    public RepositoryError ()
    {
        this (null, null);
    }

    /**
     * Create a new RepositoryError with a message but no nested Throwable.
     *
     * @param message the message.
     * @since 2.1
     */
    public RepositoryError (String message)
    {
        this (message, null);
    }

    /**
     * Create a new RepositoryError with a nested Throwable. The message is set to <code>nested.toString()</code>.
     *
     * @param nested the nested Throwable.
     * @since 2.1
     */
    public RepositoryError (Throwable nested)
    {
        this (nested.toString(), nested);
    }

    /**
     * Create a new RepositoryError with a message and nested Throwable.
     *
     * @param message the message.
     * @param nested the nested Throwable.
     * @since 2.1
     */
    public RepositoryError (String message, Throwable nested)
    {
        super (message);
        this.nested = nested;
    }

    /**
     * Get the nested Throwable if any.
     *
     * @return the Throwable that caused this Error to be thrown, or <code>null</code> if none exists.
     * @since 2.1
     */
    public Throwable getNested ()
    {
        return nested;
    }

    /**
     * Get the nested Throwable, if any.
     *
     * @return the Throwable that caused this Error to be thrown, or <code>null</code> if none exists.
     * @since 2.2
     */
    public Throwable getCause ()
    {
        return nested;
    }
}
