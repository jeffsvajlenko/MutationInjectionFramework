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

package sun.java2d;

import java.awt.GraphicsConfiguration;
import java.awt.image.BufferedImage;
import sun.awt.X11GraphicsConfig;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.SurfaceManager;
import sun.awt.image.VolatileSurfaceManager;
import sun.java2d.opengl.GLXGraphicsConfig;
import sun.java2d.opengl.GLXVolatileSurfaceManager;
import sun.java2d.x11.X11VolatileSurfaceManager;
import sun.java2d.xr.XRGraphicsConfig;
import sun.java2d.xr.XRVolatileSurfaceManager;

/**
 * This is a factory class with static methods for creating a
 * platform-specific instance of a particular SurfaceManager.  Each platform
 * (Windows, Unix, etc.) has its own specialized SurfaceManagerFactory.
 */
public class SurfaceManagerFactory
{
    /**
     * Creates a new instance of a VolatileSurfaceManager given any
     * arbitrary SunVolatileImage.  An optional context Object can be supplied
     * as a way for the caller to pass pipeline-specific context data to
     * the VolatileSurfaceManager (such as a backbuffer handle, for example).
     *
     * For Unix platforms, this method returns either an X11-, XRender or GLX-
     * specific VolatileSurfaceManager based on the GraphicsConfiguration
     * under which the SunVolatileImage was created.
     */
    public static VolatileSurfaceManager
    createVolatileManager(SunVolatileImage vImg,
                          Object context)
    {
        GraphicsConfiguration gc = vImg.getGraphicsConfig();
        if (gc instanceof GLXGraphicsConfig)
        {
            return new GLXVolatileSurfaceManager(vImg, context);
        }
        else if(gc instanceof XRGraphicsConfig)
        {
            return new XRVolatileSurfaceManager(vImg, context);
        }
        else
        {
            return new X11VolatileSurfaceManager(vImg, context);
        }
    }
}
