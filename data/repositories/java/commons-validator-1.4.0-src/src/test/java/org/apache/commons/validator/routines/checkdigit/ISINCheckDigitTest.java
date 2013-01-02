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
package org.apache.commons.validator.routines.checkdigit;


/**
 * ISIN Check Digit Test.
 *
 * @version $Revision: 909009 $
 * @since Validator 1.4
 */
public class ISINCheckDigitTest extends AbstractCheckDigitTest
{

    /**
     * Constructor
     * @param name test name
     */
    public ISINCheckDigitTest(String name)
    {
        super(name);
    }

    /**
     * Set up routine & valid codes.
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        routine = ISINCheckDigit.ISIN_CHECK_DIGIT;
        valid = new String[] {"US0378331005",
                              "BMG8571G1096",
                              "AU0000XVGZA3",
                              "GB0002634946",
                              "FR0004026250",
                              "DK0009763344"
                             };
        invalid = new String[] {"0378#3100"};
    }
}
