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

import java.lang.reflect.Method;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.AbstractTestCase;

/** Test harness for map updating
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class TestUpdaters extends AbstractTestCase
{

    public static Test suite()
    {
        return new TestSuite(TestUpdaters.class);
    }

    public TestUpdaters(String testName)
    {
        super(testName);
    }

    public void testMapUpdate() throws Exception
    {
        Class[] params = { String.class, String.class } ;
        Method method = AdderBean.class.getMethod("add", params);
        MapEntryAdder adder = new MapEntryAdder(method);

        AdderBean bean = new AdderBean();
        bean.add("UNSET", "UNSET");

        Updater keyUpdater = adder.getKeyUpdater();
        Updater valueUpdater = adder.getValueUpdater();

        Context context = new Context();
        context.setBean(bean);

        keyUpdater.update(context, "key");
        valueUpdater.update(context, "value");

        assertEquals("AdderBean not updated (1)", "key", bean.getKey());
        assertEquals("AdderBean not updated (2)", "value", bean.getValue());

        keyUpdater.update(context, "new-key");
        valueUpdater.update(context, "new-value");

        assertEquals("AdderBean not updated (1)", "new-key", bean.getKey());
        assertEquals("AdderBean not updated (2)", "new-value", bean.getValue());

    }
}

