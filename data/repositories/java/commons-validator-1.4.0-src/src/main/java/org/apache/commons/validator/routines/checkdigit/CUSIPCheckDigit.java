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
 * Modulus 10 <b>CUSIP</b> (North American Securities)
 * Check Digit calculation/validation.
 * <p>
 * CUSIP Numbers are 9 character alphanumeric codes used
 * to identify North American Securities.
 * <p>
 * Check digit calculation uses the <i>Modulus 10 Double Add Double</i> technique
 * with every second digit being weighted by 2. Alphabetic characters are
 * converted to numbers by their position in the alphabet starting with A being 10.
 * Weighted numbers greater than ten are treated as two separate numbers.
 * <p>
 *
 * <p>
 * See <a href="http://en.wikipedia.org/wiki/CUSIP">Wikipedia - CUSIP</a>
 * for more details.
 *
 * @version $Revision: 1227719 $ $Date: 2012-01-05 18:45:51 +0100 (Thu, 05 Jan 2012) $
 * @since Validator 1.4
 */
public final class CUSIPCheckDigit extends ModulusCheckDigit
{

    private static final long serialVersionUID = 666941918490152456L;

    /** Singleton CUSIP Check Digit instance */
    public static final CheckDigit CUSIP_CHECK_DIGIT = new CUSIPCheckDigit();

    /** weighting given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = new int[] {2, 1};

    /**
     * Construct an CUSIP Indetifier Check Digit routine.
     */
    public CUSIPCheckDigit()
    {
        super(10);
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

    /**
     * <p>Calculates the <i>weighted</i> value of a charcter in the
     * code at a specified position.</p>
     *
     * <p>For Luhn (from right to left) <b>odd</b> digits are weighted
     * with a factor of <b>one</b> and <b>even</b> digits with a factor
     * of <b>two</b>. Weighted values > 9, have 9 subtracted</p>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    protected int weightedValue(int charValue, int leftPos, int rightPos)
    {
        int weight = POSITION_WEIGHT[rightPos % 2];
        int weightedValue = (charValue * weight);
        return ModulusCheckDigit.sumDigits(weightedValue);
    }
}
