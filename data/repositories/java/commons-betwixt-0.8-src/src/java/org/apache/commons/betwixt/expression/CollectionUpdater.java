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
package org.apache.commons.betwixt.expression;

import java.util.Collection;

/**
 * Updates a Collection by adding the new value to it.
 * @since 0.8
 */
public class CollectionUpdater implements Updater
{

    private static CollectionUpdater INSTANCE;

    /**
     * Gets singleton instance.
     * @return <code>CollectionUpdater</code>, not null
     */
    public static synchronized CollectionUpdater getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CollectionUpdater();
        }
        return INSTANCE;
    }

    /**
     * Updates collection contained by the context by adding the new value.
     * @param context <code>Context</code>, not null
     * @param newValue value to be added, possibly null
     */
    public void update(Context context, Object newValue)
    {
        if (newValue != null)
        {
            Object subject = context.getBean();
            if (subject != null && subject instanceof Collection)
            {
                Collection collection = (Collection) subject;
                collection.add(newValue);
            }
        }
    }
}
