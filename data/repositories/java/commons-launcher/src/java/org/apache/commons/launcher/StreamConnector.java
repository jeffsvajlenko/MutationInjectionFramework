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

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
* A class for connecting an OutputStream to an InputStream.
*
* @author Patrick Luby
*/
public class StreamConnector extends Thread
{

    //------------------------------------------------------------------ Fields

    /**
     * Input stream to read from.
     */
    private InputStream is = null;

    /**
     * Output stream to write to.
     */
    private OutputStream os = null;

    //------------------------------------------------------------ Constructors

    /**
     * Specify the streams that this object will connect in the {@link #run()}
     * method.
     *
     * @param is the InputStream to read from.
     * @param os the OutputStream to write to.
     */
    public StreamConnector(InputStream is, OutputStream os)
    {

        this.is = is;
        this.os = os;

    }

    //----------------------------------------------------------------- Methods

    /**
     * Connect the InputStream and OutputStream objects specified in the
     * {@link #StreamConnector(InputStream, OutputStream)} constructor.
     */
    public void run()
    {

        // If the InputStream is null, don't do anything
        if (is == null)
            return;

        // Connect the streams until the InputStream is unreadable
        try
        {
            int bytesRead = 0;
            byte[] buf = new byte[4096];
            while ((bytesRead = is.read(buf)) != -1)
            {
                if (os != null && bytesRead > 0)
                {
                    os.write(buf, 0, bytesRead);
                    os.flush();
                }
                yield();
            }
        }
        catch (IOException e) {}

    }

}
