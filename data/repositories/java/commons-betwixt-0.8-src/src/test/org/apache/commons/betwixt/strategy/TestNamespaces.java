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

import org.apache.commons.betwixt.AbstractTestCase;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestNamespaces extends AbstractTestCase
{

    public TestNamespaces(String name)
    {
        super(name);
    }

    public void testNamespacePrefixMapper()
    {
        NamespacePrefixMapper mapper = new NamespacePrefixMapper();
        mapper.setPrefix("http://www.w3.org/2001/XMLSchema", "xsd");
        assertEquals("Expected prefix set earlier", "xsd", mapper.getPrefix("http://www.w3.org/2001/XMLSchema"));
    }

    public void testNamespacePrefixMapperPrefixGeneration()
    {
        NamespacePrefixMapper mapper = new NamespacePrefixMapper();
        mapper.setPrefix("http://www.w3.org/2001/XMLSchema", "xsd");
        assertNotNull("Expected prefix assigned not to be null", mapper.getPrefix("http://jakarta.apache.org/commons/Betwixt"));
    }

    public void testNamespacePrefixMapperMatchingPrefix()
    {
        NamespacePrefixMapper mapper = new NamespacePrefixMapper();
        mapper.setPrefix("http://www.w3.org/2001/XMLSchema", "bt1");
        String prefix = mapper.getPrefix("http://jakarta.apache.org/commons/Betwixt");
        assertFalse("Generated should not clash", prefix.equals(mapper.getPrefix("http://www.w3.org/2001/XMLSchema")));
    }
}
