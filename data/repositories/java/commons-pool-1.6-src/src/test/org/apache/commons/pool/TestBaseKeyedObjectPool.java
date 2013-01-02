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


/**
 * @author Rodney Waldhoff
 * @author Sandy McArthur
 * @version $Revision: 1222396 $ $Date: 2011-12-22 14:02:25 -0500 (Thu, 22 Dec 2011) $
 */
public class TestBaseKeyedObjectPool<K, V> extends TestKeyedObjectPool
{
    private KeyedObjectPool<K, V> _pool = null;

    public TestBaseKeyedObjectPool(final String testName)
    {
        super(testName);
    }

    @Override
    protected KeyedObjectPool<Object, Integer> makeEmptyPool(KeyedPoolableObjectFactory<Object, Integer> factory)
    {
        if (this.getClass() != TestBaseKeyedObjectPool.class)
        {
            fail("Subclasses of TestBaseKeyedObjectPool must reimplement this method.");
        }
        throw new UnsupportedOperationException("BaseKeyedObjectPool isn't a complete implementation.");
    }

    /**
     * Create an {@link KeyedObjectPool} instance
     * that can contain at least <i>mincapacity</i>
     * idle and active objects, or
     * throw {@link IllegalArgumentException}
     * if such a pool cannot be created.
     */
    protected KeyedObjectPool<K, V> makeEmptyPool(int mincapacity)
    {
        if (this.getClass() != TestBaseKeyedObjectPool.class)
        {
            fail("Subclasses of TestBaseKeyedObjectPool must reimplement this method.");
        }
        throw new UnsupportedOperationException("BaseKeyedObjectPool isn't a complete implementation.");
    }

    /**
     * Return what we expect to be the n<sup>th</sup>
     * object (zero indexed) created by the pool
     * for the given key.
     */
    protected V getNthObject(Object key, int n)
    {
        if (this.getClass() != TestBaseKeyedObjectPool.class)
        {
            fail("Subclasses of TestBaseKeyedObjectPool must reimplement this method.");
        }
        throw new UnsupportedOperationException("BaseKeyedObjectPool isn't a complete implementation.");
    }

    protected K makeKey(int n)
    {
        if (this.getClass() != TestBaseKeyedObjectPool.class)
        {
            fail("Subclasses of TestBaseKeyedObjectPool must reimplement this method.");
        }
        throw new UnsupportedOperationException("BaseKeyedObjectPool isn't a complete implementation.");
    }

    @Override
    public void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception
    {
        _pool = null;
        super.tearDown();
    }

    public void testUnsupportedOperations() throws Exception
    {
        if (!getClass().equals(TestBaseKeyedObjectPool.class))
        {
            return; // skip redundant tests
        }
        KeyedObjectPool<String, String> pool = new BaseKeyedObjectPool<String, String>()
        {
            @Override
            public String borrowObject(String key)
            {
                return null;
            }
            @Override
            public void returnObject(String key, String obj)
            {
            }
            @Override
            public void invalidateObject(String key, String obj)
            {
            }
        };

        try
        {
            pool.addObject("key");
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        assertTrue("Negative expected.", pool.getNumIdle() < 0);
        assertTrue("Negative expected.", pool.getNumIdle("key") < 0);
        assertTrue("Negative expected.", pool.getNumActive() < 0);
        assertTrue("Negative expected.", pool.getNumActive("key") < 0);

        try
        {
            pool.clear();
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            pool.clear("key");
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        try
        {
            pool.setFactory(null);
            fail("Expected UnsupportedOperationException");
        }
        catch(UnsupportedOperationException e)
        {
            // expected
        }

        pool.close(); // a no-op, probably should be remove

    }

    protected boolean isLifo()
    {
        if (getClass() != TestBaseKeyedObjectPool.class)
        {
            fail("Subclasses of TestBaseKeyedObjectPool must reimplement this method.");
        }
        return false;
    }

    protected boolean isFifo()
    {
        if (getClass() != TestBaseKeyedObjectPool.class)
        {
            fail("Subclasses of TestBaseKeyedObjectPool must reimplement this method.");
        }
        return false;
    }

    public void testBaseBorrowReturn() throws Exception
    {
        try
        {
            _pool = makeEmptyPool(3);
        }
        catch(UnsupportedOperationException uoe)
        {
            return; // skip this test if unsupported
        }
        K keya = makeKey(0);
        V obj0 = _pool.borrowObject(keya);
        assertEquals(getNthObject(keya,0),obj0);
        V obj1 = _pool.borrowObject(keya);
        assertEquals(getNthObject(keya,1),obj1);
        V obj2 = _pool.borrowObject(keya);
        assertEquals(getNthObject(keya,2),obj2);
        _pool.returnObject(keya,obj2);
        obj2 = _pool.borrowObject(keya);
        assertEquals(getNthObject(keya,2),obj2);
        _pool.returnObject(keya,obj1);
        obj1 = _pool.borrowObject(keya);
        assertEquals(getNthObject(keya,1),obj1);
        _pool.returnObject(keya,obj0);
        _pool.returnObject(keya,obj2);
        obj2 = _pool.borrowObject(keya);
        if (isLifo())
        {
            assertEquals(getNthObject(keya,2),obj2);
        }
        if (isFifo())
        {
            assertEquals(getNthObject(keya,0),obj2);
        }
        obj0 = _pool.borrowObject(keya);
        if (isLifo())
        {
            assertEquals(getNthObject(keya,0),obj0);
        }
        if (isFifo())
        {
            assertEquals(getNthObject(keya,2),obj0);
        }
    }

    public void testBaseBorrow() throws Exception
    {
        try
        {
            _pool = makeEmptyPool(3);
        }
        catch(UnsupportedOperationException uoe)
        {
            return; // skip this test if unsupported
        }
        K keya = makeKey(0);
        K keyb = makeKey(1);
        assertEquals("1",getNthObject(keya,0),_pool.borrowObject(keya));
        assertEquals("2",getNthObject(keyb,0),_pool.borrowObject(keyb));
        assertEquals("3",getNthObject(keyb,1),_pool.borrowObject(keyb));
        assertEquals("4",getNthObject(keya,1),_pool.borrowObject(keya));
        assertEquals("5",getNthObject(keyb,2),_pool.borrowObject(keyb));
        assertEquals("6",getNthObject(keya,2),_pool.borrowObject(keya));
    }

    public void testBaseNumActiveNumIdle() throws Exception
    {
        try
        {
            _pool = makeEmptyPool(3);
        }
        catch(UnsupportedOperationException uoe)
        {
            return; // skip this test if unsupported
        }
        K keya = makeKey(0);
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        V obj0 = _pool.borrowObject(keya);
        assertEquals(1,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        V obj1 = _pool.borrowObject(keya);
        assertEquals(2,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        _pool.returnObject(keya,obj1);
        assertEquals(1,_pool.getNumActive(keya));
        assertEquals(1,_pool.getNumIdle(keya));
        _pool.returnObject(keya,obj0);
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(2,_pool.getNumIdle(keya));

        // cast to KeyedObjectPool to use the wrong key type.
        @SuppressWarnings("unchecked")
        final KeyedObjectPool<Object, V> rawPool = (KeyedObjectPool<Object, V>)_pool;
        assertEquals(0,rawPool.getNumActive("xyzzy12345"));
        assertEquals(0,rawPool.getNumIdle("xyzzy12345"));
    }

    public void testBaseNumActiveNumIdle2() throws Exception
    {
        try
        {
            _pool = makeEmptyPool(6);
        }
        catch(UnsupportedOperationException uoe)
        {
            return; // skip this test if unsupported
        }
        K keya = makeKey(0);
        K keyb = makeKey(1);
        assertEquals(0,_pool.getNumActive());
        assertEquals(0,_pool.getNumIdle());
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        assertEquals(0,_pool.getNumActive(keyb));
        assertEquals(0,_pool.getNumIdle(keyb));

        V objA0 = _pool.borrowObject(keya);
        V objB0 = _pool.borrowObject(keyb);

        assertEquals(2,_pool.getNumActive());
        assertEquals(0,_pool.getNumIdle());
        assertEquals(1,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        assertEquals(1,_pool.getNumActive(keyb));
        assertEquals(0,_pool.getNumIdle(keyb));

        V objA1 = _pool.borrowObject(keya);
        V objB1 = _pool.borrowObject(keyb);

        assertEquals(4,_pool.getNumActive());
        assertEquals(0,_pool.getNumIdle());
        assertEquals(2,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        assertEquals(2,_pool.getNumActive(keyb));
        assertEquals(0,_pool.getNumIdle(keyb));

        _pool.returnObject(keya,objA0);
        _pool.returnObject(keyb,objB0);

        assertEquals(2,_pool.getNumActive());
        assertEquals(2,_pool.getNumIdle());
        assertEquals(1,_pool.getNumActive(keya));
        assertEquals(1,_pool.getNumIdle(keya));
        assertEquals(1,_pool.getNumActive(keyb));
        assertEquals(1,_pool.getNumIdle(keyb));

        _pool.returnObject(keya,objA1);
        _pool.returnObject(keyb,objB1);

        assertEquals(0,_pool.getNumActive());
        assertEquals(4,_pool.getNumIdle());
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(2,_pool.getNumIdle(keya));
        assertEquals(0,_pool.getNumActive(keyb));
        assertEquals(2,_pool.getNumIdle(keyb));
    }

    public void testBaseClear() throws Exception
    {
        try
        {
            _pool = makeEmptyPool(3);
        }
        catch(UnsupportedOperationException uoe)
        {
            return; // skip this test if unsupported
        }
        K keya = makeKey(0);
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        V obj0 = _pool.borrowObject(keya);
        V obj1 = _pool.borrowObject(keya);
        assertEquals(2,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        _pool.returnObject(keya,obj1);
        _pool.returnObject(keya,obj0);
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(2,_pool.getNumIdle(keya));
        _pool.clear(keya);
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        Object obj2 = _pool.borrowObject(keya);
        assertEquals(getNthObject(keya,2),obj2);
    }

    public void testBaseInvalidateObject() throws Exception
    {
        try
        {
            _pool = makeEmptyPool(3);
        }
        catch(UnsupportedOperationException uoe)
        {
            return; // skip this test if unsupported
        }
        K keya = makeKey(0);
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        V obj0 = _pool.borrowObject(keya);
        V obj1 = _pool.borrowObject(keya);
        assertEquals(2,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        _pool.invalidateObject(keya,obj0);
        assertEquals(1,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
        _pool.invalidateObject(keya,obj1);
        assertEquals(0,_pool.getNumActive(keya));
        assertEquals(0,_pool.getNumIdle(keya));
    }

    public void testBaseAddObject() throws Exception
    {
        try
        {
            _pool = makeEmptyPool(3);
        }
        catch(UnsupportedOperationException uoe)
        {
            return; // skip this test if unsupported
        }
        K key = makeKey(0);
        try
        {
            assertEquals(0,_pool.getNumIdle());
            assertEquals(0,_pool.getNumActive());
            assertEquals(0,_pool.getNumIdle(key));
            assertEquals(0,_pool.getNumActive(key));
            _pool.addObject(key);
            assertEquals(1,_pool.getNumIdle());
            assertEquals(0,_pool.getNumActive());
            assertEquals(1,_pool.getNumIdle(key));
            assertEquals(0,_pool.getNumActive(key));
            V obj = _pool.borrowObject(key);
            assertEquals(getNthObject(key,0),obj);
            assertEquals(0,_pool.getNumIdle());
            assertEquals(1,_pool.getNumActive());
            assertEquals(0,_pool.getNumIdle(key));
            assertEquals(1,_pool.getNumActive(key));
            _pool.returnObject(key,obj);
            assertEquals(1,_pool.getNumIdle());
            assertEquals(0,_pool.getNumActive());
            assertEquals(1,_pool.getNumIdle(key));
            assertEquals(0,_pool.getNumActive(key));
        }
        catch(UnsupportedOperationException e)
        {
            return; // skip this test if one of those calls is unsupported
        }
    }
}
