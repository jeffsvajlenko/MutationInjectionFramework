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

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;

/**
 * Rules common to both {@link org.apache.commons.betwixt.digester.MultiMappingBeanInfoDigester}
 * and {@link org.apache.commons.betwixt.digester.XMLBeanInfoDigester}.
 */
public class CommonRuleSet extends RuleSetBase
{

    /**
     * Adds rule instances.
     * @param digester <code>Digester</code>, not null
     * @since 0.8
     */
    public void addRuleInstances(Digester digester)
    {
        digester.addRule("*/element", new ElementRule());
        digester.addRule( "*/text", new TextRule() );
        digester.addRule("*/attribute", new AttributeRule());
        digester.addRule("*/hide", new HideRule());
        digester.addRule("*/addDefaults", new AddDefaultsRule());

        OptionRule optionRule = new OptionRule();
        digester.addRule( "*/option", optionRule );
        digester.addRule( "*/option/name", optionRule.getNameRule() );
        digester.addRule( "*/option/value", optionRule.getValueRule() );

    }

}
