/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.mail;

import java.lang.reflect.Method;

import javax.mail.internet.InternetAddress;

/**
 * JUnit test case demonstrating InternetAddress validation.
 *
 * @since 1.0
 * @author Niall Pemberton
 * @version $Id: InvalidInternetAddressTest.java 512208 2007-02-27 10:22:28Z dion $
 */

public class InvalidInternetAddressTest extends BaseEmailTestCase
{

    /** */
    private static final String VALID_QUOTED_EMAIL = "\"John O'Groats\"@domain.com";

    /** JavaMail 1.2. does not know about this */
    private static Method validateMethod;

    /** */
    private static final String[] ARR_INVALID_EMAILS =
    {
        "local name@domain.com",
        "local(name@domain.com",
        "local)name@domain.com",
        "local<name@domain.com",
        "local>name@domain.com",
        "local,name@domain.com",
        "local;name@domain.com",
        "local:name@domain.com",
        "local[name@domain.com",
        "local]name@domain.com",
        "local\\name@domain.com",
        "local\"name@domain.com",
        "local\tname@domain.com",
        "local\nname@domain.com",
        "local\rname@domain.com",
        "local.name@domain com",
        "local.name@domain(com",
        "local.name@domain)com",
        "local.name@domain<com",
        "local.name@domain>com",
        "local.name@domain,com",
        "local.name@domain;com",
        "local.name@domain:com",
        "local.name@domain[com",
        "local.name@domain]com",
        "local.name@domain\\com",
        "local.name@domain\tcom",
        "local.name@domain\ncom",
        "local.name@domain\rcom",
        "local.name@",
        "@domain.com"
    };
    /**
     * @param name name
     */
    public InvalidInternetAddressTest(String name)
    {
        super(name);
    }

    /**
     * Setup for a test
     * @throws Exception on any error
     */
    protected void setUp() throws Exception
    {
        super.setUp();

        try
        {
            validateMethod = InternetAddress.class.getMethod("validate", new Class [0]);
        }
        catch (Exception e)
        {
            assertEquals("Got wrong Exception when looking for validate()", NoSuchMethodException.class, e.getClass());
        }
    }

    /**
     *
     * @throws Exception Exception
     */
    public void testStrictConstructor() throws Exception
    {
        // ====================================================================
        // Prove InternetAddress constructor is throwing exception.
        // ====================================================================


        // test Invalid Email addresses
        for (int i = 0; i < ARR_INVALID_EMAILS.length; i++)
        {

            try
            {
                // Create Internet Address using "strict" constructor
                new InternetAddress(ARR_INVALID_EMAILS[i]);

                // Expected an exception to be thrown
                fail("Strict " + i + " passed: " + ARR_INVALID_EMAILS[i]);
            }
            catch (Exception ex)
            {
                // Expected Result
            }

        }

        // test valid 'quoted' Email addresses
        try
        {

            // Create Internet Address using "strict" constructor
            new InternetAddress(VALID_QUOTED_EMAIL);

        }
        catch (Exception ex)
        {
            fail("Valid Quoted Email failed: " + VALID_QUOTED_EMAIL
                 + " - " + ex.getMessage());
        }
    }

    /**
     *
     * @throws Exception Exception
     */
    public void testValidateMethod() throws Exception
    {
        if (validateMethod == null)
        {
            return;
        }

        // ====================================================================
        // Prove InternetAddress constructor isn't throwing exception and
        // the validate() method is
        // ====================================================================

        for (int i = 0; i < ARR_INVALID_EMAILS.length; i++)
        {

            InternetAddress address = new InternetAddress(ARR_INVALID_EMAILS[i], "Joe");

            // N.B. validate() doesn't check addresses containing quotes or '['
            boolean quoted = ARR_INVALID_EMAILS[i].indexOf("\"") >= 0;
            int atIndex    = ARR_INVALID_EMAILS[i].indexOf("@");
            boolean domainBracket  = (atIndex >= 0)
                                     && (ARR_INVALID_EMAILS[i].indexOf("[", atIndex)  >= 0);
            try
            {
                validateMethod.invoke(address, null);

                if (!(quoted || domainBracket))
                {
                    fail("Validate " + i + " passed: " + ARR_INVALID_EMAILS[i]);
                }
            }
            catch (Exception ex)
            {
                if (quoted || domainBracket)
                {
                    fail("Validate " + i + " failed: " + ARR_INVALID_EMAILS[i]
                         + " - " + ex.getMessage());
                }
            }
        }

        // test valid 'quoted' Email addresses
        try
        {
            validateMethod.invoke(new InternetAddress(VALID_QUOTED_EMAIL, "Joe"), null);
        }
        catch (Exception ex)
        {
            fail("Valid Quoted Email failed: " + VALID_QUOTED_EMAIL
                 + " - " + ex.getMessage());
        }
    }

    /**
     *
     * @throws Exception Exception
     */
    public void testValidateMethodCharset() throws Exception
    {
        if (validateMethod == null)
        {
            return;
        }

        // ====================================================================
        // Prove InternetAddress constructor isn't throwing exception and
        // the validate() method is
        // ====================================================================

        for (int i = 0; i < ARR_INVALID_EMAILS.length; i++)
        {

            InternetAddress address = new InternetAddress(ARR_INVALID_EMAILS[i], "Joe", "UTF-8");

            // N.B. validate() doesn't check addresses containing quotes or '['
            boolean quoted = ARR_INVALID_EMAILS[i].indexOf("\"") >= 0;
            int atIndex    = ARR_INVALID_EMAILS[i].indexOf("@");
            boolean domainBracket  = (atIndex >= 0)
                                     && (ARR_INVALID_EMAILS[i].indexOf("[", atIndex)  >= 0);

            try
            {
                validateMethod.invoke(address, null);
                if (!(quoted || domainBracket))
                {
                    fail("Validate " + i + " passed: " + ARR_INVALID_EMAILS[i]);
                }

            }
            catch (Exception ex)
            {

                if (quoted || domainBracket)
                {
                    fail("Validate " + i + " failed: " + ARR_INVALID_EMAILS[i]
                         + " - " + ex.getMessage());
                }

            }

        }

        // test valid 'quoted' Email addresses
        try
        {
            validateMethod.invoke(new InternetAddress(VALID_QUOTED_EMAIL, "Joe", "UTF-8"), null);
        }
        catch (Exception ex)
        {
            fail("Valid Quoted Email failed: " + VALID_QUOTED_EMAIL
                 + " - " + ex.getMessage());
        }
    }

}
