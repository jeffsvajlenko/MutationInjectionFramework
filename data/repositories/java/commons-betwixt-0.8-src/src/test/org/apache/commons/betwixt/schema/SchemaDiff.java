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


package org.apache.commons.betwixt.schema;

import java.io.PrintStream;
import java.util.Iterator;

/**
 * Helper class that prints differences between schema object models.
 * Useful for debugging.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class SchemaDiff
{

    private PrintStream out;

    public SchemaDiff()
    {
        this(System.err);
    }

    public SchemaDiff(PrintStream out)
    {
        this.out = out;
    }

    public void printDifferences(Schema one, Schema two)
    {
        for( Iterator it=one.getComplexTypes().iterator(); it.hasNext(); )
        {
            GlobalComplexType complexType = (GlobalComplexType)it.next();
            if (!two.getComplexTypes().contains(complexType))
            {
                boolean matched = false;
                for (Iterator otherIter=two.getComplexTypes().iterator(); it.hasNext();)
                {
                    GlobalComplexType otherType = (GlobalComplexType) otherIter.next();
                    if (otherType.getName().equals(complexType.getName()))
                    {
                        printDifferences(complexType, otherType);
                        matched = true;
                        break;
                    }
                }
                if (!matched)
                {
                    out.println("Missing Complex type: " + complexType);
                }
            }
        }

    }

    public void printDifferences(GlobalComplexType one, GlobalComplexType two)
    {
        out.println("Type " + one + " is not equal to " + two);
        for (Iterator it = one.getElements().iterator(); it.hasNext();)
        {
            Element elementOne = (Element) it.next();
            if (!two.getElements().contains(elementOne))
            {
                boolean matched = false;
                for (Iterator otherIter=two.getElements().iterator(); it.hasNext();)
                {
                    Element elementTwo = (Element) otherIter.next();
                    if (elementOne.getName().equals(elementTwo.getName()))
                    {
                        printDifferences(elementOne, elementTwo);
                        matched = true;
                        break;
                    }
                }
                if (!matched)
                {
                    out.println("Missing Element: " + elementOne);
                }
            }
        }
        for (Iterator it = one.getAttributes().iterator(); it.hasNext();)
        {
            Attribute attributeOne = (Attribute) it.next();
            if (!two.getAttributes().contains(attributeOne))
            {
                boolean matched = false;
                for (Iterator otherIter=two.getAttributes().iterator(); it.hasNext();)
                {
                    Attribute attributeTwo = (Attribute) otherIter.next();
                    if (attributeTwo.getName().equals(attributeTwo.getName()))
                    {
                        printDifferences(attributeOne, attributeTwo);
                        matched = true;
                        break;
                    }
                }
                if (!matched)
                {
                    out.println("Missing Attribute: " + attributeOne);
                }
            }
        }
    }

    private void printDifferences(Attribute one , Attribute two)
    {
        out.println("Attribute " + one + " is not equals to " + two);
    }

    private void printDifferences(Element one , Element two)
    {
        out.println("Element " + one + " is not equals to " + two);
    }
}
