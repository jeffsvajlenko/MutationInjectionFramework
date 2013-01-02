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

import java.io.PrintWriter;

import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.apache.commons.transaction.memory.TransactionalMapWrapper;
import org.apache.commons.transaction.util.LoggerFacade;
import org.apache.commons.transaction.util.PrintWriterLogger;
import org.apache.commons.transaction.util.xa.AbstractTransactionalResource;
import org.apache.commons.transaction.util.xa.AbstractXAResource;
import org.apache.commons.transaction.util.xa.TransactionalResource;

/**
 * @version $Id: MapXAResource.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class MapXAResource extends AbstractXAResource
{

    TransactionalMapWrapper map;
    LoggerFacade loggerFacade;

    public MapXAResource(TransactionalMapWrapper map)
    {
        this.map = map;
        // log important stuff to standard out as long as nothing else is configured
        this.loggerFacade = new PrintWriterLogger(new PrintWriter(System.out), "WebDAVXAResource", false);
    }

    public MapXAResource(TransactionalMapWrapper map, LoggerFacade loggerFacade)
    {
        this.map = map;
        this.loggerFacade = loggerFacade;
    }

    public int getTransactionTimeout() throws XAException
    {
        return 0;
    }

    public boolean setTransactionTimeout(int seconds) throws XAException
    {
        return false;
    }

    public boolean isSameRM(XAResource xares) throws XAException
    {
        return (xares != null && xares instanceof MapXAResource && map.equals(((MapXAResource) xares).map));
    }

    public Xid[] recover(int flag) throws XAException
    {
        return null;
    }

    public LoggerFacade getLoggerFacade()
    {
        return loggerFacade;
    }

    public void setLoggerFacade(LoggerFacade loggerFacade)
    {
        this.loggerFacade = loggerFacade;
    }

    protected void setLoggerFacade(PrintWriter out)
    {
        loggerFacade = new PrintWriterLogger(out, "WebDAVXAResource", true);
    }

    protected TransactionalResource createTransactionResource(Xid xid) throws Exception
    {
        return new MapTransactionalResource(xid, map, getLoggerFacade());
    }

    protected boolean includeBranchInXid()
    {
        return true;
    }

    protected static class MapTransactionalResource extends AbstractTransactionalResource
    {

        TransactionalMapWrapper map;
        private TransactionalMapWrapper.TxContext txContext = null;

        LoggerFacade loggerFacade;

        public MapTransactionalResource(Xid xid, TransactionalMapWrapper map, LoggerFacade loggerFacade)
        {
            super(xid);
            this.map = map;
            this.loggerFacade = loggerFacade;
        }

        public void commit() throws XAException
        {
            try
            {
                map.commitTransaction();
            }
            catch (IllegalStateException e)
            {
                throw new XAException(e.toString());
            }
        }

        public void rollback() throws XAException
        {
            // resume if suspended, because the transactional map throws an
            // exception if we call prepare on suspended txns
            if (isSuspended())
                resume();

            try
            {
                map.rollbackTransaction();
            }
            catch (IllegalStateException e)
            {
                throw new XAException(e.toString());
            }
        }

        public int prepare() throws XAException
        {
            // resume if suspended, because the transactional map throws an
            // exception if we call prepare on suspended txns
            if (isSuspended())
                resume();

            if (map.isTransactionMarkedForRollback())
            {
                throw new XAException(XAException.XA_RBROLLBACK);
            }

            return (map.isReadOnly() ? XA_RDONLY : XA_OK);
        }

        public void suspend() throws XAException
        {
            if (isSuspended())
            {
                throw new XAException(XAException.XAER_PROTO);
            }
            this.txContext = map.suspendTransaction();
        }

        public void resume() throws XAException
        {
            if (!isSuspended())
            {
                throw new XAException(XAException.XAER_PROTO);
            }
            map.resumeTransaction(this.txContext);
            this.txContext = null;
        }

        public void begin() throws XAException
        {
            if (isSuspended())
            {
                throw new XAException(XAException.XAER_PROTO);
            }
            this.map.startTransaction();
        }

        private boolean isSuspended()
        {
            return this.txContext != null;
        }
    }
}
