/* PulseAudioVolumeControl.java
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

import javax.sound.sampled.FloatControl;

final class PulseAudioVolumeControl extends FloatControl
{

    static final int MAX_VOLUME = 65536;
    static final int MIN_VOLUME = 0;

    protected PulseAudioVolumeControl(PulseAudioPlaybackLine line,
                                      EventLoop eventLoop)
    {

        /*
         * the initial volume is ignored by pulseaudio.
         */
        super(FloatControl.Type.VOLUME, MIN_VOLUME, MAX_VOLUME, 1, -1, line
              .getCachedVolume(), "pulseaudio units", "Volume Off",
              "Default Volume", "Full Volume");
        this.line = line;
        this.eventLoop = eventLoop;
    }

    private EventLoop eventLoop;
    private PulseAudioPlaybackLine line;

    @Override
    public synchronized void setValue(float newValue)
    {
        if (newValue > MAX_VOLUME || newValue < MIN_VOLUME)
        {
            throw new IllegalArgumentException("invalid value");
        }

        if (!line.isOpen())
        {
            return;
        }

        setStreamVolume(newValue);

        line.setCachedVolume(newValue);
    }

    protected synchronized void setStreamVolume(float newValue)
    {
        Operation op;
        synchronized (eventLoop.threadLock)
        {
            op = new Operation(line.native_set_volume(newValue));
        }

        op.waitForCompletion();
        op.releaseReference();

    }

    public synchronized float getValue()
    {
        Operation op;
        synchronized (eventLoop.threadLock)
        {
            op = new Operation(line.native_update_volume());
        }

        op.waitForCompletion();
        op.releaseReference();

        return line.getCachedVolume();
    }

}
