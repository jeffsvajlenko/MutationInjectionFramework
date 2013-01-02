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
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ResourceBundle;

import org.apache.commons.launcher.types.ArgumentSet;
import org.apache.commons.launcher.types.JVMArgumentSet;
import org.apache.commons.launcher.types.SysPropertySet;
import org.apache.tools.ant.Main;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.taskdefs.Ant;
import org.apache.tools.ant.taskdefs.Available;
import org.apache.tools.ant.taskdefs.CallTarget;
import org.apache.tools.ant.taskdefs.ConditionTask;
import org.apache.tools.ant.taskdefs.Exit;
import org.apache.tools.ant.taskdefs.Property;
import org.apache.tools.ant.taskdefs.Mkdir;
import org.apache.tools.ant.taskdefs.Copy;
import org.apache.tools.ant.taskdefs.Delete;
import org.apache.tools.ant.types.Description;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.PatternSet;

/**
 * A class that is used to launch a Java process. The primary purpose of this
 * class is to eliminate the need for a batch or shell script to launch a Java
 * process. Some situations where elimination of a batch or shell script may be
 * desirable are:
 * <ul>
 * <li>You want to avoid having to determining where certain application paths
 *  are e.g. your application's home directory, etc. Determining this
 *  dynamically in a Windows batch scripts is very tricky on some versions of
 *  Windows or when softlinks are used on Unix platforms.
 * <li>You need to enforce certain properties e.g. java.endorsed.dirs when
 *  running with JDK 1.4.
 * <li>You want to allow users to pass in custom JVM arguments or system
 *  properties without having to parse and reorder arguments in your script.
 *  This can be tricky and/or messy in batch and shell scripts.
 * <li>You want to bootstrap Java properties from a configuration file instead
 *  hard-coding them in your batch and shell scripts.
 * <li>You want to provide localized error messages which is very tricky to do
 *  in batch and shell scripts.
 * </ul>
 *
 * @author Patrick Luby
 */
public class Launcher implements Runnable
{

    //----------------------------------------------------------- Static Fields


    /**
     * Cached bootstrap file.
     */
    private static File bootstrapFile = null;

    /**
     * Cached java command
     */
    private static String javaCmd = null;

    /**
     * Cached JDB command
     */
    private static String jdbCmd = null;

    /**
     * Default XML file name
     */
    private final static String DEFAULT_XML_FILE_NAME = "launcher.xml";

    /**
     * Shared lock.
     */
    private static Object lock = new Object();

    /**
     * Cached log
     */
    private static PrintStream log = System.err;

    /**
     * Cached resourceBundle
     */
    private static ResourceBundle resourceBundle = null;

    /**
     * The started status flag.
     */
    private static boolean started = false;

    /**
     * The stopped status flag.
     */
    private static boolean stopped = false;

    /**
     * List of supported Ant tasks.
     */
    public final static Object[] SUPPORTED_ANT_TASKS = new Object[]
    {
        LaunchTask.TASK_NAME, LaunchTask.class,
        "ant", Ant.class,
        "antcall", CallTarget.class,
        "available", Available.class,
        "condition", ConditionTask.class,
        "fail", Exit.class,
        "property", Property.class,
        "mkdir", Mkdir.class,
        "delete", Delete.class,
        "copy", Copy.class
    };

    /**
     * List of supported Ant types.
     */
    public final static Object[] SUPPORTED_ANT_TYPES = new Object[]
    {
        ArgumentSet.TYPE_NAME, ArgumentSet.class,
        JVMArgumentSet.TYPE_NAME, JVMArgumentSet.class,
        SysPropertySet.TYPE_NAME, SysPropertySet.class,
        "description", Description.class,
        "fileset", FileSet.class,
        "filelist", FileList.class,
        "path", Path.class,
        "patternset", PatternSet.class
    };

    /**
     * Cached tools classpath.
     */
    private static String toolsClasspath = null;

    /**
     * The verbose flag
     */
    private static boolean verbose = false;

    //---------------------------------------------------------- Static Methods


    /**
     * Get the started flag.
     *
     * @return the value of the started flag
     */
    public static synchronized boolean isStarted()
    {

        return Launcher.started;

    }

    /**
     * Get the stopped flag.
     *
     * @return the value of the stopped flag
     */
    public static synchronized boolean isStopped()
    {

        return Launcher.stopped;

    }

    /**
     * Start the launching process. This method is essential the
     * <code>main(String[])<code> method for this class except that this method
     * never invokes {@link System#exit(int)}. This method is designed for
     * applications that wish to invoke this class directly from within their
     * application's code.
     *
     * @param args command line arguments
     * @return the exit value of the last synchronous child JVM that was
     *  launched or 1 if any other error occurs
     * @throws IllegalArgumentException if any error parsing the args parameter
     *  occurs
     */
    public static int start(String[] args) throws IllegalArgumentException
    {

        // Check make sure that neither this method or the stop() method is
        // already running since we do not support concurrency
        synchronized (Launcher.lock)
        {
            if (Launcher.isStarted() || Launcher.isStopped())
                return 1;
            Launcher.setStarted(true);
        }

        int returnValue = 0;
        ClassLoader parentLoader = null;
        Thread shutdownHook = new Thread(new Launcher());
        Runtime runtime = Runtime.getRuntime();

        try
        {

            // Cache the current class loader for this thread and set the class
            // loader before running Ant. Note that we only set the class loader
            // if we are running a Java version earlier than 1.4 as on 1.4 this
            // causes unnecessary loading of the XML parser classes.
            parentLoader = Thread.currentThread().getContextClassLoader();
            boolean lessThan14 = true;
            try
            {
                Class.forName("java.lang.CharSequence");
                lessThan14 = false;
            }
            catch (ClassNotFoundException cnfe)
            {
                // If this class does not exist, then we are not running Java 1.4
            }
            if (lessThan14)
                Thread.currentThread().setContextClassLoader(Launcher.class.getClassLoader());

            Project project = new Project();

            // Set the project's class loader
            project.setCoreLoader(Launcher.class.getClassLoader());

            // Initialize the project. Note that we don't invoke the
            // Project.init() method directly as this will cause all of
            // the myriad of Task subclasses to load which is a big
            // performance hit. Instead, we load only the
            // Launcher.SUPPORTED_ANT_TASKS and Launcher.SUPPORTED_ANT_TYPES
            // into the project that the Launcher supports.
            for (int i = 0; i < Launcher.SUPPORTED_ANT_TASKS.length; i++)
            {
                // The even numbered elements should be the task name
                String taskName = (String)Launcher.SUPPORTED_ANT_TASKS[i];
                // The odd numbered elements should be the task class
                Class taskClass = (Class)Launcher.SUPPORTED_ANT_TASKS[++i];
                project.addTaskDefinition(taskName, taskClass);
            }
            for (int i = 0; i < Launcher.SUPPORTED_ANT_TYPES.length; i++)
            {
                // The even numbered elements should be the type name
                String typeName = (String)Launcher.SUPPORTED_ANT_TYPES[i];
                // The odd numbered elements should be the type class
                Class typeClass = (Class)Launcher.SUPPORTED_ANT_TYPES[++i];
                project.addDataTypeDefinition(typeName, typeClass);
            }

            // Add all system properties as project properties
            project.setSystemProperties();

            // Parse the arguments
            int currentArg = 0;

            // Set default XML file
            File launchFile = new File(Launcher.getBootstrapDir(), Launcher.DEFAULT_XML_FILE_NAME);

            // Get standard launcher arguments
            for ( ; currentArg < args.length; currentArg++)
            {
                // If we find a "-" argument or an argument without a
                // leading "-", there are no more standard launcher arguments
                if ("-".equals(args[currentArg]))
                {
                    currentArg++;
                    break;
                }
                else if (args[currentArg].length() > 0 && !"-".equals(args[currentArg].substring(0, 1)))
                {
                    break;
                }
                else if ("-help".equals(args[currentArg]))
                {
                    throw new IllegalArgumentException();
                }
                else if ("-launchfile".equals(args[currentArg]))
                {
                    if (currentArg + 1 < args.length)
                    {
                        String fileArg = args[++currentArg];
                        launchFile = new File(fileArg);
                        if (!launchFile.isAbsolute())
                            launchFile = new File(Launcher.getBootstrapDir(), fileArg);
                    }
                    else
                    {
                        throw new IllegalArgumentException(args[currentArg] + " " + Launcher.getLocalizedString("missing.arg"));
                    }
                }
                else if ("-executablename".equals(args[currentArg]))
                {
                    if (currentArg + 1 < args.length)
                        System.setProperty(ChildMain.EXECUTABLE_PROP_NAME, args[++currentArg]);
                    else
                        throw new IllegalArgumentException(args[currentArg] + " " + Launcher.getLocalizedString("missing.arg"));
                }
                else if ("-verbose".equals(args[currentArg]))
                {
                    Launcher.setVerbose(true);
                }
                else
                {
                    throw new IllegalArgumentException(args[currentArg] + " " + Launcher.getLocalizedString("invalid.arg"));
                }
            }

            // Get target
            String target = null;
            if (currentArg < args.length)
                target = args[currentArg++];
            else
                throw new IllegalArgumentException(Launcher.getLocalizedString("missing.target"));

            // Get user properties
            for ( ; currentArg < args.length; currentArg++)
            {
                // If we don't find any more "-" or "-D" arguments, there are no
                // more user properties
                if ("-".equals(args[currentArg]))
                {
                    currentArg++;
                    break;
                }
                else if (args[currentArg].length() <= 2 || !"-D".equals(args[currentArg].substring(0, 2)))
                {
                    break;
                }
                int delimiter = args[currentArg].indexOf('=', 2);
                String key = null;
                String value = null;
                if (delimiter >= 2)
                {
                    key = args[currentArg].substring(2, delimiter);
                    value = args[currentArg].substring(delimiter + 1);
                }
                else
                {
                    // Unfortunately, MS-DOS batch scripts will split an
                    // "-Dname=value" argument into "-Dname" and "value"
                    // arguments. So, we need to assume that the next
                    // argument is the property value unless it appears
                    // to be a different type of argument.
                    key = args[currentArg].substring(2);
                    if (currentArg + 1 < args.length &&
                            !"-D".equals(args[currentArg + 1].substring(0, 2)))
                    {
                        value = args[++currentArg];
                    }
                    else
                    {
                        value = "";
                    }
                }
                project.setUserProperty(key, value);
            }

            // Treat all remaining arguments as application arguments
            String[] appArgs = new String[args.length - currentArg];
            for (int i = 0; i < appArgs.length; i++)
            {
                appArgs[i] = args[i + currentArg];
                project.setUserProperty(LaunchTask.ARG_PROP_NAME + Integer.toString(i), appArgs[i]);
            }

            // Set standard Ant user properties
            project.setUserProperty("ant.version", Main.getAntVersion());
            project.setUserProperty("ant.file", launchFile.getCanonicalPath());
            project.setUserProperty("ant.java.version", System.getProperty("java.specification.version"));

            // Set the buildfile
            ProjectHelper.configureProject(project, launchFile);

            // Check that the target exists
            if (!project.getTargets().containsKey(target))
                throw new IllegalArgumentException(target + " " + Launcher.getLocalizedString("invalid.target"));

            // Execute the target
            try
            {
                runtime.addShutdownHook(shutdownHook);
            }
            catch (NoSuchMethodError nsme)
            {
                // Early JVMs do not support this method
            }
            project.executeTarget(target);

        }
        catch (Throwable t)
        {
            // Log any errors
            returnValue = 1;
            String message = t.getMessage();
            if (t instanceof IllegalArgumentException)
            {
                Launcher.error(message, true);
            }
            else
            {
                if (Launcher.verbose)
                    Launcher.error(t);
                else
                    Launcher.error(message, false);
            }
        }
        finally
        {
            synchronized (Launcher.lock)
            {
                // Remove the shutdown hook
                try
                {
                    runtime.removeShutdownHook(shutdownHook);
                }
                catch (NoSuchMethodError nsme)
                {
                    // Early JVMs do not support this method
                }
                // Reset the class loader after running Ant
                Thread.currentThread().setContextClassLoader(parentLoader);
                // Reset stopped flag
                Launcher.setStarted(false);
                // Notify the stop() method that we have set the class loader
                Launcher.lock.notifyAll();
            }
        }

        // Override return value with exit value of last synchronous child JVM
        Process[] childProcesses = LaunchTask.getChildProcesses();
        if (childProcesses.length > 0)
            returnValue = childProcesses[childProcesses.length - 1].exitValue();

        return returnValue;

    }

    /**
     * Interrupt the {@link #start(String[])} method. This is done
     * by forcing the current or next scheduled invocation of the
     * {@link LaunchTask#execute()} method to throw an exception. In addition,
     * this method will terminate any synchronous child processes that any
     * instances of the {@link LaunchTask} class have launched. Note, however,
     * that this method will <b>not</b> terminate any asynchronous child
     * processes that have been launched. Accordingly, applications that use
     * this method are encouraged to always set the LaunchTask.TASK_NAME task's
     * "waitForChild" attribute to "true" to ensure that the
     * application that you want to control can be terminated via this method.
     * After this method has been executed, it will not return until is safe to
     * execute the {@link #start(String[])} method.
     *
     * @return true if this method completed without error and false if an
     *  error occurred or the launch process is already stopped
     */
    public static boolean stop()
    {

        synchronized (Launcher.lock)
        {
            // Check the stopped flag to avoid concurrent execution of this
            // method
            if (Launcher.isStopped())
                return false;

            // Make sure that the start() method is running. If not, just
            // return as there is nothing to do.
            if (Launcher.isStarted())
                Launcher.setStopped(true);
            else
                return false;
        }

        boolean returnValue = true;

        try
        {

            // Kill all of the synchronous child processes
            killChildProcesses();

            // Wait for the start() method to reset the start flag
            synchronized (Launcher.lock)
            {
                if (Launcher.isStarted())
                    Launcher.lock.wait();
            }

            // Make sure that the start() method has really finished
            if (Launcher.isStarted())
                returnValue = true;

        }
        catch (Throwable t)
        {
            // Log any errors
            returnValue = false;
            String message = t.getMessage();
            if (Launcher.verbose)
                Launcher.error(t);
            else
                Launcher.error(message, false);
        }
        finally
        {
            // Reset stopped flag
            Launcher.setStopped(false);
        }

        return returnValue;

    }

    /**
     * Print a detailed error message and exit.
     *
     * @param message the message to be printed
     * @param usage if true, print a usage statement after the message
     */
    public static void error(String message, boolean usage)
    {

        if (message != null)
            Launcher.getLog().println(Launcher.getLocalizedString("error") + ": " + message);
        if (usage)
            Launcher.getLog().println(Launcher.getLocalizedString("usage"));

    }

    /**
     * Print a detailed error message and exit.
     *
     * @param message the exception whose stack trace is to be printed.
     */
    public static void error(Throwable t)
    {

        String message = t.getMessage();
        if (!Launcher.verbose && message != null)
            Launcher.getLog().println(Launcher.getLocalizedString("error") + ": " + message);
        else
            t.printStackTrace(Launcher.getLog());

    }

    /**
     * Get the canonical directory of the class or jar file that this class was
     * loaded. This method can be used to calculate the root directory of an
     * installation.
     *
     * @return the canonical directory of the class or jar file that this class
     *  file was loaded from
     * @throws IOException if the canonical directory or jar file
     *  cannot be found
     */
    public static File getBootstrapDir() throws IOException
    {

        File file = Launcher.getBootstrapFile();
        if (file.isDirectory())
            return file;
        else
            return file.getParentFile();

    }

    /**
     * Get the canonical directory or jar file that this class was loaded
     * from.
     *
     * @return the canonical directory or jar file that this class
     *  file was loaded from
     * @throws IOException if the canonical directory or jar file
     *  cannot be found
     */
    public static File getBootstrapFile() throws IOException
    {

        if (bootstrapFile == null)
        {

            // Get a URL for where this class was loaded from
            String classResourceName = "/" + Launcher.class.getName().replace('.', '/') + ".class";
            URL resource = Launcher.class.getResource(classResourceName);
            if (resource == null)
                throw new IOException(Launcher.getLocalizedString("bootstrap.file.not.found") + ": " + Launcher.class.getName());
            String resourcePath = null;
            String embeddedClassName = null;
            boolean isJar = false;
            String protocol = resource.getProtocol();
            if ((protocol != null) &&
                    (protocol.indexOf("jar") >= 0))
            {
                isJar = true;
            }
            if (isJar)
            {
                resourcePath = URLDecoder.decode(resource.getFile());
                embeddedClassName = "!" + classResourceName;
            }
            else
            {
                resourcePath = URLDecoder.decode(resource.toExternalForm());
                embeddedClassName = classResourceName;
            }
            int sep = resourcePath.lastIndexOf(embeddedClassName);
            if (sep >= 0)
                resourcePath = resourcePath.substring(0, sep);

            // Now that we have a URL, make sure that it is a "file" URL
            // as we need to coerce the URL into a File object
            if (resourcePath.indexOf("file:") == 0)
                resourcePath = resourcePath.substring(5);
            else
                throw new IOException(Launcher.getLocalizedString("bootstrap.file.not.found") + ": " + Launcher.class.getName());

            // Coerce the URL into a file and check that it exists. Note that
            // the JVM <code>File(String)</code> constructor automatically
            // flips all '/' characters to '\' on Windows and there are no
            // valid escape characters so we sould not have to worry about
            // URL encoded slashes.
            File file = new File(resourcePath);
            if (!file.exists() || !file.canRead())
                throw new IOException(Launcher.getLocalizedString("bootstrap.file.not.found") + ": " + Launcher.class.getName());
            bootstrapFile = file.getCanonicalFile();

        }

        return bootstrapFile;

    }

    /**
     * Get the full path of the Java command to execute.
     *
     * @return a string suitable for executing a child JVM
     */
    public static synchronized String getJavaCommand()
    {

        if (javaCmd == null)
        {

            String osname = System.getProperty("os.name").toLowerCase();
            String commandName = null;
            if (osname.indexOf("windows") >= 0)
            {
                // Always use javaw.exe on Windows so that we aren't bound to an
                // MS-DOS window
                commandName = "javaw.exe";
            }
            else
            {
                commandName = "java";
            }
            javaCmd = System.getProperty("java.home") + File.separator + "bin" + File.separator + commandName;

        }

        return javaCmd;

    }

    /**
     * Get the full path of the JDB command to execute.
     *
     * @return a string suitable for executing a child JDB debugger
     */
    public static synchronized String getJDBCommand()
    {

        if (jdbCmd == null)
        {

            String osname = System.getProperty("os.name").toLowerCase();
            String commandName = null;
            if (osname.indexOf("windows") >= 0)
                commandName = "jdb.exe";
            else
                commandName = "jdb";
            jdbCmd = new File(System.getProperty("java.home")).getParent() + File.separator + "bin" + File.separator + commandName;

        }

        return jdbCmd;

    }

    /**
     * Get the PrintStream that all output should printed to. The default
     * PrintStream returned in System.err.
     *
     * @return the PrintStream instance to print output to
     */
    public static synchronized PrintStream getLog()
    {

        return Launcher.log;

    }

    /**
     * Set the classpath to the current JVM's tools classes.
     *
     * @return a string suitable for use as a JVM's -classpath argument
     * @throws IOException if the tools classes cannot be found
     */
    public static synchronized String getToolsClasspath() throws IOException
    {

        if (toolsClasspath == null)
        {

            File javaHome = null;
            javaHome = new File(System.getProperty("java.home")).getCanonicalFile();
            Class clazz = null;
            String[] toolsPaths = new String[2];
            toolsPaths[0] = javaHome.getParent() + File.separator +
                            "lib" + File.separator + "tools.jar";
            toolsPaths[1] = javaHome.getPath() + File.separator +
                            "lib" + File.separator + "tools.jar";
            File toolsFile = null;
            for (int i = 0; i < toolsPaths.length; i++)
            {
                ClassLoader loader = ClassLoader.getSystemClassLoader();
                toolsFile = new File(toolsPaths[i]);
                // Check if the jar file exists and is readable
                if (!toolsFile.isFile() || !toolsFile.canRead())
                    toolsFile = null;
                if (toolsFile != null)
                {
                    try
                    {
                        URL toolsURL = toolsFile.toURL();
                        loader = new URLClassLoader(new URL[] {toolsURL}, loader);
                    }
                    catch (Exception e)
                    {
                        toolsFile = null;
                    }
                }
                // Try to load the javac class just to be sure. Note that we
                // use the system class loader if the file does not exist to
                // handle cases like Mac OS X where the tools.jar classes are
                // loaded by the bootstrap class loader.
                try
                {
                    clazz = loader.loadClass("sun.tools.javac.Main");
                    if (clazz != null)
                        break;
                }
                catch (Exception e) {}
            }

            if (clazz == null)
                throw new IOException(Launcher.getLocalizedString("sdk.tools.not.found"));

            // Save classpath.
            if (toolsFile != null)
                toolsClasspath = toolsFile.getPath();
            else
                toolsClasspath = "";

        }

        return toolsClasspath;

    }

    /**
     * Get a localized property. This method will search for localized
     * properties and will resolve ${...} style macros in the localized string.
     *
     * @param key the localized property to retrieve
     * @return the localized and resolved property value
     */
    public static String getLocalizedString(String key)
    {

        return Launcher.getLocalizedString(key, Launcher.class.getName());

    }

    /**
     * Get a localized property. This method will search for localized
     * properties and will resolve ${...} style macros in the localized string.
     *
     * @param key the localized property to retrieve
     * @param className the name of the class to retrieve the property for
     * @return the localized and resolved property value
     */
    public static String getLocalizedString(String key, String className)
    {

        try
        {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(className);
            return Launcher.resolveString(resourceBundle.getString(key));
        }
        catch (Exception e)
        {
            // We should at least make it clear that the property is not
            // defined in the properties file
            return "<" + key + " property>";
        }

    }

    /**
     * Resolve ${...} style macros in strings. This method will replace any
     * embedded ${...} strings in the specified unresolved parameter with the
     * value of the system property in the enclosed braces. Note that any '$'
     * characters can be escaped by putting '$$' in the specified parameter.
     * In additional, the following special macros will be resolved:
     * <ul>
     * <li><code>${launcher.executable.name}</code> will be substituted with the
     * value of the "org.apache.commons.launcher.executableName" system
     * property, the "-executablename" command line argument, or, if both of
     * those are undefined, with the absolute path to the Java executable plus
     * its classpath and main class name arguments
     * <li><code>${launcher.bootstrap.file}</code> will get substituted with
     * the value returned by {@link #getBootstrapFile()}
     * <li><code>${launcher.bootstrap.dir}</code> will get substituted with
     * the value returned by {@link #getBootstrapDir()}
     *
     * @param unresolved the string to be resolved
     * @return the resolved String
     * @throws IOException if any error occurs
     */
    private static String resolveString(String unresolved) throws IOException
    {

        if (unresolved == null)
            return null;

        // Substitute system property strings
        StringBuffer buf = new StringBuffer();
        int tokenEnd = 0;
        int tokenStart = 0;
        char token = '$';
        boolean escapeChar = false;
        boolean firstToken = true;
        boolean lastToken = false;

        while (!lastToken)
        {

            tokenEnd = unresolved.indexOf(token, tokenStart);

            // Determine if this is the first token
            if (firstToken)
            {
                firstToken = false;
                // Skip if first token is zero length
                if (tokenEnd - tokenStart == 0)
                {
                    tokenStart = ++tokenEnd;
                    continue;
                }
            }
            // Determine if this is the last token
            if (tokenEnd < 0)
            {
                lastToken = true;
                tokenEnd = unresolved.length();
            }

            if (escapeChar)
            {

                // Don't parse the string
                buf.append(token + unresolved.substring(tokenStart, tokenEnd));
                escapeChar = !escapeChar;

            }
            else
            {

                // Parse the string
                int openProp = unresolved.indexOf('{', tokenStart);
                int closeProp = unresolved.indexOf('}', tokenStart + 1);
                String prop = null;

                // We must have a '{' in the first character and a closing
                // '}' after that
                if (openProp != tokenStart ||
                        closeProp < tokenStart + 1 ||
                        closeProp >= tokenEnd)
                {
                    buf.append(unresolved.substring(tokenStart, tokenEnd));
                }
                else
                {
                    // Property found
                    String propName = unresolved.substring(tokenStart + 1, closeProp);
                    if ("launcher.executable.name".equals(propName))
                    {
                        prop = System.getProperty(ChildMain.EXECUTABLE_PROP_NAME);
                        if (prop != null)
                        {
                            // Quote the property
                            prop = "\"" + prop + "\"";
                        }
                        else
                        {
                            // Set property to fully quoted Java command line
                            String classpath = Launcher.getBootstrapFile().getPath();
                            prop = "\"" + System.getProperty("java.home") + File.separator + "bin" + File.separator + "java\" -classpath \"" + classpath + "\" LauncherBootstrap";
                        }
                    }
                    else if ("launcher.bootstrap.file".equals(propName))
                    {
                        prop = Launcher.getBootstrapFile().getPath();
                    }
                    else if ("launcher.bootstrap.dir".equals(propName))
                    {
                        prop = Launcher.getBootstrapDir().getPath();
                    }
                    else
                    {
                        prop = System.getProperty(unresolved.substring(tokenStart + 1, closeProp));
                    }
                    if (prop == null)
                        prop = "";
                    buf.append(prop + unresolved.substring(++closeProp, tokenEnd));
                }

            }

            // If this is a blank token, then the next starts with the
            // token character. So, treat this token as an escape
            // character for the next token.
            if (tokenEnd - tokenStart == 0)
                escapeChar = !escapeChar;

            tokenStart = ++tokenEnd;

        }

        return buf.toString();

    }

    /**
     * Set the PrintStream that all output should printed to.
     *
     * @param a PrintStream instance to print output to
     */
    public static synchronized void setLog(PrintStream log)
    {

        if (log != null)
            Launcher.log = log;
        else
            Launcher.log = System.err;

    }

    /**
     * Set the started flag.
     *
     * @param started the value of the started flag
     */
    private static synchronized void setStarted(boolean started)
    {

        Launcher.started = started;

    }

    /**
     * Set the stopped flag.
     *
     * @param stopped the value of the stopped flag
     */
    private static synchronized void setStopped(boolean stopped)
    {

        Launcher.stopped = stopped;

    }

    /**
     * Set the verbose flag.
     *
     * @param verbose the value of the verbose flag
     */
    public static synchronized void setVerbose(boolean verbose)
    {

        Launcher.verbose = verbose;

    }

    /**
     * Iterate through the list of synchronous child process launched by
     * all of the {@link LaunchTask} instances.
     */
    public static void killChildProcesses()
    {

        Process[] procs = LaunchTask.getChildProcesses();
        for (int i = 0; i < procs.length; i++)
            procs[i].destroy();

    }

    //----------------------------------------------------------------- Methods

    /**
     * Wrapper to allow the {@link #killChildProcesses()} method to be
     * invoked in a shutdown hook.
     */
    public void run()
    {

        Launcher.killChildProcesses();

    }

}
