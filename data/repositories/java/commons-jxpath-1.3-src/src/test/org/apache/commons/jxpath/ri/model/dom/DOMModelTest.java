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
package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.ri.model.XMLModelTestCase;
import org.apache.commons.jxpath.xml.DocumentContainer;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Tests JXPath with DOM
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */

public class DOMModelTest extends XMLModelTestCase
{

    protected String getModel()
    {
        return DocumentContainer.MODEL_DOM;
    }

    protected AbstractFactory getAbstractFactory()
    {
        return new TestDOMFactory();
    }

    public void testGetNode()
    {
        assertXPathNodeType(context, "/", Document.class);
        assertXPathNodeType(context, "/vendor/location", Element.class);
        assertXPathNodeType(context, "//location/@name", Attr.class);
        assertXPathNodeType(context, "//vendor", Element.class);
    }

    protected String getXMLSignature(
        Object node,
        boolean elements,
        boolean attributes,
        boolean text,
        boolean pi)
    {
        StringBuffer buffer = new StringBuffer();
        appendXMLSignature(buffer, node, elements, attributes, text, pi);
        return buffer.toString();
    }

    private void appendXMLSignature(
        StringBuffer buffer,
        Object object,
        boolean elements,
        boolean attributes,
        boolean text,
        boolean pi)
    {
        Node node = (Node) object;
        int type = node.getNodeType();
        switch (type)
        {
        case Node.DOCUMENT_NODE :
            buffer.append("<D>");
            appendXMLSignature(
                buffer,
                node.getChildNodes(),
                elements,
                attributes,
                text,
                pi);
            buffer.append("</D");
            break;

        case Node.ELEMENT_NODE :
            String tag = elements ? ((Element) node).getTagName() : "E";
            buffer.append("<");
            buffer.append(tag);
            buffer.append(">");
            appendXMLSignature(
                buffer,
                node.getChildNodes(),
                elements,
                attributes,
                text,
                pi);
            buffer.append("</");
            buffer.append(tag);
            buffer.append(">");
            break;

        case Node.TEXT_NODE :
        case Node.CDATA_SECTION_NODE :
            if (text)
            {
                String string = node.getNodeValue();
                string = string.replace('\n', '=');
                buffer.append(string);
            }
            break;
        }
    }

    private void appendXMLSignature(
        StringBuffer buffer,
        NodeList children,
        boolean elements,
        boolean attributes,
        boolean text,
        boolean pi)
    {
        for (int i = 0; i < children.getLength(); i++)
        {
            appendXMLSignature(
                buffer,
                children.item(i),
                elements,
                attributes,
                text,
                pi);
        }
    }
}
