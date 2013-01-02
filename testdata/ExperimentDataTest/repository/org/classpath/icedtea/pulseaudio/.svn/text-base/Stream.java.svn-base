/* PulseAudioClip.java
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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.sound.sampled.LineUnavailableException;

/**
 *
 * This class encapsulates a pa_stream object and provides easier access to the
 * native functions
 *
 *
 * for more details on this see the pa_stream_* functions in the pulseaudio api
 * docs
 *
 */
final class Stream
{

    public interface StateListener
    {
        public void update();
    }

    public interface CorkListener
    {
        public void update();
    }

    public interface WriteListener
    {
        public void update();
    }

    public interface ReadListener
    {
        public void update();
    }

    public interface OverflowListener
    {
        public void update();
    }

    public interface UnderflowListener
    {
        public void update();
    }

    public interface PlaybackStartedListener
    {
        public void update();
    }

    public interface LatencyUpdateListener
    {
        public void update();
    }

    public interface MovedListener
    {
        public void update();
    }

    public interface UpdateTimingInfoListener
    {
        public void update();
    }

    public interface SuspendedListener
    {
        public void update();
    }

    // see comments in ContextEvent.java and Operation.java
    // These are the possible stream states.
    public static long STATE_UNCONNECTED = -1,
                       STATE_CREATING    = -1,
                       STATE_READY       = -1,
                       STATE_FAILED      = -1,
                       STATE_TERMINATED  = -1;

    // Throw an IllegalStateException if value is not one of the possible
    // states. Otherwise return the input.
    public static long checkNativeStreamState(long value)
    {
        if (!Arrays.asList(STATE_UNCONNECTED, STATE_CREATING,
                           STATE_READY, STATE_FAILED, STATE_TERMINATED
                          ).contains(value))
        {
            throw new IllegalStateException("Illegal constant for ContextEvent: " + value);
        }
        return value;
    }

    // These are stream flags.
    public static long FLAG_NOFLAGS                   = -1,
                       FLAG_START_CORKED              = -1,
                       FLAG_INTERPOLATE_TIMING        = -1,
                       FLAG_NOT_MONOTONIC             = -1,
                       FLAG_AUTO_TIMING_UPDATE        = -1,
                       FLAG_NO_REMAP_CHANNELS         = -1,
                       FLAG_NO_REMIX_CHANNELS         = -1,
                       FLAG_FIX_FORMAT                = -1,
                       FLAG_FIX_RATE                  = -1,
                       FLAG_FIX_CHANNELS              = -1,
                       FLAG_DONT_MOVE                 = -1,
                       FLAG_VARIABLE_RATE             = -1,
                       FLAG_PEAK_DETECT               = -1,
                       FLAG_START_MUTED               = -1,
                       FLAG_ADJUST_LATENCY            = -1,
                       FLAG_EARLY_REQUESTS            = -1,
                       FLAG_DONT_INHIBIT_AUTO_SUSPEND = -1,
                       FLAG_START_UNMUTED             = -1,
                       FLAG_FAIL_ON_SUSPEND           = -1;

    private static native void init_constants();

    // We don't change this to static longs like we did with all other pulse
    // audio enums mirrored in java because we never use the pulse audio
    // integer value of formats on the java side. In java, the handling of
    // formats is strictly symbolic and string based. For that, enums are
    // better.
    public static enum Format
    {
        PA_SAMPLE_U8,
        PA_SAMPLE_ULAW,
        PA_SAMPLE_ALAW,
        PA_SAMPLE_S16LE,
        PA_SAMPLE_S16BE,
        PA_SAMPLE_FLOAT32LE,
        PA_SAMPLE_FLOAT32BE,
        PA_SAMPLE_S32LE,
        PA_SAMPLE_S32BE
    }

    public static final String DEFAULT_DEVICE = null;

    private byte[] streamPointer;

    static
    {
        SecurityWrapper.loadNativeLibrary();
        init_constants();
    }

    private Format format;
    private float cachedVolume;

    private StreamBufferAttributes bufAttr = new StreamBufferAttributes(0,0,0,0,0);
    private static final Object bufAttrMutex = new Object();

    private List<StateListener> stateListeners;
    private List<WriteListener> writeListeners;
    private List<ReadListener> readListeners;
    private List<OverflowListener> overflowListeners;
    private List<UnderflowListener> underflowListeners;
    private List<PlaybackStartedListener> playbackStartedListeners;
    private List<LatencyUpdateListener> latencyUpdateListeners;
    private List<MovedListener> movedListeners;
    private List<SuspendedListener> suspendedListeners;
    private List<CorkListener> corkListeners;

    private native void native_pa_stream_new(byte[] contextPointer,
            String name, String format, int sampleRate, int channels);

    private native void native_pa_stream_unref();

    private native long native_pa_stream_get_state();

    private native byte[] native_pa_stream_get_context();

    private native int native_pa_stream_get_index();

    private native int native_pa_stream_get_device_index();

    private native String native_pa_stream_get_device_name();

    private native int native_pa_stream_is_suspended();

    private native int native_pa_stream_connect_playback(String name,
            int bufferMaxLength, int bufferTargetLength,
            int bufferPreBuffering, int bufferMinimumRequest,
            int bufferFragmentSize, long flags, byte[] volumePointer,
            byte[] sync_streamPointer);

    private native int native_pa_stream_connect_record(String name,
            int bufferMaxLength, int bufferTargetLength,
            int bufferPreBuffering, int bufferMinimumRequest,
            int bufferFragmentSize, long flags, byte[] volumePointer,
            byte[] sync_streamPointer);

    private native int native_pa_stream_disconnect();

    private native int native_pa_stream_write(byte[] data, int offset,
            int length);

    private native byte[] native_pa_stream_peek();

    private native int native_pa_stream_drop();

    private native int native_pa_stream_writable_size();

    private native int native_pa_stream_readable_size();

    private native byte[] native_pa_stream_drain();

    private native byte[] native_pa_stream_updateTimingInfo();

    public native int bytesInBuffer();

    /*
     * pa_operation pa_stream_update_timing_info (pa_streamp,
     * pa_stream_success_cb_t cb, voiduserdata) Request a timing info structure
     * update for a stream.
     */

    private native int native_pa_stream_is_corked();

    private native byte[] native_pa_stream_cork(int b);

    private native byte[] native_pa_stream_flush();

    /*
     * pa_operation pa_stream_prebuf (pa_streams, pa_stream_success_cb_t cb,
     * voiduserdata) Reenable prebuffering as specified in the pa_buffer_attr
     * structure.
     */

    private native byte[] native_pa_stream_trigger();

    /* returns an operationPointer */
    private native byte[] native_pa_stream_set_name(String name);

    /* Return the current playback/recording time */
    private native long native_pa_stream_get_time();

    /* Return the total stream latency */
    private native long native_pa_stream_get_latency();

    /*
     * const pa_timing_info pa_stream_get_timing_info (pa_streams) Return the
     * latest raw timing data structure.
     */

    private native StreamSampleSpecification native_pa_stream_get_sample_spec();

    /*
     * const pa_channel_map pa_stream_get_channel_map (pa_streams) Return a
     * pointer to the stream's channel map. const
     */
    private native StreamBufferAttributes native_pa_stream_get_buffer_attr();

    private native byte[] native_pa_stream_set_buffer_attr(
        StreamBufferAttributes info);

    private native byte[] native_pa_stream_update_sample_rate(int rate);

    native byte[] native_set_volume(float newValue);

    native byte[] native_update_volume();

    /*
     * pa_operation pa_stream_proplist_update (pa_streams, pa_update_mode_t
     * mode, pa_proplistp, pa_stream_success_cb_t cb, voiduserdata) Update the
     * property list of the sink input/source output of this stream, adding new
     * entries. pa_operation pa_stream_proplist_remove (pa_streams, const char
     * const keys[], pa_stream_success_cb_t cb, voiduserdata) Update the
     * property list of the sink input/source output of this stream, remove
     * entries. int pa_stream_set_monitor_stream (pa_streams, uint32_t
     * sink_input_idx) For record streams connected to a monitor source: monitor
     * only a very specific sink input of the sink. uint32_t
     * pa_stream_get_monitor_stream (pa_streams) Return what has been set with
     * pa_stream_set_monitor_stream() ebfore.
     */

    Stream(byte[] contextPointer, String name, Format format, int sampleRate,
           int channels)
    {
        // System.out.println("format: " + format.toString());

        stateListeners = new LinkedList<StateListener>();
        writeListeners = new LinkedList<WriteListener>();
        readListeners = new LinkedList<ReadListener>();
        overflowListeners = new LinkedList<OverflowListener>();
        underflowListeners = new LinkedList<UnderflowListener>();
        playbackStartedListeners = new LinkedList<PlaybackStartedListener>();
        latencyUpdateListeners = new LinkedList<LatencyUpdateListener>();
        movedListeners = new LinkedList<MovedListener>();
        suspendedListeners = new LinkedList<SuspendedListener>();
        corkListeners = new LinkedList<CorkListener>();
        this.format = format;

        StreamSampleSpecification spec = new StreamSampleSpecification(format,
                sampleRate, channels);

        native_pa_stream_new(contextPointer, name, spec.getFormat().toString(),
                             spec.getRate(), spec.getChannels());
    }

    void addStateListener(StateListener listener)
    {
        synchronized (stateListeners)
        {
            stateListeners.add(listener);
        }
    }

    void removeStateListener(StateListener listener)
    {
        synchronized (stateListeners)
        {
            stateListeners.remove(listener);
        }

    }

    void addWriteListener(WriteListener listener)
    {
        synchronized (writeListeners)
        {
            writeListeners.add(listener);
        }
    }

    void removeWriteListener(WriteListener listener)
    {
        synchronized (writeListeners)
        {
            writeListeners.remove(listener);
        }
    }

    void addReadListener(ReadListener listener)
    {
        synchronized (readListeners)
        {
            readListeners.add(listener);
        }
    }

    void removeReadListener(ReadListener listener)
    {
        synchronized (readListeners)
        {
            readListeners.remove(listener);
        }
    }

    void addOverflowListener(OverflowListener listener)
    {
        synchronized (overflowListeners)
        {
            overflowListeners.add(listener);
        }
    }

    void removeOverflowListener(OverflowListener listener)
    {
        synchronized (overflowListeners)
        {
            overflowListeners.remove(listener);
        }
    }

    void addUnderflowListener(UnderflowListener listener)
    {
        synchronized (underflowListeners)
        {
            underflowListeners.add(listener);
        }
    }

    void removeUnderflowListener(UnderflowListener listener)
    {
        synchronized (underflowListeners)
        {
            underflowListeners.remove(listener);
        }
    }

    void addCorkListener(CorkListener listener)
    {
        synchronized (corkListeners)
        {
            corkListeners.add(listener);
        }
    }

    void removeCorkListener(CorkListener listener)
    {
        synchronized (corkListeners)
        {
            corkListeners.remove(listener);
        }
    }

    void addPlaybackStartedListener(PlaybackStartedListener listener)
    {
        synchronized (playbackStartedListeners)
        {
            playbackStartedListeners.add(listener);
        }
    }

    void removePlaybackStartedListener(PlaybackStartedListener listener)
    {
        synchronized (playbackStartedListeners)
        {
            playbackStartedListeners.remove(listener);
        }
    }

    void addLatencyUpdateListener(LatencyUpdateListener listener)
    {
        synchronized (latencyUpdateListeners)
        {
            latencyUpdateListeners.add(listener);
        }
    }

    void removeLatencyUpdateListener(LatencyUpdateListener listener)
    {
        synchronized (playbackStartedListeners)
        {
            latencyUpdateListeners.remove(listener);
        }
    }

    void addMovedListener(MovedListener listener)
    {
        synchronized (movedListeners)
        {
            movedListeners.add(listener);
        }
    }

    void removeMovedListener(MovedListener listener)
    {
        synchronized (movedListeners)
        {
            movedListeners.remove(listener);
        }
    }

    void addSuspendedListener(SuspendedListener listener)
    {
        synchronized (suspendedListeners)
        {
            suspendedListeners.add(listener);
        }
    }

    void removeSuspendedListener(SuspendedListener listener)
    {
        synchronized (suspendedListeners)
        {
            suspendedListeners.remove(listener);
        }
    }

    long getState()
    {
        return checkNativeStreamState(native_pa_stream_get_state());
    }

    byte[] getContextPointer()
    {
        return native_pa_stream_get_context();
    }

    int getSinkInputIndex()
    {
        return native_pa_stream_get_index();
    }

    /**
     *
     * @return the index of the sink or source this stream is connected to in
     *         the server
     */
    int getDeviceIndex()
    {
        return native_pa_stream_get_device_index();
    }

    private void setBufAttr()
    {
        synchronized(bufAttrMutex)
        {
            bufAttr = native_pa_stream_get_buffer_attr();
        }
    }

    @SuppressWarnings("unused")
    private void bufferAttrCallback()
    {
        setBufAttr();
    }

    int getBufferSize()
    {
        synchronized (bufAttrMutex)
        {
            return bufAttr.getMaxLength();
        }
    }

    /**
     *
     * @return the name of the sink or source this stream is connected to in the
     *         server
     */
    String getDeviceName()
    {
        return native_pa_stream_get_device_name();
    }

    /**
     * if the sink or source this stream is connected to has been suspended.
     *
     * @return
     */
    boolean isSuspended()
    {
        return (native_pa_stream_is_suspended() != 0);
    }

    /**
     * Connect the stream to a sink
     *
     * @param deviceName
     *            the device to connect to. use
     *            <code>null</code for the default device
     * @throws LineUnavailableException
     */
    void connectForPlayback(String deviceName,
                            StreamBufferAttributes bufferAttributes, byte[] syncStreamPointer)
    throws LineUnavailableException
    {

        int returnValue = native_pa_stream_connect_playback(
                              deviceName,
                              bufferAttributes.getMaxLength(),
                              bufferAttributes.getTargetLength(),
                              bufferAttributes.getPreBuffering(),
                              bufferAttributes.getMinimumRequest(),
                              bufferAttributes.getFragmentSize(),
                              FLAG_START_CORKED, null, syncStreamPointer
                          );
        if (returnValue < 0)
        {
            throw new LineUnavailableException(
                "Unable To connect a line for playback");
        }
    }

    /**
     * Connect the stream to a source.
     *
     * @throws LineUnavailableException
     *
     */
    void connectForRecording(String deviceName, long flags,
                             StreamBufferAttributes bufferAttributes)
    throws LineUnavailableException
    {

        int returnValue = native_pa_stream_connect_record(
                              deviceName,
                              bufferAttributes.getMaxLength(),
                              bufferAttributes.getTargetLength(),
                              bufferAttributes.getPreBuffering(),
                              bufferAttributes.getMinimumRequest(),
                              bufferAttributes.getFragmentSize(),
                              flags, null, null
                          );
        if (returnValue < 0)
        {
            throw new LineUnavailableException(
                "Unable to connect line for recording");
        }
    }

    /**
     * Disconnect a stream from a source/sink.
     */
    void disconnect()
    {
        int returnValue = native_pa_stream_disconnect();
        assert (returnValue == 0);
    }

    /**
     * Write data to the server
     *
     * @param data
     * @param length
     * @return
     */
    int write(byte[] data, int offset, int length)
    {
        return native_pa_stream_write(data, offset, length);
    }

    /**
     * Read the next fragment from the buffer (for recording).
     *
     *
     * @param data
     */
    byte[] peek()
    {
        return native_pa_stream_peek();
    }

    /**
     *
     * Remove the current fragment on record streams.
     */
    void drop()
    {
        native_pa_stream_drop();
    }

    /**
     * Return the number of bytes that may be written using write().
     *
     * @return
     */
    int getWritableSize()
    {
        return native_pa_stream_writable_size();
    }

    /**
     * Return the number of bytes that may be read using peek().
     *
     * @return
     */
    int getReableSize()
    {
        return native_pa_stream_readable_size();
    }

    /**
     * Drain a playback stream
     *
     * @return
     */
    Operation drain()
    {
        Operation drainOperation = new Operation(native_pa_stream_drain());
        return drainOperation;
    }

    Operation updateTimingInfo()
    {
        Operation updateOperation = new Operation(
            native_pa_stream_updateTimingInfo());
        return updateOperation;
    }

    /**
     * this function is called whenever the state changes
     */
    @SuppressWarnings("unused")
    private void stateCallback()
    {
        synchronized(EventLoop.getEventLoop().threadLock)
        {
            if (getState() == Stream.STATE_READY)
            {
                setBufAttr();
            }
        }
        synchronized (stateListeners)
        {
            for (StateListener listener : stateListeners)
            {
                listener.update();
            }
        }
    }

    @SuppressWarnings("unused")
    private void writeCallback()
    {
        synchronized (writeListeners)
        {
            for (WriteListener listener : writeListeners)
            {
                listener.update();
            }
        }
    }

    @SuppressWarnings("unused")
    private void readCallback()
    {
        synchronized (readListeners)
        {
            for (ReadListener listener : readListeners)
            {
                listener.update();
            }
        }
    }

    @SuppressWarnings("unused")
    private void overflowCallback()
    {
        System.out.println("overflowCallback called");
        synchronized (overflowListeners)
        {
            for (OverflowListener listener : overflowListeners)
            {
                listener.update();
            }
        }
    }

    @SuppressWarnings("unused")
    private void underflowCallback()
    {
        synchronized (underflowListeners)
        {
            for (UnderflowListener listener : underflowListeners)
            {
                listener.update();
            }
        }
    }

    /**
     * callback function that is called when a the server starts playback after
     * an underrun or on initial startup
     */
    @SuppressWarnings("unused")
    private void playbackStartedCallback()
    {
        synchronized (playbackStartedListeners)
        {
            for (PlaybackStartedListener listener : playbackStartedListeners)
            {
                listener.update();
            }
        }
    }

    /**
     * called whenever a latency information update happens
     */
    @SuppressWarnings("unused")
    private void latencyUpdateCallback()
    {
        synchronized (latencyUpdateListeners)
        {
            for (LatencyUpdateListener listener : latencyUpdateListeners)
            {
                listener.update();
            }
        }
    }

    /**
     * whenever the stream is moved to a different sink/source
     */
    @SuppressWarnings("unused")
    private void movedCallback()
    {
        synchronized (movedListeners)
        {
            for (MovedListener listener : movedListeners)
            {
                listener.update();
            }
        }
    }

    @SuppressWarnings("unused")
    private void corkCallback()
    {
        synchronized (corkListeners)
        {
            for (CorkListener listener : corkListeners)
            {
                listener.update();
            }
        }
    }

    /**
     * whenever the sink/source this stream is connected to is suspended or
     * resumed
     */
    @SuppressWarnings("unused")
    private void suspendedCallback()
    {
        synchronized (suspendedListeners)
        {
            for (SuspendedListener listener : suspendedListeners)
            {
                listener.update();
            }
        }
    }

    boolean isCorked()
    {
        int corked = native_pa_stream_is_corked();
        if (corked < 0)
        {
            throw new IllegalStateException("Unable to determine state");
        }
        return corked == 0 ? false : true;
    }

    /**
     * Pause (or resume) playback of this stream temporarily.
     *
     * @param cork
     * @return
     */
    Operation cork(boolean cork)
    {
        int yes = cork ? 1 : 0;
        Operation corkOperation = new Operation(native_pa_stream_cork(yes));
        return corkOperation;
    }

    Operation cork()
    {
        return cork(true);
    }

    Operation unCork()
    {
        return cork(false);
    }

    /**
     * Flush the playback buffer of this stream.
     *
     * @return
     */
    Operation flush()
    {
        Operation flushOperation = new Operation(native_pa_stream_flush());
        return flushOperation;
    }

    /*
     * Operation pa_stream_prebuf (pa_streams, pa_stream_success_cb_t cb, void
     * userdata)
     *
     * Reenable prebuffering as specified in the pa_buffer_attr structure.
     */

    /**
     * Request immediate start of playback on this stream.
     */
    Operation triggerStart()
    {
        Operation triggerOperation = new Operation(native_pa_stream_trigger());
        return triggerOperation;
    }

    /**
     * set the stream's name
     *
     * @param name
     * @return
     */
    Operation setName(String name)
    {
        Operation setNameOperation = new Operation(
            native_pa_stream_set_name(name));
        return setNameOperation;
    }

    /**
     *
     *
     * @return a time between ? and ?
     */
    long getTime()
    {
        return native_pa_stream_get_time();
    }

    /**
     * @return the total stream latency in microseconds
     */
    long getLatency()
    {
        return native_pa_stream_get_latency();
    }

    /*
     * const pa_timing_info pa_stream_get_timing_info (pa_streams) Return the
     * latest raw timing data structure.
     */

    Format getFormat()
    {
        return format;
    }

    /*
     * const pa_channel_map pa_stream_get_channel_map (pa_streams) Return a
     * pointer to the stream's channel map.
     */

    StreamBufferAttributes getBufferAttributes()
    {
        return native_pa_stream_get_buffer_attr();
    }

    Operation setBufferAtrributes(StreamBufferAttributes attr)
    {
        return new Operation(native_pa_stream_set_buffer_attr(attr));
    }

    /**
     * Change the stream sampling rate during playback.
     */
    Operation updateSampleRate(int rate)
    {
        return new Operation(native_pa_stream_update_sample_rate(rate));
    }

    byte[] getStreamPointer()
    {
        return streamPointer;
    }

    void free()
    {
        native_pa_stream_unref();
    }

    float getCachedVolume()
    {
        return this.cachedVolume;
    }

    void setCachedVolume(float volume)
    {
        this.cachedVolume = volume;
    }

    void update_channels_and_volume(int channels, float volume)
    {
        this.cachedVolume = volume;
    }

}
