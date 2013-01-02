/*
 * $Header: /home/cvs/jakarta-commons/primitives/src/java/org/apache/commons/collections/primitives/adapters/AbstractListCharList.java,v 1.3 2003/10/16 20:49:39 scolebourne Exp $
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
import java.util.List;

import org.apache.commons.collections.primitives.CharCollection;
import org.apache.commons.collections.primitives.CharIterator;
import org.apache.commons.collections.primitives.CharList;
import org.apache.commons.collections.primitives.CharListIterator;

/**
 *
 * @since Commons Primitives 1.0
 * @version $Revision: 1.3 $ $Date: 2003/10/16 20:49:39 $
 * @author Rodney Waldhoff
 */
abstract class AbstractListCharList extends AbstractCollectionCharCollection implements CharList
{

    public void add(int index, char element)
    {
        getList().add(index,new Character(element));
    }

    public boolean addAll(int index, CharCollection collection)
    {
        return getList().addAll(index,CharCollectionCollection.wrap(collection));
    }

    public char get(int index)
    {
        return ((Character)getList().get(index)).charValue();
    }

    public int indexOf(char element)
    {
        return getList().indexOf(new Character(element));
    }

    public int lastIndexOf(char element)
    {
        return getList().lastIndexOf(new Character(element));
    }

    /**
     * {@link ListIteratorCharListIterator#wrap wraps} the
     * {@link CharList CharList}
     * returned by my underlying
     * {@link CharListIterator CharListIterator},
     * if any.
     */
    public CharListIterator listIterator()
    {
        return ListIteratorCharListIterator.wrap(getList().listIterator());
    }

    /**
     * {@link ListIteratorCharListIterator#wrap wraps} the
     * {@link CharList CharList}
     * returned by my underlying
     * {@link CharListIterator CharListIterator},
     * if any.
     */
    public CharListIterator listIterator(int index)
    {
        return ListIteratorCharListIterator.wrap(getList().listIterator(index));
    }

    public char removeElementAt(int index)
    {
        return ((Character)getList().remove(index)).charValue();
    }

    public char set(int index, char element)
    {
        return ((Character)getList().set(index,new Character(element))).charValue();
    }

    public CharList subList(int fromIndex, int toIndex)
    {
        return ListCharList.wrap(getList().subList(fromIndex,toIndex));
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof CharList)
        {
            CharList that = (CharList)obj;
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
                CharIterator thisiter = iterator();
                CharIterator thatiter = that.iterator();
                while(thisiter.hasNext())
                {
                    if(thisiter.next() != thatiter.next())
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
        return getList().hashCode();
    }

    final protected Collection getCollection()
    {
        return getList();
    }

    abstract protected List getList();
}
