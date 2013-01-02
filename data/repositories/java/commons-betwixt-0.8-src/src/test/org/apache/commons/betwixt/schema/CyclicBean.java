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

import java.util.Vector;

/**
 * <p> This is a bean specifically designed to test cyclic references.
 * The idea is that there's a count that counts every time <code>getFriend</code>
 * gets called and throws a <code>RuntimeException</code> if the count gets too high.</p>
 *
 * @author <a href='http://jakarta.apache.org/commons'>Jakarta Commons Team</a>, <a href='http://www.apache.org'>Apache Software Foundation</a>
 */
public class CyclicBean
{
    private Vector layers = new Vector(); //CyclicLayer

    private String name;

    public CyclicBean(String name)
    {
        this.name = name;
    }

    public Vector getLayers()
    {
        return this.layers;
    }

    public void setLayers(Vector vLayers)
    {
        this.layers = vLayers;
    }

    public void addLayer(CyclicLayer aLayer)
    {
        layers.add(aLayer);
    }
    public String getName()
    {
        return name;
    }

    public String toString()
    {
        return "[CyclicBean] name=" + name;
    }

}
