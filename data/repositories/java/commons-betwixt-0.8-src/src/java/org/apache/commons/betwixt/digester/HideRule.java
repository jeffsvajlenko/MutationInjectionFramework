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
package org.apache.commons.betwixt.digester;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** <p><code>HideRule</code> hides the property of the given name.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class HideRule extends RuleSupport
{

    /** Logger */
    private static final Log log = LogFactory.getLog( HideRule.class );

    /** Base constructor */
    public HideRule()
    {
    }

    // Rule interface
    //-------------------------------------------------------------------------

    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     * @throws SAXException when the mandatory 'property' attribute is missing
     */
    public void begin(String name, String namespace, Attributes attributes) throws SAXException
    {
        String propertyAttributeValue = attributes.getValue( "property" );
        if ( propertyAttributeValue == null || propertyAttributeValue.length() == 0 )
        {
            throw new SAXException(
                "<hide> element is missing the mandatory attribute 'property'" );
        }
        Set propertySet = getProcessedPropertyNameSet();
        propertySet.add( propertyAttributeValue );
    }
}
