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

package com.sun.xml.internal.ws.encoding;

import com.sun.istack.internal.Nullable;
import com.sun.istack.internal.NotNull;

/**
 * @author Vivek Pandey
 */
public final class ContentTypeImpl implements com.sun.xml.internal.ws.api.pipe.ContentType
{
    private final @NotNull String contentType;
    private final @NotNull String soapAction;
    private final @Nullable String accept;
    private final @Nullable String charset;

    public ContentTypeImpl(String contentType)
    {
        this(contentType, null, null);
    }

    public ContentTypeImpl(String contentType, @Nullable String soapAction)
    {
        this(contentType, soapAction, null);
    }

    public ContentTypeImpl(String contentType, @Nullable String soapAction, @Nullable String accept)
    {
        this.contentType = contentType;
        this.accept = accept;
        this.soapAction = getQuotedSOAPAction(soapAction);
        String tmpCharset = null;
        try
        {
            tmpCharset = new ContentType(contentType).getParameter("charset");
        }
        catch(Exception e)
        {
            //Ignore the parsing exception.
        }
        charset = tmpCharset;
    }

    /**
     * Returns the character set encoding.
     *
     * @return returns the character set encoding.
     */
    public @Nullable String getCharSet()
    {
        return charset;
    }

    /** BP 1.1 R1109 requires SOAPAction too be a quoted value **/
    private String getQuotedSOAPAction(String soapAction)
    {
        if(soapAction == null || soapAction.length() == 0)
        {
            return "\"\"";
        }
        else if(soapAction.charAt(0) != '"' && soapAction.charAt(soapAction.length() -1) != '"')
        {
            //surround soapAction by double quotes for BP R1109
            return "\"" + soapAction + "\"";
        }
        else
        {
            return soapAction;
        }
    }

    public String getContentType()
    {
        return contentType;
    }

    public String getSOAPActionHeader()
    {
        return soapAction;
    }

    public String getAcceptHeader()
    {
        return accept;
    }
}
