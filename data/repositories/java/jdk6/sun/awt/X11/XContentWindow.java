/*
 * Copyright (c) 2003, 2007, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package sun.awt.X11;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Insets;

import java.awt.event.ComponentEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

import sun.awt.ComponentAccessor;

/**
 * This class implements window which serves as content window for decorated frames.
 * Its purpose to provide correct events dispatching for the complex
 * constructs such as decorated frames.
 */
public class XContentWindow extends XWindow implements XConstants
{
    private static Logger insLog = Logger.getLogger("sun.awt.X11.insets.XContentWindow");

    XDecoratedPeer parentFrame;

    // A list of expose events that come when the parentFrame is iconified
    private java.util.List<SavedExposeEvent> iconifiedExposeEvents = new java.util.ArrayList<SavedExposeEvent>();

    XContentWindow(XDecoratedPeer parentFrame, Rectangle bounds)
    {
        super((Component)parentFrame.getTarget(), parentFrame.getShell(), bounds);
        this.parentFrame = parentFrame;
    }

    void preInit(XCreateWindowParams params)
    {
        super.preInit(params);
        params.putIfNull(BIT_GRAVITY, Integer.valueOf(NorthWestGravity));
        Long eventMask = (Long)params.get(EVENT_MASK);
        if (eventMask != null)
        {
            eventMask = eventMask & ~(StructureNotifyMask);
            params.put(EVENT_MASK, eventMask);
        }
    }

    void initialize()
    {
        xSetVisible(true);
    }
    protected String getWMName()
    {
        return "Content window";
    }
    protected boolean isEventDisabled(XEvent e)
    {
        switch (e.get_type())
        {
            // Override parentFrame to receive MouseEnter/Exit
        case EnterNotify:
        case LeaveNotify:
            return false;
            // We handle ConfigureNotify specifically in XDecoratedPeer
        case ConfigureNotify:
            return true;
            // We don't want SHOWN/HIDDEN on content window since it will duplicate XDecoratedPeer
        case MapNotify:
        case UnmapNotify:
            return true;
        default:
            return super.isEventDisabled(e) || parentFrame.isEventDisabled(e);
        }
    }

    // Coordinates are that of the shell
    void setContentBounds(WindowDimensions dims)
    {
        XToolkit.awtLock();
        try
        {
            // Bounds of content window are of the same size as bounds of Java window and with
            // location as -(insets)
            Rectangle newBounds = dims.getBounds();
            Insets in = dims.getInsets();
            if (in != null)
            {
                newBounds.setLocation(-in.left, -in.top);
            }
            if (insLog.isLoggable(Level.FINE))
            {
                insLog.log(Level.FINE, "Setting content bounds {0}, old bounds {1}",
                           new Object[] {String.valueOf(newBounds), String.valueOf(getBounds())});
            }
            // Fix for 5023533:
            // Change in the size of the content window means, well, change of the size
            // Change in the location of the content window means change in insets
            boolean needHandleResize = !(newBounds.equals(getBounds()));
            reshape(newBounds);
            if (needHandleResize)
            {
                insLog.fine("Sending RESIZED");
                handleResize(newBounds);
            }
        }
        finally
        {
            XToolkit.awtUnlock();
        }
        validateSurface();
    }

    // NOTE: This method may be called by privileged threads.
    //       DO NOT INVOKE CLIENT CODE ON THIS THREAD!
    public void handleResize(Rectangle bounds)
    {
        ComponentAccessor.setWidth((Component)target, bounds.width);
        ComponentAccessor.setHeight((Component)target, bounds.height);
        postEvent(new ComponentEvent(target, ComponentEvent.COMPONENT_RESIZED));
    }


    public void handleExposeEvent(Component target, int x, int y, int w, int h)
    {
        // TODO: ?
        // get rid of 'istanceof' by subclassing:
        // XContentWindow -> XFrameContentWindow

        // Expose event(s) that result from deiconification
        // come before a deicinofication notification.
        // We reorder these events by saving all expose events
        // that come when the frame is iconified. Then we
        // actually handle saved expose events on deiconification.

        if (parentFrame instanceof XFramePeer &&
                (((XFramePeer)parentFrame).getState() & java.awt.Frame.ICONIFIED) != 0)
        {
            // Save expose events if the frame is iconified
            // in order to handle them on deiconification.
            iconifiedExposeEvents.add(new SavedExposeEvent(target, x, y, w, h));
        }
        else
        {
            // Normal case: [it is not a frame or] the frame is not iconified.
            super.handleExposeEvent(target, x, y, w, h);
        }
    }

    void purgeIconifiedExposeEvents()
    {
        for (SavedExposeEvent evt : iconifiedExposeEvents)
        {
            super.handleExposeEvent(evt.target, evt.x, evt.y, evt.w, evt.h);
        }
        iconifiedExposeEvents.clear();
    }

    private static class SavedExposeEvent
    {
        Component target;
        int x, y, w, h;
        SavedExposeEvent(Component target, int x, int y, int w, int h)
        {
            this.target = target;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }

    public String toString()
    {
        return getClass().getName() + "[" + getBounds() + "]";
    }
}
