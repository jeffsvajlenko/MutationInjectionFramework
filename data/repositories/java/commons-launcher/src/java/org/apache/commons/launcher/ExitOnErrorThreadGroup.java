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

/**
 * A class that subclasses the {@link ThreadGroup} class. This class is used
 * by {@link ChildMain#main(String[])} to run the target application. By using
 * this class, any {@link Error} other than {@link ThreadDeath} thrown by
 * threads created by the target application will be caught the process
 * terminated. By default, the JVM will only print a stack trace of the
 * {@link Error} and destroy the thread. However, when an uncaught
 * {@link Error} occurs, it normally means that the JVM has encountered a
 * severe problem. Hence, an orderly shutdown is a reasonable approach.
 * <p>
 * Note: not all threads created by the target application are guaranteed to
 * use this class. Target application's may bypass this class by creating a
 * thread using the {@link Thread#Thread(ThreadGroup, String)} or other similar
 * constructors.
 *
 * @author Patrick Luby
 */
public class ExitOnErrorThreadGroup extends ThreadGroup
{

    //------------------------------------------------------------ Constructors

    /**
     * Constructs a new thread group. The parent of this new group is the
     * thread group of the currently running thread.
     *
     * @param name the name of the new thread group
     */
    public ExitOnErrorThreadGroup(String name)
    {

        super(name);

    }

    //----------------------------------------------------------------- Methods

    /**
     * Trap any uncaught {@link Error} other than {@link ThreadDeath} and exit.
     *
     * @param t the thread that is about to exit
     * @param e the uncaught exception
     */
    public void uncaughtException(Thread t, Throwable e)
    {

        if (e instanceof ThreadDeath)
            return;

        Launcher.error(e);
        System.exit(1);

    }

}
