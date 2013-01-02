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
 * Thrown when attempting to get attributes for a parameter of a constructor or method
 * and the parameter index is out of bounds.
 *
 * <p><b>Note:</b> for performance reasons, this exception is only thrown when the
 * Commons Attribute runtime can quickly determine that the index is out of bounds.
 * Therefore, you may sometimes be able to pass an invalid index and not receive this
 * exception. For example, if the runtime knows that the method or constructor has
 * no attributes at all, an empty collection / null or false (as appropriate) will be
 * returned instead. Put simply, <i>don't</i> use this exception to test how many parameters
 * a method or constructor has.
 *
 * @since 2.2
 */
public class ParameterIndexOutOfBoundsException extends IndexOutOfBoundsException
{

    /**
     * Create a new ParameterIndexOutOfBoundsException.
     *
     * @param methodOrConstructorName the name of the method or constructor whose parameter
     *                                the client tried to get attributes for.
     * @param index the index supplied by the client.
     * @param maxIndex the maximum + 1 parameter index allowed. For example, if a method takes
     *                 two parameters, the maximum allowed index is 1, and this parameter should
     *                 be set to 2. There is no minIndex parameter - it is assumed to be 0.
     */
    public ParameterIndexOutOfBoundsException (String methodOrConstructorName, int index, int maxIndex)
    {
        super (formatMessage (methodOrConstructorName, index, maxIndex));
    }

    private static String formatMessage (String methodOrConstructorName, int index, int maxIndex)
    {
        if (index < 0)
        {
            return String.valueOf (index);
        }
        else
        {
            return index + ". " + methodOrConstructorName + " has " + maxIndex + " parameters.";
        }
    }
}
