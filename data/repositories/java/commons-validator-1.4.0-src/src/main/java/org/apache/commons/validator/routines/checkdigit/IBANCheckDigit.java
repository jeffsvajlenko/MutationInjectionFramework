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

import java.io.Serializable;

/**
 * <b>IBAN</b> (International Bank Account Number) Check Digit calculation/validation.
 * <p>
 * This rountine is based on the ISO 7064 Mod 97,10 check digit caluclation routine.
 * <p>
 * The two check digit characters in a IBAN number are the third and fourth characters
 * in the code. For <i>check digit</i> calculation/validation the first four characters are moved
 * to the end of the code.
 *  So <code>CCDDnnnnnnn</code> becomes <code>nnnnnnnCCDD</code> (where
 *  <code>CC</code> is the country code and <code>DD</code> is the check digit). For
 *  check digit calcualtion the check digit value should be set to zero (i.e.
 *  <code>CC00nnnnnnn</code> in this example.
 * <p>
 * For further information see
 *  <a href="http://en.wikipedia.org/wiki/International_Bank_Account_Number">Wikipedia -
 *  IBAN number</a>.
 *
 * @version $Revision: 1227719 $ $Date: 2012-01-05 18:45:51 +0100 (Thu, 05 Jan 2012) $
 * @since Validator 1.4
 */
public final class IBANCheckDigit implements CheckDigit, Serializable
{

    private static final long serialVersionUID = -3600191725934382801L;

    /** Singleton IBAN Number Check Digit instance */
    public static final CheckDigit IBAN_CHECK_DIGIT = new IBANCheckDigit();

    private static final long MAX = 999999999;

    private static final long MODULUS = 97;

    /**
     * Construct Check Digit routine for IBAN Numbers.
     */
    public IBANCheckDigit()
    {
    }

    /**
     * Validate the check digit for an the IBAN code.
     *
     * @param code The code to validate
     * @return <code>true</code> if the check digit is valid, otherwise
     * <code>false</code>
     */
    public boolean isValid(String code)
    {
        if (code == null || code.length() < 5)
        {
            return false;
        }
        try
        {
            int modulusResult = calculateModulus(code);
            return (modulusResult == 1);
        }
        catch (CheckDigitException  ex)
        {
            return false;
        }
    }

    /**
     * Calculate the <i>Check Digit</i> for an IBAN code.
     * <p>
     * <b>Note:</b> The check digit is the third and fourth
     * characters and and should contain value "<code>00</code>".
     *
     * @param code The code to calculate the Check Digit for
     * @return The calculated Check Digit
     * @throws CheckDigitException if an error occurs calculating
     * the check digit for the specified code
     */
    public String calculate(String code) throws CheckDigitException
    {
        if (code == null || code.length() < 5)
        {
            throw new CheckDigitException("Invalid Code length=" +
                                          (code == null ? 0 : code.length()));
        }
        int modulusResult = calculateModulus(code);
        int charValue = (98 - modulusResult);
        String checkDigit = Integer.toString(charValue);
        return (charValue > 9 ? checkDigit : "0" + checkDigit);
    }

    /**
     * Calculate the modulus for a code.
     *
     * @param code The code to calculate the modulus for.
     * @return The modulus value
     * @throws CheckDigitException if an error occurs calculating the modulus
     * for the specified code
     */
    private int calculateModulus(String code) throws CheckDigitException
    {
        String reformattedCode = code.substring(4) + code.substring(0, 4);
        long total = 0;
        for (int i = 0; i < reformattedCode.length(); i++)
        {
            int charValue = Character.getNumericValue(reformattedCode.charAt(i));
            if (charValue < 0 || charValue > 35)
            {
                throw new CheckDigitException("Invalid Character[" +
                                              i + "] = '" + charValue + "'");
            }
            total = (charValue > 9 ? total * 100 : total * 10) + charValue;
            if (total > MAX)
            {
                total = (total % MODULUS);
            }
        }
        return (int)(total % MODULUS);
    }

}
