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

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Default <code>DataTypeMapper</code>implementation.
 * Provides a reasonably standard and compatible mapping.
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class DefaultDataTypeMapper extends DataTypeMapper
{

    /**
     * This implementation provides
     * @see org.apache.commons.betwixt.schema.DataTypeMapper#toXMLSchemaDataType(java.lang.Class)
     */
    public String toXMLSchemaDataType(Class type)
    {
        // default mapping is to string
        String result = "xsd:string";
        if (String.class.equals(type))
        {
            result = "xsd:string";

        }
        else if (BigInteger.class.equals(type))
        {
            result = "xsd:integer";

        }
        else if (Integer.TYPE.equals(type))
        {
            result = "xsd:int";

        }
        else if (Integer.class.equals(type))
        {
            result = "xsd:int";

        }
        else if (Long.TYPE.equals(type))
        {
            result = "xsd:long";

        }
        else if (Long.class.equals(type))
        {
            result = "xsd:long";

        }
        else if (Short.TYPE.equals(type))
        {
            result = "xsd:short";

        }
        else if (Short.class.equals(type))
        {
            result = "xsd:short";

        }
        else if (BigDecimal.class.equals(type))
        {
            result = "xsd:decimal";

        }
        else if (Float.TYPE.equals(type))
        {
            result = "xsd:float";

        }
        else if (Float.class.equals(type))
        {
            result = "xsd:float";

        }
        else if (Double.TYPE.equals(type))
        {
            result = "xsd:double";

        }
        else if (Double.class.equals(type))
        {
            result = "xsd:double";

        }
        else if (Boolean.TYPE.equals(type))
        {
            result = "xsd:boolean";

        }
        else if (Boolean.class.equals(type))
        {
            result = "xsd:boolean";

        }
        else if (Byte.TYPE.equals(type))
        {
            result = "xsd:byte";

        }
        else if (Byte.class.equals(type))
        {
            result = "xsd:byte";

        }
        else if (java.util.Date.class.equals(type))
        {
            result = "xsd:dateTime";

        }
        else if (java.sql.Date.class.equals(type))
        {
            result = "xsd:date";

        }
        else if (java.sql.Time.class.equals(type))
        {
            result = "xsd:time";
        }

        return result;
    }


}
