/* $Id: ThrowExceptionCreationFactory.java 1127511 2011-05-25 13:08:25Z simonetripodi $
 *
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

package org.apache.commons.digester3.xmlrules;

import org.apache.commons.digester3.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;

/**
 * Object creation factory used for testing exception propagation.
 *
 * @author Robert Burrell Donkin
 */

public class ThrowExceptionCreationFactory
    extends AbstractObjectCreationFactory<Object>
{

    @Override
    public Object createObject( Attributes attributes )
    throws Exception
    {
        throw new RuntimeException();
    }
}
