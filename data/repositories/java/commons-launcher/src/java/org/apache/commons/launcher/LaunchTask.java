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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.apache.commons.launcher.types.ArgumentSet;
import org.apache.commons.launcher.types.ConditionalArgument;
import org.apache.commons.launcher.types.ConditionalVariable;
import org.apache.commons.launcher.types.JVMArgumentSet;
import org.apache.commons.launcher.types.SysPropertySet;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/**
 * A class that eliminates the need for a batch or shell script to launch a Java
 * class. Some situations where elimination of a batch or shell script may be
 * desirable are:
 * <ul>
 * <li>You want to avoid having to determining where certain application paths
 *  are e.g. your application's home directory, etc. Determining this
 *  dynamically in a Windows batch scripts is very tricky on some versions of
 *  Windows or when softlinks are used on Unix platforms.
 * <li>You want to avoid having to handle native file and path separators or
 *  native path quoting issues.
 * <li>You need to enforce certain system properties e.g.
 *  <code>java.endorsed.dirs</code> when running with JDK 1.4.
 * <li>You want to allow users to pass in custom JVM arguments or system
 *  properties without having to parse and reorder arguments in your script.
 *  This can be tricky and/or messy in batch and shell scripts.
 * <li>You want to bootstrap system properties from a configuration file instead
 *  hard-coding them in your batch and shell scripts.
 * <li>You want to provide localized error messages which is very tricky to do
 *  in batch and shell scripts.
 * </ul>
 *
 * @author Patrick Luby
 */
public class LaunchTask extends Task
{

    //----------------------------------------------------------- Static Fields

    /**
     * The argument property name.
     */
    public final static String ARG_PROP_NAME = "launch.arg.";

    /**
     * The name of this task.
     */
    public final static String TASK_NAME = "launch";

    /**
     * Cached synchronous child processes for all instances of this class.
     */
    private static ArrayList childProcesses = new ArrayList();

    //------------------------------------------------------------------ Fields

    /**
     * Cached appendOutput flag.
     */
    private boolean appendOutput = false;

    /**
     * Cached synchronously executing child process.
     */
    private Process childProc = null;

    /**
     * Cached classpath.
     */
    private Path classpath = null;

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
    private boolean failOnError = false;

    /**
     * Cached filter instance.
     */
    private LaunchFilter filter = null;

    /**
     * Cached filterClassName.
     */
    private String filterClassName = null;

    /**
     * Cached filterClasspath.
     */
    private Path filterClasspath = null;

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
     * Cached redirect flag.
     */
    private boolean redirect = false;

    /**
     * Cached requireTools flag.
     */
    private boolean requireTools = false;

    /**
     * Cached arg elements
     */
    private ArgumentSet taskArgumentSet = new ArgumentSet();

    /**
     * Cached jvmarg elements
     */
    private JVMArgumentSet taskJVMArgumentSet = new JVMArgumentSet();

    /**
     * Cached sysproperty elements
     */
    private SysPropertySet taskSysPropertySet = new SysPropertySet();

    /**
     * Cached useArgs flag.
     */
    private boolean useArgs = true;

    /**
     * Cached useSystemIn flag.
     */
    private boolean useSystemIn = true;

    /**
     * Cached waitForChild flag.
     */
    private boolean waitForChild = true;

    //---------------------------------------------------------- Static Methods

    /**
     * Get the synchronous child processes for all instances of this class.
     *
     * @return the instances of this class.
     */
    public static Process[] getChildProcesses()
    {

        return (Process[])childProcesses.toArray(new Process[childProcesses.size()]);

    }

    //----------------------------------------------------------------- Methods

    /**
     * Add a nested arg element. Note that Ant will not invoke the specified
     * arg object's setter methods until after Ant invokes this method so
     * processing of the specified arg object is handled in the
     * {@link #execute()} method.
     *
     * @param arg the arg element
     */
    public void addArg(ConditionalArgument arg)
    {

        taskArgumentSet.addArg(arg);

    }

    /**
     * Add a nested argset element.
     *
     * @param set the argset element
     */
    public void addArgset(ArgumentSet set)
    {

        taskArgumentSet.addArgset(set);

    }

    /**
     * Add a nested jvmarg element. Note that Ant will not invoke the specified
     * jvmarg object's setter methods until after Ant invokes this method so
     * processing of the specified jvmarg object is handled in the
     * {@link #execute()} method.
     *
     * @param jvmArg the jvmarg element
     */
    public void addJvmarg(ConditionalArgument jvmArg)
    {

        taskJVMArgumentSet.addJvmarg(jvmArg);

    }

    /**
     * Add a nested jvmargset element.
     *
     * @param set the jvmargset element
     */
    public void addJvmargset(JVMArgumentSet set)
    {

        taskJVMArgumentSet.addJvmargset(set);

    }

    /**
     * Add a nested sysproperty element. Note that Ant will not invoke the
     * specified sysproperty object's setter methods until after Ant invokes
     * this method so processing of the specified sysproperty object is handled
     * in the {@link #execute()} method.
     *
     * @param var the sysproperty element
     */
    public void addSysproperty(ConditionalVariable var)
    {

        taskSysPropertySet.addSysproperty(var);

    }

    /**
     * Add a nested syspropertyset element.
     *
     * @param set the syspropertyset element
     */
    public void addSyspropertyset(SysPropertySet set)
    {

        taskSysPropertySet.addSyspropertyset(set);

    }

    /**
     * Create a nested classpath element.
     *
     * @return the Path object that contains all nested classpath elements
     */
    public Path createClasspath()
    {

        if (classpath == null)
            classpath = new Path(project);
        return classpath;

    }

    /**
     * Create a nested filter classpath element.
     *
     * @return the Path object that contains all nested filter classpath
     *  elements
     */
    public Path createFilterclasspath()
    {

        if (filterClasspath == null)
            filterClasspath = new Path(project);
        return filterClasspath;

    }

    /**
     * Construct a Java command and execute it using the settings that Ant
     * parsed from the Launcher's XML file. This method is called by the Ant
     * classes.
     *
     * @throws BuildException if there is a configuration or other error
     */
    public void execute() throws BuildException
    {

        try
        {

            // Check that the Launcher class was used to start Ant as this
            // task is not designed to use in a standalone Ant installation
            if (!Launcher.isStarted())
                throw new BuildException(Launcher.getLocalizedString("no.run.standalone", this.getClass().getName()));

            // Don't do anything if the launching process has been stopped
            if (Launcher.isStopped())
                throw new BuildException();

            if (mainClassName == null)
                throw new BuildException(Launcher.getLocalizedString("classname.null", this.getClass().getName()));

            // Copy all of the nested jvmarg elements into the jvmArgs object
            ArrayList taskJVMArgs = taskJVMArgumentSet.getList();
            ArrayList jvmArgs = new ArrayList(taskJVMArgs.size());
            for (int i = 0; i < taskJVMArgs.size(); i++)
            {
                ConditionalArgument value = (ConditionalArgument)taskJVMArgs.get(i);
                // Test "if" and "unless" conditions
                if (testIfCondition(value.getIf()) && testUnlessCondition(value.getUnless()))
                {
                    String[] list = value.getParts();
                    for (int j = 0; j < list.length; j++)
                        jvmArgs.add(list[j]);
                }
            }

            // Copy all of the nested sysproperty elements into the sysProps
            // object
            ArrayList taskSysProps = taskSysPropertySet.getList();
            HashMap sysProps = new HashMap(taskSysProps.size());
            for (int i = 0; i < taskSysProps.size(); i++)
            {
                ConditionalVariable variable = (ConditionalVariable)taskSysProps.get(i);
                // Test "if" and "unless" conditions
                if (testIfCondition(variable.getIf()) && testUnlessCondition(variable.getUnless()))
                    sysProps.put(variable.getKey(), variable.getValue());
            }

            // Copy all of the nested arg elements into the appArgs object
            ArrayList taskArgs = taskArgumentSet.getList();
            ArrayList appArgs = new ArrayList(taskArgs.size());
            for (int i = 0; i < taskArgs.size(); i++)
            {
                ConditionalArgument value = (ConditionalArgument)taskArgs.get(i);
                // Test "if" and "unless" conditions
                if (testIfCondition(value.getIf()) && testUnlessCondition(value.getUnless()))
                {
                    String[] list = value.getParts();
                    for (int j = 0; j < list.length; j++)
                        appArgs.add(list[j]);
                }
            }

            // Add the Launcher's command line arguments to the appArgs object
            if (useArgs)
            {
                int currentArg = 0;
                String arg = null;
                while ((arg = project.getUserProperty(LaunchTask.ARG_PROP_NAME + Integer.toString(currentArg++))) != null)
                    appArgs.add(arg);
            }

            // Make working copies of some of the flags since they may get
            // changed by a filter class
            String filteredClasspath = null;
            if (classpath != null)
                filteredClasspath = classpath.toString();
            String filteredMainClassName = mainClassName;
            boolean filteredRedirect = redirect;
            File filteredOutputFile = outputFile;
            boolean filteredAppendOutput = appendOutput;
            boolean filteredDebug = debug;
            boolean filteredDisplayMinimizedWindow = displayMinimizedWindow;
            boolean filteredDisposeMinimizedWindow = disposeMinimizedWindow;
            boolean filteredFailOnError = failOnError;
            String filteredMinimizedWindowTitle = minimizedWindowTitle;
            File filteredMinimizedWindowIcon = minimizedWindowIcon;
            boolean filteredPrint = print;
            boolean filteredRequireTools = requireTools;
            boolean filteredUseSystemIn = useSystemIn;
            boolean filteredWaitForChild = waitForChild;

            // If there is a filter in the filterclassname attribute, let it
            // evaluate and edit the attributes and nested elements before we
            // start evaluating them
            if (filterClassName != null)
            {
                if (filter == null)
                {
                    try
                    {
                        ClassLoader loader = this.getClass().getClassLoader();
                        if (filterClasspath != null)
                        {
                            // Construct a class loader to load the class
                            String[] fileList = filterClasspath.list();
                            URL[] urls = new URL[fileList.length];
                            for (int i = 0; i < fileList.length; i++)
                                urls[i] = new File(fileList[i]).toURL();
                            loader = new URLClassLoader(urls, loader);
                        }
                        Class filterClass = loader.loadClass(filterClassName);
                        filter = (LaunchFilter)filterClass.newInstance();
                        // Execute filter and save any changes
                        LaunchCommand command = new LaunchCommand();
                        command.setJvmargs(jvmArgs);
                        command.setSysproperties(sysProps);
                        command.setArgs(appArgs);
                        command.setClasspath(filteredClasspath);
                        command.setClassname(filteredMainClassName);
                        command.setRedirectoutput(filteredRedirect);
                        command.setOutput(filteredOutputFile);
                        command.setAppendoutput(filteredAppendOutput);
                        command.setDebug(filteredDebug);
                        command.setDisplayminimizedwindow(filteredDisplayMinimizedWindow);
                        command.setDisposeminimizedwindow(filteredDisposeMinimizedWindow);
                        command.setFailonerror(filteredFailOnError);
                        command.setMinimizedwindowtitle(filteredMinimizedWindowTitle);
                        command.setMinimizedwindowicon(filteredMinimizedWindowIcon);
                        command.setPrint(filteredPrint);
                        command.setRequiretools(filteredRequireTools);
                        command.setUsesystemin(filteredUseSystemIn);
                        command.setWaitforchild(filteredWaitForChild);
                        filter.filter(command);
                        jvmArgs = command.getJvmargs();
                        sysProps = command.getSysproperties();
                        appArgs = command.getArgs();
                        filteredClasspath = command.getClasspath();
                        filteredMainClassName = command.getClassname();
                        filteredRedirect = command.getRedirectoutput();
                        filteredOutputFile = command.getOutput();
                        filteredAppendOutput = command.getAppendoutput();
                        filteredDebug = command.getDebug();
                        filteredDisplayMinimizedWindow = command.getDisplayminimizedwindow();
                        filteredDisposeMinimizedWindow = command.getDisposeminimizedwindow();
                        filteredFailOnError = command.getFailonerror();
                        filteredMinimizedWindowTitle = command.getMinimizedwindowtitle();
                        filteredMinimizedWindowIcon = command.getMinimizedwindowicon();
                        filteredPrint = command.getPrint();
                        filteredRequireTools = command.getRequiretools();
                        filteredUseSystemIn = command.getUsesystemin();
                        filteredWaitForChild = command.getWaitforchild();
                        // Check changes
                        if (filteredMainClassName == null)
                            throw new BuildException(Launcher.getLocalizedString("classname.null", this.getClass().getName()));
                        if (jvmArgs == null)
                            jvmArgs = new ArrayList();
                        if (sysProps == null)
                            sysProps = new HashMap();
                        if (appArgs == null)
                            appArgs = new ArrayList();
                    }
                    catch (BuildException be)
                    {
                        throw new BuildException(filterClassName + " " + Launcher.getLocalizedString("filter.exception", this.getClass().getName()), be);
                    }
                    catch (ClassCastException cce)
                    {
                        throw new BuildException(filterClassName + " " + Launcher.getLocalizedString("filter.not.filter", this.getClass().getName()));
                    }
                    catch (Exception e)
                    {
                        throw new BuildException(e);
                    }
                }
            }

            // Force child JVM into foreground if running using JDB
            if (filteredDebug)
            {
                filteredWaitForChild = true;
                filteredUseSystemIn = true;
            }

            // Prepend standard paths to classpath
            StringBuffer fullClasspath = new StringBuffer(Launcher.getBootstrapFile().getPath());
            if (filteredRequireTools)
            {
                fullClasspath.append(File.pathSeparator);
                fullClasspath.append(Launcher.getToolsClasspath());
            }
            if (filteredClasspath != null)
            {
                fullClasspath.append(File.pathSeparator);
                fullClasspath.append(filteredClasspath);
            }

            // Set ChildMain.WAIT_FOR_CHILD_PROP_NAME property for child JVM
            sysProps.remove(ChildMain.WAIT_FOR_CHILD_PROP_NAME);
            if (filteredWaitForChild)
                sysProps.put(ChildMain.WAIT_FOR_CHILD_PROP_NAME, "");

            // Set minimized window properties for child JVM
            sysProps.remove(ChildMain.DISPLAY_MINIMIZED_WINDOW_PROP_NAME);
            sysProps.remove(ChildMain.MINIMIZED_WINDOW_TITLE_PROP_NAME);
            sysProps.remove(ChildMain.MINIMIZED_WINDOW_ICON_PROP_NAME);
            sysProps.remove(ChildMain.DISPOSE_MINIMIZED_WINDOW_PROP_NAME);
            if (!filteredWaitForChild && filteredDisplayMinimizedWindow)
            {
                sysProps.put(ChildMain.DISPLAY_MINIMIZED_WINDOW_PROP_NAME, "");
                if (filteredMinimizedWindowTitle != null)
                    sysProps.put(ChildMain.MINIMIZED_WINDOW_TITLE_PROP_NAME, filteredMinimizedWindowTitle);
                else
                    sysProps.put(ChildMain.MINIMIZED_WINDOW_TITLE_PROP_NAME, getOwningTarget().getName());
                if (filteredMinimizedWindowIcon != null)
                    sysProps.put(ChildMain.MINIMIZED_WINDOW_ICON_PROP_NAME, filteredMinimizedWindowIcon.getCanonicalPath());
                // Set ChildMain.DISPOSE_MINIMIZED_WINDOW_PROP_NAME property
                if (filteredDisposeMinimizedWindow)
                    sysProps.put(ChildMain.DISPOSE_MINIMIZED_WINDOW_PROP_NAME, "");
            }

            // Set ChildMain.OUTPUT_FILE_PROP_NAME property for child JVM
            sysProps.remove(ChildMain.OUTPUT_FILE_PROP_NAME);
            if (!filteredWaitForChild && filteredRedirect)
            {
                if (filteredOutputFile != null)
                {
                    String outputFilePath = filteredOutputFile.getCanonicalPath();
                    // Verify that we can write to the output file
                    try
                    {
                        File parentFile = new File(filteredOutputFile.getParent());
                        // To take care of non-existent log directories
                        if ( !parentFile.exists() )
                        {
                            //Trying to create non-existent parent directories
                            parentFile.mkdirs();
                            //If this fails createNewFile also fails
                            //We can give more exact error message, if we choose
                        }
                        filteredOutputFile.createNewFile();
                    }
                    catch (IOException ioe)
                    {
                        throw new BuildException(outputFilePath + " " + Launcher.getLocalizedString("output.file.not.creatable", this.getClass().getName()));
                    }
                    if (!filteredOutputFile.canWrite())
                        throw new BuildException(outputFilePath + " " + Launcher.getLocalizedString("output.file.not.writable", this.getClass().getName()));
                    sysProps.put(ChildMain.OUTPUT_FILE_PROP_NAME, outputFilePath);
                    if (filteredAppendOutput)
                        sysProps.put(ChildMain.APPEND_OUTPUT_PROP_NAME, "");
                    Launcher.getLog().println(Launcher.getLocalizedString("redirect.notice", this.getClass().getName()) + " " + outputFilePath);
                }
                else
                {
                    throw new BuildException(Launcher.getLocalizedString("output.file.null", this.getClass().getName()));
                }
            }

            // Create the heartbeatFile. This file is needed by the
            // ParentListener class on Windows since the entire child JVM
            // process will block on Windows machines using some versions of
            // Unix shells such as MKS, etc.
            File heartbeatFile = null;
            FileOutputStream heartbeatOutputStream = null;
            if (filteredWaitForChild)
            {
                File tmpDir = null;
                String tmpDirName = (String)sysProps.get("java.io.tmpdir");
                if (tmpDirName != null)
                    tmpDir = new File(tmpDirName);
                heartbeatFile = File.createTempFile(ChildMain.HEARTBEAT_FILE_PROP_NAME + ".", "", tmpDir);
                // Open the heartbeat file for writing so that it the child JVM
                // will not be able to delete it while this process is running
                heartbeatOutputStream = new FileOutputStream(heartbeatFile);
                sysProps.put(ChildMain.HEARTBEAT_FILE_PROP_NAME, heartbeatFile.getCanonicalPath());
            }

            // Assemble child command
            String[] cmd = new String[5 + jvmArgs.size() + sysProps.size() + appArgs.size()];
            int nextCmdArg = 0;
            if (filteredDebug)
                cmd[nextCmdArg++] = Launcher.getJDBCommand();
            else
                cmd[nextCmdArg++] = Launcher.getJavaCommand();
            // Add jvmArgs to command
            for (int i = 0; i < jvmArgs.size(); i++)
                cmd[nextCmdArg++] = (String)jvmArgs.get(i);
            // Add properties to command
            Iterator sysPropsKeys = sysProps.keySet().iterator();
            while (sysPropsKeys.hasNext())
            {
                String key = (String)sysPropsKeys.next();
                if (key == null)
                    continue;
                String value = (String)sysProps.get(key);
                if (value == null)
                    value = "";
                cmd[nextCmdArg++] = "-D" + key + "=" + value;
            }
            // Add classpath to command. Note that it is after the jvmArgs
            // and system properties to prevent the user from sneaking in an
            // alterate classpath through the jvmArgs.
            cmd[nextCmdArg++] = "-classpath";
            cmd[nextCmdArg++] = fullClasspath.toString();
            // Add main class to command
            int mainClassArg = nextCmdArg;
            cmd[nextCmdArg++] = ChildMain.class.getName();
            cmd[nextCmdArg++] = filteredMainClassName;
            // Add args to command
            for (int i = 0; i < appArgs.size(); i++)
            {
                cmd[nextCmdArg++] = (String)appArgs.get(i);
            }
            // Print command
            if (filteredPrint)
            {
                // Quote the command arguments
                String osname = System.getProperty("os.name").toLowerCase();
                StringBuffer buf = new StringBuffer(cmd.length * 100);
                String quote = null;
                String replaceQuote = null;
                if (osname.indexOf("windows") >= 0)
                {
                    // Use double-quotes to quote on Windows
                    quote = "\"";
                    replaceQuote = quote + quote + quote;
                }
                else
                {
                    // Use single-quotes to quote on Unix
                    quote = "'";
                    replaceQuote = quote + "\\" + quote + quote;
                }
                for (int i = 0; i < cmd.length; i++)
                {
                    // Pull ChildMain out of command as we want to print the
                    // real JVM command that can be executed by the user
                    if (i == mainClassArg)
                        continue;
                    if (i > 0)
                        buf.append(" ");
                    buf.append(quote);
                    StringTokenizer tokenizer = new StringTokenizer(cmd[i], quote, true);
                    while (tokenizer.hasMoreTokens())
                    {
                        String token = tokenizer.nextToken();
                        if (quote.equals(token))
                            buf.append(replaceQuote);
                        else
                            buf.append(token);
                    }
                    buf.append(quote);
                }
                // Print the quoted command
                System.err.println(Launcher.getLocalizedString("executing.child.command", this.getClass().getName()) + ":");
                System.err.println(buf.toString());
            }

            // Create a child JVM
            if (Launcher.isStopped())
                throw new BuildException();
            Process proc = null;
            synchronized (LaunchTask.childProcesses)
            {
                proc = Runtime.getRuntime().exec(cmd);
                // Add the synchronous child process
                if (filteredWaitForChild)
                {
                    childProc = proc;
                    LaunchTask.childProcesses.add(proc);
                }
            }
            if (filteredWaitForChild)
            {
                StreamConnector stdout =
                    new StreamConnector(proc.getInputStream(), System.out);
                StreamConnector stderr =
                    new StreamConnector(proc.getErrorStream(), System.err);
                stdout.start();
                stderr.start();
                if (filteredUseSystemIn)
                {
                    StreamConnector stdin =
                        new StreamConnector(System.in, proc.getOutputStream());
                    stdin.start();
                }
                proc.waitFor();
                // Let threads flush any unflushed output
                stdout.join();
                stderr.join();
                if (heartbeatOutputStream != null)
                    heartbeatOutputStream.close();
                if (heartbeatFile != null)
                    heartbeatFile.delete();
                int exitValue = proc.exitValue();
                if (filteredFailOnError && exitValue != 0)
                    throw new BuildException(Launcher.getLocalizedString("child.failed", this.getClass().getName()) + " " + exitValue);
            }
            // Need to check if the launching process has stopped because
            // processes don't throw exceptions when they are terminated
            if (Launcher.isStopped())
                throw new BuildException();

        }
        catch (BuildException be)
        {
            throw be;
        }
        catch (Exception e)
        {
            if (Launcher.isStopped())
                throw new BuildException(Launcher.getLocalizedString("launch.task.stopped", this.getClass().getName()));
            else
                throw new BuildException(e);
        }

    }

    /**
     * Set the useArgs flag. Setting this flag to true will cause this
     * task to append all of the command line arguments used to start the
     * {@link Launcher#start(String[])} method to the arguments
     * passed to the child JVM.
     *
     * @param useArgs the useArgs flag
     */
    public void setUseargs(boolean useArgs)
    {

        this.useArgs = useArgs;

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
    public void setClasspath(Path classpath)
    {

        createClasspath().append(classpath);

    }

    /**
     * Adds a reference to a classpath defined elsewhere.
     *
     * @param ref reference to the classpath
     */
    public void setClasspathref(Reference ref)
    {

        createClasspath().setRefid(ref);

    }

    /**
     * Set the debug flag. Setting this flag to true will cause this
     * task to run the child JVM using the JDB debugger.
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
     * @param failOnError true if the launch process should stop if the child
     *  JVM returns an exit value other than 0
     */
    public void setFailonerror(boolean failOnError)
    {

        this.failOnError = failOnError;

    }
    /**
     * Set the filter class name.
     *
     * @param filterClassName the class that implements the
     *  {@link LaunchFilter} interface
     */
    public void setFilterclassname(String filterClassName)
    {

        this.filterClassName = filterClassName;

    }

    /**
     * Set the filter class' classpath.
     *
     * @param classpath the classpath for the filter class
     */
    public void setFilterclasspath(Path filterClasspath)
    {

        createFilterclasspath().append(filterClasspath);

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
     * Set the print flag. Setting this flag to true will cause the full child
     * JVM command to be printed to {@link System#out}.
     *
     * @param print the print flag
     */
    public void setPrint(boolean print)
    {

        this.print = print;

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

    /**
     * Set the requireTools flag. Setting this flag to true will cause the
     * JVM's tools.jar to be added to the child JVM's classpath. This
     * sets an explicit requirement that the user use a JDK instead of a
     * JRE. Setting this flag to false explicitly allows the user to use
     * a JRE.
     *
     * @param redirect true if a JDK is required and false if only a JRE
     *  is required
     */
    public void setRequiretools(boolean requireTools)
    {

        this.requireTools = requireTools;

    }

    /**
     * Determine if the "if" condition flag for a nested element meets all
     * criteria for use.
     *
     * @param ifCondition the "if" condition flag for a nested element
     * @return true if the nested element should be process and false if it
     *  should be ignored
     */
    private boolean testIfCondition(String ifCondition)
    {

        if (ifCondition == null || "".equals(ifCondition))
            return true;
        return project.getProperty(ifCondition) != null;

    }

    /**
     * Determine if the "unless" condition flag for a nested element meets all
     * criteria for use.
     *
     * @param unlessCondition the "unless" condition flag for a nested element
     * @return true if the nested element should be process and false if it
     *  should be ignored
     */
    private boolean testUnlessCondition(String unlessCondition)
    {

        if (unlessCondition == null || "".equals(unlessCondition))
            return true;
        return project.getProperty(unlessCondition) == null;

    }

}
