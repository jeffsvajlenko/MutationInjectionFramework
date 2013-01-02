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

import java.beans.BeanDescriptor;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.XMLIntrospector;

/** Test harness for the HyphenatedNameMapper
  *
  * @author <a href="mailto:jason@zenplex.com">Jason van Zyl</a>
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
  * @version $Revision: 438373 $
  */
public class TestHyphenatedNameMapper extends TestCase
{

    public static Test suite()
    {
        return new TestSuite(TestHyphenatedNameMapper.class);
    }

    public TestHyphenatedNameMapper(String testName)
    {
        super(testName);
    }

    public void testLowerCase()
    {
        HyphenatedNameMapper mapper = new HyphenatedNameMapper();
        String result = mapper.mapTypeToElementName("FooBar");
        assertEquals("foo-bar", result);
    }

    public void testLowerCaseViaBeanDescriptor()
    {
        HyphenatedNameMapper mapper = new HyphenatedNameMapper(false, "_");
        BeanDescriptor bd = new BeanDescriptor(getClass());
        String result = mapper.mapTypeToElementName(bd.getName());
        assertEquals("test_hyphenated_name_mapper", result);
    }

    public void testUpperCase()
    {
        HyphenatedNameMapper mapper = new HyphenatedNameMapper(true, "_");
        String result = mapper.mapTypeToElementName("FooBar");
        assertEquals("FOO_BAR", result);
    }

    public void testUpperCaseViaProperties()
    {
        HyphenatedNameMapper mapper = new HyphenatedNameMapper();
        mapper.setUpperCase(true);
        mapper.setSeparator("_");
        String result = mapper.mapTypeToElementName("FooBar");
        assertEquals("FOO_BAR", result);
    }

    /**
     * A more "complicated" exmple
     */
    public void testUpperCaseLongViaProperties()
    {
        HyphenatedNameMapper mapper = new HyphenatedNameMapper(true, "__");
        String result = mapper.mapTypeToElementName("FooBarFooBar");
        assertEquals("FOO__BAR__FOO__BAR", result);

    }

    public void testBeanWithAdd() throws Exception
    {
        //
        // simple test this one
        // a problem was reported when introspecting classes with 'add' properties
        // when using the HyphenatedNameMapper
        // basically, the test is that no exception is thrown
        //
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        introspector.introspect(new ArrayList());
    }
}
