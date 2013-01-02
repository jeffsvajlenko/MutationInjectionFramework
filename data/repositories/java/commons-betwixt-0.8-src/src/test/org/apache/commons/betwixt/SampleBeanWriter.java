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

import org.apache.commons.betwixt.io.BeanWriter;


/** A sample program to output a bean as pretty printed XML
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class SampleBeanWriter extends AbstractTestCase
{

    public SampleBeanWriter(String testName)
    {
        super(testName);
    }

    public static void main(String[] args) throws Exception
    {
        SampleBeanWriter sample = new SampleBeanWriter("foo");
        sample.run( args );
    }

    public void run(String[] args) throws Exception
    {
        Object bean = null;
        if ( args.length > 0 )
        {
            bean = Class.forName( args[0] ).newInstance();
        }
        else
        {
            bean = createBean();
        }
        write( bean );
    }

    public void write(Object bean) throws Exception
    {
        BeanWriter writer = new BeanWriter();
        writer.enablePrettyPrint();
        writer.write( bean );
    }
}

