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
package org.apache.commons.betwixt.derived;

import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanReader;


/** Test harness for the BeanReader
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class TestDerived extends AbstractTestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestDerived.class);
    }

    public TestDerived(String testName)
    {
        super(testName);
    }

    public void testPersonList() throws Exception
    {

        BeanReader reader = new BeanReader();
        reader.registerBeanClass( PersonListBean.class );

        InputStream in =
            new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/derived/person-list.xml") );
        try
        {

            checkBean((PersonListBean) reader.parse( in ));

        }
        finally
        {
            in.close();
        }
    }

    protected void checkBean(PersonListBean bean) throws Exception
    {
        PersonBean owner = bean.getOwner();
        assertTrue("should have found an owner", owner != null );

        assertEquals("should be derived class", "org.apache.commons.betwixt.derived.EmployeeBean", owner.getClass().getName());


        assertEquals("PersonList size", 4, bean.getPersonList().size());
        assertEquals("PersonList value (1)", "Athos", ((PersonBean) bean.getPersonList().get(0)).getName());
        assertEquals("PersonList value (2)", "Porthos", ((PersonBean) bean.getPersonList().get(1)).getName());
        assertEquals("PersonList value (3)", "Aramis", ((PersonBean) bean.getPersonList().get(2)).getName());
        assertEquals("PersonList value (4)", "D'Artagnan", ((PersonBean) bean.getPersonList().get(3)).getName());

        PersonBean employee = (PersonBean) bean.getPersonList().get(1);
        assertEquals("should be derived class", "org.apache.commons.betwixt.derived.EmployeeBean", employee.getClass().getName());

        PersonBean manager = (PersonBean) bean.getPersonList().get(2);
        assertEquals("should be derived class", "org.apache.commons.betwixt.derived.ManagerBean", manager.getClass().getName());

        // test derived properties
        //assertEquals("should have a derived property", 12, ((ManagerBean) manager).getCheeseSize());
    }

}

