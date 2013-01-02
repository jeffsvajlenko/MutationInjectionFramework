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
import java.io.InputStream;
import java.io.IOException;

/**
 * A class for detecting if the parent JVM that launched this process has
 * terminated.
 *
 * @author Patrick Luby
 */
public class ParentListener extends Thread
{

    //------------------------------------------------------------------ Fields

    /**
     * Cached heartbeat file.
     */
    private File heartbeatFile = null;

    //------------------------------------------------------------ Constructors

    /**
     * Validates and caches a lock file created by the parent JVM.
     *
     * @param path the lock file that the parent JVM has an open
     *  FileOutputStream
     * @throws IOException if the heartbeat cannot be converted into a valid
     *  File object
     */
    public ParentListener(String path) throws IOException
    {

        if (path == null)
            throw new IOException();

        // Make sure we have a valid path
        heartbeatFile = new File(path);
        heartbeatFile.getCanonicalPath();

    }

    //----------------------------------------------------------------- Methods

    /**
     * Periodically check that the parent JVM has not terminated. On all
     * platforms other than Windows, this method will check that System.in has
     * not been closed. On Windows NT, 2000, and XP the lock file specified in
     * the {@link #ParentListener(String)} constructor is monitored as reading
     * System.in will block the entire process on Windows machines that use
     * some versions of Unix shells such as MKS, etc. No monitoring is done
     * on Window 95, 98, and ME.
     */
    public void run()
    {

        String osname = System.getProperty("os.name").toLowerCase();

        // We need to use file locking on Windows since reading System.in
        // will block the entire process on some Windows machines.
        if (osname.indexOf("windows") >= 0)
        {

            // Do nothing if this is a Windows 9x platform since our file
            // locking mechanism does not work on the early versions of
            // Windows
            if (osname.indexOf("nt") == -1 && osname.indexOf("2000") == -1 && osname.indexOf("xp") == -1)
                return;

            // If we can delete the heartbeatFile on Windows, it means that
            // the parent JVM has closed its FileOutputStream on the file.
            // Note that the parent JVM's stream should only be closed when
            // it exits.
            for ( ; ; )
            {
                if (heartbeatFile.delete())
                    break;
                // Wait awhile before we try again
                yield();
                try
                {
                    sleep(5000);
                }
                catch (Exception e) {}
            }

        }
        else
        {

            // Cache System.in in case the application redirects
            InputStream is = System.in;
            int bytesAvailable = 0;
            int bytesRead = 0;
            byte[] buf = new byte[1024];
            try
            {
                while (true)
                {
                    synchronized (is)
                    {
                        // Mark the stream position so that other threads can
                        // reread the strea
                        is.mark(buf.length);
                        // Read one more byte than has already been read to
                        // force the stream to wait for input
                        bytesAvailable = is.available();
                        if (bytesAvailable < buf.length)
                        {
                            bytesRead = is.read(buf, 0, bytesAvailable + 1);
                            // Reset so that we "unread" the bytes that we read
                            is.reset();
                            if (bytesRead == -1)
                                break;
                        }
                        else
                        {
                            // Make the buffer larger
                            if (buf.length < Integer.MAX_VALUE / 2)
                                buf = new byte[buf.length * 2];
                        }
                    }
                    yield();
                }
            }
            catch (IOException ioe) {}

        }

        // Clean up before exiting
        if (heartbeatFile != null)
            heartbeatFile.delete();

        // Exit this process since the parent JVM has exited
        System.exit(0);

    }

}
