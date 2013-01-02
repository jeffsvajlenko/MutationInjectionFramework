package org.apache.commons.digester3.plugins;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

/**
 * Thrown when an error occurs due to bad data in the file being parsed.
 *
 * @since 1.6
 */
public class PluginInvalidInputException
    extends PluginException
{

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new exception with the specified cause.
     *
     * @param cause underlying exception that caused this to be thrown
     */
    public PluginInvalidInputException( Throwable cause )
    {
        super( cause );
    }

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param msg describes the reason this exception is being thrown.
     */
    public PluginInvalidInputException( String msg )
    {
        super( msg );
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param msg describes the reason this exception is being thrown.
     * @param cause underlying exception that caused this to be thrown
     */
    public PluginInvalidInputException( String msg, Throwable cause )
    {
        super( msg, cause );
    }

}
