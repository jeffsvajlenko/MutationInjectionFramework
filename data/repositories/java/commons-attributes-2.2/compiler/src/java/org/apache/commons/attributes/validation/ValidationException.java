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
package org.apache.commons.attributes.validation;

/**
 * Thrown by {@link AttributeValidator}s when an invalid set of
 * attributes are detected.
 *
 * @since 2.1
 */
public class ValidationException extends Exception
{

    private final Class invalidClass;

    /**
     * Creates a new ValidationException.
     *
     * @param invalidClass the class whose attributes are
     *                     invalid.
     * @param message a message describing why the attributes are invalid.
    * @since 2.1
     */
    public ValidationException (Class invalidClass, String message)
    {
        super (message);
        this.invalidClass = invalidClass;
    }

    /**
     * Returns the class that triggered the ValidationExeption to
     * be thrown.
    * @since 2.1
     */
    public Class getInvalidClass ()
    {
        return invalidClass;
    }
}
