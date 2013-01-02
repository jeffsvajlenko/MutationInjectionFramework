/*
 * Copyright (c) 2002, 2009, Oracle and/or its affiliates. All rights reserved.
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

import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.WindowEvent;

import java.awt.image.BufferedImage;

import java.awt.peer.ComponentPeer;
import java.awt.peer.WindowPeer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;

import sun.awt.AWTAccessor;
import sun.awt.ComponentAccessor;
import sun.awt.WindowAccessor;
import sun.awt.DisplayChangedListener;
import sun.awt.SunToolkit;
import sun.awt.X11GraphicsDevice;
import sun.awt.X11GraphicsEnvironment;

import sun.java2d.pipe.Region;

class XWindowPeer extends XPanelPeer implements WindowPeer,
    DisplayChangedListener, MWMConstants
{

    private static final Logger log = Logger.getLogger("sun.awt.X11.XWindowPeer");
    private static final Logger focusLog = Logger.getLogger("sun.awt.X11.focus.XWindowPeer");
    private static final Logger insLog = Logger.getLogger("sun.awt.X11.insets.XWindowPeer");
    private static final Logger grabLog = Logger.getLogger("sun.awt.X11.grab.XWindowPeer");
    private static final Logger iconLog = Logger.getLogger("sun.awt.X11.icon.XWindowPeer");

    // should be synchronized on awtLock
    private static Set<XWindowPeer> windows = new HashSet<XWindowPeer>();

    Insets insets = new Insets( 0, 0, 0, 0 );
    private boolean cachedFocusableWindow;
    XWarningWindow warningWindow;

    private boolean alwaysOnTop;
    private boolean locationByPlatform;

    Dialog modalBlocker;
    boolean delayedModalBlocking = false;
    Dimension targetMinimumSize = null;

    private XWindowPeer ownerPeer;

    // used for modal blocking to keep existing z-order
    protected XWindowPeer prevTransientFor, nextTransientFor;
    // value of WM_TRANSIENT_FOR hint set on this window
    private XWindowPeer curRealTransientFor;

    private boolean grab = false; // Whether to do a grab during showing

    private boolean isMapped = false; // Is this window mapped or not
    private boolean mustControlStackPosition = false; // Am override-redirect not on top
    private XEventDispatcher rootPropertyEventDispatcher = null;

    /*
     * Focus related flags
     */
    private boolean isUnhiding = false;             // Is the window unhiding.
    private boolean isBeforeFirstMapNotify = false; // Is the window (being shown) between
    //    setVisible(true) & handleMapNotify().

    // It need to be accessed from XFramePeer.
    protected Vector <ToplevelStateListener> toplevelStateListeners = new Vector<ToplevelStateListener>();
    XWindowPeer(XCreateWindowParams params)
    {
        super(params.putIfNull(PARENT_WINDOW, Long.valueOf(0)));
    }

    XWindowPeer(Window target)
    {
        super(new XCreateWindowParams(new Object[]
                                      {
                                          TARGET, target,
                                          PARENT_WINDOW, Long.valueOf(0)
                                      }));
    }

    // fallback default font object
    static Font defaultFont;

    /*
     * This constant defines icon size recommended for using.
     * Apparently, we should use XGetIconSizes which should
     * return icon sizes would be most appreciated by the WM.
     * However, XGetIconSizes always returns 0 for some reason.
     * So the constant has been introduced.
     */
    private static final int PREFERRED_SIZE_FOR_ICON = 128;

    /*
     * Sometimes XChangeProperty(_NET_WM_ICON) doesn't work if
     * image buffer is too large. This constant holds maximum
     * length of buffer which can be used with _NET_WM_ICON hint.
     * It holds int's value.
     */
    private static final int MAXIMUM_BUFFER_LENGTH_NET_WM_ICON = (2<<15) - 1;

    void preInit(XCreateWindowParams params)
    {
        target = (Component)params.get(TARGET);
        params.put(REPARENTED,
                   Boolean.valueOf(isOverrideRedirect() || isSimpleWindow()));
        super.preInit(params);
        params.putIfNull(BIT_GRAVITY, Integer.valueOf(NorthWestGravity));

        long eventMask = 0;
        if (params.containsKey(EVENT_MASK))
        {
            eventMask = ((Long)params.get(EVENT_MASK));
        }
        eventMask |= XConstants.VisibilityChangeMask;
        params.put(EVENT_MASK, eventMask);

        XA_NET_WM_STATE = XAtom.get("_NET_WM_STATE");

        insets = new Insets(0,0,0,0);

        params.put(OVERRIDE_REDIRECT, Boolean.valueOf(isOverrideRedirect()));

        SunToolkit.awtLock();
        try
        {
            windows.add(this);
        }
        finally
        {
            SunToolkit.awtUnlock();
        }

        cachedFocusableWindow = isFocusableWindow();

        Font f = target.getFont();
        if (f == null)
        {
            if (defaultFont == null)
            {
                defaultFont = new Font(Font.DIALOG, Font.PLAIN, 12);
            }
            f = defaultFont;
            target.setFont(f);
            // we should not call setFont because it will call a repaint
            // which the peer may not be ready to do yet.
        }
        Color c = target.getBackground();
        if (c == null)
        {
            Color background = SystemColor.window;
            target.setBackground(background);
            // we should not call setBackGround because it will call a repaint
            // which the peer may not be ready to do yet.
        }
        c = target.getForeground();
        if (c == null)
        {
            target.setForeground(SystemColor.windowText);
            // we should not call setForeGround because it will call a repaint
            // which the peer may not be ready to do yet.
        }

        alwaysOnTop = ((Window)target).isAlwaysOnTop() && ((Window)target).isAlwaysOnTopSupported();

        GraphicsConfiguration gc = getGraphicsConfiguration();
        ((X11GraphicsDevice)gc.getDevice()).addDisplayChangedListener(this);

        Rectangle bounds = (Rectangle)(params.get(BOUNDS));
        params.put(BOUNDS, constrainBounds(bounds.x, bounds.y, bounds.width, bounds.height));
    }

    protected String getWMName()
    {
        String name = target.getName();
        if (name == null || name.trim().equals(""))
        {
            name = " ";
        }
        return name;
    }

    private static native String getLocalHostname();
    private static native int getJvmPID();

    void postInit(XCreateWindowParams params)
    {
        super.postInit(params);

        // Init WM_PROTOCOLS atom
        initWMProtocols();

        // Set _NET_WM_PID and WM_CLIENT_MACHINE using this JVM
        XAtom.get("WM_CLIENT_MACHINE").setProperty(getWindow(), getLocalHostname());
        XAtom.get("_NET_WM_PID").setCard32Property(getWindow(), getJvmPID());

        // Set WM_TRANSIENT_FOR and group_leader
        Window t_window = (Window)target;
        Window owner = t_window.getOwner();
        if (owner != null)
        {
            ownerPeer = (XWindowPeer)owner.getPeer();
            if (focusLog.isLoggable(Level.FINER))
            {
                focusLog.fine("Owner is " + owner);
                focusLog.fine("Owner peer is " + ownerPeer);
                focusLog.fine("Owner X window " + Long.toHexString(ownerPeer.getWindow()));
                focusLog.fine("Owner content X window " + Long.toHexString(ownerPeer.getContentWindow()));
            }
            // as owner window may be an embedded window, we must get a toplevel window
            // to set as TRANSIENT_FOR hint
            long ownerWindow = ownerPeer.getWindow();
            if (ownerWindow != 0)
            {
                XToolkit.awtLock();
                try
                {
                    // Set WM_TRANSIENT_FOR
                    if (focusLog.isLoggable(Level.FINE)) focusLog.fine("Setting transient on " + Long.toHexString(getWindow())
                                + " for " + Long.toHexString(ownerWindow));
                    setToplevelTransientFor(this, ownerPeer, false, true);

                    // Set group leader
                    XWMHints hints = getWMHints();
                    hints.set_flags(hints.get_flags() | (int)XlibWrapper.WindowGroupHint);
                    hints.set_window_group(ownerWindow);
                    XlibWrapper.XSetWMHints(XToolkit.getDisplay(), getWindow(), hints.pData);
                }
                finally
                {
                    XToolkit.awtUnlock();
                }
            }
        }

        // Init warning window(for applets)
        if (((Window)target).getWarningString() != null)
        {
            // accessSystemTray permission allows to display TrayIcon, TrayIcon tooltip
            // and TrayIcon balloon windows without a warning window.
            if (!WindowAccessor.isTrayIconWindow((Window)target))
            {
                warningWindow = new XWarningWindow((Window)target, getWindow(), this);
            }
        }

        setSaveUnder(true);

        XWM.requestWMExtents(getWindow());
        updateIconImages();

        updateShape();
        updateOpacity();
        // no need in updateOpaque() as it is no-op
    }

    public void updateIconImages()
    {
        Window target = (Window)this.target;
        java.util.List<Image> iconImages = ((Window)target).getIconImages();
        XWindowPeer ownerPeer = getOwnerPeer();
        winAttr.icons = new ArrayList<XIconInfo>();
        if (iconImages.size() != 0)
        {
            //read icon images from target
            winAttr.iconsInherited = false;
            for (Iterator<Image> i = iconImages.iterator(); i.hasNext(); )
            {
                Image image = i.next();
                if (image == null)
                {
                    if (log.isLoggable(Level.FINEST))
                    {
                        log.finest("XWindowPeer.updateIconImages: Skipping the image passed into Java because it's null.");
                    }
                    continue;
                }
                XIconInfo iconInfo;
                try
                {
                    iconInfo = new XIconInfo(image);
                }
                catch (Exception e)
                {
                    if (log.isLoggable(Level.FINEST))
                    {
                        log.finest("XWindowPeer.updateIconImages: Perhaps the image passed into Java is broken. Skipping this icon.");
                    }
                    continue;
                }
                if (iconInfo.isValid())
                {
                    winAttr.icons.add(iconInfo);
                }
            }
        }

        // Fix for CR#6425089
        winAttr.icons = normalizeIconImages(winAttr.icons);

        if (winAttr.icons.size() == 0)
        {
            //target.icons is empty or all icon images are broken
            if (ownerPeer != null)
            {
                //icon is inherited from parent
                winAttr.iconsInherited = true;
                winAttr.icons = ownerPeer.getIconInfo();
            }
            else
            {
                //default icon is used
                winAttr.iconsInherited = false;
                winAttr.icons = getDefaultIconInfo();
            }
        }
        recursivelySetIcon(winAttr.icons);
    }

    /*
     * Sometimes XChangeProperty(_NET_WM_ICON) doesn't work if
     * image buffer is too large. This function help us accommodate
     * initial list of the icon images to certainly-acceptable.
     * It does scale some of these icons to appropriate size
     * if it's necessary.
     */
    static java.util.List<XIconInfo> normalizeIconImages(java.util.List<XIconInfo> icons)
    {
        java.util.List<XIconInfo> result = new ArrayList<XIconInfo>();
        int totalLength = 0;
        boolean haveLargeIcon = false;

        for (XIconInfo icon : icons)
        {
            int width = icon.getWidth();
            int height = icon.getHeight();
            int length = icon.getRawLength();

            if (width > PREFERRED_SIZE_FOR_ICON || height > PREFERRED_SIZE_FOR_ICON)
            {
                if (haveLargeIcon)
                {
                    continue;
                }
                int scaledWidth = width;
                int scaledHeight = height;
                while (scaledWidth > PREFERRED_SIZE_FOR_ICON ||
                        scaledHeight > PREFERRED_SIZE_FOR_ICON)
                {
                    scaledWidth = scaledWidth / 2;
                    scaledHeight = scaledHeight / 2;
                }

                icon.setScaledSize(scaledWidth, scaledHeight);
                length = icon.getRawLength();
            }

            if (totalLength + length <= MAXIMUM_BUFFER_LENGTH_NET_WM_ICON)
            {
                totalLength += length;
                result.add(icon);
                if (width > PREFERRED_SIZE_FOR_ICON || height > PREFERRED_SIZE_FOR_ICON)
                {
                    haveLargeIcon = true;
                }
            }
        }

        if (iconLog.isLoggable(Level.FINEST))
        {
            iconLog.log(Level.FINEST, ">>> Length_ of buffer of icons data: " + totalLength +
                        ", maximum length: " + MAXIMUM_BUFFER_LENGTH_NET_WM_ICON);
        }

        return result;
    }

    /*
     * Dumps each icon from the list
     */
    static void dumpIcons(java.util.List<XIconInfo> icons)
    {
        if (iconLog.isLoggable(Level.FINEST))
        {
            iconLog.log(Level.FINEST, ">>> Sizes of icon images:");
            for (Iterator<XIconInfo> i = icons.iterator(); i.hasNext(); )
            {
                iconLog.log(Level.FINEST, "    {0}", String.valueOf(i.next()));
            }
        }
    }

    public void recursivelySetIcon(java.util.List<XIconInfo> icons)
    {
        dumpIcons(winAttr.icons);
        setIconHints(icons);
        Window target = (Window)this.target;
        Window[] children = target.getOwnedWindows();
        int cnt = children.length;
        for (int i = 0; i < cnt; i++)
        {
            ComponentPeer childPeer = children[i].getPeer();
            if (childPeer != null && childPeer instanceof XWindowPeer)
            {
                if (((XWindowPeer)childPeer).winAttr.iconsInherited)
                {
                    ((XWindowPeer)childPeer).winAttr.icons = icons;
                    ((XWindowPeer)childPeer).recursivelySetIcon(icons);
                }
            }
        }
    }

    java.util.List<XIconInfo> getIconInfo()
    {
        return winAttr.icons;
    }
    void setIconHints(java.util.List<XIconInfo> icons)
    {
        //This does nothing for XWindowPeer,
        //It's overriden in XDecoratedPeer
    }

    private static ArrayList<XIconInfo> defaultIconInfo;
    protected synchronized static java.util.List<XIconInfo> getDefaultIconInfo()
    {
        if (defaultIconInfo == null)
        {
            defaultIconInfo = new ArrayList<XIconInfo>();
            if (XlibWrapper.dataModel == 32)
            {
                defaultIconInfo.add(new XIconInfo(XAWTIcon32_java_icon16_png.java_icon16_png));
                defaultIconInfo.add(new XIconInfo(XAWTIcon32_java_icon24_png.java_icon24_png));
                defaultIconInfo.add(new XIconInfo(XAWTIcon32_java_icon32_png.java_icon32_png));
                defaultIconInfo.add(new XIconInfo(XAWTIcon32_java_icon48_png.java_icon48_png));
            }
            else
            {
                defaultIconInfo.add(new XIconInfo(XAWTIcon64_java_icon16_png.java_icon16_png));
                defaultIconInfo.add(new XIconInfo(XAWTIcon64_java_icon24_png.java_icon24_png));
                defaultIconInfo.add(new XIconInfo(XAWTIcon64_java_icon32_png.java_icon32_png));
                defaultIconInfo.add(new XIconInfo(XAWTIcon64_java_icon48_png.java_icon48_png));
            }
        }
        return defaultIconInfo;
    }

    private void updateShape()
    {
        // Shape shape = ((Window)target).getShape();
        Shape shape = AWTAccessor.getWindowAccessor().getShape((Window)target);
        if (shape != null)
        {
            applyShape(Region.getInstance(shape, null));
        }
    }

    private void updateOpacity()
    {
        // float opacity = ((Window)target).getOpacity();
        float opacity = AWTAccessor.getWindowAccessor().getOpacity((Window)target);
        if (opacity < 1.0f)
        {
            setOpacity(opacity);
        }
    }

    public void updateMinimumSize()
    {
        //This function only saves minimumSize value in XWindowPeer
        //Setting WMSizeHints is implemented in XDecoratedPeer
        targetMinimumSize = (((Component)target).isMinimumSizeSet()) ?
                            ((Component)target).getMinimumSize() : null;
    }

    public Dimension getTargetMinimumSize()
    {
        return (targetMinimumSize == null) ? null : new Dimension(targetMinimumSize);
    }

    public XWindowPeer getOwnerPeer()
    {
        return ownerPeer;
    }

    // This method is overriden at the XDecoratedPeer to handle
    // decorated windows a bit differently.
    Rectangle constrainBounds(int x, int y, int width, int height)
    {
        // We don't restrict the setBounds() operation if the code is trusted.
        if (!hasWarningWindow())
        {
            return new Rectangle(x, y, width, height);
        }

        // The window bounds should be within the visible part of the screen
        int newX = x;
        int newY = y;
        int newW = width;
        int newH = height;

        // Now check each point is within the visible part of the screen
        GraphicsConfiguration gc = ((Window)target).getGraphicsConfiguration();
        Rectangle sB = gc.getBounds();
        Insets sIn = ((Window)target).getToolkit().getScreenInsets(gc);

        int screenX = sB.x + sIn.left;
        int screenY = sB.y + sIn.top;
        int screenW = sB.width - sIn.left - sIn.right;
        int screenH = sB.height - sIn.top - sIn.bottom;


        // First make sure the size is withing the visible part of the screen
        if (newW > screenW)
        {
            newW = screenW;
        }

        if (newH > screenH)
        {
            newH = screenH;
        }

        // Tweak the location if needed
        if (newX < screenX)
        {
            newX = screenX;
        }
        else if (newX + newW > screenX + screenW)
        {
            newX = screenX + screenW - newW;
        }

        if (newY < screenY)
        {
            newY = screenY;
        }
        else if (newY + newH > screenY + screenH)
        {
            newY = screenY + screenH - newH;
        }

        return new Rectangle(newX, newY, newW, newH);
    }

    //Fix for 6318144: PIT:Setting Min Size bigger than current size enlarges
    //the window but fails to revalidate, Sol-CDE
    //This bug is regression for
    //5025858: Resizing a decorated frame triggers componentResized event twice.
    //Since events are not posted from Component.setBounds we need to send them here.
    //Note that this function is overriden in XDecoratedPeer so event
    //posting is not changing for decorated peers
    public void setBounds(int x, int y, int width, int height, int op)
    {
        Rectangle newBounds = constrainBounds(x, y, width, height);

        XToolkit.awtLock();
        try
        {
            Rectangle oldBounds = getBounds();

            super.setBounds(newBounds.x, newBounds.y, newBounds.width, newBounds.height, op);

            Rectangle bounds = getBounds();

            XSizeHints hints = getHints();
            setSizeHints(hints.get_flags() | XlibWrapper.PPosition | XlibWrapper.PSize,
                         bounds.x, bounds.y, bounds.width, bounds.height);
            XWM.setMotifDecor(this, false, 0, 0);

            XNETProtocol protocol = XWM.getWM().getNETProtocol();
            if (protocol != null && protocol.active())
            {
                XAtomList net_wm_state = getNETWMState();
                net_wm_state.add(protocol.XA_NET_WM_STATE_SKIP_TASKBAR);
                setNETWMState(net_wm_state);
            }


            boolean isResized = !bounds.getSize().equals(oldBounds.getSize());
            boolean isMoved = !bounds.getLocation().equals(oldBounds.getLocation());
            if (isMoved || isResized)
            {
                repositionSecurityWarning();
            }
            if (isResized)
            {
                postEventToEventQueue(new ComponentEvent(getEventSource(), ComponentEvent.COMPONENT_RESIZED));
            }
            if (isMoved)
            {
                postEventToEventQueue(new ComponentEvent(getEventSource(), ComponentEvent.COMPONENT_MOVED));
            }
        }
        finally
        {
            XToolkit.awtUnlock();
        }
    }

    void updateFocusability()
    {
        updateFocusableWindowState();
        XToolkit.awtLock();
        try
        {
            XWMHints hints = getWMHints();
            hints.set_flags(hints.get_flags() | (int)XlibWrapper.InputHint);
            hints.set_input(false/*isNativelyNonFocusableWindow() ? (0):(1)*/);
            XlibWrapper.XSetWMHints(XToolkit.getDisplay(), getWindow(), hints.pData);
        }
        finally
        {
            XToolkit.awtUnlock();
        }
    }

    public Insets getInsets()
    {
        return new Insets(0, 0, 0, 0);
    }

    // NOTE: This method may be called by privileged threads.
    //       DO NOT INVOKE CLIENT CODE ON THIS THREAD!
    public void handleIconify()
    {
        postEvent(new WindowEvent((Window)target, WindowEvent.WINDOW_ICONIFIED));
    }

    // NOTE: This method may be called by privileged threads.
    //       DO NOT INVOKE CLIENT CODE ON THIS THREAD!
    public void handleDeiconify()
    {
        postEvent(new WindowEvent((Window)target, WindowEvent.WINDOW_DEICONIFIED));
    }

    // NOTE: This method may be called by privileged threads.
    //       DO NOT INVOKE CLIENT CODE ON THIS THREAD!
    public void handleStateChange(int oldState, int newState)
    {
        postEvent(new WindowEvent((Window)target,
                                  WindowEvent.WINDOW_STATE_CHANGED,
                                  oldState, newState));
    }

    /**
     * DEPRECATED:  Replaced by getInsets().
     */
    public Insets insets()
    {
        return getInsets();
    }

    boolean isAutoRequestFocus()
    {
        /* comment this as a part of 6187066 revertion.
        if (XToolkit.isToolkitThread()) {
            return WindowAccessor.isAutoRequestFocus((Window)target);
        } else {
            return ((Window)target).isAutoRequestFocus();
        }
        */
        return true;
    }

    /*
     * Converts native focused X window id into Java peer.
     */
    static XWindowPeer getNativeFocusedWindowPeer()
    {
        XBaseWindow baseWindow = XToolkit.windowToXWindow(xGetInputFocus());
        return (baseWindow instanceof XWindowPeer) ? (XWindowPeer)baseWindow :
               (baseWindow instanceof XFocusProxyWindow) ?
               ((XFocusProxyWindow)baseWindow).getOwner() : null;
    }

    boolean isFocusableWindow()
    {
        if (XToolkit.isToolkitThread() || SunToolkit.isAWTLockHeldByCurrentThread())
        {
            return cachedFocusableWindow;
        }
        else
        {
            return ((Window)target).isFocusableWindow();
        }
    }

    /* WARNING: don't call client code in this method! */
    boolean isFocusedWindowModalBlocker()
    {
        return false;
    }

    long getFocusTargetWindow()
    {
        return getContentWindow();
    }

    /**
     * Returns whether or not this window peer has native X window
     * configured as non-focusable window. It might happen if:
     * - Java window is non-focusable
     * - Java window is simple Window(not Frame or Dialog)
     */
    boolean isNativelyNonFocusableWindow()
    {
        if (XToolkit.isToolkitThread() || SunToolkit.isAWTLockHeldByCurrentThread())
        {
            return isSimpleWindow() || !cachedFocusableWindow;
        }
        else
        {
            return isSimpleWindow() || !(((Window)target).isFocusableWindow());
        }
    }

    public void handleWindowFocusIn_Dispatch()
    {
        if (EventQueue.isDispatchThread())
        {
            XKeyboardFocusManagerPeer.setCurrentNativeFocusedWindow((Window) target);
            target.dispatchEvent(new WindowEvent((Window)target, WindowEvent.WINDOW_GAINED_FOCUS));
        }
    }

    public void handleWindowFocusInSync(long serial)
    {
        WindowEvent we = new WindowEvent((Window)target, WindowEvent.WINDOW_GAINED_FOCUS);
        XKeyboardFocusManagerPeer.setCurrentNativeFocusedWindow((Window) target);
        sendEvent(we);
    }
    // NOTE: This method may be called by privileged threads.
    //       DO NOT INVOKE CLIENT CODE ON THIS THREAD!
    public void handleWindowFocusIn(long serial)
    {
        WindowEvent we = new WindowEvent((Window)target, WindowEvent.WINDOW_GAINED_FOCUS);
        /* wrap in Sequenced, then post*/
        XKeyboardFocusManagerPeer.setCurrentNativeFocusedWindow((Window) target);
        postEvent(wrapInSequenced((AWTEvent) we));
    }

    // NOTE: This method may be called by privileged threads.
    //       DO NOT INVOKE CLIENT CODE ON THIS THREAD!
    public void handleWindowFocusOut(Window oppositeWindow, long serial)
    {
        WindowEvent we = new WindowEvent((Window)target, WindowEvent.WINDOW_LOST_FOCUS, oppositeWindow);
        XKeyboardFocusManagerPeer.setCurrentNativeFocusedWindow(null);
        XKeyboardFocusManagerPeer.setCurrentNativeFocusOwner(null);
        /* wrap in Sequenced, then post*/
        postEvent(wrapInSequenced((AWTEvent) we));
    }
    public void handleWindowFocusOutSync(Window oppositeWindow, long serial)
    {
        WindowEvent we = new WindowEvent((Window)target, WindowEvent.WINDOW_LOST_FOCUS, oppositeWindow);
        XKeyboardFocusManagerPeer.setCurrentNativeFocusedWindow(null);
        XKeyboardFocusManagerPeer.setCurrentNativeFocusOwner(null);
        sendEvent(we);
    }

    /* --- DisplayChangedListener Stuff --- */

    /* Xinerama
     * called to check if we've been moved onto a different screen
     * Based on checkNewXineramaScreen() in awt_GraphicsEnv.c
     */
    public void checkIfOnNewScreen(Rectangle newBounds)
    {
        if (!XToolkit.localEnv.runningXinerama())
        {
            return;
        }

        if (log.isLoggable(Level.FINEST))
        {
            log.finest("XWindowPeer: Check if we've been moved to a new screen since we're running in Xinerama mode");
        }

        int area = newBounds.width * newBounds.height;
        int intAmt, vertAmt, horizAmt;
        int largestAmt = 0;
        int curScreenNum = ((X11GraphicsDevice)getGraphicsConfiguration().getDevice()).getScreen();
        int newScreenNum = 0;
        GraphicsDevice gds[] = XToolkit.localEnv.getScreenDevices();
        Rectangle screenBounds;

        for (int i = 0; i < gds.length; i++)
        {
            screenBounds = gds[i].getDefaultConfiguration().getBounds();
            if (newBounds.intersects(screenBounds))
            {
                horizAmt = Math.min(newBounds.x + newBounds.width,
                                    screenBounds.x + screenBounds.width) -
                           Math.max(newBounds.x, screenBounds.x);
                vertAmt = Math.min(newBounds.y + newBounds.height,
                                   screenBounds.y + screenBounds.height)-
                          Math.max(newBounds.y, screenBounds.y);
                intAmt = horizAmt * vertAmt;
                if (intAmt == area)
                {
                    // Completely on this screen - done!
                    newScreenNum = i;
                    break;
                }
                if (intAmt > largestAmt)
                {
                    largestAmt = intAmt;
                    newScreenNum = i;
                }
            }
        }
        if (newScreenNum != curScreenNum)
        {
            if (log.isLoggable(Level.FINEST))
            {
                log.finest("XWindowPeer: Moved to a new screen");
            }
            draggedToNewScreen(newScreenNum);
        }
    }

    /* Xinerama
     * called to update our GC when dragged onto another screen
     */
    public void draggedToNewScreen(int screenNum)
    {
        executeDisplayChangedOnEDT(screenNum);
    }

    /**
     * Helper method that executes the displayChanged(screen) method on
     * the event dispatch thread.  This method is used in the Xinerama case
     * and after display mode change events.
     */
    private void executeDisplayChangedOnEDT(final int screenNum)
    {
        Runnable dc = new Runnable()
        {
            public void run()
            {
                // Updates this window's GC and notifies all the children.
                // See XPanelPeer/XCanvasPeer.displayChanged(int) for details.
                displayChanged(screenNum);
            }
        };
        SunToolkit.executeOnEventHandlerThread((Component)target, dc);
    }

    /**
     * From the DisplayChangedListener interface; called from
     * X11GraphicsDevice when the display mode has been changed.
     */
    public void displayChanged()
    {
        GraphicsConfiguration gc = getGraphicsConfiguration();
        int curScreenNum = ((X11GraphicsDevice)gc.getDevice()).getScreen();
        executeDisplayChangedOnEDT(curScreenNum);
    }

    /**
     * From the DisplayChangedListener interface; top-levels do not need
     * to react to this event.
     */
    public void paletteChanged()
    {
    }

    /*
     * Overridden to check if we need to update our GraphicsDevice/Config
     * Added for 4934052.
     */
    @Override
    public void handleConfigureNotifyEvent(XEvent xev)
    {
        // TODO: We create an XConfigureEvent every time we override
        // handleConfigureNotify() - too many!
        XConfigureEvent xe = xev.get_xconfigure();
        checkIfOnNewScreen(new Rectangle(xe.get_x(),
                                         xe.get_y(),
                                         xe.get_width(),
                                         xe.get_height()));

        // Don't call super until we've handled a screen change.  Otherwise
        // there could be a race condition in which a ComponentListener could
        // see the old screen.
        super.handleConfigureNotifyEvent(xev);
        repositionSecurityWarning();
    }

    final void requestXFocus(long time)
    {
        requestXFocus(time, true);
    }

    final void requestXFocus()
    {
        requestXFocus(0, false);
    }

    /**
     * Requests focus to this top-level. Descendants should override to provide
     * implementations based on a class of top-level.
     */
    protected void requestXFocus(long time, boolean timeProvided)
    {
        // Since in XAWT focus is synthetic and all basic Windows are
        // override_redirect all we can do is check whether our parent
        // is active. If it is - we can freely synthesize focus transfer.
        // Luckily, this logic is already implemented in requestWindowFocus.
        if (focusLog.isLoggable(Level.FINE)) focusLog.fine("Requesting window focus");
        requestWindowFocus(time, timeProvided);
    }

    public final boolean focusAllowedFor()
    {
        if (isNativelyNonFocusableWindow())
        {
            return false;
        }
        /*
                Window target = (Window)this.target;
                if (!target.isVisible() ||
                    !target.isEnabled() ||
                    !target.isFocusable())
                {
                    return false;
                }
        */
        if (isModalBlocked())
        {
            return false;
        }
        return true;
    }

    public void handleFocusEvent(XEvent xev)
    {
        XFocusChangeEvent xfe = xev.get_xfocus();
        FocusEvent fe;
        if (focusLog.isLoggable(Level.FINER))
        {
            focusLog.log(Level.FINER, "{0}", new Object[] {String.valueOf(xfe)});
        }
        if (isEventDisabled(xev))
        {
            return;
        }
        if (xev.get_type() == XlibWrapper.FocusIn)
        {
            // If this window is non-focusable don't post any java focus event
            if (focusAllowedFor())
            {
                if (xfe.get_mode() == XlibWrapper.NotifyNormal // Normal notify
                        || xfe.get_mode() == XlibWrapper.NotifyWhileGrabbed) // Alt-Tab notify
                {
                    handleWindowFocusIn(xfe.get_serial());
                }
            }
        }
        else
        {
            if (xfe.get_mode() == XlibWrapper.NotifyNormal // Normal notify
                    || xfe.get_mode() == XlibWrapper.NotifyWhileGrabbed) // Alt-Tab notify
            {
                // If this window is non-focusable don't post any java focus event
                if (!isNativelyNonFocusableWindow())
                {
                    XWindowPeer oppositeXWindow = getNativeFocusedWindowPeer();
                    Object oppositeTarget = (oppositeXWindow!=null)? oppositeXWindow.getTarget() : null;
                    Window oppositeWindow = null;
                    if (oppositeTarget instanceof Window)
                    {
                        oppositeWindow = (Window) oppositeTarget;
                    }
                    // Check if opposite window is non-focusable. In that case we don't want to
                    // post any event.
                    if (oppositeXWindow != null && oppositeXWindow.isNativelyNonFocusableWindow())
                    {
                        return;
                    }
                    if (this == oppositeXWindow)
                    {
                        oppositeWindow = null;
                    }
                    else if (oppositeXWindow instanceof XDecoratedPeer)
                    {
                        if (((XDecoratedPeer) oppositeXWindow).actualFocusedWindow != null)
                        {
                            oppositeXWindow = ((XDecoratedPeer) oppositeXWindow).actualFocusedWindow;
                            oppositeTarget = oppositeXWindow.getTarget();
                            if (oppositeTarget instanceof Window
                                    && oppositeXWindow.isVisible()
                                    && oppositeXWindow.isNativelyNonFocusableWindow())
                            {
                                oppositeWindow = ((Window) oppositeTarget);
                            }
                        }
                    }
                    handleWindowFocusOut(oppositeWindow, xfe.get_serial());
                }
            }
        }
    }

    void setSaveUnder(boolean state) {}

    public void toFront()
    {
        if (isOverrideRedirect() && mustControlStackPosition)
        {
            mustControlStackPosition = false;
            removeRootPropertyEventDispatcher();
        }
        if (isVisible())
        {
            super.toFront();
            if (isFocusableWindow() && isAutoRequestFocus() &&
                    !isModalBlocked() && !isWithdrawn())
            {
                requestInitialFocus();
            }
        }
        else
        {
            setVisible(true);
        }
    }

    public void toBack()
    {
        XToolkit.awtLock();
        try
        {
            if(!isOverrideRedirect())
            {
                XlibWrapper.XLowerWindow(XToolkit.getDisplay(), getWindow());
            }
            else
            {
                lowerOverrideRedirect();
            }
        }
        finally
        {
            XToolkit.awtUnlock();
        }
    }
    private void lowerOverrideRedirect()
    {
        //
        // make new hash of toplevels of all windows from 'windows' hash.
        // FIXME: do not call them "toplevel" as it is misleading.
        //
        HashSet toplevels = new HashSet();
        long topl = 0, mytopl = 0;

        for (XWindowPeer xp : windows)
        {
            topl = getToplevelWindow( xp.getWindow() );
            if( xp.equals( this ) )
            {
                mytopl = topl;
            }
            if( topl > 0 )
                toplevels.add( Long.valueOf( topl ) );
        }

        //
        // find in the root's tree:
        // (1) my toplevel, (2) lowest java toplevel, (3) desktop
        // We must enforce (3), (1), (2) order, upward;
        // note that nautilus on the next restacking will do (1),(3),(2).
        //
        long laux,     wDesktop = -1, wBottom = -1;
        int  iMy = -1, iDesktop = -1, iBottom = -1;
        int i = 0;
        XQueryTree xqt = new XQueryTree(XToolkit.getDefaultRootWindow());
        try
        {
            if( xqt.execute() > 0 )
            {
                int nchildren = xqt.get_nchildren();
                long children = xqt.get_children();
                for(i = 0; i < nchildren; i++)
                {
                    laux = Native.getWindow(children, i);
                    if( laux == mytopl )
                    {
                        iMy = i;
                    }
                    else if( isDesktopWindow( laux ) )
                    {
                        // we need topmost desktop of them all.
                        iDesktop = i;
                        wDesktop = laux;
                    }
                    else if(iBottom < 0 &&
                            toplevels.contains( Long.valueOf(laux) ) &&
                            laux != mytopl)
                    {
                        iBottom = i;
                        wBottom = laux;
                    }
                }
            }

            if( (iMy < iBottom || iBottom < 0 )&& iDesktop < iMy)
                return; // no action necessary

            long to_restack = Native.allocateLongArray(2);
            Native.putLong(to_restack, 0, wBottom);
            Native.putLong(to_restack, 1,  mytopl);
            XlibWrapper.XRestackWindows(XToolkit.getDisplay(), to_restack, 2);
            XlibWrapper.unsafe.freeMemory(to_restack);


            if( !mustControlStackPosition )
            {
                mustControlStackPosition = true;
                // add root window property listener:
                // somebody (eg nautilus desktop) may obscure us
                addRootPropertyEventDispatcher();
            }
        }
        finally
        {
            xqt.dispose();
        }
    }
    /**
        Get XID of closest to root window in a given window hierarchy.
        FIXME: do not call it "toplevel" as it is misleading.
        On error return 0.
    */
    private long getToplevelWindow( long w )
    {
        long wi = w, ret, root;
        do
        {
            ret = wi;
            XQueryTree qt = new XQueryTree(wi);
            try
            {
                if (qt.execute() == 0)
                {
                    return 0;
                }
                root = qt.get_root();
                wi = qt.get_parent();
            }
            finally
            {
                qt.dispose();
            }

        }
        while (wi != root);

        return ret;
    }
    private boolean isDesktopWindow( long wi )
    {
        return XWM.getWM().isDesktopWindow( wi );
    }

    private void updateAlwaysOnTop()
    {
        log.log(Level.FINE, "Promoting always-on-top state {0}", Boolean.valueOf(alwaysOnTop));
        XWM.getWM().setLayer(this,
                             alwaysOnTop ?
                             XLayerProtocol.LAYER_ALWAYS_ON_TOP :
                             XLayerProtocol.LAYER_NORMAL);
    }

    public void setAlwaysOnTop(boolean alwaysOnTop)
    {
        this.alwaysOnTop = alwaysOnTop;
        updateAlwaysOnTop();
    }

    boolean isLocationByPlatform()
    {
        return locationByPlatform;
    }

    private void promoteDefaultPosition()
    {
        this.locationByPlatform = ((Window)target).isLocationByPlatform();
        if (locationByPlatform)
        {
            XToolkit.awtLock();
            try
            {
                Rectangle bounds = getBounds();
                XSizeHints hints = getHints();
                setSizeHints(hints.get_flags() & ~(USPosition | PPosition),
                             bounds.x, bounds.y, bounds.width, bounds.height);
            }
            finally
            {
                XToolkit.awtUnlock();
            }
        }
    }

    public void setVisible(boolean vis)
    {
        if (!isVisible() && vis)
        {
            isBeforeFirstMapNotify = true;
            winAttr.initialFocus = isAutoRequestFocus();
            if (!winAttr.initialFocus)
            {
                /*
                 * It's easier and safer to temporary suppress WM_TAKE_FOCUS
                 * protocol itself than to ignore WM_TAKE_FOCUS client message.
                 * Because we will have to make the difference between
                 * the message come after showing and the message come after
                 * activation. Also, on Metacity, for some reason, we have _two_
                 * WM_TAKE_FOCUS client messages when showing a frame/dialog.
                 */
                suppressWmTakeFocus(true);
            }
        }
        updateFocusability();
        promoteDefaultPosition();
        if (!vis && warningWindow != null)
        {
            warningWindow.setSecurityWarningVisible(false, false);
        }
        super.setVisible(vis);
        if (!vis && !isWithdrawn())
        {
            // ICCCM, 4.1.4. Changing Window State:
            // "Iconic -> Withdrawn - The client should unmap the window and follow it
            // with a synthetic UnmapNotify event as described later in this section."
            // The same is true for Normal -> Withdrawn
            XToolkit.awtLock();
            try
            {
                XUnmapEvent unmap = new XUnmapEvent();
                unmap.set_window(window);
                unmap.set_event(XToolkit.getDefaultRootWindow());
                unmap.set_type((int)XlibWrapper.UnmapNotify);
                unmap.set_from_configure(false);
                XlibWrapper.XSendEvent(XToolkit.getDisplay(), XToolkit.getDefaultRootWindow(),
                                       false, XlibWrapper.SubstructureNotifyMask | XlibWrapper.SubstructureRedirectMask,
                                       unmap.pData);
                unmap.dispose();
            }
            finally
            {
                XToolkit.awtUnlock();
            }
        }
        // method called somewhere in parent does not generate configure-notify
        // event for override-redirect.
        // Ergo, no reshape and bugs like 5085647 in case setBounds was
        // called before setVisible.
        if (isOverrideRedirect() && vis)
        {
            updateChildrenSizes();
        }
        repositionSecurityWarning();
    }

    protected void suppressWmTakeFocus(boolean doSuppress)
    {
    }

    final boolean isSimpleWindow()
    {
        return !(target instanceof Frame || target instanceof Dialog);
    }
    boolean hasWarningWindow()
    {
        return ((Window)target).getWarningString() != null;
    }

    // The height of menu bar window
    int getMenuBarHeight()
    {
        return 0;
    }

    // Called when shell changes its size and requires children windows
    // to update their sizes appropriately
    void updateChildrenSizes()
    {
    }

    public void repositionSecurityWarning()
    {
        // NOTE: On KWin if the window/border snapping option is enabled,
        // the Java window may be swinging while it's being moved.
        // This doesn't make the application unusable though looks quite ugly.
        // Probobly we need to find some hint to assign to our Security
        // Warning window in order to exclude it from the snapping option.
        // We are not currently aware of existance of such a property.
        if (warningWindow != null)
        {
            // We can't use the coordinates stored in the XBaseWindow since
            // they are zeros for decorated frames.
            int x = ComponentAccessor.getX(target);
            int y = ComponentAccessor.getY(target);
            int width = ComponentAccessor.getWidth(target);
            int height = ComponentAccessor.getHeight(target);
            warningWindow.reposition(x, y, width, height);
        }
    }

    @Override
    protected void setMouseAbove(boolean above)
    {
        super.setMouseAbove(above);
        updateSecurityWarningVisibility();
    }

    public void updateSecurityWarningVisibility()
    {
        if (warningWindow == null)
        {
            return;
        }

        boolean show = false;

        int state = getWMState();

        if (!isVisible())
        {
            return; // The warning window should already be hidden.
        }

        // getWMState() always returns 0 (Withdrawn) for simple windows. Hence
        // we ignore the state for such windows.
        if (isVisible() && (state == XUtilConstants.NormalState || isSimpleWindow()))
        {
            if (XKeyboardFocusManagerPeer.getCurrentNativeFocusedWindow() ==
                    getTarget())
            {
                show = true;
            }

            if (isMouseAbove() || warningWindow.isMouseAbove())
            {
                show = true;
            }
        }

        warningWindow.setSecurityWarningVisible(show);
    }

    boolean isOverrideRedirect()
    {
        return (XWM.getWMID() == XWM.OPENLOOK_WM ? true : false) ||
               (XWM.getWMID() == XWM.METACITY_WM ? true : false) ||
               (XWM.getWMID() == XWM.COMPIZ_WM ? true : false) ||
               target.getName().equals("###overrideRedirect###") ||
               ((XToolkit)Toolkit.getDefaultToolkit()).isOverrideRedirect((Window)target) ||
               XTrayIconPeer.isTrayIconStuffWindow((Window)target);
    }

    final boolean isOLWMDecorBug()
    {
        return XWM.getWMID() == XWM.OPENLOOK_WM &&
               winAttr.nativeDecor == false;
    }

    public void dispose()
    {
        SunToolkit.awtLock();
        try
        {
            windows.remove(this);
        }
        finally
        {
            SunToolkit.awtUnlock();
        }
        if (warningWindow != null)
        {
            warningWindow.destroy();
        }
        removeRootPropertyEventDispatcher();
        mustControlStackPosition = false;
        super.dispose();

        /*
         * Fix for 6457980.
         * When disposing an owned Window we should implicitly
         * return focus to its decorated owner because it won't
         * receive WM_TAKE_FOCUS.
         */
        if (isSimpleWindow())
        {
            if (target == XKeyboardFocusManagerPeer.getCurrentNativeFocusedWindow())
            {
                Window owner = getDecoratedOwner((Window)target);
                ((XWindowPeer)ComponentAccessor.getPeer(owner)).requestWindowFocus();
            }
        }
    }
    boolean isResizable()
    {
        return winAttr.isResizable;
    }

    public void handleVisibilityEvent(XEvent xev)
    {
        super.handleVisibilityEvent(xev);
        XVisibilityEvent ve = xev.get_xvisibility();
        winAttr.visibilityState = ve.get_state();
//         if (ve.get_state() == XlibWrapper.VisibilityUnobscured) {
//             // raiseInputMethodWindow
//         }
        repositionSecurityWarning();
    }

    void handleRootPropertyNotify(XEvent xev)
    {
        XPropertyEvent ev = xev.get_xproperty();
        if( mustControlStackPosition &&
                ev.get_atom() == XAtom.get("_NET_CLIENT_LIST_STACKING").getAtom())
        {
            // Restore stack order unhadled/spoiled by WM or some app (nautilus).
            // As of now, don't use any generic machinery: just
            // do toBack() again.
            if(isOverrideRedirect())
            {
                toBack();
            }
        }
    }

    public void handleMapNotifyEvent(XEvent xev)
    {
        // See 6480534.
        isUnhiding |= isWMStateNetHidden();

        super.handleMapNotifyEvent(xev);
        if (!winAttr.initialFocus)
        {
            suppressWmTakeFocus(false); // restore the protocol.
            /*
             * For some reason, on Metacity, a frame/dialog being shown
             * without WM_TAKE_FOCUS protocol doesn't get moved to the front.
             * So, we do it evidently.
             */
            XToolkit.awtLock();
            try
            {
                XlibWrapper.XRaiseWindow(XToolkit.getDisplay(), getWindow());
            }
            finally
            {
                XToolkit.awtUnlock();
            }
        }
        if (shouldFocusOnMapNotify())
        {
            focusLog.fine("Automatically request focus on window");
            requestInitialFocus();
        }
        isUnhiding = false;
        isBeforeFirstMapNotify = false;
        updateAlwaysOnTop();

        synchronized (getStateLock())
        {
            if (!isMapped)
            {
                isMapped = true;
            }
        }
    }

    public void handleUnmapNotifyEvent(XEvent xev)
    {
        super.handleUnmapNotifyEvent(xev);

        // On Metacity UnmapNotify comes before PropertyNotify (for _NET_WM_STATE_HIDDEN).
        // So we also check for the property later in MapNotify. See 6480534.
        isUnhiding |= isWMStateNetHidden();

        synchronized (getStateLock())
        {
            if (isMapped)
            {
                isMapped = false;
            }
        }
    }

    private boolean shouldFocusOnMapNotify()
    {
        boolean res = false;

        if (isBeforeFirstMapNotify)
        {
            res = (winAttr.initialFocus ||          // Window.autoRequestFocus
                   isFocusedWindowModalBlocker());
        }
        else
        {
            res = isUnhiding;                       // Unhiding
        }
        res = res &&
              isFocusableWindow() &&                  // General focusability
              !isModalBlocked();                      // Modality

        return res;
    }

    private boolean isWMStateNetHidden()
    {
        XNETProtocol protocol = XWM.getWM().getNETProtocol();
        return (protocol != null && protocol.isWMStateNetHidden(this));
    }

    protected void requestInitialFocus()
    {
        requestXFocus();
    }

    public void addToplevelStateListener(ToplevelStateListener l)
    {
        toplevelStateListeners.add(l);
    }

    public void removeToplevelStateListener(ToplevelStateListener l)
    {
        toplevelStateListeners.remove(l);
    }

    /**
     * Override this methods to get notifications when top-level window state changes. The state is
     * meant in terms of ICCCM: WithdrawnState, IconicState, NormalState
     */
    @Override
    protected void stateChanged(long time, int oldState, int newState)
    {
        // Fix for 6401700, 6412803
        // If this window is modal blocked, it is put into the transient_for
        // chain using prevTransientFor and nextTransientFor hints. However,
        // the real WM_TRANSIENT_FOR hint shouldn't be set for windows in
        // different WM states (except for owner-window relationship), so
        // if the window changes its state, its real WM_TRANSIENT_FOR hint
        // should be updated accordingly.
        updateTransientFor();

        for (ToplevelStateListener topLevelListenerTmp : toplevelStateListeners)
        {
            topLevelListenerTmp.stateChangedICCCM(oldState, newState);
        }

        updateSecurityWarningVisibility();
    }

    boolean isWithdrawn()
    {
        return getWMState() == XlibWrapper.WithdrawnState;
    }

    boolean hasDecorations(int decor)
    {
        if (!winAttr.nativeDecor)
        {
            return false;
        }
        else
        {
            int myDecor = winAttr.decorations;
            boolean hasBits = ((myDecor & decor) == decor);
            if ((myDecor & XWindowAttributesData.AWT_DECOR_ALL) != 0)
                return !hasBits;
            else
                return hasBits;
        }
    }

    void setReparented(boolean newValue)
    {
        super.setReparented(newValue);
        XToolkit.awtLock();
        try
        {
            if (isReparented() && delayedModalBlocking)
            {
                addToTransientFors((XDialogPeer) ComponentAccessor.getPeer(modalBlocker));
                delayedModalBlocking = false;
            }
        }
        finally
        {
            XToolkit.awtUnlock();
        }
    }

    /*
     * Returns a Vector of all Java top-level windows,
     * sorted by their current Z-order
     */
    static Vector<XWindowPeer> collectJavaToplevels()
    {
        Vector<XWindowPeer> javaToplevels = new Vector<XWindowPeer>();
        Vector<Long> v = new Vector<Long>();
        X11GraphicsEnvironment ge =
            (X11GraphicsEnvironment)GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        if (!ge.runningXinerama() && (gds.length > 1))
        {
            for (GraphicsDevice gd : gds)
            {
                int screen = ((X11GraphicsDevice)gd).getScreen();
                long rootWindow = XlibWrapper.RootWindow(XToolkit.getDisplay(), screen);
                v.add(rootWindow);
            }
        }
        else
        {
            v.add(XToolkit.getDefaultRootWindow());
        }
        final int windowsCount = windows.size();
        while ((v.size() > 0) && (javaToplevels.size() < windowsCount))
        {
            long win = v.remove(0);
            XQueryTree qt = new XQueryTree(win);
            try
            {
                if (qt.execute() != 0)
                {
                    int nchildren = qt.get_nchildren();
                    long children = qt.get_children();
                    // XQueryTree returns window children ordered by z-order
                    for (int i = 0; i < nchildren; i++)
                    {
                        long child = Native.getWindow(children, i);
                        XBaseWindow childWindow = XToolkit.windowToXWindow(child);
                        // filter out Java non-toplevels
                        if ((childWindow != null) && !(childWindow instanceof XWindowPeer))
                        {
                            continue;
                        }
                        else
                        {
                            v.add(child);
                        }
                        if (childWindow instanceof XWindowPeer)
                        {
                            XWindowPeer np = (XWindowPeer)childWindow;
                            javaToplevels.add(np);
                            // XQueryTree returns windows sorted by their z-order. However,
                            // if WM has not handled transient for hint for a child window,
                            // it may appear in javaToplevels before its owner. Move such
                            // children after their owners.
                            int k = 0;
                            XWindowPeer toCheck = javaToplevels.get(k);
                            while (toCheck != np)
                            {
                                XWindowPeer toCheckOwnerPeer = toCheck.getOwnerPeer();
                                if (toCheckOwnerPeer == np)
                                {
                                    javaToplevels.remove(k);
                                    javaToplevels.add(toCheck);
                                }
                                else
                                {
                                    k++;
                                }
                                toCheck = javaToplevels.get(k);
                            }
                        }
                    }
                }
            }
            finally
            {
                qt.dispose();
            }
        }
        return javaToplevels;
    }

    public void setModalBlocked(Dialog d, boolean blocked)
    {
        setModalBlocked(d, blocked, null);
    }
    public void setModalBlocked(Dialog d, boolean blocked,
                                Vector<XWindowPeer> javaToplevels)
    {
        XToolkit.awtLock();
        try
        {
            // State lock should always be after awtLock
            synchronized(getStateLock())
            {
                XDialogPeer blockerPeer = (XDialogPeer) ComponentAccessor.getPeer(d);
                if (blocked)
                {
                    if (log.isLoggable(Level.FINE))
                    {
                        log.log(Level.FINE, "{0} is blocked by {1}",
                                new Object[] { String.valueOf(this), String.valueOf(blockerPeer)});
                    }
                    modalBlocker = d;

                    if (isReparented() || XWM.isNonReparentingWM())
                    {
                        addToTransientFors(blockerPeer, javaToplevels);
                    }
                    else
                    {
                        delayedModalBlocking = true;
                    }
                }
                else
                {
                    if (d != modalBlocker)
                    {
                        throw new IllegalStateException("Trying to unblock window blocked by another dialog");
                    }
                    modalBlocker = null;

                    if (isReparented() || XWM.isNonReparentingWM())
                    {
                        removeFromTransientFors();
                    }
                    else
                    {
                        delayedModalBlocking = false;
                    }
                }

                updateTransientFor();
            }
        }
        finally
        {
            XToolkit.awtUnlock();
        }
    }

    /*
     * Sets the TRANSIENT_FOR hint to the given top-level window. This
     *  method is used when a window is modal blocked/unblocked or
     *  changed its state from/to NormalState to/from other states.
     * If window or transientForWindow are embedded frames, the containing
     *  top-level windows are used.
     *
     * @param window specifies the top-level window that the hint
     *  is to be set to
     * @param transientForWindow the top-level window
     * @param updateChain specifies if next/prevTransientFor fields are
     *  to be updated
     * @param allStates if set to <code>true</code> then TRANSIENT_FOR hint
     *  is set regardless of the state of window and transientForWindow,
     *  otherwise it is set only if both are in the same state
     */
    static void setToplevelTransientFor(XWindowPeer window, XWindowPeer transientForWindow,
                                        boolean updateChain, boolean allStates)
    {
        if ((window == null) || (transientForWindow == null))
        {
            return;
        }
        if (updateChain)
        {
            window.prevTransientFor = transientForWindow;
            transientForWindow.nextTransientFor = window;
        }
        if (window.curRealTransientFor == transientForWindow)
        {
            return;
        }
        if (!allStates && (window.getWMState() != transientForWindow.getWMState()))
        {
            return;
        }
        if (window.getScreenNumber() != transientForWindow.getScreenNumber())
        {
            return;
        }
        long bpw = window.getWindow();
        while (!XlibUtil.isToplevelWindow(bpw) && !XlibUtil.isXAWTToplevelWindow(bpw))
        {
            bpw = XlibUtil.getParentWindow(bpw);
        }
        long tpw = transientForWindow.getWindow();
        while (!XlibUtil.isToplevelWindow(tpw) && !XlibUtil.isXAWTToplevelWindow(tpw))
        {
            tpw = XlibUtil.getParentWindow(tpw);
        }
        XlibWrapper.XSetTransientFor(XToolkit.getDisplay(), bpw, tpw);
        window.curRealTransientFor = transientForWindow;
    }

    /*
     * This method does nothing if this window is not blocked by any modal dialog.
     * For modal blocked windows this method looks up for the nearest
     *  prevTransiendFor window that is in the same state (Normal/Iconified/Withdrawn)
     *  as this one and makes this window transient for it. The same operation is
     *  performed for nextTransientFor window.
     * Values of prevTransientFor and nextTransientFor fields are not changed.
     */
    void updateTransientFor()
    {
        int state = getWMState();
        XWindowPeer p = prevTransientFor;
        while ((p != null) && ((p.getWMState() != state) || (p.getScreenNumber() != getScreenNumber())))
        {
            p = p.prevTransientFor;
        }
        if (p != null)
        {
            setToplevelTransientFor(this, p, false, false);
        }
        else
        {
            restoreTransientFor(this);
        }
        XWindowPeer n = nextTransientFor;
        while ((n != null) && ((n.getWMState() != state) || (n.getScreenNumber() != getScreenNumber())))
        {
            n = n.nextTransientFor;
        }
        if (n != null)
        {
            setToplevelTransientFor(n, this, false, false);
        }
    }

    /*
     * Removes the TRANSIENT_FOR hint from the given top-level window.
     * If window or transientForWindow are embedded frames, the containing
     *  top-level windows are used.
     *
     * @param window specifies the top-level window that the hint
     *  is to be removed from
     */
    private static void removeTransientForHint(XWindowPeer window)
    {
        XAtom XA_WM_TRANSIENT_FOR = XAtom.get(XAtom.XA_WM_TRANSIENT_FOR);
        long bpw = window.getWindow();
        while (!XlibUtil.isToplevelWindow(bpw) && !XlibUtil.isXAWTToplevelWindow(bpw))
        {
            bpw = XlibUtil.getParentWindow(bpw);
        }
        XlibWrapper.XDeleteProperty(XToolkit.getDisplay(), bpw, XA_WM_TRANSIENT_FOR.getAtom());
        window.curRealTransientFor = null;
    }

    /*
     * When a modal dialog is shown, all its blocked windows are lined up into
     *  a chain in such a way that each window is a transient_for window for
     *  the next one. That allows us to keep the modal dialog above all its
     *  blocked windows (even if there are some another modal dialogs between
     *  them).
     * This method adds this top-level window to the chain of the given modal
     *  dialog. To keep the current relative z-order, we should use the
     *  XQueryTree to find the place to insert this window to. As each window
     *  can be blocked by only one modal dialog (such checks are performed in
     *  shared code), both this and blockerPeer are on the top of their chains
     *  (chains may be empty).
     * If this window is a modal dialog and has its own chain, these chains are
     *  merged according to the current z-order (XQueryTree is used again).
     *  Below are some simple examples (z-order is from left to right, -- is
     *  modal blocking).
     *
     * Example 0:
     *     T (current chain of this, no windows are blocked by this)
     *  W1---B (current chain of blockerPeer, W2 is blocked by blockerPeer)
     *  Result is:
     *  W1-T-B (merged chain, all the windows are blocked by blockerPeer)
     *
     * Example 1:
     *  W1-T (current chain of this, W1 is blocked by this)
     *       W2-B (current chain of blockerPeer, W2 is blocked by blockerPeer)
     *  Result is:
     *  W1-T-W2-B (merged chain, all the windows are blocked by blockerPeer)
     *
     * Example 2:
     *  W1----T (current chain of this, W1 is blocked by this)
     *     W2---B (current chain of blockerPeer, W2 is blocked by blockerPeer)
     *  Result is:
     *  W1-W2-T-B (merged chain, all the windows are blocked by blockerPeer)
     *
     * This method should be called under the AWT lock.
     *
     * @see #removeFromTransientFors
     * @see #setModalBlocked
     */
    private void addToTransientFors(XDialogPeer blockerPeer)
    {
        addToTransientFors(blockerPeer, null);
    }

    private void addToTransientFors(XDialogPeer blockerPeer, Vector<XWindowPeer> javaToplevels)
    {
        // blockerPeer chain iterator
        XWindowPeer blockerChain = blockerPeer;
        while (blockerChain.prevTransientFor != null)
        {
            blockerChain = blockerChain.prevTransientFor;
        }
        // this window chain iterator
        // each window can be blocked no more than once, so this window
        //   is on top of its chain
        XWindowPeer thisChain = this;
        while (thisChain.prevTransientFor != null)
        {
            thisChain = thisChain.prevTransientFor;
        }
        // if there are no windows blocked by modalBlocker, simply add this window
        //  and its chain to blocker's chain
        if (blockerChain == blockerPeer)
        {
            setToplevelTransientFor(blockerPeer, this, true, false);
        }
        else
        {
            // Collect all the Java top-levels, if required
            if (javaToplevels == null)
            {
                javaToplevels = collectJavaToplevels();
            }
            // merged chain tail
            XWindowPeer mergedChain = null;
            for (XWindowPeer w : javaToplevels)
            {
                XWindowPeer prevMergedChain = mergedChain;
                if (w == thisChain)
                {
                    if (thisChain == this)
                    {
                        if (prevMergedChain != null)
                        {
                            setToplevelTransientFor(this, prevMergedChain, true, false);
                        }
                        setToplevelTransientFor(blockerChain, this, true, false);
                        break;
                    }
                    else
                    {
                        mergedChain = thisChain;
                        thisChain = thisChain.nextTransientFor;
                    }
                }
                else if (w == blockerChain)
                {
                    mergedChain = blockerChain;
                    blockerChain = blockerChain.nextTransientFor;
                }
                else
                {
                    continue;
                }
                if (prevMergedChain == null)
                {
                    mergedChain.prevTransientFor = null;
                }
                else
                {
                    setToplevelTransientFor(mergedChain, prevMergedChain, true, false);
                    mergedChain.updateTransientFor();
                }
                if (blockerChain == blockerPeer)
                {
                    setToplevelTransientFor(thisChain, mergedChain, true, false);
                    setToplevelTransientFor(blockerChain, this, true, false);
                    break;
                }
            }
        }

        XToolkit.XSync();
    }

    static void restoreTransientFor(XWindowPeer window)
    {
        XWindowPeer ownerPeer = window.getOwnerPeer();
        if (ownerPeer != null)
        {
            setToplevelTransientFor(window, ownerPeer, false, true);
        }
        else
        {
            removeTransientForHint(window);
        }
    }

    /*
     * When a window is modally unblocked, it should be removed from its blocker
     *  chain, see {@link #addToTransientFor addToTransientFors} method for the
     *  chain definition.
     * The problem is that we cannot simply restore window's original
     *  TRANSIENT_FOR hint (if any) and link prevTransientFor and
     *  nextTransientFor together as the whole chain could be created as a merge
     *  of two other chains in addToTransientFors. In that case, if this window is
     *  a modal dialog, it would lost all its own chain, if we simply exclude it
     *  from the chain.
     * The correct behaviour of this method should be to split the chain, this
     *  window is currently in, into two chains. First chain is this window own
     *  chain (i. e. all the windows blocked by this one, directly or indirectly),
     *  if any, and the rest windows from the current chain.
     *
     * Example:
     *  Original state:
     *   W1-B1 (window W1 is blocked by B1)
     *   W2-B2 (window W2 is blocked by B2)
     *  B3 is shown and blocks B1 and B2:
     *   W1-W2-B1-B2-B3 (a single chain after B1.addToTransientFors() and B2.addToTransientFors())
     *  If we then unblock B1, the state should be:
     *   W1-B1 (window W1 is blocked by B1)
     *   W2-B2-B3 (window W2 is blocked by B2 and B2 is blocked by B3)
     *
     * This method should be called under the AWT lock.
     *
     * @see #addToTransientFors
     * @see #setModalBlocked
     */
    private void removeFromTransientFors()
    {
        // the head of the chain of this window
        XWindowPeer thisChain = this;
        // the head of the current chain
        // nextTransientFor is always not null as this window is in the chain
        XWindowPeer otherChain = nextTransientFor;
        // the set of blockers in this chain: if this dialog blocks some other
        // modal dialogs, their blocked windows should stay in this dialog's chain
        Set<XWindowPeer> thisChainBlockers = new HashSet<XWindowPeer>();
        thisChainBlockers.add(this);
        // current chain iterator in the order from next to prev
        XWindowPeer chainToSplit = prevTransientFor;
        while (chainToSplit != null)
        {
            XWindowPeer blocker = (XWindowPeer) ComponentAccessor.getPeer(chainToSplit.modalBlocker);
            if (thisChainBlockers.contains(blocker))
            {
                // add to this dialog's chain
                setToplevelTransientFor(thisChain, chainToSplit, true, false);
                thisChain = chainToSplit;
                thisChainBlockers.add(chainToSplit);
            }
            else
            {
                // leave in the current chain
                setToplevelTransientFor(otherChain, chainToSplit, true, false);
                otherChain = chainToSplit;
            }
            chainToSplit = chainToSplit.prevTransientFor;
        }
        restoreTransientFor(thisChain);
        thisChain.prevTransientFor = null;
        restoreTransientFor(otherChain);
        otherChain.prevTransientFor = null;
        nextTransientFor = null;

        XToolkit.XSync();
    }

    boolean isModalBlocked()
    {
        return modalBlocker != null;
    }

    static Window getDecoratedOwner(Window window)
    {
        while ((null != window) && !(window instanceof Frame || window instanceof Dialog))
        {
            window = (Window) ComponentAccessor.getParent_NoClientCode(window);
        }
        return window;
    }

    public boolean requestWindowFocus()
    {
        return requestWindowFocus(0, false);
    }

    public boolean requestWindowFocus(long time, boolean timeProvided)
    {
        focusLog.fine("Request for window focus");
        // If this is Frame or Dialog we can't assure focus request success - but we still can try
        // If this is Window and its owner Frame is active we can be sure request succedded.
        Window win = (Window) target;
        Window owner = XWindowPeer.getDecoratedOwner(win);

        final Window activeWindow =
            XWindowPeer.getDecoratedOwner(XKeyboardFocusManagerPeer.getCurrentNativeFocusedWindow());
        if (activeWindow == owner)
        {
            focusLog.fine("Parent window is active - generating focus for this window");
            handleWindowFocusInSync(-1);
            return true;
        }
        else
        {
            focusLog.fine("Parent window is not active");
        }
        ComponentPeer peer = ComponentAccessor.getPeer(owner);
        if (peer instanceof XDecoratedPeer)
        {
            XDecoratedPeer wpeer = (XDecoratedPeer) peer;
            if (wpeer.requestWindowFocus(this, time, timeProvided))
            {
                focusLog.fine("Parent window accepted focus request - generating focus for this window");
                return true;
            }
        }
        focusLog.fine("Denied - parent window is not active and didn't accept focus request");
        return false;
    }

    // This method is to be overriden in XDecoratedPeer.
    void setActualFocusedWindow(XWindowPeer actualFocusedWindow)
    {
    }

    public void xSetVisible(boolean visible)
    {
        if (log.isLoggable(Level.FINE)) log.fine("Setting visible on " + this + " to " + visible);
        XToolkit.awtLock();
        try
        {
            this.visible = visible;
            if (visible)
            {
                XlibWrapper.XMapRaised(XToolkit.getDisplay(), getWindow());
            }
            else
            {
                XlibWrapper.XUnmapWindow(XToolkit.getDisplay(), getWindow());
            }
            XlibWrapper.XFlush(XToolkit.getDisplay());
        }
        finally
        {
            XToolkit.awtUnlock();
        }
    }

    private int dropTargetCount = 0;

    public synchronized void addDropTarget()
    {
        if (dropTargetCount == 0)
        {
            long window = getWindow();
            if (window != 0)
            {
                XDropTargetRegistry.getRegistry().registerDropSite(window);
            }
        }
        dropTargetCount++;
    }

    public synchronized void removeDropTarget()
    {
        dropTargetCount--;
        if (dropTargetCount == 0)
        {
            long window = getWindow();
            if (window != 0)
            {
                XDropTargetRegistry.getRegistry().unregisterDropSite(window);
            }
        }
    }
    void addRootPropertyEventDispatcher()
    {
        if( rootPropertyEventDispatcher == null )
        {
            rootPropertyEventDispatcher = new XEventDispatcher()
            {
                public void dispatchEvent(XEvent ev)
                {
                    if( ev.get_type() == PropertyNotify )
                    {
                        handleRootPropertyNotify( ev );
                    }
                }
            };
            XlibWrapper.XSelectInput( XToolkit.getDisplay(),
                                      XToolkit.getDefaultRootWindow(),
                                      XlibWrapper.PropertyChangeMask);
            XToolkit.addEventDispatcher(XToolkit.getDefaultRootWindow(),
                                        rootPropertyEventDispatcher);
        }
    }
    void removeRootPropertyEventDispatcher()
    {
        if( rootPropertyEventDispatcher != null )
        {
            XToolkit.removeEventDispatcher(XToolkit.getDefaultRootWindow(),
                                           rootPropertyEventDispatcher);
            rootPropertyEventDispatcher = null;
        }
    }
    public void updateFocusableWindowState()
    {
        cachedFocusableWindow = isFocusableWindow();
    }

    XAtom XA_NET_WM_STATE;
    XAtomList net_wm_state;
    public XAtomList getNETWMState()
    {
        if (net_wm_state == null)
        {
            net_wm_state = XA_NET_WM_STATE.getAtomListPropertyList(this);
        }
        return net_wm_state;
    }

    public void setNETWMState(XAtomList state)
    {
        net_wm_state = state;
        if (state != null)
        {
            XA_NET_WM_STATE.setAtomListProperty(this, state);
        }
    }

    public PropMwmHints getMWMHints()
    {
        if (mwm_hints == null)
        {
            mwm_hints = new PropMwmHints();
            if (!XWM.XA_MWM_HINTS.getAtomData(getWindow(), mwm_hints.pData, PROP_MWM_HINTS_ELEMENTS))
            {
                mwm_hints.zero();
            }
        }
        return mwm_hints;
    }

    public void setMWMHints(PropMwmHints hints)
    {
        mwm_hints = hints;
        if (hints != null)
        {
            XWM.XA_MWM_HINTS.setAtomData(getWindow(), mwm_hints.pData, PROP_MWM_HINTS_ELEMENTS);
        }
    }

    private Insets wm_set_insets;
    public Insets getWMSetInsets(XAtom changedAtom)
    {
        if (isEmbedded())
        {
            return null;
        }

        if (wm_set_insets != null)
        {
            return wm_set_insets;
        }

        if (changedAtom == null)
        {
            wm_set_insets = XWM.getInsetsFromExtents(getWindow());
        }
        else
        {
            wm_set_insets = XWM.getInsetsFromProp(getWindow(), changedAtom);
        }

        if (insLog.isLoggable(Level.FINER))
        {
            insLog.log(Level.FINER, "FRAME_EXTENTS: {0}", new Object[] {String.valueOf(wm_set_insets)});
        }

        if (wm_set_insets != null)
        {
            handleWMSetInsets(wm_set_insets);
        }
        return wm_set_insets;
    }

    protected void handleWMSetInsets(Insets newInsets)
    {
        wm_set_insets = (Insets)newInsets.clone();
    }

    public void resetWMSetInsets()
    {
        wm_set_insets = null;
    }
    protected synchronized void updateDropTarget()
    {
        if (dropTargetCount > 0)
        {
            long window = getWindow();
            if (window != 0)
            {
                XDropTargetRegistry.getRegistry().unregisterDropSite(window);
                XDropTargetRegistry.getRegistry().registerDropSite(window);
            }
        }
    }

    public void setGrab(boolean grab)
    {
        this.grab = grab;
        if (grab)
        {
            pressTarget = this;
            grabInput();
        }
        else
        {
            ungrabInput();
        }
    }

    public boolean isGrabbed()
    {
        return grab && XAwtState.getGrabWindow() == this;
    }

    public void handleXCrossingEvent(XEvent xev)
    {
        XCrossingEvent xce = xev.get_xcrossing();
        if (grabLog.isLoggable(Level.FINE))
        {
            grabLog.log(Level.FINE, "{0}, when grabbed {1}, contains {2}",
                        new Object[] {String.valueOf(xce), isGrabbed(),
                                      containsGlobal(xce.get_x_root(), xce.get_y_root())
                                     });
        }
        if (isGrabbed())
        {
            // When window is grabbed, all events are dispatched to
            // it.  Retarget them to the corresponding windows (notice
            // that XBaseWindow.dispatchEvent does the opposite
            // translation)
            // Note that we need to retarget XCrossingEvents to content window
            // since it generates MOUSE_ENTERED/MOUSE_EXITED for frame and dialog.
            // (fix for 6390326)
            XBaseWindow target = XToolkit.windowToXWindow(xce.get_window());
            if (grabLog.isLoggable(Level.FINER))
            {
                grabLog.log(Level.FINER, "  -  Grab event target {0}",
                            new Object[] {String.valueOf(target)});
            }
            if (target != null && target != this)
            {
                target.dispatchEvent(xev);
                return;
            }
        }
        super.handleXCrossingEvent(xev);
    }

    public void handleMotionNotify(XEvent xev)
    {
        XMotionEvent xme = xev.get_xmotion();
        if (grabLog.isLoggable(Level.FINE))
        {
            grabLog.log(Level.FINER, "{0}, when grabbed {1}, contains {2}",
                        new Object[] {String.valueOf(xme), isGrabbed(),
                                      containsGlobal(xme.get_x_root(), xme.get_y_root())
                                     });
        }
        if (isGrabbed())
        {
            boolean dragging = (xme.get_state() & (Button1Mask | Button2Mask | Button3Mask)) != 0;
            // When window is grabbed, all events are dispatched to
            // it.  Retarget them to the corresponding windows (notice
            // that XBaseWindow.dispatchEvent does the opposite
            // translation)
            XBaseWindow target = XToolkit.windowToXWindow(xme.get_window());
            if (dragging && pressTarget != target)
            {
                // for some reasons if we grab input MotionNotify for drag is reported with target
                // to underlying window, not to window on which we have initiated drag
                // so we need to retarget them.  Here I use simplified logic which retarget all
                // such events to source of mouse press (or the grabber).  It helps with fix for 6390326.
                // So, I do not want to implement complicated logic for better retargeting.
                target = pressTarget.isVisible() ? pressTarget : this;
                xme.set_window(target.getWindow());
                xme.set_x(xme.get_x_root() - target.getX());
                xme.set_y(xme.get_y_root() - target.getY());
            }
            if (grabLog.isLoggable(Level.FINER))
            {
                grabLog.log(Level.FINER, "  -  Grab event target {0}",
                            new Object[] {String.valueOf(target)});
            }
            if (target != null)
            {
                if (target != getContentXWindow() && target != this)
                {
                    target.dispatchEvent(xev);
                    return;
                }
            }

            // note that we need to pass dragging events to the grabber (6390326)
            // see comment above for more inforamtion.
            if (!containsGlobal(xme.get_x_root(), xme.get_y_root()) && !dragging)
            {
                // Outside of Java
                return;
            }
        }
        super.handleMotionNotify(xev);
    }

    // we use it to retarget mouse drag and mouse release during grab.
    private XBaseWindow pressTarget = this;

    public void handleButtonPressRelease(XEvent xev)
    {
        XButtonEvent xbe = xev.get_xbutton();
        if (grabLog.isLoggable(Level.FINE))
        {
            grabLog.log(Level.FINE, "{0}, when grabbed {1}, contains {2} ({3}, {4}, {5}x{6})",
                        new Object[] {String.valueOf(xbe), isGrabbed(), containsGlobal(xbe.get_x_root(),
                                      xbe.get_y_root()), getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight()
                                     });
        }
        if (isGrabbed())
        {
            // When window is grabbed, all events are dispatched to
            // it.  Retarget them to the corresponding windows (notice
            // that XBaseWindow.dispatchEvent does the opposite
            // translation)
            XBaseWindow target = XToolkit.windowToXWindow(xbe.get_window());
            try
            {
                if (grabLog.isLoggable(Level.FINER))
                {
                    grabLog.log(Level.FINER, "  -  Grab event target {0} (press target {1})",
                                new Object[] {String.valueOf(target), String.valueOf(pressTarget)});
                }
                if (xbe.get_type() == XConstants.ButtonPress
                        && xbe.get_button() == XlibWrapper.Button1)
                {
                    // need to keep it to retarget mouse release
                    pressTarget = target;
                }
                else if (xbe.get_type() == XConstants.ButtonRelease
                         && xbe.get_button() == XlibWrapper.Button1
                         && pressTarget != target)
                {
                    // during grab we do receive mouse release on different component (not on the source
                    // of mouse press).  So we need to retarget it.
                    // see 6390326 for more information.
                    target = pressTarget.isVisible() ? pressTarget : this;
                    xbe.set_window(target.getWindow());
                    xbe.set_x(xbe.get_x_root() - target.getX());
                    xbe.set_y(xbe.get_y_root() - target.getY());
                    pressTarget = this;
                }
                if (target != null && target != getContentXWindow() && target != this)
                {
                    target.dispatchEvent(xev);
                    return;
                }
            }
            finally
            {
                if (target != null)
                {
                    // Target is either us or our content window -
                    // check that event is inside.  'Us' in case of
                    // shell will mean that this will also filter out press on title
                    if ((target == this || target == getContentXWindow()) && !containsGlobal(xbe.get_x_root(), xbe.get_y_root()))
                    {
                        // Outside this toplevel hierarchy
                        // According to the specification of UngrabEvent, post it
                        // when press occurs outside of the window and not on its owned windows
                        if (grabLog.isLoggable(Level.FINE))
                        {
                            grabLog.log(Level.FINE, "Generating UngrabEvent on {0} because not inside of shell",
                                        String.valueOf(this));
                        }
                        postEventToEventQueue(new sun.awt.UngrabEvent(getEventSource()));
                        return;
                    }
                    // First, get the toplevel
                    XWindowPeer toplevel = target.getToplevelXWindow();
                    if (toplevel != null)
                    {
                        Window w = (Window)toplevel.target;
                        while (w != null && toplevel != this && !(toplevel instanceof XDialogPeer))
                        {
                            w = (Window) ComponentAccessor.getParent_NoClientCode(w);
                            if (w != null)
                            {
                                toplevel = (XWindowPeer) ComponentAccessor.getPeer(w);
                            }
                        }
                        if (w == null || (w != this.target && w instanceof Dialog))
                        {
                            // toplevel == null - outside of
                            // hierarchy, toplevel is Dialog - should
                            // send ungrab (but shouldn't for Window)
                            if (grabLog.isLoggable(Level.FINE))
                            {
                                grabLog.log(Level.FINE, "Generating UngrabEvent on {0} because hierarchy ended",
                                            String.valueOf(this));
                            }
                            postEventToEventQueue(new sun.awt.UngrabEvent(getEventSource()));
                        }
                    }
                    else
                    {
                        // toplevel is null - outside of hierarchy
                        if (grabLog.isLoggable(Level.FINE))
                        {
                            grabLog.log(Level.FINE, "Generating UngrabEvent on {0} because toplevel is null",
                                        String.valueOf(this));
                        }
                        postEventToEventQueue(new sun.awt.UngrabEvent(getEventSource()));
                        return;
                    }
                }
                else
                {
                    // target doesn't map to XAWT window - outside of hierarchy
                    if (grabLog.isLoggable(Level.FINE))
                    {
                        grabLog.log(Level.FINE, "Generating UngrabEvent on because target is null {0}",
                                    String.valueOf(this));
                    }
                    postEventToEventQueue(new sun.awt.UngrabEvent(getEventSource()));
                    return;
                }
            }
        }
        super.handleButtonPressRelease(xev);
    }

    public void print(Graphics g)
    {
        // We assume we print the whole frame,
        // so we expect no clip was set previously
        Shape shape = AWTAccessor.getWindowAccessor().getShape((Window)target);
        if (shape != null)
        {
            g.setClip(shape);
        }
        super.print(g);
    }

    @Override
    public void setOpacity(float opacity)
    {
        final long maxOpacity = 0xffffffffl;
        long iOpacity = (long)(opacity * maxOpacity);
        if (iOpacity < 0)
        {
            iOpacity = 0;
        }
        if (iOpacity > maxOpacity)
        {
            iOpacity = maxOpacity;
        }

        XAtom netWmWindowOpacityAtom = XAtom.get("_NET_WM_WINDOW_OPACITY");

        if (iOpacity == maxOpacity)
        {
            netWmWindowOpacityAtom.DeleteProperty(getWindow());
        }
        else
        {
            netWmWindowOpacityAtom.setCard32Property(getWindow(), iOpacity);
        }
    }

    @Override
    public void setOpaque(boolean isOpaque)
    {
        // no-op
    }

    @Override
    public void updateWindow(BufferedImage backBuffer)
    {
        // no-op
    }
}
