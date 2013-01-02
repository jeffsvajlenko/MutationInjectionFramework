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

import junit.framework.TestCase;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestOptions extends TestCase
{

    private static final int A_SHIFT = 1 << 0;
    private static final int B_SHIFT = 1 << 1;
    private static final int C_SHIFT = 1 << 2;


    public void testGetValue()
    {
        Options options = new Options();
        options.addOption("A", "Alpha");
        options.addOption("B", "Beta");

        assertEquals("Get added value", "Alpha", options.getValue("A"));
        assertNull("Value not added is null", options.getValue("C"));

        options.addOption("A", "New Value");
        assertEquals("Lat value set wins", "New Value", options.getValue("A"));
    }

    public void testGetNames()
    {
        Options options = new Options();
        options.addOption("A", "Alpha");
        options.addOption("B", "Beta");
        options.addOption("C", "Gamma");

        String[] names = options.getNames();
        assertEquals("Expected three names", 3, names.length);
        int flag = (A_SHIFT) + (B_SHIFT) + (C_SHIFT);
        for ( int i = 0; i <3 ; i++ )
        {
            if (names[i].equals("A"))
            {
                assertTrue("A named twice", (flag & (A_SHIFT))>0);
                flag -= (A_SHIFT);
            }
            else if (names[i].equals("B"))
            {
                assertTrue("B named twice", (flag & (B_SHIFT))>0);
                flag -= (B_SHIFT);
            }
            else if (names[i].equals("C"))
            {
                assertTrue("C named twice", (flag & (C_SHIFT))>0);
                flag -= (C_SHIFT);
            }
            else
            {
                fail("Unexpected name: " + names[i]);
            }
        }
    }

    public void testAddOptions()
    {
        Options a = new Options();
        a.addOption("A", "Alpha");
        a.addOption("B", "Beta");
        a.addOption("C", "Gamma");

        Options b = new Options();
        b.addOption("A", "Apple");
        b.addOption("C", "Carrot");
        b.addOption("E", "Egg Plant");

        a.addOptions(b);

        // Lat value set wins
        assertEquals("Apple",a.getValue("A"));
        assertEquals("Beta",a.getValue("B"));
        assertEquals("Carrot",a.getValue("C"));
        assertEquals("Egg Plant",a.getValue("E"));
    }

}
