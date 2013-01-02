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
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class AllSimplesBean
{

    private String string;
    private BigInteger bigInteger;
    private int primitiveInt;
    private Integer objectInt;
    private long primitiveLong;
    private Long objectLong;
    private short primitiveShort;
    private Short objectShort;
    private BigDecimal bigDecimal;
    private float primitiveFloat;
    private Float objectFloat;
    private double primitiveDouble;
    private Double objectDouble;
    private boolean primitiveBoolean;
    private Boolean objectBoolean;
    private byte primitiveByte;
    private Byte objectByte;
    private java.util.Date utilDate;
    private java.sql.Date sqlDate;
    private java.sql.Time sqlTime;


    public BigDecimal getBigDecimal()
    {
        return bigDecimal;
    }

    public BigInteger getBigInteger()
    {
        return bigInteger;
    }

    public Boolean getObjectBoolean()
    {
        return objectBoolean;
    }

    public Byte getObjectByte()
    {
        return objectByte;
    }

    public Double getObjectDouble()
    {
        return objectDouble;
    }

    public Float getObjectFloat()
    {
        return objectFloat;
    }

    public Integer getObjectInt()
    {
        return objectInt;
    }

    public Long getObjectLong()
    {
        return objectLong;
    }

    public Short getObjectShort()
    {
        return objectShort;
    }

    public boolean isPrimitiveBoolean()
    {
        return primitiveBoolean;
    }

    public byte getPrimitiveByte()
    {
        return primitiveByte;
    }

    public double getPrimitiveDouble()
    {
        return primitiveDouble;
    }

    public float getPrimitiveFloat()
    {
        return primitiveFloat;
    }

    public int getPrimitiveInt()
    {
        return primitiveInt;
    }

    public long getPrimitiveLong()
    {
        return primitiveLong;
    }

    public short getPrimitiveShort()
    {
        return primitiveShort;
    }

    public java.sql.Date getSqlDate()
    {
        return sqlDate;
    }

    public java.sql.Time getSqlTime()
    {
        return sqlTime;
    }

    public String getString()
    {
        return string;
    }

    public java.util.Date getUtilDate()
    {
        return utilDate;
    }

    public void setBigDecimal(BigDecimal decimal)
    {
        bigDecimal = decimal;
    }

    public void setBigInteger(BigInteger integer)
    {
        bigInteger = integer;
    }

    public void setObjectBoolean(Boolean boolean1)
    {
        objectBoolean = boolean1;
    }

    public void setObjectByte(Byte byte1)
    {
        objectByte = byte1;
    }

    public void setObjectDouble(Double double1)
    {
        objectDouble = double1;
    }

    public void setObjectFloat(Float float1)
    {
        objectFloat = float1;
    }

    public void setObjectInt(Integer integer)
    {
        objectInt = integer;
    }

    public void setObjectLong(Long long1)
    {
        objectLong = long1;
    }

    public void setObjectShort(Short short1)
    {
        objectShort = short1;
    }

    public void setPrimitiveBoolean(boolean b)
    {
        primitiveBoolean = b;
    }

    public void setPrimitiveByte(byte b)
    {
        primitiveByte = b;
    }

    public void setPrimitiveDouble(double d)
    {
        primitiveDouble = d;
    }

    public void setPrimitiveFloat(float f)
    {
        primitiveFloat = f;
    }

    public void setPrimitiveInt(int i)
    {
        primitiveInt = i;
    }

    public void setPrimitiveLong(long l)
    {
        primitiveLong = l;
    }

    public void setPrimitiveShort(short s)
    {
        primitiveShort = s;
    }

    public void setSqlDate(java.sql.Date date)
    {
        sqlDate = date;
    }

    public void setSqlTime(java.sql.Time time)
    {
        sqlTime = time;
    }

    public void setString(String string)
    {
        this.string = string;
    }

    public void setUtilDate(java.util.Date date)
    {
        utilDate = date;
    }

}
