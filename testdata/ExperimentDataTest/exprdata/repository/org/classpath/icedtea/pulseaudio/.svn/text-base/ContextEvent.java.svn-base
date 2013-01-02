/* ContextEvent.java
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

/**
 * This class encapsulates a change in the PulseAudio's connection context.
 *
 * When this event is fired, something has happened to the connection of this
 * program to the PulseAudio server.
 */
class ContextEvent
{
    // There are certain enumerations from pulse audio that we need to use in
    // the java side. For all of these, we declare static longs in the proper
    // java classes and we use static native methods to initialize them to
    // the values used by pulse audio. This makes us immune to changes in
    // the integer values of the enum symbols in pulse audio.
    /**
     *  Basically, what is the new state of the context
     *  These will be initialized to the proper values in the JNI.
     */
    static long UNCONNECTED  = -1,
                CONNECTING   = -1,
                AUTHORIZING  = -1,
                SETTING_NAME = -1,
                READY        = -1,
                FAILED       = -1,
                TERMINATED   = -1;

    private static native void init_constants();

    static
    {
        SecurityWrapper.loadNativeLibrary();
        init_constants();
    }

    /**
     * Throws an IllegalStateException if value is not one of the above
     * context events. We do this for all pulse audio enumerations that
     * we need to use in the java side. If pulse audio decides to add
     * new events/states, we need to add them to the classes. The exception
     * will let us know.
     * return the input if there is no error
     *
     * @param value is the context event to be checked against one of the known
     *        events.
     * @return value if it is a known event. Otherwise throw an exception.
     */
    public static long checkNativeEnumReturn(long value)
    {
        if (!Arrays.asList(
                    UNCONNECTED, CONNECTING, AUTHORIZING, SETTING_NAME,
                    READY, FAILED, TERMINATED
                ).contains(value))
        {
            throw new IllegalStateException("Illegal constant for ContextEvent: " + value);
        }
        return value;
    }

    private long type;

    public ContextEvent(long type)
    {
        this.type = checkNativeEnumReturn(type);
    }

    public long getType()
    {
        return type;
    }
}
