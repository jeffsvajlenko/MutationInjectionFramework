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

/**
 * This attribute is used to mark attributes as being indexed.
 * Elements with indexed attributes can be found via an {@link AttributeIndex},
 * but incur a slight processing and memory penalty. You must also
 * run the attribute-indexer tool on the Jar-file containing the classes
 * you wish to find via the index.
 *
 * <p><b>Note regarding {@link Inheritable} attributes:</b> Indexed attributes
 * that are inherited will not be found via an {@link AttributeIndex}. You will
 * only find the class/method/field where the attribute is actually declared via the index.
 *
 * @since 2.1
 */
public class Indexed
{
}
