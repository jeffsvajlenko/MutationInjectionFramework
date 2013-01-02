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


package org.apache.commons.betwixt.io.read;

import java.util.HashMap;
import java.util.Iterator;


/**
 * <code>Elements</code> is a sample bean for use with test cases
 *
 * @author <a href="mailto:tima@intalio.com">Tim Anderson</a>
 * @version $Revision: 438373 $
 */
public class Elements
{

    private HashMap elements = new HashMap();

    public Elements()
    {
    }

    public void addElement(Element element)
    {
        elements.put(element.getValue(), element);
    }

    public Iterator getElements()
    {
        return elements.values().iterator();
    }

    public Element getElement(String name)
    {
        return (Element) elements.get(name);
    }

}
