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


import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanWriter;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestMixedCollections extends AbstractTestCase
{

    public TestMixedCollections(String testName)
    {
        super(testName);
    }

    public void testNoNameIntrospection() throws Exception
    {

        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        xmlIntrospector.getConfiguration().setWrapCollectionsInElement(false);
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(MixedCollectionBean.class);
        ElementDescriptor elementDescriptor = xmlBeanInfo.getElementDescriptor();
        ElementDescriptor[] childDescriptors = elementDescriptor.getElementDescriptors();
        assertEquals("One child", 1, childDescriptors.length);
        assertNull("Expected null name", childDescriptors[0].getLocalName());
    }

    public void testNoNameWrite() throws Exception
    {
        MixedCollectionBean bean = new MixedCollectionBean();
        bean.getGubbins().add(new String("Blake"));
        bean.getGubbins().add(new Integer(7));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(bean);
        String expected = "<?xml version='1.0'?>" +
                          "<stuff><String>Blake</String><Integer>7</Integer></stuff>";

        xmlAssertIsomorphic(parseString(expected), parseString(out));
    }
}
