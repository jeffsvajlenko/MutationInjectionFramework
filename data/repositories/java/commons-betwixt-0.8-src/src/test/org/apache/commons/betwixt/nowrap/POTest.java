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

package org.apache.commons.betwixt.nowrap;

import java.util.ArrayList;
import java.util.List;


public class POTest
{
    private static final boolean debug = false;

    private List componentTests;

    private String printingNumber = "";

    public POTest()
    {
        if (debug) System.out.println("-- INSTANTIATING NEW PO");
        componentTests = new ArrayList();
    }

    public List getComponenttests()
    {
        if (debug) System.out.println("-- GET PO.getComponents");
        return this.componentTests;
    }

    public void setComponenttests(List componentTests)
    {
    }

    public void addComponenttest(Componenttest c)
    {
        if (debug) System.out.println("-- ADD PO.addComponent");
        componentTests.add(c);
    }

    public void setPrintingNumber(String s)
    {
        if (debug) System.out.println("-- SET PO.setPrintingNumber");
        printingNumber = s;
    }

    public String getPrintingNumber()
    {
        if (debug) System.out.println("-- GET PO.getPrintingNumber");
        return printingNumber;
    }
}
