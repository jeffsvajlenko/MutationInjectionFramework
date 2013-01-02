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

package org.apache.commons.launcher;

import org.apache.tools.ant.BuildException;

/**
 * An interface that provides a means for application developers to perform
 * dynamic configuration and error checking of the attributes and nested
 * elements associated with a "launch" task that connot be easily done within
 * the constraints of Ant.
 * <p>
 * An implementor of this interface can be attached to a "launch" task by
 * setting the following "launch" task attributes in the Launcher's XML
 * file:
 * <ul>
 * <li><code>filterclassname</code> - The name of the class that implements
 * this interface
 * <li><code>filterclasspath</code> - (Optional) The classpath for the class
 * that implements
 * </ul>
 *
 * @author Patrick Luby
 */
public interface LaunchFilter
{

    //----------------------------------------------------------------- Methods

    /**
     * Perform error checking and editing of the JVM command line arguments
     * that an instance of the {@link LaunchTask} class has constructed.
     * Implementors will receive an instance of the {@link LaunchCommand} from
     * the {@link LaunchTask} instance that invokes this method. The
     * implementor of this method can then retrieve and edit any of the
     * JVM command line arguments via the {@link LaunchCommand} class' public
     * methods.
     *
     * @param launchCommand a configured {@link LaunchCommand} instance
     * @throws BuildException if any errors occur
     */
    public void filter(LaunchCommand launchCommand) throws BuildException;

}
