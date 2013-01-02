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



/**
 * A name mapper which converts types to a hypenated String. So
 * a bean type of FooBar will be converted to the element name "foo-bar".
 * The name mapper can be configured to convert to upper case and to
 * use a different separator via the <code>separator</code> and
 * <code>upperCase</code> properties, so that FooBar can be converted
 * to FOO_BAR if needed, by calling the constructor
 * <code>new HyphenatedNameMapper(true, "_")</code>.
 *
 * @author <a href="mailto:jason@zenplex.com">Jason van Zyl</a>
 * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 * @version $Revision: 471234 $
 */
public class HyphenatedNameMapper implements NameMapper
{

    /** the separator used to seperate words, which defaults to '-' */
    private String separator = "-";

    /** whether upper or lower case conversions should be performed */
    private boolean upperCase = false;

    /**
     * Construct a hyphenated name mapper that converts the name to lower case
     * and uses the default separator.
     */
    public HyphenatedNameMapper()
    {
    }

    /**
     * Construct a hyphenated name mapper with default separator.
     *
     * @param upperCase should the type name be converted (entirely) to upper case
     */
    public HyphenatedNameMapper(boolean upperCase)
    {
        this.upperCase = upperCase;
    }

    /**
     * Construct a hyphenated name mapper.
     *
     * @param upperCase should the type name be converted (entirely) to upper case
     * @param separator use this string to separate the words in the name returned.
     * The words in the bean name are deduced by relying on the standard camel's hump
     * property naming convention.
     */
    public HyphenatedNameMapper(boolean upperCase, String separator)
    {
        this.upperCase = upperCase;
        this.separator = separator;
    }

    /**
     * <p>The words within the bean name are deduced assuming the
     * first-letter-capital (for example camel's hump) naming convention. For
     * example, the words in <code>FooBar</code> are <code>foo</code>
     * and <code>bar</code>.</p>
     *
     * <p>Next convert all letter in the bean name to either upper case or lower case
     * based on the {@link #isUpperCase} property value.</p>
     *
     * <p>Then the {@link #getSeparator} property value is inserted so that it separates
     * each word.</p>
     *
     * @param typeName The name string to convert.  If a JavaBean
     * class name, should included only the last part of the name
     * rather than the fully qualified name (e.g. FooBar rather than
     * org.example.FooBar).
     * @return the bean name converted to either upper or lower case with words separated
     * by the separator.
     */
    public String mapTypeToElementName(String typeName)
    {

        int length = typeName.length();
        if (length == 0)
        {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        sb.append(convertChar(typeName.charAt(0)));

        for (int i = 1; i < length; i++)
        {
            if (Character.isUpperCase(typeName.charAt(i)))
            {
                sb.append(separator);
                sb.append(convertChar(typeName.charAt(i)));
            }
            else
            {
                if ( upperCase )
                {
                    sb.append(convertChar(typeName.charAt(i)));
                }
                else
                {
                    sb.append(typeName.charAt(i));
                }
            }
        }

        return sb.toString();
    }

    // Properties
    //-------------------------------------------------------------------------
    /**
     * This separator will be inserted between the words in the bean name.
     *
     * @return the separator used to seperate words, which defaults to '-'
     */
    public String getSeparator()
    {
        return separator;
    }

    /**
     * Sets the separator used to seperate words, which defaults to '-'
     *
     * @param separator the string inserted to separate words
     */
    public void setSeparator(String separator)
    {
        this.separator = separator;
    }

    /**
     * <p>Should the bean name be converted to upper case?
     * </p>
     * <p>
     * Otherwise, it will be converted to lower case.
     * </p>
     * @return whether upper or lower case conversions should be performed,
     * which defaults to false for lower case
     */
    public boolean isUpperCase()
    {
        return upperCase;
    }

    /**
     * Sets whether upper or lower case conversions should be performed,
     * which defaults to false for lower case.
     *
     * @param upperCase whether the name is to be converted to upper case
     */
    public void setUpperCase(boolean upperCase)
    {
        this.upperCase = upperCase;
    }

    // Implementation methods
    //-------------------------------------------------------------------------

    /**
     * Performs type conversion on the given character based on whether
     * upper or lower case conversions are being used
     *
     * @param ch the character to be converted
     * @return converted to upper case if {@link #isUpperCase} otherwise to lower case
     */
    protected char convertChar(char ch)
    {
        if ( upperCase )
        {
            return Character.toUpperCase(ch);

        }
        else
        {
            return Character.toLowerCase(ch);
        }
    }

    /**
     * Outputs brief description.
     * @since 0.8
     */
    public String toString()
    {
        return "Hyphenated Name Mapper";
    }
}
