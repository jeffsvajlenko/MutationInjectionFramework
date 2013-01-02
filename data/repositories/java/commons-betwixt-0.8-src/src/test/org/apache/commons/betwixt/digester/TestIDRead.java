
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
package org.apache.commons.betwixt.digester;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.impl.SimpleLog;
//import org.apache.commons.betwixt.expression.MethodUpdater;
//import org.apache.commons.betwixt.io.BeanCreateRule;
//import org.apache.commons.betwixt.io.BeanRuleSet;


/** Test harness for ID-IDRef reading.
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class TestIDRead extends AbstractTestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestIDRead.class);
    }

    public TestIDRead(String testName)
    {
        super(testName);
    }

    public void testSimpleRead() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        IDBean bean = new IDBean("alpha","one");
        bean.addChild(new IDBean("beta","two"));
        bean.addChild(new IDBean("gamma","three"));
        writer.write(bean);

        String xml = "<IDBean><name>one</name><children><child><name>two</name><children/>"
                     + "<id>beta</id></child><child><name>three</name><children/>"
                     + "<id>gamma</id></child></children><id>alpha</id></IDBean>";

        xmlAssertIsomorphicContent(
            parseString(xml),
            parseString(out.getBuffer().toString()),
            true);

        BeanReader reader = new BeanReader();

//         logging just for this method
//        SimpleLog log = new SimpleLog("[testSimpleRead:XMLIntrospectorHelper]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        XMLIntrospectorHelper.setLog(log);

//        log = new SimpleLog("[testSimpleRead:MethodUpdater]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        MethodUpdater.setLog(log);

//        log = new SimpleLog("[testSimpleRead:BeanCreateRule]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        BeanCreateRule.setLog(log);

//        log = new SimpleLog("[testSimpleRead:BeanRuleSet]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        BeanRuleSet.setLog(log);

//        log = new SimpleLog("[testSimpleRead:IDBean]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        IDBean.log = log;

//        log = new SimpleLog("[testSimpleRead:BeanReader]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        reader.setLog(log);

//        log = new SimpleLog("[testSimpleRead:XMLIntrospector]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        reader.getXMLIntrospector().setLog(log);

        reader.registerBeanClass( IDBean.class );

        InputStream in = new FileInputStream(
            getTestFile("src/test/org/apache/commons/betwixt/digester/SimpleReadTest.xml") );

        try
        {
//            log = new SimpleLog("[testSimpleRead]");
//            log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
            Object obj = reader.parse( in );
//            log.debug(obj);

            assertEquals("Read bean type is incorrect", true, (obj instanceof IDBean) );
            IDBean alpha = (IDBean) obj;

            assertEquals("Wrong list size", 2 ,  alpha.getChildren().size());

            IDBean beta = (IDBean) alpha.getChildren().get(0);
            assertEquals("Wrong name (A)", "beta" ,  beta.getName());

            IDBean gamma = (IDBean) alpha.getChildren().get(1);
            assertEquals("Wrong name (B)", "gamma" ,  gamma.getName());
        }
        finally
        {
            in.close();
        }
    }

    public void testIDRead() throws Exception
    {

        BeanReader reader = new BeanReader();

//         logging just for this method
//        SimpleLog log = new SimpleLog("[testIDRead:XMLIntrospectorHelper]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        XMLIntrospectorHelper.setLog(log);
//
//        log = new SimpleLog("[testIDRead:BeanCreateRule]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        BeanCreateRule.setLog(log);
//
//        log = new SimpleLog("[testIDRead:BeanReader]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        reader.setLog(log);
//
//        log = new SimpleLog("[testIDRead:XMLIntrospector]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        reader.getXMLIntrospector().setLog(log);

        reader.registerBeanClass( IDBean.class );

        InputStream in = new FileInputStream(
            getTestFile("src/test/org/apache/commons/betwixt/digester/IDTest1.xml") );

        try
        {
            Object obj = reader.parse( in );

            assertEquals("Read bean type is incorrect", true, (obj instanceof IDBean) );
            IDBean alpha = (IDBean) obj;

            assertEquals("Wrong list size (A)", 2 ,  alpha.getChildren().size());

            IDBean beta = (IDBean) alpha.getChildren().get(0);
            assertEquals("Wrong name (A)", "beta" ,  beta.getName());

            IDBean gamma = (IDBean) alpha.getChildren().get(1);
            assertEquals("Wrong name (B)", "gamma" ,  gamma.getName());
            assertEquals("Wrong list size (B)", 2 ,  gamma.getChildren().size());

            IDBean sonOfGamma = (IDBean) gamma.getChildren().get(1);

            assertEquals("Wrong id (A)", "two" ,  sonOfGamma.getId());
            assertEquals("Wrong name (C)", "beta" ,  sonOfGamma.getName());

            assertEquals("IDREF bean not equal to ID bean", beta,  sonOfGamma);
        }
        finally
        {
            in.close();
        }
    }
}
