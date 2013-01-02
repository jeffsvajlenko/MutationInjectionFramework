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

import java.io.File;

import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Path;

/**
 * A class that represents nested <sysproperty> or <env> elements. This class
 * provides the same functionality as the class that represents these same
 * elements in a "java" task. In addition, this class supports conditional "if"
 * and "unless" attributes.
 *
 * @author Patrick Luby
 */
public class ConditionalVariable extends DataType
{

    //------------------------------------------------------------------ Fields

    /**
     * Cached "if" condition flag.
     */
    private String ifCondition = null;

    /**
     * Cached key.
     */
    private String key = null;

    /**
     * Cached "unless" condition flag.
     */
    private String unlessCondition = null;

    /**
     * Cached value.
     */
    private String value = null;

    //----------------------------------------------------------------- Methods

    /**
     * Get the "if" condition flag.
     *
     * @return the "if" condition flag
     */
    public String getIf()
    {

        return ProjectHelper.replaceProperties(project, ifCondition, project.getProperties());

    }

    /**
     * Get the key.
     *
     * @return the key for this variable
     */
    public String getKey()
    {

        return ProjectHelper.replaceProperties(project, key, project.getProperties());

    }

    /**
     * Get the "unless" condition flag.
     *
     * @return the "unless" condition flag
     */
    public String getUnless()
    {

        return ProjectHelper.replaceProperties(project, unlessCondition, project.getProperties());

    }

    /**
     * Get the value.
     *
     * @return the value for this variable
     */
    public String getValue()
    {

        return ProjectHelper.replaceProperties(project, value, project.getProperties());

    }

    /**
     * Set the value to a {@link File}.
     *
     * @param value the {@link File} for this variable
     */
    public void setFile(File file)
    {

        this.value = file.getAbsolutePath();

    }

    /**
     * Set the value to a {@link Path}.
     *
     * @param value the {@link Path} for this variable
     */
    public void setPath(Path path)
    {

        this.value = path.toString();

    }

    /**
     * Set the "if" condition. Tasks that nest this class as an element
     * should evaluate this flag in their {@link org.apache.tools.ant.Task#execute()} method. If the
     * following conditions are true, the task should process this element:
     * <ul>
     * <ol>The flag is neither null nor a empty string
     * <ol>The property that the flag resolves to after macro substitution
     *  is defined
     * </ul>
     *
     * @param property a property name or macro
     */
    public void setIf(String property)
    {

        this.ifCondition = property;

    }

    /**
     * Set the key.
     *
     * @param key the key for this variable
     */
    public void setKey(String key)
    {

        this.key = key;

    }

    /**
     * Set the value to a {@link Path}.
     *
     * @param value the {@link Path} for this variable
     */
    public void setFile(Path path)
    {

        this.value = path.toString();

    }

    /**
     * Set the "unless" condition. Tasks that nest this class as an element
     * should evaluate this flag in their {@link org.apache.tools.ant.Task#execute()} method. If the
     * following conditions are true, the task should ignore this element:
     * <ul>
     * <ol>The flag is neither null nor a empty string
     * <ol>The property that the flag resolves to after macro substitution
     *  is defined
     * </ul>
     *
     * @param property a property name or macro
     */
    public void setUnless(String property)
    {

        this.unlessCondition = property;

    }

    /**
     * Set the value.
     *
     * @param value the value for this variable
     */
    public void setValue(String value)
    {

        this.value = value;

    }

}
