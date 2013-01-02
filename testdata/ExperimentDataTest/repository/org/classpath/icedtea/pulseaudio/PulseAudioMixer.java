/* PulseAudioMixer.java
   Copyright (C) 2008 Red Hat, Inc.

This file is part of IcedTea.

IcedTea is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License as published by
the Free Software Foundation, version 2.

IcedTea is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with IcedTea; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version.
 */

package org.classpath.icedtea.pulseaudio;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioPermission;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Control;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.Control.Type;

import org.classpath.icedtea.pulseaudio.Debug.DebugLevel;

public final class PulseAudioMixer implements Mixer
{
    // singleton

    private Thread eventLoopThread;

    private List<Line.Info> sourceLineInfos = new ArrayList<Line.Info>();
    private List<Line.Info> staticSourceLineInfos = new ArrayList<Line.Info>();

    private List<Line.Info> targetLineInfos = new ArrayList<Line.Info>();
    private List<Line.Info> staticTargetLineInfos = new ArrayList<Line.Info>();

    private static PulseAudioMixer _instance = null;

    private static final String DEFAULT_APP_NAME = "Java";
    static final String PULSEAUDIO_FORMAT_KEY = "PulseAudioFormatKey";

    private boolean isOpen = false;

    private final List<PulseAudioLine> sourceLines = new ArrayList<PulseAudioLine>();
    private final List<PulseAudioLine> targetLines = new ArrayList<PulseAudioLine>();

    private final List<LineListener> lineListeners = new ArrayList<LineListener>();

    private PulseAudioMixer()
    {

        Debug.println(DebugLevel.Verbose, "PulseAudioMixer.PulseAudioMixer(): "
                      + "Contructing PulseAudioMixer...");

        AudioFormat[] formats = getSupportedFormats();

        staticSourceLineInfos.add(new DataLine.Info(SourceDataLine.class,
                                  formats, StreamBufferAttributes.MIN_VALUE,
                                  StreamBufferAttributes.MAX_VALUE));
        staticSourceLineInfos.add(new DataLine.Info(Clip.class, formats,
                                  StreamBufferAttributes.MIN_VALUE,
                                  StreamBufferAttributes.MAX_VALUE));

        staticTargetLineInfos.add(new DataLine.Info(TargetDataLine.class,
                                  formats, StreamBufferAttributes.MIN_VALUE,
                                  StreamBufferAttributes.MAX_VALUE));

        refreshSourceAndTargetLines();

        Debug.println(DebugLevel.Verbose, "PulseAudioMixer.PulseAudioMixer(): "
                      + "Finished constructing PulseAudioMixer");

    }

    synchronized public static PulseAudioMixer getInstance()
    {
        if (_instance == null)
        {
            _instance = new PulseAudioMixer();
        }
        return _instance;
    }

    private AudioFormat[] getSupportedFormats()
    {

        List<AudioFormat> supportedFormats = new ArrayList<AudioFormat>();

        Map<String, Object> properties;

        /*
         * frameSize = sample size (in bytes, not bits) x # of channels
         *
         * From PulseAudio's sources
         * http://git.0pointer.de/?p=pulseaudio.git;a=blob
         * ;f=src/pulse/sample.c;h=93da2465f4301e27af4976e82737c3a048124a68;hb=
         * 82ea8dde8abc51165a781c69bc3b38034d62d969#l63
         */

        /*
         * technically, PulseAudio supports up to 16 channels, but things get
         * interesting with channel maps
         *
         * PA_CHANNEL_MAP_DEFAULT (=PA_CHANNEL_MAP_AIFF) supports 1,2,3,4,5 or 6
         * channels only
         */
        int[] channelSizes = new int[] { 1, 2, 3, 4, 5, 6 };

        for (int channelSize : channelSizes)
        {
            properties = new HashMap<String, Object>();
            properties.put(PULSEAUDIO_FORMAT_KEY, "PA_SAMPLE_ALAW");

            int sampleSize = 8;
            final AudioFormat PA_SAMPLE_ALAW = new AudioFormat(Encoding.ALAW, // encoding
                    AudioSystem.NOT_SPECIFIED, // sample rate
                    sampleSize, // sample size
                    channelSize, // channels
                    sampleSize / 8 * channelSize, // frame size
                    AudioSystem.NOT_SPECIFIED, // frame rate
                    false, // big endian?
                    properties);

            supportedFormats.add(PA_SAMPLE_ALAW);
        }

        for (int channelSize : channelSizes)
        {
            properties = new HashMap<String, Object>();
            properties.put(PULSEAUDIO_FORMAT_KEY, "PA_SAMPLE_ULAW");

            int sampleSize = 8;
            final AudioFormat PA_SAMPLE_ULAW = new AudioFormat(Encoding.ULAW, // encoding
                    AudioSystem.NOT_SPECIFIED, // sample rate
                    sampleSize, // sample size
                    channelSize, // channels
                    sampleSize / 8 * channelSize, // frame size
                    AudioSystem.NOT_SPECIFIED, // frame rate
                    false, // big endian?
                    properties);

            supportedFormats.add(PA_SAMPLE_ULAW);
        }

        for (int channelSize : channelSizes)
        {
            properties = new HashMap<String, Object>();
            properties.put(PULSEAUDIO_FORMAT_KEY, "PA_SAMPLE_S16BE");

            int sampleSize = 16;
            final AudioFormat PA_SAMPLE_S16BE = new AudioFormat(
                Encoding.PCM_SIGNED, // encoding
                AudioSystem.NOT_SPECIFIED, // sample rate
                sampleSize, // sample size
                channelSize, // channels
                sampleSize / 8 * channelSize, // frame size
                AudioSystem.NOT_SPECIFIED, // frame rate
                true, // big endian?
                properties);

            supportedFormats.add(PA_SAMPLE_S16BE);
        }

        for (int channelSize : channelSizes)
        {
            properties = new HashMap<String, Object>();
            properties.put(PULSEAUDIO_FORMAT_KEY, "PA_SAMPLE_S16LE");

            int sampleSize = 16;
            final AudioFormat A_SAMPLE_S16LE = new AudioFormat(
                Encoding.PCM_SIGNED, // encoding
                AudioSystem.NOT_SPECIFIED, // sample rate
                sampleSize, // sample size
                channelSize, // channels
                sampleSize / 8 * channelSize, // frame size
                AudioSystem.NOT_SPECIFIED, // frame rate
                false, // big endian?
                properties);

            supportedFormats.add(A_SAMPLE_S16LE);
        }

        for (int channelSize : channelSizes)
        {
            properties = new HashMap<String, Object>();
            properties.put(PULSEAUDIO_FORMAT_KEY, "PA_SAMPLE_S32BE");

            int sampleSize = 32;
            final AudioFormat PA_SAMPLE_S32BE = new AudioFormat(
                Encoding.PCM_SIGNED, // encoding
                AudioSystem.NOT_SPECIFIED, // sample rate
                sampleSize, // sample size
                channelSize, // channels
                sampleSize / 8 * channelSize, // frame size
                AudioSystem.NOT_SPECIFIED, // frame rate
                true, // big endian?
                properties);

            supportedFormats.add(PA_SAMPLE_S32BE);
        }

        for (int channelSize : channelSizes)
        {
            properties = new HashMap<String, Object>();
            properties.put(PULSEAUDIO_FORMAT_KEY, "PA_SAMPLE_S32LE");

            int sampleSize = 32;
            final AudioFormat PA_SAMPLE_S32LE = new AudioFormat(
                Encoding.PCM_SIGNED, // encoding
                AudioSystem.NOT_SPECIFIED, // sample rate
                sampleSize, // sample size
                channelSize, // channels
                sampleSize / 8 * channelSize, // frame size
                AudioSystem.NOT_SPECIFIED, // frame rate
                false, // big endian?
                properties);

            supportedFormats.add(PA_SAMPLE_S32LE);
        }

        for (int channelSize : channelSizes)
        {
            properties = new HashMap<String, Object>();
            properties.put(PULSEAUDIO_FORMAT_KEY, "PA_SAMPLE_U8");

            int sampleSize = 8; // in bits
            AudioFormat PA_SAMPLE_U8 = new AudioFormat(Encoding.PCM_UNSIGNED, // encoding
                    AudioSystem.NOT_SPECIFIED, // sample rate
                    sampleSize, // sample size
                    channelSize, // channels
                    sampleSize / 8 * channelSize, // frame size in bytes
                    AudioSystem.NOT_SPECIFIED, // frame rate
                    false, // big endian?
                    properties);

            supportedFormats.add(PA_SAMPLE_U8);
        }

        return supportedFormats.toArray(new AudioFormat[0]);
    }

    @Override
    public Line getLine(Line.Info info) throws LineUnavailableException
    {

        if (!isLineSupported(info))
        {
            throw new IllegalArgumentException("Line unsupported: " + info);
        }

        AudioFormat[] formats = null;
        AudioFormat defaultFormat = null;

        if (DataLine.Info.class.isInstance(info))
        {
            ArrayList<AudioFormat> formatList = new ArrayList<AudioFormat>();
            AudioFormat[] requestedFormats = ((DataLine.Info) info)
                                             .getFormats();
            for (int i = 0; i < requestedFormats.length; i++)
            {
                AudioFormat f1 = requestedFormats[i];
                for (AudioFormat f2 : getSupportedFormats())
                {

                    if (f1.matches(f2))
                    {
                        formatList.add(f2);
                        defaultFormat = f1;
                    }
                }
            }
            formats = formatList.toArray(new AudioFormat[0]);

        }
        else
        {
            formats = getSupportedFormats();
            defaultFormat = new AudioFormat(Encoding.PCM_UNSIGNED, 44100, 8, 2,
                                            2, AudioSystem.NOT_SPECIFIED, false);
        }

        if ((info.getLineClass() == SourceDataLine.class))
        {
            return new PulseAudioSourceDataLine(formats, defaultFormat);
        }

        if ((info.getLineClass() == TargetDataLine.class))
        {
            /* check for permission to record audio */
            AudioPermission perm = new AudioPermission("record", null);
            perm.checkGuard(null);

            return new PulseAudioTargetDataLine(formats, defaultFormat);
        }

        if ((info.getLineClass() == Clip.class))
        {
            return new PulseAudioClip(formats, defaultFormat);
        }

        if (Port.Info.class.isInstance(info))
        {
            Port.Info portInfo = (Port.Info) info;
            if (portInfo.isSource())
            {
                /* check for permission to record audio */
                AudioPermission perm = new AudioPermission("record", null);
                perm.checkGuard(null);

                return new PulseAudioSourcePort(portInfo.getName());
            }
            else
            {
                return new PulseAudioTargetPort(portInfo.getName());
            }
        }

        Debug.println(DebugLevel.Info, "PulseAudioMixer.getLine(): "
                      + "No matching line supported by PulseAudio");

        throw new IllegalArgumentException("No matching lines found");

    }

    @Override
    public int getMaxLines(Line.Info info)
    {
        /*
         * PulseAudio supports (theoretically) unlimited number of streams for
         * supported formats
         */
        if (isLineSupported(info))
        {
            return AudioSystem.NOT_SPECIFIED;
        }

        return 0;
    }

    @Override
    public Info getMixerInfo()
    {
        return PulseAudioMixerInfo.getInfo();
    }

    public Line.Info[] getSourceLineInfo()
    {
        return sourceLineInfos.toArray(new Line.Info[0]);
    }

    @Override
    public Line.Info[] getSourceLineInfo(Line.Info info)
    {
        ArrayList<Line.Info> infos = new ArrayList<Line.Info>();

        for (Line.Info supportedInfo : sourceLineInfos)
        {
            if (info.matches(supportedInfo))
            {
                infos.add(supportedInfo);
            }
        }
        return infos.toArray(new Line.Info[0]);
    }

    @Override
    public Line[] getSourceLines()
    {
        return sourceLines.toArray(new Line[0]);

    }

    @Override
    public Line.Info[] getTargetLineInfo()
    {
        return targetLineInfos.toArray(new Line.Info[0]);
    }

    @Override
    public Line.Info[] getTargetLineInfo(Line.Info info)
    {
        ArrayList<Line.Info> infos = new ArrayList<Line.Info>();

        for (Line.Info supportedInfo : targetLineInfos)
        {
            if (info.matches(supportedInfo))
            {
                infos.add(supportedInfo);
            }
        }
        return infos.toArray(new Line.Info[0]);
    }

    @Override
    public Line[] getTargetLines()
    {

        /* check for permission to record audio */
        AudioPermission perm = new AudioPermission("record", null);
        perm.checkGuard(null);

        return (Line[]) targetLines.toArray(new Line[0]);
    }

    @Override
    public boolean isLineSupported(Line.Info info)
    {
        if (info != null)
        {
            for (Line.Info myInfo : sourceLineInfos)
            {
                if (info.matches(myInfo))
                {
                    return true;
                }
            }

            for (Line.Info myInfo : targetLineInfos)
            {
                if (info.matches(myInfo))
                {
                    return true;
                }
            }

        }
        return false;

    }

    @Override
    public boolean isSynchronizationSupported(Line[] lines, boolean maintainSync)
    {

        return false;
    }

    @Override
    public void synchronize(Line[] lines, boolean maintainSync)
    {

        throw new IllegalArgumentException(
            "Mixer does not support synchronizing lines");

        // Line masterStream = null;
        // for (Line line : lines) {
        // if (line.isOpen()) {
        // masterStream = line;
        // break;
        // }
        // }
        // if (masterStream == null) {
        // // for now, can't synchronize lines if none of them is open (no
        // // stream pointer to pass)
        // // will see what to do about this later
        // throw new IllegalArgumentException();
        // }
        //
        // try {
        //
        // for (Line line : lines) {
        // if (line != masterStream) {
        //
        // ((PulseAudioDataLine) line)
        // .reconnectforSynchronization(((PulseAudioDataLine) masterStream)
        // .getStream());
        //
        // }
        // }
        // } catch (LineUnavailableException e) {
        // // we couldn't reconnect, so tell the user we failed by throwing an
        // // exception
        // throw new IllegalArgumentException(e);
        // }

    }

    @Override
    public void unsynchronize(Line[] lines)
    {
        // FIXME should be able to implement this
        throw new IllegalArgumentException();
    }

    @Override
    public void addLineListener(LineListener listener)
    {
        lineListeners.add(listener);
    }

    @Override
    synchronized public void close()
    {

        /*
         * only allow the mixer to be controlled if either playback or recording
         * is allowed
         */

        if (!this.isOpen)
        {
            throw new IllegalStateException("Mixer is not open; cant close");
        }

        List<Line> linesToClose = new LinkedList<Line>();
        linesToClose.addAll(sourceLines);
        if (sourceLines.size() > 0)
        {

            Debug.println(DebugLevel.Warning, "PulseAudioMixer.close(): "
                          + linesToClose.size()
                          + " source lines were not closed. closing them now.");

            linesToClose.addAll(sourceLines);
            for (Line line : linesToClose)
            {
                if (line.isOpen())
                {
                    line.close();
                }
            }
        }
        linesToClose.clear();

        if (targetLines.size() > 0)
        {
            Debug.println(DebugLevel.Warning, "PulseAudioMixer.close(): "
                          + linesToClose.size()
                          + " target lines have not been closed");

            linesToClose.addAll(targetLines);
            for (Line line : linesToClose)
            {
                if (line.isOpen())
                {
                    line.close();
                }
            }
        }

        synchronized (lineListeners)
        {
            lineListeners.clear();
        }

        eventLoopThread.interrupt();

        try
        {
            eventLoopThread.join();
        }
        catch (InterruptedException e)
        {
            System.out.println(this.getClass().getName()
                               + ": interrupted while waiting for eventloop to finish");
        }

        isOpen = false;

        refreshSourceAndTargetLines();

        Debug.println(DebugLevel.Verbose, "PulseAudioMixer.close(): "
                      + "Mixer closed");

    }

    @Override
    public Control getControl(Type control)
    {
        // mixer supports no controls
        throw new IllegalArgumentException();
    }

    @Override
    public Control[] getControls()
    {
        // mixer supports no controls; return an array of length 0
        return new Control[] {};
    }

    @Override
    public javax.sound.sampled.Line.Info getLineInfo()
    {
        // System.out.println("DEBUG: PulseAudioMixer.getLineInfo() called");
        return new Line.Info(PulseAudioMixer.class);
    }

    @Override
    public boolean isControlSupported(Type control)
    {
        // mixer supports no controls
        return false;
    }

    @Override
    public boolean isOpen()
    {
        return isOpen;
    }

    @Override
    public void open() throws LineUnavailableException
    {
        openLocal();

    }

    public void openLocal() throws LineUnavailableException
    {
        openLocal(DEFAULT_APP_NAME);
    }

    public void openLocal(String appName) throws LineUnavailableException
    {
        openImpl(appName, null);
    }

    public void openRemote(String appName, String host)
    throws UnknownHostException, LineUnavailableException
    {
        if (host == null)
        {
            throw new NullPointerException("hostname");
        }

        final int PULSEAUDIO_DEFAULT_PORT = 4713;

        /*
         * If trying to connect to a remote machine, check for permissions
         */
        SecurityManager sm = System.getSecurityManager();
        if (sm != null)
        {
            sm.checkConnect(host,PULSEAUDIO_DEFAULT_PORT );
        }

        openImpl(appName, host);
    }

    public void openRemote(String appName, String host, int port)
    throws UnknownHostException, LineUnavailableException
    {

        if ((port < 1) && (port != -1))
        {
            throw new IllegalArgumentException("Invalid value for port");
        }

        if (host == null)
        {
            throw new NullPointerException("hostname");
        }

        /*
         * If trying to connect to a remote machine, check for permissions
         */
        SecurityManager sm = System.getSecurityManager();
        if (sm != null)
        {
            sm.checkConnect(host, port);
        }

        InetAddress addr = InetAddress.getAllByName(host)[0];

        host = addr.getHostAddress();
        host = host + ":" + String.valueOf(port);

        openImpl(appName, host);

    }

    /*
     *
     * @param appName name of the application
     *
     * @param hostAndIp a string consisting of the host and ip address of the
     * server to connect to. Format: "<host>:<ip>". Set to null to indicate a
     * local connection
     */
    synchronized private void openImpl(String appName, String hostAndIp)
    throws LineUnavailableException
    {

        if (isOpen)
        {
            throw new IllegalStateException("Mixer is already open");
        }

        EventLoop eventLoop;
        eventLoop = EventLoop.getEventLoop();
        eventLoop.setAppName(appName);
        eventLoop.setServer(hostAndIp);

        ContextListener generalEventListener = new ContextListener()
        {
            @Override
            public void update(ContextEvent e)
            {
                if (e.getType() == ContextEvent.READY)
                {
                    fireEvent(new LineEvent(PulseAudioMixer.this,
                                            LineEvent.Type.OPEN, AudioSystem.NOT_SPECIFIED));
                }
                else if (e.getType() == ContextEvent.FAILED
                         || e.getType() == ContextEvent.TERMINATED)
                {
                    fireEvent(new LineEvent(PulseAudioMixer.this,
                                            LineEvent.Type.CLOSE, AudioSystem.NOT_SPECIFIED));
                }
            }
        };

        eventLoop.addContextListener(generalEventListener);

        final Semaphore ready = new Semaphore(0);

        ContextListener initListener = new ContextListener()
        {

            @Override
            public void update(ContextEvent e)
            {
                if (e.getType() == ContextEvent.READY
                        || e.getType() == ContextEvent.FAILED
                        || e.getType() == ContextEvent.TERMINATED)
                {
                    ready.release();
                }
            }

        };

        eventLoop.addContextListener(initListener);

        eventLoopThread = new Thread(eventLoop, "PulseAudio Eventloop Thread");

        /*
         * Make the thread exit if by some weird error it is the only thread
         * running. The application should be able to exit if the main thread
         * doesn't or can't (perhaps an assert?) do a mixer.close().
         */
        eventLoopThread.setDaemon(true);
        eventLoopThread.start();

        try
        {
            // System.out.println("waiting...");
            ready.acquire();
            if (eventLoop.getStatus() != ContextEvent.READY)
            {
                /*
                 * when exiting, wait for the thread to end otherwise we get one
                 * thread that inits the singleton with new data and the old
                 * thread then cleans up the singleton asserts fail all over the
                 * place
                 */
                eventLoop.removeContextListener(initListener);
                eventLoopThread.interrupt();
                eventLoopThread.join();
                throw new LineUnavailableException();
            }
            eventLoop.removeContextListener(initListener);
            // System.out.println("got signal");
        }
        catch (InterruptedException e)
        {
            System.out.println("PulseAudioMixer: got interrupted while waiting for the EventLoop to initialize");
        }

        // System.out.println(this.getClass().getName() + ": ready");

        this.isOpen = true;

        // sourceLineInfo and targetLineInfo need to be updated with
        // port infos, which can only be obtained after EventLoop had started

        refreshSourceAndTargetLines();

        for (String portName : eventLoop.updateSourcePortNameList())
        {
            sourceLineInfos.add(new Port.Info(Port.class, portName, true));
        }

        for (String portName : eventLoop.updateTargetPortNameList())
        {
            targetLineInfos.add(new Port.Info(Port.class, portName, false));
        }

        Debug.println(DebugLevel.Debug, "PulseAudioMixer.open(): "
                      + "Mixer opened");

    }

    @Override
    public void removeLineListener(LineListener listener)
    {
        lineListeners.remove(listener);
    }

    /*
     * Should this method be synchronized? I had a few reasons, but i forgot
     * them Pros: - Thread safety?
     *
     * Cons: - eventListeners are run from other threads, if those then call
     * fireEvent while a method is waiting on a listener, this synchronized
     * block wont be entered: deadlock!
     */
    private void fireEvent(final LineEvent e)
    {
        synchronized (lineListeners)
        {
            for (LineListener lineListener : lineListeners)
            {
                lineListener.update(e);
            }
        }
    }

    void addSourceLine(PulseAudioLine line)
    {
        sourceLines.add(line);
    }

    void removeSourceLine(PulseAudioLine line)
    {
        sourceLines.remove(line);
    }

    void addTargetLine(PulseAudioLine line)
    {
        targetLines.add(line);
    }

    void removeTargetLine(PulseAudioLine line)
    {
        targetLines.remove(line);
    }

    void refreshSourceAndTargetLines()
    {

        sourceLineInfos.clear();
        targetLineInfos.clear();

        sourceLineInfos.addAll(staticSourceLineInfos);

        targetLineInfos.addAll(staticTargetLineInfos);

    }

}
