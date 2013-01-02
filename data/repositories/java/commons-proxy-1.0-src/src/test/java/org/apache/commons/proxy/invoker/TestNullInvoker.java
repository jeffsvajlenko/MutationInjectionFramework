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

package org.apache.commons.proxy.invoker;
import junit.framework.TestCase;
import org.apache.commons.proxy.ProxyUtils;
import org.apache.commons.proxy.factory.cglib.CglibProxyFactory;

public class TestNullInvoker extends TestCase
{
    public void testReturnValues()
    {
        final Tester tester = ( Tester )ProxyUtils.createNullObject( new CglibProxyFactory(), new Class[] { Tester.class } );
        assertEquals( 0, tester.intMethod() );
        assertEquals( 0L, tester.longMethod() );
        assertEquals( ( short )0, tester.shortMethod() );
        assertEquals( ( byte )0, tester.byteMethod() );
        assertEquals( ( char )0, tester.charMethod() );
        assertEquals( 0.0f, tester.floatMethod(), 0.0f );
        assertEquals( 0.0, tester.doubleMethod(), 0.0f );
        assertFalse( tester.booleanMethod() );
        assertNull( tester.stringMethod() );
    }

    public static interface Tester
    {
        public int intMethod();
        public long longMethod();
        public short shortMethod();
        public byte byteMethod();
        public char charMethod();
        public double doubleMethod();
        public float floatMethod();
        public String stringMethod();
        public boolean booleanMethod();
    }
}
