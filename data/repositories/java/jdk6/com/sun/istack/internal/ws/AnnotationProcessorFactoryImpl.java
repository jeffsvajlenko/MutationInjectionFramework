/*
 * Copyright (c) 2005, 2006, Oracle and/or its affiliates. All rights reserved.
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
package com.sun.istack.internal.ws;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.sun.mirror.apt.*;
import com.sun.mirror.declaration.*;


import com.sun.tools.internal.ws.processor.modeler.annotation.AnnotationProcessorContext;
import com.sun.tools.internal.ws.processor.modeler.annotation.WebServiceAP;

/*
 * The JAX-WS {@com.sun.mirror.apt.AnnotationProcessorFactory AnnotationProcessorFactory}
 * class used by the <a href="http://java.sun.com/j2se/1.5.0/docs/tooldocs/share/apt.html">APT</a>
 * framework.
 */
public class AnnotationProcessorFactoryImpl implements AnnotationProcessorFactory
{

    private static WebServiceAP wsAP;
    /*
     * Processor doesn't examine any options.
     */
    static final Collection<String> supportedOptions = Collections.unmodifiableSet(new HashSet<String>());


    /*
     * Supports javax.jws.*, javax.jws.soap.* and javax.xml.ws.* annotations.
     */
    static Collection<String> supportedAnnotations;
    static
    {
        Collection<String> types = new HashSet<String>();
        types.add("javax.jws.HandlerChain");
        types.add("javax.jws.Oneway");
        types.add("javax.jws.WebMethod");
        types.add("javax.jws.WebParam");
        types.add("javax.jws.WebResult");
        types.add("javax.jws.WebService");
        types.add("javax.jws.soap.InitParam");
        types.add("javax.jws.soap.SOAPBinding");
        types.add("javax.jws.soap.SOAPMessageHandler");
        types.add("javax.jws.soap.SOAPMessageHandlers");
        types.add("javax.xml.ws.BindingType");
        types.add("javax.xml.ws.RequestWrapper");
        types.add("javax.xml.ws.ResponseWrapper");
        types.add("javax.xml.ws.ServiceMode");
        types.add("javax.xml.ws.WebEndpoint");
        types.add("javax.xml.ws.WebFault");
        types.add("javax.xml.ws.WebServiceClient");
        types.add("javax.xml.ws.WebServiceProvider");
        types.add("javax.xml.ws.WebServiceRef");
        supportedAnnotations = Collections.unmodifiableCollection(types);
    }

    public AnnotationProcessorFactoryImpl()
    {
    }


    public Collection<String> supportedOptions()
    {
        return supportedOptions;
    }

    public Collection<String> supportedAnnotationTypes()
    {
        return supportedAnnotations;
    }

    /*
     * Return an instance of the {@link com.sun.istack.internal.ws.WSAP WSAP} AnnotationProcesor.
     */
    public AnnotationProcessor getProcessorFor(Set<AnnotationTypeDeclaration> atds,
            AnnotationProcessorEnvironment apEnv)
    {

        if (wsAP == null)
        {
            AnnotationProcessorContext context = new AnnotationProcessorContext();
            wsAP = new WebServiceAP(null, context, null, null);

        }
        wsAP.init(apEnv);
        return wsAP;
    }
}
