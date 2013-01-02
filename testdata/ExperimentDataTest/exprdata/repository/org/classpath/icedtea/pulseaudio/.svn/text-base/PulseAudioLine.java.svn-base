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

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Control;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.Control.Type;

abstract class PulseAudioLine implements Line
{

    protected List<LineListener> lineListeners = new ArrayList<LineListener>();
    protected List<Control> controls = new ArrayList<Control>();

    // true between open() and close(). ie represents when a line has acquire
    // resources
    protected boolean isOpen = false;

    @Override
    public void addLineListener(LineListener listener)
    {
        this.lineListeners.add(listener);
    }

    @Override
    public void close()
    {
        if (!isOpen())
        {
            throw new IllegalStateException("Line is not open");
        }

        lineListeners.clear();

        isOpen = false;
    }

    protected void fireLineEvent(LineEvent e)
    {
        for (LineListener lineListener : lineListeners)
        {
            lineListener.update(e);
        }
    }

    @Override
    public Control getControl(Type control)
    {
        if (isOpen())
        {
            for (Control aControl : controls)
            {
                if (aControl.getType() == control)
                {
                    return aControl;
                }
            }
        }
        throw new IllegalArgumentException(control.toString()
                                           + " not supported");
    }

    @Override
    public Control[] getControls()
    {
        if (!isOpen())
        {
            return new Control[] {};
        }

        return (Control[]) controls.toArray(new Control[0]);
    }

    @Override
    public boolean isControlSupported(Type control)
    {
        for (Control myControl : controls)
        {
            //Control.Type's known descendants keep a set of
            //static Types.
            if (myControl.getType().equals(control))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isOpen()
    {
        return isOpen;
    }

    @Override
    public void removeLineListener(LineListener listener)
    {
        lineListeners.remove(listener);
    }

}
