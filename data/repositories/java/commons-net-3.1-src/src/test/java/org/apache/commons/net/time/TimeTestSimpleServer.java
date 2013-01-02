package org.apache.commons.net.time;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The TimetSimpleServer class is a simple TCP implementation of a server
 * for the Time Protocol described in RFC 868.
 * <p>
 * Listens for TCP socket connections on the time protocol port and writes
 * the local time to socket outputStream as 32-bit integer of seconds
 * since midnight on 1 January 1900 GMT.
 * See <A HREF="ftp://ftp.rfc-editor.org/in-notes/rfc868.txt"> the spec </A> for
 * details.
 * <p>
 * Note this is for <B>debugging purposes only</B> and not meant to be run as a realiable time service.
 *
 * @author Jason Mathews, MITRE Corporation
 *
 * @version $Revision: 1230358 $ $Date: 2012-01-12 01:51:02 +0000 (Thu, 12 Jan 2012) $
 */
public class TimeTestSimpleServer implements Runnable
{

    /**
     * baseline time 1900-01-01T00:00:00 UTC
     */
    public static final long SECONDS_1900_TO_1970 = 2208988800L;

    /*** The default time port.  It is set to 37 according to RFC 868. ***/
    public static final int DEFAULT_PORT = 37;

    private ServerSocket server;
    private int port;
    private boolean running = false;

    /**
     * Default constructor for TimetSimpleServer.
     * Initializes port to defaul time port.
     */
    public TimeTestSimpleServer()
    {
        port = DEFAULT_PORT;
    }

    /**
     * Constructor for TimetSimpleServer given a specific port.
     */
    public TimeTestSimpleServer(int port)
    {
        this.port = port;
    }

    public void connect() throws IOException
    {
        if (server == null)
        {
            server = new ServerSocket(port);
        }
    }

    public int getPort()
    {
        return server == null ? port : server.getLocalPort();
    }

    public boolean isRunning()
    {
        return running;
    }

    /**
     * Start time service and provide time to client connections.
     * @throws IOException
     */
    public void start() throws IOException
    {
        if (server == null)
        {
            connect();
        }
        if (!running)
        {
            running = true;
            new Thread(this).start();
        }
    }

    public void run()
    {
        Socket socket = null;
        while (running)
        {
            try
            {
                socket = server.accept();
                DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                // add 500 ms to round off to nearest second
                int time = (int) ((System.currentTimeMillis() + 500) / 1000 + SECONDS_1900_TO_1970);
                os.writeInt(time);
                os.flush();
            }
            catch (IOException e)
            {
            }
            finally
            {
                if (socket != null)
                {
                    try
                    {
                        socket.close();  // force closing of the socket
                    }
                    catch (IOException e)
                    {
                        System.err.println("close socket error: " + e);
                    }
                }
            }
        }
    }

    /**
     * Close server socket.
     */
    public void stop()
    {
        running = false;
        if (server != null)
        {
            try
            {
                server.close();  // force closing of the socket
            }
            catch (IOException e)
            {
                System.err.println("close socket error: " + e);
            }
            server = null;
        }
    }

    public static void main(String[] args)
    {
        TimeTestSimpleServer server = new TimeTestSimpleServer();
        try
        {
            server.start();
        }
        catch (IOException e)
        {
        }
    }

}
