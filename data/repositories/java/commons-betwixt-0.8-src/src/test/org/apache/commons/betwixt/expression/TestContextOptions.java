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
package org.apache.commons.betwixt.expression;

import junit.framework.TestCase;

import org.apache.commons.betwixt.Options;

public class TestContextOptions extends TestCase
{


    private static final String OPTION_NAME_ONE = "Option1";
    private static final String OPTION_NAME_TWO = "Option2";
    private static final String OPTION_NAME_THREE = "Option3";
    private static final String OPTION_NAME_FOUR = "Option4";
    private static final String ALPHA = "ALPHA";
    private static final String BETA = "BETA";
    private static final String GAMMA = "GAMMA";

    public void testOptionInheritance() throws Exception
    {

        Options optionsAlpha = new Options();
        optionsAlpha.addOption(OPTION_NAME_TWO, ALPHA);
        optionsAlpha.addOption(OPTION_NAME_THREE, ALPHA);
        optionsAlpha.addOption(OPTION_NAME_FOUR, ALPHA);
        Options optionsBeta = new Options();
        optionsBeta.addOption(OPTION_NAME_TWO, BETA);
        optionsBeta.addOption(OPTION_NAME_THREE, BETA);
        Options optionsGamma = new Options();
        optionsGamma.addOption(OPTION_NAME_TWO, GAMMA);

        Context context = new Context();
        context.pushOptions(optionsAlpha);
        context.pushOptions(optionsBeta);
        context.pushOptions(optionsGamma);

        assertNull("Null when no option set", context.getInheritedOption(OPTION_NAME_ONE));
        assertEquals("Return first value when that is set", GAMMA, context.getInheritedOption(OPTION_NAME_TWO));
        assertEquals("Return first value that is set", BETA, context.getInheritedOption(OPTION_NAME_THREE));
        assertEquals("Return first value that is set", ALPHA, context.getInheritedOption(OPTION_NAME_FOUR));

    }
}
