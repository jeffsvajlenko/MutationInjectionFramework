/*
 * Copyright 1999-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.launcher.types;


/**
 * A class that represents a set of nested <arg> elements.
 *
 * @author Patrick Luby
 */
public class ArgumentSet extends ConditionalArgumentSet
{

    //----------------------------------------------------------- Static Fields

    /**
     * The name of this data type.
     */
    public final static String TYPE_NAME = "argset";

    //----------------------------------------------------------------- Methods

    /**
     * Add a {@link ConditionalArgument}.
     *
     * @param argument the {@link ConditionalArgument} to be
     *  added
     */
    public void addArg(ConditionalArgument argument)
    {

        addConditionalargument(argument);

    }

    /**
     * Add a {@link ArgumentSet}.
     *
     * @param set the {@link ArgumentSet} to be added
     */
    public void addArgset(ArgumentSet set)
    {

        addConditionalargumentset(set);

    }

}
