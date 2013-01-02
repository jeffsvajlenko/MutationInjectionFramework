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
import org.apache.commons.betwixt.BindingConfiguration;
import org.apache.commons.betwixt.LibraryBeanWithArraySetter;
import org.apache.commons.betwixt.io.read.ArrayBindAction;
import org.apache.commons.betwixt.io.read.MappingAction;
import org.apache.commons.betwixt.io.read.ReadConfiguration;
import org.apache.commons.betwixt.io.read.ReadContext;
import org.xml.sax.helpers.AttributesImpl;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestBaseMappingStrategy extends AbstractTestCase
{

    public TestBaseMappingStrategy(String testName)
    {
        super(testName);
    }

    public void testArrayMapping() throws Exception
    {
        ReadContext context = new ReadContext(
            new BindingConfiguration(),
            new ReadConfiguration());

        context.pushElement("LibraryBeanWithArraySetter");
        context.markClassMap(LibraryBeanWithArraySetter.class);
        context.pushElement("books");

        ActionMappingStrategy strategy = ActionMappingStrategy.DEFAULT;
        MappingAction action = strategy.getMappingAction("", "books", new AttributesImpl(), context);
        assertTrue("Should be mapped to an array bind action", action instanceof ArrayBindAction);
    }
}
