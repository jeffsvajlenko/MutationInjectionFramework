/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.transaction.memory.jca;

import java.util.Map;

import javax.resource.ResourceException;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionMetaData;
import javax.resource.cci.Interaction;
import javax.resource.cci.LocalTransaction;
import javax.resource.cci.ResultSetInfo;
import javax.resource.spi.ManagedConnection;

/**
 * @version $Id: MapConnection.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class MapConnection implements Connection
{

    protected MapManagedConnection mc;

    public MapConnection(ManagedConnection mc)
    {
        this.mc = (MapManagedConnection) mc;
    }

    public Map getMap()
    {
        return mc.map;
    }

    public void close() throws ResourceException
    {
        mc.close();
    }

    public Interaction createInteraction() throws ResourceException
    {
        return null;
    }

    public LocalTransaction getLocalTransaction() throws ResourceException
    {
        return (LocalTransaction)mc.getLocalTransaction();
    }

    public ConnectionMetaData getMetaData() throws ResourceException
    {
        return null;
    }

    public ResultSetInfo getResultSetInfo() throws ResourceException
    {
        return null;
    }

    void invalidate()
    {
        mc = null;
    }

}
