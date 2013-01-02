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
package org.apache.commons.transaction.util;

import java.io.PrintWriter;

/**
 * Logger implementation that logs into a pring writer like the one
 * passed in JCA.
 *
 * @version $Id: PrintWriterLogger.java 493628 2007-01-07 01:42:48Z joerg $
 */
public class PrintWriterLogger implements LoggerFacade
{

    protected PrintWriter printWriter;
    protected String name;
    protected boolean debug;

    public PrintWriterLogger(PrintWriter printWriter, String name, boolean debug)
    {
        this.printWriter = printWriter;
        this.name = name;
        this.debug = debug;
    }

    public LoggerFacade createLogger(String newName)
    {
        return new PrintWriterLogger(this.printWriter, newName, this.debug);
    }

    public void logInfo(String message)
    {
        log("INFO", message);
    }

    public void logFine(String message)
    {
        if (debug)
            log("FINE", message);
    }

    public boolean isFineEnabled()
    {
        return debug;
    }

    public void logFiner(String message)
    {
        if (debug)
            log("FINER", message);
    }

    public boolean isFinerEnabled()
    {
        return debug;
    }

    public void logFinest(String message)
    {
        if (debug)
            log("FINEST", message);
    }

    public boolean isFinestEnabled()
    {
        return debug;
    }

    public void logWarning(String message)
    {
        log("WARNING", message);
    }

    public void logWarning(String message, Throwable t)
    {
        log("WARNING", message);
        t.printStackTrace(printWriter);
    }

    public void logSevere(String message)
    {
        log("SEVERE", message);
    }

    public void logSevere(String message, Throwable t)
    {
        log("SEVERE", message);
        t.printStackTrace(printWriter);
    }

    protected void log(String level, String message)
    {
        printWriter.write(name + "(" + level + ":" + message);
        printWriter.flush();
    }
}
