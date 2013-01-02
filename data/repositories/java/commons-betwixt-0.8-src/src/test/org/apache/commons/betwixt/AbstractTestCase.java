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
package org.apache.commons.betwixt;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.betwixt.xmlunit.XmlTestCase;

/** Abstract base class for test cases.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public abstract class AbstractTestCase extends XmlTestCase
{

    /**
     * Basedir for all i/o
     */
    public String basedir = System.getProperty("basedir");

    public AbstractTestCase(String testName)
    {
        super(testName);
    }

    public String getTestFile(String path)
    {
        return new File(basedir,path).getAbsolutePath();
    }

    public String getTestFileURL(String path) throws MalformedURLException
    {
        return new File(basedir,path).toURL().toString();
    }

    protected Object createBean()
    {
        CustomerBean bean = new CustomerBean();
        bean.setID( "1" );
        bean.setName( "James" );
        bean.setEmails( new String[] { "jstrachan@apache.org", "james_strachan@yahoo.co.uk" } );
        bean.setNumbers( new int[] { 3, 4, 5 } );
        bean.setLocation(0, "Highbury Barn" );
        bean.setLocation(1, "Monument" );
        bean.setLocation(2, "Leeds" );

        Map projects = new HashMap();
        projects.put( "dom4j", "http://dom4j.org" );
        projects.put( "jaxen", "http://jaxen.org" );
        projects.put( "jakarta-commons", "http://jakarta.apache.org/commons/" );
        projects.put( "jakarta-taglibs", "http://jakarta.apache.org/taglibs/" );
        bean.setProjectMap( projects );

        AddressBean address = new AddressBean();
        address.setStreet( "Near the park" );
        address.setCity( "London" );
        address.setCountry( "UK" );
        address.setCode( "N5" );

        bean.setAddress( address );

        bean.setDate((Date) ConvertUtils.convert("2002-03-17", Date.class));
        bean.setTime((Time) ConvertUtils.convert("20:30:40", Time.class));
        bean.setTimestamp((Timestamp) ConvertUtils.convert("2002-03-17 20:30:40.0", Timestamp.class));

        bean.setBigDecimal(new BigDecimal("1234567890.12345"));
        bean.setBigInteger(new BigInteger("1234567890"));

        return bean;
    }
}

