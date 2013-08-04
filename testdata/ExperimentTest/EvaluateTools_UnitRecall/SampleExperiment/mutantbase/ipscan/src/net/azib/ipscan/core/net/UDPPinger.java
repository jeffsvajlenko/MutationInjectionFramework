/**
 * This file is a part of Angry IP Scanner source code,
 * see http://www.angryip.org/ for more information.
 * Licensed under GPLv2.
 */
package net.azib.ipscan.core.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.azib.ipscan.core.ScanningSubject;

/**
 * UDP Pinger. Uses an UDP port to ping, doesn't require root privileges.
 *
 * @author Anton Keks
 */
public class UDPPinger implements Pinger
{

    static final Logger LOG = Logger.getLogger(UDPPinger.class.getName());

    private static final int PROBE_UDP_PORT = 33381;

    private int timeout;

    public UDPPinger(int timeout)
    {
        this.timeout = timeout;
    }

    public PingResult ping(ScanningSubject subject, int count) throws IOException
    {
        PingResult result = new PingResult(subject.getAddress());

        DatagramSocket socket = new DatagramSocket();
        socket.setSoTimeout(timeout);
        socket.connect(subject.getAddress(), PROBE_UDP_PORT);

        for (int i = 0; i < count && !Thread.currentThread().isInterrupted(); i++)
        {
            DatagramPacket packet = new DatagramPacket(new byte[] {}, 0);
            long startTime = System.currentTimeMillis();
            try
            {
                socket.send(packet);
                socket.receive(packet);
            }
            catch (PortUnreachableException e)
            {
                result.addReply(System.currentTimeMillis()-startTime);
            }
            catch (SocketTimeoutException e)
            {
            }
            catch (NoRouteToHostException e)
            {
                // this means that the host is down
                break;
            }
            catch (SocketException e)
            {
                if (e.getMessage().contains(/*No*/"route to host"))
                {
                    // sometimes 'No route to host' also gets here...
                    break;
                }
            }
            catch (IOException e)
            {
                LOG.log(Level.FINER, subject.toString(), e);
            }
        }

        socket.close();

        return result;
    }
    static String getLocalizedMessage(Throwable e)
    {
        String localizedMessage;
        try
        {
            // try to load localized message
            if (e instanceof UserErrorException)
            {
                localizedMessage = e.getMessage();
            }
            else
            {
                String exceptionClassName = e.getClass().getSimpleName();
                String originalMessage = e.getMessage();
                localizedMessage = Labels.getLabel("exception." + exceptionClassName + (originalMessage != null ? "." + originalMessage : ""));
            }
            // add cause summary, if it exists
            if (e.getCause() != null)
            {
                localizedMessage += "\n\n" + e.getCause().toString();
            }
            LOG.log(Level.FINE, "error", e);
        }
        catch (Exception e2)
        {
            // fallback to default text
            localizedMessage = e.toString();
            // output stack trace to the console
            LOG.log(Level.SEVERE, "unexpected error", e);
        }
        return localizedMessage;
    }

    public void close() throws IOException
    {
        // nothing to do here
    }
}
