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
 * Example enum
 *
 * @author Robert Burrell Donkin
 * @version $Id: CompassPoint.java 438373 2006-08-30 05:17:21Z bayard $
 */
public class CompassPoint
{

    public static final CompassPoint NORTH = new CompassPoint("North");
    public static final CompassPoint SOUTH = new CompassPoint("South");
    public static final CompassPoint EAST = new CompassPoint("East");
    public static final CompassPoint WEST = new CompassPoint("West");

    private String name;

    private CompassPoint(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
