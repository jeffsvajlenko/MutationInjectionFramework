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

import java.awt.Frame;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

/**
 * A wrapper class that invokes another class'
 * <code>main(String[])</code>. This particular class uses several system
 * properties to control features:
 * <ul>
 * <li>Redirecting System.out and System.err.
 * <li>Displaying a minimized window in the Windows taskbar.
 * </ul>
 * This class is normally not invoked directly. Instead, it is invoked by the
 * {@link LaunchTask} class.
 *
 * @author Patrick Luby
 */
public class ChildMain extends Thread
{

    //----------------------------------------------------------- Static Fields

    /**
     * The appendOutput system property name.
     */
    public final static String APPEND_OUTPUT_PROP_NAME =
        "org.apache.commons.launcher.appendOutput";

    /**
     * The displayMiminizedWindow system property name.
     */
    public final static String DISPLAY_MINIMIZED_WINDOW_PROP_NAME =
        "org.apache.commons.launcher.displayMinimizedWindow";

    /**
     * The disposeMiminizedWindow system property name.
     */
    public final static String DISPOSE_MINIMIZED_WINDOW_PROP_NAME =
        "org.apache.commons.launcher.disposeMinimizedWindow";

    /**
     * The executableName system property name.
     */
    public final static String EXECUTABLE_PROP_NAME =
        "org.apache.commons.launcher.executableName";

    /**
     * The heartbeatFile system property name.
     */
    public final static String HEARTBEAT_FILE_PROP_NAME =
        "org.apache.commons.launcher.heartbeatFile";

    /**
     * The miminizedWindowTitle system property name.
     */
    public final static String MINIMIZED_WINDOW_TITLE_PROP_NAME =
        "org.apache.commons.launcher.minimizedWindowTitle";

    /**
     * The miminizedWindowIcon system property name.
     */
    public final static String MINIMIZED_WINDOW_ICON_PROP_NAME=
        "org.apache.commons.launcher.minimizedWindowIcon";

    /**
     * The outputFile system property name.
     */
    public final static String OUTPUT_FILE_PROP_NAME =
        "org.apache.commons.launcher.outputFile";

    /**
     * The waitForChild system property name.
     */
    public final static String WAIT_FOR_CHILD_PROP_NAME =
        "org.apache.commons.launcher.waitForChild";

    //------------------------------------------------------------------ Fields

    /**
     * Cached command line arguments
     */
    private String[] args = null;

    //------------------------------------------------------------ Constructors

    /**
     * Construct an instance of this {@link Thread} subclass and cache the
     * args parameter for use by the {@link #run()} method.
     *
     * @param group the ThreadGroup to use for this thread
     * @param args the command line arguments
     */
    private ChildMain(ThreadGroup group, String[] args)
    {

        super(group, ChildMain.class.getName());
        this.args = args;

    }

    //---------------------------------------------------------- Static Methods

    /**
     * Main entry point for the child process. This method should only be
     * invoked by the {@link LaunchTask} class.
     *
     * @param args command line arguments
     */
    public static void main(String[] args)
    {

        // Invoke the target application in a separate thread so that we
        // caught any uncaught errors thrown by the target application
        Thread mainThread = new ChildMain(new ExitOnErrorThreadGroup(ChildMain.class.getName()), args);
        mainThread.start();

    }

    //----------------------------------------------------------------- Methods

    /**
     * Invoke the target application.
     *
     * @param args command line arguments
     */
    public void run()
    {

        // If there are no arguments, do nothing
        if (args == null || args.length == 0)
            return;

        // Invoke the target application
        try
        {

            // Start the thread to check if the parent JVM exits.
            boolean waitForChild = false;
            if (System.getProperty(ChildMain.WAIT_FOR_CHILD_PROP_NAME) != null)
            {
                waitForChild = true;
                String heartbeatFile = System.getProperty(ChildMain.HEARTBEAT_FILE_PROP_NAME);
                ParentListener heartbeat = new ParentListener(heartbeatFile);
                // Make the thread a daemon thread so that it does not
                // prevent this process from exiting when all of the
                // appliation's threads finish.
                heartbeat.setDaemon(true);
                heartbeat.start();
            }

            // If applicable, redirect output and error streams
            String outputPath = System.getProperty(ChildMain.OUTPUT_FILE_PROP_NAME);
            if (outputPath != null)
            {
                boolean appendOutput = false;
                if (System.getProperty(ChildMain.APPEND_OUTPUT_PROP_NAME) != null)
                    appendOutput = true;
                PrintStream ps = new PrintStream(new FileOutputStream(outputPath, appendOutput), true);
                System.setOut(ps);
                System.setErr(ps);
            }

            // The first argument should be the class that we really want to
            // invoke. Try to load the class and invoke its main(String[])
            // method with the first argument shifted out.
            Class mainClass = Class.forName(args[0]);
            Class[] paramTypes = new Class[1];
            Object[] paramValues = new Object[1];
            String[] params = new String[args.length - 1];
            // Shift args[0] out of the arguments
            for (int i = 0; i < params.length; i++)
                params[i] = args[i + 1];
            paramTypes[0] = params.getClass();
            paramValues[0] = params;

            // Create the icon window if this is a waitForChild task
            Frame frame = null;
            boolean displayMinimizedWindow = false;
            if (System.getProperty(ChildMain.DISPLAY_MINIMIZED_WINDOW_PROP_NAME) != null)
                displayMinimizedWindow = true;
            String osname = System.getProperty("os.name").toLowerCase();
            if (displayMinimizedWindow && osname.indexOf("windows") >= 0)
            {
                try
                {
                    frame = new Frame();
                    String title = System.getProperty(ChildMain.MINIMIZED_WINDOW_TITLE_PROP_NAME);
                    if (title != null)
                        frame.setTitle(title);
                    frame.setState(Frame.ICONIFIED);
                    String icon = System.getProperty(ChildMain.MINIMIZED_WINDOW_TITLE_PROP_NAME);
                    if (icon != null)
                    {
                        Image iconImage = Toolkit.getDefaultToolkit().createImage(icon);
                        if (iconImage != null)
                            frame.setIconImage(iconImage);
                    }

                    // Ensure that window always remains minimized
                    frame.addWindowListener(new ChildWindowAdapter());
                    Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
                    int width = (int)frame.getBounds().getWidth();
                    int height = frame.getInsets().top + frame.getInsets().bottom;
                    int x = (int)bounds.getWidth() - width;
                    int y = (int)bounds.getHeight() - height;
                    frame.setBounds(x, y, width, height);
                    frame.setResizable(false);
                    frame.setVisible(true);
                }
                catch(Exception fe) {}
            }

            // Invoke the main() method
            Method mainMethod = mainClass.getDeclaredMethod("main", paramTypes);
            mainMethod.invoke(null, paramValues);

            // Close the frame if it exists
            if (frame != null && System.getProperty(ChildMain.DISPOSE_MINIMIZED_WINDOW_PROP_NAME) != null)
            {
                // Exit this process. Closing or disposing of the window is not
                // enough to allow the process to exit.
                System.exit(0);
            }

        }
        catch (Throwable t)
        {
            String message = t.getMessage();
            t.printStackTrace();
            System.exit(1);
        }

    }

    /**
     * A WindowAdapter subclass that causes the application to exit when its
     * {@link #windowClosing(WindowEvent)} method is invoked.
     */
    private static class ChildWindowAdapter extends WindowAdapter
    {

        /**
         * Invoked when a window is in the process of being closed.
         *
         * @param e the event
         */
        public void windowClosing(WindowEvent e)
        {

            System.exit(0);

        }

    }

}
