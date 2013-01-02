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

import org.apache.commons.betwixt.expression.Updater;
import org.xml.sax.Attributes;

/**
  * Action binds a simple type.
  * @author <a href="mailto:rdonkin@apache.org">Robert Burrell Donkin</a>
  * @version $Revision: 438373 $
  */
public class SimpleTypeBindAction extends MappingAction.Base
{

    public static final SimpleTypeBindAction INSTANCE = new SimpleTypeBindAction();



    public void body(String text, ReadContext context) throws Exception
    {
        // add dyna-bean support!
        // probably refactoring needed
        Updater updater = context.getCurrentUpdater();
        if (updater != null)
        {
            updater.update(context, text);
        }
        else
        {
            if (context.getLog().isDebugEnabled())
            {
                context.getLog().debug("No updater for simple type '" + context.getCurrentElement() + "'");
            }
        }
    }

    public MappingAction next(
        String namespace,
        String name,
        Attributes attributes,
        ReadContext context)
    throws Exception
    {
        return MappingAction.IGNORE;
    }

}
