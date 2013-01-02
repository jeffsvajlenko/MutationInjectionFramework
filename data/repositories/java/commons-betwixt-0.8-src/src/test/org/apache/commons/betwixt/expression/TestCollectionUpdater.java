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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

/**
 * Tests for {@link CollectionUpdater}.
 */
public class TestCollectionUpdater extends TestCase
{

    private CollectionUpdater updater = CollectionUpdater.getInstance();
    private Context context = new Context();

    protected void setUp() throws Exception
    {
        super.setUp();
        updater = CollectionUpdater.getInstance();
        context = new Context();
    }

    public void testUpdateNull() throws Exception
    {
        context.setBean(null);
        updater.update(context, null);
        updater.update(context, "Whatever");
    }

    public void testUpdateNotCollection() throws Exception
    {
        context.setBean("Whatever");
        updater.update(context, null);
        updater.update(context, "Whatever");
    }

    public void testUpdateCollection() throws Exception
    {
        List list = new ArrayList();
        context.setBean(list);
        updater.update(context, null);
        updater.update(context, "Whatever");
        assertEquals("Updater updates the list with the value", 1, list.size());
        updater.update(context, "Thus");
        assertEquals("Updater updates the list with the value",  2, list.size());
        assertEquals("Updater updates the list in order", "Whatever", list.get(0));
        assertEquals("Updater updates the list in order", "Thus", list.get(1));
    }
}
