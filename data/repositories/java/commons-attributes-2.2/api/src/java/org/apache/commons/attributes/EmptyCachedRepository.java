/*
 * Copyright 2003-2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.attributes;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Null implementation of a cached repository.
 *
 * @since 2.1
 */
class EmptyCachedRepository implements CachedRepository
{

    private final static Collection EMPTY_COLLECTION = new ArrayList (0);

    public Collection getAttributes ()
    {
        return EMPTY_COLLECTION;
    }

    public Collection getAttributes (Field f)
    {
        return EMPTY_COLLECTION;
    }

    public Collection getAttributes (Method m)
    {
        return EMPTY_COLLECTION;
    }

    public Collection getParameterAttributes (Constructor c, int parameter)
    {
        return EMPTY_COLLECTION;
    }

    public Collection getParameterAttributes (Method m, int parameter)
    {
        return EMPTY_COLLECTION;
    }

    public Collection getReturnAttributes (Method m)
    {
        return EMPTY_COLLECTION;
    }

    public Collection getAttributes (Constructor c)
    {
        return EMPTY_COLLECTION;
    }
}
