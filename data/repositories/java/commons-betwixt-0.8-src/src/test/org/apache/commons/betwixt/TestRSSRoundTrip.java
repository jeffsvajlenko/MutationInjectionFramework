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
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.digester.rss.Channel;
import org.apache.commons.digester.rss.RSSDigester;

/** Test harness which parses an RSS document using Digester
  * then outputs it using Betwixt, then parses it again with Digester
  * to check that the document is parseable again.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class TestRSSRoundTrip extends AbstractTestCase
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

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestRSSRoundTrip.class);
    }

    public TestRSSRoundTrip(String testName)
    {
        super(testName);
    }



    public void testRoundTrip() throws Exception
    {
        // lets parse the example
        RSSDigester digester = new RSSDigester();

        InputStream in = new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/rss-example.xml") );
        Object bean = digester.parse( in );
        in.close();

        // now lets output it to a buffer
        StringWriter buffer = new StringWriter();
        write( bean, buffer );

        // now lets try parse again
        String text = buffer.toString();
        bean = digester.parse( new StringReader( text ) );

        // managed to parse it again!

        // now lets write it to another buffer
        buffer = new StringWriter();
        write( bean, buffer );

        String text2 = buffer.toString();

        // if the two strings are equal then we've done a full round trip
        // with the XML staying the same. Though the original source XML
        // could well be different
        assertEquals( "Round trip value should remain unchanged", text, text2 );
    }

    /**
     * This tests using the both the RSSDigester
     * and the BeanReader to parse an RSS and output it
     * using the BeanWriter
     */
    public void testBeanWriterRoundTrip() throws Exception
    {
        // lets parse the example using the RSSDigester
        RSSDigester digester = new RSSDigester();

        InputStream in = new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/rss-example.xml") );
        Object bean = digester.parse( in );
        in.close();

        // now lets output it to a buffer
        StringWriter buffer = new StringWriter();
        write( bean, buffer );


        // create a BeanReader
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

        // now lets try parse the output sing the BeanReader
        String text = buffer.toString();
        bean = reader.parse( new StringReader( text ) );

        // managed to parse it again!

        // now lets write it to another buffer
        buffer = new StringWriter();
        write( bean, buffer );

        String text2 = buffer.toString();

        // if the two strings are equal then we've done a full round trip
        // with the XML staying the same. Though the original source XML
        // could well be different
        assertEquals( "Round trip value should remain unchanged", text, text2 );
    }

    public void testRSSRead() throws Exception
    {
        /*
            this test isn't working at the moment.
            the problem seems to be that you can't configure betwixt to ignore empty elements

            // create a BeanReader
            BeanReader reader = new BeanReader();
            reader.registerBeanClass( Channel.class );

            // Register local copies of the DTDs we understand
            for (int i = 0; i < registrations.length; i += 2) {
                URL url = RSSDigester.class.getResource(registrations[i + 1]);
                if (url != null) {
                    reader.register(registrations[i], url.toString());
                }
            }

            Object bean = reader.parse(
                new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/rss-example.xml") ));

            StringWriter out = new StringWriter();
            out.write( "<?xml version='1.0'?>" );
            write( bean, out );

            String xml = out.toString();
            System.out.println( xml );

            xmlAssertIsomorphic(
                parseString( xml ),
                parseFile( "src/test/org/apache/commons/betwixt/rss-example.xml" ));
            */
    }

    protected void write(Object bean, Writer out) throws Exception
    {
        BeanWriter writer = new BeanWriter(out);
        writer.setWriteEmptyElements(true);
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.setEndOfLine("\n");
        writer.enablePrettyPrint();
        writer.write( bean );
    }
}

