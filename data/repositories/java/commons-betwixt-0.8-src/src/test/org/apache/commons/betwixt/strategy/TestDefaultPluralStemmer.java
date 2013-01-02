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

package org.apache.commons.betwixt.strategy;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.betwixt.ElementDescriptor;

/**
 * Tests the defaultPluralStemmer
 *
 * @author <a href="mailto:martin@mvdb.net">Martin van den Bemt</a>
 * @version $Id: TestDefaultPluralStemmer.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class TestDefaultPluralStemmer extends TestCase
{

    public static Test suite()
    {
        return new TestSuite(TestDefaultPluralStemmer.class);
    }

    public TestDefaultPluralStemmer(String testName)
    {
        super(testName);
    }

    public void testNullMap()
    {
        DefaultPluralStemmer stemmer = new DefaultPluralStemmer();
        try
        {
            stemmer.findPluralDescriptor("test", null);
            fail("Should throw a nullpointer exception, since the map in the stemmer cannot be null");
        }
        catch(NullPointerException npe)
        {
        }
    }

    /**
     * This is the first match when calling the defaultStemmer.
     * It just adds an s to the the property and it should find it..
     */
    public void testFirstMatch()
    {

        ElementDescriptor des = new ElementDescriptor();
        des.setQualifiedName("FooBars");
        des.setPropertyType(java.util.List.class);
        HashMap map = new HashMap();
        map.put("FooBars", des);
        DefaultPluralStemmer dps = new DefaultPluralStemmer();
        ElementDescriptor result = dps.findPluralDescriptor("FooBar", map);
        assertEquals(des, result);
    }
    /**
     * Tests if the y is nicely replaces with ies and the correct
     * ElementDescriptor is returned
     */
    public void testSecondMatch()
    {
        ElementDescriptor des = new ElementDescriptor();
        des.setQualifiedName("FooBary");
        des.setPropertyType(java.util.List.class);
        HashMap map = new HashMap();
        map.put("FooBaries", des);
        DefaultPluralStemmer dps = new DefaultPluralStemmer();
        ElementDescriptor result = dps.findPluralDescriptor("FooBary", map);
        assertEquals(des, result);
    }

    /**
     * Tests if it actually skips the y if the length not greater than 1.
     */
    public void testSecondNonMatch()
    {
        ElementDescriptor des = new ElementDescriptor();
        des.setQualifiedName("y");
        des.setPropertyType(java.util.List.class);
        HashMap map = new HashMap();
        map.put("yies", des);
        DefaultPluralStemmer dps = new DefaultPluralStemmer();
        ElementDescriptor result = dps.findPluralDescriptor("y", map);
        assertNotNull(result);
    }

    /**
     * Uses the third if in pluralstemmer.
     * It should return the specified y, without any changing.
     */
    public void testThirdMatch()
    {
        ElementDescriptor des = new ElementDescriptor();
        des.setQualifiedName("y");
        des.setPropertyType(java.util.List.class);
        HashMap map = new HashMap();
        map.put("y", des);
        DefaultPluralStemmer dps = new DefaultPluralStemmer();
        ElementDescriptor result = dps.findPluralDescriptor("y", map);
        assertEquals(des, result);
    }

    /**
     * Tests to see if you get warned when there are multiple matches
     * found
     */
    public void testMultipleMatches()
    {
        ElementDescriptor des = new ElementDescriptor();
        des.setQualifiedName("y");
        des.setPropertyType(java.util.List.class);
        ElementDescriptor desyes = new ElementDescriptor();
        desyes.setQualifiedName("yes");
        desyes.setPropertyType(java.util.List.class);
        ElementDescriptor desyesno = new ElementDescriptor();
        desyesno.setQualifiedName("yesno");
        desyesno.setPropertyType(java.util.List.class);
        HashMap map = new HashMap();
        map.put("y", des);
        map.put("yes", desyes);
        map.put("yesno", desyesno);
        DefaultPluralStemmer dps = new DefaultPluralStemmer();
        ElementDescriptor result = dps.findPluralDescriptor("y", map);
        assertEquals(des, result);
        result = dps.findPluralDescriptor("yes", map);
        assertEquals(desyes, result);
        result = dps.findPluralDescriptor("yesno", map);
        assertEquals(desyesno, result);
    }

    /**
     *  Test to find matched where plural ending is "es"
     */
    public void testESPluralEndingMatch()
    {
        HashMap map = new HashMap();

        ElementDescriptor index = new ElementDescriptor("index", "index","");
        map.put("index", index);
        ElementDescriptor indexes = new ElementDescriptor("indexes", "indexes","");
        map.put("indexes", indexes);

        ElementDescriptor patch = new ElementDescriptor("patch", "patch","");
        map.put("patch", patch);
        ElementDescriptor patches = new ElementDescriptor("patches", "patches","");
        map.put("patches", patches);

        DefaultPluralStemmer stemmer = new DefaultPluralStemmer();
        ElementDescriptor result = stemmer.findPluralDescriptor("index", map);
        assertEquals(indexes, result);

        result = stemmer.findPluralDescriptor("patches", map);
        assertEquals(patches, result);
    }

    /**
     *  Test if the closest match mechanisme is working
     */
    public void testClosestMatch()
    {
        HashMap map = new HashMap();
        ElementDescriptor yes1 = new ElementDescriptor("yes1", "yes1","");
        map.put("yes1", yes1);
        ElementDescriptor yes12 = new ElementDescriptor("yes12", "yes12","");
        map.put("yes12", yes12);
        ElementDescriptor yes123 = new ElementDescriptor("yes123", "yes123","");
        map.put("yes123", yes123);
        DefaultPluralStemmer stemmer = new DefaultPluralStemmer();
        ElementDescriptor result = stemmer.findPluralDescriptor("yes", map);
        assertEquals(yes1, result);
    }

}

