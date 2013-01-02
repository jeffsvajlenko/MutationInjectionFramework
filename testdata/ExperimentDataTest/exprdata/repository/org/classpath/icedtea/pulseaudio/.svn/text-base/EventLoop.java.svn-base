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

import java.util.ArrayList;
import java.util.List;

import org.classpath.icedtea.pulseaudio.ContextEvent;
import org.classpath.icedtea.pulseaudio.Debug.DebugLevel;

/**
 * This class wraps pulseaudio's event loop. It also holds the lock used in the
 * rest of pulse-java
 */

final class EventLoop implements Runnable
{

    /*
     * any methods that can obstruct the behaviour of pa_mainloop should run
     * synchronized
     */

    /*
     * the threadLock object is the object used for synchronizing the
     * non-thread-safe operations of pulseaudio's c api
     */
    final Object threadLock = new Object();

    private static EventLoop instance = null;

    private List<ContextListener> contextListeners;
    // private List<SourceDataLine> lines;
    private String appName;
    private String serverString;

    private long status;
    // private boolean eventLoopIsRunning = false;

    private List<String> targetPortNameList = new ArrayList<String>();
    private List<String> sourcePortNameList = new ArrayList<String>();

    /*
     * JNI stuff
     *
     * Do not synchronize the individual functions, synchronize
     * block/method/lines around the call
     */

    private native void native_setup(String appName, String server);

    private native int native_iterate(int timeout);

    private native void native_shutdown();

    /*
     * These fields hold pointers
     */
    private byte[] contextPointer;
    private byte[] mainloopPointer;

    static
    {
        SecurityWrapper.loadNativeLibrary();
    }

    private EventLoop()
    {
        contextListeners = new ArrayList<ContextListener>();
    }

    synchronized static EventLoop getEventLoop()
    {
        if (instance == null)
        {
            instance = new EventLoop();
        }
        return instance;
    }

    void setAppName(String appName)
    {
        this.appName = appName;
    }

    void setServer(String serverString)
    {
        this.serverString = serverString;
    }

    @Override
    public void run()
    {
        native_setup(this.appName, this.serverString);

        Debug.println(DebugLevel.Info, "Eventloop.run(): eventloop starting");

        /*
         * Perhaps this loop should be written in C doing a Java to C call on
         * every iteration of the loop might be slow
         */
        while (true)
        {
            synchronized (threadLock)
            {
                // timeout is in milliseconds
                // timout = 0 means dont block
                native_iterate(100);

                if (Thread.interrupted())
                {
                    native_shutdown();

                    // clean up the listeners
                    synchronized (contextListeners)
                    {
                        contextListeners.clear();
                    }

                    Debug.println(DebugLevel.Info,
                                  "EventLoop.run(): event loop terminated");

                    return;

                }
            }
        }

    }

    void addContextListener(ContextListener contextListener)
    {
        synchronized (contextListeners)
        {
            contextListeners.add(contextListener);
        }
    }

    void removeContextListener(ContextListener contextListener)
    {
        synchronized (contextListeners)
        {
            contextListeners.remove(contextListener);
        }
    }

    long getStatus()
    {
        return this.status;
    }

    void update(long status)
    {
        synchronized (threadLock)
        {
            // System.out.println(this.getClass().getName()
            // + ".update() called! status = " + status);
            this.status = status;
            fireEvent(new ContextEvent(status));
        }

        if (status == ContextEvent.FAILED)
        {
            Debug.println(DebugLevel.Warning,
                          "EventLoop.update(): Context failed");
        }
    }

    private void fireEvent(final ContextEvent e)
    {
        // System.out.println(this.getClass().getName() + "firing event: "
        // + e.getType().toString());

        synchronized (contextListeners)
        {
            // System.out.println(contextListeners.size());
            for (ContextListener listener : contextListeners)
            {
                listener.update(e);
            }
        }

    }

    byte[] getContextPointer()
    {
        return contextPointer;
    }

    byte[] getMainLoopPointer()
    {
        return mainloopPointer;
    }

    private native byte[] nativeUpdateTargetPortNameList();

    private native byte[] nativeUpdateSourcePortNameList();

    synchronized List<String> updateTargetPortNameList()
    {
        targetPortNameList = new ArrayList<String>();
        Operation op;
        synchronized (this.threadLock)
        {
            op = new Operation(nativeUpdateTargetPortNameList());
        }

        op.waitForCompletion();

        assert (op.getState() == Operation.DONE);

        op.releaseReference();
        return targetPortNameList;
    }

    protected synchronized List<String> updateSourcePortNameList()
    {
        sourcePortNameList = new ArrayList<String>();
        Operation op;
        synchronized (this.threadLock)
        {
            op = new Operation(nativeUpdateSourcePortNameList());
        }

        op.waitForCompletion();

        assert (op.getState() == Operation.DONE);

        op.releaseReference();
        return sourcePortNameList;
    }

    public void source_callback(String name)
    {
        sourcePortNameList.add(name);
    }

    public void sink_callback(String name)
    {
        targetPortNameList.add(name);
    }
}
