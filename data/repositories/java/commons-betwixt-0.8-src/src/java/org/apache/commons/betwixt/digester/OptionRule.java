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

import org.apache.commons.betwixt.Descriptor;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;

/**
 * Maps option tree to an option in the
 * {@link org.apache.commons.betwixt.Options}
 * on the current description.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @since 0.5
 */
public class OptionRule extends Rule
{

    private String currentValue;
    private String currentName;

    /**
     * @see org.apache.commons.digester.Rule#begin(java.lang.String, java.lang.String, Attributes)
     */
    public void begin(String namespace, String name, Attributes attributes)
    throws Exception
    {
        currentValue = null;
        currentName = null;
    }



    /**
     * @see org.apache.commons.digester.Rule#end(java.lang.String, java.lang.String)
     */
    public void end(String namespace, String name)
    {
        if (currentName != null && currentValue != null)
        {
            Object top = getDigester().peek();
            if (top instanceof Descriptor)
            {
                Descriptor descriptor = (Descriptor) top;
                descriptor.getOptions().addOption(currentName, currentValue);
            }
        }
    }

    /**
     * Gets the rule that maps the <code>name</code> element
     * associated with the option
     * @return <code>Rule</code>, not null
     */
    public Rule getNameRule()
    {
        return new Rule()
        {
            public void body(String namespace, String name, String text)
            {
                currentName = text;
            }
        };
    }

    /**
     * Gets the rule that maps the <code>value</code> element
     * associated with the option
     * @return <code>Rule</code>, not null
     */
    public Rule getValueRule()
    {
        return new Rule()
        {
            public void body(String namespace, String name, String text)
            {
                currentValue = text;
            }
        };
    }
}
