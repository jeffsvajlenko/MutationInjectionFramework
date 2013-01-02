/*
 * Copyright (c) 2005, 2010, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.*;
import java.awt.peer.SystemTrayPeer;

public class XSystemTrayPeer implements SystemTrayPeer
{
    SystemTray target;
    long tray_owner;
    static XSystemTrayPeer peerInstance; // there is only one SystemTray peer per application

    final static XAtom _NET_SYSTEM_TRAY = XAtom.get("_NET_SYSTEM_TRAY_S0");
    final static XAtom _XEMBED_INFO = XAtom.get("_XEMBED_INFO");
    final static XAtom _NET_SYSTEM_TRAY_OPCODE = XAtom.get("_NET_SYSTEM_TRAY_OPCODE");
    final static XAtom _NET_WM_ICON = XAtom.get("_NET_WM_ICON");
    final static long SYSTEM_TRAY_REQUEST_DOCK = 0;

    XSystemTrayPeer(SystemTray target)
    {
        this.target = target;
        peerInstance = this;

        XToolkit.awtLock();
        try
        {
            tray_owner = XlibWrapper.XGetSelectionOwner(XToolkit.getDisplay(), _NET_SYSTEM_TRAY.getAtom());
        }
        finally
        {
            XToolkit.awtUnlock();
        }
    }

    public Dimension getTrayIconSize()
    {
        return new Dimension(XTrayIconPeer.TRAY_ICON_HEIGHT, XTrayIconPeer.TRAY_ICON_WIDTH);
    }

    boolean isAvailable()
    {
        boolean available = false;
        XToolkit.awtLock();
        try
        {
            long selection_owner = XlibWrapper.XGetSelectionOwner(XToolkit.getDisplay(),
                                   _NET_SYSTEM_TRAY.getAtom());
            available = (selection_owner != XConstants.None);
        }
        finally
        {
            XToolkit.awtUnlock();
        }
        return available;
    }

    // ***********************************************************************
    // ***********************************************************************

    void addTrayIcon(XTrayIconPeer tiPeer) throws AWTException
    {
        tray_owner = 0;
        XToolkit.awtLock();
        try
        {
            tray_owner = XlibWrapper.XGetSelectionOwner(XToolkit.getDisplay(), _NET_SYSTEM_TRAY.getAtom());
        }
        finally
        {
            XToolkit.awtUnlock();
        }

        if (tray_owner == 0)
        {
            throw new AWTException("TrayIcon couldn't be displayed.");
        }

        long tray_window = tiPeer.getWindow();
        long data[] = new long[] {XEmbedHelper.XEMBED_VERSION, XEmbedHelper.XEMBED_MAPPED};
        long data_ptr = Native.card32ToData(data);

        _XEMBED_INFO.setAtomData(tray_window, data_ptr, data.length);

        sendMessage(tray_owner, SYSTEM_TRAY_REQUEST_DOCK, tray_window, 0, 0);
    }

    void sendMessage(long win, long msg, long data1, long data2, long data3)
    {
        XClientMessageEvent xev = new XClientMessageEvent();

        try
        {
            xev.set_type(XlibWrapper.ClientMessage);
            xev.set_window(win);
            xev.set_format(32);
            xev.set_message_type(_NET_SYSTEM_TRAY_OPCODE.getAtom());
            xev.set_data(0, 0);
            xev.set_data(1, msg);
            xev.set_data(2, data1);
            xev.set_data(3, data2);
            xev.set_data(4, data3);

            XToolkit.awtLock();
            try
            {
                XlibWrapper.XSendEvent(XToolkit.getDisplay(), win, false,
                                       XlibWrapper.NoEventMask, xev.pData);
            }
            finally
            {
                XToolkit.awtUnlock();
            }
        }
        finally
        {
            xev.dispose();
        }
    }

    static XSystemTrayPeer getPeerInstance()
    {
        return peerInstance;
    }
}
