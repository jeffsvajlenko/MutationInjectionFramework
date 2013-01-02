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
 * @author <a href='http://jakarta.apache.org/'>Jakarta Commons Team</a>
 * @version $Revision: 438373 $
 */
public class ComposerBean
{

    private String forename;
    private String surname;
    private int born;

    public ComposerBean() {}

    public ComposerBean(String forename, String surname, int born)
    {
        setForename(forename);
        setSurname(surname);
        setBorn(born);
    }

    public int getBorn()
    {
        return born;
    }

    public String getForename()
    {
        return forename;
    }


    public String getSurname()
    {
        return surname;
    }

    public void setBorn(int i)
    {
        born = i;
    }

    public void setForename(String string)
    {
        forename = string;
    }


    public void setSurname(String string)
    {
        surname = string;
    }

    public int hashCode()
    {
        return born;
    }

    public boolean equals(Object obj)
    {
        boolean result = false;
        if (obj instanceof ComposerBean)
        {
            ComposerBean composer = (ComposerBean) obj;
            result =
                born == composer.born &&
                surname.equals(composer.surname) &&
                forename.equals(composer.forename);
        }
        return result;
    }


}
