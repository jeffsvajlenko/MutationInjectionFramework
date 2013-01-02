/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/java/org/apache/commons/collections/primitives/adapters/DoubleListIteratorListIterator.java,v 1.3 2003/10/16 20:49:38 scolebourne Exp $
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

package org.apache.commons.collections.primitives.adapters;

import java.util.ListIterator;

import org.apache.commons.collections.primitives.DoubleListIterator;

/**
 * Adapts an {@link DoubleListIterator DoubleListIterator} to the
 * {@link ListIterator ListIterator} interface.
 * <p />
 * This implementation delegates most methods
 * to the provided {@link DoubleListIterator DoubleListIterator}
 * implementation in the "obvious" way.
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 1.3 $ $Date: 2003/10/16 20:49:38 $
 * @author Rodney Waldhoff
 */
public class DoubleListIteratorListIterator implements ListIterator
{

    /**
     * Create a {@link ListIterator ListIterator} wrapping
     * the specified {@link DoubleListIterator DoubleListIterator}.  When
     * the given <i>iterator</i> is <code>null</code>,
     * returns <code>null</code>.
     *
     * @param iterator the (possibly <code>null</code>)
     *        {@link DoubleListIterator DoubleListIterator} to wrap
     * @return a {@link ListIterator ListIterator} wrapping the given
     *         <i>iterator</i>, or <code>null</code> when <i>iterator</i> is
     *         <code>null</code>.
     */
    public static ListIterator wrap(DoubleListIterator iterator)
    {
        return null == iterator ? null : new DoubleListIteratorListIterator(iterator);
    }

    /**
     * Creates an {@link ListIterator ListIterator} wrapping
     * the specified {@link DoubleListIterator DoubleListIterator}.
     * @see #wrap
     */
    public DoubleListIteratorListIterator(DoubleListIterator iterator)
    {
        _iterator = iterator;
    }

    public int nextIndex()
    {
        return _iterator.nextIndex();
    }

    public int previousIndex()
    {
        return _iterator.previousIndex();
    }

    public boolean hasNext()
    {
        return _iterator.hasNext();
    }

    public boolean hasPrevious()
    {
        return _iterator.hasPrevious();
    }

    public Object next()
    {
        return new Double(_iterator.next());
    }

    public Object previous()
    {
        return new Double(_iterator.previous());
    }

    public void add(Object obj)
    {
        _iterator.add(((Number)obj).doubleValue());
    }

    public void set(Object obj)
    {
        _iterator.set(((Number)obj).doubleValue());
    }

    public void remove()
    {
        _iterator.remove();
    }

    private DoubleListIterator _iterator = null;

}
