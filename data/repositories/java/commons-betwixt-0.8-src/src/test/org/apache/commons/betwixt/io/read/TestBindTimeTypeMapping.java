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
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.MappingDerivationStrategy;
import org.xml.sax.InputSource;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class TestBindTimeTypeMapping extends AbstractTestCase
{



    public TestBindTimeTypeMapping(String testName)
    {
        super(testName);
    }

    //todo: note to self need to consider how the design of the introspection will work when you have strategy which takes
    // singular property which is not know until after the digestion

    public void testDefaultMappingDerivationStrategy() throws Exception
    {
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(Animals.class);
        ElementDescriptor animalsDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Use bind time type", true, animalsDescriptor.isUseBindTimeTypeForMapping());
        ElementDescriptor animalDescriptor = animalsDescriptor.getElementDescriptors()[0];
        assertEquals("Use bind time type", true, animalDescriptor.isUseBindTimeTypeForMapping());
    }

    public void testIntrospectionTimeMappingDerivationStrategy() throws Exception
    {
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        xmlIntrospector.getConfiguration().setMappingDerivationStrategy(MappingDerivationStrategy.USE_INTROSPECTION_TIME_TYPE);
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(Animals.class);
        ElementDescriptor animalsDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Use introspection time type", false, animalsDescriptor.isUseBindTimeTypeForMapping());
        ElementDescriptor animalDescriptor = animalsDescriptor.getElementDescriptors()[0];
        assertEquals("Use introspection time type", false, animalDescriptor.isUseBindTimeTypeForMapping());
    }

    public void testBindTypeMappingDerivationStrategy() throws Exception
    {
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        xmlIntrospector.getConfiguration().setMappingDerivationStrategy(MappingDerivationStrategy.USE_BIND_TIME_TYPE);
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(Animals.class);
        ElementDescriptor animalsDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Use bind time type", true, animalsDescriptor.isUseBindTimeTypeForMapping());
        ElementDescriptor animalDescriptor = animalsDescriptor.getElementDescriptors()[0];
        assertEquals("Use bind time type", true, animalDescriptor.isUseBindTimeTypeForMapping());
    }

    public void testBindTypeMappingDerivationDotBetwixt() throws Exception
    {
        String mappingDocument="<?xml version='1.0'?><info>" +
                               "<element name='pet-record'>" +
                               "	<element name='pet' property='pet' mappingDerivation='bind'/>" +
                               "</element>" +
                               "</info>";
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        xmlIntrospector.getConfiguration().setMappingDerivationStrategy(MappingDerivationStrategy.USE_INTROSPECTION_TIME_TYPE);
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(PetBean.class, new InputSource(new StringReader(mappingDocument)));
        ElementDescriptor petDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Use type from strategy", true, petDescriptor.isUseBindTimeTypeForMapping());
        ElementDescriptor animalDescriptor = petDescriptor.getElementDescriptors()[0];
        assertEquals("Use type from document", true, animalDescriptor.isUseBindTimeTypeForMapping());
    }

    public void testIntrospectionTypeMappingDerivationDotBetwixt() throws Exception
    {
        String mappingDocument="<?xml version='1.0'?><info>" +
                               "<element name='pet-record'>" +
                               "	<element name='pet' property='pet' mappingDerivation='introspection'/>" +
                               "</element>" +
                               "</info>";
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        xmlIntrospector.getConfiguration().setMappingDerivationStrategy(MappingDerivationStrategy.USE_BIND_TIME_TYPE);
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(PetBean.class, new InputSource(new StringReader(mappingDocument)));
        ElementDescriptor petDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Use type from strategy", true, petDescriptor.isUseBindTimeTypeForMapping());
        ElementDescriptor animalDescriptor = petDescriptor.getElementDescriptors()[0];
        assertEquals("Use type from document", false, animalDescriptor.isUseBindTimeTypeForMapping());
    }

    public void testMappingDerivationDotBetwixtAddDefaults() throws Exception
    {
        String mappingDocument="<?xml version='1.0'?><info>" +
                               "<element name='pet-record'>" +
                               "	<element name='pet' property='pet' mappingDerivation='introspection'/>" +
                               "   <addDefaults/>" +
                               "</element>" +
                               "</info>";
        XMLIntrospector xmlIntrospector = new XMLIntrospector();
        xmlIntrospector.getConfiguration().setMappingDerivationStrategy(MappingDerivationStrategy.USE_BIND_TIME_TYPE);
        XMLBeanInfo xmlBeanInfo = xmlIntrospector.introspect(PetBean.class, new InputSource(new StringReader(mappingDocument)));
        ElementDescriptor petDescriptor = xmlBeanInfo.getElementDescriptor();
        assertEquals("Use type from strategy", true, petDescriptor.isUseBindTimeTypeForMapping());
        ElementDescriptor animalDescriptor = petDescriptor.getElementDescriptors()[0];
        assertEquals("Use type from document", false, animalDescriptor.isUseBindTimeTypeForMapping());
        ElementDescriptor personDescriptor = petDescriptor.getElementDescriptors()[1];
        assertEquals("Use type from document", true, personDescriptor.isUseBindTimeTypeForMapping());
    }

    public void testBindTimeTypeWrite() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        Animals animals = new Animals();
        animals.addAnimal(new FerretBean("albino", "Lector"));
        animals.addAnimal(new CatBean("Sam", "black"));
        animals.addAnimal(new DogBean("Bobby"));

        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration()
        .setMappingDerivationStrategy(MappingDerivationStrategy.USE_BIND_TIME_TYPE);
        writer.getXMLIntrospector().getConfiguration()
        .setWrapCollectionsInElement(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(animals);

        String expected = "<?xml version='1.0'?>" +
                          "<Animals>" +
                          "	<animal>" +
                          "		<call>Dook</call><colour>albino</colour>" +
                          "		<latinName>Mustela putoris furo</latinName><name>Lector</name>" +
                          "	</animal>" +
                          "	<animal>" +
                          "		<call>Meow</call><colour>black</colour>" +
                          "		<latinName>Felis catus</latinName><name>Sam</name>" +
                          "	</animal>" +
                          "	<animal>" +
                          "		<breed>mongrol</breed><call>Woof</call><latinName>Canis familiaris</latinName>" +
                          "		<name>Bobby</name><pedigree>false</pedigree>" +
                          "	</animal>" +
                          "</Animals>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out), true);
    }

    public void testBindTimeTypeRead() throws Exception
    {
        String xml = "<?xml version='1.0'?>" +
                     "<Animals>" +
                     "	<animal className='org.apache.commons.betwixt.io.read.FerretBean'>" +
                     "		<call>Dook</call><colour>albino</colour>" +
                     "		<latinName>Mustela putoris furo</latinName><name>Lector</name>" +
                     "	</animal>" +
                     "	<animal className='org.apache.commons.betwixt.io.read.CatBean'>" +
                     "		<call>Meow</call><colour>black</colour>" +
                     "		<latinName>Felis catus</latinName><name>Sam</name>" +
                     "	</animal>" +
                     "	<animal className='org.apache.commons.betwixt.io.read.DogBean'>" +
                     "		<breed>mongrol</breed><call>Woof</call><latinName>Canis familiaris</latinName>" +
                     "		<name>Bobby</name><pedigree>false</pedigree>" +
                     "	</animal>" +
                     "</Animals>";

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration()
        .setMappingDerivationStrategy(MappingDerivationStrategy.USE_BIND_TIME_TYPE);
        reader.getXMLIntrospector().getConfiguration()
        .setWrapCollectionsInElement(false);
        reader.getBindingConfiguration().setMapIDs(false);

        reader.registerBeanClass(Animals.class);
        Animals animals = (Animals) reader.parse(new StringReader(xml));
        assertEquals("Expexted three animals", 3, animals.size());
        Iterator it=animals.getAnimals();
        Animal animalOne = (Animal) it.next();
        assertTrue("Expected ferret", animalOne instanceof FerretBean);
        FerretBean ferretBean = (FerretBean) animalOne;
        assertEquals("Latin name property mapped", "Mustela putoris furo" , ferretBean.getLatinName());
        assertEquals("Call property mapped", "Dook" , ferretBean.getCall());
        assertEquals("Colour property mapped", "albino", ferretBean.getColour());
        assertEquals("Name property mapped", "Lector", ferretBean.getName());
        Animal animalTwo = (Animal) it.next();
        assertTrue("Expected cat", animalTwo instanceof CatBean);
        CatBean catBean = (CatBean) animalTwo;
        assertEquals("Latin name property mapped", "Felis catus" , catBean.getLatinName());
        assertEquals("Call property mapped", "Meow" , catBean.getCall());
        assertEquals("Colour property mapped", "black", catBean.getColour());
        assertEquals("Name property mapped", "Sam", catBean.getName());
        Animal animalThree = (Animal) it.next();
        assertTrue("Expected dog", animalThree instanceof DogBean);
        DogBean dogBean = (DogBean) animalThree;
        assertEquals("Latin name property mapped", "Canis familiaris" , dogBean.getLatinName());
        assertEquals("Call property mapped", "Woof" , dogBean.getCall());
        assertEquals("Breed property mapped", "mongrol", dogBean.getBreed());
        assertEquals("Name property mapped", "Bobby", dogBean.getName());
    }

    public void testIntrospectionTimeTypeWrite() throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        Animals animals = new Animals();
        animals.addAnimal(new FerretBean("albino", "Lector"));
        animals.addAnimal(new CatBean("Sam", "black"));
        animals.addAnimal(new DogBean("Bobby"));

        BeanWriter writer = new BeanWriter(out);
        writer.getXMLIntrospector().getConfiguration()
        .setMappingDerivationStrategy(MappingDerivationStrategy.USE_INTROSPECTION_TIME_TYPE);
        writer.getXMLIntrospector().getConfiguration()
        .setWrapCollectionsInElement(false);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(animals);

        String expected = "<?xml version='1.0'?><Animals>" +
                          "	<animal><call>Dook</call><latinName>Mustela putoris furo</latinName></animal>" +
                          "	<animal><call>Meow</call><latinName>Felis catus</latinName></animal>" +
                          "	<animal><call>Woof</call><latinName>Canis familiaris</latinName></animal>" +
                          "</Animals>";

        xmlAssertIsomorphicContent(parseString(expected), parseString(out), true);
    }

    public void testIntrospectionTimeTypeRead() throws Exception
    {
        String xml = "<?xml version='1.0'?>" +
                     "<Animals>" +
                     "	<animal className='org.apache.commons.betwixt.io.read.FerretBean'>" +
                     "		<call>Dook</call><colour>albino</colour>" +
                     "		<latinName>Mustela putoris furo</latinName><name>Lector</name>" +
                     "	</animal>" +
                     "	<animal className='org.apache.commons.betwixt.io.read.CatBean'>" +
                     "		<call>Meow</call><colour>black</colour>" +
                     "		<latinName>Felis catus</latinName><name>Sam</name>" +
                     "	</animal>" +
                     "	<animal className='org.apache.commons.betwixt.io.read.DogBean'>" +
                     "		<breed>mongrol</breed><call>Woof</call><latinName>Canis familiaris</latinName>" +
                     "		<name>Bobby</name><pedigree>false</pedigree>" +
                     "	</animal>" +
                     "</Animals>";

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration()
        .setMappingDerivationStrategy(MappingDerivationStrategy.USE_INTROSPECTION_TIME_TYPE);
        reader.getXMLIntrospector().getConfiguration()
        .setWrapCollectionsInElement(false);
        reader.getBindingConfiguration().setMapIDs(false);

        reader.registerBeanClass(Animals.class);
        Animals animals = (Animals) reader.parse(new StringReader(xml));
        assertEquals("Expexted three animals", 3, animals.size());
        Iterator it=animals.getAnimals();
        Animal animalOne = (Animal) it.next();
        assertTrue("Expected ferret", animalOne instanceof FerretBean);
        FerretBean ferretBean = (FerretBean) animalOne;
        assertEquals("Latin name property mapped", "Mustela putoris furo" , ferretBean.getLatinName());
        assertEquals("Call property mapped", "Dook" , ferretBean.getCall());
        assertNull("Colour property not mapped", ferretBean.getColour());
        assertNull("Name property not mapped", ferretBean.getName());
        Animal animalTwo = (Animal) it.next();
        assertTrue("Expected cat", animalTwo instanceof CatBean);
        CatBean catBean = (CatBean) animalTwo;
        assertEquals("Latin name property mapped", "Felis catus" , catBean.getLatinName());
        assertEquals("Call property mapped", "Meow" , catBean.getCall());
        assertNull("Colour property not mapped", catBean.getColour());
        assertNull("Name property not mapped", catBean.getName());
        Animal animalThree = (Animal) it.next();
        assertTrue("Expected dog", animalThree instanceof DogBean);
        DogBean dogBean = (DogBean) animalThree;
        assertEquals("Latin name property mapped", "Canis familiaris" , dogBean.getLatinName());
        assertEquals("Call property mapped", "Woof" , dogBean.getCall());
        assertNull("Breed property not mapped", dogBean.getBreed());
        assertNull("Name property not mapped", dogBean.getName());
    }
}
