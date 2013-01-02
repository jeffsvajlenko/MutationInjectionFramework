/* EventLoop.java
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

/**
 *
 * A simple debugging class. Set the debugging level through the system property
 * pulseaudio.debugLevel=<Level>. Level is one of Verbose, Debug, Info, Warning,
 * Error or None
 *
 * and then do DebugLevel.println(level, string) and so on
 *
 */

class Debug
{

    enum DebugLevel
    {
        Verbose, Debug, Info, Warning, Error, None
    }

    private static DebugLevel currentDebugLevel = DebugLevel.None;

    static
    {
        // System.out.println("PulseAudio: initializing Debug");

        String systemSetting;
        try
        {
            systemSetting = System.getProperty("pulseaudio.debugLevel");
        }
        catch (SecurityException e)
        {
            // sigh, we cant read that property
            systemSetting = null;
        }

        DebugLevel wantedLevel;
        try
        {
            wantedLevel = DebugLevel.valueOf(systemSetting);

        }
        catch (IllegalArgumentException e)
        {
            wantedLevel = DebugLevel.Info;
        }
        catch (NullPointerException e)
        {
            wantedLevel = DebugLevel.None;
        }

        currentDebugLevel = wantedLevel;
        println(DebugLevel.Info, "Using debug level: " + currentDebugLevel);
    }

    static void println(String string)
    {
        println(DebugLevel.Info, string);
    }

    static void print(DebugLevel level, String string)
    {
        int result = level.compareTo(currentDebugLevel);
        if (result >= 0)
        {
            if (level.compareTo(DebugLevel.Error) >= 0)
            {
                System.err.print(string);
            }
            else
            {
                System.out.print(string);
            }
        }
        else
        {
            // do nothing
        }
    }

    static void println(DebugLevel level, String string)
    {

        int result = level.compareTo(currentDebugLevel);
        if (result >= 0)
        {
            if (level.compareTo(DebugLevel.Error) >= 0)
            {
                System.err.println("DEBUG: pulse-java: " + string);
            }
            else
            {
                System.out.println("DEBUG: pulse-java: " + string);
            }
        }
        else
        {
            // do nothing
        }
    }

}
