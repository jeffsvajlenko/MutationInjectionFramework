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
package com.sun.xml.internal.ws.client;

import com.sun.xml.internal.ws.api.handler.MessageHandler;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.LogicalHandler;
import javax.xml.ws.handler.soap.SOAPHandler;
import java.util.*;

/**
 * This class holds the handler information on the BindingProvider.
 * HandlerConfiguration is immutable, and a new object is created
 * when the BindingImpl is created or User calls Binding.setHandlerChain() or
 * SOAPBinding.setRoles()
 * During inovcation in Stub.process(), snapshot of the handler configuration is set in
 * Packet.handlerConfig
 * The information in the HandlerConfiguration is used by MUPipe and HandlerTube
 * implementations.
 *
 * @author Rama Pulavarthi
 */
public class HandlerConfiguration
{
    private final Set<String> roles;
    /**
     * This chain may contain both soap and logical handlers.
     */
    private final List<Handler> handlerChain;
    private final List<LogicalHandler> logicalHandlers;
    private final List<SOAPHandler> soapHandlers;
    private final List<MessageHandler> messageHandlers;
    private Set<QName> knownHeaders;
    private Set<QName> handlerKnownHeaders;
    /**
     * @param roles                    This contains the roles assumed by the Binding implementation.
     * @param portKnownHeaders    This contains the headers that are bound to the current WSDL Port
     * @param handlerChain             This contains the handler chain set on the Binding
     * @param logicalHandlers
     * @param soapHandlers
     * @param handlerKnownHeaders The set is comprised of headers returned from SOAPHandler.getHeaders()
     *                                 method calls.
     */
    public HandlerConfiguration(Set<String> roles, Set<QName> portKnownHeaders,
                                List<Handler> handlerChain,
                                List<LogicalHandler> logicalHandlers, List<SOAPHandler> soapHandlers,
                                List<MessageHandler> messageHandlers,
                                Set<QName> handlerKnownHeaders)
    {
        this.roles = roles;
        this.handlerChain = handlerChain;
        this.logicalHandlers = logicalHandlers;
        this.soapHandlers = soapHandlers;
        this.messageHandlers = messageHandlers;
        this.handlerKnownHeaders = handlerKnownHeaders;
        this.knownHeaders = new HashSet<QName>();
        if(portKnownHeaders != null)
            knownHeaders.addAll(portKnownHeaders);
        if(handlerKnownHeaders != null)
            knownHeaders.addAll(handlerKnownHeaders);
    }

    public Set<String> getRoles()
    {
        return roles;
    }

    /**
     *
     * @return return a copy of handler chain
     */
    public List<Handler> getHandlerChain()
    {
        if(handlerChain == null)
            return Collections.emptyList();
        return new ArrayList<Handler>(handlerChain);

    }

    public List<LogicalHandler> getLogicalHandlers()
    {
        return logicalHandlers;
    }

    public List<SOAPHandler> getSoapHandlers()
    {
        return soapHandlers;
    }

    public List<MessageHandler> getMessageHandlers()
    {
        return messageHandlers;
    }

    public Set<QName> getKnownHeaders()
    {
        return knownHeaders;
    }

    public Set<QName> getHandlerKnownHeaders()
    {
        return handlerKnownHeaders;
    }

}
