package org.apache.commons.betwixt.scarab;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p><code>ScarabSettings</code> is a sample bean for use by the test cases.</p>
 *
 * @author <a href="mailto:jason@zenplex.com">Jason van Zyl</a>
 * @version $Id: ScarabSettings.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class ScarabSettings implements Serializable
{

    /**
     * Logger
     */
    private final static Log log = LogFactory.getLog(ScarabSettings.class);

    private List globalAttributes;

    private List modules;

    private List globalIssueTypes;

    /**
     * Constructor for the ScarabSettings object
     */
    public ScarabSettings()
    {
        globalAttributes = new ArrayList();
        modules = new ArrayList();
        globalIssueTypes = new ArrayList();
    }

    public List getGlobalAttributes()
    {
        return globalAttributes;
    }

    public void addGlobalAttribute(GlobalAttribute globalAttribute)
    {
        // adds an assertion that the name must be populated first
        // as an extra test case
        if (globalAttribute.getName() == null)
        {
            throw new AssertionFailedError("Cannot add a new GlobalAttribute that has no name: " + globalAttribute);
        }
        globalAttributes.add(globalAttribute);
    }

    public List getGlobalIssueTypes()
    {
        return globalIssueTypes;
    }

    public void addGlobalIssueType(GlobalIssueType globalIssueType)
    {
        globalIssueTypes.add(globalIssueType);
    }

    public List getModules()
    {
        return modules;
    }

    public void addModule(Module module)
    {
        modules.add(module);
    }
}
