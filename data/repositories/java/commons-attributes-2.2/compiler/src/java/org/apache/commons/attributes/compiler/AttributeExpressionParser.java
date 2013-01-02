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
package org.apache.commons.attributes.compiler;

import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;

/**
 * Parser for attribute expressions.
 */
public class AttributeExpressionParser
{

    public static class Argument
    {
        public final String field;
        public final String text;
        public final int length;

        public Argument (String field, String text, int length)
        {
            this.field = field;
            this.text = text;
            this.length = length;
        }

        public boolean equalsOrNull (String a, String b)
        {
            if (a == null)
            {
                return b == null;
            }
            else
            {
                return b != null && a.equals (b);
            }
        }

        public boolean equals (Object o)
        {
            return o instanceof Argument &&
                   equalsOrNull (field, ((Argument) o).field) &&
                   ((Argument) o).text.equals (text);
        }

        public String toString ()
        {
            return "[Argument: " + field + ", " + text + ", " + length + "]";
        }
    }

    public static class ParseResult
    {
        public final List arguments = new ArrayList ();
        public final String className;

        public ParseResult (String className)
        {
            this.className = className;
        }

        public boolean equals (Object o)
        {
            return o instanceof ParseResult &&
                   className.equals (((ParseResult) o).className) &&
                   arguments.equals (((ParseResult) o).arguments);
        }

        public String toString ()
        {
            return "[ParseResult: " + className + ", " + arguments + "]";
        }
    }

    protected static Argument nextArgument (String string, int startPos, String filename, int line)
    {
        if (string.charAt (startPos) == ')')
        {
            return null;
        }

        StringBuffer sb = new StringBuffer ();

        int i = startPos;
        int depth = 0;
        while (!( (string.charAt (i) == ',' || string.charAt (i) == ')') && depth == 0))
        {
            switch (string.charAt (i))
            {
            case '[':
            case '{':
            case '(':
                depth++;
                break;

            case ']':
            case '}':
            case ')':
                depth--;
                break;

            case '\'':
            case '"':
            {
                // handle string literals
                char endChar = string.charAt (i);
                sb.append (string.charAt (i));
                i++;
                while (true)
                {
                    char ch = string.charAt (i);
                    if (ch == '\\')
                    {
                        sb.append (ch);
                        i++;
                        ch = string.charAt (i);
                        sb.append (ch);
                    }
                    else
                    {
                        if (ch == endChar)
                        {
                            break;
                        }
                        sb.append (ch);
                    }
                    i++;
                }
            }
            }

            sb.append (string.charAt (i));
            i++;
            if (i == string.length ())
            {
                throw new BuildException (filename + ":" + line + ": Unterminated argument: " + string);
            }
        }

        if (string.charAt (i) == ',')
        {
            i++;
        }

        String text = sb.toString ();
        String field = null;
        int eqPos = text.indexOf ('=');
        if (eqPos > -1)
        {
            boolean identifier = true;
            for (int j = 0; j < eqPos; j++)
            {
                char ch = text.charAt (j);

                if (Character.isJavaIdentifierPart (ch) || ch == ' ')
                {
                }
                else
                {
                    identifier = false;
                }
            }

            if (identifier)
            {
                field = text.substring (0, eqPos).trim ();
                text = text.substring (eqPos + 1).trim ();
            }
        }

        Argument arg = new Argument (field, text, i - startPos);

        return arg;
    }

    public static ParseResult parse (String string, String filename, int line)
    {
        StringBuffer sb = new StringBuffer ();
        int i = 0;
        while (i < string.length () && (Character.isJavaIdentifierPart (string.charAt (i)) || string.charAt (i) == '.' || string.charAt (i) == ' '))
        {
            sb.append (string.charAt (i));
            i++;
        }

        if (i == string.length () || string.charAt (i) != '(')
        {
            throw new BuildException (filename + ":" + line + ": Illegal expression: " + string);
        }

        ParseResult result = new ParseResult (sb.toString ());

        i++;
        Argument arg = null;
        boolean seenField = false;
        while ((arg = nextArgument (string, i, filename, line)) != null)
        {
            if (arg.field != null)
            {
                seenField = true;
            }

            if (seenField && arg.field == null)
            {
                throw new BuildException (filename + ":" + line + ": Un-named parameters must come before the named parameters: " + string);
            }

            result.arguments.add (arg);
            i += arg.length;
        }

        return result;
    }
}
