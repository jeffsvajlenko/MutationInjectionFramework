/* PulseAudioTargetDataLine.java
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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioPermission;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.classpath.icedtea.pulseaudio.Debug.DebugLevel;

public final class PulseAudioTargetDataLine extends PulseAudioDataLine
    implements TargetDataLine
{

    /*
     * This contains the data from the PulseAudio buffer that has since been
     * dropped. If 20 bytes of a fragment of size 200 are read, the other 180
     * are dumped in this
     */
    private byte[] fragmentBuffer;

    /*
     * these are set to true only by the respective functions (flush(), drain())
     * set to false only by read()
     */
    private boolean flushed = false;
    private boolean drained = false;

    public static final String DEFAULT_TARGETDATALINE_NAME = "Audio Stream";

    PulseAudioTargetDataLine(AudioFormat[] formats, AudioFormat defaultFormat)
    {
        this.supportedFormats = formats;
        this.defaultFormat = defaultFormat;
        this.currentFormat = defaultFormat;
        this.streamName = DEFAULT_TARGETDATALINE_NAME;

    }

    @Override
    synchronized public void close()
    {
        if (!isOpen())
        {
            // Probably due to some programmer error, we are being
            // asked to close an already closed line.  Oh well.
            Debug.println(DebugLevel.Verbose,
                          "PulseAudioTargetDataLine.close(): "
                          + "Line closed that wasn't open.");
            return;
        }

        /* check for permission to record audio */
        AudioPermission perm = new AudioPermission("record", null);
        perm.checkGuard(null);

        PulseAudioMixer parentMixer = PulseAudioMixer.getInstance();
        parentMixer.removeTargetLine(this);

        super.close();

        Debug.println(DebugLevel.Verbose, "PulseAudioTargetDataLine.close(): "
                      + "Line closed");
    }

    @Override
    synchronized public void open(AudioFormat format, int bufferSize)
    throws LineUnavailableException
    {
        /* check for permission to record audio */
        AudioPermission perm = new AudioPermission("record", null);
        perm.checkGuard(null);

        if (isOpen())
        {
            throw new IllegalStateException("already open");
        }
        super.open(format, bufferSize);

        /* initialize all the member variables */
        framesSinceOpen = 0;
        fragmentBuffer = null;
        flushed = false;
        drained = false;

        /* add this open line to the mixer */
        PulseAudioMixer parentMixer = PulseAudioMixer.getInstance();
        parentMixer.addTargetLine(this);

        Debug.println(DebugLevel.Verbose, "PulseAudioTargetDataLine.open(): "
                      + "Line opened");
    }

    @Override
    synchronized public void open(AudioFormat format)
    throws LineUnavailableException
    {
        open(format, DEFAULT_BUFFER_SIZE);
    }

    @Override
    protected void connectLine(int bufferSize, Stream masterStream)
    throws LineUnavailableException
    {
        int fs = currentFormat.getFrameSize();
        float fr = currentFormat.getFrameRate();
        int bps = (int)(fs*fr); // bytes per second.

        // if 2 seconds' worth of data can fit in the buffer of the specified
        // size, we don't have to adjust the latency. Otherwise we do, so as
        // to avoid overruns.
        long flags = Stream.FLAG_START_CORKED;
        StreamBufferAttributes bufferAttributes;
        if (bps*2 < bufferSize)
        {
            // pulse audio completely ignores our fragmentSize attribute unless
            // ADJUST_LATENCY is set, so we just leave it at -1.
            bufferAttributes = new StreamBufferAttributes(bufferSize, -1, -1, -1, -1);
        }
        else
        {
            flags |= Stream.FLAG_ADJUST_LATENCY;
            // in this case, the pulse audio docs:
            // http://www.pulseaudio.org/wiki/LatencyControl
            // say every field (including bufferSize) must be initialized
            // to -1 except fragmentSize.
            // XXX: but in my tests, it just sets it to about 4MB, which
            // effectively makes it impossible to allocate a small buffer
            // and nothing bad happens (yet) when you don't set it to -1
            // so we just leave it at bufferSize.
            // XXX: the java api has no way to specify latency, which probably
            // means it should be as low as possible. Right now this method's
            // primary concern is avoiding dropouts, and if the user-provided
            // buffer size is large enough, we leave the latency up to pulse
            // audio (which sets it to something extremely high - about 2
            // seconds). We might want to always set a low latency.
            int fragmentSize = bufferSize/2;
            fragmentSize = Math.max((fragmentSize/fs)*fs, fs);
            bufferAttributes = new StreamBufferAttributes(bufferSize, -1, -1, -1, fragmentSize);
        }

        synchronized (eventLoop.threadLock)
        {
            stream.connectForRecording(Stream.DEFAULT_DEVICE, flags, bufferAttributes);
        }
    }

    @Override
    public int read(byte[] data, int offset, int length)
    {

        /* check state and inputs */

        if (!isOpen())
        {
            // A closed line can produce zero bytes of data.
            return 0;
        }

        int frameSize = currentFormat.getFrameSize();

        if (length % frameSize != 0)
        {
            throw new IllegalArgumentException(
                "amount of data to read does not represent an integral number of frames");
        }

        if (length < 0)
        {
            throw new IllegalArgumentException("length is negative");
        }

        if ( offset < 0 || offset > data.length - length)
        {
            throw new ArrayIndexOutOfBoundsException("array size: " + data.length
                    + " offset:" + offset + " length:" + length );
        }

        /* everything ok */

        int position = offset;
        int remainingLength = length;
        int sizeRead = 0;

        /* bytes read on each iteration of loop */
        int bytesRead;

        flushed = false;
        drained = false;

        /*
         * to read, we first take stuff from the fragmentBuffer
         */

        /* on first read() of the line, fragmentBuffer is null */
        synchronized (this)
        {
            if (fragmentBuffer != null)
            {
                boolean fragmentBufferSmaller = fragmentBuffer.length < length;
                int smallerBufferLength = Math.min(fragmentBuffer.length,
                                                   length);
                System.arraycopy(fragmentBuffer, 0, data, position,
                                 smallerBufferLength);
                framesSinceOpen += smallerBufferLength
                                   / currentFormat.getFrameSize();

                if (!fragmentBufferSmaller)
                {
                    /*
                     * if fragment was larger, then we already have all the data
                     * we need. clean up the buffer before returning. Make a new
                     * fragmentBuffer from the remaining bytes
                     */
                    int remainingBytesInFragment = (fragmentBuffer.length - length);
                    byte[] newFragmentBuffer = new byte[remainingBytesInFragment];
                    System.arraycopy(fragmentBuffer, length, newFragmentBuffer,
                                     0, newFragmentBuffer.length);
                    fragmentBuffer = newFragmentBuffer;
                    return length;
                }

                /* done with fragment buffer, remove it */
                bytesRead = smallerBufferLength;
                sizeRead += bytesRead;
                position += bytesRead;
                remainingLength -= bytesRead;
                fragmentBuffer = null;
            }
        }

        /*
         * if we need to read more data, then we read from PulseAudio's buffer
         */
        while (remainingLength != 0)
        {
            synchronized (this)
            {

                if (!isOpen() || !isStarted)
                {
                    return sizeRead;
                }

                if (flushed)
                {
                    flushed = false;
                    return sizeRead;
                }

                if (drained)
                {
                    drained = false;
                    return sizeRead;
                }

                byte[] currentFragment;
                synchronized (eventLoop.threadLock)
                {

                    /* read a fragment, and drop it from the server */
                    currentFragment = stream.peek();

                    stream.drop();
                    if (currentFragment == null)
                    {
                        Debug.println(DebugLevel.Verbose,
                                      "PulseAudioTargetDataLine.read(): "
                                      + " error in stream.peek()");
                        continue;
                    }

                    bytesRead = Math.min(currentFragment.length,
                                         remainingLength);

                    /*
                     * we read more than we required, save the rest of the data
                     * in the fragmentBuffer
                     */
                    if (bytesRead < currentFragment.length)
                    {
                        /* allocate a buffer to store unsaved data */
                        fragmentBuffer = new byte[currentFragment.length
                                                  - bytesRead];

                        /* copy over the unsaved data */
                        System.arraycopy(currentFragment, bytesRead,
                                         fragmentBuffer, 0, currentFragment.length
                                         - bytesRead);
                    }

                    System.arraycopy(currentFragment, 0, data, position,
                                     bytesRead);

                    sizeRead += bytesRead;
                    position += bytesRead;
                    remainingLength -= bytesRead;
                    framesSinceOpen += bytesRead / currentFormat.getFrameSize();
                }
            }
        }

        // all the data should have been played by now
        assert (sizeRead == length);

        return sizeRead;

    }

    @Override
    public void drain()
    {

        // blocks when there is data on the line
        // http://www.jsresources.org/faq_audio.html#stop_drain_tdl
        while (true)
        {
            synchronized (this)
            {
                if (!isStarted || !isOpen())
                {
                    break;
                }
            }
            try
            {
                //TODO: Is this the best length of sleep?
                //Maybe in case this loop runs for a long time
                //it would be good to switch to a longer
                //sleep.  Like bump it up each iteration after
                //the Nth iteration, up to a MAXSLEEP length.
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
                // do nothing
            }
        }

        synchronized (this)
        {
            drained = true;
        }
    }

    @Override
    public synchronized void flush()
    {
        if (isOpen())
        {

            /* flush the buffer on pulseaudio's side */
            Operation operation;
            synchronized (eventLoop.threadLock)
            {
                operation = stream.flush();
            }
            operation.waitForCompletion();
            operation.releaseReference();
        }

        flushed = true;
        /* flush the partial fragment we stored */
        fragmentBuffer = null;
    }

    @Override
    public int available()
    {
        if (!isOpen())
        {
            // a closed line has 0 bytes available.
            return 0;
        }

        synchronized (eventLoop.threadLock)
        {
            return stream.getReableSize();
        }
    }

    @Override
    public int getFramePosition()
    {
        return (int) framesSinceOpen;
    }

    @Override
    public long getLongFramePosition()
    {
        return framesSinceOpen;
    }

    @Override
    public long getMicrosecondPosition()
    {
        return (long) (framesSinceOpen / currentFormat.getFrameRate());
    }

    /*
     * A TargetData starts when we ask it to and continues playing until we ask
     * it to stop. There are no buffer underruns/overflows or anything so we
     * will just fire the LineEvents manually
     */

    @Override
    synchronized public void start()
    {
        super.start();

        fireLineEvent(new LineEvent(this, LineEvent.Type.START, framesSinceOpen));
    }

    @Override
    synchronized public void stop()
    {
        super.stop();

        fireLineEvent(new LineEvent(this, LineEvent.Type.STOP, framesSinceOpen));
    }

    @Override
    public Line.Info getLineInfo()
    {
        return new DataLine.Info(TargetDataLine.class, supportedFormats,
                                 StreamBufferAttributes.MIN_VALUE,
                                 StreamBufferAttributes.MAX_VALUE);
    }

}
