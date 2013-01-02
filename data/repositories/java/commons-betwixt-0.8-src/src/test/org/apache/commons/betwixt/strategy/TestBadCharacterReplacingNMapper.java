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

/** Test harness for the BadCharacterReplacingNMapper
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class TestBadCharacterReplacingNMapper extends TestCase
{


    public static Test suite()
    {
        return new TestSuite(TestBadCharacterReplacingNMapper.class);
    }

    public TestBadCharacterReplacingNMapper(String testName)
    {
        super(testName);
    }

    public void testNoReplacementBadFirstNoChainedMapper()
    {
        String name="$LoadsOfMoney";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(null);
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LoadsOfMoney", out);
    }

    public void testNoReplacementBadFirstWithChainedMapper()
    {
        String name="$LOADS£OF$MONEY";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(new PlainMapper());
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LOADSOFMONEY", out);
    }

    public void testNoReplacementGoodFirstNoChainedMapper()
    {
        String name="L$oads%OfMone$y$";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(null);
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LoadsOfMoney", out);
    }

    public void testNoReplacementGoodFirstWithChainedMapper()
    {
        String name="LOADSOFMONE$$Y";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(new PlainMapper());
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "LOADSOFMONEY", out);
    }

    public void testReplacementBadFirstNoChainedMapper()
    {
        String name="$LoadsOfMoney$";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(null);
        mapper.setReplacement(new Character('_'));
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "_LoadsOfMoney_", out);
    }

    public void testReplacementBadFirstWithChainedMapper()
    {
        String name="$LOADS£OF$MONEY";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(new PlainMapper());
        mapper.setReplacement(new Character('_'));
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "_LOADS_OF_MONEY", out);
    }

    public void testReplacementGoodFirstNoChainedMapper()
    {
        String name="L$$$$$oads%OfMone$y$";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(null);
        mapper.setReplacement(new Character('_'));
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "L_____oads_OfMone_y_", out);
    }

    public void testReplacementGoodFirstWithChainedMapper()
    {
        String name="L$OADSOFMONE$$$$$Y";
        BadCharacterReplacingNMapper mapper = new BadCharacterReplacingNMapper(new PlainMapper());
        mapper.setReplacement(new Character('_'));
        String out = mapper.mapTypeToElementName(name);
        assertEquals("Expected", "L_OADSOFMONE_____Y", out);
    }

    private class PlainMapper implements NameMapper
    {
        public String mapTypeToElementName(String typeName)
        {
            return typeName;
        }
    }
}
