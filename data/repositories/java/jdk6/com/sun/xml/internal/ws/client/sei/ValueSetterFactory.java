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
package com.sun.xml.internal.ws.client.sei;

import com.sun.xml.internal.ws.model.ParameterImpl;

import javax.xml.ws.WebServiceException;

/**
 * {@link ValueSetterFactory} is used to create {@link ValueSetter}.
 *
 * @author Jitendra Kotamraju
 */
abstract class ValueSetterFactory
{

    abstract ValueSetter get(ParameterImpl p);

    static final ValueSetterFactory SYNC = new ValueSetterFactory()
    {
        ValueSetter get(ParameterImpl p)
        {
            return ValueSetter.getSync(p);
        }
    };

    static final ValueSetterFactory NONE = new ValueSetterFactory()
    {
        ValueSetter get(ParameterImpl p)
        {
            throw new WebServiceException("This shouldn't happen. No response parameters.");
        }
    };

    static final ValueSetterFactory SINGLE = new ValueSetterFactory()
    {
        ValueSetter get(ParameterImpl p)
        {
            return ValueSetter.SINGLE_VALUE;
        }
    };

    static final class AsyncBeanValueSetterFactory extends ValueSetterFactory
    {
        private Class asyncBean;

        AsyncBeanValueSetterFactory(Class asyncBean)
        {
            this.asyncBean = asyncBean;
        }

        ValueSetter get(ParameterImpl p)
        {
            return new ValueSetter.AsyncBeanValueSetter(p, asyncBean);
        }
    }

}
