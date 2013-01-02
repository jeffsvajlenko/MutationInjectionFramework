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


/**
 * Facade for all kinds of logging engines.
 *
 * @version $Id: LoggerFacade.java 493628 2007-01-07 01:42:48Z joerg $
 */
public interface LoggerFacade
{

    public LoggerFacade createLogger(String name);

    public void logInfo(String message);
    public void logFine(String message);
    public boolean isFineEnabled();
    public void logFiner(String message);
    public boolean isFinerEnabled();
    public void logFinest(String message);
    public boolean isFinestEnabled();
    public void logWarning(String message);
    public void logWarning(String message, Throwable t);
    public void logSevere(String message);
    public void logSevere(String message, Throwable t);
}
