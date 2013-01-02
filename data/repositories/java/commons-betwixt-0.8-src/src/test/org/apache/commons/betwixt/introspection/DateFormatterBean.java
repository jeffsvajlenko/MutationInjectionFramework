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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class DateFormatterBean
{

    private Map formatsByLocale = new HashMap();

    //TODO: want to be able to match iterators from maps...
    //      but this mean ammending the descriptors when the property type
    //      is discovered
    public Map getFormats()
    {
        return formatsByLocale;
    }

    //TODO: should be able to use another verb eg register
    //TODO: support for specifying adders in the dot betwixt file
    //TODO: support for Simple types so that we can use Locale -> String
    public void addFormat(Locale locale, SimpleDateFormat format)
    {
        formatsByLocale.put(locale, format);
    }

    public String format(Date date, Locale locale)
    {
        String result = "";
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) formatsByLocale.get(locale);
        if (simpleDateFormat == null)
        {
            result = DateFormat.getDateInstance(DateFormat.SHORT, locale).format(date);
        }
        else
        {
            result = simpleDateFormat.format(date);
        }
        return result;
    }

}
