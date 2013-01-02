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
package org.apache.commons.betwixt.expression;

import junit.framework.TestCase;

import org.apache.commons.betwixt.Options;

/**
 */
public class TestContext extends TestCase
{

    public void testOptions() throws Exception
    {
        Context context = new Context();
        assertNull(context.getOptions());
        Options firstOptions = new Options();
        context.pushOptions(firstOptions);
        assertEquals(firstOptions, context.getOptions());
        Options secondOptions = new Options();
        context.pushOptions(secondOptions);
        assertEquals(secondOptions, context.getOptions());
        context.popOptions();
        assertEquals(firstOptions, context.getOptions());
        context.popOptions();
        assertNull(context.getOptions());
    }
}
