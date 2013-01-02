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

package org.apache.commons.betwixt.digester;

import junit.framework.TestCase;

import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.Rule;
import org.xml.sax.helpers.AttributesImpl;



/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class TestOptionDigestion extends TestCase
{

    private Digester digester;
    private OptionRule optionRule;
    private Rule nameRule;
    private Rule valueRule;
    private ElementDescriptor elementDescriptor;

    protected void setUp() throws Exception
    {
        super.setUp();
        elementDescriptor = new ElementDescriptor();
        digester = new Digester();
        digester.push(elementDescriptor);
        optionRule = new OptionRule();
        optionRule.setDigester(digester);
        nameRule = optionRule.getNameRule();
        valueRule = optionRule.getValueRule();
    }

    public void testGoodDigestion() throws Exception
    {

        optionRule.begin("","option", new AttributesImpl());
        nameRule.begin("","name", new AttributesImpl());
        nameRule.body("", "name", "one");
        nameRule.end("","name");
        valueRule.begin("","value", new AttributesImpl());
        valueRule.body("", "value", "ONE");
        valueRule.end("","value");
        optionRule.end("","option");

        assertEquals("Option set", "ONE", elementDescriptor.getOptions().getValue("one"));
    }


    public void testTwoDigestions() throws Exception
    {

        optionRule.begin("","option", new AttributesImpl());
        nameRule.begin("","name", new AttributesImpl());
        nameRule.body("", "name", "one");
        nameRule.end("","name");
        valueRule.begin("","value", new AttributesImpl());
        valueRule.body("", "value", "ONE");
        valueRule.end("","value");
        optionRule.end("","option");
        optionRule.begin("","option", new AttributesImpl());
        valueRule.begin("","value", new AttributesImpl());
        valueRule.body("", "value", "TWO");
        valueRule.end("","value");
        nameRule.begin("","name", new AttributesImpl());
        nameRule.body("", "name", "two");
        nameRule.end("","name");
        optionRule.end("","option");

        assertEquals("Option set", "ONE", elementDescriptor.getOptions().getValue("one"));
        assertEquals("Option set", "TWO", elementDescriptor.getOptions().getValue("two"));

    }


    public void testGracefulBadMapping() throws Exception
    {

        optionRule.begin("","option", new AttributesImpl());
        nameRule.begin("","name", new AttributesImpl());
        nameRule.body("", "name", "one");
        nameRule.end("","name");
        optionRule.end("","option");
        optionRule.begin("","option", new AttributesImpl());
        valueRule.begin("","value", new AttributesImpl());
        valueRule.body("", "value", "ONE");
        valueRule.end("","value");
        optionRule.end("","option");
        optionRule.begin("","option", new AttributesImpl());
        nameRule.begin("","name", new AttributesImpl());
        nameRule.body("", "name", "two");
        nameRule.end("","name");
        valueRule.begin("","value", new AttributesImpl());
        valueRule.body("", "value", "TWO");
        valueRule.end("","value");
        optionRule.end("","option");

        assertEquals("Option set", null, elementDescriptor.getOptions().getValue("one"));
        assertEquals("Option set", "TWO", elementDescriptor.getOptions().getValue("two"));

    }
}
