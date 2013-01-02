/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.model.beans;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathAbstractFactoryException;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;
import org.apache.commons.jxpath.Pointer;

/**
 * Badly-implemented Factory test.  From JIRA JXPATH-68.
 *
 * @author Matt Benson
 * @version $Revision: 658784 $ $Date: 2008-05-21 12:44:55 -0500 (Wed, 21 May 2008) $
 */
public class BadlyImplementedFactoryTest extends TestCase
{

    private JXPathContext context;

    public void setUp()
    {
        context = JXPathContext.newContext(new HashMap());
        context.setFactory(new AbstractFactory()
        {
            public boolean createObject(JXPathContext context, Pointer pointer, Object parent, String name, int index)
            {
                ((Map) parent).put(name, null);
                return true;
            }
        });
    }

    public void testBadFactoryImplementation()
    {
        try
        {
            context.createPath("foo/bar");
            fail("should fail with JXPathException caused by JXPathAbstractFactoryException");
        }
        catch (JXPathException e)
        {
            assertTrue(e.getCause() instanceof JXPathAbstractFactoryException);
        }
    }

}
