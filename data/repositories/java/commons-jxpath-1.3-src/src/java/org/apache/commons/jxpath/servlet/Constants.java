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
package org.apache.commons.jxpath.servlet;

/**
 * String constants for this package.
 *
 * @author Dmitri Plotnikov
 * @version $Revision: 652845 $ $Date: 2008-05-02 12:46:46 -0500 (Fri, 02 May 2008) $
 */
public final class Constants
{

    /**
     * Variable name for {@link javax.servlet.ServletContext}.
     */
    public static final String APPLICATION_SCOPE = "application";

    /**
     * Variable name for {@link javax.servlet.http.HttpSession}.
     */
    public static final String SESSION_SCOPE = "session";

    /**
     * Variable name for {@link javax.servlet.ServletRequest}.
     */
    public static final String REQUEST_SCOPE = "request";

    /**
     * Variable name for {@link javax.servlet.jsp.PageContext}.
     */
    public static final String PAGE_SCOPE = "page";

    /**
     * Attribute  name used in page context, requst, session, and servlet
     * context to store the corresponding {@link org.apache.commons.jxpath.JXPathContext}.
     */
    public static final String JXPATH_CONTEXT =
        "org.apache.commons.jxpath.JXPATH_CONTEXT";

}
