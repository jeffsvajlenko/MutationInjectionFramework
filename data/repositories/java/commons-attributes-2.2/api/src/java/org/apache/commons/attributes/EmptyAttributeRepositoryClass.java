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

import java.util.Collections;
import java.util.Set;
import java.util.Map;

/**
 * Empty implementation of AttributeRepositoryClass.
 *
 * @since 2.1
 */
class EmptyAttributeRepositoryClass implements AttributeRepositoryClass
{

    public static final AttributeRepositoryClass INSTANCE = new EmptyAttributeRepositoryClass();

    public Set getClassAttributes ()
    {
        return Collections.EMPTY_SET;
    }

    public Map getFieldAttributes ()
    {
        return Collections.EMPTY_MAP;
    }

    public Map getMethodAttributes ()
    {
        return Collections.EMPTY_MAP;
    }

    public Map getConstructorAttributes ()
    {
        return Collections.EMPTY_MAP;
    }
}
