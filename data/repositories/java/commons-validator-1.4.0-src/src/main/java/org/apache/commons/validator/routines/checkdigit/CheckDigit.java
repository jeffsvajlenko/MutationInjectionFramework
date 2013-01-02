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
 * <b>Check Digit</b> calculation and validation.
 * <p>
 * The logic for validating check digits has previously been
 * embedded within the logic for specific code validation, which
 * includes other validations such as verifying the format
 * or length of a code. {@link CheckDigit} provides for separating out
 * the check digit calculation logic enabling it to be more easily
 * tested and reused.
 * <p>
 * Although Commons Validator is primarily concerned with validation,
 * {@link CheckDigit} also defines behaviour for calculating/generating check
 * digits, since it makes sense that users will want to (re-)use the
 * same logic for both. The {@link org.apache.commons.validator.routines.ISBNValidator}
 * makes specific use of this feature by providing the facility to validate ISBN-10 codes
 * and then convert them to the new ISBN-13 standard.
 * <p>
 * {@link CheckDigit} is used by the new generic
 * <a href="..\CodeValidator.html">CodeValidator</a> implementation.
 * <p>
 * <h3>Implementations</h3>
 * See the
 * <a href="package-summary.html">Package Summary</a> for a full
 * list of implementations provided within Commons Validator.
 *
 * @see org.apache.commons.validator.routines.CodeValidator
 * @version $Revision: 589328 $ $Date: 2007-10-28 11:43:47 +0100 (Sun, 28 Oct 2007) $
 * @since Validator 1.4
 */
public interface CheckDigit
{

    /**
     * Calculate the <i>Check Digit</i> for a code.
     *
     * @param code The code to calculate the Check Digit for.
     * @return The calculated Check Digit
     * @throws CheckDigitException if an error occurs.
     */
    public String calculate(String code) throws CheckDigitException;

    /**
     * Validate the check digit for the code.
     *
     * @param code The code to validate.
     * @return <code>true</code> if the check digit is valid, otherwise
     * <code>false</code>.
     */
    public boolean isValid(String code);

}
