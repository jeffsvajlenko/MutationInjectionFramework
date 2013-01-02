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

import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** <p><code>RuleSupport</code> is an abstract base class containing useful
  * helper methods.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class RuleSupport extends Rule
{

    /** Logger */
    private static final Log log = LogFactory.getLog( RuleSupport.class );
    /** Base constructor */
    public RuleSupport()
    {
    }



    // Implementation methods
    //-------------------------------------------------------------------------
    /**
     * Gets <code>XMLBeanInfoDigester</code> using this rule.
     *
     * @return <code>XMLBeanInfoDigester</code> for this rule
     */
    protected XMLBeanInfoDigester getXMLInfoDigester()
    {
        return (XMLBeanInfoDigester) getDigester();
    }

    /**
    * Gets <code>XMLIntrospector</code> to be used for introspection
    *
    * @return <code>XMLIntrospector</code> to use
    */
    protected XMLIntrospector getXMLIntrospector()
    {
        return getXMLInfoDigester().getXMLIntrospector();
    }

    /**
     * Gets the class of the bean whose .betwixt file is being digested
     *
     * @return the <code>Class</code> of the bean being processed
     */
    protected Class getBeanClass()
    {
        return getXMLInfoDigester().getBeanClass();
    }

    /**
     * Gets the property names already processed
     *
     * @return the set of property names that have been processed so far
     */
    protected Set getProcessedPropertyNameSet()
    {
        return getXMLInfoDigester().getProcessedPropertyNameSet();
    }
}
