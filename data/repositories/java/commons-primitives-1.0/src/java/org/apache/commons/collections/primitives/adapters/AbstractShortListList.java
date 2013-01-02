/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/java/org/apache/commons/collections/primitives/adapters/AbstractShortListList.java,v 1.3 2003/10/16 20:49:39 scolebourne Exp $
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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.collections.primitives.ShortCollection;
import org.apache.commons.collections.primitives.ShortList;

/**
 * @since Commons Primitives 1.0
 * @version $Revision: 1.3 $ $Date: 2003/10/16 20:49:39 $
 * @author Rodney Waldhoff
 */
abstract class AbstractShortListList extends AbstractShortCollectionCollection implements List
{

    public void add(int index, Object element)
    {
        getShortList().add(index,((Number)element).shortValue());
    }

    public boolean addAll(int index, Collection c)
    {
        return getShortList().addAll(index,CollectionShortCollection.wrap(c));
    }

    public Object get(int index)
    {
        return new Short(getShortList().get(index));
    }

    public int indexOf(Object element)
    {
        return getShortList().indexOf(((Number)element).shortValue());
    }

    public int lastIndexOf(Object element)
    {
        return getShortList().lastIndexOf(((Number)element).shortValue());
    }

    /**
     * {@link ShortListIteratorListIterator#wrap wraps} the
     * {@link org.apache.commons.collections.primitives.ShortListIterator ShortListIterator}
     * returned by my underlying
     * {@link ShortList ShortList},
     * if any.
     */
    public ListIterator listIterator()
    {
        return ShortListIteratorListIterator.wrap(getShortList().listIterator());
    }

    /**
     * {@link ShortListIteratorListIterator#wrap wraps} the
     * {@link org.apache.commons.collections.primitives.ShortListIterator ShortListIterator}
     * returned by my underlying
     * {@link ShortList ShortList},
     * if any.
     */
    public ListIterator listIterator(int index)
    {
        return ShortListIteratorListIterator.wrap(getShortList().listIterator(index));
    }

    public Object remove(int index)
    {
        return new Short(getShortList().removeElementAt(index));
    }

    public Object set(int index, Object element)
    {
        return new Short(getShortList().set(index, ((Number)element).shortValue() ));
    }

    public List subList(int fromIndex, int toIndex)
    {
        return ShortListList.wrap(getShortList().subList(fromIndex,toIndex));
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof List)
        {
            List that = (List)obj;
            if(this == that)
            {
                return true;
            }
            else if(this.size() != that.size())
            {
                return false;
            }
            else
            {
                Iterator thisiter = iterator();
                Iterator thatiter = that.iterator();
                while(thisiter.hasNext())
                {
                    Object thiselt = thisiter.next();
                    Object thatelt = thatiter.next();
                    if(null == thiselt ? null != thatelt : !(thiselt.equals(thatelt)))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    public int hashCode()
    {
        return getShortList().hashCode();
    }

    protected final ShortCollection getShortCollection()
    {
        return getShortList();
    }

    protected abstract ShortList getShortList();


}
