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

package org.apache.commons.betwixt.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestAbstractBeanWriter extends AbstractTestCase
{

    public TestAbstractBeanWriter(String testName)
    {
        super(testName);
    }

    public void testContextCurrentElement() throws Exception
    {
        MovieBean bean =
            new MovieBean("Excalibur", 1981, new PersonBean("John", "Boorman"));
        bean.addActor(new PersonBean("Nigel", "Terry"));
        bean.addActor(new PersonBean("Helen", "Mirren"));
        bean.addActor(new PersonBean("Nicol", "Williamson"));

        TestWritingAPI writer = new TestWritingAPI();
        writer.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo personXmlBeanInfo
            = writer.getXMLIntrospector().introspect(PersonBean.class);
        XMLBeanInfo movieXmlBeanInfo
            = writer.getXMLIntrospector().introspect(MovieBean.class);
        writer.write(bean);

        List expected = new ArrayList();
        ElementDescriptor movieElementdescriptor
            = movieXmlBeanInfo.getElementDescriptor();
        ElementDescriptor nameDescriptor
            = movieElementdescriptor.getElementDescriptors()[0];
        ElementDescriptor yearDescriptor
            = movieElementdescriptor.getElementDescriptors()[1];
        ElementDescriptor directorDescriptor
            = movieElementdescriptor.getElementDescriptors()[2];
        ElementDescriptor actorsDescriptor
            = movieElementdescriptor.getElementDescriptors()[3];
        ElementDescriptor personDescriptor
            = personXmlBeanInfo.getElementDescriptor();

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                movieElementdescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                nameDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.BODY_TEXT,
                nameDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                nameDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                yearDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.BODY_TEXT,
                yearDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                yearDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                personDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                personDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                actorsDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                personDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                personDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                personDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                personDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.START_ELEMENT,
                personDescriptor));


        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                personDescriptor));
        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                actorsDescriptor));

        expected.add(
            new TestWritingAPI.Record(
                TestWritingAPI.END_ELEMENT,
                movieElementdescriptor));

        assertEquals("Collections same size", expected.size(), writer.recording.size());

        assertEquals("Movie element start", expected.get(0), writer.recording.get(0));
        assertEquals("Name element start", expected.get(1), writer.recording.get(1));
        assertEquals("Name element body", expected.get(2), writer.recording.get(2));
        assertEquals("Name element end", expected.get(3), writer.recording.get(3));
        assertEquals("Year element start", expected.get(4), writer.recording.get(4));
        assertEquals("Year element body", expected.get(5), writer.recording.get(5));
        assertEquals("Year element end", expected.get(6), writer.recording.get(6));
        assertEquals("Director element start", expected.get(7), writer.recording.get(7));
        assertEquals("Director element end", expected.get(8), writer.recording.get(8));
        assertEquals("Actors element start", expected.get(9), writer.recording.get(9));;
        assertEquals("Actor element body", expected.get(10), writer.recording.get(10));
        assertEquals("Actor element end", expected.get(11), writer.recording.get(12));
        assertEquals("Actor element body", expected.get(12), writer.recording.get(12));
        assertEquals("Actor element end", expected.get(13), writer.recording.get(13));
        assertEquals("Actor element body", expected.get(14), writer.recording.get(14));
        assertEquals("Actor element end", expected.get(15), writer.recording.get(15));
        assertEquals("Actors element end", expected.get(16), writer.recording.get(16));
        assertEquals("Movie element end", expected.get(17), writer.recording.get(17));
    }


    public static class TestWritingAPI extends AbstractBeanWriter
    {

        public static final int START_ELEMENT = 1;
        public static final int BODY_TEXT = 2;
        public static final int END_ELEMENT = 3;

        private List recording = new ArrayList();

        protected void bodyText(String text) throws IOException, SAXException
        {
            throw new RuntimeException("Deprecated method called");
        }


        protected void bodyText(WriteContext context, String text)
        throws IOException, SAXException
        {
            recording.add(new Record(BODY_TEXT, context.getCurrentDescriptor()));
        }

        protected void endElement(String uri, String localName, String qName)
        throws IOException, SAXException
        {
            throw new RuntimeException("Deprecated method called");
        }

        protected void endElement(
            WriteContext context,
            String uri,
            String localName,
            String qName)
        throws IOException, SAXException
        {
            ;
            recording.add(new Record(END_ELEMENT, context.getCurrentDescriptor()));
        }

        protected void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attr)
        throws IOException, SAXException
        {
            throw new RuntimeException("Deprecated method called");
        }

        protected void startElement(
            WriteContext context,
            String uri,
            String localName,
            String qName,
            Attributes attr)
        throws IOException, SAXException
        {
            recording.add(new Record(START_ELEMENT, context.getCurrentDescriptor()));
        }

        public static class Record
        {
            ElementDescriptor currentDescriptor;
            int type;

            Record(int type, ElementDescriptor currentDescriptor)
            {
                this.currentDescriptor = currentDescriptor;
                this.type = type;
            }

            public int hashCode()
            {
                return type;
            }

            public String toString()
            {
                return "[Record: type=" + type + "; " + currentDescriptor + "]";
            }

            public boolean equals(Object arg)
            {
                boolean result = false;
                if (arg instanceof Record)
                {
                    Record record = (Record) arg;
                    result = (type == type)
                             && currentDescriptor.equals(record.currentDescriptor);
                }
                return result;
            }

        }
    }
}
