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
 * Modulus 10 <b>SEDOL</b> (UK Securities) Check Digit calculation/validation.
 * <p>
 * SEDOL Numbers are 7 character alphanumeric codes used
 * to identify UK Securities (SEDOL stands for Stock Exchange Daily Official List).
 * <p>
 * Check digit calculation is based on <i>modulus 10</i> with digits being weighted
 * based on their position, from left to right, as follows:
 * <p>
 * <pre><code>
 *      position:  1  2  3  4  5  6  7
 *     weighting:  1  3  1  7  3  9  1
 * </code></pre>
 * <p>
 * See <a href="http://en.wikipedia.org/wiki/SEDOL">Wikipedia - SEDOL</a>
 * for more details.
 *
 * @version $Revision: 1227719 $ $Date: 2012-01-05 18:45:51 +0100 (Thu, 05 Jan 2012) $
 * @since Validator 1.4
 */
public final class SedolCheckDigit extends ModulusCheckDigit
{

    private static final long serialVersionUID = -8976881621148878443L;

    /** Singleton SEDOL check digit instance */
    public static final CheckDigit SEDOL_CHECK_DIGIT = new SedolCheckDigit();

    /** weighting given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = new int[] {1, 3, 1, 7, 3, 9, 1};

    /**
     * Construct a modulus 11 Check Digit routine for ISBN-10.
     */
    public SedolCheckDigit()
    {
        super(10);
    }

    /**
     * Calculate the modulus for an SEDOL code.
     *
     * @param code The code to calculate the modulus for.
     * @param includesCheckDigit Whether the code includes the Check Digit or not.
     * @return The modulus value
     * @throws CheckDigitException if an error occurs calculating the modulus
     * for the specified code
     */
    protected int calculateModulus(String code, boolean includesCheckDigit) throws CheckDigitException
    {
        if (code.length() > 7)
        {
            throw new CheckDigitException("Invalid Code Length = " + code.length());
        }
        return super.calculateModulus(code, includesCheckDigit);
    }

    /**
     * Calculates the <i>weighted</i> value of a charcter in the
     * code at a specified position.
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    protected int weightedValue(int charValue, int leftPos, int rightPos)
    {
        return (charValue * POSITION_WEIGHT[leftPos - 1]);
    }

    /**
     * Convert a character at a specified position to an integer value.
     *
     * @param character The character to convert
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The integer value of the character
     * @throws CheckDigitException if character is not alphanumeric
     */
    protected int toInt(char character, int leftPos, int rightPos)
    throws CheckDigitException
    {
        int charValue = Character.getNumericValue(character);
        if (charValue < 0 || charValue > 35)
        {
            throw new CheckDigitException("Invalid Character[" +
                                          leftPos + "] = '" + charValue + "'");
        }
        return charValue;
    }

}
