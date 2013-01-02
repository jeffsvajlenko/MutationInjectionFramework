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
package org.apache.commons.betwixt.io.read;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class DogBean extends Animal
{

    private boolean pedigree;
    private String breed;
    private String name;

    public DogBean() {}

    public DogBean(String name)
    {
        this(false, "mongrol", name);
    }

    public DogBean(boolean pedigree, String breed, String name)
    {
        this.pedigree = pedigree;
        this.breed = breed;
        this.name = name;
    }

    public String getCall()
    {
        return "Woof";
    }

    public String getBreed()
    {
        return breed;
    }

    public void setBreed(String breed)
    {
        this.breed = breed;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isPedigree()
    {
        return pedigree;
    }

    public void setPedigree(boolean pedigree)
    {
        this.pedigree = pedigree;
    }

    public String getLatinName()
    {
        return "Canis familiaris";
    }
}
