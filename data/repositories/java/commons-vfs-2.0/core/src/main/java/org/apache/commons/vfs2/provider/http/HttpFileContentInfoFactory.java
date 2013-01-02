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
package org.apache.commons.vfs2.provider.http;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.vfs2.FileContent;
import org.apache.commons.vfs2.FileContentInfo;
import org.apache.commons.vfs2.FileContentInfoFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.impl.DefaultFileContentInfo;
import org.apache.commons.vfs2.util.FileObjectUtils;

/**
 * Creates the FileContentInfo.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
public class HttpFileContentInfoFactory implements FileContentInfoFactory
{
    public FileContentInfo create(FileContent fileContent) throws FileSystemException
    {
        HttpFileObject httpFile = (HttpFileObject) (FileObjectUtils
                                  .getAbstractFileObject(fileContent.getFile()));

        String contentType = null;
        String contentEncoding = null;

        Header header = httpFile.getHeadMethod().getResponseHeader("content-type");
        if (header != null)
        {
            HeaderElement[] element = header.getElements();
            if (element != null && element.length > 0)
            {
                contentType = element[0].getName();
            }
        }

        contentEncoding = httpFile.getHeadMethod().getResponseCharSet();

        return new DefaultFileContentInfo(contentType, contentEncoding);
    }
}
