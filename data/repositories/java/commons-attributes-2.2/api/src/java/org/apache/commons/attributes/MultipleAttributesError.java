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
 * Thrown when one of the {@link Attributes}.getAttribute methods find more
 * than one instance of the specified attribute class and the method
 * only expected to find a single instance.
 *
 * @since 2.1
 */
public class MultipleAttributesError extends Error
{

    /**
     * Create a new MultipleAttributesError.
     *
     * @param clazz the name of the attribute class of which multiple instances were found.
     * @since 2.1
     */
    public MultipleAttributesError (String clazz)
    {
        super ("There was more than one attribute of class " + clazz + " associated with the element.");
    }

}
