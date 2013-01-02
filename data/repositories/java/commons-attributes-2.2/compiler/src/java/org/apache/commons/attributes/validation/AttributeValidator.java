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

import java.util.Set;

/**
 * Validates that a set of classes have the correct attributes
 * attached to them. This interface must be implemented by the
 * validation rules given to the {@link AttributeValidatorTask}.
 *
 * @since 2.1
 */
public interface AttributeValidator
{

    /**
     * Validates a set of classes.
     *
     * @param classes the classes to validate.
     * @throws ValidationException if one or more classes have
     *         an invalid set of attributes.
     * @since 2.1
     */
    public void validate (Set classes) throws ValidationException;
}
