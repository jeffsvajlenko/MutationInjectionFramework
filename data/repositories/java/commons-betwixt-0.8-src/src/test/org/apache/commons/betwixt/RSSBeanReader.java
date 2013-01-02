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

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.digester.rss.Channel;
import org.apache.commons.digester.rss.RSSDigester;

/** Reads an RSS file using Betwixt's auto-digester rules then
  * outputs it again.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class RSSBeanReader extends AbstractTestCase
{

    /**
     * The set of public identifiers, and corresponding resource names,
     * for the versions of the DTDs that we know about.
     */
    protected static final String registrations[] =
    {
        "-//Netscape Communications//DTD RSS 0.9//EN",
        "/org/apache/commons/digester/rss/rss-0.9.dtd",
        "-//Netscape Communications//DTD RSS 0.91//EN",
        "/org/apache/commons/digester/rss/rss-0.91.dtd",
    };

    public RSSBeanReader(String testName)
    {
        super(testName);
    }

    public static void main(String[] args) throws Exception
    {
        RSSBeanReader sample = new RSSBeanReader("RSS");
        sample.run( args );
    }

    public void run(String[] args) throws Exception
    {
        BeanReader reader = new BeanReader();

        reader.registerBeanClass( Channel.class );

        // Register local copies of the DTDs we understand
        for (int i = 0; i < registrations.length; i += 2)
        {
            URL url = RSSDigester.class.getResource(registrations[i + 1]);
            if (url != null)
            {
                reader.register(registrations[i], url.toString());
            }
        }

        Object bean = null;
        if ( args.length > 0 )
        {
            bean = reader.parse( args[0] );
        }
        else
        {
            InputStream in = new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/rss-example.xml") );
            bean = reader.parse( in );
            in.close();
        }

        write( bean );
    }

    public void write(Object bean) throws Exception
    {
        if ( bean == null )
        {
            throw new Exception( "No bean read from the XML document!" );
        }
        BeanWriter writer = new BeanWriter();
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.enablePrettyPrint();
        writer.write( bean );
    }
}

