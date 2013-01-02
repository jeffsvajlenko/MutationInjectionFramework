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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that represents the holds the various argument types that are used
 * in a Java command. In addition, it holds many of the flags that are used
 * by the {@link LaunchTask} class when executing a JVM process.
 *
 * @author Patrick Luby
 */
public class LaunchCommand
{

    //------------------------------------------------------------------ Fields

    /**
     * Cached appendOutput flag.
     */
    private boolean appendOutput = false;

    /**
     * Cached classpath.
     */
    private String classpath = null;

    /**
     * Cached debug flag.
     */
    private boolean debug = false;

    /**
     * Cached displayMinimizedWindow flag.
     */
    private boolean displayMinimizedWindow = false;

    /**
     * Cached disposeMinimizedWindow flag.
     */
    private boolean disposeMinimizedWindow = true;

    /**
     * Cached failOnError flag.
     */
    private boolean failOnError = true;

    /**
     * Cached main class name.
     */
    private String mainClassName = null;

    /**
     * Cached minimizedWindowIcon.
     */
    private File minimizedWindowIcon = null;

    /**
     * Cached minimizedWindowTitle.
     */
    private String minimizedWindowTitle = null;

    /**
     * Cached output file.
     */
    private File outputFile = null;

    /**
     * Cached print flag.
     */
    private boolean print = false;

    /**
     * Cached requireTools flag.
     */
    private boolean requireTools = false;

    /**
     * Cached redirect flag.
     */
    private boolean redirect = false;

    /**
     * Cached arg elements
     */
    private ArrayList args = null;

    /**
     * Cached jvmarg elements
     */
    private ArrayList jvmArgs = null;

    /**
     * Cached sysproperty elements
     */
    private HashMap sysProperties = null;

    /**
     * Cached useSystemIn flag.
     */
    private boolean useSystemIn = true;

    /**
     * Cached waitForChild flag.
     */
    private boolean waitForChild = true;

    //----------------------------------------------------------------- Methods

    /**
     * Get the class name.
     *
     * @return the class to execute <code>main(String[])</code>
     */
    public String getClassname()
    {

        return mainClassName;

    }

    /**
     * Get the classpath.
     *
     * @return the classpath
     */
    public String getClasspath()
    {

        return classpath;

    }

    /**
     * Get the debug flag.
     *
     * @return the debug flag
     */
    public boolean getDebug()
    {

        return debug;

    }

    /**
     * Get the displayMinimizedWindow flag.
     *
     * @return the displayMinimizedWindow flag
     */
    public boolean getDisplayminimizedwindow()
    {

        return displayMinimizedWindow;

    }

    /**
     * Get the disposeMinimizedWindow flag.
     *
     * @return the disposeMinimizedWindow flag
     */
    public boolean getDisposeminimizedwindow()
    {

        return disposeMinimizedWindow;

    }

    /**
     * Get the failOnError flag.
     *
     * @return the failOnError flag
     */
    public boolean getFailonerror()
    {

        return failOnError;

    }

    /**
     * Get the title for the minimized window that will be displayed in the
     * Windows taskbar.
     *
     * @return the title to set for any minimized window that is displayed
     *  in the Windows taskbar
     */
    public String getMinimizedwindowtitle()
    {

        return minimizedWindowTitle;

    }

    /**
     * Get the icon file for the minimized window that will be displayed in the
     * Windows taskbar.
     *
     * @return the icon file to use for any minimized window that is displayed
     *  in the Windows taskbar
     */
    public File getMinimizedwindowicon()
    {

        return minimizedWindowIcon;

    }

    /**
     * Get the file that the child JVM's System.out and System.err will be
     * redirected to.
     *
     * @return the File to redirect System.out and System.err to
     */
    public File getOutput()
    {

        return outputFile;

    }

    /**
     * Get the appendOutput flag.
     *
     * @return the appendOutput flag
     */
    public boolean getAppendoutput()
    {

        return appendOutput;

    }

    /**
     * Get the redirect flag.
     *
     * @return the redirect flag
     */
    public boolean getRedirectoutput()
    {

        return redirect;

    }

    /**
     * Get the list of nested arg elements.
     *
     * @return the list of {@link String} objects
     */
    public ArrayList getArgs()
    {

        return args;

    }

    /**
     * Get the list of nested jvmarg elements.
     *
     * @return the list of {@link String} objects
     */
    public ArrayList getJvmargs()
    {

        return jvmArgs;

    }

    /**
     * Get the print flag.
     *
     * @return the print flag
     */
    public boolean getPrint()
    {

        return print;

    }

    /**
     * Get the requireTools flag.
     *
     * @return the requireTools flag
     */
    public boolean getRequiretools()
    {

        return requireTools;

    }

    /**
     * Get the list of nested sysproperty elements.
     *
     * @return the {@link String} objects
     */
    public HashMap getSysproperties()
    {

        return sysProperties;

    }

    /**
     * Get the useSystemIn flag.
     *
     * @return the useSystemIn flag
     */
    public boolean getUsesystemin()
    {

        return useSystemIn;

    }

    /**
     * Get the waitForChild flag.
     *
     * @return the waitForChild flag
     */
    public boolean getWaitforchild()
    {

        return waitForChild;

    }

    /**
     * Set the print flag.
     *
     * @param print the print flag
     */
    public void setPrint(boolean print)
    {

        this.print = print;

    }

    /**
     * Set the requireTools flag.
     *
     * @param requireTools the requireTools flag
     */
    public void setRequiretools(boolean requireTools)
    {

        this.requireTools = requireTools;

    }

    /**
     * Set the useSystemIn flag. Setting this flag to false will cause this
     * task to not read System.in. This will cause the child JVM to never
     * receive any bytes when it reads System.in. Setting this flag to false
     * is useful in some Unix environments where processes cannot be put in
     * the background when they read System.in.
     *
     * @param useSystemIn the useSystemIn flag
     */
    public void setUsesystemin(boolean useSystemIn)
    {

        this.useSystemIn = useSystemIn;

    }

    /**
     * Set the waitForChild flag. Setting this flag to true will cause this
     * task to wait for the child JVM to finish executing before the task
     * completes. Setting this flag to false will cause this task to complete
     * immediately after it starts the execution of the child JVM. Setting it
     * false emulates the "&" background operator in most Unix shells and is
     * most of set to false when launching server or GUI applications.
     *
     * @param waitForChild the waitForChild flag
     */
    public void setWaitforchild(boolean waitForChild)
    {

        this.waitForChild = waitForChild;

    }

    /**
     * Set the class name.
     *
     * @param mainClassName the class to execute <code>main(String[])</code>
     */
    public void setClassname(String mainClassName)
    {

        this.mainClassName = mainClassName;

    }

    /**
     * Set the classpath.
     *
     * @param classpath the classpath
     */
    public void setClasspath(String classpath)
    {

        this.classpath = classpath;

    }

    /**
     * Set the debug flag.
     *
     * @param debug the debug flag
     */
    public void setDebug(boolean debug)
    {

        this.debug = debug;

    }

    /**
     * Set the displayMinimizedWindow flag. Note that this flag has no effect
     * on non-Windows platforms. On Windows platform, setting this flag to true
     * will cause a minimized window to be displayed in the Windows task bar
     * while the child process is executing. This flag is usually set to true
     * for server applications that also have their "waitForChild" attribute
     * set to false via the {@link #setWaitforchild(boolean)} method.
     *
     * @param displayMinimizedWindow true if a minimized window should be
     *  displayed in the Windows task bar while the child process is executing
     */
    public void setDisplayminimizedwindow(boolean displayMinimizedWindow)
    {

        this.displayMinimizedWindow = displayMinimizedWindow;

    }

    /**
     * Set the disposeMinimizedWindow flag. Note that this flag has no effect
     * on non-Windows platforms. On Windows platform, setting this flag to true
     * will cause any minimized window that is display by setting the
     * "displayMinimizedWindow" attribute to true via the
     * {@link #setDisplayminimizedwindow(boolean)} to be automatically
     * disposed of when the child JVM's <code>main(String[])</code> returns.
     * This flag is normally used for applications that don't explicitly call
     * {@link System#exit(int)}. If an application does not explicitly call
     * {@link System#exit(int)}, an minimized windows need to be disposed of
     * for the child JVM to exit.
     *
     * @param disposeMinimizedWindow true if a minimized window in the Windows
     *  taskbar should be automatically disposed of after the child JVM's
     *  <code>main(String[])</code> returns
     */
    public void setDisposeminimizedwindow(boolean disposeMinimizedWindow)
    {

        this.disposeMinimizedWindow = disposeMinimizedWindow;

    }

    /**
     * Set the failOnError flag.
     *
     * @param failOnError the failOnError flag
     */
    public void setFailonerror(boolean failOnError)
    {

        this.failOnError = failOnError;

    }

    /**
     * Set the title for the minimized window that will be displayed in the
     * Windows taskbar. Note that this property has no effect on non-Windows
     * platforms.
     *
     * @param minimizedWindowTitle the title to set for any minimized window
     *  that is displayed in the Windows taskbar
     */
    public void setMinimizedwindowtitle(String minimizedWindowTitle)
    {

        this.minimizedWindowTitle = minimizedWindowTitle;

    }

    /**
     * Set the icon file for the minimized window that will be displayed in the
     * Windows taskbar. Note that this property has no effect on non-Windows
     * platforms.
     *
     * @param minimizedWindowIcon the icon file to use for any minimized window
     *  that is displayed in the Windows taskbar
     */
    public void setMinimizedwindowicon(File minimizedWindowIcon)
    {

        this.minimizedWindowIcon = minimizedWindowIcon;

    }

    /**
     * Set the file that the child JVM's System.out and System.err will be
     * redirected to. Output will only be redirected if the redirect flag
     * is set to true via the {@link #setRedirectoutput(boolean)} method.
     *
     * @param outputFile a File to redirect System.out and System.err to
     */
    public void setOutput(File outputFile)
    {

        this.outputFile = outputFile;

    }

    /**
     * Set the appendOutput flag. Setting this flag to true will cause the child
     * JVM to append System.out and System.err to the file specified by the
     * {@link #setOutput(File)} method. Setting this flag to false will cause
     * the child to overwrite the file.
     *
     * @param appendOutput true if output should be appended to the output file
     */
    public void setAppendoutput(boolean appendOutput)
    {

        this.appendOutput = appendOutput;

    }

    /**
     * Set the list of nested arg elements.
     *
     * @param args a list of {@link String} objects
     */
    public void setArgs(ArrayList args)
    {

        this.args = args;

    }

    /**
     * Set the list of nested jvmarg elements.
     *
     * @param jvmArgs a list of {@link String} objects
     */
    public void setJvmargs(ArrayList jvmArgs)
    {

        this.jvmArgs = jvmArgs;

    }

    /**
     * Set the list of nested sysproperty elements.
     *
     * @param sysProperties a map of {@link String} objects
     */
    public void setSysproperties(HashMap sysProperties)
    {

        this.sysProperties = sysProperties;

    }

    /**
     * Set the redirect flag. Setting this flag to true will cause the child
     * JVM's System.out and System.err to be redirected to file set using the
     * {@link #setOutput(File)} method. Setting this flag to false will
     * cause no redirection.
     *
     * @param redirect true if System.out and System.err should be redirected
     */
    public void setRedirectoutput(boolean redirect)
    {

        this.redirect = redirect;

    }

}
