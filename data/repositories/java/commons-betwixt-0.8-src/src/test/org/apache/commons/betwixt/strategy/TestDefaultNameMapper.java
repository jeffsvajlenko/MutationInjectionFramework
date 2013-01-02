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

package org.apache.commons.betwixt.strategy;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Testcase that covers the DefaultNameMapper.
 *
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Id: TestDefaultNameMapper.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestDefaultNameMapper extends TestCase
{

    public static Test suite()
    {
        return new TestSuite(TestDefaultNameMapper.class);
    }

    public TestDefaultNameMapper(String testName)
    {
        super(testName);
    }
    /**
     * Just put in some strings and expect them back unchanged.
     * This looks stupid, but enables us to check for unexpected
     * changes, which breaks the orignal behaviour.
     */
    public void testDefault()
    {
        String[] values = { "foo", "Foo", "FooBar", "fooBar",
                            "FOOBAR", "FOOBar", "FoOBaR"
                          };
        DefaultNameMapper mapper = new DefaultNameMapper();
        for (int i=0; i < values.length; i++)
        {
            String result = mapper.mapTypeToElementName(values[i]);
            assertEquals(values[i], result);
        }
    }

    public void testBadCharBadFirstOne()
    {
        String name="$LoadsOfMoney";
        DefaultNameMapper mapper = new DefaultNameMapper();
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LoadsOfMoney", out);
    }

    public void testBadCharBadFirstTwo()
    {
        String name="$LOADS£OF$MONEY";
        DefaultNameMapper mapper = new DefaultNameMapper();
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LOADSOFMONEY", out);
    }

    public void testBadCharGoodFirstOne()
    {
        String name="L$oads%OfMone$y$";
        DefaultNameMapper mapper = new DefaultNameMapper();
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LoadsOfMoney", out);
    }

    public void testBadCharGoodFirstTwo()
    {
        String name="LOADSOFMONE$$Y";
        DefaultNameMapper mapper = new DefaultNameMapper();
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LOADSOFMONEY", out);
    }
}

