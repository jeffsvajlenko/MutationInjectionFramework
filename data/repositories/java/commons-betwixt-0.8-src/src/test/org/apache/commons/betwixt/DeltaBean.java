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

/** <p>A simple bean that demonstrates conversions of primitives and objects.</p>
  *
  * @author Robert Burrell Donkin
  * @version $Revision: 438373 $
  */
public class DeltaBean
{

    private java.sql.Date sqlDate;
    private java.sql.Time sqlTime;
    private java.sql.Timestamp sqlTimestamp;
    private java.util.Date utilDate;
    private String name;
    private Float objFloat;
    private float primitiveFloat;

    public DeltaBean()
    {
    }

    public DeltaBean(
        java.sql.Date sqlDate,
        java.sql.Time sqlTime,
        java.sql.Timestamp sqlTimestamp,
        java.util.Date utilDate,
        String name,
        Float objFloat,
        float primitiveFloat)
    {
        setSqlDate(sqlDate);
        setSqlTime(sqlTime);
        setSqlTimestamp(sqlTimestamp);
        setUtilDate(utilDate);
        setName(name);
        setObjFloat(objFloat);
        setPrimitiveFloat(primitiveFloat);
    }

    public java.sql.Date getSqlDate()
    {
        return sqlDate;
    }

    public void setSqlDate(java.sql.Date sqlDate)
    {
        this.sqlDate = sqlDate;
    }

    public java.sql.Time getSqlTime()
    {
        return sqlTime;
    }

    public void setSqlTime(java.sql.Time sqlTime)
    {
        this.sqlTime = sqlTime;
    }

    public java.sql.Timestamp getSqlTimestamp()
    {
        return sqlTimestamp;
    }

    public void setSqlTimestamp(java.sql.Timestamp sqlTimestamp)
    {
        this.sqlTimestamp = sqlTimestamp;
    }

    public java.util.Date getUtilDate()
    {
        return utilDate;
    }

    public void setUtilDate(java.util.Date utilDate)
    {
        this.utilDate = utilDate;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Float getObjFloat()
    {
        return objFloat;
    }

    public void setObjFloat(Float objFloat)
    {
        this.objFloat = objFloat;
    }

    public float getPrimitiveFloat()
    {
        return primitiveFloat;
    }

    public void setPrimitiveFloat(float primitiveFloat)
    {
        this.primitiveFloat = primitiveFloat;
    }
}
