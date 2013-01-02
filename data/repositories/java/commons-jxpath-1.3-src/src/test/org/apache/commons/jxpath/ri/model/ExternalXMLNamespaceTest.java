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
package org.apache.commons.jxpath.ri.model;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathTestCase;
import org.apache.commons.jxpath.xml.DocumentContainer;

/**
 * Test externally registered XML namespaces; JXPATH-97.
 *
 * @author Matt Benson
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public class ExternalXMLNamespaceTest extends JXPathTestCase
{
    protected JXPathContext context;

    protected DocumentContainer createDocumentContainer(String model)
    {
        DocumentContainer result = new DocumentContainer(JXPathTestCase.class
                .getResource("ExternalNS.xml"), model);
        // this setting only works for DOM, so no JDOM tests :|
        result.setNamespaceAware(false);
        return result;
    }

    protected JXPathContext createContext(String model)
    {
        JXPathContext context = JXPathContext
                                .newContext(createDocumentContainer(model));
        context.registerNamespace("A", "foo");
        context.registerNamespace("B", "bar");
        return context;
    }

    protected void doTest(String xpath, String model, String expected)
    {
        assertXPathValue(createContext(model), xpath, expected);
    }

    protected void doTestAttribute(String model)
    {
        doTest("/ElementA/@A:myAttr", model, "Mytype");
    }

    protected void doTestElement(String model)
    {
        doTest("/ElementA/B:ElementB", model, "MY VALUE");
    }

    protected void doTestCreateAndSetAttribute(String model)
    {
        assertXPathCreatePathAndSetValue(createContext(model),
                                         "/ElementA/@A:newAttr", "newValue", "/ElementA[1]/@A:newAttr");
    }

    public void testAttributeDOM()
    {
        doTestAttribute(DocumentContainer.MODEL_DOM);
    }

    public void testElementDOM()
    {
        doTestElement(DocumentContainer.MODEL_DOM);
    }

    public void testCreateAndSetAttributeDOM()
    {
        doTestCreateAndSetAttribute(DocumentContainer.MODEL_DOM);
    }

}
