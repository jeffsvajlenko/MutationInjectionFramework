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
import java.util.Collection;

/**
 * An attribute repository cache. Used internally to speed up operation.
 * All collections returned should be unmodifiable.
 *
 * @since 2.1
 */
interface CachedRepository
{

    public static final CachedRepository EMPTY = new EmptyCachedRepository ();

    public Collection getAttributes ();
    public Collection getAttributes (Field f);
    public Collection getAttributes (Method m);
    public Collection getParameterAttributes (Method m, int parameter);
    public Collection getParameterAttributes (Constructor c, int parameter);
    public Collection getReturnAttributes (Method m);
    public Collection getAttributes (Constructor c);
}
