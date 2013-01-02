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

import java.util.Iterator;
import java.util.List;

/**
 * Thrown when an attribute repository class can't be
 * loaded because it resulted in a circular dependency.
 *
 * @since 2.1
 */
public class CircularDependencyError extends RepositoryError
{

    /**
     * Create a new CircularDependencyError.
     *
     * @param className the name of the class that started it all.
     * @param dependencyList a list of the classes ({@link java.lang.Class})
     *                       that the original
     *                       class depended on, the classes they
     *                       depended on, and so on. The list should
     *                       show the chain of dependencies that resulted
     *                       in the exception being thrown. <b>Note</b>:
     *                       Versions prior to 2.2 accepted a list of Objects
     *                       of any type. This is still supported, but the
     *                       formatting may suffer. Please only use lists of
     *                       {@link java.lang.Class}.
     *
     * @since 2.1
     */
    public CircularDependencyError (String className, List dependencyList)
    {
        super (className + ":" + listDeps (dependencyList), null);
    }

    /**
     * Joins together the elements of a list with <code>-&gt;</code>
     * delimiters. Used to show the sequence that resulted in the circular
     * dependency.
     *
     * @since 2.1
     */
    private static String listDeps (List dependencyList)
    {
        StringBuffer sb = new StringBuffer ();
        Iterator iter = dependencyList.iterator ();
        while (iter.hasNext ())
        {
            Object o = iter.next ();

            // Test that the user really passed in a Class. Versions
            // prior to 2.2 received Strings instead, and any user that
            // used the code would find it broken.
            if (o != null && o instanceof Class)
            {
                sb.append (((Class) o).getName ());
            }
            else
            {
                sb.append (o);
            }

            if (iter.hasNext ())
            {
                sb.append (" -> ");
            }
        }

        return sb.toString ();
    }
}
