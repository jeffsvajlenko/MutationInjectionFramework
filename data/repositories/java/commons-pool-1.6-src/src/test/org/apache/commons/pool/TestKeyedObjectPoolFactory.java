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

package org.apache.commons.pool;

import junit.framework.TestCase;

/**
 * Tests for all {@link KeyedObjectPoolFactory}s.
 *
 * @author Sandy McArthur
 * @version $Revision: 1221705 $ $Date: 2011-12-21 08:03:54 -0500 (Wed, 21 Dec 2011) $
 */
public abstract class TestKeyedObjectPoolFactory extends TestCase
{
    protected TestKeyedObjectPoolFactory(final String name)
    {
        super(name);
    }

    /**
     * @throws UnsupportedOperationException when this is unsupported by this KeyedPoolableObjectFactory type.
     */
    protected KeyedObjectPoolFactory<Object, Integer> makeFactory() throws UnsupportedOperationException
    {
        return makeFactory(createObjectFactory());
    }

    /**
     * @throws UnsupportedOperationException when this is unsupported by this KeyedPoolableObjectFactory type.
     */
    protected abstract KeyedObjectPoolFactory<Object, Integer> makeFactory(KeyedPoolableObjectFactory<Object, Integer> objectFactory) throws UnsupportedOperationException;

    protected static KeyedPoolableObjectFactory<Object, Integer> createObjectFactory()
    {
        return PoolUtils.adapt(new MethodCallPoolableObjectFactory());
    }

    public void testCreatePool() throws Exception
    {
        final KeyedObjectPoolFactory<?, Integer> factory;
        try
        {
            factory = makeFactory();
        }
        catch (UnsupportedOperationException uoe)
        {
            return;
        }
        final KeyedObjectPool<?, Integer> pool = factory.createPool();
        pool.close();
    }

    public void testToString()
    {
        final KeyedObjectPoolFactory<?, Integer> factory;
        try
        {
            factory = makeFactory();
        }
        catch (UnsupportedOperationException uoe)
        {
            return;
        }
        factory.toString();
    }
}
