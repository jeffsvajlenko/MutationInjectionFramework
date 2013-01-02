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
package org.apache.commons.betwixt.derived;

/**
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class BeanWithSecrets
{

    private String job;
    private String employer;
    private String name;
    private String secretCodeName;
    private String secretEmployer;

    public BeanWithSecrets() {}

    public BeanWithSecrets(String job, String employer, String name,
                           String secretCodeName, String secretEmployer)
    {
        super();
        this.job = job;
        this.employer = employer;
        this.name = name;
        this.secretCodeName = secretCodeName;
        this.secretEmployer = secretEmployer;
    }
    public String getEmployer()
    {
        return employer;
    }

    public void setEmployer(String employer)
    {
        this.employer = employer;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSecretCodeName()
    {
        return secretCodeName;
    }

    public void setSecretCodeName(String secretCodeName)
    {
        this.secretCodeName = secretCodeName;
    }

    public String getSecretEmployer()
    {
        return secretEmployer;
    }

    public void setSecretEmployer(String secretEmployer)
    {
        this.secretEmployer = secretEmployer;
    }
}
