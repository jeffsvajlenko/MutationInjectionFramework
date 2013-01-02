/*
 * Copyright 2003-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.attributes.compiler.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.attributes.compiler.AttributeExpressionParser;
import junit.framework.TestCase;

public class AttributeExpressionParserTestCase extends TestCase
{

    private void singleTest (String expression, String className, AttributeExpressionParser.Argument[] args)
    {
        AttributeExpressionParser.ParseResult result = AttributeExpressionParser.parse (expression, "noFile", 1);
        AttributeExpressionParser.ParseResult expected = new AttributeExpressionParser.ParseResult (className);
        for (int i = 0; i < args.length; i++)
        {
            expected.arguments.add (args[i]);
        }

        assertEquals (expected, result);
    }

    public void testExpressions () throws Exception
    {
        singleTest ("Inherited()", "Inherited", new AttributeExpressionParser.Argument[] {});

        singleTest ("AnAttribute(1,2)", "AnAttribute", new AttributeExpressionParser.Argument[]
                    {
                        new AttributeExpressionParser.Argument(null, "1", 0),
                        new AttributeExpressionParser.Argument(null, "2", 0)
                    });

        singleTest ("AnAttribute(\"sometext,1,2\",'a',',')", "AnAttribute", new AttributeExpressionParser.Argument[]
                    {
                        new AttributeExpressionParser.Argument(null, "\"sometext,1,2\"", 0),
                        new AttributeExpressionParser.Argument(null, "'a'", 0),
                        new AttributeExpressionParser.Argument(null, "','", 0)
                    });

        singleTest ("AnAttribute(\"sometext,1,2\",'a',',',alpha='\"',beta=\"\\\'\")", "AnAttribute", new AttributeExpressionParser.Argument[]
                    {
                        new AttributeExpressionParser.Argument(null, "\"sometext,1,2\"", 0),
                        new AttributeExpressionParser.Argument(null, "'a'", 0),
                        new AttributeExpressionParser.Argument(null, "','", 0),
                        new AttributeExpressionParser.Argument("alpha", "'\"'", 0),
                        new AttributeExpressionParser.Argument("beta", "\"\\\'\"", 0)
                    });

        singleTest ("my.package. AnAttribute(\"sometext,1,2\",'a',',',alpha='\"',beta=\"\\\'\")",
                    "my.package. AnAttribute", new AttributeExpressionParser.Argument[]
                    {
                        new AttributeExpressionParser.Argument(null, "\"sometext,1,2\"", 0),
                        new AttributeExpressionParser.Argument(null, "'a'", 0),
                        new AttributeExpressionParser.Argument(null, "','", 0),
                        new AttributeExpressionParser.Argument("alpha", "'\"'", 0),
                        new AttributeExpressionParser.Argument("beta", "\"\\\'\"", 0)
                    });

        /*
        singleTest ("my.package.AnAttribute( \"sometext,1,2\" , 'a' , ',' , alpha='\"', beta=\"\\\'\")",
            "my.package.AnAttribute", new AttributeExpressionParser.Argument[] {
                new AttributeExpressionParser.Argument(null, "\"sometext,1,2\" ", 0),
                new AttributeExpressionParser.Argument(null, "'a'", 0),
                new AttributeExpressionParser.Argument(null, "','", 0),
                new AttributeExpressionParser.Argument("alpha", "'\"'", 0),
                new AttributeExpressionParser.Argument("beta", "\"\\\'\"", 0)
                });
        */

        singleTest ("AnAttribute( \"sometext,1,2\" )",
                    "AnAttribute", new AttributeExpressionParser.Argument[]
                    {
                        new AttributeExpressionParser.Argument(null, " \"sometext,1,2\" ", 0)
                    });
    }

}
