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
package org.apache.commons.validator.routines;

import junit.framework.TestCase;

/**
 * Tests for the DomainValidator.
 *
 * @version $Revision: 1128456 $ $Date: 2011-05-27 23:04:29 +0200 (Fri, 27 May 2011) $
 */
public class DomainValidatorTest extends TestCase
{

    private DomainValidator validator;

    public void setUp()
    {
        validator = DomainValidator.getInstance();
    }

    public void testValidDomains()
    {
        assertTrue("apache.org should validate", validator.isValid("apache.org"));
        assertTrue("www.google.com should validate", validator.isValid("www.google.com"));

        assertTrue("test-domain.com should validate", validator.isValid("test-domain.com"));
        assertTrue("test---domain.com should validate", validator.isValid("test---domain.com"));
        assertTrue("test-d-o-m-ain.com should validate", validator.isValid("test-d-o-m-ain.com"));
        assertTrue("two-letter domain label should validate", validator.isValid("as.uk"));

        assertTrue("case-insensitive ApAchE.Org should validate", validator.isValid("ApAchE.Org"));

        assertTrue("single-character domain label should validate", validator.isValid("z.com"));

        assertTrue("i.have.an-example.domain.name should validate", validator.isValid("i.have.an-example.domain.name"));
    }

    public void testInvalidDomains()
    {
        assertFalse("bare TLD .org shouldn't validate", validator.isValid(".org"));
        assertFalse("domain name with spaces shouldn't validate", validator.isValid(" apache.org "));
        assertFalse("domain name containing spaces shouldn't validate", validator.isValid("apa che.org"));
        assertFalse("domain name starting with dash shouldn't validate", validator.isValid("-testdomain.name"));
        assertFalse("domain name ending with dash shouldn't validate", validator.isValid("testdomain-.name"));
        assertFalse("domain name starting with multiple dashes shouldn't validate", validator.isValid("---c.com"));
        assertFalse("domain name ending with multiple dashes shouldn't validate", validator.isValid("c--.com"));
        assertFalse("domain name with invalid TLD shouldn't validate", validator.isValid("apache.rog"));

        assertFalse("URL shouldn't validate", validator.isValid("http://www.apache.org"));
        assertFalse("Empty string shouldn't validate as domain name", validator.isValid(" "));
        assertFalse("Null shouldn't validate as domain name", validator.isValid(null));
    }

    public void testTopLevelDomains()
    {
        // infrastructure TLDs
        assertTrue(".arpa should validate as iTLD", validator.isValidInfrastructureTld(".arpa"));
        assertFalse(".com shouldn't validate as iTLD", validator.isValidInfrastructureTld(".com"));

        // generic TLDs
        assertTrue(".name should validate as gTLD", validator.isValidGenericTld(".name"));
        assertFalse(".us shouldn't validate as gTLD", validator.isValidGenericTld(".us"));

        // country code TLDs
        assertTrue(".uk should validate as ccTLD", validator.isValidCountryCodeTld(".uk"));
        assertFalse(".org shouldn't validate as ccTLD", validator.isValidCountryCodeTld(".org"));

        // case-insensitive
        assertTrue(".COM should validate as TLD", validator.isValidTld(".COM"));
        assertTrue(".BiZ should validate as TLD", validator.isValidTld(".BiZ"));

        // corner cases
        assertFalse("invalid TLD shouldn't validate", validator.isValid(".nope"));
        assertFalse("empty string shouldn't validate as TLD", validator.isValid(""));
        assertFalse("null shouldn't validate as TLD", validator.isValid(null));
    }

    public void testAllowLocal()
    {
        DomainValidator noLocal = DomainValidator.getInstance(false);
        DomainValidator allowLocal = DomainValidator.getInstance(true);

        // Default is false, and should use singletons
        assertEquals(noLocal, validator);

        // Default won't allow local
        assertFalse("localhost.localdomain should validate", noLocal.isValid("localhost.localdomain"));
        assertFalse("localhost should validate", noLocal.isValid("localhost"));

        // But it may be requested
        assertTrue("localhost.localdomain should validate", allowLocal.isValid("localhost.localdomain"));
        assertTrue("localhost should validate", allowLocal.isValid("localhost"));
        assertTrue("hostname should validate", allowLocal.isValid("hostname"));
        assertTrue("machinename should validate", allowLocal.isValid("machinename"));

        // Check the localhost one with a few others
        assertTrue("apache.org should validate", allowLocal.isValid("apache.org"));
        assertFalse("domain name with spaces shouldn't validate", allowLocal.isValid(" apache.org "));
    }

    public void testIDN()
    {
        assertTrue("b\u00fccher.ch in IDN should validate", validator.isValid("www.xn--bcher-kva.ch"));
    }
}
