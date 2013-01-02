/* Operation.java
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
 * Encapsulates a pa_operation object
 *
 * This is really needed only so that we can deallocate the reference counted
 * object. Any time a function returns an Operation object, the reference has
 * been incremented. The object wont be freed unless a releaseReference() is
 * done.
 *
 * Please see the pulseaudio api docs for more information on a pa_opreation
 * object
 *
 *
 *
 */

class Operation
{

    private byte[] operationPointer;
    private EventLoop eventLoop;

    // These should never be written to in java. They will be initialized
    // properly in native code.
    public static long RUNNING   = -1,
                       DONE      = -1,
                       CANCELLED = -1;

    private static native void init_constants();

    static
    {
        SecurityWrapper.loadNativeLibrary();
        init_constants();
    }

    // If value is not one of RUNNING, DONE, CANCELLED, throw an
    // IllegalStateException. Otherwise return the input.
    private static long checkNativeOperationState(long value)
    {
        if (!Arrays.asList(RUNNING, DONE, CANCELLED).contains(value))
        {
            throw new IllegalStateException("Unknown operation state: " + value);
        }
        return value;
    }

    private native void native_ref();

    private native void native_unref();

    private native long native_get_state();

    Operation(byte[] operationPointer)
    {
        assert (operationPointer != null);
        this.operationPointer = operationPointer;
        this.eventLoop = EventLoop.getEventLoop();
    }

    @Override
    protected void finalize() throws Throwable
    {
        // might catch operations which havent been released
        assert (operationPointer == null);
        super.finalize();
    }

    /**
     * Increase reference count by 1
     */
    void addReference()
    {
        assert (operationPointer != null);
        synchronized (eventLoop.threadLock)
        {
            native_ref();
        }
    }

    /**
     * Decrease reference count by 1. If the count reaches 0, object will be freed
     */
    void releaseReference()
    {
        assert (operationPointer != null);
        synchronized (eventLoop.threadLock)
        {
            native_unref();
        }
        operationPointer = null;
    }

    // FIXME broken function
    boolean isNull()
    {
        if (operationPointer == null)
        {
            return true;
        }
        return false;
    }

    long getState()
    {
        assert (operationPointer != null);
        synchronized (eventLoop.threadLock)
        {
            return checkNativeOperationState(native_get_state());
        }
    }

    /**
     * Block until the operation has completed
     *
     */
    void waitForCompletion()
    {
        assert (operationPointer != null);

        boolean interrupted = false;
        do
        {
            synchronized (eventLoop.threadLock)
            {
                if (getState() == DONE)
                {
                    return;
                }
                try
                {
                    eventLoop.threadLock.wait();
                }
                catch (InterruptedException e)
                {
                    // ingore the interrupt for now
                    interrupted = true;
                }
            }
        }
        while (getState() != DONE);

        // let the caller know about the interrupt
        if (interrupted)
        {
            Thread.currentThread().interrupt();
        }
    }
}
