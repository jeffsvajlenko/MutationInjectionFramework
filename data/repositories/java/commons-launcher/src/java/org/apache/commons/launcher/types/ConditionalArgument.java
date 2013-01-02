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
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Path;

/**
 * A class that represents nested <arg> or <jvmarg> elements. This class
 * provides the same functionality as the class that represents these same
 * elements in a "java" task. In addition, this class supports conditional "if"
 * and "unless" attributes.
 *
 * @author Patrick Luby
 */
public class ConditionalArgument extends DataType
{

    //------------------------------------------------------------------ Fields

    /**
     * Cached "if" condition flag.
     */
    private String ifCondition = null;

    /**
     * Cached "unless" condition flag.
     */
    private String unlessCondition = null;

    /**
     * Cached command line arguments.
     */
    private String[] parts = null;

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
     * Get a single command line argument.
     *
     * @return a single command line argument
     */
    public String[] getParts()
    {

        String[] list = new String[parts.length];
        for (int i = 0; i < parts.length; i++)
            list[i] = ProjectHelper.replaceProperties(project, parts[i], project.getProperties());
        return list;

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
     * Set a single command line argument to the absolute
     * filename of the specified file.
     *
     * @param file a single command line argument
     */
    public void setFile(File file)
    {

        this.parts = new String[] { file.getAbsolutePath() };

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
     * Set a line to split into several command line arguments.
     *
     * @param line line to split into several commandline arguments
     */
    public void setLine(String line)
    {

        parts = Commandline.translateCommandline(line);

    }

    /**
     * Set a single command line argument and treat it like a path. The
     * correct path separator for the platform is used.
     *
     * @param path a single command line argument
     */
    public void setPath(Path path)
    {

        this.parts = new String[] { path.toString() };

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
     * Set a single command line argument.
     *
     * @param value a single command line argument
     */
    public void setValue(String value)
    {

        this.parts = new String[] { value };

    }

}
