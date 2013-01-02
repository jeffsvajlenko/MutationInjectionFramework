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

import java.util.HashMap;
import java.util.Map;

/**
 * <code>XMLSingleMappingFileBeanInfoDigester</code> is a digester of XML files
 * containing XMLBeanInfo definitions for a JavaBeans.
 *
 * @since 0.7
 * @author Brian Pugh
 */
public class MultiMappingBeanInfoDigester extends XMLBeanInfoDigester
{
    /** <code>XMLBeanInfo</code>'s indexed by <code>Class</code> */
    private Map beanInfoMap = new HashMap();

    // Implementation methods
    //-------------------------------------------------------------------------
    /**
     * Reset configure for new digestion.
     */
    protected void configure()
    {
        if (!configured)
        {
            configured = true;

            // add the various rules
            addRule("betwixt-config", new ConfigRule());
            addRule("betwixt-config/class", new ClassRule());
            addRuleSet(new CommonRuleSet());
        }

        // now initialize
        //setAttributesForPrimitives(true);
        getProcessedPropertyNameSet().clear();
        getXMLIntrospector().getRegistry().flush();
    }

    /**
     * Map containing <code>XMLBeanInfo</code> classes.
     * Keys are the <code>Class</code> and values are the <code>XMLBeanInfo</code> objects.
     *
     * @return map of XMLBeanInfos
     */
    public Map getBeanInfoMap()
    {
        return beanInfoMap;
    }

    /**
     * Set the Map containing <code>XMLBeanInfo</code> classes.
     * @param beanInfoMap map to set.
     */
    public void setBeanInfoMap(Map beanInfoMap)
    {
        this.beanInfoMap = beanInfoMap;
    }

}
