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

package org.apache.commons.betwixt.strategy;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests ObjectStringConverters in FRENCH locale
 *
 * @author Robert Burrell Donkin
 * @version $Id: Testi18nObjectStringConversion.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class Testi18nObjectStringConversion extends TestObjectStringConverters
{
    static
    {
        Locale.setDefault(Locale.FRENCH);
    }

    public static Test suite()
    {
        return new TestSuite(Testi18nObjectStringConversion.class);
    }

    public Testi18nObjectStringConversion(String testName)
    {
        super(testName);
    }

    public void testFrenchDefaultLocale() throws Exception
    {
        //check locale has been changed
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:sss yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(1980, 11, 9, 5, 0, 0);
        java.util.Date date = calendar.getTime();
        String formatted = format.format(date);
        assertEquals("Locale dependent conversions", "mar. d\u00E9c. 09 05:00:000 1980", formatted);

    }
}
