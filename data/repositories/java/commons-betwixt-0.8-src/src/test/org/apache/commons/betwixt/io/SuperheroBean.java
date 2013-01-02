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
package org.apache.commons.betwixt.io;

/**
 */
public class SuperheroBean
{

    private String moniker;
    private SidekickBean sidekick;

    public SuperheroBean()
    {
        super();
    }

    public SuperheroBean(String moniker, SidekickBean sidekick)
    {
        super();
        this.moniker = moniker;
        this.sidekick = sidekick;
    }

    public SuperheroBean(SidekickBean sidekick)
    {
        super();
        this.sidekick = sidekick;
    }

    public String getMoniker()
    {
        return moniker;
    }

    public void setMoniker(String moniker)
    {
        this.moniker = moniker;
    }

    public SidekickBean getSidekick()
    {
        return sidekick;
    }

    public void setSidekick(SidekickBean sidekick)
    {
        this.sidekick = sidekick;
    }
}
