/*
 * Copyright (c) 1998, 2003, Oracle and/or its affiliates. All rights reserved.
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

package com.sun.tools.doclets.formats.html;

import com.sun.tools.doclets.internal.toolkit.*;
import com.sun.tools.doclets.internal.toolkit.taglets.*;
import com.sun.javadoc.*;

/**
 * Generate serialized form for Serializable/Externalizable methods.
 * Documentation denoted by the <code>serialData</code> tag is processed.
 *
 * @author Joe Fialli
 */
public class HtmlSerialMethodWriter extends MethodWriterImpl implements
    SerializedFormWriter.SerialMethodWriter
{

    private boolean printedFirstMember = false;

    public HtmlSerialMethodWriter(SubWriterHolderWriter writer,
                                  ClassDoc classdoc)
    {
        super(writer, classdoc);
    }

    public void writeHeader(String heading)
    {
        writer.anchor("serialized_methods");
        writer.printTableHeadingBackground(heading);
        writer.p();
    }

    public void writeNoCustomizationMsg(String msg)
    {
        writer.print(msg);
        writer.p();
    }

    public void writeMemberHeader(MethodDoc member)
    {
        if (printedFirstMember)
        {
            writer.printMemberHeader();
        }
        printedFirstMember = true;
        writer.anchor(member);
        printHead(member);
        writeSignature(member);
    }

    public void writeMemberFooter(MethodDoc member)
    {
        writer.dlEnd();
    }

    public void writeDeprecatedMemberInfo(MethodDoc member)
    {
        print(((TagletOutputImpl)
               (new DeprecatedTaglet()).getTagletOutput(member,
                       writer.getTagletWriterInstance(false))).toString());
    }

    public void writeMemberDescription(MethodDoc member)
    {
        printComment(member);
    }

    public void writeMemberTags(MethodDoc member)
    {
        writer.dd();
        writer.dl();
        TagletOutputImpl output = new TagletOutputImpl("");
        TagletManager tagletManager =
            ConfigurationImpl.getInstance().tagletManager;
        TagletWriter.genTagOuput(tagletManager, member,
                                 tagletManager.getSerializedFormTags(),
                                 writer.getTagletWriterInstance(false), output);
        print(output.toString());
        MethodDoc method = (MethodDoc)member;
        if (method.name().compareTo("writeExternal") == 0
                && method.tags("serialData").length == 0)
        {
            serialWarning(member.position(), "doclet.MissingSerialDataTag",
                          method.containingClass().qualifiedName(), method.name());
        }
        writer.ddEnd();
        writer.dlEnd();
    }

    protected void printTypeLinkNoDimension(Type type)
    {
        ClassDoc cd = type.asClassDoc();
        if (type.isPrimitive() || cd.isPackagePrivate())
        {
            print(type.typeName());
        }
        else
        {
            writer.printLink(new LinkInfoImpl(
                                 LinkInfoImpl.CONTEXT_SERIAL_MEMBER,type));
        }
    }
}
