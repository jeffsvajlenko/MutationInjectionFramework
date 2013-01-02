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
package org.apache.commons.betwixt.io.read;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.xml.sax.InputSource;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a> for <a href='http://www.apache.org'>The Apache Software Foundation</a>
 */
public class TestPolymorphic extends AbstractTestCase
{

    public TestPolymorphic(String arg0)
    {
        super(arg0);
    }

    private static final String MAPPING = "<?xml version='1.0'?>" +
                                          "<betwixt-config>" +
                                          "  <class name='org.apache.commons.betwixt.io.read.Animals'>" +
                                          "    <element name='animals'>" +
                                          "      <element property='animals' updater='addAnimal'/>" +
                                          "    </element>" +
                                          "  </class>" +
                                          "  <class name='org.apache.commons.betwixt.io.read.FerretBean'>" +
                                          "    <element name='ferret'>" +
                                          "      <addDefaults/>" +
                                          "    </element>" +
                                          "  </class>" +
                                          "  <class name='org.apache.commons.betwixt.io.read.CatBean'>" +
                                          "    <element name='cat'>" +
                                          "      <addDefaults/>" +
                                          "    </element>" +
                                          "  </class>" +
                                          "  <class name='org.apache.commons.betwixt.io.read.DogBean'>" +
                                          "    <element name='dog'>" +
                                          "      <addDefaults/>" +
                                          "    </element>" +
                                          "  </class>" +
                                          "</betwixt-config>";

    private static final String XML = "<?xml version='1.0'?>" +
                                      "    <animals> " +
                                      "        <ferret>" +
                                      "            <call>Dook</call>" +
                                      "            <colour>albino</colour>" +
                                      "            <latinName>Mustela putoris furo</latinName>" +
                                      "            <name>Lector</name>" +
                                      "        </ferret>" +
                                      "        <cat>" +
                                      "            <call>Meow</call>" +
                                      "            <colour>black</colour>" +
                                      "            <latinName>Felis catus</latinName>" +
                                      "            <name>Sam</name>" +
                                      "        </cat>" +
                                      "        <dog>" +
                                      "            <breed>mongrol</breed>" +
                                      "            <call>Woof</call>" +
                                      "            <latinName>Canis familiaris</latinName>" +
                                      "            <name>Bobby</name>" +
                                      "            <pedigree>false</pedigree>" +
                                      "        </dog>" +
                                      "    </animals>";

    public void testWrite() throws Exception
    {
        Animals animals = new Animals();
        animals.addAnimal(new FerretBean("albino", "Lector"));
        animals.addAnimal(new CatBean("Sam", "black"));
        animals.addAnimal(new DogBean(false, "mongrol", "Bobby"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        writer.write(animals);
        xmlAssertIsomorphic(parseString(XML), parseString(out), true);
    }


    public void testRead() throws Exception
    {
        StringReader in = new StringReader(XML);
        BeanReader reader = new BeanReader();
        reader.getBindingConfiguration().setMapIDs(false);
        reader.getXMLIntrospector().register(new InputSource(new StringReader(MAPPING)));
        reader.registerBeanClass(Animals.class);
        Animals animals = (Animals) reader.parse(in);

        assertNotNull(animals);
        assertEquals(3, animals.size());

        Iterator it = animals.getAnimals();
        Object firstAnimal = it.next();
        assertTrue("First animal is a ferret", firstAnimal instanceof FerretBean);
        FerretBean ferret = (FerretBean) firstAnimal;
        assertEquals("Ferret name", "Lector", ferret.getName());
        assertEquals("Ferret colour", "albino", ferret.getColour());

        Object secondAnimal = it.next();
        assertTrue(secondAnimal instanceof CatBean);
        CatBean cat = (CatBean) secondAnimal;
        assertEquals("Cat name", "Sam", cat.getName());
        assertEquals("Cat colour", "black", cat.getColour());

        Object thirdAnimal = it.next();
        assertTrue(thirdAnimal instanceof DogBean);
        DogBean dog = (DogBean) thirdAnimal;
        assertEquals("Dog pedigree", false, dog.isPedigree());
        assertEquals("Dog name", "Bobby", dog.getName());
    }
}
