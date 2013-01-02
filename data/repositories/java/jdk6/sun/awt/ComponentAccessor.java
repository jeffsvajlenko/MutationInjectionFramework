/*
 * Copyright (c) 2002, 2007, Oracle and/or its affiliates. All rights reserved.
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

package sun.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.AWTEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;

import java.awt.peer.ComponentPeer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

import java.util.logging.Logger;
import java.util.logging.Level;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * A collection of methods for modifying package private fields in AWT components.
 * This class is meant to be used by Peer code only. Previously peer code
 * got around this problem by modifying fields from native code. However
 * as we move away from native code to Pure-java peers we need this class.
 *
 * @author Bino George
 */


public class ComponentAccessor
{

    private static final AWTAccessor.ComponentAccessor ca = AWTAccessor.getComponentAccessor();

    private ComponentAccessor()
    {
    }

    public static void setX(Component c, int x)
    {
        ca.setX(c, x);
    }

    public static void setY(Component c, int y)
    {
        ca.setY(c, y);
    }

    public static void setWidth(Component c, int width)
    {
        ca.setWidth(c, width);
    }

    public static void setHeight(Component c, int height)
    {
        ca.setHeight(c, height);
    }

    public static void setBounds(Component c, int x, int y, int width, int height)
    {
        ca.setX(c, x);
        ca.setY(c, y);
        ca.setWidth(c, width);
        ca.setHeight(c, height);
    }

    public static int getX(Component c)
    {
        return ca.getX(c);
    }

    public static int getY(Component c)
    {
        return ca.getY(c);
    }

    public static int getWidth(Component c)
    {
        return ca.getWidth(c);
    }

    public static int getHeight(Component c)
    {
        return ca.getHeight(c);
    }

    public static boolean getIsPacked(Component c)
    {
        return ca.isPacked(c);
    }

    public static Container getParent_NoClientCode(Component c)
    {
        return ca.getParent(c);
    }

    public static Font getFont_NoClientCode(Component c)
    {
        return ca.getFont_NoClientCode(c);
    }

    public static void processEvent(Component c, AWTEvent event)
    {
        ca.processEvent(c, event);
    }

    public static void enableEvents(Component c, long event_mask)
    {
        ca.enableEvents(c, event_mask);
    }

    public static void setParent(Component c, Container parent)
    {
        ca.setParent(c, parent);
    }

    public static Color getForeground(Component c)
    {
        return ca.getForeground(c);
    }

    public static Color getBackground(Component c)
    {
        return ca.getBackground(c);
    }

    public static void setBackground(Component c, Color color)
    {
        ca.setBackground(c, color);
    }

    public static Font getFont(Component c)
    {
        return ca.getFont(c);
    }

    public static ComponentPeer getPeer(Component c)
    {
        return ca.getPeer(c);
    }

    public static void setPeer(Component c, ComponentPeer peer)
    {
        ca.setPeer(c, peer);
    }

    public static boolean getIgnoreRepaint(Component comp)
    {
        return ca.getIgnoreRepaint(comp);
    }

    public static void resetGC(Component c)
    {
        ca.resetGC(c);
    }

    public static boolean getVisible(Component c)
    {
        return ca.isVisible(c);
    }

    public static boolean isEnabledImpl(Component c)
    {
        return ca.isEnabled(c);
    }

    public static Cursor getCursor_NoClientCode(Component c)
    {
        return ca.getCursor(c);
    }

    public static Point getLocation_NoClientCode(Component c)
    {
        return ca.getLocation(c);
    }
}
