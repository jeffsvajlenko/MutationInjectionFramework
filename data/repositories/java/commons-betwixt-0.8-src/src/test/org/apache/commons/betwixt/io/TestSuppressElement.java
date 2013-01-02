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

import java.io.StringWriter;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.AttributeDescriptor;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.strategy.ValueSuppressionStrategy;

/**
 * Tests supress element strategy.
 *
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>
 *         of the <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestSuppressElement extends AbstractTestCase
{

    public TestSuppressElement(String testName)
    {
        super(testName);
    }

    public void testSuppressNothing() throws Exception
    {
        PersonBean angLee = new NullPersonBean("Ang","Lee");
        MovieBean movie = new MovieBean("Crouching Tiger, Hidden Dragon", 2000, angLee);
        movie.addActor(new NullPersonBean("Yun-Fat", "Chow"));
        movie.addActor(new PersonBean("Michelle", "Yeoh"));
        movie.addActor(new PersonBean("Ziyi", "Zhang"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(movie);

        String expected = "<?xml version='1.0'?>" +
                          "<movie>" +
                          "    <name>Crouching Tiger, Hidden Dragon</name>" +
                          "    <year>2000</year>" +
                          "    <director>" +
                          "        <forenames>Ang</forenames>" +
                          "        <surname>Lee</surname>" +
                          "     </director>" +
                          "    <actors>" +
                          "         <actor>" +
                          "              <forenames>Yun-Fat</forenames>" +
                          "              <surname>Chow</surname>" +
                          "         </actor>" +
                          "         <actor>" +
                          "              <forenames>Michelle</forenames>" +
                          "              <surname>Yeoh</surname>" +
                          "         </actor>" +
                          "         <actor>" +
                          "           <forenames>Ziyi</forenames>" +
                          "            <surname>Zhang</surname>" +
                          "          </actor>" +
                          "    </actors>" +
                          "</movie>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }


    public void testSuppressType() throws Exception
    {
        PersonBean angLee = new NullPersonBean("Ang","Lee");
        MovieBean movie = new MovieBean("Crouching Tiger, Hidden Dragon", 2000, angLee);
        movie.addActor(new NullPersonBean("Yun-Fat", "Chow"));
        movie.addActor(new PersonBean("Michelle", "Yeoh"));
        movie.addActor(new PersonBean("Ziyi", "Zhang"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getBindingConfiguration().setValueSuppressionStrategy(new ValueSuppressionStrategy()
        {

            public boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value)
            {
                return DEFAULT.suppressAttribute(attributeDescriptor, value);
            }

            public boolean suppressElement(ElementDescriptor element, String namespaceUrl, String localName, String qualifiedName, Object value)
            {
                // suppress NullPersonBean's
                boolean result = false;
                if (value instanceof NullPersonBean)
                {
                    result = true;
                }
                return result;
            }
        });
        writer.write(movie);

        String expected = "<?xml version='1.0'?>" +
                          "<movie>" +
                          "    <name>Crouching Tiger, Hidden Dragon</name>" +
                          "    <year>2000</year>" +
                          "    <actors>" +
                          "         <actor>" +
                          "              <forenames>Michelle</forenames>" +
                          "              <surname>Yeoh</surname>" +
                          "         </actor>" +
                          "         <actor>" +
                          "           <forenames>Ziyi</forenames>" +
                          "            <surname>Zhang</surname>" +
                          "          </actor>" +
                          "    </actors>" +
                          "</movie>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }


    public void testSuppressElementName() throws Exception
    {
        PersonBean angLee = new NullPersonBean("Ang","Lee");
        MovieBean movie = new MovieBean("Crouching Tiger, Hidden Dragon", 2000, angLee);
        movie.addActor(new NullPersonBean("Yun-Fat", "Chow"));
        movie.addActor(new PersonBean("Michelle", "Yeoh"));
        movie.addActor(new PersonBean("Ziyi", "Zhang"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getBindingConfiguration().setValueSuppressionStrategy(new ValueSuppressionStrategy()
        {

            public boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value)
            {
                return DEFAULT.suppressAttribute(attributeDescriptor, value);
            }

            public boolean suppressElement(ElementDescriptor element, String namespaceUrl, String localName, String qualifiedName, Object value)
            {
                // suppress NullPersonBean's
                boolean result = false;
                if ("year".equals(element.getQualifiedName()))
                {
                    result = true;
                }
                return result;
            }
        });
        writer.write(movie);

        String expected = "<?xml version='1.0'?>" +
                          "<movie>" +
                          "    <name>Crouching Tiger, Hidden Dragon</name>" +
                          "    <director>" +
                          "        <forenames>Ang</forenames>" +
                          "        <surname>Lee</surname>" +
                          "     </director>" +
                          "    <actors>" +
                          "         <actor>" +
                          "              <forenames>Yun-Fat</forenames>" +
                          "              <surname>Chow</surname>" +
                          "         </actor>" +
                          "         <actor>" +
                          "              <forenames>Michelle</forenames>" +
                          "              <surname>Yeoh</surname>" +
                          "         </actor>" +
                          "         <actor>" +
                          "           <forenames>Ziyi</forenames>" +
                          "            <surname>Zhang</surname>" +
                          "          </actor>" +
                          "    </actors>" +
                          "</movie>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }

    public void testSuppressName() throws Exception
    {
        PersonBean angLee = new NullPersonBean("Ang","Lee");
        MovieBean movie = new MovieBean("Crouching Tiger, Hidden Dragon", 2000, angLee);
        movie.addActor(new NullPersonBean("Yun-Fat", "Chow"));
        movie.addActor(new PersonBean("Michelle", "Yeoh"));
        movie.addActor(new PersonBean("Ziyi", "Zhang"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getBindingConfiguration().setValueSuppressionStrategy(new ValueSuppressionStrategy()
        {

            public boolean suppressAttribute(AttributeDescriptor attributeDescriptor, String value)
            {
                return DEFAULT.suppressAttribute(attributeDescriptor, value);
            }

            public boolean suppressElement(ElementDescriptor element, String namespaceUrl, String localName, String qualifiedName, Object value)
            {
                // suppress NullPersonBean's
                boolean result = false;
                if ("actor".equals(qualifiedName))
                {
                    result = true;
                }
                return result;
            }
        });
        writer.write(movie);

        String expected = "<?xml version='1.0'?>" +
                          "<movie>" +
                          "    <name>Crouching Tiger, Hidden Dragon</name>" +
                          "    <year>2000</year>" +
                          "    <director>" +
                          "        <forenames>Ang</forenames>" +
                          "        <surname>Lee</surname>" +
                          "     </director>" +
                          "    <actors>" +
                          "    </actors>" +
                          "</movie>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out));
    }
}
