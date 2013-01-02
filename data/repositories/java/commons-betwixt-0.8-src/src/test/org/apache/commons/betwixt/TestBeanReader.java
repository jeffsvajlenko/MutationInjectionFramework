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
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.apache.commons.beanutils.ConversionException;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.betwixt.io.BeanReader;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.ConvertUtilsObjectStringConverter;
import org.apache.commons.betwixt.strategy.HyphenatedNameMapper;
import org.apache.commons.digester.ExtendedBaseRules;
import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;


/** Test harness for the BeanReader
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision: 438373 $
  */
public class TestBeanReader extends AbstractTestCase
{

    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }

    public static Test suite()
    {
        return new TestSuite(TestBeanReader.class);
    }

    public TestBeanReader(String testName)
    {
        super(testName);
    }

    public void testBeanWriter() throws Exception
    {
        BeanReader reader = new BeanReader();
        reader.registerBeanClass( getBeanClass() );

        InputStream in = getXMLInput();
        try
        {
            Object bean = reader.parse( in );

            testCustomer(bean);

            String out = writeBean( bean );
            String xml = "<?xml version='1.0'?><CustomerBean><name>James</name><time>20:30:40</time>"
                         + "<date>2002-03-17</date><projectMap/><bigDecimal>1234567890.12345</bigDecimal>"
                         + "<bigInteger>1234567890</bigInteger><projectNames/><emails>"
                         + "<email>jstrachan@apache.org</email><email>james_strachan@yahoo.co.uk</email>"
                         + "</emails><timestamp>2002-03-17 20:30:40.0</timestamp><locations>"
                         + "<location>London</location><location>Bath</location></locations>"
                         + "<ID/><projectURLs/><nickName/><address><code/><country/>"
                         + "<city/><street/></address><numbers><number>3</number><number>4</number>"
                         + "<number>5</number></numbers></CustomerBean>";

            xmlAssertIsomorphic(parseString(xml), parseString(out) , true);
        }
        finally
        {
            in.close();
        }
    }

    public void testWriteThenRead() throws Exception
    {
        // test defaults
        PersonBean bean = new PersonBean(21, "Samual Smith");
        StringWriter stringWriter = new StringWriter();
        BeanWriter beanWriter = new BeanWriter(stringWriter);
        beanWriter.write(bean);
        stringWriter.flush();
        String xml = "<?xml version='1.0'?>" + stringWriter.toString();

        BeanReader reader = new BeanReader();
        reader.registerBeanClass( PersonBean.class );
        bean = (PersonBean) reader.parse(new StringReader(xml));

        assertEquals("Person age wrong", 21 , bean.getAge());
        assertEquals("Person name wrong", "Samual Smith" , bean.getName());

        // test now with attributes for primitives
        bean = new PersonBean(19, "John Smith");
        stringWriter = new StringWriter();
        beanWriter = new BeanWriter(stringWriter);
        beanWriter.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        beanWriter.write(bean);
        stringWriter.flush();
        xml = "<?xml version='1.0'?>" + stringWriter.toString();

        reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        reader.registerBeanClass( PersonBean.class );
        bean = (PersonBean) reader.parse(new StringReader(xml));

        assertEquals("[Attribute] Person age wrong", 19 , bean.getAge());
        assertEquals("[Attribute] Person name wrong", "John Smith" , bean.getName());
    }

    public String writeBean(Object bean) throws Exception
    {
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.setEndOfLine("\n");
        writer.enablePrettyPrint();
        writer.write( bean );
        return out.getBuffer().toString();
    }

    /** @return the bean class to use as the root */
    public Class getBeanClass()
    {
        return CustomerBean.class;
    }

    /**
     * Asserts that the parsed CustomerBean looks fine
     */
    protected void testCustomer(Object bean) throws Exception
    {
        assertTrue( "Is a CustomerBean", bean instanceof CustomerBean );
        CustomerBean customer = (CustomerBean) bean;

        assertEquals( "name", "James", customer.getName() );

        String[] emails = customer.getEmails();
        assertTrue( "contains some emails", emails != null );
        assertEquals( "emails.length", 2, emails.length );
        assertEquals( "emails[0]", "jstrachan@apache.org", emails[0] );
        assertEquals( "emails[1]", "james_strachan@yahoo.co.uk", emails[1] );

        int[] numbers = customer.getNumbers();
        assertTrue( "contains some numbers", numbers != null );
        assertEquals( "numbers.length", 3, numbers.length );
        assertEquals( "numbers[0]", 3, numbers[0] );
        assertEquals( "numbers[1]", 4, numbers[1] );
        assertEquals( "numbers[2]", 5, numbers[2] );

        List locations = customer.getLocations();
        assertTrue( "contains some locations", locations != null );
        assertEquals( "locations.size()", 2, locations.size() );
        assertEquals( "locations[0]", "London", locations.get(0) );
        assertEquals( "locations[1]", "Bath", locations.get(1) );

        assertEquals( ConvertUtils.convert("2002-03-17", Date.class), customer.getDate());
        assertEquals( ConvertUtils.convert("20:30:40", Time.class), customer.getTime());
        assertEquals( ConvertUtils.convert("2002-03-17 20:30:40.0", Timestamp.class), customer.getTimestamp());

        assertEquals( new BigDecimal("1234567890.12345"), customer.getBigDecimal() );
        assertEquals( new BigInteger("1234567890"), customer.getBigInteger() );
    }

    protected InputStream getXMLInput() throws IOException
    {
        return new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/customer.xml") );
    }

    /**
     * This tests that you can read a bean which has an adder but not a property
     */
    public void testAdderButNoProperty() throws Exception
    {
        /*
        //
        // This is a test for an unfixed issue that might - or might not - be a bug
        // a developer submitted a patch but this broke the other unit test
        // a proper fix would require quite a lot of work including some refactoring
        // of various interfaces
        //

        // check bean's still working
        AdderButNoPropertyBean bean = new AdderButNoPropertyBean();
        bean.addString("one");
        bean.addString("two");
        bean.addString("three");
        checkBean(bean);

        BeanReader reader = new BeanReader();
        reader.registerBeanClass( AdderButNoPropertyBean.class );

        InputStream in =
            new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/adder-np.xml") );
        try {

            checkBean((AdderButNoPropertyBean) reader.parse( in ));

        }
        finally {
            in.close();
        }
        */
    }

    private void checkBean(AdderButNoPropertyBean bean) throws Exception
    {
        assertEquals("Bad addString call count", 3, bean.stringCallCount());
    }

    private void checkBean(PersonListBean bean) throws Exception
    {
        assertEquals("PersonList size", 4, bean.getPersonList().size());
        assertEquals("PersonList value (1)", "Athos", ((PersonBean) bean.getPersonList().get(0)).getName());
        assertEquals("PersonList value (2)", "Porthos", ((PersonBean) bean.getPersonList().get(1)).getName());
        assertEquals("PersonList value (3)", "Aramis", ((PersonBean) bean.getPersonList().get(2)).getName());
        assertEquals("PersonList value (4)", "D'Artagnan", ((PersonBean) bean.getPersonList().get(3)).getName());
    }

    public void testPersonList() throws Exception
    {

        PersonListBean people = new PersonListBean();
        people.addPerson(new PersonBean(22, "Athos"));
        people.addPerson(new PersonBean(25, "Porthos"));
        people.addPerson(new PersonBean(23, "Aramis"));
        people.addPerson(new PersonBean(18, "D'Artagnan"));

        checkBean(people);
//
// Logging and debugging code for this method commented out
//
//        writeBean(people);

//        SimpleLog log = new SimpleLog("[TestPersonList:XMLIntrospectorHelper]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        XMLIntrospectorHelper.setLog(log);


//        log = new SimpleLog("[TestPersonList:BeanCreateRule]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        BeanCreateRule.setLog(log);

//        log = new SimpleLog("[TestPersonList:XMLIntrospector]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

        BeanReader reader = new BeanReader();
//        reader.getXMLIntrospector().setLog(log);

//        log = new SimpleLog("[TestPersonList:BeanReader]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);

//        reader.setLog(log);
        reader.registerBeanClass( PersonListBean.class );

        InputStream in =
            new FileInputStream( getTestFile("src/test/org/apache/commons/betwixt/person-list.xml") );
        try
        {

            checkBean((PersonListBean) reader.parse( in ));

        }
        finally
        {
            in.close();
        }
    }

    /** Another test for reading wrapped collections */
    public void testWrapElements() throws Exception
    {
        ListOfNames listOfNames = new ListOfNames();
        listOfNames.addName( new NameBean("Martin") );

        String xml = "<ListOfNames><names><name name='Martin'/></names></ListOfNames>";

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(true);

        reader.registerBeanClass(ListOfNames.class);
        ListOfNames newListOfNames = (ListOfNames) reader.parse(new StringReader(xml));

        assertEquals("Wrapped collection read fails", listOfNames, newListOfNames);
    }

    public void testSetDigesterRules() throws Exception
    {
        NameBean martinBean = new NameBean("Martin");
        ListOfNames listOfNames = new ListOfNames();
        listOfNames.addName( martinBean );

        String xml = "<ListOfNames><names><name name='Martin'/></names></ListOfNames>";

        BeanReader reader = new BeanReader();
        reader.setRules( new ExtendedBaseRules() );
        reader.getXMLIntrospector().getConfiguration().setAttributesForPrimitives(true);
        reader.getXMLIntrospector().getConfiguration().setWrapCollectionsInElement(true);

        TestRule ruleOne = new TestRule();
        TestRule ruleTwo = new TestRule();

        // add a test rule before the bean rules
        reader.addRule("!*/ListOfNames/names/name", ruleOne);
        reader.registerBeanClass(ListOfNames.class);
        // add a test rule after the bean rules
        reader.addRule("!*/ListOfNames/names/name", ruleTwo);

        ListOfNames newListOfNames = (ListOfNames) reader.parse(new StringReader(xml));

        reader.parse(new StringReader(xml));

        // test that the rules were called
        assertEquals("Rule one called", true , ruleOne.isCalled());
        assertEquals("Rule two called", true , ruleTwo.isCalled());

        // test that the top objects are correct
        assertEquals("Rule one digester top object", listOfNames , ruleOne.getTop());
        assertEquals("Rule two digester top object", martinBean , ruleTwo.getTop());
    }

    public void testDateReadConversion() throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2003, 7, 2, 19, 30, 00);
        java.util.Date date = calendar.getTime();

        String dateToString = date.toString();

        PartyBean bean = new PartyBean(
            "Wedding",
            date,
            1930,
            new AddressBean("Old White Lion Hotel", "Howarth", "Merry Old England", "BD22 8EP"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        XMLIntrospector introspector = writer.getXMLIntrospector();
        introspector.getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        introspector.getConfiguration().setAttributesForPrimitives(false);

        writer.write("party", bean);

        String xml = "<?xml version='1.0'?><party>"
                     + "<venue><street>Old White Lion Hotel</street><city>Howarth</city>"
                     + "<code>BD22 8EP</code><country>Merry Old England</country></venue>"
                     + "<date-of-party>" + dateToString
                     + "</date-of-party><from-hour>1930</from-hour>"
                     + "<excuse>Wedding</excuse>"
                     + "</party>";

        xmlAssertIsomorphic(parseString(xml), parseString(out) , true);

        BeanReader reader = new BeanReader();
        reader.setXMLIntrospector(introspector);
        reader.registerBeanClass("party", PartyBean.class);
        PartyBean readBean = (PartyBean) reader.parse(new StringReader(xml));

        assertEquals("FromHours incorrect property value", readBean.getFromHour(), bean.getFromHour());
        assertEquals("Excuse incorrect property value", readBean.getExcuse(), bean.getExcuse());

        // check address
        AddressBean readAddress = readBean.getVenue();
        AddressBean address = bean.getVenue();
        assertEquals("address.street incorrect property value", readAddress.getStreet(), address.getStreet());
        assertEquals("address.city incorrect property value", readAddress.getCity(), address.getCity());
        assertEquals("address.code incorrect property value", readAddress.getCode(), address.getCode());
        assertEquals("address.country incorrect property value", readAddress.getCountry(), address.getCountry());

        // check dates
        assertEquals("Incorrect date property", date.toGMTString(), readBean.getDateOfParty().toGMTString());
    }

    public void testHyphenatedNameMapping() throws Exception
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2003, 7, 2, 19, 30, 00);
        java.util.Date date = calendar.getTime();

        String dateToString = date.toString();

        PartyBean bean = new PartyBean(
            "Wedding",
            date,
            1930,
            new AddressBean("Old White Lion Hotel", "Howarth", "Merry Old England", "BD22 8EP"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        XMLIntrospector introspector = writer.getXMLIntrospector();
        introspector.getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        introspector.getConfiguration().setAttributesForPrimitives(false);

        writer.write(bean);

        String xml = "<?xml version='1.0'?><party-bean>"
                     + "<venue><street>Old White Lion Hotel</street><city>Howarth</city>"
                     + "<code>BD22 8EP</code><country>Merry Old England</country></venue>"
                     + "<date-of-party>" + dateToString
                     + "</date-of-party><from-hour>1930</from-hour>"
                     + "<excuse>Wedding</excuse>"
                     + "</party-bean>";

        xmlAssertIsomorphic(parseString(xml), parseString(out) , true);

        BeanReader reader = new BeanReader();
        reader.setXMLIntrospector(introspector);
        reader.registerBeanClass(PartyBean.class);
        PartyBean readBean = (PartyBean) reader.parse(new StringReader(xml));

        assertEquals("FromHours incorrect property value", readBean.getFromHour(), bean.getFromHour());
        assertEquals("Excuse incorrect property value", readBean.getExcuse(), bean.getExcuse());

        // check address
        AddressBean readAddress = readBean.getVenue();
        AddressBean address = bean.getVenue();
        assertEquals("address.street incorrect property value", readAddress.getStreet(), address.getStreet());
        assertEquals("address.city incorrect property value", readAddress.getCity(), address.getCity());
        assertEquals("address.code incorrect property value", readAddress.getCode(), address.getCode());
        assertEquals("address.country incorrect property value", readAddress.getCountry(), address.getCountry());

        // check dates
        assertEquals("Incorrect date property", date.toGMTString(), readBean.getDateOfParty().toGMTString());

    }

    public void testCustomDateReadConversion() throws Exception
    {

        BindingConfiguration configuration = new BindingConfiguration(
            new ConvertUtilsObjectStringConverter(),false);

        //SimpleLog log = new SimpleLog("testDateReadConversion:MethodUpdater");
        //log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
        //MethodUpdater.setLog(log);

        class ISOToStringConverter implements Converter
        {
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            public Object convert(Class type, Object value)
            {
                if (value == null)
                {
                    return null;
                }
                if (value instanceof java.util.Date)
                {
                    return formatter.format((java.util.Date)value);
                }
                return value.toString();
            }
        }

        class ISODateConverter implements Converter
        {
            final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            public Object convert(Class type, Object value)
            {

                if (value == null)
                {
                    return null;
                }

                if (value instanceof java.util.Date)
                {

                    return formatter.format((java.util.Date)value);
                }

                try
                {
                    return formatter.parse(value.toString());
                }
                catch (ParseException ex)
                {
                    throw new ConversionException(ex);
                }
            }
        }

        ISODateConverter converter = new ISODateConverter();
        ConvertUtils.register(converter, java.util.Date.class);
        ISOToStringConverter tsConverter = new ISOToStringConverter();
        ConvertUtils.register(tsConverter, String.class);

        Converter dateConverter = ConvertUtils.lookup(java.util.Date.class);
        assertEquals("Date converter successfully registered", dateConverter, converter);
        Converter stringConverter = ConvertUtils.lookup(String.class);
        assertEquals("Date converter successfully registered", tsConverter, stringConverter);

        java.util.Date conversionResult = (java.util.Date)
                                          ConvertUtils.convert("20030101", java.util.Date.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(conversionResult);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        assertEquals("Correct conversion result", dayOfYear, 1);

        calendar.set(2003, 7, 2);
        java.util.Date date = calendar.getTime();

        PartyBean bean = new PartyBean(
            "Wedding",
            date,
            1900,
            new AddressBean("Old White Lion Hotel", "Howarth", "Merry Old England", "BD22 8EP"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");

        BeanWriter writer = new BeanWriter(out);
        writer.setBindingConfiguration(configuration);
        XMLIntrospector introspector = writer.getXMLIntrospector();
        introspector.getConfiguration().setElementNameMapper(new HyphenatedNameMapper());
        introspector.getConfiguration().setAttributesForPrimitives(false);

        writer.write("party", bean);

        String xml = "<?xml version='1.0'?><party>"
                     + "<venue><street>Old White Lion Hotel</street><city>Howarth</city>"
                     + "<code>BD22 8EP</code><country>Merry Old England</country></venue>"
                     + "<date-of-party>20030802</date-of-party><from-hour>1900</from-hour>"
                     + "<excuse>Wedding</excuse>"
                     + "</party>";

        xmlAssertIsomorphic(parseString(xml), parseString(out) , true);

        BeanReader reader = new BeanReader();
        reader.setBindingConfiguration(configuration);
        reader.setXMLIntrospector(introspector);
        reader.registerBeanClass("party", PartyBean.class);
        PartyBean readBean = (PartyBean) reader.parse(new StringReader(xml));

        assertEquals("FromHours incorrect property value", readBean.getFromHour(), bean.getFromHour());
        assertEquals("Excuse incorrect property value", readBean.getExcuse(), bean.getExcuse());

        // check address
        AddressBean readAddress = readBean.getVenue();
        AddressBean address = bean.getVenue();
        assertEquals("address.street incorrect property value", readAddress.getStreet(), address.getStreet());
        assertEquals("address.city incorrect property value", readAddress.getCity(), address.getCity());
        assertEquals("address.code incorrect property value", readAddress.getCode(), address.getCode());
        assertEquals("address.country incorrect property value", readAddress.getCountry(), address.getCountry());

        // check dates
        calendar.setTime(bean.getDateOfParty());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int year = calendar.get(Calendar.YEAR);
        calendar.setTime(readBean.getDateOfParty());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        int readDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int readYear = calendar.get(Calendar.YEAR);
        assertEquals("date incorrect property value (year)", year, readYear);
        assertEquals("date incorrect property value (day)", dayOfYear, readDayOfYear);

        ConvertUtils.deregister();
    }


    public void testReadMap() throws Exception
    {
        // we might as well start by writing out
        MapBean bean = new MapBean("drinkers");
        bean.addAddress(
            "Russell McManus",
            new AddressBean("6, Westgate","Shipley", "United Kingdom", "BD17 5EJ"));
        bean.addAddress(
            "Alex Compbell",
            new AddressBean("5, Kirkgate","Shipley", "United Kingdom", "BD18 3QW"));
        bean.addAddress(
            "Sid Gardner",
            new AddressBean("Old House At Home, Otley Road","Shipley", "United Kingdom", "BD18 2BJ"));

        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write("address-book", bean);

        String xml = "<?xml version='1.0'?><address-book><title>drinkers</title>"
                     + "<addresses>"
                     + "<entry><key>Alex Compbell</key><value><country>United Kingdom</country>"
                     + "<code>BD18 3QW</code><city>Shipley</city><street>5, Kirkgate</street></value></entry>"
                     + "<entry><key>Russell McManus</key><value><country>United Kingdom</country><code>BD17 5EJ</code>"
                     + "<city>Shipley</city><street>6, Westgate</street></value></entry>"
                     + "<entry><key>Sid Gardner</key><value><country>United Kingdom</country>"
                     + "<code>BD18 2BJ</code><city>Shipley</city><street>Old House At Home, Otley Road</street>"
                     + "</value></entry>"
                     + "</addresses></address-book>";

        xmlAssertIsomorphic(parseString(out.toString()), parseString(xml), true);

//        SimpleLog log = new SimpleLog("[testReadMap:BeanRuleSet]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        BeanRuleSet.setLog(log);
//        log = new SimpleLog("[testReadMap:XMLIntrospectorHelper]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        XMLIntrospectorHelper.setLog(log);
//        log = new SimpleLog("[testReadMap:MapEntryAdder]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        MapEntryAdder.setLog(log);

        BeanReader reader = new BeanReader();

//        log = new SimpleLog("[testReadMap:BeanReader]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        reader.setLog(log);
//        log = new SimpleLog("[testReadMap:XMLIntrospector]");
//        log.setLevel(SimpleLog.LOG_LEVEL_TRACE);
//        reader.getXMLIntrospector().setLog(log);

        reader.getBindingConfiguration().setMapIDs(false);
        reader.registerBeanClass("address-book", MapBean.class);
        bean = (MapBean) reader.parse(new StringReader(xml));

        assertEquals("Title property is incorrect", "drinkers", bean.getTitle());
        assertEquals("Map entries", 3, bean.getAddresses().size());

        AddressBean address = (AddressBean) bean.getAddresses().get("Russell McManus");
        assertNotNull("Missing entry for 'Russell McManus'", address);
        assertEquals("Bad address (1)", "6, Westgate", address.getStreet());
        assertEquals("Bad address (2)", "Shipley", address.getCity());
        assertEquals("Bad address (3)",  "United Kingdom", address.getCountry());
        assertEquals("Bad address (4)", "BD17 5EJ", address.getCode());

        address = (AddressBean) bean.getAddresses().get("Alex Compbell");
        assertNotNull("Missing entry for 'Alex Compbell'", address);
        assertEquals("Bad address (5)", "5, Kirkgate", address.getStreet());
        assertEquals("Bad address (6)", "Shipley", address.getCity());
        assertEquals("Bad address (7)",  "United Kingdom", address.getCountry());
        assertEquals("Bad address (8)", "BD18 3QW", address.getCode());

        address = (AddressBean) bean.getAddresses().get("Sid Gardner");
        assertNotNull("Missing entry for 'Sid Gardner'", address);
        assertEquals("Bad address (9)", "Old House At Home, Otley Road", address.getStreet());
        assertEquals("Bad address (10)", "Shipley", address.getCity());
        assertEquals("Bad address (11)",  "United Kingdom", address.getCountry());
        assertEquals("Bad address (12)", "BD18 2BJ", address.getCode());
    }

    public void testReadMap2() throws Exception
    {
        IdMap idMap = new IdMap();
        String id ="3920";
        idMap.addId(id, new Integer(1));
        StringWriter outputWriter = new StringWriter();
        outputWriter.write("<?xml version='1.0' ?>\n");
        BeanWriter beanWriter = new BeanWriter(outputWriter);
        beanWriter.write(idMap);
        String xml = outputWriter.toString();
        System.out.println("Map test: " + xml);

        BeanReader beanReader = new BeanReader();
        beanReader.registerBeanClass(IdMap.class);
        IdMap result = (IdMap)beanReader.parse(new StringReader(xml));
        assertNotNull("didn't get an object back!", result);
        assertNotNull("didn't get a Map out of the IdMap!", result.getIds());
        assertEquals("Got the Map, but doesn't have an entry!", 1, result.getIds().size());
        assertNotNull("Got the Map, but doesn't have correct values!", result.getIds().get(id));
    }

    public void testIndirectReference() throws Exception
    {
        Tweedledum dum = new Tweedledum();
        Tweedledee dee = new Tweedledee(dum);
        StringWriter out = new StringWriter();
        out.write("<?xml version='1.0'?>");
        BeanWriter writer = new BeanWriter(out);
        writer.getBindingConfiguration().setMapIDs(false);
        writer.write(dee);
        String xml =  "<?xml version='1.0'?><Tweedledee><name>Tweedledee</name>"
                      + "<brother><name>Tweedledum</name></brother></Tweedledee>";
        xmlAssertIsomorphic(parseString(xml), parseString(out) , true);

        BeanReader reader = new BeanReader();

        reader.getBindingConfiguration().setMapIDs(false);
        reader.registerBeanClass(Tweedledee.class);
        Tweedledee bean = (Tweedledee) reader.parse(new StringReader(xml));
        assertNotNull(bean.getBrother());
    }

    public void _testDoubleLinkedCollectionRead() throws Exception
    {
        String xml =  "<?xml version='1.0'?><DOUBLE_LINKED_PARENT_BEAN>"
                      + "<NAME>Cronus</NAME>"
                      + "<CHILDREN>"
                      + "<CHILD><NAME>Hades</NAME></CHILD>"
                      + "<CHILD><NAME>Hera</NAME></CHILD>"
                      + "<CHILD><NAME>Hestia</NAME></CHILD>"
                      + "<CHILD><NAME>Demeter</NAME></CHILD>"
                      + "<CHILD><NAME>Poseidon</NAME></CHILD>"
                      + "<CHILD><NAME>Zeus</NAME></CHILD>"
                      + "</CHILDREN></DOUBLE_LINKED_PARENT_BEAN>";

        BeanReader reader = new BeanReader();
        reader.getXMLIntrospector().getConfiguration().setElementNameMapper(new HyphenatedNameMapper(true, "_"));
        reader.registerBeanClass(DoubleLinkedParentBean.class);
        DoubleLinkedParentBean bean = (DoubleLinkedParentBean) reader.parse(new StringReader(xml));

        assertNotNull("Bean read", bean);
        assertEquals("Cronus", "Parent name", bean.getName());
        assertEquals("Number of children", 6, bean.getSize());

        ArrayList list = new ArrayList();
        for (Iterator it=bean.getChildren(); it.hasNext();)
        {
            list.add(it.next());
        }

        DoubleLinkedChildBean childZero = (DoubleLinkedChildBean) list.get(0);
        DoubleLinkedChildBean childOne = (DoubleLinkedChildBean) list.get(1);
        DoubleLinkedChildBean childTwo = (DoubleLinkedChildBean) list.get(2);
        DoubleLinkedChildBean childThree = (DoubleLinkedChildBean) list.get(3);
        DoubleLinkedChildBean childFour = (DoubleLinkedChildBean) list.get(4);
        DoubleLinkedChildBean childFive = (DoubleLinkedChildBean) list.get(5);

        assertEquals("Child name zero", "Hades", childZero.getName());
        assertEquals("Child name one", "Hera", childZero.getName());
        assertEquals("Child name two", "Hestia", childZero.getName());
        assertEquals("Child name three", "Demeter", childZero.getName());
        assertEquals("Child name four", "Poseidon", childZero.getName());
        assertEquals("Child name five", "Zeus", childZero.getName());

    }

    /**
      * This is a member class since all classes starting with test
      * will be run as test cases.
      */
    private class TestRule extends Rule
    {

        private String name;
        private boolean called = false;
        private Object top;

        public Object getTop()
        {
            return top;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public boolean isCalled()
        {
            return called;
        }

        public void begin(String name, String namespace, Attributes attributes)
        {
            top = getDigester().peek();
            called = true;
        }
    }
}

