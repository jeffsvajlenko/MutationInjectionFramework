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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.betwixt.expression.Context;

/**
 * Test harness for ObjectStringConverter implementations
 *
 * @author <a href="mailto:rdonkin at apache.org">Robert Burrell Donkin</a>
 * @version $Id: TestObjectStringConverters.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestObjectStringConverters extends TestCase
{

    protected Context dummyContext = new Context();

    public static Test suite()
    {
        return new TestSuite(TestObjectStringConverters.class);
    }

    public TestObjectStringConverters(String testName)
    {
        super(testName);
    }

    public void testBaseConverter()
    {
        Object test = new Object ()
        {
            public String toString()
            {
                return "funciporcini";
            }
        };

        ObjectStringConverter converter = new ObjectStringConverter();
        String stringFromObject = converter.objectToString( null, Object.class, "raspberry", dummyContext );
        assertEquals("Null should return empty string", "", stringFromObject);
        stringFromObject = converter.objectToString( test, Object.class, "raspberry", dummyContext );
        assertEquals("Object should return toString", "funciporcini", stringFromObject);

        Object objectFromString = converter.stringToObject( "Mungo Jerry", Object.class, "strawberry", dummyContext );
        assertEquals("String should return itself", "Mungo Jerry", objectFromString);
    }


    public void testConvertUtilsConverter() throws Exception
    {
        ObjectStringConverter converter = new ConvertUtilsObjectStringConverter();
        commonTestForConvertUtilsConverters( converter );
    }

    private void commonTestForConvertUtilsConverters(ObjectStringConverter objectStringConverter)
    {
        Converter converter = new Converter()
        {
            public Object convert(Class type, Object value)
            {
                if ( type == SecurityManager.class)
                {
                    return "Life, The Universe And Everything";
                }
                return "The answer is " + value.toString();
            }
        };

        Long test = new Long(42);

        ConvertUtils.register( converter, Object.class );
        ConvertUtils.register( converter, String.class );
        ConvertUtils.register( converter, SecurityManager.class );

        String stringFromObject = objectStringConverter.objectToString( null, Object.class, "gooseberry", dummyContext );
        assertEquals("Null should return empty string", "", stringFromObject);
        stringFromObject = objectStringConverter.objectToString( test, Object.class, "logonberry", dummyContext );
        assertEquals("Normal object conversion (1)", "The answer is 42", stringFromObject);


        Object objectFromString = objectStringConverter.stringToObject(
                                      "Forty Two", Object.class, "damsen", dummyContext );
        assertEquals("Normal object conversion (2)", "The answer is Forty Two", objectFromString);
        objectFromString = objectStringConverter.stringToObject(
                               "Trillian", SecurityManager.class, "cranberry", dummyContext );
        assertEquals("Special object conversion", "Life, The Universe And Everything", objectFromString);

        ConvertUtils.deregister();
    }

    public void testDefaultOSConverter()
    {
        ObjectStringConverter converter = new DefaultObjectStringConverter();
        commonTestForConvertUtilsConverters( converter );
    }

    public void testDefaultOSConverterDates()
    {


        Converter converter = new Converter()
        {
            public Object convert(Class type, Object value)
            {
                return "Arthur Dent";
            }
        };

        ConvertUtils.register( converter, java.sql.Date.class );

        converter = new Converter()
        {
            public Object convert(Class type, Object value)
            {
                return "Ford Prefect";
            }
        };

        ConvertUtils.register( converter, String.class );

        converter = new Converter()
        {
            public Object convert(Class type, Object value)
            {
                return "Marvin";
            }
        };

        ConvertUtils.register( converter, java.util.Date.class );

        java.util.Date utilNow = new java.util.Date();
        String nowAsString = utilNow.toString();
        java.sql.Date sqlNow = new java.sql.Date(System.currentTimeMillis());
        ObjectStringConverter objectStringConverter = new DefaultObjectStringConverter();

        String stringFromObject = objectStringConverter.objectToString(
                                      utilNow, java.util.Date.class, "blackcurrent", dummyContext );
        assertEquals( "String output same as java.util.Date.toString() (1)", utilNow.toString(), stringFromObject );

        stringFromObject = objectStringConverter.objectToString(
                               sqlNow, java.util.Date.class, "redcurrent", dummyContext );
        assertEquals( "String output same as java.util.Date.toString() (2)", utilNow.toString(), stringFromObject );

        stringFromObject = objectStringConverter.objectToString(
                               utilNow, java.sql.Date.class, "whitecurrent", dummyContext );
        assertEquals( "Should use converter (2)", "Ford Prefect", stringFromObject );

        Object objectFromString = objectStringConverter.stringToObject(
                                      nowAsString, java.sql.Date.class, "blackberry", dummyContext );
        assertEquals( "Should use converter (3)", "Ford Prefect", stringFromObject );
        objectFromString = objectStringConverter.stringToObject(
                               nowAsString, java.util.Date.class, "tayberry", dummyContext );
        assertTrue( "Date should be returned", objectFromString instanceof java.util.Date);
        assertEquals( "Date returned should be the same", nowAsString,  objectFromString.toString());

        ConvertUtils.deregister();
    }
}


