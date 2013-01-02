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
package org.apache.commons.vfs2.provider;

import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;

/**
 * A {@link FileProvider} that is layered on top of another, such as the
 * contents of a zip or tar file.
 *
 * @author <a href="http://commons.apache.org/vfs/team-list.html">Commons VFS team</a>
 */
public abstract class AbstractLayeredFileProvider
    extends AbstractFileProvider
    implements FileProvider
{
    public AbstractLayeredFileProvider()
    {
        super();
        setFileNameParser(LayeredFileNameParser.getInstance());
    }

    /**
     * Locates a file object, by absolute URI.
     * @param baseFile The base FileObject.
     * @param uri The name of the file to locate.
     * @param properties The FileSystemOptions.
     * @return The FileObject if it is located, null otherwise.
     * @throws FileSystemException if an error occurs.
     */
    public FileObject findFile(final FileObject baseFile,
                               final String uri,
                               final FileSystemOptions properties) throws FileSystemException
    {
        // Split the URI up into its parts
        final LayeredFileName name = (LayeredFileName) parseUri(baseFile != null ? baseFile.getName() : null, uri);

        // Make the URI canonical

        // Resolve the outer file name
        final FileName fileName = name.getOuterName();
        final FileObject file = getContext().resolveFile(baseFile, fileName.getURI(), properties);

        // Create the file system
        final FileObject rootFile = createFileSystem(name.getScheme(), file, properties);

        // Resolve the file
        return rootFile.resolveFile(name.getPath());
    }

    /**
     * Creates a layered file system.
     * @param scheme The protocol to use.
     * @param file a FileObject.
     * @param fileSystemOptions Options to access the FileSystem.
     * @return A FileObject associated with the new FileSystem.
     * @throws FileSystemException if an error occurs.
     */
    @Override
    public synchronized FileObject createFileSystem(final String scheme,
            final FileObject file,
            final FileSystemOptions fileSystemOptions)
    throws FileSystemException
    {
        // Check if cached
        final FileName rootName = file.getName();
        FileSystem fs = findFileSystem(rootName, null);
        if (fs == null)
        {
            // Create the file system
            fs = doCreateFileSystem(scheme, file, fileSystemOptions);
            addFileSystem(rootName, fs);
        }
        return fs.getRoot();
    }

    /**
     * Creates a layered file system.  This method is called if the file system
     * is not cached.  The file system may implement {@link VfsComponent}.
     *
     * @param scheme The URI scheme.
     * @param file   The file to create the file system on top of.
     * @return The file system.
     */
    protected abstract FileSystem doCreateFileSystem(final String scheme,
            final FileObject file,
            final FileSystemOptions fileSystemOptions)
    throws FileSystemException;

}
