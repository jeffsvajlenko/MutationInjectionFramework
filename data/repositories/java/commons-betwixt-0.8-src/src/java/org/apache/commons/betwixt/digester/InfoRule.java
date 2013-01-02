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

import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/** <p><code>InfoRule</code> the digester Rule for parsing the info element.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class InfoRule extends RuleSupport
{

    /** Logger */
    private static final Log log = LogFactory.getLog( InfoRule.class );
    /** <code>XMLBeanInfo</code> being created */
    private XMLBeanInfo xmlBeanInfo;

    /** Base constructor */
    public InfoRule()
    {
    }

    // Rule interface
    //-------------------------------------------------------------------------

    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     * @throws SAXException if the primitiveTypes attribute contains an invalid value
     */
    public void begin(String name, String namespace, Attributes attributes) throws SAXException
    {
        Class beanClass = getBeanClass();

        xmlBeanInfo = new XMLBeanInfo( beanClass );

        String value = attributes.getValue( "primitiveTypes" );
        if ( value != null )
        {
            if ( value.equalsIgnoreCase( "element" ) )
            {
                getXMLInfoDigester().setAttributesForPrimitives( false );

            }
            else if ( value.equalsIgnoreCase( "attribute" ) )
            {
                getXMLInfoDigester().setAttributesForPrimitives( true );

            }
            else
            {
                throw new SAXException(
                    "Invalid value inside element <info> for attribute 'primitiveTypes'."
                    + " Value should be 'element' or 'attribute'" );
            }
        }

        getDigester().push(xmlBeanInfo);
    }


    /**
     * Process the end of this element.
     */
    public void end(String name, String namespace)
    {
        Object top = getDigester().pop();
    }
}
