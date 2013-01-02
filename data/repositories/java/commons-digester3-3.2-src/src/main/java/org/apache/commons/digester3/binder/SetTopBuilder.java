package org.apache.commons.digester3.binder;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.commons.digester3.SetTopRule;

/**
 * Builder chained when invoking {@link LinkedRuleBuilder#setTop(String)}.
 *
 * @since 3.0
 */
public final class SetTopBuilder
    extends AbstractParamTypeBuilder<SetTopRule>
{

    SetTopBuilder( String keyPattern, String namespaceURI, RulesBinder mainBinder,
                   LinkedRuleBuilder mainBuilder, String methodName, ClassLoader classLoader )
    {
        super( keyPattern, namespaceURI, mainBinder, mainBuilder, methodName, classLoader );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected SetTopRule createRule()
    {
        SetTopRule rule;

        if ( getParamType() != null )
        {
            rule = new SetTopRule( getMethodName(), getParamType() );
        }
        else
        {
            rule = new SetTopRule( getMethodName() );
        }

        rule.setExactMatch( isUseExactMatch() );
        rule.setFireOnBegin( isFireOnBegin() );
        return rule;
    }

}
