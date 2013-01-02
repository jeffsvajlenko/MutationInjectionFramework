/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.betwixt.io.read;

import java.util.ArrayList;
import java.util.Iterator;

/**
  * <p>Chain implementation that's backed by a list.
  * This is the default implementation used by Betwixt.
  * </p><p>
  * <strong>Note</strong> this implementation is <em>not</em>
  * intended to allow multiple threads of execution to perform
  * modification operations concurrently with traversal of the chain.
  * Users who require this behaviour are advised to create their own implementation.
  * </p>
  *
  * @author Robert Burrell Donkin
  * @since 0.5
  */
public class BeanCreationList extends BeanCreationChain
{

//-------------------------------------------------------- Class Methods

    /**
     * Creates the default <code>BeanCreationChain</code> used when reading beans.
     * @return a <code>BeanCreationList</code> with the default creators loader in order, not null
     */
    public static final BeanCreationList createStandardChain()
    {
        BeanCreationList chain = new BeanCreationList();
        chain.addBeanCreator( ChainedBeanCreatorFactory.createIDREFBeanCreator() );
        chain.addBeanCreator( ChainedBeanCreatorFactory.createDerivedBeanCreator() );
        chain.addBeanCreator( ChainedBeanCreatorFactory.createElementTypeBeanCreator() );
        return chain;
    }



//-------------------------------------------------------- Attributes
    /** The list backing this chain */
    private ArrayList beanCreators = new ArrayList();

//-------------------------------------------------------- Methods

    /**
      * Creates an Object based on the given element mapping and read context.
      * Delegates to chain.
      *
      * @param elementMapping the element mapping details
      * @param readContext create against this context
      * @return the created bean, possibly null
      */
    public Object create( ElementMapping elementMapping, ReadContext readContext )
    {
        ChainWorker worker = new ChainWorker();
        return worker.create( elementMapping, readContext );
    }

//-------------------------------------------------------- Properties

    /**
      * Gets the number of BeanCreators in the wrapped chain.
      * @return the number of <code>ChainedBeanCreator</code>'s in the current chain
      */
    public int getSize()
    {
        return beanCreators.size();
    }

    /**
      * Inserts a <code>BeanCreator</code> at the given position in the chain.
      * Shifts the object currently in that position - and any subsequent elements -
      * to the right.
      *
      * @param index index at which the creator should be inserted
      * @param beanCreator the <code>BeanCreator</code> to be inserted, not null
      * @throws IndexOutOfBoundsException if the index is out of the range
      * <code>(index < 0 || index > getSize())
      */
    public void insertBeanCreator(
        int index,
        ChainedBeanCreator beanCreator )
    throws IndexOutOfBoundsException
    {
        beanCreators.add( index, beanCreator );
    }

    /**
      * Adds a <code>BeanCreator</code> to the end of the chain.
      * @param beanCreator the <code>BeanCreator</code> to be inserted, not null
      */
    public void addBeanCreator( ChainedBeanCreator beanCreator )
    {
        beanCreators.add( beanCreator );
    }

    /**
      * Clears the creator chain.
      */
    public void clearBeanCreators()
    {
        beanCreators.clear();
    }

    /** Worker class walks a chain */
    private class ChainWorker extends BeanCreationChain
    {
        /** Iterator for the creator list */
        private Iterator iterator;
        /** Creates the iterator */
        ChainWorker()
        {
            iterator = beanCreators.iterator();
        }

        /**
          * @see BeanCreationChain#create
          */
        public Object create( ElementMapping elementMapping, ReadContext readContext )
        {
            if ( iterator.hasNext() )
            {
                ChainedBeanCreator beanCreator = (ChainedBeanCreator) iterator.next();
                return beanCreator.create( elementMapping, readContext, this );
            }

            return null;
        }
    }
}
