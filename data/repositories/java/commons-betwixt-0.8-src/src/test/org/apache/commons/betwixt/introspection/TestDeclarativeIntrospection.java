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

package org.apache.commons.betwixt.introspection;

import java.util.List;

import org.apache.commons.betwixt.AbstractTestCase;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.examples.rss.Channel;

/**
 * Tests for the new, more declarative style of introspection.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestDeclarativeIntrospection extends AbstractTestCase
{
    public TestDeclarativeIntrospection(String name)
    {
        super(name);
    }

    /** Tests whether a standard property's ElementDescriptor is hollow (as expected) */
    public void testStandardPropertyIsHollow() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo out = introspector.introspect(CompanyBean.class);

        ElementDescriptor companyBeanDescriptor = out.getElementDescriptor();
        ElementDescriptor[] childDescriptors = companyBeanDescriptor.getElementDescriptors();
        assertEquals("Correct number of child descriptors", 1, childDescriptors.length);

        ElementDescriptor addressDescriptor = childDescriptors[0];
        assertEquals("standard property is hollow", true, addressDescriptor.isHollow());
    }


    /** Tests whether a simple element's ElementDescriptor is hollow */
    public void testSimpleElementIsHollow() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setAttributesForPrimitives(false);
        XMLBeanInfo out = introspector.introspect(CompanyBean.class);

        ElementDescriptor companyBeanDescriptor = out.getElementDescriptor();
        ElementDescriptor[] childDescriptors = companyBeanDescriptor.getElementDescriptors();
        assertEquals("Correct number of child descriptors", 2, childDescriptors.length);

        ElementDescriptor nameDescriptor = null;
        for (int i=0, size=childDescriptors.length; i<size; i++)
        {
            if ("name".equals(childDescriptors[i].getLocalName()))
            {
                nameDescriptor = childDescriptors[i];
            }
        }

        assertNotNull("Expected to find an element descriptor for 'name'", nameDescriptor);
        assertFalse("Expected simple element not to be hollow", nameDescriptor.isHollow());
    }

    public void testWrappedCollective() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(true);
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo out = introspector.introspect(PhoneBookBean.class);

        // with wrapped collective, we expect a spacer element descriptor
        // (for the collective) containing a single collective descriptor
        ElementDescriptor phoneBookBeanDescriptor = out.getElementDescriptor();
        ElementDescriptor[] phoneBookChildDescriptors = phoneBookBeanDescriptor.getElementDescriptors();
        assertEquals("Expected single wrapping descriptor", 1, phoneBookChildDescriptors.length);

        ElementDescriptor wrappingDescriptor = phoneBookChildDescriptors[0];
        assertNull("Spacer should not have an updater", wrappingDescriptor.getUpdater());
        assertEquals("Wrapper element name should match getter", "numbers" , wrappingDescriptor.getQualifiedName());

        ElementDescriptor[] wrappingChildDescriptors = wrappingDescriptor.getElementDescriptors();
        assertEquals("Expected single child for wrapping descriptor", 1, wrappingChildDescriptors.length);

        ElementDescriptor hollowPhoneNumberDescriptor = wrappingChildDescriptors[0];
        assertTrue("Expected wrapped descriptor to be hollow", hollowPhoneNumberDescriptor.isHollow());
        assertEquals("Expected the collective property type to be a list",
                     List.class,
                     hollowPhoneNumberDescriptor.getPropertyType());
        assertEquals("Expected the singular property type to be the phone number",
                     PhoneNumberBean.class,
                     hollowPhoneNumberDescriptor.getSingularPropertyType());

        assertEquals("Collective element name should match adder", "number" , hollowPhoneNumberDescriptor.getQualifiedName());

    }

    public void testUnwrappedCollective() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(false);
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo out = introspector.introspect(PhoneBookBean.class);

        // with wrapped collective, we expect a spacer element descriptor
        // (for the collective) containing a single collective descriptor
        ElementDescriptor phoneBookBeanDescriptor = out.getElementDescriptor();
        ElementDescriptor[] phoneBookChildDescriptors = phoneBookBeanDescriptor.getElementDescriptors();
        assertEquals("Expected single child descriptor", 1, phoneBookChildDescriptors.length);

        ElementDescriptor hollowPhoneNumberDescriptor = phoneBookChildDescriptors[0];

        assertTrue("Expected collective element descriptor to be hollow", hollowPhoneNumberDescriptor.isHollow());
        assertEquals("Expected the collective property type to be a list",
                     List.class,
                     hollowPhoneNumberDescriptor.getPropertyType());
        assertEquals("Expected the singular property type to be the phone number",
                     PhoneNumberBean.class,
                     hollowPhoneNumberDescriptor.getSingularPropertyType());
        assertEquals("Collective element name should match adder", "number" , hollowPhoneNumberDescriptor.getQualifiedName());
    }

    public void testUnwrappedMap() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(false);
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo out = introspector.introspect(DateFormatterBean.class);

        ElementDescriptor formatterDescriptor = out.getElementDescriptor();
        ElementDescriptor[] formatterChildDescriptors = formatterDescriptor.getElementDescriptors();

        assertEquals("Only one top level child", 1, formatterChildDescriptors.length);

        ElementDescriptor entryDescriptor = formatterChildDescriptors[0];
        assertEquals("Must be called entry", "entry" , entryDescriptor.getLocalName());
        assertFalse("Is not hollow",  entryDescriptor.isHollow());
        assertNull("No updater for entry spacer",  entryDescriptor.getUpdater());

        ElementDescriptor[] entryChildDesciptors = entryDescriptor.getElementDescriptors();
        assertEquals("Entry has two children", 2, entryChildDesciptors.length);

        ElementDescriptor keyDescriptor = entryChildDesciptors[0];
        assertEquals("Must be called key", "key", keyDescriptor.getLocalName());
        assertTrue("Is not simple therefore hollow",  keyDescriptor.isHollow());
        assertNotNull("Key should have an updater", keyDescriptor.getUpdater());

        ElementDescriptor valueDescriptor = entryChildDesciptors[1];
        assertEquals("Must be called key", "value", valueDescriptor.getLocalName());
        assertTrue("Is not simple therefore hollow",  valueDescriptor.isHollow());
        assertNotNull("Value should have an updater", valueDescriptor.getUpdater());
    }

    public void testWrappedMap() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(true);
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo out = introspector.introspect(DateFormatterBean.class);

        ElementDescriptor formatterDescriptor = out.getElementDescriptor();
        ElementDescriptor[] formatterChildDescriptors = formatterDescriptor.getElementDescriptors();

        assertEquals("Only one top level child", 1, formatterChildDescriptors.length);

        ElementDescriptor spacerDescriptor = formatterChildDescriptors[0];
        assertEquals("Spacer must be called formats", "formats" , spacerDescriptor.getLocalName());
        assertFalse("Is not hollow",  spacerDescriptor.isHollow());
        assertNull("No updater for entry spacer",  spacerDescriptor.getUpdater());

        ElementDescriptor[] spacerChildDescriptors = spacerDescriptor.getElementDescriptors();
        assertEquals("Only one top level child", 1, spacerChildDescriptors.length);

        ElementDescriptor entryDescriptor = spacerChildDescriptors[0];
        assertEquals("Must be called entry", "entry" , entryDescriptor.getLocalName());
        assertFalse("Is not hollow",  entryDescriptor.isHollow());
        assertNull("No updater for entry spacer",  entryDescriptor.getUpdater());

        ElementDescriptor[] entryChildDesciptors = entryDescriptor.getElementDescriptors();
        assertEquals("Entry has two children", 2, entryChildDesciptors.length);

        ElementDescriptor keyDescriptor = entryChildDesciptors[0];
        assertEquals("Must be called key", "key", keyDescriptor.getLocalName());
        assertTrue("Is not simple therefore hollow",  keyDescriptor.isHollow());
        assertNotNull("Key should have an updater", keyDescriptor.getUpdater());

        ElementDescriptor valueDescriptor = entryChildDesciptors[1];
        assertEquals("Must be called key", "value", valueDescriptor.getLocalName());
        assertTrue("Is not simple therefore hollow",  valueDescriptor.isHollow());
        assertNotNull("Value should have an updater", valueDescriptor.getUpdater());
    }

    public void testIsSimpleForPrimitives() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(true);
        introspector.getConfiguration().setAttributesForPrimitives(false);
        XMLBeanInfo out = introspector.introspect(PhoneNumberBean.class);

        // the bean is mapped to a complex type structure and so should not be simple
        ElementDescriptor phoneNumberDescriptor = out.getElementDescriptor();

        assertFalse("Phone number descriptor is complex", phoneNumberDescriptor.isSimple());

        ElementDescriptor[] phoneNumberChildDescriptors = phoneNumberDescriptor.getElementDescriptors();
        assertEquals("Expected three child elements", 3, phoneNumberChildDescriptors.length);

        // all children should be simple
        assertTrue("Descriptor " + phoneNumberChildDescriptors[0] + " should be simple",
                   phoneNumberChildDescriptors[0].isSimple());
        assertTrue("Descriptor " + phoneNumberChildDescriptors[1] + " should be simple",
                   phoneNumberChildDescriptors[1].isSimple());
        assertTrue("Descriptor " + phoneNumberChildDescriptors[2] + " should be simple",
                   phoneNumberChildDescriptors[2].isSimple());
    }

    public void testSimpleForRSS() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(true);
        introspector.getConfiguration().setAttributesForPrimitives(false);
        XMLBeanInfo out = introspector.introspect(Channel.class);

        ElementDescriptor channelDescriptor = out.getElementDescriptor();
        ElementDescriptor[] childNodesOfRSS = channelDescriptor.getElementDescriptors();
        assertEquals("RSS has only one child, channel", 1, childNodesOfRSS.length);
        ElementDescriptor[] childNodesOfChannel = childNodesOfRSS[0].getElementDescriptors();

        boolean matched = false;
        for (int i=0, size=childNodesOfChannel.length; i<size; i++)
        {
            if ("item".equals(childNodesOfChannel[i].getLocalName()))
            {
                matched = true;
            }
        }
        assertTrue("Local element named item", matched);

        for (int i=0, size=childNodesOfChannel.length; i<size; i++)
        {
            if ("title".equals(childNodesOfChannel[i].getLocalName()))
            {
                assertFalse("Title is not hollow", childNodesOfChannel[i].isHollow());
            }
            else if ("item".equals(childNodesOfChannel[i].getLocalName()))
            {
                assertTrue("Item is hollow", childNodesOfChannel[i].isHollow());
            }
            else if ("textinput".equals(childNodesOfChannel[i].getLocalName()))
            {
                assertTrue("TextInput is hollow", childNodesOfChannel[i].isHollow());
            }
            else if ("skipDays".equals(childNodesOfChannel[i].getLocalName()))
            {
                assertFalse("skipDays is not hollow", childNodesOfChannel[i].isHollow());
                assertFalse("day is not hollow", childNodesOfChannel[i].getElementDescriptors()[0].isHollow());
            }
            else if ("skipHours".equals(childNodesOfChannel[i].getLocalName()))
            {
                assertFalse("skipHours is not hollow", childNodesOfChannel[i].isHollow());
                assertFalse("hour is not hollow", childNodesOfChannel[i].getElementDescriptors()[0].isHollow());
            }
        }
    }

    /** Tests for setting for map with a simple key */
    public void testMapWithSimpleKey() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(false);
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo out = introspector.introspect(AddressBook.class);

        ElementDescriptor formatterDescriptor = out.getElementDescriptor();
        ElementDescriptor[] formatterChildDescriptors = formatterDescriptor.getElementDescriptors();

        assertEquals("Two top level children", 2, formatterChildDescriptors.length);

        ElementDescriptor entryDescriptor = formatterChildDescriptors[0];
        assertEquals("Must be called entry", "entry" , entryDescriptor.getLocalName());
        assertFalse("Is not hollow",  entryDescriptor.isHollow());
        assertNull("No updater for entry spacer",  entryDescriptor.getUpdater());

        ElementDescriptor[] entryChildDesciptors = entryDescriptor.getElementDescriptors();
        assertEquals("Entry has two children", 2, entryChildDesciptors.length);

        ElementDescriptor keyDescriptor = entryChildDesciptors[0];
        assertEquals("Must be called key", "key", keyDescriptor.getLocalName());
        assertFalse("Is simple therefore not hollow",  keyDescriptor.isHollow());
        assertNotNull("Key should have an updater", keyDescriptor.getUpdater());

        ElementDescriptor valueDescriptor = entryChildDesciptors[1];
        assertEquals("Must be called key", "value", valueDescriptor.getLocalName());
        assertTrue("Is not simple therefore hollow",  valueDescriptor.isHollow());
        assertNotNull("Value should have an updater", valueDescriptor.getUpdater());
    }

    /** Tests introspector of map with simple entries */
    public void testMapWithSimpleEntry() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(false);
        introspector.getConfiguration().setAttributesForPrimitives(true);
        XMLBeanInfo out = introspector.introspect(AddressBook.class);

        ElementDescriptor formatterDescriptor = out.getElementDescriptor();
        ElementDescriptor[] formatterChildDescriptors = formatterDescriptor.getElementDescriptors();

        assertEquals("Two top level children", 2, formatterChildDescriptors.length);

        ElementDescriptor entryDescriptor = formatterChildDescriptors[1];
        assertEquals("Must be called entry", "entry" , entryDescriptor.getLocalName());
        assertFalse("Is not hollow",  entryDescriptor.isHollow());
        assertNull("No updater for entry spacer",  entryDescriptor.getUpdater());

        ElementDescriptor[] entryChildDesciptors = entryDescriptor.getElementDescriptors();
        assertEquals("Entry has two children", 2, entryChildDesciptors.length);

        ElementDescriptor keyDescriptor = entryChildDesciptors[0];
        assertEquals("Must be called key", "key", keyDescriptor.getLocalName());
        assertTrue("Is not simple therefore hollow",  keyDescriptor.isHollow());
        assertNotNull("Key should have an updater", keyDescriptor.getUpdater());

        ElementDescriptor valueDescriptor = entryChildDesciptors[1];
        assertEquals("Must be called key", "value", valueDescriptor.getLocalName());
        assertFalse("Is simple therefore not hollow",  valueDescriptor.isHollow());
        assertNotNull("Value should have an updater", valueDescriptor.getUpdater());
    }

    public void testConcreteMapNoWrap() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(false);
        XMLBeanInfo beanInfo = introspector.introspect(BeanWithConcreteMap.class);
        ElementDescriptor beanDescriptor = beanInfo.getElementDescriptor();

        ElementDescriptor[] beanChildDescriptors = beanDescriptor.getElementDescriptors();
        assertEquals("One Entry element", 1, beanChildDescriptors.length);

        ElementDescriptor entry = beanChildDescriptors[0];
        ElementDescriptor[] entryChildren = entry.getElementDescriptors();
        assertEquals("Expected key and entry elements", 2 , entryChildren.length);
    }

    public void testConcreteMapWithWrap() throws Exception
    {
        XMLIntrospector introspector = new XMLIntrospector();
        introspector.getConfiguration().setWrapCollectionsInElement(true);
        XMLBeanInfo beanInfo = introspector.introspect(BeanWithConcreteMap.class);

        ElementDescriptor beanDescriptor = beanInfo.getElementDescriptor();

        ElementDescriptor[] beanChildDescriptors = beanDescriptor.getElementDescriptors();
        assertEquals("One wrapper element", 1, beanChildDescriptors.length);

        ElementDescriptor wrapper = beanChildDescriptors[0];
        ElementDescriptor[] wrapperChildren = wrapper.getElementDescriptors();
        assertEquals("One Entry element", 1, wrapperChildren.length);

        ElementDescriptor entry = wrapperChildren[0];
        ElementDescriptor[] entryChildren = entry.getElementDescriptors();
        assertEquals("Expected key and entry elements", 2 , entryChildren.length);


    }
}
