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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

/**
 * Luhn Check Digit Test.
 *
 * @version $Revision: 909004 $
 * @since Validator 1.4
 */
public abstract class AbstractCheckDigitTest extends TestCase
{

    /** logging instance */
    protected Log log = LogFactory.getLog(getClass());

    /** Check digit routine being tested */
    protected int checkDigitLth = 1;

    /** Check digit routine being tested */
    protected CheckDigit routine;

    /** Array of valid code values */
    protected String[] valid;

    /** Array of invalid code values */
    protected String[] invalid = new String[] {"12345678A"};

    /** code value which sums to zero */
    protected String zeroSum = "0000000000";

    /** Prefix for error messages */
    protected String missingMessage = "Code is missing";

    /**
     * Constructor
     * @param name test name
     */
    public AbstractCheckDigitTest(String name)
    {
        super(name);
    }

    /**
     * Tear Down - clears routine and valid codes.
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
        valid = null;
        routine = null;
    }

    /**
     * Test isValid() for valid values.
     */
    public void testIsValidTrue()
    {
        if (log.isDebugEnabled())
        {
            log.debug("testIsValidTrue() for " + routine.getClass().getName());
        }

        // test valid values
        for (int i = 0; i < valid.length; i++)
        {
            if (log.isDebugEnabled())
            {
                log.debug("   " + i + " Testing Valid Code=[" + valid[i] + "]");
            }
            assertTrue("valid[" + i +"]: " + valid[i], routine.isValid(valid[i]));
        }
    }

    /**
     * Test isValid() for invalid values.
     */
    public void testIsValidFalse()
    {
        if (log.isDebugEnabled())
        {
            log.debug("testIsValidFalse() for " + routine.getClass().getName());
        }

        // test invalid code values
        for (int i = 0; i < invalid.length; i++)
        {
            if (log.isDebugEnabled())
            {
                log.debug("   " + i + " Testing Invalid Code=[" + invalid[i] + "]");
            }
            assertFalse("invalid[" + i +"]: " + invalid[i], routine.isValid(invalid[i]));
        }

        // test invalid check digit values
        String[] invalidCheckDigits = createInvalidCodes(valid);
        for (int i = 0; i < invalidCheckDigits.length; i++)
        {
            if (log.isDebugEnabled())
            {
                log.debug("   " + i + " Testing Invalid Check Digit, Code=[" + invalidCheckDigits[i] + "]");
            }
            assertFalse("invalid check digit[" + i +"]: " + invalidCheckDigits[i], routine.isValid(invalidCheckDigits[i]));
        }
    }

    /**
     * Test calculate() for valid values.
     */
    public void testCalculateValid()
    {
        if (log.isDebugEnabled())
        {
            log.debug("testCalculateValid() for " + routine.getClass().getName());
        }

        // test valid values
        for (int i = 0; i < valid.length; i++)
        {
            String code = removeCheckDigit(valid[i]);
            String expected = checkDigit(valid[i]);
            try
            {
                if (log.isDebugEnabled())
                {
                    log.debug("   " + i + " Testing Valid Check Digit, Code=[" + code + "] expected=[" + expected + "]");
                }
                assertEquals("valid[" + i +"]: " + valid[i], expected, routine.calculate(code));
            }
            catch (Exception e)
            {
                fail("valid[" + i +"] threw " + e);
            }
        }

    }

    /**
     * Test calculate() for invalid values.
     */
    public void testCalculateInvalid()
    {

        if (log.isDebugEnabled())
        {
            log.debug("testCalculateInvalid() for " + routine.getClass().getName());
        }

        // test invalid code values
        for (int i = 0; i < invalid.length; i++)
        {
            try
            {
                if (log.isDebugEnabled())
                {
                    log.debug("   " + i + " Testing Invalid Check Digit, Code=[" + invalid[i] + "]");
                }
                routine.calculate(invalid[i]);
                fail("Invalid Characters[" + i + "]=" +  invalid[i] + " - expected exception");
            }
            catch (Exception e)
            {
                assertTrue("Invalid Character[" +i +"]=" +  e.getMessage(), e.getMessage().startsWith("Invalid Character["));
            }
        }
    }

    /**
     * Test missing code
     */
    public void testMissingCode()
    {

        // isValid() null
        assertFalse("isValid() Null", routine.isValid(null));

        // isValid() zero length
        assertFalse("isValid() Zero Length", routine.isValid(""));

        // calculate() null
        try
        {
            routine.calculate(null);
            fail("calculate() Null - expected exception");
        }
        catch (Exception e)
        {
            assertEquals("calculate() Null", missingMessage, e.getMessage());
        }

        // calculate() zero length
        try
        {
            routine.calculate("");
            fail("calculate() Zero Length - expected exception");
        }
        catch (Exception e)
        {
            assertEquals("calculate() Zero Length",  missingMessage, e.getMessage());
        }
    }

    /**
     * Test zero sum
     */
    public void testZeroSum()
    {

        assertFalse("isValid() Zero Sum", routine.isValid(zeroSum));

        try
        {
            routine.calculate(zeroSum);
            fail("Zero Sum - expected exception");
        }
        catch (Exception e)
        {
            assertEquals("isValid() Zero Sum",  "Invalid code, sum is zero", e.getMessage());
        }

    }

    /**
     * Test check digit serialization.
     */
    public void testSerialization()
    {
        // Serialize the check digit routine
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(routine);
            oos.flush();
            oos.close();
        }
        catch (Exception e)
        {
            fail(routine.getClass().getName() + " error during serialization: " + e);
        }

        // Deserialize the test object
        Object result = null;
        try
        {
            ByteArrayInputStream bais =
                new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            result = ois.readObject();
            bais.close();
        }
        catch (Exception e)
        {
            fail(routine.getClass().getName() + " error during deserialization: " + e);
        }
        assertNotNull(result);
    }

    /**
     * Returns an array of codes with invalid check digits.
     *
     * @param codes Codes with valid check digits
     * @return Codes with invalid check digits
     */
    protected String[] createInvalidCodes(String[] codes)
    {
        List list = new ArrayList();

        // create invalid check digit values
        for (int i = 0; i < codes.length; i++)
        {
            String code = removeCheckDigit(codes[i]);
            String check  = checkDigit(codes[i]);
            for (int j = 0; j < 10; j++)
            {
                String curr =  "" + Character.forDigit(j, 10);
                if (!curr.equals(check))
                {
                    list.add(code + curr);
                }
            }
        }

        return (String[])list.toArray(new String[list.size()]);
    }

    /**
     * Returns a code with the Check Digit (i.e. last character) removed.
     *
     * @param code The code
     * @return The code without the check digit
     */
    protected String removeCheckDigit(String code)
    {
        if (code == null || code.length() <= checkDigitLth)
        {
            return null;
        }
        return code.substring(0, code.length() - checkDigitLth);
    }

    /**
     * Returns the check digit (i.e. last character) for a code.
     *
     * @param code The code
     * @return The check digit
     */
    protected String checkDigit(String code)
    {
        if (code == null || code.length() <= checkDigitLth)
        {
            return "";
        }
        int start = code.length() - checkDigitLth;
        return code.substring(start);
    }

}
