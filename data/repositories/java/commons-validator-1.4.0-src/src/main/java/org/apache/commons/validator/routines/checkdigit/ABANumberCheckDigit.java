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
 * Modulus 10 <b>ABA Number</b> (or <b>Routing Transit Number</b> (RTN)) Check Digit
 * calculation/validation.
 * <p>
 * ABA Numbers (or Routing Transit Numbers) are a nine digit numeric code used
 * to identify American financial institutions for things such as checks or deposits
 * (ABA stands for the American Bankers Association).
 * <p>
 * Check digit calculation is based on <i>modulus 10</i> with digits being weighted
 * based on their position (from right to left) as follows:
 * <ul>
 *     <li>Digits 1, 4 and & 7 are weighted 1
 *     <li>Digits 2, 5 and & 8 are weighted 7
 *     <li>Digits 3, 6 and & 9 are weighted 3
 * </ul>
 * <p>
 * For further information see
 *  <a href="http://en.wikipedia.org/wiki/Routing_transit_number">Wikipedia -
 *  Routing transit number</a>.
 *
 * @version $Revision: 1227719 $ $Date: 2012-01-05 18:45:51 +0100 (Thu, 05 Jan 2012) $
 * @since Validator 1.4
 */
public final class ABANumberCheckDigit extends ModulusCheckDigit
{

    private static final long serialVersionUID = -8255937433810380145L;

    /** Singleton Routing Transit Number Check Digit instance */
    public static final CheckDigit ABAN_CHECK_DIGIT = new ABANumberCheckDigit();

    /** weighting given to digits depending on their right position */
    private static final int[] POSITION_WEIGHT = new int[] {3, 1, 7};

    /**
     * Construct a modulus 10 Check Digit routine for ABA Numbers.
     */
    public ABANumberCheckDigit()
    {
        super(10);
    }

    /**
     * Calculates the <i>weighted</i> value of a character in the
     * code at a specified position.
     * <p>
     * ABA Routing numbers are weighted in the following manner:
     * <pre><code>
     *     left position: 1  2  3  4  5  6  7  8  9
     *            weight: 3  7  1  3  7  1  3  7  1
     * </code></pre>
     *
     * @param charValue The numeric value of the character.
     * @param leftPos The position of the character in the code, counting from left to right
     * @param rightPos The positionof the character in the code, counting from right to left
     * @return The weighted value of the character.
     */
    protected int weightedValue(int charValue, int leftPos, int rightPos)
    {
        int weight = POSITION_WEIGHT[rightPos % 3];
        return (charValue * weight);
    }

}
