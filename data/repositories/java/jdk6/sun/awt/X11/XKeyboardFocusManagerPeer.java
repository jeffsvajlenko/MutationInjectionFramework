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
import java.awt.KeyboardFocusManager;
import java.awt.Window;

import java.awt.event.FocusEvent;

import java.awt.peer.KeyboardFocusManagerPeer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.logging.Level;
import java.util.logging.Logger;

import sun.awt.AWTAccessor;
import sun.awt.CausedFocusEvent;
import sun.awt.SunToolkit;

public class XKeyboardFocusManagerPeer implements KeyboardFocusManagerPeer
{
    private static final Logger focusLog = Logger.getLogger("sun.awt.X11.focus.XKeyboardFocusManagerPeer");
    KeyboardFocusManager manager;

    XKeyboardFocusManagerPeer(KeyboardFocusManager manager)
    {
        this.manager = manager;
    }

    private static Object lock = new Object() {};
    private static Component currentFocusOwner;
    private static Window currentFocusedWindow;

    static void setCurrentNativeFocusOwner(Component comp)
    {
        if (focusLog.isLoggable(Level.FINER)) focusLog.finer("Setting current native focus owner " + comp);
        synchronized(lock)
        {
            currentFocusOwner = comp;
        }
    }

    static void setCurrentNativeFocusedWindow(Window win)
    {
        if (focusLog.isLoggable(Level.FINER)) focusLog.finer("Setting current native focused window " + win);
        XWindowPeer from = null, to = null;

        synchronized(lock)
        {
            if (currentFocusedWindow != null)
            {
                from = (XWindowPeer)currentFocusedWindow.getPeer();
            }

            currentFocusedWindow = win;

            if (currentFocusedWindow != null)
            {
                to = (XWindowPeer)currentFocusedWindow.getPeer();
            }
        }

        if (from != null)
        {
            from.updateSecurityWarningVisibility();
        }
        if (to != null)
        {
            to.updateSecurityWarningVisibility();
        }
    }

    static Component getCurrentNativeFocusOwner()
    {
        synchronized(lock)
        {
            return currentFocusOwner;
        }
    }

    static Window getCurrentNativeFocusedWindow()
    {
        synchronized(lock)
        {
            return currentFocusedWindow;
        }
    }

    public Window getCurrentFocusedWindow()
    {
        return getCurrentNativeFocusedWindow();
    }

    public void setCurrentFocusOwner(Component comp)
    {
        setCurrentNativeFocusOwner(comp);
    }

    public Component getCurrentFocusOwner()
    {
        return getCurrentNativeFocusOwner();
    }

    public void clearGlobalFocusOwner(Window activeWindow)
    {
        if (activeWindow != null)
        {
            Component focusOwner = activeWindow.getFocusOwner();
            if (focusLog.isLoggable(Level.FINE)) focusLog.fine("Clearing global focus owner " + focusOwner);
            if (focusOwner != null)
            {
//                XComponentPeer nativePeer = XComponentPeer.getNativeContainer(focusOwner);
//                if (nativePeer != null) {
                FocusEvent fl = new CausedFocusEvent(focusOwner, FocusEvent.FOCUS_LOST, false, null,
                                                     CausedFocusEvent.Cause.CLEAR_GLOBAL_FOCUS_OWNER);
                XWindow.sendEvent(fl);
//                }
            }
        }
    }

    static boolean simulateMotifRequestFocus(Component lightweightChild, Component target, boolean temporary,
            boolean focusedWindowChangeAllowed, long time, CausedFocusEvent.Cause cause)
    {
        if (lightweightChild == null)
        {
            lightweightChild = (Component)target;
        }
        Component currentOwner = XKeyboardFocusManagerPeer.getCurrentNativeFocusOwner();
        if (currentOwner != null && currentOwner.getPeer() == null)
        {
            currentOwner = null;
        }
        if (focusLog.isLoggable(Level.FINER)) focusLog.finer("Simulating transfer from " + currentOwner + " to " + lightweightChild);
        FocusEvent  fg = new CausedFocusEvent(lightweightChild, FocusEvent.FOCUS_GAINED, false, currentOwner, cause);
        FocusEvent fl = null;
        if (currentOwner != null)
        {
            fl = new CausedFocusEvent(currentOwner, FocusEvent.FOCUS_LOST, false, lightweightChild, cause);
        }

        if (fl != null)
        {
            XWindow.sendEvent(fl);
        }
        XWindow.sendEvent(fg);
        return true;
    }

    static int shouldNativelyFocusHeavyweight(Component heavyweight,
            Component descendant, boolean temporary,
            boolean focusedWindowChangeAllowed, long time, CausedFocusEvent.Cause cause)
    {
        return AWTAccessor.getKeyboardFocusManagerAccessor()
               .shouldNativelyFocusHeavyweight(heavyweight,
                                               descendant,
                                               temporary,
                                               focusedWindowChangeAllowed,
                                               time,
                                               cause);
    }
}
