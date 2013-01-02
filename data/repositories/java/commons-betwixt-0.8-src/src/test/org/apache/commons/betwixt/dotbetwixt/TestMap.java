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

package org.apache.commons.betwixt.dotbetwixt;


import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.TestCase;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author Brian Pugh
 */
public class TestMap extends TestCase
{

    public void testMapWithDotBetwixtFile() throws Exception
    {
        MapBean map = new MapBean();
        String key = "one";
        map.addValue(key, new Integer(1));
        StringWriter outputWriter = new StringWriter();
        outputWriter.write("<?xml version='1.0' ?>\n");
        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.setEndOfLine("\n");
        beanWriter.enablePrettyPrint();
        beanWriter.getBindingConfiguration().setMapIDs(true);
        beanWriter.write(map);
        BeanReader beanReader = new BeanReader();

        // Configure the reader
        beanReader.registerBeanClass(MapBean.class);
        StringReader xmlReader = new StringReader(outputWriter.toString());

        //Parse the xml
        MapBean result = (MapBean) beanReader.parse(xmlReader);
        assertNotNull("Should have deserialized a MapBean but got null.", result);
        assertEquals("Should have gotten the same value back from the Map after deserializing that was put in.",
                     map.getValues().get(key),
                     result.getValues().get(key));

    }
}

