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

import java.util.Set;
import java.util.Map;

/**
 * Interface implemented by all attribute repository classes.
 * This interface is used internally and should not be used
 * by clients. The only reason it is public is because the
 * classes implementing it may be in any package.
 *
 * @since 2.1
 */
public interface AttributeRepositoryClass
{

    /**
     * Returns a set containing all attributes (instances) associated with this class.
     * Should not return any attributes of superclasses etc.
     *
     * @since 2.1
     */
    public Set getClassAttributes ();

    /**
     * Returns a map with String keys and Set values. The keys correspond to field names,
     * and their associated Set values are the set of all attributes (instances) associated with that field.
     * Should not return any attributes of superclasses etc.
     *
     * @since 2.1
     */
    public Map getFieldAttributes ();

    /**
     * Returns a map with String keys and List values. The keys correspond to method signatures,
     * given by get Util.getSignature method, and the lists are as follows:<p>
     *
     * list.get(0) = A Set with the attributes associated with the method.<p>
     * list.get(1) = A Set with the attributes associated with the method's return value.<p>
     * list.get(2) = A Set with the attributes associated with the method's first parameter.<p>
     * list.get(n) = A Set with the attributes associated with the method's (n - 1) th parameter.<p>
     *
     * All slots in the list must be filled, not just those where there are attributes.
     *
     * <p>Should not return any attributes of superclasses etc.
     *
     * @since 2.1
     */
    public Map getMethodAttributes ();

    /**
     * Returns a map with String keys and List values. The keys correspond to constructor signatures,
     * given by get Util.getSignature method, and the lists are as follows:<p>
     *
     * list.get(0) = A Set with the attributes associated with the constructor.<p>
     * list.get(1) = A Set with the attributes associated with the constructor's first parameter.<p>
     * list.get(n) = A Set with the attributes associated with the constructor's (n - 1) th parameter.<p>
     *
     * All slots in the list must be filled, not just those where there are attributes.
     *
     * <p>Should not return any attributes of superclasses etc.
     *
     * @since 2.1
     */
    public Map getConstructorAttributes ();
}
