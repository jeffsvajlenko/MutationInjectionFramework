/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/java/org/apache/commons/collections/primitives/decorators/BaseProxyFloatCollection.java,v 1.1 2003/10/29 20:07:54 rwaldhoff Exp $
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.commons.collections.primitives.decorators;

import org.apache.commons.collections.primitives.FloatCollection;
import org.apache.commons.collections.primitives.FloatIterator;

/**
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 1.1 $ $Date: 2003/10/29 20:07:54 $
 *
 * @author Rodney Waldhoff
 */
abstract class BaseProxyFloatCollection implements FloatCollection
{
    protected abstract FloatCollection getProxiedCollection();

    protected BaseProxyFloatCollection()
    {
    }

    public boolean add(float element)
    {
        return getProxiedCollection().add(element);
    }

    public boolean addAll(FloatCollection c)
    {
        return getProxiedCollection().addAll(c);
    }

    public void clear()
    {
        getProxiedCollection().clear();
    }

    public boolean contains(float element)
    {
        return getProxiedCollection().contains(element);
    }

    public boolean containsAll(FloatCollection c)
    {
        return getProxiedCollection().containsAll(c);
    }

    public boolean isEmpty()
    {
        return getProxiedCollection().isEmpty();
    }

    public FloatIterator iterator()
    {
        return getProxiedCollection().iterator();
    }

    public boolean removeAll(FloatCollection c)
    {
        return getProxiedCollection().removeAll(c);
    }

    public boolean removeElement(float element)
    {
        return getProxiedCollection().removeElement(element);
    }

    public boolean retainAll(FloatCollection c)
    {
        return getProxiedCollection().retainAll(c);
    }

    public int size()
    {
        return getProxiedCollection().size();
    }

    public float[] toArray()
    {
        return getProxiedCollection().toArray();
    }

    public float[] toArray(float[] a)
    {
        return getProxiedCollection().toArray(a);
    }

    // TODO: Add note about possible contract violations here.

    public boolean equals(Object obj)
    {
        return getProxiedCollection().equals(obj);
    }

    public int hashCode()
    {
        return getProxiedCollection().hashCode();
    }

    public String toString()
    {
        return getProxiedCollection().toString();
    }

}
